package com.google.foobar.levelthree.parttwo;

import java.util.ArrayList;
import java.util.List;

/**
 * Doomsday Fuel =============
 *
 * <p>Making fuel for the LAMBCHOP's reactor core is a tricky process because of the exotic matter
 * involved. It starts as raw ore, then during processing, begins randomly changing between forms,
 * eventually reaching a stable form. There may be multiple stable forms that a sample could
 * ultimately reach, not all of which are useful as fuel.
 *
 * <p>Commander Lambda has tasked you to help the scientists increase fuel creation efficiency by
 * predicting the end state of a given ore sample. You have carefully studied the different
 * structures that the ore can take and which transitions it undergoes. It appears that, while
 * random, the probability of each structure transforming is fixed. That is, each time the ore is in
 * 1 state, it has the same probabilities of entering the next state (which might be the same
 * state). You have recorded the observed transitions in a matrix. The others in the lab have
 * hypothesized more exotic forms that the ore can become, but you haven't seen all of them.
 *
 * <p>Write a function solution(m) that takes an array of array of nonnegative ints representing how
 * many times that state has gone to the next state and return an array of ints for each terminal
 * state giving the exact probabilities of each terminal state, represented as the numerator for
 * each state, then the denominator for all of them at the end and in simplest form. The matrix is
 * at most 10 by 10. It is guaranteed that no matter which state the ore is in, there is a path from
 * that state to a terminal state. That is, the processing will always eventually end in a stable
 * state. The ore starts in state 0. The denominator will fit within a signed 32-bit integer during
 * the calculation, as long as the fraction is simplified regularly.
 *
 * <p>For example, consider the matrix m: [ [0,1,0,0,0,1], # s0, the initial state, goes to s1 and
 * s5 with equal probability [4,0,0,3,2,0], # s1 can become s0, s3, or s4, but with different
 * probabilities [0,0,0,0,0,0], # s2 is terminal, and unreachable (never observed in practice)
 * [0,0,0,0,0,0], # s3 is terminal [0,0,0,0,0,0], # s4 is terminal [0,0,0,0,0,0], # s5 is terminal ]
 * So, we can consider different paths to terminal states, such as: s0 -> s1 -> s3 s0 -> s1 -> s0 ->
 * s1 -> s0 -> s1 -> s4 s0 -> s1 -> s0 -> s5 Tracing the probabilities of each, we find that s2 has
 * probability 0 s3 has probability 3/14 s4 has probability 1/7 s5 has probability 9/14 So, putting
 * that together, and making a common denominator, gives an answer in the form of [s2.numerator,
 * s3.numerator, s4.numerator, s5.numerator, denominator] which is [0, 3, 2, 9, 14].
 *
 * <p>Languages =========
 *
 * <p>To provide a Java solution, edit Solution.java To provide a Python solution, edit solution.py
 *
 * <p>Test cases ========== Your code should pass the following test cases. Note that it may also be
 * run against hidden test cases not shown here.
 *
 * <p>-- Java cases -- Input: Solution.solution({{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0},
 * {0, 0, 0, 0,0}, {0, 0, 0, 0, 0}}) Output: [7, 6, 8, 21]
 *
 * <p>Input: Solution.solution({{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0,
 * 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}}) Output: [0, 3, 2, 9, 14]
 */
public class Solution {

  /**
   * @param m the input matrix of states
   * @return an array of ints for each terminal state giving the exact probabilities of each
   *     terminal state, represented as the numerator for each state, then the denominator for all
   *     of them at the end and in simplest form
   */
  public static int[] solution(final int[][] m) {

    final List<Integer> terminalStateList = new ArrayList<>();
    final List<Integer> nonTerminalStateList = new ArrayList<>();
    final List<Integer> stateDenominatorList = new ArrayList<>();
    final int[] result;

    // Check if state 0 is a terminating state.
    if (isInitialStateTerminating(m)) {
      result = new int[m[0].length + 1];
      result[0] = 1;
      for (int i = 1; i < m[0].length + 1; i++) {
        result[i] = 0;
      }
      result[m[0].length] = 1;
    } else {

      determineStates(m, terminalStateList, nonTerminalStateList, stateDenominatorList);
      final Matrix iMatrix = createIMatrix(nonTerminalStateList);
      final Matrix qMatrix = createQMatrix(nonTerminalStateList, stateDenominatorList, m);
      final Matrix rMatrix =
          createRMatrix(nonTerminalStateList, stateDenominatorList, terminalStateList, m);

      // Find I - Q
      final Matrix iMinusQMatrix = iMatrix.minus(qMatrix);
      // Find F = (I - Q)^-1
      final Matrix fMatrix = iMinusQMatrix.getInverseMatrix();
      // Find FR
      final Matrix frMatrix = fMatrix.multiply(rMatrix);
      // Take the first row of FR
      final List<Fraction> frRow = frMatrix.getRow(0);
      final List<Fraction> numeratorList = new ArrayList<>();
      final int[] denominatorList = new int[frRow.size()];

      // Find the numerators and the common denominator and convert them to the result array
      for (int i = 0; i < frRow.size(); i++) {
        denominatorList[i] = frRow.get(i).getDenominator();
        numeratorList.add(frRow.get(i));
      }
      final int lcm = getLcm(denominatorList);
      result = new int[frRow.size() + 1];
      for (int j = 0; j < result.length - 1; j++) {
        numeratorList.set(j, numeratorList.get(j).multiply(new Fraction(lcm)));
        result[j] = numeratorList.get(j).getNumerator();
      }
      result[frRow.size()] = lcm;
    }
    return result;
  }

  private static void determineStates(
      final int[][] m,
      final List<Integer> terminalStateList,
      final List<Integer> nonTerminalStateList,
      final List<Integer> stateDenominatorList) {
    for (int i = 0; i < m.length; i++) {
      boolean allZeroInState = true;
      int stateDenominatorTemp = 0;
      // loop through probability of all states for a particular state
      for (int j = 0; j < m[0].length; j++) {
        if (m[i][j] != 0) {
          allZeroInState = false;
          stateDenominatorTemp += m[i][j];
        }
      }
      if (allZeroInState) {
        terminalStateList.add(i);
      } else {
        nonTerminalStateList.add(i);
        stateDenominatorList.add(stateDenominatorTemp);
      }
    }
  }

  private static Matrix createIMatrix(final List<Integer> nonTerminalStateList) {
    final Fraction one = new Fraction(1);
    final Fraction zero = new Fraction(0);

    // Create I matrix
    final List<List<Fraction>> iList = new ArrayList<>();
    for (int i = 0; i < nonTerminalStateList.size(); i++) {
      final List<Fraction> iRow = new ArrayList<>();
      for (int j = 0; j < nonTerminalStateList.size(); j++) {
        if (i == j) {
          iRow.add(one);
        } else {
          iRow.add(zero);
        }
      }
      iList.add(iRow);
    }
    return new Matrix(iList, nonTerminalStateList.size(), nonTerminalStateList.size());
  }

  private static Matrix createQMatrix(
      final List<Integer> nonTerminalStateList,
      final List<Integer> stateDenominatorList,
      final int[][] m) {
    // Create Q
    final List<List<Fraction>> qList = new ArrayList<>();
    for (int i = 0; i < nonTerminalStateList.size(); i++) {
      final List<Fraction> qRow = new ArrayList<>();
      for (final Integer nonTerminalState : nonTerminalStateList) {
        qRow.add(
            new Fraction(
                m[nonTerminalStateList.get(i)][nonTerminalState], stateDenominatorList.get(i)));
      }
      qList.add(qRow);
    }

    return new Matrix(qList, nonTerminalStateList.size(), nonTerminalStateList.size());
  }

  private static Matrix createRMatrix(
      final List<Integer> nonTerminalStateList,
      final List<Integer> stateDenominatorList,
      final List<Integer> terminalStateList,
      final int[][] m) {
    // Create R
    final List<List<Fraction>> rList = new ArrayList<>();
    for (int i = 0; i < nonTerminalStateList.size(); i++) {
      final ArrayList<Fraction> rRow = new ArrayList<>();
      for (final Integer terminalState : terminalStateList) {
        rRow.add(
            new Fraction(
                m[nonTerminalStateList.get(i)][terminalState], stateDenominatorList.get(i)));
      }
      rList.add(rRow);
    }

    return new Matrix(rList, nonTerminalStateList.size(), terminalStateList.size());
  }

  /**
   * @param arr input array
   * @return the least common multiple of the values in the array
   */
  private static int getLcm(final int[] arr) {
    int max = 0;
    final int n = arr.length;
    for (final int k : arr) {
      if (max < k) {
        max = k;
      }
    }
    int res = 1;
    int factor = 2;
    while (factor <= max) {
      final List<Integer> arrIndex = new ArrayList<>();
      for (int j = 0; j < n; j++) {
        if (arr[j] % factor == 0) {
          arrIndex.add(arrIndex.size(), j);
        }
      }
      if (arrIndex.size() >= 2) {
        // Reduce all array elements divisible
        // by factor.
        for (final Integer index : arrIndex) {
          arr[index] /= factor;
        }

        res *= factor;
      } else {
        factor++;
      }
    }

    // Then multiply all reduced array elements
    for (final int j : arr) {
      res *= j;
    }

    return res;
  }

  private static boolean isInitialStateTerminating(final int[][] m) {
    int sum = 0;
    for (int i = 0; i < m[0].length; i++) {
      sum += m[0][i];
    }
    return sum == 0;
  }

  /** Class to represent a matrix */
  private static class Matrix {

    private final int rows;
    private final int cols;
    private final Fraction det;
    private final List<List<Fraction>> fractionMatrix;
    private final List<List<Fraction>> inverseMatrix;

    /**
     * Construct a Matrix object
     *
     * @param fractionMatrix a matrix of fractions
     * @param rows the number of rows
     * @param cols the number of columns
     */
    public Matrix(final List<List<Fraction>> fractionMatrix, final int rows, final int cols) {
      this.fractionMatrix = fractionMatrix;
      this.rows = rows;
      this.cols = cols;
      this.det = this.determinant(fractionMatrix, cols);
      this.inverseMatrix = this.inverse();
    }

    private void getCofactor(
        final List<List<Fraction>> mat,
        final List<List<Fraction>> tempMat,
        final int p,
        final int q,
        final int n) {
      int i = 0;
      int j = 0;
      for (int row = 0; row < n; row++) {
        for (int col = 0; col < n; col++) {
          if (row != p && col != q) {
            tempMat.get(i).set(j++, mat.get(row).get(col));
            if (j == n - 1) {
              j = 0;
              i++;
            }
          }
        }
      }
    }

    private Fraction determinant(final List<List<Fraction>> mat, final int n) {
      Fraction ans = new Fraction(0, 1);
      if (this.rows != this.cols) {
        return ans;
      }
      if (n == 1) {
        return mat.get(0).get(0);
      }
      final List<List<Fraction>> tempMat = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.rows; i++) {
        final List<Fraction> tempMatRow = new ArrayList<>();
        for (int j = 0; j < this.cols; j++) {
          tempMatRow.add(new Fraction(0, 1));
        }
        tempMat.add(tempMatRow);
      }

      int sign = 1;
      Fraction signFraction = new Fraction(sign, 1);
      for (int k = 0; k < n; k++) {
        this.getCofactor(mat, tempMat, 0, k, n);
        ans =
            ans.plus(
                signFraction.multiply(mat.get(0).get(k).multiply(determinant(tempMat, n - 1))));
        sign = -sign;
        signFraction = new Fraction(sign, 1);
      }
      return ans;
    }

    private void adjoint(final List<List<Fraction>> mat, final List<List<Fraction>> adj) {
      if (this.cols == 1) {
        adj.get(0).set(0, new Fraction(1, 1));
        return;
      }

      int sign;
      final List<List<Fraction>> tempMat = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.cols; i++) {
        final List<Fraction> tempMatRow = new ArrayList<>();
        for (int j = 0; j < this.cols; j++) {
          tempMatRow.add(new Fraction(0, 1));
        }
        tempMat.add(tempMatRow);
      }

      for (int p = 0; p < this.cols; p++) {
        for (int q = 0; q < this.cols; q++) {
          this.getCofactor(mat, tempMat, p, q, this.cols);
          sign = ((p + q) % 2 == 0) ? 1 : -1;
          final Fraction signFraction = new Fraction(sign, 1);
          adj.get(q).set(p, signFraction.multiply((this.determinant(tempMat, this.cols - 1))));
        }
      }
    }

    /** @return the inverse of the current matrix */
    private List<List<Fraction>> inverse() {
      final List<List<Fraction>> inv = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.rows; i++) {
        final ArrayList<Fraction> invRow = new ArrayList<>();
        for (int j = 0; j < this.cols; j++) {
          invRow.add(new Fraction(0, 1));
        }
        inv.add(invRow);
      }

      if (this.det.isFractionEqualTo(new Fraction(0))) {
        return inv;
      }

      final List<List<Fraction>> adj = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.rows; i++) {
        final ArrayList<Fraction> adjRow = new ArrayList<>();
        for (int j = 0; j < this.cols; j++) {
          adjRow.add(new Fraction(0, 1));
        }
        adj.add(adjRow);
      }

      adjoint(this.fractionMatrix, adj);
      for (int p = 0; p < this.cols; p++) {
        for (int q = 0; q < this.cols; q++) {
          final Fraction temp = adj.get(p).get(q).dividedBy(this.det);
          inv.get(p).set(q, temp);
        }
      }
      return inv;
    }

    private Matrix getInverseMatrix() {
      return new Matrix(this.inverseMatrix, this.rows, this.cols);
    }

    private Fraction getElement(final int m, final int n) {
      return this.fractionMatrix.get(m).get(n);
    }

    private List<Fraction> getRow(final int m) {
      if (m <= this.rows) {
        return this.fractionMatrix.get(m);
      }
      return new ArrayList<>();
    }

    /**
     * Adds two matrices together
     *
     * @param mat the matrix to add to the current
     * @return the result of the multiplication as a new Matrix
     */
    private Matrix plus(final Matrix mat) {
      final int matRows = mat.getDimension()[0];
      final int matCols = mat.getDimension()[1];
      // If the dimensions of the two matrices are not equal
      if (this.rows != matRows || this.cols != matCols) {
        return mat;
      } else {
        final List<List<Fraction>> sum = new ArrayList<>();
        // Init 2d fraction arraylist
        for (int i = 0; i < this.rows; i++) {
          final ArrayList<Fraction> sumRow = new ArrayList<>();
          for (int j = 0; j < this.cols; j++) {
            sumRow.add(new Fraction(0, 1));
          }
          sum.add(sumRow);
        }
        for (int i = 0; i < this.rows; i++) {
          for (int j = 0; j < this.cols; j++) {
            sum.get(i).set(j, this.fractionMatrix.get(i).get(j).plus(mat.getElement(i, j)));
          }
        }
        return new Matrix(sum, this.rows, this.cols);
      }
    }

    /**
     * Subtracts two matrices together
     *
     * @param mat the matrix to subtracted to the current
     * @return the result of the subtraction as a new Matrix
     */
    private Matrix minus(final Matrix mat) {
      final int matRows = mat.getDimension()[0];
      final int matCols = mat.getDimension()[1];

      // If the dimensions of the two matrices are not equal
      if (this.rows != matRows || this.cols != matCols) {
        return mat;
      } else {
        final List<List<Fraction>> difference = new ArrayList<>();
        // Init 2d fraction arraylist
        for (int i = 0; i < this.rows; i++) {
          final ArrayList<Fraction> differenceRow = new ArrayList<>();
          for (int j = 0; j < this.cols; j++) {
            differenceRow.add(new Fraction(0, 1));
          }
          difference.add(differenceRow);
        }
        for (int i = 0; i < this.rows; i++) {
          for (int j = 0; j < this.cols; j++) {
            difference.get(i).set(j, this.fractionMatrix.get(i).get(j).minus(mat.getElement(i, j)));
          }
        }
        return new Matrix(difference, this.rows, this.cols);
      }
    }

    /**
     * Multiply two matrices together
     *
     * @param mat the matrix to multiplied to the current
     * @return the result of the multiplication as a new Matrix
     */
    private Matrix multiply(final Matrix mat) {
      // M N M N
      // X(m, n) x Y(n, p) = Z(m, p)
      final int matRows = mat.getDimension()[0];
      final int matCols = mat.getDimension()[1];
      // if the dimensions of two matrices are not valid for cross multiplication
      if (this.cols != matRows) {
        return mat;
      } else {
        final List<List<Fraction>> product = new ArrayList<>();
        // Init 2d fraction arraylist
        for (int i = 0; i < this.rows; i++) {
          final List<Fraction> productRow = new ArrayList<>();
          for (int j = 0; j < matCols; j++) {
            productRow.add(new Fraction(0, 1));
          }
          product.add(productRow);
        }
        for (int i = 0; i < this.rows; i++) {
          for (int j = 0; j < matCols; j++) {
            for (int k = 0; k < this.cols; k++) {
              final Fraction temp = product.get(i).get(j);
              product
                  .get(i)
                  .set(
                      j,
                      temp.plus(this.fractionMatrix.get(i).get(k).multiply(mat.getElement(k, j))));
            }
          }
        }
        return new Matrix(product, this.rows, matCols);
      }
    }

    private int[] getDimension() {
      return new int[] {this.rows, this.cols};
    }
  }

  /** Class to represent a fraction */
  private static class Fraction {

    private int numerator;
    private int denominator = 1;
    // true = negative, false = positive
    private boolean sign = false;

    /**
     * Constructs a Fraction object
     *
     * @param numerator the numerator
     * @param denominator the denominator
     */
    public Fraction(final int numerator, final int denominator) {
      this.numerator = numerator;
      if (denominator != 0) {
        this.denominator = denominator;
      }
      this.simplify();
    }

    /**
     * Constructs a Fraction object
     *
     * @param num the numberator
     */
    public Fraction(final int num) {
      this.numerator = num;
      this.simplify();
    }

    private int getGcm(final int num1, final int num2) {
      return num2 == 0 ? num1 : this.getGcm(num2, num1 % num2);
    }

    /** Simplify fraction to simplest form */
    private void simplify() {
      this.sign =
          !(this.numerator <= 0 && this.denominator <= 0)
              && !(this.numerator >= 0 && this.denominator >= 0);

      this.numerator = Math.abs(this.numerator);
      this.denominator = Math.abs(this.denominator);

      final int gcm = this.getGcm(this.numerator, this.denominator);
      this.numerator = this.numerator / gcm;
      this.denominator = this.denominator / gcm;
      // When fraction is zero, make sure denominator is one and no negative sign
      if (this.numerator == 0 && this.denominator != 0) {
        this.denominator = 1;
        this.sign = false;
      }
    }

    /**
     * Adds a fraction to the current fraction
     *
     * @param fractionToAdd the fraction to add
     * @return the sum of the two fractions as a new Fraction object
     */
    private Fraction plus(final Fraction fractionToAdd) {
      int num = 0;
      if (this.sign) { // this fraction is negative
        if (fractionToAdd.getSign()) { // f1 is negative
          num =
              (-1) * this.numerator * fractionToAdd.denominator
                  + this.denominator * (-1) * fractionToAdd.numerator;
        } else { // f1 is positive
          num =
              (-1) * this.numerator * fractionToAdd.denominator
                  + this.denominator * fractionToAdd.numerator;
        }
      } else { // this fraction is positive
        if (fractionToAdd.getSign()) { // f1 is negative
          num =
              this.numerator * fractionToAdd.denominator
                  + this.denominator * (-1) * fractionToAdd.numerator;
        } else { // f1 is positive
          num =
              this.numerator * fractionToAdd.denominator
                  + this.denominator * fractionToAdd.numerator;
        }
      }
      final int denom = this.denominator * fractionToAdd.getDenominator();
      return new Fraction(num, denom);
    }

    /**
     * Subtracts a fraction from the current fraction
     *
     * @param fractionToSubtract the fraction to subtract
     * @return the result of the subtraction as a new Fraction object
     */
    private Fraction minus(final Fraction fractionToSubtract) {
      final int num;
      if (this.sign) { // this fraction is negative
        if (fractionToSubtract.getSign()) { // f1 is negative
          num =
              (-1) * this.numerator * fractionToSubtract.denominator
                  + this.denominator * fractionToSubtract.numerator;
        } else { // f1 is positive
          num =
              (-1) * this.numerator * fractionToSubtract.denominator
                  - this.denominator * fractionToSubtract.numerator;
        }
      } else { // this fraction is positive
        if (fractionToSubtract.getSign()) { // f1 is negative
          num =
              this.numerator * fractionToSubtract.denominator
                  + this.denominator * fractionToSubtract.numerator;
        } else { // f1 is positive
          num =
              this.numerator * fractionToSubtract.denominator
                  - this.denominator * fractionToSubtract.numerator;
        }
      }
      final int denom = this.denominator * fractionToSubtract.getDenominator();
      return new Fraction(num, denom);
    }

    /**
     * Multiplies a fraction from the current fraction
     *
     * @param fractionToMultiply the fraction to multiply
     * @return the result of the multiplication as a new Fraction object
     */
    private Fraction multiply(final Fraction fractionToMultiply) {
      int signInt = 1;
      // Either one fraction is negative will make the product fraction negative, but not for both
      // fractions are negative.
      if (this.sign && !fractionToMultiply.getSign()
          || !this.sign && fractionToMultiply.getSign()) {
        signInt = -1;
      }
      return new Fraction(
          signInt * this.numerator * fractionToMultiply.getNumerator(),
          this.denominator * fractionToMultiply.getDenominator());
    }

    /**
     * Divides the current fraction by another fraction
     *
     * @param fractionToDivideBy the fraction to divide by
     * @return the result of the division as a new Fraction object
     */
    private Fraction dividedBy(final Fraction fractionToDivideBy) {
      int signInt = 1;
      // Either one fraction is negative will make the product fraction negative, but not for both
      // fractions are negative.
      if (this.sign && !fractionToDivideBy.getSign()
          || !this.sign && fractionToDivideBy.getSign()) {
        signInt = -1;
      }
      return new Fraction(
          signInt * this.numerator * fractionToDivideBy.getDenominator(),
          this.denominator * fractionToDivideBy.getNumerator());
    }

    private boolean isFractionEqualTo(final Fraction f1) {
      return this.numerator == f1.getNumerator()
          && this.denominator == f1.getDenominator()
          && this.sign == f1.getSign();
    }

    int getNumerator() {
      return this.numerator;
    }

    public int getDenominator() {
      return this.denominator;
    }

    public boolean getSign() {
      return this.sign;
    }
  }
}

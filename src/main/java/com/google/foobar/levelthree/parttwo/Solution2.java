package com.google.foobar.levelthree.parttwo;

import java.util.ArrayList;
import java.util.List;

/** Same as solution one but code is less tidy with all print statements etc. */
public class Solution2 {
  public static int[] solution(final int[][] m) {

    int sum = 0;
    for (int i = 0; i < m[0].length; i++) {
      sum += m[0][i];
    }

    if (sum == 0) {
      final int[] ans = new int[m[0].length + 1];
      ans[0] = 1;
      for (int i = 1; i < m[0].length + 1; i++) {
        ans[i] = 0;
      }
      ans[m[0].length] = 1;
      return ans;
    }

    final List<Integer> termStateList = new ArrayList<>();
    final List<Integer> nonTermStateList = new ArrayList<>();
    final List<Integer> stateDenominatorList = new ArrayList<>();
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
        termStateList.add(i);
      } else {
        nonTermStateList.add(i);
        stateDenominatorList.add(stateDenominatorTemp);
      }
    }

    // Create I 0 R Q matrix
    final Fraction one = new Fraction(1);
    final Fraction zero = new Fraction(0);

    // Create I matrix
    final List<List<Fraction>> iList = new ArrayList<>();
    for (int i = 0; i < nonTermStateList.size(); i++) {
      final List<Fraction> iRow = new ArrayList<>();
      for (int j = 0; j < nonTermStateList.size(); j++) {
        if (i == j) {
          iRow.add(one);
        } else {
          iRow.add(zero);
        }
      }
      iList.add(iRow);
    }
    final Matrix iMatrix = new Matrix(iList, nonTermStateList.size(), nonTermStateList.size());
    iMatrix.print();

    // Create Q
    final List<List<Fraction>> qList = new ArrayList<>();
    for (int i = 0; i < nonTermStateList.size(); i++) {
      final List<Fraction> qRow = new ArrayList<>();
      for (int j = 0; j < nonTermStateList.size(); j++) {
        qRow.add(
            new Fraction(
                m[nonTermStateList.get(i)][nonTermStateList.get(j)], stateDenominatorList.get(i)));
      }
      qList.add(qRow);
    }

    final Matrix qMatrix = new Matrix(qList, nonTermStateList.size(), nonTermStateList.size());
    qMatrix.print();

    // Create R
    final List<List<Fraction>> rList = new ArrayList<>();
    for (int i = 0; i < nonTermStateList.size(); i++) {
      final ArrayList<Fraction> rRow = new ArrayList<>();
      for (int j = 0; j < termStateList.size(); j++) {
        rRow.add(
            new Fraction(
                m[nonTermStateList.get(i)][termStateList.get(j)], stateDenominatorList.get(i)));
      }
      rList.add(rRow);
    }

    final Matrix rMatrix = new Matrix(rList, nonTermStateList.size(), termStateList.size());
    rMatrix.print();

    // Find I - Q
    final Matrix iMinusQMatrix = iMatrix.minus(qMatrix);
    iMinusQMatrix.print();
    // Find F = (I - Q)^-1
    final Matrix fMatrix = iMinusQMatrix.getInverseMatrix();
    fMatrix.print();
    // Find FR
    final Matrix frMatrix = fMatrix.multiply(rMatrix);
    frMatrix.print();
    // Take the first row of FR
    final List<Fraction> frRow = frMatrix.getRow(0);
    final List<Fraction> numeratorList = new ArrayList<>(); // numeratorList
    final int[] denomList = new int[frRow.size()]; // denomList
    // Find the numerators and the common denominator, make it an array
    for (int i = 0; i < frRow.size(); i++) {
      denomList[i] = frRow.get(i).getDenominator();
      numeratorList.add(frRow.get(i));
    }
    final int lcm = getLcm(denomList);
    final int[] result = new int[frRow.size() + 1];
    for (int j = 0; j < result.length - 1; j++) {
      numeratorList.set(j, numeratorList.get(j).multiply(new Fraction(lcm)));
      result[j] = numeratorList.get(j).getNumerator();
    }
    result[frRow.size()] = lcm;

    return result;
  }

  public static int getLcm(final int[] arr) {
    int max = 0;
    final int n = arr.length;
    for (int i = 0; i < n; i++) {
      if (max < arr[i]) {
        max = arr[i];
      }
    }
    int res = 1;
    int factor = 2;
    while (factor <= max) {
      final ArrayList<Integer> arrIndex = new ArrayList<>();
      for (int j = 0; j < n; j++) {
        if (arr[j] % factor == 0) {
          arrIndex.add(arrIndex.size(), j);
        }
      }
      if (arrIndex.size() >= 2) {
        // Reduce all array elements divisible
        // by factor.
        for (int j = 0; j < arrIndex.size(); j++) {
          arr[arrIndex.get(j)] /= factor;
        }

        res *= factor;
      } else {
        factor++;
      }
    }

    // Then multiply all reduced array elements
    for (int i = 0; i < n; i++) {
      res *= arr[i];
    }

    return res;
  }

  private static class Matrix {

    private final int m;
    private final int n;
    private final Fraction det;
    private final List<List<Fraction>> matrix;
    private final List<List<Fraction>> inverseMatrix;

    public Matrix(final List<List<Fraction>> mat, final int m, final int n) {
      this.matrix = mat;
      this.m = m;
      this.n = n;
      this.det = this.determinant(mat, n);
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
      if (this.m != this.n) {
        return ans;
      }
      if (n == 1) {
        return mat.get(0).get(0);
      }
      final List<List<Fraction>> tempMat = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.m; i++) {
        final List<Fraction> tempMatRow = new ArrayList<>();
        for (int j = 0; j < this.n; j++) {
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
      if (this.n == 1) {
        adj.get(0).set(0, new Fraction(1, 1));
        return;
      }
      int sign = 1;

      final List<List<Fraction>> tempMat = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.n; i++) {
        final List<Fraction> tempMatRow = new ArrayList<>();
        for (int j = 0; j < this.n; j++) {
          tempMatRow.add(new Fraction(0, 1));
        }
        tempMat.add(tempMatRow);
      }

      for (int p = 0; p < this.n; p++) {
        for (int q = 0; q < this.n; q++) {
          this.getCofactor(mat, tempMat, p, q, this.n);
          sign = ((p + q) % 2 == 0) ? 1 : -1;
          final Fraction signFraction = new Fraction(sign, 1);
          adj.get(q).set(p, signFraction.multiply((this.determinant(tempMat, this.n - 1))));
        }
      }
    }

    private List<List<Fraction>> inverse() {
      final List<List<Fraction>> inv = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.m; i++) {
        final ArrayList<Fraction> invRow = new ArrayList<>();
        for (int j = 0; j < this.n; j++) {
          invRow.add(new Fraction(0, 1));
        }
        inv.add(invRow);
      }

      if (this.det.equals(new Fraction(0))) {
        return inv;
      }

      final List<List<Fraction>> adj = new ArrayList<>();
      // Init 2d fraction arraylist
      for (int i = 0; i < this.m; i++) {
        final ArrayList<Fraction> adjRow = new ArrayList<>();
        for (int j = 0; j < this.n; j++) {
          adjRow.add(new Fraction(0, 1));
        }
        adj.add(adjRow);
      }

      adjoint(this.matrix, adj);
      for (int p = 0; p < this.n; p++) {
        for (int q = 0; q < this.n; q++) {
          final Fraction temp = adj.get(p).get(q).dividedBy(this.det);
          inv.get(p).set(q, temp);
        }
      }
      return inv;
    }

    public Matrix getInverseMatrix() {
      if (this.m != this.n) {
        // System.out.println("No inverse matrix for non-square matrices");
      }
      return new Matrix(this.inverseMatrix, this.m, this.n);
    }

    public Fraction getElement(final int m, final int n) {
      return this.matrix.get(m).get(n);
    }

    public List<Fraction> getRow(final int m) {
      if (m <= this.m) {
        return this.matrix.get(m);
      }
      return new ArrayList<>();
    }

    public Matrix plus(final Matrix mat) {
      final int M_m = mat.getDimension()[0];
      final int N_m = mat.getDimension()[1];
      if (this.m != M_m || this.n != N_m) {
        // System.out.println("Error in plus: Dimensions of two matrices are not equal!");
        return mat;
      } else {
        final List<List<Fraction>> sum = new ArrayList<>();
        // Init 2d fraction arraylist
        for (int i = 0; i < this.m; i++) {
          final ArrayList<Fraction> sumRow = new ArrayList<>();
          for (int j = 0; j < this.n; j++) {
            sumRow.add(new Fraction(0, 1));
          }
          sum.add(sumRow);
        }
        for (int i = 0; i < this.m; i++) {
          for (int j = 0; j < this.n; j++) {
            sum.get(i).set(j, this.matrix.get(i).get(j).plus(mat.getElement(i, j)));
          }
        }
        return new Matrix(sum, this.m, this.n);
      }
    }

    public Matrix minus(final Matrix mat) {
      final int M_m = mat.getDimension()[0];
      final int N_m = mat.getDimension()[1];
      if (this.m != M_m || this.n != N_m) {
        // System.out.println("Error in minus: Dimensions of two matrices are not equal!");
        return mat;
      } else {
        final List<List<Fraction>> difference = new ArrayList<>();
        // Init 2d fraction arraylist
        for (int i = 0; i < this.m; i++) {
          final ArrayList<Fraction> differenceRow = new ArrayList<>();
          for (int j = 0; j < this.n; j++) {
            differenceRow.add(new Fraction(0, 1));
          }
          difference.add(differenceRow);
        }
        for (int i = 0; i < this.m; i++) {
          for (int j = 0; j < this.n; j++) {
            difference.get(i).set(j, this.matrix.get(i).get(j).minus(mat.getElement(i, j)));
          }
        }
        return new Matrix(difference, this.m, this.n);
      }
    }

    public Matrix multiply(final Matrix mat) {
      // M N M N
      // X(m, n) x Y(n, p) = Z(m, p)
      final int M_m = mat.getDimension()[0];
      final int p_m = mat.getDimension()[1];
      if (this.n != M_m) {
        // system.out.println("Error in multiply: Dimensions of two matrices are valid for cross
        // multiplication!"); // Debug
        return mat;
      } else {
        final List<List<Fraction>> product = new ArrayList<>();
        // Init 2d fraction arraylist
        for (int i = 0; i < this.m; i++) {
          final List<Fraction> productRow = new ArrayList<>();
          for (int j = 0; j < p_m; j++) {
            productRow.add(new Fraction(0, 1));
          }
          product.add(productRow);
        }
        for (int i = 0; i < this.m; i++) {
          for (int j = 0; j < p_m; j++) {
            for (int k = 0; k < this.n; k++) {
              final Fraction temp = product.get(i).get(j);
              product
                  .get(i)
                  .set(j, temp.plus(this.matrix.get(i).get(k).multiply(mat.getElement(k, j))));
            }
          }
        }
        return new Matrix(product, this.m, p_m);
      }
    }

    public int[] getDimension() {
      return new int[] {this.m, this.n};
    }

    public void print() {
      for (int i = 0; i < this.m; i++) {
        for (int j = 0; j < this.n; j++) {
          // System.out.print(this.matrix.get(i).get(j).toString() + "  ");
        }
        // System.out.println();
      }
    }
  }

  private static class Fraction {

    private int numerator;
    private int denominator = 1;
    private boolean sign = false; // true = negative, false = positive

    public Fraction(final int num, final int denom) {
      this.numerator = num;
      if (denom == 0) {
        // System.out.println("Denominator cannot be 0. Setting it to 1");
      } else {
        this.denominator = denom;
      }
      this.simplify();
    }

    public Fraction(final int num) {
      this.numerator = num;
      this.simplify();
    }

    private int getGcm(final int num1, final int num2) {
      return num2 == 0 ? num1 : this.getGcm(num2, num1 % num2);
    }

    // Simplify fraction to simplest form
    public void simplify() {
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

    public Fraction plus(final Fraction f1) {
      int num = 0;
      if (this.sign) { // this fraction is negative
        if (f1.getSign()) { // f1 is negative
          num = (-1) * this.numerator * f1.denominator + this.denominator * (-1) * f1.numerator;
        } else { // f1 is positive
          num = (-1) * this.numerator * f1.denominator + this.denominator * f1.numerator;
        }
      } else { // this fraction is positive
        if (f1.getSign()) { // f1 is negative
          num = this.numerator * f1.denominator + this.denominator * (-1) * f1.numerator;
        } else { // f1 is positive
          num = this.numerator * f1.denominator + this.denominator * f1.numerator;
        }
      }
      final int denom = this.denominator * f1.getDenominator();
      return new Fraction(num, denom);
    }

    public Fraction minus(final Fraction f1) {
      int num = 0;
      if (this.sign) { // this fraction is negative
        if (f1.getSign()) { // f1 is negative
          num = (-1) * this.numerator * f1.denominator + this.denominator * f1.numerator;
        } else { // f1 is positive
          num = (-1) * this.numerator * f1.denominator - this.denominator * f1.numerator;
        }
      } else { // this fraction is positive
        if (f1.getSign()) { // f1 is negative
          num = this.numerator * f1.denominator + this.denominator * f1.numerator;
        } else { // f1 is positive
          num = this.numerator * f1.denominator - this.denominator * f1.numerator;
        }
      }
      final int denom = this.denominator * f1.getDenominator();
      return new Fraction(num, denom);
    }

    public Fraction multiply(final Fraction f1) {
      int signInt = 1;
      // Either one fraction is negative will make the product fraction negative, but not for both
      // fractions are negative.
      if (this.sign && !f1.getSign() || !this.sign && f1.getSign()) {
        signInt = -1;
      }
      return new Fraction(
          signInt * this.numerator * f1.getNumerator(), this.denominator * f1.getDenominator());
    }

    public Fraction dividedBy(final Fraction f1) {
      int signInt = 1;
      // Either one fraction is negative will make the product fraction negative, but not for both
      // fractions are negative.
      if (this.sign && !f1.getSign() || !this.sign && f1.getSign()) {
        signInt = -1;
      }
      return new Fraction(
          signInt * this.numerator * f1.getDenominator(), this.denominator * f1.getNumerator());
    }

    public boolean equals(final Fraction f1) {
      return this.numerator == f1.getNumerator()
          && this.denominator == f1.getDenominator()
          && this.sign == f1.getSign();
    }

    public int getNumerator() {
      return this.numerator;
    }

    public int getDenominator() {
      return this.denominator;
    }

    public boolean getSign() {
      return this.sign;
    }

    @Override
    public String toString() {
      String signStr = "";
      String fractionStr = "";
      if (this.sign) {
        signStr = "-";
      }
      if (this.numerator == this.denominator) {
        fractionStr = "1";
      } else if (this.denominator == 1) {
        fractionStr = Integer.toString(this.numerator);
      } else {
        fractionStr = this.numerator + "/" + this.denominator;
      }
      return signStr + fractionStr;
    }
  }
}

package com.google.foobar.levelthree.partthree;

import java.math.BigInteger;

/**
 * Fuel Injection Perfection =========================
 *
 * <p>Commander Lambda has asked for your help to refine the automatic quantum antimatter fuel
 * injection system for her LAMBCHOP doomsday device. It's a great chance for you to get a closer
 * look at the LAMBCHOP - and maybe sneak in a bit of sabotage while you're at it - so you took the
 * job gladly.
 *
 * <p>Quantum antimatter fuel comes in small pellets, which is convenient since the many moving
 * parts of the LAMBCHOP each need to be fed fuel one pellet at a time. However, minions dump
 * pellets in bulk into the fuel intake. You need to figure out the most efficient way to sort and
 * shift the pellets down to a single pellet at a time.
 *
 * <p>The fuel control mechanisms have three operations:
 *
 * <p>1) Add one fuel pellet 2) Remove one fuel pellet 3) Divide the entire group of fuel pellets by
 * 2 (due to the destructive energy released when a quantum antimatter pellet is cut in half, the
 * safety controls will only allow this to happen if there is an even number of pellets)
 *
 * <p>Write a function called solution(n) which takes a positive integer as a string and returns the
 * minimum number of operations needed to transform the number of pellets to 1. The fuel intake
 * control panel can only display a number up to 309 digits long, so there won't ever be more
 * pellets than you can express in that many digits.
 *
 * <p>For example: solution(4) returns 2: 4 -> 2 -> 1 solution(15) returns 5: 15 -> 16 -> 8 -> 4 ->
 * 2 -> 1
 *
 * <p>Languages =========
 *
 * <p>To provide a Python solution, edit solution.py To provide a Java solution, edit Solution.java
 *
 * <p>Test cases ========== Your code should pass the following test cases. Note that it may also be
 * run against hidden test cases not shown here.
 *
 * <p>-- Python cases -- Input: solution.solution('15') Output: 5
 *
 * <p>Input: solution.solution('4') Output: 2
 *
 * <p>-- Java cases -- Input: Solution.solution('4') Output: 2
 *
 * <p>Input: Solution.solution('15') Output: 5
 */
public class Solution {
  /**
   * @param x a String representing the number of pellets
   * @return the minimum number of operations needed to transform the number of pellets to 1
   */
  public static int solution(final String x) {
    BigInteger input = new BigInteger(x);
    final BigInteger two = BigInteger.valueOf(2);
    final BigInteger three = BigInteger.valueOf(3);
    int minNumTransformations = 0;

    while (!input.equals(BigInteger.ONE)) {
      if (input.mod(two).equals(BigInteger.ZERO)) {
        input = input.divide(two);
      } else {
        // Check whether subtracting 1 or adding 1 results in shortest path
        final BigInteger tempSubtractionResult = input.subtract(BigInteger.ONE);
        final BigInteger tempDivisionResult = tempSubtractionResult.divide(two);
        if (tempDivisionResult.mod(two).equals(BigInteger.ZERO) || input.equals(three)) {
          input = tempSubtractionResult;
        } else {
          input = input.add(BigInteger.ONE);
        }
      }
      minNumTransformations++;
    }
    return minNumTransformations;
  }
}

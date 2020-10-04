package com.google.foobar.levelthree.partone;

import java.util.HashMap;
import java.util.Map;

/**
 * Find the Access Codes =====================
 *
 * <p>In order to destroy Commander Lambda's LAMBCHOP doomsday device, you'll need access to it. But
 * the only door leading to the LAMBCHOP chamber is secured with a unique lock system whose number
 * of passcodes changes daily. Commander Lambda gets a report every day that includes the locks'
 * access codes, but only she knows how to figure out which of several lists contains the access
 * codes. You need to find a way to determine which list contains the access codes once you're ready
 * to go in.
 *
 * <p>Fortunately, now that you're Commander Lambda's personal assistant, she's confided to you that
 * she made all the access codes "lucky triples" in order to help her better find them in the lists.
 * A "lucky triple" is a tuple (x, y, z) where x divides y and y divides z, such as (1, 2, 4). With
 * that information, you can figure out which list contains the number of access codes that matches
 * the number of locks on the door when you're ready to go in (for example, if there's 5 passcodes,
 * you'd need to find a list with 5 "lucky triple" access codes).
 *
 * <p>Write a function solution(l) that takes a list of positive integers l and counts the number of
 * "lucky triples" of (li, lj, lk) where the list indices meet the requirement i < j < k. The length
 * of l is between 2 and 2000 inclusive. The elements of l are between 1 and 999999 inclusive. The
 * answer fits within a signed 32-bit integer. Some of the lists are purposely generated without any
 * access codes to throw off spies, so if no triples are found, return 0.
 *
 * <p>For example, [1, 2, 3, 4, 5, 6] has the triples: [1, 2, 4], [1, 2, 6], [1, 3, 6], making the
 * answer 3 total.
 *
 * <p>Languages =========
 *
 * <p>To provide a Java solution, edit Solution.java To provide a Python solution, edit solution.py
 *
 * <p>Test cases ========== Your code should pass the following test cases. Note that it may also be
 * run against hidden test cases not shown here.
 *
 * <p>-- Java cases -- Input: Solution.solution([1, 1, 1]) Output: 1
 *
 * <p>Input: Solution.solution([1, 2, 3, 4, 5, 6]) Output: 3
 */
public class Solution {
  /**
   * Not currently working
   *
   * @param l
   * @return
   */
  public static int solution2(final int[] l) {

    final Map<Integer, Integer> numPreviousDivisorMap = new HashMap<>();
    int numberOfTriplets = 0;
    for (int i = 0; i < l.length; i++) {
      for (int j = 0; j < i; j++) {
        if (l[i] % l[j] == 0) {
          if (!numPreviousDivisorMap.containsKey(l[i])) {
            numPreviousDivisorMap.put(l[i], 1);
          } else {
            final int val = numPreviousDivisorMap.get(l[i]) + 1;
            numPreviousDivisorMap.put(l[i], val);
            numberOfTriplets += numPreviousDivisorMap.get(l[j]);
          }
        }
      }
    }
    return numberOfTriplets;
  }

  public static int foobar(final int[] arr) {
    int numLuckyTriplets = 0;
    final int[] noOfDoubles = new int[arr.length];

    for (int i = 1; i < arr.length - 1; ++i) {
      for (int j = 0; j < i; ++j) {
        if (arr[i] % arr[j] == 0) {
          ++noOfDoubles[i];
        }
      }
    }

    // Count lucky triples
    for (int i = 2; i < arr.length; i++) {
      for (int j = 1; j < i; ++j) {
        if (arr[i] % arr[j] == 0) {
          numLuckyTriplets += noOfDoubles[j];
        }
      }
    }

    return numLuckyTriplets;
  }

  /**
   * @param l the input array of numbers
   * @return the number of lucky triples in the input array
   */
  public static int solution(final int[] l) {
    // Array used to track the count of numbers in the original array (at a given index) that can be
    // divided by another number within the array
    final int[] tracker = new int[l.length];
    int numberOfTriples = 0;

    for (int i = 0; i < l.length; i++) {
      for (int j = 0; j < i; j++) {
        if (l[i] % l[j] == 0) {
          // Increment the count for this index in the tracker array
          tracker[i]++;
          // The value at tracker[j] will tell us the count of other numbers that j can be divided
          // by within the array, which will indicate a lucky triple, since to get here j already
          // needs to divide into i. If nothing divided the value a tracker[j] then numberOfTriples
          // would increment by 0 (default value)
          numberOfTriples += tracker[j];
        }
      }
    }
    return numberOfTriples;
  }
}

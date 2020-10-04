package com.google.foobar.levelone;

/**
 * The cake is not a lie! ======================
 *
 * <p>Commander Lambda has had an incredibly successful week: she completed the first test run of
 * her LAMBCHOP doomsday device, she captured six key members of the Bunny Rebellion, and she beat
 * her personal high score in Tetris. To celebrate, she's ordered cake for everyone - even the
 * lowliest of minions! But competition among minions is fierce, and if you don't cut exactly equal
 * slices of cake for everyone, you'll get in big trouble.
 *
 * <p>The cake is round, and decorated with M&Ms in a circle around the edge. But while the rest of
 * the cake is uniform, the M&Ms are not: there are multiple colors, and every minion must get
 * exactly the same sequence of M&Ms. Commander Lambda hates waste and will not tolerate any
 * leftovers, so you also want to make sure you can serve the entire cake.
 *
 * <p>To help you best cut the cake, you have turned the sequence of colors of the M&Ms on the cake
 * into a string: each possible letter (between a and z) corresponds to a unique color, and the
 * sequence of M&Ms is given clockwise (the decorations form a circle around the outer edge of the
 * cake).
 *
 * <p>Write a function called solution(s) that, given a non-empty string less than 200 characters in
 * length describing the sequence of M&Ms, returns the maximum number of equal parts that can be cut
 * from the cake without leaving any leftovers.
 */
public class Solution {

  /**
   * @param x the input string describing the sequence of M&M
   * @return the max number of equal parts that can be cut from the cake without leaving any
   *     leftovers.
   */
  public static int solution(final String x) {
    int maxNumEqualParts = 1;
    final int length = x.length();

    // Strings longer than 200 characters are not valid
    if (length <= 200) {
      for (int i = 1; i <= length; i++) {
        int equalPartsCount = 0;
        String subStr = null;
        // Only check if this iteration can have equal parts (i.e. no remaining slices)
        if (length % i == 0) {
          subStr = x.substring(0, length / i);
          equalPartsCount = countNumOfSubstring(x, subStr);

          // Check the number of parts is equal to the length of the string divided by the substring
          // length. This is for a case for example where if the string is "abccbaabccba" and the
          // substring is "a", the max count at this point will be 2. "a" will then occur 4 times in
          // the string, but it is not a valid case to update the maxNumEqualParts variable because
          // it does not include all of the M&Ms
          if (equalPartsCount == length / subStr.length() && equalPartsCount > maxNumEqualParts) {
            maxNumEqualParts = equalPartsCount;
          }
        }
      }
    }
    return maxNumEqualParts;
  }

  /**
   * @param str the search space
   * @param strToFind the string to find in the search space
   * @return the number of times the strToFind occurs in str
   */
  private static int countNumOfSubstring(final String str, final String strToFind) {
    int lastIndex = 0;
    int count = 0;

    while (lastIndex != -1) {
      lastIndex = str.indexOf(strToFind, lastIndex);
      if (lastIndex != -1) {
        count++;
        lastIndex += strToFind.length();
      }
    }
    return count;
  }
}

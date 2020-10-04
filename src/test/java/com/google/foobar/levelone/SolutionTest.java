package com.google.foobar.levelone;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
  private long startTime;
  private long endTime;
  private long testExecutionTime;
  static List<Long> runTimes = new ArrayList<>();

  @Test
  void testOne() {
    final String input = "abcabcabcabc";
    final int expectedOutput = 4;
    assertEquals(expectedOutput, Solution.solution(input));
  }

  @Test
  void testTwo() {
    final String input = "abccbaabccba";
    final int expectedOutput = 2;
    assertEquals(expectedOutput, Solution.solution(input));
  }

  @Test
  void testThree() {
    final String input = "ccccccccc";
    final int expectedOutput = 9;
    assertEquals(expectedOutput, Solution.solution(input));
  }

  @Test
  void testFour() {
    final String input = "a";
    final int expectedOutput = 1;
    // In the case where the cake cannot be cut evenly we are returning 1
    assertEquals(expectedOutput, Solution.solution(input));
  }

  @Test
  void testFive() {
    final String input =
        "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
    final int expectedOutput = 1;
    // In the case where the cake cannot be cut evenly we are returning zero
    assertEquals(expectedOutput, Solution.solution(input));
  }
}

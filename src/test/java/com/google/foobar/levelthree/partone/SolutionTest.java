package com.google.foobar.levelthree.partone;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

  @Test
  void solution() {
    assertEquals(3, Solution.solution(new int[] {1, 2, 3, 4, 5, 6}));
    assertEquals(1, Solution.solution(new int[] {1, 1, 1}));
    assertEquals(2, Solution.solution(new int[] {3, 6, 5, 30, 18}));
  }

  @Test
  void foobar() {
    assertEquals(3, Solution.foobar(new int[] {1, 2, 3, 4, 5, 6}));
    assertEquals(1, Solution.foobar(new int[] {1, 1, 1}));
    assertEquals(2, Solution.foobar(new int[] {3, 6, 5, 30, 18}));
  }
}

package com.google.foobar.leveltwo.partone;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

  @Test
  void testOne() {
    assertEquals(1, Solution.solution(19, 36));
    assertEquals(3, Solution.solution(0, 1));
    assertEquals(3, Solution.solution(36, 23));
    assertEquals(6, Solution.solution(56, 7));
  }
}

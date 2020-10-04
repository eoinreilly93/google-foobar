package com.google.foobar.levelthree.parttwo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class SolutionTest {

  @Test
  public void testSolution() {
    final int[] expected = {0, 3, 2, 9, 14};
    assertArrayEquals(
        expected,
        Solution.solution(
            new int[][] {
              {0, 1, 0, 0, 0, 1},
              {4, 0, 0, 3, 2, 0},
              {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0}
            }));
  }

  @Test
  public void testSolution2() {
    final int[] expected = {7, 6, 8, 21};
    assertArrayEquals(
        expected,
        Solution.solution(
            new int[][] {
              {0, 2, 1, 0, 0},
              {0, 0, 0, 3, 4},
              {0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0}
            }));
  }

  @Test
  public void testSolution3() {
    final int[] expected = {1, 0, 0, 0, 0, 0, 1};
    assertArrayEquals(
        expected,
        Solution.solution(
            new int[][] {
              {0, 0, 0, 0, 0, 0},
              {4, 0, 0, 3, 2, 0},
              {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0, 0}
            }));
  }

  @Test
  public void testSolution4() {
    final int[] expected = {1, 0, 0, 0, 0, 1};
    final int[] result =
        Solution.solution(
            new int[][] {
              {0, 0, 0, 0, 0},
              {0, 0, 0, 3, 4},
              {0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0},
              {0, 0, 0, 0, 0}
            });
    assertArrayEquals(expected, result);
  }
}

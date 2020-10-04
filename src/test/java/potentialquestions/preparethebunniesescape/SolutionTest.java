package potentialquestions.preparethebunniesescape;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

  @Test
  public void testSolution() {
    assertEquals(
        7, Solution.solution(new int[][] {{0, 1, 1, 0}, {0, 0, 0, 1}, {1, 1, 0, 0}, {1, 1, 1, 0}}));
  }
}

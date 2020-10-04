package potentialquestions.fuelinjection;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
  @Test
  public void testSolution() {
    assertEquals(2, Solution.solution("4"));
    assertEquals(5, Solution.solution("15"));
  }
}

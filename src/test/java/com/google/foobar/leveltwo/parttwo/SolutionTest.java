package com.google.foobar.leveltwo.parttwo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {

  @Test
  public void test() {
    assertEquals(4, Solution.solution("<<>><"));
    assertEquals(2, Solution.solution(">----<"));
    assertEquals(20, Solution.solution("<->>--<>><<"));
    assertEquals(0, Solution.solution("-------"));

    // https://foobar.withgoogle.com/?eid=uAbLa - Referral link
  }
}

package com.google.foobar.leveltwo.partone;

import java.util.*;

/**
 * Don't Get Volunteered! ======================
 *
 * <p>As a henchman on Commander Lambda's space station, you're expected to be resourceful, smart,
 * and a quick thinker. It's not easy building a doomsday device and capturing bunnies at the same
 * time, after all! In order to make sure that everyone working for her is sufficiently
 * quick-witted, Commander Lambda has installed new flooring outside the henchman dormitories. It
 * looks like a chessboard, and every morning and evening you have to solve a new movement puzzle in
 * order to cross the floor. That would be fine if you got to be the rook or the queen, but instead,
 * you have to be the knight. Worse, if you take too much time solving the puzzle, you get
 * "volunteered" as a test subject for the LAMBCHOP doomsday device!
 *
 * <p>To help yourself get to and from your bunk every day, write a function called solution(src,
 * dest) which takes in two parameters: the source square, on which you start, and the destination
 * square, which is where you need to land to solve the puzzle. The function should return an
 * integer representing the smallest number of moves it will take for you to travel from the source
 * square to the destination square using a chess knight's moves (that is, two squares in any
 * direction immediately followed by one square perpendicular to that direction, or vice versa, in
 * an "L" shape). Both the source and destination squares will be an integer between 0 and 63,
 * inclusive, and are numbered like the example chessboard below:
 *
 * <p>------------------------- | 0| 1| 2| 3| 4| 5| 6| 7| ------------------------- | 8|
 * 9|10|11|12|13|14|15| ------------------------- |16|17|18|19|20|21|22|23|
 * |24|25|26|27|28|29|30|31| ------------------------- |32|33|34|35|36|37|38|39|
 * ------------------------- |40|41|42|43|44|45|46|47| |48|49|50|51|52|53|54|55|
 * ------------------------- |56|57|58|59|60|61|62|63| -------------------------
 *
 * <p>Languages =========
 *
 * <p>To provide a Python solution, edit solution.py To provide a Java solution, edit Solution.java
 *
 * <p>Test cases ========== Your code should pass the following test cases. Note that it may also be
 * run against hidden test cases not shown here.
 *
 * <p>-- Python cases -- Input: solution.solution(0, 1) Output: 3
 *
 * <p>Input: solution.solution(19, 36) Output: 1
 *
 * <p>-- Java cases -- Input: Solution.solution(19, 36) Output: 1
 *
 * <p>Input: Solution.solution(0, 1) Output: 3
 */
public class Solution {
  // Maps the chessboard number to the Square containing the x,y coordinates of its position on the
  // board and its distance from the source square
  private static final Map<Integer, Square> squarePositionMap = new HashMap<>();

  // x and y direction of where a knight is allowed to move on board
  private static final int[] KNIGHT_ROW_MOVES = {-2, -1, 1, 2, -2, -1, 1, 2};
  private static final int[] KNIGHT_COL_MOVES = {-1, -2, -2, -1, 1, 2, 2, 1};
  private static final int MAX_KNIGHT_MOVES = 8;

  // Initialise the map
  static {
    int counter = 0;
    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        squarePositionMap.put(counter, new Square(j, i));
        counter++;
      }
    }
  }

  public static int solution(final int src, final int dest) {
    final Square srcSquare = squarePositionMap.get(src);
    final Square destSquare = squarePositionMap.get(dest);
    return findShortestPath(srcSquare, destSquare);
  }

  /**
   * Use BFS to find to shortest path between two squares
   *
   * @param src the source square
   * @param dest the destination square
   * @return the shortest path between the two
   */
  private static int findShortestPath(final Square src, final Square dest) {
    // Will return -1 if no path can be found
    int shortestDistance = -1;
    // Track the squares we have already visited
    final Set<Square> visitedSquares = new HashSet<>();
    final Queue<Square> queue = new ArrayDeque<>();
    queue.add(src);

    while (!queue.isEmpty()) {
      final Square currentSquare = queue.poll();

      // If the destination is reached, set the distance travelled and break the loop
      if (currentSquare.x == dest.x && currentSquare.y == dest.y) {
        shortestDistance = currentSquare.distance;
        break;
      }

      // If this is square we have not seen yet, add up to 8 moves to valid squares to the queue
      if (!visitedSquares.contains(currentSquare)) {
        visitedSquares.add(currentSquare);

        for (int i = 0; i < MAX_KNIGHT_MOVES; i++) {
          final int x1 = currentSquare.x + KNIGHT_ROW_MOVES[i];
          final int y1 = currentSquare.y + KNIGHT_COL_MOVES[i];

          if (validMove(x1, y1)) {
            // Add the square to queue with incremented distance
            queue.add(new Square(x1, y1, currentSquare.distance + 1));
          }
        }
      }
    }
    return shortestDistance;
  }

  private static boolean validMove(final int x, final int y) {
    boolean valid = true;
    if (x < 0 || y < 0 || x > MAX_KNIGHT_MOVES || y > MAX_KNIGHT_MOVES) {
      valid = false;
    }
    return valid;
  }

  /** Denotes a position on the board and its distance from the starting point */
  static class Square {
    private final int x;
    private final int y;
    private int distance;

    /**
     * Constructs a Square object
     *
     * @param x the x position of the square
     * @param y the y position of the square
     */
    public Square(final int x, final int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Constructs a Square object with its distance from the source square
     *
     * @param x the x position of the square
     * @param y the y position of the square
     * @param distance the distance from the source square to this square (i.e the number of moves
     *     it takes to get to it)
     */
    public Square(final int x, final int y, final int distance) {
      this.x = x;
      this.y = y;
      this.distance = distance;
    }
  }
}

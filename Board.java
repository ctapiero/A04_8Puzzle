package a04;

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;

/**
 * Performance requirements. Your implementation should support all Board
 * methods in time proportional to N ^ 2 (or better) in the worst case, with the
 * exception that isSolvable() may take up to N ^ 4 in the worst case.
 *
 * @author Cristian Tapiero + Elliot Kohles
 */
public final class Board {
    // this class should contain immutable data types
    private final Integer[] givenTiles;
    private final Integer N;
    private final Integer[] goalBoard;

    /**
     * You may assume that the constructor receives an N-by-N array containing the
     * N^2 integers between 0 and N^2 - 1, where 0 represents the blank square.
     * <p>
     * construct a board from an N-by-N array of blocks (where blocks[i][j] = block
     * in row i, column j)
     *
     * @param blocks
     */

    public Board(int[][] blocks) {
        N = blocks.length;

        //Constructs a 1D array given the initial board
        givenTiles = new Integer[blocks.length * blocks.length];
        int count = 0;
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {
                givenTiles[count] = blocks[i][j];
                count++;
            }
        }

        //Constructs the goalboard given the initial board
        goalBoard = new Integer[blocks.length * blocks.length];
        int count2 = 1;
        for (int i = 0; i < blocks.length; ++i) {
            for (int j = 0; j < blocks.length; ++j) {

                if (count2 == blocks.length * blocks.length) {
                    goalBoard[count2 - 1] = 0;
                    break;
                }
                goalBoard[count2 - 1] = count2;
                count2++;
            }
        }
    }

    /**
     * board size N
     *
     * @return
     */
    public int size() {
        return N;

    }

    /**
     * number of blocks out of place
     *
     * @return
     */

    public int hamming() {
        int hammingNumber = 0;
        for (int i = 0; i < goalBoard.length; i++) {

            if (givenTiles[i] != 0 && givenTiles[i] != goalBoard[i]) {
                hammingNumber++;
            }
        }
        return hammingNumber;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     *
     * @return
     */
    public int manhattan() {
        int manhattanDistanceSum = 0;
        int count = 0;
        for (int x = 0; x < N; x++) // x-dimension, traversing rows (i)
            for (int y = 0; y < N; y++) { // y-dimension, traversing cols (j)
                int value = givenTiles[count];
                count++;
                // tiles array contains board elements
                if (value != 0) { // we don't compute MD for element 0
                    int targetX = (value - 1) / N; // expected x-coordinate (row)
                    int targetY = (value - 1) % N; // expected y-coordinate (col)
                    int dx = x - targetX; // x-distance to expected coordinate
                    int dy = y - targetY; // y-distance to expected coordinate
                    manhattanDistanceSum += Math.abs(dx) + Math.abs(dy);
                }
            }
        return manhattanDistanceSum;
    }

    /**
     * is this board the goal board?
     *
     * @return
     */
    public boolean isGoal() {
        return Arrays.equals(goalBoard, givenTiles);

    }

    /**
     * is this board solvable?,
     *
     * @return
     */
    public boolean isSolvable() {
        int row = 0;
        for (int i = 0; i < givenTiles.length; i++) {
            if (givenTiles[i] == 0) {
                row = i / N;
                break;
            }
        }
        if (N % 2 == 0) {
            int inversion = 0;
            int blankRow = row;
            for (int i = 0; i < givenTiles.length; i++) {
                for (int j = i; j < givenTiles.length; j++) {

                    if (givenTiles[i] == 0) {
                        continue;
                    }
                    if (givenTiles[j] == 0) {
                        continue;
                    }
                    if (givenTiles[i] > givenTiles[j]) {
                        inversion++;
                    }

                }
            }
            return !((inversion + blankRow) % 2 == 0);
        }
        // odd board
        else {
            int inversion = 0;
            for (int i = 0; i < givenTiles.length; i++) {
                for (int j = i; j < givenTiles.length; j++) {

                    if (givenTiles[i] == 0) {
                        continue;
                    }
                    if (givenTiles[j] == 0) {
                        continue;
                    }
                    if (givenTiles[i] > givenTiles[j]) {
                        inversion++;
                    }

                }
            }
            return (inversion % 2 == 0);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((N == null) ? 0 : N.hashCode());
        result = prime * result + Arrays.hashCode(givenTiles);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Board other = (Board) obj;
        if (N == null) {
            if (other.N != null)
                return false;
        } else if (!N.equals(other.N))
            return false;
        if (!Arrays.equals(givenTiles, other.givenTiles))
            return false;
        return true;
    }

    /**
     * all neighboring boards
     *
     * @return
     */
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<Board>();

        int row = 0;
        int column = 0;
        for (int i = 0; i < givenTiles.length; i++) {
            if (givenTiles[i] == 0) {
                column = i % N;
                row = i / N;
                break;
            }
        }

        int above = (row - 1) * N + column;
        int below = (row + 1) * N + column;
        int left = row * N + (column - 1);
        int right = row * N + (column + 1);

        int blankSquare = row * N + column;

        if (row > 0) {
            //Integer[] copyArr = Arrays.copyOf(givenTiles, givenTiles.length);
            int copyArr[] = new int[givenTiles.length];

            // Copy elements of a[] to b[]
            for (int i = 0; i < givenTiles.length; i++)
                copyArr[i] = givenTiles[i];
            int[][] aboveBoard = new int[N][N];
            Integer storageInt = copyArr[above];
            copyArr[above] = copyArr[blankSquare];
            copyArr[blankSquare] = storageInt;

            int counter = 0;
            for (int i = 0; i < aboveBoard.length; i++) {
                for (int j = 0; j < aboveBoard.length; j++) {
                    aboveBoard[i][j] = copyArr[counter];
                    counter++;
                }
            }
            neighbors.push(new Board(aboveBoard));
        }

        if (row < N - 1) {
            //Integer[] copyArr = Arrays.copyOf(givenTiles, givenTiles.length);
            int copyArr[] = new int[givenTiles.length];

            // Copy elements of a[] to b[]
            for (int i = 0; i < givenTiles.length; i++)
                copyArr[i] = givenTiles[i];
            int[][] belowBoard = new int[N][N];
            Integer storageInt = copyArr[below];
            copyArr[below] = copyArr[blankSquare];
            copyArr[blankSquare] = storageInt;

            int counter = 0;
            for (int i = 0; i < belowBoard.length; i++) {
                for (int j = 0; j < belowBoard.length; j++) {
                    belowBoard[i][j] = copyArr[counter];
                    counter++;
                }
            }
            neighbors.push(new Board(belowBoard));
        }
        if (column > 0) {
            //Integer[] copyArr = Arrays.copyOf(givenTiles, givenTiles.length);
            int copyArr[] = new int[givenTiles.length];

            // Copy elements of a[] to b[]
            for (int i = 0; i < givenTiles.length; i++)
                copyArr[i] = givenTiles[i];
            int[][] leftBoard = new int[N][N];
            Integer storageInt = copyArr[left];
            copyArr[left] = copyArr[blankSquare];
            copyArr[blankSquare] = storageInt;

            int counter = 0;
            for (int i = 0; i < leftBoard.length; i++) {
                for (int j = 0; j < leftBoard.length; j++) {
                    leftBoard[i][j] = copyArr[counter];
                    counter++;
                }
            }

            neighbors.push(new Board(leftBoard));
        }
        if (column < N - 1) {
            //Integer[] copyArr = Arrays.copyOf(givenTiles, givenTiles.length);
            int copyArr[] = new int[givenTiles.length];

            // Copy elements of a[] to b[]
            for (int i = 0; i < givenTiles.length; i++)
                copyArr[i] = givenTiles[i];
            int[][] rightBoard = new int[N][N];
            Integer storageInt = copyArr[right];
            copyArr[right] = copyArr[blankSquare];
            copyArr[blankSquare] = storageInt;

            int counter = 0;
            for (int i = 0; i < rightBoard.length; i++) {
                for (int j = 0; j < rightBoard.length; j++) {
                    rightBoard[i][j] = copyArr[counter];
                    counter++;
                }
            }
            neighbors.push(new Board(rightBoard));
        }

        return neighbors;
    }

    /**
     * string representation of this board (in the output format specified below)
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        int count = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                s.append(String.format("%2d ", (int) givenTiles[count]));

                count++;
            }
            s.append("\n");
        }
        return s.toString();
    }
}

    // Helper method to test the goalboard
    // public void printGoal() {
    // System.out.println(Arrays.toString(goalBoard));
//	}

    /**
     * unit tests (not graded)
     *
     * @param args
     */
//    public static void main(String[] args) {
//
//        // tests the construction of the tiles in any size
//        int n = 3;
//        int count2 = 0;
//        int[][] test = new int[n][n];
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                if (count2 == n * n - 1) {
//                    test[i][j] = 0;
//                    break;
//                }
//                test[i][j] = count2 + 1;
//                count2++;
//            }
//        }
//
//        Board myBoard = new Board(test);
//        System.out.println(myBoard.toString());
//        System.out.println(myBoard.isGoal());
//
//        // testing more blocks
//        int[][] test2 = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
////		int[][] test3 = { { 1, 2, 3}, {4, 5, 6}, {7, 8, 0} };
//        int[][] test4 = {{2, 0}, {1, 3}};
//
//        Board myBoard1 = new Board(test2);
//
//        System.out.println(myBoard1.toString());
//        System.out.println(myBoard1.isGoal());
//        System.out.println(myBoard1.hamming());
//        System.out.println(myBoard1.manhattan());
//        System.out.println(myBoard1.isSolvable());
//
//        System.out.println(myBoard1.neighbors());
//
//        Board myBoard2 = new Board(test4);
//
//        System.out.println(myBoard2.toString());
//        System.out.println(myBoard2.isGoal());
//        System.out.println(myBoard2.hamming());
//        System.out.println(myBoard2.manhattan());
//        System.out.println(myBoard2.isSolvable());
//
//        System.out.println(myBoard2.neighbors());
//
//    }
//}

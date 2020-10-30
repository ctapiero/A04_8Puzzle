package a04;

import edu.princeton.cs.algs4.*;

/**
 * @author Cristian Tapiero + Elliot Kohles
 */
public class Solver {
    Queue<Board> solutions = new Queue<>();
    private int moves;
    private Board initial;
    private SearchNode first;
    private SearchNode last;


    private class SearchNode implements Comparable<SearchNode> {
        private Board item;
        private int moves;
        private int priority;
        private SearchNode next;
        private SearchNode previous;

        public SearchNode(Board newItem, int moves) {
            this.item = newItem;
            this.moves = moves;
            this.priority = newItem.manhattan() + moves;

            if (moves == 0) {
                first = this;
                last = this;
                previous = null;
            } else {
                this.previous = last;
                last.next = this;
                last = this;
            }
        }

        @Override
        public int compareTo(SearchNode other) {
            if (this.priority > other.priority) return 1;
            if (this.priority < other.priority) return -1;
            else return 0;
        }
    }

    /**
     *  Helper class.
     *  represents a search node of the game (such as the board, the number of moves to reach it,
     *  and the previous search node).You can either make this a nested class within Solver or make it a stand-alone class.
     *  Be sure to include a constructor which initializes the value of each instance variable.
     * @author Cristian Tapiero
     *
     */
	/*public class SearchNode implements Comparable<SearchNode> {
		private Board board;
		private Integer moves = 0;
		private Integer priority;
		private Character id;
		private Node initial;
		private Node last;

		private Iterable<Board> children;


		private class Node {
			Board item;
			Node parent;
		}

		public void append(Board newItem) {
			// create a new node based on the word provided by the user
			Node newNode = new Node();
			newNode.item = newItem;

			if (moves == 0) {
				initial = newNode;
				last = newNode;
			}
			else {
				last.parent = newNode;
				last = newNode;
			}
			//n++;
		}
		/**
		 * @param board
		 * @param moves
		 */
		/*public SearchNode(Board board, int moves) {
			append(board);

			this.id = 'A';
			this.board = board;
			//this.moves = moves++;
			this.priority = board.manhattan() + moves;
			this.children = board.neighbors();
		    if(this.id.equals('A')) {
		    	this.parent = null;
		    }
		    else{
		    	this.id = (char) (this.id + 1);
		    	this.parent = (char) (this.id -1);
		    }
		}
		@Override
		public int compareTo(SearchNode other) {
			if(this.priority > other.priority) return 1;
			if(this.priority < other.priority) return -1;
			else return 0;
		}





	}*/

    /**
     * find a solution to the initial board (using the A* algorithm)
     * The constructor should throw a java.lang.IllegalArgumentException if the initial board
     * is not solvable and a java.lang.NullPointerException if the initial board is null.
     * <p>
     * This will include creating a MinPQ. You can choose one of two options to determine order:
     * 1.Make it implement the Comparable<SearchNode> interface so that you can use it with a MinPQ.
     * The compareTo() method should compare search nodes based on their Hamming or Manhattan priorities.
     * 2.Create a Comparator<SearchNode> for each priority function and initialize the MinPQ with it.
     *
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) throw new java.lang.NullPointerException();
        if (initial.isSolvable() == false) throw new java.lang.IllegalArgumentException();

        this.initial = initial;
        MinPQ<SearchNode> myPQ = new MinPQ<>();
        SearchNode myNode = new SearchNode(initial, 0);
        myPQ.insert(myNode);
        getSolution(myPQ);
        //char id =  myNode.id;
        //char parent = myNode.parent;

    }

    private void getSolution(MinPQ<SearchNode> solver) {
        SearchNode tempBoard = solver.delMin();

        solutions.enqueue(tempBoard.item);
        if (!tempBoard.item.isGoal()) {
            moves++;
            for (Board el : tempBoard.item.neighbors()) {
                SearchNode tempNode = new SearchNode(el, moves);
                if (tempNode.previous != null && !el.equals(tempNode.previous.item)) {

                    solver.insert(new SearchNode(el, moves));
                }
            }

            getSolution(solver);
        }
    }

    /**
     * min number of moves to solve initial board
     *
     * @return
     */
    public int moves() {
        return moves;

    }

    /**
     * sequence of boards in a shortest solution
     *
     * @return
     */
    public Iterable<Board> solution() {
        return solutions;

    }


    /**
     * solve a slider puzzle (given below)
     *
     * @param args
     */
    public static void main(String[] args) {
        // header
        StdOut.printf("%-25s %7s %8s\n", "filename", "moves", "time");
        StdOut.println("------------------------------------------");

        // for each command-line argument
        String filename = "src/a04/files/puzzle25.txt";
            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] blocks = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    blocks[i][j] = in.readInt();
            Board initial = new Board(blocks);

            // check if puzzle is solvable; if so, solve it print out number of moves
            if (initial.isSolvable()) {
                Stopwatch timer = new Stopwatch();
                Solver solver = new Solver(initial);
                int moves = solver.moves();
                double time = timer.elapsedTime();
                StdOut.printf("%-25s %7d %8.2f\n", filename, moves, time);
            }

            // if not, print that it is unsolvable
            else {
                StdOut.printf("%-25s   unsolvable\n", filename);
            }
        }
    }



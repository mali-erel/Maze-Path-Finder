import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Mali
 */

//----------------------------------------------------- 
//Title: Homework I - James Bond
//Author: Mehmet Ali EREL
//Section: 03
//Assignment: 01
//Description: The maze puzzle.
//-----------------------------------------------------
public class JamesBond {

	//I created the "Maze" class with multiple variables to contains necessary informations.
	//After that i made a constructor to access these informations when needed.
	  private static class Maze {

	        List<Coordinate> visitedCoordinates = new ArrayList<Coordinate>();
	        List<List<Character>> matrix = new ArrayList<List<Character>>();
	        String valid = "abcdefghijklmnopqrstuvwxyzE";
	        List<List<Integer>> treasureCordinats = new ArrayList<List<Integer>>();
	        String path = "";
	        Coordinate entry;

	        public Maze() {
	        }
	        //This method gets the coordinates of entry.
	        private Coordinate getEntry() {
	            return this.entry;
	        }
	        //This checks the path is made of walls or not.
	        private boolean isValidLocation(int row, int col) {
	            if (this.valid.contains(this.matrix.get(row).get(col).toString())) {
	                return true;
	            } else {
	                return false;
	            }
	        }
	        //This method checks the path of already explored by checking in the visitedCoordinates. Returns boolean.
	        private boolean isExplored(int row, int col) {
	            Coordinate xy = new Coordinate(row, col);
	            for (int i = 0; i < visitedCoordinates.size(); i++) {
	                if (visitedCoordinates.get(i).col == xy.col && visitedCoordinates.get(i).row == xy.row) {
	                    return true;
	                }
	            }
	            return false;
	        }
	        //This method is sets the block of path that already passed but has a dead end.
	        private void setVisited(int row, int col, boolean b) {
	            Coordinate xy = new Coordinate(row, col);
	            visitedCoordinates.add(xy);

	        }
	        //This is not necessary method but i made it since felt like it made my code easier.
	        private boolean isExit(int row, int col) {
	            if (row == 1 && col == 0) {
	                return true;
	            } else {
	                return false;
	            }

	        }
	    }
	//This class reads input files line by line and contains each Character in a list. And while reading file, 
	//class counts the treasures by finding them and put the coordinates of them in an integer list. After treasure coordinates found,
	//they put in the coordinate list in the "james" object.
	//When all done, lines are added in the matrix.
    public static void readMaze(Maze james) throws Exception {
        List<List<Character>> matrix = new ArrayList<List<Character>>();

        BufferedReader buffer = new BufferedReader(new FileReader(james.path));

        String line;
        int row = 0;

        while ((line = buffer.readLine()) != null) {
            String vals = line;

            List<Character> asList = new ArrayList<Character>();

            for (int i = 0; i < line.length(); i++) {
                asList.add(line.charAt(i));
                if (vals.charAt(i) == 'E') {
                    List<Integer> cordinat = new ArrayList<Integer>();
                    cordinat.add(row);
                    cordinat.add(i);

                    james.treasureCordinats.add(cordinat);
                }
            }

            matrix.add(asList);

            row++;
        }
        buffer.close();
        james.matrix = matrix;

    }
    //I created this 2D int array directive so the code can look around.
    // {0, -1} means left, {-1, 0} means up , {0, 1} means right, {1, 0} means down.
    private static int[][] DIRECTIONS
            = {{0, -1}, {-1, 0} , {0, 1}, {1, 0}};

    private static Coordinate getNextCoordinate(
            int row, int col, int i, int j) {
        return new Coordinate(row + i, col + j);
    }

    //This part is recursive. I send the maze object itself, the entry's x and y coordinates and an empty path to contain correct 
    //paths. There is an "if" that is boolean. If the explore return false, the method returns an empty list to the collections class.
    public static List<Coordinate> solve(Maze maze) {
        List<Coordinate> path = new ArrayList<>();
        if (explore(
                maze,
                maze.getEntry().getY(),
                maze.getEntry().getX(),
                path
        )) {
            return path;
        }
        return Collections.emptyList();
    }
    //This method checks every blocks around and tries to find a valid exit. If it finds it, returns true.
    private static boolean explore(
            Maze maze, int row, int col, List<Coordinate> path) {
        if (!maze.isValidLocation(row, col)
                || maze.isExplored(row, col)) {
            return false;
        }

        path.add(new Coordinate(row, col));
        maze.setVisited(row, col, true);

        if (maze.isExit(row, col)) {
            return true;
        }

        for (int[] direction : DIRECTIONS) {
            Coordinate coordinate = getNextCoordinate(
                    row, col, direction[0], direction[1]);
            if (explore(
                    maze,
                    coordinate.getY(),
                    coordinate.getX(),
                    path
            )) {
                return true;
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    /*This is my main method. In here, a maze object created at first. Then the file path assigned into this object.
    Finally with the treasure size, needed informations printed.
    */
    public static void main(String[] args) throws Exception {
        Maze james = new Maze();

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        String s = bufferRead.readLine();
        james.path = s;
       
        
        //james.path = "/Users/mali.erel/Desktop/MazeFile.txt";
        readMaze(james);

        /* In this for loop, every solved coordinates are added into the pathCoordinates. Also, this pathCoordinates
         * includes empty indexes. So, the if on below checks the pathCoordinates is empty; if it's not the path
         * puts in a string to print out.
         * */
        List<String> paths = new ArrayList<String>();
        for (int i = 0; i < james.treasureCordinats.size(); i++) {
            Maze JamesBond = new Maze();
            JamesBond.matrix = james.matrix;
            JamesBond.entry = new Coordinate(james.treasureCordinats.get(i).get(0), james.treasureCordinats.get(i).get(1));
            List<Coordinate> pathCoordinates = solve(JamesBond);
            if (!pathCoordinates.isEmpty()) {

                String pathString = "";
                for (int j = pathCoordinates.size() - 1; j >= 0; j--) {
                    pathString += JamesBond.matrix.get(pathCoordinates.get(j).row).get(pathCoordinates.get(j).col);
                }

                paths.add(pathString);

            }
        }
        if(paths.size()>0){
        System.out.println(james.path);
        System.out.println(paths.size() + " treasures are found.");
        System.out.println("Paths are:");
        paths.sort(null);
        for (int i = 0; i < paths.size(); i++) {
            System.out.println(i+1+")"+paths.get(i));

        }
}else{
    System.out.println("0 treasures are found.");
}
    }
    // Coordinate class contains row and coloum data and their get methods.
    private static class Coordinate {

        int row;
        int col;

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getX() {
            return this.col;
        }

        public int getY() {
            return this.row;
        }
    }
}

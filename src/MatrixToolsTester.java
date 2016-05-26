/**
 * Created by timstoddard on 5/21/16.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MatrixToolsTester {

    private static int failedTests = 0;

    public static void main(String[] args) {

        // Test the transpose function
        testTranspose();

        // Test the add function
        testAdd();

        // Test the multiply function
        testMultiply();

        // Test the determinant function
        testDet();

        // Test the cofactor function
        testCofactor();

        finalMessage();
    }

    private static void testTranspose() {
        ArrayList<int[][]> matrices = read("transpose");
        ArrayList<int[][]> expected = read("transpose_expected");
        for (int i = 0; i < matrices.size(); i++) {
            assertMatrixEquals(MatrixTools.transpose(matrices.get(i)), expected.get(i), "transpose");
        }
    }

    private static void testAdd() {
        ArrayList<int[][]> matrices = read("add");
        ArrayList<int[][]> expected = read("add_expected");
        assertMatrixEquals(MatrixTools.add(matrices), expected.get(0), "add");
    }

    private static void testMultiply() {
        ArrayList<int[][]> matrices = read("multiply");
        ArrayList<int[][]> expected = read("multiply_expected");
        for (int i = 0; i < matrices.size(); i += 2) {
            assertMatrixEquals(
                    MatrixTools.multiply(matrices.get(i), matrices.get(i + 1)),
                    expected.get(i / 2),
                    "multiply");
        }
    }

    private static void testDet() {
        ArrayList<int[][]> matrices = read("data");
        int[] expected = new int[] {
                2,          // det [[1,0],[-1,2]]
                -7,         // det [[-1,2,5],[1,0,-2],[1,1,3]]
                0,          // det [[1,2,3],[2,3,4],[3,4,5]]
                -3385,      // det [[5,8,9,0],[-2,4,-1,7],[8,1,0,2],[5,3,6,6]]
                -4340,      // det [[-8,5,-4,0],[2,-9,7,-9],[2,0,-9,5],[0,-6,-1,3]]
                -1024,      // det [[-1,-5,3,0],[8,4,-7,2],[9,7,1,0],[-4,-7,8,-4]]
                -5769,      // det [[1,1,8,-6],[6,0,-1,8],[-3,0,8,8],[8,6,-6,9]]
                -5274,      // det [[8,1,8,-6],[3,0,-1,8],[2,0,8,8],[-3,6,-6,9]]
                -3464,      // det [[8,1,8,-6],[3,6,-1,8],[2,-3,8,8],[-3,8,-6,9]]
                -4633,      // det [[8,1,1,-6],[3,6,0,8],[2,-3,0,8],[-3,8,6,9]]
                -1213,      // det [[8,1,1,8],[3,6,0,-1],[2,-3,0,8],[-3,8,6,-6]]
                52903,      // det [[-9,-8,-6,3,-7],[8,1,1,8,-6],[3,6,0,-1,8],[2,-3,0,8,8],[-3,8,6,-6,9]]
                -6286,      // det [[-1,2,0,0,5],[9,-1,8,-3,-4],[7,-4,8,3,1],[-3,-2,2,-5,-7],[2,1,4,0,-2]]
                -7076,      // det [[5,-4,1,8,-3],[6,2,6,-7,2],[-2,0,2,-4,0],[8,-2,3,9,7],[1,6,3,-4,7]]
                343460,     // det [[4,2,-3,0,8,9],[-2,-2,8,0,7,6],[2,-9,0,0,-2,2],[4,-9,-5,-1,-9,7],[7,-4,8,-8,8,-5],[2,8,-9,7,5,-3]]
                329839,     // det [[-2,-4,-6,7,1,7],[-8,3,3,1,6,6],[-4,0,8,-2,0,-3],[-4,0,0,0,-1,9],[1,-9,-3,-4,-3,-7],[0,0,-1,-9,7,9]]
                8148127,    // det [[-3,8,0,1,5,-6,2,2],[-4,5,-7,4,1,6,-1,8],[6,-7,2,0,2,4,0,2],[5,8,2,9,5,8,9,-5],[6,5,4,1,-1,1,2,0],[9,0,4,5,2,4,1,4],[3,0,8,5,9,3,9,8],[4,5,2,0,0,4,6,3]]
                41493208    // det [[9,6,9,3,5,2,3,9,4],[-5,6,5,1,6,6,4,-4,3],[8,9,5,8,5,2,9,4,8],[-3,2,1,1,0,4,0,5,6],[3,0,5,1,1,1,3,7,3],[6,5,5,0,0,8,2,4,4],[5,1,5,7,1,1,3,2,8],[2,-9,-5,7,0,0,0,0,9],[5,8,2,-7,1,8,6,4,-2]]
        };
        for (int i = 0; i < matrices.size(); i++) {
            assertEquals(MatrixTools.det(matrices.get(i)), expected[i], "determinant");
        }
    }

    private static void testCofactor() {
        ArrayList<int[][]> matrices = read("data");
        ArrayList<int[][]> expected = read("cofactors_expected");

        for (int i = 0; i < matrices.size() - 1; i++) {
            assertMatrixEquals(MatrixTools.cofactor(matrices.get(i)), expected.get(i), "cofactor");
        }
    }

    /**
     * Reads in matrix data from a specified file
     * @param name name of the text file to read from
     * @return an ArrayList of matrices from the text file
     */
    private static ArrayList<int[][]> read(String name) {
        ArrayList<int[][]> matrixList = new ArrayList<>();
        BufferedReader in;
        try {
            in = new BufferedReader(new FileReader("txt/" + name + ".txt"));
            String text;
            while ((text = in.readLine()) != null) {
                if (text.length() == 0 || text.length() > 0 && !Character.isDigit(text.charAt(0))) {
                    continue;
                }
                int wid = Integer.parseInt(text), hgt = Integer.parseInt(in.readLine());
                int[][] matrix = new int[wid][hgt];
                for (int i = 0; i < wid; i++) {
                    String[] data = in.readLine().split(",");
                    for (int j = 0; j < hgt; j++) {
                        matrix[i][j] = Integer.parseInt(data[j]);
                    }
                }
                matrixList.add(matrix);
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            System.out.println("IO Error occurred.");
        }
        return matrixList;
    }

    /**
     * Private helper function to generate a random n x n matrix
     * @param n dimension of the matrix
     * @return an n x n matrix in which every space is a random integer -9 ≤ val ≤ 9, with a bias towards positive integers
     */
    private static int[][] randMatrix(int n) {

        // create the double array used to hold the matrix
        int[][] temp = new int[n][n];

        // initialize the matrix values, and print in .txt format
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[i][j] = (int) (Math.random() * 20 - 10);
                // add a bias towards positive values
                if (temp[i][j] < 0) {
                    temp[i][j] *= Math.random() < 0.8 ? -1 : 1;
                }
                System.out.print(temp[i][j] + (j < n - 1 ? "," : ""));
            }
            System.out.println();
        }

        // print in format optimized for WolframAlpha
        System.out.println("\n" + java.util.Arrays.deepToString(temp).replace(" ", ""));

        // return the matrix
        return temp;
    }

    /**
     * Prints an error message if the values are not equal
     * @param value experimental value
     * @param expected expected value
     * @param testName name of the test from which this function is called
     */
    private static void assertEquals(int value, int expected, String testName) {
        if (value != expected) {
            System.out.println("You failed one of the " + testName + " tests!");
            System.out.println("Expected " + expected + ", but found " + value + ".");
            failedTests++;
        }
    }

    /**
     * Prints an error message if the matrices are not equal
     * @param matrix experimental matrix
     * @param expected expected matrix
     * @param testName name of the test from which this function is called
     */
    private static void assertMatrixEquals(int[][] matrix, int[][] expected, String testName) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != expected[i][j]) {
                    System.out.println("You failed one of the " + testName + " tests!");
                    System.out.println("Expected: ");
                    MatrixTools.print(expected);
                    System.out.println("Found: ");
                    MatrixTools.print(matrix);
                    failedTests++;
                    return;
                }
            }
        }
    }

    private static void finalMessage() {
        if (failedTests == 0) {
            System.out.println("You passed all the tests!");
        }
    }
}
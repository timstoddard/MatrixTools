/**
 * Created by timstoddard on 5/21/16.
 */

import java.util.ArrayList;

public class MatrixTools {

    /**
     * Returns the transpose of a given matrix
     * @param m 2d array representing a matrix
     * @return the transpose of the given matrix
     */
    public static int[][] transpose(final int[][] m) {

        // given that m has n rows and p columns, make a new matrix with p rows and n columns
        int[][] temp = new int[m[0].length][m.length];

        // loop through every value in the matrix
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                // transpose each value in the matrix
                temp[j][i] = m[i][j];
            }
        }

        return temp;
    }

    /**
     * Adds 2 or more matrices
     */
    public static int[][] add(final ArrayList<int[][]> matrixList) {

        // the matrices need to be the same size; otherwise, throw an error
        if (matrixList.size() == 0) {
            throw new MatrixException("Given matrices are not compatible for addition.");
        }

        int[][] temp = new int[matrixList.get(0).length][matrixList.get(0)[0].length];

        // sum the values at each location of the matrices
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[0].length; j++) {
                for (int k = 0; k < matrixList.size(); k++) {
                    temp[i][j] += matrixList.get(k)[i][j];
                }
            }
        }

        return temp;
    }

    /**
     * Multiplies 2 matrices
     * @param m1 the first matrix
     * @param m2 the second matrix
     * @return the matrix resulting from multiplying m1 and m2
     */
    public static int[][] multiply(final int[][] m1, final int[][] m2) {

        // for m1 having dimensions m x n, m2 must have dimensions n x p,
        // so if that is not the case multiplication cannot be performed
        if (m1[0].length != m2.length) {
            throw new MatrixException("Given matrices are not compatible for multiplication.");
        }

        // create a temp matrix to hold the results
        int[][] temp = new int[m1.length][m2[0].length];

        // loop through the rows of m1
        for (int i = 0; i < m1.length; i++) {

            // loop through the columns of m2
            for (int j = 0; j < m2[0].length; j++) {

                // loop through the columns of m1
                for (int k = 0; k < m1[i].length; k++) {

                    // perform the multiplication and add it to the running total
                    temp[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return temp;
    }

    /**
     * Finds the determinant of an n x n matrix
     * @param m the matrix for which we will calculate the determinant
     * @return the determinant of the given matrix
     */
    public static int det(final int[][] m) {

        // check if we have a 0 x 0 or 1 x 1 matrix; if so, throw an error
        if (m.length == 0 || m.length == 1) {
            throw new MatrixException("Given matrix is too small to find the determinant.");
        }

        // check if we have a square matrix; otherwise, throw an error
        for (int i = 0; i < m.length; i++) {
            if (m[i].length != m.length) {
                throw new MatrixException("Given matrix is not a square matrix.");
            }
        }

        return det(m, new ArrayList<>(), 0);
    }

    /**
     * Recursively finds the determinant of an n x n matrix
     * @param m 2d array representing a matrix
     * @param skipCols list of columns to skip over
     * @param vOffset vertical offset from the top
     * @return the determinant of the given square matrix
     */
    private static int det(final int[][] m, final ArrayList<Integer> skipCols, final int vOffset) {

        // base case: we have a 2 x 2 matrix
        if (m.length - vOffset == 2) {
            // find the two valid columns
            int col1 = -1, col2 = -1;
            for (int i = 0; i < m.length; i++) {
                if (!skipCols.contains(i)) {
                    // if col1 has a valid value, move on to col2
                    if (col1 > -1) {
                        col2 = i;
                        break;
                    // col1 doesn't yet have a valid value, so give it the one we just found
                    } else {
                        col1 = i;
                    }
                }
            }

            // return the determinant of the 2 x 2 matrix
            return m[vOffset][col1] * m[vOffset + 1][col2] - m[vOffset][col2] * m[vOffset + 1][col1];
        }

        // move along the top row
        int det = 0, count = 0;
        // create a temp list holding all of the columns to skip
        ArrayList<Integer> temp = new ArrayList<>();
        temp.addAll(skipCols);
        for (int i = 0; i < m.length; i++) {
            // only check non-skipped columns
            if (!skipCols.contains(i)) {
                // if the current value is 0, we can ignore it since it won't affect the determinant
                if (m[vOffset][i] != 0) {
                    // add the current column to the list of columns to skip
                    temp.add(i);
                    // add the current value times the inner determinant of the current space in the matrix
                    // also, check if it's necessary to multiply by -1 based on where we are in the row
                    det += (count % 2 == 0 ? 1 : -1) * m[vOffset][i] * det(m, temp, vOffset + 1);;
                    temp.remove(temp.size() - 1);
                }
                // increment the number of rows we've looked at
                count++;
            }
        }

        return det;
    }

    /**
     * Replaces each element in a given matrix with its cofactor
     */
    public static int[][] cofactor(final int[][] m) {

        // check if we have a 0 x 0 or 1 x 1 matrix; if so, throw an error
        if (m.length == 0 || m.length == 1) {
            throw new MatrixException("Given matrix is too small to find the cofactor matrix.");
        }

        // check if we have a square matrix; otherwise, throw an error
        for (int i = 0; i < m.length; i++) {
            if (m[i].length != m.length) {
                throw new MatrixException("Given matrix is not a square matrix.");
            }
        }

        // check if we have a 2 x 2 matrix; if so, return the cofactor matrix
        if (m.length == 2) {
            return new int[][] {
                    {m[1][1], -m[1][0]},
                    {-m[0][1], m[0][0]}
            };
        }

        // create a new matrix with the same dimensions as the given matrix
        int[][] cofactors = new int[m.length][m[0].length];

        // loop through all the values in the array
        for (int i = 0; i < m.length; i++) {

            // create a temp matrix to hold the determinant matrix of each value
            // initialize it to the determinant matrix of the leftmost value in the current row
            int[][] temp = new int[m.length - 1][m[0].length - 1];
            boolean skippedRow = false;
            for (int j = 0; j < m.length - 1; j++) {
                if (j == i) {
                    skippedRow = true;
                }
                for (int k = 1; k < m[0].length; k++) {
                    temp[j][k - 1] = m[j + (skippedRow ? 1 : 0)][k];
                }
            }

            for (int j = 0; j < m[0].length; j++) {

                // find the determinant, and put it in the current space in the new matrix
                cofactors[i][j] = ((i + j) % 2 == 0 ? 1 : -1) * det(temp);

                // we've reached the last sub-matrix of this row, so don't bother messing with the column data
                if (j == m[0].length - 1) {
                    continue;
                }

                // shift matrix to prepare for the next element by replacing the nth column of _temp with the nth column of m
                boolean skippedCol = false;
                for (int k = 0; k < temp.length; k++) {
                    if (k == i) {
                        skippedCol = true;
                    }
                    temp[k][j] = m[k + (skippedCol ? 1 : 0)][j];
                }
            }
        }

        return cofactors;
    }

    /**
     * Prints a given matrix
     */
    public static void print(final int[][] arr) {

        // find the length of the longest element (as a string)
        int maxLen = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                int temp = (arr[i][j] + "").length();
                if (temp > maxLen) {
                    maxLen = temp;
                }
            }
        }

        // print out all the elements
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                int currLen = (arr[i][j] + "").length();
                // print spaces for padding, if necessary
                if (currLen < maxLen) {
                    for (int k = 0; k < maxLen - currLen; k++) {
                        System.out.print(" ");
                    }
                }
                // print out the element, and a space if it is not the last element in a row
                System.out.print(arr[i][j] + (j < arr[0].length - 1 ? " " : ""));
            }
            System.out.println();
        }
    }
}
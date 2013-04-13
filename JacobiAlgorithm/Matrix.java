import java.util.Random;

public class Matrix {

	public double[][] matrix;
	public int rows;
	public int columns;
	final double offThreshold = 0.0000000010;
	final int maxPossibleValue = 20;
	final double roundingThreshold = 0.00000001;


	/**
	 * Constructs a matrix with the specified dimensions
	 * @param r number of rows
	 * @param c number of columns
	 */
	public Matrix(int r, int c) {
		matrix = new double[r][c];
		rows = r;
		columns = c;
	}

	public void randomizeMatrix() {
		Random rand = new Random();

		// Randomize Diagonals
		for (int i = 0; i < rows; i++) {
			matrix[i][i] = rand.nextDouble() * maxPossibleValue - (maxPossibleValue / 2);
		}
		
		for (int r = 0; r < rows; r++) {
			for (int c = r + 1; c < columns; c++) {
				double value = rand.nextDouble() * maxPossibleValue - (maxPossibleValue / 2);
				matrix[r][c] = value;
				matrix[c][r] = value;
			}
		}
	};

	public Pair getLargestOffDiagnoal() {
		int largeR  = 0;
		int largeC = 1;

		double largestDiagonal = 0;

		for (int r = 0; r < rows; r++) {
			for (int c = r + 1; c < columns; c++) {
				double value = Math.abs(matrix[r][c]); 
				if (value > largestDiagonal) {
					largestDiagonal = value;
					largeR = r;
					largeC = c;
				}
			}
		}

		return new Pair(largeR, largeC);
	};

	/**
	 * Gets a 2x2 matrix using the off-diagonal coordinates.
	 * 
	 * @param r coordinate of the row
	 * @return c coordinate of the column
	 */
	public Matrix getOffDiagonalMatrix(int r, int c) {

		Matrix mat = new Matrix(2, 2);

		mat.matrix[0][0] = matrix[r][r];
		mat.matrix[0][1] = matrix[r][c];
		mat.matrix[1][0] = matrix[c][r];
		mat.matrix[1][1] = matrix[c][c];

		return mat;

	}

	/**
	 * For a 2x2 matrix, retrieves a matrix that contains all the nontrivial eigenvectors.
	 */
	public Matrix getEigenvectors(Matrix mat) {
		double a = mat.matrix[0][0];
		double b = mat.matrix[0][1];
		double d = mat.matrix[1][1];

		// First get eigenvalue
		double eigenvalue = (a+d)/2 + Math.sqrt(b*b + Math.pow((a-d)/2 ,2));

		// Find eigenvectors
		Matrix eigenMat = new Matrix(2, 2);
		double eigen1 = a - eigenvalue;
		double eigen2 = b;
		double eigen_mag = Math.sqrt(eigen1 * eigen1 + eigen2 * eigen2);

		eigenMat.matrix[0][0] = -eigen2 / eigen_mag;
		eigenMat.matrix[1][0] = eigen1 / eigen_mag;
		eigenMat.matrix[0][1] = -eigen1 / eigen_mag;
		eigenMat.matrix[1][1] = -eigen2 / eigen_mag;
		
		return eigenMat;


	}

	/**
	 * Returns the transpose of the given matrix
	 * 
	 * @param mat
	 * @return mat transposed
	 */
	public Matrix getTranspose(Matrix mat) {
		Matrix toRet = new Matrix(rows, columns);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				toRet.matrix[c][r] = mat.matrix[r][c];
			}
		}

		return toRet;
	}


	/**
	 * Given a U matrix and knowing where from the original matrix we got the submatrix from, we can make a Givens matrix.
	 * 
	 * @param eigenvectorMatrix the matrix that contains the eigenvectors
	 * @param row the row at which the largest off diagonal is located
	 * @param column the column at which the largest off diagonal is located
	 * @return a Givens matrix
	 */
	public Matrix getGivensMatrix(Matrix eigenVectorMatrix, int row, int column) {
		Matrix toRet = new Matrix(rows, columns);

		for (int i = 0; i < rows; i++) {
			toRet.matrix[i][i] = 1;
		}

		toRet.matrix[row][row] = eigenVectorMatrix.matrix[0][0];
		toRet.matrix[column][row] = eigenVectorMatrix.matrix[1][0];
		toRet.matrix[row][column] = eigenVectorMatrix.matrix[0][1];
		toRet.matrix[column][column] = eigenVectorMatrix.matrix[1][1];

		return toRet;


	}

	/**
	 * Performs matrix multiplication on two matrices.
	 * 
	 * @param mat1
	 * @param mat2
	 * @return product of mat1 and mat2
	 */
	public Matrix matrixMultiplication(Matrix mat1, Matrix mat2) {

		Matrix toRet = new Matrix(rows, columns);
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				double value = 0;
				for (int i = 0; i < rows; i++) {
					value += mat1.matrix[r][i] * mat2.matrix[i][c];
				}
				toRet.matrix[r][c] = value;
			}
		}

		return toRet;
	}

	/**
	 * Gets the Off value of a matrix 
	 * 
	 * @param mat
	 * @return Off value of mat
	 */
	public double getOff(Matrix mat) {
		
		double value = 0;
		
		for (int r = 0 ; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				if (r != c) {
					value += Math.pow(mat.matrix[r][c], 2);
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Checks for rounding errors and converts them if necessary.
	 */
	public void checkForRoundingErrors() {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				double ceiling = Math.ceil(matrix[r][c]);
				double floor = Math.floor(matrix[r][c]);
				if (ceiling - matrix[r][c] < roundingThreshold) matrix[r][c] = ceiling;
				else if (matrix[r][c] - floor < roundingThreshold) matrix[r][c] = floor;
			}
		}
	}
	
	public String getDiagonals() {
		
		String toRet = "";
		for (int i = 0; i < rows - 1; i++) {
			toRet += matrix[i][i] + ", ";
		}
		
		toRet += matrix[rows-1][rows-1];
		
		return toRet;
	}
	
	public String toString() {
		
		String toRet = "";
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				toRet += matrix[r][c] + "              ";
			}
			toRet += "\n";
		}
		
		return toRet;
	}



}
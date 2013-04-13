import java.util.ArrayList;


public class JacobiAlgorithm {
	
	public static Matrix endMatrix;

	public static ArrayList<Double> runJacobiAlgorithm(Matrix originalMat, boolean sorted) {

		Matrix mat = new Matrix(originalMat.rows, originalMat.columns);
		
		for (int r = 0; r < originalMat.rows; r++) {
			for (int c = 0; c < originalMat.columns; c++) {
				mat.matrix[r][c] = originalMat.matrix[r][c];
			}
		}
		
		
		ArrayList<Double> offValues = new ArrayList<Double>();

		double offValue = -1;
		
		// For unsorted runs
		int row = 0;
		int column = 1;
		int maxRows = mat.rows;
		int maxColumns = mat.columns;

		while (offValue < 0 || offValue > mat.offThreshold) {

			if (sorted) {
				Pair pair = mat.getLargestOffDiagnoal();
				Matrix pairMatrix = mat.getOffDiagonalMatrix(pair.row, pair.column);
				Matrix eigenVectorMatrix = mat.getEigenvectors(pairMatrix);
				Matrix givensMatrix = mat.getGivensMatrix(eigenVectorMatrix, pair.row, pair.column);

				// G-transposed A G
				mat = mat.matrixMultiplication(mat.matrixMultiplication(mat.getTranspose(givensMatrix), mat), givensMatrix);
				mat.checkForRoundingErrors();
				offValue = mat.getOff(mat);
				offValues.add(offValue);
			}
			else {
				Matrix pairMatrix = mat.getOffDiagonalMatrix(row, column);
				Matrix eigenVectorMatrix = mat.getEigenvectors(pairMatrix);
				Matrix givensMatrix = mat.getGivensMatrix(eigenVectorMatrix, row, column);

				// G-transposed A G
				mat = mat.matrixMultiplication(mat.matrixMultiplication(mat.getTranspose(givensMatrix), mat), givensMatrix);
				mat.checkForRoundingErrors();
				offValue = mat.getOff(mat);
				offValues.add(offValue);
				
				column++;
				if (column >= maxColumns) {
					if (row >= maxRows - 2) row = 0;
					else row++;
					column = row + 1;
				}
			}
		}

		endMatrix = mat;
		return offValues;
		

	}

	public static void main(String[] args) {		


		Matrix testMat = new Matrix(5, 5);
		testMat.randomizeMatrix();
		System.out.println("Start Matrix:  " + testMat.toString());
		ArrayList<Double> offValuesSort = JacobiAlgorithm.runJacobiAlgorithm(testMat, true);
		System.out.println("End Matrix (Sort):  " + endMatrix.toString());
		System.out.println("End Matrix (Sort) Diagonals:  " + JacobiAlgorithm.endMatrix.getDiagonals());
		System.out.println("End Matrix (Sort) Off(B) Values:  " + offValuesSort);
		ArrayList<Double> offValuesUnsort = JacobiAlgorithm.runJacobiAlgorithm(testMat, false);
		System.out.println("End Matrix (No sort):  " + endMatrix.toString());
		System.out.println("End Matrix (No sort) Diagonals:  " + JacobiAlgorithm.endMatrix.getDiagonals());
		System.out.println("End Matrix (No sort) Off(B) Values:  " + offValuesUnsort);
		
		
		
	}

}

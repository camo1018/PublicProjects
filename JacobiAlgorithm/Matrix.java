import java.Random;

public class Matrix {

	double[][] matrix;
	int rows;
	int columns;
	double offThreshold = 0.000000010;


	/**
	* Constructs a matrix with the specified dimensions
	* @params r number of rows
	* @params c number of columns
	*//
	public Matrix(double r, double c) {
		matrix = new double[r][c];
		rows = r;
		columns = c;
	}

	private void randomizeMatrix() {
		Random rand = new Random();
		
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				matrix[r][c] = random.nextDouble() * 200 - 100;
			}
		}
	};

	private Pair getLargestOffDiagnoal() {
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

	
	
}

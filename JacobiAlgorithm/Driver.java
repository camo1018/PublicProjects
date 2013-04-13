import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class Driver {

	private static Matrix originalMatrix;
	private static ArrayList<Double> offValues;
	private static Chart mainPanel;
	private static JLabel sortedLabel;
	private static JLabel diagonalsLabel;
	private static boolean isSorted = true;

	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void createAndShowGUI() {
		mainPanel = new Chart(offValues, originalMatrix);

		sortedLabel = new JLabel("Sorted");
		mainPanel.add(sortedLabel);
		
		
		
		JButton button1 = new JButton("Toggle Sorted/Unsorted");
		mainPanel.add(button1);
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isSorted) {
					offValues = JacobiAlgorithm.runJacobiAlgorithm(originalMatrix, false);
					mainPanel.changePlots(offValues);
					mainPanel.repaint();
					sortedLabel.setText("Unsorted");
					diagonalsLabel.setText("Diagonal values:  " + JacobiAlgorithm.endMatrix.getDiagonals());
					isSorted = false;
				}
				else {
					offValues = JacobiAlgorithm.runJacobiAlgorithm(originalMatrix, true);
					mainPanel.changePlots(offValues);
					mainPanel.repaint();
					sortedLabel.setText("Sorted");
					diagonalsLabel.setText("Diagonal values:  " + JacobiAlgorithm.endMatrix.getDiagonals());
					isSorted = true;
				}
			}
		});

		JButton button2 = new JButton("New Matrix");
		mainPanel.add(button2);
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Matrix mat = new Matrix(5, 5);
				mat.randomizeMatrix();
				originalMatrix = mat;
				offValues = JacobiAlgorithm.runJacobiAlgorithm(mat, isSorted);
				mainPanel.changePlots(offValues, mat);
				mainPanel.repaint();
				diagonalsLabel.setText("Diagonal values:  " + JacobiAlgorithm.endMatrix.getDiagonals());
				JOptionPane.showMessageDialog(null, "Original Matrix\n" + originalMatrix.toString());
			}
		});
		
		diagonalsLabel = new JLabel("Diagonal values:  " + JacobiAlgorithm.endMatrix.getDiagonals());
		mainPanel.add(diagonalsLabel);
		
		JFrame frame = new JFrame("Jacobi's Algorithm by Paul Park");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);		
		
		JOptionPane.showMessageDialog(null, "Original Matrix\n" + originalMatrix.toString());
		
	}

	public static void main(String[] args) {

		Matrix mat = new Matrix(5, 5);
		mat.randomizeMatrix();
		System.out.println(mat.getOff(mat));
		originalMatrix = mat;
		System.out.println(mat.toString());
		offValues = JacobiAlgorithm.runJacobiAlgorithm(mat, true);

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				createAndShowGUI();
			}
		});
	}

}

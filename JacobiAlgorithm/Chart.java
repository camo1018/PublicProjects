import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Chart extends JPanel {

	private static final int preferredWidth = 800;
	private static final int preferredHeight = 600;
	private static final int gapBorder = 30;

	private static final Color chartColor = Color.blue;
	private static final Color plotColor = Color.black;
	private static final Stroke chartStroke = new BasicStroke(3f);
	private static final int plotWidth = 12;
	//private static final int hatchMarkCount = 10;

	private double maxYValue = 0;

	private Matrix matrix;

	private double off;

	private ArrayList<Double> plots;

	public Chart(ArrayList<Double> givenPlots, Matrix mat) {
		plots = givenPlots;
		matrix = mat;
		off = matrix.getOff(matrix);
		maxYValue = Math.log(off) * 2;
	}

	public void changePlots(ArrayList<Double> givenPlots) {
		plots = givenPlots;
	}

	public void changePlots(ArrayList<Double> givenPlots, Matrix newMat) {
		plots = givenPlots;
		matrix = newMat;
		off = matrix.getOff(matrix);
		maxYValue = Math.log(off) * 2;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double xScale = ((double) getWidth() - 2 * gapBorder) / (plots.size() - 1);
		double yScale = ((double) getHeight()/2 - 2 * gapBorder) / (maxYValue - 1);

		ArrayList<Point> chartPoints = new ArrayList<Point>();
		for (int i = 0; i < plots.size(); i++) {
			int xTemp = (int) (xScale * i + gapBorder);
			int yTemp = (int) ((maxYValue - Math.log(plots.get(i))) * yScale + gapBorder); 
			chartPoints.add(new Point(xTemp, yTemp));
		}

		g2.drawLine(gapBorder, getHeight() - gapBorder, gapBorder, gapBorder);
		g2.drawLine(gapBorder, getHeight()/2 - gapBorder, getWidth() - gapBorder, getHeight()/2 - gapBorder);

		// X-axis
		for (int i = 0; i < plots.size() - 1; i++) {
			int x1 = (i + 1) * (getWidth() - (gapBorder * 2)) / (plots.size() - 1) + gapBorder;
			int y1 = getHeight()/2 - gapBorder + plotWidth;
			int x2 = x1;
			int y2 = y1 - 2*plotWidth;

			g2.drawLine(x1, y1, x2, y2);
		}

		/**
		// Y-axis
		for (int i = 0; i < hatchMarkCount; i++) {
			int x1 = gapBorder;
			int y1 = getHeight() - (((i + 1) * (getHeight() - gapBorder * 2)) / hatchMarkCount + gapBorder);
			int x2 = plotWidth + gapBorder;
			int y2 = y1;

			g2.drawLine(x1, y1, x2, y2);
		}
		*/

		// Set up strokes for chart
		Stroke defaultStroke = g2.getStroke();
		g2.setColor(chartColor);
		g2.setStroke(chartStroke);


		// draw points and lines
		for (int i = 0; i < chartPoints.size() - 1; i++) {
			int x1 = chartPoints.get(i).x;
			int y1 = chartPoints.get(i).y;
			int x2 = chartPoints.get(i + 1).x;
			int y2 = chartPoints.get(i + 1).y;
			g2.drawLine(x1, y1, x2, y2);
		}

		// Draw the theoretical bound
		double x1 = chartPoints.get(0).x;
		double y1 = (int) ((maxYValue - Math.log(off)) * yScale + gapBorder); 
		double x2 = chartPoints.get(chartPoints.size()-1).x;
		double y2 = (int) (((maxYValue - (Math.log(off) + (chartPoints.size() * Math.log(9.0/10.0)))) * yScale + gapBorder));
		g2.setColor(Color.red);
		g2.drawLine((int) x1, (int)Math.round(y1), (int)x2, (int)Math.round(y2));


		// Draw the plot points
		g2.setStroke(defaultStroke);
		g2.setColor(plotColor);
		for (int i = 0; i < chartPoints.size(); i++) {
			int x = chartPoints.get(i).x - plotWidth / 2;
			int y = chartPoints.get(i).y - plotWidth / 2;
			int width = plotWidth;
			int height = plotWidth;
			g2.fillOval(x, y, width, height);
		}



	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(preferredWidth, preferredHeight);
	}

}

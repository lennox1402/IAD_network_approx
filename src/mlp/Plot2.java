package mlp;


import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


public class Plot2 extends ApplicationFrame {

    private XYSeries series1 = new XYSeries("Function");
    private XYSeries series2 = new XYSeries("Approximation");
    //private XYSeries series3 = new XYSeries("Classifying line");
    //private final int HORIZONTAL_SIZE = 3;  //bo sa trzy kolumny w pliku

    public Plot2(String title, double[][] data, double[][] data1) {
        super(title);

        for (int i = 0; i < data.length; i++) {

            series1.add(data[i][0], data[i][1]);
        }

        for (int i = 0; i < data1.length; i++) {

            series2.add(data1[i][0], data1[i][1]);
        }

        JPanel panel = createDemoPanel();
        panel.setPreferredSize(new java.awt.Dimension(600, 300));
        setContentPane(panel);
    }

    private JFreeChart createChart() {

        XYDataset data = createDataset1();
        XYItemRenderer renderer1 = new XYLineAndShapeRenderer(true, false);

        ValueAxis domainAxis = new NumberAxis("Number of epochs");
        ValueAxis rangeAxis = new NumberAxis("Mean squared error");
        //domainAxis.setRange(0, 10000);
        //rangeAxis.setRange(0, 5);
        XYPlot plot = new XYPlot(data, domainAxis, rangeAxis, renderer1);

        // add a second dataset and renderer...
        XYDataset data2 = createDataset2();
        XYItemRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
        plot.setDataset(1, data2);
        plot.setRenderer(1, renderer2);

        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        // return a new chart containing the overlaid plot...
        JFreeChart chart = new JFreeChart("",
                JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        ChartUtilities.applyCurrentTheme(chart);
        return chart;
    }

    private XYDataset createDataset1() {

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);

        return dataset;
    }

    private XYDataset createDataset2() {

        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(series2);

        return dataset2;
    }

    public JPanel createDemoPanel() {
        JFreeChart chart = createChart();
        return new ChartPanel(chart);
    }

    public static void main(String[] args) {

    }

}

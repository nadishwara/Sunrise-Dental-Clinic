/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.forms;

import DAO.AnalyticsDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author nadis
 */
public class AnalyticsForm extends javax.swing.JPanel {

    /**
     * Creates new form AnalyticsForm
     */
    private final AnalyticsDAO analyticsDAO;

    public AnalyticsForm() {
        initComponents();
        this.analyticsDAO = new AnalyticsDAO();

        setLayout(new BorderLayout());
        initDashboardCharts();
    }

    public final void initDashboardCharts() {
        JPanel chartContainer = new JPanel(new GridLayout(0, 2, 15, 15));
        chartContainer.setBackground(new Color(245, 245, 250));

        chartContainer.setBorder(BorderFactory.createEmptyBorder(40, 20, 20, 25));

        ChartPanel p1 = configureChartPanel(createPatientsTodayPieChart());
        ChartPanel p2 = configureChartPanel(createWeeklyPatientsBarChart());
        ChartPanel p3 = configureChartPanel(createTreatmentDistributionChart());

        chartContainer.add(p1);
        chartContainer.add(p2);
        chartContainer.add(p3);

        JPanel scrollContent = new JPanel(new BorderLayout());
        scrollContent.setBackground(new Color(245, 245, 250));
        scrollContent.add(chartContainer, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        removeAll();
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private ChartPanel configureChartPanel(JFreeChart chart) {
        chart.getTitle().setFont(new Font("Segoe UI", Font.BOLD, 14));
        if (chart.getLegend() != null) {
            chart.getLegend().setItemFont(new Font("Segoe UI", Font.PLAIN, 11));
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(false);

        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMaximumDrawWidth(2000);
        chartPanel.setMaximumDrawHeight(2000);

        chartPanel.setPreferredSize(new Dimension(340, 250));
        return chartPanel;
    }

    private JFreeChart createPatientsTodayPieChart() {
        int todayCount = analyticsDAO.getTotalPatientsToday();

        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Patients Today (" + todayCount + ")", todayCount);

        int remainingCapacity = Math.max(0, 20 - todayCount);
        dataset.setValue("Remaining Capacity", remainingCapacity);

        JFreeChart chart = ChartFactory.createPieChart(
                "Total Patients Today",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setSectionPaint("Patients Today (" + todayCount + ")", new Color(0, 153, 255));
        plot.setSectionPaint("Remaining Capacity", new Color(220, 220, 220));
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        return chart;
    }

    private JFreeChart createWeeklyPatientsBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> weeklyData = analyticsDAO.getWeeklyPatientCount();
        if (weeklyData != null) {
            for (Map.Entry<String, Integer> entry : weeklyData.entrySet()) {
                dataset.addValue(entry.getValue(), "Patients", entry.getKey());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Weekly Patient Traffic",
                "Day",
                "Count",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(46, 204, 113));

        return chart;
    }

    private JFreeChart createTreatmentDistributionChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();

        Map<String, Integer> treatmentData = analyticsDAO.getTreatmentTypeDistribution();
        if (treatmentData != null) {
            for (Map.Entry<String, Integer> entry : treatmentData.entrySet()) {
                String key = entry.getKey();
                if (key == null || key.trim().isEmpty()) {
                    key = "Unspecified / Unknown";
                }
                Integer value = entry.getValue();
                if (value != null) {
                    dataset.setValue(key, value);
                }
            }
        }

        JFreeChart chart = ChartFactory.createPieChart(
                "Treatment Type Breakdown",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 10));

        return chart;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1013, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package view.forms;

import DAO.AnalyticsDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.JPanel;
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
    JPanel chartContainer = new JPanel(new java.awt.GridLayout(0, 2, 12, 12));
    chartContainer.setBackground(new Color(245, 245, 250));

    ChartPanel p1 = (ChartPanel) createPatientsTodayPieChart();
    ChartPanel p2 = (ChartPanel) createWeeklyPatientsBarChart();
    ChartPanel p3 = (ChartPanel) createTreatmentDistributionChart();

    p1.setMouseWheelEnabled(false);
    p2.setMouseWheelEnabled(false);
    p3.setMouseWheelEnabled(false);

    java.awt.Dimension compactChartSize = new java.awt.Dimension(360, 320);
    p1.setPreferredSize(compactChartSize);
    p2.setPreferredSize(compactChartSize);
    p3.setPreferredSize(compactChartSize);

    chartContainer.add(p1);
    chartContainer.add(p2);
    chartContainer.add(p3);

    JPanel scrollContent = new JPanel(new java.awt.BorderLayout());
    scrollContent.setBackground(new Color(245, 245, 250));
    scrollContent.add(chartContainer, java.awt.BorderLayout.NORTH);

    javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(scrollContent);
    
    scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    scrollPane.getVerticalScrollBar().setUnitIncrement(22);
    scrollPane.setBorder(null);

    removeAll();
    setLayout(new java.awt.BorderLayout());
    add(scrollPane, java.awt.BorderLayout.CENTER);

    revalidate();
    repaint();
}

    private JPanel createPatientsTodayPieChart() {
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
        plot.setLabelFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

        return new ChartPanel(chart);
    }

    private JPanel createWeeklyPatientsBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> weeklyData = analyticsDAO.getWeeklyPatientCount();
        for (Map.Entry<String, Integer> entry : weeklyData.entrySet()) {
            dataset.addValue(entry.getValue(), "Patients", entry.getKey());
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Weekly Patient Traffic",
                "Day of Week",
                "Patient Count",
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

        return new ChartPanel(chart);
    }

    private JPanel createTreatmentDistributionChart() {
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

        return new ChartPanel(chart);
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

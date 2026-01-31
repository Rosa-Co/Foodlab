package progetto.app.view;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import progetto.app.controller.AppController;

import progetto.app.dto.ChefStatsDTO;
import java.awt.Color;

public class ReportViewGUI {

    @FXML
    private Label totalCoursesLabel;
    @FXML
    private Label onlineSessionsLabel;
    @FXML
    private Label presenceSessionsLabel;
    @FXML
    private Label avgRecipesLabel;
    @FXML
    private Label maxRecipesLabel;
    @FXML
    private Label minRecipesLabel;
    @FXML
    private BorderPane chartContainer;

    private final AppController appController = AppController.getInstance();

    @FXML
    public void initialize() {
        loadData();
    }

    public void loadData() {
        new Thread(() -> {
            ChefStatsDTO stats = appController.getChefReportData();
            Platform.runLater(() -> updateUI(stats));
        }).start();
    }

    private void updateUI(ChefStatsDTO stats) {
        if (stats == null)
            return;

        totalCoursesLabel.setText(String.valueOf(stats.getTotalCourses()));

        int online = stats.getOnlineSessions();
        int presence = stats.getPresenceSessions();
        onlineSessionsLabel.setText(String.valueOf(online));
        presenceSessionsLabel.setText(String.valueOf(presence));

        avgRecipesLabel.setText(String.format("%.1f", stats.getAvgRecipes()));

        maxRecipesLabel.setText(String.valueOf(stats.getMaxRecipes()));
        minRecipesLabel.setText(String.valueOf(stats.getMinRecipes()));

        createChart(online, presence);
    }

    @SuppressWarnings("unchecked")
    private void createChart(int online, int presence) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Online", online);
        dataset.setValue("In Presenza", presence);

        JFreeChart chart = ChartFactory.createPieChart(
                "Distribuzione Sessioni",
                dataset,
                true,
                true,
                false);

        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setSectionPaint("Online", new Color(52, 152, 219)); // Blue
        plot.setSectionPaint("In Presenza", new Color(230, 126, 34)); // Orange
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        ChartViewer viewer = new ChartViewer(chart);
        chartContainer.setCenter(viewer);
    }
}

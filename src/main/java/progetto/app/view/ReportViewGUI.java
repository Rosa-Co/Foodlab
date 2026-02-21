package progetto.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import progetto.app.controller.AppController;
import progetto.app.dto.ChefStatsDTO;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Controller della schermata del report mensile dello chef.
 * <p>
 * Mostra le statistiche aggregate (corsi, sessioni, ricette) sotto forma
 * di label e un grafico a torta (JFreeChart) che confronta sessioni online
 * e in presenza.
 * </p>
 */
public class ReportViewGUI {

    /** Label che mostra il numero totale di corsi. */
    @FXML
    private Label totalCoursesLabel;
    /** Label che mostra il numero di sessioni online. */
    @FXML
    private Label onlineSessionsLabel;
    /** Label che mostra il numero di sessioni in presenza. */
    @FXML
    private Label presenceSessionsLabel;
    /** Label che mostra la media di ricette per sessione in presenza. */
    @FXML
    private Label avgRecipesLabel;
    /**
     * Label che mostra il numero massimo di ricette in una sessione in presenza.
     */
    @FXML
    private Label maxRecipesLabel;
    /** Label che mostra il numero minimo di ricette in una sessione in presenza. */
    @FXML
    private Label minRecipesLabel;
    /** Contenitore in cui viene inserito il grafico JFreeChart. */
    @FXML
    private BorderPane chartContainer;

    private final AppController appController = AppController.getInstance();

    /** Carica i dati al momento dell'inizializzazione della view. */
    @FXML
    public void initialize() {
        loadData();
    }

    /**
     * Recupera le statistiche su un thread in background e aggiorna la UI
     * nel thread JavaFX.
     */
    public void loadData() {
        new Thread(() -> {
            ChefStatsDTO stats = appController.getChefReportData();
            Platform.runLater(() -> updateUI(stats));
        }).start();
    }

    /**
     * Aggiorna tutte le label e ricostruisce il grafico a torta.
     *
     * @param stats statistiche dello chef; se {@code null} non fa nulla
     */
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

    /**
     * Crea e inserisce nel layout un grafico a torta che mostra la distribuzione
     * tra sessioni online e in presenza.
     *
     * @param online   numero di sessioni online
     * @param presence numero di sessioni in presenza
     */
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
        plot.setSectionPaint("Online", new Color(52, 152, 219)); // Blu
        plot.setSectionPaint("In Presenza", new Color(230, 126, 34)); // Arancione
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        plot.setLabelGenerator(
                new StandardPieSectionLabelGenerator(
                        "{0}: {2}",              // nome + percentuale
                        new DecimalFormat("0"),  // valore assoluto
                        new DecimalFormat("0.0%")// formato percentuale
                )
        );

        ChartViewer viewer = new ChartViewer(chart);
        chartContainer.setCenter(viewer);
    }
}

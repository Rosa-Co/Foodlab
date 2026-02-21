package progetto.app.dto;

/**
 * DTO che raccoglie le statistiche aggregate di uno chef.
 * <p>
 * Viene popolato da {@link progetto.app.dao.Interface.StatsDAO} e passato
 * alla GUI per visualizzare il report mensile. I valori di ricette
 * ({@code avgRecipes}, {@code maxRecipes}, {@code minRecipes}) fanno
 * riferimento esclusivamente alle sessioni in presenza.
 * </p>
 */
public class ChefStatsDTO {
    /** Numero totale di corsi creati dallo chef. */
    private int totalCourses;
    /** Numero di sessioni con modalità {@code Online}. */
    private int onlineSessions;
    /** Numero di sessioni con modalità {@code In Presenza}. */
    private int presenceSessions;
    /** Media del numero di ricette per sessione in presenza. */
    private double avgRecipes;
    /** Valore massimo di ricette in una sessione in presenza. */
    private int maxRecipes;
    /** Valore minimo di ricette in una sessione in presenza. */
    private int minRecipes;

    /**
     * Costruisce un {@code ChefStatsDTO} con tutti i campi a zero.
     */
    public ChefStatsDTO() {
    }

    /**
     * Costruisce un {@code ChefStatsDTO} con tutti i campi esplicitati.
     *
     * @param totalCourses     numero totale di corsi
     * @param onlineSessions   numero di sessioni online
     * @param presenceSessions numero di sessioni in presenza
     * @param avgRecipes       media di ricette per sessione in presenza
     * @param maxRecipes       massimo numero di ricette in una sessione in presenza
     * @param minRecipes       minimo numero di ricette in una sessione in presenza
     */
    public ChefStatsDTO(int totalCourses, int onlineSessions, int presenceSessions, double avgRecipes, int maxRecipes,
            int minRecipes) {
        this.totalCourses = totalCourses;
        this.onlineSessions = onlineSessions;
        this.presenceSessions = presenceSessions;
        this.avgRecipes = avgRecipes;
        this.maxRecipes = maxRecipes;
        this.minRecipes = minRecipes;
    }

    /** @return numero totale di corsi */
    public int getTotalCourses() {
        return totalCourses;
    }

    /** @param totalCourses numero totale di corsi */
    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
    }

    /** @return numero di sessioni online */
    public int getOnlineSessions() {
        return onlineSessions;
    }

    /** @param onlineSessions numero di sessioni online */
    public void setOnlineSessions(int onlineSessions) {
        this.onlineSessions = onlineSessions;
    }

    /** @return numero di sessioni in presenza */
    public int getPresenceSessions() {
        return presenceSessions;
    }

    /** @param presenceSessions numero di sessioni in presenza */
    public void setPresenceSessions(int presenceSessions) {
        this.presenceSessions = presenceSessions;
    }

    /** @return media del numero di ricette per sessione in presenza */
    public double getAvgRecipes() {
        return avgRecipes;
    }

    /** @param avgRecipes media del numero di ricette per sessione in presenza */
    public void setAvgRecipes(double avgRecipes) {
        this.avgRecipes = avgRecipes;
    }

    /** @return massimo numero di ricette in una singola sessione in presenza */
    public int getMaxRecipes() {
        return maxRecipes;
    }

    /**
     * @param maxRecipes massimo numero di ricette in una singola sessione in
     *                   presenza
     */
    public void setMaxRecipes(int maxRecipes) {
        this.maxRecipes = maxRecipes;
    }

    /** @return minimo numero di ricette in una singola sessione in presenza */
    public int getMinRecipes() {
        return minRecipes;
    }

    /**
     * @param minRecipes minimo numero di ricette in una singola sessione in
     *                   presenza
     */
    public void setMinRecipes(int minRecipes) {
        this.minRecipes = minRecipes;
    }
}

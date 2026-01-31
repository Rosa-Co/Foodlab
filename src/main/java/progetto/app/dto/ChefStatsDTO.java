package progetto.app.dto;

public class ChefStatsDTO {
    private int totalCourses;
    private int onlineSessions;
    private int presenceSessions;
    private double avgRecipes;
    private int maxRecipes;
    private int minRecipes;

    public ChefStatsDTO() {
    }

    public ChefStatsDTO(int totalCourses, int onlineSessions, int presenceSessions, double avgRecipes, int maxRecipes,
            int minRecipes) {
        this.totalCourses = totalCourses;
        this.onlineSessions = onlineSessions;
        this.presenceSessions = presenceSessions;
        this.avgRecipes = avgRecipes;
        this.maxRecipes = maxRecipes;
        this.minRecipes = minRecipes;
    }

    public int getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
    }

    public int getOnlineSessions() {
        return onlineSessions;
    }

    public void setOnlineSessions(int onlineSessions) {
        this.onlineSessions = onlineSessions;
    }

    public int getPresenceSessions() {
        return presenceSessions;
    }

    public void setPresenceSessions(int presenceSessions) {
        this.presenceSessions = presenceSessions;
    }

    public double getAvgRecipes() {
        return avgRecipes;
    }

    public void setAvgRecipes(double avgRecipes) {
        this.avgRecipes = avgRecipes;
    }

    public int getMaxRecipes() {
        return maxRecipes;
    }

    public void setMaxRecipes(int maxRecipes) {
        this.maxRecipes = maxRecipes;
    }

    public int getMinRecipes() {
        return minRecipes;
    }

    public void setMinRecipes(int minRecipes) {
        this.minRecipes = minRecipes;
    }
}

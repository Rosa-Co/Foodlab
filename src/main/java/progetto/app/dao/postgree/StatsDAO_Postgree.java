package progetto.app.dao.postgree;

import progetto.app.dao.Interface.StatsDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.dto.ChefStatsDTO;
import progetto.app.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatsDAO_Postgree implements StatsDAO {

    @Override
    public ChefStatsDTO getChefStats(int chefId) throws DAOException {
        ChefStatsDTO stats = new ChefStatsDTO();

        try (Connection con = DatabaseConnection.getConnection()) {

            // 1. Total Courses
            String coursesSql = "SELECT COUNT(*) FROM corso WHERE chef_id = ?";
            try (PreparedStatement ps = con.prepareStatement(coursesSql)) {
                ps.setInt(1, chefId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        stats.setTotalCourses(rs.getInt(1));
                    }
                }
            }

            // 2. Sessions (Online vs In Presenza)
            String sessionsSql = "SELECT s.modalita, COUNT(*) " +
                    "FROM sessione s " +
                    "JOIN corso c ON s.corso_id = c.id " +
                    "WHERE c.chef_id = ? " +
                    "GROUP BY s.modalita";

            try (PreparedStatement ps = con.prepareStatement(sessionsSql)) {
                ps.setInt(1, chefId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String mod = rs.getString(1);
                        int count = rs.getInt(2);
                        if ("Online".equalsIgnoreCase(mod)) {
                            stats.setOnlineSessions(count);
                        } else if ("In Presenza".equalsIgnoreCase(mod)) {
                            stats.setPresenceSessions(count);
                        }
                    }
                }
            }

            // 3. Recipe Stats for "In Presenza" sessions
            String recipeStatsSql = "WITH RecipeCounts AS ( " +
                    "    SELECT s.id, COUNT(sr.ricetta_id) as num_recipes " +
                    "    FROM sessione s " +
                    "    JOIN corso c ON s.corso_id = c.id " +
                    "    LEFT JOIN sessione_ricetta sr ON s.id = sr.sessione_id " +
                    "    WHERE c.chef_id = ? AND s.modalita = 'In Presenza' " +
                    "    GROUP BY s.id " +
                    ") " +
                    "SELECT AVG(num_recipes), MAX(num_recipes), MIN(num_recipes) FROM RecipeCounts";

            try (PreparedStatement ps = con.prepareStatement(recipeStatsSql)) {
                ps.setInt(1, chefId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double avg = rs.getDouble(1);
                        if (!rs.wasNull()) {
                            stats.setAvgRecipes(avg);
                        }

                        int max = rs.getInt(2);
                        if (!rs.wasNull())
                            stats.setMaxRecipes(max);

                        int min = rs.getInt(3);
                        if (!rs.wasNull())
                            stats.setMinRecipes(min);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Errore nel recupero delle statistiche: " + e.getMessage());
        }
        return stats;
    }
}

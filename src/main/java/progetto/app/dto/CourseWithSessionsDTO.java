package progetto.app.dto;

import java.util.List;

/**
 * DTO composito che associa un corso alla lista delle sue sessioni.
 * <p>
 * Utilizzato tipicamente quando si devono trasferire insieme le informazioni
 * di un corso e tutte le sessioni ad esso collegate (es. durante la
 * creazione di un corso con le sessioni pianificate).
 * Entrambi i campi sono finali e non modificabili dopo la costruzione.
 * </p>
 */
public class CourseWithSessionsDTO {
    /** Il corso di riferimento. */
    private final CourseDTO course;
    /** La lista delle sessioni associate al corso. */
    private final List<SessionDTO> sessions;

    /**
     * Crea un {@code CourseWithSessionsDTO} associando un corso alle sue sessioni.
     *
     * @param course   il {@link CourseDTO} del corso
     * @param sessions la lista di {@link SessionDTO} delle sessioni pianificate
     */
    public CourseWithSessionsDTO(CourseDTO course, List<SessionDTO> sessions) {
        this.course = course;
        this.sessions = sessions;
    }

    /** @return il {@link CourseDTO} del corso */
    public CourseDTO getCourse() {
        return course;
    }

    /** @return la lista di {@link SessionDTO} delle sessioni del corso */
    public List<SessionDTO> getSessions() {
        return sessions;
    }
}

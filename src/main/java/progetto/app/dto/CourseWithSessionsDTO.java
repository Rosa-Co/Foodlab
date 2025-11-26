package progetto.app.dto;

import java.util.List;

public class CourseWithSessionsDTO {
    private final CourseDTO course;
    private final List<SessionDTO> sessions;

    public CourseWithSessionsDTO(CourseDTO course, List<SessionDTO> sessions) {
        this.course = course;
        this.sessions = sessions;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public List<SessionDTO> getSessions() {
        return sessions;
    }
}

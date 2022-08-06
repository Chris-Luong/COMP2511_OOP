package unsw.enrolment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Student {
    
    private String zid;
    private ArrayList<Enrolment> enrolments = new ArrayList<Enrolment>();
    private String name;
    private int program;
    private String[] streams;
    
	public Student(String zid, String name, int program, String[] streams) {
        this.zid = zid;
        this.name = name;
        this.program = program;
        this.streams = streams;
    }
    
    public String getZid() {
        return zid;
    }
    
    public List<Enrolment> getEnrolments() {
        return enrolments;
    }
    public String getName() {
        return name;
    }

    public int getProgram() {
        return program;
    }

    public int getStreamLength() {
        return streams.length;
    }
    
    public void setEnrolment(Enrolment enrolment) {
        enrolments.add(enrolment);
    }

    /**
     * Sets the grade by finding the matching enrolment offering to the grade
     * given and adds the grade to the enrolment
     * @param grade
     */    
    public void setGrade(Grade grade) {
        enrolments.stream()
                  .filter(e -> e.getOffering().equals(grade.getOffering()))
                  .forEach(e -> e.setGrade(grade));
    }

    /**
     * Checks if a student is enrolled by checking if
     * the offering is in the list of enrolments
     * @param offering
     * @return boolean
     */
    public boolean isEnrolled(CourseOffering offering) {
        Optional<Enrolment> enrolled =
        enrolments.stream()
                   .filter(e -> e.getOffering().equals(offering))
                   .findFirst();
        return enrolled.isPresent();
    }

    /**
     * Checks if the prerequisite course has been done and passed
     * by finding it in the list of enrolments
     * @param prereq
     * @return boolean
     */
    public boolean isValidEnrolment(Course prereq) {
        return enrolments.stream()
                         .filter(e -> (e.getCourse().equals(prereq) && e.getGrade() != null))
                         .anyMatch(e -> e.hasPassedCourse());
    }
}

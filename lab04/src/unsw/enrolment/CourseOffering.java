package unsw.enrolment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

public class CourseOffering {

    private Course course;
    private String courseCode;
    private String term;
    private List<Enrolment> enrolments = new ArrayList<Enrolment>();

    public CourseOffering(Course course, String term) {
        // super(course.getCourseCode(), course.getTitle());
        this.course = course;
        this.courseCode = course.getCourseCode();
        this.term = term;
        this.course.addOffering(this);
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public List<Course> getCoursePrereqs() {
        return course.getPrereqs();
    }

    public String getTerm() {
        return term;
    }

    /**
     * Adds enrolment to list of all enrolments and to student's list of enrolments
     * @param student
     * @return Enrolment
     * @throws InvalidEnrolmentException
     */
    public Enrolment addEnrolment(Student student) throws InvalidEnrolmentException {
        if (checkValidEnrolment(student)) {
            Enrolment enrolment = new Enrolment(this, student);
            enrolments.add(enrolment);
            student.setEnrolment(enrolment);
            return enrolment;
        } else {
            throw new InvalidEnrolmentException("student has not satisfied the prerequisites");
        }
    }

    private boolean checkValidEnrolment(Student student) {
        return course.isValidEnrolment(student);
    }

    /**
     * Gets the students enrolled in a course and sorts with a custom comparator where
     * students are sorted by their program, then the number of streams they are enrolled
     * in (ascending order), their name and their zID.
     * @return List<Student>
     */
    public List<Student> studentsEnrolledInCourse() {
        List<Student> students = enrolments.stream()
                                           .map(Enrolment::getStudent).collect(Collectors.toList());
        Collections.sort(students, Comparator.comparingInt(Student::getProgram)
                   .thenComparingInt(Student::getStreamLength)
                   .thenComparing(Student::getName)
                   .thenComparing(Student::getZid));
        return students;
    }

}
package staff;

import java.time.LocalDate;

/**
 * A lecturer
 * @author Chris
 *
 */
public class Lecturer extends StaffMember {
    // public static enum Type {
    //     A,
    //     B,
    //     C
    // }
    private String school;
    private String status;


    public Lecturer(String name, int salary, LocalDate hireDate, LocalDate endDate, String school, String status) {
        super(name, salary, hireDate, endDate);
        this.school = school;
        this.status = status;
    }

    /**
     * Returns the school of the lecturer
     */
    public String getSchool() {
        return this.school;
    }

    /**
     * Returns the academic status of the lecturer
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Setter for school
     */
    public void setSchool(String school) {
        if (school.length() < 100) {
            this.school = school;            
        }
    }

    /**
     * Setter for status A for an Associate Lecturer, B  for a Lecturer, and C for a Senior Lecturer
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return super.toString() + ", school: " + this.school + ", academic status: " + this.status;
    }

    @Override
    public boolean equals(Object obj) {

        // Comparing object with itself
        if (obj == this) {
            return true;
        }

        // Check if object is an instance of Lecturer, if null or not an instance: return false
        if (obj == null || !(obj instanceof Lecturer)) {
            return false;
        }

        Lecturer lec = (Lecturer) obj;

        return super.equals(obj) && this.school.equals(lec.school)
                && this.status.equals(lec.status);
    }
    public static void main(String[] args) {

    }
}
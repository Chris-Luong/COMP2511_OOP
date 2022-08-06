package staff;

import java.time.LocalDate;

/**
 * A staff member
 * @author Robert Clifton-Everest
 *
 */
public class StaffMember {

    private String name;
    private int salary;
    private LocalDate hireDate;
    private LocalDate endDate;

    /**
     * Constructor for StaffMember
     * @param name
     * @param salary
     * @param hireDate
     * @param endDate
     */
    public StaffMember(String name, int salary, LocalDate hireDate, LocalDate endDate) {
        this.name = name;
        this.salary = salary;
        this.hireDate = hireDate;
        this.endDate = endDate;
    }

    /**
     * Returns the name of the staff member
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the salary of the staff member
     */
    public int getSalary() {
        return this.salary;
    }

    /**
     * Returns the hire date of the staff member
     */
    public LocalDate getHireDate() {
        return this.hireDate;
    }

    /**
     * Returns the end date of the staff member
     */
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**
     * Setter for name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for salary
     */
    public void setSalary(int salary) {
        this.salary = salary;
    }

    /**
     * Setter for endDate
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return getClass().getName() + ", name: " + this.name + ", salary: " + this.salary
        + ", hire date: " + this.hireDate + ", end date: " + this.endDate;
    }

    @Override
    public boolean equals(Object obj) {
        // Comparing object with itself
        if (obj == this) {
            return true;
        }

        // Check if object is an instance of StaffMember, if null or not staff: return false
        if (obj == null || !(obj instanceof StaffMember)) {
            return false;
        }

        StaffMember s1 = (StaffMember) obj;

        return this.name.compareTo(s1.name) == 0 && this.salary == s1.salary &&
                this.hireDate.compareTo(s1.hireDate) == 0 && this.endDate.compareTo(s1.endDate) == 0;
    }

    public static void main(String[] args) {
        
    }
}

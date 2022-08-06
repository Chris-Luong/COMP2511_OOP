package unsw.enrolment;

public class Grade {

    private int mark;
    private String status;
    private CourseOffering offering;

    public Grade(CourseOffering offering, int mark, String status) {
        this.mark = mark;
        this.status = status;
        this.offering = offering;
    }

    /**
     * Getter for mark (0-100)
     * @return int
     */
	public int getMark() {
		return mark;
	}

    /**
     * Getter for status (FL, UF or PS)
     * @return String
     */
	public String getStatus() {
		return status;
	}

    public CourseOffering getOffering() {
        return offering;
    }

    public boolean hasPassedCourse() {
        if(status == null) {
            return false;
        }
        return getMark() >= 50 && getStatus() != "FL" && getStatus() != "UF";
    }
}

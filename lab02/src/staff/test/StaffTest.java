package staff.test;

import staff.StaffMember;
import staff.Lecturer;
import java.time.LocalDate;

public class StaffTest {
    
    public static void printStaffDetails(StaffMember s1) {
            System.out.println(s1.toString());
    }

    public static void main(String[] args) {
        LocalDate present = LocalDate.now();

        StaffMember s1 = new StaffMember("Tom", 9000, present, present.plusMonths(12));
        Lecturer lec1 = new Lecturer("Jerry", 1, present, present.plusYears(2), "CSE", "A");
        StaffMember lec2 = new Lecturer("Tom", 9000, present, present.plusMonths(12), "ENG", "B");
        StaffMember s2 = new StaffMember("Jerry", 1, present, present.plusYears(2));
        StaffMember sameAsFirst = new StaffMember("Tom", 9000, present, present.plusMonths(12));
        StaffMember anotherSameAsFirst = new StaffMember("Tom", 9000, present, present.plusMonths(12));
        StaffMember similarTos2 = new StaffMember("jerry", 1, present, present.plusYears(2));

        printStaffDetails(s1);
        printStaffDetails(lec1);
        printStaffDetails(lec2);
        printStaffDetails(s2);
        printStaffDetails(sameAsFirst);
        printStaffDetails(similarTos2);
        
        // Reflexive
        if (s1.equals(s1)) {
            System.out.println("Reflexive and true: s1 equals s1");
        } else {
            System.out.println("Failed");
        }
        // Consistent
        if (!(s1.equals(lec1))) {
            System.out.println("False: s1 does not equal lec1");
        } else {
            System.out.println("Failed");
        }
        if (!(s1.equals(lec1))) {
            System.out.println("Consistent and False: s1 does not equal lec1");
        } else {
            System.out.println("Failed");
        }
        // Null is false for non-null values
        if (!(lec2.equals(null))) {
            System.out.println("Null is False for non-null values: Lec2 is not null");
        } else {
            System.out.println("Failed");
        }
        // Symmetric
        if (s1.equals(sameAsFirst) && sameAsFirst.equals(s1)) {
            System.out.println("Symmetric and True: s1 = sameAsFirst and sameAsFirst = s1");
        } else {
            System.out.println("Failed");
        }
        // Transitive
        if (s1.equals(sameAsFirst) && sameAsFirst.equals(anotherSameAsFirst) && s1.equals(anotherSameAsFirst)) {
            System.out.println("Transitive and True: s1 = sameAsFirst = anotherSameAsFirst");
        } else {
            System.out.println("Failed");
        }
    }
}

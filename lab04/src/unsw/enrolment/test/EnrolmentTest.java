package unsw.enrolment.test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.time.DayOfWeek;
import java.time.LocalTime;

import unsw.enrolment.Course;
import unsw.enrolment.CourseOffering;
import unsw.enrolment.Enrolment;
import unsw.enrolment.exceptions.InvalidEnrolmentException;
import unsw.enrolment.Student;
import unsw.enrolment.Grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.junit.jupiter.api.Test;

/**
 * Simple tests for the enrolment system
 * @author Nick Patrikeos + @your name
 */
public class EnrolmentTest {

    private List<Student> parseStudentsCSV(String path) {
        File file = new File(path);
        CSVParser csvParser = null;

        try {
            csvParser = CSVParser.parse(file, Charset.defaultCharset(), CSVFormat.RFC4180);
        } catch (IOException e) {
            return null;
        }

        List<Student> students = new ArrayList<Student>();
        csvParser.forEach(record -> {
            if (record.getRecordNumber() == 1) return;
            students.add(new Student(record.get(0), record.get(1), 
                         Integer.parseInt(record.get(2)), record.get(3).split(" ")));
        });

        return students;
    }

    @Test
    public void testIntegration() {
        // Create courses
        Course cs1511 = new Course("COMP1511", "Programming Fundamentals");
        Course cs1531 = new Course("COMP1531", "Software Engineering Fundamentals");
        cs1531.addPrereq(cs1511);
        Course cs2521 = new Course("COMP2521", "Data Structures and Algorithms");
        cs2521.addPrereq(cs1511);

        CourseOffering cs1511Offering = new CourseOffering(cs1511, "19T1");
        CourseOffering cs1531Offering = new CourseOffering(cs1531, "19T1");
        CourseOffering cs2521Offering = new CourseOffering(cs2521, "19T2");

        // Create a student
        Student student1 = new Student("z5555555", "Jon Snow", 3707, new String[] {"SENGAH"});

        // Enrol the student in COMP1511 for T1 (this should succeed)
        assertDoesNotThrow(() -> {
            cs1511Offering.addEnrolment(student1);
        });
        assertTrue(student1.isEnrolled(cs1511Offering));

        // Enrol the student in COMP1531 for T1 (this should fail as they
        // have not met the prereq)
        assertThrows(InvalidEnrolmentException.class, () -> {
           cs1531Offering.addEnrolment(student1);
        });

        // Give the student a passing grade for COMP1511
        Grade student1comp1511grade = new Grade(cs1511Offering, 98, "HD");
        student1.setGrade(student1comp1511grade);

        // Enrol the student in 2521 & 1531 (this should succeed as they have met
        // the prereqs)
        assertDoesNotThrow(() -> { 
            cs2521Offering.addEnrolment(student1);
            cs1531Offering.addEnrolment(student1);
        });

        assertTrue(student1.isEnrolled(cs2521Offering));
        assertTrue(student1.isEnrolled(cs1531Offering));
    }

    @Test
    public void testComparator() {
        // students is a list of student in alphabetical order for their first names
        List<Student> students = parseStudentsCSV("bin/unsw/enrolment/test/students.csv");

        Course cs1511 = new Course("COMP1511", "Programming Fundamentals");
        CourseOffering cs1511Offering = new CourseOffering(cs1511, "19T1");
        // Enrol all students into the course
        students.stream().forEach(s -> assertDoesNotThrow(() -> {
            cs1511Offering.addEnrolment(s);})
        );

        // Sort using the comparator
        List<Student> ordered = cs1511Offering.studentsEnrolledInCourse();
        // Get the names of list of students (ordered by comparator) and put in another list
        List<String> orderedNames = new ArrayList<String>();
        ordered.stream().forEach(o -> orderedNames.add(o.getName()));

        String[] nameArray = {"Andrew Han", "Carlin Williamson", "Nick Patrikeos", "Noa Challis", "Chloe Cheong",
                             "Esther Wong", "Kaiqi Liang", "Adam Stucci", "Clarence Feng", "Weijie Wang", "Braedon Wooding",
                             "Eddie Qi", "Max Kelly", "An Thy Tran", "Vivian Shen", "Dominic Wong"};
        
        List<String> expected = new ArrayList<String>();
        // Put the array of names of correctly ordered students into a list
        Arrays.stream(nameArray).forEach(n -> expected.add(n));

        // Compare result with expected
        assertEquals(expected, orderedNames);

        // sorted correctly
        // z5204829,Andrew Han,3707,SENGAH
        // z5122521,Carlin Williamson,3707,SENGAH
        // z5169779,Nick Patrikeos,3707,SENGAH
        // z5169766,Noa Challis,3707,SENGAH
        // z5259819,Chloe Cheong,3778,COMPA1
        // z5263737,Esther Wong,3778,COMPA1
        // z5210932,Kaiqi Liang,3778,COMPA1
        // z5157372,Adam Stucci,3781,COMPA1 MATHP1
        // z5260633,Clarence Feng,3782,COMPA1
        // z5260889,Weijie Wang,3782,COMPA1 MATHJ19B
        // z5204996,Braedon Wooding,3782,COMPA1 PHYSC1 PHYSL1
        // z5169811,Eddie Qi,3784,COMPA1
        // z5113139,Max Kelly,3785,COMPA1 MTRNAH9A
        // z5255918,An Thy Tran,3791,COMPA1 DIGMG1
        // z5214750,Vivian Shen,3959,COMPZ1
        // z5208437,Dominic Wong,8543,COMPAS COMPDS
    }

}

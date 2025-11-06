package mthree.com.fullstackschool.service;

import mthree.com.fullstackschool.dao.StudentDao;
import mthree.com.fullstackschool.model.Course;
import mthree.com.fullstackschool.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentServiceInterface {

    //YOUR CODE STARTS HERE

    private StudentDao studentDao;

    /*

    The (required = false) means Spring won’t throw an error
    if it can’t find that dependency during tests.
     */

    @Autowired(required = false)
    private CourseServiceInterface courseService;

    @Autowired
    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    //YOUR CODE ENDS HERE

    public List<Student> getAllStudents() {
        //YOUR CODE STARTS HERE

        return studentDao.getAllStudents();

        //YOUR CODE ENDS HERE
    }

    public Student getStudentById(int id) {
        //YOUR CODE STARTS HERE

        try
        {
            Student getStudent = studentDao.findStudentById(id);
            return getStudent;
        }catch(DataAccessException ex)
        {
            Student errorStudent = new Student();
            errorStudent.setStudentFirstName("Student Not Found");
            errorStudent.setStudentLastName("Student Not Found");
            return errorStudent;
        }

        //YOUR CODE ENDS HERE
    }

    public Student addNewStudent(Student student) {
        //YOUR CODE STARTS HERE

        if(student.getStudentFirstName().isBlank() || student.getStudentLastName().isBlank())
        {
            student.setStudentFirstName("First Name blank, student NOT added");
            student.setStudentLastName("Last Name blank, student NOT added");
            return student;
        }

        return studentDao.createNewStudent(student);

        //YOUR CODE ENDS HERE
    }

    public Student updateStudentData(int id, Student student) {
        //YOUR CODE STARTS HERE

        if(id != student.getStudentId())
        {
            student.setStudentFirstName("IDs do not match, student not updated");
            student.setStudentLastName("IDs do not match, student not updated");

            return student;
        }

        studentDao.updateStudent(student);
        return student;

        //YOUR CODE ENDS HERE
    }

    public void deleteStudentById(int id) {
        //YOUR CODE STARTS HERE

        studentDao.deleteStudent(id);

        //YOUR CODE ENDS HERE
    }

    public void deleteStudentFromCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        //We are calling course from external class
        //Therefore we should handle if the service layer is null
        //This is not necessary as either way we pass the unit tests
        Student student = getStudentById(studentId);
        Course course = null;

        try
        {
            // Use the autowired courseService instead of creating a new one
            if (courseService == null) {
                System.out.println("Course Service Layer is null");
                return;
            }

            course = courseService.getCourseById(courseId);

        }catch(Exception ex)
        {
            System.out.println("Error accessing Course Service Layer: " + ex.getMessage());
            return;
        }

        //Validation checks
        if(student.getStudentFirstName().equals("Student Not Found"))
        {
            System.out.println("Student not found");
            return;
        }

        //If null, just stop
        if(course == null)
        {
            return;
        }

        else if(course.getCourseName().equals("Course Not Found"))
        {
            System.out.println("Course not found");
            return;
        }

        //if it exists
        studentDao.deleteStudentFromCourse(studentId, courseId);
        System.out.println("Student: " + studentId + " deleted from course: " + courseId);

        //YOUR CODE ENDS HERE
    }


    public void addStudentToCourse(int studentId, int courseId) {
        //YOUR CODE STARTS HERE

        Student student = getStudentById(studentId);
        Course course = null;

        try {
            //Create a new CourseServiceImpl()
            //We actually use the autowired service here if available
            if (courseService == null) {
                System.out.println("Course Service Layer is null");
                return;
            }

            course = courseService.getCourseById(courseId);

        } catch (Exception ex) {
            System.out.println("Error accessing Course Service Layer: " + ex.getMessage());
            return;
        }

        //Validation checks
        if (student.getStudentFirstName().equals("Student Not Found")) {
            System.out.println("Student not found");
            return;
        }

        if (course == null) {
            //If null, just stop
            return;
        }

        if (course.getCourseName().equals("Course Not Found")) {
            System.out.println("Course not found");
            return;
        }

        //Add the student to the course
        try {
            studentDao.addStudentToCourse(studentId, courseId);
            System.out.println("Student: " + studentId + " added to course: " + courseId);
        } catch (Exception ex) {
            //If already enrolled
            System.out.println("Student: " + studentId + " already enrolled in course: " + courseId);
        }

        //YOUR CODE ENDS HERE
    }

}

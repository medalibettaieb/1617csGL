package mBeans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import tn.esprit.cs.g2.entities.Course;
import tn.esprit.cs.g2.entities.ExamType;
import tn.esprit.cs.g2.entities.Student;
import tn.esprit.cs.g2.services.CourseManagementLocal;

@ManagedBean
@ViewScoped
public class CourseManagementBean {
	private Boolean displayF1 = true;
	private Boolean displayF2 = false;
	private Boolean displayF3 = false;
	private Date date = new Date();

	private Course course = new Course();
	private List<Course> coursesByTeacher;
	private List<Course> coursesByStudent;
	private List<Course> allCourses;
	private List<Student> studentsByCourse;
	private List<ExamType> keyList;

	private ExamType examType;
	private Float mark;
	@EJB
	private CourseManagementLocal courseManagementLocal;
	@ManagedProperty(value = "#{identity}")
	private Identity identity;

	public void doIt(Student student) {
		courseManagementLocal.assignMarks(identity.getUser().getId(), course.getId(), student.getId(), examType, mark,
				date);
	}

	public void dodo() {
		System.out.println(date);
	}

	public String doSaveOrUpdateCourse() {
		int idTeacher = identity.getUser().getId();
		courseManagementLocal.addCourseWithTeacher(course, idTeacher);
		return null;
	}

	public String doFindStudentsByCourse() {
		displayF3 = true;
		studentsByCourse = courseManagementLocal.findAllStudentsByCourseId(course.getId());
		return null;

	}

	public String doSubscribe() {
		courseManagementLocal.subscibeToCourse(course.getId(), identity.getUser().getId());
		return "/pages/studentHome/home?faces-redirect=true";
	}

	public void select() {
		displayF1 = false;
		displayF2 = true;
	}

	public void cancel() {
		displayF1 = true;
		displayF2 = false;
		displayF3 = false;
	}

	public void doDeleteCourse() {
		courseManagementLocal.deleteCourse(course.getId());
		cancel();
	}

	public void doUpdateCourse() {
		courseManagementLocal.saveOrUpdateCourse(course);
		cancel();
	}

	public Identity getIdentity() {
		return identity;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Course> getCoursesByTeacher() {
		coursesByTeacher = courseManagementLocal.findCoursesByTeacherId(identity.getUser().getId());
		return coursesByTeacher;
	}

	public void setCoursesByTeacher(List<Course> coursesByTeacher) {
		this.coursesByTeacher = coursesByTeacher;
	}

	public Boolean getDisplayF1() {
		return displayF1;
	}

	public void setDisplayF1(Boolean displayF1) {
		this.displayF1 = displayF1;
	}

	public Boolean getDisplayF2() {
		return displayF2;
	}

	public void setDisplayF2(Boolean displayF2) {
		this.displayF2 = displayF2;
	}

	public List<Course> getAllCourses() {
		allCourses = courseManagementLocal.findAllCourses();
		return allCourses;
	}

	public void setAllCourses(List<Course> allCourses) {
		this.allCourses = allCourses;
	}

	public List<Course> getCoursesByStudent() {
		coursesByStudent = courseManagementLocal.findAllCoursesByIdUser(identity.getUser().getId());
		return coursesByStudent;
	}

	public void setCoursesByStudent(List<Course> coursesByStudent) {
		this.coursesByStudent = coursesByStudent;
	}

	public List<Student> getStudentsByCourse() {
		return studentsByCourse;
	}

	public void setStudentsByCourse(List<Student> studentsByCourse) {
		this.studentsByCourse = studentsByCourse;
	}

	public Boolean getDisplayF3() {
		return displayF3;
	}

	public void setDisplayF3(Boolean displayF3) {
		this.displayF3 = displayF3;
	}

	public ExamType getExamType() {
		return examType;
	}

	public void setExamType(ExamType examType) {
		this.examType = examType;
	}

	public List<ExamType> getKeyList() {
		keyList = new ArrayList<ExamType>(course.getMapExamType().keySet());
		return keyList;
	}

	public void setKeyList(List<ExamType> keyList) {
		this.keyList = keyList;
	}

	public Float getMark() {
		return mark;
	}

	public void setMark(Float mark) {
		this.mark = mark;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}

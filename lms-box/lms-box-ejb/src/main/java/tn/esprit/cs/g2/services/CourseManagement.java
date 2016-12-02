package tn.esprit.cs.g2.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import tn.esprit.cs.g2.entities.Course;
import tn.esprit.cs.g2.entities.ExamType;
import tn.esprit.cs.g2.entities.Student;
import tn.esprit.cs.g2.entities.SubscriptionDetail;
import tn.esprit.cs.g2.entities.Teacher;
import tn.esprit.cs.g2.entities.User;

/**
 * Session Bean implementation class CourseManagement
 */
@Stateless
public class CourseManagement implements CourseManagementRemote, CourseManagementLocal {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB
	private UserManagementLocal userManagementLocal;

	/**
	 * Default constructor.
	 */
	public CourseManagement() {
	}

	@Override
	public void addCourseWithTeacher(Course course, int idTeacher) {
		Teacher teacher = (Teacher) userManagementLocal.findUserById(idTeacher);
		course.setTeacher(teacher);

		entityManager.persist(course);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Course> findCoursesByTeacherId(int idTeacher) {
		String jpql = "SELECT c FROM Course c WHERE c.teacher.id=:param";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("param", idTeacher);
		return query.getResultList();
	}

	@Override
	public Course findCourseById(int idCourse) {
		return entityManager.find(Course.class, idCourse);
	}

	@Override
	public void subscibeToCourse(int idCourse, int idStudent) {
		Course courseFound = findCourseById(idCourse);
		User userFound = userManagementLocal.findUserById(idStudent);

		SubscriptionDetail subscriptionDetail = new SubscriptionDetail(userFound, courseFound);
		entityManager.merge(subscriptionDetail);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Student> findAllStudentsByCourseId(int idCourse) {
		List<Student> students = null;
		String jpql = "SELECT u FROM User u JOIN u.subscriptionDetails us WHERE us.course.id=:param1";
		Query query = entityManager.createQuery(jpql);
		query.setParameter("param1", idCourse);
		try {
			students = query.getResultList();
		} catch (Exception e) {
			System.err.println("no subscriptions in this course  ...");
		}
		return students;
	}

	@Override
	public List<Course> findAllCoursesByIdUser(int idUser) {
		return entityManager.createQuery("select c from Course c where c.teacher.id=:param", Course.class)
				.setParameter("param", idUser).getResultList();
	}

	@Override
	public void saveOrUpdateCourse(Course course) {
		entityManager.merge(course);
	}

	@Override
	public List<Course> findAllCourses() {
		return entityManager.createQuery("select c from Course c", Course.class).getResultList();
	}

	@Override
	public void assignMarks(int idTeacher, int idCourse, int idStudent, ExamType typeOfTheEvaluation, Float mark,
			Date dateOfTheEvaluation) {
		Course course = findCourseById(idCourse);
		if (userManagementLocal.checkTeacherByCourseId(idCourse, idTeacher) != null) {
			if (userManagementLocal.checkIfStudentIsSuscribed(idStudent, idCourse) != null) {
				if (!userManagementLocal.findSubscriptionOfStudentInCourse(idStudent, idCourse, dateOfTheEvaluation)
						.getStateOfValidation()) {
					// check if typeOfExam exist int the map of the course
					Map<ExamType, Integer> map = course.getMapExamType();
					Boolean isExamTypeValid = false;
					for (ExamType et : map.keySet()) {
						if (et == typeOfTheEvaluation) {
							SubscriptionDetail subscriptionDetail = userManagementLocal
									.findSubscriptionOfStudentInCourse(idStudent, idCourse, dateOfTheEvaluation);
							subscriptionDetail.getMapMarks().put(typeOfTheEvaluation, mark);
							entityManager.merge(subscriptionDetail);
							isExamTypeValid = true;
						}

					}
					System.out.println(isExamTypeValid);

				} else {
					System.out.println("the student has valitaded this course");
				}
			} else {
				System.out.println("student not subscribed in this course");
			}
		} else {
			System.err.println("you are not authorized ");
		}

	}

	@Override
	public void deleteCourse(int courseId) {
		entityManager.remove(findCourseById(courseId));

	}
}

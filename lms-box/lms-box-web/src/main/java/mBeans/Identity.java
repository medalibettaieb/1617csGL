package mBeans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import tn.esprit.cs.g2.entities.Student;
import tn.esprit.cs.g2.entities.Teacher;
import tn.esprit.cs.g2.entities.User;
import tn.esprit.cs.g2.services.UserManagementLocal;

@ManagedBean
@SessionScoped
public class Identity {
	private User user = new User();
	@EJB
	private UserManagementLocal userManagementLocal;

	public String doLogin() {
		String navigateTo = null;
		User userLoggedIn = userManagementLocal.login(user.getLogin(), user.getPassword());
		if (userLoggedIn != null) {
			user=userLoggedIn;
			if (userLoggedIn instanceof Teacher) {
				navigateTo = "/pages/teacherHome/home?faces-redirect=true";
			} else if (userLoggedIn instanceof Student) {
				navigateTo = "/pages/studentHome/home?faces-redirect=true";
			}
		} else {
			navigateTo = "/fail?faces-redirect=true";
		}
		return navigateTo;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
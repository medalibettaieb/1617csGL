package tn.esprit.cs.g2.entities;

import java.io.Serializable;

import javax.persistence.Entity;

/**
 * Entity implementation class for Entity: Teacher
 *
 */
@Entity(name = "Teacher")

public class Teacher extends User implements Serializable {

	private int experience;
	private static final long serialVersionUID = 1L;

	public Teacher() {
		super();
	}

	public Teacher(String name, int experience) {
		super(name);
		this.experience = experience;
	}

	public Teacher(String name, String login, String password, int experience) {
		super(name, login, password);
		this.experience = experience;
	}

	public int getExperience() {
		return this.experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

}

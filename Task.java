package taskmanager;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{

	int id;
	String description;
	Date addDate;
	Status status;
	String byUser;
	
	public Task() {}
	
	public Task(int id, String description, Date addDate, Status status, String byUser) {
		super();
		this.id = id;
		this.description = description;
		this.addDate = addDate;
		this.status = status;
		this.byUser = byUser;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getByUser() {
		return byUser;
	}

	public void setByUser(String byUser) {
		this.byUser = byUser;
	}

}

package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.Password;
import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Model;

@Table(name="tb_user")
@Entity
public class UserBO extends Model{

	@Unique
	@Email
	@Required
	public String email;
	
	@Required
	@Password
	public String password;
	
	public String fullname;
	public boolean isAdmin;
	
	public UserBO(String email, String password, String fullname){
		
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		
	}
	
	public static UserBO connect(String email, String password){
		return find("byEmailAndPassword", email, password).first();
	}
	
	public String toString(){
		return email;
	}
	
	public static UserBO findUser(String controller){
		return find("byEmail", controller).first();
	}
	
	
//	Parte dos Getters and Setters

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
}

package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name="blog_user")
@Entity
public class UserBO extends Model{

	@Email
	@Required
	public String email;
	
	@Required
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
	
}

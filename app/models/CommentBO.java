package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name="tb_comment")
@Entity
public class CommentBO extends Model{

	@Required
	public String author;
	
	@Required
	public Date postedAt;
	
	@Lob
	@Required
	@MaxSize(10000)
	public String content;

	@ManyToOne
	@Required
	@JoinColumn(name="id_post")
	public PostBO post;
	
	public CommentBO(PostBO post, String author, String content){
		this.post = post;
		this.author = author;
		this.content = content;
		this.postedAt = new Date();
	}
	
	
	
	
	
	
//	Parte dos Getters and Setters
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PostBO getPost() {
		return post;
	}

	public void setPost(PostBO post) {
		this.post = post;
	}
	
}

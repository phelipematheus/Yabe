package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class PostBO extends Model{

	@Required
	public String title;
	
	@Required
	public Date postedAt;
	
	@Lob
	@Required()
	@MaxSize(10000)
	public String content;

	@Required
	@ManyToOne
	public UserBO author;
	
	@OneToMany(mappedBy="post", cascade=CascadeType.ALL)
	public List<CommentBO> comments;
	
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<TagBO> tags;
	
	public PostBO(UserBO author, String title, String content){
		this.comments = new ArrayList<CommentBO>();
		this.tags = new TreeSet<TagBO>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date();
	}

	public PostBO addComment(String author, String content){
		CommentBO newComment = new CommentBO(this, author, content).save();
		this.comments.add(newComment);
		this.save();
		return this;
	}
	
	public PostBO previous(){
		return PostBO.find("postedAt < ? order by postedAt desc", postedAt).first();
	}
	
	public PostBO next(){
		return PostBO.find("postedAt > ? order by postedAt asc", postedAt).first();
	}
	
	public PostBO tagItWith(String name){
		tags.add(TagBO.findOrCreateByName(name));
		return this;
	}
	
	public static List<PostBO> findTaggedWith(String tag) {
	    return PostBO.find(
	        "select distinct p from PostBO p join p.tags as t where t.name = ?", tag
	    ).fetch();
	}
	
}
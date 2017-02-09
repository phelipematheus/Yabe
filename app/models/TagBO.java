package models;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Table(name="tb_tag")
@Entity
public class TagBO extends Model implements Comparable<TagBO>{

	@Required
	public String name;
	
	private TagBO(String name){
		this.name = name;
	}
	
	public String toString(){
		return name;
	}
	
	public int compareTo(TagBO otherTag){
		return name.compareTo(otherTag.name);
	}
	
	public static TagBO findOrCreateByName(String name){
		TagBO tag = TagBO.find("byName", name).first();
		if(tag == null){
			tag = new TagBO(name);
		}
		return tag;
	}
	
	public  static List<Map> getCloud(){	
		List<Map> result = TagBO.find(
		        "select new map(t.name as tag, count(p.id) as pound) from PostBO p join p.tags as t group by t.name order by t.name"
			    ).fetch();
		return result;
	}

	
	
	
//	Parte dos Getters and Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}

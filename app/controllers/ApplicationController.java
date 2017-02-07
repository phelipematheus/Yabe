package controllers;

import java.util.List;

import models.PostBO;
import play.Play;
import play.cache.*;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Before;
import play.mvc.Controller;

public class ApplicationController extends Controller {

    public static void index() {
    	PostBO frontPost = PostBO.find("order by postedAt desc").first();
    	List<PostBO> olderPosts = PostBO.find("order by postedAt desc").from(1).fetch(10);
        render(frontPost, olderPosts);
    }

    @Before
    public static void addDefaults(){
    	renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
    	renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }
    
    public static void show(Long id){
    	PostBO post = PostBO.findById(id);
    	String randomID = Codec.UUID();
    	render(post, randomID);
    }
    
    public static void postComment(
    		Long postId,
    		@Required(message="Author is required") String author,
    		@Required(message="A message is required") String content, 
    		@Required(message="Please type the code") String code,
    		String randomID){
    	PostBO post = PostBO.findById(postId);
 
    	validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");
    	
    	if(validation.hasErrors()){
    		render("ApplicationController/show.html", post);
    	}
    	
    	post.addComment(author, content);
    	flash.success("Thanks for posting", author);
    	Cache.delete(randomID);
    	show(postId);
    }
    
    public static void captcha(String id){
    	Images.Captcha captcha = Images.captcha();
    	String code = captcha.getText("#E4EAFD"); //essas letras em azul é a cor!
    	Cache.set(id, code, "10mn");
    	renderBinary(captcha);
    }
    
    public static void listTagged(String tag){
    	List<PostBO> posts = PostBO.findTaggedWith(tag);
    	render(tag, posts);
    }
    
    
    
    
    
}
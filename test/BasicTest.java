import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import models.CommentBO;
import models.PostBO;
import models.TagBO;
import models.UserBO;
import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

    @Test
    public void createAndRetrieveUser() {
        // Create a new user and save it
        new UserBO("bob@gmail.com", "secret", "Bob").save();
        
        // Retrieve the user with e-mail address bob@gmail.com
        UserBO bob = UserBO.find("byEmail", "bob@gmail.com").first();
        
        // Test 
        assertNotNull(bob);
        assertEquals("Bob", bob.fullname);
    }
    
    @Test
    public void tryConnectAsUser(){
//    	Creiando o novo usuário
    	new UserBO("bob@gmail.com","secret", "Bob").save();
//    	Teste
    	assertNotNull(UserBO.connect("bob@gmail.com", "secret"));
    	assertNull(UserBO.connect("bob@gmail.com", "badpassword"));
    	assertNull(UserBO.connect("tom@gmail.com", "secret"));
    }
    
    @Before
    public void setup(){
    	Fixtures.deleteDatabase();
    }

    
    @Test
    public void createPost(){
//    	Criar o novo usuário e salvá-lo
    	UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save();

//    	Criando um novo post
    	new PostBO(bob, "My first post", "Hello world").save();
    	
//    	Testando se o post foi criado
    	assertEquals(1, PostBO.count());
    	
//    	Recuperando todos os posts criados por bob
    	List<PostBO> bobPosts = PostBO.find("byAuthor", bob).fetch();

//    	Testes
    	assertEquals(1, bobPosts.size());
    	PostBO firstPost = bobPosts.get(0);
    	assertNotNull(firstPost);
    	assertEquals(bob, firstPost.author);
    	assertEquals("My first post", firstPost.title);
    	assertEquals("Hello world", firstPost.content);
    	assertNotNull(firstPost.postedAt); 	

    }
    
    @Test
    public void postComments(){
//    	Criando o novo usuário e salvando-o
    	UserBO bob = new UserBO("bob@gmail.com","secret","Bob").save();
    	
//    	Criando o novo post
    	PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save();

//    	Postar o primeiro comentário
    	new CommentBO(bobPost, "Jeff", "Nice post").save();
    	new CommentBO(bobPost, "Tom", "I knew that !").save();
    	
    	
//    	Recuperar todos os comentários
    	List<CommentBO> bobPostComments = CommentBO.find("byPost", bobPost).fetch();

//    	Tests
    	assertEquals(2, bobPostComments.size());
    
    	CommentBO firstComment = bobPostComments.get(0);
    	assertNotNull(firstComment);
    	assertEquals("Jeff", firstComment.author);
    	assertEquals("Nice post", firstComment.content);
    	assertNotNull(firstComment.postedAt);
    	
    	CommentBO secondComment = bobPostComments.get(1);
    	assertNotNull(secondComment);
    	assertEquals("Tom", secondComment.author);
    	assertEquals("I knew that !", secondComment.content);
    	assertNotNull(secondComment.postedAt);
    
    }
    
    @Test
    public void userTheCommentsRelation(){
//    	Criar um novo usuario e salva-los
    	UserBO bob = new UserBO("bob@gmail.com","secret","Bob").save();
    
//    	Criar um novo post
    	PostBO bobPost = new PostBO(bob, "My first post","Hello world").save();
    	
//    	Postar o primeiro comentário
    	bobPost.addComment("Jeff", "Nice post");
    	bobPost.addComment("Tom", "I knew that !");
    	
//    	Contando coisas
    	assertEquals(1,UserBO.count());
    	assertEquals(1, PostBO.count());
    	assertEquals(2, CommentBO.count());
    	
//    	Recuperando o post do Bob
    	bobPost = PostBO.find("byAuthor", bob).first();
    	assertNotNull(bobPost);
    	
//    	Navegando nos comentários
    	assertEquals(2, bobPost.comments.size());
    	assertEquals("Jeff", bobPost.comments.get(0).author);
    	
//    	Deletando o post
    	bobPost.delete();
    	
//    	Checando se todos os comentários foram deletados
    	assertEquals(1, UserBO.count());
    	assertEquals(0, PostBO.count());
    	assertEquals(0, CommentBO.count()); 	
    }
    
    @Test
    public void fullTest(){
    	Fixtures.loadModels("data.yml");
    	
//    	Contando coisas
    	assertEquals(2, UserBO.count());
    	assertEquals(3, PostBO.count());
    	assertEquals(3, CommentBO.count());
    	
//    	Tentando se conectar como usuário
    	assertNotNull(UserBO.connect("bob@gmail.com", "secret"));
    	assertNotNull(UserBO.connect("jeff@gmail.com", "secret"));
    	assertNull(UserBO.connect("jeff@gmail.com","badpassword"));
    	assertNull(UserBO.connect("tom@gmail.com","secret"));
    	
//    	Achando todos os post do Bob
    	List<PostBO> bobPost = PostBO.find("author.email","bob@gmail.com").fetch();
    	assertEquals(2, bobPost.size());
    	
//    	Achando todos os comentários relacionados aos posts do Bob
    	List<CommentBO> bobComments = CommentBO.find("post.author.email", "bob@gmail.com").fetch();
    	assertEquals(3, bobComments.size());
    	
//    	Achando o post mais recente
    	PostBO frontPost = PostBO.find("order by postedAt desc").first();
    	assertNotNull(frontPost);
    	assertEquals("About the model layer", frontPost.title);
    	
//    	Checando se esse post tem dois comentários
    	assertEquals(2, frontPost.comments.size());
    	
//    	Postando um novo comentário
    	frontPost.addComment("Jim", "Hello guys");
    	assertEquals(3, frontPost.comments.size());
    	assertEquals(4, CommentBO.count());		
    	
    }
    
    public void testTags(){
    	 // Create a new user and save it
        UserBO bob = new UserBO("bob@gmail.com", "secret", "Bob").save();
     
        // Create a new post
        PostBO bobPost = new PostBO(bob, "My first post", "Hello world").save();
        PostBO anotherBobPost = new PostBO(bob, "Hop", "Hello world").save();
     
        // Well
        assertEquals(0, PostBO.findTaggedWith("Red").size());
     
        // Tag it now
        bobPost.tagItWith("Red").tagItWith("Blue").save();
        anotherBobPost.tagItWith("Red").tagItWith("Green").save();
     
        // Check
        assertEquals(2, PostBO.findTaggedWith("Red").size());
        assertEquals(1, PostBO.findTaggedWith("Blue").size());
        assertEquals(1, PostBO.findTaggedWith("Green").size());
        
        List<Map> cloud = TagBO.getCloud();
        	assertEquals("[{tag=Blue, pound=1}, {tag=Green, pound=1}, {tag=Red, pound=2}]", cloud.toString());
    }
    
}

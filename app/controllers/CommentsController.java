package controllers;

import models.CommentBO;
import play.mvc.*;
import play.*;

@Check("admin")
@With(Secure.class)
@CRUD.For(CommentBO.class)
public class CommentsController extends CRUD{

}

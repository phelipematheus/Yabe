package controllers;

import models.PostBO;
import play.mvc.*;
import play.*;


@Check("admin")
@With(Secure.class)
@CRUD.For(PostBO.class)
public class PostsController extends CRUD{

}

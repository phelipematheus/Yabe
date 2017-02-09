package controllers;

import models.UserBO;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
@CRUD.For(UserBO.class)
public class UsersController extends CRUD{

	
	
	
}

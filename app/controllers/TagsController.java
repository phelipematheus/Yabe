package controllers;

import models.TagBO;
import play.mvc.With;

@Check("admin")
@With(Secure.class)
@CRUD.For(TagBO.class)
public class TagsController extends CRUD{

}

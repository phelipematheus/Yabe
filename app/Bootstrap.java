
import play.*;
import play.jobs.*;
import play.test.*;

import models.*;

@OnApplicationStart
public class Bootstrap extends Job{

	public void doJob(){
//		checar se o banco da dados está vazio
		if(UserBO.count() == 0){
			Fixtures.loadModels("initial-data.yml");
		}
	}
	
}

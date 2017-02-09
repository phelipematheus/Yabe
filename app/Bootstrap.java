
import models.UserBO;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job{

	public void doJob(){
//		checar se o banco da dados está vazio
		if(UserBO.count() >= 0){
			Fixtures.deleteDatabase();
			Fixtures.loadModels("initial-data.yml");
		}
	}
	
}

import java.util.ArrayList;
public class Logger {
	ArrayList<Object> objectsToCheck;

	public Logger(){
		objectsToCheck = new ArrayList<Object>();
	}

	public void check(){
		for(Object o:objectsToCheck){
			System.out.println(o.toString() + "\n");
		}
	}

}

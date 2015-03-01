package multimidia;

public class NoMusicFoundMood extends Exception{
	
	public NoMusicFoundMood(String mood){
		super("Nenhuam música com o humor : " + mood +" foi encontrada.");
	}
	

}

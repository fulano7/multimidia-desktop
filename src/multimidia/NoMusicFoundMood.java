package multimidia;

public class NoMusicFoundMood extends Exception{
	
	public NoMusicFoundMood(String mood){
		super("Nenhuam m�sica com o humor : " + mood +" foi encontrada.");
	}
	

}

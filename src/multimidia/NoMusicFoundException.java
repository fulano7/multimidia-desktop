package multimidia;

public class NoMusicFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NoMusicFoundException(String dir){
		super("No MP3 files were found in directory "+dir);
	}

}

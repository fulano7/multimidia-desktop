package multimidia;

public class NoMusicFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NoMusicFoundException(String dir){
		super("Nenhum arquivo MP3 foi encontrado no diretório : " + dir);
	}

}

package multimidia;

public class Musica {
	private int valencia;
	private int ativacao;
	public int getValencia() {
		return valencia;
	}

	public void setValencia(int valencia) {
		this.valencia = valencia;
	}

	public int getAtivacao() {
		return ativacao;
	}

	public void setAtivacao(int ativacao) {
		this.ativacao = ativacao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCaminho() {
		return caminho;
	}

	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}

	private String nome;
	private String caminho;
	
	public Musica (String nome, String caminho, int valencia, int ativacao){
		this.valencia = valencia;
		this.ativacao = ativacao;
		this.nome = nome;
		this.caminho = caminho;
	}



}

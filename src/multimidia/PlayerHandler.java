package multimidia;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import java.util.Vector;

import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;


public class PlayerHandler implements BasicPlayerListener {
	private PrintStream out = null;
	private Vector<Musica> listaReproducao;
	public boolean paused, stopped, userStopped;
	private BasicPlayer player;
	private BasicController control = (BasicController) player;
	private int last, current;
	
	// testes
	/*public static void main(String[] args) {
		try {
			PlayerHandler test = new PlayerHandler();
			test.play("C:\\Users\\irvm\\Music\\A-ha\\Totally 80's - Disc 1\\Take On Me.mp3");
			Thread.sleep(3000);
			test.pause();
			Thread.sleep(2000);
			test.resume();
			Thread.sleep(2000);
			test.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/

	public PlayerHandler(Vector<Musica> listaReproducao) {
		this.out = System.out;
		this.player = new BasicPlayer();
        this.control = (BasicController) player;
        this.player.addBasicPlayerListener(this);
        this.paused = false;
        this.stopped = true;
        this.userStopped = true;
        this.listaReproducao = listaReproducao;
        this.last = listaReproducao.size()-1;
        this.current = 0;
	}
	
	public void next(){
		if(!stopped) this.stop();
		this.current = this.current == this.last ? 0 : this.current+1;
		this.play(this.listaReproducao.get(this.current).getCaminho());
	}
	
	public void previous(){
		if(!stopped) this.stop();
		this.current = this.current == 0 ? this.last : this.current-1;
		this.play(this.listaReproducao.get(this.current).getCaminho());
	}
	
	/*public void setCurrent(String current){ // caminho da musica
		if (!stopped){
		this.stop();
		}
		this.current = current;
		this.play(this.current);
	}*/

	public void play(String filename) {


		try {
			if (stopped) {
				control.open(new File(filename));
				control.play();
				control.setPan(0.0);
				this.stopped = false;
				this.paused = false;
			}
		} catch (BasicPlayerException e) {
			e.printStackTrace();
		}
	}

	public void opened(Object stream, Map properties) {
		display("opened : " + properties.toString());
	}

	public void progress(int bytesread, long microseconds, byte[] pcmdata,
			Map properties) {
		display("progress : " + properties.toString());
	}
	
	public void stop(){
		try{
			control.stop();
			this.paused = false;
			this.stopped = true;
		}catch(Exception e){}
	}
	
	public void pause(){
		try{
			if(!paused){
				control.pause();
			    this.paused = true;
			    this.stopped = false;
			}
		}catch(Exception e){}
	}
	
	public void resume() throws BasicPlayerException{
		if(paused){
			control.resume();
			this.paused = false;
			this.stopped = false;
		}
	}

	public void stateUpdated(BasicPlayerEvent event) {
		display("stateUpdated : " + event.toString());
		if(BasicPlayerEvent.EOM==event.getCode()){
			userStopped = false;
		} else if (BasicPlayerEvent.STOPPED==event.getCode()){
			if(!userStopped){
				userStopped = true;
				next();
			}
		}
				
	}
	
	
	
	public void setController(BasicController controller) {
		display("setController : " + controller);
	}

	public void display(String msg) {
		if (out != null)
			out.println(msg);
	}
	
	

}

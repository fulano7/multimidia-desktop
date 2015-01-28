package multimidia;

import java.io.File;

import com.echonest.api.v4.Track;

public final class EchoNestAnalysis {
	public final File music;
	public final Track track;
	public final int order;
	
	public EchoNestAnalysis(File file, Track track, int order) {
		this.music = file;
		
		this.track = track;
		
		this.order = order;
	}
}
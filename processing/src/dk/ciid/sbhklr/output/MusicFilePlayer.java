package dk.ciid.sbhklr.output;

import java.io.File;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class MusicFilePlayer extends Instrument {

	private Minim minim;
	private AudioPlayer player;
	private String filePath;

	public MusicFilePlayer(PApplet applet, String filePath, int bpm) {
		this(applet, filePath);
		setBPM(bpm);
	}
	
	public MusicFilePlayer(PApplet applet, String filePath) {
		minim = new Minim(applet);
		this.filePath = filePath;
		File audioFile = new File(filePath);
		String audioFilePath = audioFile.getAbsolutePath();
		player = minim.loadFile(audioFilePath);			
	}
	
	@Override
	public void setMuted(boolean isMuted) {
		super.setMuted(isMuted);
		if(isMuted) {
			player.mute();
		} else {
			player.unmute();
		}
	}

	@Override
	public void beat(int beatCount) {
		log("* Music File Player 🔈playing: " + filePath + " @ " + beat.getBPM() + " *");
		play();
	}

	public void play() {
		if(!isMuted()){			
			player.rewind();
			player.play();
		}
	}
}

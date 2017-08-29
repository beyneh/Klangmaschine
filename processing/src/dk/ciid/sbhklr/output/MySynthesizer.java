package dk.ciid.sbhklr.output;

import de.hfkbremen.klang.SynthUtil;
import de.hfkbremen.klang.Synthesizer;

public class MySynthesizer extends Instrument {

	protected static final int DEFAULT_VELOCITY = 127;
	protected Synthesizer synth;
	private Integer currentNote = null;
	
	public MySynthesizer(Synthesizer synth) {
		this.synth = synth;
	}
	
	public MySynthesizer(int bpm, Synthesizer synth) {
		SynthUtil.dumpMidiOutputDevices();		
		this.synth = synth;
		setBPM(bpm);
	}
	
	public MySynthesizer(int bpm) {
		this(bpm, Synthesizer.getSynth("jsyn-filter+lfo"));
	}
	
	public void printDeviceList(){
		SynthUtil.dumpMidiOutputDevices();
	}
	
	@Override
	public void setMuted(boolean isMuted) {
		super.setMuted(isMuted);
		synth.noteOff();
	}
	
	public void setPitch(int percent){
		int pitchMax = 16383;
		int pitchValue = (int) pitchMax / 100 * percent;
		log("Setting pitch to: " + pitchValue);
		synth.pitch_bend(pitchValue);
	}
	
	public void beat(int currentBeat) {
		String className = this.getClass().getName();
		currentNote = dequeueSignal();
		ControlSignal controlSignal = dequeueControlSignal();		
		synth.control_change(controlSignal.getNumber(), controlSignal.getValue());
		
		if(currentNote != null && !isMuted()){	
			log("* " + className + " 🎹 playing: " + currentNote + "🎵 @ " + beat.getBPM() + " *");
			synth.noteOn(currentNote, DEFAULT_VELOCITY);
		} else {
			synth.noteOff();
			log("* " + className + " 🎹 has an empty queue *");
		}
	}

}

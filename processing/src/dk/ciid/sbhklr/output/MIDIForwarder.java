package dk.ciid.sbhklr.output;

import de.hfkbremen.klang.Synthesizer;

public class MIDIForwarder extends MySynthesizer {

	public MIDIForwarder(String deviceName, int bpm) {
		super(bpm, Synthesizer.getSynth("midi", deviceName));
	}
	
	public MIDIForwarder(String deviceName) {
		super(Synthesizer.getSynth("midi", deviceName));
	}
	
	@Override
	public void queueSignal(Integer signal) {		
		if(!hasBeat()){		
			synth.noteOn(signal, DEFAULT_VELOCITY);
		} else {
			super.queueSignal(signal);
		}
	}
	
	@Override
	public void queueControlSignal(ControlSignal signal) {
		if(!hasBeat()){		
			synth.instrument(signal.getChannel());
			synth.control_change(signal.getNumber(), signal.getValue());
			System.out.println("Sending control signal: " + signal.getNumber() + "/" + signal.getValue() + " on channel " + signal.getChannel());
		} else {
			super.queueControlSignal(signal);
		}
	}
}

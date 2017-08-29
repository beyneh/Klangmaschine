package dk.ciid.sbhklr.applet;

import java.util.ArrayList;

import dk.ciid.sbhklr.input.NoteGenerator;
import dk.ciid.sbhklr.input.StringGenerator;
import dk.ciid.sbhklr.network.Connector;
import dk.ciid.sbhklr.network.Processor;
import dk.ciid.sbhklr.output.ControlSignal;
import dk.ciid.sbhklr.output.Instrument;
import dk.ciid.sbhklr.output.MIDIForwarder;
import dk.ciid.sbhklr.output.MusicFilePlayer;
import dk.ciid.sbhklr.output.MySynthesizer;
import dk.ciid.sbhklr.output.SpeechSynthesizer;
import dk.ciid.sbhklr.visuals.RandomBackgroundEffect;
import dk.ciid.sbhklr.visuals.VisualizationEngine;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class Main extends PApplet {

	    private static final int MIDI_MIN_VALUE = 0;
		private static final int MIDI_MAX_VALUE = 127;
		private static final String SOUND_FLOWER_DEVICE = "175"; //Print using 'say -a ?'
		private static final String CLIENT_NAME = "sbhklr";
	    private static final String SERVER_NAME = "localhost"; //"itp2017.local"
		private static final int CANVAS_HEIGHT = 600;
		private static final int CANVAS_WIDTH = 800;
		
		private VisualizationEngine visualizationEngine;
		private ArrayList<Instrument> instruments = new ArrayList<>();
		private Connector connector;
		
		//private NoteGenerator randomNoteGenerator;
		//private NoteGenerator noteGenerator;
		//private StringGenerator stringGenerator;

		//private MusicFilePlayer bassBeatPlayer;
		private MusicFilePlayer snarePlayer;
		private MusicFilePlayer hornPlayer;
		
		private MySynthesizer synthesizer;
		private SpeechSynthesizer speechSynthesizer;
		
		private MIDIForwarder midiForwarderA;

		public void settings() {
	        size(CANVAS_WIDTH, CANVAS_HEIGHT);
	    }

	    public void setup() {	    	
	    	connector = new Connector(SERVER_NAME, CLIENT_NAME);
	    	visualizationEngine = new VisualizationEngine(this);	
	    	visualizationEngine.addEffect(new RandomBackgroundEffect(240));

	    	//randomNoteGenerator = new NoteGenerator();
	    	//noteGenerator = new NoteGenerator(connector);
	    	//noteGenerator.setUseScaleNotes(true);
	    	//stringGenerator = new StringGenerator(connector);
	    	
	    	setupInstruments();
			registerProcessors();
	    }

		private void setupInstruments() {
			midiForwarderA = new MIDIForwarder("IAC Bus 1");
			
	    	//synthesizer = new MySynthesizer(256);
			//bassBeatPlayer = new MusicFilePlayer(this, "resources/Bip.q1.wav", 64);
	    	speechSynthesizer = new SpeechSynthesizer(65, true, SOUND_FLOWER_DEVICE);
	    	snarePlayer = new MusicFilePlayer(this, "resources/Ensoniq-ESQ-1-Snare-2.wav");
	    	hornPlayer = new MusicFilePlayer(this, "resources/12736__leady__distant-horn.mp3");
	    	
	    	instruments.add(midiForwarderA);
	    	instruments.add(speechSynthesizer);	  
		}

		private void registerProcessors() {
			
			connector.register("mute", new Processor() {
				
				@Override
				public void processSignal(float value) {
					Instrument instrument = instruments.get((int) value);
					instrument.setMuted(true);
				}
				
				@Override
				public void processSignal(float valueA, float valueB) {}

				@Override
				public void processSignal(float valueA, float valueB, float valueC) {}
				
				@Override
				public void processMessages(ArrayList<String> messages) {}
				
				@Override
				public void processMessage(String message) {}
			});
			
			connector.register("unmute", new Processor() {
				
				@Override
				public void processSignal(float value) {
					Instrument instrument = instruments.get((int) value);
					instrument.setMuted(false);
				}
				
				@Override
				public void processSignal(float valueA, float valueB) {}

				@Override
				public void processSignal(float valueA, float valueB, float valueC) {}
				
				@Override
				public void processMessages(ArrayList<String> messages) {}
				
				@Override
				public void processMessage(String message) {}
			});
			
			connector.register("words", new Processor() {
				
				@Override
				public void processSignal(float value) {}
				
				@Override
				public void processSignal(float valueA, float valueB) {}

				@Override
				public void processSignal(float valueA, float valueB, float valueC) {}
				
				@Override
				public void processMessages(ArrayList<String> messages) {}
				
				@Override
				public void processMessage(String message) {
					speechSynthesizer.queueMessage(message);
				}
			});
			
			connector.register("notes", new Processor() {
				
				@Override
				public void processSignal(float value) {
					synthesizer.queueSignal((int) value); 
				}
				
				@Override
				public void processMessage(String message) {}

				@Override
				public void processSignal(float valueA, float valueB) {}
				
				@Override
				public void processSignal(float valueA, float valueB, float valueC) {}
				
				@Override
				public void processMessages(ArrayList<String> messages) {}
			});
			
			connector.register("circles", new Processor() {
				
				@Override
				public void processSignal(float valueA, float valueB, float valueC) {}
				
				@Override
				public void processSignal(float valueA, float valueB) {}
				
				@Override
				public void processSignal(float value) {}
				
				@Override
				public void processMessages(ArrayList<String> messages) {
					int circleID = Integer.parseInt(messages.get(0));
					int circleX = Integer.parseInt(messages.get(1));
					int circleY = Integer.parseInt(messages.get(2));
					int radius = Integer.parseInt(messages.get(3));
					System.out.println("Forwarding signals to instrument 🎷" + circleID);
					
					Instrument instrument = midiForwarderA;
					int channel = circleID - 1; //Circle IDs are 1 based, Instruments 0 based
					int volumeSignal = 1;
					int pitchSignal = 2;
					int pitchFrequencySignal = 3;
					int volume = getVolume(radius);
					int maximumXPos = 1900;
					int pitchX = (int) map(circleX, 0, maximumXPos, MIDI_MAX_VALUE, MIDI_MIN_VALUE);
					int maximumYPos = 980;
					int pitchY = (int) map(circleY, 0, maximumYPos, MIDI_MAX_VALUE, MIDI_MIN_VALUE);
					instrument.queueControlSignal(new ControlSignal(channel, volumeSignal, volume ));
					instrument.queueControlSignal(new ControlSignal(channel, pitchSignal, pitchX ));
					instrument.queueControlSignal(new ControlSignal(channel, pitchFrequencySignal, pitchY ));
				}

				private int getVolume(int radius) {
					int maximumRadius = 1000;
					int minimumRadius = 20;
					float volume = map(radius, minimumRadius, maximumRadius, MIDI_MAX_VALUE, MIDI_MIN_VALUE);
					if(volume > MIDI_MAX_VALUE) return MIDI_MAX_VALUE;
					if(volume < MIDI_MIN_VALUE) return MIDI_MIN_VALUE;
					return Math.round(volume);
				}
				
				@Override
				public void processMessage(String message) {}
			});
			
			connector.register("control", new Processor() {
				
				@Override
				public void processSignal(float valueA, float valueB, float valueC) {}
				
				@Override
				public void processSignal(float valueA, float valueB) {}
				
				@Override
				public void processSignal(float value) {}
				
				@Override
				public void processMessages(ArrayList<String> messages) {}
				
				@Override
				public void processMessage(String message) {
					if(message.equals("reset-words")){
						speechSynthesizer.emptyQueues();
					}
					
					if(message.equals("change-voice")){
						String newVoice = speechSynthesizer.getVoice().equals(SpeechSynthesizer.VOICE_ANNA) ? SpeechSynthesizer.VOICE_ALEX : SpeechSynthesizer.VOICE_ANNA;
						speechSynthesizer.setVoice(newVoice);
					}
				}
			});		
			
		}

	    public void draw() {
	        visualizationEngine.draw();
	    }
	    
	    @Override
	    public void keyPressed(KeyEvent event) {
		    super.keyPressed(event);
		    if(event.getKey() == ' '){
		    	snarePlayer.play();
		    }
		    
		    if(event.getKey() == 'r'){
		    	speechSynthesizer.emptyQueues();
		    }
		    
		    if(event.isShiftDown()){
		    	hornPlayer.play();
		    }
	    }
	    
	    public void mousePressed() {
	    	setPitch();
//	    	Instrument instrument = midiForwarderA;
//	    	int maxValue = 127;
//			int minValue = 0;
//			int value = (int) map(mouseY, 0, CANVAS_HEIGHT, maxValue, minValue);
//			instrument.queueControlSignal(new ControlSignal(3, 3, value));
	    }

		public void setPitch() {
			int pitchValue = (int) map(mouseY, 0, CANVAS_HEIGHT, 100, 0);
	    	speechSynthesizer.setPitch(pitchValue);
		}

	    public static void main(String[] args) {
	        PApplet.main(Main.class.getName());
	    }
}

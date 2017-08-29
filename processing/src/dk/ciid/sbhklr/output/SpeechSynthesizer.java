package dk.ciid.sbhklr.output;

public class SpeechSynthesizer extends Instrument {
	private static final int MIN_WORDS_PER_MINUTE = 25;
	private static final int MAX_WORDS_PER_MINUTE = 450;
	private static final int DEFAULT_WORDS_PER_MINUTE = 175;
	public static final String VOICE_ANNA = "Anna";
	public static final String VOICE_ALEX = "Alex";
	private String voice = VOICE_ANNA;
	private SpeechSynthesis speech;
	private boolean memorizeMessages;
	
	public SpeechSynthesizer(int bpm, boolean memorizeMessages) {
		this.memorizeMessages = memorizeMessages;
		speech = new SpeechSynthesis();
		speech.setWordsPerMinute(DEFAULT_WORDS_PER_MINUTE);
        speech.blocking(false);
        setBPM(bpm);
	}
	
	public SpeechSynthesizer(int bpm, boolean memorizeMessages, String audioDevice) {
		this(bpm, memorizeMessages);
		speech.setAudioDevice(audioDevice);
	}
	
	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}

	@Override
	public void beat(int beatCount) {
		String currentWord = dequeueMessage();

		if(memorizeMessages) {
			queueMessage(currentWord);
		}
		
		if(currentWord != null) {			
			log("* Speech Synthesizer 🗣 saying: " + currentWord + " @ " + beat.getBPM() + " *");
			speech.say(voice, currentWord);
		} else {
			log("* Speech Synthesizer 🗣 has an empty queue. *");
		}
	}

	public void setPitch(int percent) {
		int range = MAX_WORDS_PER_MINUTE - MIN_WORDS_PER_MINUTE;
		int pitchValue = MIN_WORDS_PER_MINUTE + (int) range / 100 * percent;
		log("* Speech Synthesizer 🗣: Setting words per minute to " + pitchValue);
		speech.setWordsPerMinute(pitchValue);
	}

}

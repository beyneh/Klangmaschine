package dk.ciid.sbhklr.input;

import de.hfkbremen.klang.Note;
import de.hfkbremen.klang.Scale;
import dk.ciid.sbhklr.logic.Beat;
import dk.ciid.sbhklr.logic.Timeable;
import dk.ciid.sbhklr.network.Connector;

public class NoteGenerator extends SignalProducer{

	private static final String TAG_NAME = "notes";
	private static final int MESSAGE_SEND_BPM = 128;
	private int counter = 0;
	private boolean useScaleNotes = false;
	
	public boolean useScaleNotes() {
		return useScaleNotes;
	}

	public void setUseScaleNotes(boolean useScaleNotes) {
		this.useScaleNotes = useScaleNotes;
	}

	private int[] scaleNotes = { 
				Scale.note(Scale.MAJOR_CHORD, Note.NOTE_A3, 2),
				Scale.note(Scale.MAJOR_CHORD, Note.NOTE_A3, 3),
				Scale.note(Scale.MAJOR_CHORD, Note.NOTE_A3, 4),
				Scale.note(Scale.MAJOR_CHORD, Note.NOTE_A3, 5),
			};

	private int[] notes = { Note.NOTE_A1, Note.NOTE_A2, Note.NOTE_A3, Note.NOTE_A4 };
	
	public NoteGenerator(Connector connector) {
		super(connector);
		
		new Beat(new Timeable() {
			
			@Override
			public void beat(int beatCount) {
				connector.send(TAG_NAME, pollSignal());
			}
		}, MESSAGE_SEND_BPM);
	}
	
	public NoteGenerator() {}

	@Override
	public Integer pollSignal() {
		int note;
		
		if(useScaleNotes()){
			note = scaleNotes[counter % notes.length];
		} else {
			note = notes[counter % notes.length];
		}
		++counter;
		return note;
	}
	
}

package dk.ciid.sbhklr.input;

import dk.ciid.sbhklr.logic.Beat;
import dk.ciid.sbhklr.logic.Timeable;
import dk.ciid.sbhklr.network.Connector;

public class StringGenerator extends MessageProducer {
	private static final int MESSAGE_SEND_BPM = 64;
	private static final String TAG_NAME = "words";

	private int counter = 0;
	private String[] messages = { "Polyester", "Baumwolle", "Paper" };

	public StringGenerator(Connector connector) {
		super(connector);
		
		new Beat(new Timeable() {
			
			@Override
			public void beat(int beatCount) {
				connector.send(TAG_NAME, pollMessage());
			}
		}, MESSAGE_SEND_BPM);
	}

	@Override
	public String pollMessage() {
		String message = messages[counter % messages.length];
		++counter;
		return message;
	}

}

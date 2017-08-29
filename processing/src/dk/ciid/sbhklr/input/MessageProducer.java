package dk.ciid.sbhklr.input;

import java.util.ArrayList;

import dk.ciid.sbhklr.network.Connector;
import dk.ciid.sbhklr.network.Producer;

public abstract class MessageProducer extends Producer {

	public MessageProducer(Connector connector) {
		super(connector);
	}

	public MessageProducer() {
	}

	public abstract String pollMessage();

	public ArrayList<String> pollMessages(int maxAmount) {
		ArrayList<String> messages = new ArrayList<>();

		for (int i = 0; i < maxAmount; i++) {
			messages.add(pollMessage());
		}
		return messages;
	}
}

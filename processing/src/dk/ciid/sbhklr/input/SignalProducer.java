package dk.ciid.sbhklr.input;

import java.util.ArrayList;

import dk.ciid.sbhklr.network.Connector;
import dk.ciid.sbhklr.network.Producer;

public abstract class SignalProducer extends Producer {
	public abstract Integer pollSignal();

	public SignalProducer(Connector connector) {
		super(connector);
	}
	
	public SignalProducer() {}
	
	public ArrayList<Integer> pollSignals(int maxAmount) {
		ArrayList<Integer> singals = new ArrayList<>();
		
		for (int i = 0; i < maxAmount; i++) {
			singals.add(pollSignal());
		}
		return  singals;
	}
}

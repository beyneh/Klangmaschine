package dk.ciid.sbhklr.network;

import java.util.ArrayList;

public interface Processor {
	public void processMessage(String message);
	public void processMessages(ArrayList<String> messages);
	public void processSignal(float value);
	public void processSignal(float valueA, float valueB);
	public void processSignal(float valueA, float valueB, float valueC);
}

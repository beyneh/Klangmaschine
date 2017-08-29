package dk.ciid.sbhklr.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hfkbremen.netzwerk.NetzwerkClient;
import oscP5.OscMessage;

public class Connector {
	private NetzwerkClient client;
	private HashMap<String, ArrayList<Processor> > subscribers = new HashMap<>();

	public Connector(String serverName, String clientName) {
		client = new NetzwerkClient(this, serverName, clientName);
		client.connect();
	}
	
	public void send(String tag, ArrayList<String> messages){
		for (String message : messages) {
			send(tag, message);
		}
	}
	
	public void send(String tag, String message){
		System.out.println("* Sending message ✉️ " + message + " for Tag 🏷 " + tag + " *");
		client.send(tag, message);		
	}
	
	public void send(String tag, float value){
		System.out.println("* Sending signal 〰️️ " + value + " for Tag 🏷 " + tag + " *");
		client.send(tag, value);
	}
	
	public void receive(String name, String tag, String message) {
		ArrayList<Processor> tagSubscribers = subscribers.get(tag);
		System.out.println("* Receiving message ✉️ " + message + " for Tag 🏷 " + tag + " from " + name + " *");
		
		if(message.contains(";")){
			ArrayList<String> components = new ArrayList<String>(Arrays.asList(message.split(";")));
			for (Processor processor : tagSubscribers) {
				processor.processMessages(components);
			}
		} else {
			for (Processor processor : tagSubscribers) {
				processor.processMessage(message);
			}			
		}
    }
	
	public void receive_raw(OscMessage message){
		throw new UnsupportedOperationException();
	}
	
	public void receive(String name, String tag, float value) {
		System.out.println("* Receiving signal 〰️ " + value + " for Tag 🏷 " + tag + " from " + name + " *");
		ArrayList<Processor> tagSubscribers = subscribers.get(tag);
		for (Processor processor : tagSubscribers) {
			processor.processSignal(value);
		}
    }
	
	public void receive(String name, String tag, float value1, float value2) {
		System.out.println("* Receiving signal 〰️ " + value1 + "/" + value2 + " for Tag 🏷 " + tag + " from " + name + " *");
		ArrayList<Processor> tagSubscribers = subscribers.get(tag);
		for (Processor processor : tagSubscribers) {
			processor.processSignal(value1, value2);
		}
    }
	
	public void receive(String name, String tag, float value1, float value2, float value3) {
		System.out.println("* Receiving signal 〰️ " + value1 + "/" + value2 + "/" + value3 + " for Tag 🏷 " + tag + " from " + name + " *");
		ArrayList<Processor> tagSubscribers = subscribers.get(tag);
		for (Processor processor : tagSubscribers) {
			processor.processSignal(value1, value2, value3);
		}
    }
	
	public void register(String tag, Processor processor){
		ArrayList<Processor> tagSubscribers = subscribers.get(tag);
		if(tagSubscribers == null){
			tagSubscribers = new ArrayList<>();
			subscribers.put(tag, tagSubscribers);
		}
		if(!tagSubscribers.contains(processor)){			
			tagSubscribers.add(processor);
		}
	}
	
	public void deregister(String tag, Processor processor) {
		ArrayList<Processor> tagSubscribers = subscribers.get(tag);
		if(tagSubscribers != null && tagSubscribers.contains(processor)){
			tagSubscribers.remove(processor);
		}
	}
	
}

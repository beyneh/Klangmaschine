package dk.ciid.sbhklr.output;

import java.util.ArrayList;
import java.util.LinkedList;

import dk.ciid.sbhklr.logic.Beat;
import dk.ciid.sbhklr.logic.Timeable;

public abstract class Instrument implements Timeable {
	
	private LinkedList<Integer> signals;
	private LinkedList<ControlSignal> controlSignals;
	private LinkedList<String> messages;
	protected Beat beat;
	private boolean isMuted = false;
	private boolean showLog = false;
	
	public void setShowLog(boolean showLog) {
		this.showLog = showLog;
	}

	protected void log(String message) {
		if(!showLog) return;
		System.out.println(message);
	}
	
	public boolean isMuted() {
		return isMuted;
	}

	public void setMuted(boolean isMuted) {
		this.isMuted = isMuted;
	}
	
	public boolean hasBeat(){
		return beat != null;
	}

	public Instrument() {
		signals = new LinkedList<>();
		controlSignals = new LinkedList<>();
		messages = new LinkedList<>();
	}
	
	public void setBPM(int bpm) {
		if(beat == null){
			beat = new Beat(this);
		}
		beat.bpm(bpm);
	}
	
	public void emptyQueues(){
		signals = new LinkedList<>();
		controlSignals = new LinkedList<>();
		messages = new LinkedList<>();
	}
	
	public void queueControlSingals(ArrayList<Integer> signals) {
		for (ControlSignal controlSignal : controlSignals) {
			queueControlSignal(controlSignal);
		}
	}
	
	public void queueControlSignal(ControlSignal signal){
		controlSignals.add(signal);
	}
	
	protected ControlSignal dequeueControlSignal(){
		return controlSignals.poll();
	}
	
	protected ControlSignal peekControlSignal(){
		return controlSignals.peek();
	}
	
	protected synchronized boolean hasControlSignals(){
		return !controlSignals.isEmpty();
	}
	
	public void queueSingals(ArrayList<Integer> signals) {
		for (Integer signal : signals) {
			queueSignal(signal);
		}
	}
	
	public void queueSignal(Integer signal){
		signals.add(signal);
	}
	
	protected Integer dequeueSignal(){
		return signals.poll();
	}
	
	protected Integer peekSignal(){
		return signals.peek();
	}
	
	protected synchronized boolean hasSignals(){
		return !signals.isEmpty();
	}

	public void queueMessages(ArrayList<String> messages) {
		for (String message : messages) {
			queueMessage(message);
		}
	}
	
	public void queueMessage(String message){
		messages.add(message);
	}
	
	protected String dequeueMessage(){
		return messages.poll();
	}
	
	protected String peekMessage(){
		return messages.peek();
	}
	
	protected synchronized boolean hasMessages(){
		return !messages.isEmpty();
	}
}

package dk.ciid.sbhklr.visuals;

import processing.core.PApplet;

public class RandomBackgroundEffect extends Effect {

	private int beat;
	private double lastAppliedTime;

	public RandomBackgroundEffect(int beat) {
		this.beat = beat;
	}

	public void apply(PApplet applet) {
		double playTime = playTime(applet);
		
		if(playTime >= lastAppliedTime + (60.0f / beat)){			
			applet.background(applet.random(255), applet.random(255), applet.random(255));
			lastAppliedTime = playTime;
		}
	}
}

package dk.ciid.sbhklr.visuals;

import processing.core.PApplet;

public class BitsZoomEffect extends Effect {

	private String message;

	public BitsZoomEffect(String message) {
		this.message = message;
	}

	public void apply(PApplet applet) {
		double playTime = playTime(applet);
		
//		if(playTime >= lastAppliedTime + (60.0f / beat)){			
//			applet.background(applet.random(255), applet.random(255), applet.random(255));
//			lastAppliedTime = playTime;
//		}
	}
}

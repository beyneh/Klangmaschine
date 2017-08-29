package dk.ciid.sbhklr.visuals;

import processing.core.PApplet;

public abstract class Effect {
	abstract void apply(PApplet applet);

	protected double playTime(PApplet applet) {
		return applet.frameCount / applet.frameRate;
	}
}

package dk.ciid.sbhklr.visuals;

import java.util.ArrayList;

import processing.core.PApplet;

public class VisualizationEngine {
	private static final int WHITE_COLOR = 255;
	private PApplet applet;
	private ArrayList<Effect> effects = new ArrayList<>();
	
	public VisualizationEngine(PApplet applet) {
		this.applet = applet;
		this.applet.background(WHITE_COLOR);
	}
	
	public void draw(){
		for (Effect effect : effects) {
			effect.apply(applet);
		}
	}

	public void addEffect(Effect effect) {
		effects.add(effect);
	}
}

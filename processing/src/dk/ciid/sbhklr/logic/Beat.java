package dk.ciid.sbhklr.logic;

import java.util.Timer;
import java.util.TimerTask;

public class Beat {

    private int beatCount = -1;
    private Timeable timeable;
    private final Timer timer;
    private TimerTask task;
	private float bpm;

    public Beat(Timeable timeable, int bpm) {
        this(timeable);
        bpm(bpm);
    }

    public Beat(Timeable timeable) {
        this.timeable = timeable;
        timer = new Timer();
    }

    public void bpm(float bpm) {
    	this.bpm = bpm;
        final int period = (int) (60.0f / bpm * 1000.0f);
        if (task != null) {
            task.cancel();
        }
        task = new BeatTimerTask();
        timer.scheduleAtFixedRate(task, 1000, period);
    }

    public class BeatTimerTask extends TimerTask {

        @Override
        public void run() {
        	beatCount++;
        	timeable.beat(beatCount);
        }
    }

	public float getBPM() {
		return bpm;
	}
}

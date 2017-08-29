package dk.ciid.sbhklr.network;

public abstract class Producer {
	protected Connector connector;
	
	public Producer(Connector connector) {
		this.connector = connector;
	}
	
	public Producer() {}
}

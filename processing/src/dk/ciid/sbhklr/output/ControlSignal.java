package dk.ciid.sbhklr.output;

public class ControlSignal {
	private Integer channel;
	private Integer number;
	private Integer value;
	
	public ControlSignal(Integer channel, Integer number, Integer value) {
		this.channel = channel;
		this.number = number;
		this.value = value;
	}

	public Integer getChannel() {
		return channel;
	}
	
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
}

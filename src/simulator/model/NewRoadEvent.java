package simulator.model;

public abstract class NewRoadEvent extends Event {
	String id;
	String srcJun;
	String destJunc;
	int length;
	int co2Limit;
	int maxSpeed;
	Weather weather;
	RoadMap map;

	public NewRoadEvent(int time, String id, String srcJun, String destJunc, int length, int co2Limit, int maxSpeed,
			Weather weather) {
		super(time);
		this.id = id;
		this.srcJun = srcJun;
		this.destJunc = destJunc;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.weather = weather;
	}

	@Override
	void execute(RoadMap map) {
		this.map = map;
		map.addRoad(createRoadObject());
	}

	@Override
	public String toString() {
		return "New Road '"+id+"'";		
	}
	
	abstract Road createRoadObject();

}

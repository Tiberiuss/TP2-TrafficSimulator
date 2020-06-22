package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.Weather;

public abstract class NewRoadEventBuilder extends Builder<Event> {
	int time;
	String id;
	String srcJun;
	String destJunc;
	int length;
	int co2Limit;
	int maxSpeed;
	Weather weather;

	public NewRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {		
		time = data.getInt("time");
		id = data.getString("id");
		srcJun = data.getString("src");
		destJunc = data.getString("dest");
		length = data.getInt("length");
		co2Limit = data.getInt("co2limit");
		maxSpeed = data.getInt("maxspeed");
		weather = Weather.valueOf(data.getString("weather"));

		return createTheRoad();
	}

	abstract Event createTheRoad();
}

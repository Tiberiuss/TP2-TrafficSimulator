package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event> {

	public NewVehicleEventBuilder() {
		super("new_vehicle");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time;
		String id;

		int maxSpeed;
		int contClass;
		List<String> itinerary = new ArrayList<String>();

		time = data.getInt("time");
		id = data.getString("id");
		maxSpeed = data.getInt("maxspeed");
		contClass = data.getInt("class");

		JSONArray ja = data.getJSONArray("itinerary");		
		for (int i = 0; i < ja.length(); i++) {
			itinerary.add(ja.getString(i));
		}

		return new NewVehicleEvent(time, id, maxSpeed, contClass, itinerary);
	}

}

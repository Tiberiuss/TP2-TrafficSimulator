package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event> {

	public SetWeatherEventBuilder() {
		super("set_weather");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time;
		List<Pair<String, Weather>> ws = new ArrayList<Pair<String,Weather>>();

		time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		for (int i = 0; i < ja.length(); i++) {
			String road = ja.getJSONObject(i).getString("road");
			Weather weather = Weather.valueOf(ja.getJSONObject(i).getString("weather"));
			Pair<String, Weather> pair = new Pair<String, Weather>(road, weather);
			ws.add(pair);
		}

		return new NewSetWeatherEvent(time, ws);
	}

}

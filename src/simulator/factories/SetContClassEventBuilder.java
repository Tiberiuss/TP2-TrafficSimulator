package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event> {

	public SetContClassEventBuilder() {
		super("set_cont_class");
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time;
		List<Pair<String, Integer>> cs = new ArrayList<Pair<String, Integer>>();

		time = data.getInt("time");
		JSONArray ja = data.getJSONArray("info");
		for (int i = 0; i < ja.length(); i++) {
			String vehicle = ja.getJSONObject(i).getString("vehicle");
			int contClass = ja.getJSONObject(i).getInt("class");
			Pair<String, Integer> pair = new Pair<String, Integer>(vehicle, contClass);
			cs.add(pair);
		}

		return new NewSetContClassEvent(time, cs);
	}

}

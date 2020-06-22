package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event> {
	private Factory<LightSwitchingStrategy> lssFactory;
	private Factory<DequeuingStrategy> dqsFactory;

	public NewJunctionEventBuilder(Factory<LightSwitchingStrategy> lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super("new_junction");
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		int time;
		String id;
		int xCoor;
		int yCoor;		
		LightSwitchingStrategy lsStrategy;
		DequeuingStrategy dqStrategy;
		
		
		time = data.getInt("time");
		id = data.getString("id");
		JSONArray ja = data.getJSONArray("coor");
		xCoor = ja.getInt(0);
		yCoor = ja.getInt(1);
		lsStrategy = lssFactory.createInstance(data.getJSONObject("ls_strategy"));
		dqStrategy = dqsFactory.createInstance(data.getJSONObject("dq_strategy"));

		return new NewJunctionEvent(time, id, lsStrategy, dqStrategy, xCoor, yCoor);
	}

}

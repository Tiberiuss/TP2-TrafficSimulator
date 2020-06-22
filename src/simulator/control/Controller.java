package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator ts;
	private Factory<Event> factory;

	public Controller(TrafficSimulator ts, Factory<Event> factory) {
		if (ts == null || factory == null)
			throw new NullPointerException("Los parametros del controlador no pueden ser nulos.");
		else {
			this.ts = ts;
			this.factory = factory;
		}
	}

	public void loadEvents(InputStream in) throws IllegalArgumentException {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		JSONArray events;

		if (jo.has("events")) {
			events = jo.getJSONArray("events");

			for (int i = 0; i < events.length(); i++) {
				Event e = factory.createInstance(events.getJSONObject(i));
				ts.addEvent(e);
			}

		} else {
			throw new IllegalArgumentException(
					"La entrada no es correcta, entrada esperada: { “events”: [e1,...,en] }");
		}
	}

	public void run(int n, OutputStream out) throws Exception {
		PrintStream p = new PrintStream(out);
		JSONObject jo = new JSONObject();
		JSONArray ja = new JSONArray();

		for (int i = 0; i < n; i++) {
			ts.advance();
			ja.put(ts.report());
		}

		jo.put("states", ja);

		p.println(jo.toString(1));

	}

	public void run(int n) throws Exception  {	
		ts.advance();
	}

	public void reset() {
		ts.reset();
	}

	public void addObserver(TrafficSimObserver o) {
		ts.addObserver(o);
	}

	public void removeObserver(TrafficSimObserver o) {
		ts.removeObserver(o);
	}

	public void addEvent(Event e) {
		ts.addEvent(e);
	}
}

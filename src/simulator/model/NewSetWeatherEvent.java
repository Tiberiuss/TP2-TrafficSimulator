package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetWeatherEvent extends Event {
	private List<Pair<String, Weather>> ws;

	public NewSetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
		super(time);
		this._time = time;
		this.ws = ws;
	}

	@Override
	void execute(RoadMap map) throws IllegalArgumentException {
		if (ws == null) {
			throw new IllegalArgumentException("No es posible cambiar al clima indicado.");
		} else {
			for (Pair<String, Weather> w : ws) {
				Road r = map.getRoad(w.getFirst());
				if (r != null)
					r.setWeather(w.getSecond());
				else
					throw new IllegalArgumentException("No se puede cambiar el clima de la carretera (Carretera no existe).");
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Change Weather: [");
		int index = 0;
		for (Pair<String, Weather> c : ws) {
			sb.append("(" + c.getFirst() + "," + c.getSecond() + ")");
			if (index < ws.size() - 1) {
				sb.append(", ");
			}
			index++;
		}
		sb.append("]");

		return sb.toString();
	}

}

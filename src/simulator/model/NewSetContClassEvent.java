package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetContClassEvent extends Event {
	private List<Pair<String, Integer>> cs;

	public NewSetContClassEvent(int time, List<Pair<String, Integer>> cs) {
		super(time);
		this._time = time;
		this.cs = cs;
	}

	@Override
	void execute(RoadMap map) throws NullPointerException, IllegalArgumentException {
		if (cs == null) {
			throw new NullPointerException("Cambio de clase de vehiculo no es posible.");
		} else {
			for (Pair<String, Integer> c : cs) {
				Vehicle v = map.getVehicle(c.getFirst());
				if (v != null)
					v.setContaminationClass(c.getSecond());
				else
					throw new IllegalArgumentException("No se puede cambiar la clase del vehiculo (Vehiculo no existe).");
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Change CO2 class: [");
		int index = 0;
		for (Pair<String, Integer> c : cs) {
			sb.append("(" + c.getFirst() + "," + c.getSecond() + ")");
			if (index < cs.size() - 1) {
				sb.append(", ");
			}
			index++;
		}
		sb.append("]");

		return sb.toString();
	}

}

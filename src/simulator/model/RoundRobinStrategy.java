package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchingStrategy {

	private int timeSlot;

	public RoundRobinStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int index;

		if (roads.isEmpty())
			index = -1;
		else if (currGreen == -1)
			index = 0;
		else if (currTime - lastSwitchingTime < timeSlot)
			index = currGreen;
		else
			index = (currGreen + 1) % roads.size(); // el índice de la siguiente carretera entrante, recorriendo de
													// forma
													// circular
		return index;
	}
}

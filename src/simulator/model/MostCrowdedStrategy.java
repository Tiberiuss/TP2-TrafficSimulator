package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		this.timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int index = -2;

		if (roads.isEmpty())
			index = -1;
		else if (currGreen == -1) {
			index = 0;
			int maxLength = 0;
			for (int i = 0; i < qs.size(); i++) {
				List<Vehicle> queue = qs.get(i);
				if (queue.size() > maxLength) {
					maxLength = queue.size();
					index = i;
				}
			}
		} else if (currTime - lastSwitchingTime < timeSlot)
			index = currGreen;
		else {
			List<Vehicle> queue;
			int currentPos = (currGreen + 1) % roads.size();
			int maxLength = -1;

			for (int i = 0; i < qs.size(); i++, currentPos = (currentPos + 1) % qs.size()) {
				queue = qs.get(currentPos);
				if (queue.size() > maxLength) {
					maxLength = queue.size();
					index = currentPos;
				}				
			}

		}

		return index;
	}

}

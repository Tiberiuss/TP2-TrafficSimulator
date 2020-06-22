package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class MoveAllStrategy implements DequeuingStrategy {

	public MoveAllStrategy() {	
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> vehiclesToMove = new ArrayList<Vehicle>();
		vehiclesToMove.addAll(q);

		return vehiclesToMove;
	}

}

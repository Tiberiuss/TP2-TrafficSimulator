package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VipStrategy implements DequeuingStrategy {
	private String vipTag;
	private int limit;

	public VipStrategy(String vipTag, int limit) {
		this.vipTag = vipTag;
		this.limit = limit;
	}

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> vehiclesToMove = new ArrayList<Vehicle>();

		Iterator<Vehicle> itr = q.iterator();

		while (itr.hasNext() && vehiclesToMove.size() < limit) {
			Vehicle v = itr.next();
			if (v._id.endsWith(vipTag)) {
				vehiclesToMove.add(v);				
			}
		}

		if (vehiclesToMove.size() < limit) {
			itr = q.iterator();

			while (itr.hasNext() && vehiclesToMove.size() < limit) {
				Vehicle v = itr.next();
				if (!v._id.endsWith(vipTag)) {
					vehiclesToMove.add(v);
				}
			}
		}

		return vehiclesToMove;
	}

}

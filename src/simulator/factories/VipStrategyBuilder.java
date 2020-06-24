package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.VipStrategy;

public class VipStrategyBuilder extends Builder<DequeuingStrategy> {

	public VipStrategyBuilder() {
		super("vip_dqs");	
	}

	@Override
	protected DequeuingStrategy createTheInstance(JSONObject data) {
		int limit = data.has("limit") ? data.getInt("limit") : 1; // default=1
		String vipTag;
		
		if(data.has("viptag")) {
			vipTag=data.getString("viptag");	//Puede ser cadena vacia?
		}else {
			throw new IllegalArgumentException("Vip tag not found.");
		}
		
		return new VipStrategy(vipTag,limit);
	}

}



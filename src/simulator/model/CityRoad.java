package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		int reducir;
		if (condicionesAmbientales == Weather.WINDY || condicionesAmbientales == Weather.STORM) {
			reducir = 10;
		} else {
			reducir = 2;
		}
		if (contaminacionTotal - reducir >= 0)
			contaminacionTotal -= reducir;
		else
			contaminacionTotal=0;
	}

	@Override
	public void updateSpeedLimit() {
		// La velocidad l�mite no cambia, siempre es la velocidad m�xima.
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int speed;
		int f = v.getContClass();
		int s = limiteActualVelocidad;

		speed = (int) (((11.0 - f) / 11.0) * s);
		//speed = (int) Math.ceil(((11.0 - f) / 11.0) * s);	//TODO MATH CEIL
		return speed;
	}

}

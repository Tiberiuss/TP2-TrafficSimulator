package simulator.model;

public class InterCityRoad extends Road {

	public InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	public void reduceTotalContamination() {
		contaminacionTotal = (int) (((100.0 - condicionesAmbientales.getX()) / 100.0) * contaminacionTotal);
	}

	@Override
	public void updateSpeedLimit() {
		if (contaminacionTotal > limiteContaminacion)
			limiteActualVelocidad = ((int) (velocidadMax * 0.5));
		else
			limiteActualVelocidad = velocidadMax;
	}

	@Override
	public int calculateVehicleSpeed(Vehicle v) {
		int speed;

		if (condicionesAmbientales == Weather.STORM)
			speed = (int) (limiteActualVelocidad * 0.8);
		else
			speed = limiteActualVelocidad;

		return speed;
	}

}

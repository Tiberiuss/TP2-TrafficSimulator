package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public abstract class Road extends SimulatedObject {

	private Junction cruceOrigen;
	private Junction cruceDestino;
	private int longitud;
	int velocidadMax;// vMax de la carretera
	int limiteActualVelocidad;// limite actual por la contaminacion, inicialmente igual a vMax
	int limiteContaminacion;
	Weather condicionesAmbientales;
	int contaminacionTotal;
	private List<Vehicle> vehiculos;
	/*
	 * debe estar siempre ordenada por la localización de los vehículos (orden
	 * descendente).
	 */

	Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id);

		if (maxSpeed < 0)
			throw new IllegalArgumentException("La velocidad de la carretera no puede ser negativa.");

		if (contLimit < 0)
			throw new IllegalArgumentException("El valor de la contaminación no puede ser negativo.");

		if (length < 0)
			throw new IllegalArgumentException("La longitud de la carretera debe ser positiva.");

		if (srcJunc == null)
			throw new NullPointerException("El cruce origen no existe.");

		if (destJunc == null)
			throw new NullPointerException("El cruce destino no existe.");

		if (weather == null)
			throw new NullPointerException("El clima indicado no existe.");

		velocidadMax = maxSpeed;
		limiteActualVelocidad = velocidadMax;
		limiteContaminacion = contLimit;
		longitud = length;
		condicionesAmbientales = weather;
		cruceOrigen = srcJunc;
		cruceDestino = destJunc;
		vehiculos = new SortedArrayList<Vehicle>(new Comparator<Vehicle>() {
			@Override
			public int compare(Vehicle v1, Vehicle v2) {
				return Integer.compare(v1.getLocation(), v2.getLocation());
			}
		});
		cruceDestino.addIncomingRoad(this);
		cruceOrigen.addOutGoingRoad(this);

	}

	public List<Vehicle> getVehicles() {
		// Devolver list de solo lectura
		return Collections.unmodifiableList(new ArrayList<>(vehiculos));
	}

	public int getLength() {
		return longitud;
	}

	public Junction getDest() {
		return cruceDestino;
	}

	public Junction getSrc() {
		return cruceOrigen;
	}

	void setWeather(Weather w) {
		if (w == null)
			throw new NullPointerException("Cilma no disponible");
		condicionesAmbientales = w;

	}

	public void addContamination(int c) {

		if (c < 0)
			throw new IllegalArgumentException("Incremento de contaminación no puede ser negativo.");
		else
			contaminacionTotal += c;

	}

	void enter(Vehicle v) {

		if (v.getLocation() == 0 && v.getSpeed() == 0) {
			vehiculos.add(v);
		} else
			throw new IllegalArgumentException("El vehiculo debe estar parado en la posicion inicial.");

	}

	void exit(Vehicle v) {
		vehiculos.remove(v);
	}

	@Override
	void advance(int time) {
		reduceTotalContamination();
		updateSpeedLimit();

		for (Vehicle v : vehiculos) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);
		jo.put("speedlimit", limiteActualVelocidad);
		jo.put("weather", condicionesAmbientales);
		jo.put("co2", contaminacionTotal);

		JSONArray jsonVehiclesId = new JSONArray();
		for (Vehicle v : vehiculos) {
			jsonVehiclesId.put(v._id);
		}
		jo.put("vehicles", jsonVehiclesId);

		return jo;
	}

	public abstract void reduceTotalContamination();

	public abstract void updateSpeedLimit();

	public abstract int calculateVehicleSpeed(Vehicle v);

	public Weather getWeather() {
		return condicionesAmbientales;
	}

	public int getMaxSpeed() {
		return velocidadMax;
	}

	public int getSpeedLimit() {
		return limiteActualVelocidad;
	}

	public int getTotalCO2() {
		return contaminacionTotal;
	}

	public int getCO2Limit() {
		return limiteContaminacion;
	}

}

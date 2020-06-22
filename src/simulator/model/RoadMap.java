package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	private List<Junction> listaCruces;
	private List<Road> listaCarreteras;
	private List<Vehicle> listaVehiculos;
	private Map<String, Junction> mapaCruces;
	private Map<String, Road> mapaCarreteras;
	private Map<String, Vehicle> mapaVehiculos;

	RoadMap() {

		this.listaCruces = new ArrayList<Junction>();
		this.listaCarreteras = new ArrayList<Road>();
		this.listaVehiculos = new ArrayList<Vehicle>();
		this.mapaCruces = new HashMap<String, Junction>();
		this.mapaCarreteras = new HashMap<String, Road>();
		this.mapaVehiculos = new HashMap<String, Vehicle>();

	}

	void addJunction(Junction j) {
		if (mapaCruces.get(j._id) == null) {
			listaCruces.add(j);
			mapaCruces.put(j._id, j);
		} else {
			throw new IllegalArgumentException("Ya existe un cruce con ese identificador.");
		}

	}

	void addRoad(Road r) {

		if (mapaCarreteras.get(r._id) == null && mapaCruces.get(r.getSrc()._id) != null
				&& mapaCruces.get(r.getDest()._id) != null) {
			listaCarreteras.add(r);
			mapaCarreteras.put(r._id, r);

		} else
			throw new IllegalArgumentException("No se puede añadir la carretera al mapa de carreteras.");

	}

	void addVehicle(Vehicle v) {

		// Mejor usar contains
		if (!mapaVehiculos.containsKey(v._id)) {

			for (int i = 0; i < v.getItinerario().size() - 1; i++) {
				if (v.getItinerario().get(i).roadTo(v.getItinerario().get(i + 1)) == null)
					throw new IllegalArgumentException("No se puede añadir (" + v._id + ") al mapa de vehiculos. ");
			}

			listaVehiculos.add(v);
			mapaVehiculos.put(v._id, v);
		} else
			throw new IllegalArgumentException("No se pueden procesar vehiculos duplicados.");

		/*
		 * Debes comprobar que los siguientes puntos se cumplen:
		 * 
		 * (i) no existe ningún otro vehículo con el mismo identificador;
		 * 
		 * (ii) el itinerario es válido, es decir, existen carreteras que conecten los
		 * cruces consecutivos de su itinerario.
		 * 
		 * En caso de que no se cumplan (i) y (ii), el método debe lanzar una excepción.
		 * 
		 */

	}

	public Junction getJuction(String id) {
		return mapaCruces.get(id);
	}

	public Road getRoad(String id) {
		return mapaCarreteras.get(id);
	}

	public Vehicle getVehicle(String id) {
		return mapaVehiculos.get(id);
	}

	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(new ArrayList<>(listaCruces));
	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(new ArrayList<>(listaCarreteras));
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(new ArrayList<>(listaVehiculos));
	}

	void reset() {
		listaCruces.clear();
		listaCarreteras.clear();
		listaVehiculos.clear();
		mapaCruces.clear();
		mapaCarreteras.clear();
		mapaVehiculos.clear();
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();
		JSONArray junctionReport = new JSONArray();
		JSONArray roadReport = new JSONArray();
		JSONArray vehicleReport = new JSONArray();

		for (Junction j : listaCruces) {
			junctionReport.put(j.report());
		}

		for (Road r : listaCarreteras) {
			roadReport.put(r.report());
		}

		for (Vehicle v : listaVehiculos) {
			vehicleReport.put(v.report());
		}

		jo.put("junctions", junctionReport);
		jo.put("roads", roadReport);
		jo.put("vehicles", vehicleReport);

		return jo;
	}

}

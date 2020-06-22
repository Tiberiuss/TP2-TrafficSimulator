package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {
	private int xCoor;
	private int yCoor;
	private List<Road> carreterasEntrantes;
	private Map<Junction, Road> carreterasSalientes;
	private List<List<Vehicle>> listaColas;
	private int semaforoVerde;
	private int pasoCambioSemaforo;
	private LightSwitchingStrategy lsStrategy;
	private DequeuingStrategy dqStrategy;

	Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(id);

		if (lsStrategy == null || dqStrategy == null) {
			throw new IllegalArgumentException("Estrategia no existe.");
		}

		if (xCoor < 0 || yCoor < 0) {
			throw new IllegalArgumentException("Las coordenadas están fuera de los límites.");
		}

		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		this.carreterasEntrantes = new ArrayList<Road>();
		this.carreterasSalientes = new HashMap<Junction, Road>();
		this.listaColas = new ArrayList<List<Vehicle>>();
		this.semaforoVerde = -1;
		this.pasoCambioSemaforo = 0;
		/*
		 * Observa que la constructora recibe las estrategias como parámetros. Debe
		 * comprobar que lsStrategy y dqStrategy no son null, y que xCoor y yCoor no son
		 * negativos, y lanzar una excepción en caso contrario.
		 * 
		 **/

	}

	public int getX() {
		return xCoor;
	}

	public int getY() {
		return yCoor;
	}

	void addIncomingRoad(Road r) {

		if (r.getDest()._id == this._id) {
			carreterasEntrantes.add(r);

			List<Vehicle> cola = new LinkedList<Vehicle>();
			cola.addAll(r.getVehicles());
			listaColas.add(cola);

		} else {
			throw new IllegalArgumentException("El destino de la carretera entrante (" + r.getDest()._id
					+ ") no es el que se esperaba (" + this._id + "). ");
		}

		/*
		 * Añadirlo a carreteras entrantes y lanzar excepcion
		 * 
		 * Debes comprobar que la carretera r es realmente una carretera entrante, es
		 * decir, su cruce destino es igual al cruce actual y, lanzar una excepción en
		 * caso contrario.
		 * 
		 */
	}

	void addOutGoingRoad(Road r) {

		if (r.getSrc()._id == this._id && carreterasSalientes.get(r.getDest()) == null) {
			carreterasSalientes.put(r.getDest(), r);
		} else {
			throw new IllegalArgumentException("No se puede añadir esta carretera a la lista de carreteras salientes.");
		}

		/*
		 * Añade el par (j,r) al mapa de carreteras salientes, donde j es el cruce
		 * destino de la carretera r. Tienes que comprobar que ninguna otra carretera va
		 * desde this al cruce j y, que la carretera r, es realmente una carretera
		 * saliente al cruce actual. En otro caso debes lanzar una excepción
		 * 
		 */
	}

	void enter(Vehicle v) {
		listaColas.get(carreterasEntrantes.indexOf(v.getRoad())).add(v);
		// v.getCarretera().enter(v);
	}

	Road roadTo(Junction j) {
		return carreterasSalientes.get(j);

	}

	@Override
	void advance(int time) {

		if (semaforoVerde != -1) {
			List<Vehicle> lista = listaColas.get(semaforoVerde);
			if (!lista.isEmpty()) {
				List<Vehicle> avanzarVehiculos = dqStrategy.dequeue(lista);
				for (Vehicle v : avanzarVehiculos) {
					v.moveToNextRoad();
					lista.remove(v);
				}
			}
		}
		
		int siguienteVerde = lsStrategy.chooseNextGreen(carreterasEntrantes, listaColas, semaforoVerde,
				pasoCambioSemaforo, time);
		
		if (siguienteVerde != -1 && siguienteVerde != semaforoVerde) {
			pasoCambioSemaforo = time;
			semaforoVerde = siguienteVerde;
		}
	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);

		jo.put("green", semaforoVerde > -1 ? carreterasEntrantes.get(semaforoVerde) : "none");

		JSONArray queues = new JSONArray();

		for (int i = 0; i < listaColas.size(); i++) {
			JSONObject q = new JSONObject();
			JSONArray vehicles = new JSONArray();

			for (Vehicle v : listaColas.get(i)) {
				vehicles.put(v);
			}

			q.put("road", carreterasEntrantes.get(i));
			q.put("vehicles", vehicles);
			queues.put(q);
		}
		jo.put("queues", queues);

		return jo;
	}

	public int getGreenLightIndex() {
		return semaforoVerde;
	}

	public List<List<Vehicle>> getQueues() {
		return listaColas;
	}

	public List<Road> getInRoads() {
		return carreterasEntrantes;
	}

}

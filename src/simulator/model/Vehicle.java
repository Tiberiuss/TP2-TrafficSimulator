package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class Vehicle extends SimulatedObject {
	private List<Junction> itinerario;
	private int cruceActual;
	private int velocidadMax;
	private int velocidadActual;
	private VehicleStatus estado;
	private Road carretera;
	private int localizacion;
	private int gradoContaminacion;
	private int contaminacionTotal;
	private int distanciaRecorrida;

	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws IllegalArgumentException {
		super(id);

		if (maxSpeed < 0) {
			throw new IllegalArgumentException("La velocidad debe ser positiva");
		}

		if (contClass < 0 || contClass > 10) {
			throw new IllegalArgumentException("El valor de la contaminacion debe estar entre 1 y 10.");
		}

		if (itinerary.size() < 2) {
			throw new IllegalArgumentException("El tamaño del itinerio es muy reducido.");
		}
		// Lista de solo lectura
		itinerario = Collections.unmodifiableList(new ArrayList<>(itinerary));
		velocidadMax = maxSpeed;
		gradoContaminacion = contClass;

		estado = VehicleStatus.PENDING;
		carretera = null;
		velocidadActual = 0;
		localizacion = 0;
		contaminacionTotal = 0;
		distanciaRecorrida = 0;
		cruceActual = 0;

	}

	@Override
	void advance(int time) {
		if (estado == VehicleStatus.TRAVELING) {

			int localizacionPrevia = localizacion;

			/*
			 * valor mï¿½nimo entre (i) la localizaciï¿½n actual mï¿½s la velocidad actual; y (ii)
			 * la longitud de la carretera por la que estï¿½ circulando
			 */

			localizacion = Math.min((localizacionPrevia + velocidadActual), carretera.getLength());
			distanciaRecorrida += localizacion - localizacionPrevia;
			/*
			 * calcula la contaminaciï¿½n c producida utilizando la fï¿½rmula c = l * f,
			 * 
			 * f es el grado de contaminaciï¿½n
			 * 
			 * l es la distancia recorrida en ese paso de la simulaciï¿½n, es decir, la nueva
			 * localizaciï¿½n menos la localizaciï¿½n previa.
			 * 
			 * Despuï¿½s aï¿½ade c a la contaminaciï¿½n total del vehï¿½culo y tambiï¿½n aï¿½ade c al
			 * grado de contaminaciï¿½n de la carretera actual, invocando al mï¿½todo
			 * correspondiente de la clase Road.
			 */

			int c = (localizacion - localizacionPrevia) * gradoContaminacion;
			contaminacionTotal += c;
			carretera.addContamination(c);

			if (localizacion >= carretera.getLength()) {
				// Estado pending o waiting??
				estado = VehicleStatus.WAITING;
				velocidadActual = 0;				
				itinerario.get(cruceActual).enter(this);
			}

		} else {
			velocidadActual = 0; // Si el estado no es travelling velocidad=0.
		}

	}

	void moveToNextRoad() {
		if (estado == VehicleStatus.PENDING || estado == VehicleStatus.WAITING) {
			if (cruceActual == itinerario.size() - 1) {
				carretera.exit(this);
				estado = VehicleStatus.ARRIVED;
				velocidadActual = 0;
			} else {
				localizacion = 0;
				estado = VehicleStatus.TRAVELING;
				if (carretera != null)
					carretera.exit(this);
				carretera = itinerario.get(cruceActual).roadTo(itinerario.get(cruceActual + 1));
				cruceActual++;
				carretera.enter(this);
			}

		} else {
			throw new IllegalArgumentException("El vehiculo debe estar a la espera en la cola.");
		}

	}

	@Override
	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("id", _id);
		jo.put("speed", velocidadActual);
		jo.put("distance", distanciaRecorrida);
		jo.put("co2", contaminacionTotal);
		jo.put("class", gradoContaminacion);
		jo.put("status", estado);

		if (estado != VehicleStatus.PENDING && estado != VehicleStatus.ARRIVED) {
			jo.put("road", carretera.getId());
			jo.put("location", localizacion);
		}

		return jo;
	}

	public int getSpeed() {
		return velocidadActual;
	}

	public int getMaxSpeed() {
		return velocidadMax;
	}

	public void setSpeed(int s) {
		if (s < 0) {
			throw new IllegalArgumentException("La velocidad del vehiculo debe ser positiva.");
		} else {
			velocidadActual = Math.min(s, velocidadMax);
		}
	}

	public int getCont() {
		return contaminacionTotal;
	}

	public int getContClass() {
		return gradoContaminacion;
	}

	public void setContaminationClass(int c) {
		if (c < 0 || c > 10) {
			throw new IllegalArgumentException("El valor de la contaminacion debe estar entre 1 y 10.");
		} else {			
			gradoContaminacion = c;
		}
	}

	public int getLocation() {
		return localizacion;
	}

	public int getDistance() {
		return distanciaRecorrida;
	}

	public int getCurrentJunction() {
		return cruceActual;
	}

	public List<Junction> getItinerario() {
		return itinerario;
	}

	public VehicleStatus getStatus() {
		return estado;
	}

	public Road getRoad() {
		return carretera;
	}
}

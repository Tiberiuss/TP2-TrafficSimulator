package simulator.model;

import java.util.ArrayList;
import java.util.List;

public class NewVehicleEvent extends Event {
	private String id;
	private int maxSpeed;
	private int contClass;
	private List<String> itinerary;

	public NewVehicleEvent(int time, String id, int maxSpeed, int contClass, List<String> itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	/*
	 * El método execute de este evento crea un vehículo en función de sus
	 * argumentos y lo añade al mapa de carreteras. Después llama a su método
	 * moveToNext para comenzar su viaje
	 */
	@Override
	void execute(RoadMap map) throws IllegalArgumentException {

		List<Junction> itin = new ArrayList<Junction>();

		IllegalArgumentException ex = new IllegalArgumentException(
				"El itinerario del vehiculo no es correcto (Se ha corregido para seguir con la ejecución del programa).");

		for (String jun_id : itinerary) {
			try {
				
				Junction jun = map.getJuction(jun_id);
				if (jun != null)
					itin.add(jun);
				else
					throw ex;

			} catch (IllegalArgumentException e) {
				System.err.println(e.getLocalizedMessage());			
			}

		}

		Vehicle v = new Vehicle(id, maxSpeed, contClass, itin);
		map.addVehicle(v);
		v.moveToNextRoad();

	}

	@Override
	public String toString() {
		return "New Vehicle '" + id + "'";
	}
}

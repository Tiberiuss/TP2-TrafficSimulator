package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver> {

	private RoadMap mapaCarreteras;
	private List<Event> listaEventos;
	private List<TrafficSimObserver> observadores;
	private int tiempo;

	public TrafficSimulator() {
		// Si tiempos son iguales el que fue añadido antes ira primero
		listaEventos = new SortedArrayList<>();
		mapaCarreteras = new RoadMap();
		observadores = new ArrayList<>();
		tiempo = 0;
	}

	public void addEvent(Event e) {
		listaEventos.add(e);
		notifyOnEventAdded(mapaCarreteras, listaEventos, e, tiempo);
	}

	public void advance() throws Exception {
		tiempo++;

		notifyOnAdvanceStart(mapaCarreteras, listaEventos, tiempo);

		Iterator<Event> i = listaEventos.iterator();
		while (i.hasNext()) {
			// Añadido try catch dentro para evitar	que si falla un evento se salte todos los demás
			try {

				Event e = i.next();

				if (e._time == tiempo) {
					i.remove();
					e.execute(mapaCarreteras);
				}

			} catch (Exception e) {
				notifyOnError(e.getMessage());
			}
		}
		
		try {
			for (Junction j : mapaCarreteras.getJunctions()) {
				j.advance(tiempo);
			}

			for (Road r : mapaCarreteras.getRoads()) {
				r.advance(tiempo);
			}
		} catch (Exception e) {
			notifyOnError(e.getMessage());
			throw e;
		}

		notifyOnAdvanceEnd(mapaCarreteras, listaEventos, tiempo);

	}

	public void reset() {
		mapaCarreteras.reset();
		listaEventos.clear();
		tiempo = 0;
		notifyOnReset(mapaCarreteras, listaEventos, tiempo);
	}

	public JSONObject report() {
		JSONObject jo = new JSONObject();

		jo.put("time", tiempo);
		jo.put("state", mapaCarreteras.report());

		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observadores.add(o);
		o.onRegister(mapaCarreteras, listaEventos, tiempo);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observadores.remove(o);
	}

	public void notifyOnAdvanceStart(RoadMap map, List<Event> events, int time) {
		for (TrafficSimObserver obs : observadores) {
			obs.onAdvanceStart(map, events, time);
		}
	}

	public void notifyOnAdvanceEnd(RoadMap map, List<Event> events, int time) {
		for (TrafficSimObserver obs : observadores) {
			obs.onAdvanceEnd(map, events, time);
		}
	}

	public void notifyOnEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		for (TrafficSimObserver obs : observadores) {
			obs.onEventAdded(map, events, e, time);
		}
	}

	public void notifyOnReset(RoadMap map, List<Event> events, int time) {
		for (TrafficSimObserver obs : observadores) {
			obs.onReset(map, events, time);
		}
	}

	public void notifyOnError(String err) {
		for (TrafficSimObserver obs : observadores) {
			obs.onError(err);
		}
	}
}

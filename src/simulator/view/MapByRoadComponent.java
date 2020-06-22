package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.misc.ImageEnum;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver {

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _ROAD_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;

	MapByRoadComponent(Controller ctrl) {
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		setPreferredSize(new Dimension(300, 200));
		_car = ImageEnum.CAR.getImageIO();
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize(); // Hace que parpadee
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
		// drawJunctions(g);
	}

	private void drawRoads(Graphics g) {
		int i = 0;
		for (Road r : _map.getRoads()) {

			// the road goes from (x1,y1) to (x2,y2)
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i + 1) * 50;

			g.setColor(Color.BLACK);
			// draw the road's identifier
			g.drawString(r.getId(), x1 - 30, y + 3);
			g.drawLine(x1, y, x2, y);

			g.setColor(_ROAD_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// choose a color for the arrow depending on the traffic light of the road
			Color destColor = _RED_LIGHT_COLOR;
			int idx = r.getDest().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) {
				destColor = _GREEN_LIGHT_COLOR;
			}
			g.setColor(destColor);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);

			// draw the junction's identifier
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSrc().getId(), x1 - 5, y - 10);
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getDest().getId(), x2 - 5, y - 10);

			// draw an image of weather
			Image w = ImageEnum.valueOf(r.getWeather().toString().toUpperCase()).getImageIO();
			g.drawImage(w, x2 + 16, y - 16, 32, 32, this);

			int C = (int) Math.floor(Math.min((double) r.getTotalCO2() / (1.0 + (double) r.getCO2Limit()), 1.0) / 0.19);
			String contImage="cont_" + C;
			Image cont = ImageEnum.valueOf(contImage.toUpperCase()).getImageIO();
			g.drawImage(cont, x2 + 60, y - 16, 32, 32, this);

			i++;

		}

	}

	private void drawVehicles(Graphics g) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				Road r = v.getRoad();

				int x1 = 50;
				int x2 = getWidth() - 100;
				int y = (_map.getRoads().indexOf(r) + 1) * 50;

				int vX = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));

				// draw an image of a car
				g.drawImage(_car, vX - 5, y - 10, 16, 16, this);
				g.drawString(v.getId(), vX - 7, y - 10);
			}
		}
	}

	// this method is used to update the preffered and actual size of the component,
	// so when we draw outside the visible area the scrollbars show up
	private void updatePrefferedSize() {
		int maxW = 500;
		int maxH = (_map.getRoads().size() + 2) * 50;
		maxW -= 50;
		maxH -= 50;

		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}

	public void update(RoadMap map) {
		_map = map;
		repaint();
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {

	}

}

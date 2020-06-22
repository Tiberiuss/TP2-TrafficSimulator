package simulator.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

@SuppressWarnings("serial")
public class StatusBar extends JPanel implements TrafficSimObserver {
	private JLabel tick;
	private JLabel info;

	
	public StatusBar(Controller _ctrl) {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));		
		
		tick = new JLabel("Time");
		info = new JLabel("Welcome!");
				
		info.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(0, 10, 0, 10)));
		
		this.add(tick);
		this.add(Box.createHorizontalStrut(100));
		this.add(info);		
		this.setBorder(BorderFactory.createBevelBorder(1));
		
		_ctrl.addObserver(this);

	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.tick.setText("Time: " + time);
		this.info.setText(" ");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.tick.setText("Time: " + time);
		this.info.setText(" ");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.tick.setText("Time: " + time);
		this.info.setText("Event added (" + e.toString() + ")");
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.tick.setText("Time");
		this.info.setText("Welcome!");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.tick.setText("Time: " + time);
		this.info.setText("Added observer");
	}

	@Override
	public void onError(String err) {
		this.info.setText(err);
	}

}

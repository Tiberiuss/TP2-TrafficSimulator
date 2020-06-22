package simulator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import org.json.JSONException;

import simulator.control.Controller;
import simulator.misc.ImageEnum;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.NewSetWeatherEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.view.dialog.ChangeCO2ClassDialog;
import simulator.view.dialog.ChangeWeatherDialog;

@SuppressWarnings("serial")
public class ControlPanel extends JPanel implements TrafficSimObserver, ActionListener {

	private Controller _ctrl;

	private JButton open;
	private JButton co2;
	private JButton weather;
	private JButton run;
	private JButton stop;
	private JSpinner ticks;
	private JButton exit;
	private boolean _stopped;
	private List<Vehicle> _vehiculos;
	private List<Road> _carreteras;
	private int _time;
	
	public ControlPanel(Controller _ctrl) {
		ControlPanel.setDefaultLocale(Locale.ENGLISH);
		this._ctrl = _ctrl;
		_stopped = true;
		_vehiculos = new ArrayList<>();
		_carreteras = new ArrayList<>();
		_time = 0;
		_ctrl.addObserver(this);

		this.setLayout(new BorderLayout());

		JToolBar toolBar = new JToolBar();

		open = createImageButton(ImageEnum.OPEN);
		co2 = createImageButton(ImageEnum.CO2CLASS);
		weather = createImageButton(ImageEnum.WEATHER);
		run = createImageButton(ImageEnum.RUN);
		stop = createImageButton(ImageEnum.STOP);
		exit = createImageButton(ImageEnum.EXIT);		
		
		JLabel ticksLabel = new JLabel(" Ticks: ");
		ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 1));

		stop.setEnabled(false);

		ticks.setPreferredSize(new Dimension(70, 40));
		ticks.setMaximumSize(new Dimension(70, 40));
		open.setToolTipText("Open an event file");
		co2.setToolTipText("Change CO2 class of a Vehicle");
		weather.setToolTipText("Change Weather of a Road");
		run.setToolTipText("Run the simulator");
		stop.setToolTipText("Stop the simulator");
		ticks.setToolTipText("Simulation ticks to run 1-10000");
		exit.setToolTipText("Exit the simulator");

		// Mas limpio que funciones anonimas ->
		open.addActionListener(this);
		co2.addActionListener(this);
		weather.addActionListener(this);
		run.addActionListener(this);
		stop.addActionListener(this);
		exit.addActionListener(this);

		toolBar.add(open);
		toolBar.addSeparator();
		toolBar.add(co2);
		toolBar.add(weather);
		toolBar.addSeparator();
		toolBar.add(run);
		toolBar.add(stop);
		toolBar.add(ticksLabel);
		toolBar.add(ticks);
		toolBar.add(Box.createHorizontalGlue());
		toolBar.add(exit);

		this.add(toolBar, BorderLayout.PAGE_START);

		this.setVisible(true);
	}

	private void run_sim(int n) {
		if (n > 0 && !_stopped) {
			try {
				_ctrl.run(1);
			} catch (Exception e) {		
				enableToolBar(true);
				_stopped = true;			
				return;
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					run_sim(n - 1);
				}
			});

		} else {
			enableToolBar(true);
			_stopped = true;
		}
	}

	private void enableToolBar(boolean b) {
		open.setEnabled(b);
		co2.setEnabled(b);
		weather.setEnabled(b);
		run.setEnabled(b);
		stop.setEnabled(!b);
		ticks.setEnabled(b);
		exit.setEnabled(b);

	}

	
	private JButton createImageButton(ImageEnum img){
		if(img.getImage()!=null)
			return new JButton(img.getImage());
		else
			return new JButton(img.toString());
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() == open) {

			JFileChooser file = new JFileChooser("./");
			int value = file.showOpenDialog(this.getTopLevelAncestor());
			if (value == JFileChooser.APPROVE_OPTION) {
				try {
					File f = file.getSelectedFile();
					String fileName = f.getName();
					if (fileName.substring(fileName.indexOf('.') + 1).equals("json")) {
						_ctrl.reset();
						_ctrl.loadEvents(new FileInputStream(file.getSelectedFile()));
					} else {
						throw new IllegalArgumentException();
					}
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (JSONException | IllegalArgumentException e2) {
					JOptionPane.showMessageDialog(null, "Invalid file", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}

		} else if (evt.getSource() == co2) {

			ChangeCO2ClassDialog dialog = new ChangeCO2ClassDialog(null);
			int status = dialog.open(_vehiculos);
			if (status == 1)
				_ctrl.addEvent(new NewSetContClassEvent(_time + dialog.getTicks(), dialog.getPair()));

		} else if (evt.getSource() == weather) {

			ChangeWeatherDialog dialog = new ChangeWeatherDialog(null);
			int status = dialog.open(_carreteras);
			if (status == 1)
				_ctrl.addEvent(new NewSetWeatherEvent(_time + dialog.getTicks(), dialog.getPair()));

		} else if (evt.getSource() == run) {

			_stopped = false;
			enableToolBar(false);
			run_sim((int) ticks.getValue());

		} else if (evt.getSource() == stop) {

			_stopped = true;

		} else if (evt.getSource() == exit) {

			int i = JOptionPane.showConfirmDialog(this.getParent(), "Are you sure you want to exit the application?", "Exit ",
					JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if (i == JOptionPane.YES_OPTION) {

				System.exit(0);
			}

		}
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		// TODO PREGUNTAR si esta bien cargar vehiculos y carreteras aqui o en start
		_vehiculos = map.getVehicles();
		_carreteras = map.getRoads();
		_time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
	}

	@Override
	public void onError(String err) {
		JOptionPane.showConfirmDialog(this.getParent(), err, "",JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);	
	}

}

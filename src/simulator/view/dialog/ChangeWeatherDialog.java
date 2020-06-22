package simulator.view.dialog;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.misc.Pair;
import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<String> _roads;
	private DefaultComboBoxModel<String> _roadsModel;
	private JComboBox<Weather> _weather;
	private JSpinner _ticks;

	public ChangeWeatherDialog(Frame parent) {
		super(parent, true);
		initGUI();

		setLocationRelativeTo(null);
		setVisible(false);
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change CO2 Class");
		Container mainPanel = getContentPane();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			
		JLabel helpMsg = new JLabel(
				"<html>Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.</html>");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		
		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new BoxLayout(viewsPanel,BoxLayout.X_AXIS));	
		viewsPanel.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));

		_roadsModel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadsModel);
		_weather = new JComboBox<Weather>(Weather.values());
		_ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
	
		JPanel buttonsPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");		
		JButton okButton = new JButton("OK");
		
				
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_roadsModel.getSelectedItem() != null) {
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});

		mainPanel.add(helpMsg);		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		viewsPanel.add(new JLabel("Road:"));
		viewsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		viewsPanel.add(_roads);
		viewsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		viewsPanel.add(new JLabel("Weather:"));
		viewsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		viewsPanel.add(_weather);
		viewsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		viewsPanel.add(new JLabel("Ticks:"));
		viewsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		viewsPanel.add(_ticks);
		
		mainPanel.add(viewsPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(2, 0)));
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open(List<Road> carreteras) {
		_roadsModel.removeAllElements();
		for (Road r : carreteras)
			_roadsModel.addElement(r.getId());
		this.setVisible(true);

		return _status;
	}

	private String getRoad() {
		return (String) _roadsModel.getSelectedItem();
	}

	private Weather getWeather() {
		return (Weather) _weather.getSelectedItem();
	}

	public List<Pair<String, Weather>> getPair() {
		List<Pair<String, Weather>> cs = new ArrayList<Pair<String, Weather>>();
		cs.add(new Pair<String, Weather>(getRoad(), getWeather()));
		return cs;
	}

	public int getTicks() {
		return (int) _ticks.getValue();
	}

}

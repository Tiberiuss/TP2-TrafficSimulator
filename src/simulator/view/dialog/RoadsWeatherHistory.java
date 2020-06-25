package simulator.view.dialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import simulator.model.Weather;
import simulator.view.jtable.RoadsWeatherTableModel;

public class RoadsWeatherHistory extends JDialog {

	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<Weather> _weather;
	private RoadsWeatherTableModel model;
	private List<Map<Weather, List<String>>> _weatherMap;

	public RoadsWeatherHistory(Frame parent) {
		super(parent, true);
		_weatherMap = new ArrayList<Map<Weather,List<String>>>();
		initGUI();
		setLocationRelativeTo(null);
		setVisible(false);
	}

	private void initGUI() {

		_status = 0;

		setTitle("Roads Weather History");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);

		model = new RoadsWeatherTableModel();
		JTable table = new JTable(model);
		JScrollPane tableScroll = new JScrollPane(table);

		JLabel helpMsg = new JLabel(
				"<html>Select a weather and press UPDATE to show the roads that have this weather in each tick.</html>");
		helpMsg.setAlignmentX(CENTER_ALIGNMENT);

		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		viewsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

		JPanel tablePanel = new JPanel();
		tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));

		_weather = new JComboBox<Weather>(Weather.values());

		JPanel buttonsPanel = new JPanel();
		JButton cancelButton = new JButton("Close");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				RoadsWeatherHistory.this.setVisible(false);
			}
		});

		JButton okButton = new JButton("Update");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				pickWeather();			
				_status = 1;
			}
		});

		mainPanel.add(helpMsg);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		viewsPanel.add(Box.createRigidArea(new Dimension(100, 0)));
		viewsPanel.add(new JLabel("Weather: "));
		viewsPanel.add(_weather);
		viewsPanel.add(Box.createRigidArea(new Dimension(100, 0)));

		tablePanel.add(tableScroll);

		mainPanel.add(viewsPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		buttonsPanel.add(cancelButton);
		buttonsPanel.add(Box.createRigidArea(new Dimension(2, 0)));
		buttonsPanel.add(okButton);

		mainPanel.add(buttonsPanel);
		mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

		mainPanel.add(tablePanel);

		setPreferredSize(new Dimension(500, 400));
		pack();
		setResizable(false);
		setVisible(false);
	}

	public int open(List<Map<Weather, List<String>>> _weatherHistory) {
		_weatherMap = _weatherHistory;
		model.setRoadsList(_weatherMap);
		pickWeather();		
		this.setVisible(true);
		
		return _status;
	}
	
	private void pickWeather() {	
		model.setWeather(getWeather());
	}

	private Weather getWeather() {
		return (Weather) _weather.getSelectedItem();
	}
}

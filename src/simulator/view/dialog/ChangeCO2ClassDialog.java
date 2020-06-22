package simulator.view.dialog;

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
import simulator.model.Vehicle;

public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	private int _status;
	private JComboBox<String> _vehicles;
	private DefaultComboBoxModel<String> _vehiclesModel;
	private JComboBox<Integer> _contClass;
	private JSpinner _ticks;

	public ChangeCO2ClassDialog(Frame parent) {
		super(parent, true);
		initGUI();
		
		setLocationRelativeTo(null);
		setVisible(false);
	}

	private void initGUI() {

		_status = 0;

		setTitle("Change CO2 Class");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		JLabel helpMsg = new JLabel(
				"<html>Schedule an event to change the CO2 class of a vehicle after a given number of simulation ticks from now.</html>");

		helpMsg.setAlignmentX(CENTER_ALIGNMENT);
		

		JPanel viewsPanel = new JPanel();
		viewsPanel.setLayout(new BoxLayout(viewsPanel,BoxLayout.X_AXIS));	
		viewsPanel.setBorder(BorderFactory.createEmptyBorder(0,20,20,20));
		
		_vehiclesModel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclesModel);
	
		_ticks = new JSpinner(new SpinnerNumberModel(1, 1, 10000, 1));
		_contClass = new JComboBox<Integer>(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });

	
		
		JPanel buttonsPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
	
		
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (_vehiclesModel.getSelectedItem() != null) {
					_status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		
		
		mainPanel.add(helpMsg);		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		viewsPanel.add(new JLabel("Vehicle: "));
		viewsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		viewsPanel.add(_vehicles);
		viewsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		viewsPanel.add(new JLabel(" CO2 Class: "));
		viewsPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		viewsPanel.add(_contClass);
		viewsPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		viewsPanel.add(new JLabel(" Ticks: "));
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

	public int open(List<Vehicle> vehicles) {
		_vehiclesModel.removeAllElements();
		for (Vehicle v : vehicles)
			_vehiclesModel.addElement(v.getId());
		this.setVisible(true);

		return _status;
	}

	private String getVehicle() {
		return (String) _vehiclesModel.getSelectedItem();
	}

	private int getContClass() {
		return (int) _contClass.getSelectedItem();
	}

	public List<Pair<String, Integer>> getPair() {
		List<Pair<String, Integer>> cs = new ArrayList<Pair<String, Integer>>();
		cs.add(new Pair<String, Integer>(getVehicle(), getContClass()));
		return cs;
	}

	public int getTicks() {
		return (int) _ticks.getValue();
	}
}

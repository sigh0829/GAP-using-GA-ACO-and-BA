package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import controller.Controller;
import model.Result;

/**
 * 
 * JTable wrapper.
 * 
 * @author acco
 * 
 *         Jul 5, 2016 7:58:59 AM
 *
 */
public class ResultsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private TableModel tableModel;
	private JLabel resultsLabel;
	private JButton clearButton;
	private Controller controller;
	private JMenuItem removeSelected;

	public ResultsPanel(Controller controller) {
		this.controller = controller;

		this.setBackground(R.BACKGROUND_COLOR);

		this.setLayout(new BorderLayout());

		resultsLabel = new JLabel("..:: RESULTS & STATISTICS (empty) ::..", SwingConstants.CENTER);
		this.add(resultsLabel, BorderLayout.NORTH);

		tableModel = new TableModel(controller);

		JTable table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new CellRenderer());

		JScrollPane scroll = new JScrollPane(table);
		scroll.setOpaque(false);
		scroll.setBorder(new EmptyBorder(5, 5, 5, 5));

		scroll.getViewport().setBackground(R.BACKGROUND_COLOR);
		this.add(scroll, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.setOpaque(false);

		clearButton = new JButton(R.CLEAR_ICON);
		clearButton.setToolTipText("Clear all results");
		clearButton.setContentAreaFilled(false);
		clearButton.addActionListener((e) -> {
			controller.clearResults();
			this.refreshResults();
		});
		bottomPanel.add(clearButton);

		this.add(bottomPanel, BorderLayout.SOUTH);

		JPopupMenu jPopupMenu = new JPopupMenu() {

			private static final long serialVersionUID = 1L;

			@Override
			public void show(Component invoker, int x, int y) {

				int[] rows = table.getSelectedRows();

				if (rows.length > 0) {
					// list.setSelectedIndex(row);
					super.show(invoker, x, y);
				}

			}
		};

		table.setComponentPopupMenu(jPopupMenu);

		removeSelected = new JMenuItem("Remove selected", R.REMOVE_ICON);
		removeSelected.addActionListener((e) -> {
			int[] indices = table.getSelectedRows();
			for (int i = 0; i < indices.length; i++) {
				tableModel.removeRow(indices[i]);
			}
			this.refreshResults();
		});
		jPopupMenu.add(removeSelected);
	}

	/**
	 * Refresh the jtable as soon as new results are available.
	 */
	public void refreshResults() {
		this.tableModel.refreshModel();
		tableModel.fireTableDataChanged();
		int size = controller.getResults().size();
		if (size == 0) {
			this.resultsLabel.setText("..:: RESULTS & STATISTICS (empty) ::..");
		} else {
			this.resultsLabel.setText("..:: RESULTS & STATISTICS (" + controller.getResults().size() + ") ::..");
		}

	}

	/**
	 * Disable buttons while processing.
	 */
	public void disableInput() {
		this.clearButton.setEnabled(false);
		this.removeSelected.setEnabled(false);
	}

	/**
	 * Re-enable them once finished
	 */
	public void enableInput() {
		this.clearButton.setEnabled(true);
		this.removeSelected.setEnabled(true);
	}

	/**
	 * 
	 * Simple Table model implementation as inner class.
	 * 
	 * @author acco
	 * 
	 *         Jul 5, 2016 8:04:26 AM
	 *
	 */
	public class TableModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		private String[] columnNames = { "Instance", "Runs", "GA z*", "GA avg z ", "GA avg t (ms)", "ANTS z*",
				"ANTS avg z", "ANTS avg t (ms)", "BA z*", "BA avg z", "BA avg t (ms)" };

		private List<Result> list;

		private Controller controller;

		public TableModel(Controller controller) {
			this.controller = controller;
			this.refreshModel();

		}

		public void removeRow(int i) {
			Result resultToRemove = this.list.get(i);
			this.controller.getResults().remove(resultToRemove.getInstance().getName());
		}

		/**
		 * Get the results (map), transform them into a list in order to display
		 * them in fixed order.
		 */
		public void refreshModel() {
			Collection<Result> collection = this.controller.getResults().values();
			this.list = new ArrayList<Result>(collection);
			Collections.sort(list, (a, b) -> {
				return a.getInstance().getLogicalName().compareTo(b.getInstance().getLogicalName());
			});
		}

		@Override
		public int getRowCount() {
			return list.size();
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex > this.list.size() - 1) {
				return null;
			}
			Result result = this.list.get(rowIndex);
			switch (columnIndex) {
			case 0:
				return result.getInstance().getLogicalName();
			case 1:
				return result.getRuns();
			case 2:
				return result.getGaBestValue();
			case 3:
				return result.getGaAvgValue();
			case 4:
				return result.getGaAvgTime();
			case 5:
				return result.getAntsBestValue();
			case 6:
				return result.getAntsAvgValue();
			case 7:
				return result.getAntsAvgTime();
			case 8:
				return result.getBaBestValue();
			case 9:
				return result.getBaAvgValue();
			case 10:
				return result.getBaAvgTime();
			default:
				return null;
			}

		}

	}

	/**
	 * JTable Cell renderer as inner class.
	 * 
	 * @author acco
	 * 
	 *         Jul 5, 2016 8:06:48 AM
	 *
	 */
	public class CellRenderer extends JLabel implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public CellRenderer() {
			this.setOpaque(true);
			this.setHorizontalAlignment(SwingConstants.RIGHT);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			this.setText(value.toString() + " ");

			if (isSelected) {
				this.setBackground(new Color(189, 128, 171));
			} else {
				if (column >= 2 && column <= 4) {
					this.setBackground(R.GA_BACKGROUND_COLOR);
				} else if (column > 4 & column <= 7) {
					this.setBackground(R.ANTS_BACKGROUND_COLOR);
				} else if (column > 7 && column <= 10) {
					this.setBackground(R.BA_BACKGROUND_COLOR);
				} else {
					this.setBackground(R.BACKGROUND_COLOR);
				}
			}

			return this;
		}

	}

}

package app.renderer;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import util.Size;

public class FileTableCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table, 
			Object value,
            boolean isSelected, 
            boolean hasFocus, 
            int row, 
            int column) {
		Component component = super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
		
		if (value != null && value instanceof Date) {
			SimpleDateFormat f = new SimpleDateFormat("  dd/MM/yyyy hh:mm");
			Date date = (Date)value;
			((JLabel) component).setText(f.format(date));
		} 
		else if (value != null && value instanceof Size) {
			((JLabel) component).setHorizontalAlignment(JLabel.RIGHT); // Set the Size column as right aligned.
		}
		
		return component;
	}

}

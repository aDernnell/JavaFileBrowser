package app.renderer;

import java.awt.Color;
import java.awt.Component;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import util.Size;
import app.component.FileTable;

public class FileTableCellRenderer extends DefaultTableCellRenderer {
	
	private FileTable ft;

	public FileTableCellRenderer(FileTable ft) {
		this.ft = ft;
	}
	
	@Override
	public Component getTableCellRendererComponent(
			JTable table, 
			Object value,
            boolean isSelected, 
            boolean hasFocus, 
            int row, 
            int column) {
		Component component = super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
		if (value != null && value instanceof FileTime) {
			//SimpleDateFormat f = new SimpleDateFormat("  dd/MM/yyyy hh:mm");
			FileTime date = (FileTime)value;
			((JLabel) component).setText(new SimpleDateFormat("  dd/MM/yyyy hh:mm").format(new Date(date.toMillis())));
		}
		else if (value != null && value instanceof Size) {
			((JLabel) component).setHorizontalAlignment(JLabel.RIGHT); // Set the Size column as right aligned.
		}
		
		if(column==1 && !Files.isReadable(ft.getModel().getFile(row))) {
			((JLabel) component).setForeground(new Color(200,200,200));
		}
		else if(column==1) ((JLabel) component).setForeground(new Color(0,0,0));
		
		return component;
	}

}

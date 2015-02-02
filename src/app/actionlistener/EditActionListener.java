package app.actionlistener;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JOptionPane;

import app.component.FileTable;

public class EditActionListener extends FileTableActionListener{

	public EditActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Path> files = ft.getSelectedFiles();
		// for all selected files
		for (Path file : files) {
			try {
	            Desktop.getDesktop().edit(file.toFile());
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog((Component)ft.getView(), ex.getMessage(), "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
		}
		
	}

}

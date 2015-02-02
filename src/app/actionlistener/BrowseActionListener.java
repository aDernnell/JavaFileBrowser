package app.actionlistener;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import app.component.FileTable;

public class BrowseActionListener implements ActionListener{

	private FileTable ft;
	
	public BrowseActionListener(FileTable ft) {
		this.ft = ft;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
            Desktop.getDesktop().browse(ft.getLeadSelectedFile().toURI());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog((Component)ft.getView(), ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
	}

}

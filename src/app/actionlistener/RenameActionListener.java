package app.actionlistener;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import app.Application;
import app.component.FileTable;

public class RenameActionListener extends FileTableActionListener{

	public RenameActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Path p = ft.getLeadSelectedFile();
		
		String name = (String)JOptionPane.showInputDialog(
                ft.getView(),
                "New name :",
                "Rename "+p.getFileName(),
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                p.getFileName());
		
		if(name != null && name.length() > 0) {
			try {
				Files.move(p, Paths.get(p.getParent().toString()+"/"+name));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Application.instance().getCurrentPage().getNav().activateProgressBar(true);
			ft.update();
			// @TODO open the file just after rename does not work if it is a folder...
			// idem for new folder and for paste...
		}
	}

}

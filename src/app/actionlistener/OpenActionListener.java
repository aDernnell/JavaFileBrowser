package app.actionlistener;

import java.awt.event.ActionEvent;
import java.nio.file.Files;
import java.nio.file.Path;

import app.Application;
import app.component.FileTable;

public class OpenActionListener extends FileTableActionListener {

	public OpenActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Path file = ft.getLeadSelectedFile();
		if(Files.isDirectory(file)) {
			Application.instance().openFolder(Application.instance().getTree().getSelectedNode(),file, true);
		}
		else Application.instance().openFile(file);	
	}

}

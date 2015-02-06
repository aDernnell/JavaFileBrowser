package app.actionlistener;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.List;

import util.DesktopAPI;
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
            DesktopAPI.edit(file.toFile());
		}
		
	}

}

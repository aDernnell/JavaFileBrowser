package app.actionlistener;

import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.List;

import app.component.FileTable;

public class CopyActionListener extends FileTableActionListener{

	static public List<Path> copiedFiles;
	
	public CopyActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		copiedFiles = ft.getSelectedFiles();
	}

}
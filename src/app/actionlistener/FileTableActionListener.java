package app.actionlistener;

import java.awt.event.ActionListener;

import app.component.FileTable;

public abstract class FileTableActionListener implements ActionListener{

	protected FileTable ft;
	
	public FileTableActionListener(FileTable ft) {
		this.ft = ft;
	}
}

package app.actionlistener;

import java.awt.event.ActionEvent;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.SwingWorker;

import app.Application;
import app.component.FileTable;

public class PasteActionListener extends FileTableActionListener {

	public PasteActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final List<Path> files = CopyActionListener.copiedFiles;
		
		Path f = ft.getLeadSelectedFile();
		if(Files.isRegularFile(f)) {
			f = f.getParent();
		}
		final Path folder = f;
		
		if(files != null) {
			SwingWorker<Void, Path> worker = new SwingWorker<Void, Path>() {
				@Override
				protected Void doInBackground() throws Exception {
					for (Path path : files) {
						try {
							System.out.println("paste "+path);
							Files.copy(path, Paths.get(folder.toString()+"/"+path.getFileName()));
						} catch (FileAlreadyExistsException e1) {
							// @TODO copy file that already exist
							e1.printStackTrace();
						}
					}
					
					return null;
				}
				
				@Override
	            protected void done() {
					CopyActionListener.copiedFiles = null;
					ft.update();
	            }
	    		
	    	};
	    	Application.instance().getNav().activateProgressBar(true);
	    	worker.execute();
		}
		
		
		
	}

}

package app.actionlistener;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import app.Application;
import app.component.FileTable;

public class DeleteActionListener extends FileTableActionListener {

	public DeleteActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final List<Path> filesToDelete = ft.getSelectedFiles();
		
		SwingWorker<Void, Path> worker = new SwingWorker<Void, Path>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (Path path : filesToDelete) {
					try {
						Files.delete(path);
						//ft.getModel().removeFile(ft.getView().) // @TODO remove file from model
					} catch (NoSuchFileException nsfe) {
					  System.err.println("Fichier ou repertoire " + path + " n'existe pas");
					  JOptionPane.showMessageDialog(ft.getView(), "File or directory "+path+" doesn't exist", "Error",
	    	                    JOptionPane.ERROR_MESSAGE);
					} catch (DirectoryNotEmptyException dnee) {
					  System.err.println("Le repertoire " + path + " n'est pas vide");
					  JOptionPane.showMessageDialog(ft.getView(), "The directory " + path + " is not empty", "Error",
	    	                    JOptionPane.ERROR_MESSAGE);
					} catch (IOException ioe) {
					  System.err.println("Impossible de supprimer " + path + " : " + ioe);
					  JOptionPane.showMessageDialog(ft.getView(), "Unable to delete " + path + " : " + ioe, "Error",
	    	                    JOptionPane.ERROR_MESSAGE);
					}
				}
				return null;
			}
			
			@Override
            protected void done() {
				ft.update();
            }
    		
    	};
    	Application.instance().getCurrentPage().getNav().activateProgressBar(true);
    	worker.execute();
	}

}

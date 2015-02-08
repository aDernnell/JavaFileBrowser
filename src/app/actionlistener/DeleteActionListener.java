package app.actionlistener;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
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
		final int[] selections = ft.getView().getSelectedRows();
		
		
		SwingWorker<Void, Path> worker = new SwingWorker<Void, Path>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < selections.length; i++) {
					ft.getModel().removeFile(i);
				}
				for (Path path : filesToDelete) {
					try {
						if(Files.isDirectory(path)) {
							Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
								   @Override
								   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
									   Files.delete(file);
									   return FileVisitResult.CONTINUE;
								   }

								   @Override
								   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
									   Files.delete(dir);
									   return FileVisitResult.CONTINUE;
								   }

							   });
						}
						else Files.delete(path);
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

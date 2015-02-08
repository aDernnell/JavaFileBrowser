package app.actionlistener;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemLoopException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.EnumSet;
import java.util.List;

import javax.swing.JOptionPane;
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
			SwingWorker<Void, Boolean> worker = new SwingWorker<Void, Boolean>() {
				@Override
				protected Void doInBackground() throws Exception {
					for (Path path : files) {
						try {
							if(Files.isDirectory(path)) copyFolder(path, folder);
							else copyFile(path, folder);
						} catch (FileAlreadyExistsException e1) {
							System.err.format("Unable to copy: %s: %s%n", path, e1);
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
	    	Application.instance().getCurrentPage().getNav().activateProgressBar(true);
	    	worker.execute();
		}
		
	}
	
	boolean copyFile(Path path,Path folder) throws IOException {
		boolean checkOverwrite=false;
		if(Files.exists(Paths.get(folder.toString()+"/"+path.getFileName()))) {
			checkOverwrite=true;
		}
		int result = -1;
		if(checkOverwrite) JOptionPane.showConfirmDialog(ft.getView(),"Overwrite "+path.getFileName()+" ?");
		
		if(!checkOverwrite || result==JOptionPane.YES_OPTION) {
			Files.copy(path, Paths.get(folder.toString()+"/"+path.getFileName()), REPLACE_EXISTING);
			return true;
		}
		else if(result==JOptionPane.NO_OPTION) {
			int i=0;
			String name;
			do {
				i++;
				String[] parts = path.getFileName().toString().split("\\.");
				name = folder.toString()+"/";
				if(parts.length>1) {
					System.out.println("length>1");
					String end = "_"+String.valueOf(i)+"."+parts[parts.length-1];
					for(int j=0;j<parts.length-1;j++) {
						name+=parts[j]+(j<parts.length-2?".":"");
					}
					name+=end;
				}
				else name+=parts[0]+"_"+String.valueOf(i);
			}
			while(Files.exists(Paths.get(name)));
			Files.copy(path, Paths.get(name));
			return true;
		}
		return false;
	}
	
	
	void copyFolder(Path file, Path target) throws IOException {
		System.out.println("copying folder "+file+" --> "+target);
		EnumSet<FileVisitOption> opts = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		if(Files.isSameFile(file, target)) return;
        TreeCopier tc = new TreeCopier(file, Paths.get(target.toString()+"/"+file.getFileName()), true);
        
        Files.walkFileTree(file, opts, Integer.MAX_VALUE, tc);
	}
	
	class TreeCopier implements FileVisitor<Path> {
        private final Path source;
        private final Path target;
        private final boolean preserve;
 
        TreeCopier(Path source, Path target, boolean preserve) {
            this.source = source;
            this.target = target;
            this.preserve = preserve;
        }
 
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            // before visiting entries in a directory we copy the directory
            // (okay if directory already exists).
            CopyOption[] options = (preserve) ?
                new CopyOption[] { COPY_ATTRIBUTES } : new CopyOption[0];
 
            Path newdir = target.resolve(source.relativize(dir));
            try {
                Files.copy(dir, newdir, options);
            } catch (FileAlreadyExistsException x) {
                // ignore
            } catch (IOException x) {
                System.err.format("Unable to create: %s: %s%n", newdir, x);
                return SKIP_SUBTREE;
            }
            return CONTINUE;
        }
 
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            copyFile(file, target.resolve(source.relativize(file)).getParent());
            return CONTINUE;
        }
 
        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            // fix up modification time of directory when done
            if (exc == null && preserve) {
                Path newdir = target.resolve(source.relativize(dir));
                try {
                    FileTime time = Files.getLastModifiedTime(dir);
                    Files.setLastModifiedTime(newdir, time);
                } catch (IOException x) {
                    System.err.format("Unable to copy all attributes to: %s: %s%n", newdir, x);
                }
            }
            return CONTINUE;
        }
 
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            if (exc instanceof FileSystemLoopException) {
                System.err.println("cycle detected: " + file);
            } else {
                System.err.format("Unable to copy: %s: %s%n", file, exc);
            }
            return CONTINUE;
        }
    }

}

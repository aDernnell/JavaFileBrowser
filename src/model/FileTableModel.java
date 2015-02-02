package model;


import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

import app.Application;

import util.Size;

public class FileTableModel extends AbstractTableModel {
	
	private ArrayList<Path> files;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
            "Icon",
            "Name",
            "Size",
            "Last Modified",
            "Properties"
    };

    public FileTableModel() {
        files = new ArrayList<Path>();
    }

    public FileTableModel(Path parentFolder) {
        setFiles(parentFolder);
    }

    public Object getValueAt(int row, int column) {
        Path file = files.get(row);
        switch (column) {
            case 0:
                return fileSystemView.getSystemIcon(file.toFile());
            case 1:
                return fileSystemView.getSystemDisplayName(file.toFile());
            case 2:
				try {
					return new Size(Files.size(file));
				} catch (IOException e2) {
					e2.printStackTrace();
				}
            case 3:
				try {
					return Files.getLastModifiedTime(file);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            case 4:
            {
            	StringBuilder sb = new StringBuilder();
            	if(Files.isReadable(file)) sb.append("R");
            	else sb.append(" ");
            	if(Files.isWritable(file)) sb.append("W");
            	else sb.append(" ");
            	if(Files.isExecutable(file)) sb.append("E");
            	else sb.append(" ");
            	try {
					if(Files.isHidden(file)) sb.append("H");
					else sb.append(" ");
				} catch (IOException e) {
					e.printStackTrace();
				}
            	if(Files.isSymbolicLink(file)) sb.append("S");
            	else sb.append(" ");
            	
            	return sb.toString();
            }
            default:
                throw new IllegalArgumentException(this.getClass().getSimpleName()+" getValueAt("+ String.valueOf(row)+", "+String.valueOf(column)+")");
        }
    }

    public int getColumnCount() {
        return columns.length;
    }

    public Class<?> getColumnClass(int column) {
        switch (column) {
            case 0:
                return ImageIcon.class;
            case 2:
            	return Size.class;
            case 3:
                return FileTime.class;
            default:
            	return String.class;
        }   
    }
    
    public int getColumnMaxWidth(int column) {
    	switch (column) {
	        case 0:
	            return 20;
	        default:
	        	return 300;
	    } 
    }
    
    public int getColumnMinWidth(int column) {
    	switch (column) {
	        case 0:
	            return 0;
	        default:
	        	return 20;
	    } 
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    public int getRowCount() {
        return files.size();
    }

    public Path getFile(int row) {
        return files.get(row);
    }

    public void setFiles(Path parentFolder) {
    	files = new ArrayList<Path>();
    	try (DirectoryStream<Path> stream = Application.instance().getNav().getStreamFromGlobbing(parentFolder)) {
    	    for (Path file: stream) {
    	        files.add(file);
    	    }
    	} catch (IOException | DirectoryIteratorException x) {
    	    System.err.println(x);
    	}
        fireTableDataChanged();
    }

}

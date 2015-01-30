package model;


import java.io.File;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;

import util.Size;

public class FileTableModel extends AbstractTableModel {
	
	private File[] files;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = {
            "Icon",
            "Name",
            "Size",
            "Last Modified"
    };

    public FileTableModel() {
        this(new File[0]);
    }

    public FileTableModel(File[] files) {
        this.files = files;
    }

    public Object getValueAt(int row, int column) {
        File file = files[row];
        switch (column) {
            case 0:
                return fileSystemView.getSystemIcon(file);
            case 1:
                return fileSystemView.getSystemDisplayName(file);
            case 2:
            	return new Size(file.length());
            case 3:
            	return new Date(file.lastModified());
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
                return Date.class;
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
        return files.length;
    }

    public File getFile(int row) {
        return files[row];
    }

    public void setFiles(File[] files) {
        this.files = files;
        fireTableDataChanged();
    }

}

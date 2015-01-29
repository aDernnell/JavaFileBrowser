package app.controller;

import java.io.File;

import javax.swing.JTable;

import model.FileTableModel;

public class FileTable {
	private JTable view;
	private FileTableModel model;
	
	public FileTable() {
		model = new FileTableModel();
		view = new JTable(model);
		
		view.setAutoCreateRowSorter(true);
        view.setShowVerticalLines(false);
        view.setShowHorizontalLines(false);
	}
	
	public void setTableData(File[] files) {
		model.setFiles(files);
	}

	public JTable getView() {
		return view;
	}

	public FileTableModel getModel() {
		return model;
	}
	
	
	
	
}

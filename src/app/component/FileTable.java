package app.component;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import util.Size;

import app.Application;
import app.renderer.FileTableCellRenderer;

import model.FileTableModel;

public class FileTable {
	private static final int ROW_PADDING = 4;
	private static final int COL_PADDING = 6;
	private JTable view;
	private FileTableModel model;
	
	public FileTable() {
		model = new FileTableModel();
		view = new JTable(model);
		
		view.setAutoCreateRowSorter(true);
        view.setShowVerticalLines(false);
        view.setShowHorizontalLines(false);
        view.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set the custom renderer
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) view.getColumnModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
        	TableColumn col = colModel.getColumn(i);
        	if(model.getColumnClass(i).equals(Date.class) || model.getColumnClass(i).equals(Size.class))
        		col.setCellRenderer(new FileTableCellRenderer());
        }
        
        // Mouse listener
        view.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			
			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					// select the row before popup if not yet selected (erase the previous selection
					int row = view.rowAtPoint(new Point(e.getX(), e.getY()));
					if(!view.getSelectionModel().isSelectedIndex(row))
						view.setRowSelectionInterval(row, row);
					
					PopupMenu popup = new PopupMenu();
		            popup.show(e.getComponent(),e.getX(), e.getY());
		        }
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = view.rowAtPoint(new Point(e.getX(), e.getY()));
				
				if (row == -1) {
		            //@TODO update infos (clear)
		            view.clearSelection();
		            return;
		        }
				
				
				
				if (SwingUtilities.isRightMouseButton(e)) {
					//@TODO contextual menu
		        }
				
				else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) { // double click
					File file = model.getFile(row);
					if(file.isDirectory()) {
						System.out.println("open folder from table");
						Application.instance().openFolder(Application.instance().getTree().getSelectedNode(),file, true);
					}
					else Application.instance().openFile(file);
				}
				
			}
		});
        
        
	}
	
	public List<File> getSelectedFiles() {
		int[] indices = view.getSelectedRows();
		List<File> lf = new ArrayList<File>();
		for (int index : indices) {
			lf.add(model.getFile(index));
		}
		return lf;
	}
	
	public File getLeadSelectedFile() {
		int index = view.getSelectionModel().getLeadSelectionIndex();
		return model.getFile(index);
	}
	
	public void setTableData(File[] files) {
		model.setFiles(files);
		setColumnsWidth();
		setRowsHeight();
	}
	
	private void setColumnsWidth() {
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) view.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			TableColumn col = colModel.getColumn(i);
			int width = 0;
			
			TableCellRenderer renderer = col.getHeaderRenderer();
			for (int r = 0; r < view.getRowCount(); r++) {
				renderer = view.getCellRenderer(r, i);
				Component comp = renderer.getTableCellRendererComponent(view, view.getValueAt(r, i),false, false, r, i);
				width = Math.max(width, comp.getPreferredSize().width);
				width = Math.min(width, model.getColumnMaxWidth(i));
				width = Math.max(model.getColumnMinWidth(i), width);
		    }
		    col.setPreferredWidth(width + COL_PADDING);
		}
	}
	
	private void setRowsHeight() {
		if(model.getRowCount()>0) {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(model.getFile(0));
			view.setRowHeight( icon.getIconHeight()+ROW_PADDING );
		}
		
	}

	public JTable getView() {
		return view;
	}

	public FileTableModel getModel() {
		return model;
	}
	
	public void setEnabled(boolean enabled) {
		view.setEnabled(enabled);
	}
	
	
}

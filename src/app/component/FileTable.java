package app.component;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import model.FileTableModel;
import util.Size;
import app.Application;
import app.renderer.FileTableCellRenderer;

public class FileTable {
	private static final int ROW_PADDING = 4;
	private static final int COL_PADDING = 6;
	private JTable view;
	private FileTableModel model;
	private MimetypesFileTypeMap mtftp;
	
	public FileTable() {
		mtftp = new MimetypesFileTypeMap();
		mtftp.addMimeTypes("image png tif jpg jpeg bmp gif PNG JPG JPEG TIF GIF BMP");
		
		model = new FileTableModel();
		view = new JTable(model);
		
		view.setAutoCreateRowSorter(false);
        view.setShowVerticalLines(false);
        view.setShowHorizontalLines(false);
        view.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set the custom renderer
        DefaultTableColumnModel colModel = (DefaultTableColumnModel) view.getColumnModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
        	TableColumn col = colModel.getColumn(i);
        	if(model.getColumnClass(i).equals(FileTime.class) || model.getColumnClass(i).equals(Size.class) || i==1)
        		col.setCellRenderer(new FileTableCellRenderer(this));
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
					
					// @TODO send the selected file(s) to the menu in order to display only actions that can be done
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
					Path file = model.getFile(row);
					if(Files.isDirectory(file) && Files.isReadable(file)) {
						System.out.println("open folder from table");
						Application.instance().openFolder(Application.instance().getCurrentPage().getTree().getSelectedNode(),file, true);
					}
					else if(Files.isReadable(file)) Application.instance().openFile(file);
				}
				
				else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
					Path file = model.getFile(row);
					String[] parts = file.getFileName().toString().split("\\.");
					String ext = parts[parts.length-1];
					String mimetype= mtftp.getContentType(file.toFile()).toLowerCase();
					System.out.println(mimetype);
			        String type = mimetype.split("/")[0];
					if(ext.equals("pdf")) {
						try {
							Application.instance().getCurrentPage().getViewer().setupPDF(file.toString());
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							System.err.println("Error loading pdf : "+e1.getMessage());
						}
					}
					else if(type.equals("image")) {
						try {
							System.out.println("image preview");
							Application.instance().getCurrentPage().getViewer().setupImage(file.toString());
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				
			}
		});
        
        
	}
	
	public List<Path> getSelectedFiles() {
		int[] indices = view.getSelectedRows();
		List<Path> lf = new ArrayList<Path>();
		for (int index : indices) {
			lf.add(model.getFile(index));
		}
		return lf;
	}
	
	public Path getLeadSelectedFile() {
		int index = view.getSelectionModel().getLeadSelectionIndex();
		return model.getFile(index);
	}
	
	public void setTableData(Path parentFolder) {
		model.setFiles(parentFolder, this);
	}
	
	/*public void setColumnsWidth() {
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
	
	public void setRowsHeight() {
		if(model.getRowCount()>0) {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(model.getFile(0).toFile());
			view.setRowHeight( icon.getIconHeight()+ROW_PADDING );
		}
		
	}*/
	
	public void setCellsSize() {
		Dimension iconSize = new Dimension(0,0);
		if(model.getRowCount()>0) {
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(model.getFile(0).toFile());
			iconSize.setSize(icon.getIconWidth() + COL_PADDING, icon.getIconHeight() + ROW_PADDING);
			view.setRowHeight( iconSize.height );
		}
		DefaultTableColumnModel colModel = (DefaultTableColumnModel) view.getColumnModel();
		colModel.getColumn(0).setPreferredWidth(iconSize.width);
		for (int i = 1; i < model.getColumnCount(); i++) {
			TableColumn col = colModel.getColumn(i);
			col.setPreferredWidth(model.getColumnPreferedWidth(i) + COL_PADDING);
		}
		
		
		
	}
	
	public void update() {
		setTableData(Application.instance().getCurrentPage().getNav().getStack().getSubdir());
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

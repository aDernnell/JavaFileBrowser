package app.component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import app.Application;
import app.renderer.FileTreeCellRenderer;


public class FileTree {
	private DefaultMutableTreeNode root;
	private DefaultMutableTreeNode currentSelectedNode;
	private DefaultTreeModel model;
	private JTree view;
	
	
	public FileTree() {
		root = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(root);
		
		initRoot();
        
        view = new JTree(model);
        
        view.setRootVisible(false);
        //view.setExpandsSelectedPaths(true);
        view.setCellRenderer(new FileTreeCellRenderer());
        //view.expandRow(0);
        
        view.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				//System.out.println(e);
				TreePath curTreePath = view.getClosestPathForLocation(e.getX(),e.getY());

		        //view.clearSelection();
		        //view.addSelectionPath(curTreePath);
				
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
				
			}
		});
        
        view.addTreeExpansionListener(new TreeExpansionListener() {
			
			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				DefaultTreeModel treeModel = (DefaultTreeModel) view.getModel();
		        TreePath newPath = new TreePath(treeModel.getPathToRoot(currentSelectedNode));

		        view.setSelectionPath(newPath);
		        view.scrollPathToVisible(newPath);
			}
			
			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
        
        view.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				//System.out.println(e);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)view.getLastSelectedPathComponent();
				if(/*view.isValid() &&*/ node != null) {
	                
	                Path p = (Path)node.getUserObject();
	                if(Files.isReadable(p)) {
	                	currentSelectedNode = node;
	                	System.out.println("open folder from tree");
		                Application.instance().openFolder((DefaultMutableTreeNode)node.getParent(),p, true);
	                }
            	}
			}
		}); 
	}
	
	private void initRoot() {
        Iterable<Path> dirs = FileSystems.getDefault().getRootDirectories();
        for (Path dir: dirs) {
        	DefaultMutableTreeNode node = new DefaultMutableTreeNode(dir);
        	root.add( node );
        }
	}
	
	public void showRootFile() {
        view.setSelectionInterval(0,0); // ensure that the main files are displayed
    }

	public DefaultTreeModel getModel() {
		return model;
	}

	public JTree getView() {
		return view;
	}
	
	public void setEnabled(boolean enabled) {
		view.setEnabled(enabled);
	}
	
	public DefaultMutableTreeNode getSelectedNode() {
		return currentSelectedNode;
	}
	
	public String getSelectedFolderName() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)view.getLastSelectedPathComponent();
        Path p = (Path)node.getUserObject();
        return p.getFileName().toString();
	}
	
	

}

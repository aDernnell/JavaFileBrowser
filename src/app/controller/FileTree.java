package app.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
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
        view.expandRow(0);
        
        view.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println(e);
				TreePath curTreePath = view.getClosestPathForLocation(e.getX(),e.getY());

		        view.clearSelection();
		        view.addSelectionPath(curTreePath);
				
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
				System.out.println(e);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)view.getLastSelectedPathComponent();
				if(/*view.isValid() &&*/ node != null) {
	                currentSelectedNode = node;
	                File f = (File)node.getUserObject();
	                System.out.println("open folder from tree");
	                Application.instance().openFolder((DefaultMutableTreeNode)node.getParent(),f, true);
	                
            	}
            	else currentSelectedNode = node;
			}
		}); 
	}
	
	private void initRoot() {
		// show the file system roots.
		FileSystemView fsv = FileSystemView.getFileSystemView();
        File[] roots = fsv.getRoots();
        for (File fileSystemRoot : roots) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
            root.add( node );
            System.out.println(fileSystemRoot.toString());
            File[] files = fsv.getFiles(fileSystemRoot, true);
            for (File file : files) {
                if (file.isDirectory()) {
                    node.add(new DefaultMutableTreeNode(file));
                }
            }
        }
	}
	
	/*private TreePath findTreePath(File fileToFind) {
        for (int i=0; i<view.getRowCount(); i++) {
            TreePath treePath = view.getPathForRow(i);
            Object object = treePath.getLastPathComponent();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)object;
            File nodeFile = (File)node.getUserObject();

            if (nodeFile==fileToFind) {
                return treePath;
            }
        }
        return null; // not found
    }*/
	
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
	
	

}

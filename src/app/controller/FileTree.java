package app.controller;

import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import app.Application;
import app.renderer.FileTreeCellRenderer;


public class FileTree {
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;
	private JTree view;
	
	public FileTree() {
		root = new DefaultMutableTreeNode();
		model = new DefaultTreeModel(root);
		
		TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent tse){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                Application.instance().setFolder(node);
                File f = (File)node.getUserObject();
            }
        };
        
        initRoot();
        
        view = new JTree(model);
        
        view.setRootVisible(false);
        view.addTreeSelectionListener(treeSelectionListener);
        view.setCellRenderer(new FileTreeCellRenderer());
        view.expandRow(0);
        
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
	
	private TreePath findTreePath(File fileToFind) {
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
	
	

}

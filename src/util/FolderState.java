package util;

import java.io.File;

import javax.swing.tree.DefaultMutableTreeNode;

import app.Application;

public class FolderState {
	private DefaultMutableTreeNode parentNode;
	private File subdir;
	private FolderState previous, next;
	
	public FolderState(){
		
	}
	
	public FolderState(DefaultMutableTreeNode parentNode, File subdir) {
		this.parentNode = parentNode;
		this.subdir = subdir;
	}
	
	public void applyState() {
		Application.instance().openFolder(parentNode, subdir, false);
	}
	
	public void setNext(FolderState fs) {
		fs.setPrevious(this);
		next = fs;
	}
	
	public void setPrevious(FolderState fs) {
		previous = fs;
	}
	
	public boolean hasNext() {
		return next!=null;
	}
	
	public boolean hasPrevious() {
		return previous!=null;
	}
	
	public FolderState getNext() {
		return next;
	}
	
	public FolderState getPrevious() {
		return previous;
	}

	public DefaultMutableTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(DefaultMutableTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public File getSubdir() {
		return subdir;
	}

	public void setSubdir(File subdir) {
		this.subdir = subdir;
	}
	
	
}

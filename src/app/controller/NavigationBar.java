package app.controller;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;

import util.FolderState;

public class NavigationBar {
	
	// Navigation buttons
    private JButton previous, next;
    
    // Buttons Container
    private JToolBar toolBar; // view
    
    // the stack for navigation in folders
    private FolderState stack; // model
    
    public NavigationBar() {
    	
    	Icon previousIcon = new ImageIcon("images/arrow_left_32.png");
		Icon nextIcon = new ImageIcon("images/arrow_right_32.png");
		
		previous = new JButton(previousIcon);
		previous.setEnabled(false);
		previous.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stack != null && stack.hasPrevious()) {
					stack.getPrevious().applyState();
					stack = stack.getPrevious();
					next.setEnabled(true);
					if(!stack.hasPrevious()) previous.setEnabled(false);
				}
				
			}
		});
		next = new JButton(nextIcon);
		next.setEnabled(false);
		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(stack != null && stack.hasNext()) {
					stack.getNext().applyState();
					stack = stack.getNext();
					previous.setEnabled(true);
					if(!stack.hasNext()) next.setEnabled(false);
				}
				
			}
		});
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(previous);
		toolBar.add(next);
    }
    
    // Navigation stack update
 	public void saveState(DefaultMutableTreeNode parentNode, File subdir) {
 		if(stack==null) {
 			System.out.println("saving first folder");
 			stack = new FolderState(parentNode, subdir);
 		}
 		// we don't save if we open the save folder many time consecutively
 		else if(!stack.getParentNode().equals(parentNode) && !stack.getSubdir().equals(subdir)){
 			System.out.println("saving folder");
 			stack.setNext(new FolderState(parentNode, subdir));
 			stack = stack.getNext();
 			previous.setEnabled(true);
 			next.setEnabled(false);
 		}
 	}
    
    public FolderState getStack() {
    	return stack;
    }
    
    public Container getView() {
    	return toolBar;
    }
}

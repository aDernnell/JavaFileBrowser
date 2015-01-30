package app.controller;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;

import util.FolderState;

public class NavigationBar {
	
    private JButton previous, next;
    private JToolBar toolBar;
    private JPanel container;
	private JProgressBar progressBar;
	private JTextField path;
    
    // the stack for navigation in folders
    private FolderState stack;
    
    public NavigationBar() {
    	
    	// Navigation buttons
    	Icon previousIcon = new ImageIcon("images/arrow_left_16.png");
		Icon nextIcon = new ImageIcon("images/arrow_right_16.png");
		
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
        
        progressBar = new JProgressBar();
        
        path = new JTextField();
        
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(toolBar);
        container.add(path);
        container.add(progressBar);
		
    }
    
    // Navigation stack update
 	public void saveState(DefaultMutableTreeNode parentNode, File subdir) {
 		if(stack==null) {
 			System.out.println("saving first folder");
 			stack = new FolderState(parentNode, subdir);
 		}
 		// we don't save if we open the save folder many time consecutively
 		else if(!sameState(parentNode, subdir)){
 			System.out.println("saving folder");
 			stack.setNext(new FolderState(parentNode, subdir));
 			stack = stack.getNext();
 			previous.setEnabled(true);
 			next.setEnabled(false);
 		}
 	}
 	
 	public boolean sameState(DefaultMutableTreeNode parentNode, File subdir) {
 		return stack!=null && stack.getParentNode().equals(parentNode) && stack.getSubdir().equals(subdir);
 	}
    
    public FolderState getStack() {
    	return stack;
    }
    
    public void activateProgressBar(boolean activate) {
		progressBar.setVisible(activate);
        progressBar.setIndeterminate(activate);
	}
    
    public void setPath(String str) {
		path.setText(str);
	}
    
    public Container getView() {
    	return container;
    }
}

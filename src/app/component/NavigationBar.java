package app.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.xswingx.PromptSupport;

import util.FolderState;
import app.Application;

public class NavigationBar {
	
    private JButton previous, next, refresh;
    private JToolBar toolBar;
    private JPanel container;
	private JProgressBar progressBar;
	private JTextField path;
	private JTextField filter;
    
    // the stack for navigation in folders
    private FolderState stack;
    
    public NavigationBar() {
    	
    	// Navigation buttons
    	Icon previousIcon = null;
		try {
			previousIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("images/arrow_left_16.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Icon nextIcon = null;
		try {
			nextIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("images/arrow_right_16.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Icon refreshIcon = null;
		try {
			refreshIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("images/refresh_16.png")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
		
		refresh = new JButton(refreshIcon);
		refresh.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				FileTable ft = Application.instance().getCurrentPage().getTable();
				activateProgressBar(true);
				ft.update();
				
			}
		});
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(previous);
		toolBar.add(next);
        
        progressBar = new JProgressBar();
        
        path = new JTextField();
        filter = new JTextField();
        PromptSupport.setPrompt("Filter", filter);
        
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(toolBar);
        container.add(path);
        container.add(progressBar);
        container.add(filter);
        container.add(refresh);
		
    }
    
    // Navigation stack update
 	public void saveState(DefaultMutableTreeNode parentNode, Path subdir) throws IOException {
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
 	
 	public DirectoryStream<Path> getStreamFromGlobbing(Path folder) throws IOException {
 		String filter = this.filter.getText().equals("") ? "*" : this.filter.getText();
 		return Files.newDirectoryStream(folder, filter);
 	}
 	
 	public boolean sameState(DefaultMutableTreeNode parentNode, Path subdir) throws IOException {
 		return stack!=null && stack.getParentNode().equals(parentNode) && Files.isSameFile(stack.getSubdir(),subdir);
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

package app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import util.DesktopAPI;
import app.component.FileTable;
import app.component.FileTree;
import app.component.NavigationBar;
import app.component.Pager;
import app.component.TabPage;


public class Application {

	private static Application instance; // singleton
	private static final String APP_NAME = "My explorer";
	
	// Main Gui container
	private JPanel gui;
    
    private Pager pager;
    

	public Container getGui() {
		if(gui==null) {
			gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));
            
			pager = new Pager();
			gui.add(pager.getView());
		}
		return gui;
	}
	
	public TabPage getCurrentPage() {
		return pager.getCurrentPage();
	}

	// When a file is opened
	public void openFile(Path f) {
		if(Files.isDirectory(f)) throw new IllegalArgumentException("File expected");
		DesktopAPI.open(f.toFile());
	}
	
	// When a folder is opened
	public void openFolder(final DefaultMutableTreeNode parentNode, final Path subdir, boolean saveState) {
		if(!Files.isDirectory(subdir)) throw new IllegalArgumentException("Folder expected");
		
		final NavigationBar nav = getCurrentPage().getNav();
		final FileTree tree = getCurrentPage().getTree();
		final FileTable table = getCurrentPage().getTable();
		
		
		try {
			if(nav.sameState(parentNode, subdir)) {
				System.out.println("folder already opened");
				return; // no need to open the folder already opened
			}
			
			if(saveState) nav.saveState(parentNode, subdir); // update the navigation stack
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		nav.setPath(subdir.toString()); // display the path of the folder
		
		tree.setEnabled(false);
		table.setEnabled(false);
		nav.activateProgressBar(true);
		
    	final int childrenCount = tree.getModel().getChildCount(parentNode);
    	
    	SwingWorker<Void, DefaultMutableTreeNode> worker1 = new SwingWorker<Void, DefaultMutableTreeNode>() {

			@Override
			protected Void doInBackground() throws Exception {
				// find the node corresponding to the subdir in the tree
				boolean found = false;
		    	for (int i = 0; i < childrenCount; i++) {
		    		DefaultMutableTreeNode child = (DefaultMutableTreeNode)tree.getModel().getChild(parentNode, i);
		    		Path f = (Path) child.getUserObject();
		    		if(f.equals(subdir)) {
		    			publish(child);
		    			found = true;
		    			break;
		    		}
				}
		    	if(!found) throw new IllegalStateException("Node not found");
		    	return null;
			}
			
			@Override
			protected void process(List<DefaultMutableTreeNode> chunks) { // in EDT
				final DefaultMutableTreeNode node = chunks.get(0);
				SwingWorker<Void, Path> worker2 = new SwingWorker<Void, Path>() {
		            @Override
		            public Void doInBackground() {
		            	try {
		            		DirectoryStream<Path> stream = nav.getStreamFromGlobbing(subdir);
		            		// lazy tree loading
			                if ( node.isLeaf()) {
			                	System.out.println("publish");
			            	    for (Path file: stream) {
			            	    	if(Files.isDirectory(file)) {
			            	    		publish(file);
			            	    	}
			            	    }
			                }
			                stream.close();
			                
			                //Fill the table with files ( /!\ call EDT thread with invoke later )
			                SwingUtilities.invokeLater(new Runnable() {
			                	public void run() {
			                		table.setTableData(subdir);
			                	}
			                });
		            	} catch (AccessDeniedException ex) {
		            		JOptionPane.showMessageDialog(gui, "Access denied to "+ex.getMessage(), "Error",
		    	                    JOptionPane.ERROR_MESSAGE);
						} catch (IOException e) {
							e.printStackTrace();
						}
		            	return null;
		            }

		            @Override
		            protected void process(List<Path> chunks) { // executed in EDT
		                for (Path child : chunks) {
		                    node.add(new DefaultMutableTreeNode(child)); // population of the tree
		                }
		            }

		            @Override
		            protected void done() {
		            	// select the node
		        		tree.getView().setSelectionPath(new TreePath(node.getPath()));
		        		tree.getView().scrollPathToVisible(new TreePath(node.getPath()));
		        		//getCurrentPage().adjustSize();
		        		
		                //nav.activateProgressBar(false); // done in FileTableModel
		                tree.setEnabled(true);
		                table.setEnabled(true);
		            }
		        };
		        worker2.execute();
			}
    		
    	};
    	worker1.execute();
		
	}
	
	// Singleton
	public static Application instance() {
		if(instance == null) instance = new Application();
		return instance;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception ex) {
                }
                JFrame f = new JFrame(APP_NAME);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                Application app = Application.instance();
                f.setContentPane(app.getGui());

                //@TODO fix resources access 
                try {
                    URL urlBig = this.getClass().getClassLoader().getResource("images/icon-32x32.png");
                    URL urlSmall = this.getClass().getClassLoader().getResource("images/icon-16x16.png");
                    ArrayList<Image> images = new ArrayList<Image>();
                    images.add( ImageIO.read(urlBig) );
                    images.add( ImageIO.read(urlSmall) );
                    f.setIconImages(images);
                } catch(Exception ex) {}

                f.pack();
                f.setLocationByPlatform(true);
                f.setMinimumSize(f.getSize());
                f.setVisible(true);

                Application.instance().getCurrentPage().getTree().showRootFile();
            }
        });
	}

}

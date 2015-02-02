package app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import app.component.FileTable;
import app.component.FileTree;
import app.component.NavigationBar;


public class Application {

	private static Application instance; // singleton
	private static final String APP_NAME = "My explorer";
	
	// Main Gui container
	private JPanel gui;
    
    private FileTree tree;
    
    private FileTable table;
    
    private NavigationBar nav;
    

	public Container getGui() {
		if(gui==null) {
			gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));
            
			tree = new FileTree();
			JScrollPane treeScroll = new JScrollPane(tree.getView());
			gui.add(treeScroll, BorderLayout.WEST);
			
			table = new FileTable();
			JScrollPane tableScroll = new JScrollPane(table.getView());
			gui.add(tableScroll);
			
			nav = new NavigationBar();
			gui.add(nav.getView(), BorderLayout.NORTH);
		}
		return gui;
	}
	
	public FileTree getTree() {
		return tree;
	}
	
	public FileTable getTable() {
		return table;
	}

	// When a file is opened
	public void openFile(File f) {
		if(f.isDirectory()) throw new IllegalArgumentException("File expected");
		try {
			Desktop.getDesktop().open(f);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// When a folder is opened
	public void openFolder(final DefaultMutableTreeNode parentNode, final File subdir, boolean saveState) {
		if(!subdir.isDirectory()) throw new IllegalArgumentException("Folder expected");
		
		if(nav.sameState(parentNode, subdir)) return; // no need to open the folder already opened
		
		if(saveState) nav.saveState(parentNode, subdir); // update the navigation stack
		
		nav.setPath(subdir.getAbsolutePath());
		
		tree.setEnabled(false);
		table.setEnabled(false);
		nav.activateProgressBar(true);
		
    	final int childrenCount = tree.getModel().getChildCount(parentNode);
    	
    	SwingWorker<Void, DefaultMutableTreeNode> worker1 = new SwingWorker<Void, DefaultMutableTreeNode>() {

			@Override
			protected Void doInBackground() throws Exception {
				// find the node corresponding to the subdir in the tree
		    	for (int i = 0; i < childrenCount; i++) {
		    		DefaultMutableTreeNode child = (DefaultMutableTreeNode)tree.getModel().getChild(parentNode, i);
		    		File f = (File) child.getUserObject();
		    		if(f.equals(subdir)) {
		    			publish(child);
		    			break;
		    		}
				}
		    	return null;
			}
			
			@Override
			protected void process(List<DefaultMutableTreeNode> chunks) { // in EDT
				final DefaultMutableTreeNode node = chunks.get(0);
				SwingWorker<Void, File> worker2 = new SwingWorker<Void, File>() {
		            @Override
		            public Void doInBackground() {

		            	final File[] files = FileSystemView.getFileSystemView().getFiles(subdir, true);
		            	
		            	// lazy tree loading
		                if ( node.isLeaf()) {
		                	System.out.println("publish");
		                    for (File child : files) {
		                        if (child.isDirectory()) {
		                            publish(child);
		                        }
		                    }
		                }
		                
		                //Fill the table with files ( /!\ call EDT thread with invoke later )
		                SwingUtilities.invokeLater(new Runnable() {
		                	public void run() {
		                		table.setTableData(files);
		                	}
		                });
		                return null;
		            }

		            @Override
		            protected void process(List<File> chunks) { // executed in EDT
		                for (File child : chunks) {
		                    node.add(new DefaultMutableTreeNode(child)); // population of the tree
		                }
		            }

		            @Override
		            protected void done() {
		            	// select the node
		        		tree.getView().setSelectionPath(new TreePath(node.getPath()));
		        		tree.getView().scrollPathToVisible(new TreePath(node.getPath()));
		        		
		                nav.activateProgressBar(false);
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

                Application.instance().tree.showRootFile();
            }
        });
	}

}

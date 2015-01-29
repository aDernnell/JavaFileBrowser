package app;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
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

import app.controller.FileTable;
import app.controller.FileTree;





public class Application {

	private static Application instance; // singleton
	private static final String APP_NAME = "My application";
	
	// Main Gui container
	private JPanel gui;
	
	// Used to open/edit/print files.
    private Desktop desktop;
    
    // Provides nice icons and names for files.
    private FileSystemView fileSystemView;
    
    // The file tree
    private FileTree tree;
    
    // The file table
    private FileTable table;
    

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
		}
		return gui;
	}
	
	public Desktop getDesktop() {
		return desktop;
	}
	
	public FileSystemView getFileSystemView() {
		return fileSystemView;
	}
	
	public FileTree getTree() {
		return tree;
	}
	
	// When a folder is selected
	public void setFolder(final DefaultMutableTreeNode node) {
		tree.setEnabled(false);
		// @TODO progress bar ON
		
		SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
                	final File[] files = fileSystemView.getFiles(file, true);
                    if (node.isLeaf()) {
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
                }
                return null;
            }

            @Override
            protected void process(List<File> chunks) {
                for (File child : chunks) {
                    node.add(new DefaultMutableTreeNode(child));
                }
            }

            @Override
            protected void done() {
                //@TODO progress bar OFF
                tree.setEnabled(true);
            }
        };
        worker.execute();
		
	}

	public Application() {
		fileSystemView = FileSystemView.getFileSystemView();
        desktop = Desktop.getDesktop();
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

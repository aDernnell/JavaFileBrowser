package app.component;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import util.Viewer;

public class TabPage {
	private JPanel panel;
	
	private JSplitPane splitPane1;
	private JSplitPane splitPane2;
	
	private FileTree tree;
    
    private FileTable table;
    
    private NavigationBar nav;
    
    private Viewer viewer;
    
    public TabPage() {
    	panel = new JPanel(new BorderLayout(3,3));
        panel.setBorder(new EmptyBorder(5,5,5,5));
        
    	splitPane1 = new JSplitPane();
    	splitPane2 = new JSplitPane();
    	
    	tree = new FileTree();
		JScrollPane treeScroll = new JScrollPane(tree.getView());
		splitPane1.add(treeScroll, JSplitPane.LEFT);
		
		table = new FileTable();
		JScrollPane tableScroll = new JScrollPane(table.getView());
		splitPane1.add(tableScroll, JSplitPane.RIGHT);
		
		nav = new NavigationBar();
		panel.add(nav.getView(), BorderLayout.NORTH);
		
		viewer = new Viewer();
		JScrollPane viewerScroll = new JScrollPane(viewer.getView());
		splitPane2.add(splitPane1, JSplitPane.LEFT);
		splitPane2.add(viewerScroll,JSplitPane.RIGHT);
		
		panel.add(splitPane2);
		
		splitPane1.setResizeWeight(0.3);
		splitPane2.setResizeWeight(0.5);
    }
    
    public FileTree getTree() {
		return tree;
	}
	
	public FileTable getTable() {
		return table;
	}
	
	public NavigationBar getNav() {
		return nav;
	}

	public Viewer getViewer() {
		return viewer;
	}
	
	public Container getView() {
		return panel;
	}
	
	public void adjustSize() {
		splitPane1.resetToPreferredSizes();
		splitPane2.resetToPreferredSizes();
	}
}

package app.component;

import java.awt.Container;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import app.Application;

public class Pager {
	private JTabbedPane pane;
	private HashMap<Integer, TabPage> pages;
	static int num = 1;
	
	public Pager() {
		pane = new JTabbedPane();
		pages = new HashMap<Integer, TabPage>();
		
        TabPage tp = new TabPage();
        //pages.put(new Integer(0), tp);
        pane.add("Tab 0", tp.getView());
        initTabClose(0,tp);
        
        pane.add("", new JLabel());
        initTabAdd(1);
            
        pane.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		
	}
	
	private void initTabClose(int i, TabPage page) {
        pane.setTabComponentAt(i,new CloseTabComponent(this,page));
    }
	private void initTabAdd(int i) {
		pane.setTabComponentAt(i, new AddTabComponent(this));
	}
	
	public Container getView() {
		return pane;
	}
	
	public TabPage getCurrentPage() {
		return ((CloseTabComponent)pane.getTabComponentAt(pane.getSelectedIndex())).getPage();
		//return pages.get(new Integer(pane.getSelectedIndex()));
	}
	
	public void addPage() {
		int lastIndex = pane.getTabCount()-1;
		
		
		// delete the add tab button
		pane.remove(lastIndex);
		pages.remove(new Integer(lastIndex));
		
		// add the new tab
		TabPage tp = new TabPage();
        pages.put(new Integer(lastIndex), tp);
        pane.add("Tab"+num, tp.getView());
        num++;
        initTabClose(lastIndex,tp);
        
        // add the add tab button
        pane.add("", new JLabel());
        initTabAdd(lastIndex+1);
        pane.getTabComponentAt(lastIndex+1).setFocusable(false);
        
        // select the right tab
        pane.setSelectedIndex(lastIndex);
        Application.instance().getCurrentPage().getTree().showRootFile();
	}
	
	public void removePage(int i) {
		int count = pane.getTabCount();
		pane.remove(i);
		if(i==count-2) {
			pane.setSelectedIndex(count-3);
		}
	}
}

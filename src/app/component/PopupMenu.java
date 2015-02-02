package app.component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import app.Application;
import app.actionlistener.BrowseActionListener;

public class PopupMenu extends JPopupMenu {
	
	private JMenuItem jMenuItemBrowse;
	private JMenuItem jMenuItemEdit;
	private JMenuItem jMenuItemCopy;
	private JMenuItem jMenuItemPaste;
	private JMenuItem jMenuItemRename;
	private JMenuItem jMenuItemSuppr;
	

	public PopupMenu() {
		this("Right click menu");
	}

	public PopupMenu(String label) {
		super(label);
		jMenuItemBrowse = new JMenuItem("Browse");
		jMenuItemBrowse.addActionListener(new BrowseActionListener(Application.instance().getTable()));
		jMenuItemEdit = new JMenuItem("Edit");
		jMenuItemCopy = new JMenuItem("Copy");
		jMenuItemPaste = new JMenuItem("Paste");
		jMenuItemRename = new JMenuItem("Rename");
		jMenuItemSuppr = new JMenuItem("Delete");
		
		add(jMenuItemBrowse);
		add(jMenuItemEdit);
		add(jMenuItemCopy);
		add(jMenuItemPaste);
		add(jMenuItemRename);
		add(jMenuItemSuppr);
		
	}

	
}

package app.component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import app.Application;
import app.actionlistener.BrowseActionListener;
import app.actionlistener.EditActionListener;
import app.actionlistener.OpenActionListener;

public class PopupMenu extends JPopupMenu {
	
	private JMenuItem jMenuItemOpen;
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
		FileTable ft = Application.instance().getTable();
		jMenuItemOpen = new JMenuItem("Open");
		jMenuItemOpen.addActionListener(new OpenActionListener(ft));
		jMenuItemBrowse = new JMenuItem("Browse");
		jMenuItemBrowse.addActionListener(new BrowseActionListener(ft));
		jMenuItemEdit = new JMenuItem("Edit");
		jMenuItemEdit.addActionListener(new EditActionListener(ft));
		jMenuItemCopy = new JMenuItem("Copy");
		jMenuItemPaste = new JMenuItem("Paste");
		jMenuItemRename = new JMenuItem("Rename");
		jMenuItemSuppr = new JMenuItem("Delete");
		
		add(jMenuItemOpen);
		add(jMenuItemBrowse);
		add(jMenuItemEdit);
		add(jMenuItemCopy);
		add(jMenuItemPaste);
		add(jMenuItemRename);
		add(jMenuItemSuppr);
		
	}

	
}

package app.component;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import app.Application;
import app.actionlistener.BrowseActionListener;
import app.actionlistener.CopyActionListener;
import app.actionlistener.DeleteActionListener;
import app.actionlistener.EditActionListener;
import app.actionlistener.NewFolderActionListener;
import app.actionlistener.OpenActionListener;
import app.actionlistener.PasteActionListener;
import app.actionlistener.RenameActionListener;

public class PopupMenu extends JPopupMenu {
	
	private JMenuItem jMenuItemOpen;
	private JMenuItem jMenuItemBrowse;
	private JMenuItem jMenuItemEdit;
	private JMenuItem jMenuItemCopy;
	private JMenuItem jMenuItemPaste;
	private JMenuItem jMenuItemRename;
	private JMenuItem jMenuItemSuppr;
	private JMenuItem jMenuItemNewFolder;
	

	public PopupMenu() {
		this("Right click menu");
	}

	public PopupMenu(String label) {
		super(label);
		FileTable ft = Application.instance().getCurrentPage().getTable();
		jMenuItemOpen = new JMenuItem("Open");
		jMenuItemOpen.addActionListener(new OpenActionListener(ft));
		jMenuItemBrowse = new JMenuItem("Browse");
		jMenuItemBrowse.addActionListener(new BrowseActionListener(ft));
		jMenuItemEdit = new JMenuItem("Edit");
		jMenuItemEdit.addActionListener(new EditActionListener(ft));
		jMenuItemCopy = new JMenuItem("Copy");
		jMenuItemCopy.addActionListener(new CopyActionListener(ft));
		jMenuItemPaste = new JMenuItem("Paste");
		jMenuItemPaste.addActionListener(new PasteActionListener(ft));
		jMenuItemRename = new JMenuItem("Rename");
		jMenuItemRename.addActionListener(new RenameActionListener(ft));
		jMenuItemSuppr = new JMenuItem("Delete");
		jMenuItemSuppr.addActionListener(new DeleteActionListener(ft));
		jMenuItemNewFolder = new JMenuItem("New Folder");
		jMenuItemNewFolder.addActionListener(new NewFolderActionListener(ft));
		
		add(jMenuItemOpen);
		add(jMenuItemBrowse);
		add(jMenuItemEdit);
		add(jMenuItemCopy);
		add(jMenuItemPaste);
		add(jMenuItemRename);
		add(jMenuItemSuppr);
		add(jMenuItemNewFolder);
		
	}

	
}

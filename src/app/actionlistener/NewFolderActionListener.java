package app.actionlistener;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JOptionPane;

import app.Application;
import app.component.FileTable;

public class NewFolderActionListener extends FileTableActionListener{

	public NewFolderActionListener(FileTable ft) {
		super(ft);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Path f = ft.getLeadSelectedFile();
		if(Files.isRegularFile(f)) {
			f = f.getParent();
		}
		Path folder = f;
		
		String name = (String)JOptionPane.showInputDialog(
                ft.getView(),
                "New folder name :",
                "New folder in "+folder,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "new_folder");
		
		if(name != null && name.length() > 0) {
			try {
				Files.createDirectory(Paths.get(folder.toString()+"/"+name));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Application.instance().getCurrentPage().getNav().activateProgressBar(true);
			ft.update();
		}
		
	}

}

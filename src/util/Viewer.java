package util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Viewer {
	private JLabel label;
	
	public Viewer() {
		label = new JLabel();
		label.setPreferredSize(new Dimension(300, 423));
	}

	public void setup(String path) throws IOException {

		PDDocument document = PDDocument.load(new File(path));
        List<PDPage> pages = document.getDocumentCatalog().getAllPages();
        PDPage pDPage = pages.get(0);
        Image img = pDPage.convertToImage();
        document.close();
        img = img.getScaledInstance(label.getWidth(), label.getHeight(), img.SCALE_SMOOTH);
        
        label.setIcon(new ImageIcon(img));
        
    }
	
	public Container getView() {
		return label;
	}
}

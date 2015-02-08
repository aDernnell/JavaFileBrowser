package util;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
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

	public void setupPDF(String path) throws IOException {
		// @TODO add workerThread
		PDDocument document = PDDocument.load(new File(path));
        List<PDPage> pages = document.getDocumentCatalog().getAllPages();
        PDPage pDPage = pages.get(0);
        BufferedImage image = pDPage.convertToImage();
        document.close();
        
        setImage(image);
        
    }
	
	public void setupImage(String path) throws IOException {
		// @TODO add workerThread
		BufferedImage image = ImageIO.read(new File(path));
		setImage(image);
	}
	
	public Container getView() {
		return label;
	}
	
	private void setImage(BufferedImage image) {
		float ratio;
		if(image.getWidth()>label.getWidth() || image.getHeight()>label.getHeight()) {
			if(image.getWidth()>image.getHeight()) {
				ratio = label.getWidth()/(float)image.getWidth();
				label.setIcon(new ImageIcon(image.getScaledInstance(label.getWidth(), (int)(image.getHeight()*ratio), image.SCALE_SMOOTH)));
			}
			else {
				ratio = label.getHeight()/(float)image.getHeight();
				label.setIcon(new ImageIcon(image.getScaledInstance((int)(image.getWidth()*ratio), label.getHeight(), image.SCALE_SMOOTH)));
			}
		}
		else label.setIcon(new ImageIcon(image));
	}
}

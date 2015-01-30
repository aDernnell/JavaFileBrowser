package util;

import java.text.DecimalFormat;

public class Size {
	long size;
	final String[] units = new String[] { "b", "Kb", "Mb", "Gb", "Tb" };
	
	public Size(long s) {
		this.size=s;
	}
	
	public String toString() {
		if(size <= 0) return "";
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
}

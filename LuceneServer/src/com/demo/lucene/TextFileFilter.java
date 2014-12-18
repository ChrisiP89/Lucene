package com.demo.lucene;

import java.io.File;
import java.io.FileFilter;

//This class is used as a .txt file filter
public class TextFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().toLowerCase().endsWith(".txt");
	}
}

package com.google.gwt.sample.stockwatcher.fileuploadserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

public class SaveFile {

	public void saveFile(HttpServletRequest request, List<FileItem> item)
			throws IOException {
		try {
			for (FileItem ite : item) {
				File file = File.createTempFile(ite.getFieldName(), ".rdf");
				ite.write(file);
				System.out.println("file saved as: " + file.getAbsolutePath());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

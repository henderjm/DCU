package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

public class SaveFileToServer {

	private String file_path = "";
	
	public void saveFile(HttpServletRequest req, List<FileItem> items) throws Exception{
		for(FileItem ite : items){
			File file = File.createTempFile(ite.getFieldName(), ".rdf");
			ite.write(file);
			System.out.println(ite.getFieldName());
			this.file_path = file.getAbsolutePath();
		}
	}
	public void setFilePath(String path){
		System.out.println("SaveFile CLass;" + path);
		this.file_path = path;
	}
	
	public String getFilePath(){
		return this.file_path;
	}
}

package com.google.gwt.sample.stockwatcher.fileuploadserver;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

@SuppressWarnings("serial")
public class FileUpload extends HttpServlet{
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		boolean isMultiPart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
		// creating factory for disk based files and an upload handler
		if(isMultiPart){
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		// parse request to retrieve individual items
		List<FileItem> items;
		SaveFile saveFile = new SaveFile(); // save file to temporary location
		FileUploadListener ful = new FileUploadListener(); // monitor file upload progress
		request.getSession().setAttribute("progress", ful);
		upload.setProgressListener(ful); // 
		try{
			items = upload.parseRequest(request);
		}catch(FileUploadException e){
			throw new ServletException("File upload failed: " + e);
		}
		saveFile.saveFile(request, items);
		}else{
		//	super.doPost(request, response);
			return;
		}
		
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{

		FileUploadListener ful = (FileUploadListener) request.getSession().getAttribute("progress");
		if(ful != null){
			StringBuilder builder = new StringBuilder();
			builder.append((ful.getBytesRead()));
	        builder.append(" / ");
	        builder.append((ful.getContentLength()));
	        response.getOutputStream().write(builder.toString().getBytes());
		}
	}
}

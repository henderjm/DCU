package com.google.gwt.sample.stockwatcher.fileuploadserver;

import org.apache.commons.fileupload.*;

public class FileUploadListener implements ProgressListener{
	private long kiloBytes = -1;
	private long bytesRead = -1;
	private long contentLength = -1;
	private int numOfItems = -1;
	
	@Override
	public void update(long bytesRead, long contentLength, int numOfItems) {
		// TODO Auto-generated method stub
		long kBytes = bytesRead / 1024;
		if(kiloBytes == kBytes)
			return;
		kiloBytes = kBytes;
		this.bytesRead = bytesRead;
		this.contentLength = contentLength;
		this.numOfItems = numOfItems;
	}
	public long getBytesRead(){
		return this.bytesRead;
	}
	public long getContentLength(){
		return this.contentLength;
	}
	public int getNumOfItems(){
		return this.numOfItems;
	}
}

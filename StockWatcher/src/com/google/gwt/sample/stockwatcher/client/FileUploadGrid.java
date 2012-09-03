//package com.google.gwt.sample.stockwatcher.client;
//
//import gxt.multiupload.model.FileUploadModel;
//import gxt.multiupload.model.Model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class FileUploadGrid extends EditorGrid<Model> {
//	
//	private static final String NAME = "Name";
//	private static final String STATE = "State";
//	private static final ColumnModel columnModel = createColumnsModel();
//	
//	public FileUploadGrid() {
//		super(new ListStore<FileUploadModel>(), columnModel);
//		getView().setForceFit(true);
//		getView().setAutoFill(true);
//		getView().setShowDirtyCells(false);
//		setBorders(false);
//		getStore().setMonitorChanges(true);
//	}
//
//	public static ColumnModel createColumnsModel() {
//		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
//		columns.add(new ColumnConfig(FileUploadModel.NAME, NAME, 250));
//		columns.add(new ColumnConfig(FileUploadModel.MESSAGE, STATE, 100));
//		ColumnModel columnsModel = new ColumnModel(columns);
//		return columnsModel;
//	}
//
//}
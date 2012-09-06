package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;

public class Ontology {
	private String name, file_path, baseURI;
	private ArrayList<String> classes = new ArrayList<String>();
	private ArrayList<String> properties = new ArrayList<String>();
	private ArrayList<String> literals = new ArrayList<String>();
	
	public Ontology(String n, String fp, String uri, ArrayList<String> c, ArrayList<String> pro, ArrayList<String> l){
		this.name =n; this.file_path = fp; this.baseURI = uri; 
		this.classes = c; this.properties=pro; this.literals = l;
	}
	
	public String getName(){
		return this.name;
	}
	public String getFilePath(){
		return this.file_path;
	}
	public String getBaseURI(){
		return this.baseURI;
	}
	public void setName(String ontName){
		this.name = ontName;
	}
	public void setBaseURI(String uri){
		this.baseURI = uri;
	}
	public void setClasses(ArrayList<String> str){
		this.classes = str;
	}
	public void setFilePath(String result) {
		this.file_path = result;
	}
	public void setProperties(ArrayList<String> str){
		this.properties = str;
	}
	public void setLiterals(ArrayList<String> str){
		this.literals = str;
	}
	public ArrayList<String> getClasses(){
		return this.classes;
	}
	public ArrayList<String> getProperties(){
		return this.properties;
	}
	public ArrayList<String> getLiterals(){
		return this.literals;
	}
}

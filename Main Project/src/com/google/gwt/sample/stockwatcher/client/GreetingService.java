package com.google.gwt.sample.stockwatcher.client;

import java.io.IOException;
import java.util.*;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	ArrayList<String> greetServer(ArrayList<String> list, String filename, int type);
	Integer[] wordExists(String search_word, String web_page) throws IllegalArgumentException, IOException;
	String[] sendToTripleStore(String[] triple) throws IOException;
	String getBaseURI();
	String filePath(); // returns filepath of uploaded file by user
	String loadOntologyFromWeb(String input) throws IOException;
	String getOntName();
	String downloadRepository(String input) throws IOException;
	ArrayList<String[]> suggestedTriples(String input);
	ArrayList<String> getChildren(String ontology, String parent) throws IllegalArgumentException;
	ArrayList<String[]> getInstances(String ontology, String parent, String context) throws IllegalArgumentException, IOException;
	ArrayList<String[]> getQueryInstances(String subject, String predicate, String object, String context);
}

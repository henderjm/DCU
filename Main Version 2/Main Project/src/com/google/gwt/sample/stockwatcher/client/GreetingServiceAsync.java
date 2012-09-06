package com.google.gwt.sample.stockwatcher.client;

import java.util.*;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(ArrayList<String> list, String filename,int type, AsyncCallback<ArrayList<String>> callback);
	void wordExists(String search_word, String web_page, AsyncCallback<Integer[]> callback)
			throws IllegalArgumentException;
	void sendToTripleStore(String[] triples, AsyncCallback<String[]> callback);
	void filePath(AsyncCallback<String> callback);
	void loadOntologyFromWeb(String input, AsyncCallback<String> callback);
	void getBaseURI(AsyncCallback<String> callback);
	void suggestedTriples(String input, AsyncCallback<ArrayList<String[]> > callback);
	void getOntName(AsyncCallback<String> callback);
	void getChildren(String ontology, String parent, AsyncCallback<ArrayList<String>> cb);
	void getInstances(String ontology, String parent, String context, AsyncCallback<ArrayList<String[]>> cb);
	void getQueryInstances(String subject, String predicate, String object, String context, AsyncCallback<ArrayList<String[]>> callback);
	void downloadRepository(String input, AsyncCallback<String> callback);
}

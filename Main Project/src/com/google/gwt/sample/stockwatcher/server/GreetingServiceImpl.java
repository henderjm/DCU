package com.google.gwt.sample.stockwatcher.server;

import com.google.gwt.sample.stockwatcher.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.xml.internal.fastinfoset.Encoder;

import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.jsoup.Jsoup;
import org.openrdf.repository.Repository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.UnsupportedRDFormatException;
import org.semanticweb.owlapi.apibinding.*;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * The server side implementation of the RPC service.
 */

@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {
	/*
	 * TODO internet upload 
	 */
	private File file;
	private FileItem file_item;
	private int modifiedIndex;
	private String path_file = "";
	private String baseURI = "";
	private String ontName = "";
	private Sesame ses = new Sesame();
	private SuggestedTriple ST = new SuggestedTriple();
	private OWLClass thing;
	SaveFileToServer save_file = new SaveFileToServer();
	OntologyToSesame Ont_upload = new OntologyToSesame();

	/**
	 * Setting up http request handler for User file uploads
	 */
	protected void service(final HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
		if (isMultipart) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			try {
				@SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(request);
				file_item = items.get(0);
				System.out.println("\t\t\tScrew this" + file_item.getFieldName());
				// System.out.println("Name:"+file.getName());
				save_file.saveFile(request, items);
			} catch (FileUploadException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			super.service(request, response);
		}
		Ont_upload.upload(save_file.getFilePath());
		baseURI = Ont_upload.getBaseURI(save_file.getFilePath()) + "#";

	}

	public String loadOntologyFromWeb(String input) throws IOException {
		File src = new File(input);
		File dest = File.createTempFile("UploadFromWeb", ".rdf");
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		save_file.setFilePath(dest.getAbsolutePath());
		return null;
	}

	@Override
	public ArrayList<String> greetServer(ArrayList<String> arrayList, String input, int type) {
		// TODO Auto-generated method stub
		// Getting file
		file = new File(input);
		ontName = file.getName();
		ArrayList<String> newList = new ArrayList<String>();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);

			// model.getNsPrefixURI(arg0)
			if (type == 1)
				getClasses(ontology, newList); // get classes into an arrayList
			if (type == 2) {
				getProperty_Resources(ontology, newList); // get classes into an
															// arrayList
			}
			if (type == 3)
				getProperty_Literals(ontology, newList); // get classes into an
															// arrayList
			manager.removeOntology(ontology);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}

		arrayList = newList;
		// newList.clear()''
		return arrayList;
	}

	// This is not necessary but decided to break the code up a bit.
	private ArrayList<String> getClasses(OWLOntology model, ArrayList<String> ontologyList) {
		boolean hasURI = false;

		Set<OWLClass> it = model.getClassesInSignature();
		Iterator<OWLClass> iter = it.iterator();
		while (iter.hasNext()) {
			OWLClass name = iter.next();

			if (hasURI == false) {
				String uri = name.getSignature().toString();
				if (uri.equalsIgnoreCase("[owl:Thing]") && iter.hasNext()) {
					System.out.println("FOUND: " + uri);
					name = iter.next();
					uri = name.getSignature().toString();
				} else if (uri.equalsIgnoreCase("[owl:Thing]")) {
					return ontologyList;
				}

				int indexOfhash = uri.indexOf('#', 0) + 1;
				uri = uri.substring(indexOfhash, uri.length() - 2);
				// System.out.println(uri); // HAVE BASE URI!!!!!
				ontologyList.add(uri);
			}
		}

		return ontologyList;
	}

	private ArrayList<String> getProperty_Resources(OWLOntology model, ArrayList<String> ontologyList) {
		Set<OWLObjectProperty> property_resources = model.getObjectPropertiesInSignature();
		Iterator<OWLObjectProperty> iterator = property_resources.iterator();
		while (iterator.hasNext()) {
			OWLObjectProperty name = iterator.next();

			if (name.getClass().getName() != null) {
				String uri = name.getSignature().toString();
				int indexOfhash = uri.indexOf('#', 0) + 1;
				uri = uri.substring(indexOfhash, uri.length() - 2);
				// System.out.println(uri); // HAVE BASE URI!!!!!
				ontologyList.add(uri);
			}
		}
		// System.out.println(ontologyList.size());
		return ontologyList;
	}

	private ArrayList<String> getProperty_Literals(OWLOntology model, ArrayList<String> ontologyList) {
		Set<OWLDataProperty> property_literals = model.getDataPropertiesInSignature();
		Iterator<OWLDataProperty> iterator = property_literals.iterator();
		while (iterator.hasNext()) {
			OWLDataProperty name = iterator.next();
			if (name.getClass().getName() != null) {
				String uri = name.getSignature().toString();
				int indexOfhash = uri.indexOf('#', 0) + 1;
				uri = uri.substring(indexOfhash, uri.length() - 2);
				// System.out.println(uri); // HAVE BASE URI!!!!!
				ontologyList.add(uri.concat("*"));
			}
			// System.out.println("Literal: " + name.getLocalName());

		}
		if (ontologyList.size() == 0)
			ontologyList.add("EMPTY");
		// System.out.println(ontologyList.size());
		return ontologyList;
	}

	@Override
	public Integer[] wordExists(String searchWord, String webPage) throws IllegalArgumentException, IOException {
		Integer[] start_and_end_index = new Integer[2];
		// File file = new File("/tmp/input.html");
		// System.out.println(webPage + "/" + searchWord);

		// Document doc = Jsoup.connect(input).get();
		String html;
		int index = 0;
		setIndex(index);
		System.out.println("The global index is: " + this.modifiedIndex);
		URL webpage = new URL(webPage);
		URLConnection conn = webpage.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		while ((html = br.readLine()) != null) {
			html = Jsoup.parse(html).text();
			// try {
			if (check_FullString(searchWord, html) == true) {
				start_and_end_index[0] = getIndex();
				start_and_end_index[1] = start_and_end_index[0] + (searchWord.length() - 1);
				System.out.println("\tFINAL index: " + getIndex());
				System.out.println(searchWord + ": was found on selected page!");
				ses.subjectOnRepository(webPage + "/", searchWord, start_and_end_index);
				return start_and_end_index;
			} else {
				// System.out.println("FALSE");
				index += html.length();
				setIndex(index);
			}
		}
		System.out.println(searchWord + ": was NOT found on selected page!");
		start_and_end_index[0] = -99; // to show word is not in document
		start_and_end_index[1] = -99;
		return start_and_end_index;
	}

	@SuppressWarnings("unused")
	private boolean checkString(String search, String html) {
		int index = getIndex();
		String temp;
		System.out.println("\t\t" + html + "/" + search);
		// Using tokeniser to break up input string to search for word
		StringTokenizer strtk = new StringTokenizer(html, " /().',");
		while (strtk.hasMoreTokens()) {
			temp = strtk.nextToken();
			System.out.println(temp + ":" + search);
			if (temp.equals(search)) {
				setIndex(index + html.indexOf(temp));
				System.out.println("Index of first character: " + html.indexOf(temp));

				return true; // Word has been found in html page
			}
		}
		return false; // Word is not in this string
	}

	private boolean check_FullString(String search, String html) {
		int startingIndex = html.indexOf(search);
		setIndex(startingIndex);
		if (startingIndex == -1) {
			return false;
		}
		System.out.println(html + "/" + search);
		return true;
	}

	public void setIndex(int index) {
		this.modifiedIndex = index;
	}

	public int getIndex() {
		return this.modifiedIndex;
	}

	@Override
	public String[] sendToTripleStore(String[] triple) throws IOException {
		/*
		 * Setup connection to sesame to upload Triple
		 */
		/*
		 * direct upload
		 */
		boolean object;
		System.out.println(baseURI);
		Repository rep = new HTTPRepository("http://localhost:8080/openrdf-sesame/", "University");		
		ValueFactory fa = rep.getValueFactory();
		URI sub = fa.createURI(triple[0]);
		URI pre;
		URI rObject = null;
		Literal lObject = null;
		URI context = fa.createURI("http://www.cngl.ie");
		if (triple[1].equals("RDF.type")) {
			System.out.println("RDF.TYPE");
			pre = RDF.TYPE;
			object = true;
		}
		else if (triple[1].endsWith("*")) {
			System.out.println("Literal");
			lObject = fa.createLiteral(triple[2]);
			triple[1] = triple[1].substring(0, triple[1].length() - 1);
			pre = fa.createURI(triple[1]);
			System.out.println(triple[1]);
			object = false;
		} 
		else{
			System.out.println("Resource");
			rObject = fa.createURI(triple[2]);
			pre = fa.createURI(triple[1]);
			object = true;
		}
		try {
			rep.initialize();
			RepositoryConnection con = rep.getConnection();
			
			System.out.println("Object or Literal [t/f]: " + object);
			if(object) 
				con.add(sub, pre, rObject, context);
			else 
				con.add(sub, pre, lObject, context);
			con.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		}

		System.out.println("FILE UPLOADED");
		return triple;
	}

	public String filePath() {
		path_file = save_file.getFilePath();
		System.out.println("Calling to server for filepath: " + path_file);
		return path_file;
	}

	public String getBaseURI() {
		return baseURI;
	}

	public String getOntName() {
		System.out.println("Ontology name: " + ontName);
		return ontName;
	}

	@Override
	public ArrayList<String[]> suggestedTriples(String input) {
		System.out.println(input);
		ArrayList<String[]> result = new ArrayList<String[]>();
		result = ST.getSuggestedTriples(input);
		return result;
	}

	@Override
	public ArrayList<String> getChildren(String ontology, String parent) throws IllegalArgumentException {
		ArrayList<String> blah = new ArrayList<String>();
		OWLClassExpression temp = null;
		OWLOntology ont = null;
		System.out.println("Setting up tree");
		File file = new File(ontology);
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			System.out.println("Reading Ontology..");
			ont = manager.loadOntologyFromOntologyDocument(file);
			System.out.println(".. Done");
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		if (parent.equals("Thing")) {
			System.out.println("WOoOoOoOoOoO");
			thing = null;
			Set<OWLClass> set = ont.getClassesInSignature();
			Iterator<OWLClass> it = set.iterator();
			while (it.hasNext()) {
				thing = it.next();
				System.out.println(thing.getSignature().toString());
				if (thing.getSignature().toString().equals("[owl:Thing]")) {
					break;
				}
			}
			blah.add(thing.getSignature().toString());

		} else if (parent.equals("[owl:Thing]")) {
			System.out.println("Here1");
			Set<OWLClass> set = ont.getClassesInSignature();
			Iterator<OWLClass> it = set.iterator();
			System.out.println("\n\n");
			while (it.hasNext()) {
				thing = it.next();
				System.out.println(thing.getSignature().toString());
				if (thing.getSignature().toString().equals(parent)) {
					break;
				}

			}
			Set<OWLClassExpression> s = thing.getSubClasses(ont);
			Iterator<OWLClassExpression> iter = s.iterator();
			System.out.println(s.size());
			System.out.println(thing.getSignature().toString());
			while (iter.hasNext()) {
				temp = iter.next();
				String name = temp.getSignature().toString();
				name = name.substring(2, name.length() - 2);
				System.out.println("Reading..." + name);
				blah.add(name);
			}
		} else {
			System.out.println("Here2");
			OWLClassExpression oce = null;
			OWLClass match = null;
			Set<OWLClass> set = ont.getClassesInSignature();
			Iterator<OWLClass> it = set.iterator();
			OWLClass other = null;
			outter: while (it.hasNext()) {
				other = it.next();
				Set<OWLClassExpression> s = other.getSubClasses(ont);
				Iterator<OWLClassExpression> itera = s.iterator();
				System.out.println("Here3");
				while (itera.hasNext()) {
					oce = itera.next();
					if (oce.getSignature().toString().equals("[<" + parent + ">]")) {
						match = oce.asOWLClass();
						System.out.println("FOUND");
						break outter;
					} else if (oce.getSignature().toString().equals(parent)) {
						match = oce.asOWLClass();
						System.out.println("FOUND");
						break outter;
					}

				}
			}
			set.clear();
			Set<OWLClassExpression> sets = match.getSubClasses(ont);
			Iterator<OWLClassExpression> ii = sets.iterator();
			while (ii.hasNext()) {
				OWLClassExpression css = ii.next();
				String name = css.asOWLClass().getSignature().toString();
				name = name.substring(2, name.length() - 2);
				System.out.println("Reading..." + name);
				blah.add(name);
				System.out.println(css.getSignature().toString());
			}
		}
		manager.removeOntology(ont);
		return blah;
	}

	@SuppressWarnings("unused")
	@Override
	public String downloadRepository(String input) throws IOException {
		String filepath = "";
		Repository myrepo = new HTTPRepository("http://localhost:8080/openrdf-sesame", "University");
		ValueFactory fa = myrepo.getValueFactory();
		System.out.println("context: " + input);
		URI context = null;
		if (input != null)
			context = fa.createURI("http://www.cngl.ie");

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File downFile = File.createTempFile("downloadedFile", ".rdf");
		try {
			RepositoryConnection con = myrepo.getConnection();
			try {
				con.export(Rio.createWriter(RDFFormat.RDFXML, new OutputStreamWriter(new FileOutputStream(downFile), Encoder.UTF_8)));
				OWLOntology owl = manager.loadOntologyFromOntologyDocument(downFile);
				filepath = downFile.getAbsolutePath();
				Set<OWLClass> set = owl.getClassesInSignature();
				Iterator<OWLClass> i = set.iterator();
				while (i.hasNext()) {
					OWLClass o = i.next();
					System.out.println("Printing Class: " + o.getSignature().toString());
				}
				manager.removeOntology(owl);

			} catch (UnsupportedRDFormatException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (RDFHandlerException e) {
				e.printStackTrace();
			} catch (OWLOntologyCreationException e) {
				e.printStackTrace();
			}
			con.close();
		} catch (RepositoryException e1) {
			e1.printStackTrace();
		}
		return filepath;
	}

	@SuppressWarnings( "unused" )
	@Override
	public ArrayList<String[]> getInstances(String ontology, String parent, String c) throws IllegalArgumentException, IOException {

		ArrayList<String[]> blah = new ArrayList<String[]>();
		System.out.println("here3");
		boolean iscontext = false;
		try {
			Repository myRepository = new HTTPRepository("http://localhost:8080/openrdf-sesame/", "University");
			RepositoryConnection con = myRepository.getConnection();
			ValueFactory factory = myRepository.getValueFactory();
			URI subject = null;
			URI predicate = null;
			URI object = null;
			URI context = null;
			if (!c.equals("")) {
				context = factory.createURI(c);
				System.out.println("NOT EMPTY CONTEXT");
				iscontext = true;
			}
			object = factory.createURI(parent);
			System.out.println("Preparing to query...");
			if (iscontext) {
				System.out.println("NOT EMPTY CONTEXT: " + context.stringValue());
			}
			RepositoryResult<Statement> result = iscontext ? con.getStatements(subject, RDF.TYPE, object, false, context) : con
					.getStatements(subject, RDF.TYPE, object, false);
			while (result.hasNext()) {
				Statement st = result.next();

				String[] queryResults = { st.getSubject().toString(), st.getPredicate().stringValue(), st.getObject().stringValue() };
				blah.add(queryResults);
				System.out.println("Results" + st.getSubject() + "\t\t" + st.getPredicate() + "\t\t" + st.getObject() + "\t\t");
			}
			con.close();
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Query!");
		return blah;
	}

	@SuppressWarnings("unused")
	@Override
	public ArrayList<String[]> getQueryInstances(String s, String p, String o, String c) {
		System.out.println("" + s + "\t\t" + p + "\t\t" + o + "\t\t" + c);
		ArrayList<String[]> instances = new ArrayList<String[]>();
		Iterator<String[]> it = instances.iterator();
		Repository myRepository = new HTTPRepository("http://localhost:8080/openrdf-sesame/", "University");
		ValueFactory factory = myRepository.getValueFactory();
		URI subject = null;
		subject = s.equals("NONE") ? null : factory.createURI(s);
		URI predicate = null;
		URI object = null;
		URI context = null;

		if (p.equals("RDF.type"))
			predicate = RDF.TYPE;
		else
			predicate = p.equals("NONE")? null : factory.createURI(p);
		object = o.equals("NONE") ? null : factory.createURI(o);
		context = c.equals("NONE") ? null : factory.createURI(c);
		try {
			RepositoryConnection con = myRepository.getConnection();
			RepositoryResult<Statement> result;
			result = c.equals("NONE") ? con.getStatements(subject, p.equals("NONE") ? null : predicate, (o.equals("NONE") ? null : object),
					false) : con.getStatements(s.equals("NONE") ? null : subject, p.equals("NONE") ? null : predicate,
					o.equals("NONE") ? null : object, false, context);
			while (result.hasNext()) {
				Statement st = result.next();
				String[] queryResults = { st.getSubject().toString(), st.getPredicate().stringValue(), st.getObject().stringValue() };
				instances.add(queryResults);
				System.out.println("b: " + st.getSubject() + "\t\t" + st.getPredicate() + "\t\t" + st.getObject() + "\t\t"
						+ st.getContext());
			}
			con.close();
		} catch (RepositoryException e) {

		}

		return instances;
	}
}
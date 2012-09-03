package com.google.gwt.sample.stockwatcher.server;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.openrdf.OpenRDFException;
import org.openrdf.model.URI;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;

public class OntologyToSesame {
	private String baseURI = "";

	// Upload to Sesame server
	public void upload(String file_path) {
		File file = new File(file_path);
		String sesame_server = "http://localhost:8080/openrdf-sesame";
		String database_name = "University";
		Repository myRepo = new HTTPRepository(sesame_server, database_name);
		try {
			myRepo.initialize();
			RepositoryConnection con = myRepo.getConnection();
			try {
				con.add(file, "file:///" + file.getAbsolutePath(),
						RDFFormat.forFileName(file.getPath()));

			} catch (RepositoryException re) {

			}

			finally {
				con.close();
			}
		} catch (OpenRDFException e) {

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO NOT Connected\n\n");
		}
		System.out.println("here, unfortunately");

	}

	// Store baseURI of uploaded ontology to sesame
	public String getBaseURI(String input) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		File temp = new File(input);
		try {
			OWLOntology model = manager.loadOntologyFromOntologyDocument(temp);

//			Iterator<OWLClass> iter = model.getClassesInSignature().iterator();
			this.baseURI = model.getOntologyID().getOntologyIRI().toString();
			
//			if (iter.hasNext()) {
//				OWLClass owlClass = iter.next();
//				this.baseURI = owlClass.getSignature().toString();
//				this.baseURI = this.baseURI.substring(2,
//						this.baseURI.indexOf('#', 0) + 1);
//				System.out.println(this.baseURI.indexOf('#', 0));
//
//			}
			manager.removeOntology(model);
			temp.deleteOnExit();
			return this.baseURI;
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			return "Ontology could not be created";
		}
		
	}

	// Repository myRepo = new HTTPRepository(sesame_server, database_name);
	// try {
	// myRepo.initialize();
	// System.out.println("Connection: " + myRepo.isInitialized());
	// // File file = new
	// File("/Users/markhender/ontologies/pizzas/pizzas.owl");
	// RepositoryConnection con = myRepo.getConnection();
	// URI uri ;
	// try {
	// System.out.println("\there2");
	// con.add(file, "file:///" + file.getAbsolutePath(),
	// RDFFormat.forFileName(file.getPath()));
	// // con.getNamespace(model.getOntologyID().toString());
	// OWLOntologyID temp = model.getOntologyID();
	//
	// // IRI possible = temp.getOntologyIRI();
	// // System.out.println(possible);
	// // System.out.println(possible + "\n");
	// } catch (RepositoryException re) {
	// re.printStackTrace();
	// } finally {
	// con.close();
	// }
	// } catch (OpenRDFException e) {
	//
	// // handle exception
	// } catch (IOException e) {
	// e.printStackTrace();
	// System.out.println("IO NOT Connected\n\n");
	// // handle io exception
	// }logyToSesame {

}

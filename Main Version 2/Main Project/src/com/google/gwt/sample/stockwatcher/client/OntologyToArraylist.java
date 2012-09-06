//package com.google.gwt.sample.stockwatcher.client;
//
//import java.util.*;
//import java.io.*;
//import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.model.OWLClass;
//import org.semanticweb.owlapi.model.OWLOntology;
//import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyManager;
//
//import com.google.gwt.user.client.rpc.IsSerializable;
//
//
//public class OntologyToArraylist implements IsSerializable {
//	
//	private String path;
//	
//	public OntologyToArraylist(String str){
//		this.path = str;
//	}
//	
//	public ArrayList<String> getClasses(){
//		ArrayList<String> classes = new ArrayList<String>();
//		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
//		File file = new File("/Users/markhender/ontologies/pizzas/pizza.rdf");
//		try {
//			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(file);
//			Set<OWLClass> it = ontology.getClassesInSignature();
//			System.out.println("Size of set: " + it.size());
//			Iterator<OWLClass> iter = it.iterator();
//			while (iter.hasNext()) {
//				OWLClass name = iter.next();
//				if (name.isOWLClass() && name.getClass().getName() != null) {
//					System.out.println(name.getSignature());
//					classes.add(name.getSignature().toString());
//				}
//			}
//			// arrayList
//		} catch (OWLOntologyCreationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return classes;
//	}
//}

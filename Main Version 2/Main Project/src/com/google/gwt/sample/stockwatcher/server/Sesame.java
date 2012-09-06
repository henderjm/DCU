package com.google.gwt.sample.stockwatcher.server;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;

public class Sesame {
	private URI subject;

	public void subjectOnRepository(String webpage, String search, Integer[] indexes) {
		Repository myRepository = new HTTPRepository(
				"http://localhost:8080/openrdf-sesame/", "University");
		String content = webpage + search.replace(' ', '_');
		try {
			System.out.println("MADE IT!");
			myRepository.initialize();
			ValueFactory factory = myRepository.getValueFactory();
			subject = factory.createURI(content);
			RepositoryConnection con = myRepository.getConnection();
			RepositoryResult<Statement> statements = con.getStatements(subject,
					null, null, false);
			if (statements.asList().isEmpty()) {
				System.out.println("ADD " + subject.toString() + " to Repo");
				con.add(subject, org.openrdf.model.vocabulary.RDFS.LABEL, factory.createURI(webpage + indexes[0] + "_" + indexes[1]),
						factory.createURI("http://www.cngl.ie"));
			} else
				System.out.println("DONT! ADD " + content + " to Repo");
			
			con.close();
			statements.close();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

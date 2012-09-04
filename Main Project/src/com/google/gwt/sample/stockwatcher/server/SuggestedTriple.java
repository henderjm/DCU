package com.google.gwt.sample.stockwatcher.server;

import java.util.*;
import org.openrdf.model.Literal;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.Value;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.http.HTTPRepository;

public class SuggestedTriple {

	public ArrayList<String[]> getSuggestedTriples(String in) {
		int index = in.lastIndexOf('/') + 1;
		String subject = in.substring(index);
		ArrayList<String[]> st = new ArrayList<String[]>();
		Repository myRepository = new HTTPRepository("http://localhost:8080/openrdf-sesame/", "University");
		ValueFactory factory = myRepository.getValueFactory();
		URI context = factory.createURI("http://www.cngl.ie");
		try {
			RepositoryConnection con = myRepository.getConnection();
			RepositoryResult<Statement> result = con.getStatements(null, null, null, false, context);
			while (result.hasNext()) {
				Statement statement = result.next();
				Resource sub = statement.getSubject();

				String name = sub.stringValue();
				index = name.lastIndexOf('/') + 1;
				name = name.substring(index);
				if (subject.equals(name)) {
					Value object = statement.getObject();
					if (object instanceof Literal) {
						String[] triple = { name, statement.getPredicate().stringValue() + "*", object.stringValue() };
						st.add(triple);
					} else {
						String[] triple = { name, statement.getPredicate().stringValue(), statement.getObject().stringValue() };
						st.add(triple);
					}
				}
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
		return st;
	}
}

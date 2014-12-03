package de.rwth.lofip.library;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class SchemaCreatingSchemaOutput extends SchemaOutputResolver {

	public StringWriter out = new StringWriter();

	@Override
	public Result createOutput(String namespaceUri, String suggestedFilename)
			throws IOException {
		StreamResult res = new StreamResult(out);
		res.setSystemId(suggestedFilename);
		return res;
	}

	public Schema createSchema() throws SAXException {
		return SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
				.newSchema(new StreamSource(new StringReader(out.toString())));
	}

}

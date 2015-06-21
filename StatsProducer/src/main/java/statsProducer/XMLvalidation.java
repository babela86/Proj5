package statsProducer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class XMLvalidation {

	private static final Logger log = Logger.getLogger(XMLvalidation.class.getName());

	public static void validateXMLSchema(String xmlPath) throws SAXException, IOException {
		String xsdPath = Paths.get("src/main/resources/noticia.xsd").toString();
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = factory.newSchema(new File(xsdPath));
		Validator validator = schema.newValidator();
		validator.validate(new StreamSource(new File(xmlPath)));
		log.info("A validacao do XML foi realizada com sucesso.");
	}
}


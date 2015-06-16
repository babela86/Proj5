package statsProducer;

import java.io.StringReader;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class StringToXML {
	
	private static final Logger log = Logger.getLogger(StringToXML.class.getName());
	
	public static Document stringToXML(String xml) {

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(xml));
			log.info("Conversão de String para XML com sucesso.");
			return builder.parse(is);
		} catch (Exception e) {
			log.severe("A conversão de String para XML falhou -> " + e.getMessage());
		}
		return null;
	}
}

package htmlReceiver;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Converter {

	public static boolean convertXMLtoHTML() {
		boolean convert = false;
		// Input xml data file
		String xmlInput = "noticiasoutput.xml";

		// Input xsl (stylesheet) file
		String xslInput = "noticia.xsl";

		// Output html file
		String htmlOutput = "noticiasoutputHTML.html";

		// Set the property to use xalan processor
		System.setProperty("javax.xml.transform.TransformerFactory",
				"org.apache.xalan.processor.TransformerFactoryImpl");

		// try with resources
		try (FileOutputStream os = new FileOutputStream(htmlOutput)) {
			FileInputStream xml = new FileInputStream(xmlInput);
			FileInputStream xsl = new FileInputStream(xslInput);

			// Instantiate a transformer factory
			TransformerFactory tFactory = TransformerFactory.newInstance();

			// Use the TransformerFactory to process the stylesheet source and
			// produce a Transformer
			StreamSource styleSource = new StreamSource(xsl);
			Transformer transformer = tFactory.newTransformer(styleSource);

			// Use the transformer and perform the transformation
			StreamSource xmlSource = new StreamSource(xml);
			StreamResult result = new StreamResult(os);
			transformer.transform(xmlSource, result);
			convert = true;
		} catch (Exception e) {
			e.printStackTrace();
			convert = false;
		}
		return convert;
	}
}

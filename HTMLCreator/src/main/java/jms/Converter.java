package jms;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Converter {

	public static void convertXMLtoHTML() {
		String agora = String.valueOf(new java.util.Date().getTime());
		String xmlInput = "src//main//resources//noticiasoutput.xml";
		String xslInput = "src//main//resources//noticia.xsl";
		String htmlOutput = "src//main//resources//archive//noticiasoutputHTML"	+ agora + ".html";
		System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");
		try (FileOutputStream os = new FileOutputStream(htmlOutput)) {
			FileInputStream xml = new FileInputStream(xmlInput);
			FileInputStream xsl = new FileInputStream(xslInput);
			TransformerFactory tFactory = TransformerFactory.newInstance();
			StreamSource styleSource = new StreamSource(xsl);
			Transformer transformer = tFactory.newTransformer(styleSource);
			StreamSource xmlSource = new StreamSource(xml);
			StreamResult result = new StreamResult(os);
			transformer.transform(xmlSource, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

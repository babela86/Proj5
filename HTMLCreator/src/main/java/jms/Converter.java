package jms;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Converter {

	private static final Logger log = Logger.getLogger(Converter.class.getName());

	static FileOutputStream os = null;
	static FileInputStream xml = null;
	static FileInputStream xsl = null;
	static Transformer transformer = null;

	public static void convertXMLtoHTML() throws Exception {

		String xmlInput = Paths.get("src/main/resources/noticiasoutput.xml").toString();
		String xslInput = Paths.get("src/main/resources/noticia.xsl").toString();
		String htmlOutput = Paths.get("src/main/resources/archive/noticiasoutputHTML"	+ agora() + ".html").toString();
		System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");

		try {
			os = new FileOutputStream(htmlOutput);
		} catch (FileNotFoundException e) {
			log.severe("Ocorreu o seguinte erro -> " + e.getMessage());
			close();
			throw e;
		}
		try {
			xml = new FileInputStream(xmlInput);
		} catch (FileNotFoundException e) {
			log.severe("Ocorreu o seguinte erro -> " + e.getMessage());
			close();
			throw e;
		}
		try {
			xsl = new FileInputStream(xslInput);
		} catch (FileNotFoundException e) {
			log.severe("Ocorreu o seguinte erro -> " + e.getMessage());
			close();
			throw e;
		}
		TransformerFactory tFactory = TransformerFactory.newInstance();
		StreamSource styleSource = new StreamSource(xsl);
		try {
			transformer = tFactory.newTransformer(styleSource);
		} catch (TransformerConfigurationException e) {
			log.severe("Ocorreu o seguinte erro -> " + e.getMessage());
			close();
			throw e;
		}
		StreamSource xmlSource = new StreamSource(xml);
		StreamResult result = new StreamResult(os);
		try {
			transformer.transform(xmlSource, result);
		} catch (TransformerException e) {
			log.severe("Ocorreu o seguinte erro -> " + e.getMessage());
			close();
			throw e;
		}
	}

	private static void close () throws Exception  {
		try {
			os.close();
			xml.close();
			xsl.close();
		} catch (Exception e) {
			log.severe("Ocorreu o seguinte erro -> " + e.getMessage());
			throw e;
		}
	}
	
	private static String agora() {
		Calendar agora = new GregorianCalendar(); 
		String timestamp = String.valueOf(agora.get(Calendar.YEAR)) + "_" +
				String.valueOf((agora.get(Calendar.MONTH) + 1)) + "_" +
				String.valueOf(agora.get(Calendar.DAY_OF_MONTH)) + "_" +
				String.valueOf(agora.get(Calendar.HOUR_OF_DAY))+ "_" +
				String.valueOf(agora.get(Calendar.MINUTE)) + "_" +
				String.valueOf(agora.get(Calendar.SECOND)) + "_" +
				String.valueOf(agora.get(Calendar.MILLISECOND));
		return timestamp;
	}
}

package marshall;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import classes.Noticia;
import classes.Noticias;

public class JAXBHandler {

	private static final Logger log = Logger.getLogger(JAXBHandler.class.getName());
	
	public static void marshal (Noticias news, File selectedFile) {
		JAXBContext context;
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(selectedFile));
			context = JAXBContext.newInstance(Noticias.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "US-ASCII");
			//m.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml-stylesheet type=\"text/xsl\" href=\"noticia.xsl\"?>\n\n");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(news, writer);
			writer.close();
			log.info("O marshal do objecto " + news.toString() + " para o ficheiro " + selectedFile + " foi realizado com sucesso.");
		} catch (Exception e) {
			log.severe("O marshal do objecto " + news.toString() + " para o ficheiro " + selectedFile + " falhou -> " + e.getMessage());
		}
		
	}

	public static List<Noticia> unmarshal(File importFile) throws JAXBException {
		Noticias news = new Noticias();
		JAXBContext context = JAXBContext.newInstance(Noticias.class);
		Unmarshaller um = context.createUnmarshaller();
		news = (Noticias) um.unmarshal(importFile);
		return news.getNoticia();
	}

}

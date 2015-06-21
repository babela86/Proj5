package jms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Receiver implements MessageListener {

	private static final Logger log = Logger.getLogger(Receiver.class.getName());

	private TopicConnectionFactory cf;
	private Topic topic;

	public Receiver () {
		try {
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			topic = InitialContext.doLookup("jms/topic/MyCNN");
			log.info("Conexao ao topico realizada com sucesso.");
		} catch (NamingException e) {
			log.severe("A conexao ao topico falhou -> " + e.getMessage());
			return;
		}
	}

	public static void main(String[] args) {
		Receiver rec = new Receiver();
		rec.initialize();
	}

	public void onMessage(Message msg) {
		String msgToXML = null;
		Transformer transformer = null;
		try {
			msgToXML = ((TextMessage)msg).getText();
		} catch (JMSException e1) {
			log.severe("A conversao para XML falhou -> " + e1.getMessage());
			return;
		}
		Document file = null;
		try {
			file = StringToXML.stringToXML(msgToXML);
		} catch (ParserConfigurationException | SAXException | IOException e2) {
			log.severe("A conversao de String para XML falhou -> " + e2.getMessage());
			return;
		}
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException	| TransformerFactoryConfigurationError e1) {
			log.severe("Ocorreu o erro -> " + e1.getMessage());
			return;
		}
		File ficheiro = new File(Paths.get("src/main/resources/noticiasoutput.xml").toString());
		Result output = new StreamResult(ficheiro);
		try {
			XMLvalidation.validateXMLSchema(ficheiro.getPath());
		} catch (Exception e) {
			log.severe("A validacao do XML falhou - > " + e.getMessage());
			return;
		}
		try {
			Converter.convertXMLtoHTML();
		} catch (Exception e) {
			log.severe("A conversao de XML para HTML falhou - > " + e.getMessage());
			return;
		}
		Source input = new DOMSource(file);
		try {
			transformer.transform(input, output);
		} catch (TransformerException e) {
			log.severe("A conversao para HTML falhou -> " + e.getMessage());
			return;
		}
		log.info("Foi recebida uma mensagem com sucesso.");
		System.out.println("A recepcao de mensagens esta activa");
		System.out.println("#### Pressione enter para sair ####");
	}

	public void initialize() {
		JMSContext jmsC = cf.createContext("user", "123");
		try {
			jmsC.setClientID("userHTML");
			JMSConsumer consumer = jmsC.createDurableConsumer(topic,"userHTML");
			consumer.setMessageListener(this);
			System.out.println("A recepcao de mensagens esta activa");
			System.out.println("#### Pressione enter para sair ####");
			System.in.read();
			jmsC.close();
		} catch (Exception e) {
			log.severe("Ocorreu um erro na recepcao da mensagem -> " + e.getMessage());
			jmsC.close();
			return;
		}
	}
}



package jms;

import java.io.File;
import java.util.logging.Logger;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class Receiver implements MessageListener {

	private static final Logger log = Logger.getLogger(Receiver.class.getName());

	private TopicConnectionFactory cf;
	private Topic topic;

	public Receiver () {
		try {
			cf = InitialContext.doLookup("jms/RemoteConnectionFactory");
			topic = InitialContext.doLookup("jms/topic/MyCNN");
			log.info("Conexão ao tópico realizada com sucesso.");
		} catch (NamingException e) {
			log.severe("A conexão ao tópico falhou -> " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		Receiver rec = new Receiver();
		rec.initialize();
	}

	public void onMessage(Message msg) {
		try {
			String msgToXML = ((TextMessage)msg).getText();
			Document file = StringToXML.stringToXML(msgToXML);
			Transformer transformer  = TransformerFactory.newInstance().newTransformer();
			File ficheiro = new File("..//src//main//resources//noticiasoutput.xml");
			Result output = new StreamResult(ficheiro);
			try {
				XMLvalidation.validateXMLSchema(ficheiro.getPath());
			} catch (Exception e) {
				log.severe("A validação do XML falhou - > " + e.getMessage());
			}
			Converter.convertXMLtoHTML();
			Source input = new DOMSource(file);
			transformer.transform(input, output);
			log.info("Foi recebida uma mensagem com sucesso.");
			System.out.println("A recepção de mensagens está activa");
			System.out.println("#### Pressione enter para sair ####");
		} catch (Exception e) {
			log.severe("A recepção da mensagem falhou -> " + e.getMessage());
		}
	}

	public void initialize() {
		try {
			JMSContext jmsC = cf.createContext("user", "123");
			jmsC.setClientID("userHTML");
			JMSConsumer consumer = jmsC.createDurableConsumer(topic,"userHTML");
			consumer.setMessageListener(this);
			System.in.read();
			jmsC.close();
		} catch (Exception e) {
			log.severe("Ocorreu um erro na recepção da mensagem -> " + e.getMessage());
		}
	}
}



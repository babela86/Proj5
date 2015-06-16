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
			System.out.println("Subscriber is now connected to topic"); //APAGAR
			log.info("Conex�o ao t�pico realizada com sucesso.");
		} catch (NamingException e) {
			log.severe("A conex�o ao t�pico falhou -> " + e.getMessage());
			System.out.println("Unable to connect to topic."); //APAGAR
		}
	}

	public static void main(String[] args) {
		Receiver rec = new Receiver();
		rec.initialize();
	}

	public void onMessage(Message msg) {
		try {
			System.out.println("Generating XML.."); //APAGAR
			String msgToXML = ((TextMessage)msg).getText();
			Document file = StringToXML.stringToXML(msgToXML);
			Transformer transformer  = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(new File("src//main//resources//noticiasoutput.xml"));
			Converter.convertXMLtoHTML();
			Source input = new DOMSource(file);
			transformer.transform(input, output);
			log.info("Foi recebida uma mensagem com sucesso.");
			System.out.println("A recep��o de mensagens est� activa");
			System.out.println("### Pressione enter para sair ###");
			Thread.sleep(1000);
		} catch (Exception e) {
			log.severe("A recep��o da mensagem falhou -> " + e.getMessage());
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
			log.severe("Ocorreu um erro na recep��o da mensagem -> " + e.getMessage());
		}
	}
}



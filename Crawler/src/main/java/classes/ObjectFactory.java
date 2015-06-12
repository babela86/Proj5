//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.11 at 05:22:05 PM BST 
//


package classes;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the classes package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Corpo_QNAME = new QName("", "corpo");
    private final static QName _FotoUrl_QNAME = new QName("", "foto_url");
    private final static QName _Highlights_QNAME = new QName("", "highlights");
    private final static QName _VideoUrl_QNAME = new QName("", "video_url");
    private final static QName _Data_QNAME = new QName("", "data");
    private final static QName _Titulo_QNAME = new QName("", "titulo");
    private final static QName _Url_QNAME = new QName("", "url");
    private final static QName _Autor_QNAME = new QName("", "autor");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: classes
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Noticias }
     * 
     */
    public Noticias createNoticias() {
        return new Noticias();
    }

    /**
     * Create an instance of {@link Noticia }
     * 
     */
    public Noticia createNoticia() {
        return new Noticia();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "corpo")
    public JAXBElement<String> createCorpo(String value) {
        return new JAXBElement<String>(_Corpo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "foto_url")
    public JAXBElement<String> createFotoUrl(String value) {
        return new JAXBElement<String>(_FotoUrl_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "highlights")
    public JAXBElement<String> createHighlights(String value) {
        return new JAXBElement<String>(_Highlights_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "video_url")
    public JAXBElement<String> createVideoUrl(String value) {
        return new JAXBElement<String>(_VideoUrl_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "data")
    public JAXBElement<XMLGregorianCalendar> createData(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_Data_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "titulo")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createTitulo(String value) {
        return new JAXBElement<String>(_Titulo_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "url")
    public JAXBElement<String> createUrl(String value) {
        return new JAXBElement<String>(_Url_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "autor")
    public JAXBElement<String> createAutor(String value) {
        return new JAXBElement<String>(_Autor_QNAME, String.class, null, value);
    }

}

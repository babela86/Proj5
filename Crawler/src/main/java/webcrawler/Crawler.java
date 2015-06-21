package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import marshall.JAXBHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import classes.Noticia;
import classes.Noticias;

public class Crawler {

	private static final Logger log = Logger.getLogger(Crawler.class.getName());

	private Document paginaPrincipal = null;
	private Document paginaInterior = null;
	private Element link = null;

	private Noticias noticias = new Noticias();
	private Noticia noticia = null;

	private final String urlGeral = "http://edition.cnn.com";
	private final String ficheiroOutput = "noticiascrawler.xml";

	private String urlPagina = null;
	private String categoria = null;
	private String data = null;
	private String autor = null;
	private String titulo = null;
	private String descricao = null;
	private String corpo = null;
	private String imagem = null;
	private String video = null;

	private ArrayList<String> urlsAlvo = new ArrayList<String>();
	private String url = "";

	public String executaCrawler() throws Exception {
		boolean paginasOk = false;
		boolean marshalOk=false;
		
		if (descobreLinks()) {
			paginasOk = pesquisaPaginas();
		} else {
			return null;
		}
		if (paginasOk) {
			marshalOk = marshallNoticia();
		} else {
			return null;
		}
		if (marshalOk==false) {
			return null;
		}
		return ficheiroOutput;
	}

	private boolean descobreLinks()  {
		try {
			paginaPrincipal = Jsoup.connect(urlGeral).get();
			log.info("Conexão à página " + urlGeral +  " realizada com sucesso");
			Elements urlsElement = paginaPrincipal.getElementsByClass("cd__headline");
			for (Element urlElement : urlsElement) {
				link = urlElement.getElementsByTag("a").first();
				if ((link.attr("href").contains("/asia/")
						|| link.attr("href").contains("/us/")
						|| link.attr("href").contains("/china/")
						|| link.attr("href").contains("/world/")
						|| link.attr("href").contains("/europe/")
						|| link.attr("href").contains("/middleeast/")
						|| link.attr("href").contains("/africa/") 
						|| link.attr("href").contains("/americas/"))
						&& !link.attr("href").contains("/videos/")
						&& !link.attr("href").contains("/gallery/")) {
					url = link.attr("href");
					if (!urlsAlvo.contains(url)) {
						urlsAlvo.add(url);
					}
				}
			}
			return true;
		} catch (Exception e) {
			log.severe("A conexao para a pagina falhou " + urlGeral + " -> " + e.getMessage());
			return false;
		}
	}

	private boolean pesquisaPaginas() {
		boolean ok = false;
		for (String urlAlvo : urlsAlvo) {
			try {
				paginaInterior = Jsoup.connect(urlGeral + urlAlvo).get();
				log.info("Conexao para pagina " + urlGeral + urlAlvo + " realizada com sucesso");
			} catch (IOException e) {
				log.severe("A conexao para a pagina falhou " + urlGeral + urlAlvo + " -> " + e.getMessage());
			}
			urlPagina = urlGeral + urlAlvo;
			corpo = paginaInterior.getElementsByClass("zn-body__paragraph").text();
			for (Element meta : paginaInterior.select("meta")) {
				String key = meta.attr("itemprop");
				String value = meta.attr("content");
				noticia = new Noticia();
				switch (key) {
				case "articleSection":
					categoria = value;
					break;
				case "dateModified":
					data = value;
					break;
				case "author":
					autor = value;
					break;
				case "headline":
					titulo = value;
					break;
				case "description":
					descricao = value;
					break;
				case "image":
					imagem = value;
					break;
				case "url":
					if (value.endsWith(".cnn")) {
						video = value;
					}
					break;
				}
				ok = true;
			}
			if (ok) {
				constroiNoticia();
			}
		}
		return ok;
	}

	private void constroiNoticia() {
		noticia.setCategoria(categoria);
		noticia.setDescricao(descricao);
		noticia.setUrlPagina(urlPagina);
		noticia.setTitulo(titulo);
		noticia.setData(formataData(data));
		noticia.setAutor(autor);
		noticia.setCorpo(corpo);
		noticia.setImagem(imagem);
		noticia.setVideo(video);
		noticias.getNoticia().add(noticia);
	}

	private String formataData (String dataHora) {
		String dataHoraT = dataHora.substring(0,10) + " " + dataHora.substring(11,19);
		return dataHoraT;
	}

	private boolean marshallNoticia()  {
		new JAXBHandler();
		try {
			JAXBHandler.marshal(noticias, new File(ficheiroOutput));
			return true;
		} catch (Exception e) {
			e.getMessage();
			return false;
		}
	}
}
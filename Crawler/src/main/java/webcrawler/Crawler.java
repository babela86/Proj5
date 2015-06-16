package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import marshall.JAXBHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import classes.Noticia;
import classes.Noticias;

public class Crawler {

	private int cont1 = 0, cont2 = 0;// RETIRAR

	private Document paginaPrincipal = null;
	private Document paginaInterior = null;
	private Element link = null;

	private Noticias noticias = new Noticias();
	private Noticia noticia = null;

	private final String urlGeral = "http://edition.cnn.com";
	private final String ficheiroOutput = "src//main//resources//noticiascrawler.xml";

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

	public String executaCrawler() {
		descobreLinks();
		pesquisaPaginas();
		marshallNoticia();
		return ficheiroOutput;
	}

	private void descobreLinks() {
		try {
			paginaPrincipal = Jsoup.connect(urlGeral).get();
		} catch (IOException e) {
			e.getMessage();
		}
		Elements urlsElement = paginaPrincipal
				.getElementsByClass("cd__headline");
		for (Element urlElement : urlsElement) {
			link = urlElement.getElementsByTag("a").first();
			if ((link.attr("href").contains("/asia/")
					|| link.attr("href").contains("/us/")
					|| link.attr("href").contains("/china/")
					|| link.attr("href").contains("/world/")// RETIRAR ??
					|| link.attr("href").contains("/europe/")
					|| link.attr("href").contains("/middleeast/")
					|| link.attr("href").contains("/africa/") || link.attr(
							"href").contains("/americas/"))
							&& !link.attr("href").contains("/videos/")
							&& !link.attr("href").contains("/gallery/")) {
				url = link.attr("href");
				if (!urlsAlvo.contains(url)) {
					urlsAlvo.add(url);

					cont1++; // RETIRAR

				}
			}
		}
	}

	private void pesquisaPaginas() {
		for (String urlAlvo : urlsAlvo) {
			try {
				paginaInterior = Jsoup.connect(urlGeral + urlAlvo).get();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			urlPagina = urlGeral + urlAlvo;
			corpo = paginaInterior.getElementsByClass("zn-body__paragraph")
					.text();
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
			}

			constroiNoticia();
		}
	}

	private void constroiNoticia() {
		noticia.setCategoria(categoria);
		noticia.setDescricao(descricao);
		noticia.setUrlPagina(urlPagina);
		noticia.setTitulo(titulo);
		noticia.setData(data);
		noticia.setAutor(autor);
		noticia.setCorpo(corpo);
		noticia.setImagem(imagem);
		noticia.setVideo(video);// ÀS VEZES FALHA
		noticias.getNoticia().add(noticia);
	}

	private void marshallNoticia() {
		System.out.println("Total links -> " + cont1);// RETIRAR
		System.out.println("Total noticias -> " + cont2);// RETIRAR
		new JAXBHandler();
		JAXBHandler.marshal(noticias, new File(ficheiroOutput));
	}

}
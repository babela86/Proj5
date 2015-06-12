package webcrawler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import marshall.JAXBHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import classes.Noticia;
import classes.Noticias;
public class Crawler {

	public static void main(String[] args) {

		int cont1=0,cont2=0;

		Document paginaPrincipal = null;	
		Document paginaInterior = null;
		Element link = null;

		Noticias noticias = null;
		Noticia noticia = null;

		final String urlGeral = "http://edition.cnn.com";

		String urlPagina = null;
		String regiao = null;
		String data = null;
		String autor = null;
		String cabecalho = null;
		String descricao = null;
		String corpo = null;
		String imagem = null;
		String video = null;

		ArrayList<String> urlsAlvo = new ArrayList<String>();
		String url = "";

		//descobre LINKS
		try {
			paginaPrincipal = Jsoup.connect(urlGeral).get();
		} catch (IOException e) {
			e.getMessage();
		}
		Elements urlsElement = paginaPrincipal.getElementsByClass("cd__headline");
		for (Element urlElement : urlsElement) {
			link = urlElement.getElementsByTag("a").first();
			if ((link.attr("href").contains("/asia/")
					|| link.attr("href").contains("/us/")
					|| link.attr("href").contains("/china/")
					|| link.attr("href").contains("/europe/")
					|| link.attr("href").contains("/middleeast/")
					|| link.attr("href").contains("/africa/") || link.attr(
							"href").contains("/americas/"))
							&& !link.attr("href").contains("/videos/")) {
				url = link.attr("href");
				if (!urlsAlvo.contains(url)) {
					urlsAlvo.add(url);
					System.out.println(url);
					cont1++;
				}
			}
		}

		//percorre LINKS
		for (String urlAlvo : urlsAlvo) {
			try {
				paginaInterior = Jsoup.connect(urlGeral + urlAlvo).get();
			} catch (IOException e) {
				e.getMessage();
			}
			noticias = new Noticias();
			urlPagina = urlGeral + urlAlvo;
			for(Element meta : paginaInterior.select("meta")) {
				String key = meta.attr("itemprop");
				String value = meta.attr("content");
				noticia = new Noticia();
				switch (key) {
				case "articleSection":
					regiao = value;
					break;	
				case "dateModified":
					data= value;
					break;	
				case "author":
					autor = value;
					break;	
				case "headline":
					cabecalho = value;
					break;
				case "description":
					descricao = value;
					break;	
				case "image":
					imagem = value;
					break;	
				case "url":
					video = value;
					break;	
				}
			}
			noticia.setUrl(urlPagina);
			noticia.setTitulo(cabecalho);
			noticia.setData(data);
			noticia.setAutor(autor);
			noticia.setCorpo(paginaInterior.getElementsByClass("zn-body__paragraph").text());
			cont2++;
			noticias.getNoticia().add(noticia);
			for (Noticia n:noticias.getNoticia()) {
				System.out.println(n.toString());
			}
		}
		System.out.println("Total links -> " + cont1);
		System.out.println("Total noticias -> " + cont2);
		new JAXBHandler();
		try {
			System.out.println(noticias.getNoticia().size());
			JAXBHandler.marshal(new Noticias().getNoticia(), new File ("C:\\textexml\\teste.xml"));
			System.out.println("Marshall ok");
		} catch (IOException | JAXBException e) {
			e.printStackTrace();
		}	
	}
}
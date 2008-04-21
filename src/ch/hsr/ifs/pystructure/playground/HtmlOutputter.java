package ch.hsr.ifs.pystructure.playground;

import org.jdom.Content;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

import ch.hsr.ifs.pystructure.utils.StringUtils;

public abstract class HtmlOutputter {
	
	protected HtmlOutputter() {
	}
	
	private static final Namespace NAMESPACE = Namespace.getNamespace("http://www.w3.org/1999/xhtml");

	protected static void th(Element tr, String content) {
		tr.addContent(tag("th", content));
	}

	protected static Document createDocument(int i, String title) {
		Element html = new Element("html", NAMESPACE);

		Document doc = new Document(html);
		doc.setDocType(new DocType("html", "-//W3C//DTD XHTML 1.0 Strict//EN", ""));

		String style = StringUtils.multiply(i, "../") + "style.css";
		Element head = emptyTag("head");
		html.addContent(head);

		head.addContent(emptyTag("link", "rel", "stylesheet", "type", "text/css", "href", style));
		head.addContent(tag("title", title));

		return doc;
	}

	protected static void td(Element tr, String content, String klass) {
		tr.addContent(tag("td", content, "class", klass)); 
	}

	protected static Element td(String content) {
		return tag("td", content); 
	}

	protected static void td(Element parent, Content content) {
		parent.addContent(tag("td", content));
	}

	protected static void td(Element parent, String content) {
		td(parent, new Text(content));
	}

	protected static Element link(String label, String url) {
		return tag("a", label, "href", url);
	}

	protected static void printDetail(Element details, String key, String value) {
		printDetail(details, key, new Text(value));
	}

	protected static void printDetail(Element details, String key, Content value) {
		details.addContent(tag("b", key + ":"));
		details.addContent(value);
		details.addContent(emptyTag("br"));
	}

	protected static Element tag(String name, Content content, String... attributes) {
		Element e = emptyTag(name, attributes);

		e.addContent(content);

		return e;
	}

	protected static Element tag(String name, String content, String... attributes) {
		return tag(name, new Text(content), attributes);
	}

	protected static Element emptyTag(String name, String... attributes) {
		Element e = new Element(name, NAMESPACE);

		if (attributes.length % 2 != 0) {
			throw new RuntimeException("The attributes parameter has to have a even length (key => value paris)");
		}

		for (int i = 0; i < attributes.length; i += 2) {
			String key = attributes[i];
			String value = attributes[i + 1];

			e.setAttribute(key, value);
		}
		return e;
	}

}

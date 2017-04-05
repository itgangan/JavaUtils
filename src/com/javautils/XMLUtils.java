package com.javautils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLUtils {
	public static Document loadXML(final String path) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(path));
		return doc;
	}

	public static Document loadXMl(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(file);
		return doc;
	}

	public static Document loadXMl(InputStream is) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(is);
		return doc;
	}

	/**
	 * 将简单的xml文档（只有一级子元素的xml文档）转成map
	 * 
	 * @param doc
	 * @return
	 */
	public static Map<String, Object> parseMap(Document doc) {
		Map<String, Object> map = new HashMap<String, Object>();
		Element root = doc.getRootElement();
		Iterator<?> iter = root.elementIterator();
		while (iter.hasNext()) {
			Element e = (Element) iter.next();
			map.put(e.getName(), e.getText());
		}
		return map;
	}

}

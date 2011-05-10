package knox.ravi;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import android.os.Environment;

public class XMLHandler {

	public static Document getXMLFile(String filename) throws DocumentException{
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(filename));
		
		return doc;
	}
}

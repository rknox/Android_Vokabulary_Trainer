//package knox.ravi;
//
//import static knox.ravi.Constants.TAG;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.io.SAXReader;
//import org.xmlpull.v1.XmlSerializer;
//
//import android.util.Log;
//import android.util.Xml;
//
//public class XMLHandler_bak {
//
//	public static Document getXMLFile(String filename) throws DocumentException {
//		SAXReader reader = new SAXReader();
//		reader.setEncoding("UTF-8");
//		Document doc = reader.read(new File(filename));
//		return doc;
//	}
//
//	public void writeList(List<Vocable> vocables, String path) {
//		File newxmlfile = new File(path);
//		try {
//			newxmlfile.createNewFile();
//		} catch (IOException e) {
//			Log.e(TAG, "exception in createNewFile() method");
//		}
//		// we have to bind the new file with a FileOutputStream
//		FileOutputStream fileos = null;
//		try {
//			fileos = new FileOutputStream(newxmlfile);
//		} catch (FileNotFoundException e) {
//			new VocabularyTrainer();
//			Tools.showToast(VocabularyTrainer.getContext(),
//					"Could not write file");
//			Log.e("FileNotFoundException", "can't create FileOutputStream");
//		}
//		// we create a XmlSerializer in order to write xml data
//		XmlSerializer serializer = Xml.newSerializer();
//		try {
//			// we set the FileOutputStream as output for the serializer, using
//			// UTF-8 encoding
//			serializer.setOutput(fileos, "UTF-8");
//			// Write <?xml declaration with encoding (if encoding not null) and
//			// standalone flag (if standalone not null)
//			serializer.startDocument(null, Boolean.valueOf(true));
//			// set indentation option
//			serializer.setFeature(
//					"http://xmlpull.org/v1/doc/features.html#indent-output",
//					true);
//			// start a tag called "root"
//			serializer.startTag(null, "vocables");
//			// i indent code just to have a view similar to xml-tree
//			for (Vocable vocable : vocables) {
//				serializer.startTag(null, "vocable");
//				serializer.startTag(null, "german");
//				serializer.text(vocable.getGerman());
//				serializer.endTag(null, "german");
//				serializer.startTag(null, "english");
//				serializer.text(vocable.getEnglish());
//				serializer.endTag(null, "english");
//				serializer.endTag(null, "vocable");
//			}
//			serializer.endTag(null, "vocables");
//			serializer.endDocument();
//			// write xml data into the FileOutputStream
//			serializer.flush();
//			// finally we close the file stream
//			fileos.close();
//
//			Tools.showToast(new VocabularyTrainer().getContext(),
//					"File has been written to /sdcard/dropbox/vocables_bak.xml");
//		} catch (Exception e) {
//			Log.e("Exception", "error occurred while creating xml file");
//		}
//
//	}
//}

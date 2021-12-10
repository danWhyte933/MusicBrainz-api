import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class APItests {

    @Test
    void test1() {

        Response responder = RestAssured.get("http://musicbrainz.org/ws/2/release/?query=Metallica&limit=100&fmt&inc=work=xml");


        System.out.println("Status code : " + responder.getStatusCode());
        System.out.println("Body : " + responder.getBody());
        System.out.println("Time taken : " + responder.getTime());
        System.out.println("Header : " + responder.getHeader("content-type"));

        String xmlResponse = responder.asString();
        Document doc = convertStringToXMLDocument(xmlResponse);
        assert doc != null;
        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("name");
        int theLength = nList.getLength();
        System.out.println("The length is " + theLength);


        ArrayList<String> songs = new ArrayList();
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                String song = nNode.getTextContent();
                songs.add(song);
            }
        }

        System.out.println("Arraylist " + songs);
        songs = new ArrayList<String>(new LinkedHashSet<String>(songs));
        System.out.println("Without duplicates " + songs);



        Response responderTwo = RestAssured.get("https://private-anon-1f438acdaa-lyricsovh.apiary-proxy.com/v1/Nirvana/Smells%20like%20teen%20spirit");
        String lyricResponse = responderTwo.asString();
        System.out.println("Lyric Status code : " + responderTwo.getStatusCode());
        System.out.println("Lyric Body : " + responderTwo.getBody());
        System.out.println("Lyric Time taken : " + responderTwo.getTime());
        System.out.println("Lyric Header : " + responderTwo.getHeader("content-type"));
        System.out.println(lyricResponse); // no lyrics found, gets 404 error

    }
    private String getDocumentElementText(Document doc, String elementName) {

        return doc.getElementsByTagName(elementName).item(0).getTextContent();
    }

    private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}



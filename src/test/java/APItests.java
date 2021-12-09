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

public class APItests {

    @Test
    void test1() {

        Response responder = RestAssured.get("http://musicbrainz.org/ws/2/release/?query=radiohead&limit=100&fmt&inc=work=xml");


        System.out.println("Status code : " + responder.getStatusCode());
        System.out.println("Body : " + responder.getBody());
        System.out.println("Time taken : " + responder.getTime());
        System.out.println("Header : " + responder.getHeader("content-type"));

        String xmlResponse = responder.asString();
        Document doc = convertStringToXMLDocument(xmlResponse);


        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("name");
        int theLength = nList.getLength();
        System.out.println("The length is " + theLength);

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            System.out.println("\nCurrent Element: " + nNode.getNodeName());
            //this prints name for every time it is a tag
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println("Current Value: " + nNode.getTextContent());
            }
        }
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



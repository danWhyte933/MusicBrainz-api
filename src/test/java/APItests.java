import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class APItests {

    @Test
    void test1() throws UnirestException {

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

        String artistEntry = "Amy Winehouse";
        String songEntry = "Back to black";
        String spacedArtist = artistEntry.replaceAll(" ", "%20");
        String spacedSong = songEntry.replaceAll(" ", "%20");
        String lyricRequest = "https://private-anon-1f438acdaa-lyricsovh.apiary-proxy.com/v1/" + spacedArtist + "/" + spacedSong;
        System.out.println("lyric request " + lyricRequest);
        // adds and relevant % sign to artists and songs

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get(lyricRequest)
                .asString();

        String firstResponse = response.getBody();
        System.out.println("first response body " + firstResponse);
        String secondResponse = firstResponse.replaceAll("\n",", ");
        String thirdResponse = StringUtils.replaceChars(firstResponse, '\n', ' ');
        System.out.println("second response body " + secondResponse);
        System.out.println("third response body " + thirdResponse);
        // need to replace \n with a space, replaceAll and replace do not work, neither does REGEX
        String[] words = firstResponse.split("\\s+");
        System.out.println("Number of words " + words.length);
        // successfully counts the words but with \n not separating some

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



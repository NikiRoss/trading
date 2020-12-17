package com.fdm.trading.utils.json;

import org.w3c.dom.Document;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class XmlWriter implements DataWriter<Document> {
    @Override
    public void writeData(Document data, String target) {
        try {
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = null;

            aTransformer = tranFactory.newTransformer();


            // format the XML nicely
            aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            aTransformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(data);

            // location and name of XML file you can change as per need
            FileWriter fos = new FileWriter(target);
            StreamResult result = new StreamResult(fos);
            aTransformer.transform(source, result);

        } catch (IOException | TransformerConfigurationException e) {

            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
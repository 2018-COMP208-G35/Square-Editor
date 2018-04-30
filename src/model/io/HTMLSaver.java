package model.io;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

public class HTMLSaver {
    private static HTMLSaver htmlSaver = new HTMLSaver();


    private HTMLSaver()
    {

    }

    public static HTMLSaver getInstance()
    {
        return htmlSaver;
    }

    public void saveToHTML(String xml, File file) throws TransformerException
    {
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer(new StreamSource(getClass().getResource("/xsl/HTML.xsl").toExternalForm()));
        StreamSource ss = new StreamSource(new StringReader(xml));
        StreamResult sr = new StreamResult(file);
        tf.transform(ss, sr);
        System.out.println("Done: XML -> HTML in File");
    }

    public String transformToHTML(String xml) throws TransformerException
    {
        String html;
        StringWriter writer = new StringWriter();
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer(new StreamSource(getClass().getResource("/xsl/HTML.xsl").toExternalForm()));
        StreamSource ss = new StreamSource(new StringReader(xml));
        StreamResult sr = new StreamResult(writer);
        tf.transform(ss, sr);
        html = writer.toString();
        System.out.println("Done: XML -> HTML in String");
        return html;
    }
}

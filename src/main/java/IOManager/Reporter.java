package IOManager;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reporter {
    private static final Path surefirePath = FileSystems.getDefault().getPath("target"+File.separator+"surefire-reports");
    
    private Document doc;
    private Element testsuite;
    private Element testcase;

    public Element getTestcase() {
        return testcase;
    }
    public void setTestcase(Element testcase_in) {
        testcase = testcase_in;
    }
    public Document getDoc() {
        return doc;
    }
    public void setDoc(Document doc_in) {
        doc = doc_in;
    }
    public Element getTestsuite() {
        return testsuite;
    }
    public void setTestsuite(Element testsuite_in) {
        testsuite = testsuite_in;
    }

    public Reporter() throws Exception {
        doc = CreateXML();
        testsuite = CreateTestsuite(doc);
        testcase = null;
    }

    public Document CreateXML() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        doc = builder.newDocument();
        return doc;
    }

    public Element CreateTestsuite(Document doc) {
        Element elementTestSuites = doc.createElement("testsuites");
        doc.appendChild(elementTestSuites);
        Comment comment = doc.createComment("Comment");
        doc.insertBefore(comment, elementTestSuites);
        testsuite = doc.createElement("testsuite");
        elementTestSuites.appendChild(testsuite);
        testsuite.setAttribute("timestamp", new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
        return testsuite;
    }

    public Element addTC(String strName, String strClassName ) {
        Element tcElement = doc.createElement("testcase");
        testsuite.appendChild(tcElement);
        tcElement.setAttribute("name", strName);
        tcElement.setAttribute("classname", strClassName);
        testcase = tcElement;
        return tcElement;
    }

    public void addErr(Element tc, String strErrMsg, String strErrType, String strErrText ) {
        Element errElement = doc.createElement("error");
        tc.appendChild(errElement);
        errElement.setAttribute("message", strErrMsg);
        errElement.setAttribute("type", strErrType);
        errElement.insertBefore(doc.createTextNode(strErrText), errElement.getLastChild());
    }

    public void addSystemOut(Element testcase, String strSystemOut) {
        Element systemOutElement = doc.createElement("system-out");
        testcase.appendChild(systemOutElement);
        systemOutElement.insertBefore(doc.createTextNode(strSystemOut), systemOutElement.getLastChild());
    }

    public void addComment(Element testcase, String strComment) {
        Element commentElement = doc.createElement("comment");
        testcase.appendChild(commentElement);
        commentElement.insertBefore(doc.createTextNode(strComment), commentElement.getLastChild());
    }

    public final void printWriter(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }

    public final void printFile(Document xml) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer tf = transformerFactory.newTransformer();
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DOMSource source = new DOMSource(xml);
        
        if(!Files.exists(surefirePath))
        {
            Files.createDirectories(surefirePath);   
        }
        StreamResult result = new StreamResult(new File(surefirePath.toFile(), "file.xml"));
        tf.transform(source, result);
    }

    public void main(String[] args) throws Exception {
        this.setDoc(CreateXML());
        this.setTestsuite(CreateTestsuite(doc));
        Element tc = addTC("BC1", "package1.test1");
        addErr(tc, "failmsg", "failtype", "failtext");
        tc = addTC("BC2", "package1.test1");
        addErr(tc, "errmsg", "errtype", "errtext");
        tc = addTC("BC2", "package1.test1");
        tc = addTC("BC3", "package1.test1");
        tc = addTC("BC1", "package1.test2");
        tc = addTC("BC2", "package1.test2");
        tc = addTC("BC1", "package2.test");
        tc = addTC("BC2", "package2.test");
        addErr(tc, "errmsg", "errtype", "errtext");
        printFile(doc);
        printWriter(doc);
    }
}

/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StringReader;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ class XMLUtils
/*     */ {
/*     */   private static final String PROPS_DTD_URI = "http://java.sun.com/dtd/properties.dtd";
/*     */   private static final String PROPS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for properties --><!ELEMENT properties ( comment?, entry* ) ><!ATTLIST properties version CDATA #FIXED \"1.0\"><!ELEMENT comment (#PCDATA) ><!ELEMENT entry (#PCDATA) ><!ATTLIST entry  key CDATA #REQUIRED>";
/*     */   private static final String EXTERNAL_XML_VERSION = "1.0";
/*     */ 
/*     */   static void load(Properties paramProperties, InputStream paramInputStream)
/*     */     throws IOException, InvalidPropertiesFormatException
/*     */   {
/*  72 */     Document localDocument = null;
/*     */     try {
/*  74 */       localDocument = getLoadingDoc(paramInputStream);
/*     */     } catch (SAXException localSAXException) {
/*  76 */       throw new InvalidPropertiesFormatException(localSAXException);
/*     */     }
/*  78 */     Element localElement = localDocument.getDocumentElement();
/*  79 */     String str = localElement.getAttribute("version");
/*  80 */     if (str.compareTo("1.0") > 0) {
/*  81 */       throw new InvalidPropertiesFormatException("Exported Properties file format version " + str + " is not supported. This java installation can read" + " versions " + "1.0" + " or older. You" + " may need to install a newer version of JDK.");
/*     */     }
/*     */ 
/*  86 */     importProperties(paramProperties, localElement);
/*     */   }
/*     */ 
/*     */   static Document getLoadingDoc(InputStream paramInputStream)
/*     */     throws SAXException, IOException
/*     */   {
/*  92 */     DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  93 */     localDocumentBuilderFactory.setIgnoringElementContentWhitespace(true);
/*  94 */     localDocumentBuilderFactory.setValidating(true);
/*  95 */     localDocumentBuilderFactory.setCoalescing(true);
/*  96 */     localDocumentBuilderFactory.setIgnoringComments(true);
/*     */     try {
/*  98 */       DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/*  99 */       localDocumentBuilder.setEntityResolver(new Resolver(null));
/* 100 */       localDocumentBuilder.setErrorHandler(new EH(null));
/* 101 */       InputSource localInputSource = new InputSource(paramInputStream);
/* 102 */       return localDocumentBuilder.parse(localInputSource);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 104 */       throw new Error(localParserConfigurationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static void importProperties(Properties paramProperties, Element paramElement) {
/* 109 */     NodeList localNodeList = paramElement.getChildNodes();
/* 110 */     int i = localNodeList.getLength();
/* 111 */     int j = (i > 0) && (localNodeList.item(0).getNodeName().equals("comment")) ? 1 : 0;
/*     */ 
/* 113 */     for (int k = j; k < i; k++) {
/* 114 */       Element localElement = (Element)localNodeList.item(k);
/* 115 */       if (localElement.hasAttribute("key")) {
/* 116 */         Node localNode = localElement.getFirstChild();
/* 117 */         String str = localNode == null ? "" : localNode.getNodeValue();
/* 118 */         paramProperties.setProperty(localElement.getAttribute("key"), str);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static void save(Properties paramProperties, OutputStream paramOutputStream, String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 127 */     DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 128 */     DocumentBuilder localDocumentBuilder = null;
/*     */     try {
/* 130 */       localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 132 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 134 */     Document localDocument = localDocumentBuilder.newDocument();
/* 135 */     Element localElement1 = (Element)localDocument.appendChild(localDocument.createElement("properties"));
/*     */ 
/* 138 */     if (paramString1 != null) {
/* 139 */       Element localElement2 = (Element)localElement1.appendChild(localDocument.createElement("comment"));
/*     */ 
/* 141 */       localElement2.appendChild(localDocument.createTextNode(paramString1));
/*     */     }
/*     */ 
/* 144 */     synchronized (paramProperties) {
/* 145 */       for (String str : paramProperties.stringPropertyNames()) {
/* 146 */         Element localElement3 = (Element)localElement1.appendChild(localDocument.createElement("entry"));
/*     */ 
/* 148 */         localElement3.setAttribute("key", str);
/* 149 */         localElement3.appendChild(localDocument.createTextNode(paramProperties.getProperty(str)));
/*     */       }
/*     */     }
/* 152 */     emitDocument(localDocument, paramOutputStream, paramString2);
/*     */   }
/*     */ 
/*     */   static void emitDocument(Document paramDocument, OutputStream paramOutputStream, String paramString)
/*     */     throws IOException
/*     */   {
/* 158 */     TransformerFactory localTransformerFactory = TransformerFactory.newInstance();
/* 159 */     Transformer localTransformer = null;
/*     */     try {
/* 161 */       localTransformer = localTransformerFactory.newTransformer();
/* 162 */       localTransformer.setOutputProperty("doctype-system", "http://java.sun.com/dtd/properties.dtd");
/* 163 */       localTransformer.setOutputProperty("indent", "yes");
/* 164 */       localTransformer.setOutputProperty("method", "xml");
/* 165 */       localTransformer.setOutputProperty("encoding", paramString);
/*     */     } catch (TransformerConfigurationException localTransformerConfigurationException) {
/* 167 */       if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/* 169 */     DOMSource localDOMSource = new DOMSource(paramDocument);
/* 170 */     StreamResult localStreamResult = new StreamResult(paramOutputStream);
/*     */     try {
/* 172 */       localTransformer.transform(localDOMSource, localStreamResult);
/*     */     } catch (TransformerException localTransformerException) {
/* 174 */       IOException localIOException = new IOException();
/* 175 */       localIOException.initCause(localTransformerException);
/* 176 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class EH
/*     */     implements ErrorHandler
/*     */   {
/*     */     public void error(SAXParseException paramSAXParseException)
/*     */       throws SAXException
/*     */     {
/* 196 */       throw paramSAXParseException;
/*     */     }
/*     */     public void fatalError(SAXParseException paramSAXParseException) throws SAXException {
/* 199 */       throw paramSAXParseException;
/*     */     }
/*     */     public void warning(SAXParseException paramSAXParseException) throws SAXException {
/* 202 */       throw paramSAXParseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Resolver
/*     */     implements EntityResolver
/*     */   {
/*     */     public InputSource resolveEntity(String paramString1, String paramString2)
/*     */       throws SAXException
/*     */     {
/* 184 */       if (paramString2.equals("http://java.sun.com/dtd/properties.dtd"))
/*     */       {
/* 186 */         InputSource localInputSource = new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for properties --><!ELEMENT properties ( comment?, entry* ) ><!ATTLIST properties version CDATA #FIXED \"1.0\"><!ELEMENT comment (#PCDATA) ><!ELEMENT entry (#PCDATA) ><!ATTLIST entry  key CDATA #REQUIRED>"));
/* 187 */         localInputSource.setSystemId("http://java.sun.com/dtd/properties.dtd");
/* 188 */         return localInputSource;
/*     */       }
/* 190 */       throw new SAXException("Invalid system identifier: " + paramString2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.XMLUtils
 * JD-Core Version:    0.6.2
 */
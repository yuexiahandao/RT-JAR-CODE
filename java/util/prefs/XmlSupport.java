/*     */ package java.util.prefs;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ class XmlSupport
/*     */ {
/*     */   private static final String PREFS_DTD_URI = "http://java.sun.com/dtd/preferences.dtd";
/*     */   private static final String PREFS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for preferences --><!ELEMENT preferences (root) ><!ATTLIST preferences EXTERNAL_XML_VERSION CDATA \"0.0\"  ><!ELEMENT root (map, node*) ><!ATTLIST root          type (system|user) #REQUIRED ><!ELEMENT node (map, node*) ><!ATTLIST node          name CDATA #REQUIRED ><!ELEMENT map (entry*) ><!ATTLIST map  MAP_XML_VERSION CDATA \"0.0\"  ><!ELEMENT entry EMPTY ><!ATTLIST entry          key CDATA #REQUIRED          value CDATA #REQUIRED >";
/*     */   private static final String EXTERNAL_XML_VERSION = "1.0";
/*     */   private static final String MAP_XML_VERSION = "1.0";
/*     */ 
/*     */   static void export(OutputStream paramOutputStream, Preferences paramPreferences, boolean paramBoolean)
/*     */     throws IOException, BackingStoreException
/*     */   {
/*  99 */     if (((AbstractPreferences)paramPreferences).isRemoved())
/* 100 */       throw new IllegalStateException("Node has been removed");
/* 101 */     Document localDocument = createPrefsDoc("preferences");
/* 102 */     Element localElement1 = localDocument.getDocumentElement();
/* 103 */     localElement1.setAttribute("EXTERNAL_XML_VERSION", "1.0");
/* 104 */     Element localElement2 = (Element)localElement1.appendChild(localDocument.createElement("root"));
/*     */ 
/* 106 */     localElement2.setAttribute("type", paramPreferences.isUserNode() ? "user" : "system");
/*     */ 
/* 109 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 111 */     Object localObject = paramPreferences; for (Preferences localPreferences = ((Preferences)localObject).parent(); localPreferences != null; 
/* 112 */       localPreferences = ((Preferences)localObject).parent()) {
/* 113 */       localArrayList.add(localObject);
/*     */ 
/* 112 */       localObject = localPreferences;
/*     */     }
/*     */ 
/* 115 */     localObject = localElement2;
/* 116 */     for (int i = localArrayList.size() - 1; i >= 0; i--) {
/* 117 */       ((Element)localObject).appendChild(localDocument.createElement("map"));
/* 118 */       localObject = (Element)((Element)localObject).appendChild(localDocument.createElement("node"));
/* 119 */       ((Element)localObject).setAttribute("name", ((Preferences)localArrayList.get(i)).name());
/*     */     }
/* 121 */     putPreferencesInXml((Element)localObject, localDocument, paramPreferences, paramBoolean);
/*     */ 
/* 123 */     writeDoc(localDocument, paramOutputStream);
/*     */   }
/*     */ 
/*     */   private static void putPreferencesInXml(Element paramElement, Document paramDocument, Preferences paramPreferences, boolean paramBoolean)
/*     */     throws BackingStoreException
/*     */   {
/* 141 */     Preferences[] arrayOfPreferences = null;
/* 142 */     String[] arrayOfString = null;
/*     */     Object localObject1;
/* 147 */     synchronized (((AbstractPreferences)paramPreferences).lock)
/*     */     {
/* 150 */       if (((AbstractPreferences)paramPreferences).isRemoved()) {
/* 151 */         paramElement.getParentNode().removeChild(paramElement);
/* 152 */         return;
/*     */       }
/*     */ 
/* 155 */       localObject1 = paramPreferences.keys();
/* 156 */       Element localElement1 = (Element)paramElement.appendChild(paramDocument.createElement("map"));
/* 157 */       for (int j = 0; j < localObject1.length; j++) {
/* 158 */         Element localElement2 = (Element)localElement1.appendChild(paramDocument.createElement("entry"));
/*     */ 
/* 160 */         localElement2.setAttribute("key", localObject1[j]);
/*     */ 
/* 162 */         localElement2.setAttribute("value", paramPreferences.get(localObject1[j], null));
/*     */       }
/*     */ 
/* 165 */       if (paramBoolean)
/*     */       {
/* 167 */         arrayOfString = paramPreferences.childrenNames();
/* 168 */         arrayOfPreferences = new Preferences[arrayOfString.length];
/* 169 */         for (j = 0; j < arrayOfString.length; j++) {
/* 170 */           arrayOfPreferences[j] = paramPreferences.node(arrayOfString[j]);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 175 */     if (paramBoolean)
/* 176 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 177 */         localObject1 = (Element)paramElement.appendChild(paramDocument.createElement("node"));
/*     */ 
/* 179 */         ((Element)localObject1).setAttribute("name", arrayOfString[i]);
/* 180 */         putPreferencesInXml((Element)localObject1, paramDocument, arrayOfPreferences[i], paramBoolean);
/*     */       }
/*     */   }
/*     */ 
/*     */   static void importPreferences(InputStream paramInputStream)
/*     */     throws IOException, InvalidPreferencesFormatException
/*     */   {
/*     */     try
/*     */     {
/* 199 */       Document localDocument = loadPrefsDoc(paramInputStream);
/* 200 */       String str = localDocument.getDocumentElement().getAttribute("EXTERNAL_XML_VERSION");
/*     */ 
/* 202 */       if (str.compareTo("1.0") > 0) {
/* 203 */         throw new InvalidPreferencesFormatException("Exported preferences file format version " + str + " is not supported. This java installation can read" + " versions " + "1.0" + " or older. You may need" + " to install a newer version of JDK.");
/*     */       }
/*     */ 
/* 209 */       Element localElement = (Element)localDocument.getDocumentElement().getChildNodes().item(0);
/*     */ 
/* 211 */       Preferences localPreferences = localElement.getAttribute("type").equals("user") ? Preferences.userRoot() : Preferences.systemRoot();
/*     */ 
/* 214 */       ImportSubtree(localPreferences, localElement);
/*     */     } catch (SAXException localSAXException) {
/* 216 */       throw new InvalidPreferencesFormatException(localSAXException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Document createPrefsDoc(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 225 */       DOMImplementation localDOMImplementation = DocumentBuilderFactory.newInstance().newDocumentBuilder().getDOMImplementation();
/*     */ 
/* 227 */       DocumentType localDocumentType = localDOMImplementation.createDocumentType(paramString, null, "http://java.sun.com/dtd/preferences.dtd");
/* 228 */       return localDOMImplementation.createDocument(null, paramString, localDocumentType);
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 230 */       throw new AssertionError(localParserConfigurationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Document loadPrefsDoc(InputStream paramInputStream)
/*     */     throws SAXException, IOException
/*     */   {
/* 241 */     DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 242 */     localDocumentBuilderFactory.setIgnoringElementContentWhitespace(true);
/* 243 */     localDocumentBuilderFactory.setValidating(true);
/* 244 */     localDocumentBuilderFactory.setCoalescing(true);
/* 245 */     localDocumentBuilderFactory.setIgnoringComments(true);
/*     */     try {
/* 247 */       DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/* 248 */       localDocumentBuilder.setEntityResolver(new Resolver(null));
/* 249 */       localDocumentBuilder.setErrorHandler(new EH(null));
/* 250 */       return localDocumentBuilder.parse(new InputSource(paramInputStream));
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 252 */       throw new AssertionError(localParserConfigurationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final void writeDoc(Document paramDocument, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 263 */       TransformerFactory localTransformerFactory = TransformerFactory.newInstance();
/*     */       try {
/* 265 */         localTransformerFactory.setAttribute("indent-number", new Integer(2));
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/* 270 */       Transformer localTransformer = localTransformerFactory.newTransformer();
/* 271 */       localTransformer.setOutputProperty("doctype-system", paramDocument.getDoctype().getSystemId());
/* 272 */       localTransformer.setOutputProperty("indent", "yes");
/*     */ 
/* 276 */       localTransformer.transform(new DOMSource(paramDocument), new StreamResult(new BufferedWriter(new OutputStreamWriter(paramOutputStream, "UTF-8"))));
/*     */     }
/*     */     catch (TransformerException localTransformerException) {
/* 279 */       throw new AssertionError(localTransformerException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void ImportSubtree(Preferences paramPreferences, Element paramElement)
/*     */   {
/* 289 */     NodeList localNodeList = paramElement.getChildNodes();
/* 290 */     Object localObject1 = localNodeList.getLength();
/*     */     Preferences[] arrayOfPreferences;
/* 299 */     synchronized (((AbstractPreferences)paramPreferences).lock)
/*     */     {
/* 301 */       if (((AbstractPreferences)paramPreferences).isRemoved()) {
/* 302 */         return;
/*     */       }
/*     */ 
/* 305 */       Element localElement1 = (Element)localNodeList.item(0);
/* 306 */       ImportPrefs(paramPreferences, localElement1);
/* 307 */       arrayOfPreferences = new Preferences[localObject1 - 1];
/*     */ 
/* 310 */       for (Object localObject2 = 1; localObject2 < localObject1; localObject2++) {
/* 311 */         Element localElement2 = (Element)localNodeList.item(localObject2);
/* 312 */         arrayOfPreferences[(localObject2 - 1)] = paramPreferences.node(localElement2.getAttribute("name"));
/*     */       }
/*     */     }
/*     */ 
/* 316 */     for (??? = 1; ??? < localObject1; ???++)
/* 317 */       ImportSubtree(arrayOfPreferences[(??? - 1)], (Element)localNodeList.item(???));
/*     */   }
/*     */ 
/*     */   private static void ImportPrefs(Preferences paramPreferences, Element paramElement)
/*     */   {
/* 326 */     NodeList localNodeList = paramElement.getChildNodes();
/* 327 */     int i = 0; for (int j = localNodeList.getLength(); i < j; i++) {
/* 328 */       Element localElement = (Element)localNodeList.item(i);
/* 329 */       paramPreferences.put(localElement.getAttribute("key"), localElement.getAttribute("value"));
/*     */     }
/*     */   }
/*     */ 
/*     */   static void exportMap(OutputStream paramOutputStream, Map paramMap)
/*     */     throws IOException
/*     */   {
/* 343 */     Document localDocument = createPrefsDoc("map");
/* 344 */     Element localElement1 = localDocument.getDocumentElement();
/* 345 */     localElement1.setAttribute("MAP_XML_VERSION", "1.0");
/*     */ 
/* 347 */     for (Iterator localIterator = paramMap.entrySet().iterator(); localIterator.hasNext(); ) {
/* 348 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 349 */       Element localElement2 = (Element)localElement1.appendChild(localDocument.createElement("entry"));
/*     */ 
/* 351 */       localElement2.setAttribute("key", (String)localEntry.getKey());
/* 352 */       localElement2.setAttribute("value", (String)localEntry.getValue());
/*     */     }
/*     */ 
/* 355 */     writeDoc(localDocument, paramOutputStream);
/*     */   }
/*     */ 
/*     */   static void importMap(InputStream paramInputStream, Map paramMap)
/*     */     throws IOException, InvalidPreferencesFormatException
/*     */   {
/*     */     try
/*     */     {
/* 375 */       Document localDocument = loadPrefsDoc(paramInputStream);
/* 376 */       Element localElement1 = localDocument.getDocumentElement();
/*     */ 
/* 378 */       String str = localElement1.getAttribute("MAP_XML_VERSION");
/* 379 */       if (str.compareTo("1.0") > 0) {
/* 380 */         throw new InvalidPreferencesFormatException("Preferences map file format version " + str + " is not supported. This java installation can read" + " versions " + "1.0" + " or older. You may need" + " to install a newer version of JDK.");
/*     */       }
/*     */ 
/* 386 */       NodeList localNodeList = localElement1.getChildNodes();
/* 387 */       int i = 0; for (int j = localNodeList.getLength(); i < j; i++) {
/* 388 */         Element localElement2 = (Element)localNodeList.item(i);
/* 389 */         paramMap.put(localElement2.getAttribute("key"), localElement2.getAttribute("value"));
/*     */       }
/*     */     } catch (SAXException localSAXException) {
/* 392 */       throw new InvalidPreferencesFormatException(localSAXException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class EH
/*     */     implements ErrorHandler
/*     */   {
/*     */     public void error(SAXParseException paramSAXParseException)
/*     */       throws SAXException
/*     */     {
/* 412 */       throw paramSAXParseException;
/*     */     }
/*     */     public void fatalError(SAXParseException paramSAXParseException) throws SAXException {
/* 415 */       throw paramSAXParseException;
/*     */     }
/*     */     public void warning(SAXParseException paramSAXParseException) throws SAXException {
/* 418 */       throw paramSAXParseException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class Resolver
/*     */     implements EntityResolver
/*     */   {
/*     */     public InputSource resolveEntity(String paramString1, String paramString2)
/*     */       throws SAXException
/*     */     {
/* 400 */       if (paramString2.equals("http://java.sun.com/dtd/preferences.dtd"))
/*     */       {
/* 402 */         InputSource localInputSource = new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?><!-- DTD for preferences --><!ELEMENT preferences (root) ><!ATTLIST preferences EXTERNAL_XML_VERSION CDATA \"0.0\"  ><!ELEMENT root (map, node*) ><!ATTLIST root          type (system|user) #REQUIRED ><!ELEMENT node (map, node*) ><!ATTLIST node          name CDATA #REQUIRED ><!ELEMENT map (entry*) ><!ATTLIST map  MAP_XML_VERSION CDATA \"0.0\"  ><!ELEMENT entry EMPTY ><!ATTLIST entry          key CDATA #REQUIRED          value CDATA #REQUIRED >"));
/* 403 */         localInputSource.setSystemId("http://java.sun.com/dtd/preferences.dtd");
/* 404 */         return localInputSource;
/*     */       }
/* 406 */       throw new SAXException("Invalid system identifier: " + paramString2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.XmlSupport
 * JD-Core Version:    0.6.2
 */
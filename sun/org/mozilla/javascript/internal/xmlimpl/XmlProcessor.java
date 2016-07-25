/*     */ package sun.org.mozilla.javascript.internal.xmlimpl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.ProcessingInstruction;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ 
/*     */ class XmlProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 6903514433204808713L;
/*     */   private boolean ignoreComments;
/*     */   private boolean ignoreProcessingInstructions;
/*     */   private boolean ignoreWhitespace;
/*     */   private boolean prettyPrint;
/*     */   private int prettyIndent;
/*     */   private transient DocumentBuilderFactory dom;
/*     */   private transient TransformerFactory xform;
/*     */   private transient DocumentBuilder documentBuilder;
/*  68 */   private RhinoSAXErrorHandler errorHandler = new RhinoSAXErrorHandler(null);
/*     */ 
/*     */   XmlProcessor()
/*     */   {
/*  93 */     setDefault();
/*  94 */     this.dom = DocumentBuilderFactory.newInstance();
/*  95 */     this.dom.setNamespaceAware(true);
/*  96 */     this.dom.setIgnoringComments(false);
/*  97 */     this.xform = TransformerFactory.newInstance();
/*     */   }
/*     */ 
/*     */   final void setDefault() {
/* 101 */     setIgnoreComments(true);
/* 102 */     setIgnoreProcessingInstructions(true);
/* 103 */     setIgnoreWhitespace(true);
/* 104 */     setPrettyPrinting(true);
/* 105 */     setPrettyIndent(2);
/*     */   }
/*     */ 
/*     */   final void setIgnoreComments(boolean paramBoolean) {
/* 109 */     this.ignoreComments = paramBoolean;
/*     */   }
/*     */ 
/*     */   final void setIgnoreWhitespace(boolean paramBoolean) {
/* 113 */     this.ignoreWhitespace = paramBoolean;
/*     */   }
/*     */ 
/*     */   final void setIgnoreProcessingInstructions(boolean paramBoolean) {
/* 117 */     this.ignoreProcessingInstructions = paramBoolean;
/*     */   }
/*     */ 
/*     */   final void setPrettyPrinting(boolean paramBoolean) {
/* 121 */     this.prettyPrint = paramBoolean;
/*     */   }
/*     */ 
/*     */   final void setPrettyIndent(int paramInt) {
/* 125 */     this.prettyIndent = paramInt;
/*     */   }
/*     */ 
/*     */   final boolean isIgnoreComments() {
/* 129 */     return this.ignoreComments;
/*     */   }
/*     */ 
/*     */   final boolean isIgnoreProcessingInstructions() {
/* 133 */     return this.ignoreProcessingInstructions;
/*     */   }
/*     */ 
/*     */   final boolean isIgnoreWhitespace() {
/* 137 */     return this.ignoreWhitespace;
/*     */   }
/*     */ 
/*     */   final boolean isPrettyPrinting() {
/* 141 */     return this.prettyPrint;
/*     */   }
/*     */ 
/*     */   final int getPrettyIndent() {
/* 145 */     return this.prettyIndent;
/*     */   }
/*     */ 
/*     */   private String toXmlNewlines(String paramString) {
/* 149 */     StringBuffer localStringBuffer = new StringBuffer();
/* 150 */     for (int i = 0; i < paramString.length(); i++) {
/* 151 */       if (paramString.charAt(i) == '\r') {
/* 152 */         if (paramString.charAt(i + 1) != '\n')
/*     */         {
/* 156 */           localStringBuffer.append('\n');
/*     */         }
/*     */       }
/* 159 */       else localStringBuffer.append(paramString.charAt(i));
/*     */     }
/*     */ 
/* 162 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private DocumentBuilderFactory getDomFactory() {
/* 166 */     return this.dom;
/*     */   }
/*     */ 
/*     */   private synchronized DocumentBuilder getDocumentBuilderFromPool()
/*     */     throws ParserConfigurationException
/*     */   {
/*     */     DocumentBuilder localDocumentBuilder;
/* 173 */     if (this.documentBuilder == null) {
/* 174 */       DocumentBuilderFactory localDocumentBuilderFactory = getDomFactory();
/* 175 */       localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/*     */     } else {
/* 177 */       localDocumentBuilder = this.documentBuilder;
/* 178 */       this.documentBuilder = null;
/*     */     }
/* 180 */     localDocumentBuilder.setErrorHandler(this.errorHandler);
/* 181 */     return localDocumentBuilder;
/*     */   }
/*     */ 
/*     */   private synchronized void returnDocumentBuilderToPool(DocumentBuilder paramDocumentBuilder) {
/* 185 */     if (this.documentBuilder == null)
/*     */       try {
/* 187 */         paramDocumentBuilder.reset();
/* 188 */         this.documentBuilder = paramDocumentBuilder;
/*     */       }
/*     */       catch (UnsupportedOperationException localUnsupportedOperationException)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   private void addProcessingInstructionsTo(List<Node> paramList, Node paramNode)
/*     */   {
/* 197 */     if ((paramNode instanceof ProcessingInstruction)) {
/* 198 */       paramList.add(paramNode);
/*     */     }
/* 200 */     if (paramNode.getChildNodes() != null)
/* 201 */       for (int i = 0; i < paramNode.getChildNodes().getLength(); i++)
/* 202 */         addProcessingInstructionsTo(paramList, paramNode.getChildNodes().item(i));
/*     */   }
/*     */ 
/*     */   private void addCommentsTo(List<Node> paramList, Node paramNode)
/*     */   {
/* 208 */     if ((paramNode instanceof Comment)) {
/* 209 */       paramList.add(paramNode);
/*     */     }
/* 211 */     if (paramNode.getChildNodes() != null)
/* 212 */       for (int i = 0; i < paramNode.getChildNodes().getLength(); i++)
/* 213 */         addProcessingInstructionsTo(paramList, paramNode.getChildNodes().item(i));
/*     */   }
/*     */ 
/*     */   private void addTextNodesToRemoveAndTrim(List<Node> paramList, Node paramNode)
/*     */   {
/* 219 */     if ((paramNode instanceof Text)) {
/* 220 */       Text localText = (Text)paramNode;
/* 221 */       int j = 0;
/* 222 */       if (j == 0) {
/* 223 */         localText.setData(localText.getData().trim());
/*     */       }
/* 225 */       else if (localText.getData().trim().length() == 0) {
/* 226 */         localText.setData("");
/*     */       }
/*     */ 
/* 229 */       if (localText.getData().length() == 0) {
/* 230 */         paramList.add(paramNode);
/*     */       }
/*     */     }
/* 233 */     if (paramNode.getChildNodes() != null)
/* 234 */       for (int i = 0; i < paramNode.getChildNodes().getLength(); i++)
/* 235 */         addTextNodesToRemoveAndTrim(paramList, paramNode.getChildNodes().item(i));
/*     */   }
/*     */ 
/*     */   final Node toXml(String paramString1, String paramString2)
/*     */     throws SAXException
/*     */   {
/* 242 */     DocumentBuilder localDocumentBuilder = null;
/*     */     try {
/* 244 */       String str = "<parent xmlns=\"" + paramString1 + "\">" + paramString2 + "</parent>";
/*     */ 
/* 246 */       localDocumentBuilder = getDocumentBuilderFromPool();
/* 247 */       Document localDocument = localDocumentBuilder.parse(new InputSource(new StringReader(str)));
/* 248 */       if (this.ignoreProcessingInstructions) {
/* 249 */         localObject1 = new ArrayList();
/* 250 */         addProcessingInstructionsTo((List)localObject1, localDocument);
/* 251 */         for (localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Node)((Iterator)localObject2).next();
/* 252 */           ((Node)localObject3).getParentNode().removeChild((Node)localObject3);
/*     */         }
/*     */       }
/*     */       Object localObject3;
/* 255 */       if (this.ignoreComments) {
/* 256 */         localObject1 = new ArrayList();
/* 257 */         addCommentsTo((List)localObject1, localDocument);
/* 258 */         for (localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Node)((Iterator)localObject2).next();
/* 259 */           ((Node)localObject3).getParentNode().removeChild((Node)localObject3);
/*     */         }
/*     */       }
/* 262 */       if (this.ignoreWhitespace)
/*     */       {
/* 268 */         localObject1 = new ArrayList();
/* 269 */         addTextNodesToRemoveAndTrim((List)localObject1, localDocument);
/* 270 */         for (localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Node)((Iterator)localObject2).next();
/* 271 */           ((Node)localObject3).getParentNode().removeChild((Node)localObject3);
/*     */         }
/*     */       }
/* 274 */       Object localObject1 = localDocument.getDocumentElement().getChildNodes();
/* 275 */       if (((NodeList)localObject1).getLength() > 1)
/* 276 */         throw ScriptRuntime.constructError("SyntaxError", "XML objects may contain at most one node.");
/* 277 */       if (((NodeList)localObject1).getLength() == 0) {
/* 278 */         localObject2 = localDocument.createTextNode("");
/* 279 */         return localObject2;
/*     */       }
/* 281 */       Object localObject2 = ((NodeList)localObject1).item(0);
/* 282 */       localDocument.getDocumentElement().removeChild((Node)localObject2);
/* 283 */       return localObject2;
/*     */     }
/*     */     catch (IOException localIOException) {
/* 286 */       throw new RuntimeException("Unreachable.");
/*     */     } catch (ParserConfigurationException localParserConfigurationException) {
/* 288 */       throw new RuntimeException(localParserConfigurationException);
/*     */     } finally {
/* 290 */       if (localDocumentBuilder != null)
/* 291 */         returnDocumentBuilderToPool(localDocumentBuilder);
/*     */     }
/*     */   }
/*     */ 
/*     */   Document newDocument() {
/* 296 */     DocumentBuilder localDocumentBuilder = null;
/*     */     try
/*     */     {
/* 299 */       localDocumentBuilder = getDocumentBuilderFromPool();
/* 300 */       return localDocumentBuilder.newDocument();
/*     */     }
/*     */     catch (ParserConfigurationException localParserConfigurationException) {
/* 303 */       throw new RuntimeException(localParserConfigurationException);
/*     */     } finally {
/* 305 */       if (localDocumentBuilder != null)
/* 306 */         returnDocumentBuilderToPool(localDocumentBuilder);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String toString(Node paramNode)
/*     */   {
/* 312 */     DOMSource localDOMSource = new DOMSource(paramNode);
/* 313 */     StringWriter localStringWriter = new StringWriter();
/* 314 */     StreamResult localStreamResult = new StreamResult(localStringWriter);
/*     */     try {
/* 316 */       Transformer localTransformer = this.xform.newTransformer();
/* 317 */       localTransformer.setOutputProperty("omit-xml-declaration", "yes");
/* 318 */       localTransformer.setOutputProperty("indent", "no");
/* 319 */       localTransformer.setOutputProperty("method", "xml");
/* 320 */       localTransformer.transform(localDOMSource, localStreamResult);
/*     */     }
/*     */     catch (TransformerConfigurationException localTransformerConfigurationException) {
/* 323 */       throw new RuntimeException(localTransformerConfigurationException);
/*     */     }
/*     */     catch (TransformerException localTransformerException) {
/* 326 */       throw new RuntimeException(localTransformerException);
/*     */     }
/* 328 */     return toXmlNewlines(localStringWriter.toString());
/*     */   }
/*     */ 
/*     */   String escapeAttributeValue(Object paramObject) {
/* 332 */     String str1 = ScriptRuntime.toString(paramObject);
/*     */ 
/* 334 */     if (str1.length() == 0) return "";
/*     */ 
/* 336 */     Document localDocument = newDocument();
/* 337 */     Element localElement = localDocument.createElement("a");
/* 338 */     localElement.setAttribute("b", str1);
/* 339 */     String str2 = toString(localElement);
/* 340 */     int i = str2.indexOf('"');
/* 341 */     int j = str2.lastIndexOf('"');
/* 342 */     return str2.substring(i + 1, j);
/*     */   }
/*     */ 
/*     */   String escapeTextValue(Object paramObject) {
/* 346 */     if ((paramObject instanceof XMLObjectImpl)) {
/* 347 */       return ((XMLObjectImpl)paramObject).toXMLString();
/*     */     }
/*     */ 
/* 350 */     String str1 = ScriptRuntime.toString(paramObject);
/*     */ 
/* 352 */     if (str1.length() == 0) return str1;
/*     */ 
/* 354 */     Document localDocument = newDocument();
/* 355 */     Element localElement = localDocument.createElement("a");
/* 356 */     localElement.setTextContent(str1);
/* 357 */     String str2 = toString(localElement);
/*     */ 
/* 359 */     int i = str2.indexOf('>') + 1;
/* 360 */     int j = str2.lastIndexOf('<');
/* 361 */     return i < j ? str2.substring(i, j) : "";
/*     */   }
/*     */ 
/*     */   private String escapeElementValue(String paramString)
/*     */   {
/* 366 */     return escapeTextValue(paramString);
/*     */   }
/*     */ 
/*     */   private String elementToXmlString(Element paramElement)
/*     */   {
/* 371 */     Element localElement = (Element)paramElement.cloneNode(true);
/* 372 */     if (this.prettyPrint) {
/* 373 */       beautifyElement(localElement, 0);
/*     */     }
/* 375 */     return toString(localElement);
/*     */   }
/*     */ 
/*     */   final String ecmaToXmlString(Node paramNode)
/*     */   {
/* 380 */     StringBuffer localStringBuffer = new StringBuffer();
/* 381 */     int i = 0;
/* 382 */     if (this.prettyPrint)
/* 383 */       for (int j = 0; j < i; j++)
/* 384 */         localStringBuffer.append(' ');
/*     */     Object localObject1;
/* 387 */     if ((paramNode instanceof Text)) {
/* 388 */       localObject1 = ((Text)paramNode).getData();
/*     */ 
/* 390 */       Object localObject2 = this.prettyPrint ? ((String)localObject1).trim() : localObject1;
/* 391 */       localStringBuffer.append(escapeElementValue(localObject2));
/* 392 */       return localStringBuffer.toString();
/*     */     }
/* 394 */     if ((paramNode instanceof Attr)) {
/* 395 */       localObject1 = ((Attr)paramNode).getValue();
/* 396 */       localStringBuffer.append(escapeAttributeValue(localObject1));
/* 397 */       return localStringBuffer.toString();
/*     */     }
/* 399 */     if ((paramNode instanceof Comment)) {
/* 400 */       localStringBuffer.append("<!--" + ((Comment)paramNode).getNodeValue() + "-->");
/* 401 */       return localStringBuffer.toString();
/*     */     }
/* 403 */     if ((paramNode instanceof ProcessingInstruction)) {
/* 404 */       localObject1 = (ProcessingInstruction)paramNode;
/* 405 */       localStringBuffer.append("<?" + ((ProcessingInstruction)localObject1).getTarget() + " " + ((ProcessingInstruction)localObject1).getData() + "?>");
/* 406 */       return localStringBuffer.toString();
/*     */     }
/* 408 */     localStringBuffer.append(elementToXmlString((Element)paramNode));
/* 409 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void beautifyElement(Element paramElement, int paramInt) {
/* 413 */     StringBuffer localStringBuffer = new StringBuffer();
/* 414 */     localStringBuffer.append('\n');
/* 415 */     for (int i = 0; i < paramInt; i++) {
/* 416 */       localStringBuffer.append(' ');
/*     */     }
/* 418 */     String str1 = localStringBuffer.toString();
/* 419 */     for (int j = 0; j < this.prettyIndent; j++) {
/* 420 */       localStringBuffer.append(' ');
/*     */     }
/* 422 */     String str2 = localStringBuffer.toString();
/*     */ 
/* 426 */     ArrayList localArrayList1 = new ArrayList();
/* 427 */     int k = 0;
/* 428 */     for (int m = 0; m < paramElement.getChildNodes().getLength(); m++) {
/* 429 */       if (m == 1) k = 1;
/* 430 */       if ((paramElement.getChildNodes().item(m) instanceof Text)) {
/* 431 */         localArrayList1.add(paramElement.getChildNodes().item(m));
/*     */       } else {
/* 433 */         k = 1;
/* 434 */         localArrayList1.add(paramElement.getChildNodes().item(m));
/*     */       }
/*     */     }
/* 437 */     if (k != 0) {
/* 438 */       for (m = 0; m < localArrayList1.size(); m++) {
/* 439 */         paramElement.insertBefore(paramElement.getOwnerDocument().createTextNode(str2), (Node)localArrayList1.get(m));
/*     */       }
/*     */     }
/*     */ 
/* 443 */     NodeList localNodeList = paramElement.getChildNodes();
/* 444 */     ArrayList localArrayList2 = new ArrayList();
/* 445 */     for (int n = 0; n < localNodeList.getLength(); n++) {
/* 446 */       if ((localNodeList.item(n) instanceof Element)) {
/* 447 */         localArrayList2.add((Element)localNodeList.item(n));
/*     */       }
/*     */     }
/* 450 */     for (Element localElement : localArrayList2) {
/* 451 */       beautifyElement(localElement, paramInt + this.prettyIndent);
/*     */     }
/* 453 */     if (k != 0)
/* 454 */       paramElement.appendChild(paramElement.getOwnerDocument().createTextNode(str1));
/*     */   }
/*     */ 
/*     */   private static class RhinoSAXErrorHandler
/*     */     implements ErrorHandler
/*     */   {
/*     */     private static final long serialVersionUID = 6918417235413084055L;
/*     */ 
/*     */     private void throwError(SAXParseException paramSAXParseException)
/*     */     {
/*  75 */       throw ScriptRuntime.constructError("TypeError", paramSAXParseException.getMessage(), paramSAXParseException.getLineNumber() - 1);
/*     */     }
/*     */ 
/*     */     public void error(SAXParseException paramSAXParseException)
/*     */     {
/*  80 */       throwError(paramSAXParseException);
/*     */     }
/*     */ 
/*     */     public void fatalError(SAXParseException paramSAXParseException) {
/*  84 */       throwError(paramSAXParseException);
/*     */     }
/*     */ 
/*     */     public void warning(SAXParseException paramSAXParseException) {
/*  88 */       Context.reportWarning(paramSAXParseException.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.xmlimpl.XmlProcessor
 * JD-Core Version:    0.6.2
 */
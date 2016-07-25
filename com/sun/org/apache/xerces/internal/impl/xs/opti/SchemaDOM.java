/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.XMLSymbols;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import com.sun.org.apache.xerces.internal.xni.QName;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*     */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class SchemaDOM extends DefaultDocument
/*     */ {
/*     */   static final int relationsRowResizeFactor = 15;
/*     */   static final int relationsColResizeFactor = 10;
/*     */   NodeImpl[][] relations;
/*     */   ElementImpl parent;
/*     */   int currLoc;
/*     */   int nextFreeLoc;
/*     */   boolean hidden;
/*     */   boolean inCDATA;
/*  59 */   private StringBuffer fAnnotationBuffer = null;
/*     */ 
/*     */   public SchemaDOM() {
/*  62 */     reset();
/*     */   }
/*     */ 
/*     */   public ElementImpl startElement(QName element, XMLAttributes attributes, int line, int column, int offset)
/*     */   {
/*  68 */     ElementImpl node = new ElementImpl(line, column, offset);
/*  69 */     processElement(element, attributes, node);
/*     */ 
/*  71 */     this.parent = node;
/*  72 */     return node;
/*     */   }
/*     */ 
/*     */   public ElementImpl emptyElement(QName element, XMLAttributes attributes, int line, int column, int offset)
/*     */   {
/*  77 */     ElementImpl node = new ElementImpl(line, column, offset);
/*  78 */     processElement(element, attributes, node);
/*  79 */     return node;
/*     */   }
/*     */ 
/*     */   public ElementImpl startElement(QName element, XMLAttributes attributes, int line, int column)
/*     */   {
/*  84 */     return startElement(element, attributes, line, column, -1);
/*     */   }
/*     */ 
/*     */   public ElementImpl emptyElement(QName element, XMLAttributes attributes, int line, int column)
/*     */   {
/*  89 */     return emptyElement(element, attributes, line, column, -1);
/*     */   }
/*     */ 
/*     */   private void processElement(QName element, XMLAttributes attributes, ElementImpl node)
/*     */   {
/*  95 */     node.prefix = element.prefix;
/*  96 */     node.localpart = element.localpart;
/*  97 */     node.rawname = element.rawname;
/*  98 */     node.uri = element.uri;
/*  99 */     node.schemaDOM = this;
/*     */ 
/* 102 */     Attr[] attrs = new Attr[attributes.getLength()];
/* 103 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 104 */       attrs[i] = new AttrImpl(node, attributes.getPrefix(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getURI(i), attributes.getValue(i));
/*     */     }
/*     */ 
/* 111 */     node.attrs = attrs;
/*     */ 
/* 114 */     if (this.nextFreeLoc == this.relations.length) {
/* 115 */       resizeRelations();
/*     */     }
/*     */ 
/* 120 */     if (this.relations[this.currLoc][0] != this.parent) {
/* 121 */       this.relations[this.nextFreeLoc][0] = this.parent;
/* 122 */       this.currLoc = (this.nextFreeLoc++);
/*     */     }
/*     */ 
/* 126 */     boolean foundPlace = false;
/* 127 */     int i = 1;
/* 128 */     for (i = 1; i < this.relations[this.currLoc].length; i++) {
/* 129 */       if (this.relations[this.currLoc][i] == null) {
/* 130 */         foundPlace = true;
/* 131 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 135 */     if (!foundPlace) {
/* 136 */       resizeRelations(this.currLoc);
/*     */     }
/* 138 */     this.relations[this.currLoc][i] = node;
/*     */ 
/* 140 */     this.parent.parentRow = this.currLoc;
/* 141 */     node.row = this.currLoc;
/* 142 */     node.col = i;
/*     */   }
/*     */ 
/*     */   public void endElement()
/*     */   {
/* 149 */     this.currLoc = this.parent.row;
/* 150 */     this.parent = ((ElementImpl)this.relations[this.currLoc][0]);
/*     */   }
/*     */ 
/*     */   void comment(XMLString text)
/*     */   {
/* 155 */     this.fAnnotationBuffer.append("<!--");
/* 156 */     if (text.length > 0) {
/* 157 */       this.fAnnotationBuffer.append(text.ch, text.offset, text.length);
/*     */     }
/* 159 */     this.fAnnotationBuffer.append("-->");
/*     */   }
/*     */ 
/*     */   void processingInstruction(String target, XMLString data)
/*     */   {
/* 164 */     this.fAnnotationBuffer.append("<?").append(target);
/* 165 */     if (data.length > 0) {
/* 166 */       this.fAnnotationBuffer.append(' ').append(data.ch, data.offset, data.length);
/*     */     }
/* 168 */     this.fAnnotationBuffer.append("?>");
/*     */   }
/*     */ 
/*     */   void characters(XMLString text)
/*     */   {
/* 175 */     if (!this.inCDATA) {
/* 176 */       StringBuffer annotationBuffer = this.fAnnotationBuffer;
/* 177 */       for (int i = text.offset; i < text.offset + text.length; i++) {
/* 178 */         char ch = text.ch[i];
/* 179 */         if (ch == '&') {
/* 180 */           annotationBuffer.append("&amp;");
/*     */         }
/* 182 */         else if (ch == '<') {
/* 183 */           annotationBuffer.append("&lt;");
/*     */         }
/* 187 */         else if (ch == '>') {
/* 188 */           annotationBuffer.append("&gt;");
/*     */         }
/* 194 */         else if (ch == '\r') {
/* 195 */           annotationBuffer.append("&#xD;");
/*     */         }
/*     */         else
/* 198 */           annotationBuffer.append(ch);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 203 */       this.fAnnotationBuffer.append(text.ch, text.offset, text.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   void charactersRaw(String text)
/*     */   {
/* 209 */     this.fAnnotationBuffer.append(text);
/*     */   }
/*     */ 
/*     */   void endAnnotation(QName elemName, ElementImpl annotation) {
/* 213 */     this.fAnnotationBuffer.append("\n</").append(elemName.rawname).append(">");
/* 214 */     annotation.fAnnotation = this.fAnnotationBuffer.toString();
/*     */ 
/* 216 */     this.fAnnotationBuffer = null;
/*     */   }
/*     */ 
/*     */   void endAnnotationElement(QName elemName) {
/* 220 */     endAnnotationElement(elemName.rawname);
/*     */   }
/*     */ 
/*     */   void endAnnotationElement(String elemRawName) {
/* 224 */     this.fAnnotationBuffer.append("</").append(elemRawName).append(">");
/*     */   }
/*     */ 
/*     */   void endSyntheticAnnotationElement(QName elemName, boolean complete) {
/* 228 */     endSyntheticAnnotationElement(elemName.rawname, complete);
/*     */   }
/*     */ 
/*     */   void endSyntheticAnnotationElement(String elemRawName, boolean complete) {
/* 232 */     if (complete) {
/* 233 */       this.fAnnotationBuffer.append("\n</").append(elemRawName).append(">");
/*     */ 
/* 238 */       this.parent.fSyntheticAnnotation = this.fAnnotationBuffer.toString();
/*     */ 
/* 242 */       this.fAnnotationBuffer = null;
/*     */     } else {
/* 244 */       this.fAnnotationBuffer.append("</").append(elemRawName).append(">");
/*     */     }
/*     */   }
/*     */ 
/* 248 */   void startAnnotationCDATA() { this.inCDATA = true;
/* 249 */     this.fAnnotationBuffer.append("<![CDATA["); }
/*     */ 
/*     */   void endAnnotationCDATA()
/*     */   {
/* 253 */     this.fAnnotationBuffer.append("]]>");
/* 254 */     this.inCDATA = false;
/*     */   }
/*     */ 
/*     */   private void resizeRelations() {
/* 258 */     NodeImpl[][] temp = new NodeImpl[this.relations.length + 15][];
/* 259 */     System.arraycopy(this.relations, 0, temp, 0, this.relations.length);
/* 260 */     for (int i = this.relations.length; i < temp.length; i++) {
/* 261 */       temp[i] = new NodeImpl[10];
/*     */     }
/* 263 */     this.relations = temp;
/*     */   }
/*     */ 
/*     */   private void resizeRelations(int i) {
/* 267 */     NodeImpl[] temp = new NodeImpl[this.relations[i].length + 10];
/* 268 */     System.arraycopy(this.relations[i], 0, temp, 0, this.relations[i].length);
/* 269 */     this.relations[i] = temp;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 276 */     if (this.relations != null)
/* 277 */       for (int i = 0; i < this.relations.length; i++)
/* 278 */         for (int j = 0; j < this.relations[i].length; j++)
/* 279 */           this.relations[i][j] = null;
/* 280 */     this.relations = new NodeImpl[15][];
/* 281 */     this.parent = new ElementImpl(0, 0, 0);
/* 282 */     this.parent.rawname = "DOCUMENT_NODE";
/* 283 */     this.currLoc = 0;
/* 284 */     this.nextFreeLoc = 1;
/* 285 */     this.inCDATA = false;
/* 286 */     for (int i = 0; i < 15; i++) {
/* 287 */       this.relations[i] = new NodeImpl[10];
/*     */     }
/* 289 */     this.relations[this.currLoc][0] = this.parent;
/*     */   }
/*     */ 
/*     */   public void printDOM()
/*     */   {
/*     */   }
/*     */ 
/*     */   public static void traverse(Node node, int depth)
/*     */   {
/* 313 */     indent(depth);
/* 314 */     System.out.print("<" + node.getNodeName());
/*     */ 
/* 316 */     if (node.hasAttributes()) {
/* 317 */       NamedNodeMap attrs = node.getAttributes();
/* 318 */       for (int i = 0; i < attrs.getLength(); i++) {
/* 319 */         System.out.print("  " + ((Attr)attrs.item(i)).getName() + "=\"" + ((Attr)attrs.item(i)).getValue() + "\"");
/*     */       }
/*     */     }
/*     */ 
/* 323 */     if (node.hasChildNodes()) {
/* 324 */       System.out.println(">");
/* 325 */       depth += 4;
/* 326 */       for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
/* 327 */         traverse(child, depth);
/*     */       }
/* 329 */       depth -= 4;
/* 330 */       indent(depth);
/* 331 */       System.out.println("</" + node.getNodeName() + ">");
/*     */     }
/*     */     else {
/* 334 */       System.out.println("/>");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void indent(int amount) {
/* 339 */     for (int i = 0; i < amount; i++)
/* 340 */       System.out.print(' ');
/*     */   }
/*     */ 
/*     */   public Element getDocumentElement()
/*     */   {
/* 347 */     return (ElementImpl)this.relations[0][1];
/*     */   }
/*     */ 
/*     */   public DOMImplementation getImplementation() {
/* 351 */     return SchemaDOMImplementation.getDOMImplementation();
/*     */   }
/*     */ 
/*     */   void startAnnotation(QName elemName, XMLAttributes attributes, NamespaceContext namespaceContext)
/*     */   {
/* 357 */     startAnnotation(elemName.rawname, attributes, namespaceContext);
/*     */   }
/*     */ 
/*     */   void startAnnotation(String elemRawName, XMLAttributes attributes, NamespaceContext namespaceContext) {
/* 361 */     if (this.fAnnotationBuffer == null) this.fAnnotationBuffer = new StringBuffer(256);
/* 362 */     this.fAnnotationBuffer.append("<").append(elemRawName).append(" ");
/*     */ 
/* 369 */     ArrayList namespaces = new ArrayList();
/* 370 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 371 */       String aValue = attributes.getValue(i);
/* 372 */       String aPrefix = attributes.getPrefix(i);
/* 373 */       String aQName = attributes.getQName(i);
/*     */ 
/* 375 */       if ((aPrefix == XMLSymbols.PREFIX_XMLNS) || (aQName == XMLSymbols.PREFIX_XMLNS)) {
/* 376 */         namespaces.add(aPrefix == XMLSymbols.PREFIX_XMLNS ? attributes.getLocalName(i) : XMLSymbols.EMPTY_STRING);
/*     */       }
/*     */ 
/* 379 */       this.fAnnotationBuffer.append(aQName).append("=\"").append(processAttValue(aValue)).append("\" ");
/*     */     }
/*     */ 
/* 383 */     Enumeration currPrefixes = namespaceContext.getAllPrefixes();
/* 384 */     while (currPrefixes.hasMoreElements()) {
/* 385 */       String prefix = (String)currPrefixes.nextElement();
/* 386 */       String uri = namespaceContext.getURI(prefix);
/* 387 */       if (uri == null) {
/* 388 */         uri = XMLSymbols.EMPTY_STRING;
/*     */       }
/* 390 */       if (!namespaces.contains(prefix))
/*     */       {
/* 392 */         if (prefix == XMLSymbols.EMPTY_STRING) {
/* 393 */           this.fAnnotationBuffer.append("xmlns").append("=\"").append(processAttValue(uri)).append("\" ");
/*     */         }
/*     */         else {
/* 396 */           this.fAnnotationBuffer.append("xmlns:").append(prefix).append("=\"").append(processAttValue(uri)).append("\" ");
/*     */         }
/*     */       }
/*     */     }
/* 400 */     this.fAnnotationBuffer.append(">\n");
/*     */   }
/*     */   void startAnnotationElement(QName elemName, XMLAttributes attributes) {
/* 403 */     startAnnotationElement(elemName.rawname, attributes);
/*     */   }
/*     */   void startAnnotationElement(String elemRawName, XMLAttributes attributes) {
/* 406 */     this.fAnnotationBuffer.append("<").append(elemRawName);
/* 407 */     for (int i = 0; i < attributes.getLength(); i++) {
/* 408 */       String aValue = attributes.getValue(i);
/* 409 */       this.fAnnotationBuffer.append(" ").append(attributes.getQName(i)).append("=\"").append(processAttValue(aValue)).append("\"");
/*     */     }
/* 411 */     this.fAnnotationBuffer.append(">");
/*     */   }
/*     */ 
/*     */   private static String processAttValue(String original) {
/* 415 */     int length = original.length();
/*     */ 
/* 417 */     for (int i = 0; i < length; i++) {
/* 418 */       char currChar = original.charAt(i);
/* 419 */       if ((currChar == '"') || (currChar == '<') || (currChar == '&') || (currChar == '\t') || (currChar == '\n') || (currChar == '\r'))
/*     */       {
/* 421 */         return escapeAttValue(original, i);
/*     */       }
/*     */     }
/* 424 */     return original;
/*     */   }
/*     */ 
/*     */   private static String escapeAttValue(String original, int from)
/*     */   {
/* 429 */     int length = original.length();
/* 430 */     StringBuffer newVal = new StringBuffer(length);
/* 431 */     newVal.append(original.substring(0, from));
/* 432 */     for (int i = from; i < length; i++) {
/* 433 */       char currChar = original.charAt(i);
/* 434 */       if (currChar == '"') {
/* 435 */         newVal.append("&quot;");
/*     */       }
/* 437 */       else if (currChar == '<') {
/* 438 */         newVal.append("&lt;");
/*     */       }
/* 440 */       else if (currChar == '&') {
/* 441 */         newVal.append("&amp;");
/*     */       }
/* 446 */       else if (currChar == '\t') {
/* 447 */         newVal.append("&#x9;");
/*     */       }
/* 449 */       else if (currChar == '\n') {
/* 450 */         newVal.append("&#xA;");
/*     */       }
/* 452 */       else if (currChar == '\r') {
/* 453 */         newVal.append("&#xD;");
/*     */       }
/*     */       else {
/* 456 */         newVal.append(currChar);
/*     */       }
/*     */     }
/* 459 */     return newVal.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.SchemaDOM
 * JD-Core Version:    0.6.2
 */
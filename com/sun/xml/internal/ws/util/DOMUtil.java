/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class DOMUtil
/*     */ {
/*     */   private static DocumentBuilder db;
/*     */ 
/*     */   public static Document createDom()
/*     */   {
/*  63 */     synchronized (DOMUtil.class) {
/*  64 */       if (db == null) {
/*     */         try {
/*  66 */           DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  67 */           dbf.setNamespaceAware(true);
/*  68 */           db = dbf.newDocumentBuilder();
/*     */         } catch (ParserConfigurationException e) {
/*  70 */           throw new FactoryConfigurationError(e);
/*     */         }
/*     */       }
/*  73 */       return db.newDocument();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Node createDOMNode(InputStream inputStream)
/*     */   {
/*  79 */     DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
/*  80 */     dbf.setNamespaceAware(true);
/*  81 */     dbf.setValidating(false);
/*     */     try {
/*  83 */       DocumentBuilder builder = dbf.newDocumentBuilder();
/*     */       try {
/*  85 */         return builder.parse(inputStream);
/*     */       } catch (SAXException e) {
/*  87 */         e.printStackTrace();
/*     */       } catch (IOException e) {
/*  89 */         e.printStackTrace();
/*     */       }
/*     */     } catch (ParserConfigurationException pce) {
/*  92 */       IllegalArgumentException iae = new IllegalArgumentException(pce.getMessage());
/*  93 */       iae.initCause(pce);
/*  94 */       throw iae;
/*     */     }
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */   public static void serializeNode(Element node, XMLStreamWriter writer)
/*     */     throws XMLStreamException
/*     */   {
/* 106 */     writeTagWithAttributes(node, writer);
/*     */ 
/* 108 */     if (node.hasChildNodes()) {
/* 109 */       NodeList children = node.getChildNodes();
/* 110 */       for (int i = 0; i < children.getLength(); i++) {
/* 111 */         Node child = children.item(i);
/* 112 */         switch (child.getNodeType()) {
/*     */         case 7:
/* 114 */           writer.writeProcessingInstruction(child.getNodeValue());
/*     */         case 10:
/* 116 */           break;
/*     */         case 4:
/* 118 */           writer.writeCData(child.getNodeValue());
/* 119 */           break;
/*     */         case 8:
/* 121 */           writer.writeComment(child.getNodeValue());
/* 122 */           break;
/*     */         case 3:
/* 124 */           writer.writeCharacters(child.getNodeValue());
/* 125 */           break;
/*     */         case 1:
/* 127 */           serializeNode((Element)child, writer);
/*     */         case 2:
/*     */         case 5:
/*     */         case 6:
/*     */         case 9: }  } 
/* 132 */     }writer.writeEndElement();
/*     */   }
/*     */ 
/*     */   public static void writeTagWithAttributes(Element node, XMLStreamWriter writer) throws XMLStreamException {
/* 136 */     String nodePrefix = fixNull(node.getPrefix());
/* 137 */     String nodeNS = fixNull(node.getNamespaceURI());
/*     */ 
/* 139 */     String nodeLocalName = node.getLocalName() == null ? node.getNodeName() : node.getLocalName();
/*     */ 
/* 143 */     boolean prefixDecl = isPrefixDeclared(writer, nodeNS, nodePrefix);
/* 144 */     writer.writeStartElement(nodePrefix, nodeLocalName, nodeNS);
/*     */ 
/* 146 */     if (node.hasAttributes()) {
/* 147 */       NamedNodeMap attrs = node.getAttributes();
/* 148 */       int numOfAttributes = attrs.getLength();
/*     */ 
/* 152 */       for (int i = 0; i < numOfAttributes; i++) {
/* 153 */         Node attr = attrs.item(i);
/* 154 */         String nsUri = fixNull(attr.getNamespaceURI());
/* 155 */         if (nsUri.equals("http://www.w3.org/2000/xmlns/"))
/*     */         {
/* 157 */           String local = attr.getLocalName().equals("xmlns") ? "" : attr.getLocalName();
/* 158 */           if ((local.equals(nodePrefix)) && (attr.getNodeValue().equals(nodeNS))) {
/* 159 */             prefixDecl = true;
/*     */           }
/* 161 */           if (local.equals("")) {
/* 162 */             writer.writeDefaultNamespace(attr.getNodeValue());
/*     */           }
/*     */           else {
/* 165 */             writer.setPrefix(attr.getLocalName(), attr.getNodeValue());
/* 166 */             writer.writeNamespace(attr.getLocalName(), attr.getNodeValue());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 172 */     if (!prefixDecl) {
/* 173 */       writer.writeNamespace(nodePrefix, nodeNS);
/*     */     }
/*     */ 
/* 177 */     if (node.hasAttributes()) {
/* 178 */       NamedNodeMap attrs = node.getAttributes();
/* 179 */       int numOfAttributes = attrs.getLength();
/*     */ 
/* 181 */       for (int i = 0; i < numOfAttributes; i++) {
/* 182 */         Node attr = attrs.item(i);
/* 183 */         String attrPrefix = fixNull(attr.getPrefix());
/* 184 */         String attrNS = fixNull(attr.getNamespaceURI());
/* 185 */         if (!attrNS.equals("http://www.w3.org/2000/xmlns/")) {
/* 186 */           String localName = attr.getLocalName();
/* 187 */           if (localName == null)
/*     */           {
/* 190 */             localName = attr.getNodeName();
/*     */           }
/* 192 */           boolean attrPrefixDecl = isPrefixDeclared(writer, attrNS, attrPrefix);
/* 193 */           if ((!attrPrefix.equals("")) && (!attrPrefixDecl))
/*     */           {
/* 196 */             writer.setPrefix(attr.getLocalName(), attr.getNodeValue());
/* 197 */             writer.writeNamespace(attrPrefix, attrNS);
/*     */           }
/* 199 */           writer.writeAttribute(attrPrefix, attrNS, localName, attr.getNodeValue());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isPrefixDeclared(XMLStreamWriter writer, String nsUri, String prefix) {
/* 206 */     boolean prefixDecl = false;
/* 207 */     NamespaceContext nscontext = writer.getNamespaceContext();
/* 208 */     Iterator prefixItr = nscontext.getPrefixes(nsUri);
/* 209 */     while (prefixItr.hasNext()) {
/* 210 */       if (prefix.equals(prefixItr.next())) {
/* 211 */         prefixDecl = true;
/*     */       }
/*     */     }
/*     */ 
/* 215 */     return prefixDecl;
/*     */   }
/*     */ 
/*     */   public static Element getFirstChild(Element e, String nsUri, String local)
/*     */   {
/* 222 */     for (Node n = e.getFirstChild(); n != null; n = n.getNextSibling()) {
/* 223 */       if (n.getNodeType() == 1) {
/* 224 */         Element c = (Element)n;
/* 225 */         if ((c.getLocalName().equals(local)) && (c.getNamespaceURI().equals(nsUri)))
/* 226 */           return c;
/*     */       }
/*     */     }
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private static String fixNull(@Nullable String s)
/*     */   {
/* 235 */     if (s == null) return "";
/* 236 */     return s;
/*     */   }
/*     */ 
/*     */   @Nullable
/*     */   public static Element getFirstElementChild(Node parent)
/*     */   {
/* 245 */     for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
/* 246 */       if (n.getNodeType() == 1) {
/* 247 */         return (Element)n;
/*     */       }
/*     */     }
/* 250 */     return null;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public static List<Element> getChildElements(Node parent) {
/* 255 */     List elements = new ArrayList();
/* 256 */     for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
/* 257 */       if (n.getNodeType() == 1) {
/* 258 */         elements.add((Element)n);
/*     */       }
/*     */     }
/* 261 */     return elements;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.DOMUtil
 * JD-Core Version:    0.6.2
 */
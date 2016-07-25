/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ /** @deprecated */
/*     */ public class DOM2Helper extends DOMHelper
/*     */ {
/*     */   private Document m_doc;
/*     */ 
/*     */   public void checkNode(Node node)
/*     */     throws TransformerException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean supportsSAX()
/*     */   {
/*  81 */     return true;
/*     */   }
/*     */ 
/*     */   public void setDocument(Document doc)
/*     */   {
/*  99 */     this.m_doc = doc;
/*     */   }
/*     */ 
/*     */   public Document getDocument()
/*     */   {
/* 110 */     return this.m_doc;
/*     */   }
/*     */ 
/*     */   public void parse(InputSource source)
/*     */     throws TransformerException
/*     */   {
/*     */     try
/*     */     {
/* 144 */       DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
/*     */ 
/* 147 */       builderFactory.setNamespaceAware(true);
/* 148 */       builderFactory.setValidating(true);
/*     */ 
/* 150 */       DocumentBuilder parser = builderFactory.newDocumentBuilder();
/*     */ 
/* 167 */       parser.setErrorHandler(new DefaultErrorHandler());
/*     */ 
/* 175 */       setDocument(parser.parse(source));
/*     */     }
/*     */     catch (SAXException se)
/*     */     {
/* 179 */       throw new TransformerException(se);
/*     */     }
/*     */     catch (ParserConfigurationException pce)
/*     */     {
/* 183 */       throw new TransformerException(pce);
/*     */     }
/*     */     catch (IOException ioe)
/*     */     {
/* 187 */       throw new TransformerException(ioe);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Element getElementByID(String id, Document doc)
/*     */   {
/* 210 */     return doc.getElementById(id);
/*     */   }
/*     */ 
/*     */   public static boolean isNodeAfter(Node node1, Node node2)
/*     */   {
/* 236 */     if (((node1 instanceof DOMOrder)) && ((node2 instanceof DOMOrder)))
/*     */     {
/* 238 */       int index1 = ((DOMOrder)node1).getUid();
/* 239 */       int index2 = ((DOMOrder)node2).getUid();
/*     */ 
/* 241 */       return index1 <= index2;
/*     */     }
/*     */ 
/* 248 */     return DOMHelper.isNodeAfter(node1, node2);
/*     */   }
/*     */ 
/*     */   public static Node getParentOfNode(Node node)
/*     */   {
/* 266 */     Node parent = node.getParentNode();
/* 267 */     if ((parent == null) && (2 == node.getNodeType()))
/* 268 */       parent = ((Attr)node).getOwnerElement();
/* 269 */     return parent;
/*     */   }
/*     */ 
/*     */   public String getLocalNameOfNode(Node n)
/*     */   {
/* 286 */     String name = n.getLocalName();
/*     */ 
/* 288 */     return null == name ? super.getLocalNameOfNode(n) : name;
/*     */   }
/*     */ 
/*     */   public String getNamespaceOfNode(Node n)
/*     */   {
/* 308 */     return n.getNamespaceURI();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.DOM2Helper
 * JD-Core Version:    0.6.2
 */
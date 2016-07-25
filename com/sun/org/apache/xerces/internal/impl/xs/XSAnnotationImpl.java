/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
/*     */ import com.sun.org.apache.xerces.internal.parsers.DOMParser;
/*     */ import com.sun.org.apache.xerces.internal.parsers.SAXParser;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class XSAnnotationImpl
/*     */   implements XSAnnotation
/*     */ {
/*  49 */   private String fData = null;
/*     */ 
/*  53 */   private SchemaGrammar fGrammar = null;
/*     */ 
/*     */   public XSAnnotationImpl(String contents, SchemaGrammar grammar)
/*     */   {
/*  57 */     this.fData = contents;
/*  58 */     this.fGrammar = grammar;
/*     */   }
/*     */ 
/*     */   public boolean writeAnnotation(Object target, short targetType)
/*     */   {
/*  77 */     if ((targetType == 1) || (targetType == 3)) {
/*  78 */       writeToDOM((Node)target, targetType);
/*  79 */       return true;
/*  80 */     }if (targetType == 2) {
/*  81 */       writeToSAX((ContentHandler)target);
/*  82 */       return true;
/*     */     }
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   public String getAnnotationString()
/*     */   {
/*  91 */     return this.fData;
/*     */   }
/*     */ 
/*     */   public short getType()
/*     */   {
/* 101 */     return 12;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNamespace()
/*     */   {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public XSNamespaceItem getNamespaceItem()
/*     */   {
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */   private synchronized void writeToSAX(ContentHandler handler)
/*     */   {
/* 132 */     SAXParser parser = this.fGrammar.getSAXParser();
/* 133 */     StringReader aReader = new StringReader(this.fData);
/* 134 */     InputSource aSource = new InputSource(aReader);
/* 135 */     parser.setContentHandler(handler);
/*     */     try {
/* 137 */       parser.parse(aSource);
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/*     */     catch (IOException i)
/*     */     {
/*     */     }
/*     */ 
/* 148 */     parser.setContentHandler(null);
/*     */   }
/*     */ 
/*     */   private synchronized void writeToDOM(Node target, short type)
/*     */   {
/* 154 */     Document futureOwner = type == 1 ? target.getOwnerDocument() : (Document)target;
/*     */ 
/* 156 */     DOMParser parser = this.fGrammar.getDOMParser();
/* 157 */     StringReader aReader = new StringReader(this.fData);
/* 158 */     InputSource aSource = new InputSource(aReader);
/*     */     try {
/* 160 */       parser.parse(aSource);
/*     */     }
/*     */     catch (SAXException e)
/*     */     {
/*     */     }
/*     */     catch (IOException i)
/*     */     {
/*     */     }
/*     */ 
/* 170 */     Document aDocument = parser.getDocument();
/* 171 */     parser.dropDocumentReferences();
/* 172 */     Element annotation = aDocument.getDocumentElement();
/* 173 */     Node newElem = null;
/* 174 */     if ((futureOwner instanceof CoreDocumentImpl)) {
/* 175 */       newElem = futureOwner.adoptNode(annotation);
/*     */ 
/* 177 */       if (newElem == null)
/* 178 */         newElem = futureOwner.importNode(annotation, true);
/*     */     }
/*     */     else
/*     */     {
/* 182 */       newElem = futureOwner.importNode(annotation, true);
/*     */     }
/* 184 */     target.insertBefore(newElem, target.getFirstChild());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSAnnotationImpl
 * JD-Core Version:    0.6.2
 */
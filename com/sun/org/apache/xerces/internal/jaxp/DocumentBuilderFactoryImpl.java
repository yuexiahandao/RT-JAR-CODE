/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.parsers.DOMParser;
/*     */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ public class DocumentBuilderFactoryImpl extends DocumentBuilderFactory
/*     */ {
/*     */   private Hashtable attributes;
/*     */   private Hashtable features;
/*     */   private Schema grammar;
/*     */   private boolean isXIncludeAware;
/*  52 */   private boolean fSecureProcess = true;
/*     */ 
/*     */   public DocumentBuilder newDocumentBuilder()
/*     */     throws ParserConfigurationException
/*     */   {
/*  62 */     if ((this.grammar != null) && (this.attributes != null)) {
/*  63 */       if (this.attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaLanguage")) {
/*  64 */         throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaLanguage" }));
/*     */       }
/*     */ 
/*  68 */       if (this.attributes.containsKey("http://java.sun.com/xml/jaxp/properties/schemaSource")) {
/*  69 */         throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "schema-already-specified", new Object[] { "http://java.sun.com/xml/jaxp/properties/schemaSource" }));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  76 */       return new DocumentBuilderImpl(this, this.attributes, this.features, this.fSecureProcess);
/*     */     }
/*     */     catch (SAXException se) {
/*  79 */       throw new ParserConfigurationException(se.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, Object value)
/*     */     throws IllegalArgumentException
/*     */   {
/*  93 */     if (value == null) {
/*  94 */       if (this.attributes != null) {
/*  95 */         this.attributes.remove(name);
/*     */       }
/*     */ 
/*  98 */       return;
/*     */     }
/*     */ 
/* 105 */     if (this.attributes == null) {
/* 106 */       this.attributes = new Hashtable();
/*     */     }
/*     */ 
/* 109 */     this.attributes.put(name, value);
/*     */     try
/*     */     {
/* 113 */       new DocumentBuilderImpl(this, this.attributes, this.features);
/*     */     } catch (Exception e) {
/* 115 */       this.attributes.remove(name);
/* 116 */       throw new IllegalArgumentException(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String name)
/*     */     throws IllegalArgumentException
/*     */   {
/* 128 */     if (this.attributes != null) {
/* 129 */       Object val = this.attributes.get(name);
/* 130 */       if (val != null) {
/* 131 */         return val;
/*     */       }
/*     */     }
/*     */ 
/* 135 */     DOMParser domParser = null;
/*     */     try
/*     */     {
/* 139 */       domParser = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
/*     */ 
/* 141 */       return domParser.getProperty(name);
/*     */     }
/*     */     catch (SAXException se1) {
/*     */       try {
/* 145 */         boolean result = domParser.getFeature(name);
/*     */ 
/* 147 */         return result ? Boolean.TRUE : Boolean.FALSE;
/*     */       } catch (SAXException se2) {
/*     */       }
/* 150 */       throw new IllegalArgumentException(se1.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 156 */     return this.grammar;
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema grammar) {
/* 160 */     this.grammar = grammar;
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware() {
/* 164 */     return this.isXIncludeAware;
/*     */   }
/*     */ 
/*     */   public void setXIncludeAware(boolean state) {
/* 168 */     this.isXIncludeAware = state;
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name) throws ParserConfigurationException
/*     */   {
/* 173 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 174 */       return this.fSecureProcess;
/*     */     }
/*     */ 
/* 177 */     if (this.features != null) {
/* 178 */       Object val = this.features.get(name);
/* 179 */       if (val != null)
/* 180 */         return ((Boolean)val).booleanValue();
/*     */     }
/*     */     try
/*     */     {
/* 184 */       DOMParser domParser = new DocumentBuilderImpl(this, this.attributes, this.features).getDOMParser();
/* 185 */       return domParser.getFeature(name);
/*     */     }
/*     */     catch (SAXException e) {
/* 188 */       throw new ParserConfigurationException(e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value) throws ParserConfigurationException
/*     */   {
/* 194 */     if (this.features == null) {
/* 195 */       this.features = new Hashtable();
/*     */     }
/*     */ 
/* 198 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 199 */       if ((System.getSecurityManager() != null) && (!value)) {
/* 200 */         throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
/*     */       }
/*     */ 
/* 204 */       this.fSecureProcess = value;
/* 205 */       this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
/* 206 */       return;
/*     */     }
/*     */ 
/* 209 */     this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
/*     */     try
/*     */     {
/* 212 */       new DocumentBuilderImpl(this, this.attributes, this.features);
/*     */     }
/*     */     catch (SAXNotSupportedException e) {
/* 215 */       this.features.remove(name);
/* 216 */       throw new ParserConfigurationException(e.getMessage());
/*     */     }
/*     */     catch (SAXNotRecognizedException e) {
/* 219 */       this.features.remove(name);
/* 220 */       throw new ParserConfigurationException(e.getMessage());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl
 * JD-Core Version:    0.6.2
 */
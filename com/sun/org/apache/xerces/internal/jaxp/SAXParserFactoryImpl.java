/*     */ package com.sun.org.apache.xerces.internal.jaxp;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SAXMessageFormatter;
/*     */ import java.util.Hashtable;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class SAXParserFactoryImpl extends SAXParserFactory
/*     */ {
/*     */   private static final String VALIDATION_FEATURE = "http://xml.org/sax/features/validation";
/*     */   private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */   private static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";
/*     */   private Hashtable features;
/*     */   private Schema grammar;
/*     */   private boolean isXIncludeAware;
/*  69 */   private boolean fSecureProcess = true;
/*     */ 
/*     */   public SAXParser newSAXParser()
/*     */     throws ParserConfigurationException
/*     */   {
/*     */     SAXParser saxParserImpl;
/*     */     try
/*     */     {
/*  81 */       saxParserImpl = new SAXParserImpl(this, this.features, this.fSecureProcess);
/*     */     }
/*     */     catch (SAXException se) {
/*  84 */       throw new ParserConfigurationException(se.getMessage());
/*     */     }
/*  86 */     return saxParserImpl;
/*     */   }
/*     */ 
/*     */   private SAXParserImpl newSAXParserImpl()
/*     */     throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/*     */     SAXParserImpl saxParserImpl;
/*     */     try
/*     */     {
/*  98 */       saxParserImpl = new SAXParserImpl(this, this.features);
/*     */     } catch (SAXNotSupportedException e) {
/* 100 */       throw e;
/*     */     } catch (SAXNotRecognizedException e) {
/* 102 */       throw e;
/*     */     } catch (SAXException se) {
/* 104 */       throw new ParserConfigurationException(se.getMessage());
/*     */     }
/* 106 */     return saxParserImpl;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */     throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 116 */     if (name == null) {
/* 117 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 120 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 121 */       if ((System.getSecurityManager() != null) && (!value)) {
/* 122 */         throw new ParserConfigurationException(SAXMessageFormatter.formatMessage(null, "jaxp-secureprocessing-feature", null));
/*     */       }
/*     */ 
/* 126 */       this.fSecureProcess = value;
/* 127 */       putInFeatures(name, value);
/* 128 */       return;
/*     */     }
/*     */ 
/* 133 */     putInFeatures(name, value);
/*     */     try
/*     */     {
/* 136 */       newSAXParserImpl();
/*     */     } catch (SAXNotSupportedException e) {
/* 138 */       this.features.remove(name);
/* 139 */       throw e;
/*     */     } catch (SAXNotRecognizedException e) {
/* 141 */       this.features.remove(name);
/* 142 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean getFeature(String name)
/*     */     throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException
/*     */   {
/* 153 */     if (name == null) {
/* 154 */       throw new NullPointerException();
/*     */     }
/* 156 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing")) {
/* 157 */       return this.fSecureProcess;
/*     */     }
/*     */ 
/* 161 */     return newSAXParserImpl().getXMLReader().getFeature(name);
/*     */   }
/*     */ 
/*     */   public Schema getSchema() {
/* 165 */     return this.grammar;
/*     */   }
/*     */ 
/*     */   public void setSchema(Schema grammar) {
/* 169 */     this.grammar = grammar;
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware() {
/* 173 */     return getFromFeatures("http://apache.org/xml/features/xinclude");
/*     */   }
/*     */ 
/*     */   public void setXIncludeAware(boolean state) {
/* 177 */     putInFeatures("http://apache.org/xml/features/xinclude", state);
/*     */   }
/*     */ 
/*     */   public void setValidating(boolean validating)
/*     */   {
/* 182 */     putInFeatures("http://xml.org/sax/features/validation", validating);
/*     */   }
/*     */ 
/*     */   public boolean isValidating() {
/* 186 */     return getFromFeatures("http://xml.org/sax/features/validation");
/*     */   }
/*     */ 
/*     */   private void putInFeatures(String name, boolean value) {
/* 190 */     if (this.features == null) {
/* 191 */       this.features = new Hashtable();
/*     */     }
/* 193 */     this.features.put(name, value ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   private boolean getFromFeatures(String name) {
/* 197 */     if (this.features == null) {
/* 198 */       return false;
/*     */     }
/*     */ 
/* 201 */     Object value = this.features.get(name);
/* 202 */     return value == null ? false : Boolean.valueOf(value.toString()).booleanValue();
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceAware()
/*     */   {
/* 207 */     return getFromFeatures("http://xml.org/sax/features/namespaces");
/*     */   }
/*     */ 
/*     */   public void setNamespaceAware(boolean awareness) {
/* 211 */     putInFeatures("http://xml.org/sax/features/namespaces", awareness);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.FactoryImpl;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xalan.internal.utils.XMLSecurityManager.Limit;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.parsers.FactoryConfigurationError;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.XMLReaderFactory;
/*     */ 
/*     */ public class XMLReaderManager
/*     */ {
/*     */   private static final String NAMESPACES_FEATURE = "http://xml.org/sax/features/namespaces";
/*     */   private static final String NAMESPACE_PREFIXES_FEATURE = "http://xml.org/sax/features/namespace-prefixes";
/*  50 */   private static final XMLReaderManager m_singletonManager = new XMLReaderManager();
/*     */   private static final String property = "org.xml.sax.driver";
/*     */   private static SAXParserFactory m_parserFactory;
/*     */   private ThreadLocal m_readers;
/*     */   private HashMap m_inUse;
/*  68 */   private boolean m_useServicesMechanism = true;
/*     */   private boolean _secureProcessing;
/*  74 */   private String _accessExternalDTD = "all";
/*     */   private XMLSecurityManager _xmlSecurityManager;
/*     */ 
/*     */   public static XMLReaderManager getInstance(boolean useServicesMechanism)
/*     */   {
/*  88 */     m_singletonManager.setServicesMechnism(useServicesMechanism);
/*  89 */     return m_singletonManager;
/*     */   }
/*     */ 
/*     */   public synchronized XMLReader getXMLReader()
/*     */     throws SAXException
/*     */   {
/* 101 */     if (this.m_readers == null)
/*     */     {
/* 104 */       this.m_readers = new ThreadLocal();
/*     */     }
/*     */ 
/* 107 */     if (this.m_inUse == null) {
/* 108 */       this.m_inUse = new HashMap();
/*     */     }
/*     */ 
/* 114 */     XMLReader reader = (XMLReader)this.m_readers.get();
/* 115 */     boolean threadHasReader = reader != null;
/* 116 */     String factory = SecuritySupport.getSystemProperty("org.xml.sax.driver");
/* 117 */     if ((threadHasReader) && (this.m_inUse.get(reader) != Boolean.TRUE) && ((factory == null) || (reader.getClass().getName().equals(factory))))
/*     */     {
/* 119 */       this.m_inUse.put(reader, Boolean.TRUE);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/*     */         try
/*     */         {
/* 127 */           reader = XMLReaderFactory.createXMLReader();
/*     */           try {
/* 129 */             reader.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", this._secureProcessing);
/*     */           } catch (SAXNotRecognizedException e) {
/* 131 */             System.err.println("Warning:  " + reader.getClass().getName() + ": " + e.getMessage());
/*     */           }
/*     */         }
/*     */         catch (Exception e)
/*     */         {
/*     */           try
/*     */           {
/* 138 */             if (m_parserFactory == null) {
/* 139 */               m_parserFactory = FactoryImpl.getSAXFactory(this.m_useServicesMechanism);
/* 140 */               m_parserFactory.setNamespaceAware(true);
/*     */             }
/*     */ 
/* 143 */             reader = m_parserFactory.newSAXParser().getXMLReader();
/*     */           } catch (ParserConfigurationException pce) {
/* 145 */             throw pce;
/*     */           }
/*     */         }
/*     */         try {
/* 149 */           reader.setFeature("http://xml.org/sax/features/namespaces", true);
/* 150 */           reader.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
/*     */         }
/*     */         catch (SAXException se) {
/*     */         }
/*     */       }
/*     */       catch (ParserConfigurationException ex) {
/* 156 */         throw new SAXException(ex);
/*     */       } catch (FactoryConfigurationError ex1) {
/* 158 */         throw new SAXException(ex1.toString());
/*     */       }
/*     */       catch (NoSuchMethodError ex2)
/*     */       {
/*     */       }
/*     */       catch (AbstractMethodError ame) {
/*     */       }
/* 165 */       if (!threadHasReader) {
/* 166 */         this.m_readers.set(reader);
/* 167 */         this.m_inUse.put(reader, Boolean.TRUE);
/*     */       }
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 173 */       reader.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", this._accessExternalDTD);
/*     */     } catch (SAXException se) {
/* 175 */       System.err.println("Warning:  " + reader.getClass().getName() + ": " + se.getMessage());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 180 */       if (this._xmlSecurityManager != null) {
/* 181 */         for (XMLSecurityManager.Limit limit : XMLSecurityManager.Limit.values()) {
/* 182 */           reader.setProperty(limit.apiProperty(), this._xmlSecurityManager.getLimitValueAsString(limit));
/*     */         }
/*     */ 
/* 185 */         if (this._xmlSecurityManager.printEntityCountInfo())
/* 186 */           reader.setProperty("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo", "yes");
/*     */       }
/*     */     }
/*     */     catch (SAXException se) {
/* 190 */       System.err.println("Warning:  " + reader.getClass().getName() + ": " + se.getMessage());
/*     */     }
/*     */ 
/* 194 */     return reader;
/*     */   }
/*     */ 
/*     */   public synchronized void releaseXMLReader(XMLReader reader)
/*     */   {
/* 206 */     if ((this.m_readers.get() == reader) && (reader != null))
/* 207 */       this.m_inUse.remove(reader);
/*     */   }
/*     */ 
/*     */   public boolean useServicesMechnism()
/*     */   {
/* 214 */     return this.m_useServicesMechanism;
/*     */   }
/*     */ 
/*     */   public void setServicesMechnism(boolean flag)
/*     */   {
/* 221 */     this.m_useServicesMechanism = flag;
/*     */   }
/*     */ 
/*     */   public void setFeature(String name, boolean value)
/*     */   {
/* 228 */     if (name.equals("http://javax.xml.XMLConstants/feature/secure-processing"))
/* 229 */       this._secureProcessing = value;
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name)
/*     */   {
/* 237 */     if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD"))
/* 238 */       return this._accessExternalDTD;
/* 239 */     if (name.equals("http://apache.org/xml/properties/security-manager")) {
/* 240 */       return this._xmlSecurityManager;
/*     */     }
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value)
/*     */   {
/* 249 */     if (name.equals("http://javax.xml.XMLConstants/property/accessExternalDTD"))
/* 250 */       this._accessExternalDTD = ((String)value);
/* 251 */     else if (name.equals("http://apache.org/xml/properties/security-manager"))
/* 252 */       this._xmlSecurityManager = ((XMLSecurityManager)value);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.XMLReaderManager
 * JD-Core Version:    0.6.2
 */
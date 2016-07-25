/*     */ package com.sun.rowset.internal;
/*     */ 
/*     */ import com.sun.rowset.JdbcRowSetResourceBundle;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLException;
/*     */ import java.text.MessageFormat;
/*     */ import javax.sql.RowSetInternal;
/*     */ import javax.sql.rowset.WebRowSet;
/*     */ import javax.sql.rowset.spi.XmlReader;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ public class WebRowSetXmlReader
/*     */   implements XmlReader, Serializable
/*     */ {
/*     */   private JdbcRowSetResourceBundle resBundle;
/*     */   static final long serialVersionUID = -9127058392819008014L;
/*     */ 
/*     */   public WebRowSetXmlReader()
/*     */   {
/*     */     try
/*     */     {
/*  54 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/*  56 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readXML(WebRowSet paramWebRowSet, Reader paramReader)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/*  92 */       InputSource localInputSource = new InputSource(paramReader);
/*  93 */       localObject = new XmlErrorHandler();
/*  94 */       XmlReaderContentHandler localXmlReaderContentHandler = new XmlReaderContentHandler(paramWebRowSet);
/*  95 */       SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
/*  96 */       localSAXParserFactory.setNamespaceAware(true);
/*  97 */       localSAXParserFactory.setValidating(true);
/*  98 */       SAXParser localSAXParser = localSAXParserFactory.newSAXParser();
/*     */ 
/* 100 */       localSAXParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */ 
/* 103 */       XMLReader localXMLReader = localSAXParser.getXMLReader();
/* 104 */       localXMLReader.setEntityResolver(new XmlResolver());
/* 105 */       localXMLReader.setContentHandler(localXmlReaderContentHandler);
/*     */ 
/* 107 */       localXMLReader.setErrorHandler((ErrorHandler)localObject);
/*     */ 
/* 109 */       localXMLReader.parse(localInputSource);
/*     */     }
/*     */     catch (SAXParseException localSAXParseException) {
/* 112 */       System.out.println(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.parseerr").toString(), new Object[] { localSAXParseException.getMessage(), Integer.valueOf(localSAXParseException.getLineNumber()), localSAXParseException.getSystemId() }));
/* 113 */       localSAXParseException.printStackTrace();
/* 114 */       throw new SQLException(localSAXParseException.getMessage());
/*     */     }
/*     */     catch (SAXException localSAXException) {
/* 117 */       Object localObject = localSAXException;
/* 118 */       if (localSAXException.getException() != null)
/* 119 */         localObject = localSAXException.getException();
/* 120 */       ((Exception)localObject).printStackTrace();
/* 121 */       throw new SQLException(((Exception)localObject).getMessage());
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */     {
/* 128 */       throw new SQLException(this.resBundle.handleGetObject("wrsxmlreader.invalidcp").toString());
/*     */     }
/*     */     catch (Throwable localThrowable) {
/* 131 */       throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.readxml").toString(), new Object[] { localThrowable.getMessage() }));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readXML(WebRowSet paramWebRowSet, InputStream paramInputStream)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 164 */       InputSource localInputSource = new InputSource(paramInputStream);
/* 165 */       localObject = new XmlErrorHandler();
/*     */ 
/* 167 */       XmlReaderContentHandler localXmlReaderContentHandler = new XmlReaderContentHandler(paramWebRowSet);
/* 168 */       SAXParserFactory localSAXParserFactory = SAXParserFactory.newInstance();
/* 169 */       localSAXParserFactory.setNamespaceAware(true);
/* 170 */       localSAXParserFactory.setValidating(true);
/*     */ 
/* 172 */       SAXParser localSAXParser = localSAXParserFactory.newSAXParser();
/*     */ 
/* 174 */       localSAXParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
/*     */ 
/* 177 */       XMLReader localXMLReader = localSAXParser.getXMLReader();
/* 178 */       localXMLReader.setEntityResolver(new XmlResolver());
/* 179 */       localXMLReader.setContentHandler(localXmlReaderContentHandler);
/*     */ 
/* 181 */       localXMLReader.setErrorHandler((ErrorHandler)localObject);
/*     */ 
/* 183 */       localXMLReader.parse(localInputSource);
/*     */     }
/*     */     catch (SAXParseException localSAXParseException) {
/* 186 */       System.out.println(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.parseerr").toString(), new Object[] { Integer.valueOf(localSAXParseException.getLineNumber()), localSAXParseException.getSystemId() }));
/* 187 */       System.out.println("   " + localSAXParseException.getMessage());
/* 188 */       localSAXParseException.printStackTrace();
/* 189 */       throw new SQLException(localSAXParseException.getMessage());
/*     */     }
/*     */     catch (SAXException localSAXException) {
/* 192 */       Object localObject = localSAXException;
/* 193 */       if (localSAXException.getException() != null)
/* 194 */         localObject = localSAXException.getException();
/* 195 */       ((Exception)localObject).printStackTrace();
/* 196 */       throw new SQLException(((Exception)localObject).getMessage());
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */     {
/* 203 */       throw new SQLException(this.resBundle.handleGetObject("wrsxmlreader.invalidcp").toString());
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/* 207 */       throw new SQLException(MessageFormat.format(this.resBundle.handleGetObject("wrsxmlreader.readxml").toString(), new Object[] { localThrowable.getMessage() }));
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readData(RowSetInternal paramRowSetInternal)
/*     */   {
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 226 */     paramObjectInputStream.defaultReadObject();
/*     */     try
/*     */     {
/* 229 */       this.resBundle = JdbcRowSetResourceBundle.getJdbcRowSetResourceBundle();
/*     */     } catch (IOException localIOException) {
/* 231 */       throw new RuntimeException(localIOException);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.rowset.internal.WebRowSetXmlReader
 * JD-Core Version:    0.6.2
 */
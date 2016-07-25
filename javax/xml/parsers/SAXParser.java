/*     */ package javax.xml.parsers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.xml.sax.HandlerBase;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Parser;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.XMLReader;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public abstract class SAXParser
/*     */ {
/*     */   public void reset()
/*     */   {
/* 109 */     throw new UnsupportedOperationException("This SAXParser, \"" + getClass().getName() + "\", does not support the reset functionality." + "  Specification \"" + getClass().getPackage().getSpecificationTitle() + "\"" + " version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public void parse(InputStream is, HandlerBase hb)
/*     */     throws SAXException, IOException
/*     */   {
/* 134 */     if (is == null) {
/* 135 */       throw new IllegalArgumentException("InputStream cannot be null");
/*     */     }
/*     */ 
/* 138 */     InputSource input = new InputSource(is);
/* 139 */     parse(input, hb);
/*     */   }
/*     */ 
/*     */   public void parse(InputStream is, HandlerBase hb, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 165 */     if (is == null) {
/* 166 */       throw new IllegalArgumentException("InputStream cannot be null");
/*     */     }
/*     */ 
/* 169 */     InputSource input = new InputSource(is);
/* 170 */     input.setSystemId(systemId);
/* 171 */     parse(input, hb);
/*     */   }
/*     */ 
/*     */   public void parse(InputStream is, DefaultHandler dh)
/*     */     throws SAXException, IOException
/*     */   {
/* 190 */     if (is == null) {
/* 191 */       throw new IllegalArgumentException("InputStream cannot be null");
/*     */     }
/*     */ 
/* 194 */     InputSource input = new InputSource(is);
/* 195 */     parse(input, dh);
/*     */   }
/*     */ 
/*     */   public void parse(InputStream is, DefaultHandler dh, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 218 */     if (is == null) {
/* 219 */       throw new IllegalArgumentException("InputStream cannot be null");
/*     */     }
/*     */ 
/* 222 */     InputSource input = new InputSource(is);
/* 223 */     input.setSystemId(systemId);
/* 224 */     parse(input, dh);
/*     */   }
/*     */ 
/*     */   public void parse(String uri, HandlerBase hb)
/*     */     throws SAXException, IOException
/*     */   {
/* 245 */     if (uri == null) {
/* 246 */       throw new IllegalArgumentException("uri cannot be null");
/*     */     }
/*     */ 
/* 249 */     InputSource input = new InputSource(uri);
/* 250 */     parse(input, hb);
/*     */   }
/*     */ 
/*     */   public void parse(String uri, DefaultHandler dh)
/*     */     throws SAXException, IOException
/*     */   {
/* 269 */     if (uri == null) {
/* 270 */       throw new IllegalArgumentException("uri cannot be null");
/*     */     }
/*     */ 
/* 273 */     InputSource input = new InputSource(uri);
/* 274 */     parse(input, dh);
/*     */   }
/*     */ 
/*     */   public void parse(File f, HandlerBase hb)
/*     */     throws SAXException, IOException
/*     */   {
/* 294 */     if (f == null) {
/* 295 */       throw new IllegalArgumentException("File cannot be null");
/*     */     }
/*     */ 
/* 301 */     InputSource input = new InputSource(f.toURI().toASCIIString());
/* 302 */     parse(input, hb);
/*     */   }
/*     */ 
/*     */   public void parse(File f, DefaultHandler dh)
/*     */     throws SAXException, IOException
/*     */   {
/* 320 */     if (f == null) {
/* 321 */       throw new IllegalArgumentException("File cannot be null");
/*     */     }
/*     */ 
/* 327 */     InputSource input = new InputSource(f.toURI().toASCIIString());
/* 328 */     parse(input, dh);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource is, HandlerBase hb)
/*     */     throws SAXException, IOException
/*     */   {
/* 350 */     if (is == null) {
/* 351 */       throw new IllegalArgumentException("InputSource cannot be null");
/*     */     }
/*     */ 
/* 354 */     Parser parser = getParser();
/* 355 */     if (hb != null) {
/* 356 */       parser.setDocumentHandler(hb);
/* 357 */       parser.setEntityResolver(hb);
/* 358 */       parser.setErrorHandler(hb);
/* 359 */       parser.setDTDHandler(hb);
/*     */     }
/* 361 */     parser.parse(is);
/*     */   }
/*     */ 
/*     */   public void parse(InputSource is, DefaultHandler dh)
/*     */     throws SAXException, IOException
/*     */   {
/* 381 */     if (is == null) {
/* 382 */       throw new IllegalArgumentException("InputSource cannot be null");
/*     */     }
/*     */ 
/* 385 */     XMLReader reader = getXMLReader();
/* 386 */     if (dh != null) {
/* 387 */       reader.setContentHandler(dh);
/* 388 */       reader.setEntityResolver(dh);
/* 389 */       reader.setErrorHandler(dh);
/* 390 */       reader.setDTDHandler(dh);
/*     */     }
/* 392 */     reader.parse(is);
/*     */   }
/*     */ 
/*     */   public abstract Parser getParser()
/*     */     throws SAXException;
/*     */ 
/*     */   public abstract XMLReader getXMLReader()
/*     */     throws SAXException;
/*     */ 
/*     */   public abstract boolean isNamespaceAware();
/*     */ 
/*     */   public abstract boolean isValidating();
/*     */ 
/*     */   public abstract void setProperty(String paramString, Object paramObject)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException;
/*     */ 
/*     */   public abstract Object getProperty(String paramString)
/*     */     throws SAXNotRecognizedException, SAXNotSupportedException;
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 522 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware()
/*     */   {
/* 547 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.parsers.SAXParser
 * JD-Core Version:    0.6.2
 */
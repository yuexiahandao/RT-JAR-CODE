/*     */ package javax.xml.parsers;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import javax.xml.validation.Schema;
/*     */ import org.w3c.dom.DOMImplementation;
/*     */ import org.w3c.dom.Document;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class DocumentBuilder
/*     */ {
/*     */   public void reset()
/*     */   {
/*  89 */     throw new UnsupportedOperationException("This DocumentBuilder, \"" + getClass().getName() + "\", does not support the reset functionality." + "  Specification \"" + getClass().getPackage().getSpecificationTitle() + "\"" + " version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public Document parse(InputStream is)
/*     */     throws SAXException, IOException
/*     */   {
/* 116 */     if (is == null) {
/* 117 */       throw new IllegalArgumentException("InputStream cannot be null");
/*     */     }
/*     */ 
/* 120 */     InputSource in = new InputSource(is);
/* 121 */     return parse(in);
/*     */   }
/*     */ 
/*     */   public Document parse(InputStream is, String systemId)
/*     */     throws SAXException, IOException
/*     */   {
/* 144 */     if (is == null) {
/* 145 */       throw new IllegalArgumentException("InputStream cannot be null");
/*     */     }
/*     */ 
/* 148 */     InputSource in = new InputSource(is);
/* 149 */     in.setSystemId(systemId);
/* 150 */     return parse(in);
/*     */   }
/*     */ 
/*     */   public Document parse(String uri)
/*     */     throws SAXException, IOException
/*     */   {
/* 172 */     if (uri == null) {
/* 173 */       throw new IllegalArgumentException("URI cannot be null");
/*     */     }
/*     */ 
/* 176 */     InputSource in = new InputSource(uri);
/* 177 */     return parse(in);
/*     */   }
/*     */ 
/*     */   public Document parse(File f)
/*     */     throws SAXException, IOException
/*     */   {
/* 197 */     if (f == null) {
/* 198 */       throw new IllegalArgumentException("File cannot be null");
/*     */     }
/*     */ 
/* 204 */     InputSource in = new InputSource(f.toURI().toASCIIString());
/* 205 */     return parse(in);
/*     */   }
/*     */ 
/*     */   public abstract Document parse(InputSource paramInputSource)
/*     */     throws SAXException, IOException;
/*     */ 
/*     */   public abstract boolean isNamespaceAware();
/*     */ 
/*     */   public abstract boolean isValidating();
/*     */ 
/*     */   public abstract void setEntityResolver(EntityResolver paramEntityResolver);
/*     */ 
/*     */   public abstract void setErrorHandler(ErrorHandler paramErrorHandler);
/*     */ 
/*     */   public abstract Document newDocument();
/*     */ 
/*     */   public abstract DOMImplementation getDOMImplementation();
/*     */ 
/*     */   public Schema getSchema()
/*     */   {
/* 314 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ 
/*     */   public boolean isXIncludeAware()
/*     */   {
/* 340 */     throw new UnsupportedOperationException("This parser does not support specification \"" + getClass().getPackage().getSpecificationTitle() + "\" version \"" + getClass().getPackage().getSpecificationVersion() + "\"");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.parsers.DocumentBuilder
 * JD-Core Version:    0.6.2
 */
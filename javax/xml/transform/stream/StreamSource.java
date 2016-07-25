/*     */ package javax.xml.transform.stream;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URI;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public class StreamSource
/*     */   implements Source
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.stream.StreamSource/feature";
/*     */   private String publicId;
/*     */   private String systemId;
/*     */   private InputStream inputStream;
/*     */   private Reader reader;
/*     */ 
/*     */   public StreamSource()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StreamSource(InputStream inputStream)
/*     */   {
/*  78 */     setInputStream(inputStream);
/*     */   }
/*     */ 
/*     */   public StreamSource(InputStream inputStream, String systemId)
/*     */   {
/*  95 */     setInputStream(inputStream);
/*  96 */     setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public StreamSource(Reader reader)
/*     */   {
/* 110 */     setReader(reader);
/*     */   }
/*     */ 
/*     */   public StreamSource(Reader reader, String systemId)
/*     */   {
/* 125 */     setReader(reader);
/* 126 */     setSystemId(systemId);
/*     */   }
/*     */ 
/*     */   public StreamSource(String systemId)
/*     */   {
/* 135 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public StreamSource(File f)
/*     */   {
/* 147 */     setSystemId(f.toURI().toASCIIString());
/*     */   }
/*     */ 
/*     */   public void setInputStream(InputStream inputStream)
/*     */   {
/* 163 */     this.inputStream = inputStream;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */   {
/* 173 */     return this.inputStream;
/*     */   }
/*     */ 
/*     */   public void setReader(Reader reader)
/*     */   {
/* 187 */     this.reader = reader;
/*     */   }
/*     */ 
/*     */   public Reader getReader()
/*     */   {
/* 197 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public void setPublicId(String publicId)
/*     */   {
/* 210 */     this.publicId = publicId;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 220 */     return this.publicId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 236 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 246 */     return this.systemId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(File f)
/*     */   {
/* 258 */     this.systemId = f.toURI().toASCIIString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.stream.StreamSource
 * JD-Core Version:    0.6.2
 */
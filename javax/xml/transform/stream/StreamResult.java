/*     */ package javax.xml.transform.stream;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import javax.xml.transform.Result;
/*     */ 
/*     */ public class StreamResult
/*     */   implements Result
/*     */ {
/*     */   public static final String FEATURE = "http://javax.xml.transform.stream.StreamResult/feature";
/*     */   private String systemId;
/*     */   private OutputStream outputStream;
/*     */   private Writer writer;
/*     */ 
/*     */   public StreamResult()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StreamResult(OutputStream outputStream)
/*     */   {
/*  65 */     setOutputStream(outputStream);
/*     */   }
/*     */ 
/*     */   public StreamResult(Writer writer)
/*     */   {
/*  79 */     setWriter(writer);
/*     */   }
/*     */ 
/*     */   public StreamResult(String systemId)
/*     */   {
/*  88 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public StreamResult(File f)
/*     */   {
/* 100 */     setSystemId(f.toURI().toASCIIString());
/*     */   }
/*     */ 
/*     */   public void setOutputStream(OutputStream outputStream)
/*     */   {
/* 112 */     this.outputStream = outputStream;
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */   {
/* 122 */     return this.outputStream;
/*     */   }
/*     */ 
/*     */   public void setWriter(Writer writer)
/*     */   {
/* 136 */     this.writer = writer;
/*     */   }
/*     */ 
/*     */   public Writer getWriter()
/*     */   {
/* 146 */     return this.writer;
/*     */   }
/*     */ 
/*     */   public void setSystemId(String systemId)
/*     */   {
/* 157 */     this.systemId = systemId;
/*     */   }
/*     */ 
/*     */   public void setSystemId(File f)
/*     */   {
/* 170 */     this.systemId = f.toURI().toASCIIString();
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 180 */     return this.systemId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.stream.StreamResult
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.xml.internal.ws.encoding;
/*     */ 
/*     */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
/*     */ import com.sun.xml.internal.ws.developer.StreamingDataHandler;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public class MIMEPartStreamingDataHandler extends StreamingDataHandler
/*     */ {
/*     */   private final StreamingDataSource ds;
/*     */ 
/*     */   public MIMEPartStreamingDataHandler(MIMEPart part)
/*     */   {
/*  61 */     super(new StreamingDataSource(part));
/*  62 */     this.ds = ((StreamingDataSource)getDataSource());
/*     */   }
/*     */ 
/*     */   public InputStream readOnce() throws IOException {
/*  66 */     return this.ds.readOnce();
/*     */   }
/*     */ 
/*     */   public void moveTo(File file) throws IOException {
/*  70 */     this.ds.moveTo(file);
/*     */   }
/*     */ 
/*     */   public void close() throws IOException {
/*  74 */     this.ds.close();
/*     */   }
/*     */ 
/*     */   private static final class MyIOException extends IOException
/*     */   {
/*     */     private final Exception linkedException;
/*     */ 
/*     */     MyIOException(Exception linkedException)
/*     */     {
/* 121 */       this.linkedException = linkedException;
/*     */     }
/*     */ 
/*     */     public Throwable getCause()
/*     */     {
/* 126 */       return this.linkedException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StreamingDataSource
/*     */     implements DataSource
/*     */   {
/*     */     private final MIMEPart part;
/*     */ 
/*     */     StreamingDataSource(MIMEPart part)
/*     */     {
/*  81 */       this.part = part;
/*     */     }
/*     */ 
/*     */     public InputStream getInputStream() throws IOException {
/*  85 */       return this.part.read();
/*     */     }
/*     */ 
/*     */     InputStream readOnce() throws IOException {
/*     */       try {
/*  90 */         return this.part.readOnce();
/*     */       } catch (Exception e) {
/*  92 */         throw new MIMEPartStreamingDataHandler.MyIOException(e);
/*     */       }
/*     */     }
/*     */ 
/*     */     void moveTo(File file) throws IOException {
/*  97 */       this.part.moveTo(file);
/*     */     }
/*     */ 
/*     */     public OutputStream getOutputStream() throws IOException {
/* 101 */       return null;
/*     */     }
/*     */ 
/*     */     public String getContentType() {
/* 105 */       return this.part.getContentType();
/*     */     }
/*     */ 
/*     */     public String getName() {
/* 109 */       return "";
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 113 */       this.part.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.MIMEPartStreamingDataHandler
 * JD-Core Version:    0.6.2
 */
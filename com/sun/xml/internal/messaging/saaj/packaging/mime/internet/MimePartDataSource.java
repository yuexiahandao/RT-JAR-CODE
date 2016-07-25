/*     */ package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;
/*     */ 
/*     */ import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.UnknownServiceException;
/*     */ import javax.activation.DataSource;
/*     */ 
/*     */ public final class MimePartDataSource
/*     */   implements DataSource
/*     */ {
/*     */   private final MimeBodyPart part;
/*     */ 
/*     */   public MimePartDataSource(MimeBodyPart part)
/*     */   {
/*  55 */     this.part = part;
/*     */   }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  74 */       InputStream is = this.part.getContentStream();
/*     */ 
/*  76 */       String encoding = this.part.getEncoding();
/*  77 */       if (encoding != null) {
/*  78 */         return MimeUtility.decode(is, encoding);
/*     */       }
/*  80 */       return is;
/*     */     } catch (MessagingException mex) {
/*  82 */       throw new IOException(mex.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public OutputStream getOutputStream()
/*     */     throws IOException
/*     */   {
/*  92 */     throw new UnknownServiceException();
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 102 */     return this.part.getContentType();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*     */     try
/*     */     {
/* 112 */       return this.part.getFileName(); } catch (MessagingException mex) {
/*     */     }
/* 114 */     return "";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimePartDataSource
 * JD-Core Version:    0.6.2
 */
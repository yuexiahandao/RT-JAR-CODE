/*    */ package com.sun.xml.internal.messaging.saaj.soap;
/*    */ 
/*    */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ContentType;
/*    */ import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart;
/*    */ import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import javax.activation.ActivationDataFlavor;
/*    */ import javax.activation.DataContentHandler;
/*    */ import javax.activation.DataSource;
/*    */ 
/*    */ public class MultipartDataContentHandler
/*    */   implements DataContentHandler
/*    */ {
/* 36 */   private ActivationDataFlavor myDF = new ActivationDataFlavor(MimeMultipart.class, "multipart/mixed", "Multipart");
/*    */ 
/*    */   public DataFlavor[] getTransferDataFlavors()
/*    */   {
/* 47 */     return new DataFlavor[] { this.myDF };
/*    */   }
/*    */ 
/*    */   public Object getTransferData(DataFlavor df, DataSource ds)
/*    */   {
/* 60 */     if (this.myDF.equals(df)) {
/* 61 */       return getContent(ds);
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   public Object getContent(DataSource ds)
/*    */   {
/*    */     try
/*    */     {
/* 71 */       return new MimeMultipart(ds, new ContentType(ds.getContentType()));
/*    */     } catch (Exception e) {
/*    */     }
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   public void writeTo(Object obj, String mimeType, OutputStream os)
/*    */     throws IOException
/*    */   {
/* 83 */     if ((obj instanceof MimeMultipart))
/*    */     {
/*    */       try
/*    */       {
/* 87 */         ByteOutputStream baos = null;
/* 88 */         if ((os instanceof ByteOutputStream))
/* 89 */           baos = (ByteOutputStream)os;
/*    */         else {
/* 91 */           throw new IOException("Input Stream expected to be a com.sun.xml.internal.messaging.saaj.util.ByteOutputStream, but found " + os.getClass().getName());
/*    */         }
/*    */ 
/* 94 */         ((MimeMultipart)obj).writeTo(baos);
/*    */       } catch (Exception e) {
/* 96 */         throw new IOException(e.toString());
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.messaging.saaj.soap.MultipartDataContentHandler
 * JD-Core Version:    0.6.2
 */
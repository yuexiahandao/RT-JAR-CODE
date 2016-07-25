/*     */ package javax.xml.soap;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import javax.activation.DataHandler;
/*     */ 
/*     */ public abstract class AttachmentPart
/*     */ {
/*     */   public abstract int getSize()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract void clearContent();
/*     */ 
/*     */   public abstract Object getContent()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract InputStream getRawContent()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract byte[] getRawContentBytes()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract InputStream getBase64Content()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract void setContent(Object paramObject, String paramString);
/*     */ 
/*     */   public abstract void setRawContent(InputStream paramInputStream, String paramString)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract void setRawContentBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract void setBase64Content(InputStream paramInputStream, String paramString)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract DataHandler getDataHandler()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract void setDataHandler(DataHandler paramDataHandler);
/*     */ 
/*     */   public String getContentId()
/*     */   {
/* 356 */     String[] values = getMimeHeader("Content-ID");
/* 357 */     if ((values != null) && (values.length > 0))
/* 358 */       return values[0];
/* 359 */     return null;
/*     */   }
/*     */ 
/*     */   public String getContentLocation()
/*     */   {
/* 370 */     String[] values = getMimeHeader("Content-Location");
/* 371 */     if ((values != null) && (values.length > 0))
/* 372 */       return values[0];
/* 373 */     return null;
/*     */   }
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 384 */     String[] values = getMimeHeader("Content-Type");
/* 385 */     if ((values != null) && (values.length > 0))
/* 386 */       return values[0];
/* 387 */     return null;
/*     */   }
/*     */ 
/*     */   public void setContentId(String contentId)
/*     */   {
/* 402 */     setMimeHeader("Content-ID", contentId);
/*     */   }
/*     */ 
/*     */   public void setContentLocation(String contentLocation)
/*     */   {
/* 417 */     setMimeHeader("Content-Location", contentLocation);
/*     */   }
/*     */ 
/*     */   public void setContentType(String contentType)
/*     */   {
/* 431 */     setMimeHeader("Content-Type", contentType);
/*     */   }
/*     */ 
/*     */   public abstract void removeMimeHeader(String paramString);
/*     */ 
/*     */   public abstract void removeAllMimeHeaders();
/*     */ 
/*     */   public abstract String[] getMimeHeader(String paramString);
/*     */ 
/*     */   public abstract void setMimeHeader(String paramString1, String paramString2);
/*     */ 
/*     */   public abstract void addMimeHeader(String paramString1, String paramString2);
/*     */ 
/*     */   public abstract Iterator getAllMimeHeaders();
/*     */ 
/*     */   public abstract Iterator getMatchingMimeHeaders(String[] paramArrayOfString);
/*     */ 
/*     */   public abstract Iterator getNonMatchingMimeHeaders(String[] paramArrayOfString);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.AttachmentPart
 * JD-Core Version:    0.6.2
 */
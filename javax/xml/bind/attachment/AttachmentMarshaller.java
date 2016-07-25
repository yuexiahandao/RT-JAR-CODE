/*     */ package javax.xml.bind.attachment;
/*     */ 
/*     */ import javax.activation.DataHandler;
/*     */ 
/*     */ public abstract class AttachmentMarshaller
/*     */ {
/*     */   public abstract String addMtomAttachment(DataHandler paramDataHandler, String paramString1, String paramString2);
/*     */ 
/*     */   public abstract String addMtomAttachment(byte[] paramArrayOfByte, int paramInt1, int paramInt2, String paramString1, String paramString2, String paramString3);
/*     */ 
/*     */   public boolean isXOPPackage()
/*     */   {
/* 179 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract String addSwaRefAttachment(DataHandler paramDataHandler);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.attachment.AttachmentMarshaller
 * JD-Core Version:    0.6.2
 */
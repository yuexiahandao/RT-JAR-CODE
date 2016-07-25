/*     */ package javax.xml.soap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import javax.activation.DataHandler;
/*     */ 
/*     */ public abstract class SOAPMessage
/*     */ {
/*     */   public static final String CHARACTER_SET_ENCODING = "javax.xml.soap.character-set-encoding";
/*     */   public static final String WRITE_XML_DECLARATION = "javax.xml.soap.write-xml-declaration";
/*     */ 
/*     */   public abstract void setContentDescription(String paramString);
/*     */ 
/*     */   public abstract String getContentDescription();
/*     */ 
/*     */   public abstract SOAPPart getSOAPPart();
/*     */ 
/*     */   public SOAPBody getSOAPBody()
/*     */     throws SOAPException
/*     */   {
/* 169 */     throw new UnsupportedOperationException("getSOAPBody must be overridden by all subclasses of SOAPMessage");
/*     */   }
/*     */ 
/*     */   public SOAPHeader getSOAPHeader()
/*     */     throws SOAPException
/*     */   {
/* 184 */     throw new UnsupportedOperationException("getSOAPHeader must be overridden by all subclasses of SOAPMessage");
/*     */   }
/*     */ 
/*     */   public abstract void removeAllAttachments();
/*     */ 
/*     */   public abstract int countAttachments();
/*     */ 
/*     */   public abstract Iterator getAttachments();
/*     */ 
/*     */   public abstract Iterator getAttachments(MimeHeaders paramMimeHeaders);
/*     */ 
/*     */   public abstract void removeAttachments(MimeHeaders paramMimeHeaders);
/*     */ 
/*     */   public abstract AttachmentPart getAttachment(SOAPElement paramSOAPElement)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract void addAttachmentPart(AttachmentPart paramAttachmentPart);
/*     */ 
/*     */   public abstract AttachmentPart createAttachmentPart();
/*     */ 
/*     */   public AttachmentPart createAttachmentPart(DataHandler dataHandler)
/*     */   {
/* 306 */     AttachmentPart attachment = createAttachmentPart();
/* 307 */     attachment.setDataHandler(dataHandler);
/* 308 */     return attachment;
/*     */   }
/*     */ 
/*     */   public abstract MimeHeaders getMimeHeaders();
/*     */ 
/*     */   public AttachmentPart createAttachmentPart(Object content, String contentType)
/*     */   {
/* 345 */     AttachmentPart attachment = createAttachmentPart();
/* 346 */     attachment.setContent(content, contentType);
/* 347 */     return attachment;
/*     */   }
/*     */ 
/*     */   public abstract void saveChanges()
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract boolean saveRequired();
/*     */ 
/*     */   public abstract void writeTo(OutputStream paramOutputStream)
/*     */     throws SOAPException, IOException;
/*     */ 
/*     */   public void setProperty(String property, Object value)
/*     */     throws SOAPException
/*     */   {
/* 439 */     throw new UnsupportedOperationException("setProperty must be overridden by all subclasses of SOAPMessage");
/*     */   }
/*     */ 
/*     */   public Object getProperty(String property)
/*     */     throws SOAPException
/*     */   {
/* 454 */     throw new UnsupportedOperationException("getProperty must be overridden by all subclasses of SOAPMessage");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPMessage
 * JD-Core Version:    0.6.2
 */
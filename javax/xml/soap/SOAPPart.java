/*     */ package javax.xml.soap;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javax.xml.transform.Source;
/*     */ import org.w3c.dom.Document;
/*     */ 
/*     */ public abstract class SOAPPart
/*     */   implements Document, Node
/*     */ {
/*     */   public abstract SOAPEnvelope getEnvelope()
/*     */     throws SOAPException;
/*     */ 
/*     */   public String getContentId()
/*     */   {
/*  89 */     String[] values = getMimeHeader("Content-Id");
/*  90 */     if ((values != null) && (values.length > 0))
/*  91 */       return values[0];
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public String getContentLocation()
/*     */   {
/* 103 */     String[] values = getMimeHeader("Content-Location");
/* 104 */     if ((values != null) && (values.length > 0))
/* 105 */       return values[0];
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   public void setContentId(String contentId)
/*     */   {
/* 122 */     setMimeHeader("Content-Id", contentId);
/*     */   }
/*     */ 
/*     */   public void setContentLocation(String contentLocation)
/*     */   {
/* 137 */     setMimeHeader("Content-Location", contentLocation);
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
/*     */ 
/*     */   public abstract void setContent(Source paramSource)
/*     */     throws SOAPException;
/*     */ 
/*     */   public abstract Source getContent()
/*     */     throws SOAPException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.soap.SOAPPart
 * JD-Core Version:    0.6.2
 */
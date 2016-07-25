/*     */ package javax.xml.ws.soap;
/*     */ 
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public final class MTOMFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://www.w3.org/2004/08/soap/features/http-optimization";
/*  73 */   protected int threshold = 0;
/*     */ 
/*     */   public MTOMFeature()
/*     */   {
/*  81 */     this.enabled = true;
/*     */   }
/*     */ 
/*     */   public MTOMFeature(boolean enabled)
/*     */   {
/*  90 */     this.enabled = enabled;
/*     */   }
/*     */ 
/*     */   public MTOMFeature(int threshold)
/*     */   {
/* 104 */     if (threshold < 0)
/* 105 */       throw new WebServiceException("MTOMFeature.threshold must be >= 0, actual value: " + threshold);
/* 106 */     this.enabled = true;
/* 107 */     this.threshold = threshold;
/*     */   }
/*     */ 
/*     */   public MTOMFeature(boolean enabled, int threshold)
/*     */   {
/* 120 */     if (threshold < 0)
/* 121 */       throw new WebServiceException("MTOMFeature.threshold must be >= 0, actual value: " + threshold);
/* 122 */     this.enabled = enabled;
/* 123 */     this.threshold = threshold;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 130 */     return "http://www.w3.org/2004/08/soap/features/http-optimization";
/*     */   }
/*     */ 
/*     */   public int getThreshold()
/*     */   {
/* 140 */     return this.threshold;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.soap.MTOMFeature
 * JD-Core Version:    0.6.2
 */
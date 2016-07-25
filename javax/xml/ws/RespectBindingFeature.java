/*     */ package javax.xml.ws;
/*     */ 
/*     */ public final class RespectBindingFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "javax.xml.ws.RespectBindingFeature";
/*     */ 
/*     */   public RespectBindingFeature()
/*     */   {
/*  98 */     this.enabled = true;
/*     */   }
/*     */ 
/*     */   public RespectBindingFeature(boolean enabled)
/*     */   {
/* 108 */     this.enabled = enabled;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 115 */     return "javax.xml.ws.RespectBindingFeature";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.RespectBindingFeature
 * JD-Core Version:    0.6.2
 */
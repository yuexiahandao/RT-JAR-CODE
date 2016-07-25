/*     */ package javax.xml.ws.soap;
/*     */ 
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ public final class AddressingFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://www.w3.org/2005/08/addressing/module";
/*     */   protected boolean required;
/*     */   private final Responses responses;
/*     */ 
/*     */   public AddressingFeature()
/*     */   {
/* 191 */     this(true, false, Responses.ALL);
/*     */   }
/*     */ 
/*     */   public AddressingFeature(boolean enabled)
/*     */   {
/* 204 */     this(enabled, false, Responses.ALL);
/*     */   }
/*     */ 
/*     */   public AddressingFeature(boolean enabled, boolean required)
/*     */   {
/* 218 */     this(enabled, required, Responses.ALL);
/*     */   }
/*     */ 
/*     */   public AddressingFeature(boolean enabled, boolean required, Responses responses)
/*     */   {
/* 236 */     this.enabled = enabled;
/* 237 */     this.required = required;
/* 238 */     this.responses = responses;
/*     */   }
/*     */ 
/*     */   public String getID()
/*     */   {
/* 245 */     return "http://www.w3.org/2005/08/addressing/module";
/*     */   }
/*     */ 
/*     */   public boolean isRequired()
/*     */   {
/* 256 */     return this.required;
/*     */   }
/*     */ 
/*     */   public Responses getResponses()
/*     */   {
/* 275 */     return this.responses;
/*     */   }
/*     */ 
/*     */   public static enum Responses
/*     */   {
/* 165 */     ANONYMOUS, 
/*     */ 
/* 174 */     NON_ANONYMOUS, 
/*     */ 
/* 179 */     ALL;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.soap.AddressingFeature
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.xml.internal.ws.developer;
/*     */ 
/*     */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*     */ import com.sun.org.glassfish.gmbal.ManagedData;
/*     */ import com.sun.xml.internal.ws.api.FeatureConstructor;
/*     */ import com.sun.xml.internal.ws.server.DraconianValidationErrorHandler;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ @ManagedData
/*     */ public class SchemaValidationFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://jax-ws.dev.java.net/features/schema-validation";
/*     */   private final Class<? extends ValidationErrorHandler> clazz;
/*     */   private final boolean inbound;
/*     */   private final boolean outbound;
/*     */ 
/*     */   public SchemaValidationFeature()
/*     */   {
/*  55 */     this(true, true, DraconianValidationErrorHandler.class);
/*     */   }
/*     */ 
/*     */   public SchemaValidationFeature(Class<? extends ValidationErrorHandler> clazz)
/*     */   {
/*  63 */     this(true, true, clazz);
/*     */   }
/*     */ 
/*     */   public SchemaValidationFeature(boolean inbound, boolean outbound)
/*     */   {
/*  70 */     this(inbound, outbound, DraconianValidationErrorHandler.class);
/*     */   }
/*     */ 
/*     */   @FeatureConstructor({"inbound", "outbound", "handler"})
/*     */   public SchemaValidationFeature(boolean inbound, boolean outbound, Class<? extends ValidationErrorHandler> clazz)
/*     */   {
/*  78 */     this.enabled = true;
/*  79 */     this.inbound = inbound;
/*  80 */     this.outbound = outbound;
/*  81 */     this.clazz = clazz;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public String getID() {
/*  86 */     return "http://jax-ws.dev.java.net/features/schema-validation";
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public Class<? extends ValidationErrorHandler> getErrorHandler()
/*     */   {
/*  96 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public boolean isInbound()
/*     */   {
/* 105 */     return this.inbound;
/*     */   }
/*     */ 
/*     */   public boolean isOutbound()
/*     */   {
/* 114 */     return this.outbound;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.SchemaValidationFeature
 * JD-Core Version:    0.6.2
 */
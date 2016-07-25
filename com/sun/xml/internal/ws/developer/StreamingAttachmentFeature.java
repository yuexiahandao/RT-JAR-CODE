/*     */ package com.sun.xml.internal.ws.developer;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*     */ import com.sun.org.glassfish.gmbal.ManagedData;
/*     */ import com.sun.xml.internal.org.jvnet.mimepull.MIMEConfig;
/*     */ import com.sun.xml.internal.ws.api.FeatureConstructor;
/*     */ import javax.xml.ws.WebServiceFeature;
/*     */ 
/*     */ @ManagedData
/*     */ public final class StreamingAttachmentFeature extends WebServiceFeature
/*     */ {
/*     */   public static final String ID = "http://jax-ws.dev.java.net/features/mime";
/*     */   private MIMEConfig config;
/*     */   private String dir;
/*     */   private boolean parseEagerly;
/*     */   private long memoryThreshold;
/*     */ 
/*     */   public StreamingAttachmentFeature()
/*     */   {
/*     */   }
/*     */ 
/*     */   @FeatureConstructor({"dir", "parseEagerly", "memoryThreshold"})
/*     */   public StreamingAttachmentFeature(@Nullable String dir, boolean parseEagerly, long memoryThreshold)
/*     */   {
/*  73 */     this.enabled = true;
/*  74 */     this.dir = dir;
/*  75 */     this.parseEagerly = parseEagerly;
/*  76 */     this.memoryThreshold = memoryThreshold;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public String getID() {
/*  81 */     return "http://jax-ws.dev.java.net/features/mime";
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   public MIMEConfig getConfig()
/*     */   {
/*  92 */     if (this.config == null) {
/*  93 */       this.config = new MIMEConfig();
/*  94 */       this.config.setDir(this.dir);
/*  95 */       this.config.setParseEagerly(this.parseEagerly);
/*  96 */       this.config.setMemoryThreshold(this.memoryThreshold);
/*  97 */       this.config.validate();
/*     */     }
/*  99 */     return this.config;
/*     */   }
/*     */ 
/*     */   public void setDir(String dir)
/*     */   {
/* 106 */     this.dir = dir;
/*     */   }
/*     */ 
/*     */   public void setParseEagerly(boolean parseEagerly)
/*     */   {
/* 113 */     this.parseEagerly = parseEagerly;
/*     */   }
/*     */ 
/*     */   public void setMemoryThreshold(long memoryThreshold)
/*     */   {
/* 121 */     this.memoryThreshold = memoryThreshold;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.StreamingAttachmentFeature
 * JD-Core Version:    0.6.2
 */
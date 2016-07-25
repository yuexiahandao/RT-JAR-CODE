/*     */ package com.sun.xml.internal.ws.developer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.transform.Source;
/*     */ 
/*     */ public final class EPRRecipe
/*     */ {
/*  68 */   private final List<Header> referenceParameters = new ArrayList();
/*  69 */   private final List<Source> metadata = new ArrayList();
/*     */ 
/*     */   @NotNull
/*     */   public List<Header> getReferenceParameters()
/*     */   {
/*  75 */     return this.referenceParameters;
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   public List<Source> getMetadata()
/*     */   {
/*  82 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   public EPRRecipe addReferenceParameter(Header h)
/*     */   {
/*  89 */     if (h == null) throw new IllegalArgumentException();
/*  90 */     this.referenceParameters.add(h);
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */   public EPRRecipe addReferenceParameters(Header[] headers)
/*     */   {
/*  98 */     for (Header h : headers)
/*  99 */       addReferenceParameter(h);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */   public EPRRecipe addReferenceParameters(Iterable<? extends Header> headers)
/*     */   {
/* 107 */     for (Header h : headers)
/* 108 */       addReferenceParameter(h);
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */   public EPRRecipe addMetadata(Source source)
/*     */   {
/* 116 */     if (source == null) throw new IllegalArgumentException();
/* 117 */     this.metadata.add(source);
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   public EPRRecipe addMetadata(Source[] sources) {
/* 122 */     for (Source s : sources)
/* 123 */       addMetadata(s);
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   public EPRRecipe addMetadata(Iterable<? extends Source> sources) {
/* 128 */     for (Source s : sources)
/* 129 */       addMetadata(s);
/* 130 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.developer.EPRRecipe
 * JD-Core Version:    0.6.2
 */
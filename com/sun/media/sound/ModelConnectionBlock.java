/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class ModelConnectionBlock
/*     */ {
/*  42 */   private static final ModelSource[] no_sources = new ModelSource[0];
/*  43 */   private ModelSource[] sources = no_sources;
/*  44 */   private double scale = 1.0D;
/*     */   private ModelDestination destination;
/*     */ 
/*     */   public ModelConnectionBlock()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ModelConnectionBlock(double paramDouble, ModelDestination paramModelDestination)
/*     */   {
/*  51 */     this.scale = paramDouble;
/*  52 */     this.destination = paramModelDestination;
/*     */   }
/*     */ 
/*     */   public ModelConnectionBlock(ModelSource paramModelSource, ModelDestination paramModelDestination)
/*     */   {
/*  57 */     if (paramModelSource != null) {
/*  58 */       this.sources = new ModelSource[1];
/*  59 */       this.sources[0] = paramModelSource;
/*     */     }
/*  61 */     this.destination = paramModelDestination;
/*     */   }
/*     */ 
/*     */   public ModelConnectionBlock(ModelSource paramModelSource, double paramDouble, ModelDestination paramModelDestination)
/*     */   {
/*  66 */     if (paramModelSource != null) {
/*  67 */       this.sources = new ModelSource[1];
/*  68 */       this.sources[0] = paramModelSource;
/*     */     }
/*  70 */     this.scale = paramDouble;
/*  71 */     this.destination = paramModelDestination;
/*     */   }
/*     */ 
/*     */   public ModelConnectionBlock(ModelSource paramModelSource1, ModelSource paramModelSource2, ModelDestination paramModelDestination)
/*     */   {
/*  76 */     if (paramModelSource1 != null) {
/*  77 */       if (paramModelSource2 == null) {
/*  78 */         this.sources = new ModelSource[1];
/*  79 */         this.sources[0] = paramModelSource1;
/*     */       } else {
/*  81 */         this.sources = new ModelSource[2];
/*  82 */         this.sources[0] = paramModelSource1;
/*  83 */         this.sources[1] = paramModelSource2;
/*     */       }
/*     */     }
/*  86 */     this.destination = paramModelDestination;
/*     */   }
/*     */ 
/*     */   public ModelConnectionBlock(ModelSource paramModelSource1, ModelSource paramModelSource2, double paramDouble, ModelDestination paramModelDestination)
/*     */   {
/*  91 */     if (paramModelSource1 != null) {
/*  92 */       if (paramModelSource2 == null) {
/*  93 */         this.sources = new ModelSource[1];
/*  94 */         this.sources[0] = paramModelSource1;
/*     */       } else {
/*  96 */         this.sources = new ModelSource[2];
/*  97 */         this.sources[0] = paramModelSource1;
/*  98 */         this.sources[1] = paramModelSource2;
/*     */       }
/*     */     }
/* 101 */     this.scale = paramDouble;
/* 102 */     this.destination = paramModelDestination;
/*     */   }
/*     */ 
/*     */   public ModelDestination getDestination() {
/* 106 */     return this.destination;
/*     */   }
/*     */ 
/*     */   public void setDestination(ModelDestination paramModelDestination) {
/* 110 */     this.destination = paramModelDestination;
/*     */   }
/*     */ 
/*     */   public double getScale() {
/* 114 */     return this.scale;
/*     */   }
/*     */ 
/*     */   public void setScale(double paramDouble) {
/* 118 */     this.scale = paramDouble;
/*     */   }
/*     */ 
/*     */   public ModelSource[] getSources() {
/* 122 */     return (ModelSource[])Arrays.copyOf(this.sources, this.sources.length);
/*     */   }
/*     */ 
/*     */   public void setSources(ModelSource[] paramArrayOfModelSource) {
/* 126 */     this.sources = (paramArrayOfModelSource == null ? no_sources : (ModelSource[])Arrays.copyOf(paramArrayOfModelSource, paramArrayOfModelSource.length));
/*     */   }
/*     */ 
/*     */   public void addSource(ModelSource paramModelSource) {
/* 130 */     ModelSource[] arrayOfModelSource = this.sources;
/* 131 */     this.sources = new ModelSource[arrayOfModelSource.length + 1];
/* 132 */     System.arraycopy(arrayOfModelSource, 0, this.sources, 0, arrayOfModelSource.length);
/* 133 */     this.sources[(this.sources.length - 1)] = paramModelSource;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelConnectionBlock
 * JD-Core Version:    0.6.2
 */
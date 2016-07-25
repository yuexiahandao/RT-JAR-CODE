/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract interface Mixer extends Line
/*     */ {
/*     */   public abstract Info getMixerInfo();
/*     */ 
/*     */   public abstract Line.Info[] getSourceLineInfo();
/*     */ 
/*     */   public abstract Line.Info[] getTargetLineInfo();
/*     */ 
/*     */   public abstract Line.Info[] getSourceLineInfo(Line.Info paramInfo);
/*     */ 
/*     */   public abstract Line.Info[] getTargetLineInfo(Line.Info paramInfo);
/*     */ 
/*     */   public abstract boolean isLineSupported(Line.Info paramInfo);
/*     */ 
/*     */   public abstract Line getLine(Line.Info paramInfo)
/*     */     throws LineUnavailableException;
/*     */ 
/*     */   public abstract int getMaxLines(Line.Info paramInfo);
/*     */ 
/*     */   public abstract Line[] getSourceLines();
/*     */ 
/*     */   public abstract Line[] getTargetLines();
/*     */ 
/*     */   public abstract void synchronize(Line[] paramArrayOfLine, boolean paramBoolean);
/*     */ 
/*     */   public abstract void unsynchronize(Line[] paramArrayOfLine);
/*     */ 
/*     */   public abstract boolean isSynchronizationSupported(Line[] paramArrayOfLine, boolean paramBoolean);
/*     */ 
/*     */   public static class Info
/*     */   {
/*     */     private final String name;
/*     */     private final String vendor;
/*     */     private final String description;
/*     */     private final String version;
/*     */ 
/*     */     protected Info(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     {
/* 284 */       this.name = paramString1;
/* 285 */       this.vendor = paramString2;
/* 286 */       this.description = paramString3;
/* 287 */       this.version = paramString4;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 300 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 309 */       return super.hashCode();
/*     */     }
/*     */ 
/*     */     public final String getName()
/*     */     {
/* 317 */       return this.name;
/*     */     }
/*     */ 
/*     */     public final String getVendor()
/*     */     {
/* 325 */       return this.vendor;
/*     */     }
/*     */ 
/*     */     public final String getDescription()
/*     */     {
/* 333 */       return this.description;
/*     */     }
/*     */ 
/*     */     public final String getVersion()
/*     */     {
/* 341 */       return this.version;
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 349 */       return this.name + ", version " + this.version;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.Mixer
 * JD-Core Version:    0.6.2
 */
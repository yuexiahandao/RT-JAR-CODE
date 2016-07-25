/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract interface Port extends Line
/*     */ {
/*     */   public static class Info extends Line.Info
/*     */   {
/*  73 */     public static final Info MICROPHONE = new Info(Port.class, "MICROPHONE", true);
/*     */ 
/*  78 */     public static final Info LINE_IN = new Info(Port.class, "LINE_IN", true);
/*     */ 
/*  83 */     public static final Info COMPACT_DISC = new Info(Port.class, "COMPACT_DISC", true);
/*     */ 
/*  91 */     public static final Info SPEAKER = new Info(Port.class, "SPEAKER", false);
/*     */ 
/*  96 */     public static final Info HEADPHONE = new Info(Port.class, "HEADPHONE", false);
/*     */ 
/* 101 */     public static final Info LINE_OUT = new Info(Port.class, "LINE_OUT", false);
/*     */     private String name;
/*     */     private boolean isSource;
/*     */ 
/*     */     public Info(Class<?> paramClass, String paramString, boolean paramBoolean)
/*     */     {
/* 132 */       super();
/* 133 */       this.name = paramString;
/* 134 */       this.isSource = paramBoolean;
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 145 */       return this.name;
/*     */     }
/*     */ 
/*     */     public boolean isSource()
/*     */     {
/* 155 */       return this.isSource;
/*     */     }
/*     */ 
/*     */     public boolean matches(Line.Info paramInfo)
/*     */     {
/* 166 */       if (!super.matches(paramInfo)) {
/* 167 */         return false;
/*     */       }
/*     */ 
/* 170 */       if (!this.name.equals(((Info)paramInfo).getName())) {
/* 171 */         return false;
/*     */       }
/*     */ 
/* 174 */       if (this.isSource != ((Info)paramInfo).isSource()) {
/* 175 */         return false;
/*     */       }
/*     */ 
/* 178 */       return true;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 186 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 193 */       return super.hashCode();
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 204 */       return this.name + (this.isSource == true ? " source" : " target") + " port";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.Port
 * JD-Core Version:    0.6.2
 */
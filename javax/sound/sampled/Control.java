/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract class Control
/*     */ {
/*     */   private final Type type;
/*     */ 
/*     */   protected Control(Type paramType)
/*     */   {
/*  63 */     this.type = paramType;
/*     */   }
/*     */ 
/*     */   public Type getType()
/*     */   {
/*  74 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  85 */     return new String(getType() + " Control");
/*     */   }
/*     */ 
/*     */   public static class Type
/*     */   {
/*     */     private String name;
/*     */ 
/*     */     protected Type(String paramString)
/*     */     {
/* 115 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 125 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 132 */       return super.hashCode();
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 142 */       return this.name;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.Control
 * JD-Core Version:    0.6.2
 */
/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract class BooleanControl extends Control
/*     */ {
/*     */   private final String trueStateLabel;
/*     */   private final String falseStateLabel;
/*     */   private boolean value;
/*     */ 
/*     */   protected BooleanControl(Type paramType, boolean paramBoolean, String paramString1, String paramString2)
/*     */   {
/*  81 */     super(paramType);
/*  82 */     this.value = paramBoolean;
/*  83 */     this.trueStateLabel = paramString1;
/*  84 */     this.falseStateLabel = paramString2;
/*     */   }
/*     */ 
/*     */   protected BooleanControl(Type paramType, boolean paramBoolean)
/*     */   {
/*  97 */     this(paramType, paramBoolean, "true", "false");
/*     */   }
/*     */ 
/*     */   public void setValue(boolean paramBoolean)
/*     */   {
/* 112 */     this.value = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getValue()
/*     */   {
/* 122 */     return this.value;
/*     */   }
/*     */ 
/*     */   public String getStateLabel(boolean paramBoolean)
/*     */   {
/* 132 */     return paramBoolean == true ? this.trueStateLabel : this.falseStateLabel;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 145 */     return new String(super.toString() + " with current value: " + getStateLabel(getValue()));
/*     */   }
/*     */ 
/*     */   public static class Type extends Control.Type
/*     */   {
/* 170 */     public static final Type MUTE = new Type("Mute");
/*     */ 
/* 178 */     public static final Type APPLY_REVERB = new Type("Apply Reverb");
/*     */ 
/*     */     protected Type(String paramString)
/*     */     {
/* 189 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.BooleanControl
 * JD-Core Version:    0.6.2
 */
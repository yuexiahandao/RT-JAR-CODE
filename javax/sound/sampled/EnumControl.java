/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract class EnumControl extends Control
/*     */ {
/*     */   private Object[] values;
/*     */   private Object value;
/*     */ 
/*     */   protected EnumControl(Type paramType, Object[] paramArrayOfObject, Object paramObject)
/*     */   {
/*  86 */     super(paramType);
/*     */ 
/*  88 */     this.values = paramArrayOfObject;
/*  89 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public void setValue(Object paramObject)
/*     */   {
/* 108 */     if (!isValueSupported(paramObject)) {
/* 109 */       throw new IllegalArgumentException("Requested value " + paramObject + " is not supported.");
/*     */     }
/*     */ 
/* 112 */     this.value = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getValue()
/*     */   {
/* 121 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Object[] getValues()
/*     */   {
/* 131 */     Object[] arrayOfObject = new Object[this.values.length];
/*     */ 
/* 133 */     for (int i = 0; i < this.values.length; i++) {
/* 134 */       arrayOfObject[i] = this.values[i];
/*     */     }
/*     */ 
/* 137 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   private boolean isValueSupported(Object paramObject)
/*     */   {
/* 149 */     for (int i = 0; i < this.values.length; i++)
/*     */     {
/* 152 */       if (paramObject.equals(this.values[i])) {
/* 153 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 170 */     return new String(getType() + " with current value: " + getValue());
/*     */   }
/*     */ 
/*     */   public static class Type extends Control.Type
/*     */   {
/* 199 */     public static final Type REVERB = new Type("Reverb");
/*     */ 
/*     */     protected Type(String paramString)
/*     */     {
/* 210 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.EnumControl
 * JD-Core Version:    0.6.2
 */
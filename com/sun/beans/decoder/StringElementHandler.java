/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ public class StringElementHandler extends ElementHandler
/*     */ {
/*  54 */   private StringBuilder sb = new StringBuilder();
/*  55 */   private ValueObject value = ValueObjectImpl.NULL;
/*     */ 
/*     */   public final void addCharacter(char paramChar)
/*     */   {
/*  64 */     if (this.sb == null) {
/*  65 */       throw new IllegalStateException("Could not add chararcter to evaluated string element");
/*     */     }
/*  67 */     this.sb.append(paramChar);
/*     */   }
/*     */ 
/*     */   protected final void addArgument(Object paramObject)
/*     */   {
/*  77 */     if (this.sb == null) {
/*  78 */       throw new IllegalStateException("Could not add argument to evaluated string element");
/*     */     }
/*  80 */     this.sb.append(paramObject);
/*     */   }
/*     */ 
/*     */   protected final ValueObject getValueObject()
/*     */   {
/*  90 */     if (this.sb != null) {
/*     */       try {
/*  92 */         this.value = ValueObjectImpl.create(getValue(this.sb.toString()));
/*     */       }
/*     */       catch (RuntimeException localRuntimeException) {
/*  95 */         getOwner().handleException(localRuntimeException);
/*     */       }
/*     */       finally {
/*  98 */         this.sb = null;
/*     */       }
/*     */     }
/* 101 */     return this.value;
/*     */   }
/*     */ 
/*     */   protected Object getValue(String paramString)
/*     */   {
/* 114 */     return paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.StringElementHandler
 * JD-Core Version:    0.6.2
 */
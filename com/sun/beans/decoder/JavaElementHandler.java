/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import java.beans.XMLDecoder;
/*     */ 
/*     */ final class JavaElementHandler extends ElementHandler
/*     */ {
/*     */   private Class<?> type;
/*     */   private ValueObject value;
/*     */ 
/*     */   public void addAttribute(String paramString1, String paramString2)
/*     */   {
/*  72 */     if (!paramString1.equals("version"))
/*     */     {
/*  74 */       if (paramString1.equals("class"))
/*     */       {
/*  76 */         this.type = getOwner().findClass(paramString2);
/*     */       }
/*  78 */       else super.addAttribute(paramString1, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addArgument(Object paramObject)
/*     */   {
/*  89 */     getOwner().addObject(paramObject);
/*     */   }
/*     */ 
/*     */   protected boolean isArgument()
/*     */   {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   protected ValueObject getValueObject()
/*     */   {
/* 112 */     if (this.value == null) {
/* 113 */       this.value = ValueObjectImpl.create(getValue());
/*     */     }
/* 115 */     return this.value;
/*     */   }
/*     */ 
/*     */   private Object getValue()
/*     */   {
/* 125 */     Object localObject = getOwner().getOwner();
/* 126 */     if ((this.type == null) || (isValid(localObject))) {
/* 127 */       return localObject;
/*     */     }
/* 129 */     if ((localObject instanceof XMLDecoder)) {
/* 130 */       XMLDecoder localXMLDecoder = (XMLDecoder)localObject;
/* 131 */       localObject = localXMLDecoder.getOwner();
/* 132 */       if (isValid(localObject)) {
/* 133 */         return localObject;
/*     */       }
/*     */     }
/* 136 */     throw new IllegalStateException("Unexpected owner class: " + localObject.getClass().getName());
/*     */   }
/*     */ 
/*     */   private boolean isValid(Object paramObject)
/*     */   {
/* 149 */     return (paramObject == null) || (this.type.isInstance(paramObject));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.JavaElementHandler
 * JD-Core Version:    0.6.2
 */
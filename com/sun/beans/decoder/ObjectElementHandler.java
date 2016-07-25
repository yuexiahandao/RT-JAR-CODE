/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import java.beans.Expression;
/*     */ import java.util.Locale;
/*     */ 
/*     */ class ObjectElementHandler extends NewElementHandler
/*     */ {
/*     */   private String idref;
/*     */   private String field;
/*     */   private Integer index;
/*     */   private String property;
/*     */   private String method;
/*     */ 
/*     */   public final void addAttribute(String paramString1, String paramString2)
/*     */   {
/*  90 */     if (paramString1.equals("idref")) {
/*  91 */       this.idref = paramString2;
/*  92 */     } else if (paramString1.equals("field")) {
/*  93 */       this.field = paramString2;
/*  94 */     } else if (paramString1.equals("index")) {
/*  95 */       this.index = Integer.valueOf(paramString2);
/*  96 */       addArgument(this.index);
/*  97 */     } else if (paramString1.equals("property")) {
/*  98 */       this.property = paramString2;
/*  99 */     } else if (paramString1.equals("method")) {
/* 100 */       this.method = paramString2;
/*     */     } else {
/* 102 */       super.addAttribute(paramString1, paramString2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void startElement()
/*     */   {
/* 112 */     if ((this.field != null) || (this.idref != null))
/* 113 */       getValueObject();
/*     */   }
/*     */ 
/*     */   protected boolean isArgument()
/*     */   {
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */   protected final ValueObject getValueObject(Class<?> paramClass, Object[] paramArrayOfObject)
/*     */     throws Exception
/*     */   {
/* 140 */     if (this.field != null) {
/* 141 */       return ValueObjectImpl.create(FieldElementHandler.getFieldValue(getContextBean(), this.field));
/*     */     }
/* 143 */     if (this.idref != null) {
/* 144 */       return ValueObjectImpl.create(getVariable(this.idref));
/*     */     }
/* 146 */     Object localObject = getContextBean();
/*     */     String str;
/* 148 */     if (this.index != null) {
/* 149 */       str = paramArrayOfObject.length == 2 ? "set" : "get";
/*     */     }
/* 152 */     else if (this.property != null) {
/* 153 */       str = paramArrayOfObject.length == 1 ? "set" : "get";
/*     */ 
/* 157 */       if (0 < this.property.length())
/* 158 */         str = str + this.property.substring(0, 1).toUpperCase(Locale.ENGLISH) + this.property.substring(1);
/*     */     }
/*     */     else {
/* 161 */       str = (this.method != null) && (0 < this.method.length()) ? this.method : "new";
/*     */     }
/*     */ 
/* 165 */     Expression localExpression = new Expression(localObject, str, paramArrayOfObject);
/* 166 */     return ValueObjectImpl.create(localExpression.getValue());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.ObjectElementHandler
 * JD-Core Version:    0.6.2
 */
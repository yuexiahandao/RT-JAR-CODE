/*     */ package com.sun.org.apache.bcel.internal.generic;
/*     */ 
/*     */ public final class BasicType extends Type
/*     */ {
/*     */   BasicType(byte type)
/*     */   {
/*  75 */     super(type, com.sun.org.apache.bcel.internal.Constants.SHORT_TYPE_NAMES[type]);
/*     */ 
/*  77 */     if ((type < 4) || (type > 12))
/*  78 */       throw new ClassGenException("Invalid type: " + type);
/*     */   }
/*     */ 
/*     */   public static final BasicType getType(byte type) {
/*  82 */     switch (type) { case 12:
/*  83 */       return VOID;
/*     */     case 4:
/*  84 */       return BOOLEAN;
/*     */     case 8:
/*  85 */       return BYTE;
/*     */     case 9:
/*  86 */       return SHORT;
/*     */     case 5:
/*  87 */       return CHAR;
/*     */     case 10:
/*  88 */       return INT;
/*     */     case 11:
/*  89 */       return LONG;
/*     */     case 7:
/*  90 */       return DOUBLE;
/*     */     case 6:
/*  91 */       return FLOAT;
/*     */     }
/*     */ 
/*  94 */     throw new ClassGenException("Invalid type: " + type);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object type)
/*     */   {
/* 102 */     return ((BasicType)type).type == this.type;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 108 */     return this.type;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.generic.BasicType
 * JD-Core Version:    0.6.2
 */
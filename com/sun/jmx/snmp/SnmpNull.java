/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ public class SnmpNull extends SnmpValue
/*     */ {
/*     */   private static final long serialVersionUID = 1783782515994279177L;
/*     */   static final String name = "Null";
/* 182 */   private int tag = 5;
/*     */ 
/*     */   public SnmpNull()
/*     */   {
/*  46 */     this.tag = 5;
/*     */   }
/*     */ 
/*     */   public SnmpNull(String paramString)
/*     */   {
/*  54 */     this();
/*     */   }
/*     */ 
/*     */   public SnmpNull(int paramInt)
/*     */   {
/*  62 */     this.tag = paramInt;
/*     */   }
/*     */ 
/*     */   public int getTag()
/*     */   {
/*  72 */     return this.tag;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  82 */     String str = "";
/*  83 */     if (this.tag != 5) {
/*  84 */       str = str + "[" + this.tag + "] ";
/*     */     }
/*  86 */     str = str + "NULL";
/*  87 */     switch (this.tag) {
/*     */     case 128:
/*  89 */       str = str + " (noSuchObject)";
/*  90 */       break;
/*     */     case 129:
/*  93 */       str = str + " (noSuchInstance)";
/*  94 */       break;
/*     */     case 130:
/*  97 */       str = str + " (endOfMibView)";
/*     */     }
/*     */ 
/* 100 */     return str;
/*     */   }
/*     */ 
/*     */   public SnmpOid toOid()
/*     */   {
/* 110 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpValue duplicate()
/*     */   {
/* 119 */     return (SnmpValue)clone();
/*     */   }
/*     */ 
/*     */   public final synchronized Object clone()
/*     */   {
/* 127 */     SnmpNull localSnmpNull = null;
/*     */     try {
/* 129 */       localSnmpNull = (SnmpNull)super.clone();
/* 130 */       localSnmpNull.tag = this.tag;
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 132 */       throw new InternalError();
/*     */     }
/* 134 */     return localSnmpNull;
/*     */   }
/*     */ 
/*     */   public final String getTypeName()
/*     */   {
/* 142 */     return "Null";
/*     */   }
/*     */ 
/*     */   public boolean isNoSuchObjectValue()
/*     */   {
/* 151 */     return this.tag == 128;
/*     */   }
/*     */ 
/*     */   public boolean isNoSuchInstanceValue()
/*     */   {
/* 160 */     return this.tag == 129;
/*     */   }
/*     */ 
/*     */   public boolean isEndOfMibViewValue()
/*     */   {
/* 169 */     return this.tag == 130;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpNull
 * JD-Core Version:    0.6.2
 */
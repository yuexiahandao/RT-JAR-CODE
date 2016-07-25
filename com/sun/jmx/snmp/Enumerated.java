/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public abstract class Enumerated
/*     */   implements Serializable
/*     */ {
/*     */   protected int value;
/*     */ 
/*     */   public Enumerated()
/*     */     throws IllegalArgumentException
/*     */   {
/*  57 */     Enumeration localEnumeration = getIntTable().keys();
/*  58 */     if (localEnumeration.hasMoreElements()) {
/*  59 */       this.value = ((Integer)localEnumeration.nextElement()).intValue();
/*     */     }
/*     */     else
/*  62 */       throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public Enumerated(int paramInt)
/*     */     throws IllegalArgumentException
/*     */   {
/*  74 */     if (getIntTable().get(new Integer(paramInt)) == null) {
/*  75 */       throw new IllegalArgumentException();
/*     */     }
/*  77 */     this.value = paramInt;
/*     */   }
/*     */ 
/*     */   public Enumerated(Integer paramInteger)
/*     */     throws IllegalArgumentException
/*     */   {
/*  88 */     if (getIntTable().get(paramInteger) == null) {
/*  89 */       throw new IllegalArgumentException();
/*     */     }
/*  91 */     this.value = paramInteger.intValue();
/*     */   }
/*     */ 
/*     */   public Enumerated(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 103 */     Integer localInteger = (Integer)getStringTable().get(paramString);
/* 104 */     if (localInteger == null) {
/* 105 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/* 108 */     this.value = localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public int intValue()
/*     */   {
/* 120 */     return this.value;
/*     */   }
/*     */ 
/*     */   public Enumeration valueIndexes()
/*     */   {
/* 131 */     return getIntTable().keys();
/*     */   }
/*     */ 
/*     */   public Enumeration valueStrings()
/*     */   {
/* 142 */     return getStringTable().keys();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 158 */     return (paramObject != null) && (getClass() == paramObject.getClass()) && (this.value == ((Enumerated)paramObject).value);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 170 */     String str = getClass().getName() + String.valueOf(this.value);
/* 171 */     return str.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 182 */     return (String)getIntTable().get(new Integer(this.value));
/*     */   }
/*     */ 
/*     */   protected abstract Hashtable getIntTable();
/*     */ 
/*     */   protected abstract Hashtable getStringTable();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.Enumerated
 * JD-Core Version:    0.6.2
 */
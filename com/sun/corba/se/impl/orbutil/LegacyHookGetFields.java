/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class LegacyHookGetFields extends ObjectInputStream.GetField
/*     */ {
/*  34 */   private Hashtable fields = null;
/*     */ 
/*     */   LegacyHookGetFields(Hashtable paramHashtable) {
/*  37 */     this.fields = paramHashtable;
/*     */   }
/*     */ 
/*     */   public ObjectStreamClass getObjectStreamClass()
/*     */   {
/*  44 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean defaulted(String paramString)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/*  53 */     return !this.fields.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public boolean get(String paramString, boolean paramBoolean)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/*  61 */     if (defaulted(paramString))
/*  62 */       return paramBoolean;
/*  63 */     return ((Boolean)this.fields.get(paramString)).booleanValue();
/*     */   }
/*     */ 
/*     */   public char get(String paramString, char paramChar)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/*  71 */     if (defaulted(paramString))
/*  72 */       return paramChar;
/*  73 */     return ((Character)this.fields.get(paramString)).charValue();
/*     */   }
/*     */ 
/*     */   public byte get(String paramString, byte paramByte)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/*  82 */     if (defaulted(paramString))
/*  83 */       return paramByte;
/*  84 */     return ((Byte)this.fields.get(paramString)).byteValue();
/*     */   }
/*     */ 
/*     */   public short get(String paramString, short paramShort)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/*  93 */     if (defaulted(paramString))
/*  94 */       return paramShort;
/*  95 */     return ((Short)this.fields.get(paramString)).shortValue();
/*     */   }
/*     */ 
/*     */   public int get(String paramString, int paramInt)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 104 */     if (defaulted(paramString))
/* 105 */       return paramInt;
/* 106 */     return ((Integer)this.fields.get(paramString)).intValue();
/*     */   }
/*     */ 
/*     */   public long get(String paramString, long paramLong)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 115 */     if (defaulted(paramString))
/* 116 */       return paramLong;
/* 117 */     return ((Long)this.fields.get(paramString)).longValue();
/*     */   }
/*     */ 
/*     */   public float get(String paramString, float paramFloat)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 126 */     if (defaulted(paramString))
/* 127 */       return paramFloat;
/* 128 */     return ((Float)this.fields.get(paramString)).floatValue();
/*     */   }
/*     */ 
/*     */   public double get(String paramString, double paramDouble)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 137 */     if (defaulted(paramString))
/* 138 */       return paramDouble;
/* 139 */     return ((Double)this.fields.get(paramString)).doubleValue();
/*     */   }
/*     */ 
/*     */   public Object get(String paramString, Object paramObject)
/*     */     throws IOException, IllegalArgumentException
/*     */   {
/* 148 */     if (defaulted(paramString))
/* 149 */       return paramObject;
/* 150 */     return this.fields.get(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 155 */     return this.fields.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.LegacyHookGetFields
 * JD-Core Version:    0.6.2
 */
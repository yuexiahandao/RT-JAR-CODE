/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public abstract class ObjectWriter
/*     */ {
/*     */   protected StringBuffer result;
/*     */ 
/*     */   public static ObjectWriter make(boolean paramBoolean, int paramInt1, int paramInt2)
/*     */   {
/*  34 */     if (paramBoolean) {
/*  35 */       return new IndentingObjectWriter(paramInt1, paramInt2);
/*     */     }
/*  37 */     return new SimpleObjectWriter(null);
/*     */   }
/*     */   public abstract void startObject(Object paramObject);
/*     */ 
/*     */   public abstract void startElement();
/*     */ 
/*     */   public abstract void endElement();
/*     */ 
/*     */   public abstract void endObject(String paramString);
/*     */ 
/*     */   public abstract void endObject();
/*     */ 
/*     */   public String toString() {
/*  50 */     return this.result.toString();
/*     */   }
/*  52 */   public void append(boolean paramBoolean) { this.result.append(paramBoolean); } 
/*     */   public void append(char paramChar) {
/*  54 */     this.result.append(paramChar);
/*     */   }
/*  56 */   public void append(short paramShort) { this.result.append(paramShort); } 
/*     */   public void append(int paramInt) {
/*  58 */     this.result.append(paramInt);
/*     */   }
/*  60 */   public void append(long paramLong) { this.result.append(paramLong); } 
/*     */   public void append(float paramFloat) {
/*  62 */     this.result.append(paramFloat);
/*     */   }
/*  64 */   public void append(double paramDouble) { this.result.append(paramDouble); } 
/*     */   public void append(String paramString) {
/*  66 */     this.result.append(paramString);
/*     */   }
/*     */ 
/*     */   protected ObjectWriter()
/*     */   {
/*  76 */     this.result = new StringBuffer();
/*     */   }
/*     */ 
/*     */   protected void appendObjectHeader(Object paramObject)
/*     */   {
/*  81 */     this.result.append(paramObject.getClass().getName());
/*  82 */     this.result.append("<");
/*  83 */     this.result.append(System.identityHashCode(paramObject));
/*  84 */     this.result.append(">");
/*  85 */     Class localClass = paramObject.getClass().getComponentType();
/*     */ 
/*  87 */     if (localClass != null) {
/*  88 */       this.result.append("[");
/*     */       Object localObject;
/*  89 */       if (localClass == Boolean.TYPE) {
/*  90 */         localObject = (boolean[])paramObject;
/*  91 */         this.result.append(localObject.length);
/*  92 */         this.result.append("]");
/*  93 */       } else if (localClass == Byte.TYPE) {
/*  94 */         localObject = (byte[])paramObject;
/*  95 */         this.result.append(localObject.length);
/*  96 */         this.result.append("]");
/*  97 */       } else if (localClass == Short.TYPE) {
/*  98 */         localObject = (short[])paramObject;
/*  99 */         this.result.append(localObject.length);
/* 100 */         this.result.append("]");
/* 101 */       } else if (localClass == Integer.TYPE) {
/* 102 */         localObject = (int[])paramObject;
/* 103 */         this.result.append(localObject.length);
/* 104 */         this.result.append("]");
/* 105 */       } else if (localClass == Long.TYPE) {
/* 106 */         localObject = (long[])paramObject;
/* 107 */         this.result.append(localObject.length);
/* 108 */         this.result.append("]");
/* 109 */       } else if (localClass == Character.TYPE) {
/* 110 */         localObject = (char[])paramObject;
/* 111 */         this.result.append(localObject.length);
/* 112 */         this.result.append("]");
/* 113 */       } else if (localClass == Float.TYPE) {
/* 114 */         localObject = (float[])paramObject;
/* 115 */         this.result.append(localObject.length);
/* 116 */         this.result.append("]");
/* 117 */       } else if (localClass == Double.TYPE) {
/* 118 */         localObject = (double[])paramObject;
/* 119 */         this.result.append(localObject.length);
/* 120 */         this.result.append("]");
/*     */       } else {
/* 122 */         localObject = (Object[])paramObject;
/* 123 */         this.result.append(localObject.length);
/* 124 */         this.result.append("]");
/*     */       }
/*     */     }
/*     */ 
/* 128 */     this.result.append("(");
/*     */   }
/*     */ 
/*     */   private static class IndentingObjectWriter extends ObjectWriter
/*     */   {
/*     */     private int level;
/*     */     private int increment;
/*     */ 
/*     */     public IndentingObjectWriter(int paramInt1, int paramInt2)
/*     */     {
/* 145 */       this.level = paramInt1;
/* 146 */       this.increment = paramInt2;
/* 147 */       startLine();
/*     */     }
/*     */ 
/*     */     private void startLine()
/*     */     {
/* 152 */       char[] arrayOfChar = new char[this.level * this.increment];
/* 153 */       Arrays.fill(arrayOfChar, ' ');
/* 154 */       this.result.append(arrayOfChar);
/*     */     }
/*     */ 
/*     */     public void startObject(Object paramObject)
/*     */     {
/* 159 */       appendObjectHeader(paramObject);
/* 160 */       this.level += 1;
/*     */     }
/*     */ 
/*     */     public void startElement()
/*     */     {
/* 165 */       this.result.append("\n");
/* 166 */       startLine();
/*     */     }
/*     */ 
/*     */     public void endElement()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void endObject(String paramString)
/*     */     {
/* 175 */       this.level -= 1;
/* 176 */       this.result.append(paramString);
/* 177 */       this.result.append(")");
/*     */     }
/*     */ 
/*     */     public void endObject()
/*     */     {
/* 182 */       this.level -= 1;
/* 183 */       this.result.append("\n");
/* 184 */       startLine();
/* 185 */       this.result.append(")");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SimpleObjectWriter extends ObjectWriter
/*     */   {
/*     */     public void startObject(Object paramObject) {
/* 192 */       appendObjectHeader(paramObject);
/* 193 */       this.result.append(" ");
/*     */     }
/*     */ 
/*     */     public void startElement()
/*     */     {
/* 198 */       this.result.append(" ");
/*     */     }
/*     */ 
/*     */     public void endObject(String paramString)
/*     */     {
/* 203 */       this.result.append(paramString);
/* 204 */       this.result.append(")");
/*     */     }
/*     */ 
/*     */     public void endElement()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void endObject()
/*     */     {
/* 213 */       this.result.append(")");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.ObjectWriter
 * JD-Core Version:    0.6.2
 */
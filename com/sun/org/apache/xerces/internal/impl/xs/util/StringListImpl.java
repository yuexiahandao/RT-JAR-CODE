/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public final class StringListImpl extends AbstractList
/*     */   implements StringList
/*     */ {
/*  43 */   public static final StringListImpl EMPTY_LIST = new StringListImpl(new String[0], 0);
/*     */   private final String[] fArray;
/*     */   private final int fLength;
/*     */   private final Vector fVector;
/*     */ 
/*     */   public StringListImpl(Vector v)
/*     */   {
/*  55 */     this.fVector = v;
/*  56 */     this.fLength = (v == null ? 0 : v.size());
/*  57 */     this.fArray = null;
/*     */   }
/*     */ 
/*     */   public StringListImpl(String[] array, int length)
/*     */   {
/*  67 */     this.fArray = array;
/*  68 */     this.fLength = length;
/*  69 */     this.fVector = null;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  77 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public boolean contains(String item)
/*     */   {
/*  89 */     if (this.fVector != null) {
/*  90 */       return this.fVector.contains(item);
/*     */     }
/*  92 */     if (item == null) {
/*  93 */       for (int i = 0; i < this.fLength; i++) {
/*  94 */         if (this.fArray[i] == null)
/*  95 */           return true;
/*     */       }
/*     */     }
/*     */     else {
/*  99 */       for (int i = 0; i < this.fLength; i++) {
/* 100 */         if (item.equals(this.fArray[i]))
/* 101 */           return true;
/*     */       }
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   public String item(int index) {
/* 108 */     if ((index < 0) || (index >= this.fLength)) {
/* 109 */       return null;
/*     */     }
/* 111 */     if (this.fVector != null) {
/* 112 */       return (String)this.fVector.elementAt(index);
/*     */     }
/* 114 */     return this.fArray[index];
/*     */   }
/*     */ 
/*     */   public Object get(int index)
/*     */   {
/* 122 */     if ((index >= 0) && (index < this.fLength)) {
/* 123 */       if (this.fVector != null) {
/* 124 */         return this.fVector.elementAt(index);
/*     */       }
/* 126 */       return this.fArray[index];
/*     */     }
/* 128 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 132 */     return getLength();
/*     */   }
/*     */ 
/*     */   public Object[] toArray() {
/* 136 */     if (this.fVector != null) {
/* 137 */       return this.fVector.toArray();
/*     */     }
/* 139 */     Object[] a = new Object[this.fLength];
/* 140 */     toArray0(a);
/* 141 */     return a;
/*     */   }
/*     */ 
/*     */   public Object[] toArray(Object[] a) {
/* 145 */     if (this.fVector != null) {
/* 146 */       return this.fVector.toArray(a);
/*     */     }
/* 148 */     if (a.length < this.fLength) {
/* 149 */       Class arrayClass = a.getClass();
/* 150 */       Class componentType = arrayClass.getComponentType();
/* 151 */       a = (Object[])Array.newInstance(componentType, this.fLength);
/*     */     }
/* 153 */     toArray0(a);
/* 154 */     if (a.length > this.fLength) {
/* 155 */       a[this.fLength] = null;
/*     */     }
/* 157 */     return a;
/*     */   }
/*     */ 
/*     */   private void toArray0(Object[] a) {
/* 161 */     if (this.fLength > 0)
/* 162 */       System.arraycopy(this.fArray, 0, a, 0, this.fLength);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl
 * JD-Core Version:    0.6.2
 */
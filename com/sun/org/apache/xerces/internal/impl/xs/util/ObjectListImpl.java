/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractList;
/*     */ 
/*     */ public final class ObjectListImpl extends AbstractList
/*     */   implements ObjectList
/*     */ {
/*  41 */   public static final ObjectListImpl EMPTY_LIST = new ObjectListImpl(new Object[0], 0);
/*     */   private final Object[] fArray;
/*     */   private final int fLength;
/*     */ 
/*     */   public ObjectListImpl(Object[] array, int length)
/*     */   {
/*  50 */     this.fArray = array;
/*  51 */     this.fLength = length;
/*     */   }
/*     */ 
/*     */   public int getLength() {
/*  55 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object item) {
/*  59 */     if (item == null) {
/*  60 */       for (int i = 0; i < this.fLength; i++) {
/*  61 */         if (this.fArray[i] == null)
/*  62 */           return true;
/*     */       }
/*     */     }
/*     */     else {
/*  66 */       for (int i = 0; i < this.fLength; i++) {
/*  67 */         if (item.equals(this.fArray[i]))
/*  68 */           return true;
/*     */       }
/*     */     }
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   public Object item(int index) {
/*  75 */     if ((index < 0) || (index >= this.fLength)) {
/*  76 */       return null;
/*     */     }
/*  78 */     return this.fArray[index];
/*     */   }
/*     */ 
/*     */   public Object get(int index)
/*     */   {
/*  85 */     if ((index >= 0) && (index < this.fLength)) {
/*  86 */       return this.fArray[index];
/*     */     }
/*  88 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   public int size() {
/*  92 */     return getLength();
/*     */   }
/*     */ 
/*     */   public Object[] toArray() {
/*  96 */     Object[] a = new Object[this.fLength];
/*  97 */     toArray0(a);
/*  98 */     return a;
/*     */   }
/*     */ 
/*     */   public Object[] toArray(Object[] a) {
/* 102 */     if (a.length < this.fLength) {
/* 103 */       Class arrayClass = a.getClass();
/* 104 */       Class componentType = arrayClass.getComponentType();
/* 105 */       a = (Object[])Array.newInstance(componentType, this.fLength);
/*     */     }
/* 107 */     toArray0(a);
/* 108 */     if (a.length > this.fLength) {
/* 109 */       a[this.fLength] = null;
/*     */     }
/* 111 */     return a;
/*     */   }
/*     */ 
/*     */   private void toArray0(Object[] a) {
/* 115 */     if (this.fLength > 0)
/* 116 */       System.arraycopy(this.fArray, 0, a, 0, this.fLength);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.ObjectListImpl
 * JD-Core Version:    0.6.2
 */
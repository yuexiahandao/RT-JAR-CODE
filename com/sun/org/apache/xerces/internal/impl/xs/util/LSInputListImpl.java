/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.LSInputList;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractList;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ 
/*     */ public final class LSInputListImpl extends AbstractList
/*     */   implements LSInputList
/*     */ {
/*  44 */   public static final LSInputListImpl EMPTY_LIST = new LSInputListImpl(new LSInput[0], 0);
/*     */   private final LSInput[] fArray;
/*     */   private final int fLength;
/*     */ 
/*     */   public LSInputListImpl(LSInput[] array, int length)
/*     */   {
/*  58 */     this.fArray = array;
/*  59 */     this.fLength = length;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  67 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public LSInput item(int index)
/*     */   {
/*  80 */     if ((index < 0) || (index >= this.fLength)) {
/*  81 */       return null;
/*     */     }
/*  83 */     return this.fArray[index];
/*     */   }
/*     */ 
/*     */   public Object get(int index)
/*     */   {
/*  91 */     if ((index >= 0) && (index < this.fLength)) {
/*  92 */       return this.fArray[index];
/*     */     }
/*  94 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   public int size() {
/*  98 */     return getLength();
/*     */   }
/*     */ 
/*     */   public Object[] toArray() {
/* 102 */     Object[] a = new Object[this.fLength];
/* 103 */     toArray0(a);
/* 104 */     return a;
/*     */   }
/*     */ 
/*     */   public Object[] toArray(Object[] a) {
/* 108 */     if (a.length < this.fLength) {
/* 109 */       Class arrayClass = a.getClass();
/* 110 */       Class componentType = arrayClass.getComponentType();
/* 111 */       a = (Object[])Array.newInstance(componentType, this.fLength);
/*     */     }
/* 113 */     toArray0(a);
/* 114 */     if (a.length > this.fLength) {
/* 115 */       a[this.fLength] = null;
/*     */     }
/* 117 */     return a;
/*     */   }
/*     */ 
/*     */   private void toArray0(Object[] a) {
/* 121 */     if (this.fLength > 0)
/* 122 */       System.arraycopy(this.fArray, 0, a, 0, this.fLength);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.LSInputListImpl
 * JD-Core Version:    0.6.2
 */
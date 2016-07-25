/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.ShortList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSException;
/*     */ import java.util.AbstractList;
/*     */ 
/*     */ public final class ShortListImpl extends AbstractList
/*     */   implements ShortList
/*     */ {
/*  42 */   public static final ShortListImpl EMPTY_LIST = new ShortListImpl(new short[0], 0);
/*     */   private final short[] fArray;
/*     */   private final int fLength;
/*     */ 
/*     */   public ShortListImpl(short[] array, int length)
/*     */   {
/*  56 */     this.fArray = array;
/*  57 */     this.fLength = length;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/*  65 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public boolean contains(short item)
/*     */   {
/*  77 */     for (int i = 0; i < this.fLength; i++) {
/*  78 */       if (this.fArray[i] == item) {
/*  79 */         return true;
/*     */       }
/*     */     }
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   public short item(int index) throws XSException {
/*  86 */     if ((index < 0) || (index >= this.fLength)) {
/*  87 */       throw new XSException((short)2, null);
/*     */     }
/*  89 */     return this.fArray[index];
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj) {
/*  93 */     if ((obj == null) || (!(obj instanceof ShortList))) {
/*  94 */       return false;
/*     */     }
/*  96 */     ShortList rhs = (ShortList)obj;
/*     */ 
/*  98 */     if (this.fLength != rhs.getLength()) {
/*  99 */       return false;
/*     */     }
/* 101 */     for (int i = 0; i < this.fLength; i++) {
/* 102 */       if (this.fArray[i] != rhs.item(i)) {
/* 103 */         return false;
/*     */       }
/*     */     }
/* 106 */     return true;
/*     */   }
/*     */ 
/*     */   public Object get(int index)
/*     */   {
/* 114 */     if ((index >= 0) && (index < this.fLength)) {
/* 115 */       return new Short(this.fArray[index]);
/*     */     }
/* 117 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 121 */     return getLength();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.ShortListImpl
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.org.apache.xerces.internal.impl.dv.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
/*     */ import java.util.AbstractList;
/*     */ 
/*     */ public class ListDV extends TypeValidator
/*     */ {
/*     */   public short getAllowedFacets()
/*     */   {
/*  42 */     return 2079;
/*     */   }
/*     */ 
/*     */   public Object getActualValue(String content, ValidationContext context)
/*     */     throws InvalidDatatypeValueException
/*     */   {
/*  48 */     return content;
/*     */   }
/*     */ 
/*     */   public int getDataLength(Object value)
/*     */   {
/*  53 */     return ((ListData)value).getLength();
/*     */   }
/*     */   static final class ListData extends AbstractList implements ObjectList {
/*     */     final Object[] data;
/*     */     private String canonical;
/*     */ 
/*  60 */     public ListData(Object[] data) { this.data = data; }
/*     */ 
/*     */     public synchronized String toString() {
/*  63 */       if (this.canonical == null) {
/*  64 */         int len = this.data.length;
/*  65 */         StringBuffer buf = new StringBuffer();
/*  66 */         if (len > 0) {
/*  67 */           buf.append(this.data[0].toString());
/*     */         }
/*  69 */         for (int i = 1; i < len; i++) {
/*  70 */           buf.append(' ');
/*  71 */           buf.append(this.data[i].toString());
/*     */         }
/*  73 */         this.canonical = buf.toString();
/*     */       }
/*  75 */       return this.canonical;
/*     */     }
/*     */     public int getLength() {
/*  78 */       return this.data.length;
/*     */     }
/*     */     public boolean equals(Object obj) {
/*  81 */       if (!(obj instanceof ListData))
/*  82 */         return false;
/*  83 */       Object[] odata = ((ListData)obj).data;
/*     */ 
/*  85 */       int count = this.data.length;
/*  86 */       if (count != odata.length) {
/*  87 */         return false;
/*     */       }
/*  89 */       for (int i = 0; i < count; i++) {
/*  90 */         if (!this.data[i].equals(odata[i])) {
/*  91 */           return false;
/*     */         }
/*     */       }
/*     */ 
/*  95 */       return true;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/*  99 */       int hash = 0;
/* 100 */       for (int i = 0; i < this.data.length; i++) {
/* 101 */         hash ^= this.data[i].hashCode();
/*     */       }
/* 103 */       return hash;
/*     */     }
/*     */ 
/*     */     public boolean contains(Object item) {
/* 107 */       for (int i = 0; i < this.data.length; i++) {
/* 108 */         if (item == this.data[i]) {
/* 109 */           return true;
/*     */         }
/*     */       }
/* 112 */       return false;
/*     */     }
/*     */ 
/*     */     public Object item(int index) {
/* 116 */       if ((index < 0) || (index >= this.data.length)) {
/* 117 */         return null;
/*     */       }
/* 119 */       return this.data[index];
/*     */     }
/*     */ 
/*     */     public Object get(int index)
/*     */     {
/* 127 */       if ((index >= 0) && (index < this.data.length)) {
/* 128 */         return this.data[index];
/*     */       }
/* 130 */       throw new IndexOutOfBoundsException("Index: " + index);
/*     */     }
/*     */ 
/*     */     public int size() {
/* 134 */       return getLength();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.dv.xs.ListDV
 * JD-Core Version:    0.6.2
 */
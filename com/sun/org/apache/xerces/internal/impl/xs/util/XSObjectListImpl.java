/*     */ package com.sun.org.apache.xerces.internal.impl.xs.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObject;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class XSObjectListImpl extends AbstractList
/*     */   implements XSObjectList
/*     */ {
/*  46 */   public static final XSObjectListImpl EMPTY_LIST = new XSObjectListImpl(new XSObject[0], 0);
/*  47 */   private static final ListIterator EMPTY_ITERATOR = new ListIterator() {
/*     */     public boolean hasNext() {
/*  49 */       return false;
/*     */     }
/*     */     public Object next() {
/*  52 */       throw new NoSuchElementException();
/*     */     }
/*     */     public boolean hasPrevious() {
/*  55 */       return false;
/*     */     }
/*     */     public Object previous() {
/*  58 */       throw new NoSuchElementException();
/*     */     }
/*     */     public int nextIndex() {
/*  61 */       return 0;
/*     */     }
/*     */     public int previousIndex() {
/*  64 */       return -1;
/*     */     }
/*     */     public void remove() {
/*  67 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public void set(Object object) {
/*  70 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public void add(Object object) {
/*  73 */       throw new UnsupportedOperationException();
/*     */     }
/*  47 */   };
/*     */   private static final int DEFAULT_SIZE = 4;
/*  80 */   private XSObject[] fArray = null;
/*     */ 
/*  82 */   private int fLength = 0;
/*     */ 
/*     */   public XSObjectListImpl() {
/*  85 */     this.fArray = new XSObject[4];
/*  86 */     this.fLength = 0;
/*     */   }
/*     */ 
/*     */   public XSObjectListImpl(XSObject[] array, int length)
/*     */   {
/*  96 */     this.fArray = array;
/*  97 */     this.fLength = length;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 105 */     return this.fLength;
/*     */   }
/*     */ 
/*     */   public XSObject item(int index)
/*     */   {
/* 118 */     if ((index < 0) || (index >= this.fLength)) {
/* 119 */       return null;
/*     */     }
/* 121 */     return this.fArray[index];
/*     */   }
/*     */ 
/*     */   public void clearXSObjectList()
/*     */   {
/* 126 */     for (int i = 0; i < this.fLength; i++) {
/* 127 */       this.fArray[i] = null;
/*     */     }
/* 129 */     this.fArray = null;
/* 130 */     this.fLength = 0;
/*     */   }
/*     */ 
/*     */   public void addXSObject(XSObject object) {
/* 134 */     if (this.fLength == this.fArray.length) {
/* 135 */       XSObject[] temp = new XSObject[this.fLength + 4];
/* 136 */       System.arraycopy(this.fArray, 0, temp, 0, this.fLength);
/* 137 */       this.fArray = temp;
/*     */     }
/* 139 */     this.fArray[(this.fLength++)] = object;
/*     */   }
/*     */ 
/*     */   public void addXSObject(int index, XSObject object) {
/* 143 */     this.fArray[index] = object;
/*     */   }
/*     */ 
/*     */   public boolean contains(Object value)
/*     */   {
/* 151 */     return value == null ? containsNull() : containsObject(value);
/*     */   }
/*     */ 
/*     */   public Object get(int index) {
/* 155 */     if ((index >= 0) && (index < this.fLength)) {
/* 156 */       return this.fArray[index];
/*     */     }
/* 158 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 162 */     return getLength();
/*     */   }
/*     */ 
/*     */   public Iterator iterator() {
/* 166 */     return listIterator0(0);
/*     */   }
/*     */ 
/*     */   public ListIterator listIterator() {
/* 170 */     return listIterator0(0);
/*     */   }
/*     */ 
/*     */   public ListIterator listIterator(int index) {
/* 174 */     if ((index >= 0) && (index < this.fLength)) {
/* 175 */       return listIterator0(index);
/*     */     }
/* 177 */     throw new IndexOutOfBoundsException("Index: " + index);
/*     */   }
/*     */ 
/*     */   private ListIterator listIterator0(int index) {
/* 181 */     return this.fLength == 0 ? EMPTY_ITERATOR : new XSObjectListIterator(index);
/*     */   }
/*     */ 
/*     */   private boolean containsObject(Object value) {
/* 185 */     for (int i = this.fLength - 1; i >= 0; i--) {
/* 186 */       if (value.equals(this.fArray[i])) {
/* 187 */         return true;
/*     */       }
/*     */     }
/* 190 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean containsNull() {
/* 194 */     for (int i = this.fLength - 1; i >= 0; i--) {
/* 195 */       if (this.fArray[i] == null) {
/* 196 */         return true;
/*     */       }
/*     */     }
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */   public Object[] toArray() {
/* 203 */     Object[] a = new Object[this.fLength];
/* 204 */     toArray0(a);
/* 205 */     return a;
/*     */   }
/*     */ 
/*     */   public Object[] toArray(Object[] a) {
/* 209 */     if (a.length < this.fLength) {
/* 210 */       Class arrayClass = a.getClass();
/* 211 */       Class componentType = arrayClass.getComponentType();
/* 212 */       a = (Object[])Array.newInstance(componentType, this.fLength);
/*     */     }
/* 214 */     toArray0(a);
/* 215 */     if (a.length > this.fLength) {
/* 216 */       a[this.fLength] = null;
/*     */     }
/* 218 */     return a;
/*     */   }
/*     */ 
/*     */   private void toArray0(Object[] a) {
/* 222 */     if (this.fLength > 0)
/* 223 */       System.arraycopy(this.fArray, 0, a, 0, this.fLength);
/*     */   }
/*     */ 
/*     */   private final class XSObjectListIterator implements ListIterator {
/*     */     private int index;
/*     */ 
/*     */     public XSObjectListIterator(int index) {
/* 230 */       this.index = index;
/*     */     }
/*     */     public boolean hasNext() {
/* 233 */       return this.index < XSObjectListImpl.this.fLength;
/*     */     }
/*     */     public Object next() {
/* 236 */       if (this.index < XSObjectListImpl.this.fLength) {
/* 237 */         return XSObjectListImpl.this.fArray[(this.index++)];
/*     */       }
/* 239 */       throw new NoSuchElementException();
/*     */     }
/*     */     public boolean hasPrevious() {
/* 242 */       return this.index > 0;
/*     */     }
/*     */     public Object previous() {
/* 245 */       if (this.index > 0) {
/* 246 */         return XSObjectListImpl.this.fArray[(--this.index)];
/*     */       }
/* 248 */       throw new NoSuchElementException();
/*     */     }
/*     */     public int nextIndex() {
/* 251 */       return this.index;
/*     */     }
/*     */     public int previousIndex() {
/* 254 */       return this.index - 1;
/*     */     }
/*     */     public void remove() {
/* 257 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public void set(Object o) {
/* 260 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     public void add(Object o) {
/* 263 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl
 * JD-Core Version:    0.6.2
 */
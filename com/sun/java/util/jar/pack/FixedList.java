/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ 
/*     */ final class FixedList<E>
/*     */   implements List<E>
/*     */ {
/*     */   private final ArrayList<E> flist;
/*     */ 
/*     */   protected FixedList(int paramInt)
/*     */   {
/*  48 */     this.flist = new ArrayList(paramInt);
/*     */ 
/*  50 */     for (int i = 0; i < paramInt; i++)
/*  51 */       this.flist.add(null);
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  56 */     return this.flist.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  61 */     return this.flist.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean contains(Object paramObject)
/*     */   {
/*  66 */     return this.flist.contains(paramObject);
/*     */   }
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/*  71 */     return this.flist.iterator();
/*     */   }
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/*  76 */     return this.flist.toArray();
/*     */   }
/*     */ 
/*     */   public <T> T[] toArray(T[] paramArrayOfT)
/*     */   {
/*  81 */     return this.flist.toArray(paramArrayOfT);
/*     */   }
/*     */ 
/*     */   public boolean add(E paramE) throws UnsupportedOperationException
/*     */   {
/*  86 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public boolean remove(Object paramObject) throws UnsupportedOperationException
/*     */   {
/*  91 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public boolean containsAll(Collection<?> paramCollection)
/*     */   {
/*  96 */     return this.flist.containsAll(paramCollection);
/*     */   }
/*     */ 
/*     */   public boolean addAll(Collection<? extends E> paramCollection) throws UnsupportedOperationException
/*     */   {
/* 101 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public boolean addAll(int paramInt, Collection<? extends E> paramCollection) throws UnsupportedOperationException
/*     */   {
/* 106 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public boolean removeAll(Collection<?> paramCollection) throws UnsupportedOperationException
/*     */   {
/* 111 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public boolean retainAll(Collection<?> paramCollection) throws UnsupportedOperationException
/*     */   {
/* 116 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public void clear() throws UnsupportedOperationException
/*     */   {
/* 121 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public E get(int paramInt)
/*     */   {
/* 126 */     return this.flist.get(paramInt);
/*     */   }
/*     */ 
/*     */   public E set(int paramInt, E paramE)
/*     */   {
/* 131 */     return this.flist.set(paramInt, paramE);
/*     */   }
/*     */ 
/*     */   public void add(int paramInt, E paramE) throws UnsupportedOperationException
/*     */   {
/* 136 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public E remove(int paramInt) throws UnsupportedOperationException
/*     */   {
/* 141 */     throw new UnsupportedOperationException("operation not permitted");
/*     */   }
/*     */ 
/*     */   public int indexOf(Object paramObject)
/*     */   {
/* 146 */     return this.flist.indexOf(paramObject);
/*     */   }
/*     */ 
/*     */   public int lastIndexOf(Object paramObject)
/*     */   {
/* 151 */     return this.flist.lastIndexOf(paramObject);
/*     */   }
/*     */ 
/*     */   public ListIterator<E> listIterator()
/*     */   {
/* 156 */     return this.flist.listIterator();
/*     */   }
/*     */ 
/*     */   public ListIterator<E> listIterator(int paramInt)
/*     */   {
/* 161 */     return this.flist.listIterator(paramInt);
/*     */   }
/*     */ 
/*     */   public List<E> subList(int paramInt1, int paramInt2)
/*     */   {
/* 166 */     return this.flist.subList(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 171 */     return "FixedList{plist=" + this.flist + '}';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.FixedList
 * JD-Core Version:    0.6.2
 */
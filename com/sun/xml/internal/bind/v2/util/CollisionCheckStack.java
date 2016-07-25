/*     */ package com.sun.xml.internal.bind.v2.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class CollisionCheckStack<E> extends AbstractList<E>
/*     */ {
/*     */   private Object[] data;
/*     */   private int[] next;
/*  54 */   private int size = 0;
/*     */ 
/*  56 */   private boolean latestPushResult = false;
/*     */ 
/*  62 */   private boolean useIdentity = true;
/*     */   private final int[] initialHash;
/*     */ 
/*     */   public CollisionCheckStack()
/*     */   {
/*  69 */     this.initialHash = new int[17];
/*  70 */     this.data = new Object[16];
/*  71 */     this.next = new int[16];
/*     */   }
/*     */ 
/*     */   public void setUseIdentity(boolean useIdentity)
/*     */   {
/*  79 */     this.useIdentity = useIdentity;
/*     */   }
/*     */ 
/*     */   public boolean getUseIdentity() {
/*  83 */     return this.useIdentity;
/*     */   }
/*     */ 
/*     */   public boolean getLatestPushResult() {
/*  87 */     return this.latestPushResult;
/*     */   }
/*     */ 
/*     */   public boolean push(E o)
/*     */   {
/*  97 */     if (this.data.length == this.size) {
/*  98 */       expandCapacity();
/*     */     }
/* 100 */     this.data[this.size] = o;
/* 101 */     int hash = hash(o);
/* 102 */     boolean r = findDuplicate(o, hash);
/* 103 */     this.next[this.size] = this.initialHash[hash];
/* 104 */     this.initialHash[hash] = (this.size + 1);
/* 105 */     this.size += 1;
/* 106 */     this.latestPushResult = r;
/* 107 */     return this.latestPushResult;
/*     */   }
/*     */ 
/*     */   public void pushNocheck(E o)
/*     */   {
/* 115 */     if (this.data.length == this.size)
/* 116 */       expandCapacity();
/* 117 */     this.data[this.size] = o;
/* 118 */     this.next[this.size] = -1;
/* 119 */     this.size += 1;
/*     */   }
/*     */ 
/*     */   public boolean findDuplicate(E o) {
/* 123 */     int hash = hash(o);
/* 124 */     return findDuplicate(o, hash);
/*     */   }
/*     */ 
/*     */   public E get(int index)
/*     */   {
/* 129 */     return this.data[index];
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 134 */     return this.size;
/*     */   }
/*     */ 
/*     */   private int hash(Object o) {
/* 138 */     return ((this.useIdentity ? System.identityHashCode(o) : o.hashCode()) & 0x7FFFFFFF) % this.initialHash.length;
/*     */   }
/*     */ 
/*     */   public E pop()
/*     */   {
/* 145 */     this.size -= 1;
/* 146 */     Object o = this.data[this.size];
/* 147 */     this.data[this.size] = null;
/* 148 */     int n = this.next[this.size];
/* 149 */     if (n >= 0)
/*     */     {
/* 152 */       int hash = hash(o);
/* 153 */       assert (this.initialHash[hash] == this.size + 1);
/* 154 */       this.initialHash[hash] = n;
/*     */     }
/* 156 */     return o;
/*     */   }
/*     */ 
/*     */   public E peek()
/*     */   {
/* 163 */     return this.data[(this.size - 1)];
/*     */   }
/*     */ 
/*     */   private boolean findDuplicate(E o, int hash) {
/* 167 */     int p = this.initialHash[hash];
/* 168 */     while (p != 0) {
/* 169 */       p--;
/* 170 */       Object existing = this.data[p];
/* 171 */       if (this.useIdentity) {
/* 172 */         if (existing == o) return true;
/*     */       }
/* 174 */       else if (o.equals(existing)) return true;
/*     */ 
/* 176 */       p = this.next[p];
/*     */     }
/* 178 */     return false;
/*     */   }
/*     */ 
/*     */   private void expandCapacity() {
/* 182 */     int oldSize = this.data.length;
/* 183 */     int newSize = oldSize * 2;
/* 184 */     Object[] d = new Object[newSize];
/* 185 */     int[] n = new int[newSize];
/*     */ 
/* 187 */     System.arraycopy(this.data, 0, d, 0, oldSize);
/* 188 */     System.arraycopy(this.next, 0, n, 0, oldSize);
/*     */ 
/* 190 */     this.data = d;
/* 191 */     this.next = n;
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 198 */     if (this.size > 0) {
/* 199 */       this.size = 0;
/* 200 */       Arrays.fill(this.initialHash, 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getCycleString() {
/* 208 */     StringBuilder sb = new StringBuilder();
/* 209 */     int i = size() - 1;
/* 210 */     Object obj = get(i);
/* 211 */     sb.append(obj);
/*     */     Object x;
/*     */     do { sb.append(" -> ");
/* 215 */       x = get(--i);
/* 216 */       sb.append(x); }
/* 217 */     while (obj != x);
/*     */ 
/* 219 */     return sb.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.CollisionCheckStack
 * JD-Core Version:    0.6.2
 */
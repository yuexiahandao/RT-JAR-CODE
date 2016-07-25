/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
/*     */ 
/*     */ public class DuplicateAttributeVerifier
/*     */ {
/*     */   public static final int MAP_SIZE = 256;
/*     */   public int _currentIteration;
/*     */   private Entry[] _map;
/*     */   public final Entry _poolHead;
/*     */   public Entry _poolCurrent;
/*     */   private Entry _poolTail;
/*     */ 
/*     */   public DuplicateAttributeVerifier()
/*     */   {
/*  56 */     this._poolTail = (this._poolHead = new Entry());
/*     */   }
/*     */ 
/*     */   public final void clear() {
/*  60 */     this._currentIteration = 0;
/*     */ 
/*  62 */     Entry e = this._poolHead;
/*  63 */     while (e != null) {
/*  64 */       e.iteration = 0;
/*  65 */       e = e.poolNext;
/*     */     }
/*     */ 
/*  68 */     reset();
/*     */   }
/*     */ 
/*     */   public final void reset() {
/*  72 */     this._poolCurrent = this._poolHead;
/*  73 */     if (this._map == null)
/*  74 */       this._map = new Entry[256];
/*     */   }
/*     */ 
/*     */   private final void increasePool(int capacity)
/*     */   {
/*  79 */     if (this._map == null) {
/*  80 */       this._map = new Entry[256];
/*  81 */       this._poolCurrent = this._poolHead;
/*     */     } else {
/*  83 */       Entry tail = this._poolTail;
/*  84 */       for (int i = 0; i < capacity; i++) {
/*  85 */         Entry e = new Entry();
/*  86 */         this._poolTail.poolNext = e;
/*  87 */         this._poolTail = e;
/*     */       }
/*     */ 
/*  90 */       this._poolCurrent = tail.poolNext;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void checkForDuplicateAttribute(int hash, int value) throws FastInfosetException {
/*  95 */     if (this._poolCurrent == null) {
/*  96 */       increasePool(16);
/*     */     }
/*     */ 
/* 100 */     Entry newEntry = this._poolCurrent;
/* 101 */     this._poolCurrent = this._poolCurrent.poolNext;
/*     */ 
/* 103 */     Entry head = this._map[hash];
/* 104 */     if ((head == null) || (head.iteration < this._currentIteration)) {
/* 105 */       newEntry.hashNext = null;
/* 106 */       this._map[hash] = newEntry;
/* 107 */       newEntry.iteration = this._currentIteration;
/* 108 */       newEntry.value = value;
/*     */     } else {
/* 110 */       Entry e = head;
/*     */       do
/* 112 */         if (e.value == value) {
/* 113 */           reset();
/* 114 */           throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateAttribute"));
/*     */         }
/* 116 */       while ((e = e.hashNext) != null);
/*     */ 
/* 118 */       newEntry.hashNext = head;
/* 119 */       this._map[hash] = newEntry;
/* 120 */       newEntry.iteration = this._currentIteration;
/* 121 */       newEntry.value = value;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     private int iteration;
/*     */     private int value;
/*     */     private Entry hashNext;
/*     */     private Entry poolNext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.DuplicateAttributeVerifier
 * JD-Core Version:    0.6.2
 */
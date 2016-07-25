/*     */ package com.sun.org.apache.xerces.internal.jaxp.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLSchemaDescription;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ 
/*     */ final class SoftReferenceGrammarPool
/*     */   implements XMLGrammarPool
/*     */ {
/*     */   protected static final int TABLE_SIZE = 11;
/*  51 */   protected static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
/*     */ 
/*  58 */   protected Entry[] fGrammars = null;
/*     */   protected boolean fPoolIsLocked;
/*  64 */   protected int fGrammarCount = 0;
/*     */ 
/*  67 */   protected final ReferenceQueue fReferenceQueue = new ReferenceQueue();
/*     */ 
/*     */   public SoftReferenceGrammarPool()
/*     */   {
/*  75 */     this.fGrammars = new Entry[11];
/*  76 */     this.fPoolIsLocked = false;
/*     */   }
/*     */ 
/*     */   public SoftReferenceGrammarPool(int initialCapacity)
/*     */   {
/*  81 */     this.fGrammars = new Entry[initialCapacity];
/*  82 */     this.fPoolIsLocked = false;
/*     */   }
/*     */ 
/*     */   public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*     */   {
/* 100 */     synchronized (this.fGrammars) {
/* 101 */       clean();
/*     */ 
/* 106 */       return ZERO_LENGTH_GRAMMAR_ARRAY;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cacheGrammars(String grammarType, Grammar[] grammars)
/*     */   {
/* 122 */     if (!this.fPoolIsLocked)
/* 123 */       for (int i = 0; i < grammars.length; i++)
/* 124 */         putGrammar(grammars[i]);
/*     */   }
/*     */ 
/*     */   public Grammar retrieveGrammar(XMLGrammarDescription desc)
/*     */   {
/* 144 */     return getGrammar(desc);
/*     */   }
/*     */ 
/*     */   public void putGrammar(Grammar grammar)
/*     */   {
/* 158 */     if (!this.fPoolIsLocked)
/* 159 */       synchronized (this.fGrammars) {
/* 160 */         clean();
/* 161 */         XMLGrammarDescription desc = grammar.getGrammarDescription();
/* 162 */         int hash = hashCode(desc);
/* 163 */         int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 164 */         for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 165 */           if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 166 */             if (entry.grammar.get() != grammar) {
/* 167 */               entry.grammar = new SoftGrammarReference(entry, grammar, this.fReferenceQueue);
/*     */             }
/* 169 */             return;
/*     */           }
/*     */         }
/*     */ 
/* 173 */         Entry entry = new Entry(hash, index, desc, grammar, this.fGrammars[index], this.fReferenceQueue);
/* 174 */         this.fGrammars[index] = entry;
/* 175 */         this.fGrammarCount += 1;
/*     */       }
/*     */   }
/*     */ 
/*     */   public Grammar getGrammar(XMLGrammarDescription desc)
/*     */   {
/* 188 */     synchronized (this.fGrammars) {
/* 189 */       clean();
/* 190 */       int hash = hashCode(desc);
/* 191 */       int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 192 */       for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 193 */         Grammar tempGrammar = (Grammar)entry.grammar.get();
/*     */ 
/* 195 */         if (tempGrammar == null) {
/* 196 */           removeEntry(entry);
/*     */         }
/* 198 */         else if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 199 */           return tempGrammar;
/*     */         }
/*     */       }
/* 202 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Grammar removeGrammar(XMLGrammarDescription desc)
/*     */   {
/* 216 */     synchronized (this.fGrammars) {
/* 217 */       clean();
/* 218 */       int hash = hashCode(desc);
/* 219 */       int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 220 */       for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 221 */         if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 222 */           return removeEntry(entry);
/*     */         }
/*     */       }
/* 225 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean containsGrammar(XMLGrammarDescription desc)
/*     */   {
/* 238 */     synchronized (this.fGrammars) {
/* 239 */       clean();
/* 240 */       int hash = hashCode(desc);
/* 241 */       int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 242 */       for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 243 */         Grammar tempGrammar = (Grammar)entry.grammar.get();
/*     */ 
/* 245 */         if (tempGrammar == null) {
/* 246 */           removeEntry(entry);
/*     */         }
/* 248 */         else if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 249 */           return true;
/*     */         }
/*     */       }
/* 252 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void lockPool()
/*     */   {
/* 260 */     this.fPoolIsLocked = true;
/*     */   }
/*     */ 
/*     */   public void unlockPool()
/*     */   {
/* 268 */     this.fPoolIsLocked = false;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 276 */     for (int i = 0; i < this.fGrammars.length; i++) {
/* 277 */       if (this.fGrammars[i] != null) {
/* 278 */         this.fGrammars[i].clear();
/* 279 */         this.fGrammars[i] = null;
/*     */       }
/*     */     }
/* 282 */     this.fGrammarCount = 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(XMLGrammarDescription desc1, XMLGrammarDescription desc2)
/*     */   {
/* 295 */     if ((desc1 instanceof XMLSchemaDescription)) {
/* 296 */       if (!(desc2 instanceof XMLSchemaDescription)) {
/* 297 */         return false;
/*     */       }
/* 299 */       XMLSchemaDescription sd1 = (XMLSchemaDescription)desc1;
/* 300 */       XMLSchemaDescription sd2 = (XMLSchemaDescription)desc2;
/* 301 */       String targetNamespace = sd1.getTargetNamespace();
/* 302 */       if (targetNamespace != null) {
/* 303 */         if (!targetNamespace.equals(sd2.getTargetNamespace())) {
/* 304 */           return false;
/*     */         }
/*     */       }
/* 307 */       else if (sd2.getTargetNamespace() != null) {
/* 308 */         return false;
/*     */       }
/*     */ 
/* 315 */       String expandedSystemId = sd1.getExpandedSystemId();
/* 316 */       if (expandedSystemId != null) {
/* 317 */         if (!expandedSystemId.equals(sd2.getExpandedSystemId())) {
/* 318 */           return false;
/*     */         }
/*     */       }
/* 321 */       else if (sd2.getExpandedSystemId() != null) {
/* 322 */         return false;
/*     */       }
/* 324 */       return true;
/*     */     }
/* 326 */     return desc1.equals(desc2);
/*     */   }
/*     */ 
/*     */   public int hashCode(XMLGrammarDescription desc)
/*     */   {
/* 336 */     if ((desc instanceof XMLSchemaDescription)) {
/* 337 */       XMLSchemaDescription sd = (XMLSchemaDescription)desc;
/* 338 */       String targetNamespace = sd.getTargetNamespace();
/* 339 */       String expandedSystemId = sd.getExpandedSystemId();
/* 340 */       int hash = targetNamespace != null ? targetNamespace.hashCode() : 0;
/* 341 */       hash ^= (expandedSystemId != null ? expandedSystemId.hashCode() : 0);
/* 342 */       return hash;
/*     */     }
/* 344 */     return desc.hashCode();
/*     */   }
/*     */ 
/*     */   private Grammar removeEntry(Entry entry)
/*     */   {
/* 354 */     if (entry.prev != null) {
/* 355 */       entry.prev.next = entry.next;
/*     */     }
/*     */     else {
/* 358 */       this.fGrammars[entry.bucket] = entry.next;
/*     */     }
/* 360 */     if (entry.next != null) {
/* 361 */       entry.next.prev = entry.prev;
/*     */     }
/* 363 */     this.fGrammarCount -= 1;
/* 364 */     entry.grammar.entry = null;
/* 365 */     return (Grammar)entry.grammar.get();
/*     */   }
/*     */ 
/*     */   private void clean()
/*     */   {
/* 372 */     Reference ref = this.fReferenceQueue.poll();
/* 373 */     while (ref != null) {
/* 374 */       Entry entry = ((SoftGrammarReference)ref).entry;
/* 375 */       if (entry != null) {
/* 376 */         removeEntry(entry);
/*     */       }
/* 378 */       ref = this.fReferenceQueue.poll();
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class Entry {
/*     */     public int hash;
/*     */     public int bucket;
/*     */     public Entry prev;
/*     */     public Entry next;
/*     */     public XMLGrammarDescription desc;
/*     */     public SoftReferenceGrammarPool.SoftGrammarReference grammar;
/*     */ 
/*     */     protected Entry(int hash, int bucket, XMLGrammarDescription desc, Grammar grammar, Entry next, ReferenceQueue queue) {
/* 396 */       this.hash = hash;
/* 397 */       this.bucket = bucket;
/* 398 */       this.prev = null;
/* 399 */       this.next = next;
/* 400 */       if (next != null) {
/* 401 */         next.prev = this;
/*     */       }
/* 403 */       this.desc = desc;
/* 404 */       this.grammar = new SoftReferenceGrammarPool.SoftGrammarReference(this, grammar, queue);
/*     */     }
/*     */ 
/*     */     protected void clear()
/*     */     {
/* 410 */       this.desc = null;
/* 411 */       this.grammar = null;
/* 412 */       if (this.next != null) {
/* 413 */         this.next.clear();
/* 414 */         this.next = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SoftGrammarReference extends SoftReference
/*     */   {
/*     */     public SoftReferenceGrammarPool.Entry entry;
/*     */ 
/*     */     protected SoftGrammarReference(SoftReferenceGrammarPool.Entry entry, Grammar grammar, ReferenceQueue queue)
/*     */     {
/* 429 */       super(queue);
/* 430 */       this.entry = entry;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.jaxp.validation.SoftReferenceGrammarPool
 * JD-Core Version:    0.6.2
 */
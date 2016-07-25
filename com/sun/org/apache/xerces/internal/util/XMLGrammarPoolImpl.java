/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
/*     */ 
/*     */ public class XMLGrammarPoolImpl
/*     */   implements XMLGrammarPool
/*     */ {
/*     */   protected static final int TABLE_SIZE = 11;
/*  56 */   protected Entry[] fGrammars = null;
/*     */   protected boolean fPoolIsLocked;
/*  62 */   protected int fGrammarCount = 0;
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   public XMLGrammarPoolImpl()
/*     */   {
/*  72 */     this.fGrammars = new Entry[11];
/*  73 */     this.fPoolIsLocked = false;
/*     */   }
/*     */ 
/*     */   public XMLGrammarPoolImpl(int initialCapacity)
/*     */   {
/*  78 */     this.fGrammars = new Entry[initialCapacity];
/*  79 */     this.fPoolIsLocked = false;
/*     */   }
/*     */ 
/*     */   public Grammar[] retrieveInitialGrammarSet(String grammarType)
/*     */   {
/*  97 */     synchronized (this.fGrammars) {
/*  98 */       int grammarSize = this.fGrammars.length;
/*  99 */       Grammar[] tempGrammars = new Grammar[this.fGrammarCount];
/* 100 */       int pos = 0;
/* 101 */       for (int i = 0; i < grammarSize; i++) {
/* 102 */         for (Entry e = this.fGrammars[i]; e != null; e = e.next) {
/* 103 */           if (e.desc.getGrammarType().equals(grammarType)) {
/* 104 */             tempGrammars[(pos++)] = e.grammar;
/*     */           }
/*     */         }
/*     */       }
/* 108 */       Grammar[] toReturn = new Grammar[pos];
/* 109 */       System.arraycopy(tempGrammars, 0, toReturn, 0, pos);
/* 110 */       return toReturn;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void cacheGrammars(String grammarType, Grammar[] grammars)
/*     */   {
/* 126 */     if (!this.fPoolIsLocked)
/* 127 */       for (int i = 0; i < grammars.length; i++)
/*     */       {
/* 133 */         putGrammar(grammars[i]);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Grammar retrieveGrammar(XMLGrammarDescription desc)
/*     */   {
/* 157 */     return getGrammar(desc);
/*     */   }
/*     */ 
/*     */   public void putGrammar(Grammar grammar)
/*     */   {
/* 171 */     if (!this.fPoolIsLocked)
/* 172 */       synchronized (this.fGrammars) {
/* 173 */         XMLGrammarDescription desc = grammar.getGrammarDescription();
/* 174 */         int hash = hashCode(desc);
/* 175 */         int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 176 */         for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 177 */           if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 178 */             entry.grammar = grammar;
/* 179 */             return;
/*     */           }
/*     */         }
/*     */ 
/* 183 */         Entry entry = new Entry(hash, desc, grammar, this.fGrammars[index]);
/* 184 */         this.fGrammars[index] = entry;
/* 185 */         this.fGrammarCount += 1;
/*     */       }
/*     */   }
/*     */ 
/*     */   public Grammar getGrammar(XMLGrammarDescription desc)
/*     */   {
/* 198 */     synchronized (this.fGrammars) {
/* 199 */       int hash = hashCode(desc);
/* 200 */       int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 201 */       for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 202 */         if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 203 */           return entry.grammar;
/*     */         }
/*     */       }
/* 206 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Grammar removeGrammar(XMLGrammarDescription desc)
/*     */   {
/* 220 */     synchronized (this.fGrammars) {
/* 221 */       int hash = hashCode(desc);
/* 222 */       int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 223 */       Entry entry = this.fGrammars[index]; for (Entry prev = null; entry != null; entry = entry.next) {
/* 224 */         if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 225 */           if (prev != null) {
/* 226 */             prev.next = entry.next;
/*     */           }
/*     */           else {
/* 229 */             this.fGrammars[index] = entry.next;
/*     */           }
/* 231 */           Grammar tempGrammar = entry.grammar;
/* 232 */           entry.grammar = null;
/* 233 */           this.fGrammarCount -= 1;
/* 234 */           return tempGrammar;
/*     */         }
/* 223 */         prev = entry;
/*     */       }
/*     */ 
/* 237 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean containsGrammar(XMLGrammarDescription desc)
/*     */   {
/* 250 */     synchronized (this.fGrammars) {
/* 251 */       int hash = hashCode(desc);
/* 252 */       int index = (hash & 0x7FFFFFFF) % this.fGrammars.length;
/* 253 */       for (Entry entry = this.fGrammars[index]; entry != null; entry = entry.next) {
/* 254 */         if ((entry.hash == hash) && (equals(entry.desc, desc))) {
/* 255 */           return true;
/*     */         }
/*     */       }
/* 258 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void lockPool()
/*     */   {
/* 266 */     this.fPoolIsLocked = true;
/*     */   }
/*     */ 
/*     */   public void unlockPool()
/*     */   {
/* 274 */     this.fPoolIsLocked = false;
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 282 */     for (int i = 0; i < this.fGrammars.length; i++) {
/* 283 */       if (this.fGrammars[i] != null) {
/* 284 */         this.fGrammars[i].clear();
/* 285 */         this.fGrammars[i] = null;
/*     */       }
/*     */     }
/* 288 */     this.fGrammarCount = 0;
/*     */   }
/*     */ 
/*     */   public boolean equals(XMLGrammarDescription desc1, XMLGrammarDescription desc2)
/*     */   {
/* 301 */     return desc1.equals(desc2);
/*     */   }
/*     */ 
/*     */   public int hashCode(XMLGrammarDescription desc)
/*     */   {
/* 311 */     return desc.hashCode();
/*     */   }
/*     */ 
/*     */   protected static final class Entry
/*     */   {
/*     */     public int hash;
/*     */     public XMLGrammarDescription desc;
/*     */     public Grammar grammar;
/*     */     public Entry next;
/*     */ 
/*     */     protected Entry(int hash, XMLGrammarDescription desc, Grammar grammar, Entry next) {
/* 325 */       this.hash = hash;
/* 326 */       this.desc = desc;
/* 327 */       this.grammar = grammar;
/* 328 */       this.next = next;
/*     */     }
/*     */ 
/*     */     protected void clear()
/*     */     {
/* 334 */       this.desc = null;
/* 335 */       this.grammar = null;
/* 336 */       if (this.next != null) {
/* 337 */         this.next.clear();
/* 338 */         this.next = null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLGrammarPoolImpl
 * JD-Core Version:    0.6.2
 */
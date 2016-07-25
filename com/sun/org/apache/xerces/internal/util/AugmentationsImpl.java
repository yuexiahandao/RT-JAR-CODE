/*     */ package com.sun.org.apache.xerces.internal.util;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class AugmentationsImpl
/*     */   implements Augmentations
/*     */ {
/*     */   private AugmentationsItemsContainer fAugmentationsContainer;
/*     */ 
/*     */   public AugmentationsImpl()
/*     */   {
/*  40 */     this.fAugmentationsContainer = new SmallContainer();
/*     */   }
/*     */ 
/*     */   public Object putItem(String key, Object item)
/*     */   {
/*  53 */     Object oldValue = this.fAugmentationsContainer.putItem(key, item);
/*     */ 
/*  55 */     if ((oldValue == null) && (this.fAugmentationsContainer.isFull())) {
/*  56 */       this.fAugmentationsContainer = this.fAugmentationsContainer.expand();
/*     */     }
/*     */ 
/*  59 */     return oldValue;
/*     */   }
/*     */ 
/*     */   public Object getItem(String key)
/*     */   {
/*  72 */     return this.fAugmentationsContainer.getItem(key);
/*     */   }
/*     */ 
/*     */   public Object removeItem(String key)
/*     */   {
/*  82 */     return this.fAugmentationsContainer.removeItem(key);
/*     */   }
/*     */ 
/*     */   public Enumeration keys()
/*     */   {
/*  90 */     return this.fAugmentationsContainer.keys();
/*     */   }
/*     */ 
/*     */   public void removeAllItems()
/*     */   {
/*  97 */     this.fAugmentationsContainer.clear();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 101 */     return this.fAugmentationsContainer.toString();
/*     */   }
/*     */ 
/*     */   abstract class AugmentationsItemsContainer
/*     */   {
/*     */     AugmentationsItemsContainer()
/*     */     {
/*     */     }
/*     */ 
/*     */     public abstract Object putItem(Object paramObject1, Object paramObject2);
/*     */ 
/*     */     public abstract Object getItem(Object paramObject);
/*     */ 
/*     */     public abstract Object removeItem(Object paramObject);
/*     */ 
/*     */     public abstract Enumeration keys();
/*     */ 
/*     */     public abstract void clear();
/*     */ 
/*     */     public abstract boolean isFull();
/*     */ 
/*     */     public abstract AugmentationsItemsContainer expand();
/*     */   }
/*     */ 
/*     */   class LargeContainer extends AugmentationsImpl.AugmentationsItemsContainer
/*     */   {
/* 243 */     final Hashtable fAugmentations = new Hashtable();
/*     */ 
/*     */     LargeContainer()
/*     */     {
/* 242 */       super();
/*     */     }
/*     */ 
/*     */     public Object getItem(Object key) {
/* 246 */       return this.fAugmentations.get(key);
/*     */     }
/*     */ 
/*     */     public Object putItem(Object key, Object item) {
/* 250 */       return this.fAugmentations.put(key, item);
/*     */     }
/*     */ 
/*     */     public Object removeItem(Object key) {
/* 254 */       return this.fAugmentations.remove(key);
/*     */     }
/*     */ 
/*     */     public Enumeration keys() {
/* 258 */       return this.fAugmentations.keys();
/*     */     }
/*     */ 
/*     */     public void clear() {
/* 262 */       this.fAugmentations.clear();
/*     */     }
/*     */ 
/*     */     public boolean isFull() {
/* 266 */       return false;
/*     */     }
/*     */ 
/*     */     public AugmentationsImpl.AugmentationsItemsContainer expand() {
/* 270 */       return this;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 274 */       StringBuffer buff = new StringBuffer();
/* 275 */       buff.append("LargeContainer");
/* 276 */       Enumeration keys = this.fAugmentations.keys();
/*     */ 
/* 278 */       while (keys.hasMoreElements()) {
/* 279 */         Object key = keys.nextElement();
/* 280 */         buff.append("\nkey == ");
/* 281 */         buff.append(key);
/* 282 */         buff.append("; value == ");
/* 283 */         buff.append(this.fAugmentations.get(key));
/*     */       }
/*     */ 
/* 286 */       return buff.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   class SmallContainer extends AugmentationsImpl.AugmentationsItemsContainer
/*     */   {
/*     */     static final int SIZE_LIMIT = 10;
/*     */     final Object[] fAugmentations;
/*     */     int fNumEntries;
/*     */ 
/*     */     SmallContainer()
/*     */     {
/* 114 */       super();
/*     */ 
/* 116 */       this.fAugmentations = new Object[20];
/* 117 */       this.fNumEntries = 0;
/*     */     }
/*     */     public Enumeration keys() {
/* 120 */       return new SmallContainerKeyEnumeration();
/*     */     }
/*     */ 
/*     */     public Object getItem(Object key) {
/* 124 */       for (int i = 0; i < this.fNumEntries * 2; i += 2) {
/* 125 */         if (this.fAugmentations[i].equals(key)) {
/* 126 */           return this.fAugmentations[(i + 1)];
/*     */         }
/*     */       }
/*     */ 
/* 130 */       return null;
/*     */     }
/*     */ 
/*     */     public Object putItem(Object key, Object item) {
/* 134 */       for (int i = 0; i < this.fNumEntries * 2; i += 2) {
/* 135 */         if (this.fAugmentations[i].equals(key)) {
/* 136 */           Object oldValue = this.fAugmentations[(i + 1)];
/* 137 */           this.fAugmentations[(i + 1)] = item;
/*     */ 
/* 139 */           return oldValue;
/*     */         }
/*     */       }
/*     */ 
/* 143 */       this.fAugmentations[(this.fNumEntries * 2)] = key;
/* 144 */       this.fAugmentations[(this.fNumEntries * 2 + 1)] = item;
/* 145 */       this.fNumEntries += 1;
/*     */ 
/* 147 */       return null;
/*     */     }
/*     */ 
/*     */     public Object removeItem(Object key)
/*     */     {
/* 152 */       for (int i = 0; i < this.fNumEntries * 2; i += 2) {
/* 153 */         if (this.fAugmentations[i].equals(key)) {
/* 154 */           Object oldValue = this.fAugmentations[(i + 1)];
/*     */ 
/* 156 */           for (int j = i; j < this.fNumEntries * 2 - 2; j += 2) {
/* 157 */             this.fAugmentations[j] = this.fAugmentations[(j + 2)];
/* 158 */             this.fAugmentations[(j + 1)] = this.fAugmentations[(j + 3)];
/*     */           }
/*     */ 
/* 161 */           this.fAugmentations[(this.fNumEntries * 2 - 2)] = null;
/* 162 */           this.fAugmentations[(this.fNumEntries * 2 - 1)] = null;
/* 163 */           this.fNumEntries -= 1;
/*     */ 
/* 165 */           return oldValue;
/*     */         }
/*     */       }
/*     */ 
/* 169 */       return null;
/*     */     }
/*     */ 
/*     */     public void clear() {
/* 173 */       for (int i = 0; i < this.fNumEntries * 2; i += 2) {
/* 174 */         this.fAugmentations[i] = null;
/* 175 */         this.fAugmentations[(i + 1)] = null;
/*     */       }
/*     */ 
/* 178 */       this.fNumEntries = 0;
/*     */     }
/*     */ 
/*     */     public boolean isFull() {
/* 182 */       return this.fNumEntries == 10;
/*     */     }
/*     */ 
/*     */     public AugmentationsImpl.AugmentationsItemsContainer expand() {
/* 186 */       AugmentationsImpl.LargeContainer expandedContainer = new AugmentationsImpl.LargeContainer(AugmentationsImpl.this);
/*     */ 
/* 188 */       for (int i = 0; i < this.fNumEntries * 2; i += 2) {
/* 189 */         expandedContainer.putItem(this.fAugmentations[i], this.fAugmentations[(i + 1)]);
/*     */       }
/*     */ 
/* 193 */       return expandedContainer;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 197 */       StringBuffer buff = new StringBuffer();
/* 198 */       buff.append("SmallContainer - fNumEntries == " + this.fNumEntries);
/*     */ 
/* 200 */       for (int i = 0; i < 20; i += 2) {
/* 201 */         buff.append("\nfAugmentations[");
/* 202 */         buff.append(i);
/* 203 */         buff.append("] == ");
/* 204 */         buff.append(this.fAugmentations[i]);
/* 205 */         buff.append("; fAugmentations[");
/* 206 */         buff.append(i + 1);
/* 207 */         buff.append("] == ");
/* 208 */         buff.append(this.fAugmentations[(i + 1)]);
/*     */       }
/*     */ 
/* 211 */       return buff.toString();
/*     */     }
/*     */ 
/*     */     class SmallContainerKeyEnumeration implements Enumeration {
/* 215 */       Object[] enumArray = new Object[AugmentationsImpl.SmallContainer.this.fNumEntries];
/* 216 */       int next = 0;
/*     */ 
/*     */       SmallContainerKeyEnumeration() {
/* 219 */         for (int i = 0; i < AugmentationsImpl.SmallContainer.this.fNumEntries; i++)
/* 220 */           this.enumArray[i] = AugmentationsImpl.SmallContainer.this.fAugmentations[(i * 2)];
/*     */       }
/*     */ 
/*     */       public boolean hasMoreElements()
/*     */       {
/* 225 */         return this.next < this.enumArray.length;
/*     */       }
/*     */ 
/*     */       public Object nextElement() {
/* 229 */         if (this.next >= this.enumArray.length) {
/* 230 */           throw new NoSuchElementException();
/*     */         }
/*     */ 
/* 233 */         Object nextVal = this.enumArray[this.next];
/* 234 */         this.enumArray[this.next] = null;
/* 235 */         this.next += 1;
/*     */ 
/* 237 */         return nextVal;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.AugmentationsImpl
 * JD-Core Version:    0.6.2
 */
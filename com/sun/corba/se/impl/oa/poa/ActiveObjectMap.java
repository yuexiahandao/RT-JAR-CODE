/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public abstract class ActiveObjectMap
/*     */ {
/*     */   protected POAImpl poa;
/* 100 */   private Map keyToEntry = new HashMap();
/* 101 */   private Map entryToServant = new HashMap();
/* 102 */   private Map servantToEntry = new HashMap();
/*     */ 
/*     */   protected ActiveObjectMap(POAImpl paramPOAImpl)
/*     */   {
/*  89 */     this.poa = paramPOAImpl;
/*     */   }
/*     */ 
/*     */   public static ActiveObjectMap create(POAImpl paramPOAImpl, boolean paramBoolean)
/*     */   {
/*  94 */     if (paramBoolean) {
/*  95 */       return new MultipleObjectMap(paramPOAImpl);
/*     */     }
/*  97 */     return new SingleObjectMap(paramPOAImpl);
/*     */   }
/*     */ 
/*     */   public final boolean contains(Servant paramServant)
/*     */   {
/* 106 */     return this.servantToEntry.containsKey(paramServant);
/*     */   }
/*     */ 
/*     */   public final boolean containsKey(Key paramKey)
/*     */   {
/* 111 */     return this.keyToEntry.containsKey(paramKey);
/*     */   }
/*     */ 
/*     */   public final AOMEntry get(Key paramKey)
/*     */   {
/* 119 */     AOMEntry localAOMEntry = (AOMEntry)this.keyToEntry.get(paramKey);
/* 120 */     if (localAOMEntry == null) {
/* 121 */       localAOMEntry = new AOMEntry(this.poa);
/* 122 */       putEntry(paramKey, localAOMEntry);
/*     */     }
/*     */ 
/* 125 */     return localAOMEntry;
/*     */   }
/*     */ 
/*     */   public final Servant getServant(AOMEntry paramAOMEntry)
/*     */   {
/* 130 */     return (Servant)this.entryToServant.get(paramAOMEntry);
/*     */   }
/*     */ 
/*     */   public abstract Key getKey(AOMEntry paramAOMEntry) throws WrongPolicy;
/*     */ 
/*     */   public Key getKey(Servant paramServant) throws WrongPolicy
/*     */   {
/* 137 */     AOMEntry localAOMEntry = (AOMEntry)this.servantToEntry.get(paramServant);
/* 138 */     return getKey(localAOMEntry);
/*     */   }
/*     */ 
/*     */   protected void putEntry(Key paramKey, AOMEntry paramAOMEntry)
/*     */   {
/* 143 */     this.keyToEntry.put(paramKey, paramAOMEntry);
/*     */   }
/*     */ 
/*     */   public final void putServant(Servant paramServant, AOMEntry paramAOMEntry)
/*     */   {
/* 148 */     this.entryToServant.put(paramAOMEntry, paramServant);
/* 149 */     this.servantToEntry.put(paramServant, paramAOMEntry);
/*     */   }
/*     */ 
/*     */   protected abstract void removeEntry(AOMEntry paramAOMEntry, Key paramKey);
/*     */ 
/*     */   public final void remove(Key paramKey)
/*     */   {
/* 156 */     AOMEntry localAOMEntry = (AOMEntry)this.keyToEntry.remove(paramKey);
/* 157 */     Servant localServant = (Servant)this.entryToServant.remove(localAOMEntry);
/* 158 */     if (localServant != null) {
/* 159 */       this.servantToEntry.remove(localServant);
/*     */     }
/* 161 */     removeEntry(localAOMEntry, paramKey);
/*     */   }
/*     */ 
/*     */   public abstract boolean hasMultipleIDs(AOMEntry paramAOMEntry);
/*     */ 
/*     */   protected void clear()
/*     */   {
/* 168 */     this.keyToEntry.clear();
/*     */   }
/*     */ 
/*     */   public final Set keySet()
/*     */   {
/* 173 */     return this.keyToEntry.keySet();
/*     */   }
/*     */ 
/*     */   public static class Key
/*     */   {
/*     */     public byte[] id;
/*     */ 
/*     */     Key(byte[] paramArrayOfByte)
/*     */     {
/*  51 */       this.id = paramArrayOfByte;
/*     */     }
/*     */ 
/*     */     public String toString() {
/*  55 */       StringBuffer localStringBuffer = new StringBuffer();
/*  56 */       for (int i = 0; i < this.id.length; i++) {
/*  57 */         localStringBuffer.append(Integer.toString(this.id[i], 16));
/*  58 */         if (i != this.id.length - 1)
/*  59 */           localStringBuffer.append(":");
/*     */       }
/*  61 */       return localStringBuffer.toString();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/*  65 */       if (!(paramObject instanceof Key))
/*  66 */         return false;
/*  67 */       Key localKey = (Key)paramObject;
/*  68 */       if (localKey.id.length != this.id.length)
/*  69 */         return false;
/*  70 */       for (int i = 0; i < this.id.length; i++)
/*  71 */         if (this.id[i] != localKey.id[i])
/*  72 */           return false;
/*  73 */       return true;
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/*  78 */       int i = 0;
/*  79 */       for (int j = 0; j < this.id.length; j++)
/*  80 */         i = 31 * i + this.id[j];
/*  81 */       return i;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.ActiveObjectMap
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ 
/*     */ class MultipleObjectMap extends ActiveObjectMap
/*     */ {
/* 218 */   private Map entryToKeys = new HashMap();
/*     */ 
/*     */   public MultipleObjectMap(POAImpl paramPOAImpl)
/*     */   {
/* 222 */     super(paramPOAImpl);
/*     */   }
/*     */ 
/*     */   public ActiveObjectMap.Key getKey(AOMEntry paramAOMEntry) throws WrongPolicy
/*     */   {
/* 227 */     throw new WrongPolicy();
/*     */   }
/*     */ 
/*     */   protected void putEntry(ActiveObjectMap.Key paramKey, AOMEntry paramAOMEntry)
/*     */   {
/* 232 */     super.putEntry(paramKey, paramAOMEntry);
/*     */ 
/* 234 */     Object localObject = (Set)this.entryToKeys.get(paramAOMEntry);
/* 235 */     if (localObject == null) {
/* 236 */       localObject = new HashSet();
/* 237 */       this.entryToKeys.put(paramAOMEntry, localObject);
/*     */     }
/* 239 */     ((Set)localObject).add(paramKey);
/*     */   }
/*     */ 
/*     */   public boolean hasMultipleIDs(AOMEntry paramAOMEntry)
/*     */   {
/* 244 */     Set localSet = (Set)this.entryToKeys.get(paramAOMEntry);
/* 245 */     if (localSet == null)
/* 246 */       return false;
/* 247 */     return localSet.size() > 1;
/*     */   }
/*     */ 
/*     */   protected void removeEntry(AOMEntry paramAOMEntry, ActiveObjectMap.Key paramKey)
/*     */   {
/* 252 */     Set localSet = (Set)this.entryToKeys.get(paramAOMEntry);
/* 253 */     if (localSet != null) {
/* 254 */       localSet.remove(paramKey);
/* 255 */       if (localSet.isEmpty())
/* 256 */         this.entryToKeys.remove(paramAOMEntry);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 262 */     super.clear();
/* 263 */     this.entryToKeys.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.MultipleObjectMap
 * JD-Core Version:    0.6.2
 */
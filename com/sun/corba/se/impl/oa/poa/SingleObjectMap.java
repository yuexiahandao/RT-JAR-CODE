/*     */ package com.sun.corba.se.impl.oa.poa;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ 
/*     */ class SingleObjectMap extends ActiveObjectMap
/*     */ {
/* 179 */   private Map entryToKey = new HashMap();
/*     */ 
/*     */   public SingleObjectMap(POAImpl paramPOAImpl)
/*     */   {
/* 183 */     super(paramPOAImpl);
/*     */   }
/*     */ 
/*     */   public ActiveObjectMap.Key getKey(AOMEntry paramAOMEntry) throws WrongPolicy
/*     */   {
/* 188 */     return (ActiveObjectMap.Key)this.entryToKey.get(paramAOMEntry);
/*     */   }
/*     */ 
/*     */   protected void putEntry(ActiveObjectMap.Key paramKey, AOMEntry paramAOMEntry)
/*     */   {
/* 193 */     super.putEntry(paramKey, paramAOMEntry);
/*     */ 
/* 195 */     this.entryToKey.put(paramAOMEntry, paramKey);
/*     */   }
/*     */ 
/*     */   public boolean hasMultipleIDs(AOMEntry paramAOMEntry)
/*     */   {
/* 200 */     return false;
/*     */   }
/*     */ 
/*     */   protected void removeEntry(AOMEntry paramAOMEntry, ActiveObjectMap.Key paramKey)
/*     */   {
/* 206 */     this.entryToKey.remove(paramAOMEntry);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 211 */     super.clear();
/* 212 */     this.entryToKey.clear();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.oa.poa.SingleObjectMap
 * JD-Core Version:    0.6.2
 */
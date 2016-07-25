/*     */ package com.sun.corba.se.impl.util;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class RepositoryIdCache extends Hashtable
/*     */ {
/*  89 */   private RepositoryIdPool pool = new RepositoryIdPool();
/*     */ 
/*     */   public RepositoryIdCache() {
/*  92 */     this.pool.setCaches(this);
/*     */   }
/*     */ 
/*     */   public final synchronized RepositoryId getId(String paramString) {
/*  96 */     RepositoryId localRepositoryId = (RepositoryId)super.get(paramString);
/*     */ 
/*  98 */     if (localRepositoryId != null) {
/*  99 */       return localRepositoryId;
/*     */     }
/*     */ 
/* 102 */     localRepositoryId = new RepositoryId(paramString);
/* 103 */     put(paramString, localRepositoryId);
/* 104 */     return localRepositoryId;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.RepositoryIdCache
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.corba.se.impl.util;
/*    */ 
/*    */ import java.util.EmptyStackException;
/*    */ import java.util.Stack;
/*    */ 
/*    */ class RepositoryIdPool extends Stack
/*    */ {
/* 42 */   private static int MAX_CACHE_SIZE = 4;
/*    */   private RepositoryIdCache cache;
/*    */ 
/*    */   public final synchronized RepositoryId popId()
/*    */   {
/*    */     try
/*    */     {
/* 48 */       return (RepositoryId)super.pop();
/*    */     }
/*    */     catch (EmptyStackException localEmptyStackException) {
/* 51 */       increasePool(5);
/* 52 */     }return (RepositoryId)super.pop();
/*    */   }
/*    */ 
/*    */   final void increasePool(int paramInt)
/*    */   {
/* 60 */     for (int i = paramInt; i > 0; i--)
/* 61 */       push(new RepositoryId());
/*    */   }
/*    */ 
/*    */   final void setCaches(RepositoryIdCache paramRepositoryIdCache)
/*    */   {
/* 82 */     this.cache = paramRepositoryIdCache;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.util.RepositoryIdPool
 * JD-Core Version:    0.6.2
 */
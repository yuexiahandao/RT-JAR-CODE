/*    */ package com.sun.xml.internal.ws.api.ha;
/*    */ 
/*    */ public class HaInfo
/*    */ {
/*    */   private final String replicaInstance;
/*    */   private final String key;
/*    */   private final boolean failOver;
/*    */ 
/*    */   public HaInfo(String key, String replicaInstance, boolean failOver)
/*    */   {
/* 68 */     this.key = key;
/* 69 */     this.replicaInstance = replicaInstance;
/* 70 */     this.failOver = failOver;
/*    */   }
/*    */ 
/*    */   public String getReplicaInstance() {
/* 74 */     return this.replicaInstance;
/*    */   }
/*    */ 
/*    */   public String getKey() {
/* 78 */     return this.key;
/*    */   }
/*    */ 
/*    */   public boolean isFailOver() {
/* 82 */     return this.failOver;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.ha.HaInfo
 * JD-Core Version:    0.6.2
 */
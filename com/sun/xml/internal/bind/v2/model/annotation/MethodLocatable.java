/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*    */ 
/*    */ public class MethodLocatable<M>
/*    */   implements Locatable
/*    */ {
/*    */   private final Locatable upstream;
/*    */   private final M method;
/*    */   private final Navigator<?, ?, ?, M> nav;
/*    */ 
/*    */   public MethodLocatable(Locatable upstream, M method, Navigator<?, ?, ?, M> nav)
/*    */   {
/* 42 */     this.upstream = upstream;
/* 43 */     this.method = method;
/* 44 */     this.nav = nav;
/*    */   }
/*    */ 
/*    */   public Locatable getUpstream() {
/* 48 */     return this.upstream;
/*    */   }
/*    */ 
/*    */   public Location getLocation() {
/* 52 */     return this.nav.getMethodLocation(this.method);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.MethodLocatable
 * JD-Core Version:    0.6.2
 */
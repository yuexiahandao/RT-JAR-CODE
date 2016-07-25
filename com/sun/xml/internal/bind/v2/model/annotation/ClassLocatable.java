/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*    */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*    */ 
/*    */ public class ClassLocatable<C>
/*    */   implements Locatable
/*    */ {
/*    */   private final Locatable upstream;
/*    */   private final C clazz;
/*    */   private final Navigator<?, C, ?, ?> nav;
/*    */ 
/*    */   public ClassLocatable(Locatable upstream, C clazz, Navigator<?, C, ?, ?> nav)
/*    */   {
/* 42 */     this.upstream = upstream;
/* 43 */     this.clazz = clazz;
/* 44 */     this.nav = nav;
/*    */   }
/*    */ 
/*    */   public Locatable getUpstream() {
/* 48 */     return this.upstream;
/*    */   }
/*    */ 
/*    */   public Location getLocation() {
/* 52 */     return this.nav.getClassLocation(this.clazz);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.ClassLocatable
 * JD-Core Version:    0.6.2
 */
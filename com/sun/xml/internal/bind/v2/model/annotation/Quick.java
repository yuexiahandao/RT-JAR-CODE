/*    */ package com.sun.xml.internal.bind.v2.model.annotation;
/*    */ 
/*    */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*    */ import java.lang.annotation.Annotation;
/*    */ 
/*    */ public abstract class Quick
/*    */   implements Annotation, Locatable, Location
/*    */ {
/*    */   private final Locatable upstream;
/*    */ 
/*    */   protected Quick(Locatable upstream)
/*    */   {
/* 45 */     this.upstream = upstream;
/*    */   }
/*    */ 
/*    */   protected abstract Annotation getAnnotation();
/*    */ 
/*    */   protected abstract Quick newInstance(Locatable paramLocatable, Annotation paramAnnotation);
/*    */ 
/*    */   public final Location getLocation()
/*    */   {
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */   public final Locatable getUpstream() {
/* 64 */     return this.upstream;
/*    */   }
/*    */ 
/*    */   public final String toString() {
/* 68 */     return getAnnotation().toString();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.annotation.Quick
 * JD-Core Version:    0.6.2
 */
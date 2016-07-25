/*    */ package com.sun.corba.se.impl.resolver;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*    */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class LocalResolverImpl
/*    */   implements LocalResolver
/*    */ {
/* 32 */   Map nameToClosure = new HashMap();
/*    */ 
/*    */   public synchronized org.omg.CORBA.Object resolve(String paramString)
/*    */   {
/* 36 */     Closure localClosure = (Closure)this.nameToClosure.get(paramString);
/* 37 */     if (localClosure == null) {
/* 38 */       return null;
/*    */     }
/* 40 */     return (org.omg.CORBA.Object)localClosure.evaluate();
/*    */   }
/*    */ 
/*    */   public synchronized Set list()
/*    */   {
/* 45 */     return this.nameToClosure.keySet();
/*    */   }
/*    */ 
/*    */   public synchronized void register(String paramString, Closure paramClosure)
/*    */   {
/* 50 */     this.nameToClosure.put(paramString, paramClosure);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.LocalResolverImpl
 * JD-Core Version:    0.6.2
 */
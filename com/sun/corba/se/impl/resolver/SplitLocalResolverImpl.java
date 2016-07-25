/*    */ package com.sun.corba.se.impl.resolver;
/*    */ 
/*    */ import com.sun.corba.se.spi.orbutil.closure.Closure;
/*    */ import com.sun.corba.se.spi.resolver.LocalResolver;
/*    */ import com.sun.corba.se.spi.resolver.Resolver;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class SplitLocalResolverImpl
/*    */   implements LocalResolver
/*    */ {
/*    */   private Resolver resolver;
/*    */   private LocalResolver localResolver;
/*    */ 
/*    */   public SplitLocalResolverImpl(Resolver paramResolver, LocalResolver paramLocalResolver)
/*    */   {
/* 41 */     this.resolver = paramResolver;
/* 42 */     this.localResolver = paramLocalResolver;
/*    */   }
/*    */ 
/*    */   public void register(String paramString, Closure paramClosure)
/*    */   {
/* 47 */     this.localResolver.register(paramString, paramClosure);
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object resolve(String paramString)
/*    */   {
/* 52 */     return this.resolver.resolve(paramString);
/*    */   }
/*    */ 
/*    */   public Set list()
/*    */   {
/* 57 */     return this.resolver.list();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.SplitLocalResolverImpl
 * JD-Core Version:    0.6.2
 */
/*    */ package com.sun.corba.se.impl.resolver;
/*    */ 
/*    */ import com.sun.corba.se.spi.resolver.Resolver;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class CompositeResolverImpl
/*    */   implements Resolver
/*    */ {
/*    */   private Resolver first;
/*    */   private Resolver second;
/*    */ 
/*    */   public CompositeResolverImpl(Resolver paramResolver1, Resolver paramResolver2)
/*    */   {
/* 39 */     this.first = paramResolver1;
/* 40 */     this.second = paramResolver2;
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object resolve(String paramString)
/*    */   {
/* 45 */     org.omg.CORBA.Object localObject = this.first.resolve(paramString);
/* 46 */     if (localObject == null)
/* 47 */       localObject = this.second.resolve(paramString);
/* 48 */     return localObject;
/*    */   }
/*    */ 
/*    */   public Set list()
/*    */   {
/* 53 */     HashSet localHashSet = new HashSet();
/* 54 */     localHashSet.addAll(this.first.list());
/* 55 */     localHashSet.addAll(this.second.list());
/* 56 */     return localHashSet;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.CompositeResolverImpl
 * JD-Core Version:    0.6.2
 */
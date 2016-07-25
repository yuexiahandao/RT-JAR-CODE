/*    */ package com.sun.corba.se.impl.resolver;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import com.sun.corba.se.spi.resolver.Resolver;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ORBDefaultInitRefResolverImpl
/*    */   implements Resolver
/*    */ {
/*    */   Operation urlHandler;
/*    */   String orbDefaultInitRef;
/*    */ 
/*    */   public ORBDefaultInitRefResolverImpl(Operation paramOperation, String paramString)
/*    */   {
/* 38 */     this.urlHandler = paramOperation;
/*    */ 
/* 41 */     this.orbDefaultInitRef = paramString;
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object resolve(String paramString)
/*    */   {
/* 47 */     if (this.orbDefaultInitRef == null)
/* 48 */       return null;
/*    */     String str;
/* 56 */     if (this.orbDefaultInitRef.startsWith("corbaloc:"))
/* 57 */       str = this.orbDefaultInitRef + "/" + paramString;
/*    */     else {
/* 59 */       str = this.orbDefaultInitRef + "#" + paramString;
/*    */     }
/*    */ 
/* 62 */     return (org.omg.CORBA.Object)this.urlHandler.operate(str);
/*    */   }
/*    */ 
/*    */   public Set list()
/*    */   {
/* 67 */     return new HashSet();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.ORBDefaultInitRefResolverImpl
 * JD-Core Version:    0.6.2
 */
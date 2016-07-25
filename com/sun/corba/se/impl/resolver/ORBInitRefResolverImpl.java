/*    */ package com.sun.corba.se.impl.resolver;
/*    */ 
/*    */ import com.sun.corba.se.spi.orb.Operation;
/*    */ import com.sun.corba.se.spi.orb.StringPair;
/*    */ import com.sun.corba.se.spi.resolver.Resolver;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class ORBInitRefResolverImpl
/*    */   implements Resolver
/*    */ {
/*    */   Operation urlHandler;
/*    */   Map orbInitRefTable;
/*    */ 
/*    */   public ORBInitRefResolverImpl(Operation paramOperation, StringPair[] paramArrayOfStringPair)
/*    */   {
/* 41 */     this.urlHandler = paramOperation;
/* 42 */     this.orbInitRefTable = new HashMap();
/*    */ 
/* 44 */     for (int i = 0; i < paramArrayOfStringPair.length; i++) {
/* 45 */       StringPair localStringPair = paramArrayOfStringPair[i];
/* 46 */       this.orbInitRefTable.put(localStringPair.getFirst(), localStringPair.getSecond());
/*    */     }
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object resolve(String paramString)
/*    */   {
/* 52 */     String str = (String)this.orbInitRefTable.get(paramString);
/* 53 */     if (str == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     org.omg.CORBA.Object localObject = (org.omg.CORBA.Object)this.urlHandler.operate(str);
/*    */ 
/* 58 */     return localObject;
/*    */   }
/*    */ 
/*    */   public Set list()
/*    */   {
/* 63 */     return this.orbInitRefTable.keySet();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.resolver.ORBInitRefResolverImpl
 * JD-Core Version:    0.6.2
 */
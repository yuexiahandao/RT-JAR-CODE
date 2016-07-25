/*    */ package com.sun.jndi.toolkit.dir;
/*    */ 
/*    */ import javax.naming.NamingEnumeration;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.directory.Attribute;
/*    */ import javax.naming.directory.Attributes;
/*    */ 
/*    */ public class ContainmentFilter
/*    */   implements AttrFilter
/*    */ {
/*    */   private Attributes matchingAttrs;
/*    */ 
/*    */   public ContainmentFilter(Attributes paramAttributes)
/*    */   {
/* 44 */     this.matchingAttrs = paramAttributes;
/*    */   }
/*    */ 
/*    */   public boolean check(Attributes paramAttributes) throws NamingException {
/* 48 */     return (this.matchingAttrs == null) || (this.matchingAttrs.size() == 0) || (contains(paramAttributes, this.matchingAttrs));
/*    */   }
/*    */ 
/*    */   public static boolean contains(Attributes paramAttributes1, Attributes paramAttributes2)
/*    */     throws NamingException
/*    */   {
/* 56 */     if (paramAttributes2 == null) {
/* 57 */       return true;
/*    */     }
/* 59 */     NamingEnumeration localNamingEnumeration1 = paramAttributes2.getAll();
/* 60 */     while (localNamingEnumeration1.hasMore()) {
/* 61 */       if (paramAttributes1 == null) {
/* 62 */         return false;
/*    */       }
/* 64 */       Attribute localAttribute1 = (Attribute)localNamingEnumeration1.next();
/* 65 */       Attribute localAttribute2 = paramAttributes1.get(localAttribute1.getID());
/* 66 */       if (localAttribute2 == null) {
/* 67 */         return false;
/*    */       }
/*    */ 
/* 70 */       if (localAttribute1.size() > 0) {
/* 71 */         NamingEnumeration localNamingEnumeration2 = localAttribute1.getAll();
/* 72 */         while (localNamingEnumeration2.hasMore()) {
/* 73 */           if (!localAttribute2.contains(localNamingEnumeration2.next())) {
/* 74 */             return false;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/*    */ 
/* 80 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.ContainmentFilter
 * JD-Core Version:    0.6.2
 */
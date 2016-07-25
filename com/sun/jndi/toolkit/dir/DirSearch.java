/*    */ package com.sun.jndi.toolkit.dir;
/*    */ 
/*    */ import javax.naming.NamingEnumeration;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.directory.Attributes;
/*    */ import javax.naming.directory.DirContext;
/*    */ import javax.naming.directory.SearchControls;
/*    */ 
/*    */ public class DirSearch
/*    */ {
/*    */   public static NamingEnumeration search(DirContext paramDirContext, Attributes paramAttributes, String[] paramArrayOfString)
/*    */     throws NamingException
/*    */   {
/* 40 */     SearchControls localSearchControls = new SearchControls(1, 0L, 0, paramArrayOfString, false, false);
/*    */ 
/* 45 */     return new LazySearchEnumerationImpl(new ContextEnumerator(paramDirContext, 1), new ContainmentFilter(paramAttributes), localSearchControls);
/*    */   }
/*    */ 
/*    */   public static NamingEnumeration search(DirContext paramDirContext, String paramString, SearchControls paramSearchControls)
/*    */     throws NamingException
/*    */   {
/* 54 */     if (paramSearchControls == null) {
/* 55 */       paramSearchControls = new SearchControls();
/*    */     }
/* 57 */     return new LazySearchEnumerationImpl(new ContextEnumerator(paramDirContext, paramSearchControls.getSearchScope()), new SearchFilter(paramString), paramSearchControls);
/*    */   }
/*    */ 
/*    */   public static NamingEnumeration search(DirContext paramDirContext, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*    */     throws NamingException
/*    */   {
/* 67 */     String str = SearchFilter.format(paramString, paramArrayOfObject);
/* 68 */     return search(paramDirContext, str, paramSearchControls);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.DirSearch
 * JD-Core Version:    0.6.2
 */
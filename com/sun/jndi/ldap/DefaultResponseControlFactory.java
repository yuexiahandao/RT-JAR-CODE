/*    */ package com.sun.jndi.ldap;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.ldap.Control;
/*    */ import javax.naming.ldap.ControlFactory;
/*    */ import javax.naming.ldap.PagedResultsResponseControl;
/*    */ import javax.naming.ldap.SortResponseControl;
/*    */ 
/*    */ public class DefaultResponseControlFactory extends ControlFactory
/*    */ {
/*    */   public Control getControlInstance(Control paramControl)
/*    */     throws NamingException
/*    */   {
/* 76 */     String str = paramControl.getID();
/*    */     try
/*    */     {
/* 80 */       if (str.equals("1.2.840.113556.1.4.474")) {
/* 81 */         return new SortResponseControl(str, paramControl.isCritical(), paramControl.getEncodedValue());
/*    */       }
/*    */ 
/* 84 */       if (str.equals("1.2.840.113556.1.4.319")) {
/* 85 */         return new PagedResultsResponseControl(str, paramControl.isCritical(), paramControl.getEncodedValue());
/*    */       }
/*    */ 
/* 88 */       if (str.equals("2.16.840.1.113730.3.4.7")) {
/* 89 */         return new EntryChangeResponseControl(str, paramControl.isCritical(), paramControl.getEncodedValue());
/*    */       }
/*    */     }
/*    */     catch (IOException localIOException)
/*    */     {
/* 94 */       NamingException localNamingException = new NamingException();
/* 95 */       localNamingException.setRootCause(localIOException);
/* 96 */       throw localNamingException;
/*    */     }
/* 98 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.DefaultResponseControlFactory
 * JD-Core Version:    0.6.2
 */
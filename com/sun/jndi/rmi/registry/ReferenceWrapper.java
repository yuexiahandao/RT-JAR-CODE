/*    */ package com.sun.jndi.rmi.registry;
/*    */ 
/*    */ import java.rmi.RemoteException;
/*    */ import java.rmi.server.UnicastRemoteObject;
/*    */ import javax.naming.NamingException;
/*    */ import javax.naming.Reference;
/*    */ 
/*    */ public class ReferenceWrapper extends UnicastRemoteObject
/*    */   implements RemoteReference
/*    */ {
/*    */   protected Reference wrappee;
/*    */   private static final long serialVersionUID = 6078186197417641456L;
/*    */ 
/*    */   public ReferenceWrapper(Reference paramReference)
/*    */     throws NamingException, RemoteException
/*    */   {
/* 53 */     this.wrappee = paramReference;
/*    */   }
/*    */ 
/*    */   public Reference getReference() throws RemoteException {
/* 57 */     return this.wrappee;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.rmi.registry.ReferenceWrapper
 * JD-Core Version:    0.6.2
 */
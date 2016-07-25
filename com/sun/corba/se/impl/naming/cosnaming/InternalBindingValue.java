/*    */ package com.sun.corba.se.impl.naming.cosnaming;
/*    */ 
/*    */ import org.omg.CosNaming.Binding;
/*    */ 
/*    */ public class InternalBindingValue
/*    */ {
/*    */   public Binding theBinding;
/*    */   public String strObjectRef;
/*    */   public org.omg.CORBA.Object theObjectRef;
/*    */ 
/*    */   public InternalBindingValue()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InternalBindingValue(Binding paramBinding, String paramString)
/*    */   {
/* 48 */     this.theBinding = paramBinding;
/* 49 */     this.strObjectRef = paramString;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.InternalBindingValue
 * JD-Core Version:    0.6.2
 */
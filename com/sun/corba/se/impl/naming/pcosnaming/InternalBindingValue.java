/*    */ package com.sun.corba.se.impl.naming.pcosnaming;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.omg.CosNaming.BindingType;
/*    */ 
/*    */ public class InternalBindingValue
/*    */   implements Serializable
/*    */ {
/*    */   public BindingType theBindingType;
/*    */   public String strObjectRef;
/*    */   private transient org.omg.CORBA.Object theObjectRef;
/*    */ 
/*    */   public InternalBindingValue()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InternalBindingValue(BindingType paramBindingType, String paramString)
/*    */   {
/* 55 */     this.theBindingType = paramBindingType;
/* 56 */     this.strObjectRef = paramString;
/*    */   }
/*    */ 
/*    */   public org.omg.CORBA.Object getObjectRef()
/*    */   {
/* 61 */     return this.theObjectRef;
/*    */   }
/*    */ 
/*    */   public void setObjectRef(org.omg.CORBA.Object paramObject)
/*    */   {
/* 66 */     this.theObjectRef = paramObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.InternalBindingValue
 * JD-Core Version:    0.6.2
 */
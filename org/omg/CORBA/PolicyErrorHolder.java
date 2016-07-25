/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class PolicyErrorHolder
/*    */   implements Streamable
/*    */ {
/* 17 */   public PolicyError value = null;
/*    */ 
/*    */   public PolicyErrorHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PolicyErrorHolder(PolicyError paramPolicyError)
/*    */   {
/* 25 */     this.value = paramPolicyError;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 30 */     this.value = PolicyErrorHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 35 */     PolicyErrorHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 40 */     return PolicyErrorHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyErrorHolder
 * JD-Core Version:    0.6.2
 */
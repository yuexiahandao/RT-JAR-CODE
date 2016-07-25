/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class PolicyHolder
/*    */   implements Streamable
/*    */ {
/* 39 */   public Policy value = null;
/*    */ 
/*    */   public PolicyHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PolicyHolder(Policy paramPolicy)
/*    */   {
/* 47 */     this.value = paramPolicy;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 52 */     this.value = PolicyHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 57 */     PolicyHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 62 */     return PolicyHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyHolder
 * JD-Core Version:    0.6.2
 */
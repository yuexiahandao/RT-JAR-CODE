/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class PolicyListHolder
/*    */   implements Streamable
/*    */ {
/* 40 */   public Policy[] value = null;
/*    */ 
/*    */   public PolicyListHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PolicyListHolder(Policy[] paramArrayOfPolicy)
/*    */   {
/* 48 */     this.value = paramArrayOfPolicy;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 53 */     this.value = PolicyListHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 58 */     PolicyListHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 63 */     return PolicyListHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PolicyListHolder
 * JD-Core Version:    0.6.2
 */
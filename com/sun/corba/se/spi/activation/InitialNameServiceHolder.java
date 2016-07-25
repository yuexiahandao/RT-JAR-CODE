/*    */ package com.sun.corba.se.spi.activation;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class InitialNameServiceHolder
/*    */   implements Streamable
/*    */ {
/* 12 */   public InitialNameService value = null;
/*    */ 
/*    */   public InitialNameServiceHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public InitialNameServiceHolder(InitialNameService paramInitialNameService)
/*    */   {
/* 20 */     this.value = paramInitialNameService;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 25 */     this.value = InitialNameServiceHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 30 */     InitialNameServiceHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 35 */     return InitialNameServiceHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation.InitialNameServiceHolder
 * JD-Core Version:    0.6.2
 */
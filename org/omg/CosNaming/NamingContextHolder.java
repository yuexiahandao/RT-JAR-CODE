/*    */ package org.omg.CosNaming;
/*    */ 
/*    */ import org.omg.CORBA.TypeCode;
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class NamingContextHolder
/*    */   implements Streamable
/*    */ {
/* 22 */   public NamingContext value = null;
/*    */ 
/*    */   public NamingContextHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NamingContextHolder(NamingContext paramNamingContext)
/*    */   {
/* 30 */     this.value = paramNamingContext;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 35 */     this.value = NamingContextHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 40 */     NamingContextHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 45 */     return NamingContextHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CosNaming.NamingContextHolder
 * JD-Core Version:    0.6.2
 */
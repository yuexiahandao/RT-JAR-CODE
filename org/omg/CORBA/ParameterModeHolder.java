/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ public final class ParameterModeHolder
/*    */   implements Streamable
/*    */ {
/* 21 */   public ParameterMode value = null;
/*    */ 
/*    */   public ParameterModeHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ParameterModeHolder(ParameterMode paramParameterMode)
/*    */   {
/* 29 */     this.value = paramParameterMode;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream)
/*    */   {
/* 34 */     this.value = ParameterModeHelper.read(paramInputStream);
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream)
/*    */   {
/* 39 */     ParameterModeHelper.write(paramOutputStream, this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type()
/*    */   {
/* 44 */     return ParameterModeHelper.type();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ParameterModeHolder
 * JD-Core Version:    0.6.2
 */
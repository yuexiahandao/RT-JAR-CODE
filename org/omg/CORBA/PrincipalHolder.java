/*    */ package org.omg.CORBA;
/*    */ 
/*    */ import org.omg.CORBA.portable.InputStream;
/*    */ import org.omg.CORBA.portable.OutputStream;
/*    */ import org.omg.CORBA.portable.Streamable;
/*    */ 
/*    */ @Deprecated
/*    */ public final class PrincipalHolder
/*    */   implements Streamable
/*    */ {
/*    */   public Principal value;
/*    */ 
/*    */   public PrincipalHolder()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PrincipalHolder(Principal paramPrincipal)
/*    */   {
/* 78 */     this.value = paramPrincipal;
/*    */   }
/*    */ 
/*    */   public void _read(InputStream paramInputStream) {
/* 82 */     this.value = paramInputStream.read_Principal();
/*    */   }
/*    */ 
/*    */   public void _write(OutputStream paramOutputStream) {
/* 86 */     paramOutputStream.write_Principal(this.value);
/*    */   }
/*    */ 
/*    */   public TypeCode _type() {
/* 90 */     return ORB.init().get_primitive_tc(TCKind.tk_Principal);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.PrincipalHolder
 * JD-Core Version:    0.6.2
 */
/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class IntHolder
/*     */   implements Streamable
/*     */ {
/*     */   public int value;
/*     */ 
/*     */   public IntHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public IntHolder(int paramInt)
/*     */   {
/*  76 */     this.value = paramInt;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  87 */     this.value = paramInputStream.read_long();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  98 */     paramOutputStream.write_long(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 110 */     return ORB.init().get_primitive_tc(TCKind.tk_long);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.IntHolder
 * JD-Core Version:    0.6.2
 */
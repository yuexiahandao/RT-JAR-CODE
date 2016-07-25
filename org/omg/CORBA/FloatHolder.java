/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class FloatHolder
/*     */   implements Streamable
/*     */ {
/*     */   public float value;
/*     */ 
/*     */   public FloatHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public FloatHolder(float paramFloat)
/*     */   {
/*  75 */     this.value = paramFloat;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  85 */     this.value = paramInputStream.read_float();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  94 */     paramOutputStream.write_float(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 103 */     return ORB.init().get_primitive_tc(TCKind.tk_float);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.FloatHolder
 * JD-Core Version:    0.6.2
 */
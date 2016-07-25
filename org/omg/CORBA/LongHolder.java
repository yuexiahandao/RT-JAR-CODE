/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class LongHolder
/*     */   implements Streamable
/*     */ {
/*     */   public long value;
/*     */ 
/*     */   public LongHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public LongHolder(long paramLong)
/*     */   {
/*  76 */     this.value = paramLong;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  86 */     this.value = paramInputStream.read_longlong();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  95 */     paramOutputStream.write_longlong(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 105 */     return ORB.init().get_primitive_tc(TCKind.tk_longlong);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.LongHolder
 * JD-Core Version:    0.6.2
 */
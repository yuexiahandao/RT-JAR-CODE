/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class ShortHolder
/*     */   implements Streamable
/*     */ {
/*     */   public short value;
/*     */ 
/*     */   public ShortHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public ShortHolder(short paramShort)
/*     */   {
/*  76 */     this.value = paramShort;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  87 */     this.value = paramInputStream.read_short();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  97 */     paramOutputStream.write_short(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 108 */     return ORB.init().get_primitive_tc(TCKind.tk_short);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.ShortHolder
 * JD-Core Version:    0.6.2
 */
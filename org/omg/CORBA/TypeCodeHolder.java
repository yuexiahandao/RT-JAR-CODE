/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class TypeCodeHolder
/*     */   implements Streamable
/*     */ {
/*     */   public TypeCode value;
/*     */ 
/*     */   public TypeCodeHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TypeCodeHolder(TypeCode paramTypeCode)
/*     */   {
/*  76 */     this.value = paramTypeCode;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  87 */     this.value = paramInputStream.read_TypeCode();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  97 */     paramOutputStream.write_TypeCode(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 108 */     return ORB.init().get_primitive_tc(TCKind.tk_TypeCode);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TypeCodeHolder
 * JD-Core Version:    0.6.2
 */
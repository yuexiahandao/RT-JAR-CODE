/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.Streamable;
/*     */ 
/*     */ public final class CharHolder
/*     */   implements Streamable
/*     */ {
/*     */   public char value;
/*     */ 
/*     */   public CharHolder()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CharHolder(char paramChar)
/*     */   {
/*  76 */     this.value = paramChar;
/*     */   }
/*     */ 
/*     */   public void _read(InputStream paramInputStream)
/*     */   {
/*  87 */     this.value = paramInputStream.read_char();
/*     */   }
/*     */ 
/*     */   public void _write(OutputStream paramOutputStream)
/*     */   {
/*  97 */     paramOutputStream.write_char(this.value);
/*     */   }
/*     */ 
/*     */   public TypeCode _type()
/*     */   {
/* 109 */     return ORB.init().get_primitive_tc(TCKind.tk_char);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.CharHolder
 * JD-Core Version:    0.6.2
 */
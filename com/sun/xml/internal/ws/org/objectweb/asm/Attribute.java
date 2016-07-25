/*     */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*     */ 
/*     */ public class Attribute
/*     */ {
/*     */   public final String type;
/*     */   byte[] value;
/*     */   Attribute next;
/*     */ 
/*     */   protected Attribute(String type)
/*     */   {
/*  91 */     this.type = type;
/*     */   }
/*     */ 
/*     */   public boolean isUnknown()
/*     */   {
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isCodeAttribute()
/*     */   {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   protected Label[] getLabels()
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   protected Attribute read(ClassReader cr, int off, int len, char[] buf, int codeOff, Label[] labels)
/*     */   {
/* 157 */     Attribute attr = new Attribute(this.type);
/* 158 */     attr.value = new byte[len];
/* 159 */     System.arraycopy(cr.b, off, attr.value, 0, len);
/* 160 */     return attr;
/*     */   }
/*     */ 
/*     */   protected ByteVector write(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals)
/*     */   {
/* 190 */     ByteVector v = new ByteVector();
/* 191 */     v.data = this.value;
/* 192 */     v.length = this.value.length;
/* 193 */     return v;
/*     */   }
/*     */ 
/*     */   final int getCount()
/*     */   {
/* 202 */     int count = 0;
/* 203 */     Attribute attr = this;
/* 204 */     while (attr != null) {
/* 205 */       count++;
/* 206 */       attr = attr.next;
/*     */     }
/* 208 */     return count;
/*     */   }
/*     */ 
/*     */   final int getSize(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals)
/*     */   {
/* 238 */     Attribute attr = this;
/* 239 */     int size = 0;
/* 240 */     while (attr != null) {
/* 241 */       cw.newUTF8(attr.type);
/* 242 */       size += attr.write(cw, code, len, maxStack, maxLocals).length + 6;
/* 243 */       attr = attr.next;
/*     */     }
/* 245 */     return size;
/*     */   }
/*     */ 
/*     */   final void put(ClassWriter cw, byte[] code, int len, int maxStack, int maxLocals, ByteVector out)
/*     */   {
/* 276 */     Attribute attr = this;
/* 277 */     while (attr != null) {
/* 278 */       ByteVector b = attr.write(cw, code, len, maxStack, maxLocals);
/* 279 */       out.putShort(cw.newUTF8(attr.type)).putInt(b.length);
/* 280 */       out.putByteArray(b.data, 0, b.length);
/* 281 */       attr = attr.next;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.Attribute
 * JD-Core Version:    0.6.2
 */
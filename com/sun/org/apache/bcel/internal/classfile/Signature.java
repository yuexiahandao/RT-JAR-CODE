/*     */ package com.sun.org.apache.bcel.internal.classfile;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public final class Signature extends Attribute
/*     */ {
/*     */   private int signature_index;
/*     */ 
/*     */   public Signature(Signature c)
/*     */   {
/*  79 */     this(c.getNameIndex(), c.getLength(), c.getSignatureIndex(), c.getConstantPool());
/*     */   }
/*     */ 
/*     */   Signature(int name_index, int length, DataInputStream file, ConstantPool constant_pool)
/*     */     throws IOException
/*     */   {
/*  93 */     this(name_index, length, file.readUnsignedShort(), constant_pool);
/*     */   }
/*     */ 
/*     */   public Signature(int name_index, int length, int signature_index, ConstantPool constant_pool)
/*     */   {
/* 105 */     super((byte)10, name_index, length, constant_pool);
/* 106 */     this.signature_index = signature_index;
/*     */   }
/*     */ 
/*     */   public void accept(Visitor v)
/*     */   {
/* 117 */     System.err.println("Visiting non-standard Signature object");
/* 118 */     v.visitSignature(this);
/*     */   }
/*     */ 
/*     */   public final void dump(DataOutputStream file)
/*     */     throws IOException
/*     */   {
/* 129 */     super.dump(file);
/* 130 */     file.writeShort(this.signature_index);
/*     */   }
/*     */ 
/*     */   public final int getSignatureIndex()
/*     */   {
/* 136 */     return this.signature_index;
/*     */   }
/*     */ 
/*     */   public final void setSignatureIndex(int signature_index)
/*     */   {
/* 142 */     this.signature_index = signature_index;
/*     */   }
/*     */ 
/*     */   public final String getSignature()
/*     */   {
/* 149 */     ConstantUtf8 c = (ConstantUtf8)this.constant_pool.getConstant(this.signature_index, (byte)1);
/*     */ 
/* 151 */     return c.getBytes();
/*     */   }
/*     */ 
/*     */   private static boolean identStart(int ch)
/*     */   {
/* 166 */     return (ch == 84) || (ch == 76);
/*     */   }
/*     */ 
/*     */   private static boolean identPart(int ch) {
/* 170 */     return (ch == 47) || (ch == 59);
/*     */   }
/*     */ 
/*     */   private static final void matchIdent(MyByteArrayInputStream in, StringBuffer buf)
/*     */   {
/* 176 */     if ((ch = in.read()) == -1) {
/* 177 */       throw new RuntimeException("Illegal signature: " + in.getData() + " no ident, reaching EOF");
/*     */     }
/*     */ 
/* 182 */     if (!identStart(ch)) {
/* 183 */       StringBuffer buf2 = new StringBuffer();
/*     */ 
/* 185 */       int count = 1;
/* 186 */       while (Character.isJavaIdentifierPart((char)ch)) {
/* 187 */         buf2.append((char)ch);
/* 188 */         count++;
/* 189 */         ch = in.read();
/*     */       }
/*     */ 
/* 192 */       if (ch == 58) {
/* 193 */         in.skip("Ljava/lang/Object".length());
/* 194 */         buf.append(buf2);
/*     */ 
/* 196 */         ch = in.read();
/* 197 */         in.unread();
/*     */       }
/*     */       else {
/* 200 */         for (int i = 0; i < count; i++) {
/* 201 */           in.unread();
/*     */         }
/*     */       }
/* 204 */       return;
/*     */     }
/*     */ 
/* 207 */     StringBuffer buf2 = new StringBuffer();
/* 208 */     int ch = in.read();
/*     */     do
/*     */     {
/* 211 */       buf2.append((char)ch);
/* 212 */       ch = in.read();
/*     */     }
/*     */ 
/* 215 */     while ((ch != -1) && ((Character.isJavaIdentifierPart((char)ch)) || (ch == 47)));
/*     */ 
/* 217 */     buf.append(buf2.toString().replace('/', '.'));
/*     */ 
/* 221 */     if (ch != -1)
/* 222 */       in.unread();
/*     */   }
/*     */ 
/*     */   private static final void matchGJIdent(MyByteArrayInputStream in, StringBuffer buf)
/*     */   {
/* 230 */     matchIdent(in, buf);
/*     */ 
/* 232 */     int ch = in.read();
/* 233 */     if ((ch == 60) || (ch == 40))
/*     */     {
/* 235 */       buf.append((char)ch);
/* 236 */       matchGJIdent(in, buf);
/*     */ 
/* 238 */       while (((ch = in.read()) != 62) && (ch != 41)) {
/* 239 */         if (ch == -1) {
/* 240 */           throw new RuntimeException("Illegal signature: " + in.getData() + " reaching EOF");
/*     */         }
/*     */ 
/* 244 */         buf.append(", ");
/* 245 */         in.unread();
/* 246 */         matchGJIdent(in, buf);
/*     */       }
/*     */ 
/* 251 */       buf.append((char)ch);
/*     */     } else {
/* 253 */       in.unread();
/*     */     }
/* 255 */     ch = in.read();
/* 256 */     if (identStart(ch)) {
/* 257 */       in.unread();
/* 258 */       matchGJIdent(in, buf); } else {
/* 259 */       if (ch == 41) {
/* 260 */         in.unread();
/* 261 */         return;
/* 262 */       }if (ch != 59)
/* 263 */         throw new RuntimeException("Illegal signature: " + in.getData() + " read " + (char)ch);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String translate(String s)
/*     */   {
/* 269 */     StringBuffer buf = new StringBuffer();
/*     */ 
/* 271 */     matchGJIdent(new MyByteArrayInputStream(s), buf);
/*     */ 
/* 273 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   public static final boolean isFormalParameterList(String s) {
/* 277 */     return (s.startsWith("<")) && (s.indexOf(':') > 0);
/*     */   }
/*     */ 
/*     */   public static final boolean isActualParameterList(String s) {
/* 281 */     return (s.startsWith("L")) && (s.endsWith(">;"));
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 288 */     String s = getSignature();
/*     */ 
/* 290 */     return "Signature(" + s + ")";
/*     */   }
/*     */ 
/*     */   public Attribute copy(ConstantPool constant_pool)
/*     */   {
/* 297 */     return (Signature)clone();
/*     */   }
/*     */ 
/*     */   private static final class MyByteArrayInputStream extends ByteArrayInputStream
/*     */   {
/*     */     MyByteArrayInputStream(String data)
/*     */     {
/* 158 */       super(); } 
/* 159 */     final int mark() { return this.pos; } 
/* 160 */     final String getData() { return new String(this.buf); } 
/* 161 */     final void reset(int p) { this.pos = p; } 
/* 162 */     final void unread() { if (this.pos > 0) this.pos -= 1;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.bcel.internal.classfile.Signature
 * JD-Core Version:    0.6.2
 */
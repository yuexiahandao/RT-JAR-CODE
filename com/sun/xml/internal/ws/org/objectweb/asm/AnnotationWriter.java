/*     */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*     */ 
/*     */ final class AnnotationWriter
/*     */   implements AnnotationVisitor
/*     */ {
/*     */   private final ClassWriter cw;
/*     */   private int size;
/*     */   private final boolean named;
/*     */   private final ByteVector bv;
/*     */   private final ByteVector parent;
/*     */   private final int offset;
/*     */   AnnotationWriter next;
/*     */   AnnotationWriter prev;
/*     */ 
/*     */   AnnotationWriter(ClassWriter cw, boolean named, ByteVector bv, ByteVector parent, int offset)
/*     */   {
/* 137 */     this.cw = cw;
/* 138 */     this.named = named;
/* 139 */     this.bv = bv;
/* 140 */     this.parent = parent;
/* 141 */     this.offset = offset;
/*     */   }
/*     */ 
/*     */   public void visit(String name, Object value)
/*     */   {
/* 149 */     this.size += 1;
/* 150 */     if (this.named) {
/* 151 */       this.bv.putShort(this.cw.newUTF8(name));
/*     */     }
/* 153 */     if ((value instanceof String)) {
/* 154 */       this.bv.put12(115, this.cw.newUTF8((String)value));
/* 155 */     } else if ((value instanceof Byte)) {
/* 156 */       this.bv.put12(66, this.cw.newInteger(((Byte)value).byteValue()).index);
/* 157 */     } else if ((value instanceof Boolean)) {
/* 158 */       int v = ((Boolean)value).booleanValue() ? 1 : 0;
/* 159 */       this.bv.put12(90, this.cw.newInteger(v).index);
/* 160 */     } else if ((value instanceof Character)) {
/* 161 */       this.bv.put12(67, this.cw.newInteger(((Character)value).charValue()).index);
/* 162 */     } else if ((value instanceof Short)) {
/* 163 */       this.bv.put12(83, this.cw.newInteger(((Short)value).shortValue()).index);
/* 164 */     } else if ((value instanceof Type)) {
/* 165 */       this.bv.put12(99, this.cw.newUTF8(((Type)value).getDescriptor()));
/* 166 */     } else if ((value instanceof byte[])) {
/* 167 */       byte[] v = (byte[])value;
/* 168 */       this.bv.put12(91, v.length);
/* 169 */       for (int i = 0; i < v.length; i++)
/* 170 */         this.bv.put12(66, this.cw.newInteger(v[i]).index);
/*     */     }
/* 172 */     else if ((value instanceof boolean[])) {
/* 173 */       boolean[] v = (boolean[])value;
/* 174 */       this.bv.put12(91, v.length);
/* 175 */       for (int i = 0; i < v.length; i++)
/* 176 */         this.bv.put12(90, this.cw.newInteger(v[i] != 0 ? 1 : 0).index);
/*     */     }
/* 178 */     else if ((value instanceof short[])) {
/* 179 */       short[] v = (short[])value;
/* 180 */       this.bv.put12(91, v.length);
/* 181 */       for (int i = 0; i < v.length; i++)
/* 182 */         this.bv.put12(83, this.cw.newInteger(v[i]).index);
/*     */     }
/* 184 */     else if ((value instanceof char[])) {
/* 185 */       char[] v = (char[])value;
/* 186 */       this.bv.put12(91, v.length);
/* 187 */       for (int i = 0; i < v.length; i++)
/* 188 */         this.bv.put12(67, this.cw.newInteger(v[i]).index);
/*     */     }
/* 190 */     else if ((value instanceof int[])) {
/* 191 */       int[] v = (int[])value;
/* 192 */       this.bv.put12(91, v.length);
/* 193 */       for (int i = 0; i < v.length; i++)
/* 194 */         this.bv.put12(73, this.cw.newInteger(v[i]).index);
/*     */     }
/* 196 */     else if ((value instanceof long[])) {
/* 197 */       long[] v = (long[])value;
/* 198 */       this.bv.put12(91, v.length);
/* 199 */       for (int i = 0; i < v.length; i++)
/* 200 */         this.bv.put12(74, this.cw.newLong(v[i]).index);
/*     */     }
/* 202 */     else if ((value instanceof float[])) {
/* 203 */       float[] v = (float[])value;
/* 204 */       this.bv.put12(91, v.length);
/* 205 */       for (int i = 0; i < v.length; i++)
/* 206 */         this.bv.put12(70, this.cw.newFloat(v[i]).index);
/*     */     }
/* 208 */     else if ((value instanceof double[])) {
/* 209 */       double[] v = (double[])value;
/* 210 */       this.bv.put12(91, v.length);
/* 211 */       for (int i = 0; i < v.length; i++)
/* 212 */         this.bv.put12(68, this.cw.newDouble(v[i]).index);
/*     */     }
/*     */     else {
/* 215 */       Item i = this.cw.newConstItem(value);
/* 216 */       this.bv.put12(".s.IFJDCS".charAt(i.type), i.index);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void visitEnum(String name, String desc, String value)
/*     */   {
/* 225 */     this.size += 1;
/* 226 */     if (this.named) {
/* 227 */       this.bv.putShort(this.cw.newUTF8(name));
/*     */     }
/* 229 */     this.bv.put12(101, this.cw.newUTF8(desc)).putShort(this.cw.newUTF8(value));
/*     */   }
/*     */ 
/*     */   public AnnotationVisitor visitAnnotation(String name, String desc)
/*     */   {
/* 236 */     this.size += 1;
/* 237 */     if (this.named) {
/* 238 */       this.bv.putShort(this.cw.newUTF8(name));
/*     */     }
/*     */ 
/* 241 */     this.bv.put12(64, this.cw.newUTF8(desc)).putShort(0);
/* 242 */     return new AnnotationWriter(this.cw, true, this.bv, this.bv, this.bv.length - 2);
/*     */   }
/*     */ 
/*     */   public AnnotationVisitor visitArray(String name) {
/* 246 */     this.size += 1;
/* 247 */     if (this.named) {
/* 248 */       this.bv.putShort(this.cw.newUTF8(name));
/*     */     }
/*     */ 
/* 251 */     this.bv.put12(91, 0);
/* 252 */     return new AnnotationWriter(this.cw, false, this.bv, this.bv, this.bv.length - 2);
/*     */   }
/*     */ 
/*     */   public void visitEnd() {
/* 256 */     if (this.parent != null) {
/* 257 */       byte[] data = this.parent.data;
/* 258 */       data[this.offset] = ((byte)(this.size >>> 8));
/* 259 */       data[(this.offset + 1)] = ((byte)this.size);
/*     */     }
/*     */   }
/*     */ 
/*     */   int getSize()
/*     */   {
/* 273 */     int size = 0;
/* 274 */     AnnotationWriter aw = this;
/* 275 */     while (aw != null) {
/* 276 */       size += aw.bv.length;
/* 277 */       aw = aw.next;
/*     */     }
/* 279 */     return size;
/*     */   }
/*     */ 
/*     */   void put(ByteVector out)
/*     */   {
/* 289 */     int n = 0;
/* 290 */     int size = 2;
/* 291 */     AnnotationWriter aw = this;
/* 292 */     AnnotationWriter last = null;
/* 293 */     while (aw != null) {
/* 294 */       n++;
/* 295 */       size += aw.bv.length;
/* 296 */       aw.visitEnd();
/* 297 */       aw.prev = last;
/* 298 */       last = aw;
/* 299 */       aw = aw.next;
/*     */     }
/* 301 */     out.putInt(size);
/* 302 */     out.putShort(n);
/* 303 */     aw = last;
/* 304 */     while (aw != null) {
/* 305 */       out.putByteArray(aw.bv.data, 0, aw.bv.length);
/* 306 */       aw = aw.prev;
/*     */     }
/*     */   }
/*     */ 
/*     */   static void put(AnnotationWriter[] panns, int off, ByteVector out)
/*     */   {
/* 322 */     int size = 1 + 2 * (panns.length - off);
/* 323 */     for (int i = off; i < panns.length; i++) {
/* 324 */       size += (panns[i] == null ? 0 : panns[i].getSize());
/*     */     }
/* 326 */     out.putInt(size).putByte(panns.length - off);
/* 327 */     for (int i = off; i < panns.length; i++) {
/* 328 */       AnnotationWriter aw = panns[i];
/* 329 */       AnnotationWriter last = null;
/* 330 */       int n = 0;
/* 331 */       while (aw != null) {
/* 332 */         n++;
/* 333 */         aw.visitEnd();
/* 334 */         aw.prev = last;
/* 335 */         last = aw;
/* 336 */         aw = aw.next;
/*     */       }
/* 338 */       out.putShort(n);
/* 339 */       aw = last;
/* 340 */       while (aw != null) {
/* 341 */         out.putByteArray(aw.bv.data, 0, aw.bv.length);
/* 342 */         aw = aw.prev;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.AnnotationWriter
 * JD-Core Version:    0.6.2
 */
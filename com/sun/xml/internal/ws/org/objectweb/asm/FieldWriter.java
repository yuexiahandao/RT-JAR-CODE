/*     */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*     */ 
/*     */ final class FieldWriter
/*     */   implements FieldVisitor
/*     */ {
/*     */   FieldWriter next;
/*     */   private final ClassWriter cw;
/*     */   private final int access;
/*     */   private final int name;
/*     */   private final int desc;
/*     */   private int signature;
/*     */   private int value;
/*     */   private AnnotationWriter anns;
/*     */   private AnnotationWriter ianns;
/*     */   private Attribute attrs;
/*     */ 
/*     */   FieldWriter(ClassWriter cw, int access, String name, String desc, String signature, Object value)
/*     */   {
/* 145 */     if (cw.firstField == null)
/* 146 */       cw.firstField = this;
/*     */     else {
/* 148 */       cw.lastField.next = this;
/*     */     }
/* 150 */     cw.lastField = this;
/* 151 */     this.cw = cw;
/* 152 */     this.access = access;
/* 153 */     this.name = cw.newUTF8(name);
/* 154 */     this.desc = cw.newUTF8(desc);
/* 155 */     if (signature != null) {
/* 156 */       this.signature = cw.newUTF8(signature);
/*     */     }
/* 158 */     if (value != null)
/* 159 */       this.value = cw.newConstItem(value).index;
/*     */   }
/*     */ 
/*     */   public AnnotationVisitor visitAnnotation(String desc, boolean visible)
/*     */   {
/* 174 */     ByteVector bv = new ByteVector();
/*     */ 
/* 176 */     bv.putShort(this.cw.newUTF8(desc)).putShort(0);
/* 177 */     AnnotationWriter aw = new AnnotationWriter(this.cw, true, bv, bv, 2);
/* 178 */     if (visible) {
/* 179 */       aw.next = this.anns;
/* 180 */       this.anns = aw;
/*     */     } else {
/* 182 */       aw.next = this.ianns;
/* 183 */       this.ianns = aw;
/*     */     }
/* 185 */     return aw;
/*     */   }
/*     */ 
/*     */   public void visitAttribute(Attribute attr) {
/* 189 */     attr.next = this.attrs;
/* 190 */     this.attrs = attr;
/*     */   }
/*     */ 
/*     */   public void visitEnd()
/*     */   {
/*     */   }
/*     */ 
/*     */   int getSize()
/*     */   {
/* 206 */     int size = 8;
/* 207 */     if (this.value != 0) {
/* 208 */       this.cw.newUTF8("ConstantValue");
/* 209 */       size += 8;
/*     */     }
/* 211 */     if (((this.access & 0x1000) != 0) && ((this.cw.version & 0xFFFF) < 49))
/*     */     {
/* 214 */       this.cw.newUTF8("Synthetic");
/* 215 */       size += 6;
/*     */     }
/* 217 */     if ((this.access & 0x20000) != 0) {
/* 218 */       this.cw.newUTF8("Deprecated");
/* 219 */       size += 6;
/*     */     }
/* 221 */     if (this.signature != 0) {
/* 222 */       this.cw.newUTF8("Signature");
/* 223 */       size += 8;
/*     */     }
/* 225 */     if (this.anns != null) {
/* 226 */       this.cw.newUTF8("RuntimeVisibleAnnotations");
/* 227 */       size += 8 + this.anns.getSize();
/*     */     }
/* 229 */     if (this.ianns != null) {
/* 230 */       this.cw.newUTF8("RuntimeInvisibleAnnotations");
/* 231 */       size += 8 + this.ianns.getSize();
/*     */     }
/* 233 */     if (this.attrs != null) {
/* 234 */       size += this.attrs.getSize(this.cw, null, 0, -1, -1);
/*     */     }
/* 236 */     return size;
/*     */   }
/*     */ 
/*     */   void put(ByteVector out)
/*     */   {
/* 245 */     out.putShort(this.access).putShort(this.name).putShort(this.desc);
/* 246 */     int attributeCount = 0;
/* 247 */     if (this.value != 0) {
/* 248 */       attributeCount++;
/*     */     }
/* 250 */     if (((this.access & 0x1000) != 0) && ((this.cw.version & 0xFFFF) < 49))
/*     */     {
/* 253 */       attributeCount++;
/*     */     }
/* 255 */     if ((this.access & 0x20000) != 0) {
/* 256 */       attributeCount++;
/*     */     }
/* 258 */     if (this.signature != 0) {
/* 259 */       attributeCount++;
/*     */     }
/* 261 */     if (this.anns != null) {
/* 262 */       attributeCount++;
/*     */     }
/* 264 */     if (this.ianns != null) {
/* 265 */       attributeCount++;
/*     */     }
/* 267 */     if (this.attrs != null) {
/* 268 */       attributeCount += this.attrs.getCount();
/*     */     }
/* 270 */     out.putShort(attributeCount);
/* 271 */     if (this.value != 0) {
/* 272 */       out.putShort(this.cw.newUTF8("ConstantValue"));
/* 273 */       out.putInt(2).putShort(this.value);
/*     */     }
/* 275 */     if (((this.access & 0x1000) != 0) && ((this.cw.version & 0xFFFF) < 49))
/*     */     {
/* 278 */       out.putShort(this.cw.newUTF8("Synthetic")).putInt(0);
/*     */     }
/* 280 */     if ((this.access & 0x20000) != 0) {
/* 281 */       out.putShort(this.cw.newUTF8("Deprecated")).putInt(0);
/*     */     }
/* 283 */     if (this.signature != 0) {
/* 284 */       out.putShort(this.cw.newUTF8("Signature"));
/* 285 */       out.putInt(2).putShort(this.signature);
/*     */     }
/* 287 */     if (this.anns != null) {
/* 288 */       out.putShort(this.cw.newUTF8("RuntimeVisibleAnnotations"));
/* 289 */       this.anns.put(out);
/*     */     }
/* 291 */     if (this.ianns != null) {
/* 292 */       out.putShort(this.cw.newUTF8("RuntimeInvisibleAnnotations"));
/* 293 */       this.ianns.put(out);
/*     */     }
/* 295 */     if (this.attrs != null)
/* 296 */       this.attrs.put(this.cw, null, 0, -1, -1, out);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.FieldWriter
 * JD-Core Version:    0.6.2
 */
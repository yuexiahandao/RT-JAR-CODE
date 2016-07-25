/*     */ package com.sun.xml.internal.ws.org.objectweb.asm;
/*     */ 
/*     */ public class ByteVector
/*     */ {
/*     */   byte[] data;
/*     */   int length;
/*     */ 
/*     */   public ByteVector()
/*     */   {
/*  85 */     this.data = new byte[64];
/*     */   }
/*     */ 
/*     */   public ByteVector(int initialSize)
/*     */   {
/*  95 */     this.data = new byte[initialSize];
/*     */   }
/*     */ 
/*     */   public ByteVector putByte(int b)
/*     */   {
/* 106 */     int length = this.length;
/* 107 */     if (length + 1 > this.data.length) {
/* 108 */       enlarge(1);
/*     */     }
/* 110 */     this.data[(length++)] = ((byte)b);
/* 111 */     this.length = length;
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   ByteVector put11(int b1, int b2)
/*     */   {
/* 124 */     int length = this.length;
/* 125 */     if (length + 2 > this.data.length) {
/* 126 */       enlarge(2);
/*     */     }
/* 128 */     byte[] data = this.data;
/* 129 */     data[(length++)] = ((byte)b1);
/* 130 */     data[(length++)] = ((byte)b2);
/* 131 */     this.length = length;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteVector putShort(int s)
/*     */   {
/* 143 */     int length = this.length;
/* 144 */     if (length + 2 > this.data.length) {
/* 145 */       enlarge(2);
/*     */     }
/* 147 */     byte[] data = this.data;
/* 148 */     data[(length++)] = ((byte)(s >>> 8));
/* 149 */     data[(length++)] = ((byte)s);
/* 150 */     this.length = length;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */   ByteVector put12(int b, int s)
/*     */   {
/* 163 */     int length = this.length;
/* 164 */     if (length + 3 > this.data.length) {
/* 165 */       enlarge(3);
/*     */     }
/* 167 */     byte[] data = this.data;
/* 168 */     data[(length++)] = ((byte)b);
/* 169 */     data[(length++)] = ((byte)(s >>> 8));
/* 170 */     data[(length++)] = ((byte)s);
/* 171 */     this.length = length;
/* 172 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteVector putInt(int i)
/*     */   {
/* 183 */     int length = this.length;
/* 184 */     if (length + 4 > this.data.length) {
/* 185 */       enlarge(4);
/*     */     }
/* 187 */     byte[] data = this.data;
/* 188 */     data[(length++)] = ((byte)(i >>> 24));
/* 189 */     data[(length++)] = ((byte)(i >>> 16));
/* 190 */     data[(length++)] = ((byte)(i >>> 8));
/* 191 */     data[(length++)] = ((byte)i);
/* 192 */     this.length = length;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteVector putLong(long l)
/*     */   {
/* 204 */     int length = this.length;
/* 205 */     if (length + 8 > this.data.length) {
/* 206 */       enlarge(8);
/*     */     }
/* 208 */     byte[] data = this.data;
/* 209 */     int i = (int)(l >>> 32);
/* 210 */     data[(length++)] = ((byte)(i >>> 24));
/* 211 */     data[(length++)] = ((byte)(i >>> 16));
/* 212 */     data[(length++)] = ((byte)(i >>> 8));
/* 213 */     data[(length++)] = ((byte)i);
/* 214 */     i = (int)l;
/* 215 */     data[(length++)] = ((byte)(i >>> 24));
/* 216 */     data[(length++)] = ((byte)(i >>> 16));
/* 217 */     data[(length++)] = ((byte)(i >>> 8));
/* 218 */     data[(length++)] = ((byte)i);
/* 219 */     this.length = length;
/* 220 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteVector putUTF8(String s)
/*     */   {
/* 231 */     int charLength = s.length();
/* 232 */     if (this.length + 2 + charLength > this.data.length) {
/* 233 */       enlarge(2 + charLength);
/*     */     }
/* 235 */     int len = this.length;
/* 236 */     byte[] data = this.data;
/*     */ 
/* 243 */     data[(len++)] = ((byte)(charLength >>> 8));
/* 244 */     data[(len++)] = ((byte)charLength);
/* 245 */     for (int i = 0; i < charLength; i++) {
/* 246 */       char c = s.charAt(i);
/* 247 */       if ((c >= '\001') && (c <= '')) {
/* 248 */         data[(len++)] = ((byte)c);
/*     */       } else {
/* 250 */         int byteLength = i;
/* 251 */         for (int j = i; j < charLength; j++) {
/* 252 */           c = s.charAt(j);
/* 253 */           if ((c >= '\001') && (c <= ''))
/* 254 */             byteLength++;
/* 255 */           else if (c > '߿')
/* 256 */             byteLength += 3;
/*     */           else {
/* 258 */             byteLength += 2;
/*     */           }
/*     */         }
/* 261 */         data[this.length] = ((byte)(byteLength >>> 8));
/* 262 */         data[(this.length + 1)] = ((byte)byteLength);
/* 263 */         if (this.length + 2 + byteLength > data.length) {
/* 264 */           this.length = len;
/* 265 */           enlarge(2 + byteLength);
/* 266 */           data = this.data;
/*     */         }
/* 268 */         for (int j = i; j < charLength; j++) {
/* 269 */           c = s.charAt(j);
/* 270 */           if ((c >= '\001') && (c <= '')) {
/* 271 */             data[(len++)] = ((byte)c);
/* 272 */           } else if (c > '߿') {
/* 273 */             data[(len++)] = ((byte)(0xE0 | c >> '\f' & 0xF));
/* 274 */             data[(len++)] = ((byte)(0x80 | c >> '\006' & 0x3F));
/* 275 */             data[(len++)] = ((byte)(0x80 | c & 0x3F));
/*     */           } else {
/* 277 */             data[(len++)] = ((byte)(0xC0 | c >> '\006' & 0x1F));
/* 278 */             data[(len++)] = ((byte)(0x80 | c & 0x3F));
/*     */           }
/*     */         }
/* 281 */         break;
/*     */       }
/*     */     }
/* 284 */     this.length = len;
/* 285 */     return this;
/*     */   }
/*     */ 
/*     */   public ByteVector putByteArray(byte[] b, int off, int len)
/*     */   {
/* 300 */     if (this.length + len > this.data.length) {
/* 301 */       enlarge(len);
/*     */     }
/* 303 */     if (b != null) {
/* 304 */       System.arraycopy(b, off, this.data, this.length, len);
/*     */     }
/* 306 */     this.length += len;
/* 307 */     return this;
/*     */   }
/*     */ 
/*     */   private void enlarge(int size)
/*     */   {
/* 317 */     int length1 = 2 * this.data.length;
/* 318 */     int length2 = this.length + size;
/* 319 */     byte[] newData = new byte[length1 > length2 ? length1 : length2];
/* 320 */     System.arraycopy(this.data, 0, newData, 0, this.length);
/* 321 */     this.data = newData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.org.objectweb.asm.ByteVector
 * JD-Core Version:    0.6.2
 */
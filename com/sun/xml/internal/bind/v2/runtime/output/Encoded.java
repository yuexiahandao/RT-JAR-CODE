/*     */ package com.sun.xml.internal.bind.v2.runtime.output;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class Encoded
/*     */ {
/*     */   public byte[] buf;
/*     */   public int len;
/* 179 */   private static final byte[][] entities = new byte[''][];
/* 180 */   private static final byte[][] attributeEntities = new byte[''][];
/*     */ 
/*     */   public Encoded()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Encoded(String text)
/*     */   {
/*  45 */     set(text);
/*     */   }
/*     */ 
/*     */   public void ensureSize(int size) {
/*  49 */     if ((this.buf == null) || (this.buf.length < size))
/*  50 */       this.buf = new byte[size];
/*     */   }
/*     */ 
/*     */   public final void set(String text) {
/*  54 */     int length = text.length();
/*     */ 
/*  56 */     ensureSize(length * 3 + 1);
/*     */ 
/*  58 */     int ptr = 0;
/*     */ 
/*  60 */     for (int i = 0; i < length; i++) {
/*  61 */       char chr = text.charAt(i);
/*  62 */       if (chr > '') {
/*  63 */         if (chr > '߿') {
/*  64 */           if ((55296 <= chr) && (chr <= 57343))
/*     */           {
/*  66 */             int uc = ((chr & 0x3FF) << '\n' | text.charAt(++i) & 0x3FF) + 65536;
/*     */ 
/*  68 */             this.buf[(ptr++)] = ((byte)(0xF0 | uc >> 18));
/*  69 */             this.buf[(ptr++)] = ((byte)(0x80 | uc >> 12 & 0x3F));
/*  70 */             this.buf[(ptr++)] = ((byte)(0x80 | uc >> 6 & 0x3F));
/*  71 */             this.buf[(ptr++)] = ((byte)(128 + (uc & 0x3F)));
/*  72 */             continue;
/*     */           }
/*  74 */           this.buf[(ptr++)] = ((byte)(224 + (chr >> '\f')));
/*  75 */           this.buf[(ptr++)] = ((byte)(128 + (chr >> '\006' & 0x3F)));
/*     */         } else {
/*  77 */           this.buf[(ptr++)] = ((byte)(192 + (chr >> '\006')));
/*     */         }
/*  79 */         this.buf[(ptr++)] = ((byte)('' + (chr & 0x3F)));
/*     */       } else {
/*  81 */         this.buf[(ptr++)] = ((byte)chr);
/*     */       }
/*     */     }
/*     */ 
/*  85 */     this.len = ptr;
/*     */   }
/*     */ 
/*     */   public final void setEscape(String text, boolean isAttribute)
/*     */   {
/*  96 */     int length = text.length();
/*  97 */     ensureSize(length * 6 + 1);
/*     */ 
/*  99 */     int ptr = 0;
/*     */ 
/* 101 */     for (int i = 0; i < length; i++) {
/* 102 */       char chr = text.charAt(i);
/*     */ 
/* 104 */       int ptr1 = ptr;
/* 105 */       if (chr > '') {
/* 106 */         if (chr > '߿') {
/* 107 */           if ((55296 <= chr) && (chr <= 57343))
/*     */           {
/* 109 */             int uc = ((chr & 0x3FF) << '\n' | text.charAt(++i) & 0x3FF) + 65536;
/*     */ 
/* 111 */             this.buf[(ptr++)] = ((byte)(0xF0 | uc >> 18));
/* 112 */             this.buf[(ptr++)] = ((byte)(0x80 | uc >> 12 & 0x3F));
/* 113 */             this.buf[(ptr++)] = ((byte)(0x80 | uc >> 6 & 0x3F));
/* 114 */             this.buf[(ptr++)] = ((byte)(128 + (uc & 0x3F)));
/* 115 */             continue;
/*     */           }
/* 117 */           this.buf[(ptr1++)] = ((byte)(224 + (chr >> '\f')));
/* 118 */           this.buf[(ptr1++)] = ((byte)(128 + (chr >> '\006' & 0x3F)));
/*     */         } else {
/* 120 */           this.buf[(ptr1++)] = ((byte)(192 + (chr >> '\006')));
/*     */         }
/* 122 */         this.buf[(ptr1++)] = ((byte)('' + (chr & 0x3F)));
/*     */       }
/*     */       else
/*     */       {
/*     */         byte[] ent;
/* 126 */         if ((ent = attributeEntities[chr]) != null)
/*     */         {
/* 131 */           if ((isAttribute) || (entities[chr] != null))
/* 132 */             ptr1 = writeEntity(ent, ptr1);
/*     */           else
/* 134 */             this.buf[(ptr1++)] = ((byte)chr);
/*     */         }
/* 136 */         else this.buf[(ptr1++)] = ((byte)chr);
/*     */       }
/* 138 */       ptr = ptr1;
/*     */     }
/* 140 */     this.len = ptr;
/*     */   }
/*     */ 
/*     */   private int writeEntity(byte[] entity, int ptr) {
/* 144 */     System.arraycopy(entity, 0, this.buf, ptr, entity.length);
/* 145 */     return ptr + entity.length;
/*     */   }
/*     */ 
/*     */   public final void write(UTF8XmlOutput out)
/*     */     throws IOException
/*     */   {
/* 152 */     out.write(this.buf, 0, this.len);
/*     */   }
/*     */ 
/*     */   public void append(char b)
/*     */   {
/* 160 */     this.buf[(this.len++)] = ((byte)b);
/*     */   }
/*     */ 
/*     */   public void compact()
/*     */   {
/* 168 */     byte[] b = new byte[this.len];
/* 169 */     System.arraycopy(this.buf, 0, b, 0, this.len);
/* 170 */     this.buf = b;
/*     */   }
/*     */ 
/*     */   private static void add(char c, String s, boolean attOnly)
/*     */   {
/* 193 */     byte[] image = UTF8XmlOutput.toBytes(s);
/* 194 */     attributeEntities[c] = image;
/* 195 */     if (!attOnly)
/* 196 */       entities[c] = image;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 183 */     add('&', "&amp;", false);
/* 184 */     add('<', "&lt;", false);
/* 185 */     add('>', "&gt;", false);
/* 186 */     add('"', "&quot;", false);
/* 187 */     add('\t', "&#x9;", true);
/* 188 */     add('\r', "&#xD;", false);
/* 189 */     add('\n', "&#xA;", true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.output.Encoded
 * JD-Core Version:    0.6.2
 */
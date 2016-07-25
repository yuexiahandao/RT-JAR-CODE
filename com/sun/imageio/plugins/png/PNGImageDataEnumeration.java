/*     */ package com.sun.imageio.plugins.png;
/*     */ 
/*     */ import com.sun.imageio.plugins.common.InputStreamAdapter;
/*     */ import com.sun.imageio.plugins.common.SubImageInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Enumeration;
/*     */ import javax.imageio.stream.ImageInputStream;
/*     */ 
/*     */ class PNGImageDataEnumeration
/*     */   implements Enumeration<InputStream>
/*     */ {
/*  65 */   boolean firstTime = true;
/*     */   ImageInputStream stream;
/*     */   int length;
/*     */ 
/*     */   public PNGImageDataEnumeration(ImageInputStream paramImageInputStream)
/*     */     throws IOException
/*     */   {
/*  71 */     this.stream = paramImageInputStream;
/*  72 */     this.length = paramImageInputStream.readInt();
/*  73 */     int i = paramImageInputStream.readInt();
/*     */   }
/*     */ 
/*     */   public InputStream nextElement() {
/*     */     try {
/*  78 */       this.firstTime = false;
/*  79 */       SubImageInputStream localSubImageInputStream = new SubImageInputStream(this.stream, this.length);
/*  80 */       return new InputStreamAdapter(localSubImageInputStream); } catch (IOException localIOException) {
/*     */     }
/*  82 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*  87 */     if (this.firstTime) {
/*  88 */       return true;
/*     */     }
/*     */     try
/*     */     {
/*  92 */       int i = this.stream.readInt();
/*  93 */       this.length = this.stream.readInt();
/*  94 */       int j = this.stream.readInt();
/*  95 */       if (j == 1229209940) {
/*  96 */         return true;
/*     */       }
/*  98 */       return false;
/*     */     } catch (IOException localIOException) {
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGImageDataEnumeration
 * JD-Core Version:    0.6.2
 */
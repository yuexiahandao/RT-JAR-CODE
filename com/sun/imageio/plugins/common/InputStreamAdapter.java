/*    */ package com.sun.imageio.plugins.common;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import javax.imageio.stream.ImageInputStream;
/*    */ 
/*    */ public class InputStreamAdapter extends InputStream
/*    */ {
/*    */   ImageInputStream stream;
/*    */ 
/*    */   public InputStreamAdapter(ImageInputStream paramImageInputStream)
/*    */   {
/* 39 */     this.stream = paramImageInputStream;
/*    */   }
/*    */ 
/*    */   public int read() throws IOException {
/* 43 */     return this.stream.read();
/*    */   }
/*    */ 
/*    */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 47 */     return this.stream.read(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.InputStreamAdapter
 * JD-Core Version:    0.6.2
 */
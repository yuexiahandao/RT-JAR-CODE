/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ public class NoCloseInputStream extends FilterInputStream
/*    */ {
/*    */   public NoCloseInputStream(InputStream is)
/*    */   {
/* 39 */     super(is);
/*    */   }
/*    */ 
/*    */   public void close() throws IOException
/*    */   {
/*    */   }
/*    */ 
/*    */   public void doClose() throws IOException
/*    */   {
/* 48 */     super.close();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.NoCloseInputStream
 * JD-Core Version:    0.6.2
 */
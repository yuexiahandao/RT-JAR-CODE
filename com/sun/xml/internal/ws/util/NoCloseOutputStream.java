/*    */ package com.sun.xml.internal.ws.util;
/*    */ 
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class NoCloseOutputStream extends FilterOutputStream
/*    */ {
/*    */   public NoCloseOutputStream(OutputStream out)
/*    */   {
/* 39 */     super(out);
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
 * Qualified Name:     com.sun.xml.internal.ws.util.NoCloseOutputStream
 * JD-Core Version:    0.6.2
 */
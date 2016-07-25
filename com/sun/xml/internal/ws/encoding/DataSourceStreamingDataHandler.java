/*    */ package com.sun.xml.internal.ws.encoding;
/*    */ 
/*    */ import com.sun.xml.internal.ws.developer.StreamingDataHandler;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import javax.activation.DataSource;
/*    */ 
/*    */ public class DataSourceStreamingDataHandler extends StreamingDataHandler
/*    */ {
/*    */   public DataSourceStreamingDataHandler(DataSource ds)
/*    */   {
/* 41 */     super(ds);
/*    */   }
/*    */ 
/*    */   public InputStream readOnce() throws IOException {
/* 45 */     return getInputStream();
/*    */   }
/*    */ 
/*    */   public void moveTo(File file) throws IOException {
/* 49 */     InputStream in = getInputStream();
/* 50 */     OutputStream os = new FileOutputStream(file);
/* 51 */     byte[] temp = new byte[8192];
/*    */     int len;
/* 53 */     while ((len = in.read(temp)) != -1) {
/* 54 */       os.write(temp, 0, len);
/*    */     }
/* 56 */     in.close();
/* 57 */     os.close();
/*    */   }
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.encoding.DataSourceStreamingDataHandler
 * JD-Core Version:    0.6.2
 */
/*    */ package sun.rmi.log;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.io.RandomAccessFile;
/*    */ 
/*    */ public class LogOutputStream extends OutputStream
/*    */ {
/*    */   private RandomAccessFile raf;
/*    */ 
/*    */   public LogOutputStream(RandomAccessFile paramRandomAccessFile)
/*    */     throws IOException
/*    */   {
/* 42 */     this.raf = paramRandomAccessFile;
/*    */   }
/*    */ 
/*    */   public void write(int paramInt)
/*    */     throws IOException
/*    */   {
/* 52 */     this.raf.write(paramInt);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte)
/*    */     throws IOException
/*    */   {
/* 62 */     this.raf.write(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/* 73 */     this.raf.write(paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public final void close()
/*    */     throws IOException
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.log.LogOutputStream
 * JD-Core Version:    0.6.2
 */
/*    */ package sun.misc;
/*    */ 
/*    */ import java.io.EOFException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class IOUtils
/*    */ {
/*    */   public static byte[] readFully(InputStream paramInputStream, int paramInt, boolean paramBoolean)
/*    */     throws IOException
/*    */   {
/* 52 */     byte[] arrayOfByte = new byte[0];
/* 53 */     if (paramInt == -1) paramInt = 2147483647;
/* 54 */     int i = 0;
/* 55 */     while (i < paramInt)
/*    */     {
/*    */       int j;
/* 57 */       if (i >= arrayOfByte.length) {
/* 58 */         j = Math.min(paramInt - i, arrayOfByte.length + 1024);
/* 59 */         if (arrayOfByte.length < i + j)
/* 60 */           arrayOfByte = Arrays.copyOf(arrayOfByte, i + j);
/*    */       }
/*    */       else {
/* 63 */         j = arrayOfByte.length - i;
/*    */       }
/* 65 */       int k = paramInputStream.read(arrayOfByte, i, j);
/* 66 */       if (k < 0) {
/* 67 */         if ((paramBoolean) && (paramInt != 2147483647)) {
/* 68 */           throw new EOFException("Detect premature EOF");
/*    */         }
/* 70 */         if (arrayOfByte.length == i) break;
/* 71 */         arrayOfByte = Arrays.copyOf(arrayOfByte, i); break;
/*    */       }
/*    */ 
/* 76 */       i += k;
/*    */     }
/* 78 */     return arrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.IOUtils
 * JD-Core Version:    0.6.2
 */
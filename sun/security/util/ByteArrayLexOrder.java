/*    */ package sun.security.util;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ public class ByteArrayLexOrder
/*    */   implements Comparator<byte[]>
/*    */ {
/*    */   public final int compare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*    */   {
/* 55 */     for (int j = 0; (j < paramArrayOfByte1.length) && (j < paramArrayOfByte2.length); j++) {
/* 56 */       int i = (paramArrayOfByte1[j] & 0xFF) - (paramArrayOfByte2[j] & 0xFF);
/* 57 */       if (i != 0) {
/* 58 */         return i;
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 63 */     return paramArrayOfByte1.length - paramArrayOfByte2.length;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.ByteArrayLexOrder
 * JD-Core Version:    0.6.2
 */
/*    */ package sun.misc;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ 
/*    */ public class BASE64Encoder extends CharacterEncoder
/*    */ {
/* 64 */   private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*    */ 
/*    */   protected int bytesPerAtom()
/*    */   {
/* 51 */     return 3;
/*    */   }
/*    */ 
/*    */   protected int bytesPerLine()
/*    */   {
/* 60 */     return 57;
/*    */   }
/*    */ 
/*    */   protected void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */     throws IOException
/*    */   {
/*    */     int i;
/*    */     int j;
/*    */     int k;
/* 86 */     if (paramInt2 == 1) {
/* 87 */       i = paramArrayOfByte[paramInt1];
/* 88 */       j = 0;
/* 89 */       k = 0;
/* 90 */       paramOutputStream.write(pem_array[(i >>> 2 & 0x3F)]);
/* 91 */       paramOutputStream.write(pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
/* 92 */       paramOutputStream.write(61);
/* 93 */       paramOutputStream.write(61);
/* 94 */     } else if (paramInt2 == 2) {
/* 95 */       i = paramArrayOfByte[paramInt1];
/* 96 */       j = paramArrayOfByte[(paramInt1 + 1)];
/* 97 */       k = 0;
/* 98 */       paramOutputStream.write(pem_array[(i >>> 2 & 0x3F)]);
/* 99 */       paramOutputStream.write(pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
/* 100 */       paramOutputStream.write(pem_array[((j << 2 & 0x3C) + (k >>> 6 & 0x3))]);
/* 101 */       paramOutputStream.write(61);
/*    */     } else {
/* 103 */       i = paramArrayOfByte[paramInt1];
/* 104 */       j = paramArrayOfByte[(paramInt1 + 1)];
/* 105 */       k = paramArrayOfByte[(paramInt1 + 2)];
/* 106 */       paramOutputStream.write(pem_array[(i >>> 2 & 0x3F)]);
/* 107 */       paramOutputStream.write(pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
/* 108 */       paramOutputStream.write(pem_array[((j << 2 & 0x3C) + (k >>> 6 & 0x3))]);
/* 109 */       paramOutputStream.write(pem_array[(k & 0x3F)]);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.BASE64Encoder
 * JD-Core Version:    0.6.2
 */
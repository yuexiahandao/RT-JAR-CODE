/*    */ package com.sun.xml.internal.org.jvnet.staxex;
/*    */ 
/*    */ class Base64Encoder
/*    */ {
/* 32 */   private static final char[] encodeMap = initEncodeMap();
/*    */ 
/*    */   private static char[] initEncodeMap() {
/* 35 */     char[] map = new char[64];
/*    */ 
/* 37 */     for (int i = 0; i < 26; i++) map[i] = ((char)(65 + i));
/* 38 */     for (i = 26; i < 52; i++) map[i] = ((char)(97 + (i - 26)));
/* 39 */     for (i = 52; i < 62; i++) map[i] = ((char)(48 + (i - 52)));
/* 40 */     map[62] = '+';
/* 41 */     map[63] = '/';
/*    */ 
/* 43 */     return map;
/*    */   }
/*    */ 
/*    */   public static char encode(int i) {
/* 47 */     return encodeMap[(i & 0x3F)];
/*    */   }
/*    */ 
/*    */   public static byte encodeByte(int i) {
/* 51 */     return (byte)encodeMap[(i & 0x3F)];
/*    */   }
/*    */ 
/*    */   public static String print(byte[] input, int offset, int len) {
/* 55 */     char[] buf = new char[(len + 2) / 3 * 4];
/* 56 */     int ptr = print(input, offset, len, buf, 0);
/* 57 */     assert (ptr == buf.length);
/* 58 */     return new String(buf);
/*    */   }
/*    */ 
/*    */   public static int print(byte[] input, int offset, int len, char[] buf, int ptr)
/*    */   {
/* 71 */     for (int i = offset; i < len; i += 3) {
/* 72 */       switch (len - i) {
/*    */       case 1:
/* 74 */         buf[(ptr++)] = encode(input[i] >> 2);
/* 75 */         buf[(ptr++)] = encode((input[i] & 0x3) << 4);
/* 76 */         buf[(ptr++)] = '=';
/* 77 */         buf[(ptr++)] = '=';
/* 78 */         break;
/*    */       case 2:
/* 80 */         buf[(ptr++)] = encode(input[i] >> 2);
/* 81 */         buf[(ptr++)] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*    */ 
/* 84 */         buf[(ptr++)] = encode((input[(i + 1)] & 0xF) << 2);
/* 85 */         buf[(ptr++)] = '=';
/* 86 */         break;
/*    */       default:
/* 88 */         buf[(ptr++)] = encode(input[i] >> 2);
/* 89 */         buf[(ptr++)] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*    */ 
/* 92 */         buf[(ptr++)] = encode((input[(i + 1)] & 0xF) << 2 | input[(i + 2)] >> 6 & 0x3);
/*    */ 
/* 95 */         buf[(ptr++)] = encode(input[(i + 2)] & 0x3F);
/*    */       }
/*    */     }
/*    */ 
/* 99 */     return ptr;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.org.jvnet.staxex.Base64Encoder
 * JD-Core Version:    0.6.2
 */
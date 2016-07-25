/*    */ package sun.reflect;
/*    */ 
/*    */ class UTF8
/*    */ {
/*    */   static byte[] encode(String paramString)
/*    */   {
/* 35 */     int i = paramString.length();
/* 36 */     byte[] arrayOfByte = new byte[utf8Length(paramString)];
/* 37 */     int j = 0;
/*    */     try {
/* 39 */       for (int k = 0; k < i; k++) {
/* 40 */         int m = paramString.charAt(k) & 0xFFFF;
/* 41 */         if ((m >= 1) && (m <= 127)) {
/* 42 */           arrayOfByte[(j++)] = ((byte)m);
/* 43 */         } else if ((m == 0) || ((m >= 128) && (m <= 2047)))
/*    */         {
/* 45 */           arrayOfByte[(j++)] = ((byte)(192 + (m >> 6)));
/* 46 */           arrayOfByte[(j++)] = ((byte)(128 + (m & 0x3F)));
/*    */         } else {
/* 48 */           arrayOfByte[(j++)] = ((byte)(224 + (m >> 12)));
/* 49 */           arrayOfByte[(j++)] = ((byte)(128 + (m >> 6 & 0x3F)));
/* 50 */           arrayOfByte[(j++)] = ((byte)(128 + (m & 0x3F)));
/*    */         }
/*    */       }
/*    */     } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 54 */       throw new InternalError("Bug in sun.reflect bootstrap UTF-8 encoder");
/*    */     }
/*    */ 
/* 57 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   private static int utf8Length(String paramString) {
/* 61 */     int i = paramString.length();
/* 62 */     int j = 0;
/* 63 */     for (int k = 0; k < i; k++) {
/* 64 */       int m = paramString.charAt(k) & 0xFFFF;
/* 65 */       if ((m >= 1) && (m <= 127))
/* 66 */         j++;
/* 67 */       else if ((m == 0) || ((m >= 128) && (m <= 2047)))
/*    */       {
/* 69 */         j += 2;
/*    */       }
/* 71 */       else j += 3;
/*    */     }
/*    */ 
/* 74 */     return j;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UTF8
 * JD-Core Version:    0.6.2
 */
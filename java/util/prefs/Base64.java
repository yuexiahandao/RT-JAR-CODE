/*     */ package java.util.prefs;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ 
/*     */ class Base64
/*     */ {
/* 100 */   private static final char[] intToBase64 = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
/*     */ 
/* 115 */   private static final char[] intToAltBase64 = { '!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?' };
/*     */ 
/* 214 */   private static final byte[] base64ToInt = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
/*     */ 
/* 228 */   private static final byte[] altBase64ToInt = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25 };
/*     */ 
/*     */   static String byteArrayToBase64(byte[] paramArrayOfByte)
/*     */   {
/*  42 */     return byteArrayToBase64(paramArrayOfByte, false);
/*     */   }
/*     */ 
/*     */   static String byteArrayToAltBase64(byte[] paramArrayOfByte)
/*     */   {
/*  52 */     return byteArrayToBase64(paramArrayOfByte, true);
/*     */   }
/*     */ 
/*     */   private static String byteArrayToBase64(byte[] paramArrayOfByte, boolean paramBoolean) {
/*  56 */     int i = paramArrayOfByte.length;
/*  57 */     int j = i / 3;
/*  58 */     int k = i - 3 * j;
/*  59 */     int m = 4 * ((i + 2) / 3);
/*  60 */     StringBuffer localStringBuffer = new StringBuffer(m);
/*  61 */     char[] arrayOfChar = paramBoolean ? intToAltBase64 : intToBase64;
/*     */ 
/*  64 */     int n = 0;
/*     */     int i2;
/*  65 */     for (int i1 = 0; i1 < j; i1++) {
/*  66 */       i2 = paramArrayOfByte[(n++)] & 0xFF;
/*  67 */       int i3 = paramArrayOfByte[(n++)] & 0xFF;
/*  68 */       int i4 = paramArrayOfByte[(n++)] & 0xFF;
/*  69 */       localStringBuffer.append(arrayOfChar[(i2 >> 2)]);
/*  70 */       localStringBuffer.append(arrayOfChar[(i2 << 4 & 0x3F | i3 >> 4)]);
/*  71 */       localStringBuffer.append(arrayOfChar[(i3 << 2 & 0x3F | i4 >> 6)]);
/*  72 */       localStringBuffer.append(arrayOfChar[(i4 & 0x3F)]);
/*     */     }
/*     */ 
/*  76 */     if (k != 0) {
/*  77 */       i1 = paramArrayOfByte[(n++)] & 0xFF;
/*  78 */       localStringBuffer.append(arrayOfChar[(i1 >> 2)]);
/*  79 */       if (k == 1) {
/*  80 */         localStringBuffer.append(arrayOfChar[(i1 << 4 & 0x3F)]);
/*  81 */         localStringBuffer.append("==");
/*     */       }
/*     */       else {
/*  84 */         i2 = paramArrayOfByte[(n++)] & 0xFF;
/*  85 */         localStringBuffer.append(arrayOfChar[(i1 << 4 & 0x3F | i2 >> 4)]);
/*  86 */         localStringBuffer.append(arrayOfChar[(i2 << 2 & 0x3F)]);
/*  87 */         localStringBuffer.append('=');
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  92 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static byte[] base64ToByteArray(String paramString)
/*     */   {
/* 131 */     return base64ToByteArray(paramString, false);
/*     */   }
/*     */ 
/*     */   static byte[] altBase64ToByteArray(String paramString)
/*     */   {
/* 143 */     return base64ToByteArray(paramString, true);
/*     */   }
/*     */ 
/*     */   private static byte[] base64ToByteArray(String paramString, boolean paramBoolean) {
/* 147 */     byte[] arrayOfByte1 = paramBoolean ? altBase64ToInt : base64ToInt;
/* 148 */     int i = paramString.length();
/* 149 */     int j = i / 4;
/* 150 */     if (4 * j != i) {
/* 151 */       throw new IllegalArgumentException("String length must be a multiple of four.");
/*     */     }
/* 153 */     int k = 0;
/* 154 */     int m = j;
/* 155 */     if (i != 0) {
/* 156 */       if (paramString.charAt(i - 1) == '=') {
/* 157 */         k++;
/* 158 */         m--;
/*     */       }
/* 160 */       if (paramString.charAt(i - 2) == '=')
/* 161 */         k++;
/*     */     }
/* 163 */     byte[] arrayOfByte2 = new byte[3 * j - k];
/*     */ 
/* 166 */     int n = 0; int i1 = 0;
/*     */     int i3;
/*     */     int i4;
/* 167 */     for (int i2 = 0; i2 < m; i2++) {
/* 168 */       i3 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 169 */       i4 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 170 */       int i5 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 171 */       int i6 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 172 */       arrayOfByte2[(i1++)] = ((byte)(i3 << 2 | i4 >> 4));
/* 173 */       arrayOfByte2[(i1++)] = ((byte)(i4 << 4 | i5 >> 2));
/* 174 */       arrayOfByte2[(i1++)] = ((byte)(i5 << 6 | i6));
/*     */     }
/*     */ 
/* 178 */     if (k != 0) {
/* 179 */       i2 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 180 */       i3 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 181 */       arrayOfByte2[(i1++)] = ((byte)(i2 << 2 | i3 >> 4));
/*     */ 
/* 183 */       if (k == 1) {
/* 184 */         i4 = base64toInt(paramString.charAt(n++), arrayOfByte1);
/* 185 */         arrayOfByte2[(i1++)] = ((byte)(i3 << 4 | i4 >> 2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 190 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   private static int base64toInt(char paramChar, byte[] paramArrayOfByte)
/*     */   {
/* 201 */     int i = paramArrayOfByte[paramChar];
/* 202 */     if (i < 0)
/* 203 */       throw new IllegalArgumentException("Illegal character " + paramChar);
/* 204 */     return i;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/* 240 */     int i = Integer.parseInt(paramArrayOfString[0]);
/* 241 */     int j = Integer.parseInt(paramArrayOfString[1]);
/* 242 */     Random localRandom = new Random();
/* 243 */     for (int k = 0; k < i; k++)
/* 244 */       for (int m = 0; m < j; m++) {
/* 245 */         byte[] arrayOfByte1 = new byte[m];
/* 246 */         for (int n = 0; n < m; n++) {
/* 247 */           arrayOfByte1[n] = ((byte)localRandom.nextInt());
/*     */         }
/* 249 */         String str = byteArrayToBase64(arrayOfByte1);
/* 250 */         byte[] arrayOfByte2 = base64ToByteArray(str);
/* 251 */         if (!Arrays.equals(arrayOfByte1, arrayOfByte2)) {
/* 252 */           System.out.println("Dismal failure!");
/*     */         }
/* 254 */         str = byteArrayToAltBase64(arrayOfByte1);
/* 255 */         arrayOfByte2 = altBase64ToByteArray(str);
/* 256 */         if (!Arrays.equals(arrayOfByte1, arrayOfByte2))
/* 257 */           System.out.println("Alternate dismal failure!");
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.Base64
 * JD-Core Version:    0.6.2
 */
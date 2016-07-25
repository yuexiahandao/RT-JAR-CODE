/*     */ package sun.net.util;
/*     */ 
/*     */ public class IPAddressUtil
/*     */ {
/*     */   private static final int INADDR4SZ = 4;
/*     */   private static final int INADDR16SZ = 16;
/*     */   private static final int INT16SZ = 2;
/*     */ 
/*     */   public static byte[] textToNumericFormatV4(String paramString)
/*     */   {
/*  42 */     if (paramString.length() == 0) {
/*  43 */       return null;
/*     */     }
/*     */ 
/*  46 */     byte[] arrayOfByte = new byte[4];
/*  47 */     String[] arrayOfString = paramString.split("\\.", -1);
/*     */     try
/*     */     {
/*     */       long l;
/*     */       int i;
/*  50 */       switch (arrayOfString.length)
/*     */       {
/*     */       case 1:
/*  57 */         l = Long.parseLong(arrayOfString[0]);
/*  58 */         if ((l < 0L) || (l > 4294967295L))
/*  59 */           return null;
/*  60 */         arrayOfByte[0] = ((byte)(int)(l >> 24 & 0xFF));
/*  61 */         arrayOfByte[1] = ((byte)(int)((l & 0xFFFFFF) >> 16 & 0xFF));
/*  62 */         arrayOfByte[2] = ((byte)(int)((l & 0xFFFF) >> 8 & 0xFF));
/*  63 */         arrayOfByte[3] = ((byte)(int)(l & 0xFF));
/*  64 */         break;
/*     */       case 2:
/*  74 */         l = Integer.parseInt(arrayOfString[0]);
/*  75 */         if ((l < 0L) || (l > 255L))
/*  76 */           return null;
/*  77 */         arrayOfByte[0] = ((byte)(int)(l & 0xFF));
/*  78 */         l = Integer.parseInt(arrayOfString[1]);
/*  79 */         if ((l < 0L) || (l > 16777215L))
/*  80 */           return null;
/*  81 */         arrayOfByte[1] = ((byte)(int)(l >> 16 & 0xFF));
/*  82 */         arrayOfByte[2] = ((byte)(int)((l & 0xFFFF) >> 8 & 0xFF));
/*  83 */         arrayOfByte[3] = ((byte)(int)(l & 0xFF));
/*  84 */         break;
/*     */       case 3:
/*  93 */         for (i = 0; i < 2; i++) {
/*  94 */           l = Integer.parseInt(arrayOfString[i]);
/*  95 */           if ((l < 0L) || (l > 255L))
/*  96 */             return null;
/*  97 */           arrayOfByte[i] = ((byte)(int)(l & 0xFF));
/*     */         }
/*  99 */         l = Integer.parseInt(arrayOfString[2]);
/* 100 */         if ((l < 0L) || (l > 65535L))
/* 101 */           return null;
/* 102 */         arrayOfByte[2] = ((byte)(int)(l >> 8 & 0xFF));
/* 103 */         arrayOfByte[3] = ((byte)(int)(l & 0xFF));
/* 104 */         break;
/*     */       case 4:
/* 111 */         for (i = 0; i < 4; i++) {
/* 112 */           l = Integer.parseInt(arrayOfString[i]);
/* 113 */           if ((l < 0L) || (l > 255L))
/* 114 */             return null;
/* 115 */           arrayOfByte[i] = ((byte)(int)(l & 0xFF));
/*     */         }
/* 117 */         break;
/*     */       default:
/* 119 */         return null;
/*     */       }
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 122 */       return null;
/*     */     }
/* 124 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static byte[] textToNumericFormatV6(String paramString)
/*     */   {
/* 140 */     if (paramString.length() < 2) {
/* 141 */       return null;
/*     */     }
/*     */ 
/* 148 */     char[] arrayOfChar = paramString.toCharArray();
/* 149 */     byte[] arrayOfByte1 = new byte[16];
/*     */ 
/* 151 */     int m = arrayOfChar.length;
/* 152 */     int n = paramString.indexOf("%");
/* 153 */     if (n == m - 1) {
/* 154 */       return null;
/*     */     }
/*     */ 
/* 157 */     if (n != -1) {
/* 158 */       m = n;
/*     */     }
/*     */ 
/* 161 */     int i = -1;
/* 162 */     int i1 = 0; int i2 = 0;
/*     */ 
/* 164 */     if ((arrayOfChar[i1] == ':') && 
/* 165 */       (arrayOfChar[(++i1)] != ':'))
/* 166 */       return null;
/* 167 */     int i3 = i1;
/* 168 */     int j = 0;
/* 169 */     int k = 0;
/*     */     int i4;
/* 170 */     while (i1 < m) {
/* 171 */       char c = arrayOfChar[(i1++)];
/* 172 */       i4 = Character.digit(c, 16);
/* 173 */       if (i4 != -1) {
/* 174 */         k <<= 4;
/* 175 */         k |= i4;
/* 176 */         if (k > 65535)
/* 177 */           return null;
/* 178 */         j = 1;
/*     */       }
/* 181 */       else if (c == ':') {
/* 182 */         i3 = i1;
/* 183 */         if (j == 0) {
/* 184 */           if (i != -1)
/* 185 */             return null;
/* 186 */           i = i2;
/*     */         } else {
/* 188 */           if (i1 == m) {
/* 189 */             return null;
/*     */           }
/* 191 */           if (i2 + 2 > 16)
/* 192 */             return null;
/* 193 */           arrayOfByte1[(i2++)] = ((byte)(k >> 8 & 0xFF));
/* 194 */           arrayOfByte1[(i2++)] = ((byte)(k & 0xFF));
/* 195 */           j = 0;
/* 196 */           k = 0;
/*     */         }
/*     */       }
/* 199 */       else if ((c == '.') && (i2 + 4 <= 16)) {
/* 200 */         String str = paramString.substring(i3, m);
/*     */ 
/* 202 */         int i5 = 0; int i6 = 0;
/* 203 */         while ((i6 = str.indexOf('.', i6)) != -1) {
/* 204 */           i5++;
/* 205 */           i6++;
/*     */         }
/* 207 */         if (i5 != 3) {
/* 208 */           return null;
/*     */         }
/* 210 */         byte[] arrayOfByte3 = textToNumericFormatV4(str);
/* 211 */         if (arrayOfByte3 == null) {
/* 212 */           return null;
/*     */         }
/* 214 */         for (int i7 = 0; i7 < 4; i7++) {
/* 215 */           arrayOfByte1[(i2++)] = arrayOfByte3[i7];
/*     */         }
/* 217 */         j = 0;
/*     */       }
/*     */       else {
/* 220 */         return null;
/*     */       }
/*     */     }
/* 222 */     if (j != 0) {
/* 223 */       if (i2 + 2 > 16)
/* 224 */         return null;
/* 225 */       arrayOfByte1[(i2++)] = ((byte)(k >> 8 & 0xFF));
/* 226 */       arrayOfByte1[(i2++)] = ((byte)(k & 0xFF));
/*     */     }
/*     */ 
/* 229 */     if (i != -1) {
/* 230 */       i4 = i2 - i;
/*     */ 
/* 232 */       if (i2 == 16)
/* 233 */         return null;
/* 234 */       for (i1 = 1; i1 <= i4; i1++) {
/* 235 */         arrayOfByte1[(16 - i1)] = arrayOfByte1[(i + i4 - i1)];
/* 236 */         arrayOfByte1[(i + i4 - i1)] = 0;
/*     */       }
/* 238 */       i2 = 16;
/*     */     }
/* 240 */     if (i2 != 16)
/* 241 */       return null;
/* 242 */     byte[] arrayOfByte2 = convertFromIPv4MappedAddress(arrayOfByte1);
/* 243 */     if (arrayOfByte2 != null) {
/* 244 */       return arrayOfByte2;
/*     */     }
/* 246 */     return arrayOfByte1;
/*     */   }
/*     */ 
/*     */   public static boolean isIPv4LiteralAddress(String paramString)
/*     */   {
/* 255 */     return textToNumericFormatV4(paramString) != null;
/*     */   }
/*     */ 
/*     */   public static boolean isIPv6LiteralAddress(String paramString)
/*     */   {
/* 263 */     return textToNumericFormatV6(paramString) != null;
/*     */   }
/*     */ 
/*     */   public static byte[] convertFromIPv4MappedAddress(byte[] paramArrayOfByte)
/*     */   {
/* 274 */     if (isIPv4MappedAddress(paramArrayOfByte)) {
/* 275 */       byte[] arrayOfByte = new byte[4];
/* 276 */       System.arraycopy(paramArrayOfByte, 12, arrayOfByte, 0, 4);
/* 277 */       return arrayOfByte;
/*     */     }
/* 279 */     return null;
/*     */   }
/*     */ 
/*     */   private static boolean isIPv4MappedAddress(byte[] paramArrayOfByte)
/*     */   {
/* 290 */     if (paramArrayOfByte.length < 16) {
/* 291 */       return false;
/*     */     }
/* 293 */     if ((paramArrayOfByte[0] == 0) && (paramArrayOfByte[1] == 0) && (paramArrayOfByte[2] == 0) && (paramArrayOfByte[3] == 0) && (paramArrayOfByte[4] == 0) && (paramArrayOfByte[5] == 0) && (paramArrayOfByte[6] == 0) && (paramArrayOfByte[7] == 0) && (paramArrayOfByte[8] == 0) && (paramArrayOfByte[9] == 0) && (paramArrayOfByte[10] == -1) && (paramArrayOfByte[11] == -1))
/*     */     {
/* 300 */       return true;
/*     */     }
/* 302 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.util.IPAddressUtil
 * JD-Core Version:    0.6.2
 */
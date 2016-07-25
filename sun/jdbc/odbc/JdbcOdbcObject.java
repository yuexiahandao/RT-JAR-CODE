/*     */ package sun.jdbc.odbc;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharacterCodingException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetDecoder;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.CodingErrorAction;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ 
/*     */ public class JdbcOdbcObject
/*     */ {
/*     */   protected static void dumpByte(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/*  61 */     for (int i = 0; i * 16 < paramInt; i++)
/*     */     {
/*  64 */       String str1 = toHex(i * 16);
/*     */ 
/*  70 */       String str2 = "";
/*     */ 
/*  73 */       for (int j = 0; j < 16; j++) {
/*  74 */         int k = i * 16 + j;
/*     */ 
/*  77 */         if (k >= paramInt) {
/*  78 */           str1 = "  ";
/*  79 */           str2 = str2 + " ";
/*     */         }
/*     */         else {
/*  82 */           str1 = toHex(paramArrayOfByte[k]);
/*  83 */           str1 = hexPad(str1, 2);
/*  84 */           if ((paramArrayOfByte[k] < 32) || (paramArrayOfByte[k] > 128)) {
/*  85 */             str2 = str2 + ".";
/*     */           }
/*     */           else
/*  88 */             str2 = str2 + new String(paramArrayOfByte, k, 1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String hexPad(String paramString, int paramInt)
/*     */   {
/* 109 */     if (!paramString.startsWith("0x")) {
/* 110 */       return paramString;
/*     */     }
/*     */ 
/* 115 */     Object localObject = paramString.substring(2);
/* 116 */     int i = ((String)localObject).length();
/*     */ 
/* 119 */     if (i > paramInt) {
/* 120 */       localObject = ((String)localObject).substring(i - paramInt);
/*     */     }
/* 122 */     else if (i < paramInt)
/*     */     {
/* 124 */       String str1 = "0000000000000000";
/* 125 */       String str2 = str1.substring(0, paramInt - i) + (String)localObject;
/* 126 */       localObject = str2;
/*     */     }
/* 128 */     localObject = ((String)localObject).toUpperCase();
/* 129 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static String toHex(int paramInt)
/*     */   {
/* 139 */     char[] arrayOfChar = new char[8];
/* 140 */     String str = "0123456789ABCDEF";
/*     */ 
/* 145 */     for (int j = 0; j < 4; j++) {
/* 146 */       int i = (byte)(paramInt & 0xFF);
/* 147 */       arrayOfChar[(6 - j * 2)] = str.charAt(i >> 4 & 0xF);
/* 148 */       arrayOfChar[(7 - j * 2)] = str.charAt(i & 0xF);
/*     */ 
/* 152 */       paramInt >>= 8;
/*     */     }
/*     */ 
/* 155 */     return "0x" + new String(arrayOfChar);
/*     */   }
/*     */ 
/*     */   public static byte[] hexStringToByteArray(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 168 */     int i = paramString.length();
/* 169 */     int j = (i + 1) / 2;
/*     */ 
/* 172 */     byte[] arrayOfByte = new byte[j];
/*     */ 
/* 177 */     for (int k = 0; k < j; k++) {
/* 178 */       arrayOfByte[k] = ((byte)hexPairToInt(paramString.substring(k * 2, (k + 1) * 2)));
/*     */     }
/*     */ 
/* 181 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public static int hexPairToInt(String paramString)
/*     */     throws NumberFormatException
/*     */   {
/* 194 */     String str1 = "0123456789ABCDEF";
/* 195 */     String str2 = paramString.toUpperCase();
/* 196 */     int i = 0;
/* 197 */     int j = 0;
/* 198 */     int k = str2.length();
/*     */ 
/* 200 */     if (k > 2) {
/* 201 */       k = 2;
/*     */     }
/*     */ 
/* 206 */     for (int m = 0; m < k; m++) {
/* 207 */       j = str1.indexOf(str2.substring(m, m + 1));
/*     */ 
/* 210 */       if (j < 0) {
/* 211 */         throw new NumberFormatException();
/*     */       }
/*     */ 
/* 214 */       if (m == 0) {
/* 215 */         j *= 16;
/*     */       }
/* 217 */       i += j;
/*     */     }
/* 219 */     return i;
/*     */   }
/*     */ 
/*     */   public String BytesToChars(String paramString, byte[] paramArrayOfByte)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 229 */     String str = new String();
/*     */     try {
/* 231 */       str = Charset.forName(paramString).newDecoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith("?").decode(ByteBuffer.wrap(paramArrayOfByte)).toString();
/*     */     }
/*     */     catch (IllegalCharsetNameException localIllegalCharsetNameException)
/*     */     {
/* 239 */       throw new UnsupportedEncodingException(paramString);
/*     */     }
/*     */     catch (IllegalStateException localIllegalStateException)
/*     */     {
/*     */     }
/*     */     catch (CharacterCodingException localCharacterCodingException) {
/*     */     }
/* 246 */     char[] arrayOfChar1 = str.toCharArray();
/*     */ 
/* 251 */     for (int i = 0; i < arrayOfChar1.length; i++)
/*     */     {
/* 255 */       if (arrayOfChar1[i] == 0)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/* 261 */     char[] arrayOfChar2 = new char[i];
/* 262 */     System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, i);
/* 263 */     str = new String(arrayOfChar2);
/* 264 */     return str;
/*     */   }
/*     */ 
/*     */   public byte[] CharsToBytes(String paramString, char[] paramArrayOfChar)
/*     */     throws UnsupportedEncodingException
/*     */   {
/*     */     try
/*     */     {
/* 277 */       char[] arrayOfChar = new char[paramArrayOfChar.length + 1];
/* 278 */       System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramArrayOfChar.length);
/* 279 */       ByteBuffer localByteBuffer = Charset.forName(paramString).newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).replaceWith(new byte[] { 63 }).encode(CharBuffer.wrap(arrayOfChar));
/*     */ 
/* 284 */       byte[] arrayOfByte = new byte[localByteBuffer.limit()];
/* 285 */       System.arraycopy(localByteBuffer.array(), 0, arrayOfByte, 0, localByteBuffer.limit());
/* 286 */       return arrayOfByte;
/*     */     }
/*     */     catch (IllegalCharsetNameException localIllegalCharsetNameException) {
/* 289 */       throw new UnsupportedEncodingException(paramString);
/*     */     } catch (IllegalStateException localIllegalStateException) {
/* 291 */       localIllegalStateException.printStackTrace(); } catch (CharacterCodingException localCharacterCodingException) {
/* 292 */       localCharacterCodingException.printStackTrace();
/* 293 */     }return new byte[0];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.jdbc.odbc.JdbcOdbcObject
 * JD-Core Version:    0.6.2
 */
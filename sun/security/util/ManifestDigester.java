/*     */ package sun.security.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class ManifestDigester
/*     */ {
/*     */   public static final String MF_MAIN_ATTRS = "Manifest-Main-Attributes";
/*     */   private byte[] rawBytes;
/*     */   private HashMap<String, Entry> entries;
/*     */ 
/*     */   private boolean findSection(int paramInt, Position paramPosition)
/*     */   {
/*  67 */     int i = paramInt; int j = this.rawBytes.length;
/*  68 */     int k = paramInt;
/*     */ 
/*  70 */     int m = 1;
/*     */ 
/*  72 */     paramPosition.endOfFirstLine = -1;
/*     */ 
/*  74 */     while (i < j) {
/*  75 */       int n = this.rawBytes[i];
/*  76 */       switch (n) {
/*     */       case 13:
/*  78 */         if (paramPosition.endOfFirstLine == -1)
/*  79 */           paramPosition.endOfFirstLine = (i - 1);
/*  80 */         if ((i < j) && (this.rawBytes[(i + 1)] == 10))
/*  81 */           i++;
/*     */       case 10:
/*  83 */         if (paramPosition.endOfFirstLine == -1)
/*  84 */           paramPosition.endOfFirstLine = (i - 1);
/*  85 */         if ((m != 0) || (i == j - 1)) {
/*  86 */           if (i == j - 1)
/*  87 */             paramPosition.endOfSection = i;
/*     */           else
/*  89 */             paramPosition.endOfSection = k;
/*  90 */           paramPosition.startOfNext = (i + 1);
/*  91 */           return true;
/*     */         }
/*     */ 
/*  95 */         k = i;
/*  96 */         m = 1;
/*     */ 
/*  98 */         break;
/*     */       }
/* 100 */       m = 0;
/*     */ 
/* 103 */       i++;
/*     */     }
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   public ManifestDigester(byte[] paramArrayOfByte)
/*     */   {
/* 110 */     this.rawBytes = paramArrayOfByte;
/* 111 */     this.entries = new HashMap();
/*     */ 
/* 113 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*     */ 
/* 115 */     Position localPosition = new Position();
/*     */ 
/* 117 */     if (!findSection(0, localPosition)) {
/* 118 */       return;
/*     */     }
/*     */ 
/* 121 */     this.entries.put("Manifest-Main-Attributes", new Entry(0, localPosition.endOfSection + 1, localPosition.startOfNext, this.rawBytes));
/*     */ 
/* 124 */     int i = localPosition.startOfNext;
/* 125 */     while (findSection(i, localPosition)) {
/* 126 */       int j = localPosition.endOfFirstLine - i + 1;
/* 127 */       int k = localPosition.endOfSection - i + 1;
/* 128 */       int m = localPosition.startOfNext - i;
/*     */ 
/* 130 */       if ((j > 6) && 
/* 131 */         (isNameAttr(paramArrayOfByte, i))) {
/* 132 */         StringBuilder localStringBuilder = new StringBuilder(k);
/*     */         try
/*     */         {
/* 135 */           localStringBuilder.append(new String(paramArrayOfByte, i + 6, j - 6, "UTF8"));
/*     */ 
/* 138 */           int n = i + j;
/* 139 */           if (n - i < k) {
/* 140 */             if (paramArrayOfByte[n] == 13)
/* 141 */               n += 2;
/*     */             else {
/* 143 */               n++;
/*     */             }
/*     */           }
/*     */ 
/* 147 */           while ((n - i < k) && 
/* 148 */             (paramArrayOfByte[(n++)] == 32))
/*     */           {
/* 150 */             int i1 = n;
/*     */ 
/* 152 */             while ((n - i < k) && (paramArrayOfByte[(n++)] != 10));
/* 153 */             if (paramArrayOfByte[(n - 1)] != 10)
/*     */               return;
/*     */             int i2;
/* 156 */             if (paramArrayOfByte[(n - 2)] == 13)
/* 157 */               i2 = n - i1 - 2;
/*     */             else {
/* 159 */               i2 = n - i1 - 1;
/*     */             }
/* 161 */             localStringBuilder.append(new String(paramArrayOfByte, i1, i2, "UTF8"));
/*     */           }
/*     */ 
/* 168 */           this.entries.put(localStringBuilder.toString(), new Entry(i, k, m, this.rawBytes));
/*     */         }
/*     */         catch (UnsupportedEncodingException localUnsupportedEncodingException)
/*     */         {
/* 173 */           throw new IllegalStateException("UTF8 not available on platform");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 178 */       i = localPosition.startOfNext;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isNameAttr(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 184 */     return ((paramArrayOfByte[paramInt] == 78) || (paramArrayOfByte[paramInt] == 110)) && ((paramArrayOfByte[(paramInt + 1)] == 97) || (paramArrayOfByte[(paramInt + 1)] == 65)) && ((paramArrayOfByte[(paramInt + 2)] == 109) || (paramArrayOfByte[(paramInt + 2)] == 77)) && ((paramArrayOfByte[(paramInt + 3)] == 101) || (paramArrayOfByte[(paramInt + 3)] == 69)) && (paramArrayOfByte[(paramInt + 4)] == 58) && (paramArrayOfByte[(paramInt + 5)] == 32);
/*     */   }
/*     */ 
/*     */   public Entry get(String paramString, boolean paramBoolean)
/*     */   {
/* 256 */     Entry localEntry = (Entry)this.entries.get(paramString);
/* 257 */     if (localEntry != null)
/* 258 */       localEntry.oldStyle = paramBoolean;
/* 259 */     return localEntry;
/*     */   }
/*     */ 
/*     */   public byte[] manifestDigest(MessageDigest paramMessageDigest)
/*     */   {
/* 264 */     paramMessageDigest.reset();
/* 265 */     paramMessageDigest.update(this.rawBytes, 0, this.rawBytes.length);
/* 266 */     return paramMessageDigest.digest();
/*     */   }
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     int offset;
/*     */     int length;
/*     */     int lengthWithBlankLine;
/*     */     byte[] rawBytes;
/*     */     boolean oldStyle;
/*     */ 
/*     */     public Entry(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte)
/*     */     {
/* 202 */       this.offset = paramInt1;
/* 203 */       this.length = paramInt2;
/* 204 */       this.lengthWithBlankLine = paramInt3;
/* 205 */       this.rawBytes = paramArrayOfByte;
/*     */     }
/*     */ 
/*     */     public byte[] digest(MessageDigest paramMessageDigest)
/*     */     {
/* 210 */       paramMessageDigest.reset();
/* 211 */       if (this.oldStyle)
/* 212 */         doOldStyle(paramMessageDigest, this.rawBytes, this.offset, this.lengthWithBlankLine);
/*     */       else {
/* 214 */         paramMessageDigest.update(this.rawBytes, this.offset, this.lengthWithBlankLine);
/*     */       }
/* 216 */       return paramMessageDigest.digest();
/*     */     }
/*     */ 
/*     */     private void doOldStyle(MessageDigest paramMessageDigest, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     {
/* 229 */       int i = paramInt1;
/* 230 */       int j = paramInt1;
/* 231 */       int k = paramInt1 + paramInt2;
/* 232 */       int m = -1;
/* 233 */       while (i < k) {
/* 234 */         if ((paramArrayOfByte[i] == 13) && (m == 32)) {
/* 235 */           paramMessageDigest.update(paramArrayOfByte, j, i - j - 1);
/* 236 */           j = i;
/*     */         }
/* 238 */         m = paramArrayOfByte[i];
/* 239 */         i++;
/*     */       }
/* 241 */       paramMessageDigest.update(paramArrayOfByte, j, i - j);
/*     */     }
/*     */ 
/*     */     public byte[] digestWorkaround(MessageDigest paramMessageDigest)
/*     */     {
/* 249 */       paramMessageDigest.reset();
/* 250 */       paramMessageDigest.update(this.rawBytes, this.offset, this.length);
/* 251 */       return paramMessageDigest.digest();
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Position
/*     */   {
/*     */     int endOfFirstLine;
/*     */     int endOfSection;
/*     */     int startOfNext;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.ManifestDigester
 * JD-Core Version:    0.6.2
 */
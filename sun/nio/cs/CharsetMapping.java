/*     */ package sun.nio.cs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ 
/*     */ public class CharsetMapping
/*     */ {
/*     */   public static final char UNMAPPABLE_DECODING = 'ÔøΩ';
/*     */   public static final int UNMAPPABLE_ENCODING = 65533;
/*     */   char[] b2cSB;
/*     */   char[] b2cDB1;
/*     */   char[] b2cDB2;
/*     */   int b2Min;
/*     */   int b2Max;
/*     */   int b1MinDB1;
/*     */   int b1MaxDB1;
/*     */   int b1MinDB2;
/*     */   int b1MaxDB2;
/*     */   int dbSegSize;
/*     */   char[] c2b;
/*     */   char[] c2bIndex;
/*     */   char[] b2cSupp;
/*     */   char[] c2bSupp;
/*     */   Entry[] b2cComp;
/*     */   Entry[] c2bComp;
/* 151 */   static Comparator<Entry> comparatorBytes = new Comparator()
/*     */   {
/*     */     public int compare(CharsetMapping.Entry paramAnonymousEntry1, CharsetMapping.Entry paramAnonymousEntry2) {
/* 154 */       return paramAnonymousEntry1.bs - paramAnonymousEntry2.bs;
/*     */     }
/*     */     public boolean equals(Object paramAnonymousObject) {
/* 157 */       return this == paramAnonymousObject;
/*     */     }
/* 151 */   };
/*     */ 
/* 161 */   static Comparator<Entry> comparatorCP = new Comparator()
/*     */   {
/*     */     public int compare(CharsetMapping.Entry paramAnonymousEntry1, CharsetMapping.Entry paramAnonymousEntry2) {
/* 164 */       return paramAnonymousEntry1.cp - paramAnonymousEntry2.cp;
/*     */     }
/*     */     public boolean equals(Object paramAnonymousObject) {
/* 167 */       return this == paramAnonymousObject;
/*     */     }
/* 161 */   };
/*     */ 
/* 171 */   static Comparator<Entry> comparatorComp = new Comparator()
/*     */   {
/*     */     public int compare(CharsetMapping.Entry paramAnonymousEntry1, CharsetMapping.Entry paramAnonymousEntry2) {
/* 174 */       int i = paramAnonymousEntry1.cp - paramAnonymousEntry2.cp;
/* 175 */       if (i == 0)
/* 176 */         i = paramAnonymousEntry1.cp2 - paramAnonymousEntry2.cp2;
/* 177 */       return i;
/*     */     }
/*     */     public boolean equals(Object paramAnonymousObject) {
/* 180 */       return this == paramAnonymousObject;
/*     */     }
/* 171 */   };
/*     */   private static final int MAP_SINGLEBYTE = 1;
/*     */   private static final int MAP_DOUBLEBYTE1 = 2;
/*     */   private static final int MAP_DOUBLEBYTE2 = 3;
/*     */   private static final int MAP_SUPPLEMENT = 5;
/*     */   private static final int MAP_SUPPLEMENT_C2B = 6;
/*     */   private static final int MAP_COMPOSITE = 7;
/*     */   private static final int MAP_INDEXC2B = 8;
/* 220 */   int off = 0;
/*     */   byte[] bb;
/*     */ 
/*     */   public char decodeSingle(int paramInt)
/*     */   {
/*  63 */     return this.b2cSB[paramInt];
/*     */   }
/*     */ 
/*     */   public char decodeDouble(int paramInt1, int paramInt2) {
/*  67 */     if ((paramInt2 >= this.b2Min) && (paramInt2 < this.b2Max)) {
/*  68 */       paramInt2 -= this.b2Min;
/*  69 */       if ((paramInt1 >= this.b1MinDB1) && (paramInt1 <= this.b1MaxDB1)) {
/*  70 */         paramInt1 -= this.b1MinDB1;
/*  71 */         return this.b2cDB1[(paramInt1 * this.dbSegSize + paramInt2)];
/*     */       }
/*  73 */       if ((paramInt1 >= this.b1MinDB2) && (paramInt1 <= this.b1MaxDB2)) {
/*  74 */         paramInt1 -= this.b1MinDB2;
/*  75 */         return this.b2cDB2[(paramInt1 * this.dbSegSize + paramInt2)];
/*     */       }
/*     */     }
/*  78 */     return 65533;
/*     */   }
/*     */ 
/*     */   public char[] decodeSurrogate(int paramInt, char[] paramArrayOfChar)
/*     */   {
/*  85 */     int i = this.b2cSupp.length / 2;
/*  86 */     int j = Arrays.binarySearch(this.b2cSupp, 0, i, (char)paramInt);
/*  87 */     if (j >= 0) {
/*  88 */       Character.toChars(this.b2cSupp[(i + j)] + 131072, paramArrayOfChar, 0);
/*  89 */       return paramArrayOfChar;
/*     */     }
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public char[] decodeComposite(Entry paramEntry, char[] paramArrayOfChar) {
/*  95 */     int i = findBytes(this.b2cComp, paramEntry);
/*  96 */     if (i >= 0) {
/*  97 */       paramArrayOfChar[0] = ((char)this.b2cComp[i].cp);
/*  98 */       paramArrayOfChar[1] = ((char)this.b2cComp[i].cp2);
/*  99 */       return paramArrayOfChar;
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   public int encodeChar(char paramChar) {
/* 105 */     int i = this.c2bIndex[(paramChar >> '\b')];
/* 106 */     if (i == 65535)
/* 107 */       return 65533;
/* 108 */     return this.c2b[(i + (paramChar & 0xFF))];
/*     */   }
/*     */ 
/*     */   public int encodeSurrogate(char paramChar1, char paramChar2) {
/* 112 */     int i = Character.toCodePoint(paramChar1, paramChar2);
/* 113 */     if ((i < 131072) || (i >= 196608))
/* 114 */       return 65533;
/* 115 */     int j = this.c2bSupp.length / 2;
/* 116 */     int k = Arrays.binarySearch(this.c2bSupp, 0, j, (char)i);
/* 117 */     if (k >= 0)
/* 118 */       return this.c2bSupp[(j + k)];
/* 119 */     return 65533;
/*     */   }
/*     */ 
/*     */   public boolean isCompositeBase(Entry paramEntry) {
/* 123 */     if ((paramEntry.cp <= 12791) && (paramEntry.cp >= 230)) {
/* 124 */       return findCP(this.c2bComp, paramEntry) >= 0;
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   public int encodeComposite(Entry paramEntry) {
/* 130 */     int i = findComp(this.c2bComp, paramEntry);
/* 131 */     if (i >= 0)
/* 132 */       return this.c2bComp[i].bs;
/* 133 */     return 65533;
/*     */   }
/*     */ 
/*     */   public static CharsetMapping get(InputStream paramInputStream)
/*     */   {
/* 138 */     return (CharsetMapping)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public CharsetMapping run() {
/* 140 */         return new CharsetMapping().load(this.val$is);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   static int findBytes(Entry[] paramArrayOfEntry, Entry paramEntry)
/*     */   {
/* 185 */     return Arrays.binarySearch(paramArrayOfEntry, 0, paramArrayOfEntry.length, paramEntry, comparatorBytes);
/*     */   }
/*     */ 
/*     */   static int findCP(Entry[] paramArrayOfEntry, Entry paramEntry) {
/* 189 */     return Arrays.binarySearch(paramArrayOfEntry, 0, paramArrayOfEntry.length, paramEntry, comparatorCP);
/*     */   }
/*     */ 
/*     */   static int findComp(Entry[] paramArrayOfEntry, Entry paramEntry) {
/* 193 */     return Arrays.binarySearch(paramArrayOfEntry, 0, paramArrayOfEntry.length, paramEntry, comparatorComp);
/*     */   }
/*     */ 
/*     */   private static final boolean readNBytes(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 209 */     int i = 0;
/* 210 */     while (paramInt > 0) {
/* 211 */       int j = paramInputStream.read(paramArrayOfByte, i, paramInt);
/* 212 */       if (j == -1)
/* 213 */         return false;
/* 214 */       paramInt -= j;
/* 215 */       i += j;
/*     */     }
/* 217 */     return true;
/*     */   }
/*     */ 
/*     */   private char[] readCharArray()
/*     */   {
/* 224 */     int i = (this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF;
/* 225 */     char[] arrayOfChar = new char[i];
/* 226 */     for (int j = 0; j < i; j++) {
/* 227 */       arrayOfChar[j] = ((char)((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF));
/*     */     }
/* 229 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   void readSINGLEBYTE() {
/* 233 */     char[] arrayOfChar = readCharArray();
/* 234 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 235 */       int j = arrayOfChar[i];
/* 236 */       if (j != 65533) {
/* 237 */         this.c2b[(this.c2bIndex[(j >> 8)] + (j & 0xFF))] = ((char)i);
/*     */       }
/*     */     }
/* 240 */     this.b2cSB = arrayOfChar;
/*     */   }
/*     */ 
/*     */   void readINDEXC2B() {
/* 244 */     char[] arrayOfChar = readCharArray();
/* 245 */     for (int i = arrayOfChar.length - 1; i >= 0; i--) {
/* 246 */       if ((this.c2b == null) && (arrayOfChar[i] != 'èøø')) {
/* 247 */         this.c2b = new char[arrayOfChar[i] + 'ƒÄ'];
/* 248 */         Arrays.fill(this.c2b, 65533);
/* 249 */         break;
/*     */       }
/*     */     }
/* 252 */     this.c2bIndex = arrayOfChar;
/*     */   }
/*     */ 
/*     */   char[] readDB(int paramInt1, int paramInt2, int paramInt3) {
/* 256 */     char[] arrayOfChar = readCharArray();
/* 257 */     for (int i = 0; i < arrayOfChar.length; i++) {
/* 258 */       int j = arrayOfChar[i];
/* 259 */       if (j != 65533) {
/* 260 */         int k = i / paramInt3;
/* 261 */         int m = i % paramInt3;
/* 262 */         int n = (k + paramInt1) * 256 + (m + paramInt2);
/*     */ 
/* 264 */         this.c2b[(this.c2bIndex[(j >> 8)] + (j & 0xFF))] = ((char)n);
/*     */       }
/*     */     }
/* 267 */     return arrayOfChar;
/*     */   }
/*     */ 
/*     */   void readDOUBLEBYTE1() {
/* 271 */     this.b1MinDB1 = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 272 */     this.b1MaxDB1 = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 273 */     this.b2Min = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 274 */     this.b2Max = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 275 */     this.dbSegSize = (this.b2Max - this.b2Min + 1);
/* 276 */     this.b2cDB1 = readDB(this.b1MinDB1, this.b2Min, this.dbSegSize);
/*     */   }
/*     */ 
/*     */   void readDOUBLEBYTE2() {
/* 280 */     this.b1MinDB2 = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 281 */     this.b1MaxDB2 = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 282 */     this.b2Min = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 283 */     this.b2Max = ((this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF);
/* 284 */     this.dbSegSize = (this.b2Max - this.b2Min + 1);
/* 285 */     this.b2cDB2 = readDB(this.b1MinDB2, this.b2Min, this.dbSegSize);
/*     */   }
/*     */ 
/*     */   void readCOMPOSITE() {
/* 289 */     char[] arrayOfChar = readCharArray();
/* 290 */     int i = arrayOfChar.length / 3;
/* 291 */     this.b2cComp = new Entry[i];
/* 292 */     this.c2bComp = new Entry[i];
/* 293 */     int j = 0; for (int k = 0; j < i; j++) {
/* 294 */       Entry localEntry = new Entry();
/* 295 */       localEntry.bs = arrayOfChar[(k++)];
/* 296 */       localEntry.cp = arrayOfChar[(k++)];
/* 297 */       localEntry.cp2 = arrayOfChar[(k++)];
/* 298 */       this.b2cComp[j] = localEntry;
/* 299 */       this.c2bComp[j] = localEntry;
/*     */     }
/* 301 */     Arrays.sort(this.c2bComp, 0, this.c2bComp.length, comparatorComp);
/*     */   }
/*     */ 
/*     */   CharsetMapping load(InputStream paramInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 308 */       int i = (paramInputStream.read() & 0xFF) << 24 | (paramInputStream.read() & 0xFF) << 16 | (paramInputStream.read() & 0xFF) << 8 | paramInputStream.read() & 0xFF;
/*     */ 
/* 310 */       this.bb = new byte[i];
/* 311 */       this.off = 0;
/*     */ 
/* 314 */       if (!readNBytes(paramInputStream, this.bb, i))
/* 315 */         throw new RuntimeException("Corrupted data file");
/* 316 */       paramInputStream.close();
/*     */ 
/* 318 */       while (this.off < i) {
/* 319 */         int j = (this.bb[(this.off++)] & 0xFF) << 8 | this.bb[(this.off++)] & 0xFF;
/* 320 */         switch (j) {
/*     */         case 8:
/* 322 */           readINDEXC2B();
/* 323 */           break;
/*     */         case 1:
/* 325 */           readSINGLEBYTE();
/* 326 */           break;
/*     */         case 2:
/* 328 */           readDOUBLEBYTE1();
/* 329 */           break;
/*     */         case 3:
/* 331 */           readDOUBLEBYTE2();
/* 332 */           break;
/*     */         case 5:
/* 334 */           this.b2cSupp = readCharArray();
/* 335 */           break;
/*     */         case 6:
/* 337 */           this.c2bSupp = readCharArray();
/* 338 */           break;
/*     */         case 7:
/* 340 */           readCOMPOSITE();
/* 341 */           break;
/*     */         case 4:
/*     */         default:
/* 343 */           throw new RuntimeException("Corrupted data file");
/*     */         }
/*     */       }
/* 346 */       this.bb = null;
/* 347 */       return this;
/*     */     } catch (IOException localIOException) {
/* 349 */       localIOException.printStackTrace();
/* 350 */     }return null;
/*     */   }
/*     */ 
/*     */   public static class Entry
/*     */   {
/*     */     public int bs;
/*     */     public int cp;
/*     */     public int cp2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.cs.CharsetMapping
 * JD-Core Version:    0.6.2
 */
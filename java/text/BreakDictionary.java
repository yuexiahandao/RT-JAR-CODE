/*     */ package java.text;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.MissingResourceException;
/*     */ import sun.text.CompactByteArray;
/*     */ import sun.text.SupplementaryCharacterData;
/*     */ 
/*     */ class BreakDictionary
/*     */ {
/*  70 */   private static int supportedVersion = 1;
/*     */ 
/*  76 */   private CompactByteArray columnMap = null;
/*  77 */   private SupplementaryCharacterData supplementaryCharColumnMap = null;
/*     */   private int numCols;
/*     */   private int numColGroups;
/* 102 */   private short[] table = null;
/*     */ 
/* 107 */   private short[] rowIndex = null;
/*     */ 
/* 115 */   private int[] rowIndexFlags = null;
/*     */ 
/* 125 */   private short[] rowIndexFlagsIndex = null;
/*     */ 
/* 131 */   private byte[] rowIndexShifts = null;
/*     */ 
/*     */   public BreakDictionary(String paramString)
/*     */     throws IOException, MissingResourceException
/*     */   {
/* 140 */     readDictionaryFile(paramString);
/*     */   }
/*     */ 
/*     */   private void readDictionaryFile(final String paramString) throws IOException, MissingResourceException
/*     */   {
/*     */     BufferedInputStream localBufferedInputStream;
/*     */     try
/*     */     {
/* 148 */       localBufferedInputStream = (BufferedInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Object run() throws Exception {
/* 151 */           return new BufferedInputStream(getClass().getResourceAsStream("/sun/text/resources/" + paramString));
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 157 */       throw new InternalError(localPrivilegedActionException.toString());
/*     */     }
/*     */ 
/* 160 */     byte[] arrayOfByte1 = new byte[8];
/* 161 */     if (localBufferedInputStream.read(arrayOfByte1) != 8) {
/* 162 */       throw new MissingResourceException("Wrong data length", paramString, "");
/*     */     }
/*     */ 
/* 167 */     int i = BreakIterator.getInt(arrayOfByte1, 0);
/* 168 */     if (i != supportedVersion) {
/* 169 */       throw new MissingResourceException("Dictionary version(" + i + ") is unsupported", paramString, "");
/*     */     }
/*     */ 
/* 174 */     int j = BreakIterator.getInt(arrayOfByte1, 4);
/* 175 */     arrayOfByte1 = new byte[j];
/* 176 */     if (localBufferedInputStream.read(arrayOfByte1) != j) {
/* 177 */       throw new MissingResourceException("Wrong data length", paramString, "");
/*     */     }
/*     */ 
/* 182 */     localBufferedInputStream.close();
/*     */ 
/* 185 */     int m = 0;
/*     */ 
/* 189 */     int k = BreakIterator.getInt(arrayOfByte1, m);
/* 190 */     m += 4;
/* 191 */     short[] arrayOfShort = new short[k];
/* 192 */     for (int n = 0; n < k; m += 2) {
/* 193 */       arrayOfShort[n] = BreakIterator.getShort(arrayOfByte1, m);
/*     */ 
/* 192 */       n++;
/*     */     }
/*     */ 
/* 195 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 196 */     m += 4;
/* 197 */     byte[] arrayOfByte2 = new byte[k];
/* 198 */     for (int i1 = 0; i1 < k; m++) {
/* 199 */       arrayOfByte2[i1] = arrayOfByte1[m];
/*     */ 
/* 198 */       i1++;
/*     */     }
/*     */ 
/* 201 */     this.columnMap = new CompactByteArray(arrayOfShort, arrayOfByte2);
/*     */ 
/* 204 */     this.numCols = BreakIterator.getInt(arrayOfByte1, m);
/* 205 */     m += 4;
/* 206 */     this.numColGroups = BreakIterator.getInt(arrayOfByte1, m);
/* 207 */     m += 4;
/*     */ 
/* 210 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 211 */     m += 4;
/* 212 */     this.rowIndex = new short[k];
/* 213 */     for (i1 = 0; i1 < k; m += 2) {
/* 214 */       this.rowIndex[i1] = BreakIterator.getShort(arrayOfByte1, m);
/*     */ 
/* 213 */       i1++;
/*     */     }
/*     */ 
/* 218 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 219 */     m += 4;
/* 220 */     this.rowIndexFlagsIndex = new short[k];
/* 221 */     for (i1 = 0; i1 < k; m += 2) {
/* 222 */       this.rowIndexFlagsIndex[i1] = BreakIterator.getShort(arrayOfByte1, m);
/*     */ 
/* 221 */       i1++;
/*     */     }
/*     */ 
/* 224 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 225 */     m += 4;
/* 226 */     this.rowIndexFlags = new int[k];
/* 227 */     for (i1 = 0; i1 < k; m += 4) {
/* 228 */       this.rowIndexFlags[i1] = BreakIterator.getInt(arrayOfByte1, m);
/*     */ 
/* 227 */       i1++;
/*     */     }
/*     */ 
/* 232 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 233 */     m += 4;
/* 234 */     this.rowIndexShifts = new byte[k];
/* 235 */     for (i1 = 0; i1 < k; m++) {
/* 236 */       this.rowIndexShifts[i1] = arrayOfByte1[m];
/*     */ 
/* 235 */       i1++;
/*     */     }
/*     */ 
/* 240 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 241 */     m += 4;
/* 242 */     this.table = new short[k];
/* 243 */     for (i1 = 0; i1 < k; m += 2) {
/* 244 */       this.table[i1] = BreakIterator.getShort(arrayOfByte1, m);
/*     */ 
/* 243 */       i1++;
/*     */     }
/*     */ 
/* 248 */     k = BreakIterator.getInt(arrayOfByte1, m);
/* 249 */     m += 4;
/* 250 */     int[] arrayOfInt = new int[k];
/* 251 */     for (int i2 = 0; i2 < k; m += 4) {
/* 252 */       arrayOfInt[i2] = BreakIterator.getInt(arrayOfByte1, m);
/*     */ 
/* 251 */       i2++;
/*     */     }
/*     */ 
/* 254 */     this.supplementaryCharColumnMap = new SupplementaryCharacterData(arrayOfInt);
/*     */   }
/*     */ 
/*     */   public final short getNextStateFromCharacter(int paramInt1, int paramInt2)
/*     */   {
/*     */     int i;
/* 270 */     if (paramInt2 < 65536)
/* 271 */       i = this.columnMap.elementAt((char)paramInt2);
/*     */     else {
/* 273 */       i = this.supplementaryCharColumnMap.getValue(paramInt2);
/*     */     }
/* 275 */     return getNextState(paramInt1, i);
/*     */   }
/*     */ 
/*     */   public final short getNextState(int paramInt1, int paramInt2)
/*     */   {
/* 290 */     if (cellIsPopulated(paramInt1, paramInt2))
/*     */     {
/* 297 */       return internalAt(this.rowIndex[paramInt1], paramInt2 + this.rowIndexShifts[paramInt1]);
/*     */     }
/*     */ 
/* 300 */     return 0;
/*     */   }
/*     */ 
/*     */   private final boolean cellIsPopulated(int paramInt1, int paramInt2)
/*     */   {
/* 312 */     if (this.rowIndexFlagsIndex[paramInt1] < 0) {
/* 313 */       return paramInt2 == -this.rowIndexFlagsIndex[paramInt1];
/*     */     }
/*     */ 
/* 323 */     int i = this.rowIndexFlags[(this.rowIndexFlagsIndex[paramInt1] + (paramInt2 >> 5))];
/* 324 */     return (i & 1 << (paramInt2 & 0x1F)) != 0;
/*     */   }
/*     */ 
/*     */   private final short internalAt(int paramInt1, int paramInt2)
/*     */   {
/* 339 */     return this.table[(paramInt1 * this.numCols + paramInt2)];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.BreakDictionary
 * JD-Core Version:    0.6.2
 */
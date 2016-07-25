/*     */ package java.text;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ final class MergeCollation
/*     */ {
/* 207 */   ArrayList patterns = new ArrayList();
/*     */ 
/* 209 */   private transient PatternEntry saveEntry = null;
/* 210 */   private transient PatternEntry lastEntry = null;
/*     */ 
/* 214 */   private transient StringBuffer excess = new StringBuffer();
/*     */ 
/* 224 */   private transient byte[] statusArray = new byte[8192];
/* 225 */   private final byte BITARRAYMASK = 1;
/* 226 */   private final int BYTEPOWER = 3;
/* 227 */   private final int BYTEMASK = 7;
/*     */ 
/*     */   public MergeCollation(String paramString)
/*     */     throws ParseException
/*     */   {
/*  71 */     for (int i = 0; i < this.statusArray.length; i++)
/*  72 */       this.statusArray[i] = 0;
/*  73 */     setPattern(paramString);
/*     */   }
/*     */ 
/*     */   public String getPattern()
/*     */   {
/*  80 */     return getPattern(true);
/*     */   }
/*     */ 
/*     */   public String getPattern(boolean paramBoolean)
/*     */   {
/*  89 */     StringBuffer localStringBuffer = new StringBuffer();
/*  90 */     PatternEntry localPatternEntry1 = null;
/*  91 */     ArrayList localArrayList = null;
/*     */     PatternEntry localPatternEntry2;
/*  93 */     for (int i = 0; i < this.patterns.size(); i++) {
/*  94 */       localPatternEntry2 = (PatternEntry)this.patterns.get(i);
/*  95 */       if (localPatternEntry2.extension.length() != 0) {
/*  96 */         if (localArrayList == null)
/*  97 */           localArrayList = new ArrayList();
/*  98 */         localArrayList.add(localPatternEntry2);
/*     */       } else {
/* 100 */         if (localArrayList != null) {
/* 101 */           PatternEntry localPatternEntry3 = findLastWithNoExtension(i - 1);
/* 102 */           for (int k = localArrayList.size() - 1; k >= 0; k--) {
/* 103 */             localPatternEntry1 = (PatternEntry)localArrayList.get(k);
/* 104 */             localPatternEntry1.addToBuffer(localStringBuffer, false, paramBoolean, localPatternEntry3);
/*     */           }
/* 106 */           localArrayList = null;
/*     */         }
/* 108 */         localPatternEntry2.addToBuffer(localStringBuffer, false, paramBoolean, null);
/*     */       }
/*     */     }
/* 111 */     if (localArrayList != null) {
/* 112 */       localPatternEntry2 = findLastWithNoExtension(i - 1);
/* 113 */       for (int j = localArrayList.size() - 1; j >= 0; j--) {
/* 114 */         localPatternEntry1 = (PatternEntry)localArrayList.get(j);
/* 115 */         localPatternEntry1.addToBuffer(localStringBuffer, false, paramBoolean, localPatternEntry2);
/*     */       }
/* 117 */       localArrayList = null;
/*     */     }
/* 119 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private final PatternEntry findLastWithNoExtension(int paramInt) {
/* 123 */     for (paramInt--; paramInt >= 0; paramInt--) {
/* 124 */       PatternEntry localPatternEntry = (PatternEntry)this.patterns.get(paramInt);
/* 125 */       if (localPatternEntry.extension.length() == 0) {
/* 126 */         return localPatternEntry;
/*     */       }
/*     */     }
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public String emitPattern()
/*     */   {
/* 138 */     return emitPattern(true);
/*     */   }
/*     */ 
/*     */   public String emitPattern(boolean paramBoolean)
/*     */   {
/* 149 */     StringBuffer localStringBuffer = new StringBuffer();
/* 150 */     for (int i = 0; i < this.patterns.size(); i++)
/*     */     {
/* 152 */       PatternEntry localPatternEntry = (PatternEntry)this.patterns.get(i);
/* 153 */       if (localPatternEntry != null) {
/* 154 */         localPatternEntry.addToBuffer(localStringBuffer, true, paramBoolean, null);
/*     */       }
/*     */     }
/* 157 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void setPattern(String paramString)
/*     */     throws ParseException
/*     */   {
/* 165 */     this.patterns.clear();
/* 166 */     addPattern(paramString);
/*     */   }
/*     */ 
/*     */   public void addPattern(String paramString)
/*     */     throws ParseException
/*     */   {
/* 175 */     if (paramString == null) {
/* 176 */       return;
/*     */     }
/* 178 */     PatternEntry.Parser localParser = new PatternEntry.Parser(paramString);
/*     */ 
/* 180 */     PatternEntry localPatternEntry = localParser.next();
/* 181 */     while (localPatternEntry != null) {
/* 182 */       fixEntry(localPatternEntry);
/* 183 */       localPatternEntry = localParser.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getCount()
/*     */   {
/* 192 */     return this.patterns.size();
/*     */   }
/*     */ 
/*     */   public PatternEntry getItemAt(int paramInt)
/*     */   {
/* 201 */     return (PatternEntry)this.patterns.get(paramInt);
/*     */   }
/*     */ 
/*     */   private final void fixEntry(PatternEntry paramPatternEntry)
/*     */     throws ParseException
/*     */   {
/* 242 */     if ((this.lastEntry != null) && (paramPatternEntry.chars.equals(this.lastEntry.chars)) && (paramPatternEntry.extension.equals(this.lastEntry.extension)))
/*     */     {
/* 244 */       if ((paramPatternEntry.strength != 3) && (paramPatternEntry.strength != -2))
/*     */       {
/* 246 */         throw new ParseException("The entries " + this.lastEntry + " and " + paramPatternEntry + " are adjacent in the rules, but have conflicting " + "strengths: A character can't be unequal to itself.", -1);
/*     */       }
/*     */ 
/* 251 */       return;
/*     */     }
/*     */ 
/* 255 */     int i = 1;
/* 256 */     if (paramPatternEntry.strength != -2) {
/* 257 */       int j = -1;
/*     */ 
/* 259 */       if (paramPatternEntry.chars.length() == 1)
/*     */       {
/* 261 */         k = paramPatternEntry.chars.charAt(0);
/* 262 */         int m = k >> 3;
/* 263 */         int n = this.statusArray[m];
/* 264 */         int i1 = (byte)(1 << (k & 0x7));
/*     */ 
/* 266 */         if ((n != 0) && ((n & i1) != 0)) {
/* 267 */           j = this.patterns.lastIndexOf(paramPatternEntry);
/*     */         }
/*     */         else
/*     */         {
/* 271 */           this.statusArray[m] = ((byte)(n | i1));
/*     */         }
/*     */       } else {
/* 274 */         j = this.patterns.lastIndexOf(paramPatternEntry);
/*     */       }
/* 276 */       if (j != -1) {
/* 277 */         this.patterns.remove(j);
/*     */       }
/*     */ 
/* 280 */       this.excess.setLength(0);
/* 281 */       int k = findLastEntry(this.lastEntry, this.excess);
/*     */ 
/* 283 */       if (this.excess.length() != 0) {
/* 284 */         paramPatternEntry.extension = (this.excess + paramPatternEntry.extension);
/* 285 */         if (k != this.patterns.size()) {
/* 286 */           this.lastEntry = this.saveEntry;
/* 287 */           i = 0;
/*     */         }
/*     */       }
/* 290 */       if (k == this.patterns.size()) {
/* 291 */         this.patterns.add(paramPatternEntry);
/* 292 */         this.saveEntry = paramPatternEntry;
/*     */       } else {
/* 294 */         this.patterns.add(k, paramPatternEntry);
/*     */       }
/*     */     }
/* 297 */     if (i != 0)
/* 298 */       this.lastEntry = paramPatternEntry;
/*     */   }
/*     */ 
/*     */   private final int findLastEntry(PatternEntry paramPatternEntry, StringBuffer paramStringBuffer)
/*     */     throws ParseException
/*     */   {
/* 305 */     if (paramPatternEntry == null) {
/* 306 */       return 0;
/*     */     }
/* 308 */     if (paramPatternEntry.strength != -2)
/*     */     {
/* 312 */       i = -1;
/* 313 */       if (paramPatternEntry.chars.length() == 1) {
/* 314 */         int j = paramPatternEntry.chars.charAt(0) >> '\003';
/* 315 */         if ((this.statusArray[j] & '\001' << (paramPatternEntry.chars.charAt(0) & 0x7)) != 0)
/*     */         {
/* 317 */           i = this.patterns.lastIndexOf(paramPatternEntry);
/*     */         }
/*     */       } else {
/* 320 */         i = this.patterns.lastIndexOf(paramPatternEntry);
/*     */       }
/* 322 */       if (i == -1) {
/* 323 */         throw new ParseException("couldn't find last entry: " + paramPatternEntry, i);
/*     */       }
/* 325 */       return i + 1;
/*     */     }
/*     */ 
/* 328 */     for (int i = this.patterns.size() - 1; i >= 0; i--) {
/* 329 */       PatternEntry localPatternEntry = (PatternEntry)this.patterns.get(i);
/* 330 */       if (localPatternEntry.chars.regionMatches(0, paramPatternEntry.chars, 0, localPatternEntry.chars.length()))
/*     */       {
/* 332 */         paramStringBuffer.append(paramPatternEntry.chars.substring(localPatternEntry.chars.length(), paramPatternEntry.chars.length()));
/*     */ 
/* 334 */         break;
/*     */       }
/*     */     }
/* 337 */     if (i == -1)
/* 338 */       throw new ParseException("couldn't find: " + paramPatternEntry, i);
/* 339 */     return i + 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.MergeCollation
 * JD-Core Version:    0.6.2
 */
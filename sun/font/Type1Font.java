/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.FontFormatException;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.MappedByteBuffer;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.channels.FileChannel.MapMode;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ 
/*     */ public class Type1Font extends FileFont
/*     */ {
/* 101 */   WeakReference bufferRef = new WeakReference(null);
/*     */ 
/* 103 */   private String psName = null;
/*     */ 
/* 109 */   private static HashMap styleAbbreviationsMapping = new HashMap();
/* 110 */   private static HashSet styleNameTokes = new HashSet();
/*     */   private static final int PSEOFTOKEN = 0;
/*     */   private static final int PSNAMETOKEN = 1;
/*     */   private static final int PSSTRINGTOKEN = 2;
/*     */ 
/*     */   public Type1Font(String paramString, Object paramObject)
/*     */     throws FontFormatException
/*     */   {
/* 157 */     this(paramString, paramObject, false);
/*     */   }
/*     */ 
/*     */   public Type1Font(String paramString, Object paramObject, boolean paramBoolean)
/*     */     throws FontFormatException
/*     */   {
/* 169 */     super(paramString, paramObject);
/* 170 */     this.fontRank = 4;
/* 171 */     this.checkedNatives = true;
/*     */     try {
/* 173 */       verify();
/*     */     } catch (Throwable localThrowable) {
/* 175 */       if (paramBoolean) {
/* 176 */         T1DisposerRecord localT1DisposerRecord = new T1DisposerRecord(paramString);
/* 177 */         Disposer.addObjectRecord(this.bufferRef, localT1DisposerRecord);
/* 178 */         this.bufferRef = null;
/*     */       }
/* 180 */       if ((localThrowable instanceof FontFormatException)) {
/* 181 */         throw ((FontFormatException)localThrowable);
/*     */       }
/* 183 */       throw new FontFormatException("Unexpected runtime exception.");
/*     */     }
/*     */   }
/*     */ 
/*     */   private synchronized ByteBuffer getBuffer() throws FontFormatException
/*     */   {
/* 189 */     MappedByteBuffer localMappedByteBuffer = (MappedByteBuffer)this.bufferRef.get();
/* 190 */     if (localMappedByteBuffer == null) {
/*     */       try
/*     */       {
/* 193 */         RandomAccessFile localRandomAccessFile = (RandomAccessFile)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run()
/*     */           {
/*     */             try {
/* 198 */               return new RandomAccessFile(Type1Font.this.platName, "r");
/*     */             } catch (FileNotFoundException localFileNotFoundException) {
/*     */             }
/* 201 */             return null;
/*     */           }
/*     */         });
/* 204 */         FileChannel localFileChannel = localRandomAccessFile.getChannel();
/* 205 */         this.fileSize = ((int)localFileChannel.size());
/* 206 */         localMappedByteBuffer = localFileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, this.fileSize);
/* 207 */         localMappedByteBuffer.position(0);
/* 208 */         this.bufferRef = new WeakReference(localMappedByteBuffer);
/* 209 */         localFileChannel.close();
/*     */       } catch (NullPointerException localNullPointerException) {
/* 211 */         throw new FontFormatException(localNullPointerException.toString());
/*     */       }
/*     */       catch (ClosedChannelException localClosedChannelException)
/*     */       {
/* 216 */         Thread.interrupted();
/* 217 */         return getBuffer();
/*     */       } catch (IOException localIOException) {
/* 219 */         throw new FontFormatException(localIOException.toString());
/*     */       }
/*     */     }
/* 222 */     return localMappedByteBuffer;
/*     */   }
/*     */ 
/*     */   protected void close()
/*     */   {
/*     */   }
/*     */ 
/*     */   void readFile(ByteBuffer paramByteBuffer) {
/* 230 */     RandomAccessFile localRandomAccessFile = null;
/*     */     try
/*     */     {
/* 233 */       localRandomAccessFile = (RandomAccessFile)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/*     */           try {
/* 238 */             return new RandomAccessFile(Type1Font.this.platName, "r");
/*     */           } catch (FileNotFoundException localFileNotFoundException) {
/*     */           }
/* 241 */           return null;
/*     */         } } );
/* 244 */       FileChannel localFileChannel = localRandomAccessFile.getChannel();
/* 245 */       while ((paramByteBuffer.remaining() > 0) && (localFileChannel.read(paramByteBuffer) != -1));
/*     */     } catch (NullPointerException localNullPointerException) {
/*     */     } catch (ClosedChannelException localClosedChannelException) {
/*     */       try { if (localRandomAccessFile != null) {
/* 250 */           localRandomAccessFile.close();
/* 251 */           localRandomAccessFile = null;
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException6)
/*     */       {
/*     */       }
/*     */ 
/* 258 */       Thread.interrupted();
/* 259 */       readFile(paramByteBuffer);
/*     */     } catch (IOException localIOException4) {
/*     */     } finally {
/* 262 */       if (localRandomAccessFile != null)
/*     */         try {
/* 264 */           localRandomAccessFile.close();
/*     */         }
/*     */         catch (IOException localIOException7) {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized ByteBuffer readBlock(int paramInt1, int paramInt2) {
/* 272 */     ByteBuffer localByteBuffer = null;
/*     */     try {
/* 274 */       localByteBuffer = getBuffer();
/* 275 */       if (paramInt1 > this.fileSize) {
/* 276 */         paramInt1 = this.fileSize;
/*     */       }
/* 278 */       localByteBuffer.position(paramInt1);
/* 279 */       return localByteBuffer.slice(); } catch (FontFormatException localFontFormatException) {
/*     */     }
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */   private void verify()
/*     */     throws FontFormatException
/*     */   {
/* 291 */     ByteBuffer localByteBuffer = getBuffer();
/* 292 */     if (localByteBuffer.capacity() < 6) {
/* 293 */       throw new FontFormatException("short file");
/*     */     }
/* 295 */     int i = localByteBuffer.get(0) & 0xFF;
/* 296 */     if ((localByteBuffer.get(0) & 0xFF) == 128) {
/* 297 */       verifyPFB(localByteBuffer);
/* 298 */       localByteBuffer.position(6);
/*     */     } else {
/* 300 */       verifyPFA(localByteBuffer);
/* 301 */       localByteBuffer.position(0);
/*     */     }
/* 303 */     initNames(localByteBuffer);
/* 304 */     if ((this.familyName == null) || (this.fullName == null)) {
/* 305 */       throw new FontFormatException("Font name not found");
/*     */     }
/* 307 */     setStyle();
/*     */   }
/*     */ 
/*     */   public int getFileSize() {
/* 311 */     if (this.fileSize == 0)
/*     */       try {
/* 313 */         getBuffer();
/*     */       }
/*     */       catch (FontFormatException localFontFormatException) {
/*     */       }
/* 317 */     return this.fileSize;
/*     */   }
/*     */ 
/*     */   private void verifyPFA(ByteBuffer paramByteBuffer) throws FontFormatException {
/* 321 */     if (paramByteBuffer.getShort() != 9505)
/* 322 */       throw new FontFormatException("bad pfa font");
/*     */   }
/*     */ 
/*     */   private void verifyPFB(ByteBuffer paramByteBuffer)
/*     */     throws FontFormatException
/*     */   {
/* 329 */     int i = 0;
/*     */     try {
/*     */       while (true) {
/* 332 */         int j = paramByteBuffer.getShort(i) & 0xFFFF;
/* 333 */         if ((j == 32769) || (j == 32770)) {
/* 334 */           paramByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
/* 335 */           int k = paramByteBuffer.getInt(i + 2);
/* 336 */           paramByteBuffer.order(ByteOrder.BIG_ENDIAN);
/* 337 */           if (k <= 0) {
/* 338 */             throw new FontFormatException("bad segment length");
/*     */           }
/* 340 */           i += k + 6; } else {
/* 341 */           if (j == 32771) {
/* 342 */             return;
/*     */           }
/* 344 */           throw new FontFormatException("bad pfb file");
/*     */         }
/*     */       }
/*     */     } catch (BufferUnderflowException localBufferUnderflowException) { throw new FontFormatException(localBufferUnderflowException.toString());
/*     */     } catch (Exception localException) {
/* 349 */       throw new FontFormatException(localException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initNames(ByteBuffer paramByteBuffer)
/*     */     throws FontFormatException
/*     */   {
/* 367 */     int i = 0;
/* 368 */     Object localObject = null;
/*     */     try
/*     */     {
/* 372 */       while (((this.fullName == null) || (this.familyName == null) || (this.psName == null) || (localObject == null)) && (i == 0)) {
/* 373 */         int j = nextTokenType(paramByteBuffer);
/* 374 */         if (j == 1) {
/* 375 */           int k = paramByteBuffer.position();
/* 376 */           if (paramByteBuffer.get(k) == 70) {
/* 377 */             String str2 = getSimpleToken(paramByteBuffer);
/* 378 */             if ("FullName".equals(str2)) {
/* 379 */               if (nextTokenType(paramByteBuffer) == 2)
/* 380 */                 this.fullName = getString(paramByteBuffer);
/*     */             }
/* 382 */             else if ("FamilyName".equals(str2)) {
/* 383 */               if (nextTokenType(paramByteBuffer) == 2)
/* 384 */                 this.familyName = getString(paramByteBuffer);
/*     */             }
/* 386 */             else if ("FontName".equals(str2)) {
/* 387 */               if (nextTokenType(paramByteBuffer) == 1)
/* 388 */                 this.psName = getSimpleToken(paramByteBuffer);
/*     */             }
/* 390 */             else if ("FontType".equals(str2))
/*     */             {
/* 394 */               String str3 = getSimpleToken(paramByteBuffer);
/* 395 */               if ("def".equals(getSimpleToken(paramByteBuffer)))
/* 396 */                 localObject = str3;
/*     */             }
/*     */           }
/* 400 */           else { while (paramByteBuffer.get() > 32); }
/*     */ 
/*     */         }
/* 402 */         else if (j == 0) {
/* 403 */           i = 1;
/*     */         }
/*     */       }
/*     */     } catch (Exception localException) {
/* 407 */       throw new FontFormatException(localException.toString());
/*     */     }
/*     */ 
/* 411 */     if (!"1".equals(localObject)) {
/* 412 */       throw new FontFormatException("Unsupported font type");
/*     */     }
/*     */ 
/* 415 */     if (this.psName == null)
/*     */     {
/* 421 */       paramByteBuffer.position(0);
/* 422 */       if (paramByteBuffer.getShort() != 9505)
/*     */       {
/* 424 */         paramByteBuffer.position(8);
/*     */       }
/*     */ 
/* 428 */       String str1 = getSimpleToken(paramByteBuffer);
/* 429 */       if ((!str1.startsWith("FontType1-")) && (!str1.startsWith("PS-AdobeFont-"))) {
/* 430 */         throw new FontFormatException("Unsupported font format [" + str1 + "]");
/*     */       }
/* 432 */       this.psName = getSimpleToken(paramByteBuffer);
/*     */     }
/*     */ 
/* 438 */     if (i != 0)
/*     */     {
/* 440 */       if (this.fullName != null) {
/* 441 */         this.familyName = fullName2FamilyName(this.fullName);
/* 442 */       } else if (this.familyName != null) {
/* 443 */         this.fullName = this.familyName;
/*     */       } else {
/* 445 */         this.fullName = psName2FullName(this.psName);
/* 446 */         this.familyName = psName2FamilyName(this.psName);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private String fullName2FamilyName(String paramString)
/*     */   {
/* 458 */     int j = paramString.length();
/*     */ 
/* 460 */     while (j > 0) {
/* 461 */       int i = j - 1;
/* 462 */       while ((i > 0) && (paramString.charAt(i) != ' ')) {
/* 463 */         i--;
/*     */       }
/*     */ 
/* 466 */       if (!isStyleToken(paramString.substring(i + 1, j))) {
/* 467 */         return paramString.substring(0, j);
/*     */       }
/* 469 */       j = i;
/*     */     }
/*     */ 
/* 472 */     return paramString;
/*     */   }
/*     */ 
/*     */   private String expandAbbreviation(String paramString) {
/* 476 */     if (styleAbbreviationsMapping.containsKey(paramString))
/* 477 */       return (String)styleAbbreviationsMapping.get(paramString);
/* 478 */     return paramString;
/*     */   }
/*     */ 
/*     */   private boolean isStyleToken(String paramString) {
/* 482 */     return styleNameTokes.contains(paramString);
/*     */   }
/*     */ 
/*     */   private String psName2FullName(String paramString)
/*     */   {
/* 495 */     int i = paramString.indexOf("-");
/*     */     String str;
/* 496 */     if (i >= 0) {
/* 497 */       str = expandName(paramString.substring(0, i), false);
/* 498 */       str = str + " " + expandName(paramString.substring(i + 1), true);
/*     */     } else {
/* 500 */       str = expandName(paramString, false);
/*     */     }
/*     */ 
/* 503 */     return str;
/*     */   }
/*     */ 
/*     */   private String psName2FamilyName(String paramString) {
/* 507 */     String str = paramString;
/*     */ 
/* 516 */     if (str.indexOf("-") > 0) {
/* 517 */       str = str.substring(0, str.indexOf("-"));
/*     */     }
/*     */ 
/* 520 */     return expandName(str, false);
/*     */   }
/*     */ 
/*     */   private int nextCapitalLetter(String paramString, int paramInt) {
/* 524 */     for (; (paramInt >= 0) && (paramInt < paramString.length()); paramInt++) {
/* 525 */       if ((paramString.charAt(paramInt) >= 'A') && (paramString.charAt(paramInt) <= 'Z'))
/* 526 */         return paramInt;
/*     */     }
/* 528 */     return -1;
/*     */   }
/*     */ 
/*     */   private String expandName(String paramString, boolean paramBoolean) {
/* 532 */     StringBuffer localStringBuffer = new StringBuffer(paramString.length() + 10);
/* 533 */     int i = 0;
/*     */ 
/* 535 */     while (i < paramString.length()) {
/* 536 */       int j = nextCapitalLetter(paramString, i + 1);
/* 537 */       if (j < 0) {
/* 538 */         j = paramString.length();
/*     */       }
/*     */ 
/* 541 */       if (i != 0) {
/* 542 */         localStringBuffer.append(" ");
/*     */       }
/*     */ 
/* 545 */       if (paramBoolean)
/* 546 */         localStringBuffer.append(expandAbbreviation(paramString.substring(i, j)));
/*     */       else {
/* 548 */         localStringBuffer.append(paramString.substring(i, j));
/*     */       }
/* 550 */       i = j;
/*     */     }
/*     */ 
/* 553 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private byte skip(ByteBuffer paramByteBuffer)
/*     */   {
/* 558 */     byte b = paramByteBuffer.get();
/* 559 */     while (b == 37) {
/*     */       do {
/* 561 */         b = paramByteBuffer.get();
/* 562 */         if (b == 13) break;  } while (b != 10);
/*     */     }
/*     */ 
/* 567 */     while (b <= 32) {
/* 568 */       b = paramByteBuffer.get();
/*     */     }
/* 570 */     return b;
/*     */   }
/*     */ 
/*     */   private int nextTokenType(ByteBuffer paramByteBuffer)
/*     */   {
/*     */     try
/*     */     {
/* 581 */       int i = skip(paramByteBuffer);
/*     */       while (true)
/*     */       {
/* 584 */         if (i == 47)
/* 585 */           return 1;
/* 586 */         if (i == 40)
/* 587 */           return 2;
/* 588 */         if ((i == 13) || (i == 10))
/* 589 */           i = skip(paramByteBuffer);
/*     */         else
/* 591 */           i = paramByteBuffer.get();
/*     */       }
/*     */     } catch (BufferUnderflowException localBufferUnderflowException) {
/*     */     }
/* 595 */     return 0;
/*     */   }
/*     */ 
/*     */   private String getSimpleToken(ByteBuffer paramByteBuffer)
/*     */   {
/* 603 */     while (paramByteBuffer.get() <= 32);
/* 604 */     int i = paramByteBuffer.position() - 1;
/* 605 */     while (paramByteBuffer.get() > 32);
/* 606 */     int j = paramByteBuffer.position();
/* 607 */     byte[] arrayOfByte = new byte[j - i - 1];
/* 608 */     paramByteBuffer.position(i);
/* 609 */     paramByteBuffer.get(arrayOfByte);
/*     */     try {
/* 611 */       return new String(arrayOfByte, "US-ASCII"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 613 */     return new String(arrayOfByte);
/*     */   }
/*     */ 
/*     */   private String getString(ByteBuffer paramByteBuffer)
/*     */   {
/* 618 */     int i = paramByteBuffer.position();
/* 619 */     while (paramByteBuffer.get() != 41);
/* 620 */     int j = paramByteBuffer.position();
/* 621 */     byte[] arrayOfByte = new byte[j - i - 1];
/* 622 */     paramByteBuffer.position(i);
/* 623 */     paramByteBuffer.get(arrayOfByte);
/*     */     try {
/* 625 */       return new String(arrayOfByte, "US-ASCII"); } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*     */     }
/* 627 */     return new String(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public String getPostscriptName()
/*     */   {
/* 633 */     return this.psName;
/*     */   }
/*     */ 
/*     */   protected synchronized FontScaler getScaler() {
/* 637 */     if (this.scaler == null) {
/* 638 */       this.scaler = FontScaler.getScaler(this, 0, false, this.fileSize);
/*     */     }
/*     */ 
/* 641 */     return this.scaler;
/*     */   }
/*     */ 
/*     */   CharToGlyphMapper getMapper() {
/* 645 */     if (this.mapper == null) {
/* 646 */       this.mapper = new Type1GlyphMapper(this);
/*     */     }
/* 648 */     return this.mapper;
/*     */   }
/*     */ 
/*     */   public int getNumGlyphs() {
/*     */     try {
/* 653 */       return getScaler().getNumGlyphs();
/*     */     } catch (FontScalerException localFontScalerException) {
/* 655 */       this.scaler = FontScaler.getNullScaler();
/* 656 */     }return getNumGlyphs();
/*     */   }
/*     */ 
/*     */   public int getMissingGlyphCode()
/*     */   {
/*     */     try {
/* 662 */       return getScaler().getMissingGlyphCode();
/*     */     } catch (FontScalerException localFontScalerException) {
/* 664 */       this.scaler = FontScaler.getNullScaler();
/* 665 */     }return getMissingGlyphCode();
/*     */   }
/*     */ 
/*     */   public int getGlyphCode(char paramChar)
/*     */   {
/*     */     try {
/* 671 */       return getScaler().getGlyphCode(paramChar);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 673 */       this.scaler = FontScaler.getNullScaler();
/* 674 */     }return getGlyphCode(paramChar);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 679 */     return "** Type1 Font: Family=" + this.familyName + " Name=" + this.fullName + " style=" + this.style + " fileName=" + getPublicFileName();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 117 */     String[] arrayOfString1 = { "Black", "Bold", "Book", "Demi", "Heavy", "Light", "Meduium", "Nord", "Poster", "Regular", "Super", "Thin", "Compressed", "Condensed", "Compact", "Extended", "Narrow", "Inclined", "Italic", "Kursiv", "Oblique", "Upright", "Sloped", "Semi", "Ultra", "Extra", "Alternate", "Alternate", "Deutsche Fraktur", "Expert", "Inline", "Ornaments", "Outline", "Roman", "Rounded", "Script", "Shaded", "Swash", "Titling", "Typewriter" };
/*     */ 
/* 124 */     String[] arrayOfString2 = { "Blk", "Bd", "Bk", "Dm", "Hv", "Lt", "Md", "Nd", "Po", "Rg", "Su", "Th", "Cm", "Cn", "Ct", "Ex", "Nr", "Ic", "It", "Ks", "Obl", "Up", "Sl", "Sm", "Ult", "X", "A", "Alt", "Dfr", "Exp", "In", "Or", "Ou", "Rm", "Rd", "Scr", "Sh", "Sw", "Ti", "Typ" };
/*     */ 
/* 134 */     String[] arrayOfString3 = { "Black", "Bold", "Book", "Demi", "Heavy", "Light", "Medium", "Nord", "Poster", "Regular", "Super", "Thin", "Compressed", "Condensed", "Compact", "Extended", "Narrow", "Inclined", "Italic", "Kursiv", "Oblique", "Upright", "Sloped", "Slanted", "Semi", "Ultra", "Extra" };
/*     */ 
/* 140 */     for (int i = 0; i < arrayOfString1.length; i++) {
/* 141 */       styleAbbreviationsMapping.put(arrayOfString2[i], arrayOfString1[i]);
/*     */     }
/* 143 */     for (i = 0; i < arrayOfString3.length; i++)
/* 144 */       styleNameTokes.add(arrayOfString3[i]);
/*     */   }
/*     */ 
/*     */   private static class T1DisposerRecord
/*     */     implements DisposerRecord
/*     */   {
/*  81 */     String fileName = null;
/*     */ 
/*     */     T1DisposerRecord(String paramString) {
/*  84 */       this.fileName = paramString;
/*     */     }
/*     */ 
/*     */     public synchronized void dispose() {
/*  88 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/*  92 */           if (Type1Font.T1DisposerRecord.this.fileName != null) {
/*  93 */             new File(Type1Font.T1DisposerRecord.this.fileName).delete();
/*     */           }
/*  95 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.Type1Font
 * JD-Core Version:    0.6.2
 */
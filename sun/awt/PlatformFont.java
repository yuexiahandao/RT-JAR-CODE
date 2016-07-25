/*     */ package sun.awt;
/*     */ 
/*     */ import java.awt.peer.FontPeer;
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.Locale;
/*     */ import java.util.Vector;
/*     */ import sun.font.SunFontManager;
/*     */ import sun.java2d.FontSupport;
/*     */ 
/*     */ public abstract class PlatformFont
/*     */   implements FontPeer
/*     */ {
/*     */   protected FontDescriptor[] componentFonts;
/*     */   protected char defaultChar;
/*     */   protected FontConfiguration fontConfig;
/*     */   protected FontDescriptor defaultFont;
/*     */   protected String familyName;
/*     */   private Object[] fontCache;
/*  55 */   protected static int FONTCACHESIZE = 256;
/*  56 */   protected static int FONTCACHEMASK = FONTCACHESIZE - 1;
/*     */   protected static String osVersion;
/*     */ 
/*     */   public PlatformFont(String paramString, int paramInt)
/*     */   {
/*  60 */     SunFontManager localSunFontManager = SunFontManager.getInstance();
/*  61 */     if ((localSunFontManager instanceof FontSupport)) {
/*  62 */       this.fontConfig = localSunFontManager.getFontConfiguration();
/*     */     }
/*  64 */     if (this.fontConfig == null) {
/*  65 */       return;
/*     */     }
/*     */ 
/*  69 */     this.familyName = paramString.toLowerCase(Locale.ENGLISH);
/*  70 */     if (!FontConfiguration.isLogicalFontFamilyName(this.familyName)) {
/*  71 */       this.familyName = this.fontConfig.getFallbackFamilyName(this.familyName, "sansserif");
/*     */     }
/*     */ 
/*  74 */     this.componentFonts = this.fontConfig.getFontDescriptors(this.familyName, paramInt);
/*     */ 
/*  78 */     char c = getMissingGlyphCharacter();
/*     */ 
/*  80 */     this.defaultChar = '?';
/*  81 */     if (this.componentFonts.length > 0) {
/*  82 */       this.defaultFont = this.componentFonts[0];
/*     */     }
/*  84 */     for (int i = 0; i < this.componentFonts.length; i++)
/*  85 */       if (!this.componentFonts[i].isExcluded(c))
/*     */       {
/*  89 */         if (this.componentFonts[i].encoder.canEncode(c)) {
/*  90 */           this.defaultFont = this.componentFonts[i];
/*  91 */           this.defaultChar = c;
/*  92 */           break;
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   protected abstract char getMissingGlyphCharacter();
/*     */ 
/*     */   public CharsetString[] makeMultiCharsetString(String paramString)
/*     */   {
/* 107 */     return makeMultiCharsetString(paramString.toCharArray(), 0, paramString.length(), true);
/*     */   }
/*     */ 
/*     */   public CharsetString[] makeMultiCharsetString(String paramString, boolean paramBoolean)
/*     */   {
/* 114 */     return makeMultiCharsetString(paramString.toCharArray(), 0, paramString.length(), paramBoolean);
/*     */   }
/*     */ 
/*     */   public CharsetString[] makeMultiCharsetString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 124 */     return makeMultiCharsetString(paramArrayOfChar, paramInt1, paramInt2, true);
/*     */   }
/*     */ 
/*     */   public CharsetString[] makeMultiCharsetString(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 143 */     if (paramInt2 < 1) {
/* 144 */       return new CharsetString[0];
/*     */     }
/* 146 */     Vector localVector = null;
/* 147 */     char[] arrayOfChar = new char[paramInt2];
/* 148 */     int i = this.defaultChar;
/* 149 */     int j = 0;
/*     */ 
/* 151 */     Object localObject = this.defaultFont;
/*     */ 
/* 154 */     for (int k = 0; k < this.componentFonts.length; k++)
/*     */     {
/*     */       int m;
/* 155 */       if (!this.componentFonts[k].isExcluded(paramArrayOfChar[paramInt1]))
/*     */       {
/* 163 */         if (this.componentFonts[k].encoder.canEncode(paramArrayOfChar[paramInt1])) {
/* 164 */           localObject = this.componentFonts[k];
/* 165 */           i = paramArrayOfChar[paramInt1];
/* 166 */           j = 1;
/* 167 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 170 */     if ((!paramBoolean) && (j == 0)) {
/* 171 */       return null;
/*     */     }
/* 173 */     arrayOfChar[0] = i;
/*     */ 
/* 176 */     k = 0;
/* 177 */     for (m = 1; m < paramInt2; m++) {
/* 178 */       char c = paramArrayOfChar[(paramInt1 + m)];
/* 179 */       FontDescriptor localFontDescriptor = this.defaultFont;
/* 180 */       i = this.defaultChar;
/* 181 */       j = 0;
/* 182 */       for (int i1 = 0; i1 < this.componentFonts.length; i1++)
/*     */       {
/*     */         CharsetString localCharsetString;
/*     */         CharsetString[] arrayOfCharsetString;
/*     */         int n;
/* 183 */         if (!this.componentFonts[i1].isExcluded(c))
/*     */         {
/* 187 */           if (this.componentFonts[i1].encoder.canEncode(c)) {
/* 188 */             localFontDescriptor = this.componentFonts[i1];
/* 189 */             i = c;
/* 190 */             j = 1;
/* 191 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 194 */       if ((!paramBoolean) && (j == 0)) {
/* 195 */         return null;
/*     */       }
/* 197 */       arrayOfChar[m] = i;
/*     */ 
/* 199 */       if (localObject != localFontDescriptor) {
/* 200 */         if (localVector == null) {
/* 201 */           localVector = new Vector(3);
/*     */         }
/* 203 */         localVector.addElement(new CharsetString(arrayOfChar, k, m - k, (FontDescriptor)localObject));
/*     */ 
/* 205 */         localObject = localFontDescriptor;
/* 206 */         localFontDescriptor = this.defaultFont;
/* 207 */         k = m;
/*     */       }
/*     */     }
/*     */ 
/* 211 */     localCharsetString = new CharsetString(arrayOfChar, k, paramInt2 - k, (FontDescriptor)localObject);
/*     */ 
/* 213 */     if (localVector == null) {
/* 214 */       arrayOfCharsetString = new CharsetString[1];
/* 215 */       arrayOfCharsetString[0] = localCharsetString;
/*     */     } else {
/* 217 */       localVector.addElement(localCharsetString);
/* 218 */       arrayOfCharsetString = new CharsetString[localVector.size()];
/* 219 */       for (n = 0; n < localVector.size(); n++) {
/* 220 */         arrayOfCharsetString[n] = ((CharsetString)localVector.elementAt(n));
/*     */       }
/*     */     }
/* 223 */     return arrayOfCharsetString;
/*     */   }
/*     */ 
/*     */   public boolean mightHaveMultiFontMetrics()
/*     */   {
/* 231 */     return this.fontConfig != null;
/*     */   }
/*     */ 
/*     */   public Object[] makeConvertedMultiFontString(String paramString)
/*     */   {
/* 239 */     return makeConvertedMultiFontChars(paramString.toCharArray(), 0, paramString.length());
/*     */   }
/*     */ 
/*     */   public Object[] makeConvertedMultiFontChars(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 245 */     Object localObject1 = new Object[2];
/*     */ 
/* 247 */     byte[] arrayOfByte = null;
/* 248 */     int i = paramInt1;
/* 249 */     int j = 0;
/* 250 */     int k = 0;
/*     */ 
/* 252 */     Object localObject2 = null;
/* 253 */     FontDescriptor localFontDescriptor1 = null;
/*     */ 
/* 258 */     int i1 = paramInt1 + paramInt2;
/* 259 */     if ((paramInt1 < 0) || (i1 > paramArrayOfChar.length)) {
/* 260 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*     */ 
/* 263 */     if (i >= i1) {
/* 264 */       return null;
/*     */     }
/*     */ 
/* 268 */     while (i < i1)
/*     */     {
/* 270 */       int n = paramArrayOfChar[i];
/*     */ 
/* 273 */       int m = n & FONTCACHEMASK;
/*     */ 
/* 275 */       PlatformFontCache localPlatformFontCache = (PlatformFontCache)getFontCache()[m];
/*     */ 
/* 278 */       if ((localPlatformFontCache == null) || (localPlatformFontCache.uniChar != n))
/*     */       {
/* 281 */         localObject2 = this.defaultFont;
/* 282 */         n = this.defaultChar;
/* 283 */         int i2 = paramArrayOfChar[i];
/* 284 */         i3 = this.componentFonts.length;
/*     */ 
/* 286 */         for (int i4 = 0; i4 < i3; i4++) {
/* 287 */           FontDescriptor localFontDescriptor2 = this.componentFonts[i4];
/*     */ 
/* 289 */           localFontDescriptor2.encoder.reset();
/*     */           char[] arrayOfChar;
/*     */           Object localObject3;
/* 292 */           if (!localFontDescriptor2.isExcluded(i2))
/*     */           {
/* 295 */             if (localFontDescriptor2.encoder.canEncode(i2)) {
/* 296 */               localObject2 = localFontDescriptor2;
/* 297 */               n = i2;
/* 298 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         try { arrayOfChar = new char[1];
/* 303 */           arrayOfChar[0] = n;
/*     */ 
/* 305 */           localPlatformFontCache = new PlatformFontCache();
/* 306 */           if (((FontDescriptor)localObject2).useUnicode())
/*     */           {
/* 312 */             if (FontDescriptor.isLE) {
/* 313 */               localPlatformFontCache.bb.put((byte)(arrayOfChar[0] & 0xFF));
/* 314 */               localPlatformFontCache.bb.put((byte)(arrayOfChar[0] >> '\b'));
/*     */             } else {
/* 316 */               localPlatformFontCache.bb.put((byte)(arrayOfChar[0] >> '\b'));
/* 317 */               localPlatformFontCache.bb.put((byte)(arrayOfChar[0] & 0xFF));
/*     */             }
/*     */           }
/*     */           else {
/* 321 */             ((FontDescriptor)localObject2).encoder.encode(CharBuffer.wrap(arrayOfChar), localPlatformFontCache.bb, true);
/*     */           }
/*     */ 
/* 325 */           localPlatformFontCache.fontDescriptor = ((FontDescriptor)localObject2);
/* 326 */           localPlatformFontCache.uniChar = paramArrayOfChar[i];
/* 327 */           getFontCache()[m] = localPlatformFontCache;
/*     */         } catch (Exception localException)
/*     */         {
/* 330 */           System.err.println(localException);
/* 331 */           localException.printStackTrace();
/* 332 */           return null;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 337 */       if (localFontDescriptor1 != localPlatformFontCache.fontDescriptor) {
/* 338 */         if (localFontDescriptor1 != null) {
/* 339 */           localObject1[(k++)] = localFontDescriptor1;
/* 340 */           localObject1[(k++)] = arrayOfByte;
/*     */ 
/* 342 */           if (arrayOfByte != null) {
/* 343 */             j -= 4;
/* 344 */             arrayOfByte[0] = ((byte)(j >> 24));
/* 345 */             arrayOfByte[1] = ((byte)(j >> 16));
/* 346 */             arrayOfByte[2] = ((byte)(j >> 8));
/* 347 */             arrayOfByte[3] = ((byte)j);
/*     */           }
/*     */ 
/* 350 */           if (k >= localObject1.length) {
/* 351 */             localObject3 = new Object[localObject1.length * 2];
/*     */ 
/* 353 */             System.arraycopy(localObject1, 0, localObject3, 0, localObject1.length);
/*     */ 
/* 355 */             localObject1 = localObject3;
/*     */           }
/*     */         }
/*     */ 
/* 359 */         if (localPlatformFontCache.fontDescriptor.useUnicode()) {
/* 360 */           arrayOfByte = new byte[(i1 - i + 1) * (int)localPlatformFontCache.fontDescriptor.unicodeEncoder.maxBytesPerChar() + 4];
/*     */         }
/*     */         else
/*     */         {
/* 365 */           arrayOfByte = new byte[(i1 - i + 1) * (int)localPlatformFontCache.fontDescriptor.encoder.maxBytesPerChar() + 4];
/*     */         }
/*     */ 
/* 370 */         j = 4;
/*     */ 
/* 372 */         localFontDescriptor1 = localPlatformFontCache.fontDescriptor;
/*     */       }
/*     */ 
/* 375 */       localObject3 = localPlatformFontCache.bb.array();
/* 376 */       int i3 = localPlatformFontCache.bb.position();
/* 377 */       if (i3 == 1) {
/* 378 */         arrayOfByte[(j++)] = localObject3[0];
/*     */       }
/* 380 */       else if (i3 == 2) {
/* 381 */         arrayOfByte[(j++)] = localObject3[0];
/* 382 */         arrayOfByte[(j++)] = localObject3[1];
/* 383 */       } else if (i3 == 3) {
/* 384 */         arrayOfByte[(j++)] = localObject3[0];
/* 385 */         arrayOfByte[(j++)] = localObject3[1];
/* 386 */         arrayOfByte[(j++)] = localObject3[2];
/* 387 */       } else if (i3 == 4) {
/* 388 */         arrayOfByte[(j++)] = localObject3[0];
/* 389 */         arrayOfByte[(j++)] = localObject3[1];
/* 390 */         arrayOfByte[(j++)] = localObject3[2];
/* 391 */         arrayOfByte[(j++)] = localObject3[3];
/*     */       }
/* 393 */       i++;
/*     */     }
/*     */ 
/* 396 */     localObject1[(k++)] = localFontDescriptor1;
/* 397 */     localObject1[k] = arrayOfByte;
/*     */ 
/* 400 */     if (arrayOfByte != null) {
/* 401 */       j -= 4;
/* 402 */       arrayOfByte[0] = ((byte)(j >> 24));
/* 403 */       arrayOfByte[1] = ((byte)(j >> 16));
/* 404 */       arrayOfByte[2] = ((byte)(j >> 8));
/* 405 */       arrayOfByte[3] = ((byte)j);
/*     */     }
/* 407 */     return localObject1;
/*     */   }
/*     */ 
/*     */   protected final Object[] getFontCache()
/*     */   {
/* 422 */     if (this.fontCache == null) {
/* 423 */       this.fontCache = new Object[FONTCACHESIZE];
/*     */     }
/*     */ 
/* 426 */     return this.fontCache;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/*  39 */     NativeLibLoader.loadLibraries();
/*  40 */     initIDs();
/*     */   }
/*     */ 
/*     */   class PlatformFontCache
/*     */   {
/*     */     char uniChar;
/*     */     FontDescriptor fontDescriptor;
/* 438 */     ByteBuffer bb = ByteBuffer.allocate(4);
/*     */ 
/*     */     PlatformFontCache()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.PlatformFont
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.FontFormatException;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Point2D.Float;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.Reference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Vector;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import sun.java2d.Disposer;
/*     */ import sun.java2d.DisposerRecord;
/*     */ 
/*     */ public abstract class FileFont extends PhysicalFont
/*     */ {
/*  45 */   protected boolean useJavaRasterizer = true;
/*     */   protected int fileSize;
/*     */   protected FontScaler scaler;
/*     */   protected boolean checkedNatives;
/*     */   protected boolean useNatives;
/*     */   protected NativeFont[] nativeFonts;
/*     */   protected char[] glyphToCharMap;
/*     */ 
/*     */   FileFont(String paramString, Object paramObject)
/*     */     throws FontFormatException
/*     */   {
/*  88 */     super(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   FontStrike createStrike(FontStrikeDesc paramFontStrikeDesc) {
/*  92 */     if (!this.checkedNatives) {
/*  93 */       checkUseNatives();
/*     */     }
/*  95 */     return new FileFontStrike(this, paramFontStrikeDesc);
/*     */   }
/*     */ 
/*     */   protected boolean checkUseNatives() {
/*  99 */     this.checkedNatives = true;
/* 100 */     return this.useNatives;
/*     */   }
/*     */ 
/*     */   protected abstract void close();
/*     */ 
/*     */   abstract ByteBuffer readBlock(int paramInt1, int paramInt2);
/*     */ 
/*     */   public boolean canDoStyle(int paramInt)
/*     */   {
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   void setFileToRemove(File paramFile, CreatedFontTracker paramCreatedFontTracker) {
/* 120 */     Disposer.addObjectRecord(this, new CreatedFontFileDisposerRecord(paramFile, paramCreatedFontTracker, null));
/*     */   }
/*     */ 
/*     */   static void setFileToRemove(Object paramObject, File paramFile, CreatedFontTracker paramCreatedFontTracker)
/*     */   {
/* 126 */     Disposer.addObjectRecord(paramObject, new CreatedFontFileDisposerRecord(paramFile, paramCreatedFontTracker, null));
/*     */   }
/*     */ 
/*     */   synchronized void deregisterFontAndClearStrikeCache()
/*     */   {
/* 159 */     SunFontManager localSunFontManager = SunFontManager.getInstance();
/* 160 */     localSunFontManager.deRegisterBadFont(this);
/*     */ 
/* 162 */     for (Reference localReference : this.strikeCache.values()) {
/* 163 */       if (localReference != null)
/*     */       {
/* 167 */         FileFontStrike localFileFontStrike = (FileFontStrike)localReference.get();
/* 168 */         if ((localFileFontStrike != null) && (localFileFontStrike.pScalerContext != 0L)) {
/* 169 */           this.scaler.invalidateScalerContext(localFileFontStrike.pScalerContext);
/*     */         }
/*     */       }
/*     */     }
/* 173 */     if (this.scaler != null) {
/* 174 */       this.scaler.dispose();
/*     */     }
/* 176 */     this.scaler = FontScaler.getNullScaler();
/*     */   }
/*     */ 
/*     */   StrikeMetrics getFontMetrics(long paramLong) {
/*     */     try {
/* 181 */       return getScaler().getFontMetrics(paramLong);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 183 */       this.scaler = FontScaler.getNullScaler();
/* 184 */     }return getFontMetrics(paramLong);
/*     */   }
/*     */ 
/*     */   float getGlyphAdvance(long paramLong, int paramInt)
/*     */   {
/*     */     try {
/* 190 */       return getScaler().getGlyphAdvance(paramLong, paramInt);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 192 */       this.scaler = FontScaler.getNullScaler();
/* 193 */     }return getGlyphAdvance(paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   void getGlyphMetrics(long paramLong, int paramInt, Point2D.Float paramFloat)
/*     */   {
/*     */     try {
/* 199 */       getScaler().getGlyphMetrics(paramLong, paramInt, paramFloat);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 201 */       this.scaler = FontScaler.getNullScaler();
/* 202 */       getGlyphMetrics(paramLong, paramInt, paramFloat);
/*     */     }
/*     */   }
/*     */ 
/*     */   long getGlyphImage(long paramLong, int paramInt) {
/*     */     try {
/* 208 */       return getScaler().getGlyphImage(paramLong, paramInt);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 210 */       this.scaler = FontScaler.getNullScaler();
/* 211 */     }return getGlyphImage(paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   Rectangle2D.Float getGlyphOutlineBounds(long paramLong, int paramInt)
/*     */   {
/*     */     try {
/* 217 */       return getScaler().getGlyphOutlineBounds(paramLong, paramInt);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 219 */       this.scaler = FontScaler.getNullScaler();
/* 220 */     }return getGlyphOutlineBounds(paramLong, paramInt);
/*     */   }
/*     */ 
/*     */   GeneralPath getGlyphOutline(long paramLong, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/*     */     try {
/* 226 */       return getScaler().getGlyphOutline(paramLong, paramInt, paramFloat1, paramFloat2);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 228 */       this.scaler = FontScaler.getNullScaler();
/* 229 */     }return getGlyphOutline(paramLong, paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   GeneralPath getGlyphVectorOutline(long paramLong, int[] paramArrayOfInt, int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/*     */     try {
/* 235 */       return getScaler().getGlyphVectorOutline(paramLong, paramArrayOfInt, paramInt, paramFloat1, paramFloat2);
/*     */     } catch (FontScalerException localFontScalerException) {
/* 237 */       this.scaler = FontScaler.getNullScaler();
/* 238 */     }return getGlyphVectorOutline(paramLong, paramArrayOfInt, paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   protected abstract FontScaler getScaler();
/*     */ 
/*     */   protected long getUnitsPerEm()
/*     */   {
/* 247 */     return getScaler().getUnitsPerEm();
/*     */   }
/*     */ 
/*     */   protected String getPublicFileName()
/*     */   {
/* 291 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 292 */     if (localSecurityManager == null) {
/* 293 */       return this.platName;
/*     */     }
/* 295 */     int i = 1;
/*     */     try
/*     */     {
/* 298 */       localSecurityManager.checkPropertyAccess("java.io.tmpdir");
/*     */     } catch (SecurityException localSecurityException) {
/* 300 */       i = 0;
/*     */     }
/*     */ 
/* 303 */     if (i != 0) {
/* 304 */       return this.platName;
/*     */     }
/*     */ 
/* 307 */     final File localFile = new File(this.platName);
/*     */ 
/* 309 */     Boolean localBoolean = Boolean.FALSE;
/*     */     try {
/* 311 */       localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Boolean run() {
/* 314 */           File localFile = new File(System.getProperty("java.io.tmpdir"));
/*     */           try {
/* 316 */             String str1 = localFile.getCanonicalPath();
/* 317 */             String str2 = localFile.getCanonicalPath();
/*     */ 
/* 319 */             return Boolean.valueOf((str2 == null) || (str2.startsWith(str1))); } catch (IOException localIOException) {
/*     */           }
/* 321 */           return Boolean.TRUE;
/*     */         }
/*     */ 
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 329 */       localBoolean = Boolean.TRUE;
/*     */     }
/*     */ 
/* 332 */     return localBoolean.booleanValue() ? "temp file" : this.platName;
/*     */   }
/*     */ 
/*     */   private static class CreatedFontFileDisposerRecord
/*     */     implements DisposerRecord
/*     */   {
/* 253 */     File fontFile = null;
/*     */     CreatedFontTracker tracker;
/*     */ 
/*     */     private CreatedFontFileDisposerRecord(File paramFile, CreatedFontTracker paramCreatedFontTracker)
/*     */     {
/* 258 */       this.fontFile = paramFile;
/* 259 */       this.tracker = paramCreatedFontTracker;
/*     */     }
/*     */ 
/*     */     public void dispose() {
/* 263 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/* 266 */           if (FileFont.CreatedFontFileDisposerRecord.this.fontFile != null)
/*     */             try {
/* 268 */               if (FileFont.CreatedFontFileDisposerRecord.this.tracker != null) {
/* 269 */                 FileFont.CreatedFontFileDisposerRecord.this.tracker.subBytes((int)FileFont.CreatedFontFileDisposerRecord.this.fontFile.length());
/*     */               }
/*     */ 
/* 277 */               FileFont.CreatedFontFileDisposerRecord.this.fontFile.delete();
/*     */ 
/* 280 */               SunFontManager.getInstance().tmpFontFiles.remove(FileFont.CreatedFontFileDisposerRecord.this.fontFile);
/*     */             }
/*     */             catch (Exception localException) {
/*     */             }
/* 284 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.FileFont
 * JD-Core Version:    0.6.2
 */
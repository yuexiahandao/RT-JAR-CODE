/*     */ package javax.imageio.spi;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import javax.imageio.metadata.IIOMetadataFormat;
/*     */ import javax.imageio.metadata.IIOMetadataFormatImpl;
/*     */ 
/*     */ public abstract class ImageReaderWriterSpi extends IIOServiceProvider
/*     */ {
/*  55 */   protected String[] names = null;
/*     */ 
/*  61 */   protected String[] suffixes = null;
/*     */ 
/*  67 */   protected String[] MIMETypes = null;
/*     */ 
/*  73 */   protected String pluginClassName = null;
/*     */ 
/*  80 */   protected boolean supportsStandardStreamMetadataFormat = false;
/*     */ 
/*  87 */   protected String nativeStreamMetadataFormatName = null;
/*     */ 
/*  94 */   protected String nativeStreamMetadataFormatClassName = null;
/*     */ 
/* 101 */   protected String[] extraStreamMetadataFormatNames = null;
/*     */ 
/* 108 */   protected String[] extraStreamMetadataFormatClassNames = null;
/*     */ 
/* 115 */   protected boolean supportsStandardImageMetadataFormat = false;
/*     */ 
/* 122 */   protected String nativeImageMetadataFormatName = null;
/*     */ 
/* 129 */   protected String nativeImageMetadataFormatClassName = null;
/*     */ 
/* 136 */   protected String[] extraImageMetadataFormatNames = null;
/*     */ 
/* 143 */   protected String[] extraImageMetadataFormatClassNames = null;
/*     */ 
/*     */   public ImageReaderWriterSpi(String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String paramString3, boolean paramBoolean1, String paramString4, String paramString5, String[] paramArrayOfString4, String[] paramArrayOfString5, boolean paramBoolean2, String paramString6, String paramString7, String[] paramArrayOfString6, String[] paramArrayOfString7)
/*     */   {
/* 231 */     super(paramString1, paramString2);
/* 232 */     if (paramArrayOfString1 == null) {
/* 233 */       throw new IllegalArgumentException("names == null!");
/*     */     }
/* 235 */     if (paramArrayOfString1.length == 0) {
/* 236 */       throw new IllegalArgumentException("names.length == 0!");
/*     */     }
/* 238 */     if (paramString3 == null) {
/* 239 */       throw new IllegalArgumentException("pluginClassName == null!");
/*     */     }
/*     */ 
/* 242 */     this.names = ((String[])paramArrayOfString1.clone());
/*     */ 
/* 244 */     if ((paramArrayOfString2 != null) && (paramArrayOfString2.length > 0)) {
/* 245 */       this.suffixes = ((String[])paramArrayOfString2.clone());
/*     */     }
/*     */ 
/* 248 */     if ((paramArrayOfString3 != null) && (paramArrayOfString3.length > 0)) {
/* 249 */       this.MIMETypes = ((String[])paramArrayOfString3.clone());
/*     */     }
/* 251 */     this.pluginClassName = paramString3;
/*     */ 
/* 253 */     this.supportsStandardStreamMetadataFormat = paramBoolean1;
/*     */ 
/* 255 */     this.nativeStreamMetadataFormatName = paramString4;
/* 256 */     this.nativeStreamMetadataFormatClassName = paramString5;
/*     */ 
/* 259 */     if ((paramArrayOfString4 != null) && (paramArrayOfString4.length > 0))
/*     */     {
/* 261 */       this.extraStreamMetadataFormatNames = ((String[])paramArrayOfString4.clone());
/*     */     }
/*     */ 
/* 265 */     if ((paramArrayOfString5 != null) && (paramArrayOfString5.length > 0))
/*     */     {
/* 267 */       this.extraStreamMetadataFormatClassNames = ((String[])paramArrayOfString5.clone());
/*     */     }
/*     */ 
/* 270 */     this.supportsStandardImageMetadataFormat = paramBoolean2;
/*     */ 
/* 272 */     this.nativeImageMetadataFormatName = paramString6;
/* 273 */     this.nativeImageMetadataFormatClassName = paramString7;
/*     */ 
/* 276 */     if ((paramArrayOfString6 != null) && (paramArrayOfString6.length > 0))
/*     */     {
/* 278 */       this.extraImageMetadataFormatNames = ((String[])paramArrayOfString6.clone());
/*     */     }
/*     */ 
/* 282 */     if ((paramArrayOfString7 != null) && (paramArrayOfString7.length > 0))
/*     */     {
/* 284 */       this.extraImageMetadataFormatClassNames = ((String[])paramArrayOfString7.clone());
/*     */     }
/*     */   }
/*     */ 
/*     */   public ImageReaderWriterSpi()
/*     */   {
/*     */   }
/*     */ 
/*     */   public String[] getFormatNames()
/*     */   {
/* 311 */     return (String[])this.names.clone();
/*     */   }
/*     */ 
/*     */   public String[] getFileSuffixes()
/*     */   {
/* 335 */     return this.suffixes == null ? null : (String[])this.suffixes.clone();
/*     */   }
/*     */ 
/*     */   public String[] getMIMETypes()
/*     */   {
/* 370 */     return this.MIMETypes == null ? null : (String[])this.MIMETypes.clone();
/*     */   }
/*     */ 
/*     */   public String getPluginClassName()
/*     */   {
/* 382 */     return this.pluginClassName;
/*     */   }
/*     */ 
/*     */   public boolean isStandardStreamMetadataFormatSupported()
/*     */   {
/* 396 */     return this.supportsStandardStreamMetadataFormat;
/*     */   }
/*     */ 
/*     */   public String getNativeStreamMetadataFormatName()
/*     */   {
/* 415 */     return this.nativeStreamMetadataFormatName;
/*     */   }
/*     */ 
/*     */   public String[] getExtraStreamMetadataFormatNames()
/*     */   {
/* 445 */     return this.extraStreamMetadataFormatNames == null ? null : (String[])this.extraStreamMetadataFormatNames.clone();
/*     */   }
/*     */ 
/*     */   public boolean isStandardImageMetadataFormatSupported()
/*     */   {
/* 460 */     return this.supportsStandardImageMetadataFormat;
/*     */   }
/*     */ 
/*     */   public String getNativeImageMetadataFormatName()
/*     */   {
/* 480 */     return this.nativeImageMetadataFormatName;
/*     */   }
/*     */ 
/*     */   public String[] getExtraImageMetadataFormatNames()
/*     */   {
/* 509 */     return this.extraImageMetadataFormatNames == null ? null : (String[])this.extraImageMetadataFormatNames.clone();
/*     */   }
/*     */ 
/*     */   public IIOMetadataFormat getStreamMetadataFormat(String paramString)
/*     */   {
/* 529 */     return getMetadataFormat(paramString, this.supportsStandardStreamMetadataFormat, this.nativeStreamMetadataFormatName, this.nativeStreamMetadataFormatClassName, this.extraStreamMetadataFormatNames, this.extraStreamMetadataFormatClassNames);
/*     */   }
/*     */ 
/*     */   public IIOMetadataFormat getImageMetadataFormat(String paramString)
/*     */   {
/* 553 */     return getMetadataFormat(paramString, this.supportsStandardImageMetadataFormat, this.nativeImageMetadataFormatName, this.nativeImageMetadataFormatClassName, this.extraImageMetadataFormatNames, this.extraImageMetadataFormatClassNames);
/*     */   }
/*     */ 
/*     */   private IIOMetadataFormat getMetadataFormat(String paramString1, boolean paramBoolean, String paramString2, String paramString3, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 567 */     if (paramString1 == null) {
/* 568 */       throw new IllegalArgumentException("formatName == null!");
/*     */     }
/* 570 */     if ((paramBoolean) && (paramString1.equals("javax_imageio_1.0")))
/*     */     {
/* 573 */       return IIOMetadataFormatImpl.getStandardFormatInstance();
/*     */     }
/* 575 */     String str = null;
/* 576 */     if (paramString1.equals(paramString2))
/* 577 */       str = paramString3;
/* 578 */     else if (paramArrayOfString1 != null) {
/* 579 */       for (int i = 0; i < paramArrayOfString1.length; i++) {
/* 580 */         if (paramString1.equals(paramArrayOfString1[i])) {
/* 581 */           str = paramArrayOfString2[i];
/* 582 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 586 */     if (str == null)
/* 587 */       throw new IllegalArgumentException("Unsupported format name");
/*     */     Object localObject;
/*     */     try {
/* 590 */       Class localClass = Class.forName(str, true, ClassLoader.getSystemClassLoader());
/*     */ 
/* 592 */       localObject = localClass.getMethod("getInstance", new Class[0]);
/* 593 */       return (IIOMetadataFormat)((Method)localObject).invoke(null, new Object[0]);
/*     */     } catch (Exception localException) {
/* 595 */       localObject = new IllegalStateException("Can't obtain format");
/*     */ 
/* 597 */       ((RuntimeException)localObject).initCause(localException);
/* 598 */     }throw ((Throwable)localObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ImageReaderWriterSpi
 * JD-Core Version:    0.6.2
 */
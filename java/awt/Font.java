/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.font.LineMetrics;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.awt.font.TextLayout;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Point2D.Float;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.awt.peer.FontPeer;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilePermission;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.text.AttributedCharacterIterator.Attribute;
/*      */ import java.text.CharacterIterator;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import sun.font.AttributeMap;
/*      */ import sun.font.AttributeValues;
/*      */ import sun.font.CompositeFont;
/*      */ import sun.font.CoreMetrics;
/*      */ import sun.font.CreatedFontTracker;
/*      */ import sun.font.EAttribute;
/*      */ import sun.font.Font2D;
/*      */ import sun.font.Font2DHandle;
/*      */ import sun.font.FontAccess;
/*      */ import sun.font.FontLineMetrics;
/*      */ import sun.font.FontManager;
/*      */ import sun.font.FontManagerFactory;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.font.GlyphLayout;
/*      */ import sun.font.StandardGlyphVector;
/*      */ 
/*      */ public class Font
/*      */   implements Serializable
/*      */ {
/*      */   private Hashtable fRequestedAttributes;
/*      */   public static final String DIALOG = "Dialog";
/*      */   public static final String DIALOG_INPUT = "DialogInput";
/*      */   public static final String SANS_SERIF = "SansSerif";
/*      */   public static final String SERIF = "Serif";
/*      */   public static final String MONOSPACED = "Monospaced";
/*      */   public static final int PLAIN = 0;
/*      */   public static final int BOLD = 1;
/*      */   public static final int ITALIC = 2;
/*      */   public static final int ROMAN_BASELINE = 0;
/*      */   public static final int CENTER_BASELINE = 1;
/*      */   public static final int HANGING_BASELINE = 2;
/*      */   public static final int TRUETYPE_FONT = 0;
/*      */   public static final int TYPE1_FONT = 1;
/*      */   protected String name;
/*      */   protected int style;
/*      */   protected int size;
/*      */   protected float pointSize;
/*      */   private transient FontPeer peer;
/*      */   private transient long pData;
/*      */   private transient Font2DHandle font2DHandle;
/*      */   private transient AttributeValues values;
/*      */   private transient boolean hasLayoutAttributes;
/*  416 */   private transient boolean createdFont = false;
/*      */   private transient boolean nonIdentityTx;
/*  429 */   private static final AffineTransform identityTx = new AffineTransform();
/*      */   private static final long serialVersionUID = -4206021311591459213L;
/*  720 */   private static final int RECOGNIZED_MASK = AttributeValues.MASK_ALL & (AttributeValues.getMask(EAttribute.EFONT) ^ 0xFFFFFFFF);
/*      */ 
/*  726 */   private static final int PRIMARY_MASK = AttributeValues.getMask(new EAttribute[] { EAttribute.EFAMILY, EAttribute.EWEIGHT, EAttribute.EWIDTH, EAttribute.EPOSTURE, EAttribute.ESIZE, EAttribute.ETRANSFORM, EAttribute.ESUPERSCRIPT, EAttribute.ETRACKING });
/*      */ 
/*  733 */   private static final int SECONDARY_MASK = RECOGNIZED_MASK & (PRIMARY_MASK ^ 0xFFFFFFFF);
/*      */ 
/*  739 */   private static final int LAYOUT_MASK = AttributeValues.getMask(new EAttribute[] { EAttribute.ECHAR_REPLACEMENT, EAttribute.EFOREGROUND, EAttribute.EBACKGROUND, EAttribute.EUNDERLINE, EAttribute.ESTRIKETHROUGH, EAttribute.ERUN_DIRECTION, EAttribute.EBIDI_EMBEDDING, EAttribute.EJUSTIFICATION, EAttribute.EINPUT_METHOD_HIGHLIGHT, EAttribute.EINPUT_METHOD_UNDERLINE, EAttribute.ESWAP_COLORS, EAttribute.ENUMERIC_SHAPING, EAttribute.EKERNING, EAttribute.ELIGATURES, EAttribute.ETRACKING, EAttribute.ESUPERSCRIPT });
/*      */ 
/*  747 */   private static final int EXTRA_MASK = AttributeValues.getMask(new EAttribute[] { EAttribute.ETRANSFORM, EAttribute.ESUPERSCRIPT, EAttribute.EWIDTH });
/*      */ 
/* 1154 */   private static final float[] ssinfo = { 0.0F, 0.375F, 0.625F, 0.7916667F, 0.9027778F, 0.9768519F, 1.026235F, 1.059156F };
/*      */   transient int hash;
/* 1701 */   private int fontSerializedDataVersion = 1;
/*      */   private transient SoftReference flmref;
/*      */   public static final int LAYOUT_LEFT_TO_RIGHT = 0;
/*      */   public static final int LAYOUT_RIGHT_TO_LEFT = 1;
/*      */   public static final int LAYOUT_NO_START_CONTEXT = 2;
/*      */   public static final int LAYOUT_NO_LIMIT_CONTEXT = 4;
/*      */ 
/*      */   @Deprecated
/*      */   public FontPeer getPeer()
/*      */   {
/*  444 */     return getPeer_NoClientCode();
/*      */   }
/*      */ 
/*      */   final FontPeer getPeer_NoClientCode()
/*      */   {
/*  451 */     if (this.peer == null) {
/*  452 */       Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  453 */       this.peer = localToolkit.getFontPeer(this.name, this.style);
/*      */     }
/*  455 */     return this.peer;
/*      */   }
/*      */ 
/*      */   private AttributeValues getAttributeValues()
/*      */   {
/*  470 */     if (this.values == null) {
/*  471 */       AttributeValues localAttributeValues = new AttributeValues();
/*  472 */       localAttributeValues.setFamily(this.name);
/*  473 */       localAttributeValues.setSize(this.pointSize);
/*      */ 
/*  475 */       if ((this.style & 0x1) != 0) {
/*  476 */         localAttributeValues.setWeight(2.0F);
/*      */       }
/*      */ 
/*  479 */       if ((this.style & 0x2) != 0) {
/*  480 */         localAttributeValues.setPosture(0.2F);
/*      */       }
/*  482 */       localAttributeValues.defineAll(PRIMARY_MASK);
/*  483 */       this.values = localAttributeValues;
/*      */     }
/*      */ 
/*  486 */     return this.values;
/*      */   }
/*      */ 
/*      */   private Font2D getFont2D() {
/*  490 */     FontManager localFontManager = FontManagerFactory.getInstance();
/*  491 */     if ((localFontManager.usingPerAppContextComposites()) && (this.font2DHandle != null) && ((this.font2DHandle.font2D instanceof CompositeFont)) && (((CompositeFont)this.font2DHandle.font2D).isStdComposite()))
/*      */     {
/*  495 */       return localFontManager.findFont2D(this.name, this.style, 2);
/*      */     }
/*  497 */     if (this.font2DHandle == null) {
/*  498 */       this.font2DHandle = localFontManager.findFont2D(this.name, this.style, 2).handle;
/*      */     }
/*      */ 
/*  506 */     return this.font2DHandle.font2D;
/*      */   }
/*      */ 
/*      */   public Font(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  567 */     this.name = (paramString != null ? paramString : "Default");
/*  568 */     this.style = ((paramInt1 & 0xFFFFFFFC) == 0 ? paramInt1 : 0);
/*  569 */     this.size = paramInt2;
/*  570 */     this.pointSize = paramInt2;
/*      */   }
/*      */ 
/*      */   private Font(String paramString, int paramInt, float paramFloat) {
/*  574 */     this.name = (paramString != null ? paramString : "Default");
/*  575 */     this.style = ((paramInt & 0xFFFFFFFC) == 0 ? paramInt : 0);
/*  576 */     this.size = ((int)(paramFloat + 0.5D));
/*  577 */     this.pointSize = paramFloat;
/*      */   }
/*      */ 
/*      */   private Font(String paramString, int paramInt, float paramFloat, boolean paramBoolean, Font2DHandle paramFont2DHandle)
/*      */   {
/*  583 */     this(paramString, paramInt, paramFloat);
/*  584 */     this.createdFont = paramBoolean;
/*      */ 
/*  594 */     if (paramBoolean)
/*  595 */       if (((paramFont2DHandle.font2D instanceof CompositeFont)) && (paramFont2DHandle.font2D.getStyle() != paramInt))
/*      */       {
/*  597 */         FontManager localFontManager = FontManagerFactory.getInstance();
/*  598 */         this.font2DHandle = localFontManager.getNewComposite(null, paramInt, paramFont2DHandle);
/*      */       } else {
/*  600 */         this.font2DHandle = paramFont2DHandle;
/*      */       }
/*      */   }
/*      */ 
/*      */   private Font(File paramFile, int paramInt, boolean paramBoolean, CreatedFontTracker paramCreatedFontTracker)
/*      */     throws FontFormatException
/*      */   {
/*  609 */     this.createdFont = true;
/*      */ 
/*  613 */     FontManager localFontManager = FontManagerFactory.getInstance();
/*  614 */     this.font2DHandle = localFontManager.createFont2D(paramFile, paramInt, paramBoolean, paramCreatedFontTracker).handle;
/*      */ 
/*  616 */     this.name = this.font2DHandle.font2D.getFontName(Locale.getDefault());
/*  617 */     this.style = 0;
/*  618 */     this.size = 1;
/*  619 */     this.pointSize = 1.0F;
/*      */   }
/*      */ 
/*      */   private Font(AttributeValues paramAttributeValues, String paramString, int paramInt, boolean paramBoolean, Font2DHandle paramFont2DHandle)
/*      */   {
/*  648 */     this.createdFont = paramBoolean;
/*  649 */     if (paramBoolean) {
/*  650 */       this.font2DHandle = paramFont2DHandle;
/*      */ 
/*  652 */       String str = null;
/*  653 */       if (paramString != null) {
/*  654 */         str = paramAttributeValues.getFamily();
/*  655 */         if (paramString.equals(str)) str = null;
/*      */       }
/*  657 */       int i = 0;
/*  658 */       if (paramInt == -1) {
/*  659 */         i = -1;
/*      */       } else {
/*  661 */         if (paramAttributeValues.getWeight() >= 2.0F) i = 1;
/*  662 */         if (paramAttributeValues.getPosture() >= 0.2F) i |= 2;
/*  663 */         if (paramInt == i) i = -1;
/*      */       }
/*  665 */       if ((paramFont2DHandle.font2D instanceof CompositeFont)) {
/*  666 */         if ((i != -1) || (str != null)) {
/*  667 */           FontManager localFontManager = FontManagerFactory.getInstance();
/*  668 */           this.font2DHandle = localFontManager.getNewComposite(str, i, paramFont2DHandle);
/*      */         }
/*      */       }
/*  671 */       else if (str != null) {
/*  672 */         this.createdFont = false;
/*  673 */         this.font2DHandle = null;
/*      */       }
/*      */     }
/*  676 */     initFromValues(paramAttributeValues);
/*      */   }
/*      */ 
/*      */   public Font(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap)
/*      */   {
/*  694 */     initFromValues(AttributeValues.fromMap(paramMap, RECOGNIZED_MASK));
/*      */   }
/*      */ 
/*      */   protected Font(Font paramFont)
/*      */   {
/*  705 */     if (paramFont.values != null) {
/*  706 */       initFromValues(paramFont.getAttributeValues().clone());
/*      */     } else {
/*  708 */       this.name = paramFont.name;
/*  709 */       this.style = paramFont.style;
/*  710 */       this.size = paramFont.size;
/*  711 */       this.pointSize = paramFont.pointSize;
/*      */     }
/*  713 */     this.font2DHandle = paramFont.font2DHandle;
/*  714 */     this.createdFont = paramFont.createdFont;
/*      */   }
/*      */ 
/*      */   private void initFromValues(AttributeValues paramAttributeValues)
/*      */   {
/*  754 */     this.values = paramAttributeValues;
/*  755 */     paramAttributeValues.defineAll(PRIMARY_MASK);
/*      */ 
/*  757 */     this.name = paramAttributeValues.getFamily();
/*  758 */     this.pointSize = paramAttributeValues.getSize();
/*  759 */     this.size = ((int)(paramAttributeValues.getSize() + 0.5D));
/*  760 */     if (paramAttributeValues.getWeight() >= 2.0F) this.style |= 1;
/*  761 */     if (paramAttributeValues.getPosture() >= 0.2F) this.style |= 2;
/*      */ 
/*  763 */     this.nonIdentityTx = paramAttributeValues.anyNonDefault(EXTRA_MASK);
/*  764 */     this.hasLayoutAttributes = paramAttributeValues.anyNonDefault(LAYOUT_MASK);
/*      */   }
/*      */ 
/*      */   public static Font getFont(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap)
/*      */   {
/*      */     Object localObject2;
/*  789 */     if (((paramMap instanceof AttributeMap)) && (((AttributeMap)paramMap).getValues() != null))
/*      */     {
/*  791 */       localObject1 = ((AttributeMap)paramMap).getValues();
/*  792 */       if (((AttributeValues)localObject1).isNonDefault(EAttribute.EFONT)) {
/*  793 */         localObject2 = ((AttributeValues)localObject1).getFont();
/*  794 */         if (!((AttributeValues)localObject1).anyDefined(SECONDARY_MASK)) {
/*  795 */           return localObject2;
/*      */         }
/*      */ 
/*  798 */         localObject1 = ((Font)localObject2).getAttributeValues().clone();
/*  799 */         ((AttributeValues)localObject1).merge(paramMap, SECONDARY_MASK);
/*  800 */         return new Font((AttributeValues)localObject1, ((Font)localObject2).name, ((Font)localObject2).style, ((Font)localObject2).createdFont, ((Font)localObject2).font2DHandle);
/*      */       }
/*      */ 
/*  803 */       return new Font(paramMap);
/*      */     }
/*      */ 
/*  806 */     Object localObject1 = (Font)paramMap.get(TextAttribute.FONT);
/*  807 */     if (localObject1 != null) {
/*  808 */       if (paramMap.size() > 1) {
/*  809 */         localObject2 = ((Font)localObject1).getAttributeValues().clone();
/*  810 */         ((AttributeValues)localObject2).merge(paramMap, SECONDARY_MASK);
/*  811 */         return new Font((AttributeValues)localObject2, ((Font)localObject1).name, ((Font)localObject1).style, ((Font)localObject1).createdFont, ((Font)localObject1).font2DHandle);
/*      */       }
/*      */ 
/*  815 */       return localObject1;
/*      */     }
/*      */ 
/*  818 */     return new Font(paramMap);
/*      */   }
/*      */ 
/*      */   private static boolean hasTempPermission()
/*      */   {
/*  828 */     if (System.getSecurityManager() == null) {
/*  829 */       return true;
/*      */     }
/*  831 */     File localFile = null;
/*  832 */     boolean bool = false;
/*      */     try {
/*  834 */       localFile = Files.createTempFile("+~JT", ".tmp", new FileAttribute[0]).toFile();
/*  835 */       localFile.delete();
/*  836 */       localFile = null;
/*  837 */       bool = true;
/*      */     }
/*      */     catch (Throwable localThrowable) {
/*      */     }
/*  841 */     return bool;
/*      */   }
/*      */ 
/*      */   public static Font createFont(int paramInt, InputStream paramInputStream)
/*      */     throws FontFormatException, IOException
/*      */   {
/*  875 */     if (hasTempPermission()) {
/*  876 */       return createFont0(paramInt, paramInputStream, null);
/*      */     }
/*      */ 
/*  881 */     CreatedFontTracker localCreatedFontTracker = CreatedFontTracker.getTracker();
/*  882 */     boolean bool = false;
/*      */     try {
/*  884 */       bool = localCreatedFontTracker.acquirePermit();
/*  885 */       if (!bool) {
/*  886 */         throw new IOException("Timed out waiting for resources.");
/*      */       }
/*  888 */       return createFont0(paramInt, paramInputStream, localCreatedFontTracker);
/*      */     } catch (InterruptedException localInterruptedException) {
/*  890 */       throw new IOException("Problem reading font data.");
/*      */     } finally {
/*  892 */       if (bool)
/*  893 */         localCreatedFontTracker.releasePermit();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Font createFont0(int paramInt, InputStream paramInputStream, CreatedFontTracker paramCreatedFontTracker)
/*      */     throws FontFormatException, IOException
/*      */   {
/*  902 */     if ((paramInt != 0) && (paramInt != 1))
/*      */     {
/*  904 */       throw new IllegalArgumentException("font format not recognized");
/*      */     }
/*  906 */     int i = 0;
/*      */     try {
/*  908 */       File localFile = (File)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public File run() throws IOException {
/*  911 */           return Files.createTempFile("+~JF", ".tmp", new FileAttribute[0]).toFile();
/*      */         }
/*      */       });
/*  915 */       if (paramCreatedFontTracker != null) {
/*  916 */         paramCreatedFontTracker.add(localFile);
/*      */       }
/*      */ 
/*  919 */       int j = 0;
/*      */       try {
/*  921 */         OutputStream localOutputStream = (OutputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public OutputStream run() throws IOException
/*      */           {
/*  925 */             return new FileOutputStream(this.val$tFile);
/*      */           }
/*      */         });
/*  929 */         if (paramCreatedFontTracker != null)
/*  930 */           paramCreatedFontTracker.set(localFile, localOutputStream);
/*      */         try
/*      */         {
/*  933 */           localObject1 = new byte[8192];
/*      */           while (true) {
/*  935 */             int k = paramInputStream.read((byte[])localObject1);
/*  936 */             if (k < 0) {
/*      */               break;
/*      */             }
/*  939 */             if (paramCreatedFontTracker != null) {
/*  940 */               if (j + k > 33554432) {
/*  941 */                 throw new IOException("File too big.");
/*      */               }
/*  943 */               if (j + paramCreatedFontTracker.getNumBytes() > 335544320)
/*      */               {
/*  946 */                 throw new IOException("Total files too big.");
/*      */               }
/*  948 */               j += k;
/*  949 */               paramCreatedFontTracker.addBytes(k);
/*      */             }
/*  951 */             localOutputStream.write((byte[])localObject1, 0, k);
/*      */           }
/*      */         }
/*      */         finally {
/*  955 */           localOutputStream.close();
/*      */         }
/*      */ 
/*  967 */         i = 1;
/*  968 */         Object localObject1 = new Font(localFile, paramInt, true, paramCreatedFontTracker);
/*  969 */         return localObject1;
/*      */       } finally {
/*  971 */         if (paramCreatedFontTracker != null) {
/*  972 */           paramCreatedFontTracker.remove(localFile);
/*      */         }
/*  974 */         if (i == 0) {
/*  975 */           if (paramCreatedFontTracker != null) {
/*  976 */             paramCreatedFontTracker.subBytes(j);
/*      */           }
/*  978 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Void run() {
/*  981 */               this.val$tFile.delete();
/*  982 */               return null;
/*      */             }
/*      */           });
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable1) {
/*  989 */       if ((localThrowable1 instanceof FontFormatException)) {
/*  990 */         throw ((FontFormatException)localThrowable1);
/*      */       }
/*  992 */       if ((localThrowable1 instanceof IOException)) {
/*  993 */         throw ((IOException)localThrowable1);
/*      */       }
/*  995 */       Throwable localThrowable2 = localThrowable1.getCause();
/*  996 */       if ((localThrowable2 instanceof FontFormatException))
/*  997 */         throw ((FontFormatException)localThrowable2);
/*      */     }
/*  999 */     throw new IOException("Problem reading font data.");
/*      */   }
/*      */ 
/*      */   public static Font createFont(int paramInt, File paramFile)
/*      */     throws FontFormatException, IOException
/*      */   {
/* 1040 */     paramFile = new File(paramFile.getPath());
/*      */ 
/* 1042 */     if ((paramInt != 0) && (paramInt != 1))
/*      */     {
/* 1044 */       throw new IllegalArgumentException("font format not recognized");
/*      */     }
/* 1046 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1047 */     if (localSecurityManager != null) {
/* 1048 */       FilePermission localFilePermission = new FilePermission(paramFile.getPath(), "read");
/*      */ 
/* 1050 */       localSecurityManager.checkPermission(localFilePermission);
/*      */     }
/* 1052 */     if (!paramFile.canRead()) {
/* 1053 */       throw new IOException("Can't read " + paramFile);
/*      */     }
/* 1055 */     return new Font(paramFile, paramInt, false, null);
/*      */   }
/*      */ 
/*      */   public AffineTransform getTransform()
/*      */   {
/* 1086 */     if (this.nonIdentityTx) {
/* 1087 */       AttributeValues localAttributeValues = getAttributeValues();
/*      */ 
/* 1089 */       AffineTransform localAffineTransform = localAttributeValues.isNonDefault(EAttribute.ETRANSFORM) ? new AffineTransform(localAttributeValues.getTransform()) : new AffineTransform();
/*      */ 
/* 1093 */       if (localAttributeValues.getSuperscript() != 0)
/*      */       {
/* 1098 */         int i = localAttributeValues.getSuperscript();
/*      */ 
/* 1100 */         double d1 = 0.0D;
/* 1101 */         int j = 0;
/* 1102 */         int k = i > 0 ? 1 : 0;
/* 1103 */         int m = k != 0 ? -1 : 1;
/* 1104 */         int n = k != 0 ? i : -i;
/*      */ 
/* 1106 */         while ((n & 0x7) > j) {
/* 1107 */           int i1 = n & 0x7;
/* 1108 */           d1 += m * (ssinfo[i1] - ssinfo[j]);
/* 1109 */           n >>= 3;
/* 1110 */           m = -m;
/* 1111 */           j = i1;
/*      */         }
/* 1113 */         d1 *= this.pointSize;
/* 1114 */         double d2 = Math.pow(0.6666666666666666D, j);
/*      */ 
/* 1116 */         localAffineTransform.preConcatenate(AffineTransform.getTranslateInstance(0.0D, d1));
/* 1117 */         localAffineTransform.scale(d2, d2);
/*      */       }
/*      */ 
/* 1133 */       if (localAttributeValues.isNonDefault(EAttribute.EWIDTH)) {
/* 1134 */         localAffineTransform.scale(localAttributeValues.getWidth(), 1.0D);
/*      */       }
/*      */ 
/* 1137 */       return localAffineTransform;
/*      */     }
/*      */ 
/* 1140 */     return new AffineTransform();
/*      */   }
/*      */ 
/*      */   public String getFamily()
/*      */   {
/* 1185 */     return getFamily_NoClientCode();
/*      */   }
/*      */ 
/*      */   final String getFamily_NoClientCode()
/*      */   {
/* 1193 */     return getFamily(Locale.getDefault());
/*      */   }
/*      */ 
/*      */   public String getFamily(Locale paramLocale)
/*      */   {
/* 1216 */     if (paramLocale == null) {
/* 1217 */       throw new NullPointerException("null locale doesn't mean default");
/*      */     }
/* 1219 */     return getFont2D().getFamilyName(paramLocale);
/*      */   }
/*      */ 
/*      */   public String getPSName()
/*      */   {
/* 1231 */     return getFont2D().getPostscriptName();
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/* 1245 */     return this.name;
/*      */   }
/*      */ 
/*      */   public String getFontName()
/*      */   {
/* 1260 */     return getFontName(Locale.getDefault());
/*      */   }
/*      */ 
/*      */   public String getFontName(Locale paramLocale)
/*      */   {
/* 1275 */     if (paramLocale == null) {
/* 1276 */       throw new NullPointerException("null locale doesn't mean default");
/*      */     }
/* 1278 */     return getFont2D().getFontName(paramLocale);
/*      */   }
/*      */ 
/*      */   public int getStyle()
/*      */   {
/* 1291 */     return this.style;
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/* 1317 */     return this.size;
/*      */   }
/*      */ 
/*      */   public float getSize2D()
/*      */   {
/* 1329 */     return this.pointSize;
/*      */   }
/*      */ 
/*      */   public boolean isPlain()
/*      */   {
/* 1342 */     return this.style == 0;
/*      */   }
/*      */ 
/*      */   public boolean isBold()
/*      */   {
/* 1355 */     return (this.style & 0x1) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isItalic()
/*      */   {
/* 1368 */     return (this.style & 0x2) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isTransformed()
/*      */   {
/* 1382 */     return this.nonIdentityTx;
/*      */   }
/*      */ 
/*      */   public boolean hasLayoutAttributes()
/*      */   {
/* 1392 */     return this.hasLayoutAttributes;
/*      */   }
/*      */ 
/*      */   public static Font getFont(String paramString)
/*      */   {
/* 1412 */     return getFont(paramString, null);
/*      */   }
/*      */ 
/*      */   public static Font decode(String paramString)
/*      */   {
/* 1489 */     String str1 = paramString;
/* 1490 */     String str2 = "";
/* 1491 */     int i = 12;
/* 1492 */     int j = 0;
/*      */ 
/* 1494 */     if (paramString == null) {
/* 1495 */       return new Font("Dialog", j, i);
/*      */     }
/*      */ 
/* 1498 */     int k = paramString.lastIndexOf('-');
/* 1499 */     int m = paramString.lastIndexOf(' ');
/* 1500 */     int n = k > m ? 45 : 32;
/* 1501 */     NumberFormatException localNumberFormatException1 = paramString.lastIndexOf(n);
/* 1502 */     NumberFormatException localNumberFormatException2 = paramString.lastIndexOf(n, localNumberFormatException1 - 1);
/* 1503 */     NumberFormatException localNumberFormatException3 = paramString.length();
/*      */ 
/* 1505 */     if ((localNumberFormatException1 > 0) && (localNumberFormatException1 + 1 < localNumberFormatException3)) {
/*      */       try {
/* 1507 */         i = Integer.valueOf(paramString.substring(localNumberFormatException1 + 1)).intValue();
/*      */ 
/* 1509 */         if (i <= 0) {
/* 1510 */           i = 12;
/*      */         }
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException4)
/*      */       {
/* 1515 */         localNumberFormatException2 = localNumberFormatException1;
/* 1516 */         localNumberFormatException1 = localNumberFormatException3;
/* 1517 */         if (paramString.charAt(localNumberFormatException1 - 1) == n) {
/* 1518 */           localNumberFormatException1--;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1523 */     if ((localNumberFormatException2 >= 0) && (localNumberFormatException2 + 1 < localNumberFormatException3)) {
/* 1524 */       str2 = paramString.substring(localNumberFormatException2 + 1, localNumberFormatException1);
/* 1525 */       str2 = str2.toLowerCase(Locale.ENGLISH);
/* 1526 */       if (str2.equals("bolditalic")) {
/* 1527 */         j = 3;
/* 1528 */       } else if (str2.equals("italic")) {
/* 1529 */         j = 2;
/* 1530 */       } else if (str2.equals("bold")) {
/* 1531 */         j = 1;
/* 1532 */       } else if (str2.equals("plain")) {
/* 1533 */         j = 0;
/*      */       }
/*      */       else
/*      */       {
/* 1538 */         localNumberFormatException2 = localNumberFormatException1;
/* 1539 */         if (paramString.charAt(localNumberFormatException2 - 1) == n) {
/* 1540 */           localNumberFormatException2--;
/*      */         }
/*      */       }
/* 1543 */       str1 = paramString.substring(0, localNumberFormatException2);
/*      */     }
/*      */     else {
/* 1546 */       localNumberFormatException4 = localNumberFormatException3;
/* 1547 */       if (localNumberFormatException2 > 0)
/* 1548 */         localNumberFormatException4 = localNumberFormatException2;
/* 1549 */       else if (localNumberFormatException1 > 0) {
/* 1550 */         localNumberFormatException4 = localNumberFormatException1;
/*      */       }
/* 1552 */       if ((localNumberFormatException4 > 0) && (paramString.charAt(localNumberFormatException4 - 1) == n)) {
/* 1553 */         localNumberFormatException4--;
/*      */       }
/* 1555 */       str1 = paramString.substring(0, localNumberFormatException4);
/*      */     }
/*      */ 
/* 1558 */     return new Font(str1, j, i);
/*      */   }
/*      */ 
/*      */   public static Font getFont(String paramString, Font paramFont)
/*      */   {
/* 1582 */     String str = null;
/*      */     try {
/* 1584 */       str = System.getProperty(paramString);
/*      */     } catch (SecurityException localSecurityException) {
/*      */     }
/* 1587 */     if (str == null) {
/* 1588 */       return paramFont;
/*      */     }
/* 1590 */     return decode(str);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1600 */     if (this.hash == 0) {
/* 1601 */       this.hash = (this.name.hashCode() ^ this.style ^ this.size);
/*      */ 
/* 1608 */       if ((this.nonIdentityTx) && (this.values != null) && (this.values.getTransform() != null))
/*      */       {
/* 1610 */         this.hash ^= this.values.getTransform().hashCode();
/*      */       }
/*      */     }
/* 1613 */     return this.hash;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1627 */     if (paramObject == this) {
/* 1628 */       return true;
/*      */     }
/*      */ 
/* 1631 */     if (paramObject != null) {
/*      */       try {
/* 1633 */         Font localFont = (Font)paramObject;
/* 1634 */         if ((this.size == localFont.size) && (this.style == localFont.style) && (this.nonIdentityTx == localFont.nonIdentityTx) && (this.hasLayoutAttributes == localFont.hasLayoutAttributes) && (this.pointSize == localFont.pointSize) && (this.name.equals(localFont.name)))
/*      */         {
/* 1647 */           if (this.values == null) {
/* 1648 */             if (localFont.values == null) {
/* 1649 */               return true;
/*      */             }
/* 1651 */             return getAttributeValues().equals(localFont.values);
/*      */           }
/*      */ 
/* 1654 */           return this.values.equals(localFont.getAttributeValues());
/*      */         }
/*      */       }
/*      */       catch (ClassCastException localClassCastException)
/*      */       {
/*      */       }
/*      */     }
/* 1661 */     return false;
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*      */     String str;
/* 1676 */     if (isBold())
/* 1677 */       str = isItalic() ? "bolditalic" : "bold";
/*      */     else {
/* 1679 */       str = isItalic() ? "italic" : "plain";
/*      */     }
/*      */ 
/* 1682 */     return getClass().getName() + "[family=" + getFamily() + ",name=" + this.name + ",style=" + str + ",size=" + this.size + "]";
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1714 */     if (this.values != null)
/* 1715 */       synchronized (this.values)
/*      */       {
/* 1717 */         this.fRequestedAttributes = this.values.toSerializableHashtable();
/* 1718 */         paramObjectOutputStream.defaultWriteObject();
/* 1719 */         this.fRequestedAttributes = null;
/*      */       }
/*      */     else
/* 1722 */       paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/* 1738 */     paramObjectInputStream.defaultReadObject();
/* 1739 */     if (this.pointSize == 0.0F) {
/* 1740 */       this.pointSize = this.size;
/*      */     }
/*      */ 
/* 1751 */     if (this.fRequestedAttributes != null) {
/* 1752 */       this.values = getAttributeValues();
/* 1753 */       AttributeValues localAttributeValues = AttributeValues.fromSerializableHashtable(this.fRequestedAttributes);
/*      */ 
/* 1755 */       if (!AttributeValues.is16Hashtable(this.fRequestedAttributes)) {
/* 1756 */         localAttributeValues.unsetDefault();
/*      */       }
/* 1758 */       this.values = getAttributeValues().merge(localAttributeValues);
/* 1759 */       this.nonIdentityTx = this.values.anyNonDefault(EXTRA_MASK);
/* 1760 */       this.hasLayoutAttributes = this.values.anyNonDefault(LAYOUT_MASK);
/*      */ 
/* 1762 */       this.fRequestedAttributes = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getNumGlyphs()
/*      */   {
/* 1774 */     return getFont2D().getNumGlyphs();
/*      */   }
/*      */ 
/*      */   public int getMissingGlyphCode()
/*      */   {
/* 1784 */     return getFont2D().getMissingGlyphCode();
/*      */   }
/*      */ 
/*      */   public byte getBaselineFor(char paramChar)
/*      */   {
/* 1804 */     return getFont2D().getBaselineFor(paramChar);
/*      */   }
/*      */ 
/*      */   public Map<TextAttribute, ?> getAttributes()
/*      */   {
/* 1814 */     return new AttributeMap(getAttributeValues());
/*      */   }
/*      */ 
/*      */   public AttributedCharacterIterator.Attribute[] getAvailableAttributes()
/*      */   {
/* 1828 */     AttributedCharacterIterator.Attribute[] arrayOfAttribute = { TextAttribute.FAMILY, TextAttribute.WEIGHT, TextAttribute.WIDTH, TextAttribute.POSTURE, TextAttribute.SIZE, TextAttribute.TRANSFORM, TextAttribute.SUPERSCRIPT, TextAttribute.CHAR_REPLACEMENT, TextAttribute.FOREGROUND, TextAttribute.BACKGROUND, TextAttribute.UNDERLINE, TextAttribute.STRIKETHROUGH, TextAttribute.RUN_DIRECTION, TextAttribute.BIDI_EMBEDDING, TextAttribute.JUSTIFICATION, TextAttribute.INPUT_METHOD_HIGHLIGHT, TextAttribute.INPUT_METHOD_UNDERLINE, TextAttribute.SWAP_COLORS, TextAttribute.NUMERIC_SHAPING, TextAttribute.KERNING, TextAttribute.LIGATURES, TextAttribute.TRACKING };
/*      */ 
/* 1853 */     return arrayOfAttribute;
/*      */   }
/*      */ 
/*      */   public Font deriveFont(int paramInt, float paramFloat)
/*      */   {
/* 1865 */     if (this.values == null) {
/* 1866 */       return new Font(this.name, paramInt, paramFloat, this.createdFont, this.font2DHandle);
/*      */     }
/* 1868 */     AttributeValues localAttributeValues = getAttributeValues().clone();
/* 1869 */     int i = this.style != paramInt ? this.style : -1;
/* 1870 */     applyStyle(paramInt, localAttributeValues);
/* 1871 */     localAttributeValues.setSize(paramFloat);
/* 1872 */     return new Font(localAttributeValues, null, i, this.createdFont, this.font2DHandle);
/*      */   }
/*      */ 
/*      */   public Font deriveFont(int paramInt, AffineTransform paramAffineTransform)
/*      */   {
/* 1887 */     AttributeValues localAttributeValues = getAttributeValues().clone();
/* 1888 */     int i = this.style != paramInt ? this.style : -1;
/* 1889 */     applyStyle(paramInt, localAttributeValues);
/* 1890 */     applyTransform(paramAffineTransform, localAttributeValues);
/* 1891 */     return new Font(localAttributeValues, null, i, this.createdFont, this.font2DHandle);
/*      */   }
/*      */ 
/*      */   public Font deriveFont(float paramFloat)
/*      */   {
/* 1902 */     if (this.values == null) {
/* 1903 */       return new Font(this.name, this.style, paramFloat, this.createdFont, this.font2DHandle);
/*      */     }
/* 1905 */     AttributeValues localAttributeValues = getAttributeValues().clone();
/* 1906 */     localAttributeValues.setSize(paramFloat);
/* 1907 */     return new Font(localAttributeValues, null, -1, this.createdFont, this.font2DHandle);
/*      */   }
/*      */ 
/*      */   public Font deriveFont(AffineTransform paramAffineTransform)
/*      */   {
/* 1921 */     AttributeValues localAttributeValues = getAttributeValues().clone();
/* 1922 */     applyTransform(paramAffineTransform, localAttributeValues);
/* 1923 */     return new Font(localAttributeValues, null, -1, this.createdFont, this.font2DHandle);
/*      */   }
/*      */ 
/*      */   public Font deriveFont(int paramInt)
/*      */   {
/* 1934 */     if (this.values == null) {
/* 1935 */       return new Font(this.name, paramInt, this.size, this.createdFont, this.font2DHandle);
/*      */     }
/* 1937 */     AttributeValues localAttributeValues = getAttributeValues().clone();
/* 1938 */     int i = this.style != paramInt ? this.style : -1;
/* 1939 */     applyStyle(paramInt, localAttributeValues);
/* 1940 */     return new Font(localAttributeValues, null, i, this.createdFont, this.font2DHandle);
/*      */   }
/*      */ 
/*      */   public Font deriveFont(Map<? extends AttributedCharacterIterator.Attribute, ?> paramMap)
/*      */   {
/* 1954 */     if (paramMap == null) {
/* 1955 */       return this;
/*      */     }
/* 1957 */     AttributeValues localAttributeValues = getAttributeValues().clone();
/* 1958 */     localAttributeValues.merge(paramMap, RECOGNIZED_MASK);
/*      */ 
/* 1960 */     return new Font(localAttributeValues, this.name, this.style, this.createdFont, this.font2DHandle);
/*      */   }
/*      */ 
/*      */   public boolean canDisplay(char paramChar)
/*      */   {
/* 1979 */     return getFont2D().canDisplay(paramChar);
/*      */   }
/*      */ 
/*      */   public boolean canDisplay(int paramInt)
/*      */   {
/* 1996 */     if (!Character.isValidCodePoint(paramInt)) {
/* 1997 */       throw new IllegalArgumentException("invalid code point: " + Integer.toHexString(paramInt));
/*      */     }
/*      */ 
/* 2000 */     return getFont2D().canDisplay(paramInt);
/*      */   }
/*      */ 
/*      */   public int canDisplayUpTo(String paramString)
/*      */   {
/* 2021 */     Font2D localFont2D = getFont2D();
/* 2022 */     int i = paramString.length();
/* 2023 */     for (int j = 0; j < i; j++) {
/* 2024 */       char c = paramString.charAt(j);
/* 2025 */       if (!localFont2D.canDisplay(c))
/*      */       {
/* 2028 */         if (!Character.isHighSurrogate(c)) {
/* 2029 */           return j;
/*      */         }
/* 2031 */         if (!localFont2D.canDisplay(paramString.codePointAt(j))) {
/* 2032 */           return j;
/*      */         }
/* 2034 */         j++;
/*      */       }
/*      */     }
/* 2036 */     return -1;
/*      */   }
/*      */ 
/*      */   public int canDisplayUpTo(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 2059 */     Font2D localFont2D = getFont2D();
/* 2060 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 2061 */       char c = paramArrayOfChar[i];
/* 2062 */       if (!localFont2D.canDisplay(c))
/*      */       {
/* 2065 */         if (!Character.isHighSurrogate(c)) {
/* 2066 */           return i;
/*      */         }
/* 2068 */         if (!localFont2D.canDisplay(Character.codePointAt(paramArrayOfChar, i, paramInt2))) {
/* 2069 */           return i;
/*      */         }
/* 2071 */         i++;
/*      */       }
/*      */     }
/* 2073 */     return -1;
/*      */   }
/*      */ 
/*      */   public int canDisplayUpTo(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/* 2094 */     Font2D localFont2D = getFont2D();
/* 2095 */     char c1 = paramCharacterIterator.setIndex(paramInt1);
/* 2096 */     for (int i = paramInt1; i < paramInt2; c1 = paramCharacterIterator.next()) {
/* 2097 */       if (!localFont2D.canDisplay(c1))
/*      */       {
/* 2100 */         if (!Character.isHighSurrogate(c1)) {
/* 2101 */           return i;
/*      */         }
/* 2103 */         char c2 = paramCharacterIterator.next();
/*      */ 
/* 2105 */         if (!Character.isLowSurrogate(c2)) {
/* 2106 */           return i;
/*      */         }
/* 2108 */         if (!localFont2D.canDisplay(Character.toCodePoint(c1, c2))) {
/* 2109 */           return i;
/*      */         }
/* 2111 */         i++;
/*      */       }
/* 2096 */       i++;
/*      */     }
/*      */ 
/* 2113 */     return -1;
/*      */   }
/*      */ 
/*      */   public float getItalicAngle()
/*      */   {
/* 2124 */     return getItalicAngle(null);
/*      */   }
/*      */ 
/*      */   private float getItalicAngle(FontRenderContext paramFontRenderContext)
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/* 2137 */     if (paramFontRenderContext == null) {
/* 2138 */       localObject1 = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
/* 2139 */       localObject2 = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
/*      */     } else {
/* 2141 */       localObject1 = paramFontRenderContext.getAntiAliasingHint();
/* 2142 */       localObject2 = paramFontRenderContext.getFractionalMetricsHint();
/*      */     }
/* 2144 */     return getFont2D().getItalicAngle(this, identityTx, localObject1, localObject2);
/*      */   }
/*      */ 
/*      */   public boolean hasUniformLineMetrics()
/*      */   {
/* 2159 */     return false;
/*      */   }
/*      */ 
/*      */   private FontLineMetrics defaultLineMetrics(FontRenderContext paramFontRenderContext)
/*      */   {
/* 2164 */     FontLineMetrics localFontLineMetrics = null;
/* 2165 */     if ((this.flmref == null) || ((localFontLineMetrics = (FontLineMetrics)this.flmref.get()) == null) || (!localFontLineMetrics.frc.equals(paramFontRenderContext)))
/*      */     {
/* 2174 */       float[] arrayOfFloat1 = new float[8];
/* 2175 */       getFont2D().getFontMetrics(this, identityTx, paramFontRenderContext.getAntiAliasingHint(), paramFontRenderContext.getFractionalMetricsHint(), arrayOfFloat1);
/*      */ 
/* 2179 */       float f1 = arrayOfFloat1[0];
/* 2180 */       float f2 = arrayOfFloat1[1];
/* 2181 */       float f3 = arrayOfFloat1[2];
/* 2182 */       float f4 = 0.0F;
/* 2183 */       if ((this.values != null) && (this.values.getSuperscript() != 0)) {
/* 2184 */         f4 = (float)getTransform().getTranslateY();
/* 2185 */         f1 -= f4;
/* 2186 */         f2 += f4;
/*      */       }
/* 2188 */       float f5 = f1 + f2 + f3;
/*      */ 
/* 2190 */       int i = 0;
/*      */ 
/* 2192 */       float[] arrayOfFloat2 = { 0.0F, (f2 / 2.0F - f1) / 2.0F, -f1 };
/*      */ 
/* 2194 */       float f6 = arrayOfFloat1[4];
/* 2195 */       float f7 = arrayOfFloat1[5];
/*      */ 
/* 2197 */       float f8 = arrayOfFloat1[6];
/* 2198 */       float f9 = arrayOfFloat1[7];
/*      */ 
/* 2200 */       float f10 = getItalicAngle(paramFontRenderContext);
/*      */ 
/* 2202 */       if (isTransformed()) {
/* 2203 */         localObject = this.values.getCharTransform();
/* 2204 */         if (localObject != null) {
/* 2205 */           Point2D.Float localFloat = new Point2D.Float();
/* 2206 */           localFloat.setLocation(0.0F, f6);
/* 2207 */           ((AffineTransform)localObject).deltaTransform(localFloat, localFloat);
/* 2208 */           f6 = localFloat.y;
/* 2209 */           localFloat.setLocation(0.0F, f7);
/* 2210 */           ((AffineTransform)localObject).deltaTransform(localFloat, localFloat);
/* 2211 */           f7 = localFloat.y;
/* 2212 */           localFloat.setLocation(0.0F, f8);
/* 2213 */           ((AffineTransform)localObject).deltaTransform(localFloat, localFloat);
/* 2214 */           f8 = localFloat.y;
/* 2215 */           localFloat.setLocation(0.0F, f9);
/* 2216 */           ((AffineTransform)localObject).deltaTransform(localFloat, localFloat);
/* 2217 */           f9 = localFloat.y;
/*      */         }
/*      */       }
/* 2220 */       f6 += f4;
/* 2221 */       f8 += f4;
/*      */ 
/* 2223 */       Object localObject = new CoreMetrics(f1, f2, f3, f5, i, arrayOfFloat2, f6, f7, f8, f9, f4, f10);
/*      */ 
/* 2229 */       localFontLineMetrics = new FontLineMetrics(0, (CoreMetrics)localObject, paramFontRenderContext);
/* 2230 */       this.flmref = new SoftReference(localFontLineMetrics);
/*      */     }
/*      */ 
/* 2233 */     return (FontLineMetrics)localFontLineMetrics.clone();
/*      */   }
/*      */ 
/*      */   public LineMetrics getLineMetrics(String paramString, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2245 */     FontLineMetrics localFontLineMetrics = defaultLineMetrics(paramFontRenderContext);
/* 2246 */     localFontLineMetrics.numchars = paramString.length();
/* 2247 */     return localFontLineMetrics;
/*      */   }
/*      */ 
/*      */   public LineMetrics getLineMetrics(String paramString, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2263 */     FontLineMetrics localFontLineMetrics = defaultLineMetrics(paramFontRenderContext);
/* 2264 */     int i = paramInt2 - paramInt1;
/* 2265 */     localFontLineMetrics.numchars = (i < 0 ? 0 : i);
/* 2266 */     return localFontLineMetrics;
/*      */   }
/*      */ 
/*      */   public LineMetrics getLineMetrics(char[] paramArrayOfChar, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2282 */     FontLineMetrics localFontLineMetrics = defaultLineMetrics(paramFontRenderContext);
/* 2283 */     int i = paramInt2 - paramInt1;
/* 2284 */     localFontLineMetrics.numchars = (i < 0 ? 0 : i);
/* 2285 */     return localFontLineMetrics;
/*      */   }
/*      */ 
/*      */   public LineMetrics getLineMetrics(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2301 */     FontLineMetrics localFontLineMetrics = defaultLineMetrics(paramFontRenderContext);
/* 2302 */     int i = paramInt2 - paramInt1;
/* 2303 */     localFontLineMetrics.numchars = (i < 0 ? 0 : i);
/* 2304 */     return localFontLineMetrics;
/*      */   }
/*      */ 
/*      */   public Rectangle2D getStringBounds(String paramString, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2329 */     char[] arrayOfChar = paramString.toCharArray();
/* 2330 */     return getStringBounds(arrayOfChar, 0, arrayOfChar.length, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getStringBounds(String paramString, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2363 */     String str = paramString.substring(paramInt1, paramInt2);
/* 2364 */     return getStringBounds(str, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getStringBounds(char[] paramArrayOfChar, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2398 */     if (paramInt1 < 0) {
/* 2399 */       throw new IndexOutOfBoundsException("beginIndex: " + paramInt1);
/*      */     }
/* 2401 */     if (paramInt2 > paramArrayOfChar.length) {
/* 2402 */       throw new IndexOutOfBoundsException("limit: " + paramInt2);
/*      */     }
/* 2404 */     if (paramInt1 > paramInt2) {
/* 2405 */       throw new IndexOutOfBoundsException("range length: " + (paramInt2 - paramInt1));
/*      */     }
/*      */ 
/* 2412 */     int i = (this.values == null) || ((this.values.getKerning() == 0) && (this.values.getLigatures() == 0) && (this.values.getBaselineTransform() == null)) ? 1 : 0;
/*      */ 
/* 2415 */     if (i != 0) {
/* 2416 */       i = !FontUtilities.isComplexText(paramArrayOfChar, paramInt1, paramInt2) ? 1 : 0;
/*      */     }
/*      */ 
/* 2419 */     if (i != 0) {
/* 2420 */       localObject = new StandardGlyphVector(this, paramArrayOfChar, paramInt1, paramInt2 - paramInt1, paramFontRenderContext);
/*      */ 
/* 2422 */       return ((GlyphVector)localObject).getLogicalBounds();
/*      */     }
/*      */ 
/* 2425 */     Object localObject = new String(paramArrayOfChar, paramInt1, paramInt2 - paramInt1);
/* 2426 */     TextLayout localTextLayout = new TextLayout((String)localObject, this, paramFontRenderContext);
/* 2427 */     return new Rectangle2D.Float(0.0F, -localTextLayout.getAscent(), localTextLayout.getAdvance(), localTextLayout.getAscent() + localTextLayout.getDescent() + localTextLayout.getLeading());
/*      */   }
/*      */ 
/*      */   public Rectangle2D getStringBounds(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2, FontRenderContext paramFontRenderContext)
/*      */   {
/* 2465 */     int i = paramCharacterIterator.getBeginIndex();
/* 2466 */     int j = paramCharacterIterator.getEndIndex();
/*      */ 
/* 2468 */     if (paramInt1 < i) {
/* 2469 */       throw new IndexOutOfBoundsException("beginIndex: " + paramInt1);
/*      */     }
/* 2471 */     if (paramInt2 > j) {
/* 2472 */       throw new IndexOutOfBoundsException("limit: " + paramInt2);
/*      */     }
/* 2474 */     if (paramInt1 > paramInt2) {
/* 2475 */       throw new IndexOutOfBoundsException("range length: " + (paramInt2 - paramInt1));
/*      */     }
/*      */ 
/* 2479 */     char[] arrayOfChar = new char[paramInt2 - paramInt1];
/*      */ 
/* 2481 */     paramCharacterIterator.setIndex(paramInt1);
/* 2482 */     for (int k = 0; k < arrayOfChar.length; k++) {
/* 2483 */       arrayOfChar[k] = paramCharacterIterator.current();
/* 2484 */       paramCharacterIterator.next();
/*      */     }
/*      */ 
/* 2487 */     return getStringBounds(arrayOfChar, 0, arrayOfChar.length, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public Rectangle2D getMaxCharBounds(FontRenderContext paramFontRenderContext)
/*      */   {
/* 2500 */     float[] arrayOfFloat = new float[4];
/*      */ 
/* 2502 */     getFont2D().getFontMetrics(this, paramFontRenderContext, arrayOfFloat);
/*      */ 
/* 2504 */     return new Rectangle2D.Float(0.0F, -arrayOfFloat[0], arrayOfFloat[3], arrayOfFloat[0] + arrayOfFloat[1] + arrayOfFloat[2]);
/*      */   }
/*      */ 
/*      */   public GlyphVector createGlyphVector(FontRenderContext paramFontRenderContext, String paramString)
/*      */   {
/* 2525 */     return new StandardGlyphVector(this, paramString, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public GlyphVector createGlyphVector(FontRenderContext paramFontRenderContext, char[] paramArrayOfChar)
/*      */   {
/* 2544 */     return new StandardGlyphVector(this, paramArrayOfChar, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public GlyphVector createGlyphVector(FontRenderContext paramFontRenderContext, CharacterIterator paramCharacterIterator)
/*      */   {
/* 2564 */     return new StandardGlyphVector(this, paramCharacterIterator, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public GlyphVector createGlyphVector(FontRenderContext paramFontRenderContext, int[] paramArrayOfInt)
/*      */   {
/* 2584 */     return new StandardGlyphVector(this, paramArrayOfInt, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public GlyphVector layoutGlyphVector(FontRenderContext paramFontRenderContext, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2635 */     GlyphLayout localGlyphLayout = GlyphLayout.get(null);
/* 2636 */     StandardGlyphVector localStandardGlyphVector = localGlyphLayout.layout(this, paramFontRenderContext, paramArrayOfChar, paramInt1, paramInt2 - paramInt1, paramInt3, null);
/*      */ 
/* 2638 */     GlyphLayout.done(localGlyphLayout);
/* 2639 */     return localStandardGlyphVector;
/*      */   }
/*      */ 
/*      */   private static void applyTransform(AffineTransform paramAffineTransform, AttributeValues paramAttributeValues)
/*      */   {
/* 2668 */     if (paramAffineTransform == null) {
/* 2669 */       throw new IllegalArgumentException("transform must not be null");
/*      */     }
/* 2671 */     paramAttributeValues.setTransform(paramAffineTransform);
/*      */   }
/*      */ 
/*      */   private static void applyStyle(int paramInt, AttributeValues paramAttributeValues)
/*      */   {
/* 2676 */     paramAttributeValues.setWeight((paramInt & 0x1) != 0 ? 2.0F : 1.0F);
/*      */ 
/* 2678 */     paramAttributeValues.setPosture((paramInt & 0x2) != 0 ? 0.2F : 0.0F);
/*      */   }
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   static
/*      */   {
/*  246 */     Toolkit.loadLibraries();
/*  247 */     initIDs();
/*  248 */     FontAccess.setFontAccess(new FontAccessImpl(null));
/*      */   }
/*      */ 
/*      */   private static class FontAccessImpl extends FontAccess
/*      */   {
/*      */     public Font2D getFont2D(Font paramFont)
/*      */     {
/*  228 */       return paramFont.getFont2D();
/*      */     }
/*      */ 
/*      */     public void setFont2D(Font paramFont, Font2DHandle paramFont2DHandle) {
/*  232 */       paramFont.font2DHandle = paramFont2DHandle;
/*      */     }
/*      */ 
/*      */     public void setCreatedFont(Font paramFont) {
/*  236 */       paramFont.createdFont = true;
/*      */     }
/*      */ 
/*      */     public boolean isCreatedFont(Font paramFont) {
/*  240 */       return paramFont.createdFont;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Font
 * JD-Core Version:    0.6.2
 */
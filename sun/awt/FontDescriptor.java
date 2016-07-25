/*     */ package sun.awt;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.AccessController;
/*     */ import sun.nio.cs.HistoricallyNamedCharset;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ public class FontDescriptor
/*     */   implements Cloneable
/*     */ {
/*     */   String nativeName;
/*     */   public CharsetEncoder encoder;
/*     */   String charsetName;
/*     */   private int[] exclusionRanges;
/*     */   public CharsetEncoder unicodeEncoder;
/*     */   boolean useUnicode;
/* 119 */   static boolean isLE = !"UnicodeBig".equals(str);
/*     */ 
/*     */   public FontDescriptor(String paramString, CharsetEncoder paramCharsetEncoder, int[] paramArrayOfInt)
/*     */   {
/*  47 */     this.nativeName = paramString;
/*  48 */     this.encoder = paramCharsetEncoder;
/*  49 */     this.exclusionRanges = paramArrayOfInt;
/*  50 */     this.useUnicode = false;
/*  51 */     Charset localCharset = paramCharsetEncoder.charset();
/*  52 */     if ((localCharset instanceof HistoricallyNamedCharset))
/*  53 */       this.charsetName = ((HistoricallyNamedCharset)localCharset).historicalName();
/*     */     else
/*  55 */       this.charsetName = localCharset.name();
/*     */   }
/*     */ 
/*     */   public String getNativeName()
/*     */   {
/*  60 */     return this.nativeName;
/*     */   }
/*     */ 
/*     */   public CharsetEncoder getFontCharsetEncoder() {
/*  64 */     return this.encoder;
/*     */   }
/*     */ 
/*     */   public String getFontCharsetName() {
/*  68 */     return this.charsetName;
/*     */   }
/*     */ 
/*     */   public int[] getExclusionRanges() {
/*  72 */     return this.exclusionRanges;
/*     */   }
/*     */ 
/*     */   public boolean isExcluded(char paramChar)
/*     */   {
/*  79 */     for (int i = 0; i < this.exclusionRanges.length; )
/*     */     {
/*  81 */       char c1 = this.exclusionRanges[(i++)];
/*  82 */       char c2 = this.exclusionRanges[(i++)];
/*     */ 
/*  84 */       if ((paramChar >= c1) && (paramChar <= c2)) {
/*  85 */         return true;
/*     */       }
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString() {
/*  92 */     return super.toString() + " [" + this.nativeName + "|" + this.encoder + "]";
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   public boolean useUnicode()
/*     */   {
/* 105 */     if ((this.useUnicode) && (this.unicodeEncoder == null))
/*     */       try {
/* 107 */         this.unicodeEncoder = (isLE ? StandardCharsets.UTF_16LE.newEncoder() : StandardCharsets.UTF_16BE.newEncoder());
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/*     */       }
/* 112 */     return this.useUnicode;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  35 */     NativeLibLoader.loadLibraries();
/*  36 */     initIDs();
/*     */ 
/* 116 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("sun.io.unicode.encoding", "UnicodeBig"));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.FontDescriptor
 * JD-Core Version:    0.6.2
 */
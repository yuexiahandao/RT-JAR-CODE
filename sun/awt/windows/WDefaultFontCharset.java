/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.CharsetEncoder;
/*    */ import sun.awt.AWTCharset;
/*    */ import sun.awt.AWTCharset.Encoder;
/*    */ 
/*    */ public class WDefaultFontCharset extends AWTCharset
/*    */ {
/*    */   private String fontName;
/*    */ 
/*    */   public WDefaultFontCharset(String paramString)
/*    */   {
/* 40 */     super("WDefaultFontCharset", Charset.forName("windows-1252"));
/* 41 */     this.fontName = paramString;
/*    */   }
/*    */ 
/*    */   public CharsetEncoder newEncoder() {
/* 45 */     return new Encoder(null);
/*    */   }
/*    */ 
/*    */   public synchronized native boolean canConvert(char paramChar);
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/*    */   static
/*    */   {
/* 33 */     initIDs();
/*    */   }
/*    */ 
/*    */   private class Encoder extends AWTCharset.Encoder
/*    */   {
/*    */     private Encoder()
/*    */     {
/* 48 */       super();
/*    */     }
/* 50 */     public boolean canEncode(char paramChar) { return WDefaultFontCharset.this.canConvert(paramChar); }
/*    */ 
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDefaultFontCharset
 * JD-Core Version:    0.6.2
 */
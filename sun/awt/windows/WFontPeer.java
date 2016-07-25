/*    */ package sun.awt.windows;
/*    */ 
/*    */ import sun.awt.PlatformFont;
/*    */ 
/*    */ public class WFontPeer extends PlatformFont
/*    */ {
/*    */   private String textComponentFontName;
/*    */ 
/*    */   public WFontPeer(String paramString, int paramInt)
/*    */   {
/* 35 */     super(paramString, paramInt);
/* 36 */     if (this.fontConfig != null)
/* 37 */       this.textComponentFontName = ((WFontConfiguration)this.fontConfig).getTextComponentFontName(this.familyName, paramInt);
/*    */   }
/*    */ 
/*    */   protected char getMissingGlyphCharacter()
/*    */   {
/* 42 */     return '❑';
/*    */   }
/*    */   private static native void initIDs();
/*    */ 
/*    */   static {
/* 47 */     initIDs();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WFontPeer
 * JD-Core Version:    0.6.2
 */
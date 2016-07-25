/*    */ package javax.swing.plaf.metal;
/*    */ 
/*    */ import com.sun.java.swing.plaf.windows.DesktopProperty;
/*    */ import java.awt.Font;
/*    */ 
/*    */ class MetalFontDesktopProperty extends DesktopProperty
/*    */ {
/* 39 */   private static final String[] propertyMapping = { "win.ansiVar.font.height", "win.tooltip.font.height", "win.ansiVar.font.height", "win.menu.font.height", "win.frame.captionFont.height", "win.menu.font.height" };
/*    */   private int type;
/*    */ 
/*    */   MetalFontDesktopProperty(int paramInt)
/*    */   {
/* 61 */     this(propertyMapping[paramInt], paramInt);
/*    */   }
/*    */ 
/*    */   MetalFontDesktopProperty(String paramString, int paramInt)
/*    */   {
/* 74 */     super(paramString, null);
/* 75 */     this.type = paramInt;
/*    */   }
/*    */ 
/*    */   protected Object configureValue(Object paramObject)
/*    */   {
/* 83 */     if ((paramObject instanceof Integer)) {
/* 84 */       paramObject = new Font(DefaultMetalTheme.getDefaultFontName(this.type), DefaultMetalTheme.getDefaultFontStyle(this.type), ((Integer)paramObject).intValue());
/*    */     }
/*    */ 
/* 88 */     return super.configureValue(paramObject);
/*    */   }
/*    */ 
/*    */   protected Object getDefaultValue()
/*    */   {
/* 95 */     return new Font(DefaultMetalTheme.getDefaultFontName(this.type), DefaultMetalTheme.getDefaultFontStyle(this.type), DefaultMetalTheme.getDefaultFontSize(this.type));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalFontDesktopProperty
 * JD-Core Version:    0.6.2
 */
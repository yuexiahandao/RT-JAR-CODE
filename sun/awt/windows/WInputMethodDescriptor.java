/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.im.spi.InputMethod;
/*    */ import java.awt.im.spi.InputMethodDescriptor;
/*    */ import java.util.Locale;
/*    */ 
/*    */ class WInputMethodDescriptor
/*    */   implements InputMethodDescriptor
/*    */ {
/*    */   public Locale[] getAvailableLocales()
/*    */   {
/* 50 */     Locale[] arrayOfLocale1 = getAvailableLocalesInternal();
/* 51 */     Locale[] arrayOfLocale2 = new Locale[arrayOfLocale1.length];
/* 52 */     System.arraycopy(arrayOfLocale1, 0, arrayOfLocale2, 0, arrayOfLocale1.length);
/* 53 */     return arrayOfLocale2;
/*    */   }
/*    */ 
/*    */   static Locale[] getAvailableLocalesInternal() {
/* 57 */     return getNativeAvailableLocales();
/*    */   }
/*    */ 
/*    */   public boolean hasDynamicLocaleList()
/*    */   {
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   public synchronized String getInputMethodDisplayName(Locale paramLocale1, Locale paramLocale2)
/*    */   {
/* 74 */     String str = "System Input Methods";
/* 75 */     if (Locale.getDefault().equals(paramLocale2)) {
/* 76 */       str = Toolkit.getProperty("AWT.HostInputMethodDisplayName", str);
/*    */     }
/* 78 */     return str;
/*    */   }
/*    */ 
/*    */   public Image getInputMethodIcon(Locale paramLocale)
/*    */   {
/* 85 */     return null;
/*    */   }
/*    */ 
/*    */   public InputMethod createInputMethod()
/*    */     throws Exception
/*    */   {
/* 92 */     return new WInputMethod();
/*    */   }
/*    */ 
/*    */   private static native Locale[] getNativeAvailableLocales();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WInputMethodDescriptor
 * JD-Core Version:    0.6.2
 */
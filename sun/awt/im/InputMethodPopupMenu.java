/*     */ package sun.awt.im;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Component;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.im.spi.InputMethodDescriptor;
/*     */ import java.util.Locale;
/*     */ import javax.swing.JDialog;
/*     */ import javax.swing.JFrame;
/*     */ 
/*     */ abstract class InputMethodPopupMenu
/*     */   implements ActionListener
/*     */ {
/*     */   static InputMethodPopupMenu getInstance(Component paramComponent, String paramString)
/*     */   {
/*  58 */     if (((paramComponent instanceof JFrame)) || ((paramComponent instanceof JDialog)))
/*     */     {
/*  60 */       return new JInputMethodPopupMenu(paramString);
/*     */     }
/*  62 */     return new AWTInputMethodPopupMenu(paramString);
/*     */   }
/*     */   abstract void show(Component paramComponent, int paramInt1, int paramInt2);
/*     */ 
/*     */   abstract void removeAll();
/*     */ 
/*     */   abstract void addSeparator();
/*     */ 
/*     */   abstract void addToComponent(Component paramComponent);
/*     */ 
/*     */   abstract Object createSubmenu(String paramString);
/*     */ 
/*     */   abstract void add(Object paramObject);
/*     */ 
/*     */   abstract void addMenuItem(String paramString1, String paramString2, String paramString3);
/*     */ 
/*     */   abstract void addMenuItem(Object paramObject, String paramString1, String paramString2, String paramString3);
/*     */ 
/*     */   void addOneInputMethodToMenu(InputMethodLocator paramInputMethodLocator, String paramString) {
/*  84 */     InputMethodDescriptor localInputMethodDescriptor = paramInputMethodLocator.getDescriptor();
/*  85 */     String str1 = localInputMethodDescriptor.getInputMethodDisplayName(null, Locale.getDefault());
/*  86 */     String str2 = paramInputMethodLocator.getActionCommandString();
/*  87 */     Locale[] arrayOfLocale = null;
/*     */     int i;
/*     */     try { arrayOfLocale = localInputMethodDescriptor.getAvailableLocales();
/*  91 */       i = arrayOfLocale.length;
/*     */     }
/*     */     catch (AWTException localAWTException)
/*     */     {
/*  96 */       i = 0;
/*     */     }
/*  98 */     if (i == 0)
/*     */     {
/* 100 */       addMenuItem(str1, null, paramString);
/* 101 */     } else if (i == 1) {
/* 102 */       if (localInputMethodDescriptor.hasDynamicLocaleList())
/*     */       {
/* 106 */         str1 = localInputMethodDescriptor.getInputMethodDisplayName(arrayOfLocale[0], Locale.getDefault());
/* 107 */         str2 = paramInputMethodLocator.deriveLocator(arrayOfLocale[0]).getActionCommandString();
/*     */       }
/* 109 */       addMenuItem(str1, str2, paramString);
/*     */     } else {
/* 111 */       Object localObject = createSubmenu(str1);
/* 112 */       add(localObject);
/* 113 */       for (int j = 0; j < i; j++) {
/* 114 */         Locale localLocale = arrayOfLocale[j];
/* 115 */         String str3 = getLocaleName(localLocale);
/* 116 */         String str4 = paramInputMethodLocator.deriveLocator(localLocale).getActionCommandString();
/* 117 */         addMenuItem(localObject, str3, str4, paramString);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static boolean isSelected(String paramString1, String paramString2)
/*     */   {
/* 127 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 128 */       return false;
/*     */     }
/* 130 */     if (paramString1.equals(paramString2)) {
/* 131 */       return true;
/*     */     }
/*     */ 
/* 134 */     int i = paramString2.indexOf('\n');
/* 135 */     if ((i != -1) && (paramString2.substring(0, i).equals(paramString1))) {
/* 136 */       return true;
/*     */     }
/* 138 */     return false;
/*     */   }
/*     */ 
/*     */   String getLocaleName(Locale paramLocale)
/*     */   {
/* 149 */     String str1 = paramLocale.toString();
/* 150 */     String str2 = Toolkit.getProperty("AWT.InputMethodLanguage." + str1, null);
/* 151 */     if (str2 == null) {
/* 152 */       str2 = paramLocale.getDisplayName();
/* 153 */       if ((str2 == null) || (str2.length() == 0))
/* 154 */         str2 = str1;
/*     */     }
/* 156 */     return str2;
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 161 */     String str = paramActionEvent.getActionCommand();
/* 162 */     ((ExecutableInputMethodManager)InputMethodManager.getInstance()).changeInputMethod(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.InputMethodPopupMenu
 * JD-Core Version:    0.6.2
 */
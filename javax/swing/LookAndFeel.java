/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Font;
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ColorUIResource;
/*     */ import javax.swing.plaf.ComponentInputMapUIResource;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ import javax.swing.plaf.InputMapUIResource;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.text.JTextComponent.KeyBinding;
/*     */ import sun.awt.SunToolkit;
/*     */ import sun.swing.DefaultLayoutStyle;
/*     */ import sun.swing.ImageIconUIResource;
/*     */ import sun.swing.SwingUtilities2;
/*     */ 
/*     */ public abstract class LookAndFeel
/*     */ {
/*     */   public static void installColors(JComponent paramJComponent, String paramString1, String paramString2)
/*     */   {
/* 173 */     Color localColor1 = paramJComponent.getBackground();
/* 174 */     if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/* 175 */       paramJComponent.setBackground(UIManager.getColor(paramString1));
/*     */     }
/*     */ 
/* 178 */     Color localColor2 = paramJComponent.getForeground();
/* 179 */     if ((localColor2 == null) || ((localColor2 instanceof UIResource)))
/* 180 */       paramJComponent.setForeground(UIManager.getColor(paramString2));
/*     */   }
/*     */ 
/*     */   public static void installColorsAndFont(JComponent paramJComponent, String paramString1, String paramString2, String paramString3)
/*     */   {
/* 206 */     Font localFont = paramJComponent.getFont();
/* 207 */     if ((localFont == null) || ((localFont instanceof UIResource))) {
/* 208 */       paramJComponent.setFont(UIManager.getFont(paramString3));
/*     */     }
/*     */ 
/* 211 */     installColors(paramJComponent, paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static void installBorder(JComponent paramJComponent, String paramString)
/*     */   {
/* 226 */     Border localBorder = paramJComponent.getBorder();
/* 227 */     if ((localBorder == null) || ((localBorder instanceof UIResource)))
/* 228 */       paramJComponent.setBorder(UIManager.getBorder(paramString));
/*     */   }
/*     */ 
/*     */   public static void uninstallBorder(JComponent paramJComponent)
/*     */   {
/* 242 */     if ((paramJComponent.getBorder() instanceof UIResource))
/* 243 */       paramJComponent.setBorder(null);
/*     */   }
/*     */ 
/*     */   public static void installProperty(JComponent paramJComponent, String paramString, Object paramObject)
/*     */   {
/* 275 */     if (SunToolkit.isInstanceOf(paramJComponent, "javax.swing.JPasswordField")) {
/* 276 */       if (!((JPasswordField)paramJComponent).customSetUIProperty(paramString, paramObject))
/* 277 */         paramJComponent.setUIProperty(paramString, paramObject);
/*     */     }
/*     */     else
/* 280 */       paramJComponent.setUIProperty(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public static JTextComponent.KeyBinding[] makeKeyBindings(Object[] paramArrayOfObject)
/*     */   {
/* 333 */     JTextComponent.KeyBinding[] arrayOfKeyBinding = new JTextComponent.KeyBinding[paramArrayOfObject.length / 2];
/*     */ 
/* 335 */     for (int i = 0; i < arrayOfKeyBinding.length; i++) {
/* 336 */       Object localObject = paramArrayOfObject[(2 * i)];
/* 337 */       KeyStroke localKeyStroke = (localObject instanceof KeyStroke) ? (KeyStroke)localObject : KeyStroke.getKeyStroke((String)localObject);
/*     */ 
/* 340 */       String str = (String)paramArrayOfObject[(2 * i + 1)];
/* 341 */       arrayOfKeyBinding[i] = new JTextComponent.KeyBinding(localKeyStroke, str);
/*     */     }
/*     */ 
/* 344 */     return arrayOfKeyBinding;
/*     */   }
/*     */ 
/*     */   public static InputMap makeInputMap(Object[] paramArrayOfObject)
/*     */   {
/* 361 */     InputMapUIResource localInputMapUIResource = new InputMapUIResource();
/* 362 */     loadKeyBindings(localInputMapUIResource, paramArrayOfObject);
/* 363 */     return localInputMapUIResource;
/*     */   }
/*     */ 
/*     */   public static ComponentInputMap makeComponentInputMap(JComponent paramJComponent, Object[] paramArrayOfObject)
/*     */   {
/* 387 */     ComponentInputMapUIResource localComponentInputMapUIResource = new ComponentInputMapUIResource(paramJComponent);
/* 388 */     loadKeyBindings(localComponentInputMapUIResource, paramArrayOfObject);
/* 389 */     return localComponentInputMapUIResource;
/*     */   }
/*     */ 
/*     */   public static void loadKeyBindings(InputMap paramInputMap, Object[] paramArrayOfObject)
/*     */   {
/* 432 */     if (paramArrayOfObject != null) {
/* 433 */       int i = 0; int j = paramArrayOfObject.length;
/* 434 */       for (; i < j; i++) {
/* 435 */         Object localObject = paramArrayOfObject[(i++)];
/* 436 */         KeyStroke localKeyStroke = (localObject instanceof KeyStroke) ? (KeyStroke)localObject : KeyStroke.getKeyStroke((String)localObject);
/*     */ 
/* 439 */         paramInputMap.put(localKeyStroke, paramArrayOfObject[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object makeIcon(Class<?> paramClass, String paramString)
/*     */   {
/* 467 */     return SwingUtilities2.makeIcon(paramClass, paramClass, paramString);
/*     */   }
/*     */ 
/*     */   public LayoutStyle getLayoutStyle()
/*     */   {
/* 483 */     return DefaultLayoutStyle.getInstance();
/*     */   }
/*     */ 
/*     */   public void provideErrorFeedback(Component paramComponent)
/*     */   {
/* 500 */     Toolkit localToolkit = null;
/* 501 */     if (paramComponent != null)
/* 502 */       localToolkit = paramComponent.getToolkit();
/*     */     else {
/* 504 */       localToolkit = Toolkit.getDefaultToolkit();
/*     */     }
/* 506 */     localToolkit.beep();
/*     */   }
/*     */ 
/*     */   public static Object getDesktopPropertyValue(String paramString, Object paramObject)
/*     */   {
/* 524 */     Object localObject = Toolkit.getDefaultToolkit().getDesktopProperty(paramString);
/* 525 */     if (localObject == null)
/* 526 */       return paramObject;
/* 527 */     if ((localObject instanceof Color))
/* 528 */       return new ColorUIResource((Color)localObject);
/* 529 */     if ((localObject instanceof Font)) {
/* 530 */       return new FontUIResource((Font)localObject);
/*     */     }
/* 532 */     return localObject;
/*     */   }
/*     */ 
/*     */   public Icon getDisabledIcon(JComponent paramJComponent, Icon paramIcon)
/*     */   {
/* 555 */     if ((paramIcon instanceof ImageIcon)) {
/* 556 */       return new ImageIconUIResource(GrayFilter.createDisabledImage(((ImageIcon)paramIcon).getImage()));
/*     */     }
/*     */ 
/* 559 */     return null;
/*     */   }
/*     */ 
/*     */   public Icon getDisabledSelectedIcon(JComponent paramJComponent, Icon paramIcon)
/*     */   {
/* 584 */     return getDisabledIcon(paramJComponent, paramIcon);
/*     */   }
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   public abstract String getID();
/*     */ 
/*     */   public abstract String getDescription();
/*     */ 
/*     */   public boolean getSupportsWindowDecorations()
/*     */   {
/* 642 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract boolean isNativeLookAndFeel();
/*     */ 
/*     */   public abstract boolean isSupportedLookAndFeel();
/*     */ 
/*     */   public void initialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void uninitialize()
/*     */   {
/*     */   }
/*     */ 
/*     */   public UIDefaults getDefaults()
/*     */   {
/* 717 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 727 */     return "[" + getDescription() + " - " + getClass().getName() + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.LookAndFeel
 * JD-Core Version:    0.6.2
 */
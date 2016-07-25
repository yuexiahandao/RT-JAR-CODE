/*     */ package sun.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class DefaultLookup
/*     */ {
/*  50 */   private static final Object DEFAULT_LOOKUP_KEY = new StringBuffer("DefaultLookup");
/*     */   private static Thread currentDefaultThread;
/*     */   private static DefaultLookup currentDefaultLookup;
/*     */   private static boolean isLookupSet;
/*     */ 
/*     */   public static void setDefaultLookup(DefaultLookup paramDefaultLookup)
/*     */   {
/*  73 */     synchronized (DefaultLookup.class) {
/*  74 */       if ((!isLookupSet) && (paramDefaultLookup == null))
/*     */       {
/*  77 */         return;
/*     */       }
/*  79 */       if (paramDefaultLookup == null)
/*     */       {
/*  83 */         paramDefaultLookup = new DefaultLookup();
/*     */       }
/*  85 */       isLookupSet = true;
/*  86 */       AppContext.getAppContext().put(DEFAULT_LOOKUP_KEY, paramDefaultLookup);
/*  87 */       currentDefaultThread = Thread.currentThread();
/*  88 */       currentDefaultLookup = paramDefaultLookup;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Object get(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString)
/*     */   {
/*     */     boolean bool;
/*  94 */     synchronized (DefaultLookup.class) {
/*  95 */       bool = isLookupSet;
/*     */     }
/*  97 */     if (!bool)
/*     */     {
/*  99 */       return UIManager.get(paramString, paramJComponent.getLocale());
/*     */     }
/* 101 */     ??? = Thread.currentThread();
/*     */     DefaultLookup localDefaultLookup;
/* 103 */     synchronized (DefaultLookup.class)
/*     */     {
/* 106 */       if (??? == currentDefaultThread)
/*     */       {
/* 108 */         localDefaultLookup = currentDefaultLookup;
/*     */       }
/*     */       else
/*     */       {
/* 112 */         localDefaultLookup = (DefaultLookup)AppContext.getAppContext().get(DEFAULT_LOOKUP_KEY);
/*     */ 
/* 114 */         if (localDefaultLookup == null)
/*     */         {
/* 117 */           localDefaultLookup = new DefaultLookup();
/* 118 */           AppContext.getAppContext().put(DEFAULT_LOOKUP_KEY, localDefaultLookup);
/*     */         }
/*     */ 
/* 121 */         currentDefaultThread = (Thread)???;
/* 122 */         currentDefaultLookup = localDefaultLookup;
/*     */       }
/*     */     }
/* 125 */     return localDefaultLookup.getDefault(paramJComponent, paramComponentUI, paramString);
/*     */   }
/*     */ 
/*     */   public static int getInt(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString, int paramInt)
/*     */   {
/* 133 */     Object localObject = get(paramJComponent, paramComponentUI, paramString);
/*     */ 
/* 135 */     if ((localObject == null) || (!(localObject instanceof Number))) {
/* 136 */       return paramInt;
/*     */     }
/* 138 */     return ((Number)localObject).intValue();
/*     */   }
/*     */ 
/*     */   public static int getInt(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString) {
/* 142 */     return getInt(paramJComponent, paramComponentUI, paramString, -1);
/*     */   }
/*     */ 
/*     */   public static Insets getInsets(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString, Insets paramInsets)
/*     */   {
/* 147 */     Object localObject = get(paramJComponent, paramComponentUI, paramString);
/*     */ 
/* 149 */     if ((localObject == null) || (!(localObject instanceof Insets))) {
/* 150 */       return paramInsets;
/*     */     }
/* 152 */     return (Insets)localObject;
/*     */   }
/*     */ 
/*     */   public static Insets getInsets(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString) {
/* 156 */     return getInsets(paramJComponent, paramComponentUI, paramString, null);
/*     */   }
/*     */ 
/*     */   public static boolean getBoolean(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString, boolean paramBoolean)
/*     */   {
/* 161 */     Object localObject = get(paramJComponent, paramComponentUI, paramString);
/*     */ 
/* 163 */     if ((localObject == null) || (!(localObject instanceof Boolean))) {
/* 164 */       return paramBoolean;
/*     */     }
/* 166 */     return ((Boolean)localObject).booleanValue();
/*     */   }
/*     */ 
/*     */   public static boolean getBoolean(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString) {
/* 170 */     return getBoolean(paramJComponent, paramComponentUI, paramString, false);
/*     */   }
/*     */ 
/*     */   public static Color getColor(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString, Color paramColor)
/*     */   {
/* 175 */     Object localObject = get(paramJComponent, paramComponentUI, paramString);
/*     */ 
/* 177 */     if ((localObject == null) || (!(localObject instanceof Color))) {
/* 178 */       return paramColor;
/*     */     }
/* 180 */     return (Color)localObject;
/*     */   }
/*     */ 
/*     */   public static Color getColor(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString) {
/* 184 */     return getColor(paramJComponent, paramComponentUI, paramString, null);
/*     */   }
/*     */ 
/*     */   public static Icon getIcon(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString, Icon paramIcon)
/*     */   {
/* 189 */     Object localObject = get(paramJComponent, paramComponentUI, paramString);
/* 190 */     if ((localObject == null) || (!(localObject instanceof Icon))) {
/* 191 */       return paramIcon;
/*     */     }
/* 193 */     return (Icon)localObject;
/*     */   }
/*     */ 
/*     */   public static Icon getIcon(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString) {
/* 197 */     return getIcon(paramJComponent, paramComponentUI, paramString, null);
/*     */   }
/*     */ 
/*     */   public static Border getBorder(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString, Border paramBorder)
/*     */   {
/* 202 */     Object localObject = get(paramJComponent, paramComponentUI, paramString);
/* 203 */     if ((localObject == null) || (!(localObject instanceof Border))) {
/* 204 */       return paramBorder;
/*     */     }
/* 206 */     return (Border)localObject;
/*     */   }
/*     */ 
/*     */   public static Border getBorder(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString) {
/* 210 */     return getBorder(paramJComponent, paramComponentUI, paramString, null);
/*     */   }
/*     */ 
/*     */   public Object getDefault(JComponent paramJComponent, ComponentUI paramComponentUI, String paramString)
/*     */   {
/* 215 */     return UIManager.get(paramString, paramJComponent.getLocale());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.DefaultLookup
 * JD-Core Version:    0.6.2
 */
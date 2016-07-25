/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Insets;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import java.util.Vector;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.SwingPropertyChangeSupport;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import sun.reflect.misc.MethodUtil;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.swing.SwingUtilities2;
/*      */ import sun.util.CoreResourceBundleControl;
/*      */ 
/*      */ public class UIDefaults extends Hashtable<Object, Object>
/*      */ {
/*   77 */   private static final Object PENDING = "Pending";
/*      */   private SwingPropertyChangeSupport changeSupport;
/*      */   private Vector<String> resourceBundles;
/*   83 */   private Locale defaultLocale = Locale.getDefault();
/*      */   private Map<Locale, Map<String, Object>> resourceCache;
/*      */ 
/*      */   public UIDefaults()
/*      */   {
/*   97 */     this(700, 0.75F);
/*      */   }
/*      */ 
/*      */   public UIDefaults(int paramInt, float paramFloat)
/*      */   {
/*  110 */     super(paramInt, paramFloat);
/*  111 */     this.resourceCache = new HashMap();
/*      */   }
/*      */ 
/*      */   public UIDefaults(Object[] paramArrayOfObject)
/*      */   {
/*  130 */     super(paramArrayOfObject.length / 2);
/*  131 */     for (int i = 0; i < paramArrayOfObject.length; i += 2)
/*  132 */       super.put(paramArrayOfObject[i], paramArrayOfObject[(i + 1)]);
/*      */   }
/*      */ 
/*      */   public Object get(Object paramObject)
/*      */   {
/*  163 */     Object localObject = getFromHashtable(paramObject);
/*  164 */     return localObject != null ? localObject : getFromResourceBundle(paramObject, null);
/*      */   }
/*      */ 
/*      */   private Object getFromHashtable(Object paramObject)
/*      */   {
/*  175 */     Object localObject1 = super.get(paramObject);
/*  176 */     if ((localObject1 != PENDING) && (!(localObject1 instanceof ActiveValue)) && (!(localObject1 instanceof LazyValue)))
/*      */     {
/*  179 */       return localObject1;
/*      */     }
/*      */ 
/*  188 */     synchronized (this) {
/*  189 */       localObject1 = super.get(paramObject);
/*  190 */       if (localObject1 == PENDING) {
/*      */         do {
/*      */           try {
/*  193 */             wait();
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {
/*      */           }
/*  197 */           localObject1 = super.get(paramObject);
/*      */         }
/*  199 */         while (localObject1 == PENDING);
/*  200 */         return localObject1;
/*      */       }
/*  202 */       if ((localObject1 instanceof LazyValue)) {
/*  203 */         super.put(paramObject, PENDING);
/*      */       }
/*  205 */       else if (!(localObject1 instanceof ActiveValue)) {
/*  206 */         return localObject1;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  213 */     if ((localObject1 instanceof LazyValue))
/*      */     {
/*      */       try
/*      */       {
/*  218 */         localObject1 = ((LazyValue)localObject1).createValue(this);
/*      */       }
/*      */       finally {
/*  221 */         synchronized (this) {
/*  222 */           if (localObject1 == null) {
/*  223 */             super.remove(paramObject);
/*      */           }
/*      */           else {
/*  226 */             super.put(paramObject, localObject1);
/*      */           }
/*  228 */           notifyAll();
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  233 */       localObject1 = ((ActiveValue)localObject1).createValue(this);
/*      */     }
/*      */ 
/*  236 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public Object get(Object paramObject, Locale paramLocale)
/*      */   {
/*  266 */     Object localObject = getFromHashtable(paramObject);
/*  267 */     return localObject != null ? localObject : getFromResourceBundle(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   private Object getFromResourceBundle(Object paramObject, Locale paramLocale)
/*      */   {
/*  275 */     if ((this.resourceBundles == null) || (this.resourceBundles.isEmpty()) || (!(paramObject instanceof String)))
/*      */     {
/*  278 */       return null;
/*      */     }
/*      */ 
/*  282 */     if (paramLocale == null) {
/*  283 */       if (this.defaultLocale == null) {
/*  284 */         return null;
/*      */       }
/*  286 */       paramLocale = this.defaultLocale;
/*      */     }
/*      */ 
/*  289 */     synchronized (this) {
/*  290 */       return getResourceCache(paramLocale).get(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Map<String, Object> getResourceCache(Locale paramLocale)
/*      */   {
/*  298 */     Object localObject1 = (Map)this.resourceCache.get(paramLocale);
/*      */ 
/*  300 */     if (localObject1 == null) {
/*  301 */       localObject1 = new TextAndMnemonicHashMap(null);
/*  302 */       for (int i = this.resourceBundles.size() - 1; i >= 0; i--) {
/*  303 */         String str1 = (String)this.resourceBundles.get(i);
/*      */         try {
/*  305 */           CoreResourceBundleControl localCoreResourceBundleControl = CoreResourceBundleControl.getRBControlInstance(str1);
/*      */           ResourceBundle localResourceBundle;
/*  307 */           if (localCoreResourceBundleControl != null)
/*  308 */             localResourceBundle = ResourceBundle.getBundle(str1, paramLocale, localCoreResourceBundleControl);
/*      */           else {
/*  310 */             localResourceBundle = ResourceBundle.getBundle(str1, paramLocale);
/*      */           }
/*  312 */           Enumeration localEnumeration = localResourceBundle.getKeys();
/*      */ 
/*  314 */           while (localEnumeration.hasMoreElements()) {
/*  315 */             String str2 = (String)localEnumeration.nextElement();
/*      */ 
/*  317 */             if (((Map)localObject1).get(str2) == null) {
/*  318 */               Object localObject2 = localResourceBundle.getObject(str2);
/*      */ 
/*  320 */               ((Map)localObject1).put(str2, localObject2);
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (MissingResourceException localMissingResourceException) {
/*      */         }
/*      */       }
/*  327 */       this.resourceCache.put(paramLocale, localObject1);
/*      */     }
/*  329 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public Object put(Object paramObject1, Object paramObject2)
/*      */   {
/*  347 */     Object localObject = paramObject2 == null ? super.remove(paramObject1) : super.put(paramObject1, paramObject2);
/*  348 */     if ((paramObject1 instanceof String)) {
/*  349 */       firePropertyChange((String)paramObject1, localObject, paramObject2);
/*      */     }
/*  351 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void putDefaults(Object[] paramArrayOfObject)
/*      */   {
/*  367 */     int i = 0; for (int j = paramArrayOfObject.length; i < j; i += 2) {
/*  368 */       Object localObject = paramArrayOfObject[(i + 1)];
/*  369 */       if (localObject == null) {
/*  370 */         super.remove(paramArrayOfObject[i]);
/*      */       }
/*      */       else {
/*  373 */         super.put(paramArrayOfObject[i], localObject);
/*      */       }
/*      */     }
/*  376 */     firePropertyChange("UIDefaults", null, null);
/*      */   }
/*      */ 
/*      */   public Font getFont(Object paramObject)
/*      */   {
/*  389 */     Object localObject = get(paramObject);
/*  390 */     return (localObject instanceof Font) ? (Font)localObject : null;
/*      */   }
/*      */ 
/*      */   public Font getFont(Object paramObject, Locale paramLocale)
/*      */   {
/*  406 */     Object localObject = get(paramObject, paramLocale);
/*  407 */     return (localObject instanceof Font) ? (Font)localObject : null;
/*      */   }
/*      */ 
/*      */   public Color getColor(Object paramObject)
/*      */   {
/*  419 */     Object localObject = get(paramObject);
/*  420 */     return (localObject instanceof Color) ? (Color)localObject : null;
/*      */   }
/*      */ 
/*      */   public Color getColor(Object paramObject, Locale paramLocale)
/*      */   {
/*  436 */     Object localObject = get(paramObject, paramLocale);
/*  437 */     return (localObject instanceof Color) ? (Color)localObject : null;
/*      */   }
/*      */ 
/*      */   public Icon getIcon(Object paramObject)
/*      */   {
/*  450 */     Object localObject = get(paramObject);
/*  451 */     return (localObject instanceof Icon) ? (Icon)localObject : null;
/*      */   }
/*      */ 
/*      */   public Icon getIcon(Object paramObject, Locale paramLocale)
/*      */   {
/*  467 */     Object localObject = get(paramObject, paramLocale);
/*  468 */     return (localObject instanceof Icon) ? (Icon)localObject : null;
/*      */   }
/*      */ 
/*      */   public Border getBorder(Object paramObject)
/*      */   {
/*  481 */     Object localObject = get(paramObject);
/*  482 */     return (localObject instanceof Border) ? (Border)localObject : null;
/*      */   }
/*      */ 
/*      */   public Border getBorder(Object paramObject, Locale paramLocale)
/*      */   {
/*  498 */     Object localObject = get(paramObject, paramLocale);
/*  499 */     return (localObject instanceof Border) ? (Border)localObject : null;
/*      */   }
/*      */ 
/*      */   public String getString(Object paramObject)
/*      */   {
/*  512 */     Object localObject = get(paramObject);
/*  513 */     return (localObject instanceof String) ? (String)localObject : null;
/*      */   }
/*      */ 
/*      */   public String getString(Object paramObject, Locale paramLocale)
/*      */   {
/*  528 */     Object localObject = get(paramObject, paramLocale);
/*  529 */     return (localObject instanceof String) ? (String)localObject : null;
/*      */   }
/*      */ 
/*      */   public int getInt(Object paramObject)
/*      */   {
/*  540 */     Object localObject = get(paramObject);
/*  541 */     return (localObject instanceof Integer) ? ((Integer)localObject).intValue() : 0;
/*      */   }
/*      */ 
/*      */   public int getInt(Object paramObject, Locale paramLocale)
/*      */   {
/*  556 */     Object localObject = get(paramObject, paramLocale);
/*  557 */     return (localObject instanceof Integer) ? ((Integer)localObject).intValue() : 0;
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(Object paramObject)
/*      */   {
/*  571 */     Object localObject = get(paramObject);
/*  572 */     return (localObject instanceof Boolean) ? ((Boolean)localObject).booleanValue() : false;
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(Object paramObject, Locale paramLocale)
/*      */   {
/*  588 */     Object localObject = get(paramObject, paramLocale);
/*  589 */     return (localObject instanceof Boolean) ? ((Boolean)localObject).booleanValue() : false;
/*      */   }
/*      */ 
/*      */   public Insets getInsets(Object paramObject)
/*      */   {
/*  602 */     Object localObject = get(paramObject);
/*  603 */     return (localObject instanceof Insets) ? (Insets)localObject : null;
/*      */   }
/*      */ 
/*      */   public Insets getInsets(Object paramObject, Locale paramLocale)
/*      */   {
/*  619 */     Object localObject = get(paramObject, paramLocale);
/*  620 */     return (localObject instanceof Insets) ? (Insets)localObject : null;
/*      */   }
/*      */ 
/*      */   public Dimension getDimension(Object paramObject)
/*      */   {
/*  633 */     Object localObject = get(paramObject);
/*  634 */     return (localObject instanceof Dimension) ? (Dimension)localObject : null;
/*      */   }
/*      */ 
/*      */   public Dimension getDimension(Object paramObject, Locale paramLocale)
/*      */   {
/*  650 */     Object localObject = get(paramObject, paramLocale);
/*  651 */     return (localObject instanceof Dimension) ? (Dimension)localObject : null;
/*      */   }
/*      */ 
/*      */   public Class<? extends ComponentUI> getUIClass(String paramString, ClassLoader paramClassLoader)
/*      */   {
/*      */     try
/*      */     {
/*  679 */       String str = (String)get(paramString);
/*  680 */       if (str != null) {
/*  681 */         ReflectUtil.checkPackageAccess(str);
/*      */ 
/*  683 */         Class localClass = (Class)get(str);
/*  684 */         if (localClass == null) {
/*  685 */           if (paramClassLoader == null) {
/*  686 */             localClass = SwingUtilities.loadSystemClass(str);
/*      */           }
/*      */           else {
/*  689 */             localClass = paramClassLoader.loadClass(str);
/*      */           }
/*  691 */           if (localClass != null)
/*      */           {
/*  693 */             put(str, localClass);
/*      */           }
/*      */         }
/*  696 */         return localClass;
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/*  700 */       return null;
/*      */     }
/*      */     catch (ClassCastException localClassCastException) {
/*  703 */       return null;
/*      */     }
/*  705 */     return null;
/*      */   }
/*      */ 
/*      */   public Class<? extends ComponentUI> getUIClass(String paramString)
/*      */   {
/*  717 */     return getUIClass(paramString, null);
/*      */   }
/*      */ 
/*      */   protected void getUIError(String paramString)
/*      */   {
/*  730 */     System.err.println("UIDefaults.getUI() failed: " + paramString);
/*      */     try {
/*  732 */       throw new Error();
/*      */     }
/*      */     catch (Throwable localThrowable) {
/*  735 */       localThrowable.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   public ComponentUI getUI(JComponent paramJComponent)
/*      */   {
/*  755 */     Object localObject1 = get("ClassLoader");
/*  756 */     ClassLoader localClassLoader = localObject1 != null ? (ClassLoader)localObject1 : paramJComponent.getClass().getClassLoader();
/*      */ 
/*  758 */     Class localClass = getUIClass(paramJComponent.getUIClassID(), localClassLoader);
/*  759 */     Object localObject2 = null;
/*      */ 
/*  761 */     if (localClass == null)
/*  762 */       getUIError("no ComponentUI class for: " + paramJComponent);
/*      */     else {
/*      */       try
/*      */       {
/*  766 */         Method localMethod = (Method)get(localClass);
/*  767 */         if (localMethod == null) {
/*  768 */           localMethod = localClass.getMethod("createUI", new Class[] { JComponent.class });
/*  769 */           put(localClass, localMethod);
/*      */         }
/*  771 */         localObject2 = MethodUtil.invoke(localMethod, null, new Object[] { paramJComponent });
/*      */       }
/*      */       catch (NoSuchMethodException localNoSuchMethodException) {
/*  774 */         getUIError("static createUI() method not found in " + localClass);
/*      */       }
/*      */       catch (Exception localException) {
/*  777 */         getUIError("createUI() failed for " + paramJComponent + " " + localException);
/*      */       }
/*      */     }
/*      */ 
/*  781 */     return (ComponentUI)localObject2;
/*      */   }
/*      */ 
/*      */   public synchronized void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  795 */     if (this.changeSupport == null) {
/*  796 */       this.changeSupport = new SwingPropertyChangeSupport(this);
/*      */     }
/*  798 */     this.changeSupport.addPropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public synchronized void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/*  811 */     if (this.changeSupport != null)
/*  812 */       this.changeSupport.removePropertyChangeListener(paramPropertyChangeListener);
/*      */   }
/*      */ 
/*      */   public synchronized PropertyChangeListener[] getPropertyChangeListeners()
/*      */   {
/*  826 */     if (this.changeSupport == null) {
/*  827 */       return new PropertyChangeListener[0];
/*      */     }
/*  829 */     return this.changeSupport.getPropertyChangeListeners();
/*      */   }
/*      */ 
/*      */   protected void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*      */   {
/*  846 */     if (this.changeSupport != null)
/*  847 */       this.changeSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public synchronized void addResourceBundle(String paramString)
/*      */   {
/*  864 */     if (paramString == null) {
/*  865 */       return;
/*      */     }
/*  867 */     if (this.resourceBundles == null) {
/*  868 */       this.resourceBundles = new Vector(5);
/*      */     }
/*  870 */     if (!this.resourceBundles.contains(paramString)) {
/*  871 */       this.resourceBundles.add(paramString);
/*  872 */       this.resourceCache.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void removeResourceBundle(String paramString)
/*      */   {
/*  887 */     if (this.resourceBundles != null) {
/*  888 */       this.resourceBundles.remove(paramString);
/*      */     }
/*  890 */     this.resourceCache.clear();
/*      */   }
/*      */ 
/*      */   public void setDefaultLocale(Locale paramLocale)
/*      */   {
/*  908 */     this.defaultLocale = paramLocale;
/*      */   }
/*      */ 
/*      */   public Locale getDefaultLocale()
/*      */   {
/*  926 */     return this.defaultLocale;
/*      */   }
/*      */ 
/*      */   public static abstract interface ActiveValue
/*      */   {
/*      */     public abstract Object createValue(UIDefaults paramUIDefaults);
/*      */   }
/*      */ 
/*      */   public static class LazyInputMap
/*      */     implements UIDefaults.LazyValue
/*      */   {
/*      */     private Object[] bindings;
/*      */ 
/*      */     public LazyInputMap(Object[] paramArrayOfObject)
/*      */     {
/* 1196 */       this.bindings = paramArrayOfObject;
/*      */     }
/*      */ 
/*      */     public Object createValue(UIDefaults paramUIDefaults)
/*      */     {
/* 1207 */       if (this.bindings != null) {
/* 1208 */         InputMap localInputMap = LookAndFeel.makeInputMap(this.bindings);
/* 1209 */         return localInputMap;
/*      */       }
/* 1211 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract interface LazyValue
/*      */   {
/*      */     public abstract Object createValue(UIDefaults paramUIDefaults);
/*      */   }
/*      */ 
/*      */   public static class ProxyLazyValue
/*      */     implements UIDefaults.LazyValue
/*      */   {
/*      */     private AccessControlContext acc;
/*      */     private String className;
/*      */     private String methodName;
/*      */     private Object[] args;
/*      */ 
/*      */     public ProxyLazyValue(String paramString)
/*      */     {
/* 1024 */       this(paramString, (String)null);
/*      */     }
/*      */ 
/*      */     public ProxyLazyValue(String paramString1, String paramString2)
/*      */     {
/* 1038 */       this(paramString1, paramString2, null);
/*      */     }
/*      */ 
/*      */     public ProxyLazyValue(String paramString, Object[] paramArrayOfObject)
/*      */     {
/* 1050 */       this(paramString, null, paramArrayOfObject);
/*      */     }
/*      */ 
/*      */     public ProxyLazyValue(String paramString1, String paramString2, Object[] paramArrayOfObject)
/*      */     {
/* 1066 */       this.acc = AccessController.getContext();
/* 1067 */       this.className = paramString1;
/* 1068 */       this.methodName = paramString2;
/* 1069 */       if (paramArrayOfObject != null)
/* 1070 */         this.args = ((Object[])paramArrayOfObject.clone());
/*      */     }
/*      */ 
/*      */     public Object createValue(final UIDefaults paramUIDefaults)
/*      */     {
/* 1085 */       if ((this.acc == null) && (System.getSecurityManager() != null)) {
/* 1086 */         throw new SecurityException("null AccessControlContext");
/*      */       }
/* 1088 */       return AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run()
/*      */         {
/*      */           try
/*      */           {
/*      */             Object localObject1;
/* 1094 */             if ((paramUIDefaults == null) || (!((localObject1 = paramUIDefaults.get("ClassLoader")) instanceof ClassLoader)))
/*      */             {
/* 1096 */               localObject1 = Thread.currentThread().getContextClassLoader();
/*      */ 
/* 1098 */               if (localObject1 == null)
/*      */               {
/* 1100 */                 localObject1 = ClassLoader.getSystemClassLoader();
/*      */               }
/*      */             }
/* 1103 */             ReflectUtil.checkPackageAccess(UIDefaults.ProxyLazyValue.this.className);
/* 1104 */             Class localClass = Class.forName(UIDefaults.ProxyLazyValue.this.className, true, (ClassLoader)localObject1);
/* 1105 */             SwingUtilities2.checkAccess(localClass.getModifiers());
/* 1106 */             if (UIDefaults.ProxyLazyValue.this.methodName != null) {
/* 1107 */               arrayOfClass = UIDefaults.ProxyLazyValue.this.getClassArray(UIDefaults.ProxyLazyValue.this.args);
/* 1108 */               localObject2 = localClass.getMethod(UIDefaults.ProxyLazyValue.this.methodName, arrayOfClass);
/* 1109 */               return MethodUtil.invoke((Method)localObject2, localClass, UIDefaults.ProxyLazyValue.this.args);
/*      */             }
/* 1111 */             Class[] arrayOfClass = UIDefaults.ProxyLazyValue.this.getClassArray(UIDefaults.ProxyLazyValue.this.args);
/* 1112 */             Object localObject2 = localClass.getConstructor(arrayOfClass);
/* 1113 */             SwingUtilities2.checkAccess(((Constructor)localObject2).getModifiers());
/* 1114 */             return ((Constructor)localObject2).newInstance(UIDefaults.ProxyLazyValue.this.args);
/*      */           }
/*      */           catch (Exception localException)
/*      */           {
/*      */           }
/*      */ 
/* 1123 */           return null;
/*      */         }
/*      */       }
/*      */       , this.acc);
/*      */     }
/*      */ 
/*      */     private Class[] getClassArray(Object[] paramArrayOfObject)
/*      */     {
/* 1136 */       Class[] arrayOfClass = null;
/* 1137 */       if (paramArrayOfObject != null) {
/* 1138 */         arrayOfClass = new Class[paramArrayOfObject.length];
/* 1139 */         for (int i = 0; i < paramArrayOfObject.length; i++)
/*      */         {
/* 1143 */           if ((paramArrayOfObject[i] instanceof Integer))
/* 1144 */             arrayOfClass[i] = Integer.TYPE;
/* 1145 */           else if ((paramArrayOfObject[i] instanceof Boolean))
/* 1146 */             arrayOfClass[i] = Boolean.TYPE;
/* 1147 */           else if ((paramArrayOfObject[i] instanceof ColorUIResource))
/*      */           {
/* 1156 */             arrayOfClass[i] = Color.class;
/*      */           }
/* 1158 */           else arrayOfClass[i] = paramArrayOfObject[i].getClass();
/*      */         }
/*      */       }
/*      */ 
/* 1162 */       return arrayOfClass;
/*      */     }
/*      */ 
/*      */     private String printArgs(Object[] paramArrayOfObject) {
/* 1166 */       String str = "{";
/* 1167 */       if (paramArrayOfObject != null) {
/* 1168 */         for (int i = 0; i < paramArrayOfObject.length - 1; i++) {
/* 1169 */           str = str.concat(paramArrayOfObject[i] + ",");
/*      */         }
/* 1171 */         str = str.concat(paramArrayOfObject[(paramArrayOfObject.length - 1)] + "}");
/*      */       } else {
/* 1173 */         str = str.concat("}");
/*      */       }
/* 1175 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class TextAndMnemonicHashMap extends HashMap<String, Object>
/*      */   {
/*      */     static final String AND_MNEMONIC = "AndMnemonic";
/*      */     static final String TITLE_SUFFIX = ".titleAndMnemonic";
/*      */     static final String TEXT_SUFFIX = ".textAndMnemonic";
/*      */ 
/*      */     public Object get(Object paramObject)
/*      */     {
/* 1246 */       Object localObject = super.get(paramObject);
/*      */ 
/* 1248 */       if (localObject == null)
/*      */       {
/* 1250 */         int i = 0;
/*      */ 
/* 1252 */         String str1 = paramObject.toString();
/* 1253 */         String str2 = null;
/*      */ 
/* 1255 */         if (str1.endsWith("AndMnemonic")) {
/* 1256 */           return null;
/*      */         }
/*      */ 
/* 1259 */         if (str1.endsWith(".mnemonic")) {
/* 1260 */           str2 = composeKey(str1, 9, ".textAndMnemonic");
/* 1261 */         } else if (str1.endsWith("NameMnemonic")) {
/* 1262 */           str2 = composeKey(str1, 12, ".textAndMnemonic");
/* 1263 */         } else if (str1.endsWith("Mnemonic")) {
/* 1264 */           str2 = composeKey(str1, 8, ".textAndMnemonic");
/* 1265 */           i = 1;
/*      */         }
/*      */ 
/* 1268 */         if (str2 != null) {
/* 1269 */           localObject = super.get(str2);
/* 1270 */           if ((localObject == null) && (i != 0)) {
/* 1271 */             str2 = composeKey(str1, 8, ".titleAndMnemonic");
/* 1272 */             localObject = super.get(str2);
/*      */           }
/*      */ 
/* 1275 */           return localObject == null ? null : getMnemonicFromProperty(localObject.toString());
/*      */         }
/*      */ 
/* 1278 */         if (str1.endsWith("NameText"))
/* 1279 */           str2 = composeKey(str1, 8, ".textAndMnemonic");
/* 1280 */         else if (str1.endsWith(".nameText"))
/* 1281 */           str2 = composeKey(str1, 9, ".textAndMnemonic");
/* 1282 */         else if (str1.endsWith("Text"))
/* 1283 */           str2 = composeKey(str1, 4, ".textAndMnemonic");
/* 1284 */         else if (str1.endsWith("Title")) {
/* 1285 */           str2 = composeKey(str1, 5, ".titleAndMnemonic");
/*      */         }
/*      */ 
/* 1288 */         if (str2 != null) {
/* 1289 */           localObject = super.get(str2);
/* 1290 */           return localObject == null ? null : getTextFromProperty(localObject.toString());
/*      */         }
/*      */ 
/* 1293 */         if (str1.endsWith("DisplayedMnemonicIndex")) {
/* 1294 */           str2 = composeKey(str1, 22, ".textAndMnemonic");
/* 1295 */           localObject = super.get(str2);
/* 1296 */           if (localObject == null) {
/* 1297 */             str2 = composeKey(str1, 22, ".titleAndMnemonic");
/* 1298 */             localObject = super.get(str2);
/*      */           }
/* 1300 */           return localObject == null ? null : getIndexFromProperty(localObject.toString());
/*      */         }
/*      */       }
/*      */ 
/* 1304 */       return localObject;
/*      */     }
/*      */ 
/*      */     String composeKey(String paramString1, int paramInt, String paramString2) {
/* 1308 */       return paramString1.substring(0, paramString1.length() - paramInt) + paramString2;
/*      */     }
/*      */ 
/*      */     String getTextFromProperty(String paramString) {
/* 1312 */       return paramString.replace("&", "");
/*      */     }
/*      */ 
/*      */     String getMnemonicFromProperty(String paramString) {
/* 1316 */       int i = paramString.indexOf('&');
/* 1317 */       if ((0 <= i) && (i < paramString.length() - 1)) {
/* 1318 */         char c = paramString.charAt(i + 1);
/* 1319 */         return Integer.toString(Character.toUpperCase(c));
/*      */       }
/* 1321 */       return null;
/*      */     }
/*      */ 
/*      */     String getIndexFromProperty(String paramString) {
/* 1325 */       int i = paramString.indexOf('&');
/* 1326 */       return i == -1 ? null : Integer.toString(i);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.UIDefaults
 * JD-Core Version:    0.6.2
 */
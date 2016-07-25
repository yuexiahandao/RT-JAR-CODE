/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Insets;
/*      */ import java.awt.KeyEventPostProcessor;
/*      */ import java.awt.KeyboardFocusManager;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.event.KeyEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.event.SwingPropertyChangeSupport;
/*      */ import javax.swing.plaf.ComponentUI;
/*      */ import javax.swing.plaf.metal.MetalLookAndFeel;
/*      */ import sun.awt.AWTAccessor;
/*      */ import sun.awt.AWTAccessor.ComponentAccessor;
/*      */ import sun.awt.AppContext;
/*      */ import sun.awt.OSInfo;
/*      */ import sun.awt.OSInfo.OSType;
/*      */ import sun.awt.PaintEventDispatcher;
/*      */ import sun.awt.SunToolkit;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ import sun.swing.DefaultLookup;
/*      */ import sun.swing.SwingUtilities2;
/*      */ 
/*      */ public class UIManager
/*      */   implements Serializable
/*      */ {
/*  231 */   private static final Object classLock = new Object();
/*      */   private static final String defaultLAFKey = "swing.defaultlaf";
/*      */   private static final String auxiliaryLAFsKey = "swing.auxiliarylaf";
/*      */   private static final String multiplexingLAFKey = "swing.plaf.multiplexinglaf";
/*      */   private static final String installedLAFsKey = "swing.installedlafs";
/*      */   private static final String disableMnemonicKey = "swing.disablenavaids";
/*  391 */   private static LookAndFeelInfo[] installedLAFs = (LookAndFeelInfo[])localArrayList.toArray(new LookAndFeelInfo[localArrayList.size()]);
/*      */ 
/*      */   private static LAFState getLAFState()
/*      */   {
/*  242 */     LAFState localLAFState = (LAFState)SwingUtilities.appContextGet(SwingUtilities2.LAF_STATE_KEY);
/*      */ 
/*  244 */     if (localLAFState == null) {
/*  245 */       synchronized (classLock) {
/*  246 */         localLAFState = (LAFState)SwingUtilities.appContextGet(SwingUtilities2.LAF_STATE_KEY);
/*      */ 
/*  248 */         if (localLAFState == null) {
/*  249 */           SwingUtilities.appContextPut(SwingUtilities2.LAF_STATE_KEY, localLAFState = new LAFState(null));
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  255 */     return localLAFState;
/*      */   }
/*      */ 
/*      */   private static String makeInstalledLAFKey(String paramString1, String paramString2)
/*      */   {
/*  275 */     return "swing.installedlaf." + paramString1 + "." + paramString2;
/*      */   }
/*      */ 
/*      */   private static String makeSwingPropertiesFilename()
/*      */   {
/*  284 */     String str1 = File.separator;
/*      */ 
/*  287 */     String str2 = System.getProperty("java.home");
/*  288 */     if (str2 == null) {
/*  289 */       str2 = "<java.home undefined>";
/*      */     }
/*  291 */     return str2 + str1 + "lib" + str1 + "swing.properties";
/*      */   }
/*      */ 
/*      */   public static LookAndFeelInfo[] getInstalledLookAndFeels()
/*      */   {
/*  416 */     maybeInitialize();
/*  417 */     LookAndFeelInfo[] arrayOfLookAndFeelInfo1 = getLAFState().installedLAFs;
/*  418 */     if (arrayOfLookAndFeelInfo1 == null) {
/*  419 */       arrayOfLookAndFeelInfo1 = installedLAFs;
/*      */     }
/*  421 */     LookAndFeelInfo[] arrayOfLookAndFeelInfo2 = new LookAndFeelInfo[arrayOfLookAndFeelInfo1.length];
/*  422 */     System.arraycopy(arrayOfLookAndFeelInfo1, 0, arrayOfLookAndFeelInfo2, 0, arrayOfLookAndFeelInfo1.length);
/*  423 */     return arrayOfLookAndFeelInfo2;
/*      */   }
/*      */ 
/*      */   public static void setInstalledLookAndFeels(LookAndFeelInfo[] paramArrayOfLookAndFeelInfo)
/*      */     throws SecurityException
/*      */   {
/*  442 */     maybeInitialize();
/*  443 */     LookAndFeelInfo[] arrayOfLookAndFeelInfo = new LookAndFeelInfo[paramArrayOfLookAndFeelInfo.length];
/*  444 */     System.arraycopy(paramArrayOfLookAndFeelInfo, 0, arrayOfLookAndFeelInfo, 0, paramArrayOfLookAndFeelInfo.length);
/*  445 */     getLAFState().installedLAFs = arrayOfLookAndFeelInfo;
/*      */   }
/*      */ 
/*      */   public static void installLookAndFeel(LookAndFeelInfo paramLookAndFeelInfo)
/*      */   {
/*  459 */     LookAndFeelInfo[] arrayOfLookAndFeelInfo1 = getInstalledLookAndFeels();
/*  460 */     LookAndFeelInfo[] arrayOfLookAndFeelInfo2 = new LookAndFeelInfo[arrayOfLookAndFeelInfo1.length + 1];
/*  461 */     System.arraycopy(arrayOfLookAndFeelInfo1, 0, arrayOfLookAndFeelInfo2, 0, arrayOfLookAndFeelInfo1.length);
/*  462 */     arrayOfLookAndFeelInfo2[arrayOfLookAndFeelInfo1.length] = paramLookAndFeelInfo;
/*  463 */     setInstalledLookAndFeels(arrayOfLookAndFeelInfo2);
/*      */   }
/*      */ 
/*      */   public static void installLookAndFeel(String paramString1, String paramString2)
/*      */   {
/*  478 */     installLookAndFeel(new LookAndFeelInfo(paramString1, paramString2));
/*      */   }
/*      */ 
/*      */   public static LookAndFeel getLookAndFeel()
/*      */   {
/*  489 */     maybeInitialize();
/*  490 */     return getLAFState().lookAndFeel;
/*      */   }
/*      */ 
/*      */   public static void setLookAndFeel(LookAndFeel paramLookAndFeel)
/*      */     throws UnsupportedLookAndFeelException
/*      */   {
/*  521 */     if ((paramLookAndFeel != null) && (!paramLookAndFeel.isSupportedLookAndFeel())) {
/*  522 */       localObject = paramLookAndFeel.toString() + " not supported on this platform";
/*  523 */       throw new UnsupportedLookAndFeelException((String)localObject);
/*      */     }
/*      */ 
/*  526 */     Object localObject = getLAFState();
/*  527 */     LookAndFeel localLookAndFeel = ((LAFState)localObject).lookAndFeel;
/*  528 */     if (localLookAndFeel != null) {
/*  529 */       localLookAndFeel.uninitialize();
/*      */     }
/*      */ 
/*  532 */     ((LAFState)localObject).lookAndFeel = paramLookAndFeel;
/*  533 */     if (paramLookAndFeel != null) {
/*  534 */       DefaultLookup.setDefaultLookup(null);
/*  535 */       paramLookAndFeel.initialize();
/*  536 */       ((LAFState)localObject).setLookAndFeelDefaults(paramLookAndFeel.getDefaults());
/*      */     }
/*      */     else {
/*  539 */       ((LAFState)localObject).setLookAndFeelDefaults(null);
/*      */     }
/*      */ 
/*  542 */     SwingPropertyChangeSupport localSwingPropertyChangeSupport = ((LAFState)localObject).getPropertyChangeSupport(false);
/*      */ 
/*  544 */     if (localSwingPropertyChangeSupport != null)
/*  545 */       localSwingPropertyChangeSupport.firePropertyChange("lookAndFeel", localLookAndFeel, paramLookAndFeel);
/*      */   }
/*      */ 
/*      */   public static void setLookAndFeel(String paramString)
/*      */     throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
/*      */   {
/*  574 */     if ("javax.swing.plaf.metal.MetalLookAndFeel".equals(paramString))
/*      */     {
/*  576 */       setLookAndFeel(new MetalLookAndFeel());
/*      */     }
/*      */     else {
/*  579 */       Class localClass = SwingUtilities.loadSystemClass(paramString);
/*  580 */       setLookAndFeel((LookAndFeel)localClass.newInstance());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static String getSystemLookAndFeelClassName()
/*      */   {
/*  598 */     String str1 = (String)AccessController.doPrivileged(new GetPropertyAction("swing.systemlaf"));
/*      */ 
/*  600 */     if (str1 != null) {
/*  601 */       return str1;
/*      */     }
/*  603 */     OSInfo.OSType localOSType = (OSInfo.OSType)AccessController.doPrivileged(OSInfo.getOSTypeAction());
/*  604 */     if (localOSType == OSInfo.OSType.WINDOWS) {
/*  605 */       return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
/*      */     }
/*  607 */     String str2 = (String)AccessController.doPrivileged(new GetPropertyAction("sun.desktop"));
/*  608 */     Toolkit localToolkit = Toolkit.getDefaultToolkit();
/*  609 */     if (("gnome".equals(str2)) && ((localToolkit instanceof SunToolkit)) && (((SunToolkit)localToolkit).isNativeGTKAvailable()))
/*      */     {
/*  613 */       return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
/*      */     }
/*  615 */     if ((localOSType == OSInfo.OSType.MACOSX) && 
/*  616 */       (localToolkit.getClass().getName().equals("sun.lwawt.macosx.LWCToolkit")))
/*      */     {
/*  618 */       return "com.apple.laf.AquaLookAndFeel";
/*      */     }
/*      */ 
/*  621 */     if (localOSType == OSInfo.OSType.SOLARIS) {
/*  622 */       return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
/*      */     }
/*      */ 
/*  625 */     return getCrossPlatformLookAndFeelClassName();
/*      */   }
/*      */ 
/*      */   public static String getCrossPlatformLookAndFeelClassName()
/*      */   {
/*  640 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("swing.crossplatformlaf"));
/*      */ 
/*  642 */     if (str != null) {
/*  643 */       return str;
/*      */     }
/*  645 */     return "javax.swing.plaf.metal.MetalLookAndFeel";
/*      */   }
/*      */ 
/*      */   public static UIDefaults getDefaults()
/*      */   {
/*  656 */     maybeInitialize();
/*  657 */     return getLAFState().multiUIDefaults;
/*      */   }
/*      */ 
/*      */   public static Font getFont(Object paramObject)
/*      */   {
/*  669 */     return getDefaults().getFont(paramObject);
/*      */   }
/*      */ 
/*      */   public static Font getFont(Object paramObject, Locale paramLocale)
/*      */   {
/*  686 */     return getDefaults().getFont(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Color getColor(Object paramObject)
/*      */   {
/*  698 */     return getDefaults().getColor(paramObject);
/*      */   }
/*      */ 
/*      */   public static Color getColor(Object paramObject, Locale paramLocale)
/*      */   {
/*  715 */     return getDefaults().getColor(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Icon getIcon(Object paramObject)
/*      */   {
/*  727 */     return getDefaults().getIcon(paramObject);
/*      */   }
/*      */ 
/*      */   public static Icon getIcon(Object paramObject, Locale paramLocale)
/*      */   {
/*  744 */     return getDefaults().getIcon(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Border getBorder(Object paramObject)
/*      */   {
/*  756 */     return getDefaults().getBorder(paramObject);
/*      */   }
/*      */ 
/*      */   public static Border getBorder(Object paramObject, Locale paramLocale)
/*      */   {
/*  773 */     return getDefaults().getBorder(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static String getString(Object paramObject)
/*      */   {
/*  785 */     return getDefaults().getString(paramObject);
/*      */   }
/*      */ 
/*      */   public static String getString(Object paramObject, Locale paramLocale)
/*      */   {
/*  802 */     return getDefaults().getString(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   static String getString(Object paramObject, Component paramComponent)
/*      */   {
/*  818 */     Locale localLocale = paramComponent == null ? Locale.getDefault() : paramComponent.getLocale();
/*  819 */     return getString(paramObject, localLocale);
/*      */   }
/*      */ 
/*      */   public static int getInt(Object paramObject)
/*      */   {
/*  832 */     return getDefaults().getInt(paramObject);
/*      */   }
/*      */ 
/*      */   public static int getInt(Object paramObject, Locale paramLocale)
/*      */   {
/*  850 */     return getDefaults().getInt(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static boolean getBoolean(Object paramObject)
/*      */   {
/*  864 */     return getDefaults().getBoolean(paramObject);
/*      */   }
/*      */ 
/*      */   public static boolean getBoolean(Object paramObject, Locale paramLocale)
/*      */   {
/*  883 */     return getDefaults().getBoolean(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Insets getInsets(Object paramObject)
/*      */   {
/*  895 */     return getDefaults().getInsets(paramObject);
/*      */   }
/*      */ 
/*      */   public static Insets getInsets(Object paramObject, Locale paramLocale)
/*      */   {
/*  912 */     return getDefaults().getInsets(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Dimension getDimension(Object paramObject)
/*      */   {
/*  924 */     return getDefaults().getDimension(paramObject);
/*      */   }
/*      */ 
/*      */   public static Dimension getDimension(Object paramObject, Locale paramLocale)
/*      */   {
/*  941 */     return getDefaults().getDimension(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Object get(Object paramObject)
/*      */   {
/*  952 */     return getDefaults().get(paramObject);
/*      */   }
/*      */ 
/*      */   public static Object get(Object paramObject, Locale paramLocale)
/*      */   {
/*  968 */     return getDefaults().get(paramObject, paramLocale);
/*      */   }
/*      */ 
/*      */   public static Object put(Object paramObject1, Object paramObject2)
/*      */   {
/*  985 */     return getDefaults().put(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public static ComponentUI getUI(JComponent paramJComponent)
/*      */   {
/* 1003 */     maybeInitialize();
/* 1004 */     maybeInitializeFocusPolicy(paramJComponent);
/* 1005 */     ComponentUI localComponentUI = null;
/* 1006 */     LookAndFeel localLookAndFeel = getLAFState().multiLookAndFeel;
/* 1007 */     if (localLookAndFeel != null)
/*      */     {
/* 1010 */       localComponentUI = localLookAndFeel.getDefaults().getUI(paramJComponent);
/*      */     }
/* 1012 */     if (localComponentUI == null) {
/* 1013 */       localComponentUI = getDefaults().getUI(paramJComponent);
/*      */     }
/* 1015 */     return localComponentUI;
/*      */   }
/*      */ 
/*      */   public static UIDefaults getLookAndFeelDefaults()
/*      */   {
/* 1034 */     maybeInitialize();
/* 1035 */     return getLAFState().getLookAndFeelDefaults();
/*      */   }
/*      */ 
/*      */   private static LookAndFeel getMultiLookAndFeel()
/*      */   {
/* 1042 */     LookAndFeel localLookAndFeel = getLAFState().multiLookAndFeel;
/* 1043 */     if (localLookAndFeel == null) {
/* 1044 */       String str1 = "javax.swing.plaf.multi.MultiLookAndFeel";
/* 1045 */       String str2 = getLAFState().swingProps.getProperty("swing.plaf.multiplexinglaf", str1);
/*      */       try {
/* 1047 */         Class localClass = SwingUtilities.loadSystemClass(str2);
/* 1048 */         localLookAndFeel = (LookAndFeel)localClass.newInstance();
/*      */       } catch (Exception localException) {
/* 1050 */         System.err.println("UIManager: failed loading " + str2);
/*      */       }
/*      */     }
/* 1053 */     return localLookAndFeel;
/*      */   }
/*      */ 
/*      */   public static void addAuxiliaryLookAndFeel(LookAndFeel paramLookAndFeel)
/*      */   {
/* 1073 */     maybeInitialize();
/*      */ 
/* 1075 */     if (!paramLookAndFeel.isSupportedLookAndFeel())
/*      */     {
/* 1078 */       return;
/*      */     }
/* 1080 */     Vector localVector = getLAFState().auxLookAndFeels;
/* 1081 */     if (localVector == null) {
/* 1082 */       localVector = new Vector();
/*      */     }
/*      */ 
/* 1085 */     if (!localVector.contains(paramLookAndFeel)) {
/* 1086 */       localVector.addElement(paramLookAndFeel);
/* 1087 */       paramLookAndFeel.initialize();
/* 1088 */       getLAFState().auxLookAndFeels = localVector;
/*      */ 
/* 1090 */       if (getLAFState().multiLookAndFeel == null)
/* 1091 */         getLAFState().multiLookAndFeel = getMultiLookAndFeel();
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean removeAuxiliaryLookAndFeel(LookAndFeel paramLookAndFeel)
/*      */   {
/* 1112 */     maybeInitialize();
/*      */ 
/* 1116 */     Vector localVector = getLAFState().auxLookAndFeels;
/* 1117 */     if ((localVector == null) || (localVector.size() == 0)) {
/* 1118 */       return false;
/*      */     }
/*      */ 
/* 1121 */     boolean bool = localVector.removeElement(paramLookAndFeel);
/* 1122 */     if (bool) {
/* 1123 */       if (localVector.size() == 0) {
/* 1124 */         getLAFState().auxLookAndFeels = null;
/* 1125 */         getLAFState().multiLookAndFeel = null;
/*      */       } else {
/* 1127 */         getLAFState().auxLookAndFeels = localVector;
/*      */       }
/*      */     }
/* 1130 */     paramLookAndFeel.uninitialize();
/*      */ 
/* 1132 */     return bool;
/*      */   }
/*      */ 
/*      */   public static LookAndFeel[] getAuxiliaryLookAndFeels()
/*      */   {
/* 1150 */     maybeInitialize();
/*      */ 
/* 1152 */     Vector localVector = getLAFState().auxLookAndFeels;
/* 1153 */     if ((localVector == null) || (localVector.size() == 0)) {
/* 1154 */       return null;
/*      */     }
/*      */ 
/* 1157 */     LookAndFeel[] arrayOfLookAndFeel = new LookAndFeel[localVector.size()];
/* 1158 */     for (int i = 0; i < arrayOfLookAndFeel.length; i++) {
/* 1159 */       arrayOfLookAndFeel[i] = ((LookAndFeel)localVector.elementAt(i));
/*      */     }
/* 1161 */     return arrayOfLookAndFeel;
/*      */   }
/*      */ 
/*      */   public static void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1175 */     synchronized (classLock) {
/* 1176 */       getLAFState().getPropertyChangeSupport(true).addPropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*      */   {
/* 1192 */     synchronized (classLock) {
/* 1193 */       getLAFState().getPropertyChangeSupport(true).removePropertyChangeListener(paramPropertyChangeListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static PropertyChangeListener[] getPropertyChangeListeners()
/*      */   {
/* 1208 */     synchronized (classLock) {
/* 1209 */       return getLAFState().getPropertyChangeSupport(true).getPropertyChangeListeners();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Properties loadSwingProperties()
/*      */   {
/* 1219 */     if (UIManager.class.getClassLoader() != null) {
/* 1220 */       return new Properties();
/*      */     }
/*      */ 
/* 1223 */     Properties localProperties = new Properties();
/*      */ 
/* 1225 */     AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/* 1228 */         OSInfo.OSType localOSType = (OSInfo.OSType)AccessController.doPrivileged(OSInfo.getOSTypeAction());
/* 1229 */         if (localOSType == OSInfo.OSType.MACOSX) {
/* 1230 */           this.val$props.put("swing.defaultlaf", UIManager.getSystemLookAndFeelClassName());
/*      */         }
/*      */         try
/*      */         {
/* 1234 */           File localFile = new File(UIManager.access$100());
/*      */ 
/* 1236 */           if (localFile.exists())
/*      */           {
/* 1239 */             FileInputStream localFileInputStream = new FileInputStream(localFile);
/* 1240 */             this.val$props.load(localFileInputStream);
/* 1241 */             localFileInputStream.close();
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*      */         }
/*      */ 
/* 1250 */         UIManager.checkProperty(this.val$props, "swing.defaultlaf");
/* 1251 */         UIManager.checkProperty(this.val$props, "swing.auxiliarylaf");
/* 1252 */         UIManager.checkProperty(this.val$props, "swing.plaf.multiplexinglaf");
/* 1253 */         UIManager.checkProperty(this.val$props, "swing.installedlafs");
/* 1254 */         UIManager.checkProperty(this.val$props, "swing.disablenavaids");
/*      */ 
/* 1256 */         return null;
/*      */       }
/*      */     });
/* 1259 */     return localProperties;
/*      */   }
/*      */ 
/*      */   private static void checkProperty(Properties paramProperties, String paramString)
/*      */   {
/* 1266 */     String str = System.getProperty(paramString);
/* 1267 */     if (str != null)
/* 1268 */       paramProperties.put(paramString, str);
/*      */   }
/*      */ 
/*      */   private static void initializeInstalledLAFs(Properties paramProperties)
/*      */   {
/* 1281 */     String str1 = paramProperties.getProperty("swing.installedlafs");
/* 1282 */     if (str1 == null) {
/* 1283 */       return;
/*      */     }
/*      */ 
/* 1290 */     Vector localVector1 = new Vector();
/* 1291 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",", false);
/* 1292 */     while (localStringTokenizer.hasMoreTokens()) {
/* 1293 */       localVector1.addElement(localStringTokenizer.nextToken());
/*      */     }
/*      */ 
/* 1300 */     Vector localVector2 = new Vector(localVector1.size());
/* 1301 */     for (Object localObject = localVector1.iterator(); ((Iterator)localObject).hasNext(); ) { String str2 = (String)((Iterator)localObject).next();
/* 1302 */       String str3 = paramProperties.getProperty(makeInstalledLAFKey(str2, "name"), str2);
/* 1303 */       String str4 = paramProperties.getProperty(makeInstalledLAFKey(str2, "class"));
/* 1304 */       if (str4 != null) {
/* 1305 */         localVector2.addElement(new LookAndFeelInfo(str3, str4));
/*      */       }
/*      */     }
/*      */ 
/* 1309 */     localObject = new LookAndFeelInfo[localVector2.size()];
/* 1310 */     for (int i = 0; i < localVector2.size(); i++) {
/* 1311 */       localObject[i] = ((LookAndFeelInfo)localVector2.elementAt(i));
/*      */     }
/* 1313 */     getLAFState().installedLAFs = ((LookAndFeelInfo[])localObject);
/*      */   }
/*      */ 
/*      */   private static void initializeDefaultLAF(Properties paramProperties)
/*      */   {
/* 1327 */     if (getLAFState().lookAndFeel != null) {
/* 1328 */       return;
/*      */     }
/*      */ 
/* 1333 */     String str = null;
/* 1334 */     HashMap localHashMap = (HashMap)AppContext.getAppContext().remove("swing.lafdata");
/*      */ 
/* 1336 */     if (localHashMap != null) {
/* 1337 */       str = (String)localHashMap.remove("defaultlaf");
/*      */     }
/* 1339 */     if (str == null) {
/* 1340 */       str = getCrossPlatformLookAndFeelClassName();
/*      */     }
/* 1342 */     str = paramProperties.getProperty("swing.defaultlaf", str);
/*      */     try
/*      */     {
/* 1345 */       setLookAndFeel(str);
/*      */     } catch (Exception localException) {
/* 1347 */       throw new Error("Cannot load " + str);
/*      */     }
/*      */     Iterator localIterator;
/* 1351 */     if (localHashMap != null)
/* 1352 */       for (localIterator = localHashMap.keySet().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1353 */         put(localObject, localHashMap.get(localObject));
/*      */       }
/*      */   }
/*      */ 
/*      */   private static void initializeAuxiliaryLAFs(Properties paramProperties)
/*      */   {
/* 1361 */     String str1 = paramProperties.getProperty("swing.auxiliarylaf");
/* 1362 */     if (str1 == null) {
/* 1363 */       return;
/*      */     }
/*      */ 
/* 1366 */     Vector localVector = new Vector();
/*      */ 
/* 1368 */     StringTokenizer localStringTokenizer = new StringTokenizer(str1, ",");
/*      */ 
/* 1374 */     while (localStringTokenizer.hasMoreTokens()) {
/* 1375 */       String str2 = localStringTokenizer.nextToken();
/*      */       try {
/* 1377 */         Class localClass = SwingUtilities.loadSystemClass(str2);
/* 1378 */         LookAndFeel localLookAndFeel = (LookAndFeel)localClass.newInstance();
/* 1379 */         localLookAndFeel.initialize();
/* 1380 */         localVector.addElement(localLookAndFeel);
/*      */       }
/*      */       catch (Exception localException) {
/* 1383 */         System.err.println("UIManager: failed loading auxiliary look and feel " + str2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1392 */     if (localVector.size() == 0) {
/* 1393 */       localVector = null;
/*      */     }
/*      */     else {
/* 1396 */       getLAFState().multiLookAndFeel = getMultiLookAndFeel();
/* 1397 */       if (getLAFState().multiLookAndFeel == null) {
/* 1398 */         localVector = null;
/*      */       }
/*      */     }
/*      */ 
/* 1402 */     getLAFState().auxLookAndFeels = localVector;
/*      */   }
/*      */ 
/*      */   private static void initializeSystemDefaults(Properties paramProperties)
/*      */   {
/* 1407 */     getLAFState().swingProps = paramProperties;
/*      */   }
/*      */ 
/*      */   private static void maybeInitialize()
/*      */   {
/* 1419 */     synchronized (classLock) {
/* 1420 */       if (!getLAFState().initialized) {
/* 1421 */         getLAFState().initialized = true;
/* 1422 */         initialize();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void maybeInitializeFocusPolicy(JComponent paramJComponent)
/*      */   {
/* 1434 */     if ((paramJComponent instanceof JRootPane))
/* 1435 */       synchronized (classLock) {
/* 1436 */         if (!getLAFState().focusPolicyInitialized) {
/* 1437 */           getLAFState().focusPolicyInitialized = true;
/*      */ 
/* 1439 */           if (FocusManager.isFocusManagerEnabled())
/* 1440 */             KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private static void initialize()
/*      */   {
/* 1453 */     Properties localProperties = loadSwingProperties();
/* 1454 */     initializeSystemDefaults(localProperties);
/* 1455 */     initializeDefaultLAF(localProperties);
/* 1456 */     initializeAuxiliaryLAFs(localProperties);
/* 1457 */     initializeInstalledLAFs(localProperties);
/*      */ 
/* 1460 */     if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
/* 1461 */       PaintEventDispatcher.setPaintEventDispatcher(new SwingPaintEventDispatcher());
/*      */     }
/*      */ 
/* 1471 */     KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor()
/*      */     {
/*      */       public boolean postProcessKeyEvent(KeyEvent paramAnonymousKeyEvent) {
/* 1474 */         Component localComponent = paramAnonymousKeyEvent.getComponent();
/*      */ 
/* 1476 */         if (((!(localComponent instanceof JComponent)) || ((localComponent != null) && (!localComponent.isEnabled()))) && (JComponent.KeyboardState.shouldProcess(paramAnonymousKeyEvent)) && (SwingUtilities.processKeyBindings(paramAnonymousKeyEvent)))
/*      */         {
/* 1480 */           paramAnonymousKeyEvent.consume();
/* 1481 */           return true;
/*      */         }
/* 1483 */         return false;
/*      */       }
/*      */     });
/* 1486 */     AWTAccessor.getComponentAccessor().setRequestFocusController(JComponent.focusController);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  364 */     ArrayList localArrayList = new ArrayList(4);
/*  365 */     localArrayList.add(new LookAndFeelInfo("Metal", "javax.swing.plaf.metal.MetalLookAndFeel"));
/*      */ 
/*  367 */     localArrayList.add(new LookAndFeelInfo("Nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
/*      */ 
/*  369 */     localArrayList.add(new LookAndFeelInfo("CDE/Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
/*      */ 
/*  373 */     OSInfo.OSType localOSType = (OSInfo.OSType)AccessController.doPrivileged(OSInfo.getOSTypeAction());
/*  374 */     if (localOSType == OSInfo.OSType.WINDOWS) {
/*  375 */       localArrayList.add(new LookAndFeelInfo("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
/*      */ 
/*  377 */       if (Toolkit.getDefaultToolkit().getDesktopProperty("win.xpstyle.themeActive") != null)
/*      */       {
/*  379 */         localArrayList.add(new LookAndFeelInfo("Windows Classic", "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"));
/*      */       }
/*      */ 
/*      */     }
/*  383 */     else if (localOSType == OSInfo.OSType.MACOSX) {
/*  384 */       localArrayList.add(new LookAndFeelInfo("Mac OS X", "com.apple.laf.AquaLookAndFeel"));
/*      */     }
/*      */     else
/*      */     {
/*  388 */       localArrayList.add(new LookAndFeelInfo("GTK+", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class LAFState
/*      */   {
/*      */     Properties swingProps;
/*  191 */     private UIDefaults[] tables = new UIDefaults[2];
/*      */ 
/*  193 */     boolean initialized = false;
/*  194 */     boolean focusPolicyInitialized = false;
/*  195 */     MultiUIDefaults multiUIDefaults = new MultiUIDefaults(this.tables);
/*      */     LookAndFeel lookAndFeel;
/*  197 */     LookAndFeel multiLookAndFeel = null;
/*  198 */     Vector<LookAndFeel> auxLookAndFeels = null;
/*      */     SwingPropertyChangeSupport changeSupport;
/*      */     UIManager.LookAndFeelInfo[] installedLAFs;
/*      */ 
/*      */     UIDefaults getLookAndFeelDefaults()
/*      */     {
/*  203 */       return this.tables[0]; } 
/*  204 */     void setLookAndFeelDefaults(UIDefaults paramUIDefaults) { this.tables[0] = paramUIDefaults; } 
/*      */     UIDefaults getSystemDefaults() {
/*  206 */       return this.tables[1]; } 
/*  207 */     void setSystemDefaults(UIDefaults paramUIDefaults) { this.tables[1] = paramUIDefaults; }
/*      */ 
/*      */ 
/*      */     public synchronized SwingPropertyChangeSupport getPropertyChangeSupport(boolean paramBoolean)
/*      */     {
/*  218 */       if ((paramBoolean) && (this.changeSupport == null)) {
/*  219 */         this.changeSupport = new SwingPropertyChangeSupport(UIManager.class);
/*      */       }
/*      */ 
/*  222 */       return this.changeSupport;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class LookAndFeelInfo
/*      */   {
/*      */     private String name;
/*      */     private String className;
/*      */ 
/*      */     public LookAndFeelInfo(String paramString1, String paramString2)
/*      */     {
/*  317 */       this.name = paramString1;
/*  318 */       this.className = paramString2;
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/*  328 */       return this.name;
/*      */     }
/*      */ 
/*      */     public String getClassName()
/*      */     {
/*  338 */       return this.className;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  348 */       return getClass().getName() + "[" + getName() + " " + getClassName() + "]";
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.UIManager
 * JD-Core Version:    0.6.2
 */
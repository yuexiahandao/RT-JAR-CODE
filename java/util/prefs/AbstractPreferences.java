/*      */ package java.util.prefs;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Collection;
/*      */ import java.util.EventObject;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ public abstract class AbstractPreferences extends Preferences
/*      */ {
/*      */   private final String name;
/*      */   private final String absolutePath;
/*      */   final AbstractPreferences parent;
/*      */   private final AbstractPreferences root;
/*  152 */   protected boolean newNode = false;
/*      */ 
/*  158 */   private Map<String, AbstractPreferences> kidCache = new HashMap();
/*      */ 
/*  164 */   private boolean removed = false;
/*      */ 
/*  169 */   private PreferenceChangeListener[] prefListeners = new PreferenceChangeListener[0];
/*      */ 
/*  175 */   private NodeChangeListener[] nodeListeners = new NodeChangeListener[0];
/*      */ 
/*  184 */   protected final Object lock = new Object();
/*      */ 
/*  722 */   private static final String[] EMPTY_STRING_ARRAY = new String[0];
/*      */ 
/*  733 */   private static final AbstractPreferences[] EMPTY_ABSTRACT_PREFS_ARRAY = new AbstractPreferences[0];
/*      */ 
/* 1444 */   private static final List<EventObject> eventQueue = new LinkedList();
/*      */ 
/* 1507 */   private static Thread eventDispatchThread = null;
/*      */ 
/*      */   protected AbstractPreferences(AbstractPreferences paramAbstractPreferences, String paramString)
/*      */   {
/*  199 */     if (paramAbstractPreferences == null) {
/*  200 */       if (!paramString.equals("")) {
/*  201 */         throw new IllegalArgumentException("Root name '" + paramString + "' must be \"\"");
/*      */       }
/*  203 */       this.absolutePath = "/";
/*  204 */       this.root = this;
/*      */     } else {
/*  206 */       if (paramString.indexOf('/') != -1) {
/*  207 */         throw new IllegalArgumentException("Name '" + paramString + "' contains '/'");
/*      */       }
/*  209 */       if (paramString.equals("")) {
/*  210 */         throw new IllegalArgumentException("Illegal name: empty string");
/*      */       }
/*  212 */       this.root = paramAbstractPreferences.root;
/*  213 */       this.absolutePath = (paramAbstractPreferences.absolutePath() + "/" + paramString);
/*      */     }
/*      */ 
/*  216 */     this.name = paramString;
/*  217 */     this.parent = paramAbstractPreferences;
/*      */   }
/*      */ 
/*      */   public void put(String paramString1, String paramString2)
/*      */   {
/*  240 */     if ((paramString1 == null) || (paramString2 == null))
/*  241 */       throw new NullPointerException();
/*  242 */     if (paramString1.length() > 80)
/*  243 */       throw new IllegalArgumentException("Key too long: " + paramString1);
/*  244 */     if (paramString2.length() > 8192) {
/*  245 */       throw new IllegalArgumentException("Value too long: " + paramString2);
/*      */     }
/*  247 */     synchronized (this.lock) {
/*  248 */       if (this.removed) {
/*  249 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*  251 */       putSpi(paramString1, paramString2);
/*  252 */       enqueuePreferenceChangeEvent(paramString1, paramString2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String get(String paramString1, String paramString2)
/*      */   {
/*  279 */     if (paramString1 == null)
/*  280 */       throw new NullPointerException("Null key");
/*  281 */     synchronized (this.lock) {
/*  282 */       if (this.removed) {
/*  283 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*  285 */       String str = null;
/*      */       try {
/*  287 */         str = getSpi(paramString1);
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/*  291 */       return str == null ? paramString2 : str;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void remove(String paramString)
/*      */   {
/*  310 */     synchronized (this.lock) {
/*  311 */       if (this.removed) {
/*  312 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*  314 */       removeSpi(paramString);
/*  315 */       enqueuePreferenceChangeEvent(paramString, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */     throws BackingStoreException
/*      */   {
/*  334 */     synchronized (this.lock) {
/*  335 */       String[] arrayOfString = keys();
/*  336 */       for (int i = 0; i < arrayOfString.length; i++)
/*  337 */         remove(arrayOfString[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void putInt(String paramString, int paramInt)
/*      */   {
/*  358 */     put(paramString, Integer.toString(paramInt));
/*      */   }
/*      */ 
/*      */   public int getInt(String paramString, int paramInt)
/*      */   {
/*  384 */     int i = paramInt;
/*      */     try {
/*  386 */       String str = get(paramString, null);
/*  387 */       if (str != null)
/*  388 */         i = Integer.parseInt(str);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/*  393 */     return i;
/*      */   }
/*      */ 
/*      */   public void putLong(String paramString, long paramLong)
/*      */   {
/*  413 */     put(paramString, Long.toString(paramLong));
/*      */   }
/*      */ 
/*      */   public long getLong(String paramString, long paramLong)
/*      */   {
/*  439 */     long l = paramLong;
/*      */     try {
/*  441 */       String str = get(paramString, null);
/*  442 */       if (str != null)
/*  443 */         l = Long.parseLong(str);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/*  448 */     return l;
/*      */   }
/*      */ 
/*      */   public void putBoolean(String paramString, boolean paramBoolean)
/*      */   {
/*  468 */     put(paramString, String.valueOf(paramBoolean));
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(String paramString, boolean paramBoolean)
/*      */   {
/*  497 */     boolean bool = paramBoolean;
/*  498 */     String str = get(paramString, null);
/*  499 */     if (str != null) {
/*  500 */       if (str.equalsIgnoreCase("true"))
/*  501 */         bool = true;
/*  502 */       else if (str.equalsIgnoreCase("false")) {
/*  503 */         bool = false;
/*      */       }
/*      */     }
/*  506 */     return bool;
/*      */   }
/*      */ 
/*      */   public void putFloat(String paramString, float paramFloat)
/*      */   {
/*  526 */     put(paramString, Float.toString(paramFloat));
/*      */   }
/*      */ 
/*      */   public float getFloat(String paramString, float paramFloat)
/*      */   {
/*  552 */     float f = paramFloat;
/*      */     try {
/*  554 */       String str = get(paramString, null);
/*  555 */       if (str != null)
/*  556 */         f = Float.parseFloat(str);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/*  561 */     return f;
/*      */   }
/*      */ 
/*      */   public void putDouble(String paramString, double paramDouble)
/*      */   {
/*  581 */     put(paramString, Double.toString(paramDouble));
/*      */   }
/*      */ 
/*      */   public double getDouble(String paramString, double paramDouble)
/*      */   {
/*  607 */     double d = paramDouble;
/*      */     try {
/*  609 */       String str = get(paramString, null);
/*  610 */       if (str != null)
/*  611 */         d = Double.parseDouble(str);
/*      */     }
/*      */     catch (NumberFormatException localNumberFormatException)
/*      */     {
/*      */     }
/*  616 */     return d;
/*      */   }
/*      */ 
/*      */   public void putByteArray(String paramString, byte[] paramArrayOfByte)
/*      */   {
/*  632 */     put(paramString, Base64.byteArrayToBase64(paramArrayOfByte));
/*      */   }
/*      */ 
/*      */   public byte[] getByteArray(String paramString, byte[] paramArrayOfByte)
/*      */   {
/*  653 */     byte[] arrayOfByte = paramArrayOfByte;
/*  654 */     String str = get(paramString, null);
/*      */     try {
/*  656 */       if (str != null) {
/*  657 */         arrayOfByte = Base64.base64ToByteArray(str);
/*      */       }
/*      */     }
/*      */     catch (RuntimeException localRuntimeException)
/*      */     {
/*      */     }
/*  663 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public String[] keys()
/*      */     throws BackingStoreException
/*      */   {
/*  682 */     synchronized (this.lock) {
/*  683 */       if (this.removed) {
/*  684 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*  686 */       return keysSpi();
/*      */     }
/*      */   }
/*      */ 
/*      */   public String[] childrenNames()
/*      */     throws BackingStoreException
/*      */   {
/*  711 */     synchronized (this.lock) {
/*  712 */       if (this.removed) {
/*  713 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*  715 */       TreeSet localTreeSet = new TreeSet(this.kidCache.keySet());
/*  716 */       for (String str : childrenNamesSpi())
/*  717 */         localTreeSet.add(str);
/*  718 */       return (String[])localTreeSet.toArray(EMPTY_STRING_ARRAY);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final AbstractPreferences[] cachedChildren()
/*      */   {
/*  730 */     return (AbstractPreferences[])this.kidCache.values().toArray(EMPTY_ABSTRACT_PREFS_ARRAY);
/*      */   }
/*      */ 
/*      */   public Preferences parent()
/*      */   {
/*  749 */     synchronized (this.lock) {
/*  750 */       if (this.removed) {
/*  751 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*  753 */       return this.parent;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Preferences node(String paramString)
/*      */   {
/*  803 */     synchronized (this.lock) {
/*  804 */       if (this.removed)
/*  805 */         throw new IllegalStateException("Node has been removed.");
/*  806 */       if (paramString.equals(""))
/*  807 */         return this;
/*  808 */       if (paramString.equals("/"))
/*  809 */         return this.root;
/*  810 */       if (paramString.charAt(0) != '/') {
/*  811 */         return node(new StringTokenizer(paramString, "/", true));
/*      */       }
/*      */     }
/*      */ 
/*  815 */     return this.root.node(new StringTokenizer(paramString.substring(1), "/", true));
/*      */   }
/*      */ 
/*      */   private Preferences node(StringTokenizer paramStringTokenizer)
/*      */   {
/*  822 */     String str = paramStringTokenizer.nextToken();
/*  823 */     if (str.equals("/"))
/*  824 */       throw new IllegalArgumentException("Consecutive slashes in path");
/*  825 */     synchronized (this.lock) {
/*  826 */       AbstractPreferences localAbstractPreferences = (AbstractPreferences)this.kidCache.get(str);
/*  827 */       if (localAbstractPreferences == null) {
/*  828 */         if (str.length() > 80) {
/*  829 */           throw new IllegalArgumentException("Node name " + str + " too long");
/*      */         }
/*  831 */         localAbstractPreferences = childSpi(str);
/*  832 */         if (localAbstractPreferences.newNode)
/*  833 */           enqueueNodeAddedEvent(localAbstractPreferences);
/*  834 */         this.kidCache.put(str, localAbstractPreferences);
/*      */       }
/*  836 */       if (!paramStringTokenizer.hasMoreTokens())
/*  837 */         return localAbstractPreferences;
/*  838 */       paramStringTokenizer.nextToken();
/*  839 */       if (!paramStringTokenizer.hasMoreTokens())
/*  840 */         throw new IllegalArgumentException("Path ends with slash");
/*  841 */       return localAbstractPreferences.node(paramStringTokenizer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean nodeExists(String paramString)
/*      */     throws BackingStoreException
/*      */   {
/*  868 */     synchronized (this.lock) {
/*  869 */       if (paramString.equals(""))
/*  870 */         return !this.removed;
/*  871 */       if (this.removed)
/*  872 */         throw new IllegalStateException("Node has been removed.");
/*  873 */       if (paramString.equals("/"))
/*  874 */         return true;
/*  875 */       if (paramString.charAt(0) != '/') {
/*  876 */         return nodeExists(new StringTokenizer(paramString, "/", true));
/*      */       }
/*      */     }
/*      */ 
/*  880 */     return this.root.nodeExists(new StringTokenizer(paramString.substring(1), "/", true));
/*      */   }
/*      */ 
/*      */   private boolean nodeExists(StringTokenizer paramStringTokenizer)
/*      */     throws BackingStoreException
/*      */   {
/*  890 */     String str = paramStringTokenizer.nextToken();
/*  891 */     if (str.equals("/"))
/*  892 */       throw new IllegalArgumentException("Consecutive slashes in path");
/*  893 */     synchronized (this.lock) {
/*  894 */       AbstractPreferences localAbstractPreferences = (AbstractPreferences)this.kidCache.get(str);
/*  895 */       if (localAbstractPreferences == null)
/*  896 */         localAbstractPreferences = getChild(str);
/*  897 */       if (localAbstractPreferences == null)
/*  898 */         return false;
/*  899 */       if (!paramStringTokenizer.hasMoreTokens())
/*  900 */         return true;
/*  901 */       paramStringTokenizer.nextToken();
/*  902 */       if (!paramStringTokenizer.hasMoreTokens())
/*  903 */         throw new IllegalArgumentException("Path ends with slash");
/*  904 */       return localAbstractPreferences.nodeExists(paramStringTokenizer);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeNode()
/*      */     throws BackingStoreException
/*      */   {
/*  941 */     if (this == this.root)
/*  942 */       throw new UnsupportedOperationException("Can't remove the root!");
/*  943 */     synchronized (this.parent.lock) {
/*  944 */       removeNode2();
/*  945 */       this.parent.kidCache.remove(this.name);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeNode2()
/*      */     throws BackingStoreException
/*      */   {
/*  954 */     synchronized (this.lock) {
/*  955 */       if (this.removed) {
/*  956 */         throw new IllegalStateException("Node already removed.");
/*      */       }
/*      */ 
/*  959 */       String[] arrayOfString = childrenNamesSpi();
/*  960 */       for (int i = 0; i < arrayOfString.length; i++) {
/*  961 */         if (!this.kidCache.containsKey(arrayOfString[i])) {
/*  962 */           this.kidCache.put(arrayOfString[i], childSpi(arrayOfString[i]));
/*      */         }
/*      */       }
/*  965 */       Iterator localIterator = this.kidCache.values().iterator();
/*  966 */       while (localIterator.hasNext())
/*      */         try {
/*  968 */           ((AbstractPreferences)localIterator.next()).removeNode2();
/*  969 */           localIterator.remove();
/*      */         }
/*      */         catch (BackingStoreException localBackingStoreException)
/*      */         {
/*      */         }
/*  974 */       removeNodeSpi();
/*  975 */       this.removed = true;
/*  976 */       this.parent.enqueueNodeRemovedEvent(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String name()
/*      */   {
/*  990 */     return this.name;
/*      */   }
/*      */ 
/*      */   public String absolutePath()
/*      */   {
/* 1005 */     return this.absolutePath;
/*      */   }
/*      */ 
/*      */   public boolean isUserNode()
/*      */   {
/* 1022 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/* 1025 */         return Boolean.valueOf(AbstractPreferences.this.root == Preferences.userRoot());
/*      */       }
/*      */     })).booleanValue();
/*      */   }
/*      */ 
/*      */   public void addPreferenceChangeListener(PreferenceChangeListener paramPreferenceChangeListener)
/*      */   {
/* 1031 */     if (paramPreferenceChangeListener == null)
/* 1032 */       throw new NullPointerException("Change listener is null.");
/* 1033 */     synchronized (this.lock) {
/* 1034 */       if (this.removed) {
/* 1035 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*      */ 
/* 1038 */       PreferenceChangeListener[] arrayOfPreferenceChangeListener = this.prefListeners;
/* 1039 */       this.prefListeners = new PreferenceChangeListener[arrayOfPreferenceChangeListener.length + 1];
/* 1040 */       System.arraycopy(arrayOfPreferenceChangeListener, 0, this.prefListeners, 0, arrayOfPreferenceChangeListener.length);
/* 1041 */       this.prefListeners[arrayOfPreferenceChangeListener.length] = paramPreferenceChangeListener;
/*      */     }
/* 1043 */     startEventDispatchThreadIfNecessary();
/*      */   }
/*      */ 
/*      */   public void removePreferenceChangeListener(PreferenceChangeListener paramPreferenceChangeListener) {
/* 1047 */     synchronized (this.lock) {
/* 1048 */       if (this.removed)
/* 1049 */         throw new IllegalStateException("Node has been removed.");
/* 1050 */       if ((this.prefListeners == null) || (this.prefListeners.length == 0)) {
/* 1051 */         throw new IllegalArgumentException("Listener not registered.");
/*      */       }
/*      */ 
/* 1054 */       PreferenceChangeListener[] arrayOfPreferenceChangeListener = new PreferenceChangeListener[this.prefListeners.length - 1];
/*      */ 
/* 1056 */       int i = 0;
/* 1057 */       while ((i < arrayOfPreferenceChangeListener.length) && (this.prefListeners[i] != paramPreferenceChangeListener)) {
/* 1058 */         arrayOfPreferenceChangeListener[i] = this.prefListeners[(i++)];
/*      */       }
/* 1060 */       if ((i == arrayOfPreferenceChangeListener.length) && (this.prefListeners[i] != paramPreferenceChangeListener))
/* 1061 */         throw new IllegalArgumentException("Listener not registered.");
/* 1062 */       while (i < arrayOfPreferenceChangeListener.length)
/* 1063 */         arrayOfPreferenceChangeListener[i] = this.prefListeners[(++i)];
/* 1064 */       this.prefListeners = arrayOfPreferenceChangeListener;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNodeChangeListener(NodeChangeListener paramNodeChangeListener) {
/* 1069 */     if (paramNodeChangeListener == null)
/* 1070 */       throw new NullPointerException("Change listener is null.");
/* 1071 */     synchronized (this.lock) {
/* 1072 */       if (this.removed) {
/* 1073 */         throw new IllegalStateException("Node has been removed.");
/*      */       }
/*      */ 
/* 1076 */       if (this.nodeListeners == null) {
/* 1077 */         this.nodeListeners = new NodeChangeListener[1];
/* 1078 */         this.nodeListeners[0] = paramNodeChangeListener;
/*      */       } else {
/* 1080 */         NodeChangeListener[] arrayOfNodeChangeListener = this.nodeListeners;
/* 1081 */         this.nodeListeners = new NodeChangeListener[arrayOfNodeChangeListener.length + 1];
/* 1082 */         System.arraycopy(arrayOfNodeChangeListener, 0, this.nodeListeners, 0, arrayOfNodeChangeListener.length);
/* 1083 */         this.nodeListeners[arrayOfNodeChangeListener.length] = paramNodeChangeListener;
/*      */       }
/*      */     }
/* 1086 */     startEventDispatchThreadIfNecessary();
/*      */   }
/*      */ 
/*      */   public void removeNodeChangeListener(NodeChangeListener paramNodeChangeListener) {
/* 1090 */     synchronized (this.lock) {
/* 1091 */       if (this.removed)
/* 1092 */         throw new IllegalStateException("Node has been removed.");
/* 1093 */       if ((this.nodeListeners == null) || (this.nodeListeners.length == 0)) {
/* 1094 */         throw new IllegalArgumentException("Listener not registered.");
/*      */       }
/*      */ 
/* 1097 */       int i = 0;
/* 1098 */       while ((i < this.nodeListeners.length) && (this.nodeListeners[i] != paramNodeChangeListener))
/* 1099 */         i++;
/* 1100 */       if (i == this.nodeListeners.length)
/* 1101 */         throw new IllegalArgumentException("Listener not registered.");
/* 1102 */       NodeChangeListener[] arrayOfNodeChangeListener = new NodeChangeListener[this.nodeListeners.length - 1];
/*      */ 
/* 1104 */       if (i != 0)
/* 1105 */         System.arraycopy(this.nodeListeners, 0, arrayOfNodeChangeListener, 0, i);
/* 1106 */       if (i != arrayOfNodeChangeListener.length) {
/* 1107 */         System.arraycopy(this.nodeListeners, i + 1, arrayOfNodeChangeListener, i, arrayOfNodeChangeListener.length - i);
/*      */       }
/* 1109 */       this.nodeListeners = arrayOfNodeChangeListener;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected abstract void putSpi(String paramString1, String paramString2);
/*      */ 
/*      */   protected abstract String getSpi(String paramString);
/*      */ 
/*      */   protected abstract void removeSpi(String paramString);
/*      */ 
/*      */   protected abstract void removeNodeSpi()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   protected abstract String[] keysSpi()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   protected abstract String[] childrenNamesSpi()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   protected AbstractPreferences getChild(String paramString)
/*      */     throws BackingStoreException
/*      */   {
/* 1251 */     synchronized (this.lock)
/*      */     {
/* 1253 */       String[] arrayOfString = childrenNames();
/* 1254 */       for (int i = 0; i < arrayOfString.length; i++)
/* 1255 */         if (arrayOfString[i].equals(paramString))
/* 1256 */           return childSpi(arrayOfString[i]);
/*      */     }
/* 1258 */     return null;
/*      */   }
/*      */ 
/*      */   protected abstract AbstractPreferences childSpi(String paramString);
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1298 */     return (isUserNode() ? "User" : "System") + " Preference Node: " + absolutePath();
/*      */   }
/*      */ 
/*      */   public void sync()
/*      */     throws BackingStoreException
/*      */   {
/* 1323 */     sync2();
/*      */   }
/*      */ 
/*      */   private void sync2()
/*      */     throws BackingStoreException
/*      */   {
/*      */     AbstractPreferences[] arrayOfAbstractPreferences;
/* 1329 */     synchronized (this.lock) {
/* 1330 */       if (this.removed)
/* 1331 */         throw new IllegalStateException("Node has been removed");
/* 1332 */       syncSpi();
/* 1333 */       arrayOfAbstractPreferences = cachedChildren();
/*      */     }
/*      */ 
/* 1336 */     for (int i = 0; i < arrayOfAbstractPreferences.length; i++)
/* 1337 */       arrayOfAbstractPreferences[i].sync2();
/*      */   }
/*      */ 
/*      */   protected abstract void syncSpi()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   public void flush()
/*      */     throws BackingStoreException
/*      */   {
/* 1383 */     flush2();
/*      */   }
/*      */ 
/*      */   private void flush2()
/*      */     throws BackingStoreException
/*      */   {
/*      */     AbstractPreferences[] arrayOfAbstractPreferences;
/* 1389 */     synchronized (this.lock) {
/* 1390 */       flushSpi();
/* 1391 */       if (this.removed)
/* 1392 */         return;
/* 1393 */       arrayOfAbstractPreferences = cachedChildren();
/*      */     }
/*      */ 
/* 1396 */     for (int i = 0; i < arrayOfAbstractPreferences.length; i++)
/* 1397 */       arrayOfAbstractPreferences[i].flush2();
/*      */   }
/*      */ 
/*      */   protected abstract void flushSpi()
/*      */     throws BackingStoreException;
/*      */ 
/*      */   protected boolean isRemoved()
/*      */   {
/* 1431 */     synchronized (this.lock) {
/* 1432 */       return this.removed;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static synchronized void startEventDispatchThreadIfNecessary()
/*      */   {
/* 1515 */     if (eventDispatchThread == null)
/*      */     {
/* 1517 */       eventDispatchThread = new EventDispatchThread(null);
/* 1518 */       eventDispatchThread.setDaemon(true);
/* 1519 */       eventDispatchThread.start();
/*      */     }
/*      */   }
/*      */ 
/*      */   PreferenceChangeListener[] prefListeners()
/*      */   {
/* 1530 */     synchronized (this.lock) {
/* 1531 */       return this.prefListeners;
/*      */     }
/*      */   }
/*      */ 
/* 1535 */   NodeChangeListener[] nodeListeners() { synchronized (this.lock) {
/* 1536 */       return this.nodeListeners;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void enqueuePreferenceChangeEvent(String paramString1, String paramString2)
/*      */   {
/* 1546 */     if (this.prefListeners.length != 0)
/* 1547 */       synchronized (eventQueue) {
/* 1548 */         eventQueue.add(new PreferenceChangeEvent(this, paramString1, paramString2));
/* 1549 */         eventQueue.notify();
/*      */       }
/*      */   }
/*      */ 
/*      */   private void enqueueNodeAddedEvent(Preferences paramPreferences)
/*      */   {
/* 1560 */     if (this.nodeListeners.length != 0)
/* 1561 */       synchronized (eventQueue) {
/* 1562 */         eventQueue.add(new NodeAddedEvent(this, paramPreferences));
/* 1563 */         eventQueue.notify();
/*      */       }
/*      */   }
/*      */ 
/*      */   private void enqueueNodeRemovedEvent(Preferences paramPreferences)
/*      */   {
/* 1574 */     if (this.nodeListeners.length != 0)
/* 1575 */       synchronized (eventQueue) {
/* 1576 */         eventQueue.add(new NodeRemovedEvent(this, paramPreferences));
/* 1577 */         eventQueue.notify();
/*      */       }
/*      */   }
/*      */ 
/*      */   public void exportNode(OutputStream paramOutputStream)
/*      */     throws IOException, BackingStoreException
/*      */   {
/* 1595 */     XmlSupport.export(paramOutputStream, this, false);
/*      */   }
/*      */ 
/*      */   public void exportSubtree(OutputStream paramOutputStream)
/*      */     throws IOException, BackingStoreException
/*      */   {
/* 1611 */     XmlSupport.export(paramOutputStream, this, true);
/*      */   }
/*      */ 
/*      */   private static class EventDispatchThread extends Thread
/*      */   {
/*      */     public void run()
/*      */     {
/*      */       while (true)
/*      */       {
/* 1472 */         EventObject localEventObject = null;
/* 1473 */         synchronized (AbstractPreferences.eventQueue) {
/*      */           try {
/* 1475 */             while (AbstractPreferences.eventQueue.isEmpty())
/* 1476 */               AbstractPreferences.eventQueue.wait();
/* 1477 */             localEventObject = (EventObject)AbstractPreferences.eventQueue.remove(0);
/*      */           }
/*      */           catch (InterruptedException localInterruptedException) {
/* 1480 */             return;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1485 */         ??? = (AbstractPreferences)localEventObject.getSource();
/*      */         Object localObject1;
/*      */         Object localObject3;
/*      */         int i;
/* 1486 */         if ((localEventObject instanceof PreferenceChangeEvent)) {
/* 1487 */           localObject1 = (PreferenceChangeEvent)localEventObject;
/* 1488 */           localObject3 = ((AbstractPreferences)???).prefListeners();
/* 1489 */           for (i = 0; i < localObject3.length; i++)
/* 1490 */             localObject3[i].preferenceChange((PreferenceChangeEvent)localObject1);
/*      */         } else {
/* 1492 */           localObject1 = (NodeChangeEvent)localEventObject;
/* 1493 */           localObject3 = ((AbstractPreferences)???).nodeListeners();
/* 1494 */           if ((localObject1 instanceof AbstractPreferences.NodeAddedEvent)) {
/* 1495 */             for (i = 0; i < localObject3.length; i++)
/* 1496 */               localObject3[i].childAdded((NodeChangeEvent)localObject1);
/*      */           }
/*      */           else
/* 1499 */             for (i = 0; i < localObject3.length; i++)
/* 1500 */               localObject3[i].childRemoved((NodeChangeEvent)localObject1);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private class NodeAddedEvent extends NodeChangeEvent
/*      */   {
/*      */     private static final long serialVersionUID = -6743557530157328528L;
/*      */ 
/*      */     NodeAddedEvent(Preferences paramPreferences1, Preferences arg3)
/*      */     {
/* 1454 */       super(localPreferences);
/*      */     }
/*      */   }
/*      */   private class NodeRemovedEvent extends NodeChangeEvent {
/*      */     private static final long serialVersionUID = 8735497392918824837L;
/*      */ 
/* 1460 */     NodeRemovedEvent(Preferences paramPreferences1, Preferences arg3) { super(localPreferences); }
/*      */ 
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.AbstractPreferences
 * JD-Core Version:    0.6.2
 */
/*      */ package java.beans;
/*      */ 
/*      */ import com.sun.beans.finder.PrimitiveWrapperMap;
/*      */ import java.awt.AWTKeyStroke;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Choice;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Font;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Menu;
/*      */ import java.awt.MenuBar;
/*      */ import java.awt.MenuShortcut;
/*      */ import java.awt.Point;
/*      */ import java.awt.Window;
/*      */ import java.awt.font.TextAttribute;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.swing.Box;
/*      */ import javax.swing.DefaultComboBoxModel;
/*      */ import javax.swing.DefaultListModel;
/*      */ import javax.swing.JLayeredPane;
/*      */ import javax.swing.JMenu;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JTabbedPane;
/*      */ import javax.swing.ToolTipManager;
/*      */ import javax.swing.border.MatteBorder;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.tree.DefaultMutableTreeNode;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ class MetaData
/*      */ {
/* 1287 */   private static final Map<String, Field> fields = Collections.synchronizedMap(new WeakHashMap());
/* 1288 */   private static Hashtable internalPersistenceDelegates = new Hashtable();
/*      */ 
/* 1290 */   private static PersistenceDelegate nullPersistenceDelegate = new NullPersistenceDelegate();
/* 1291 */   private static PersistenceDelegate enumPersistenceDelegate = new EnumPersistenceDelegate();
/* 1292 */   private static PersistenceDelegate primitivePersistenceDelegate = new PrimitivePersistenceDelegate();
/* 1293 */   private static PersistenceDelegate defaultPersistenceDelegate = new DefaultPersistenceDelegate();
/*      */   private static PersistenceDelegate arrayPersistenceDelegate;
/*      */   private static PersistenceDelegate proxyPersistenceDelegate;
/*      */ 
/*      */   public static synchronized PersistenceDelegate getPersistenceDelegate(Class paramClass)
/*      */   {
/* 1322 */     if (paramClass == null) {
/* 1323 */       return nullPersistenceDelegate;
/*      */     }
/* 1325 */     if (Enum.class.isAssignableFrom(paramClass)) {
/* 1326 */       return enumPersistenceDelegate;
/*      */     }
/* 1328 */     if (null != XMLEncoder.primitiveTypeFor(paramClass)) {
/* 1329 */       return primitivePersistenceDelegate;
/*      */     }
/*      */ 
/* 1332 */     if (paramClass.isArray()) {
/* 1333 */       if (arrayPersistenceDelegate == null) {
/* 1334 */         arrayPersistenceDelegate = new ArrayPersistenceDelegate();
/*      */       }
/* 1336 */       return arrayPersistenceDelegate;
/*      */     }
/*      */     try
/*      */     {
/* 1340 */       if (Proxy.isProxyClass(paramClass)) {
/* 1341 */         if (proxyPersistenceDelegate == null) {
/* 1342 */           proxyPersistenceDelegate = new ProxyPersistenceDelegate();
/*      */         }
/* 1344 */         return proxyPersistenceDelegate;
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException1)
/*      */     {
/*      */     }
/*      */ 
/* 1352 */     String str1 = paramClass.getName();
/* 1353 */     Object localObject1 = (PersistenceDelegate)getBeanAttribute(paramClass, "persistenceDelegate");
/* 1354 */     if (localObject1 == null) {
/* 1355 */       localObject1 = (PersistenceDelegate)internalPersistenceDelegates.get(str1);
/* 1356 */       if (localObject1 != null) {
/* 1357 */         return localObject1;
/*      */       }
/* 1359 */       internalPersistenceDelegates.put(str1, defaultPersistenceDelegate);
/*      */       try {
/* 1361 */         String str2 = paramClass.getName();
/* 1362 */         localObject2 = Class.forName("java.beans.MetaData$" + str2.replace('.', '_') + "_PersistenceDelegate");
/*      */ 
/* 1364 */         localObject1 = (PersistenceDelegate)((Class)localObject2).newInstance();
/* 1365 */         internalPersistenceDelegates.put(str1, localObject1);
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException) {
/* 1368 */         Object localObject2 = getConstructorProperties(paramClass);
/* 1369 */         if (localObject2 != null) {
/* 1370 */           localObject1 = new DefaultPersistenceDelegate((String[])localObject2);
/* 1371 */           internalPersistenceDelegates.put(str1, localObject1);
/*      */         }
/*      */       }
/*      */       catch (Exception localException2) {
/* 1375 */         System.err.println("Internal error: " + localException2);
/*      */       }
/*      */     }
/*      */ 
/* 1379 */     return localObject1 != null ? localObject1 : defaultPersistenceDelegate;
/*      */   }
/*      */ 
/*      */   private static String[] getConstructorProperties(Class paramClass) {
/* 1383 */     Object localObject = null;
/* 1384 */     int i = 0;
/* 1385 */     for (Constructor localConstructor : paramClass.getConstructors()) {
/* 1386 */       String[] arrayOfString = getAnnotationValue(localConstructor);
/* 1387 */       if ((arrayOfString != null) && (i < arrayOfString.length) && (isValid(localConstructor, arrayOfString))) {
/* 1388 */         localObject = arrayOfString;
/* 1389 */         i = arrayOfString.length;
/*      */       }
/*      */     }
/* 1392 */     return localObject;
/*      */   }
/*      */ 
/*      */   private static String[] getAnnotationValue(Constructor<?> paramConstructor) {
/* 1396 */     ConstructorProperties localConstructorProperties = (ConstructorProperties)paramConstructor.getAnnotation(ConstructorProperties.class);
/* 1397 */     return localConstructorProperties != null ? localConstructorProperties.value() : null;
/*      */   }
/*      */ 
/*      */   private static boolean isValid(Constructor<?> paramConstructor, String[] paramArrayOfString)
/*      */   {
/* 1403 */     Class[] arrayOfClass = paramConstructor.getParameterTypes();
/* 1404 */     if (paramArrayOfString.length != arrayOfClass.length) {
/* 1405 */       return false;
/*      */     }
/* 1407 */     for (String str : paramArrayOfString) {
/* 1408 */       if (str == null) {
/* 1409 */         return false;
/*      */       }
/*      */     }
/* 1412 */     return true;
/*      */   }
/*      */ 
/*      */   private static Object getBeanAttribute(Class paramClass, String paramString) {
/*      */     try {
/* 1417 */       return Introspector.getBeanInfo(paramClass).getBeanDescriptor().getValue(paramString); } catch (IntrospectionException localIntrospectionException) {
/*      */     }
/* 1419 */     return null;
/*      */   }
/*      */ 
/*      */   static Object getPrivateFieldValue(Object paramObject, String paramString)
/*      */   {
/* 1424 */     Field localField = (Field)fields.get(paramString);
/* 1425 */     if (localField == null) {
/* 1426 */       int i = paramString.lastIndexOf('.');
/* 1427 */       String str1 = paramString.substring(0, i);
/* 1428 */       final String str2 = paramString.substring(1 + i);
/* 1429 */       localField = (Field)AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Field run() {
/*      */           try {
/* 1432 */             Field localField = Class.forName(this.val$className).getDeclaredField(str2);
/* 1433 */             localField.setAccessible(true);
/* 1434 */             return localField;
/*      */           }
/*      */           catch (ClassNotFoundException localClassNotFoundException) {
/* 1437 */             throw new IllegalStateException("Could not find class", localClassNotFoundException);
/*      */           }
/*      */           catch (NoSuchFieldException localNoSuchFieldException) {
/* 1440 */             throw new IllegalStateException("Could not find field", localNoSuchFieldException);
/*      */           }
/*      */         }
/*      */       });
/* 1444 */       fields.put(paramString, localField);
/*      */     }
/*      */     try {
/* 1447 */       return localField.get(paramObject);
/*      */     }
/*      */     catch (IllegalAccessException localIllegalAccessException) {
/* 1450 */       throw new IllegalStateException("Could not get value of the field", localIllegalAccessException);
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/* 1299 */     internalPersistenceDelegates.put("java.net.URI", new PrimitivePersistenceDelegate());
/*      */ 
/* 1303 */     internalPersistenceDelegates.put("javax.swing.plaf.BorderUIResource$MatteBorderUIResource", new javax_swing_border_MatteBorder_PersistenceDelegate());
/*      */ 
/* 1307 */     internalPersistenceDelegates.put("javax.swing.plaf.FontUIResource", new java_awt_Font_PersistenceDelegate());
/*      */ 
/* 1311 */     internalPersistenceDelegates.put("javax.swing.KeyStroke", new java_awt_AWTKeyStroke_PersistenceDelegate());
/*      */ 
/* 1314 */     internalPersistenceDelegates.put("java.sql.Date", new java_util_Date_PersistenceDelegate());
/* 1315 */     internalPersistenceDelegates.put("java.sql.Time", new java_util_Date_PersistenceDelegate());
/*      */ 
/* 1317 */     internalPersistenceDelegates.put("java.util.JumboEnumSet", new java_util_EnumSet_PersistenceDelegate());
/* 1318 */     internalPersistenceDelegates.put("java.util.RegularEnumSet", new java_util_EnumSet_PersistenceDelegate());
/*      */   }
/*      */ 
/*      */   static final class ArrayPersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  116 */       return (paramObject2 != null) && (paramObject1.getClass() == paramObject2.getClass()) && (Array.getLength(paramObject1) == Array.getLength(paramObject2));
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/*  123 */       Class localClass = paramObject.getClass();
/*  124 */       return new Expression(paramObject, Array.class, "newInstance", new Object[] { localClass.getComponentType(), new Integer(Array.getLength(paramObject)) });
/*      */     }
/*      */ 
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  130 */       int i = Array.getLength(paramObject1);
/*  131 */       for (int j = 0; j < i; j++) {
/*  132 */         Integer localInteger = new Integer(j);
/*      */ 
/*  135 */         Expression localExpression1 = new Expression(paramObject1, "get", new Object[] { localInteger });
/*  136 */         Expression localExpression2 = new Expression(paramObject2, "get", new Object[] { localInteger });
/*      */         try {
/*  138 */           Object localObject1 = localExpression1.getValue();
/*  139 */           Object localObject2 = localExpression2.getValue();
/*  140 */           paramEncoder.writeExpression(localExpression1);
/*  141 */           if (!Objects.equals(localObject2, paramEncoder.get(localObject1)))
/*      */           {
/*  144 */             DefaultPersistenceDelegate.invokeStatement(paramObject1, "set", new Object[] { localInteger, localObject1 }, paramEncoder);
/*      */           }
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  149 */           paramEncoder.getExceptionListener().exceptionThrown(localException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class EnumPersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*   94 */       return paramObject1 == paramObject2;
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*   98 */       Enum localEnum = (Enum)paramObject;
/*   99 */       return new Expression(localEnum, Enum.class, "valueOf", new Object[] { localEnum.getDeclaringClass(), localEnum.name() });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class NullPersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/*   80 */       return null;
/*      */     }
/*      */ 
/*      */     public void writeObject(Object paramObject, Encoder paramEncoder)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class PrimitivePersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  105 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  109 */       return new Expression(paramObject, paramObject.getClass(), "new", new Object[] { paramObject.toString() });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class ProxyPersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/*  157 */       Class localClass = paramObject.getClass();
/*  158 */       Proxy localProxy = (Proxy)paramObject;
/*      */ 
/*  161 */       InvocationHandler localInvocationHandler = Proxy.getInvocationHandler(localProxy);
/*  162 */       if ((localInvocationHandler instanceof EventHandler)) {
/*  163 */         EventHandler localEventHandler = (EventHandler)localInvocationHandler;
/*  164 */         Vector localVector = new Vector();
/*  165 */         localVector.add(localClass.getInterfaces()[0]);
/*  166 */         localVector.add(localEventHandler.getTarget());
/*  167 */         localVector.add(localEventHandler.getAction());
/*  168 */         if (localEventHandler.getEventPropertyName() != null) {
/*  169 */           localVector.add(localEventHandler.getEventPropertyName());
/*      */         }
/*  171 */         if (localEventHandler.getListenerMethodName() != null) {
/*  172 */           localVector.setSize(4);
/*  173 */           localVector.add(localEventHandler.getListenerMethodName());
/*      */         }
/*  175 */         return new Expression(paramObject, EventHandler.class, "create", localVector.toArray());
/*      */       }
/*      */ 
/*  180 */       return new Expression(paramObject, Proxy.class, "newProxyInstance", new Object[] { localClass.getClassLoader(), localClass.getInterfaces(), localInvocationHandler });
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StaticFieldsPersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected void installFields(Encoder paramEncoder, Class<?> paramClass)
/*      */     {
/*  854 */       if ((Modifier.isPublic(paramClass.getModifiers())) && (ReflectUtil.isPackageAccessible(paramClass))) {
/*  855 */         Field[] arrayOfField = paramClass.getFields();
/*  856 */         for (int i = 0; i < arrayOfField.length; i++) {
/*  857 */           Field localField = arrayOfField[i];
/*      */ 
/*  860 */           if (Object.class.isAssignableFrom(localField.getType()))
/*  861 */             paramEncoder.writeExpression(new Expression(localField, "get", new Object[] { null }));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/*  868 */       throw new RuntimeException("Unrecognized instance: " + paramObject);
/*      */     }
/*      */ 
/*      */     public void writeObject(Object paramObject, Encoder paramEncoder) {
/*  872 */       if (paramEncoder.getAttribute(this) == null) {
/*  873 */         paramEncoder.setAttribute(this, Boolean.TRUE);
/*  874 */         installFields(paramEncoder, paramObject.getClass());
/*      */       }
/*  876 */       super.writeObject(paramObject, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_AWTKeyStroke_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  813 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  817 */       AWTKeyStroke localAWTKeyStroke = (AWTKeyStroke)paramObject;
/*      */ 
/*  819 */       int i = localAWTKeyStroke.getKeyChar();
/*  820 */       int j = localAWTKeyStroke.getKeyCode();
/*  821 */       int k = localAWTKeyStroke.getModifiers();
/*  822 */       boolean bool = localAWTKeyStroke.isOnKeyRelease();
/*      */ 
/*  824 */       Object[] arrayOfObject = null;
/*  825 */       if (i == 65535) {
/*  826 */         arrayOfObject = new Object[] { Integer.valueOf(j), Integer.valueOf(k), !bool ? new Object[] { Integer.valueOf(j), Integer.valueOf(k) } : Boolean.valueOf(bool) };
/*      */       }
/*  829 */       else if (j == 0) {
/*  830 */         if (!bool) {
/*  831 */           arrayOfObject = new Object[] { Character.valueOf(i), k == 0 ? new Object[] { Character.valueOf(i) } : Integer.valueOf(k) };
/*      */         }
/*  834 */         else if (k == 0) {
/*  835 */           arrayOfObject = new Object[] { Character.valueOf(i), Boolean.valueOf(bool) };
/*      */         }
/*      */       }
/*  838 */       if (arrayOfObject == null) {
/*  839 */         throw new IllegalStateException("Unsupported KeyStroke: " + localAWTKeyStroke);
/*      */       }
/*  841 */       Class localClass = localAWTKeyStroke.getClass();
/*  842 */       String str = localClass.getName();
/*      */ 
/*  844 */       int m = str.lastIndexOf('.') + 1;
/*  845 */       if (m > 0) {
/*  846 */         str = str.substring(m);
/*      */       }
/*  848 */       return new Expression(localAWTKeyStroke, localClass, "get" + str, arrayOfObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_BorderLayout_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/* 1034 */     private static final String[] CONSTRAINTS = { "North", "South", "East", "West", "Center", "First", "Last", "Before", "After" };
/*      */ 
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1048 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1049 */       BorderLayout localBorderLayout1 = (BorderLayout)paramObject1;
/* 1050 */       BorderLayout localBorderLayout2 = (BorderLayout)paramObject2;
/* 1051 */       for (String str : CONSTRAINTS) {
/* 1052 */         Component localComponent1 = localBorderLayout1.getLayoutComponent(str);
/* 1053 */         Component localComponent2 = localBorderLayout2.getLayoutComponent(str);
/*      */ 
/* 1055 */         if ((localComponent1 != null) && (localComponent2 == null))
/* 1056 */           invokeStatement(paramObject1, "addLayoutComponent", new Object[] { localComponent1, str }, paramEncoder);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_CardLayout_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1067 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/*      */       Iterator localIterator;
/* 1068 */       if (getVector(paramObject2).isEmpty())
/* 1069 */         for (localIterator = getVector(paramObject1).iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 1070 */           Object[] arrayOfObject = { MetaData.getPrivateFieldValue(localObject, "java.awt.CardLayout$Card.name"), MetaData.getPrivateFieldValue(localObject, "java.awt.CardLayout$Card.comp") };
/*      */ 
/* 1072 */           invokeStatement(paramObject1, "addLayoutComponent", arrayOfObject, paramEncoder); }
/*      */     }
/*      */ 
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/* 1077 */       return (super.mutatesTo(paramObject1, paramObject2)) && (getVector(paramObject2).isEmpty());
/*      */     }
/*      */     private static Vector<?> getVector(Object paramObject) {
/* 1080 */       return (Vector)MetaData.getPrivateFieldValue(paramObject, "java.awt.CardLayout.vector");
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_Choice_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  984 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/*  985 */       Choice localChoice1 = (Choice)paramObject1;
/*  986 */       Choice localChoice2 = (Choice)paramObject2;
/*  987 */       for (int i = localChoice2.getItemCount(); i < localChoice1.getItemCount(); i++)
/*  988 */         invokeStatement(paramObject1, "add", new Object[] { localChoice1.getItem(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_Component_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  902 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/*  903 */       Component localComponent1 = (Component)paramObject1;
/*  904 */       Component localComponent2 = (Component)paramObject2;
/*      */ 
/*  909 */       if (!(paramObject1 instanceof Window)) {
/*  910 */         localObject1 = localComponent1.isBackgroundSet() ? localComponent1.getBackground() : null;
/*  911 */         Object localObject2 = localComponent2.isBackgroundSet() ? localComponent2.getBackground() : null;
/*  912 */         if (!Objects.equals(localObject1, localObject2)) {
/*  913 */           invokeStatement(paramObject1, "setBackground", new Object[] { localObject1 }, paramEncoder);
/*      */         }
/*  915 */         Object localObject3 = localComponent1.isForegroundSet() ? localComponent1.getForeground() : null;
/*  916 */         Object localObject4 = localComponent2.isForegroundSet() ? localComponent2.getForeground() : null;
/*  917 */         if (!Objects.equals(localObject3, localObject4)) {
/*  918 */           invokeStatement(paramObject1, "setForeground", new Object[] { localObject3 }, paramEncoder);
/*      */         }
/*  920 */         Object localObject5 = localComponent1.isFontSet() ? localComponent1.getFont() : null;
/*  921 */         Object localObject6 = localComponent2.isFontSet() ? localComponent2.getFont() : null;
/*  922 */         if (!Objects.equals(localObject5, localObject6)) {
/*  923 */           invokeStatement(paramObject1, "setFont", new Object[] { localObject5 }, paramEncoder);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  928 */       Object localObject1 = localComponent1.getParent();
/*  929 */       if ((localObject1 == null) || (((Container)localObject1).getLayout() == null))
/*      */       {
/*  931 */         boolean bool1 = localComponent1.getLocation().equals(localComponent2.getLocation());
/*  932 */         boolean bool2 = localComponent1.getSize().equals(localComponent2.getSize());
/*  933 */         if ((!bool1) && (!bool2)) {
/*  934 */           invokeStatement(paramObject1, "setBounds", new Object[] { localComponent1.getBounds() }, paramEncoder);
/*      */         }
/*  936 */         else if (!bool1) {
/*  937 */           invokeStatement(paramObject1, "setLocation", new Object[] { localComponent1.getLocation() }, paramEncoder);
/*      */         }
/*  939 */         else if (!bool2)
/*  940 */           invokeStatement(paramObject1, "setSize", new Object[] { localComponent1.getSize() }, paramEncoder);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_Container_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  949 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/*      */ 
/*  952 */       if ((paramObject1 instanceof JScrollPane)) {
/*  953 */         return;
/*      */       }
/*  955 */       Container localContainer1 = (Container)paramObject1;
/*  956 */       Component[] arrayOfComponent1 = localContainer1.getComponents();
/*  957 */       Container localContainer2 = (Container)paramObject2;
/*  958 */       Component[] arrayOfComponent2 = localContainer2 == null ? new Component[0] : localContainer2.getComponents();
/*      */ 
/*  960 */       Object localObject1 = (localContainer1.getLayout() instanceof BorderLayout) ? (BorderLayout)localContainer1.getLayout() : null;
/*      */ 
/*  964 */       Object localObject2 = (paramObject1 instanceof JLayeredPane) ? (JLayeredPane)paramObject1 : null;
/*      */ 
/*  969 */       for (int i = arrayOfComponent2.length; i < arrayOfComponent1.length; i++) {
/*  970 */         Object[] arrayOfObject = { localObject2 != null ? new Object[] { arrayOfComponent1[i], Integer.valueOf(localObject2.getLayer(arrayOfComponent1[i])), Integer.valueOf(-1) } : localObject1 != null ? new Object[] { arrayOfComponent1[i], localObject1.getConstraints(arrayOfComponent1[i]) } : arrayOfComponent1[i] };
/*      */ 
/*  976 */         invokeStatement(paramObject1, "add", arrayOfObject, paramEncoder);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_Font_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  744 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  748 */       Font localFont = (Font)paramObject;
/*      */ 
/*  750 */       int i = 0;
/*  751 */       String str = null;
/*  752 */       int j = 0;
/*  753 */       int k = 12;
/*      */ 
/*  755 */       Map localMap = localFont.getAttributes();
/*  756 */       HashMap localHashMap = new HashMap(localMap.size());
/*  757 */       for (Object localObject1 = localMap.keySet().iterator(); ((Iterator)localObject1).hasNext(); ) { Object localObject2 = ((Iterator)localObject1).next();
/*  758 */         Object localObject3 = localMap.get(localObject2);
/*  759 */         if (localObject3 != null) {
/*  760 */           localHashMap.put(localObject2, localObject3);
/*      */         }
/*  762 */         if (localObject2 == TextAttribute.FAMILY) {
/*  763 */           if ((localObject3 instanceof String)) {
/*  764 */             i++;
/*  765 */             str = (String)localObject3;
/*      */           }
/*      */         }
/*  768 */         else if (localObject2 == TextAttribute.WEIGHT) {
/*  769 */           if (TextAttribute.WEIGHT_REGULAR.equals(localObject3)) {
/*  770 */             i++;
/*  771 */           } else if (TextAttribute.WEIGHT_BOLD.equals(localObject3)) {
/*  772 */             i++;
/*  773 */             j |= 1;
/*      */           }
/*      */         }
/*  776 */         else if (localObject2 == TextAttribute.POSTURE) {
/*  777 */           if (TextAttribute.POSTURE_REGULAR.equals(localObject3)) {
/*  778 */             i++;
/*  779 */           } else if (TextAttribute.POSTURE_OBLIQUE.equals(localObject3)) {
/*  780 */             i++;
/*  781 */             j |= 2;
/*      */           }
/*  783 */         } else if ((localObject2 == TextAttribute.SIZE) && 
/*  784 */           ((localObject3 instanceof Number))) {
/*  785 */           Number localNumber = (Number)localObject3;
/*  786 */           k = localNumber.intValue();
/*  787 */           if (k == localNumber.floatValue()) {
/*  788 */             i++;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  793 */       localObject1 = localFont.getClass();
/*  794 */       if (i == localHashMap.size()) {
/*  795 */         return new Expression(localFont, localObject1, "new", new Object[] { str, Integer.valueOf(j), Integer.valueOf(k) });
/*      */       }
/*  797 */       if (localObject1 == Font.class) {
/*  798 */         return new Expression(localFont, localObject1, "getFont", new Object[] { localHashMap });
/*      */       }
/*  800 */       return new Expression(localFont, localObject1, "new", new Object[] { Font.getFont(localHashMap) });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_GridBagLayout_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1088 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1089 */       if (getHashtable(paramObject2).isEmpty())
/* 1090 */         for (Map.Entry localEntry : getHashtable(paramObject1).entrySet()) {
/* 1091 */           Object[] arrayOfObject = { localEntry.getKey(), localEntry.getValue() };
/* 1092 */           invokeStatement(paramObject1, "addLayoutComponent", arrayOfObject, paramEncoder);
/*      */         }
/*      */     }
/*      */ 
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2) {
/* 1097 */       return (super.mutatesTo(paramObject1, paramObject2)) && (getHashtable(paramObject2).isEmpty());
/*      */     }
/*      */     private static Hashtable<?, ?> getHashtable(Object paramObject) {
/* 1100 */       return (Hashtable)MetaData.getPrivateFieldValue(paramObject, "java.awt.GridBagLayout.comptable");
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_Insets_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  720 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  724 */       Insets localInsets = (Insets)paramObject;
/*  725 */       Object[] arrayOfObject = { Integer.valueOf(localInsets.top), Integer.valueOf(localInsets.left), Integer.valueOf(localInsets.bottom), Integer.valueOf(localInsets.right) };
/*      */ 
/*  731 */       return new Expression(localInsets, localInsets.getClass(), "new", arrayOfObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_List_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1020 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1021 */       java.awt.List localList1 = (java.awt.List)paramObject1;
/* 1022 */       java.awt.List localList2 = (java.awt.List)paramObject2;
/* 1023 */       for (int i = localList2.getItemCount(); i < localList1.getItemCount(); i++)
/* 1024 */         invokeStatement(paramObject1, "add", new Object[] { localList1.getItem(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_MenuBar_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1008 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1009 */       MenuBar localMenuBar1 = (MenuBar)paramObject1;
/* 1010 */       MenuBar localMenuBar2 = (MenuBar)paramObject2;
/* 1011 */       for (int i = localMenuBar2.getMenuCount(); i < localMenuBar1.getMenuCount(); i++)
/* 1012 */         invokeStatement(paramObject1, "add", new Object[] { localMenuBar1.getMenu(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_MenuShortcut_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  889 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  893 */       MenuShortcut localMenuShortcut = (MenuShortcut)paramObject;
/*  894 */       return new Expression(paramObject, localMenuShortcut.getClass(), "new", new Object[] { new Integer(localMenuShortcut.getKey()), Boolean.valueOf(localMenuShortcut.usesShiftModifier()) });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_Menu_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  996 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/*  997 */       Menu localMenu1 = (Menu)paramObject1;
/*  998 */       Menu localMenu2 = (Menu)paramObject2;
/*  999 */       for (int i = localMenu2.getItemCount(); i < localMenu1.getItemCount(); i++)
/* 1000 */         invokeStatement(paramObject1, "add", new Object[] { localMenu1.getItem(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_awt_SystemColor_PersistenceDelegate extends MetaData.StaticFieldsPersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static final class java_awt_font_TextAttribute_PersistenceDelegate extends MetaData.StaticFieldsPersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static final class java_beans_beancontext_BeanContextSupport_PersistenceDelegate extends MetaData.java_util_Collection_PersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static final class java_lang_Class_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  201 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  205 */       Class localClass = (Class)paramObject;
/*      */ 
/*  209 */       if (localClass.isPrimitive()) {
/*  210 */         localObject = null;
/*      */         try {
/*  212 */           localObject = PrimitiveWrapperMap.getType(localClass.getName()).getDeclaredField("TYPE");
/*      */         } catch (NoSuchFieldException localNoSuchFieldException) {
/*  214 */           System.err.println("Unknown primitive type: " + localClass);
/*      */         }
/*  216 */         return new Expression(paramObject, localObject, "get", new Object[] { null });
/*      */       }
/*  218 */       if (paramObject == String.class) {
/*  219 */         return new Expression(paramObject, "", "getClass", new Object[0]);
/*      */       }
/*  221 */       if (paramObject == Class.class) {
/*  222 */         return new Expression(paramObject, String.class, "getClass", new Object[0]);
/*      */       }
/*      */ 
/*  225 */       Object localObject = new Expression(paramObject, Class.class, "forName", new Object[] { localClass.getName() });
/*  226 */       ((Expression)localObject).loader = localClass.getClassLoader();
/*  227 */       return localObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_lang_String_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/*  191 */       return null;
/*      */     }
/*      */ 
/*      */     public void writeObject(Object paramObject, Encoder paramEncoder)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_lang_reflect_Field_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  235 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  239 */       Field localField = (Field)paramObject;
/*  240 */       return new Expression(paramObject, localField.getDeclaringClass(), "getField", new Object[] { localField.getName() });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_lang_reflect_Method_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  250 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  254 */       Method localMethod = (Method)paramObject;
/*  255 */       return new Expression(paramObject, localMethod.getDeclaringClass(), "getMethod", new Object[] { localMethod.getName(), localMethod.getParameterTypes() });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_sql_Timestamp_PersistenceDelegate extends MetaData.java_util_Date_PersistenceDelegate
/*      */   {
/*  296 */     private static final Method getNanosMethod = getNanosMethod();
/*      */ 
/*      */     private static Method getNanosMethod() {
/*      */       try {
/*  300 */         Class localClass = Class.forName("java.sql.Timestamp", true, null);
/*  301 */         return localClass.getMethod("getNanos", new Class[0]);
/*      */       } catch (ClassNotFoundException localClassNotFoundException) {
/*  303 */         return null;
/*      */       } catch (NoSuchMethodException localNoSuchMethodException) {
/*  305 */         throw new AssertionError(localNoSuchMethodException);
/*      */       }
/*      */     }
/*      */ 
/*      */     private static int getNanos(Object paramObject)
/*      */     {
/*  313 */       if (getNanosMethod == null)
/*  314 */         throw new AssertionError("Should not get here");
/*      */       try {
/*  316 */         return ((Integer)getNanosMethod.invoke(paramObject, new Object[0])).intValue();
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/*  318 */         Throwable localThrowable = localInvocationTargetException.getCause();
/*  319 */         if ((localThrowable instanceof RuntimeException))
/*  320 */           throw ((RuntimeException)localThrowable);
/*  321 */         if ((localThrowable instanceof Error))
/*  322 */           throw ((Error)localThrowable);
/*  323 */         throw new AssertionError(localInvocationTargetException);
/*      */       } catch (IllegalAccessException localIllegalAccessException) {
/*  325 */         throw new AssertionError(localIllegalAccessException);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  331 */       int i = getNanos(paramObject1);
/*  332 */       if (i != getNanos(paramObject2))
/*  333 */         paramEncoder.writeStatement(new Statement(paramObject1, "setNanos", new Object[] { Integer.valueOf(i) }));
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_util_AbstractCollection_PersistenceDelegate extends MetaData.java_util_Collection_PersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static final class java_util_AbstractList_PersistenceDelegate extends MetaData.java_util_List_PersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static final class java_util_AbstractMap_PersistenceDelegate extends MetaData.java_util_Map_PersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static class java_util_Collection_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  614 */       Collection localCollection1 = (Collection)paramObject1;
/*  615 */       Collection localCollection2 = (Collection)paramObject2;
/*      */ 
/*  617 */       if (localCollection2.size() != 0) {
/*  618 */         invokeStatement(paramObject1, "clear", new Object[0], paramEncoder);
/*      */       }
/*  620 */       for (Iterator localIterator = localCollection1.iterator(); localIterator.hasNext(); )
/*  621 */         invokeStatement(paramObject1, "add", new Object[] { localIterator.next() }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static abstract class java_util_Collections extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  361 */       if (!super.mutatesTo(paramObject1, paramObject2)) {
/*  362 */         return false;
/*      */       }
/*  364 */       if (((paramObject1 instanceof java.util.List)) || ((paramObject1 instanceof Set)) || ((paramObject1 instanceof Map))) {
/*  365 */         return paramObject1.equals(paramObject2);
/*      */       }
/*  367 */       Collection localCollection1 = (Collection)paramObject1;
/*  368 */       Collection localCollection2 = (Collection)paramObject2;
/*  369 */       return (localCollection1.size() == localCollection2.size()) && (localCollection1.containsAll(localCollection2));
/*      */     }
/*      */ 
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*      */     }
/*      */ 
/*      */     static final class CheckedCollection_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       CheckedCollection_PersistenceDelegate()
/*      */       {
/*  514 */         super();
/*      */       }
/*  516 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Object localObject = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedCollection.type");
/*  517 */         ArrayList localArrayList = new ArrayList((Collection)paramObject);
/*  518 */         return new Expression(paramObject, Collections.class, "checkedCollection", new Object[] { localArrayList, localObject }); } 
/*      */     }
/*      */ 
/*      */     static final class CheckedList_PersistenceDelegate extends MetaData.java_util_Collections {
/*  522 */       CheckedList_PersistenceDelegate() { super(); } 
/*      */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  524 */         Object localObject = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedCollection.type");
/*  525 */         LinkedList localLinkedList = new LinkedList((Collection)paramObject);
/*  526 */         return new Expression(paramObject, Collections.class, "checkedList", new Object[] { localLinkedList, localObject });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class CheckedMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       CheckedMap_PersistenceDelegate()
/*      */       {
/*  554 */         super();
/*      */       }
/*  556 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Object localObject1 = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedMap.keyType");
/*  557 */         Object localObject2 = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedMap.valueType");
/*  558 */         HashMap localHashMap = new HashMap((Map)paramObject);
/*  559 */         return new Expression(paramObject, Collections.class, "checkedMap", new Object[] { localHashMap, localObject1, localObject2 });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class CheckedRandomAccessList_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       CheckedRandomAccessList_PersistenceDelegate()
/*      */       {
/*  530 */         super();
/*      */       }
/*  532 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Object localObject = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedCollection.type");
/*  533 */         ArrayList localArrayList = new ArrayList((Collection)paramObject);
/*  534 */         return new Expression(paramObject, Collections.class, "checkedList", new Object[] { localArrayList, localObject }); } 
/*      */     }
/*      */ 
/*      */     static final class CheckedSet_PersistenceDelegate extends MetaData.java_util_Collections {
/*  538 */       CheckedSet_PersistenceDelegate() { super(); } 
/*      */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  540 */         Object localObject = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedCollection.type");
/*  541 */         HashSet localHashSet = new HashSet((Set)paramObject);
/*  542 */         return new Expression(paramObject, Collections.class, "checkedSet", new Object[] { localHashSet, localObject });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class CheckedSortedMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       CheckedSortedMap_PersistenceDelegate()
/*      */       {
/*  563 */         super();
/*      */       }
/*  565 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Object localObject1 = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedMap.keyType");
/*  566 */         Object localObject2 = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedMap.valueType");
/*  567 */         TreeMap localTreeMap = new TreeMap((SortedMap)paramObject);
/*  568 */         return new Expression(paramObject, Collections.class, "checkedSortedMap", new Object[] { localTreeMap, localObject1, localObject2 });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class CheckedSortedSet_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       CheckedSortedSet_PersistenceDelegate()
/*      */       {
/*  546 */         super();
/*      */       }
/*  548 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Object localObject = MetaData.getPrivateFieldValue(paramObject, "java.util.Collections$CheckedCollection.type");
/*  549 */         TreeSet localTreeSet = new TreeSet((SortedSet)paramObject);
/*  550 */         return new Expression(paramObject, Collections.class, "checkedSortedSet", new Object[] { localTreeSet, localObject });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class EmptyList_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       EmptyList_PersistenceDelegate()
/*      */       {
/*  376 */         super();
/*      */       }
/*  378 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { return new Expression(paramObject, Collections.class, "emptyList", null); }
/*      */ 
/*      */     }
/*      */ 
/*      */     static final class EmptyMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       EmptyMap_PersistenceDelegate()
/*      */       {
/*  388 */         super();
/*      */       }
/*  390 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { return new Expression(paramObject, Collections.class, "emptyMap", null); }
/*      */ 
/*      */     }
/*      */ 
/*      */     static final class EmptySet_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       EmptySet_PersistenceDelegate()
/*      */       {
/*  382 */         super();
/*      */       }
/*  384 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { return new Expression(paramObject, Collections.class, "emptySet", null); }
/*      */ 
/*      */     }
/*      */ 
/*      */     static final class SingletonList_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SingletonList_PersistenceDelegate()
/*      */       {
/*  394 */         super();
/*      */       }
/*  396 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { java.util.List localList = (java.util.List)paramObject;
/*  397 */         return new Expression(paramObject, Collections.class, "singletonList", new Object[] { localList.get(0) });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SingletonMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SingletonMap_PersistenceDelegate()
/*      */       {
/*  408 */         super();
/*      */       }
/*  410 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Map localMap = (Map)paramObject;
/*  411 */         Object localObject = localMap.keySet().iterator().next();
/*  412 */         return new Expression(paramObject, Collections.class, "singletonMap", new Object[] { localObject, localMap.get(localObject) });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SingletonSet_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SingletonSet_PersistenceDelegate()
/*      */       {
/*  401 */         super();
/*      */       }
/*  403 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { Set localSet = (Set)paramObject;
/*  404 */         return new Expression(paramObject, Collections.class, "singleton", new Object[] { localSet.iterator().next() });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SynchronizedCollection_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SynchronizedCollection_PersistenceDelegate()
/*      */       {
/*  465 */         super();
/*      */       }
/*  467 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { ArrayList localArrayList = new ArrayList((Collection)paramObject);
/*  468 */         return new Expression(paramObject, Collections.class, "synchronizedCollection", new Object[] { localArrayList }); } 
/*      */     }
/*      */ 
/*      */     static final class SynchronizedList_PersistenceDelegate extends MetaData.java_util_Collections {
/*  472 */       SynchronizedList_PersistenceDelegate() { super(); } 
/*      */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  474 */         LinkedList localLinkedList = new LinkedList((Collection)paramObject);
/*  475 */         return new Expression(paramObject, Collections.class, "synchronizedList", new Object[] { localLinkedList });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SynchronizedMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SynchronizedMap_PersistenceDelegate()
/*      */       {
/*  500 */         super();
/*      */       }
/*  502 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { HashMap localHashMap = new HashMap((Map)paramObject);
/*  503 */         return new Expression(paramObject, Collections.class, "synchronizedMap", new Object[] { localHashMap });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SynchronizedRandomAccessList_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SynchronizedRandomAccessList_PersistenceDelegate()
/*      */       {
/*  479 */         super();
/*      */       }
/*  481 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { ArrayList localArrayList = new ArrayList((Collection)paramObject);
/*  482 */         return new Expression(paramObject, Collections.class, "synchronizedList", new Object[] { localArrayList }); } 
/*      */     }
/*      */ 
/*      */     static final class SynchronizedSet_PersistenceDelegate extends MetaData.java_util_Collections {
/*  486 */       SynchronizedSet_PersistenceDelegate() { super(); } 
/*      */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  488 */         HashSet localHashSet = new HashSet((Set)paramObject);
/*  489 */         return new Expression(paramObject, Collections.class, "synchronizedSet", new Object[] { localHashSet });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SynchronizedSortedMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SynchronizedSortedMap_PersistenceDelegate()
/*      */       {
/*  507 */         super();
/*      */       }
/*  509 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { TreeMap localTreeMap = new TreeMap((SortedMap)paramObject);
/*  510 */         return new Expression(paramObject, Collections.class, "synchronizedSortedMap", new Object[] { localTreeMap });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class SynchronizedSortedSet_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       SynchronizedSortedSet_PersistenceDelegate()
/*      */       {
/*  493 */         super();
/*      */       }
/*  495 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { TreeSet localTreeSet = new TreeSet((SortedSet)paramObject);
/*  496 */         return new Expression(paramObject, Collections.class, "synchronizedSortedSet", new Object[] { localTreeSet });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableCollection_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       UnmodifiableCollection_PersistenceDelegate()
/*      */       {
/*  416 */         super();
/*      */       }
/*  418 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { ArrayList localArrayList = new ArrayList((Collection)paramObject);
/*  419 */         return new Expression(paramObject, Collections.class, "unmodifiableCollection", new Object[] { localArrayList }); } 
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableList_PersistenceDelegate extends MetaData.java_util_Collections {
/*  423 */       UnmodifiableList_PersistenceDelegate() { super(); } 
/*      */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  425 */         LinkedList localLinkedList = new LinkedList((Collection)paramObject);
/*  426 */         return new Expression(paramObject, Collections.class, "unmodifiableList", new Object[] { localLinkedList });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       UnmodifiableMap_PersistenceDelegate()
/*      */       {
/*  451 */         super();
/*      */       }
/*  453 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { HashMap localHashMap = new HashMap((Map)paramObject);
/*  454 */         return new Expression(paramObject, Collections.class, "unmodifiableMap", new Object[] { localHashMap });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableRandomAccessList_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       UnmodifiableRandomAccessList_PersistenceDelegate()
/*      */       {
/*  430 */         super();
/*      */       }
/*  432 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { ArrayList localArrayList = new ArrayList((Collection)paramObject);
/*  433 */         return new Expression(paramObject, Collections.class, "unmodifiableList", new Object[] { localArrayList }); } 
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableSet_PersistenceDelegate extends MetaData.java_util_Collections {
/*  437 */       UnmodifiableSet_PersistenceDelegate() { super(); } 
/*      */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  439 */         HashSet localHashSet = new HashSet((Set)paramObject);
/*  440 */         return new Expression(paramObject, Collections.class, "unmodifiableSet", new Object[] { localHashSet });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableSortedMap_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       UnmodifiableSortedMap_PersistenceDelegate()
/*      */       {
/*  458 */         super();
/*      */       }
/*  460 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { TreeMap localTreeMap = new TreeMap((SortedMap)paramObject);
/*  461 */         return new Expression(paramObject, Collections.class, "unmodifiableSortedMap", new Object[] { localTreeMap });
/*      */       }
/*      */     }
/*      */ 
/*      */     static final class UnmodifiableSortedSet_PersistenceDelegate extends MetaData.java_util_Collections
/*      */     {
/*      */       UnmodifiableSortedSet_PersistenceDelegate()
/*      */       {
/*  444 */         super();
/*      */       }
/*  446 */       protected Expression instantiate(Object paramObject, Encoder paramEncoder) { TreeSet localTreeSet = new TreeSet((SortedSet)paramObject);
/*  447 */         return new Expression(paramObject, Collections.class, "unmodifiableSortedSet", new Object[] { localTreeSet });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class java_util_Date_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  274 */       if (!super.mutatesTo(paramObject1, paramObject2)) {
/*  275 */         return false;
/*      */       }
/*  277 */       Date localDate1 = (Date)paramObject1;
/*  278 */       Date localDate2 = (Date)paramObject2;
/*      */ 
/*  280 */       return localDate1.getTime() == localDate2.getTime();
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  284 */       Date localDate = (Date)paramObject;
/*  285 */       return new Expression(localDate, localDate.getClass(), "new", new Object[] { Long.valueOf(localDate.getTime()) });
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_util_EnumMap_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  580 */       return (super.mutatesTo(paramObject1, paramObject2)) && (getType(paramObject1) == getType(paramObject2));
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  584 */       return new Expression(paramObject, EnumMap.class, "new", new Object[] { getType(paramObject) });
/*      */     }
/*      */ 
/*      */     private static Object getType(Object paramObject) {
/*  588 */       return MetaData.getPrivateFieldValue(paramObject, "java.util.EnumMap.keyType");
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_util_EnumSet_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/*  599 */       return (super.mutatesTo(paramObject1, paramObject2)) && (getType(paramObject1) == getType(paramObject2));
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/*  603 */       return new Expression(paramObject, EnumSet.class, "noneOf", new Object[] { getType(paramObject) });
/*      */     }
/*      */ 
/*      */     private static Object getType(Object paramObject) {
/*  607 */       return MetaData.getPrivateFieldValue(paramObject, "java.util.EnumSet.elementType");
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class java_util_Hashtable_PersistenceDelegate extends MetaData.java_util_Map_PersistenceDelegate
/*      */   {
/*      */   }
/*      */ 
/*      */   static class java_util_List_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  629 */       java.util.List localList1 = (java.util.List)paramObject1;
/*  630 */       java.util.List localList2 = (java.util.List)paramObject2;
/*  631 */       int i = localList1.size();
/*  632 */       int j = localList2 == null ? 0 : localList2.size();
/*  633 */       if (i < j) {
/*  634 */         invokeStatement(paramObject1, "clear", new Object[0], paramEncoder);
/*  635 */         j = 0;
/*      */       }
/*  637 */       for (int k = 0; k < j; k++) {
/*  638 */         Integer localInteger = new Integer(k);
/*      */ 
/*  640 */         Expression localExpression1 = new Expression(paramObject1, "get", new Object[] { localInteger });
/*  641 */         Expression localExpression2 = new Expression(paramObject2, "get", new Object[] { localInteger });
/*      */         try {
/*  643 */           Object localObject1 = localExpression1.getValue();
/*  644 */           Object localObject2 = localExpression2.getValue();
/*  645 */           paramEncoder.writeExpression(localExpression1);
/*  646 */           if (!Objects.equals(localObject2, paramEncoder.get(localObject1)))
/*  647 */             invokeStatement(paramObject1, "set", new Object[] { localInteger, localObject1 }, paramEncoder);
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  651 */           paramEncoder.getExceptionListener().exceptionThrown(localException);
/*      */         }
/*      */       }
/*  654 */       for (k = j; k < i; k++)
/*  655 */         invokeStatement(paramObject1, "add", new Object[] { localList1.get(k) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class java_util_Map_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/*  665 */       Map localMap1 = (Map)paramObject1;
/*  666 */       Map localMap2 = (Map)paramObject2;
/*      */       Object localObject3;
/*  669 */       if (localMap2 != null) {
/*  670 */         for (localObject3 : localMap2.keySet().toArray())
/*      */         {
/*  672 */           if (!localMap1.containsKey(localObject3)) {
/*  673 */             invokeStatement(paramObject1, "remove", new Object[] { localObject3 }, paramEncoder);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  678 */       for (??? = localMap1.keySet().iterator(); ((Iterator)???).hasNext(); ) { Object localObject2 = ((Iterator)???).next();
/*  679 */         Expression localExpression = new Expression(paramObject1, "get", new Object[] { localObject2 });
/*      */ 
/*  681 */         localObject3 = new Expression(paramObject2, "get", new Object[] { localObject2 });
/*      */         try {
/*  683 */           Object localObject4 = localExpression.getValue();
/*  684 */           Object localObject5 = ((Expression)localObject3).getValue();
/*  685 */           paramEncoder.writeExpression(localExpression);
/*  686 */           if (!Objects.equals(localObject5, paramEncoder.get(localObject4)))
/*  687 */             invokeStatement(paramObject1, "put", new Object[] { localObject2, localObject4 }, paramEncoder);
/*  688 */           else if ((localObject5 == null) && (!localMap2.containsKey(localObject2)))
/*      */           {
/*  690 */             invokeStatement(paramObject1, "put", new Object[] { localObject2, localObject4 }, paramEncoder);
/*      */           }
/*      */         }
/*      */         catch (Exception localException) {
/*  694 */           paramEncoder.getExceptionListener().exceptionThrown(localException);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_Box_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/* 1196 */       return (super.mutatesTo(paramObject1, paramObject2)) && (getAxis(paramObject1).equals(getAxis(paramObject2)));
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/* 1200 */       return new Expression(paramObject, paramObject.getClass(), "new", new Object[] { getAxis(paramObject) });
/*      */     }
/*      */ 
/*      */     private Integer getAxis(Object paramObject) {
/* 1204 */       Box localBox = (Box)paramObject;
/* 1205 */       return (Integer)MetaData.getPrivateFieldValue(localBox.getLayout(), "javax.swing.BoxLayout.axis");
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_DefaultComboBoxModel_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1145 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1146 */       DefaultComboBoxModel localDefaultComboBoxModel = (DefaultComboBoxModel)paramObject1;
/* 1147 */       for (int i = 0; i < localDefaultComboBoxModel.getSize(); i++)
/* 1148 */         invokeStatement(paramObject1, "addElement", new Object[] { localDefaultComboBoxModel.getElementAt(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_DefaultListModel_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1132 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1133 */       DefaultListModel localDefaultListModel1 = (DefaultListModel)paramObject1;
/* 1134 */       DefaultListModel localDefaultListModel2 = (DefaultListModel)paramObject2;
/* 1135 */       for (int i = localDefaultListModel2.getSize(); i < localDefaultListModel1.getSize(); i++)
/* 1136 */         invokeStatement(paramObject1, "add", new Object[] { localDefaultListModel1.getElementAt(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_JFrame_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1111 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1112 */       Window localWindow1 = (Window)paramObject1;
/* 1113 */       Window localWindow2 = (Window)paramObject2;
/* 1114 */       boolean bool1 = localWindow1.isVisible();
/* 1115 */       boolean bool2 = localWindow2.isVisible();
/* 1116 */       if (bool2 != bool1)
/*      */       {
/* 1118 */         boolean bool3 = paramEncoder.executeStatements;
/* 1119 */         paramEncoder.executeStatements = false;
/* 1120 */         invokeStatement(paramObject1, "setVisible", new Object[] { Boolean.valueOf(bool1) }, paramEncoder);
/* 1121 */         paramEncoder.executeStatements = bool3;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_JMenu_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1217 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1218 */       JMenu localJMenu = (JMenu)paramObject1;
/* 1219 */       Component[] arrayOfComponent = localJMenu.getMenuComponents();
/* 1220 */       for (int i = 0; i < arrayOfComponent.length; i++)
/* 1221 */         invokeStatement(paramObject1, "add", new Object[] { arrayOfComponent[i] }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_JTabbedPane_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1181 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1182 */       JTabbedPane localJTabbedPane = (JTabbedPane)paramObject1;
/* 1183 */       for (int i = 0; i < localJTabbedPane.getTabCount(); i++)
/* 1184 */         invokeStatement(paramObject1, "addTab", new Object[] { localJTabbedPane.getTitleAt(i), localJTabbedPane.getIconAt(i), localJTabbedPane.getComponentAt(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_ToolTipManager_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/* 1173 */       return new Expression(paramObject, ToolTipManager.class, "sharedInstance", new Object[0]);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_border_MatteBorder_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder)
/*      */     {
/* 1235 */       MatteBorder localMatteBorder = (MatteBorder)paramObject;
/* 1236 */       Insets localInsets = localMatteBorder.getBorderInsets();
/* 1237 */       Object localObject = localMatteBorder.getTileIcon();
/* 1238 */       if (localObject == null) {
/* 1239 */         localObject = localMatteBorder.getMatteColor();
/*      */       }
/* 1241 */       Object[] arrayOfObject = { Integer.valueOf(localInsets.top), Integer.valueOf(localInsets.left), Integer.valueOf(localInsets.bottom), Integer.valueOf(localInsets.right), localObject };
/*      */ 
/* 1248 */       return new Expression(localMatteBorder, localMatteBorder.getClass(), "new", arrayOfObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class javax_swing_tree_DefaultMutableTreeNode_PersistenceDelegate extends DefaultPersistenceDelegate
/*      */   {
/*      */     protected void initialize(Class<?> paramClass, Object paramObject1, Object paramObject2, Encoder paramEncoder)
/*      */     {
/* 1158 */       super.initialize(paramClass, paramObject1, paramObject2, paramEncoder);
/* 1159 */       DefaultMutableTreeNode localDefaultMutableTreeNode1 = (DefaultMutableTreeNode)paramObject1;
/*      */ 
/* 1161 */       DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)paramObject2;
/*      */ 
/* 1163 */       for (int i = localDefaultMutableTreeNode2.getChildCount(); i < localDefaultMutableTreeNode1.getChildCount(); i++)
/* 1164 */         invokeStatement(paramObject1, "add", new Object[] { localDefaultMutableTreeNode1.getChildAt(i) }, paramEncoder);
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class sun_swing_PrintColorUIResource_PersistenceDelegate extends PersistenceDelegate
/*      */   {
/*      */     protected boolean mutatesTo(Object paramObject1, Object paramObject2)
/*      */     {
/* 1277 */       return paramObject1.equals(paramObject2);
/*      */     }
/*      */ 
/*      */     protected Expression instantiate(Object paramObject, Encoder paramEncoder) {
/* 1281 */       Color localColor = (Color)paramObject;
/* 1282 */       Object[] arrayOfObject = { Integer.valueOf(localColor.getRGB()) };
/* 1283 */       return new Expression(localColor, ColorUIResource.class, "new", arrayOfObject);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.MetaData
 * JD-Core Version:    0.6.2
 */
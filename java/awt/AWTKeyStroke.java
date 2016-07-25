/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.awt.AppContext;
/*     */ 
/*     */ public class AWTKeyStroke
/*     */   implements Serializable
/*     */ {
/*     */   static final long serialVersionUID = -6430539691155161871L;
/*     */   private static Map modifierKeywords;
/*     */   private static VKCollection vks;
/*     */   private static Object APP_CONTEXT_CACHE_KEY;
/*     */   private static AWTKeyStroke APP_CONTEXT_KEYSTROKE_KEY;
/*  98 */   private char keyChar = 65535;
/*  99 */   private int keyCode = 0;
/*     */   private int modifiers;
/*     */   private boolean onKeyRelease;
/*     */ 
/*     */   private static Class getAWTKeyStrokeClass()
/*     */   {
/*  89 */     AppContext localAppContext = AppContext.getAppContext();
/*  90 */     Object localObject = (Class)localAppContext.get(AWTKeyStroke.class);
/*  91 */     if (localObject == null) {
/*  92 */       localObject = AWTKeyStroke.class;
/*  93 */       localAppContext.put(AWTKeyStroke.class, AWTKeyStroke.class);
/*     */     }
/*  95 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected AWTKeyStroke()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected AWTKeyStroke(char paramChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 156 */     this.keyChar = paramChar;
/* 157 */     this.keyCode = paramInt1;
/* 158 */     this.modifiers = paramInt2;
/* 159 */     this.onKeyRelease = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected static void registerSubclass(Class<?> paramClass)
/*     */   {
/* 182 */     if (paramClass == null) {
/* 183 */       throw new IllegalArgumentException("subclass cannot be null");
/*     */     }
/* 185 */     AppContext localAppContext = AppContext.getAppContext();
/* 186 */     synchronized (AWTKeyStroke.class) {
/* 187 */       localObject1 = (Class)localAppContext.get(AWTKeyStroke.class);
/* 188 */       if ((localObject1 != null) && (localObject1.equals(paramClass)))
/*     */       {
/* 190 */         return;
/*     */       }
/*     */     }
/* 193 */     if (!AWTKeyStroke.class.isAssignableFrom(paramClass)) {
/* 194 */       throw new ClassCastException("subclass is not derived from AWTKeyStroke");
/*     */     }
/*     */ 
/* 197 */     ??? = getCtor(paramClass);
/*     */ 
/* 199 */     Object localObject1 = "subclass could not be instantiated";
/*     */ 
/* 201 */     if (??? == null)
/* 202 */       throw new IllegalArgumentException((String)localObject1);
/*     */     try
/*     */     {
/* 205 */       AWTKeyStroke localAWTKeyStroke = (AWTKeyStroke)((Constructor)???).newInstance((Object[])null);
/* 206 */       if (localAWTKeyStroke == null)
/* 207 */         throw new IllegalArgumentException((String)localObject1);
/*     */     }
/*     */     catch (NoSuchMethodError localNoSuchMethodError) {
/* 210 */       throw new IllegalArgumentException((String)localObject1);
/*     */     } catch (ExceptionInInitializerError localExceptionInInitializerError) {
/* 212 */       throw new IllegalArgumentException((String)localObject1);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 214 */       throw new IllegalArgumentException((String)localObject1);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 216 */       throw new IllegalArgumentException((String)localObject1);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 218 */       throw new IllegalArgumentException((String)localObject1);
/*     */     }
/*     */ 
/* 221 */     synchronized (AWTKeyStroke.class) {
/* 222 */       localAppContext.put(AWTKeyStroke.class, paramClass);
/* 223 */       localAppContext.remove(APP_CONTEXT_CACHE_KEY);
/* 224 */       localAppContext.remove(APP_CONTEXT_KEYSTROKE_KEY);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Constructor getCtor(Class paramClass)
/*     */   {
/* 234 */     Object localObject = AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/*     */         try {
/* 237 */           Constructor localConstructor = this.val$clazz.getDeclaredConstructor((Class[])null);
/* 238 */           if (localConstructor != null) {
/* 239 */             localConstructor.setAccessible(true);
/*     */           }
/* 241 */           return localConstructor;
/*     */         } catch (SecurityException localSecurityException) {
/*     */         } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */         }
/* 245 */         return null;
/*     */       }
/*     */     });
/* 248 */     return (Constructor)localObject;
/*     */   }
/*     */ 
/*     */   private static synchronized AWTKeyStroke getCachedStroke(char paramChar, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 254 */     AppContext localAppContext = AppContext.getAppContext();
/* 255 */     Object localObject = (Map)localAppContext.get(APP_CONTEXT_CACHE_KEY);
/* 256 */     AWTKeyStroke localAWTKeyStroke1 = (AWTKeyStroke)localAppContext.get(APP_CONTEXT_KEYSTROKE_KEY);
/*     */ 
/* 258 */     if (localObject == null) {
/* 259 */       localObject = new HashMap();
/* 260 */       localAppContext.put(APP_CONTEXT_CACHE_KEY, localObject);
/*     */     }
/*     */ 
/* 263 */     if (localAWTKeyStroke1 == null) {
/*     */       try {
/* 265 */         Class localClass = getAWTKeyStrokeClass();
/* 266 */         localAWTKeyStroke1 = (AWTKeyStroke)getCtor(localClass).newInstance((Object[])null);
/* 267 */         localAppContext.put(APP_CONTEXT_KEYSTROKE_KEY, localAWTKeyStroke1);
/*     */       } catch (InstantiationException localInstantiationException) {
/* 269 */         if (!$assertionsDisabled) throw new AssertionError(); 
/*     */       }
/* 271 */       catch (IllegalAccessException localIllegalAccessException) { if (!$assertionsDisabled) throw new AssertionError();  } catch (InvocationTargetException localInvocationTargetException)
/*     */       {
/* 273 */         if (!$assertionsDisabled) throw new AssertionError();
/*     */       }
/*     */     }
/* 276 */     localAWTKeyStroke1.keyChar = paramChar;
/* 277 */     localAWTKeyStroke1.keyCode = paramInt1;
/* 278 */     localAWTKeyStroke1.modifiers = mapNewModifiers(mapOldModifiers(paramInt2));
/* 279 */     localAWTKeyStroke1.onKeyRelease = paramBoolean;
/*     */ 
/* 281 */     AWTKeyStroke localAWTKeyStroke2 = (AWTKeyStroke)((Map)localObject).get(localAWTKeyStroke1);
/* 282 */     if (localAWTKeyStroke2 == null) {
/* 283 */       localAWTKeyStroke2 = localAWTKeyStroke1;
/* 284 */       ((Map)localObject).put(localAWTKeyStroke2, localAWTKeyStroke2);
/* 285 */       localAppContext.remove(APP_CONTEXT_KEYSTROKE_KEY);
/*     */     }
/* 287 */     return localAWTKeyStroke2;
/*     */   }
/*     */ 
/*     */   public static AWTKeyStroke getAWTKeyStroke(char paramChar)
/*     */   {
/* 299 */     return getCachedStroke(paramChar, 0, 0, false);
/*     */   }
/*     */ 
/*     */   public static AWTKeyStroke getAWTKeyStroke(Character paramCharacter, int paramInt)
/*     */   {
/* 341 */     if (paramCharacter == null) {
/* 342 */       throw new IllegalArgumentException("keyChar cannot be null");
/*     */     }
/* 344 */     return getCachedStroke(paramCharacter.charValue(), 0, paramInt, false);
/*     */   }
/*     */ 
/*     */   public static AWTKeyStroke getAWTKeyStroke(int paramInt1, int paramInt2, boolean paramBoolean)
/*     */   {
/* 394 */     return getCachedStroke(65535, paramInt1, paramInt2, paramBoolean);
/*     */   }
/*     */ 
/*     */   public static AWTKeyStroke getAWTKeyStroke(int paramInt1, int paramInt2)
/*     */   {
/* 438 */     return getCachedStroke(65535, paramInt1, paramInt2, false);
/*     */   }
/*     */ 
/*     */   public static AWTKeyStroke getAWTKeyStrokeForEvent(KeyEvent paramKeyEvent)
/*     */   {
/* 457 */     int i = paramKeyEvent.getID();
/* 458 */     switch (i) {
/*     */     case 401:
/*     */     case 402:
/* 461 */       return getCachedStroke(65535, paramKeyEvent.getKeyCode(), paramKeyEvent.getModifiers(), i == 402);
/*     */     case 400:
/* 466 */       return getCachedStroke(paramKeyEvent.getKeyChar(), 0, paramKeyEvent.getModifiers(), false);
/*     */     }
/*     */ 
/* 472 */     return null;
/*     */   }
/*     */ 
/*     */   public static AWTKeyStroke getAWTKeyStroke(String paramString)
/*     */   {
/* 504 */     if (paramString == null) {
/* 505 */       throw new IllegalArgumentException("String cannot be null");
/*     */     }
/*     */ 
/* 510 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
/*     */ 
/* 512 */     int i = 0;
/* 513 */     boolean bool = false;
/* 514 */     int j = 0;
/* 515 */     int k = 0;
/*     */ 
/* 517 */     synchronized (AWTKeyStroke.class) {
/* 518 */       if (modifierKeywords == null) {
/* 519 */         HashMap localHashMap = new HashMap(8, 1.0F);
/* 520 */         localHashMap.put("shift", Integer.valueOf(65));
/*     */ 
/* 523 */         localHashMap.put("control", Integer.valueOf(130));
/*     */ 
/* 526 */         localHashMap.put("ctrl", Integer.valueOf(130));
/*     */ 
/* 529 */         localHashMap.put("meta", Integer.valueOf(260));
/*     */ 
/* 532 */         localHashMap.put("alt", Integer.valueOf(520));
/*     */ 
/* 535 */         localHashMap.put("altGraph", Integer.valueOf(8224));
/*     */ 
/* 538 */         localHashMap.put("button1", Integer.valueOf(1024));
/*     */ 
/* 540 */         localHashMap.put("button2", Integer.valueOf(2048));
/*     */ 
/* 542 */         localHashMap.put("button3", Integer.valueOf(4096));
/*     */ 
/* 544 */         modifierKeywords = Collections.synchronizedMap(localHashMap);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 549 */     ??? = localStringTokenizer.countTokens();
/*     */ 
/* 551 */     for (Object localObject1 = 1; localObject1 <= ???; localObject1++) {
/* 552 */       String str = localStringTokenizer.nextToken();
/*     */ 
/* 554 */       if (j != 0) {
/* 555 */         if ((str.length() != 1) || (localObject1 != ???)) {
/* 556 */           throw new IllegalArgumentException("String formatted incorrectly");
/*     */         }
/* 558 */         return getCachedStroke(str.charAt(0), 0, i, false);
/*     */       }
/*     */       Object localObject3;
/* 562 */       if ((k != 0) || (bool) || (localObject1 == ???)) {
/* 563 */         if (localObject1 != ???) {
/* 564 */           throw new IllegalArgumentException("String formatted incorrectly");
/*     */         }
/*     */ 
/* 567 */         localObject3 = "VK_" + str;
/* 568 */         int m = getVKValue((String)localObject3);
/*     */ 
/* 570 */         return getCachedStroke(65535, m, i, bool);
/*     */       }
/*     */ 
/* 574 */       if (str.equals("released")) {
/* 575 */         bool = true;
/*     */       }
/* 578 */       else if (str.equals("pressed")) {
/* 579 */         k = 1;
/*     */       }
/* 582 */       else if (str.equals("typed")) {
/* 583 */         j = 1;
/*     */       }
/*     */       else
/*     */       {
/* 587 */         localObject3 = (Integer)modifierKeywords.get(str);
/* 588 */         if (localObject3 != null)
/* 589 */           i |= ((Integer)localObject3).intValue();
/*     */         else {
/* 591 */           throw new IllegalArgumentException("String formatted incorrectly");
/*     */         }
/*     */       }
/*     */     }
/* 595 */     throw new IllegalArgumentException("String formatted incorrectly");
/*     */   }
/*     */ 
/*     */   private static VKCollection getVKCollection() {
/* 599 */     if (vks == null) {
/* 600 */       vks = new VKCollection();
/*     */     }
/* 602 */     return vks;
/*     */   }
/*     */ 
/*     */   private static int getVKValue(String paramString)
/*     */   {
/* 611 */     VKCollection localVKCollection = getVKCollection();
/*     */ 
/* 613 */     Integer localInteger = localVKCollection.findCode(paramString);
/*     */ 
/* 615 */     if (localInteger == null) {
/* 616 */       int i = 0;
/*     */       try
/*     */       {
/* 620 */         i = KeyEvent.class.getField(paramString).getInt(KeyEvent.class);
/*     */       } catch (NoSuchFieldException localNoSuchFieldException) {
/* 622 */         throw new IllegalArgumentException("String formatted incorrectly");
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 624 */         throw new IllegalArgumentException("String formatted incorrectly");
/*     */       }
/* 626 */       localInteger = Integer.valueOf(i);
/* 627 */       localVKCollection.put(paramString, localInteger);
/*     */     }
/* 629 */     return localInteger.intValue();
/*     */   }
/*     */ 
/*     */   public final char getKeyChar()
/*     */   {
/* 640 */     return this.keyChar;
/*     */   }
/*     */ 
/*     */   public final int getKeyCode()
/*     */   {
/* 651 */     return this.keyCode;
/*     */   }
/*     */ 
/*     */   public final int getModifiers()
/*     */   {
/* 661 */     return this.modifiers;
/*     */   }
/*     */ 
/*     */   public final boolean isOnKeyRelease()
/*     */   {
/* 672 */     return this.onKeyRelease;
/*     */   }
/*     */ 
/*     */   public final int getKeyEventType()
/*     */   {
/* 685 */     if (this.keyCode == 0) {
/* 686 */       return 400;
/*     */     }
/* 688 */     return this.onKeyRelease ? 402 : 401;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 701 */     return (this.keyChar + '\001') * (2 * (this.keyCode + 1)) * (this.modifiers + 1) + (this.onKeyRelease ? 1 : 2);
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object paramObject)
/*     */   {
/* 712 */     if ((paramObject instanceof AWTKeyStroke)) {
/* 713 */       AWTKeyStroke localAWTKeyStroke = (AWTKeyStroke)paramObject;
/* 714 */       return (localAWTKeyStroke.keyChar == this.keyChar) && (localAWTKeyStroke.keyCode == this.keyCode) && (localAWTKeyStroke.onKeyRelease == this.onKeyRelease) && (localAWTKeyStroke.modifiers == this.modifiers);
/*     */     }
/*     */ 
/* 718 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 731 */     if (this.keyCode == 0) {
/* 732 */       return getModifiersText(this.modifiers) + "typed " + this.keyChar;
/*     */     }
/* 734 */     return getModifiersText(this.modifiers) + (this.onKeyRelease ? "released" : "pressed") + " " + getVKText(this.keyCode);
/*     */   }
/*     */ 
/*     */   static String getModifiersText(int paramInt)
/*     */   {
/* 741 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 743 */     if ((paramInt & 0x40) != 0) {
/* 744 */       localStringBuilder.append("shift ");
/*     */     }
/* 746 */     if ((paramInt & 0x80) != 0) {
/* 747 */       localStringBuilder.append("ctrl ");
/*     */     }
/* 749 */     if ((paramInt & 0x100) != 0) {
/* 750 */       localStringBuilder.append("meta ");
/*     */     }
/* 752 */     if ((paramInt & 0x200) != 0) {
/* 753 */       localStringBuilder.append("alt ");
/*     */     }
/* 755 */     if ((paramInt & 0x2000) != 0) {
/* 756 */       localStringBuilder.append("altGraph ");
/*     */     }
/* 758 */     if ((paramInt & 0x400) != 0) {
/* 759 */       localStringBuilder.append("button1 ");
/*     */     }
/* 761 */     if ((paramInt & 0x800) != 0) {
/* 762 */       localStringBuilder.append("button2 ");
/*     */     }
/* 764 */     if ((paramInt & 0x1000) != 0) {
/* 765 */       localStringBuilder.append("button3 ");
/*     */     }
/*     */ 
/* 768 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   static String getVKText(int paramInt) {
/* 772 */     VKCollection localVKCollection = getVKCollection();
/* 773 */     Integer localInteger = Integer.valueOf(paramInt);
/* 774 */     String str = localVKCollection.findName(localInteger);
/* 775 */     if (str != null) {
/* 776 */       return str.substring(3);
/*     */     }
/* 778 */     int i = 25;
/*     */ 
/* 781 */     Field[] arrayOfField = KeyEvent.class.getDeclaredFields();
/* 782 */     for (int j = 0; j < arrayOfField.length; j++) {
/*     */       try {
/* 784 */         if ((arrayOfField[j].getModifiers() == i) && (arrayOfField[j].getType() == Integer.TYPE) && (arrayOfField[j].getName().startsWith("VK_")) && (arrayOfField[j].getInt(KeyEvent.class) == paramInt))
/*     */         {
/* 789 */           str = arrayOfField[j].getName();
/* 790 */           localVKCollection.put(str, localInteger);
/* 791 */           return str.substring(3);
/*     */         }
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 794 */         if (!$assertionsDisabled) throw new AssertionError();
/*     */       }
/*     */     }
/* 797 */     return "UNKNOWN";
/*     */   }
/*     */ 
/*     */   protected Object readResolve()
/*     */     throws ObjectStreamException
/*     */   {
/* 807 */     synchronized (AWTKeyStroke.class) {
/* 808 */       if (getClass().equals(getAWTKeyStrokeClass())) {
/* 809 */         return getCachedStroke(this.keyChar, this.keyCode, this.modifiers, this.onKeyRelease);
/*     */       }
/*     */     }
/* 812 */     return this;
/*     */   }
/*     */ 
/*     */   private static int mapOldModifiers(int paramInt) {
/* 816 */     if ((paramInt & 0x1) != 0) {
/* 817 */       paramInt |= 64;
/*     */     }
/* 819 */     if ((paramInt & 0x8) != 0) {
/* 820 */       paramInt |= 512;
/*     */     }
/* 822 */     if ((paramInt & 0x20) != 0) {
/* 823 */       paramInt |= 8192;
/*     */     }
/* 825 */     if ((paramInt & 0x2) != 0) {
/* 826 */       paramInt |= 128;
/*     */     }
/* 828 */     if ((paramInt & 0x4) != 0) {
/* 829 */       paramInt |= 256;
/*     */     }
/*     */ 
/* 832 */     paramInt &= 16320;
/*     */ 
/* 841 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private static int mapNewModifiers(int paramInt) {
/* 845 */     if ((paramInt & 0x40) != 0) {
/* 846 */       paramInt |= 1;
/*     */     }
/* 848 */     if ((paramInt & 0x200) != 0) {
/* 849 */       paramInt |= 8;
/*     */     }
/* 851 */     if ((paramInt & 0x2000) != 0) {
/* 852 */       paramInt |= 32;
/*     */     }
/* 854 */     if ((paramInt & 0x80) != 0) {
/* 855 */       paramInt |= 2;
/*     */     }
/* 857 */     if ((paramInt & 0x100) != 0) {
/* 858 */       paramInt |= 4;
/*     */     }
/*     */ 
/* 861 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  79 */     APP_CONTEXT_CACHE_KEY = new Object();
/*     */ 
/*  81 */     APP_CONTEXT_KEYSTROKE_KEY = new AWTKeyStroke();
/*     */ 
/* 105 */     Toolkit.loadLibraries();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.AWTKeyStroke
 * JD-Core Version:    0.6.2
 */
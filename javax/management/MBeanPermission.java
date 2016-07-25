/*      */ package javax.management;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.security.Permission;
/*      */ 
/*      */ public class MBeanPermission extends Permission
/*      */ {
/*      */   private static final long serialVersionUID = -2416928705275160661L;
/*      */   private static final int AddNotificationListener = 1;
/*      */   private static final int GetAttribute = 2;
/*      */   private static final int GetClassLoader = 4;
/*      */   private static final int GetClassLoaderFor = 8;
/*      */   private static final int GetClassLoaderRepository = 16;
/*      */   private static final int GetDomains = 32;
/*      */   private static final int GetMBeanInfo = 64;
/*      */   private static final int GetObjectInstance = 128;
/*      */   private static final int Instantiate = 256;
/*      */   private static final int Invoke = 512;
/*      */   private static final int IsInstanceOf = 1024;
/*      */   private static final int QueryMBeans = 2048;
/*      */   private static final int QueryNames = 4096;
/*      */   private static final int RegisterMBean = 8192;
/*      */   private static final int RemoveNotificationListener = 16384;
/*      */   private static final int SetAttribute = 32768;
/*      */   private static final int UnregisterMBean = 65536;
/*      */   private static final int NONE = 0;
/*      */   private static final int ALL = 131071;
/*      */   private String actions;
/*      */   private transient int mask;
/*      */   private transient String classNamePrefix;
/*      */   private transient boolean classNameExactMatch;
/*      */   private transient String member;
/*      */   private transient ObjectName objectName;
/*      */ 
/*      */   private void parseActions()
/*      */   {
/*  256 */     if (this.actions == null) {
/*  257 */       throw new IllegalArgumentException("MBeanPermission: actions can't be null");
/*      */     }
/*  259 */     if (this.actions.equals("")) {
/*  260 */       throw new IllegalArgumentException("MBeanPermission: actions can't be empty");
/*      */     }
/*      */ 
/*  263 */     int i = getMask(this.actions);
/*      */ 
/*  265 */     if ((i & 0x1FFFF) != i)
/*  266 */       throw new IllegalArgumentException("Invalid actions mask");
/*  267 */     if (i == 0)
/*  268 */       throw new IllegalArgumentException("Invalid actions mask");
/*  269 */     this.mask = i;
/*      */   }
/*      */ 
/*      */   private void parseName()
/*      */   {
/*  276 */     String str1 = getName();
/*      */ 
/*  278 */     if (str1 == null) {
/*  279 */       throw new IllegalArgumentException("MBeanPermission name cannot be null");
/*      */     }
/*      */ 
/*  282 */     if (str1.equals("")) {
/*  283 */       throw new IllegalArgumentException("MBeanPermission name cannot be empty");
/*      */     }
/*      */ 
/*  293 */     int i = str1.indexOf("[");
/*  294 */     if (i == -1)
/*      */     {
/*  297 */       this.objectName = ObjectName.WILDCARD;
/*      */     } else {
/*  299 */       if (!str1.endsWith("]")) {
/*  300 */         throw new IllegalArgumentException("MBeanPermission: The ObjectName in the target name must be included in square brackets");
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  311 */         String str2 = str1.substring(i + 1, str1.length() - 1);
/*      */ 
/*  313 */         if (str2.equals(""))
/*  314 */           this.objectName = ObjectName.WILDCARD;
/*  315 */         else if (str2.equals("-"))
/*  316 */           this.objectName = null;
/*      */         else
/*  318 */           this.objectName = new ObjectName(str2);
/*      */       } catch (MalformedObjectNameException localMalformedObjectNameException) {
/*  320 */         throw new IllegalArgumentException("MBeanPermission: The target name does not specify a valid ObjectName", localMalformedObjectNameException);
/*      */       }
/*      */ 
/*  327 */       str1 = str1.substring(0, i);
/*      */     }
/*      */ 
/*  332 */     int j = str1.indexOf("#");
/*      */ 
/*  334 */     if (j == -1) {
/*  335 */       setMember("*");
/*      */     } else {
/*  337 */       String str3 = str1.substring(j + 1);
/*  338 */       setMember(str3);
/*  339 */       str1 = str1.substring(0, j);
/*      */     }
/*      */ 
/*  344 */     setClassName(str1);
/*      */   }
/*      */ 
/*      */   private void initName(String paramString1, String paramString2, ObjectName paramObjectName)
/*      */   {
/*  353 */     setClassName(paramString1);
/*  354 */     setMember(paramString2);
/*  355 */     this.objectName = paramObjectName;
/*      */   }
/*      */ 
/*      */   private void setClassName(String paramString) {
/*  359 */     if ((paramString == null) || (paramString.equals("-"))) {
/*  360 */       this.classNamePrefix = null;
/*  361 */       this.classNameExactMatch = false;
/*  362 */     } else if ((paramString.equals("")) || (paramString.equals("*"))) {
/*  363 */       this.classNamePrefix = "";
/*  364 */       this.classNameExactMatch = false;
/*  365 */     } else if (paramString.endsWith(".*"))
/*      */     {
/*  367 */       this.classNamePrefix = paramString.substring(0, paramString.length() - 1);
/*  368 */       this.classNameExactMatch = false;
/*      */     } else {
/*  370 */       this.classNamePrefix = paramString;
/*  371 */       this.classNameExactMatch = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setMember(String paramString) {
/*  376 */     if ((paramString == null) || (paramString.equals("-")))
/*  377 */       this.member = null;
/*  378 */     else if (paramString.equals(""))
/*  379 */       this.member = "*";
/*      */     else
/*  381 */       this.member = paramString;
/*      */   }
/*      */ 
/*      */   public MBeanPermission(String paramString1, String paramString2)
/*      */   {
/*  403 */     super(paramString1);
/*      */ 
/*  405 */     parseName();
/*      */ 
/*  407 */     this.actions = paramString2;
/*  408 */     parseActions();
/*      */   }
/*      */ 
/*      */   public MBeanPermission(String paramString1, String paramString2, ObjectName paramObjectName, String paramString3)
/*      */   {
/*  443 */     super(makeName(paramString1, paramString2, paramObjectName));
/*  444 */     initName(paramString1, paramString2, paramObjectName);
/*      */ 
/*  446 */     this.actions = paramString3;
/*  447 */     parseActions();
/*      */   }
/*      */ 
/*      */   private static String makeName(String paramString1, String paramString2, ObjectName paramObjectName)
/*      */   {
/*  452 */     StringBuilder localStringBuilder = new StringBuilder();
/*  453 */     if (paramString1 == null)
/*  454 */       paramString1 = "-";
/*  455 */     localStringBuilder.append(paramString1);
/*  456 */     if (paramString2 == null)
/*  457 */       paramString2 = "-";
/*  458 */     localStringBuilder.append("#" + paramString2);
/*  459 */     if (paramObjectName == null)
/*  460 */       localStringBuilder.append("[-]");
/*      */     else {
/*  462 */       localStringBuilder.append("[").append(paramObjectName.getCanonicalName()).append("]");
/*      */     }
/*      */ 
/*  466 */     if (localStringBuilder.length() == 0) {
/*  467 */       return "*";
/*      */     }
/*  469 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public String getActions()
/*      */   {
/*  480 */     if (this.actions == null) {
/*  481 */       this.actions = getActions(this.mask);
/*      */     }
/*  483 */     return this.actions;
/*      */   }
/*      */ 
/*      */   private static String getActions(int paramInt)
/*      */   {
/*  491 */     StringBuilder localStringBuilder = new StringBuilder();
/*  492 */     int i = 0;
/*      */ 
/*  494 */     if ((paramInt & 0x1) == 1) {
/*  495 */       i = 1;
/*  496 */       localStringBuilder.append("addNotificationListener");
/*      */     }
/*      */ 
/*  499 */     if ((paramInt & 0x2) == 2) {
/*  500 */       if (i != 0) localStringBuilder.append(','); else
/*  501 */         i = 1;
/*  502 */       localStringBuilder.append("getAttribute");
/*      */     }
/*      */ 
/*  505 */     if ((paramInt & 0x4) == 4) {
/*  506 */       if (i != 0) localStringBuilder.append(','); else
/*  507 */         i = 1;
/*  508 */       localStringBuilder.append("getClassLoader");
/*      */     }
/*      */ 
/*  511 */     if ((paramInt & 0x8) == 8) {
/*  512 */       if (i != 0) localStringBuilder.append(','); else
/*  513 */         i = 1;
/*  514 */       localStringBuilder.append("getClassLoaderFor");
/*      */     }
/*      */ 
/*  517 */     if ((paramInt & 0x10) == 16) {
/*  518 */       if (i != 0) localStringBuilder.append(','); else
/*  519 */         i = 1;
/*  520 */       localStringBuilder.append("getClassLoaderRepository");
/*      */     }
/*      */ 
/*  523 */     if ((paramInt & 0x20) == 32) {
/*  524 */       if (i != 0) localStringBuilder.append(','); else
/*  525 */         i = 1;
/*  526 */       localStringBuilder.append("getDomains");
/*      */     }
/*      */ 
/*  529 */     if ((paramInt & 0x40) == 64) {
/*  530 */       if (i != 0) localStringBuilder.append(','); else
/*  531 */         i = 1;
/*  532 */       localStringBuilder.append("getMBeanInfo");
/*      */     }
/*      */ 
/*  535 */     if ((paramInt & 0x80) == 128) {
/*  536 */       if (i != 0) localStringBuilder.append(','); else
/*  537 */         i = 1;
/*  538 */       localStringBuilder.append("getObjectInstance");
/*      */     }
/*      */ 
/*  541 */     if ((paramInt & 0x100) == 256) {
/*  542 */       if (i != 0) localStringBuilder.append(','); else
/*  543 */         i = 1;
/*  544 */       localStringBuilder.append("instantiate");
/*      */     }
/*      */ 
/*  547 */     if ((paramInt & 0x200) == 512) {
/*  548 */       if (i != 0) localStringBuilder.append(','); else
/*  549 */         i = 1;
/*  550 */       localStringBuilder.append("invoke");
/*      */     }
/*      */ 
/*  553 */     if ((paramInt & 0x400) == 1024) {
/*  554 */       if (i != 0) localStringBuilder.append(','); else
/*  555 */         i = 1;
/*  556 */       localStringBuilder.append("isInstanceOf");
/*      */     }
/*      */ 
/*  559 */     if ((paramInt & 0x800) == 2048) {
/*  560 */       if (i != 0) localStringBuilder.append(','); else
/*  561 */         i = 1;
/*  562 */       localStringBuilder.append("queryMBeans");
/*      */     }
/*      */ 
/*  565 */     if ((paramInt & 0x1000) == 4096) {
/*  566 */       if (i != 0) localStringBuilder.append(','); else
/*  567 */         i = 1;
/*  568 */       localStringBuilder.append("queryNames");
/*      */     }
/*      */ 
/*  571 */     if ((paramInt & 0x2000) == 8192) {
/*  572 */       if (i != 0) localStringBuilder.append(','); else
/*  573 */         i = 1;
/*  574 */       localStringBuilder.append("registerMBean");
/*      */     }
/*      */ 
/*  577 */     if ((paramInt & 0x4000) == 16384) {
/*  578 */       if (i != 0) localStringBuilder.append(','); else
/*  579 */         i = 1;
/*  580 */       localStringBuilder.append("removeNotificationListener");
/*      */     }
/*      */ 
/*  583 */     if ((paramInt & 0x8000) == 32768) {
/*  584 */       if (i != 0) localStringBuilder.append(','); else
/*  585 */         i = 1;
/*  586 */       localStringBuilder.append("setAttribute");
/*      */     }
/*      */ 
/*  589 */     if ((paramInt & 0x10000) == 65536) {
/*  590 */       if (i != 0) localStringBuilder.append(','); else
/*  591 */         i = 1;
/*  592 */       localStringBuilder.append("unregisterMBean");
/*      */     }
/*      */ 
/*  595 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  604 */     return getName().hashCode() + getActions().hashCode();
/*      */   }
/*      */ 
/*      */   private static int getMask(String paramString)
/*      */   {
/*  627 */     int i = 0;
/*      */ 
/*  629 */     if (paramString == null) {
/*  630 */       return i;
/*      */     }
/*      */ 
/*  633 */     if (paramString.equals("*")) {
/*  634 */       return 131071;
/*      */     }
/*      */ 
/*  637 */     char[] arrayOfChar = paramString.toCharArray();
/*      */ 
/*  639 */     int j = arrayOfChar.length - 1;
/*  640 */     if (j < 0) {
/*  641 */       return i;
/*      */     }
/*  643 */     while (j != -1)
/*      */     {
/*      */       int k;
/*  647 */       while ((j != -1) && (((k = arrayOfChar[j]) == ' ') || (k == 13) || (k == 10) || (k == 12) || (k == 9)))
/*      */       {
/*  652 */         j--;
/*      */       }
/*      */       int m;
/*  657 */       if ((j >= 25) && (arrayOfChar[(j - 25)] == 'r') && (arrayOfChar[(j - 24)] == 'e') && (arrayOfChar[(j - 23)] == 'm') && (arrayOfChar[(j - 22)] == 'o') && (arrayOfChar[(j - 21)] == 'v') && (arrayOfChar[(j - 20)] == 'e') && (arrayOfChar[(j - 19)] == 'N') && (arrayOfChar[(j - 18)] == 'o') && (arrayOfChar[(j - 17)] == 't') && (arrayOfChar[(j - 16)] == 'i') && (arrayOfChar[(j - 15)] == 'f') && (arrayOfChar[(j - 14)] == 'i') && (arrayOfChar[(j - 13)] == 'c') && (arrayOfChar[(j - 12)] == 'a') && (arrayOfChar[(j - 11)] == 't') && (arrayOfChar[(j - 10)] == 'i') && (arrayOfChar[(j - 9)] == 'o') && (arrayOfChar[(j - 8)] == 'n') && (arrayOfChar[(j - 7)] == 'L') && (arrayOfChar[(j - 6)] == 'i') && (arrayOfChar[(j - 5)] == 's') && (arrayOfChar[(j - 4)] == 't') && (arrayOfChar[(j - 3)] == 'e') && (arrayOfChar[(j - 2)] == 'n') && (arrayOfChar[(j - 1)] == 'e') && (arrayOfChar[j] == 'r'))
/*      */       {
/*  684 */         m = 26;
/*  685 */         i |= 16384;
/*  686 */       } else if ((j >= 23) && (arrayOfChar[(j - 23)] == 'g') && (arrayOfChar[(j - 22)] == 'e') && (arrayOfChar[(j - 21)] == 't') && (arrayOfChar[(j - 20)] == 'C') && (arrayOfChar[(j - 19)] == 'l') && (arrayOfChar[(j - 18)] == 'a') && (arrayOfChar[(j - 17)] == 's') && (arrayOfChar[(j - 16)] == 's') && (arrayOfChar[(j - 15)] == 'L') && (arrayOfChar[(j - 14)] == 'o') && (arrayOfChar[(j - 13)] == 'a') && (arrayOfChar[(j - 12)] == 'd') && (arrayOfChar[(j - 11)] == 'e') && (arrayOfChar[(j - 10)] == 'r') && (arrayOfChar[(j - 9)] == 'R') && (arrayOfChar[(j - 8)] == 'e') && (arrayOfChar[(j - 7)] == 'p') && (arrayOfChar[(j - 6)] == 'o') && (arrayOfChar[(j - 5)] == 's') && (arrayOfChar[(j - 4)] == 'i') && (arrayOfChar[(j - 3)] == 't') && (arrayOfChar[(j - 2)] == 'o') && (arrayOfChar[(j - 1)] == 'r') && (arrayOfChar[j] == 'y'))
/*      */       {
/*  711 */         m = 24;
/*  712 */         i |= 16;
/*  713 */       } else if ((j >= 22) && (arrayOfChar[(j - 22)] == 'a') && (arrayOfChar[(j - 21)] == 'd') && (arrayOfChar[(j - 20)] == 'd') && (arrayOfChar[(j - 19)] == 'N') && (arrayOfChar[(j - 18)] == 'o') && (arrayOfChar[(j - 17)] == 't') && (arrayOfChar[(j - 16)] == 'i') && (arrayOfChar[(j - 15)] == 'f') && (arrayOfChar[(j - 14)] == 'i') && (arrayOfChar[(j - 13)] == 'c') && (arrayOfChar[(j - 12)] == 'a') && (arrayOfChar[(j - 11)] == 't') && (arrayOfChar[(j - 10)] == 'i') && (arrayOfChar[(j - 9)] == 'o') && (arrayOfChar[(j - 8)] == 'n') && (arrayOfChar[(j - 7)] == 'L') && (arrayOfChar[(j - 6)] == 'i') && (arrayOfChar[(j - 5)] == 's') && (arrayOfChar[(j - 4)] == 't') && (arrayOfChar[(j - 3)] == 'e') && (arrayOfChar[(j - 2)] == 'n') && (arrayOfChar[(j - 1)] == 'e') && (arrayOfChar[j] == 'r'))
/*      */       {
/*  737 */         m = 23;
/*  738 */         i |= 1;
/*  739 */       } else if ((j >= 16) && (arrayOfChar[(j - 16)] == 'g') && (arrayOfChar[(j - 15)] == 'e') && (arrayOfChar[(j - 14)] == 't') && (arrayOfChar[(j - 13)] == 'C') && (arrayOfChar[(j - 12)] == 'l') && (arrayOfChar[(j - 11)] == 'a') && (arrayOfChar[(j - 10)] == 's') && (arrayOfChar[(j - 9)] == 's') && (arrayOfChar[(j - 8)] == 'L') && (arrayOfChar[(j - 7)] == 'o') && (arrayOfChar[(j - 6)] == 'a') && (arrayOfChar[(j - 5)] == 'd') && (arrayOfChar[(j - 4)] == 'e') && (arrayOfChar[(j - 3)] == 'r') && (arrayOfChar[(j - 2)] == 'F') && (arrayOfChar[(j - 1)] == 'o') && (arrayOfChar[j] == 'r'))
/*      */       {
/*  757 */         m = 17;
/*  758 */         i |= 8;
/*  759 */       } else if ((j >= 16) && (arrayOfChar[(j - 16)] == 'g') && (arrayOfChar[(j - 15)] == 'e') && (arrayOfChar[(j - 14)] == 't') && (arrayOfChar[(j - 13)] == 'O') && (arrayOfChar[(j - 12)] == 'b') && (arrayOfChar[(j - 11)] == 'j') && (arrayOfChar[(j - 10)] == 'e') && (arrayOfChar[(j - 9)] == 'c') && (arrayOfChar[(j - 8)] == 't') && (arrayOfChar[(j - 7)] == 'I') && (arrayOfChar[(j - 6)] == 'n') && (arrayOfChar[(j - 5)] == 's') && (arrayOfChar[(j - 4)] == 't') && (arrayOfChar[(j - 3)] == 'a') && (arrayOfChar[(j - 2)] == 'n') && (arrayOfChar[(j - 1)] == 'c') && (arrayOfChar[j] == 'e'))
/*      */       {
/*  777 */         m = 17;
/*  778 */         i |= 128;
/*  779 */       } else if ((j >= 14) && (arrayOfChar[(j - 14)] == 'u') && (arrayOfChar[(j - 13)] == 'n') && (arrayOfChar[(j - 12)] == 'r') && (arrayOfChar[(j - 11)] == 'e') && (arrayOfChar[(j - 10)] == 'g') && (arrayOfChar[(j - 9)] == 'i') && (arrayOfChar[(j - 8)] == 's') && (arrayOfChar[(j - 7)] == 't') && (arrayOfChar[(j - 6)] == 'e') && (arrayOfChar[(j - 5)] == 'r') && (arrayOfChar[(j - 4)] == 'M') && (arrayOfChar[(j - 3)] == 'B') && (arrayOfChar[(j - 2)] == 'e') && (arrayOfChar[(j - 1)] == 'a') && (arrayOfChar[j] == 'n'))
/*      */       {
/*  795 */         m = 15;
/*  796 */         i |= 65536;
/*  797 */       } else if ((j >= 13) && (arrayOfChar[(j - 13)] == 'g') && (arrayOfChar[(j - 12)] == 'e') && (arrayOfChar[(j - 11)] == 't') && (arrayOfChar[(j - 10)] == 'C') && (arrayOfChar[(j - 9)] == 'l') && (arrayOfChar[(j - 8)] == 'a') && (arrayOfChar[(j - 7)] == 's') && (arrayOfChar[(j - 6)] == 's') && (arrayOfChar[(j - 5)] == 'L') && (arrayOfChar[(j - 4)] == 'o') && (arrayOfChar[(j - 3)] == 'a') && (arrayOfChar[(j - 2)] == 'd') && (arrayOfChar[(j - 1)] == 'e') && (arrayOfChar[j] == 'r'))
/*      */       {
/*  812 */         m = 14;
/*  813 */         i |= 4;
/*  814 */       } else if ((j >= 12) && (arrayOfChar[(j - 12)] == 'r') && (arrayOfChar[(j - 11)] == 'e') && (arrayOfChar[(j - 10)] == 'g') && (arrayOfChar[(j - 9)] == 'i') && (arrayOfChar[(j - 8)] == 's') && (arrayOfChar[(j - 7)] == 't') && (arrayOfChar[(j - 6)] == 'e') && (arrayOfChar[(j - 5)] == 'r') && (arrayOfChar[(j - 4)] == 'M') && (arrayOfChar[(j - 3)] == 'B') && (arrayOfChar[(j - 2)] == 'e') && (arrayOfChar[(j - 1)] == 'a') && (arrayOfChar[j] == 'n'))
/*      */       {
/*  828 */         m = 13;
/*  829 */         i |= 8192;
/*  830 */       } else if ((j >= 11) && (arrayOfChar[(j - 11)] == 'g') && (arrayOfChar[(j - 10)] == 'e') && (arrayOfChar[(j - 9)] == 't') && (arrayOfChar[(j - 8)] == 'A') && (arrayOfChar[(j - 7)] == 't') && (arrayOfChar[(j - 6)] == 't') && (arrayOfChar[(j - 5)] == 'r') && (arrayOfChar[(j - 4)] == 'i') && (arrayOfChar[(j - 3)] == 'b') && (arrayOfChar[(j - 2)] == 'u') && (arrayOfChar[(j - 1)] == 't') && (arrayOfChar[j] == 'e'))
/*      */       {
/*  843 */         m = 12;
/*  844 */         i |= 2;
/*  845 */       } else if ((j >= 11) && (arrayOfChar[(j - 11)] == 'g') && (arrayOfChar[(j - 10)] == 'e') && (arrayOfChar[(j - 9)] == 't') && (arrayOfChar[(j - 8)] == 'M') && (arrayOfChar[(j - 7)] == 'B') && (arrayOfChar[(j - 6)] == 'e') && (arrayOfChar[(j - 5)] == 'a') && (arrayOfChar[(j - 4)] == 'n') && (arrayOfChar[(j - 3)] == 'I') && (arrayOfChar[(j - 2)] == 'n') && (arrayOfChar[(j - 1)] == 'f') && (arrayOfChar[j] == 'o'))
/*      */       {
/*  858 */         m = 12;
/*  859 */         i |= 64;
/*  860 */       } else if ((j >= 11) && (arrayOfChar[(j - 11)] == 'i') && (arrayOfChar[(j - 10)] == 's') && (arrayOfChar[(j - 9)] == 'I') && (arrayOfChar[(j - 8)] == 'n') && (arrayOfChar[(j - 7)] == 's') && (arrayOfChar[(j - 6)] == 't') && (arrayOfChar[(j - 5)] == 'a') && (arrayOfChar[(j - 4)] == 'n') && (arrayOfChar[(j - 3)] == 'c') && (arrayOfChar[(j - 2)] == 'e') && (arrayOfChar[(j - 1)] == 'O') && (arrayOfChar[j] == 'f'))
/*      */       {
/*  873 */         m = 12;
/*  874 */         i |= 1024;
/*  875 */       } else if ((j >= 11) && (arrayOfChar[(j - 11)] == 's') && (arrayOfChar[(j - 10)] == 'e') && (arrayOfChar[(j - 9)] == 't') && (arrayOfChar[(j - 8)] == 'A') && (arrayOfChar[(j - 7)] == 't') && (arrayOfChar[(j - 6)] == 't') && (arrayOfChar[(j - 5)] == 'r') && (arrayOfChar[(j - 4)] == 'i') && (arrayOfChar[(j - 3)] == 'b') && (arrayOfChar[(j - 2)] == 'u') && (arrayOfChar[(j - 1)] == 't') && (arrayOfChar[j] == 'e'))
/*      */       {
/*  888 */         m = 12;
/*  889 */         i |= 32768;
/*  890 */       } else if ((j >= 10) && (arrayOfChar[(j - 10)] == 'i') && (arrayOfChar[(j - 9)] == 'n') && (arrayOfChar[(j - 8)] == 's') && (arrayOfChar[(j - 7)] == 't') && (arrayOfChar[(j - 6)] == 'a') && (arrayOfChar[(j - 5)] == 'n') && (arrayOfChar[(j - 4)] == 't') && (arrayOfChar[(j - 3)] == 'i') && (arrayOfChar[(j - 2)] == 'a') && (arrayOfChar[(j - 1)] == 't') && (arrayOfChar[j] == 'e'))
/*      */       {
/*  902 */         m = 11;
/*  903 */         i |= 256;
/*  904 */       } else if ((j >= 10) && (arrayOfChar[(j - 10)] == 'q') && (arrayOfChar[(j - 9)] == 'u') && (arrayOfChar[(j - 8)] == 'e') && (arrayOfChar[(j - 7)] == 'r') && (arrayOfChar[(j - 6)] == 'y') && (arrayOfChar[(j - 5)] == 'M') && (arrayOfChar[(j - 4)] == 'B') && (arrayOfChar[(j - 3)] == 'e') && (arrayOfChar[(j - 2)] == 'a') && (arrayOfChar[(j - 1)] == 'n') && (arrayOfChar[j] == 's'))
/*      */       {
/*  916 */         m = 11;
/*  917 */         i |= 2048;
/*  918 */       } else if ((j >= 9) && (arrayOfChar[(j - 9)] == 'g') && (arrayOfChar[(j - 8)] == 'e') && (arrayOfChar[(j - 7)] == 't') && (arrayOfChar[(j - 6)] == 'D') && (arrayOfChar[(j - 5)] == 'o') && (arrayOfChar[(j - 4)] == 'm') && (arrayOfChar[(j - 3)] == 'a') && (arrayOfChar[(j - 2)] == 'i') && (arrayOfChar[(j - 1)] == 'n') && (arrayOfChar[j] == 's'))
/*      */       {
/*  929 */         m = 10;
/*  930 */         i |= 32;
/*  931 */       } else if ((j >= 9) && (arrayOfChar[(j - 9)] == 'q') && (arrayOfChar[(j - 8)] == 'u') && (arrayOfChar[(j - 7)] == 'e') && (arrayOfChar[(j - 6)] == 'r') && (arrayOfChar[(j - 5)] == 'y') && (arrayOfChar[(j - 4)] == 'N') && (arrayOfChar[(j - 3)] == 'a') && (arrayOfChar[(j - 2)] == 'm') && (arrayOfChar[(j - 1)] == 'e') && (arrayOfChar[j] == 's'))
/*      */       {
/*  942 */         m = 10;
/*  943 */         i |= 4096;
/*  944 */       } else if ((j >= 5) && (arrayOfChar[(j - 5)] == 'i') && (arrayOfChar[(j - 4)] == 'n') && (arrayOfChar[(j - 3)] == 'v') && (arrayOfChar[(j - 2)] == 'o') && (arrayOfChar[(j - 1)] == 'k') && (arrayOfChar[j] == 'e'))
/*      */       {
/*  951 */         m = 6;
/*  952 */         i |= 512;
/*      */       }
/*      */       else {
/*  955 */         throw new IllegalArgumentException("Invalid permission: " + paramString);
/*      */       }
/*      */ 
/*  961 */       int n = 0;
/*  962 */       while ((j >= m) && (n == 0)) {
/*  963 */         switch (arrayOfChar[(j - m)]) {
/*      */         case ',':
/*  965 */           n = 1;
/*  966 */           break;
/*      */         case '\t':
/*      */         case '\n':
/*      */         case '\f':
/*      */         case '\r':
/*      */         case ' ':
/*  969 */           break;
/*      */         default:
/*  971 */           throw new IllegalArgumentException("Invalid permission: " + paramString);
/*      */         }
/*      */ 
/*  974 */         j--;
/*      */       }
/*      */ 
/*  978 */       j -= m;
/*      */     }
/*      */ 
/*  981 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean implies(Permission paramPermission)
/*      */   {
/* 1027 */     if (!(paramPermission instanceof MBeanPermission)) {
/* 1028 */       return false;
/*      */     }
/* 1030 */     MBeanPermission localMBeanPermission = (MBeanPermission)paramPermission;
/*      */ 
/* 1039 */     if ((this.mask & 0x800) == 2048) {
/* 1040 */       if (((this.mask | 0x1000) & localMBeanPermission.mask) != localMBeanPermission.mask)
/*      */       {
/* 1042 */         return false;
/*      */       }
/*      */     }
/* 1045 */     else if ((this.mask & localMBeanPermission.mask) != localMBeanPermission.mask)
/*      */     {
/* 1047 */       return false;
/*      */     }
/*      */ 
/* 1079 */     if (localMBeanPermission.classNamePrefix != null)
/*      */     {
/* 1081 */       if (this.classNamePrefix == null)
/*      */       {
/* 1083 */         return false;
/* 1084 */       }if (this.classNameExactMatch) {
/* 1085 */         if (!localMBeanPermission.classNameExactMatch)
/* 1086 */           return false;
/* 1087 */         if (!localMBeanPermission.classNamePrefix.equals(this.classNamePrefix)) {
/* 1088 */           return false;
/*      */         }
/*      */ 
/*      */       }
/* 1092 */       else if (!localMBeanPermission.classNamePrefix.startsWith(this.classNamePrefix)) {
/* 1093 */         return false;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1098 */     if (localMBeanPermission.member != null)
/*      */     {
/* 1100 */       if (this.member == null)
/*      */       {
/* 1102 */         return false;
/* 1103 */       }if (!this.member.equals("*"))
/*      */       {
/* 1105 */         if (!this.member.equals(localMBeanPermission.member)) {
/* 1106 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1111 */     if (localMBeanPermission.objectName != null)
/*      */     {
/* 1113 */       if (this.objectName == null)
/*      */       {
/* 1115 */         return false;
/* 1116 */       }if (!this.objectName.apply(localMBeanPermission.objectName))
/*      */       {
/* 1121 */         if (!this.objectName.equals(localMBeanPermission.objectName))
/* 1122 */           return false;
/*      */       }
/*      */     }
/* 1125 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1138 */     if (paramObject == this) {
/* 1139 */       return true;
/*      */     }
/* 1141 */     if (!(paramObject instanceof MBeanPermission)) {
/* 1142 */       return false;
/*      */     }
/* 1144 */     MBeanPermission localMBeanPermission = (MBeanPermission)paramObject;
/*      */ 
/* 1146 */     return (this.mask == localMBeanPermission.mask) && (getName().equals(localMBeanPermission.getName()));
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1155 */     paramObjectInputStream.defaultReadObject();
/* 1156 */     parseName();
/* 1157 */     parseActions();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanPermission
 * JD-Core Version:    0.6.2
 */
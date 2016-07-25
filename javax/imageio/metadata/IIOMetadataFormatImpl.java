/*      */ package javax.imageio.metadata;
/*      */ 
/*      */ import com.sun.imageio.plugins.common.StandardMetadataFormat;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.MissingResourceException;
/*      */ import java.util.ResourceBundle;
/*      */ import javax.imageio.ImageTypeSpecifier;
/*      */ 
/*      */ public abstract class IIOMetadataFormatImpl
/*      */   implements IIOMetadataFormat
/*      */ {
/*      */   public static final String standardMetadataFormatName = "javax_imageio_1.0";
/*   86 */   private static IIOMetadataFormat standardFormat = null;
/*      */ 
/*   88 */   private String resourceBaseName = getClass().getName() + "Resources";
/*      */   private String rootName;
/*   93 */   private HashMap elementMap = new HashMap();
/*      */ 
/*      */   public IIOMetadataFormatImpl(String paramString, int paramInt)
/*      */   {
/*  171 */     if (paramString == null) {
/*  172 */       throw new IllegalArgumentException("rootName == null!");
/*      */     }
/*  174 */     if ((paramInt < 0) || (paramInt > 5) || (paramInt == 5))
/*      */     {
/*  177 */       throw new IllegalArgumentException("Invalid value for childPolicy!");
/*      */     }
/*      */ 
/*  180 */     this.rootName = paramString;
/*      */ 
/*  182 */     Element localElement = new Element();
/*  183 */     localElement.elementName = paramString;
/*  184 */     localElement.childPolicy = paramInt;
/*      */ 
/*  186 */     this.elementMap.put(paramString, localElement);
/*      */   }
/*      */ 
/*      */   public IIOMetadataFormatImpl(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  208 */     if (paramString == null) {
/*  209 */       throw new IllegalArgumentException("rootName == null!");
/*      */     }
/*  211 */     if (paramInt1 < 0) {
/*  212 */       throw new IllegalArgumentException("minChildren < 0!");
/*      */     }
/*  214 */     if (paramInt1 > paramInt2) {
/*  215 */       throw new IllegalArgumentException("minChildren > maxChildren!");
/*      */     }
/*      */ 
/*  218 */     Element localElement = new Element();
/*  219 */     localElement.elementName = paramString;
/*  220 */     localElement.childPolicy = 5;
/*  221 */     localElement.minChildren = paramInt1;
/*  222 */     localElement.maxChildren = paramInt2;
/*      */ 
/*  224 */     this.rootName = paramString;
/*  225 */     this.elementMap.put(paramString, localElement);
/*      */   }
/*      */ 
/*      */   protected void setResourceBaseName(String paramString)
/*      */   {
/*  246 */     if (paramString == null) {
/*  247 */       throw new IllegalArgumentException("resourceBaseName == null!");
/*      */     }
/*  249 */     this.resourceBaseName = paramString;
/*      */   }
/*      */ 
/*      */   protected String getResourceBaseName()
/*      */   {
/*  261 */     return this.resourceBaseName;
/*      */   }
/*      */ 
/*      */   private Element getElement(String paramString, boolean paramBoolean)
/*      */   {
/*  272 */     if ((paramBoolean) && (paramString == null)) {
/*  273 */       throw new IllegalArgumentException("element name is null!");
/*      */     }
/*  275 */     Element localElement = (Element)this.elementMap.get(paramString);
/*  276 */     if ((paramBoolean) && (localElement == null)) {
/*  277 */       throw new IllegalArgumentException("No such element: " + paramString);
/*      */     }
/*      */ 
/*  280 */     return localElement;
/*      */   }
/*      */ 
/*      */   private Element getElement(String paramString) {
/*  284 */     return getElement(paramString, true);
/*      */   }
/*      */ 
/*      */   private Attribute getAttribute(String paramString1, String paramString2)
/*      */   {
/*  289 */     Element localElement = getElement(paramString1);
/*  290 */     Attribute localAttribute = (Attribute)localElement.attrMap.get(paramString2);
/*  291 */     if (localAttribute == null) {
/*  292 */       throw new IllegalArgumentException("No such attribute \"" + paramString2 + "\"!");
/*      */     }
/*      */ 
/*  295 */     return localAttribute;
/*      */   }
/*      */ 
/*      */   protected void addElement(String paramString1, String paramString2, int paramInt)
/*      */   {
/*  320 */     Element localElement1 = getElement(paramString2);
/*  321 */     if ((paramInt < 0) || (paramInt > 5) || (paramInt == 5))
/*      */     {
/*  324 */       throw new IllegalArgumentException("Invalid value for childPolicy!");
/*      */     }
/*      */ 
/*  328 */     Element localElement2 = new Element();
/*  329 */     localElement2.elementName = paramString1;
/*  330 */     localElement2.childPolicy = paramInt;
/*      */ 
/*  332 */     localElement1.childList.add(paramString1);
/*  333 */     localElement2.parentList.add(paramString2);
/*      */ 
/*  335 */     this.elementMap.put(paramString1, localElement2);
/*      */   }
/*      */ 
/*      */   protected void addElement(String paramString1, String paramString2, int paramInt1, int paramInt2)
/*      */   {
/*  358 */     Element localElement1 = getElement(paramString2);
/*  359 */     if (paramInt1 < 0) {
/*  360 */       throw new IllegalArgumentException("minChildren < 0!");
/*      */     }
/*  362 */     if (paramInt1 > paramInt2) {
/*  363 */       throw new IllegalArgumentException("minChildren > maxChildren!");
/*      */     }
/*      */ 
/*  366 */     Element localElement2 = new Element();
/*  367 */     localElement2.elementName = paramString1;
/*  368 */     localElement2.childPolicy = 5;
/*  369 */     localElement2.minChildren = paramInt1;
/*  370 */     localElement2.maxChildren = paramInt2;
/*      */ 
/*  372 */     localElement1.childList.add(paramString1);
/*  373 */     localElement2.parentList.add(paramString2);
/*      */ 
/*  375 */     this.elementMap.put(paramString1, localElement2);
/*      */   }
/*      */ 
/*      */   protected void addChildElement(String paramString1, String paramString2)
/*      */   {
/*  395 */     Element localElement1 = getElement(paramString2);
/*  396 */     Element localElement2 = getElement(paramString1);
/*  397 */     localElement1.childList.add(paramString1);
/*  398 */     localElement2.parentList.add(paramString2);
/*      */   }
/*      */ 
/*      */   protected void removeElement(String paramString)
/*      */   {
/*  409 */     Element localElement1 = getElement(paramString, false);
/*  410 */     if (localElement1 != null) {
/*  411 */       Iterator localIterator = localElement1.parentList.iterator();
/*  412 */       while (localIterator.hasNext()) {
/*  413 */         String str = (String)localIterator.next();
/*  414 */         Element localElement2 = getElement(str, false);
/*  415 */         if (localElement2 != null) {
/*  416 */           localElement2.childList.remove(paramString);
/*      */         }
/*      */       }
/*  419 */       this.elementMap.remove(paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void addAttribute(String paramString1, String paramString2, int paramInt, boolean paramBoolean, String paramString3)
/*      */   {
/*  448 */     Element localElement = getElement(paramString1);
/*  449 */     if (paramString2 == null) {
/*  450 */       throw new IllegalArgumentException("attrName == null!");
/*      */     }
/*  452 */     if ((paramInt < 0) || (paramInt > 4)) {
/*  453 */       throw new IllegalArgumentException("Invalid value for dataType!");
/*      */     }
/*      */ 
/*  456 */     Attribute localAttribute = new Attribute();
/*  457 */     localAttribute.attrName = paramString2;
/*  458 */     localAttribute.valueType = 1;
/*  459 */     localAttribute.dataType = paramInt;
/*  460 */     localAttribute.required = paramBoolean;
/*  461 */     localAttribute.defaultValue = paramString3;
/*      */ 
/*  463 */     localElement.attrList.add(paramString2);
/*  464 */     localElement.attrMap.put(paramString2, localAttribute);
/*      */   }
/*      */ 
/*      */   protected void addAttribute(String paramString1, String paramString2, int paramInt, boolean paramBoolean, String paramString3, List<String> paramList)
/*      */   {
/*  504 */     Element localElement = getElement(paramString1);
/*  505 */     if (paramString2 == null) {
/*  506 */       throw new IllegalArgumentException("attrName == null!");
/*      */     }
/*  508 */     if ((paramInt < 0) || (paramInt > 4)) {
/*  509 */       throw new IllegalArgumentException("Invalid value for dataType!");
/*      */     }
/*  511 */     if (paramList == null) {
/*  512 */       throw new IllegalArgumentException("enumeratedValues == null!");
/*      */     }
/*  514 */     if (paramList.size() == 0) {
/*  515 */       throw new IllegalArgumentException("enumeratedValues is empty!");
/*      */     }
/*  517 */     Iterator localIterator = paramList.iterator();
/*  518 */     while (localIterator.hasNext()) {
/*  519 */       localObject = localIterator.next();
/*  520 */       if (localObject == null) {
/*  521 */         throw new IllegalArgumentException("enumeratedValues contains a null!");
/*      */       }
/*      */ 
/*  524 */       if (!(localObject instanceof String)) {
/*  525 */         throw new IllegalArgumentException("enumeratedValues contains a non-String value!");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  530 */     Object localObject = new Attribute();
/*  531 */     ((Attribute)localObject).attrName = paramString2;
/*  532 */     ((Attribute)localObject).valueType = 16;
/*  533 */     ((Attribute)localObject).dataType = paramInt;
/*  534 */     ((Attribute)localObject).required = paramBoolean;
/*  535 */     ((Attribute)localObject).defaultValue = paramString3;
/*  536 */     ((Attribute)localObject).enumeratedValues = paramList;
/*      */ 
/*  538 */     localElement.attrList.add(paramString2);
/*  539 */     localElement.attrMap.put(paramString2, localObject);
/*      */   }
/*      */ 
/*      */   protected void addAttribute(String paramString1, String paramString2, int paramInt, boolean paramBoolean1, String paramString3, String paramString4, String paramString5, boolean paramBoolean2, boolean paramBoolean3)
/*      */   {
/*  581 */     Element localElement = getElement(paramString1);
/*  582 */     if (paramString2 == null) {
/*  583 */       throw new IllegalArgumentException("attrName == null!");
/*      */     }
/*  585 */     if ((paramInt < 0) || (paramInt > 4)) {
/*  586 */       throw new IllegalArgumentException("Invalid value for dataType!");
/*      */     }
/*      */ 
/*  589 */     Attribute localAttribute = new Attribute();
/*  590 */     localAttribute.attrName = paramString2;
/*  591 */     localAttribute.valueType = 2;
/*  592 */     if (paramBoolean2) {
/*  593 */       localAttribute.valueType |= 4;
/*      */     }
/*  595 */     if (paramBoolean3) {
/*  596 */       localAttribute.valueType |= 8;
/*      */     }
/*  598 */     localAttribute.dataType = paramInt;
/*  599 */     localAttribute.required = paramBoolean1;
/*  600 */     localAttribute.defaultValue = paramString3;
/*  601 */     localAttribute.minValue = paramString4;
/*  602 */     localAttribute.maxValue = paramString5;
/*      */ 
/*  604 */     localElement.attrList.add(paramString2);
/*  605 */     localElement.attrMap.put(paramString2, localAttribute);
/*      */   }
/*      */ 
/*      */   protected void addAttribute(String paramString1, String paramString2, int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3)
/*      */   {
/*  637 */     Element localElement = getElement(paramString1);
/*  638 */     if (paramString2 == null) {
/*  639 */       throw new IllegalArgumentException("attrName == null!");
/*      */     }
/*  641 */     if ((paramInt1 < 0) || (paramInt1 > 4)) {
/*  642 */       throw new IllegalArgumentException("Invalid value for dataType!");
/*      */     }
/*  644 */     if ((paramInt2 < 0) || (paramInt2 > paramInt3)) {
/*  645 */       throw new IllegalArgumentException("Invalid list bounds!");
/*      */     }
/*      */ 
/*  648 */     Attribute localAttribute = new Attribute();
/*  649 */     localAttribute.attrName = paramString2;
/*  650 */     localAttribute.valueType = 32;
/*  651 */     localAttribute.dataType = paramInt1;
/*  652 */     localAttribute.required = paramBoolean;
/*  653 */     localAttribute.listMinLength = paramInt2;
/*  654 */     localAttribute.listMaxLength = paramInt3;
/*      */ 
/*  656 */     localElement.attrList.add(paramString2);
/*  657 */     localElement.attrMap.put(paramString2, localAttribute);
/*      */   }
/*      */ 
/*      */   protected void addBooleanAttribute(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  684 */     ArrayList localArrayList = new ArrayList();
/*  685 */     localArrayList.add("TRUE");
/*  686 */     localArrayList.add("FALSE");
/*      */ 
/*  688 */     String str = null;
/*  689 */     if (paramBoolean1) {
/*  690 */       str = paramBoolean2 ? "TRUE" : "FALSE";
/*      */     }
/*  692 */     addAttribute(paramString1, paramString2, 1, true, str, localArrayList);
/*      */   }
/*      */ 
/*      */   protected void removeAttribute(String paramString1, String paramString2)
/*      */   {
/*  712 */     Element localElement = getElement(paramString1);
/*  713 */     localElement.attrList.remove(paramString2);
/*  714 */     localElement.attrMap.remove(paramString2);
/*      */   }
/*      */ 
/*      */   protected <T> void addObjectValue(String paramString, Class<T> paramClass, boolean paramBoolean, T paramT)
/*      */   {
/*  741 */     Element localElement = getElement(paramString);
/*  742 */     ObjectValue localObjectValue = new ObjectValue();
/*  743 */     localObjectValue.valueType = 1;
/*  744 */     localObjectValue.classType = paramClass;
/*  745 */     localObjectValue.defaultValue = paramT;
/*      */ 
/*  747 */     localElement.objectValue = localObjectValue;
/*      */   }
/*      */ 
/*      */   protected <T> void addObjectValue(String paramString, Class<T> paramClass, boolean paramBoolean, T paramT, List<? extends T> paramList)
/*      */   {
/*  787 */     Element localElement = getElement(paramString);
/*  788 */     if (paramList == null) {
/*  789 */       throw new IllegalArgumentException("enumeratedValues == null!");
/*      */     }
/*  791 */     if (paramList.size() == 0) {
/*  792 */       throw new IllegalArgumentException("enumeratedValues is empty!");
/*      */     }
/*  794 */     Iterator localIterator = paramList.iterator();
/*  795 */     while (localIterator.hasNext()) {
/*  796 */       localObject = localIterator.next();
/*  797 */       if (localObject == null) {
/*  798 */         throw new IllegalArgumentException("enumeratedValues contains a null!");
/*      */       }
/*  800 */       if (!paramClass.isInstance(localObject)) {
/*  801 */         throw new IllegalArgumentException("enumeratedValues contains a value not of class classType!");
/*      */       }
/*      */     }
/*      */ 
/*  805 */     Object localObject = new ObjectValue();
/*  806 */     ((ObjectValue)localObject).valueType = 16;
/*  807 */     ((ObjectValue)localObject).classType = paramClass;
/*  808 */     ((ObjectValue)localObject).defaultValue = paramT;
/*  809 */     ((ObjectValue)localObject).enumeratedValues = paramList;
/*      */ 
/*  811 */     localElement.objectValue = ((ObjectValue)localObject);
/*      */   }
/*      */ 
/*      */   protected <T,  extends Comparable<? super T>> void addObjectValue(String paramString, Class<T> paramClass, T paramT, Comparable<? super T> paramComparable1, Comparable<? super T> paramComparable2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  853 */     Element localElement = getElement(paramString);
/*  854 */     ObjectValue localObjectValue = new ObjectValue();
/*  855 */     localObjectValue.valueType = 2;
/*  856 */     if (paramBoolean1) {
/*  857 */       localObjectValue.valueType |= 4;
/*      */     }
/*  859 */     if (paramBoolean2) {
/*  860 */       localObjectValue.valueType |= 8;
/*      */     }
/*  862 */     localObjectValue.classType = paramClass;
/*  863 */     localObjectValue.defaultValue = paramT;
/*  864 */     localObjectValue.minValue = paramComparable1;
/*  865 */     localObjectValue.maxValue = paramComparable2;
/*      */ 
/*  867 */     localElement.objectValue = localObjectValue;
/*      */   }
/*      */ 
/*      */   protected void addObjectValue(String paramString, Class<?> paramClass, int paramInt1, int paramInt2)
/*      */   {
/*  894 */     Element localElement = getElement(paramString);
/*  895 */     ObjectValue localObjectValue = new ObjectValue();
/*  896 */     localObjectValue.valueType = 32;
/*  897 */     localObjectValue.classType = paramClass;
/*  898 */     localObjectValue.arrayMinLength = paramInt1;
/*  899 */     localObjectValue.arrayMaxLength = paramInt2;
/*      */ 
/*  901 */     localElement.objectValue = localObjectValue;
/*      */   }
/*      */ 
/*      */   protected void removeObjectValue(String paramString)
/*      */   {
/*  914 */     Element localElement = getElement(paramString);
/*  915 */     localElement.objectValue = null;
/*      */   }
/*      */ 
/*      */   public String getRootName()
/*      */   {
/*  925 */     return this.rootName;
/*      */   }
/*      */ 
/*      */   public abstract boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier);
/*      */ 
/*      */   public int getElementMinChildren(String paramString)
/*      */   {
/*  934 */     Element localElement = getElement(paramString);
/*  935 */     if (localElement.childPolicy != 5) {
/*  936 */       throw new IllegalArgumentException("Child policy not CHILD_POLICY_REPEAT!");
/*      */     }
/*  938 */     return localElement.minChildren;
/*      */   }
/*      */ 
/*      */   public int getElementMaxChildren(String paramString) {
/*  942 */     Element localElement = getElement(paramString);
/*  943 */     if (localElement.childPolicy != 5) {
/*  944 */       throw new IllegalArgumentException("Child policy not CHILD_POLICY_REPEAT!");
/*      */     }
/*  946 */     return localElement.maxChildren;
/*      */   }
/*      */ 
/*      */   private String getResource(String paramString, Locale paramLocale) {
/*  950 */     if (paramLocale == null) {
/*  951 */       paramLocale = Locale.getDefault();
/*      */     }
/*      */ 
/*  962 */     ClassLoader localClassLoader = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run()
/*      */       {
/*  966 */         return Thread.currentThread().getContextClassLoader();
/*      */       }
/*      */     });
/*  970 */     ResourceBundle localResourceBundle = null;
/*      */     try {
/*  972 */       localResourceBundle = ResourceBundle.getBundle(this.resourceBaseName, paramLocale, localClassLoader);
/*      */     }
/*      */     catch (MissingResourceException localMissingResourceException1) {
/*      */       try {
/*  976 */         localResourceBundle = ResourceBundle.getBundle(this.resourceBaseName, paramLocale);
/*      */       } catch (MissingResourceException localMissingResourceException3) {
/*  978 */         return null;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  983 */       return localResourceBundle.getString(paramString); } catch (MissingResourceException localMissingResourceException2) {
/*      */     }
/*  985 */     return null;
/*      */   }
/*      */ 
/*      */   public String getElementDescription(String paramString, Locale paramLocale)
/*      */   {
/* 1021 */     Element localElement = getElement(paramString);
/* 1022 */     return getResource(paramString, paramLocale);
/*      */   }
/*      */ 
/*      */   public int getChildPolicy(String paramString)
/*      */   {
/* 1028 */     Element localElement = getElement(paramString);
/* 1029 */     return localElement.childPolicy;
/*      */   }
/*      */ 
/*      */   public String[] getChildNames(String paramString) {
/* 1033 */     Element localElement = getElement(paramString);
/* 1034 */     if (localElement.childPolicy == 0) {
/* 1035 */       return null;
/*      */     }
/* 1037 */     return (String[])localElement.childList.toArray(new String[0]);
/*      */   }
/*      */ 
/*      */   public String[] getAttributeNames(String paramString)
/*      */   {
/* 1043 */     Element localElement = getElement(paramString);
/* 1044 */     List localList = localElement.attrList;
/*      */ 
/* 1046 */     String[] arrayOfString = new String[localList.size()];
/* 1047 */     return (String[])localList.toArray(arrayOfString);
/*      */   }
/*      */ 
/*      */   public int getAttributeValueType(String paramString1, String paramString2) {
/* 1051 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1052 */     return localAttribute.valueType;
/*      */   }
/*      */ 
/*      */   public int getAttributeDataType(String paramString1, String paramString2) {
/* 1056 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1057 */     return localAttribute.dataType;
/*      */   }
/*      */ 
/*      */   public boolean isAttributeRequired(String paramString1, String paramString2) {
/* 1061 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1062 */     return localAttribute.required;
/*      */   }
/*      */ 
/*      */   public String getAttributeDefaultValue(String paramString1, String paramString2)
/*      */   {
/* 1067 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1068 */     return localAttribute.defaultValue;
/*      */   }
/*      */ 
/*      */   public String[] getAttributeEnumerations(String paramString1, String paramString2)
/*      */   {
/* 1073 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1074 */     if (localAttribute.valueType != 16) {
/* 1075 */       throw new IllegalArgumentException("Attribute not an enumeration!");
/*      */     }
/*      */ 
/* 1079 */     List localList = localAttribute.enumeratedValues;
/* 1080 */     Iterator localIterator = localList.iterator();
/* 1081 */     String[] arrayOfString = new String[localList.size()];
/* 1082 */     return (String[])localList.toArray(arrayOfString);
/*      */   }
/*      */ 
/*      */   public String getAttributeMinValue(String paramString1, String paramString2) {
/* 1086 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1087 */     if ((localAttribute.valueType != 2) && (localAttribute.valueType != 6) && (localAttribute.valueType != 10) && (localAttribute.valueType != 14))
/*      */     {
/* 1091 */       throw new IllegalArgumentException("Attribute not a range!");
/*      */     }
/*      */ 
/* 1094 */     return localAttribute.minValue;
/*      */   }
/*      */ 
/*      */   public String getAttributeMaxValue(String paramString1, String paramString2) {
/* 1098 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1099 */     if ((localAttribute.valueType != 2) && (localAttribute.valueType != 6) && (localAttribute.valueType != 10) && (localAttribute.valueType != 14))
/*      */     {
/* 1103 */       throw new IllegalArgumentException("Attribute not a range!");
/*      */     }
/*      */ 
/* 1106 */     return localAttribute.maxValue;
/*      */   }
/*      */ 
/*      */   public int getAttributeListMinLength(String paramString1, String paramString2) {
/* 1110 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1111 */     if (localAttribute.valueType != 32) {
/* 1112 */       throw new IllegalArgumentException("Attribute not a list!");
/*      */     }
/*      */ 
/* 1115 */     return localAttribute.listMinLength;
/*      */   }
/*      */ 
/*      */   public int getAttributeListMaxLength(String paramString1, String paramString2) {
/* 1119 */     Attribute localAttribute = getAttribute(paramString1, paramString2);
/* 1120 */     if (localAttribute.valueType != 32) {
/* 1121 */       throw new IllegalArgumentException("Attribute not a list!");
/*      */     }
/*      */ 
/* 1124 */     return localAttribute.listMaxLength;
/*      */   }
/*      */ 
/*      */   public String getAttributeDescription(String paramString1, String paramString2, Locale paramLocale)
/*      */   {
/* 1166 */     Element localElement = getElement(paramString1);
/* 1167 */     if (paramString2 == null) {
/* 1168 */       throw new IllegalArgumentException("attrName == null!");
/*      */     }
/* 1170 */     Attribute localAttribute = (Attribute)localElement.attrMap.get(paramString2);
/* 1171 */     if (localAttribute == null) {
/* 1172 */       throw new IllegalArgumentException("No such attribute!");
/*      */     }
/*      */ 
/* 1175 */     String str = paramString1 + "/" + paramString2;
/* 1176 */     return getResource(str, paramLocale);
/*      */   }
/*      */ 
/*      */   private ObjectValue getObjectValue(String paramString) {
/* 1180 */     Element localElement = getElement(paramString);
/* 1181 */     ObjectValue localObjectValue = localElement.objectValue;
/* 1182 */     if (localObjectValue == null) {
/* 1183 */       throw new IllegalArgumentException("No object within element " + paramString + "!");
/*      */     }
/*      */ 
/* 1186 */     return localObjectValue;
/*      */   }
/*      */ 
/*      */   public int getObjectValueType(String paramString) {
/* 1190 */     Element localElement = getElement(paramString);
/* 1191 */     ObjectValue localObjectValue = localElement.objectValue;
/* 1192 */     if (localObjectValue == null) {
/* 1193 */       return 0;
/*      */     }
/* 1195 */     return localObjectValue.valueType;
/*      */   }
/*      */ 
/*      */   public Class<?> getObjectClass(String paramString) {
/* 1199 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1200 */     return localObjectValue.classType;
/*      */   }
/*      */ 
/*      */   public Object getObjectDefaultValue(String paramString) {
/* 1204 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1205 */     return localObjectValue.defaultValue;
/*      */   }
/*      */ 
/*      */   public Object[] getObjectEnumerations(String paramString) {
/* 1209 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1210 */     if (localObjectValue.valueType != 16) {
/* 1211 */       throw new IllegalArgumentException("Not an enumeration!");
/*      */     }
/* 1213 */     List localList = localObjectValue.enumeratedValues;
/* 1214 */     Object[] arrayOfObject = new Object[localList.size()];
/* 1215 */     return localList.toArray(arrayOfObject);
/*      */   }
/*      */ 
/*      */   public Comparable<?> getObjectMinValue(String paramString) {
/* 1219 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1220 */     if ((localObjectValue.valueType & 0x2) != 2) {
/* 1221 */       throw new IllegalArgumentException("Not a range!");
/*      */     }
/* 1223 */     return localObjectValue.minValue;
/*      */   }
/*      */ 
/*      */   public Comparable<?> getObjectMaxValue(String paramString) {
/* 1227 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1228 */     if ((localObjectValue.valueType & 0x2) != 2) {
/* 1229 */       throw new IllegalArgumentException("Not a range!");
/*      */     }
/* 1231 */     return localObjectValue.maxValue;
/*      */   }
/*      */ 
/*      */   public int getObjectArrayMinLength(String paramString) {
/* 1235 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1236 */     if (localObjectValue.valueType != 32) {
/* 1237 */       throw new IllegalArgumentException("Not a list!");
/*      */     }
/* 1239 */     return localObjectValue.arrayMinLength;
/*      */   }
/*      */ 
/*      */   public int getObjectArrayMaxLength(String paramString) {
/* 1243 */     ObjectValue localObjectValue = getObjectValue(paramString);
/* 1244 */     if (localObjectValue.valueType != 32) {
/* 1245 */       throw new IllegalArgumentException("Not a list!");
/*      */     }
/* 1247 */     return localObjectValue.arrayMaxLength;
/*      */   }
/*      */ 
/*      */   private static synchronized void createStandardFormat()
/*      */   {
/* 1253 */     if (standardFormat == null)
/* 1254 */       standardFormat = new StandardMetadataFormat();
/*      */   }
/*      */ 
/*      */   public static IIOMetadataFormat getStandardFormatInstance()
/*      */   {
/* 1267 */     createStandardFormat();
/* 1268 */     return standardFormat;
/*      */   }
/*      */ 
/*      */   class Attribute
/*      */   {
/*      */     String attrName;
/*  119 */     int valueType = 1;
/*      */     int dataType;
/*      */     boolean required;
/*  122 */     String defaultValue = null;
/*      */     List enumeratedValues;
/*      */     String minValue;
/*      */     String maxValue;
/*      */     int listMinLength;
/*      */     int listMaxLength;
/*      */ 
/*      */     Attribute()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class Element
/*      */   {
/*      */     String elementName;
/*      */     int childPolicy;
/*   99 */     int minChildren = 0;
/*  100 */     int maxChildren = 0;
/*      */ 
/*  103 */     List childList = new ArrayList();
/*      */ 
/*  106 */     List parentList = new ArrayList();
/*      */ 
/*  109 */     List attrList = new ArrayList();
/*      */ 
/*  111 */     Map attrMap = new HashMap();
/*      */     IIOMetadataFormatImpl.ObjectValue objectValue;
/*      */ 
/*      */     Element()
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   class ObjectValue
/*      */   {
/*  137 */     int valueType = 0;
/*  138 */     Class classType = null;
/*  139 */     Object defaultValue = null;
/*      */ 
/*  142 */     List enumeratedValues = null;
/*      */ 
/*  145 */     Comparable minValue = null;
/*  146 */     Comparable maxValue = null;
/*      */ 
/*  149 */     int arrayMinLength = 0;
/*  150 */     int arrayMaxLength = 0;
/*      */ 
/*      */     ObjectValue()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIOMetadataFormatImpl
 * JD-Core Version:    0.6.2
 */
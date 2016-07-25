/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.orbutil.ObjectUtility;
/*     */ import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class IDLNameTranslatorImpl
/*     */   implements IDLNameTranslator
/*     */ {
/*  59 */   private static String[] IDL_KEYWORDS = { "abstract", "any", "attribute", "boolean", "case", "char", "const", "context", "custom", "default", "double", "enum", "exception", "factory", "FALSE", "fixed", "float", "in", "inout", "interface", "long", "module", "native", "Object", "octet", "oneway", "out", "private", "public", "raises", "readonly", "sequence", "short", "string", "struct", "supports", "switch", "TRUE", "truncatable", "typedef", "unsigned", "union", "ValueBase", "valuetype", "void", "wchar", "wstring" };
/*     */ 
/*  72 */   private static char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */   private static final String UNDERSCORE = "_";
/*     */   private static final String INNER_CLASS_SEPARATOR = "__";
/*  84 */   private static final String[] BASE_IDL_ARRAY_MODULE_TYPE = { "org", "omg", "boxedRMI" };
/*     */   private static final String BASE_IDL_ARRAY_ELEMENT_TYPE = "seq";
/*     */   private static final String LEADING_UNDERSCORE_CHAR = "J";
/*     */   private static final String ID_CONTAINER_CLASH_CHAR = "_";
/*     */   private static final String OVERLOADED_TYPE_SEPARATOR = "__";
/*     */   private static final String ATTRIBUTE_METHOD_CLASH_MANGLE_CHARS = "__";
/*     */   private static final String GET_ATTRIBUTE_PREFIX = "_get_";
/*     */   private static final String SET_ATTRIBUTE_PREFIX = "_set_";
/*     */   private static final String IS_ATTRIBUTE_PREFIX = "_get_";
/* 111 */   private static Set idlKeywords_ = new HashSet();
/*     */   private Class[] interf_;
/*     */   private Map methodToIDLNameMap_;
/*     */   private Map IDLNameToMethodMap_;
/*     */   private Method[] methods_;
/*     */ 
/*     */   public static IDLNameTranslator get(Class paramClass)
/*     */   {
/* 145 */     return new IDLNameTranslatorImpl(new Class[] { paramClass });
/*     */   }
/*     */ 
/*     */   public static IDLNameTranslator get(Class[] paramArrayOfClass)
/*     */   {
/* 158 */     return new IDLNameTranslatorImpl(paramArrayOfClass);
/*     */   }
/*     */ 
/*     */   public static String getExceptionId(Class paramClass)
/*     */   {
/* 172 */     IDLType localIDLType = classToIDLType(paramClass);
/* 173 */     return localIDLType.getExceptionName();
/*     */   }
/*     */ 
/*     */   public Class[] getInterfaces()
/*     */   {
/* 178 */     return this.interf_;
/*     */   }
/*     */ 
/*     */   public Method[] getMethods()
/*     */   {
/* 183 */     return this.methods_;
/*     */   }
/*     */ 
/*     */   public Method getMethod(String paramString)
/*     */   {
/* 188 */     return (Method)this.IDLNameToMethodMap_.get(paramString);
/*     */   }
/*     */ 
/*     */   public String getIDLName(Method paramMethod)
/*     */   {
/* 193 */     return (String)this.methodToIDLNameMap_.get(paramMethod);
/*     */   }
/*     */ 
/*     */   private IDLNameTranslatorImpl(Class[] paramArrayOfClass)
/*     */   {
/* 205 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 206 */     if (localSecurityManager != null)
/* 207 */       localSecurityManager.checkPermission(new DynamicAccessPermission("access"));
/*     */     try
/*     */     {
/* 210 */       IDLTypesUtil localIDLTypesUtil = new IDLTypesUtil();
/* 211 */       for (int i = 0; i < paramArrayOfClass.length; i++)
/* 212 */         localIDLTypesUtil.validateRemoteInterface(paramArrayOfClass[i]);
/* 213 */       this.interf_ = paramArrayOfClass;
/* 214 */       buildNameTranslation();
/*     */     } catch (IDLTypeException localIDLTypeException) {
/* 216 */       String str = localIDLTypeException.getMessage();
/* 217 */       IllegalStateException localIllegalStateException = new IllegalStateException(str);
/* 218 */       localIllegalStateException.initCause(localIDLTypeException);
/* 219 */       throw localIllegalStateException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void buildNameTranslation()
/*     */   {
/* 226 */     HashMap localHashMap = new HashMap();
/*     */     Object localObject1;
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 228 */     for (int i = 0; i < this.interf_.length; i++) {
/* 229 */       localObject1 = this.interf_[i];
/*     */ 
/* 231 */       localObject2 = new IDLTypesUtil();
/* 232 */       localObject3 = ((Class)localObject1).getMethods();
/*     */ 
/* 234 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Object run() {
/* 236 */           Method.setAccessible(this.val$methods, true);
/* 237 */           return null;
/*     */         }
/*     */       });
/* 244 */       for (int k = 0; k < localObject3.length; k++)
/*     */       {
/* 246 */         Method localMethod = localObject3[k];
/*     */ 
/* 248 */         IDLMethodInfo localIDLMethodInfo = new IDLMethodInfo(null);
/*     */ 
/* 250 */         localIDLMethodInfo.method = localMethod;
/*     */ 
/* 252 */         if (((IDLTypesUtil)localObject2).isPropertyAccessorMethod(localMethod, (Class)localObject1)) {
/* 253 */           localIDLMethodInfo.isProperty = true;
/* 254 */           String str = ((IDLTypesUtil)localObject2).getAttributeNameForProperty(localMethod.getName());
/*     */ 
/* 256 */           localIDLMethodInfo.originalName = str;
/* 257 */           localIDLMethodInfo.mangledName = str;
/*     */         } else {
/* 259 */           localIDLMethodInfo.isProperty = false;
/* 260 */           localIDLMethodInfo.originalName = localMethod.getName();
/* 261 */           localIDLMethodInfo.mangledName = localMethod.getName();
/*     */         }
/*     */ 
/* 264 */         localHashMap.put(localMethod, localIDLMethodInfo);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 275 */     Iterator localIterator1 = localHashMap.values().iterator();
/* 276 */     while (localIterator1.hasNext()) {
/* 277 */       localObject1 = (IDLMethodInfo)localIterator1.next();
/* 278 */       localObject2 = localHashMap.values().iterator();
/* 279 */       while (((Iterator)localObject2).hasNext()) {
/* 280 */         localObject3 = (IDLMethodInfo)((Iterator)localObject2).next();
/*     */ 
/* 282 */         if ((localObject1 != localObject3) && (!((IDLMethodInfo)localObject1).originalName.equals(((IDLMethodInfo)localObject3).originalName)) && (((IDLMethodInfo)localObject1).originalName.equalsIgnoreCase(((IDLMethodInfo)localObject3).originalName)))
/*     */         {
/* 285 */           ((IDLMethodInfo)localObject1).mangledName = mangleCaseSensitiveCollision(((IDLMethodInfo)localObject1).originalName);
/*     */ 
/* 287 */           break;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 293 */     localIterator1 = localHashMap.values().iterator();
/* 294 */     while (localIterator1.hasNext()) {
/* 295 */       localObject1 = (IDLMethodInfo)localIterator1.next();
/* 296 */       ((IDLMethodInfo)localObject1).mangledName = mangleIdentifier(((IDLMethodInfo)localObject1).mangledName, ((IDLMethodInfo)localObject1).isProperty);
/*     */     }
/*     */ 
/* 303 */     localIterator1 = localHashMap.values().iterator();
/* 304 */     while (localIterator1.hasNext()) {
/* 305 */       localObject1 = (IDLMethodInfo)localIterator1.next();
/* 306 */       if (!((IDLMethodInfo)localObject1).isProperty)
/*     */       {
/* 309 */         localObject2 = localHashMap.values().iterator();
/* 310 */         while (((Iterator)localObject2).hasNext()) {
/* 311 */           localObject3 = (IDLMethodInfo)((Iterator)localObject2).next();
/*     */ 
/* 313 */           if ((localObject1 != localObject3) && (!((IDLMethodInfo)localObject3).isProperty) && (((IDLMethodInfo)localObject1).originalName.equals(((IDLMethodInfo)localObject3).originalName)))
/*     */           {
/* 316 */             ((IDLMethodInfo)localObject1).mangledName = mangleOverloadedMethod(((IDLMethodInfo)localObject1).mangledName, ((IDLMethodInfo)localObject1).method);
/*     */ 
/* 318 */             break;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 326 */     localIterator1 = localHashMap.values().iterator();
/* 327 */     while (localIterator1.hasNext()) {
/* 328 */       localObject1 = (IDLMethodInfo)localIterator1.next();
/* 329 */       if (((IDLMethodInfo)localObject1).isProperty)
/*     */       {
/* 332 */         localObject2 = localHashMap.values().iterator();
/* 333 */         while (((Iterator)localObject2).hasNext()) {
/* 334 */           localObject3 = (IDLMethodInfo)((Iterator)localObject2).next();
/* 335 */           if ((localObject1 != localObject3) && (!((IDLMethodInfo)localObject3).isProperty) && (((IDLMethodInfo)localObject1).mangledName.equals(((IDLMethodInfo)localObject3).mangledName)))
/*     */           {
/* 338 */             ((IDLMethodInfo)localObject1).mangledName += "__";
/*     */ 
/* 340 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Object localObject4;
/* 349 */     for (int j = 0; j < this.interf_.length; j++) {
/* 350 */       localObject1 = this.interf_[j];
/* 351 */       localObject2 = getMappedContainerName((Class)localObject1);
/* 352 */       localObject3 = localHashMap.values().iterator();
/* 353 */       while (((Iterator)localObject3).hasNext()) {
/* 354 */         localObject4 = (IDLMethodInfo)((Iterator)localObject3).next();
/* 355 */         if ((!((IDLMethodInfo)localObject4).isProperty) && (identifierClashesWithContainer((String)localObject2, ((IDLMethodInfo)localObject4).mangledName)))
/*     */         {
/* 358 */           ((IDLMethodInfo)localObject4).mangledName = mangleContainerClash(((IDLMethodInfo)localObject4).mangledName);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 366 */     this.methodToIDLNameMap_ = new HashMap();
/* 367 */     this.IDLNameToMethodMap_ = new HashMap();
/* 368 */     this.methods_ = ((Method[])localHashMap.keySet().toArray(new Method[0]));
/*     */ 
/* 371 */     Iterator localIterator2 = localHashMap.values().iterator();
/* 372 */     while (localIterator2.hasNext()) {
/* 373 */       localObject1 = (IDLMethodInfo)localIterator2.next();
/* 374 */       localObject2 = ((IDLMethodInfo)localObject1).mangledName;
/* 375 */       if (((IDLMethodInfo)localObject1).isProperty) {
/* 376 */         localObject3 = ((IDLMethodInfo)localObject1).method.getName();
/* 377 */         localObject4 = "";
/*     */ 
/* 379 */         if (((String)localObject3).startsWith("get"))
/* 380 */           localObject4 = "_get_";
/* 381 */         else if (((String)localObject3).startsWith("set"))
/* 382 */           localObject4 = "_set_";
/*     */         else {
/* 384 */           localObject4 = "_get_";
/*     */         }
/*     */ 
/* 387 */         localObject2 = (String)localObject4 + ((IDLMethodInfo)localObject1).mangledName;
/*     */       }
/*     */ 
/* 390 */       this.methodToIDLNameMap_.put(((IDLMethodInfo)localObject1).method, localObject2);
/*     */ 
/* 397 */       if (this.IDLNameToMethodMap_.containsKey(localObject2))
/*     */       {
/* 399 */         localObject3 = (Method)this.IDLNameToMethodMap_.get(localObject2);
/* 400 */         throw new IllegalStateException("Error : methods " + localObject3 + " and " + ((IDLMethodInfo)localObject1).method + " both result in IDL name '" + (String)localObject2 + "'");
/*     */       }
/*     */ 
/* 404 */       this.IDLNameToMethodMap_.put(localObject2, ((IDLMethodInfo)localObject1).method);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String mangleIdentifier(String paramString)
/*     */   {
/* 423 */     return mangleIdentifier(paramString, false);
/*     */   }
/*     */ 
/*     */   private static String mangleIdentifier(String paramString, boolean paramBoolean)
/*     */   {
/* 428 */     String str = paramString;
/*     */ 
/* 435 */     if (hasLeadingUnderscore(str)) {
/* 436 */       str = mangleLeadingUnderscore(str);
/*     */     }
/*     */ 
/* 446 */     if ((!paramBoolean) && (isIDLKeyword(str))) {
/* 447 */       str = mangleIDLKeywordClash(str);
/*     */     }
/*     */ 
/* 454 */     if (!isIDLIdentifier(str)) {
/* 455 */       str = mangleUnicodeChars(str);
/*     */     }
/*     */ 
/* 458 */     return str;
/*     */   }
/*     */ 
/*     */   static boolean isIDLKeyword(String paramString)
/*     */   {
/* 483 */     String str = paramString.toUpperCase();
/*     */ 
/* 485 */     return idlKeywords_.contains(str);
/*     */   }
/*     */ 
/*     */   static String mangleIDLKeywordClash(String paramString) {
/* 489 */     return "_" + paramString;
/*     */   }
/*     */ 
/*     */   private static String mangleLeadingUnderscore(String paramString) {
/* 493 */     return "J" + paramString;
/*     */   }
/*     */ 
/*     */   private static boolean hasLeadingUnderscore(String paramString)
/*     */   {
/* 501 */     return paramString.startsWith("_");
/*     */   }
/*     */ 
/*     */   static String mangleUnicodeChars(String paramString)
/*     */   {
/* 510 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 512 */     for (int i = 0; i < paramString.length(); i++) {
/* 513 */       char c = paramString.charAt(i);
/* 514 */       if (isIDLIdentifierChar(c)) {
/* 515 */         localStringBuffer.append(c);
/*     */       } else {
/* 517 */         String str = charToUnicodeRepresentation(c);
/* 518 */         localStringBuffer.append(str);
/*     */       }
/*     */     }
/*     */ 
/* 522 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   String mangleCaseSensitiveCollision(String paramString)
/*     */   {
/* 540 */     StringBuffer localStringBuffer = new StringBuffer(paramString);
/*     */ 
/* 544 */     localStringBuffer.append("_");
/*     */ 
/* 546 */     int i = 0;
/* 547 */     for (int j = 0; j < paramString.length(); j++) {
/* 548 */       char c = paramString.charAt(j);
/* 549 */       if (Character.isUpperCase(c))
/*     */       {
/* 555 */         if (i != 0) {
/* 556 */           localStringBuffer.append("_");
/*     */         }
/* 558 */         localStringBuffer.append(j);
/* 559 */         i = 1;
/*     */       }
/*     */     }
/*     */ 
/* 563 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static String mangleContainerClash(String paramString) {
/* 567 */     return paramString + "_";
/*     */   }
/*     */ 
/*     */   private static boolean identifierClashesWithContainer(String paramString1, String paramString2)
/*     */   {
/* 578 */     return paramString2.equalsIgnoreCase(paramString1);
/*     */   }
/*     */ 
/*     */   public static String charToUnicodeRepresentation(char paramChar)
/*     */   {
/* 594 */     int i = paramChar;
/* 595 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 597 */     int j = i;
/*     */ 
/* 599 */     while (j > 0) {
/* 600 */       k = j / 16;
/* 601 */       m = j % 16;
/* 602 */       localStringBuffer.insert(0, HEX_DIGITS[m]);
/* 603 */       j = k;
/*     */     }
/*     */ 
/* 606 */     int k = 4 - localStringBuffer.length();
/* 607 */     for (int m = 0; m < k; m++) {
/* 608 */       localStringBuffer.insert(0, "0");
/*     */     }
/*     */ 
/* 611 */     localStringBuffer.insert(0, "U");
/* 612 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isIDLIdentifier(String paramString)
/*     */   {
/* 617 */     boolean bool = true;
/*     */ 
/* 619 */     for (int i = 0; i < paramString.length(); i++) {
/* 620 */       char c = paramString.charAt(i);
/*     */ 
/* 622 */       bool = i == 0 ? isIDLAlphabeticChar(c) : isIDLIdentifierChar(c);
/*     */ 
/* 625 */       if (!bool)
/*     */       {
/*     */         break;
/*     */       }
/*     */     }
/* 630 */     return bool;
/*     */   }
/*     */ 
/*     */   private static boolean isIDLIdentifierChar(char paramChar)
/*     */   {
/* 635 */     return (isIDLAlphabeticChar(paramChar)) || (isIDLDecimalDigit(paramChar)) || (isUnderscore(paramChar));
/*     */   }
/*     */ 
/*     */   private static boolean isIDLAlphabeticChar(char paramChar)
/*     */   {
/* 652 */     boolean bool = ((paramChar >= 'A') && (paramChar <= 'Z')) || ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'À') && (paramChar <= 'ÿ') && (paramChar != '×') && (paramChar != '÷'));
/*     */ 
/* 669 */     return bool;
/*     */   }
/*     */ 
/*     */   private static boolean isIDLDecimalDigit(char paramChar)
/*     */   {
/* 677 */     return (paramChar >= '0') && (paramChar <= '9');
/*     */   }
/*     */ 
/*     */   private static boolean isUnderscore(char paramChar) {
/* 681 */     return paramChar == '_';
/*     */   }
/*     */ 
/*     */   private static String mangleOverloadedMethod(String paramString, Method paramMethod)
/*     */   {
/* 692 */     IDLTypesUtil localIDLTypesUtil = new IDLTypesUtil();
/*     */ 
/* 695 */     String str1 = paramString + "__";
/*     */ 
/* 697 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/*     */ 
/* 699 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 700 */       Class localClass = arrayOfClass[i];
/*     */ 
/* 702 */       if (i > 0) {
/* 703 */         str1 = str1 + "__";
/*     */       }
/* 705 */       IDLType localIDLType = classToIDLType(localClass);
/*     */ 
/* 707 */       String str2 = localIDLType.getModuleName();
/* 708 */       String str3 = localIDLType.getMemberName();
/*     */ 
/* 710 */       String str4 = str2.length() > 0 ? str2 + "_" + str3 : str3;
/*     */ 
/* 713 */       if ((!localIDLTypesUtil.isPrimitive(localClass)) && (localIDLTypesUtil.getSpecialCaseIDLTypeMapping(localClass) == null) && (isIDLKeyword(str4)))
/*     */       {
/* 717 */         str4 = mangleIDLKeywordClash(str4);
/*     */       }
/*     */ 
/* 720 */       str4 = mangleUnicodeChars(str4);
/*     */ 
/* 722 */       str1 = str1 + str4;
/*     */     }
/*     */ 
/* 725 */     return str1;
/*     */   }
/*     */ 
/*     */   private static IDLType classToIDLType(Class paramClass)
/*     */   {
/* 731 */     IDLType localIDLType = null;
/* 732 */     IDLTypesUtil localIDLTypesUtil = new IDLTypesUtil();
/*     */ 
/* 734 */     if (localIDLTypesUtil.isPrimitive(paramClass))
/*     */     {
/* 736 */       localIDLType = localIDLTypesUtil.getPrimitiveIDLTypeMapping(paramClass);
/*     */     }
/*     */     else
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/*     */       String[] arrayOfString;
/* 738 */       if (paramClass.isArray())
/*     */       {
/* 741 */         localObject1 = paramClass.getComponentType();
/* 742 */         int i = 1;
/* 743 */         while (((Class)localObject1).isArray()) {
/* 744 */           localObject1 = ((Class)localObject1).getComponentType();
/* 745 */           i++;
/*     */         }
/* 747 */         localObject2 = classToIDLType((Class)localObject1);
/*     */ 
/* 749 */         arrayOfString = BASE_IDL_ARRAY_MODULE_TYPE;
/* 750 */         if (((IDLType)localObject2).hasModule()) {
/* 751 */           arrayOfString = (String[])ObjectUtility.concatenateArrays(arrayOfString, ((IDLType)localObject2).getModules());
/*     */         }
/*     */ 
/* 755 */         String str2 = "seq" + i + "_" + ((IDLType)localObject2).getMemberName();
/*     */ 
/* 759 */         localIDLType = new IDLType(paramClass, arrayOfString, str2);
/*     */       }
/*     */       else {
/* 762 */         localIDLType = localIDLTypesUtil.getSpecialCaseIDLTypeMapping(paramClass);
/*     */ 
/* 764 */         if (localIDLType == null)
/*     */         {
/* 767 */           localObject1 = getUnmappedContainerName(paramClass);
/*     */ 
/* 770 */           localObject1 = ((String)localObject1).replaceAll("\\$", "__");
/*     */ 
/* 773 */           if (hasLeadingUnderscore((String)localObject1)) {
/* 774 */             localObject1 = mangleLeadingUnderscore((String)localObject1);
/*     */           }
/*     */ 
/* 780 */           String str1 = getPackageName(paramClass);
/*     */ 
/* 782 */           if (str1 == null) {
/* 783 */             localIDLType = new IDLType(paramClass, (String)localObject1);
/*     */           }
/*     */           else
/*     */           {
/* 787 */             if (localIDLTypesUtil.isEntity(paramClass)) {
/* 788 */               str1 = "org.omg.boxedIDL." + str1;
/*     */             }
/*     */ 
/* 798 */             localObject2 = new StringTokenizer(str1, ".");
/*     */ 
/* 801 */             arrayOfString = new String[((StringTokenizer)localObject2).countTokens()];
/* 802 */             int j = 0;
/* 803 */             while (((StringTokenizer)localObject2).hasMoreElements()) {
/* 804 */               String str3 = ((StringTokenizer)localObject2).nextToken();
/* 805 */               String str4 = hasLeadingUnderscore(str3) ? mangleLeadingUnderscore(str3) : str3;
/*     */ 
/* 808 */               arrayOfString[(j++)] = str4;
/*     */             }
/*     */ 
/* 811 */             localIDLType = new IDLType(paramClass, arrayOfString, (String)localObject1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 816 */     return localIDLType;
/*     */   }
/*     */ 
/*     */   private static String getPackageName(Class paramClass)
/*     */   {
/* 823 */     Package localPackage = paramClass.getPackage();
/* 824 */     String str1 = null;
/*     */ 
/* 828 */     if (localPackage != null) {
/* 829 */       str1 = localPackage.getName();
/*     */     }
/*     */     else {
/* 832 */       String str2 = paramClass.getName();
/* 833 */       int i = str2.indexOf('.');
/* 834 */       str1 = i == -1 ? null : str2.substring(0, i);
/*     */     }
/*     */ 
/* 837 */     return str1;
/*     */   }
/*     */ 
/*     */   private static String getMappedContainerName(Class paramClass) {
/* 841 */     String str = getUnmappedContainerName(paramClass);
/*     */ 
/* 843 */     return mangleIdentifier(str);
/*     */   }
/*     */ 
/*     */   private static String getUnmappedContainerName(Class paramClass)
/*     */   {
/* 851 */     Object localObject = null;
/* 852 */     String str1 = getPackageName(paramClass);
/*     */ 
/* 854 */     String str2 = paramClass.getName();
/*     */ 
/* 856 */     if (str1 != null) {
/* 857 */       int i = str1.length();
/* 858 */       localObject = str2.substring(i + 1);
/*     */     } else {
/* 860 */       localObject = str2;
/*     */     }
/*     */ 
/* 864 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 888 */     StringBuffer localStringBuffer = new StringBuffer();
/* 889 */     localStringBuffer.append("IDLNameTranslator[");
/* 890 */     for (int i = 0; i < this.interf_.length; i++) {
/* 891 */       if (i != 0)
/* 892 */         localStringBuffer.append(" ");
/* 893 */       localStringBuffer.append(this.interf_[i].getName());
/*     */     }
/* 895 */     localStringBuffer.append("]\n");
/* 896 */     Iterator localIterator = this.methodToIDLNameMap_.keySet().iterator();
/* 897 */     while (localIterator.hasNext())
/*     */     {
/* 899 */       Method localMethod = (Method)localIterator.next();
/* 900 */       String str = (String)this.methodToIDLNameMap_.get(localMethod);
/*     */ 
/* 902 */       localStringBuffer.append(str + ":" + localMethod + "\n");
/*     */     }
/*     */ 
/* 906 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 112 */     for (int i = 0; i < IDL_KEYWORDS.length; i++) {
/* 113 */       String str1 = IDL_KEYWORDS[i];
/*     */ 
/* 116 */       String str2 = str1.toUpperCase();
/* 117 */       idlKeywords_.add(str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IDLMethodInfo
/*     */   {
/*     */     public Method method;
/*     */     public boolean isProperty;
/*     */     public String originalName;
/*     */     public String mangledName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.IDLNameTranslatorImpl
 * JD-Core Version:    0.6.2
 */
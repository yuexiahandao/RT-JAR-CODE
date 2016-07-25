/*     */ package com.sun.xml.internal.ws.policy.privateutil;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ public final class PolicyUtils
/*     */ {
/*     */   public static class Collections
/*     */   {
/*     */     public static <E, T extends Collection<? extends E>, U extends Collection<? extends E>> Collection<Collection<E>> combine(U initialBase, Collection<T> options, boolean ignoreEmptyOption)
/*     */     {
/* 213 */       List combinations = null;
/* 214 */       if ((options == null) || (options.isEmpty()))
/*     */       {
/* 216 */         if (initialBase != null) {
/* 217 */           combinations = new ArrayList(1);
/* 218 */           combinations.add(new ArrayList(initialBase));
/*     */         }
/* 220 */         return combinations;
/*     */       }
/*     */ 
/* 224 */       Collection base = new LinkedList();
/* 225 */       if ((initialBase != null) && (!initialBase.isEmpty())) {
/* 226 */         base.addAll(initialBase);
/*     */       }
/*     */ 
/* 236 */       int finalCombinationsSize = 1;
/* 237 */       Queue optionProcessingQueue = new LinkedList();
/* 238 */       for (Collection option : options) {
/* 239 */         int optionSize = option.size();
/*     */ 
/* 241 */         if (optionSize == 0) {
/* 242 */           if (!ignoreEmptyOption)
/* 243 */             return null;
/*     */         }
/* 245 */         else if (optionSize == 1) {
/* 246 */           base.addAll(option);
/*     */         } else {
/* 248 */           optionProcessingQueue.offer(option);
/* 249 */           finalCombinationsSize *= optionSize;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 254 */       combinations = new ArrayList(finalCombinationsSize);
/* 255 */       combinations.add(base);
/* 256 */       if (finalCombinationsSize > 1)
/*     */       {
/*     */         Collection processedOption;
/*     */         int actualSemiCombinationCollectionSize;
/*     */         int newSemiCombinationCollectionSize;
/*     */         int semiCombinationIndex;
/*     */         Iterator i$;
/* 258 */         while ((processedOption = (Collection)optionProcessingQueue.poll()) != null) {
/* 259 */           actualSemiCombinationCollectionSize = combinations.size();
/* 260 */           newSemiCombinationCollectionSize = actualSemiCombinationCollectionSize * processedOption.size();
/*     */ 
/* 262 */           semiCombinationIndex = 0;
/* 263 */           for (i$ = processedOption.iterator(); i$.hasNext(); ) { Object optionElement = i$.next();
/* 264 */             for (int i = 0; i < actualSemiCombinationCollectionSize; i++) {
/* 265 */               Collection semiCombination = (Collection)combinations.get(semiCombinationIndex);
/*     */ 
/* 267 */               if (semiCombinationIndex + actualSemiCombinationCollectionSize < newSemiCombinationCollectionSize)
/*     */               {
/* 269 */                 combinations.add(new LinkedList(semiCombination));
/*     */               }
/*     */ 
/* 272 */               semiCombination.add(optionElement);
/* 273 */               semiCombinationIndex++;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 278 */       return combinations;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Commons
/*     */   {
/*     */     public static String getStackMethodName(int methodIndexInStack)
/*     */     {
/*  67 */       StackTraceElement[] stack = Thread.currentThread().getStackTrace();
/*     */       String methodName;
/*     */       String methodName;
/*  68 */       if (stack.length > methodIndexInStack + 1)
/*  69 */         methodName = stack[methodIndexInStack].getMethodName();
/*     */       else {
/*  71 */         methodName = "UNKNOWN METHOD";
/*     */       }
/*     */ 
/*  74 */       return methodName;
/*     */     }
/*     */ 
/*     */     public static String getCallerMethodName()
/*     */     {
/*  84 */       String result = getStackMethodName(5);
/*  85 */       if (result.equals("invoke0"))
/*     */       {
/*  87 */         result = getStackMethodName(4);
/*     */       }
/*  89 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Comparison
/*     */   {
/* 163 */     public static final Comparator<QName> QNAME_COMPARATOR = new Comparator() {
/*     */       public int compare(QName qn1, QName qn2) {
/* 165 */         if ((qn1 == qn2) || (qn1.equals(qn2))) {
/* 166 */           return 0;
/*     */         }
/*     */ 
/* 171 */         int result = qn1.getNamespaceURI().compareTo(qn2.getNamespaceURI());
/* 172 */         if (result != 0) {
/* 173 */           return result;
/*     */         }
/*     */ 
/* 176 */         return qn1.getLocalPart().compareTo(qn2.getLocalPart());
/*     */       }
/* 163 */     };
/*     */ 
/*     */     public static int compareBoolean(boolean b1, boolean b2)
/*     */     {
/* 186 */       int i1 = b1 ? 1 : 0;
/* 187 */       int i2 = b2 ? 1 : 0;
/*     */ 
/* 189 */       return i1 - i2;
/*     */     }
/*     */ 
/*     */     public static int compareNullableStrings(String s1, String s2)
/*     */     {
/* 198 */       return s2 == null ? 1 : s1 == null ? -1 : s2 == null ? 0 : s1.compareTo(s2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ConfigFile
/*     */   {
/*     */     public static String generateFullName(String configFileIdentifier)
/*     */       throws PolicyException
/*     */     {
/* 349 */       if (configFileIdentifier != null) {
/* 350 */         StringBuffer buffer = new StringBuffer("wsit-");
/* 351 */         buffer.append(configFileIdentifier).append(".xml");
/* 352 */         return buffer.toString();
/*     */       }
/* 354 */       throw new PolicyException(LocalizationMessages.WSP_0080_IMPLEMENTATION_EXPECTED_NOT_NULL());
/*     */     }
/*     */ 
/*     */     public static URL loadFromContext(String configFileName, Object context)
/*     */     {
/* 368 */       return (URL)PolicyUtils.Reflection.invoke(context, "getResource", URL.class, new Object[] { configFileName });
/*     */     }
/*     */ 
/*     */     public static URL loadFromClasspath(String configFileName)
/*     */     {
/* 380 */       ClassLoader cl = Thread.currentThread().getContextClassLoader();
/* 381 */       if (cl == null) {
/* 382 */         return ClassLoader.getSystemResource(configFileName);
/*     */       }
/* 384 */       return cl.getResource(configFileName);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class IO
/*     */   {
/*  94 */     private static final PolicyLogger LOGGER = PolicyLogger.getLogger(IO.class);
/*     */ 
/*     */     public static void closeResource(Closeable resource)
/*     */     {
/* 104 */       if (resource != null)
/*     */         try {
/* 106 */           resource.close();
/*     */         } catch (IOException e) {
/* 108 */           LOGGER.warning(LocalizationMessages.WSP_0023_UNEXPECTED_ERROR_WHILE_CLOSING_RESOURCE(resource.toString()), e);
/*     */         }
/*     */     }
/*     */ 
/*     */     public static void closeResource(XMLStreamReader reader)
/*     */     {
/* 121 */       if (reader != null)
/*     */         try {
/* 123 */           reader.close();
/*     */         } catch (XMLStreamException e) {
/* 125 */           LOGGER.warning(LocalizationMessages.WSP_0023_UNEXPECTED_ERROR_WHILE_CLOSING_RESOURCE(reader.toString()), e);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Reflection
/*     */   {
/* 286 */     private static final PolicyLogger LOGGER = PolicyLogger.getLogger(Reflection.class);
/*     */ 
/*     */     static <T> T invoke(Object target, String methodName, Class<T> resultClass, Object[] parameters)
/*     */       throws RuntimePolicyUtilsException
/*     */     {
/*     */       Class[] parameterTypes;
/* 294 */       if ((parameters != null) && (parameters.length > 0)) {
/* 295 */         Class[] parameterTypes = new Class[parameters.length];
/* 296 */         int i = 0;
/* 297 */         for (Object parameter : parameters)
/* 298 */           parameterTypes[(i++)] = parameter.getClass();
/*     */       }
/*     */       else {
/* 301 */         parameterTypes = null;
/*     */       }
/*     */ 
/* 304 */       return invoke(target, methodName, resultClass, parameters, parameterTypes);
/*     */     }
/*     */ 
/*     */     public static <T> T invoke(Object target, String methodName, Class<T> resultClass, Object[] parameters, Class[] parameterTypes)
/*     */       throws RuntimePolicyUtilsException
/*     */     {
/*     */       try
/*     */       {
/* 313 */         Method method = target.getClass().getMethod(methodName, parameterTypes);
/* 314 */         Object result = MethodUtil.invoke(target, method, parameters);
/*     */ 
/* 316 */         return resultClass.cast(result);
/*     */       } catch (IllegalArgumentException e) {
/* 318 */         throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e)));
/*     */       } catch (InvocationTargetException e) {
/* 320 */         throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e)));
/*     */       } catch (IllegalAccessException e) {
/* 322 */         throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e.getCause())));
/*     */       } catch (SecurityException e) {
/* 324 */         throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e)));
/*     */       } catch (NoSuchMethodException e) {
/* 326 */         throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(createExceptionMessage(target, parameters, methodName), e)));
/*     */       }
/*     */     }
/*     */ 
/*     */     private static String createExceptionMessage(Object target, Object[] parameters, String methodName) {
/* 331 */       return LocalizationMessages.WSP_0061_METHOD_INVOCATION_FAILED(target.getClass().getName(), methodName, parameters == null ? null : Arrays.asList(parameters).toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Rfc2396
/*     */   {
/* 446 */     private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyUtils.Reflection.class);
/*     */ 
/*     */     public static String unquote(String quoted)
/*     */     {
/* 450 */       if (null == quoted) {
/* 451 */         return null;
/*     */       }
/* 453 */       byte[] unquoted = new byte[quoted.length()];
/* 454 */       int newLength = 0;
/*     */ 
/* 457 */       for (int i = 0; i < quoted.length(); i++) {
/* 458 */         char c = quoted.charAt(i);
/* 459 */         if ('%' == c) {
/* 460 */           if (i + 2 >= quoted.length()) {
/* 461 */             throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(LocalizationMessages.WSP_0079_ERROR_WHILE_RFC_2396_UNESCAPING(quoted)), false));
/*     */           }
/* 463 */           int hi = Character.digit(quoted.charAt(++i), 16);
/* 464 */           int lo = Character.digit(quoted.charAt(++i), 16);
/* 465 */           if ((0 > hi) || (0 > lo)) {
/* 466 */             throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(LocalizationMessages.WSP_0079_ERROR_WHILE_RFC_2396_UNESCAPING(quoted)), false));
/*     */           }
/* 468 */           unquoted[(newLength++)] = ((byte)(hi * 16 + lo));
/*     */         } else {
/* 470 */           unquoted[(newLength++)] = ((byte)c);
/*     */         }
/*     */       }
/*     */       try {
/* 474 */         return new String(unquoted, 0, newLength, "utf-8");
/*     */       } catch (UnsupportedEncodingException uee) {
/* 476 */         throw ((RuntimePolicyUtilsException)LOGGER.logSevereException(new RuntimePolicyUtilsException(LocalizationMessages.WSP_0079_ERROR_WHILE_RFC_2396_UNESCAPING(quoted), uee)));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class ServiceProvider
/*     */   {
/*     */     public static <T> T[] load(Class<T> serviceClass, ClassLoader loader)
/*     */     {
/* 419 */       return ServiceFinder.find(serviceClass, loader).toArray();
/*     */     }
/*     */ 
/*     */     public static <T> T[] load(Class<T> serviceClass)
/*     */     {
/* 440 */       return ServiceFinder.find(serviceClass).toArray();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Text
/*     */   {
/* 139 */     public static final String NEW_LINE = System.getProperty("line.separator");
/*     */ 
/*     */     public static String createIndent(int indentLevel)
/*     */     {
/* 149 */       char[] charData = new char[indentLevel * 4];
/* 150 */       Arrays.fill(charData, ' ');
/* 151 */       return String.valueOf(charData);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.privateutil.PolicyUtils
 * JD-Core Version:    0.6.2
 */
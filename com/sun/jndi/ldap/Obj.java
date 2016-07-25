/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttribute;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InvalidAttributeValueException;
/*     */ import javax.naming.directory.InvalidAttributesException;
/*     */ import javax.naming.spi.DirStateFactory.Result;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ import sun.misc.BASE64Decoder;
/*     */ import sun.misc.BASE64Encoder;
/*     */ 
/*     */ final class Obj
/*     */ {
/*  63 */   static VersionHelper helper = VersionHelper.getVersionHelper();
/*     */ 
/*  66 */   static final String[] JAVA_ATTRIBUTES = { "objectClass", "javaSerializedData", "javaClassName", "javaFactory", "javaCodeBase", "javaReferenceAddress", "javaClassNames", "javaRemoteLocation" };
/*     */   static final int OBJECT_CLASS = 0;
/*     */   static final int SERIALIZED_DATA = 1;
/*     */   static final int CLASSNAME = 2;
/*     */   static final int FACTORY = 3;
/*     */   static final int CODEBASE = 4;
/*     */   static final int REF_ADDR = 5;
/*     */   static final int TYPENAME = 6;
/*     */ 
/*     */   /** @deprecated */
/*     */   private static final int REMOTE_LOC = 7;
/*  90 */   static final String[] JAVA_OBJECT_CLASSES = { "javaContainer", "javaObject", "javaNamingReference", "javaSerializedObject", "javaMarshalledObject" };
/*     */ 
/*  98 */   static final String[] JAVA_OBJECT_CLASSES_LOWER = { "javacontainer", "javaobject", "javanamingreference", "javaserializedobject", "javamarshalledobject" };
/*     */   static final int STRUCTURAL = 0;
/*     */   static final int BASE_OBJECT = 1;
/*     */   static final int REF_OBJECT = 2;
/*     */   static final int SER_OBJECT = 3;
/*     */   static final int MAR_OBJECT = 4;
/*     */ 
/*     */   private static Attributes encodeObject(char paramChar, Object paramObject, Attributes paramAttributes, Attribute paramAttribute, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 132 */     int i = (paramAttribute.size() == 0) || ((paramAttribute.size() == 1) && (paramAttribute.contains("top"))) ? 1 : 0;
/*     */ 
/* 136 */     if (i != 0) {
/* 137 */       paramAttribute.add(JAVA_OBJECT_CLASSES[0]);
/*     */     }
/*     */ 
/* 141 */     if ((paramObject instanceof Referenceable)) {
/* 142 */       paramAttribute.add(JAVA_OBJECT_CLASSES[1]);
/* 143 */       paramAttribute.add(JAVA_OBJECT_CLASSES[2]);
/* 144 */       if (!paramBoolean) {
/* 145 */         paramAttributes = (Attributes)paramAttributes.clone();
/*     */       }
/* 147 */       paramAttributes.put(paramAttribute);
/* 148 */       return encodeReference(paramChar, ((Referenceable)paramObject).getReference(), paramAttributes, paramObject);
/*     */     }
/*     */ 
/* 152 */     if ((paramObject instanceof Reference)) {
/* 153 */       paramAttribute.add(JAVA_OBJECT_CLASSES[1]);
/* 154 */       paramAttribute.add(JAVA_OBJECT_CLASSES[2]);
/* 155 */       if (!paramBoolean) {
/* 156 */         paramAttributes = (Attributes)paramAttributes.clone();
/*     */       }
/* 158 */       paramAttributes.put(paramAttribute);
/* 159 */       return encodeReference(paramChar, (Reference)paramObject, paramAttributes, null);
/*     */     }
/*     */ 
/* 162 */     if ((paramObject instanceof Serializable)) {
/* 163 */       paramAttribute.add(JAVA_OBJECT_CLASSES[1]);
/* 164 */       if ((!paramAttribute.contains(JAVA_OBJECT_CLASSES[4])) && (!paramAttribute.contains(JAVA_OBJECT_CLASSES_LOWER[4])))
/*     */       {
/* 166 */         paramAttribute.add(JAVA_OBJECT_CLASSES[3]);
/*     */       }
/* 168 */       if (!paramBoolean) {
/* 169 */         paramAttributes = (Attributes)paramAttributes.clone();
/*     */       }
/* 171 */       paramAttributes.put(paramAttribute);
/* 172 */       paramAttributes.put(new BasicAttribute(JAVA_ATTRIBUTES[1], serializeObject(paramObject)));
/*     */ 
/* 174 */       if (paramAttributes.get(JAVA_ATTRIBUTES[2]) == null) {
/* 175 */         paramAttributes.put(JAVA_ATTRIBUTES[2], paramObject.getClass().getName());
/*     */       }
/*     */ 
/* 178 */       if (paramAttributes.get(JAVA_ATTRIBUTES[6]) == null) {
/* 179 */         Attribute localAttribute = LdapCtxFactory.createTypeNameAttr(paramObject.getClass());
/*     */ 
/* 181 */         if (localAttribute != null) {
/* 182 */           paramAttributes.put(localAttribute);
/*     */         }
/*     */       }
/*     */     }
/* 186 */     else if (!(paramObject instanceof DirContext))
/*     */     {
/* 189 */       throw new IllegalArgumentException("can only bind Referenceable, Serializable, DirContext");
/*     */     }
/*     */ 
/* 193 */     return paramAttributes;
/*     */   }
/*     */ 
/*     */   private static String[] getCodebases(Attribute paramAttribute)
/*     */     throws NamingException
/*     */   {
/* 204 */     if (paramAttribute == null) {
/* 205 */       return null;
/*     */     }
/* 207 */     StringTokenizer localStringTokenizer = new StringTokenizer((String)paramAttribute.get());
/*     */ 
/* 209 */     Vector localVector = new Vector(10);
/* 210 */     while (localStringTokenizer.hasMoreTokens()) {
/* 211 */       localVector.addElement(localStringTokenizer.nextToken());
/*     */     }
/* 213 */     String[] arrayOfString = new String[localVector.size()];
/* 214 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 215 */       arrayOfString[i] = ((String)localVector.elementAt(i));
/*     */     }
/* 217 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   static Object decodeObject(Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 234 */     String[] arrayOfString = getCodebases(paramAttributes.get(JAVA_ATTRIBUTES[4]));
/*     */     try {
/* 236 */       if ((localAttribute = paramAttributes.get(JAVA_ATTRIBUTES[1])) != null) {
/* 237 */         ClassLoader localClassLoader = helper.getURLClassLoader(arrayOfString);
/* 238 */         return deserializeObject((byte[])localAttribute.get(), localClassLoader);
/* 239 */       }if ((localAttribute = paramAttributes.get(JAVA_ATTRIBUTES[7])) != null)
/*     */       {
/* 241 */         return decodeRmiObject((String)paramAttributes.get(JAVA_ATTRIBUTES[2]).get(), (String)localAttribute.get(), arrayOfString);
/*     */       }
/*     */ 
/* 246 */       Attribute localAttribute = paramAttributes.get(JAVA_ATTRIBUTES[0]);
/* 247 */       if ((localAttribute != null) && ((localAttribute.contains(JAVA_OBJECT_CLASSES[2])) || (localAttribute.contains(JAVA_OBJECT_CLASSES_LOWER[2]))))
/*     */       {
/* 250 */         return decodeReference(paramAttributes, arrayOfString);
/*     */       }
/* 252 */       return null;
/*     */     } catch (IOException localIOException) {
/* 254 */       NamingException localNamingException = new NamingException();
/* 255 */       localNamingException.setRootCause(localIOException);
/* 256 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Attributes encodeReference(char paramChar, Reference paramReference, Attributes paramAttributes, Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 293 */     if (paramReference == null)
/* 294 */       return paramAttributes;
/*     */     String str;
/* 298 */     if ((str = paramReference.getClassName()) != null) {
/* 299 */       paramAttributes.put(new BasicAttribute(JAVA_ATTRIBUTES[2], str));
/*     */     }
/*     */ 
/* 302 */     if ((str = paramReference.getFactoryClassName()) != null) {
/* 303 */       paramAttributes.put(new BasicAttribute(JAVA_ATTRIBUTES[3], str));
/*     */     }
/*     */ 
/* 306 */     if ((str = paramReference.getFactoryClassLocation()) != null) {
/* 307 */       paramAttributes.put(new BasicAttribute(JAVA_ATTRIBUTES[4], str));
/*     */     }
/*     */ 
/* 312 */     if ((paramObject != null) && (paramAttributes.get(JAVA_ATTRIBUTES[6]) != null)) {
/* 313 */       Attribute localAttribute = LdapCtxFactory.createTypeNameAttr(paramObject.getClass());
/*     */ 
/* 315 */       if (localAttribute != null) {
/* 316 */         paramAttributes.put(localAttribute);
/*     */       }
/*     */     }
/*     */ 
/* 320 */     int i = paramReference.size();
/*     */ 
/* 322 */     if (i > 0)
/*     */     {
/* 324 */       BasicAttribute localBasicAttribute = new BasicAttribute(JAVA_ATTRIBUTES[5]);
/*     */ 
/* 326 */       BASE64Encoder localBASE64Encoder = null;
/*     */ 
/* 328 */       for (int j = 0; j < i; j++) {
/* 329 */         RefAddr localRefAddr = paramReference.get(j);
/*     */ 
/* 331 */         if ((localRefAddr instanceof StringRefAddr)) {
/* 332 */           localBasicAttribute.add("" + paramChar + j + paramChar + localRefAddr.getType() + paramChar + localRefAddr.getContent());
/*     */         }
/*     */         else
/*     */         {
/* 336 */           if (localBASE64Encoder == null) {
/* 337 */             localBASE64Encoder = new BASE64Encoder();
/*     */           }
/* 339 */           localBasicAttribute.add("" + paramChar + j + paramChar + localRefAddr.getType() + paramChar + paramChar + localBASE64Encoder.encodeBuffer(serializeObject(localRefAddr)));
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 345 */       paramAttributes.put(localBasicAttribute);
/*     */     }
/* 347 */     return paramAttributes;
/*     */   }
/*     */ 
/*     */   private static Object decodeRmiObject(String paramString1, String paramString2, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 366 */     return new Reference(paramString1, new StringRefAddr("URL", paramString2));
/*     */   }
/*     */ 
/*     */   private static Reference decodeReference(Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException, IOException
/*     */   {
/* 377 */     String str2 = null;
/*     */     Attribute localAttribute;
/*     */     String str1;
/* 379 */     if ((localAttribute = paramAttributes.get(JAVA_ATTRIBUTES[2])) != null)
/* 380 */       str1 = (String)localAttribute.get();
/*     */     else {
/* 382 */       throw new InvalidAttributesException(JAVA_ATTRIBUTES[2] + " attribute is required");
/*     */     }
/*     */ 
/* 386 */     if ((localAttribute = paramAttributes.get(JAVA_ATTRIBUTES[3])) != null) {
/* 387 */       str2 = (String)localAttribute.get();
/*     */     }
/*     */ 
/* 390 */     Reference localReference = new Reference(str1, str2, paramArrayOfString != null ? paramArrayOfString[0] : null);
/*     */ 
/* 400 */     if ((localAttribute = paramAttributes.get(JAVA_ATTRIBUTES[5])) != null)
/*     */     {
/* 405 */       BASE64Decoder localBASE64Decoder = null;
/*     */ 
/* 407 */       ClassLoader localClassLoader = helper.getURLClassLoader(paramArrayOfString);
/*     */ 
/* 413 */       Vector localVector = new Vector();
/* 414 */       localVector.setSize(localAttribute.size());
/*     */ 
/* 416 */       for (NamingEnumeration localNamingEnumeration = localAttribute.getAll(); localNamingEnumeration.hasMore(); )
/*     */       {
/* 418 */         String str3 = (String)localNamingEnumeration.next();
/*     */ 
/* 420 */         if (str3.length() == 0) {
/* 421 */           throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - " + "empty attribute value");
/*     */         }
/*     */ 
/* 426 */         char c = str3.charAt(0);
/* 427 */         int i = 1;
/*     */         int j;
/* 430 */         if ((j = str3.indexOf(c, i)) < 0)
/* 431 */           throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - " + "separator '" + c + "'" + "not found");
/*     */         String str4;
/* 435 */         if ((str4 = str3.substring(i, j)) == null) {
/* 436 */           throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - " + "empty RefAddr position");
/*     */         }
/*     */         int k;
/*     */         try
/*     */         {
/* 441 */           k = Integer.parseInt(str4);
/*     */         } catch (NumberFormatException localNumberFormatException) {
/* 443 */           throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - " + "RefAddr position not an integer");
/*     */         }
/*     */ 
/* 447 */         i = j + 1;
/*     */ 
/* 450 */         if ((j = str3.indexOf(c, i)) < 0)
/* 451 */           throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - " + "RefAddr type not found");
/*     */         String str5;
/* 455 */         if ((str5 = str3.substring(i, j)) == null) {
/* 456 */           throw new InvalidAttributeValueException("malformed " + JAVA_ATTRIBUTES[5] + " attribute - " + "empty RefAddr type");
/*     */         }
/*     */ 
/* 460 */         i = j + 1;
/*     */ 
/* 463 */         if (i == str3.length())
/*     */         {
/* 465 */           localVector.setElementAt(new StringRefAddr(str5, null), k);
/* 466 */         } else if (str3.charAt(i) == c)
/*     */         {
/* 470 */           i++;
/*     */ 
/* 473 */           if (localBASE64Decoder == null) {
/* 474 */             localBASE64Decoder = new BASE64Decoder();
/*     */           }
/* 476 */           RefAddr localRefAddr = (RefAddr)deserializeObject(localBASE64Decoder.decodeBuffer(str3.substring(i)), localClassLoader);
/*     */ 
/* 481 */           localVector.setElementAt(localRefAddr, k);
/*     */         }
/*     */         else {
/* 484 */           localVector.setElementAt(new StringRefAddr(str5, str3.substring(i)), k);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 490 */       for (int m = 0; m < localVector.size(); m++) {
/* 491 */         localReference.add((RefAddr)localVector.elementAt(m));
/*     */       }
/*     */     }
/*     */ 
/* 495 */     return localReference;
/*     */   }
/*     */ 
/*     */   private static byte[] serializeObject(Object paramObject)
/*     */     throws NamingException
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 504 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 505 */       localObject = new ObjectOutputStream(localByteArrayOutputStream);
/* 506 */       ((ObjectOutputStream)localObject).writeObject(paramObject);
/* 507 */       ((ObjectOutputStream)localObject).close();
/*     */ 
/* 509 */       return localByteArrayOutputStream.toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 512 */       localObject = new NamingException();
/* 513 */       ((NamingException)localObject).setRootCause(localIOException);
/* 514 */     }throw ((Throwable)localObject);
/*     */   }
/*     */ 
/*     */   private static Object deserializeObject(byte[] paramArrayOfByte, ClassLoader paramClassLoader)
/*     */     throws NamingException
/*     */   {
/*     */     Object localObject1;
/*     */     try
/*     */     {
/* 526 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/* 527 */       localObject1 = paramClassLoader == null ? new ObjectInputStream(localByteArrayInputStream) : new LoaderInputStream(localByteArrayInputStream, paramClassLoader);
/*     */       try
/*     */       {
/* 532 */         return ((ObjectInputStream)localObject1).readObject();
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 534 */         NamingException localNamingException = new NamingException();
/* 535 */         localNamingException.setRootCause(localClassNotFoundException);
/* 536 */         throw localNamingException;
/*     */       } finally {
/* 538 */         ((ObjectInputStream)localObject1).close();
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 541 */       localObject1 = new NamingException();
/* 542 */       ((NamingException)localObject1).setRootCause(localIOException);
/* 543 */     }throw ((Throwable)localObject1);
/*     */   }
/*     */ 
/*     */   static Attributes determineBindAttrs(char paramChar, Object paramObject, Attributes paramAttributes, boolean paramBoolean, Name paramName, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 556 */     DirStateFactory.Result localResult = DirectoryManager.getStateToBind(paramObject, paramName, paramContext, paramHashtable, paramAttributes);
/*     */ 
/* 558 */     paramObject = localResult.getObject();
/* 559 */     paramAttributes = localResult.getAttributes();
/*     */ 
/* 562 */     if (paramObject == null) {
/* 563 */       return paramAttributes;
/*     */     }
/*     */ 
/* 567 */     if ((paramAttributes == null) && ((paramObject instanceof DirContext))) {
/* 568 */       paramBoolean = true;
/* 569 */       paramAttributes = ((DirContext)paramObject).getAttributes("");
/*     */     }
/*     */ 
/* 572 */     int i = 0;
/*     */     Object localObject;
/* 576 */     if ((paramAttributes == null) || (paramAttributes.size() == 0)) {
/* 577 */       paramAttributes = new BasicAttributes(true);
/* 578 */       paramBoolean = true;
/*     */ 
/* 581 */       localObject = new BasicAttribute("objectClass", "top");
/*     */     }
/*     */     else
/*     */     {
/* 585 */       localObject = paramAttributes.get("objectClass");
/* 586 */       if ((localObject == null) && (!paramAttributes.isCaseIgnored()))
/*     */       {
/* 588 */         localObject = paramAttributes.get("objectclass");
/*     */       }
/*     */ 
/* 592 */       if (localObject == null)
/* 593 */         localObject = new BasicAttribute("objectClass", "top");
/* 594 */       else if ((i != 0) || (!paramBoolean)) {
/* 595 */         localObject = (Attribute)((Attribute)localObject).clone();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 600 */     paramAttributes = encodeObject(paramChar, paramObject, paramAttributes, (Attribute)localObject, paramBoolean);
/*     */ 
/* 603 */     return paramAttributes;
/*     */   }
/*     */ 
/*     */   private static final class LoaderInputStream extends ObjectInputStream
/*     */   {
/*     */     private ClassLoader classLoader;
/*     */ 
/*     */     LoaderInputStream(InputStream paramInputStream, ClassLoader paramClassLoader)
/*     */       throws IOException
/*     */     {
/* 613 */       super();
/* 614 */       this.classLoader = paramClassLoader;
/*     */     }
/*     */ 
/*     */     protected Class resolveClass(ObjectStreamClass paramObjectStreamClass)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/*     */       try
/*     */       {
/* 622 */         return this.classLoader.loadClass(paramObjectStreamClass.getName()); } catch (ClassNotFoundException localClassNotFoundException) {
/*     */       }
/* 624 */       return super.resolveClass(paramObjectStreamClass);
/*     */     }
/*     */ 
/*     */     protected Class resolveProxyClass(String[] paramArrayOfString)
/*     */       throws IOException, ClassNotFoundException
/*     */     {
/* 630 */       ClassLoader localClassLoader = null;
/* 631 */       int i = 0;
/*     */ 
/* 634 */       Class[] arrayOfClass = new Class[paramArrayOfString.length];
/* 635 */       for (int j = 0; j < paramArrayOfString.length; j++) {
/* 636 */         Class localClass = Class.forName(paramArrayOfString[j], false, this.classLoader);
/* 637 */         if ((localClass.getModifiers() & 0x1) == 0) {
/* 638 */           if (i != 0) {
/* 639 */             if (localClassLoader != localClass.getClassLoader())
/* 640 */               throw new IllegalAccessError("conflicting non-public interface class loaders");
/*     */           }
/*     */           else
/*     */           {
/* 644 */             localClassLoader = localClass.getClassLoader();
/* 645 */             i = 1;
/*     */           }
/*     */         }
/* 648 */         arrayOfClass[j] = localClass;
/*     */       }
/*     */       try {
/* 651 */         return Proxy.getProxyClass(i != 0 ? localClassLoader : this.classLoader, arrayOfClass);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException) {
/* 654 */         throw new ClassNotFoundException(null, localIllegalArgumentException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.Obj
 * JD-Core Version:    0.6.2
 */
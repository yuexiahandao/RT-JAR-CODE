/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.url.ldap.ldapURLContextFactory;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.naming.AuthenticationException;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.BasicAttribute;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.naming.spi.InitialContextFactory;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ 
/*     */ public final class LdapCtxFactory
/*     */   implements ObjectFactory, InitialContextFactory
/*     */ {
/*     */   public static final String ADDRESS_TYPE = "URL";
/*     */ 
/*     */   public Object getObjectInstance(Object paramObject, Name paramName, Context paramContext, Hashtable<?, ?> paramHashtable)
/*     */     throws Exception
/*     */   {
/*  52 */     if (!isLdapRef(paramObject)) {
/*  53 */       return null;
/*     */     }
/*  55 */     ldapURLContextFactory localldapURLContextFactory = new ldapURLContextFactory();
/*  56 */     String[] arrayOfString = getURLs((Reference)paramObject);
/*  57 */     return localldapURLContextFactory.getObjectInstance(arrayOfString, paramName, paramContext, paramHashtable);
/*     */   }
/*     */ 
/*     */   public Context getInitialContext(Hashtable<?, ?> paramHashtable)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/*  66 */       String str = paramHashtable != null ? (String)paramHashtable.get("java.naming.provider.url") : null;
/*     */ 
/*  70 */       if (str == null) {
/*  71 */         return new LdapCtx("", "localhost", 389, paramHashtable, false);
/*     */       }
/*     */ 
/*  76 */       arrayOfString = LdapURL.fromList(str);
/*     */ 
/*  78 */       if (arrayOfString.length == 0) {
/*  79 */         throw new ConfigurationException("java.naming.provider.url property does not contain a URL");
/*     */       }
/*     */ 
/*  84 */       return getLdapCtxInstance(arrayOfString, paramHashtable);
/*     */     }
/*     */     catch (LdapReferralException localLdapReferralException)
/*     */     {
/*  88 */       if ((paramHashtable != null) && ("throw".equals(paramHashtable.get("java.naming.referral"))))
/*     */       {
/*  90 */         throw localLdapReferralException;
/*     */       }
/*     */ 
/*  93 */       String[] arrayOfString = paramHashtable != null ? (Control[])paramHashtable.get("java.naming.ldap.control.connect") : null;
/*     */ 
/*  96 */       return (LdapCtx)localLdapReferralException.getReferralContext(paramHashtable, arrayOfString);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isLdapRef(Object paramObject)
/*     */   {
/* 105 */     if (!(paramObject instanceof Reference)) {
/* 106 */       return false;
/*     */     }
/* 108 */     String str = LdapCtxFactory.class.getName();
/* 109 */     Reference localReference = (Reference)paramObject;
/*     */ 
/* 111 */     return str.equals(localReference.getFactoryClassName());
/*     */   }
/*     */ 
/*     */   private static String[] getURLs(Reference paramReference)
/*     */     throws NamingException
/*     */   {
/* 119 */     int i = 0;
/* 120 */     String[] arrayOfString = new String[paramReference.size()];
/*     */ 
/* 122 */     Enumeration localEnumeration = paramReference.getAll();
/* 123 */     while (localEnumeration.hasMoreElements()) {
/* 124 */       localObject = (RefAddr)localEnumeration.nextElement();
/*     */ 
/* 126 */       if (((localObject instanceof StringRefAddr)) && (((RefAddr)localObject).getType().equals("URL")))
/*     */       {
/* 129 */         arrayOfString[(i++)] = ((String)((RefAddr)localObject).getContent());
/*     */       }
/*     */     }
/* 132 */     if (i == 0) {
/* 133 */       throw new ConfigurationException("Reference contains no valid addresses");
/*     */     }
/*     */ 
/* 138 */     if (i == paramReference.size()) {
/* 139 */       return arrayOfString;
/*     */     }
/* 141 */     Object localObject = new String[i];
/* 142 */     System.arraycopy(arrayOfString, 0, localObject, 0, i);
/* 143 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static DirContext getLdapCtxInstance(Object paramObject, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 151 */     if ((paramObject instanceof String))
/* 152 */       return getUsingURL((String)paramObject, paramHashtable);
/* 153 */     if ((paramObject instanceof String[])) {
/* 154 */       return getUsingURLs((String[])paramObject, paramHashtable);
/*     */     }
/* 156 */     throw new IllegalArgumentException("argument must be an LDAP URL String or array of them");
/*     */   }
/*     */ 
/*     */   private static DirContext getUsingURL(String paramString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 163 */     Object localObject = null;
/* 164 */     LdapURL localLdapURL = new LdapURL(paramString);
/* 165 */     String str1 = localLdapURL.getDN();
/* 166 */     String str2 = localLdapURL.getHost();
/* 167 */     int i = localLdapURL.getPort();
/*     */ 
/* 169 */     String str3 = null;
/*     */     String[] arrayOfString1;
/* 173 */     if ((str2 == null) && (i == -1) && (str1 != null) && ((str3 = ServiceLocator.mapDnToDomainName(str1)) != null) && ((arrayOfString1 = ServiceLocator.getLdapService(str3, paramHashtable)) != null))
/*     */     {
/* 181 */       String str4 = localLdapURL.getScheme() + "://";
/* 182 */       String[] arrayOfString2 = new String[arrayOfString1.length];
/* 183 */       String str5 = localLdapURL.getQuery();
/* 184 */       String str6 = localLdapURL.getPath() + (str5 != null ? str5 : "");
/* 185 */       for (int j = 0; j < arrayOfString1.length; j++) {
/* 186 */         arrayOfString2[j] = (str4 + arrayOfString1[j] + str6);
/*     */       }
/* 188 */       localObject = getUsingURLs(arrayOfString2, paramHashtable);
/*     */ 
/* 190 */       ((LdapCtx)localObject).setDomainName(str3);
/*     */     }
/*     */     else {
/* 193 */       localObject = new LdapCtx(str1, str2, i, paramHashtable, localLdapURL.useSsl());
/*     */ 
/* 195 */       ((LdapCtx)localObject).setProviderUrl(paramString);
/*     */     }
/* 197 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static DirContext getUsingURLs(String[] paramArrayOfString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 207 */     Object localObject1 = null;
/* 208 */     Object localObject2 = null;
/* 209 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*     */       try {
/* 211 */         return getUsingURL(paramArrayOfString[i], paramHashtable);
/*     */       } catch (AuthenticationException localAuthenticationException) {
/* 213 */         throw localAuthenticationException;
/*     */       } catch (NamingException localNamingException) {
/* 215 */         localObject1 = localNamingException;
/*     */       }
/*     */     }
/* 218 */     throw localObject1;
/*     */   }
/*     */ 
/*     */   public static Attribute createTypeNameAttr(Class paramClass)
/*     */   {
/* 225 */     Vector localVector = new Vector(10);
/* 226 */     String[] arrayOfString = getTypeNames(paramClass, localVector);
/* 227 */     if (arrayOfString.length > 0) {
/* 228 */       BasicAttribute localBasicAttribute = new BasicAttribute(Obj.JAVA_ATTRIBUTES[6]);
/*     */ 
/* 230 */       for (int i = 0; i < arrayOfString.length; i++) {
/* 231 */         localBasicAttribute.add(arrayOfString[i]);
/*     */       }
/* 233 */       return localBasicAttribute;
/*     */     }
/* 235 */     return null;
/*     */   }
/*     */ 
/*     */   private static String[] getTypeNames(Class paramClass, Vector paramVector)
/*     */   {
/* 240 */     getClassesAux(paramClass, paramVector);
/* 241 */     Class[] arrayOfClass = paramClass.getInterfaces();
/* 242 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 243 */       getClassesAux(arrayOfClass[i], paramVector);
/*     */     }
/* 245 */     String[] arrayOfString = new String[paramVector.size()];
/* 246 */     int j = 0;
/* 247 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); ) {
/* 248 */       arrayOfString[(j++)] = ((String)localEnumeration.nextElement());
/*     */     }
/* 250 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   private static void getClassesAux(Class paramClass, Vector paramVector) {
/* 254 */     if (!paramVector.contains(paramClass.getName())) {
/* 255 */       paramVector.addElement(paramClass.getName());
/*     */     }
/* 257 */     paramClass = paramClass.getSuperclass();
/*     */ 
/* 259 */     while (paramClass != null) {
/* 260 */       getTypeNames(paramClass, paramVector);
/* 261 */       paramClass = paramClass.getSuperclass();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapCtxFactory
 * JD-Core Version:    0.6.2
 */
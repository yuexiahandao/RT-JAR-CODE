/*     */ package com.sun.jndi.url.ldap;
/*     */ 
/*     */ import com.sun.jndi.ldap.LdapURL;
/*     */ import com.sun.jndi.toolkit.url.GenericURLDirContext;
/*     */ import java.util.Hashtable;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.spi.ResolveResult;
/*     */ 
/*     */ public final class ldapURLContext extends GenericURLDirContext
/*     */ {
/*     */   ldapURLContext(Hashtable paramHashtable)
/*     */   {
/*  46 */     super(paramHashtable);
/*     */   }
/*     */ 
/*     */   protected ResolveResult getRootURLContext(String paramString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  61 */     return ldapURLContextFactory.getUsingURLIgnoreRootDN(paramString, paramHashtable);
/*     */   }
/*     */ 
/*     */   protected Name getURLSuffix(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/*  71 */     LdapURL localLdapURL = new LdapURL(paramString2);
/*  72 */     String str = localLdapURL.getDN() != null ? localLdapURL.getDN() : "";
/*     */ 
/*  75 */     CompositeName localCompositeName = new CompositeName();
/*  76 */     if (!"".equals(str))
/*     */     {
/*  78 */       localCompositeName.add(str);
/*     */     }
/*  80 */     return localCompositeName;
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString)
/*     */     throws NamingException
/*     */   {
/*  91 */     if (LdapURL.hasQueryComponents(paramString)) {
/*  92 */       throw new InvalidNameException(paramString);
/*     */     }
/*  94 */     return super.lookup(paramString);
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException
/*     */   {
/*  99 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 100 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 102 */     return super.lookup(paramName);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 107 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 108 */       throw new InvalidNameException(paramString);
/*     */     }
/* 110 */     super.bind(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject) throws NamingException
/*     */   {
/* 115 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 116 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 118 */     super.bind(paramName, paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException
/*     */   {
/* 123 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 124 */       throw new InvalidNameException(paramString);
/*     */     }
/* 126 */     super.rebind(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException
/*     */   {
/* 131 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 132 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 134 */     super.rebind(paramName, paramObject);
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString) throws NamingException
/*     */   {
/* 139 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 140 */       throw new InvalidNameException(paramString);
/*     */     }
/* 142 */     super.unbind(paramString);
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException
/*     */   {
/* 147 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 148 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 150 */     super.unbind(paramName);
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException
/*     */   {
/* 155 */     if (LdapURL.hasQueryComponents(paramString1))
/* 156 */       throw new InvalidNameException(paramString1);
/* 157 */     if (LdapURL.hasQueryComponents(paramString2)) {
/* 158 */       throw new InvalidNameException(paramString2);
/*     */     }
/* 160 */     super.rename(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2) throws NamingException
/*     */   {
/* 165 */     if (LdapURL.hasQueryComponents(paramName1.get(0)))
/* 166 */       throw new InvalidNameException(paramName1.toString());
/* 167 */     if (LdapURL.hasQueryComponents(paramName2.get(0))) {
/* 168 */       throw new InvalidNameException(paramName2.toString());
/*     */     }
/* 170 */     super.rename(paramName1, paramName2);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(String paramString) throws NamingException
/*     */   {
/* 175 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 176 */       throw new InvalidNameException(paramString);
/*     */     }
/* 178 */     return super.list(paramString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(Name paramName) throws NamingException
/*     */   {
/* 183 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 184 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 186 */     return super.list(paramName);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(String paramString) throws NamingException
/*     */   {
/* 191 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 192 */       throw new InvalidNameException(paramString);
/*     */     }
/* 194 */     return super.listBindings(paramString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(Name paramName) throws NamingException
/*     */   {
/* 199 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 200 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 202 */     return super.listBindings(paramName);
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException
/*     */   {
/* 207 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 208 */       throw new InvalidNameException(paramString);
/*     */     }
/* 210 */     super.destroySubcontext(paramString);
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException
/*     */   {
/* 215 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 216 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 218 */     super.destroySubcontext(paramName);
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException
/*     */   {
/* 223 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 224 */       throw new InvalidNameException(paramString);
/*     */     }
/* 226 */     return super.createSubcontext(paramString);
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException
/*     */   {
/* 231 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 232 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 234 */     return super.createSubcontext(paramName);
/*     */   }
/*     */ 
/*     */   public Object lookupLink(String paramString) throws NamingException
/*     */   {
/* 239 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 240 */       throw new InvalidNameException(paramString);
/*     */     }
/* 242 */     return super.lookupLink(paramString);
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException
/*     */   {
/* 247 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 248 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 250 */     return super.lookupLink(paramName);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException
/*     */   {
/* 255 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 256 */       throw new InvalidNameException(paramString);
/*     */     }
/* 258 */     return super.getNameParser(paramString);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException
/*     */   {
/* 263 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 264 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 266 */     return super.getNameParser(paramName);
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/* 272 */     if (LdapURL.hasQueryComponents(paramString1))
/* 273 */       throw new InvalidNameException(paramString1);
/* 274 */     if (LdapURL.hasQueryComponents(paramString2)) {
/* 275 */       throw new InvalidNameException(paramString2);
/*     */     }
/* 277 */     return super.composeName(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2) throws NamingException
/*     */   {
/* 282 */     if (LdapURL.hasQueryComponents(paramName1.get(0)))
/* 283 */       throw new InvalidNameException(paramName1.toString());
/* 284 */     if (LdapURL.hasQueryComponents(paramName2.get(0))) {
/* 285 */       throw new InvalidNameException(paramName2.toString());
/*     */     }
/* 287 */     return super.composeName(paramName1, paramName2);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString) throws NamingException
/*     */   {
/* 292 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 293 */       throw new InvalidNameException(paramString);
/*     */     }
/* 295 */     return super.getAttributes(paramString);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName) throws NamingException
/*     */   {
/* 300 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 301 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 303 */     return super.getAttributes(paramName);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(String paramString, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 309 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 310 */       throw new InvalidNameException(paramString);
/*     */     }
/* 312 */     return super.getAttributes(paramString, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public Attributes getAttributes(Name paramName, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 318 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 319 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 321 */     return super.getAttributes(paramName, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, int paramInt, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 327 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 328 */       throw new InvalidNameException(paramString);
/*     */     }
/* 330 */     super.modifyAttributes(paramString, paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 336 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 337 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 339 */     super.modifyAttributes(paramName, paramInt, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(String paramString, ModificationItem[] paramArrayOfModificationItem)
/*     */     throws NamingException
/*     */   {
/* 345 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 346 */       throw new InvalidNameException(paramString);
/*     */     }
/* 348 */     super.modifyAttributes(paramString, paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem)
/*     */     throws NamingException
/*     */   {
/* 354 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 355 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 357 */     super.modifyAttributes(paramName, paramArrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 363 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 364 */       throw new InvalidNameException(paramString);
/*     */     }
/* 366 */     super.bind(paramString, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 372 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 373 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 375 */     super.bind(paramName, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 381 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 382 */       throw new InvalidNameException(paramString);
/*     */     }
/* 384 */     super.rebind(paramString, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 390 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 391 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 393 */     super.rebind(paramName, paramObject, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 399 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 400 */       throw new InvalidNameException(paramString);
/*     */     }
/* 402 */     return super.createSubcontext(paramString, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext createSubcontext(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 408 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 409 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 411 */     return super.createSubcontext(paramName, paramAttributes);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(String paramString) throws NamingException
/*     */   {
/* 416 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 417 */       throw new InvalidNameException(paramString);
/*     */     }
/* 419 */     return super.getSchema(paramString);
/*     */   }
/*     */ 
/*     */   public DirContext getSchema(Name paramName) throws NamingException
/*     */   {
/* 424 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 425 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 427 */     return super.getSchema(paramName);
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(String paramString)
/*     */     throws NamingException
/*     */   {
/* 433 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 434 */       throw new InvalidNameException(paramString);
/*     */     }
/* 436 */     return super.getSchemaClassDefinition(paramString);
/*     */   }
/*     */ 
/*     */   public DirContext getSchemaClassDefinition(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 442 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 443 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 445 */     return super.getSchemaClassDefinition(paramName);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 454 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 455 */       return searchUsingURL(paramString);
/*     */     }
/* 457 */     return super.search(paramString, paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 465 */     if (paramName.size() == 1)
/* 466 */       return search(paramName.get(0), paramAttributes);
/* 467 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 468 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 470 */     return super.search(paramName, paramAttributes);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 480 */     if (LdapURL.hasQueryComponents(paramString)) {
/* 481 */       return searchUsingURL(paramString);
/*     */     }
/* 483 */     return super.search(paramString, paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString)
/*     */     throws NamingException
/*     */   {
/* 493 */     if (paramName.size() == 1)
/* 494 */       return search(paramName.get(0), paramAttributes, paramArrayOfString);
/* 495 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 496 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 498 */     return super.search(paramName, paramAttributes, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 508 */     if (LdapURL.hasQueryComponents(paramString1)) {
/* 509 */       return searchUsingURL(paramString1);
/*     */     }
/* 511 */     return super.search(paramString1, paramString2, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 521 */     if (paramName.size() == 1)
/* 522 */       return search(paramName.get(0), paramString, paramSearchControls);
/* 523 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 524 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 526 */     return super.search(paramName, paramString, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 537 */     if (LdapURL.hasQueryComponents(paramString1)) {
/* 538 */       return searchUsingURL(paramString1);
/*     */     }
/* 540 */     return super.search(paramString1, paramString2, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls)
/*     */     throws NamingException
/*     */   {
/* 551 */     if (paramName.size() == 1)
/* 552 */       return search(paramName.get(0), paramString, paramArrayOfObject, paramSearchControls);
/* 553 */     if (LdapURL.hasQueryComponents(paramName.get(0))) {
/* 554 */       throw new InvalidNameException(paramName.toString());
/*     */     }
/* 556 */     return super.search(paramName, paramString, paramArrayOfObject, paramSearchControls);
/*     */   }
/*     */ 
/*     */   private NamingEnumeration searchUsingURL(String paramString)
/*     */     throws NamingException
/*     */   {
/* 565 */     LdapURL localLdapURL = new LdapURL(paramString);
/*     */ 
/* 567 */     ResolveResult localResolveResult = getRootURLContext(paramString, this.myEnv);
/* 568 */     DirContext localDirContext = (DirContext)localResolveResult.getResolvedObj();
/*     */     try {
/* 570 */       return localDirContext.search(localResolveResult.getRemainingName(), setFilterUsingURL(localLdapURL), setSearchControlsUsingURL(localLdapURL));
/*     */     }
/*     */     finally
/*     */     {
/* 574 */       localDirContext.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String setFilterUsingURL(LdapURL paramLdapURL)
/*     */   {
/* 585 */     String str = paramLdapURL.getFilter();
/*     */ 
/* 587 */     if (str == null) {
/* 588 */       str = "(objectClass=*)";
/*     */     }
/* 590 */     return str;
/*     */   }
/*     */ 
/*     */   private static SearchControls setSearchControlsUsingURL(LdapURL paramLdapURL)
/*     */   {
/* 600 */     SearchControls localSearchControls = new SearchControls();
/* 601 */     String str1 = paramLdapURL.getScope();
/* 602 */     String str2 = paramLdapURL.getAttributes();
/*     */ 
/* 604 */     if (str1 == null) {
/* 605 */       localSearchControls.setSearchScope(0);
/*     */     }
/* 607 */     else if (str1.equals("sub"))
/* 608 */       localSearchControls.setSearchScope(2);
/* 609 */     else if (str1.equals("one"))
/* 610 */       localSearchControls.setSearchScope(1);
/* 611 */     else if (str1.equals("base")) {
/* 612 */       localSearchControls.setSearchScope(0);
/*     */     }
/*     */ 
/* 616 */     if (str2 == null) {
/* 617 */       localSearchControls.setReturningAttributes(null);
/*     */     } else {
/* 619 */       StringTokenizer localStringTokenizer = new StringTokenizer(str2, ",");
/* 620 */       int i = localStringTokenizer.countTokens();
/* 621 */       String[] arrayOfString = new String[i];
/* 622 */       for (int j = 0; j < i; j++) {
/* 623 */         arrayOfString[j] = localStringTokenizer.nextToken();
/*     */       }
/* 625 */       localSearchControls.setReturningAttributes(arrayOfString);
/*     */     }
/* 627 */     return localSearchControls;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.url.ldap.ldapURLContext
 * JD-Core Version:    0.6.2
 */
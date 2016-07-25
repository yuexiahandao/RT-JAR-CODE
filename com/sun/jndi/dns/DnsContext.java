/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import com.sun.jndi.toolkit.ctx.ComponentDirContext;
/*     */ import com.sun.jndi.toolkit.ctx.Continuation;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttribute;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InvalidAttributeIdentifierException;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ 
/*     */ public class DnsContext extends ComponentDirContext
/*     */ {
/*     */   DnsName domain;
/*     */   Hashtable environment;
/*     */   private boolean envShared;
/*     */   private boolean parentIsDns;
/*     */   private String[] servers;
/*     */   private Resolver resolver;
/*     */   private boolean authoritative;
/*     */   private boolean recursion;
/*     */   private int timeout;
/*     */   private int retries;
/*  63 */   static final NameParser nameParser = new DnsNameParser();
/*     */   private static final int DEFAULT_INIT_TIMEOUT = 1000;
/*     */   private static final int DEFAULT_RETRIES = 4;
/*     */   private static final String INIT_TIMEOUT = "com.sun.jndi.dns.timeout.initial";
/*     */   private static final String RETRIES = "com.sun.jndi.dns.timeout.retries";
/*     */   private CT lookupCT;
/*     */   private static final String LOOKUP_ATTR = "com.sun.jndi.dns.lookup.attr";
/*     */   private static final String RECURSION = "com.sun.jndi.dns.recursion";
/*     */   private static final int ANY = 255;
/*  88 */   private static final ZoneNode zoneTree = new ZoneNode(null);
/*     */   private static final boolean debug = false;
/*     */ 
/*     */   public DnsContext(String paramString, String[] paramArrayOfString, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/* 101 */     this.domain = new DnsName(paramString + ".");
/*     */ 
/* 104 */     this.servers = paramArrayOfString;
/* 105 */     this.environment = ((Hashtable)paramHashtable.clone());
/* 106 */     this.envShared = false;
/* 107 */     this.parentIsDns = false;
/* 108 */     this.resolver = null;
/*     */ 
/* 110 */     initFromEnvironment();
/*     */   }
/*     */ 
/*     */   DnsContext(DnsContext paramDnsContext, DnsName paramDnsName)
/*     */   {
/* 118 */     this(paramDnsContext);
/* 119 */     this.domain = paramDnsName;
/* 120 */     this.parentIsDns = true;
/*     */   }
/*     */ 
/*     */   private DnsContext(DnsContext paramDnsContext)
/*     */   {
/* 131 */     this.environment = paramDnsContext.environment;
/* 132 */     this.envShared = (paramDnsContext.envShared = 1);
/* 133 */     this.parentIsDns = paramDnsContext.parentIsDns;
/* 134 */     this.domain = paramDnsContext.domain;
/* 135 */     this.servers = paramDnsContext.servers;
/* 136 */     this.resolver = paramDnsContext.resolver;
/* 137 */     this.authoritative = paramDnsContext.authoritative;
/* 138 */     this.recursion = paramDnsContext.recursion;
/* 139 */     this.timeout = paramDnsContext.timeout;
/* 140 */     this.retries = paramDnsContext.retries;
/* 141 */     this.lookupCT = paramDnsContext.lookupCT;
/*     */   }
/*     */ 
/*     */   public void close() {
/* 145 */     if (this.resolver != null) {
/* 146 */       this.resolver.close();
/* 147 */       this.resolver = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected Hashtable p_getEnvironment()
/*     */   {
/* 158 */     return this.environment;
/*     */   }
/*     */ 
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 162 */     return (Hashtable)this.environment.clone();
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 168 */     if (paramString.equals("com.sun.jndi.dns.lookup.attr")) {
/* 169 */       this.lookupCT = getLookupCT((String)paramObject);
/* 170 */     } else if (paramString.equals("java.naming.authoritative")) {
/* 171 */       this.authoritative = "true".equalsIgnoreCase((String)paramObject);
/* 172 */     } else if (paramString.equals("com.sun.jndi.dns.recursion")) {
/* 173 */       this.recursion = "true".equalsIgnoreCase((String)paramObject);
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/* 174 */       if (paramString.equals("com.sun.jndi.dns.timeout.initial")) {
/* 175 */         i = Integer.parseInt((String)paramObject);
/* 176 */         if (this.timeout != i) {
/* 177 */           this.timeout = i;
/* 178 */           this.resolver = null;
/*     */         }
/* 180 */       } else if (paramString.equals("com.sun.jndi.dns.timeout.retries")) {
/* 181 */         i = Integer.parseInt((String)paramObject);
/* 182 */         if (this.retries != i) {
/* 183 */           this.retries = i;
/* 184 */           this.resolver = null;
/*     */         }
/*     */       }
/*     */     }
/* 188 */     if (!this.envShared)
/* 189 */       return this.environment.put(paramString, paramObject);
/* 190 */     if (this.environment.get(paramString) != paramObject)
/*     */     {
/* 192 */       this.environment = ((Hashtable)this.environment.clone());
/* 193 */       this.envShared = false;
/* 194 */       return this.environment.put(paramString, paramObject);
/*     */     }
/* 196 */     return paramObject;
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString)
/*     */     throws NamingException
/*     */   {
/* 203 */     if (paramString.equals("com.sun.jndi.dns.lookup.attr")) {
/* 204 */       this.lookupCT = getLookupCT(null);
/* 205 */     } else if (paramString.equals("java.naming.authoritative")) {
/* 206 */       this.authoritative = false;
/* 207 */     } else if (paramString.equals("com.sun.jndi.dns.recursion")) {
/* 208 */       this.recursion = true;
/* 209 */     } else if (paramString.equals("com.sun.jndi.dns.timeout.initial")) {
/* 210 */       if (this.timeout != 1000) {
/* 211 */         this.timeout = 1000;
/* 212 */         this.resolver = null;
/*     */       }
/* 214 */     } else if ((paramString.equals("com.sun.jndi.dns.timeout.retries")) && 
/* 215 */       (this.retries != 4)) {
/* 216 */       this.retries = 4;
/* 217 */       this.resolver = null;
/*     */     }
/*     */ 
/* 221 */     if (!this.envShared)
/* 222 */       return this.environment.remove(paramString);
/* 223 */     if (this.environment.get(paramString) != null)
/*     */     {
/* 225 */       this.environment = ((Hashtable)this.environment.clone());
/* 226 */       this.envShared = false;
/* 227 */       return this.environment.remove(paramString);
/*     */     }
/* 229 */     return null;
/*     */   }
/*     */ 
/*     */   void setProviderUrl(String paramString)
/*     */   {
/* 239 */     this.environment.put("java.naming.provider.url", paramString);
/*     */   }
/*     */ 
/*     */   private void initFromEnvironment()
/*     */     throws InvalidAttributeIdentifierException
/*     */   {
/* 248 */     this.lookupCT = getLookupCT((String)this.environment.get("com.sun.jndi.dns.lookup.attr"));
/* 249 */     this.authoritative = "true".equalsIgnoreCase((String)this.environment.get("java.naming.authoritative"));
/*     */ 
/* 251 */     String str = (String)this.environment.get("com.sun.jndi.dns.recursion");
/* 252 */     this.recursion = ((str == null) || ("true".equalsIgnoreCase(str)));
/*     */ 
/* 254 */     str = (String)this.environment.get("com.sun.jndi.dns.timeout.initial");
/* 255 */     this.timeout = (str == null ? 1000 : Integer.parseInt(str));
/*     */ 
/* 258 */     str = (String)this.environment.get("com.sun.jndi.dns.timeout.retries");
/* 259 */     this.retries = (str == null ? 4 : Integer.parseInt(str));
/*     */   }
/*     */ 
/*     */   private CT getLookupCT(String paramString)
/*     */     throws InvalidAttributeIdentifierException
/*     */   {
/* 266 */     return paramString == null ? new CT(1, 16) : fromAttrId(paramString);
/*     */   }
/*     */ 
/*     */   public Object c_lookup(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 277 */     paramContinuation.setSuccess();
/*     */     Object localObject1;
/* 278 */     if (paramName.isEmpty()) {
/* 279 */       localObject1 = new DnsContext(this);
/* 280 */       ((DnsContext)localObject1).resolver = new Resolver(this.servers, this.timeout, this.retries);
/*     */ 
/* 282 */       return localObject1;
/*     */     }Object localObject2;
/*     */     try {
/* 285 */       localObject1 = fullyQualify(paramName);
/* 286 */       localObject2 = getResolver().query((DnsName)localObject1, this.lookupCT.rrclass, this.lookupCT.rrtype, this.recursion, this.authoritative);
/*     */ 
/* 289 */       Attributes localAttributes = rrsToAttrs((ResourceRecords)localObject2, null);
/* 290 */       DnsContext localDnsContext = new DnsContext(this, (DnsName)localObject1);
/* 291 */       return DirectoryManager.getObjectInstance(localDnsContext, paramName, this, this.environment, localAttributes);
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 294 */       paramContinuation.setError(this, paramName);
/* 295 */       throw paramContinuation.fillInException(localNamingException);
/*     */     } catch (Exception localException) {
/* 297 */       paramContinuation.setError(this, paramName);
/* 298 */       localObject2 = new NamingException("Problem generating object using object factory");
/*     */ 
/* 300 */       ((NamingException)localObject2).setRootCause(localException);
/* 301 */     }throw paramContinuation.fillInException((NamingException)localObject2);
/*     */   }
/*     */ 
/*     */   public Object c_lookupLink(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 307 */     return c_lookup(paramName, paramContinuation);
/*     */   }
/*     */ 
/*     */   public NamingEnumeration c_list(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 312 */     paramContinuation.setSuccess();
/*     */     try {
/* 314 */       DnsName localDnsName = fullyQualify(paramName);
/* 315 */       NameNode localNameNode = getNameNode(localDnsName);
/* 316 */       DnsContext localDnsContext = new DnsContext(this, localDnsName);
/* 317 */       return new NameClassPairEnumeration(localDnsContext, localNameNode.getChildren());
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 320 */       paramContinuation.setError(this, paramName);
/* 321 */       throw paramContinuation.fillInException(localNamingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration c_listBindings(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 327 */     paramContinuation.setSuccess();
/*     */     try {
/* 329 */       DnsName localDnsName = fullyQualify(paramName);
/* 330 */       NameNode localNameNode = getNameNode(localDnsName);
/* 331 */       DnsContext localDnsContext = new DnsContext(this, localDnsName);
/* 332 */       return new BindingEnumeration(localDnsContext, localNameNode.getChildren());
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 335 */       paramContinuation.setError(this, paramName);
/* 336 */       throw paramContinuation.fillInException(localNamingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void c_bind(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*     */   {
/* 342 */     paramContinuation.setError(this, paramName);
/* 343 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public void c_rebind(Name paramName, Object paramObject, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 349 */     paramContinuation.setError(this, paramName);
/* 350 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public void c_unbind(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 356 */     paramContinuation.setError(this, paramName);
/* 357 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public void c_rename(Name paramName1, Name paramName2, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 363 */     paramContinuation.setError(this, paramName1);
/* 364 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public Context c_createSubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 370 */     paramContinuation.setError(this, paramName);
/* 371 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public void c_destroySubcontext(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 377 */     paramContinuation.setError(this, paramName);
/* 378 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public NameParser c_getNameParser(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 384 */     paramContinuation.setSuccess();
/* 385 */     return nameParser;
/*     */   }
/*     */ 
/*     */   public void c_bind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 396 */     paramContinuation.setError(this, paramName);
/* 397 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public void c_rebind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 406 */     paramContinuation.setError(this, paramName);
/* 407 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public DirContext c_createSubcontext(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 415 */     paramContinuation.setError(this, paramName);
/* 416 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public Attributes c_getAttributes(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 425 */     paramContinuation.setSuccess();
/*     */     try {
/* 427 */       DnsName localDnsName = fullyQualify(paramName);
/* 428 */       CT[] arrayOfCT = attrIdsToClassesAndTypes(paramArrayOfString);
/* 429 */       CT localCT = getClassAndTypeToQuery(arrayOfCT);
/* 430 */       ResourceRecords localResourceRecords = getResolver().query(localDnsName, localCT.rrclass, localCT.rrtype, this.recursion, this.authoritative);
/*     */ 
/* 433 */       return rrsToAttrs(localResourceRecords, arrayOfCT);
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 436 */       paramContinuation.setError(this, paramName);
/* 437 */       throw paramContinuation.fillInException(localNamingException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void c_modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 446 */     paramContinuation.setError(this, paramName);
/* 447 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public void c_modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 455 */     paramContinuation.setError(this, paramName);
/* 456 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public NamingEnumeration c_search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 465 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public NamingEnumeration c_search(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 473 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public NamingEnumeration c_search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 482 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public DirContext c_getSchema(Name paramName, Continuation paramContinuation) throws NamingException
/*     */   {
/* 487 */     paramContinuation.setError(this, paramName);
/* 488 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public DirContext c_getSchemaClassDefinition(Name paramName, Continuation paramContinuation)
/*     */     throws NamingException
/*     */   {
/* 494 */     paramContinuation.setError(this, paramName);
/* 495 */     throw paramContinuation.fillInException(new OperationNotSupportedException());
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace()
/*     */   {
/* 503 */     return this.domain.toString();
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 511 */     if ((!(paramName2 instanceof DnsName)) && (!(paramName2 instanceof CompositeName))) {
/* 512 */       paramName2 = new DnsName().addAll(paramName2);
/*     */     }
/* 514 */     if ((!(paramName1 instanceof DnsName)) && (!(paramName1 instanceof CompositeName))) {
/* 515 */       paramName1 = new DnsName().addAll(paramName1);
/*     */     }
/*     */ 
/* 521 */     if (((paramName2 instanceof DnsName)) && ((paramName1 instanceof DnsName))) {
/* 522 */       localObject = (DnsName)paramName2.clone();
/* 523 */       ((Name)localObject).addAll(paramName1);
/* 524 */       return new CompositeName().add(localObject.toString());
/*     */     }
/*     */ 
/* 528 */     Name localName1 = (paramName2 instanceof CompositeName) ? paramName2 : new CompositeName().add(paramName2.toString());
/*     */ 
/* 531 */     Name localName2 = (paramName1 instanceof CompositeName) ? paramName1 : new CompositeName().add(paramName1.toString());
/*     */ 
/* 534 */     int i = localName1.size() - 1;
/*     */ 
/* 537 */     if ((localName2.isEmpty()) || (localName2.get(0).equals("")) || (localName1.isEmpty()) || (localName1.get(i).equals("")))
/*     */     {
/* 539 */       return super.composeName(localName2, localName1);
/*     */     }
/*     */ 
/* 542 */     Object localObject = paramName2 == localName1 ? (CompositeName)localName1.clone() : localName1;
/*     */ 
/* 545 */     ((Name)localObject).addAll(localName2);
/*     */ 
/* 547 */     if (this.parentIsDns) {
/* 548 */       DnsName localDnsName = (paramName2 instanceof DnsName) ? (DnsName)paramName2.clone() : new DnsName(localName1.get(i));
/*     */ 
/* 551 */       localDnsName.addAll((paramName1 instanceof DnsName) ? paramName1 : new DnsName(localName2.get(0)));
/*     */ 
/* 554 */       ((Name)localObject).remove(i + 1);
/* 555 */       ((Name)localObject).remove(i);
/* 556 */       ((Name)localObject).add(i, localDnsName.toString());
/*     */     }
/* 558 */     return localObject;
/*     */   }
/*     */ 
/*     */   private synchronized Resolver getResolver()
/*     */     throws NamingException
/*     */   {
/* 569 */     if (this.resolver == null) {
/* 570 */       this.resolver = new Resolver(this.servers, this.timeout, this.retries);
/*     */     }
/* 572 */     return this.resolver;
/*     */   }
/*     */ 
/*     */   DnsName fullyQualify(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 581 */     if (paramName.isEmpty()) {
/* 582 */       return this.domain;
/*     */     }
/* 584 */     DnsName localDnsName = (paramName instanceof CompositeName) ? new DnsName(paramName.get(0)) : (DnsName)new DnsName().addAll(paramName);
/*     */ 
/* 588 */     if (localDnsName.hasRootLabel())
/*     */     {
/* 590 */       if (this.domain.size() == 1) {
/* 591 */         return localDnsName;
/*     */       }
/* 593 */       throw new InvalidNameException("DNS name " + localDnsName + " not relative to " + this.domain);
/*     */     }
/*     */ 
/* 597 */     return (DnsName)localDnsName.addAll(0, this.domain);
/*     */   }
/*     */ 
/*     */   private static Attributes rrsToAttrs(ResourceRecords paramResourceRecords, CT[] paramArrayOfCT)
/*     */   {
/* 608 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*     */ 
/* 610 */     for (int i = 0; i < paramResourceRecords.answer.size(); i++) {
/* 611 */       ResourceRecord localResourceRecord = (ResourceRecord)paramResourceRecords.answer.elementAt(i);
/* 612 */       int j = localResourceRecord.getType();
/* 613 */       int k = localResourceRecord.getRrclass();
/*     */ 
/* 615 */       if (classAndTypeMatch(k, j, paramArrayOfCT))
/*     */       {
/* 619 */         String str = toAttrId(k, j);
/* 620 */         Object localObject = localBasicAttributes.get(str);
/* 621 */         if (localObject == null) {
/* 622 */           localObject = new BasicAttribute(str);
/* 623 */           localBasicAttributes.put((Attribute)localObject);
/*     */         }
/* 625 */         ((Attribute)localObject).add(localResourceRecord.getRdata());
/*     */       }
/*     */     }
/* 627 */     return localBasicAttributes;
/*     */   }
/*     */ 
/*     */   private static boolean classAndTypeMatch(int paramInt1, int paramInt2, CT[] paramArrayOfCT)
/*     */   {
/* 638 */     if (paramArrayOfCT == null) {
/* 639 */       return true;
/*     */     }
/* 641 */     for (int i = 0; i < paramArrayOfCT.length; i++) {
/* 642 */       CT localCT = paramArrayOfCT[i];
/* 643 */       int j = (localCT.rrclass == 255) || (localCT.rrclass == paramInt1) ? 1 : 0;
/*     */ 
/* 645 */       int k = (localCT.rrtype == 255) || (localCT.rrtype == paramInt2) ? 1 : 0;
/*     */ 
/* 647 */       if ((j != 0) && (k != 0)) {
/* 648 */         return true;
/*     */       }
/*     */     }
/* 651 */     return false;
/*     */   }
/*     */ 
/*     */   private static String toAttrId(int paramInt1, int paramInt2)
/*     */   {
/* 669 */     String str = ResourceRecord.getTypeName(paramInt2);
/* 670 */     if (paramInt1 != 1) {
/* 671 */       str = ResourceRecord.getRrclassName(paramInt1) + " " + str;
/*     */     }
/* 673 */     return str;
/*     */   }
/*     */ 
/*     */   private static CT fromAttrId(String paramString)
/*     */     throws InvalidAttributeIdentifierException
/*     */   {
/* 687 */     if (paramString.equals("")) {
/* 688 */       throw new InvalidAttributeIdentifierException("Attribute ID cannot be empty");
/*     */     }
/*     */ 
/* 693 */     int k = paramString.indexOf(' ');
/*     */     int i;
/* 696 */     if (k < 0) {
/* 697 */       i = 1;
/*     */     } else {
/* 699 */       str = paramString.substring(0, k);
/* 700 */       i = ResourceRecord.getRrclass(str);
/* 701 */       if (i < 0) {
/* 702 */         throw new InvalidAttributeIdentifierException("Unknown resource record class '" + str + '\'');
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 708 */     String str = paramString.substring(k + 1);
/* 709 */     int j = ResourceRecord.getType(str);
/* 710 */     if (j < 0) {
/* 711 */       throw new InvalidAttributeIdentifierException("Unknown resource record type '" + str + '\'');
/*     */     }
/*     */ 
/* 715 */     return new CT(i, j);
/*     */   }
/*     */ 
/*     */   private static CT[] attrIdsToClassesAndTypes(String[] paramArrayOfString)
/*     */     throws InvalidAttributeIdentifierException
/*     */   {
/* 726 */     if (paramArrayOfString == null) {
/* 727 */       return null;
/*     */     }
/* 729 */     CT[] arrayOfCT = new CT[paramArrayOfString.length];
/*     */ 
/* 731 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 732 */       arrayOfCT[i] = fromAttrId(paramArrayOfString[i]);
/*     */     }
/* 734 */     return arrayOfCT;
/*     */   }
/*     */ 
/*     */   private static CT getClassAndTypeToQuery(CT[] paramArrayOfCT)
/*     */   {
/*     */     int i;
/*     */     int j;
/* 746 */     if (paramArrayOfCT == null)
/*     */     {
/* 748 */       i = 255;
/* 749 */       j = 255;
/* 750 */     } else if (paramArrayOfCT.length == 0)
/*     */     {
/* 752 */       i = 1;
/* 753 */       j = 255;
/*     */     } else {
/* 755 */       i = paramArrayOfCT[0].rrclass;
/* 756 */       j = paramArrayOfCT[0].rrtype;
/* 757 */       for (int k = 1; k < paramArrayOfCT.length; k++) {
/* 758 */         if (i != paramArrayOfCT[k].rrclass) {
/* 759 */           i = 255;
/*     */         }
/* 761 */         if (j != paramArrayOfCT[k].rrtype) {
/* 762 */           j = 255;
/*     */         }
/*     */       }
/*     */     }
/* 766 */     return new CT(i, j);
/*     */   }
/*     */ 
/*     */   private NameNode getNameNode(DnsName paramDnsName)
/*     */     throws NamingException
/*     */   {
/* 797 */     dprint("getNameNode(" + paramDnsName + ")");
/*     */     ZoneNode localZoneNode;
/* 802 */     synchronized (zoneTree) {
/* 803 */       localZoneNode = zoneTree.getDeepestPopulated(paramDnsName);
/*     */     }
/* 805 */     dprint("Deepest related zone in zone tree: " + (localZoneNode != null ? localZoneNode.getLabel() : "[none]"));
/*     */ 
/* 811 */     if (localZoneNode != null) {
/* 812 */       synchronized (localZoneNode) {
/* 813 */         ??? = localZoneNode.getContents();
/*     */       }
/*     */ 
/* 817 */       if (??? != null) {
/* 818 */         localNameNode = ((NameNode)???).get(paramDnsName, localZoneNode.depth() + 1);
/*     */ 
/* 820 */         if ((localNameNode != null) && (!localNameNode.isZoneCut())) {
/* 821 */           dprint("Found node " + paramDnsName + " in zone tree");
/* 822 */           localDnsName = (DnsName)paramDnsName.getPrefix(localZoneNode.depth() + 1);
/*     */ 
/* 824 */           ??? = isZoneCurrent(localZoneNode, localDnsName);
/* 825 */           int i = 0;
/*     */ 
/* 827 */           synchronized (localZoneNode) {
/* 828 */             if (??? != localZoneNode.getContents())
/*     */             {
/* 831 */               i = 1;
/* 832 */             } else if (??? == 0)
/* 833 */               localZoneNode.depopulate();
/*     */             else {
/* 835 */               return localNameNode;
/*     */             }
/*     */           }
/* 838 */           dprint("Zone not current; discarding node");
/* 839 */           if (i != 0) {
/* 840 */             return getNameNode(paramDnsName);
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 847 */     dprint("Adding node " + paramDnsName + " to zone tree");
/*     */ 
/* 850 */     DnsName localDnsName = getResolver().findZoneName(paramDnsName, 1, this.recursion);
/*     */ 
/* 852 */     dprint("Node's zone is " + localDnsName);
/* 853 */     synchronized (zoneTree) {
/* 854 */       localZoneNode = (ZoneNode)zoneTree.add(localDnsName, 1);
/*     */     }
/*     */ 
/* 861 */     synchronized (localZoneNode) {
/* 862 */       ??? = localZoneNode.isPopulated() ? localZoneNode.getContents() : populateZone(localZoneNode, localDnsName);
/*     */     }
/*     */ 
/* 867 */     NameNode localNameNode = ((NameNode)???).get(paramDnsName, localDnsName.size());
/* 868 */     if (localNameNode == null) {
/* 869 */       throw new ConfigurationException("DNS error: node not found in its own zone");
/*     */     }
/*     */ 
/* 872 */     dprint("Found node in newly-populated zone");
/* 873 */     return localNameNode;
/*     */   }
/*     */ 
/*     */   private NameNode populateZone(ZoneNode paramZoneNode, DnsName paramDnsName)
/*     */     throws NamingException
/*     */   {
/* 882 */     dprint("Populating zone " + paramDnsName);
/*     */ 
/* 884 */     ResourceRecords localResourceRecords = getResolver().queryZone(paramDnsName, 1, this.recursion);
/*     */ 
/* 887 */     dprint("zone xfer complete: " + localResourceRecords.answer.size() + " records");
/* 888 */     return paramZoneNode.populate(paramDnsName, localResourceRecords);
/*     */   }
/*     */ 
/*     */   private boolean isZoneCurrent(ZoneNode paramZoneNode, DnsName paramDnsName)
/*     */     throws NamingException
/*     */   {
/* 907 */     if (!paramZoneNode.isPopulated()) {
/* 908 */       return false;
/*     */     }
/* 910 */     ResourceRecord localResourceRecord = getResolver().findSoa(paramDnsName, 1, this.recursion);
/*     */ 
/* 913 */     synchronized (paramZoneNode) {
/* 914 */       if (localResourceRecord == null) {
/* 915 */         paramZoneNode.depopulate();
/*     */       }
/* 917 */       return (paramZoneNode.isPopulated()) && (paramZoneNode.compareSerialNumberTo(localResourceRecord) >= 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final void dprint(String paramString)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.DnsContext
 * JD-Core Version:    0.6.2
 */
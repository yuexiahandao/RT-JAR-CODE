/*      */ package com.sun.jndi.ldap;
/*      */ 
/*      */ import com.sun.jndi.ldap.ext.StartTlsResponseImpl;
/*      */ import com.sun.jndi.toolkit.ctx.ComponentDirContext;
/*      */ import com.sun.jndi.toolkit.ctx.Continuation;
/*      */ import com.sun.jndi.toolkit.dir.HierMemDirCtx;
/*      */ import com.sun.jndi.toolkit.dir.SearchFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.List;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.Vector;
/*      */ import javax.naming.AuthenticationException;
/*      */ import javax.naming.AuthenticationNotSupportedException;
/*      */ import javax.naming.CommunicationException;
/*      */ import javax.naming.CompositeName;
/*      */ import javax.naming.ConfigurationException;
/*      */ import javax.naming.Context;
/*      */ import javax.naming.ContextNotEmptyException;
/*      */ import javax.naming.InvalidNameException;
/*      */ import javax.naming.LimitExceededException;
/*      */ import javax.naming.Name;
/*      */ import javax.naming.NameAlreadyBoundException;
/*      */ import javax.naming.NameNotFoundException;
/*      */ import javax.naming.NameParser;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.NoPermissionException;
/*      */ import javax.naming.OperationNotSupportedException;
/*      */ import javax.naming.PartialResultException;
/*      */ import javax.naming.ServiceUnavailableException;
/*      */ import javax.naming.SizeLimitExceededException;
/*      */ import javax.naming.TimeLimitExceededException;
/*      */ import javax.naming.directory.Attribute;
/*      */ import javax.naming.directory.AttributeInUseException;
/*      */ import javax.naming.directory.Attributes;
/*      */ import javax.naming.directory.BasicAttribute;
/*      */ import javax.naming.directory.BasicAttributes;
/*      */ import javax.naming.directory.DirContext;
/*      */ import javax.naming.directory.InvalidAttributeIdentifierException;
/*      */ import javax.naming.directory.InvalidAttributeValueException;
/*      */ import javax.naming.directory.InvalidSearchFilterException;
/*      */ import javax.naming.directory.ModificationItem;
/*      */ import javax.naming.directory.NoSuchAttributeException;
/*      */ import javax.naming.directory.SchemaViolationException;
/*      */ import javax.naming.directory.SearchControls;
/*      */ import javax.naming.directory.SearchResult;
/*      */ import javax.naming.event.EventDirContext;
/*      */ import javax.naming.event.NamingListener;
/*      */ import javax.naming.ldap.Control;
/*      */ import javax.naming.ldap.ControlFactory;
/*      */ import javax.naming.ldap.ExtendedRequest;
/*      */ import javax.naming.ldap.ExtendedResponse;
/*      */ import javax.naming.ldap.LdapContext;
/*      */ import javax.naming.ldap.LdapName;
/*      */ import javax.naming.ldap.Rdn;
/*      */ import javax.naming.ldap.UnsolicitedNotificationListener;
/*      */ import javax.naming.spi.DirectoryManager;
/*      */ 
/*      */ public final class LdapCtx extends ComponentDirContext
/*      */   implements EventDirContext, LdapContext
/*      */ {
/*      */   private static final boolean debug = false;
/*      */   private static final boolean HARD_CLOSE = true;
/*      */   private static final boolean SOFT_CLOSE = false;
/*      */   public static final int DEFAULT_PORT = 389;
/*      */   public static final int DEFAULT_SSL_PORT = 636;
/*      */   public static final String DEFAULT_HOST = "localhost";
/*      */   private static final boolean DEFAULT_DELETE_RDN = true;
/*      */   private static final boolean DEFAULT_TYPES_ONLY = false;
/*      */   private static final int DEFAULT_DEREF_ALIASES = 3;
/*      */   private static final int DEFAULT_LDAP_VERSION = 32;
/*      */   private static final int DEFAULT_BATCH_SIZE = 1;
/*      */   private static final int DEFAULT_REFERRAL_MODE = 3;
/*      */   private static final char DEFAULT_REF_SEPARATOR = '#';
/*      */   static final String DEFAULT_SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
/*      */   private static final int DEFAULT_REFERRAL_LIMIT = 10;
/*      */   private static final String STARTTLS_REQ_OID = "1.3.6.1.4.1.1466.20037";
/*  137 */   private static final String[] SCHEMA_ATTRIBUTES = { "objectClasses", "attributeTypes", "matchingRules", "ldapSyntaxes" };
/*      */   private static final String VERSION = "java.naming.ldap.version";
/*      */   private static final String BINARY_ATTRIBUTES = "java.naming.ldap.attributes.binary";
/*      */   private static final String DELETE_RDN = "java.naming.ldap.deleteRDN";
/*      */   private static final String DEREF_ALIASES = "java.naming.ldap.derefAliases";
/*      */   private static final String TYPES_ONLY = "java.naming.ldap.typesOnly";
/*      */   private static final String REF_SEPARATOR = "java.naming.ldap.ref.separator";
/*      */   private static final String SOCKET_FACTORY = "java.naming.ldap.factory.socket";
/*      */   static final String BIND_CONTROLS = "java.naming.ldap.control.connect";
/*      */   private static final String REFERRAL_LIMIT = "java.naming.ldap.referral.limit";
/*      */   private static final String TRACE_BER = "com.sun.jndi.ldap.trace.ber";
/*      */   private static final String NETSCAPE_SCHEMA_BUG = "com.sun.jndi.ldap.netscape.schemaBugs";
/*      */   private static final String OLD_NETSCAPE_SCHEMA_BUG = "com.sun.naming.netscape.schemaBugs";
/*      */   private static final String CONNECT_TIMEOUT = "com.sun.jndi.ldap.connect.timeout";
/*      */   private static final String READ_TIMEOUT = "com.sun.jndi.ldap.read.timeout";
/*      */   private static final String ENABLE_POOL = "com.sun.jndi.ldap.connect.pool";
/*      */   private static final String DOMAIN_NAME = "com.sun.jndi.ldap.domainname";
/*      */   private static final String WAIT_FOR_REPLY = "com.sun.jndi.ldap.search.waitForReply";
/*      */   private static final String REPLY_QUEUE_SIZE = "com.sun.jndi.ldap.search.replyQueueSize";
/*  203 */   private static final NameParser parser = new LdapNameParser();
/*      */ 
/*  206 */   private static final ControlFactory myResponseControlFactory = new DefaultResponseControlFactory();
/*      */ 
/*  208 */   private static final Control manageReferralControl = new ManageReferralControl(false);
/*      */ 
/*  211 */   private static final HierMemDirCtx EMPTY_SCHEMA = new HierMemDirCtx();
/*      */   int port_number;
/*  223 */   String hostname = null;
/*      */ 
/*  225 */   LdapClient clnt = null;
/*  226 */   Hashtable envprops = null;
/*  227 */   int handleReferrals = 3;
/*  228 */   boolean hasLdapsScheme = false;
/*      */   String currentDN;
/*      */   Name currentParsedDN;
/*  235 */   Vector respCtls = null;
/*  236 */   Control[] reqCtls = null;
/*      */ 
/*  243 */   private OutputStream trace = null;
/*  244 */   private boolean netscapeSchemaBug = false;
/*  245 */   private Control[] bindCtls = null;
/*  246 */   private int referralHopLimit = 10;
/*  247 */   private Hashtable schemaTrees = null;
/*  248 */   private int batchSize = 1;
/*  249 */   private boolean deleteRDN = true;
/*  250 */   private boolean typesOnly = false;
/*  251 */   private int derefAliases = 3;
/*  252 */   private char addrEncodingSeparator = '#';
/*      */ 
/*  254 */   private Hashtable binaryAttrs = null;
/*  255 */   private int connectTimeout = -1;
/*  256 */   private int readTimeout = -1;
/*  257 */   private boolean waitForReply = true;
/*  258 */   private int replyQueueSize = -1;
/*  259 */   private boolean useSsl = false;
/*  260 */   private boolean useDefaultPortNumber = false;
/*      */ 
/*  265 */   private boolean parentIsLdapCtx = false;
/*      */ 
/*  267 */   private int hopCount = 1;
/*  268 */   private String url = null;
/*      */   private EventSupport eventSupport;
/*  270 */   private boolean unsolicited = false;
/*  271 */   private boolean sharable = true;
/*      */ 
/* 2810 */   private int enumCount = 0;
/* 2811 */   private boolean closeRequested = false;
/*      */ 
/*      */   public LdapCtx(String paramString1, String paramString2, int paramInt, Hashtable paramHashtable, boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/*  278 */     this.useSsl = (this.hasLdapsScheme = paramBoolean);
/*      */ 
/*  280 */     if (paramHashtable != null) {
/*  281 */       this.envprops = ((Hashtable)paramHashtable.clone());
/*      */ 
/*  284 */       if ("ssl".equals(this.envprops.get("java.naming.security.protocol"))) {
/*  285 */         this.useSsl = true;
/*      */       }
/*      */ 
/*  290 */       this.trace = ((OutputStream)this.envprops.get("com.sun.jndi.ldap.trace.ber"));
/*      */ 
/*  292 */       if ((paramHashtable.get("com.sun.jndi.ldap.netscape.schemaBugs") != null) || (paramHashtable.get("com.sun.naming.netscape.schemaBugs") != null))
/*      */       {
/*  294 */         this.netscapeSchemaBug = true;
/*      */       }
/*      */     }
/*      */ 
/*  298 */     this.currentDN = (paramString1 != null ? paramString1 : "");
/*  299 */     this.currentParsedDN = parser.parse(this.currentDN);
/*      */ 
/*  301 */     this.hostname = ((paramString2 != null) && (paramString2.length() > 0) ? paramString2 : "localhost");
/*  302 */     if (this.hostname.charAt(0) == '[') {
/*  303 */       this.hostname = this.hostname.substring(1, this.hostname.length() - 1);
/*      */     }
/*      */ 
/*  306 */     if (paramInt > 0) {
/*  307 */       this.port_number = paramInt;
/*      */     } else {
/*  309 */       this.port_number = (this.useSsl ? 636 : 389);
/*  310 */       this.useDefaultPortNumber = true;
/*      */     }
/*      */ 
/*  313 */     this.schemaTrees = new Hashtable(11, 0.75F);
/*  314 */     initEnv();
/*      */     try {
/*  316 */       connect(false);
/*      */     } catch (NamingException localNamingException) {
/*      */       try {
/*  319 */         close();
/*      */       }
/*      */       catch (Exception localException) {
/*      */       }
/*  323 */       throw localNamingException;
/*      */     }
/*      */   }
/*      */ 
/*      */   LdapCtx(LdapCtx paramLdapCtx, String paramString) throws NamingException {
/*  328 */     this.useSsl = paramLdapCtx.useSsl;
/*  329 */     this.hasLdapsScheme = paramLdapCtx.hasLdapsScheme;
/*  330 */     this.useDefaultPortNumber = paramLdapCtx.useDefaultPortNumber;
/*      */ 
/*  332 */     this.hostname = paramLdapCtx.hostname;
/*  333 */     this.port_number = paramLdapCtx.port_number;
/*  334 */     this.currentDN = paramString;
/*  335 */     if (paramLdapCtx.currentDN == this.currentDN)
/*  336 */       this.currentParsedDN = paramLdapCtx.currentParsedDN;
/*      */     else {
/*  338 */       this.currentParsedDN = parser.parse(this.currentDN);
/*      */     }
/*      */ 
/*  341 */     this.envprops = paramLdapCtx.envprops;
/*  342 */     this.schemaTrees = paramLdapCtx.schemaTrees;
/*      */ 
/*  344 */     this.clnt = paramLdapCtx.clnt;
/*  345 */     this.clnt.incRefCount();
/*      */ 
/*  347 */     this.parentIsLdapCtx = ((paramString == null) || (paramString.equals(paramLdapCtx.currentDN)) ? paramLdapCtx.parentIsLdapCtx : true);
/*      */ 
/*  352 */     this.trace = paramLdapCtx.trace;
/*  353 */     this.netscapeSchemaBug = paramLdapCtx.netscapeSchemaBug;
/*      */ 
/*  355 */     initEnv();
/*      */   }
/*      */ 
/*      */   public LdapContext newInstance(Control[] paramArrayOfControl) throws NamingException
/*      */   {
/*  360 */     LdapCtx localLdapCtx = new LdapCtx(this, this.currentDN);
/*      */ 
/*  366 */     localLdapCtx.setRequestControls(paramArrayOfControl);
/*  367 */     return localLdapCtx;
/*      */   }
/*      */ 
/*      */   protected void c_bind(Name paramName, Object paramObject, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/*  377 */     c_bind(paramName, paramObject, null, paramContinuation);
/*      */   }
/*      */ 
/*      */   protected void c_bind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/*  396 */     paramContinuation.setError(this, paramName);
/*      */ 
/*  398 */     Attributes localAttributes = paramAttributes;
/*      */     try {
/*  400 */       ensureOpen();
/*      */ 
/*  402 */       if (paramObject == null) {
/*  403 */         if (paramAttributes == null) {
/*  404 */           throw new IllegalArgumentException("cannot bind null object with no attributes");
/*      */         }
/*      */       }
/*      */       else {
/*  408 */         paramAttributes = Obj.determineBindAttrs(this.addrEncodingSeparator, paramObject, paramAttributes, false, paramName, this, this.envprops);
/*      */       }
/*      */ 
/*  412 */       String str = fullyQualifiedName(paramName);
/*  413 */       paramAttributes = addRdnAttributes(str, paramAttributes, localAttributes != paramAttributes);
/*  414 */       localObject2 = new LdapEntry(str, paramAttributes);
/*      */ 
/*  416 */       LdapResult localLdapResult = this.clnt.add((LdapEntry)localObject2, this.reqCtls);
/*  417 */       this.respCtls = localLdapResult.resControls;
/*      */ 
/*  419 */       if (localLdapResult.status != 0)
/*  420 */         processReturnCode(localLdapResult, paramName);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*  424 */       if (this.handleReferrals == 2) {
/*  425 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/*  430 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/*  436 */           ((LdapReferralContext)localObject2).bind(paramName, paramObject, localAttributes);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2)
/*      */         {
/*  440 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/*  445 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  450 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/*  451 */       ((NamingException)localObject2).setRootCause(localIOException);
/*  452 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/*  455 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void c_rebind(Name paramName, Object paramObject, Continuation paramContinuation) throws NamingException
/*      */   {
/*  461 */     c_rebind(paramName, paramObject, null, paramContinuation);
/*      */   }
/*      */ 
/*      */   protected void c_rebind(Name paramName, Object paramObject, Attributes paramAttributes, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/*  483 */     paramContinuation.setError(this, paramName);
/*      */ 
/*  485 */     Attributes localAttributes1 = paramAttributes;
/*      */     try
/*      */     {
/*  488 */       Attributes localAttributes2 = null;
/*      */       try
/*      */       {
/*  492 */         localAttributes2 = c_getAttributes(paramName, null, paramContinuation);
/*      */       }
/*      */       catch (NameNotFoundException localNameNotFoundException) {
/*      */       }
/*  496 */       if (localAttributes2 == null) {
/*  497 */         c_bind(paramName, paramObject, paramAttributes, paramContinuation);
/*  498 */         return;
/*      */       }
/*      */ 
/*  504 */       if ((paramAttributes == null) && ((paramObject instanceof DirContext))) {
/*  505 */         paramAttributes = ((DirContext)paramObject).getAttributes("");
/*      */       }
/*  507 */       localObject2 = (Attributes)localAttributes2.clone();
/*      */ 
/*  509 */       if (paramAttributes == null)
/*      */       {
/*  513 */         localObject3 = localAttributes2.get(Obj.JAVA_ATTRIBUTES[0]);
/*      */ 
/*  516 */         if (localObject3 != null)
/*      */         {
/*  518 */           localObject3 = (Attribute)((Attribute)localObject3).clone();
/*  519 */           for (i = 0; i < Obj.JAVA_OBJECT_CLASSES.length; i++) {
/*  520 */             ((Attribute)localObject3).remove(Obj.JAVA_OBJECT_CLASSES_LOWER[i]);
/*  521 */             ((Attribute)localObject3).remove(Obj.JAVA_OBJECT_CLASSES[i]);
/*      */           }
/*      */ 
/*  524 */           localAttributes2.put((Attribute)localObject3);
/*      */         }
/*      */ 
/*  528 */         for (int i = 1; i < Obj.JAVA_ATTRIBUTES.length; i++) {
/*  529 */           localAttributes2.remove(Obj.JAVA_ATTRIBUTES[i]);
/*      */         }
/*      */ 
/*  532 */         paramAttributes = localAttributes2;
/*      */       }
/*  534 */       if (paramObject != null) {
/*  535 */         paramAttributes = Obj.determineBindAttrs(this.addrEncodingSeparator, paramObject, paramAttributes, localAttributes1 != paramAttributes, paramName, this, this.envprops);
/*      */       }
/*      */ 
/*  540 */       Object localObject3 = fullyQualifiedName(paramName);
/*      */ 
/*  542 */       LdapResult localLdapResult1 = this.clnt.delete((String)localObject3, this.reqCtls);
/*  543 */       this.respCtls = localLdapResult1.resControls;
/*      */ 
/*  545 */       if (localLdapResult1.status != 0) {
/*  546 */         processReturnCode(localLdapResult1, paramName);
/*  547 */         return;
/*      */       }
/*      */ 
/*  550 */       Object localObject4 = null;
/*      */       try {
/*  552 */         paramAttributes = addRdnAttributes((String)localObject3, paramAttributes, localAttributes1 != paramAttributes);
/*      */ 
/*  555 */         LdapEntry localLdapEntry = new LdapEntry((String)localObject3, paramAttributes);
/*  556 */         localLdapResult1 = this.clnt.add(localLdapEntry, this.reqCtls);
/*  557 */         if (localLdapResult1.resControls != null)
/*  558 */           this.respCtls = appendVector(this.respCtls, localLdapResult1.resControls);
/*      */       }
/*      */       catch (NamingException localNamingException2) {
/*  561 */         localObject4 = localNamingException2;
/*      */       } catch (IOException localIOException2) {
/*  563 */         localObject4 = localIOException2;
/*      */       }
/*      */ 
/*  566 */       if (((localObject4 != null) && (!(localObject4 instanceof LdapReferralException))) || (localLdapResult1.status != 0))
/*      */       {
/*  569 */         LdapResult localLdapResult2 = this.clnt.add(new LdapEntry((String)localObject3, (Attributes)localObject2), this.reqCtls);
/*      */ 
/*  571 */         if (localLdapResult2.resControls != null) {
/*  572 */           this.respCtls = appendVector(this.respCtls, localLdapResult2.resControls);
/*      */         }
/*      */ 
/*  575 */         if (localObject4 == null) {
/*  576 */           processReturnCode(localLdapResult1, paramName);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  581 */       if ((localObject4 instanceof NamingException))
/*  582 */         throw ((NamingException)localObject4);
/*  583 */       if ((localObject4 instanceof IOException))
/*  584 */         throw ((IOException)localObject4);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*  588 */       if (this.handleReferrals == 2) {
/*  589 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/*  594 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/*  600 */           ((LdapReferralContext)localObject2).rebind(paramName, paramObject, localAttributes1);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2)
/*      */         {
/*  604 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/*  609 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException1)
/*      */     {
/*  614 */       Object localObject2 = new CommunicationException(localIOException1.getMessage());
/*  615 */       ((NamingException)localObject2).setRootCause(localIOException1);
/*  616 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException1) {
/*  619 */       throw paramContinuation.fillInException(localNamingException1);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void c_unbind(Name paramName, Continuation paramContinuation) throws NamingException
/*      */   {
/*  625 */     paramContinuation.setError(this, paramName);
/*      */     try
/*      */     {
/*  628 */       ensureOpen();
/*      */ 
/*  630 */       String str = fullyQualifiedName(paramName);
/*  631 */       localObject2 = this.clnt.delete(str, this.reqCtls);
/*  632 */       this.respCtls = ((LdapResult)localObject2).resControls;
/*      */ 
/*  634 */       adjustDeleteStatus(str, (LdapResult)localObject2);
/*      */ 
/*  636 */       if (((LdapResult)localObject2).status != 0)
/*  637 */         processReturnCode((LdapResult)localObject2, paramName);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*  641 */       if (this.handleReferrals == 2) {
/*  642 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/*  647 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/*  653 */           ((LdapReferralContext)localObject2).unbind(paramName);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2)
/*      */         {
/*  657 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/*  662 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  667 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/*  668 */       ((NamingException)localObject2).setRootCause(localIOException);
/*  669 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/*  672 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void c_rename(Name paramName1, Name paramName2, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/*  681 */     String str1 = null;
/*  682 */     String str2 = null;
/*      */ 
/*  686 */     paramContinuation.setError(this, paramName1);
/*      */     try
/*      */     {
/*  689 */       ensureOpen();
/*      */       Name localName3;
/*  692 */       if (paramName1.isEmpty()) {
/*  693 */         localName3 = parser.parse("");
/*      */       } else {
/*  695 */         Name localName1 = parser.parse(paramName1.get(0));
/*  696 */         localName3 = localName1.getPrefix(localName1.size() - 1);
/*      */       }
/*      */       Name localName2;
/*  699 */       if ((paramName2 instanceof CompositeName))
/*  700 */         localName2 = parser.parse(paramName2.get(0));
/*      */       else {
/*  702 */         localName2 = paramName2;
/*      */       }
/*  704 */       Name localName4 = localName2.getPrefix(localName2.size() - 1);
/*      */ 
/*  706 */       if (!localName3.equals(localName4)) {
/*  707 */         if (!this.clnt.isLdapv3) {
/*  708 */           throw new InvalidNameException("LDAPv2 doesn't support changing the parent as a result of a rename");
/*      */         }
/*      */ 
/*  712 */         str2 = fullyQualifiedName(localName4.toString());
/*      */       }
/*      */ 
/*  716 */       str1 = localName2.get(localName2.size() - 1);
/*      */ 
/*  718 */       LdapResult localLdapResult = this.clnt.moddn(fullyQualifiedName(paramName1), str1, this.deleteRDN, str2, this.reqCtls);
/*      */ 
/*  723 */       this.respCtls = localLdapResult.resControls;
/*      */ 
/*  725 */       if (localLdapResult.status != 0) {
/*  726 */         processReturnCode(localLdapResult, paramName1);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*  732 */       localLdapReferralException1.setNewRdn(str1);
/*      */ 
/*  737 */       if (str2 != null) {
/*  738 */         localObject2 = new PartialResultException("Cannot continue referral processing when newSuperior is nonempty: " + str2);
/*      */ 
/*  741 */         ((PartialResultException)localObject2).setRootCause(paramContinuation.fillInException(localLdapReferralException1));
/*  742 */         throw paramContinuation.fillInException((NamingException)localObject2);
/*      */       }
/*      */ 
/*  745 */       if (this.handleReferrals == 2) {
/*  746 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/*  751 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/*  757 */           ((LdapReferralContext)localObject2).rename(paramName1, paramName2);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2)
/*      */         {
/*  761 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/*  766 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  771 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/*  772 */       ((NamingException)localObject2).setRootCause(localIOException);
/*  773 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/*  776 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Context c_createSubcontext(Name paramName, Continuation paramContinuation) throws NamingException
/*      */   {
/*  782 */     return c_createSubcontext(paramName, null, paramContinuation);
/*      */   }
/*      */ 
/*      */   protected DirContext c_createSubcontext(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/*  788 */     paramContinuation.setError(this, paramName);
/*      */ 
/*  790 */     Attributes localAttributes = paramAttributes;
/*      */     try {
/*  792 */       ensureOpen();
/*  793 */       if (paramAttributes == null)
/*      */       {
/*  795 */         localObject1 = new BasicAttribute(Obj.JAVA_ATTRIBUTES[0], Obj.JAVA_OBJECT_CLASSES[0]);
/*      */ 
/*  798 */         ((Attribute)localObject1).add("top");
/*  799 */         paramAttributes = new BasicAttributes(true);
/*  800 */         paramAttributes.put((Attribute)localObject1);
/*      */       }
/*  802 */       Object localObject1 = fullyQualifiedName(paramName);
/*  803 */       paramAttributes = addRdnAttributes((String)localObject1, paramAttributes, localAttributes != paramAttributes);
/*      */ 
/*  805 */       localObject3 = new LdapEntry((String)localObject1, paramAttributes);
/*      */ 
/*  807 */       localObject4 = this.clnt.add((LdapEntry)localObject3, this.reqCtls);
/*  808 */       this.respCtls = ((LdapResult)localObject4).resControls;
/*      */ 
/*  810 */       if (((LdapResult)localObject4).status != 0) {
/*  811 */         processReturnCode((LdapResult)localObject4, paramName);
/*  812 */         return null;
/*      */       }
/*      */ 
/*  816 */       return new LdapCtx(this, (String)localObject1);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*      */       Object localObject4;
/*  819 */       if (this.handleReferrals == 2) {
/*  820 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/*  825 */         localObject3 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/*  831 */           return ((LdapReferralContext)localObject3).createSubcontext(paramName, localAttributes);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/*  834 */           Object localObject2 = localLdapReferralException2;
/*      */ 
/*  839 */           ((LdapReferralContext)localObject3).close(); } finally { ((LdapReferralContext)localObject3).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  844 */       Object localObject3 = new CommunicationException(localIOException.getMessage());
/*  845 */       ((NamingException)localObject3).setRootCause(localIOException);
/*  846 */       throw paramContinuation.fillInException((NamingException)localObject3);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/*  849 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void c_destroySubcontext(Name paramName, Continuation paramContinuation) throws NamingException
/*      */   {
/*  855 */     paramContinuation.setError(this, paramName);
/*      */     try
/*      */     {
/*  858 */       ensureOpen();
/*      */ 
/*  860 */       String str = fullyQualifiedName(paramName);
/*  861 */       localObject2 = this.clnt.delete(str, this.reqCtls);
/*  862 */       this.respCtls = ((LdapResult)localObject2).resControls;
/*      */ 
/*  864 */       adjustDeleteStatus(str, (LdapResult)localObject2);
/*      */ 
/*  866 */       if (((LdapResult)localObject2).status != 0)
/*  867 */         processReturnCode((LdapResult)localObject2, paramName);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*  871 */       if (this.handleReferrals == 2) {
/*  872 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/*  877 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/*  883 */           ((LdapReferralContext)localObject2).destroySubcontext(paramName);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/*  886 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/*  890 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/*  894 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/*  895 */       ((NamingException)localObject2).setRootCause(localIOException);
/*  896 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     } catch (NamingException localNamingException) {
/*  898 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Attributes addRdnAttributes(String paramString, Attributes paramAttributes, boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/*  916 */     if (paramString.equals("")) {
/*  917 */       return paramAttributes;
/*      */     }
/*      */ 
/*  922 */     List localList = new LdapName(paramString).getRdns();
/*      */ 
/*  926 */     Rdn localRdn = (Rdn)localList.get(localList.size() - 1);
/*  927 */     Attributes localAttributes = localRdn.toAttributes();
/*      */ 
/*  930 */     NamingEnumeration localNamingEnumeration = localAttributes.getAll();
/*      */ 
/*  932 */     while (localNamingEnumeration.hasMore()) {
/*  933 */       Attribute localAttribute = (Attribute)localNamingEnumeration.next();
/*      */ 
/*  936 */       if ((paramAttributes.get(localAttribute.getID()) == null) && (
/*  947 */         (paramAttributes.isCaseIgnored()) || (!containsIgnoreCase(paramAttributes.getIDs(), localAttribute.getID()))))
/*      */       {
/*  952 */         if (!paramBoolean) {
/*  953 */           paramAttributes = (Attributes)paramAttributes.clone();
/*  954 */           paramBoolean = true;
/*      */         }
/*  956 */         paramAttributes.put(localAttribute);
/*      */       }
/*      */     }
/*      */ 
/*  960 */     return paramAttributes;
/*      */   }
/*      */ 
/*      */   private static boolean containsIgnoreCase(NamingEnumeration paramNamingEnumeration, String paramString)
/*      */     throws NamingException
/*      */   {
/*  968 */     while (paramNamingEnumeration.hasMore()) {
/*  969 */       String str = (String)paramNamingEnumeration.next();
/*  970 */       if (str.equalsIgnoreCase(paramString)) {
/*  971 */         return true;
/*      */       }
/*      */     }
/*  974 */     return false;
/*      */   }
/*      */ 
/*      */   private void adjustDeleteStatus(String paramString, LdapResult paramLdapResult)
/*      */   {
/*  979 */     if ((paramLdapResult.status == 32) && (paramLdapResult.matchedDN != null))
/*      */     {
/*      */       try
/*      */       {
/*  984 */         Name localName1 = parser.parse(paramString);
/*  985 */         Name localName2 = parser.parse(paramLdapResult.matchedDN);
/*  986 */         if (localName1.size() - localName2.size() == 1)
/*  987 */           paramLdapResult.status = 0;
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Vector appendVector(Vector paramVector1, Vector paramVector2)
/*      */   {
/*  997 */     if (paramVector1 == null)
/*  998 */       paramVector1 = paramVector2;
/*      */     else {
/* 1000 */       for (int i = 0; i < paramVector2.size(); i++) {
/* 1001 */         paramVector1.addElement(paramVector2.elementAt(i));
/*      */       }
/*      */     }
/* 1004 */     return paramVector1;
/*      */   }
/*      */ 
/*      */   protected Object c_lookupLink(Name paramName, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1013 */     return c_lookup(paramName, paramContinuation);
/*      */   }
/*      */   protected Object c_lookup(Name paramName, Continuation paramContinuation) throws NamingException {
/* 1018 */     paramContinuation.setError(this, paramName);
/* 1019 */     Object localObject1 = null;
/*      */     Object localObject4;
/*      */     Object localObject2;
/*      */     try { SearchControls localSearchControls = new SearchControls();
/* 1024 */       localSearchControls.setSearchScope(0);
/* 1025 */       localSearchControls.setReturningAttributes(null);
/* 1026 */       localSearchControls.setReturningObjFlag(true);
/*      */ 
/* 1028 */       localObject4 = doSearchOnce(paramName, "(objectClass=*)", localSearchControls, true);
/* 1029 */       this.respCtls = ((LdapResult)localObject4).resControls;
/*      */ 
/* 1033 */       if (((LdapResult)localObject4).status != 0) {
/* 1034 */         processReturnCode((LdapResult)localObject4, paramName);
/*      */       }
/*      */ 
/* 1037 */       if ((((LdapResult)localObject4).entries == null) || (((LdapResult)localObject4).entries.size() != 1))
/*      */       {
/* 1039 */         localObject2 = new BasicAttributes(true);
/*      */       } else {
/* 1041 */         localObject5 = (LdapEntry)((LdapResult)localObject4).entries.elementAt(0);
/* 1042 */         localObject2 = ((LdapEntry)localObject5).attributes;
/*      */ 
/* 1044 */         Vector localVector = ((LdapEntry)localObject5).respCtls;
/* 1045 */         if (localVector != null) {
/* 1046 */           appendVector(this.respCtls, localVector);
/*      */         }
/*      */       }
/*      */ 
/* 1050 */       if (((Attributes)localObject2).get(Obj.JAVA_ATTRIBUTES[2]) != null)
/*      */       {
/* 1052 */         localObject1 = Obj.decodeObject((Attributes)localObject2);
/*      */       }
/* 1054 */       if (localObject1 == null)
/* 1055 */         localObject1 = new LdapCtx(this, fullyQualifiedName(paramName));
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*      */       Object localObject5;
/* 1058 */       if (this.handleReferrals == 2) {
/* 1059 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1064 */         localObject4 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1069 */           return ((LdapReferralContext)localObject4).lookup(paramName);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/* 1072 */           Object localObject3 = localLdapReferralException2;
/*      */ 
/* 1077 */           ((LdapReferralContext)localObject4).close(); } finally { ((LdapReferralContext)localObject4).close(); }
/*      */       }
/*      */     }
/*      */     catch (NamingException localNamingException1)
/*      */     {
/* 1082 */       throw paramContinuation.fillInException(localNamingException1);
/*      */     }
/*      */     try
/*      */     {
/* 1086 */       return DirectoryManager.getObjectInstance(localObject1, paramName, this, this.envprops, (Attributes)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException2)
/*      */     {
/* 1090 */       throw paramContinuation.fillInException(localNamingException2);
/*      */     }
/*      */     catch (Exception localException) {
/* 1093 */       localObject4 = new NamingException("problem generating object using object factory");
/*      */ 
/* 1095 */       ((NamingException)localObject4).setRootCause(localException);
/* 1096 */     }throw paramContinuation.fillInException((NamingException)localObject4);
/*      */   }
/*      */ 
/*      */   protected NamingEnumeration c_list(Name paramName, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1102 */     SearchControls localSearchControls = new SearchControls();
/* 1103 */     String[] arrayOfString = new String[2];
/*      */ 
/* 1105 */     arrayOfString[0] = Obj.JAVA_ATTRIBUTES[0];
/* 1106 */     arrayOfString[1] = Obj.JAVA_ATTRIBUTES[2];
/* 1107 */     localSearchControls.setReturningAttributes(arrayOfString);
/*      */ 
/* 1110 */     localSearchControls.setReturningObjFlag(true);
/*      */ 
/* 1112 */     paramContinuation.setError(this, paramName);
/*      */ 
/* 1114 */     LdapResult localLdapResult = null;
/*      */     try
/*      */     {
/* 1117 */       localLdapResult = doSearch(paramName, "(objectClass=*)", localSearchControls, true, true);
/*      */ 
/* 1120 */       if ((localLdapResult.status != 0) || (localLdapResult.referrals != null))
/*      */       {
/* 1122 */         processReturnCode(localLdapResult, paramName);
/*      */       }
/*      */ 
/* 1125 */       return new LdapNamingEnumeration(this, localLdapResult, paramName, paramContinuation);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1) {
/* 1128 */       if (this.handleReferrals == 2) {
/* 1129 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1134 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1140 */           return ((LdapReferralContext)localObject2).list(paramName);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/* 1143 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 1148 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (LimitExceededException localLimitExceededException)
/*      */     {
/* 1153 */       localObject2 = new LdapNamingEnumeration(this, localLdapResult, paramName, paramContinuation);
/*      */ 
/* 1156 */       ((LdapNamingEnumeration)localObject2).setNamingException((LimitExceededException)paramContinuation.fillInException(localLimitExceededException));
/*      */ 
/* 1158 */       return localObject2;
/*      */     }
/*      */     catch (PartialResultException localPartialResultException) {
/* 1161 */       Object localObject2 = new LdapNamingEnumeration(this, localLdapResult, paramName, paramContinuation);
/*      */ 
/* 1164 */       ((LdapNamingEnumeration)localObject2).setNamingException((PartialResultException)paramContinuation.fillInException(localPartialResultException));
/*      */ 
/* 1166 */       return localObject2;
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1169 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected NamingEnumeration c_listBindings(Name paramName, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1176 */     SearchControls localSearchControls = new SearchControls();
/* 1177 */     localSearchControls.setReturningAttributes(null);
/* 1178 */     localSearchControls.setReturningObjFlag(true);
/*      */ 
/* 1180 */     paramContinuation.setError(this, paramName);
/*      */ 
/* 1182 */     LdapResult localLdapResult = null;
/*      */     try
/*      */     {
/* 1185 */       localLdapResult = doSearch(paramName, "(objectClass=*)", localSearchControls, true, true);
/*      */ 
/* 1188 */       if ((localLdapResult.status != 0) || (localLdapResult.referrals != null))
/*      */       {
/* 1190 */         processReturnCode(localLdapResult, paramName);
/*      */       }
/*      */ 
/* 1193 */       return new LdapBindingEnumeration(this, localLdapResult, paramName, paramContinuation);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1) {
/* 1196 */       if (this.handleReferrals == 2) {
/* 1197 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1202 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1208 */           return ((LdapReferralContext)localObject2).listBindings(paramName);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/* 1211 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 1216 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (LimitExceededException localLimitExceededException) {
/* 1220 */       localObject2 = new LdapBindingEnumeration(this, localLdapResult, paramName, paramContinuation);
/*      */ 
/* 1223 */       ((LdapBindingEnumeration)localObject2).setNamingException((LimitExceededException)paramContinuation.fillInException(localLimitExceededException));
/*      */ 
/* 1225 */       return localObject2;
/*      */     }
/*      */     catch (PartialResultException localPartialResultException) {
/* 1228 */       Object localObject2 = new LdapBindingEnumeration(this, localLdapResult, paramName, paramContinuation);
/*      */ 
/* 1231 */       ((LdapBindingEnumeration)localObject2).setNamingException((PartialResultException)paramContinuation.fillInException(localPartialResultException));
/*      */ 
/* 1233 */       return localObject2;
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1236 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected NameParser c_getNameParser(Name paramName, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1247 */     paramContinuation.setSuccess();
/* 1248 */     return parser;
/*      */   }
/*      */ 
/*      */   public String getNameInNamespace() {
/* 1252 */     return this.currentDN;
/*      */   }
/*      */ 
/*      */   public Name composeName(Name paramName1, Name paramName2)
/*      */     throws NamingException
/*      */   {
/* 1261 */     if (((paramName1 instanceof LdapName)) && ((paramName2 instanceof LdapName))) {
/* 1262 */       localName = (Name)paramName2.clone();
/* 1263 */       localName.addAll(paramName1);
/* 1264 */       return new CompositeName().add(localName.toString());
/*      */     }
/* 1266 */     if (!(paramName1 instanceof CompositeName)) {
/* 1267 */       paramName1 = new CompositeName().add(paramName1.toString());
/*      */     }
/* 1269 */     if (!(paramName2 instanceof CompositeName)) {
/* 1270 */       paramName2 = new CompositeName().add(paramName2.toString());
/*      */     }
/*      */ 
/* 1273 */     int i = paramName2.size() - 1;
/*      */ 
/* 1275 */     if ((paramName1.isEmpty()) || (paramName2.isEmpty()) || (paramName1.get(0).equals("")) || (paramName2.get(i).equals("")))
/*      */     {
/* 1277 */       return super.composeName(paramName1, paramName2);
/*      */     }
/*      */ 
/* 1280 */     Name localName = (Name)paramName2.clone();
/* 1281 */     localName.addAll(paramName1);
/*      */ 
/* 1283 */     if (this.parentIsLdapCtx) {
/* 1284 */       String str = concatNames(localName.get(i + 1), localName.get(i));
/*      */ 
/* 1286 */       localName.remove(i + 1);
/* 1287 */       localName.remove(i);
/* 1288 */       localName.add(i, str);
/*      */     }
/* 1290 */     return localName;
/*      */   }
/*      */ 
/*      */   private String fullyQualifiedName(Name paramName) {
/* 1294 */     return paramName.isEmpty() ? this.currentDN : fullyQualifiedName(paramName.get(0));
/*      */   }
/*      */ 
/*      */   private String fullyQualifiedName(String paramString)
/*      */   {
/* 1300 */     return concatNames(paramString, this.currentDN);
/*      */   }
/*      */ 
/*      */   private static String concatNames(String paramString1, String paramString2)
/*      */   {
/* 1305 */     if ((paramString1 == null) || (paramString1.equals("")))
/* 1306 */       return paramString2;
/* 1307 */     if ((paramString2 == null) || (paramString2.equals(""))) {
/* 1308 */       return paramString1;
/*      */     }
/* 1310 */     return paramString1 + "," + paramString2;
/*      */   }
/*      */ 
/*      */   protected Attributes c_getAttributes(Name paramName, String[] paramArrayOfString, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1320 */     paramContinuation.setError(this, paramName);
/*      */ 
/* 1322 */     SearchControls localSearchControls = new SearchControls();
/* 1323 */     localSearchControls.setSearchScope(0);
/* 1324 */     localSearchControls.setReturningAttributes(paramArrayOfString);
/*      */     try
/*      */     {
/* 1327 */       LdapResult localLdapResult = doSearchOnce(paramName, "(objectClass=*)", localSearchControls, true);
/*      */ 
/* 1329 */       this.respCtls = localLdapResult.resControls;
/*      */ 
/* 1331 */       if (localLdapResult.status != 0) {
/* 1332 */         processReturnCode(localLdapResult, paramName);
/*      */       }
/*      */ 
/* 1335 */       if ((localLdapResult.entries == null) || (localLdapResult.entries.size() != 1)) {
/* 1336 */         return new BasicAttributes(true);
/*      */       }
/*      */ 
/* 1340 */       localObject2 = (LdapEntry)localLdapResult.entries.elementAt(0);
/*      */ 
/* 1342 */       localObject3 = ((LdapEntry)localObject2).respCtls;
/* 1343 */       if (localObject3 != null) {
/* 1344 */         appendVector(this.respCtls, (Vector)localObject3);
/*      */       }
/*      */ 
/* 1348 */       setParents(((LdapEntry)localObject2).attributes, (Name)paramName.clone());
/*      */ 
/* 1350 */       return ((LdapEntry)localObject2).attributes;
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*      */       Object localObject2;
/*      */       Object localObject3;
/* 1353 */       if (this.handleReferrals == 2) {
/* 1354 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1359 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1365 */           return ((LdapReferralContext)localObject2).getAttributes(paramName, paramArrayOfString);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/* 1368 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 1373 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (NamingException localNamingException)
/*      */     {
/* 1378 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void c_modifyAttributes(Name paramName, int paramInt, Attributes paramAttributes, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1386 */     paramContinuation.setError(this, paramName);
/*      */     try
/*      */     {
/* 1389 */       ensureOpen();
/*      */ 
/* 1391 */       if ((paramAttributes == null) || (paramAttributes.size() == 0)) {
/* 1392 */         return;
/*      */       }
/* 1394 */       String str = fullyQualifiedName(paramName);
/* 1395 */       int i = convertToLdapModCode(paramInt);
/*      */ 
/* 1398 */       int[] arrayOfInt = new int[paramAttributes.size()];
/* 1399 */       Attribute[] arrayOfAttribute = new Attribute[paramAttributes.size()];
/*      */ 
/* 1401 */       NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/* 1402 */       for (int j = 0; (j < arrayOfInt.length) && (localNamingEnumeration.hasMore()); j++) {
/* 1403 */         arrayOfInt[j] = i;
/* 1404 */         arrayOfAttribute[j] = ((Attribute)localNamingEnumeration.next());
/*      */       }
/*      */ 
/* 1407 */       LdapResult localLdapResult = this.clnt.modify(str, arrayOfInt, arrayOfAttribute, this.reqCtls);
/* 1408 */       this.respCtls = localLdapResult.resControls;
/*      */ 
/* 1410 */       if (localLdapResult.status != 0) {
/* 1411 */         processReturnCode(localLdapResult, paramName);
/* 1412 */         return;
/*      */       }
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1) {
/* 1416 */       if (this.handleReferrals == 2) {
/* 1417 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1422 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1428 */           ((LdapReferralContext)localObject2).modifyAttributes(paramName, paramInt, paramAttributes);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2)
/*      */         {
/* 1432 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 1437 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1442 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/* 1443 */       ((NamingException)localObject2).setRootCause(localIOException);
/* 1444 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1447 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void c_modifyAttributes(Name paramName, ModificationItem[] paramArrayOfModificationItem, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1454 */     paramContinuation.setError(this, paramName);
/*      */     try
/*      */     {
/* 1457 */       ensureOpen();
/*      */ 
/* 1459 */       if ((paramArrayOfModificationItem == null) || (paramArrayOfModificationItem.length == 0)) {
/* 1460 */         return;
/*      */       }
/* 1462 */       String str = fullyQualifiedName(paramName);
/*      */ 
/* 1465 */       localObject2 = new int[paramArrayOfModificationItem.length];
/* 1466 */       Attribute[] arrayOfAttribute = new Attribute[paramArrayOfModificationItem.length];
/*      */ 
/* 1468 */       for (int i = 0; i < localObject2.length; i++) {
/* 1469 */         ModificationItem localModificationItem = paramArrayOfModificationItem[i];
/* 1470 */         localObject2[i] = convertToLdapModCode(localModificationItem.getModificationOp());
/* 1471 */         arrayOfAttribute[i] = localModificationItem.getAttribute();
/*      */       }
/*      */ 
/* 1474 */       LdapResult localLdapResult = this.clnt.modify(str, (int[])localObject2, arrayOfAttribute, this.reqCtls);
/* 1475 */       this.respCtls = localLdapResult.resControls;
/*      */ 
/* 1477 */       if (localLdapResult.status != 0)
/* 1478 */         processReturnCode(localLdapResult, paramName);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/* 1482 */       if (this.handleReferrals == 2) {
/* 1483 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1488 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1494 */           ((LdapReferralContext)localObject2).modifyAttributes(paramName, paramArrayOfModificationItem);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2)
/*      */         {
/* 1498 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 1503 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1508 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/* 1509 */       ((NamingException)localObject2).setRootCause(localIOException);
/* 1510 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1513 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static int convertToLdapModCode(int paramInt) {
/* 1518 */     switch (paramInt) {
/*      */     case 1:
/* 1520 */       return 0;
/*      */     case 2:
/* 1523 */       return 2;
/*      */     case 3:
/* 1526 */       return 1;
/*      */     }
/*      */ 
/* 1529 */     throw new IllegalArgumentException("Invalid modification code");
/*      */   }
/*      */ 
/*      */   protected DirContext c_getSchema(Name paramName, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1537 */     paramContinuation.setError(this, paramName);
/*      */     try {
/* 1539 */       return getSchemaTree(paramName);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1542 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected DirContext c_getSchemaClassDefinition(Name paramName, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1549 */     paramContinuation.setError(this, paramName);
/*      */     try
/*      */     {
/* 1553 */       Attribute localAttribute = c_getAttributes(paramName, new String[] { "objectclass" }, paramContinuation).get("objectclass");
/*      */ 
/* 1555 */       if ((localAttribute == null) || (localAttribute.size() == 0)) {
/* 1556 */         return EMPTY_SCHEMA;
/*      */       }
/*      */ 
/* 1560 */       Context localContext = (Context)c_getSchema(paramName, paramContinuation).lookup("ClassDefinition");
/*      */ 
/* 1565 */       HierMemDirCtx localHierMemDirCtx = new HierMemDirCtx();
/*      */ 
/* 1568 */       NamingEnumeration localNamingEnumeration = localAttribute.getAll();
/* 1569 */       while (localNamingEnumeration.hasMoreElements()) {
/* 1570 */         String str = (String)localNamingEnumeration.nextElement();
/*      */ 
/* 1572 */         DirContext localDirContext = (DirContext)localContext.lookup(str);
/* 1573 */         localHierMemDirCtx.bind(str, localDirContext);
/*      */       }
/*      */ 
/* 1577 */       localHierMemDirCtx.setReadOnly(new SchemaViolationException("Cannot update schema object"));
/*      */ 
/* 1579 */       return localHierMemDirCtx;
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1582 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private DirContext getSchemaTree(Name paramName)
/*      */     throws NamingException
/*      */   {
/* 1592 */     String str = getSchemaEntry(paramName, true);
/*      */ 
/* 1594 */     DirContext localDirContext = (DirContext)this.schemaTrees.get(str);
/*      */ 
/* 1596 */     if (localDirContext == null)
/*      */     {
/* 1598 */       localDirContext = buildSchemaTree(str);
/* 1599 */       this.schemaTrees.put(str, localDirContext);
/*      */     }
/*      */ 
/* 1602 */     return localDirContext;
/*      */   }
/*      */ 
/*      */   private DirContext buildSchemaTree(String paramString)
/*      */     throws NamingException
/*      */   {
/* 1616 */     SearchControls localSearchControls = new SearchControls(0, 0L, 0, SCHEMA_ATTRIBUTES, true, false);
/*      */ 
/* 1623 */     Name localName = new CompositeName().add(paramString);
/* 1624 */     NamingEnumeration localNamingEnumeration = searchAux(localName, "(objectClass=subschema)", localSearchControls, false, true, new Continuation());
/*      */ 
/* 1628 */     if (!localNamingEnumeration.hasMore()) {
/* 1629 */       throw new OperationNotSupportedException("Cannot get read subschemasubentry: " + paramString);
/*      */     }
/*      */ 
/* 1632 */     SearchResult localSearchResult = (SearchResult)localNamingEnumeration.next();
/* 1633 */     localNamingEnumeration.close();
/*      */ 
/* 1635 */     Object localObject = localSearchResult.getObject();
/* 1636 */     if (!(localObject instanceof LdapCtx)) {
/* 1637 */       throw new NamingException("Cannot get schema object as DirContext: " + paramString);
/*      */     }
/*      */ 
/* 1641 */     return LdapSchemaCtx.createSchemaTree(this.envprops, paramString, (LdapCtx)localObject, localSearchResult.getAttributes(), this.netscapeSchemaBug);
/*      */   }
/*      */ 
/*      */   private String getSchemaEntry(Name paramName, boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/* 1671 */     SearchControls localSearchControls = new SearchControls(0, 0L, 0, new String[] { "subschemasubentry" }, false, false);
/*      */     NamingEnumeration localNamingEnumeration;
/*      */     try
/*      */     {
/* 1679 */       localNamingEnumeration = searchAux(paramName, "objectclass=*", localSearchControls, paramBoolean, true, new Continuation());
/*      */     }
/*      */     catch (NamingException localNamingException)
/*      */     {
/* 1683 */       if ((!this.clnt.isLdapv3) && (this.currentDN.length() == 0) && (paramName.isEmpty()))
/*      */       {
/* 1686 */         throw new OperationNotSupportedException("Cannot get schema information from server");
/*      */       }
/*      */ 
/* 1689 */       throw localNamingException;
/*      */     }
/*      */ 
/* 1693 */     if (!localNamingEnumeration.hasMoreElements()) {
/* 1694 */       throw new ConfigurationException("Requesting schema of nonexistent entry: " + paramName);
/*      */     }
/*      */ 
/* 1698 */     SearchResult localSearchResult = (SearchResult)localNamingEnumeration.next();
/* 1699 */     localNamingEnumeration.close();
/*      */ 
/* 1701 */     Attribute localAttribute = localSearchResult.getAttributes().get("subschemasubentry");
/*      */ 
/* 1705 */     if ((localAttribute == null) || (localAttribute.size() < 0)) {
/* 1706 */       if ((this.currentDN.length() == 0) && (paramName.isEmpty()))
/*      */       {
/* 1709 */         throw new OperationNotSupportedException("Cannot read subschemasubentry of root DSE");
/*      */       }
/*      */ 
/* 1712 */       return getSchemaEntry(new CompositeName(), false);
/*      */     }
/*      */ 
/* 1716 */     return (String)localAttribute.get();
/*      */   }
/*      */ 
/*      */   void setParents(Attributes paramAttributes, Name paramName)
/*      */     throws NamingException
/*      */   {
/* 1723 */     NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/* 1724 */     while (localNamingEnumeration.hasMore())
/* 1725 */       ((LdapAttribute)localNamingEnumeration.next()).setParent(this, paramName);
/*      */   }
/*      */ 
/*      */   String getURL()
/*      */   {
/* 1734 */     if (this.url == null) {
/* 1735 */       this.url = LdapURL.toUrlString(this.hostname, this.port_number, this.currentDN, this.hasLdapsScheme);
/*      */     }
/*      */ 
/* 1739 */     return this.url;
/*      */   }
/*      */ 
/*      */   protected NamingEnumeration c_search(Name paramName, Attributes paramAttributes, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1747 */     return c_search(paramName, paramAttributes, null, paramContinuation);
/*      */   }
/*      */ 
/*      */   protected NamingEnumeration c_search(Name paramName, Attributes paramAttributes, String[] paramArrayOfString, Continuation paramContinuation) throws NamingException
/*      */   {
/* 1755 */     SearchControls localSearchControls = new SearchControls();
/* 1756 */     localSearchControls.setReturningAttributes(paramArrayOfString);
/*      */     String str;
/*      */     try
/*      */     {
/* 1759 */       str = SearchFilter.format(paramAttributes);
/*      */     } catch (NamingException localNamingException) {
/* 1761 */       paramContinuation.setError(this, paramName);
/* 1762 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/* 1764 */     return c_search(paramName, str, localSearchControls, paramContinuation);
/*      */   }
/*      */ 
/*      */   protected NamingEnumeration c_search(Name paramName, String paramString, SearchControls paramSearchControls, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1772 */     return searchAux(paramName, paramString, cloneSearchControls(paramSearchControls), true, this.waitForReply, paramContinuation);
/*      */   }
/*      */ 
/*      */   protected NamingEnumeration c_search(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/*      */     String str;
/*      */     try
/*      */     {
/* 1784 */       str = SearchFilter.format(paramString, paramArrayOfObject);
/*      */     } catch (NamingException localNamingException) {
/* 1786 */       paramContinuation.setError(this, paramName);
/* 1787 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/* 1789 */     return c_search(paramName, str, paramSearchControls, paramContinuation);
/*      */   }
/*      */ 
/*      */   NamingEnumeration searchAux(Name paramName, String paramString, SearchControls paramSearchControls, boolean paramBoolean1, boolean paramBoolean2, Continuation paramContinuation)
/*      */     throws NamingException
/*      */   {
/* 1799 */     LdapResult localLdapResult = null;
/* 1800 */     String[] arrayOfString1 = new String[2];
/*      */ 
/* 1803 */     if (paramSearchControls == null) {
/* 1804 */       paramSearchControls = new SearchControls();
/*      */     }
/* 1806 */     String[] arrayOfString2 = paramSearchControls.getReturningAttributes();
/*      */ 
/* 1810 */     if ((paramSearchControls.getReturningObjFlag()) && 
/* 1811 */       (arrayOfString2 != null))
/*      */     {
/* 1814 */       int i = 0;
/* 1815 */       for (int j = arrayOfString2.length - 1; j >= 0; j--) {
/* 1816 */         if (arrayOfString2[j].equals("*")) {
/* 1817 */           i = 1;
/* 1818 */           break;
/*      */         }
/*      */       }
/* 1821 */       if (i == 0) {
/* 1822 */         String[] arrayOfString3 = new String[arrayOfString2.length + Obj.JAVA_ATTRIBUTES.length];
/*      */ 
/* 1824 */         System.arraycopy(arrayOfString2, 0, arrayOfString3, 0, arrayOfString2.length);
/*      */ 
/* 1826 */         System.arraycopy(Obj.JAVA_ATTRIBUTES, 0, arrayOfString3, arrayOfString2.length, Obj.JAVA_ATTRIBUTES.length);
/*      */ 
/* 1829 */         paramSearchControls.setReturningAttributes(arrayOfString3);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1834 */     SearchArgs localSearchArgs = new SearchArgs(paramName, paramString, paramSearchControls, arrayOfString2);
/*      */ 
/* 1837 */     paramContinuation.setError(this, paramName);
/*      */     try
/*      */     {
/* 1840 */       if (searchToCompare(paramString, paramSearchControls, arrayOfString1))
/*      */       {
/* 1842 */         localLdapResult = compare(paramName, arrayOfString1[0], arrayOfString1[1]);
/* 1843 */         if (!localLdapResult.compareToSearchResult(fullyQualifiedName(paramName)))
/* 1844 */           processReturnCode(localLdapResult, paramName);
/*      */       }
/*      */       else {
/* 1847 */         localLdapResult = doSearch(paramName, paramString, paramSearchControls, paramBoolean1, paramBoolean2);
/*      */ 
/* 1849 */         processReturnCode(localLdapResult, paramName);
/*      */       }
/* 1851 */       return new LdapSearchEnumeration(this, localLdapResult, fullyQualifiedName(paramName), localSearchArgs, paramContinuation);
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/* 1855 */       if (this.handleReferrals == 2) {
/* 1856 */         throw paramContinuation.fillInException(localLdapReferralException1);
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 1861 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 1867 */           return ((LdapReferralContext)localObject2).search(paramName, paramString, paramSearchControls);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/* 1870 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 1875 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (LimitExceededException localLimitExceededException)
/*      */     {
/* 1880 */       localObject2 = new LdapSearchEnumeration(this, localLdapResult, fullyQualifiedName(paramName), localSearchArgs, paramContinuation);
/*      */ 
/* 1883 */       ((LdapSearchEnumeration)localObject2).setNamingException(localLimitExceededException);
/* 1884 */       return localObject2;
/*      */     }
/*      */     catch (PartialResultException localPartialResultException) {
/* 1887 */       localObject2 = new LdapSearchEnumeration(this, localLdapResult, fullyQualifiedName(paramName), localSearchArgs, paramContinuation);
/*      */ 
/* 1891 */       ((LdapSearchEnumeration)localObject2).setNamingException(localPartialResultException);
/* 1892 */       return localObject2;
/*      */     }
/*      */     catch (IOException localIOException) {
/* 1895 */       Object localObject2 = new CommunicationException(localIOException.getMessage());
/* 1896 */       ((NamingException)localObject2).setRootCause(localIOException);
/* 1897 */       throw paramContinuation.fillInException((NamingException)localObject2);
/*      */     }
/*      */     catch (NamingException localNamingException) {
/* 1900 */       throw paramContinuation.fillInException(localNamingException);
/*      */     }
/*      */   }
/*      */ 
/*      */   LdapResult getSearchReply(LdapClient paramLdapClient, LdapResult paramLdapResult)
/*      */     throws NamingException
/*      */   {
/* 1913 */     if (this.clnt != paramLdapClient) {
/* 1914 */       throw new CommunicationException("Context's connection changed; unable to continue enumeration");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1919 */       return paramLdapClient.getSearchReply(this.batchSize, paramLdapResult, this.binaryAttrs);
/*      */     } catch (IOException localIOException) {
/* 1921 */       CommunicationException localCommunicationException = new CommunicationException(localIOException.getMessage());
/* 1922 */       localCommunicationException.setRootCause(localIOException);
/* 1923 */       throw localCommunicationException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private LdapResult doSearchOnce(Name paramName, String paramString, SearchControls paramSearchControls, boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/* 1931 */     int i = this.batchSize;
/* 1932 */     this.batchSize = 2;
/*      */ 
/* 1934 */     LdapResult localLdapResult = doSearch(paramName, paramString, paramSearchControls, paramBoolean, true);
/*      */ 
/* 1936 */     this.batchSize = i;
/* 1937 */     return localLdapResult;
/*      */   }
/*      */ 
/*      */   private LdapResult doSearch(Name paramName, String paramString, SearchControls paramSearchControls, boolean paramBoolean1, boolean paramBoolean2) throws NamingException
/*      */   {
/* 1942 */     ensureOpen();
/*      */     Object localObject;
/*      */     try
/*      */     {
/*      */       int i;
/* 1946 */       switch (paramSearchControls.getSearchScope()) {
/*      */       case 0:
/* 1948 */         i = 0;
/* 1949 */         break;
/*      */       case 1:
/*      */       default:
/* 1952 */         i = 1;
/* 1953 */         break;
/*      */       case 2:
/* 1955 */         i = 2;
/*      */       }
/*      */ 
/* 1962 */       localObject = paramSearchControls.getReturningAttributes();
/* 1963 */       if ((localObject != null) && (localObject.length == 0))
/*      */       {
/* 1966 */         localObject = new String[1];
/* 1967 */         localObject[0] = "1.1";
/*      */       }
/*      */ 
/* 1970 */       String str = paramName.isEmpty() ? "" : paramBoolean1 ? fullyQualifiedName(paramName) : paramName.get(0);
/*      */ 
/* 1978 */       int j = paramSearchControls.getTimeLimit();
/* 1979 */       int k = 0;
/*      */ 
/* 1981 */       if (j > 0) {
/* 1982 */         k = j / 1000 + 1;
/*      */       }
/*      */ 
/* 1985 */       LdapResult localLdapResult = this.clnt.search(str, i, this.derefAliases, (int)paramSearchControls.getCountLimit(), k, paramSearchControls.getReturningObjFlag() ? false : this.typesOnly, (String[])localObject, paramString, this.batchSize, this.reqCtls, this.binaryAttrs, paramBoolean2, this.replyQueueSize);
/*      */ 
/* 1999 */       this.respCtls = localLdapResult.resControls;
/* 2000 */       return localLdapResult;
/*      */     }
/*      */     catch (IOException localIOException) {
/* 2003 */       localObject = new CommunicationException(localIOException.getMessage());
/* 2004 */       ((NamingException)localObject).setRootCause(localIOException);
/* 2005 */     }throw ((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   private static boolean searchToCompare(String paramString, SearchControls paramSearchControls, String[] paramArrayOfString)
/*      */   {
/* 2032 */     if (paramSearchControls.getSearchScope() != 0) {
/* 2033 */       return false;
/*      */     }
/*      */ 
/* 2037 */     String[] arrayOfString = paramSearchControls.getReturningAttributes();
/* 2038 */     if ((arrayOfString == null) || (arrayOfString.length != 0)) {
/* 2039 */       return false;
/*      */     }
/*      */ 
/* 2043 */     if (!filterToAssertion(paramString, paramArrayOfString)) {
/* 2044 */       return false;
/*      */     }
/*      */ 
/* 2048 */     return true;
/*      */   }
/*      */ 
/*      */   private static boolean filterToAssertion(String paramString, String[] paramArrayOfString)
/*      */   {
/* 2060 */     StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString, "=");
/*      */ 
/* 2062 */     if (localStringTokenizer1.countTokens() != 2) {
/* 2063 */       return false;
/*      */     }
/*      */ 
/* 2066 */     paramArrayOfString[0] = localStringTokenizer1.nextToken();
/* 2067 */     paramArrayOfString[1] = localStringTokenizer1.nextToken();
/*      */ 
/* 2070 */     if (paramArrayOfString[1].indexOf('*') != -1) {
/* 2071 */       return false;
/*      */     }
/*      */ 
/* 2075 */     int i = 0;
/* 2076 */     int j = paramArrayOfString[1].length();
/*      */ 
/* 2078 */     if ((paramArrayOfString[0].charAt(0) == '(') && (paramArrayOfString[1].charAt(j - 1) == ')'))
/*      */     {
/* 2080 */       i = 1;
/*      */     }
/* 2082 */     else if ((paramArrayOfString[0].charAt(0) == '(') || (paramArrayOfString[1].charAt(j - 1) == ')'))
/*      */     {
/* 2084 */       return false;
/*      */     }
/*      */ 
/* 2088 */     StringTokenizer localStringTokenizer2 = new StringTokenizer(paramArrayOfString[0], "()&|!=~><*", true);
/*      */ 
/* 2091 */     if (localStringTokenizer2.countTokens() != (i != 0 ? 2 : 1)) {
/* 2092 */       return false;
/*      */     }
/*      */ 
/* 2095 */     localStringTokenizer2 = new StringTokenizer(paramArrayOfString[1], "()&|!=~><*", true);
/*      */ 
/* 2098 */     if (localStringTokenizer2.countTokens() != (i != 0 ? 2 : 1)) {
/* 2099 */       return false;
/*      */     }
/*      */ 
/* 2103 */     if (i != 0) {
/* 2104 */       paramArrayOfString[0] = paramArrayOfString[0].substring(1);
/* 2105 */       paramArrayOfString[1] = paramArrayOfString[1].substring(0, j - 1);
/*      */     }
/*      */ 
/* 2108 */     return true;
/*      */   }
/*      */ 
/*      */   private LdapResult compare(Name paramName, String paramString1, String paramString2)
/*      */     throws IOException, NamingException
/*      */   {
/* 2114 */     ensureOpen();
/* 2115 */     String str = fullyQualifiedName(paramName);
/*      */ 
/* 2117 */     LdapResult localLdapResult = this.clnt.compare(str, paramString1, paramString2, this.reqCtls);
/* 2118 */     this.respCtls = localLdapResult.resControls;
/*      */ 
/* 2120 */     return localLdapResult;
/*      */   }
/*      */ 
/*      */   private static SearchControls cloneSearchControls(SearchControls paramSearchControls) {
/* 2124 */     if (paramSearchControls == null) {
/* 2125 */       return null;
/*      */     }
/* 2127 */     Object localObject = paramSearchControls.getReturningAttributes();
/* 2128 */     if (localObject != null) {
/* 2129 */       String[] arrayOfString = new String[localObject.length];
/* 2130 */       System.arraycopy(localObject, 0, arrayOfString, 0, localObject.length);
/* 2131 */       localObject = arrayOfString;
/*      */     }
/* 2133 */     return new SearchControls(paramSearchControls.getSearchScope(), paramSearchControls.getCountLimit(), paramSearchControls.getTimeLimit(), (String[])localObject, paramSearchControls.getReturningObjFlag(), paramSearchControls.getDerefLinkFlag());
/*      */   }
/*      */ 
/*      */   protected Hashtable p_getEnvironment()
/*      */   {
/* 2147 */     return this.envprops;
/*      */   }
/*      */ 
/*      */   public Hashtable getEnvironment() throws NamingException {
/* 2151 */     return this.envprops == null ? new Hashtable(5, 0.75F) : (Hashtable)this.envprops.clone();
/*      */   }
/*      */ 
/*      */   public Object removeFromEnvironment(String paramString)
/*      */     throws NamingException
/*      */   {
/* 2160 */     if ((this.envprops == null) || (this.envprops.get(paramString) == null)) {
/* 2161 */       return null;
/*      */     }
/*      */ 
/* 2164 */     if (paramString.equals("java.naming.ldap.ref.separator")) {
/* 2165 */       this.addrEncodingSeparator = '#';
/* 2166 */     } else if (paramString.equals("java.naming.ldap.typesOnly")) {
/* 2167 */       this.typesOnly = false;
/* 2168 */     } else if (paramString.equals("java.naming.ldap.deleteRDN")) {
/* 2169 */       this.deleteRDN = true;
/* 2170 */     } else if (paramString.equals("java.naming.ldap.derefAliases")) {
/* 2171 */       this.derefAliases = 3;
/* 2172 */     } else if (paramString.equals("java.naming.batchsize")) {
/* 2173 */       this.batchSize = 1;
/* 2174 */     } else if (paramString.equals("java.naming.ldap.referral.limit")) {
/* 2175 */       this.referralHopLimit = 10;
/* 2176 */     } else if (paramString.equals("java.naming.referral")) {
/* 2177 */       setReferralMode(null, true);
/* 2178 */     } else if (paramString.equals("java.naming.ldap.attributes.binary")) {
/* 2179 */       setBinaryAttributes(null);
/* 2180 */     } else if (paramString.equals("com.sun.jndi.ldap.connect.timeout")) {
/* 2181 */       this.connectTimeout = -1;
/* 2182 */     } else if (paramString.equals("com.sun.jndi.ldap.read.timeout")) {
/* 2183 */       this.readTimeout = -1;
/* 2184 */     } else if (paramString.equals("com.sun.jndi.ldap.search.waitForReply")) {
/* 2185 */       this.waitForReply = true;
/* 2186 */     } else if (paramString.equals("com.sun.jndi.ldap.search.replyQueueSize")) {
/* 2187 */       this.replyQueueSize = -1;
/*      */     }
/* 2191 */     else if (paramString.equals("java.naming.security.protocol")) {
/* 2192 */       closeConnection(false);
/*      */ 
/* 2194 */       if ((this.useSsl) && (!this.hasLdapsScheme)) {
/* 2195 */         this.useSsl = false;
/* 2196 */         this.url = null;
/* 2197 */         if (this.useDefaultPortNumber)
/* 2198 */           this.port_number = 389;
/*      */       }
/*      */     }
/* 2201 */     else if ((paramString.equals("java.naming.ldap.version")) || (paramString.equals("java.naming.ldap.factory.socket")))
/*      */     {
/* 2203 */       closeConnection(false);
/* 2204 */     } else if ((paramString.equals("java.naming.security.authentication")) || (paramString.equals("java.naming.security.principal")) || (paramString.equals("java.naming.security.credentials")))
/*      */     {
/* 2207 */       this.sharable = false;
/*      */     }
/*      */ 
/* 2211 */     this.envprops = ((Hashtable)this.envprops.clone());
/* 2212 */     return this.envprops.remove(paramString);
/*      */   }
/*      */ 
/*      */   public Object addToEnvironment(String paramString, Object paramObject)
/*      */     throws NamingException
/*      */   {
/* 2219 */     if (paramObject == null) {
/* 2220 */       return removeFromEnvironment(paramString);
/*      */     }
/*      */ 
/* 2223 */     if (paramString.equals("java.naming.ldap.ref.separator")) {
/* 2224 */       setRefSeparator((String)paramObject);
/* 2225 */     } else if (paramString.equals("java.naming.ldap.typesOnly")) {
/* 2226 */       setTypesOnly((String)paramObject);
/* 2227 */     } else if (paramString.equals("java.naming.ldap.deleteRDN")) {
/* 2228 */       setDeleteRDN((String)paramObject);
/* 2229 */     } else if (paramString.equals("java.naming.ldap.derefAliases")) {
/* 2230 */       setDerefAliases((String)paramObject);
/* 2231 */     } else if (paramString.equals("java.naming.batchsize")) {
/* 2232 */       setBatchSize((String)paramObject);
/* 2233 */     } else if (paramString.equals("java.naming.ldap.referral.limit")) {
/* 2234 */       setReferralLimit((String)paramObject);
/* 2235 */     } else if (paramString.equals("java.naming.referral")) {
/* 2236 */       setReferralMode((String)paramObject, true);
/* 2237 */     } else if (paramString.equals("java.naming.ldap.attributes.binary")) {
/* 2238 */       setBinaryAttributes((String)paramObject);
/* 2239 */     } else if (paramString.equals("com.sun.jndi.ldap.connect.timeout")) {
/* 2240 */       setConnectTimeout((String)paramObject);
/* 2241 */     } else if (paramString.equals("com.sun.jndi.ldap.read.timeout")) {
/* 2242 */       setReadTimeout((String)paramObject);
/* 2243 */     } else if (paramString.equals("com.sun.jndi.ldap.search.waitForReply")) {
/* 2244 */       setWaitForReply((String)paramObject);
/* 2245 */     } else if (paramString.equals("com.sun.jndi.ldap.search.replyQueueSize")) {
/* 2246 */       setReplyQueueSize((String)paramObject);
/*      */     }
/* 2250 */     else if (paramString.equals("java.naming.security.protocol")) {
/* 2251 */       closeConnection(false);
/*      */ 
/* 2253 */       if ("ssl".equals(paramObject)) {
/* 2254 */         this.useSsl = true;
/* 2255 */         this.url = null;
/* 2256 */         if (this.useDefaultPortNumber)
/* 2257 */           this.port_number = 636;
/*      */       }
/*      */     }
/* 2260 */     else if ((paramString.equals("java.naming.ldap.version")) || (paramString.equals("java.naming.ldap.factory.socket")))
/*      */     {
/* 2262 */       closeConnection(false);
/* 2263 */     } else if ((paramString.equals("java.naming.security.authentication")) || (paramString.equals("java.naming.security.principal")) || (paramString.equals("java.naming.security.credentials")))
/*      */     {
/* 2266 */       this.sharable = false;
/*      */     }
/*      */ 
/* 2270 */     this.envprops = (this.envprops == null ? new Hashtable(5, 0.75F) : (Hashtable)this.envprops.clone());
/*      */ 
/* 2273 */     return this.envprops.put(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   void setProviderUrl(String paramString)
/*      */   {
/* 2281 */     if (this.envprops != null)
/* 2282 */       this.envprops.put("java.naming.provider.url", paramString);
/*      */   }
/*      */ 
/*      */   void setDomainName(String paramString)
/*      */   {
/* 2292 */     if (this.envprops != null)
/* 2293 */       this.envprops.put("com.sun.jndi.ldap.domainname", paramString);
/*      */   }
/*      */ 
/*      */   private void initEnv() throws NamingException
/*      */   {
/* 2298 */     if (this.envprops == null)
/*      */     {
/* 2300 */       setReferralMode(null, false);
/* 2301 */       return;
/*      */     }
/*      */ 
/* 2305 */     setBatchSize((String)this.envprops.get("java.naming.batchsize"));
/*      */ 
/* 2308 */     setRefSeparator((String)this.envprops.get("java.naming.ldap.ref.separator"));
/*      */ 
/* 2311 */     setDeleteRDN((String)this.envprops.get("java.naming.ldap.deleteRDN"));
/*      */ 
/* 2314 */     setTypesOnly((String)this.envprops.get("java.naming.ldap.typesOnly"));
/*      */ 
/* 2317 */     setDerefAliases((String)this.envprops.get("java.naming.ldap.derefAliases"));
/*      */ 
/* 2320 */     setReferralLimit((String)this.envprops.get("java.naming.ldap.referral.limit"));
/*      */ 
/* 2322 */     setBinaryAttributes((String)this.envprops.get("java.naming.ldap.attributes.binary"));
/*      */ 
/* 2324 */     this.bindCtls = cloneControls((Control[])this.envprops.get("java.naming.ldap.control.connect"));
/*      */ 
/* 2327 */     setReferralMode((String)this.envprops.get("java.naming.referral"), false);
/*      */ 
/* 2330 */     setConnectTimeout((String)this.envprops.get("com.sun.jndi.ldap.connect.timeout"));
/*      */ 
/* 2333 */     setReadTimeout((String)this.envprops.get("com.sun.jndi.ldap.read.timeout"));
/*      */ 
/* 2337 */     setWaitForReply((String)this.envprops.get("com.sun.jndi.ldap.search.waitForReply"));
/*      */ 
/* 2340 */     setReplyQueueSize((String)this.envprops.get("com.sun.jndi.ldap.search.replyQueueSize"));
/*      */   }
/*      */ 
/*      */   private void setDeleteRDN(String paramString)
/*      */   {
/* 2347 */     if ((paramString != null) && (paramString.equalsIgnoreCase("false")))
/*      */     {
/* 2349 */       this.deleteRDN = false;
/*      */     }
/* 2351 */     else this.deleteRDN = true;
/*      */   }
/*      */ 
/*      */   private void setTypesOnly(String paramString)
/*      */   {
/* 2356 */     if ((paramString != null) && (paramString.equalsIgnoreCase("true")))
/*      */     {
/* 2358 */       this.typesOnly = true;
/*      */     }
/* 2360 */     else this.typesOnly = false;
/*      */   }
/*      */ 
/*      */   private void setBatchSize(String paramString)
/*      */   {
/* 2369 */     if (paramString != null)
/* 2370 */       this.batchSize = Integer.parseInt(paramString);
/*      */     else
/* 2372 */       this.batchSize = 1;
/*      */   }
/*      */ 
/*      */   private void setReferralMode(String paramString, boolean paramBoolean)
/*      */   {
/* 2382 */     if (paramString != null) {
/* 2383 */       if (paramString.equals("follow"))
/* 2384 */         this.handleReferrals = 1;
/* 2385 */       else if (paramString.equals("throw"))
/* 2386 */         this.handleReferrals = 2;
/* 2387 */       else if (paramString.equals("ignore"))
/* 2388 */         this.handleReferrals = 3;
/*      */       else {
/* 2390 */         throw new IllegalArgumentException("Illegal value for java.naming.referral property.");
/*      */       }
/*      */     }
/*      */     else {
/* 2394 */       this.handleReferrals = 3;
/*      */     }
/*      */ 
/* 2397 */     if (this.handleReferrals == 3)
/*      */     {
/* 2399 */       this.reqCtls = addControl(this.reqCtls, manageReferralControl);
/*      */     }
/* 2401 */     else if (paramBoolean)
/*      */     {
/* 2404 */       this.reqCtls = removeControl(this.reqCtls, manageReferralControl);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setDerefAliases(String paramString)
/*      */   {
/* 2413 */     if (paramString != null) {
/* 2414 */       if (paramString.equals("never"))
/* 2415 */         this.derefAliases = 0;
/* 2416 */       else if (paramString.equals("searching"))
/* 2417 */         this.derefAliases = 1;
/* 2418 */       else if (paramString.equals("finding"))
/* 2419 */         this.derefAliases = 2;
/* 2420 */       else if (paramString.equals("always"))
/* 2421 */         this.derefAliases = 3;
/*      */       else {
/* 2423 */         throw new IllegalArgumentException("Illegal value for java.naming.ldap.derefAliases property.");
/*      */       }
/*      */     }
/*      */     else
/* 2427 */       this.derefAliases = 3;
/*      */   }
/*      */ 
/*      */   private void setRefSeparator(String paramString) throws NamingException
/*      */   {
/* 2432 */     if ((paramString != null) && (paramString.length() > 0))
/* 2433 */       this.addrEncodingSeparator = paramString.charAt(0);
/*      */     else
/* 2435 */       this.addrEncodingSeparator = '#';
/*      */   }
/*      */ 
/*      */   private void setReferralLimit(String paramString)
/*      */   {
/* 2444 */     if (paramString != null) {
/* 2445 */       this.referralHopLimit = Integer.parseInt(paramString);
/*      */ 
/* 2448 */       if (this.referralHopLimit == 0)
/* 2449 */         this.referralHopLimit = 2147483647;
/*      */     } else {
/* 2451 */       this.referralHopLimit = 10;
/*      */     }
/*      */   }
/*      */ 
/*      */   void setHopCount(int paramInt)
/*      */   {
/* 2457 */     this.hopCount = paramInt;
/*      */   }
/*      */ 
/*      */   private void setConnectTimeout(String paramString)
/*      */   {
/* 2464 */     if (paramString != null)
/* 2465 */       this.connectTimeout = Integer.parseInt(paramString);
/*      */     else
/* 2467 */       this.connectTimeout = -1;
/*      */   }
/*      */ 
/*      */   private void setReplyQueueSize(String paramString)
/*      */   {
/* 2475 */     if (paramString != null) {
/* 2476 */       this.replyQueueSize = Integer.parseInt(paramString);
/*      */ 
/* 2478 */       if (this.replyQueueSize <= 0)
/* 2479 */         this.replyQueueSize = -1;
/*      */     }
/*      */     else {
/* 2482 */       this.replyQueueSize = -1;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setWaitForReply(String paramString)
/*      */   {
/* 2491 */     if ((paramString != null) && (paramString.equalsIgnoreCase("false")))
/*      */     {
/* 2493 */       this.waitForReply = false;
/*      */     }
/* 2495 */     else this.waitForReply = true;
/*      */   }
/*      */ 
/*      */   private void setReadTimeout(String paramString)
/*      */   {
/* 2503 */     if (paramString != null)
/* 2504 */       this.readTimeout = Integer.parseInt(paramString);
/*      */     else
/* 2506 */       this.readTimeout = -1;
/*      */   }
/*      */ 
/*      */   private static Vector extractURLs(String paramString)
/*      */   {
/* 2520 */     int i = 0;
/* 2521 */     int j = 0;
/*      */ 
/* 2524 */     while ((i = paramString.indexOf('\n', i)) >= 0) {
/* 2525 */       i++;
/* 2526 */       j++;
/*      */     }
/*      */ 
/* 2529 */     Vector localVector = new Vector(j);
/*      */ 
/* 2531 */     int m = 0;
/*      */ 
/* 2533 */     i = paramString.indexOf('\n');
/* 2534 */     int k = i + 1;
/* 2535 */     while ((i = paramString.indexOf('\n', k)) >= 0) {
/* 2536 */       localVector.addElement(paramString.substring(k, i));
/* 2537 */       k = i + 1;
/*      */     }
/* 2539 */     localVector.addElement(paramString.substring(k));
/*      */ 
/* 2541 */     return localVector;
/*      */   }
/*      */ 
/*      */   private void setBinaryAttributes(String paramString)
/*      */   {
/* 2549 */     if (paramString == null) {
/* 2550 */       this.binaryAttrs = null;
/*      */     } else {
/* 2552 */       this.binaryAttrs = new Hashtable(11, 0.75F);
/* 2553 */       StringTokenizer localStringTokenizer = new StringTokenizer(paramString.toLowerCase(), " ");
/*      */ 
/* 2556 */       while (localStringTokenizer.hasMoreTokens())
/* 2557 */         this.binaryAttrs.put(localStringTokenizer.nextToken(), Boolean.TRUE);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*      */     try
/*      */     {
/* 2566 */       close();
/*      */     }
/*      */     catch (NamingException localNamingException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws NamingException
/*      */   {
/* 2579 */     if (this.eventSupport != null) {
/* 2580 */       this.eventSupport.cleanup();
/* 2581 */       removeUnsolicited();
/*      */     }
/*      */ 
/* 2585 */     if (this.enumCount > 0)
/*      */     {
/* 2588 */       this.closeRequested = true;
/* 2589 */       return;
/*      */     }
/* 2591 */     closeConnection(false);
/*      */   }
/*      */ 
/*      */   public void reconnect(Control[] paramArrayOfControl)
/*      */     throws NamingException
/*      */   {
/* 2606 */     this.envprops = (this.envprops == null ? new Hashtable(5, 0.75F) : (Hashtable)this.envprops.clone());
/*      */ 
/* 2610 */     if (paramArrayOfControl == null) {
/* 2611 */       this.envprops.remove("java.naming.ldap.control.connect");
/* 2612 */       this.bindCtls = null;
/*      */     } else {
/* 2614 */       this.envprops.put("java.naming.ldap.control.connect", this.bindCtls = cloneControls(paramArrayOfControl));
/*      */     }
/*      */ 
/* 2617 */     this.sharable = false;
/* 2618 */     ensureOpen();
/*      */   }
/*      */ 
/*      */   private void ensureOpen() throws NamingException {
/* 2622 */     ensureOpen(false);
/*      */   }
/*      */ 
/*      */   private void ensureOpen(boolean paramBoolean) throws NamingException
/*      */   {
/*      */     try {
/* 2628 */       if (this.clnt == null)
/*      */       {
/* 2634 */         this.schemaTrees = new Hashtable(11, 0.75F);
/* 2635 */         connect(paramBoolean);
/*      */       }
/* 2637 */       else if ((!this.sharable) || (paramBoolean))
/*      */       {
/* 2639 */         synchronized (this.clnt) {
/* 2640 */           if ((!this.clnt.isLdapv3) || (this.clnt.referenceCount > 1) || (this.clnt.usingSaslStreams()))
/*      */           {
/* 2643 */             closeConnection(false);
/*      */           }
/*      */         }
/*      */ 
/* 2647 */         this.schemaTrees = new Hashtable(11, 0.75F);
/* 2648 */         connect(paramBoolean);
/*      */       }
/*      */     }
/*      */     finally {
/* 2652 */       this.sharable = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void connect(boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/* 2660 */     String str1 = null;
/* 2661 */     Object localObject1 = null;
/* 2662 */     String str2 = null;
/* 2663 */     String str3 = null;
/* 2664 */     String str4 = null;
/* 2665 */     String str5 = null;
/*      */ 
/* 2667 */     boolean bool1 = false;
/*      */ 
/* 2669 */     if (this.envprops != null) {
/* 2670 */       str1 = (String)this.envprops.get("java.naming.security.principal");
/* 2671 */       localObject1 = this.envprops.get("java.naming.security.credentials");
/* 2672 */       str5 = (String)this.envprops.get("java.naming.ldap.version");
/* 2673 */       str2 = this.useSsl ? "ssl" : (String)this.envprops.get("java.naming.security.protocol");
/*      */ 
/* 2675 */       str3 = (String)this.envprops.get("java.naming.ldap.factory.socket");
/* 2676 */       str4 = (String)this.envprops.get("java.naming.security.authentication");
/*      */ 
/* 2679 */       bool1 = "true".equalsIgnoreCase((String)this.envprops.get("com.sun.jndi.ldap.connect.pool"));
/*      */     }
/*      */ 
/* 2682 */     if (str3 == null) {
/* 2683 */       str3 = "ssl".equals(str2) ? "javax.net.ssl.SSLSocketFactory" : null;
/*      */     }
/*      */ 
/* 2687 */     if (str4 == null) {
/* 2688 */       str4 = str1 == null ? "none" : "simple";
/*      */     }
/*      */     try
/*      */     {
/* 2692 */       boolean bool2 = this.clnt == null;
/*      */       int i;
/* 2694 */       if (bool2)
/*      */       {
/* 2695 */         i = str5 != null ? Integer.parseInt(str5) : 32;
/*      */ 
/* 2698 */         this.clnt = LdapClient.getInstance(bool1, this.hostname, this.port_number, str3, this.connectTimeout, this.readTimeout, this.trace, i, str4, this.bindCtls, str2, str1, localObject1, this.envprops);
/*      */ 
/* 2727 */         if (!this.clnt.authenticateCalled());
/*      */       }
/*      */       else
/*      */       {
/* 2731 */         if ((this.sharable) && (paramBoolean)) {
/* 2732 */           return;
/*      */         }
/*      */ 
/* 2737 */         i = 3;
/*      */       }
/*      */ 
/* 2740 */       localObject2 = this.clnt.authenticate(bool2, str1, localObject1, i, str4, this.bindCtls, this.envprops);
/*      */ 
/* 2743 */       this.respCtls = ((LdapResult)localObject2).resControls;
/*      */ 
/* 2745 */       if (((LdapResult)localObject2).status != 0) {
/* 2746 */         if (bool2) {
/* 2747 */           closeConnection(true);
/*      */         }
/* 2749 */         processReturnCode((LdapResult)localObject2);
/*      */       }
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException)
/*      */     {
/*      */       Object localObject2;
/* 2753 */       if (this.handleReferrals == 2) {
/* 2754 */         throw localLdapReferralException;
/*      */       }
/*      */ 
/* 2758 */       Object localObject3 = null;
/*      */       while (true)
/*      */       {
/* 2764 */         if ((localObject2 = localLdapReferralException.getNextReferral()) == null)
/*      */         {
/* 2767 */           if (localObject3 != null) {
/* 2768 */             throw ((NamingException)localObject3.fillInStackTrace());
/*      */           }
/*      */ 
/* 2771 */           throw new NamingException("Internal error processing referral during connection");
/*      */         }
/*      */ 
/* 2777 */         LdapURL localLdapURL = new LdapURL((String)localObject2);
/* 2778 */         this.hostname = localLdapURL.getHost();
/* 2779 */         if ((this.hostname != null) && (this.hostname.charAt(0) == '[')) {
/* 2780 */           this.hostname = this.hostname.substring(1, this.hostname.length() - 1);
/*      */         }
/* 2782 */         this.port_number = localLdapURL.getPort();
/*      */         try
/*      */         {
/* 2786 */           connect(paramBoolean);
/*      */         }
/*      */         catch (NamingException localNamingException)
/*      */         {
/* 2790 */           localObject3 = localNamingException;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void closeConnection(boolean paramBoolean)
/*      */   {
/* 2798 */     removeUnsolicited();
/*      */ 
/* 2800 */     if (this.clnt != null)
/*      */     {
/* 2804 */       this.clnt.close(this.reqCtls, paramBoolean);
/* 2805 */       this.clnt = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void incEnumCount()
/*      */   {
/* 2814 */     this.enumCount += 1;
/*      */   }
/*      */ 
/*      */   synchronized void decEnumCount()
/*      */   {
/* 2819 */     this.enumCount -= 1;
/*      */ 
/* 2822 */     if ((this.enumCount == 0) && (this.closeRequested))
/*      */       try {
/* 2824 */         close();
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void processReturnCode(LdapResult paramLdapResult)
/*      */     throws NamingException
/*      */   {
/* 2835 */     processReturnCode(paramLdapResult, null, this, null, this.envprops, null);
/*      */   }
/*      */ 
/*      */   void processReturnCode(LdapResult paramLdapResult, Name paramName) throws NamingException
/*      */   {
/* 2840 */     processReturnCode(paramLdapResult, new CompositeName().add(this.currentDN), this, paramName, this.envprops, fullyQualifiedName(paramName));
/*      */   }
/*      */ 
/*      */   protected void processReturnCode(LdapResult paramLdapResult, Name paramName1, Object paramObject, Name paramName2, Hashtable paramHashtable, String paramString)
/*      */     throws NamingException
/*      */   {
/* 2852 */     String str = LdapClient.getErrorMessage(paramLdapResult.status, paramLdapResult.errorMessage);
/*      */ 
/* 2854 */     LdapReferralException localLdapReferralException1 = null;
/*      */     Object localObject;
/*      */     LimitExceededException localLimitExceededException1;
/* 2856 */     switch (paramLdapResult.status)
/*      */     {
/*      */     case 0:
/* 2861 */       if (paramLdapResult.referrals != null)
/*      */       {
/* 2863 */         str = "Unprocessed Continuation Reference(s)";
/*      */ 
/* 2865 */         if (this.handleReferrals == 3) {
/* 2866 */           localObject = new PartialResultException(str);
/* 2867 */           break;
/*      */         }
/*      */ 
/* 2871 */         int i = paramLdapResult.referrals.size();
/* 2872 */         LdapReferralException localLdapReferralException2 = null;
/* 2873 */         LdapReferralException localLdapReferralException3 = null;
/*      */ 
/* 2875 */         str = "Continuation Reference";
/*      */ 
/* 2878 */         for (int j = 0; j < i; j++)
/*      */         {
/* 2880 */           localLdapReferralException1 = new LdapReferralException(paramName1, paramObject, paramName2, str, paramHashtable, paramString, this.handleReferrals, this.reqCtls);
/*      */ 
/* 2883 */           localLdapReferralException1.setReferralInfo((Vector)paramLdapResult.referrals.elementAt(j), true);
/*      */ 
/* 2885 */           if (this.hopCount > 1) {
/* 2886 */             localLdapReferralException1.setHopCount(this.hopCount);
/*      */           }
/*      */ 
/* 2889 */           if (localLdapReferralException2 == null) {
/* 2890 */             localLdapReferralException2 = localLdapReferralException3 = localLdapReferralException1;
/*      */           } else {
/* 2892 */             localLdapReferralException3.nextReferralEx = localLdapReferralException1;
/* 2893 */             localLdapReferralException3 = localLdapReferralException1;
/*      */           }
/*      */         }
/* 2896 */         paramLdapResult.referrals = null;
/*      */ 
/* 2898 */         if (paramLdapResult.refEx == null) {
/* 2899 */           paramLdapResult.refEx = localLdapReferralException2;
/*      */         }
/*      */         else {
/* 2902 */           localLdapReferralException3 = paramLdapResult.refEx;
/*      */ 
/* 2904 */           while (localLdapReferralException3.nextReferralEx != null) {
/* 2905 */             localLdapReferralException3 = localLdapReferralException3.nextReferralEx;
/*      */           }
/* 2907 */           localLdapReferralException3.nextReferralEx = localLdapReferralException2;
/*      */         }
/*      */ 
/* 2911 */         if (this.hopCount > this.referralHopLimit) {
/* 2912 */           LimitExceededException localLimitExceededException2 = new LimitExceededException("Referral limit exceeded");
/*      */ 
/* 2914 */           localLimitExceededException2.setRootCause(localLdapReferralException1);
/* 2915 */           throw localLimitExceededException2;
/*      */         }
/*      */       }
/* 2918 */       return;
/*      */     case 10:
/* 2922 */       if (this.handleReferrals == 3) {
/* 2923 */         localObject = new PartialResultException(str);
/*      */       }
/*      */       else
/*      */       {
/* 2927 */         localLdapReferralException1 = new LdapReferralException(paramName1, paramObject, paramName2, str, paramHashtable, paramString, this.handleReferrals, this.reqCtls);
/*      */ 
/* 2930 */         localLdapReferralException1.setReferralInfo((Vector)paramLdapResult.referrals.elementAt(0), false);
/*      */ 
/* 2932 */         if (this.hopCount > 1) {
/* 2933 */           localLdapReferralException1.setHopCount(this.hopCount);
/*      */         }
/*      */ 
/* 2937 */         if (this.hopCount > this.referralHopLimit) {
/* 2938 */           localLimitExceededException1 = new LimitExceededException("Referral limit exceeded");
/*      */ 
/* 2940 */           localLimitExceededException1.setRootCause(localLdapReferralException1);
/* 2941 */           localObject = localLimitExceededException1;
/*      */         }
/*      */         else {
/* 2944 */           localObject = localLdapReferralException1;
/*      */         }
/*      */       }
/* 2946 */       break;
/*      */     case 9:
/* 2962 */       if (this.handleReferrals == 3) {
/* 2963 */         localObject = new PartialResultException(str);
/*      */       }
/*      */       else
/*      */       {
/* 2968 */         if ((paramLdapResult.errorMessage != null) && (!paramLdapResult.errorMessage.equals(""))) {
/* 2969 */           paramLdapResult.referrals = extractURLs(paramLdapResult.errorMessage);
/*      */         } else {
/* 2971 */           localObject = new PartialResultException(str);
/* 2972 */           break;
/*      */         }
/*      */ 
/* 2976 */         localLdapReferralException1 = new LdapReferralException(paramName1, paramObject, paramName2, str, paramHashtable, paramString, this.handleReferrals, this.reqCtls);
/*      */ 
/* 2985 */         if (this.hopCount > 1) {
/* 2986 */           localLdapReferralException1.setHopCount(this.hopCount);
/*      */         }
/*      */ 
/* 2998 */         if (((paramLdapResult.entries == null) || (paramLdapResult.entries.size() == 0)) && (paramLdapResult.referrals.size() == 1))
/*      */         {
/* 3001 */           localLdapReferralException1.setReferralInfo(paramLdapResult.referrals, false);
/*      */ 
/* 3004 */           if (this.hopCount > this.referralHopLimit) {
/* 3005 */             localLimitExceededException1 = new LimitExceededException("Referral limit exceeded");
/*      */ 
/* 3007 */             localLimitExceededException1.setRootCause(localLdapReferralException1);
/* 3008 */             localObject = localLimitExceededException1;
/*      */           }
/*      */           else {
/* 3011 */             localObject = localLdapReferralException1;
/*      */           }
/*      */         }
/* 3015 */         else { localLdapReferralException1.setReferralInfo(paramLdapResult.referrals, true);
/* 3016 */           paramLdapResult.refEx = localLdapReferralException1;
/*      */           return;
/*      */         }
/*      */       }
/*      */       break;
/*      */     case 34:
/*      */     case 64:
/* 3024 */       if (paramName2 != null) {
/* 3025 */         localObject = new InvalidNameException(paramName2.toString() + ": " + str);
/*      */       }
/*      */       else {
/* 3028 */         localObject = new InvalidNameException(str);
/*      */       }
/* 3030 */       break;
/*      */     default:
/* 3033 */       localObject = mapErrorCode(paramLdapResult.status, paramLdapResult.errorMessage);
/*      */     }
/*      */ 
/* 3036 */     ((NamingException)localObject).setResolvedName(paramName1);
/* 3037 */     ((NamingException)localObject).setResolvedObj(paramObject);
/* 3038 */     ((NamingException)localObject).setRemainingName(paramName2);
/* 3039 */     throw ((Throwable)localObject);
/*      */   }
/*      */ 
/*      */   public static NamingException mapErrorCode(int paramInt, String paramString)
/*      */   {
/* 3054 */     if (paramInt == 0) {
/* 3055 */       return null;
/*      */     }
/* 3057 */     Object localObject = null;
/* 3058 */     String str = LdapClient.getErrorMessage(paramInt, paramString);
/*      */ 
/* 3060 */     switch (paramInt)
/*      */     {
/*      */     case 36:
/* 3063 */       localObject = new NamingException(str);
/* 3064 */       break;
/*      */     case 33:
/* 3067 */       localObject = new NamingException(str);
/* 3068 */       break;
/*      */     case 20:
/* 3071 */       localObject = new AttributeInUseException(str);
/* 3072 */       break;
/*      */     case 7:
/*      */     case 8:
/*      */     case 13:
/*      */     case 48:
/* 3078 */       localObject = new AuthenticationNotSupportedException(str);
/* 3079 */       break;
/*      */     case 68:
/* 3082 */       localObject = new NameAlreadyBoundException(str);
/* 3083 */       break;
/*      */     case 14:
/*      */     case 49:
/* 3087 */       localObject = new AuthenticationException(str);
/* 3088 */       break;
/*      */     case 18:
/* 3091 */       localObject = new InvalidSearchFilterException(str);
/* 3092 */       break;
/*      */     case 50:
/* 3095 */       localObject = new NoPermissionException(str);
/* 3096 */       break;
/*      */     case 19:
/*      */     case 21:
/* 3100 */       localObject = new InvalidAttributeValueException(str);
/* 3101 */       break;
/*      */     case 54:
/* 3104 */       localObject = new NamingException(str);
/* 3105 */       break;
/*      */     case 16:
/* 3108 */       localObject = new NoSuchAttributeException(str);
/* 3109 */       break;
/*      */     case 32:
/* 3112 */       localObject = new NameNotFoundException(str);
/* 3113 */       break;
/*      */     case 65:
/*      */     case 67:
/*      */     case 69:
/* 3118 */       localObject = new SchemaViolationException(str);
/* 3119 */       break;
/*      */     case 66:
/* 3122 */       localObject = new ContextNotEmptyException(str);
/* 3123 */       break;
/*      */     case 1:
/* 3127 */       localObject = new NamingException(str);
/* 3128 */       break;
/*      */     case 80:
/* 3131 */       localObject = new NamingException(str);
/* 3132 */       break;
/*      */     case 2:
/* 3135 */       localObject = new CommunicationException(str);
/* 3136 */       break;
/*      */     case 4:
/* 3139 */       localObject = new SizeLimitExceededException(str);
/* 3140 */       break;
/*      */     case 3:
/* 3143 */       localObject = new TimeLimitExceededException(str);
/* 3144 */       break;
/*      */     case 12:
/* 3147 */       localObject = new OperationNotSupportedException(str);
/* 3148 */       break;
/*      */     case 51:
/*      */     case 52:
/* 3152 */       localObject = new ServiceUnavailableException(str);
/* 3153 */       break;
/*      */     case 17:
/* 3156 */       localObject = new InvalidAttributeIdentifierException(str);
/* 3157 */       break;
/*      */     case 53:
/* 3160 */       localObject = new OperationNotSupportedException(str);
/* 3161 */       break;
/*      */     case 5:
/*      */     case 6:
/*      */     case 35:
/* 3168 */       localObject = new NamingException(str);
/* 3169 */       break;
/*      */     case 11:
/* 3172 */       localObject = new LimitExceededException(str);
/* 3173 */       break;
/*      */     case 10:
/* 3176 */       localObject = new NamingException(str);
/* 3177 */       break;
/*      */     case 9:
/* 3180 */       localObject = new NamingException(str);
/* 3181 */       break;
/*      */     case 34:
/*      */     case 64:
/* 3185 */       localObject = new InvalidNameException(str);
/* 3186 */       break;
/*      */     case 15:
/*      */     case 22:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/*      */     case 28:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 37:
/*      */     case 38:
/*      */     case 39:
/*      */     case 40:
/*      */     case 41:
/*      */     case 42:
/*      */     case 43:
/*      */     case 44:
/*      */     case 45:
/*      */     case 46:
/*      */     case 47:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 58:
/*      */     case 59:
/*      */     case 60:
/*      */     case 61:
/*      */     case 62:
/*      */     case 63:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     default:
/* 3189 */       localObject = new NamingException(str);
/*      */     }
/*      */ 
/* 3193 */     return localObject;
/*      */   }
/*      */ 
/*      */   public ExtendedResponse extendedOperation(ExtendedRequest paramExtendedRequest)
/*      */     throws NamingException
/*      */   {
/* 3201 */     boolean bool = paramExtendedRequest.getID().equals("1.3.6.1.4.1.1466.20037");
/* 3202 */     ensureOpen(bool);
/*      */     Object localObject2;
/*      */     try
/*      */     {
/* 3206 */       LdapResult localLdapResult = this.clnt.extendedOp(paramExtendedRequest.getID(), paramExtendedRequest.getEncodedValue(), this.reqCtls, bool);
/*      */ 
/* 3209 */       this.respCtls = localLdapResult.resControls;
/*      */ 
/* 3211 */       if (localLdapResult.status != 0) {
/* 3212 */         processReturnCode(localLdapResult, new CompositeName());
/*      */       }
/*      */ 
/* 3216 */       int i = localLdapResult.extensionValue == null ? 0 : localLdapResult.extensionValue.length;
/*      */ 
/* 3220 */       localExtendedResponse = paramExtendedRequest.createExtendedResponse(localLdapResult.extensionId, localLdapResult.extensionValue, 0, i);
/*      */ 
/* 3224 */       if ((localExtendedResponse instanceof StartTlsResponseImpl))
/*      */       {
/* 3226 */         String str = (String)(this.envprops != null ? this.envprops.get("com.sun.jndi.ldap.domainname") : null);
/*      */ 
/* 3228 */         ((StartTlsResponseImpl)localExtendedResponse).setConnection(this.clnt.conn, str);
/*      */       }
/* 3230 */       return localExtendedResponse;
/*      */     }
/*      */     catch (LdapReferralException localLdapReferralException1)
/*      */     {
/*      */       ExtendedResponse localExtendedResponse;
/* 3234 */       if (this.handleReferrals == 2) {
/* 3235 */         throw localLdapReferralException1;
/*      */       }
/*      */ 
/*      */       while (true)
/*      */       {
/* 3240 */         localObject2 = (LdapReferralContext)localLdapReferralException1.getReferralContext(this.envprops, this.bindCtls);
/*      */         try
/*      */         {
/* 3246 */           return ((LdapReferralContext)localObject2).extendedOperation(paramExtendedRequest);
/*      */         }
/*      */         catch (LdapReferralException localLdapReferralException2) {
/* 3249 */           Object localObject1 = localLdapReferralException2;
/*      */ 
/* 3254 */           ((LdapReferralContext)localObject2).close(); } finally { ((LdapReferralContext)localObject2).close(); }
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 3259 */       localObject2 = new CommunicationException(localIOException.getMessage());
/* 3260 */       ((NamingException)localObject2).setRootCause(localIOException);
/* 3261 */     }throw ((Throwable)localObject2);
/*      */   }
/*      */ 
/*      */   public void setRequestControls(Control[] paramArrayOfControl) throws NamingException
/*      */   {
/* 3266 */     if (this.handleReferrals == 3)
/* 3267 */       this.reqCtls = addControl(paramArrayOfControl, manageReferralControl);
/*      */     else
/* 3269 */       this.reqCtls = cloneControls(paramArrayOfControl);
/*      */   }
/*      */ 
/*      */   public Control[] getRequestControls() throws NamingException
/*      */   {
/* 3274 */     return cloneControls(this.reqCtls);
/*      */   }
/*      */ 
/*      */   public Control[] getConnectControls() throws NamingException {
/* 3278 */     return cloneControls(this.bindCtls);
/*      */   }
/*      */ 
/*      */   public Control[] getResponseControls() throws NamingException {
/* 3282 */     return this.respCtls != null ? convertControls(this.respCtls) : null;
/*      */   }
/*      */ 
/*      */   Control[] convertControls(Vector paramVector)
/*      */     throws NamingException
/*      */   {
/* 3290 */     int i = paramVector.size();
/*      */ 
/* 3292 */     if (i == 0) {
/* 3293 */       return null;
/*      */     }
/*      */ 
/* 3296 */     Control[] arrayOfControl = new Control[i];
/*      */ 
/* 3298 */     for (int j = 0; j < i; j++)
/*      */     {
/* 3300 */       arrayOfControl[j] = myResponseControlFactory.getControlInstance((Control)paramVector.elementAt(j));
/*      */ 
/* 3304 */       if (arrayOfControl[j] == null) {
/* 3305 */         arrayOfControl[j] = ControlFactory.getControlInstance((Control)paramVector.elementAt(j), this, this.envprops);
/*      */       }
/*      */     }
/*      */ 
/* 3309 */     return arrayOfControl;
/*      */   }
/*      */ 
/*      */   private static Control[] addControl(Control[] paramArrayOfControl, Control paramControl) {
/* 3313 */     if (paramArrayOfControl == null) {
/* 3314 */       return new Control[] { paramControl };
/*      */     }
/*      */ 
/* 3318 */     int i = findControl(paramArrayOfControl, paramControl);
/* 3319 */     if (i != -1) {
/* 3320 */       return paramArrayOfControl;
/*      */     }
/*      */ 
/* 3323 */     Control[] arrayOfControl = new Control[paramArrayOfControl.length + 1];
/* 3324 */     System.arraycopy(paramArrayOfControl, 0, arrayOfControl, 0, paramArrayOfControl.length);
/* 3325 */     arrayOfControl[paramArrayOfControl.length] = paramControl;
/* 3326 */     return arrayOfControl;
/*      */   }
/*      */ 
/*      */   private static int findControl(Control[] paramArrayOfControl, Control paramControl) {
/* 3330 */     for (int i = 0; i < paramArrayOfControl.length; i++) {
/* 3331 */       if (paramArrayOfControl[i] == paramControl) {
/* 3332 */         return i;
/*      */       }
/*      */     }
/* 3335 */     return -1;
/*      */   }
/*      */ 
/*      */   private static Control[] removeControl(Control[] paramArrayOfControl, Control paramControl) {
/* 3339 */     if (paramArrayOfControl == null) {
/* 3340 */       return null;
/*      */     }
/*      */ 
/* 3344 */     int i = findControl(paramArrayOfControl, paramControl);
/* 3345 */     if (i == -1) {
/* 3346 */       return paramArrayOfControl;
/*      */     }
/*      */ 
/* 3350 */     Control[] arrayOfControl = new Control[paramArrayOfControl.length - 1];
/* 3351 */     System.arraycopy(paramArrayOfControl, 0, arrayOfControl, 0, i);
/* 3352 */     System.arraycopy(paramArrayOfControl, i + 1, arrayOfControl, i, paramArrayOfControl.length - i - 1);
/*      */ 
/* 3354 */     return arrayOfControl;
/*      */   }
/*      */ 
/*      */   private static Control[] cloneControls(Control[] paramArrayOfControl) {
/* 3358 */     if (paramArrayOfControl == null) {
/* 3359 */       return null;
/*      */     }
/* 3361 */     Control[] arrayOfControl = new Control[paramArrayOfControl.length];
/* 3362 */     System.arraycopy(paramArrayOfControl, 0, arrayOfControl, 0, paramArrayOfControl.length);
/* 3363 */     return arrayOfControl;
/*      */   }
/*      */ 
/*      */   public void addNamingListener(Name paramName, int paramInt, NamingListener paramNamingListener)
/*      */     throws NamingException
/*      */   {
/* 3376 */     addNamingListener(getTargetName(paramName), paramInt, paramNamingListener);
/*      */   }
/*      */ 
/*      */   public void addNamingListener(String paramString, int paramInt, NamingListener paramNamingListener) throws NamingException
/*      */   {
/* 3381 */     if (this.eventSupport == null)
/* 3382 */       this.eventSupport = new EventSupport(this);
/* 3383 */     this.eventSupport.addNamingListener(getTargetName(new CompositeName(paramString)), paramInt, paramNamingListener);
/*      */ 
/* 3387 */     if (((paramNamingListener instanceof UnsolicitedNotificationListener)) && (!this.unsolicited))
/* 3388 */       addUnsolicited();
/*      */   }
/*      */ 
/*      */   public void removeNamingListener(NamingListener paramNamingListener) throws NamingException
/*      */   {
/* 3393 */     if (this.eventSupport == null) {
/* 3394 */       return;
/*      */     }
/* 3396 */     this.eventSupport.removeNamingListener(paramNamingListener);
/*      */ 
/* 3399 */     if (((paramNamingListener instanceof UnsolicitedNotificationListener)) && (!this.eventSupport.hasUnsolicited()))
/*      */     {
/* 3401 */       removeUnsolicited();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addNamingListener(String paramString1, String paramString2, SearchControls paramSearchControls, NamingListener paramNamingListener) throws NamingException
/*      */   {
/* 3407 */     if (this.eventSupport == null)
/* 3408 */       this.eventSupport = new EventSupport(this);
/* 3409 */     this.eventSupport.addNamingListener(getTargetName(new CompositeName(paramString1)), paramString2, cloneSearchControls(paramSearchControls), paramNamingListener);
/*      */ 
/* 3413 */     if (((paramNamingListener instanceof UnsolicitedNotificationListener)) && (!this.unsolicited))
/* 3414 */       addUnsolicited();
/*      */   }
/*      */ 
/*      */   public void addNamingListener(Name paramName, String paramString, SearchControls paramSearchControls, NamingListener paramNamingListener)
/*      */     throws NamingException
/*      */   {
/* 3420 */     addNamingListener(getTargetName(paramName), paramString, paramSearchControls, paramNamingListener);
/*      */   }
/*      */ 
/*      */   public void addNamingListener(Name paramName, String paramString, Object[] paramArrayOfObject, SearchControls paramSearchControls, NamingListener paramNamingListener) throws NamingException
/*      */   {
/* 3425 */     addNamingListener(getTargetName(paramName), paramString, paramArrayOfObject, paramSearchControls, paramNamingListener);
/*      */   }
/*      */ 
/*      */   public void addNamingListener(String paramString1, String paramString2, Object[] paramArrayOfObject, SearchControls paramSearchControls, NamingListener paramNamingListener) throws NamingException
/*      */   {
/* 3430 */     String str = SearchFilter.format(paramString2, paramArrayOfObject);
/* 3431 */     addNamingListener(getTargetName(new CompositeName(paramString1)), str, paramSearchControls, paramNamingListener);
/*      */   }
/*      */ 
/*      */   public boolean targetMustExist() {
/* 3435 */     return true;
/*      */   }
/*      */ 
/*      */   private static String getTargetName(Name paramName)
/*      */     throws NamingException
/*      */   {
/* 3447 */     if ((paramName instanceof CompositeName)) {
/* 3448 */       if (paramName.size() > 1) {
/* 3449 */         throw new InvalidNameException("Target cannot span multiple namespaces: " + paramName);
/*      */       }
/* 3451 */       if (paramName.size() == 0) {
/* 3452 */         return "";
/*      */       }
/* 3454 */       return paramName.get(0);
/*      */     }
/*      */ 
/* 3458 */     return paramName.toString();
/*      */   }
/*      */ 
/*      */   private void addUnsolicited()
/*      */     throws NamingException
/*      */   {
/* 3481 */     ensureOpen();
/* 3482 */     synchronized (this.eventSupport) {
/* 3483 */       this.clnt.addUnsolicited(this);
/* 3484 */       this.unsolicited = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void removeUnsolicited()
/*      */   {
/* 3506 */     if (this.eventSupport == null) {
/* 3507 */       return;
/*      */     }
/*      */ 
/* 3511 */     synchronized (this.eventSupport) {
/* 3512 */       if ((this.unsolicited) && (this.clnt != null)) {
/* 3513 */         this.clnt.removeUnsolicited(this);
/*      */       }
/* 3515 */       this.unsolicited = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   void fireUnsolicited(Object paramObject)
/*      */   {
/* 3528 */     synchronized (this.eventSupport) {
/* 3529 */       if (this.unsolicited) {
/* 3530 */         this.eventSupport.fireUnsolicited(paramObject);
/*      */ 
/* 3532 */         if ((paramObject instanceof NamingException))
/* 3533 */           this.unsolicited = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  213 */     EMPTY_SCHEMA.setReadOnly(new SchemaViolationException("Cannot update schema object"));
/*      */   }
/*      */ 
/*      */   static final class SearchArgs
/*      */   {
/*      */     Name name;
/*      */     String filter;
/*      */     SearchControls cons;
/*      */     String[] reqAttrs;
/*      */ 
/*      */     SearchArgs(Name paramName, String paramString, SearchControls paramSearchControls, String[] paramArrayOfString)
/*      */     {
/*  104 */       this.name = paramName;
/*  105 */       this.filter = paramString;
/*  106 */       this.cons = paramSearchControls;
/*  107 */       this.reqAttrs = paramArrayOfString;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapCtx
 * JD-Core Version:    0.6.2
 */
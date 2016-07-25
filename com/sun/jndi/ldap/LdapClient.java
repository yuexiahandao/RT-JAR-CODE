/*      */ package com.sun.jndi.ldap;
/*      */ 
/*      */ import com.sun.jndi.ldap.pool.PoolCallback;
/*      */ import com.sun.jndi.ldap.pool.PooledConnection;
/*      */ import com.sun.jndi.ldap.sasl.LdapSasl;
/*      */ import com.sun.jndi.ldap.sasl.SaslInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.naming.AuthenticationException;
/*      */ import javax.naming.AuthenticationNotSupportedException;
/*      */ import javax.naming.CommunicationException;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.directory.Attribute;
/*      */ import javax.naming.directory.Attributes;
/*      */ import javax.naming.directory.BasicAttributes;
/*      */ import javax.naming.directory.InvalidAttributeValueException;
/*      */ import javax.naming.ldap.Control;
/*      */ 
/*      */ public final class LdapClient
/*      */   implements PooledConnection
/*      */ {
/*      */   private static final int debug = 0;
/*      */   static final boolean caseIgnore = true;
/*   84 */   private static final Hashtable defaultBinaryAttrs = new Hashtable(23, 0.75F);
/*      */   private static final String DISCONNECT_OID = "1.3.6.1.4.1.1466.20036";
/*      */   boolean isLdapv3;
/*  115 */   int referenceCount = 1;
/*      */   Connection conn;
/*      */   private final PoolCallback pcb;
/*      */   private final boolean pooled;
/*  122 */   private boolean authenticateCalled = false;
/*      */   static final int SCOPE_BASE_OBJECT = 0;
/*      */   static final int SCOPE_ONE_LEVEL = 1;
/*      */   static final int SCOPE_SUBTREE = 2;
/*      */   static final int ADD = 0;
/*      */   static final int DELETE = 1;
/*      */   static final int REPLACE = 2;
/*      */   static final int LDAP_VERSION3_VERSION2 = 32;
/*      */   static final int LDAP_VERSION2 = 2;
/*      */   static final int LDAP_VERSION3 = 3;
/*      */   static final int LDAP_VERSION = 3;
/*      */   static final int LDAP_REF_FOLLOW = 1;
/*      */   static final int LDAP_REF_THROW = 2;
/*      */   static final int LDAP_REF_IGNORE = 3;
/*      */   static final String LDAP_URL = "ldap://";
/*      */   static final String LDAPS_URL = "ldaps://";
/*      */   static final int LBER_BOOLEAN = 1;
/*      */   static final int LBER_INTEGER = 2;
/*      */   static final int LBER_BITSTRING = 3;
/*      */   static final int LBER_OCTETSTRING = 4;
/*      */   static final int LBER_NULL = 5;
/*      */   static final int LBER_ENUMERATED = 10;
/*      */   static final int LBER_SEQUENCE = 48;
/*      */   static final int LBER_SET = 49;
/*      */   static final int LDAP_SUPERIOR_DN = 128;
/*      */   static final int LDAP_REQ_BIND = 96;
/*      */   static final int LDAP_REQ_UNBIND = 66;
/*      */   static final int LDAP_REQ_SEARCH = 99;
/*      */   static final int LDAP_REQ_MODIFY = 102;
/*      */   static final int LDAP_REQ_ADD = 104;
/*      */   static final int LDAP_REQ_DELETE = 74;
/*      */   static final int LDAP_REQ_MODRDN = 108;
/*      */   static final int LDAP_REQ_COMPARE = 110;
/*      */   static final int LDAP_REQ_ABANDON = 80;
/*      */   static final int LDAP_REQ_EXTENSION = 119;
/*      */   static final int LDAP_REP_BIND = 97;
/*      */   static final int LDAP_REP_SEARCH = 100;
/*      */   static final int LDAP_REP_SEARCH_REF = 115;
/*      */   static final int LDAP_REP_RESULT = 101;
/*      */   static final int LDAP_REP_MODIFY = 103;
/*      */   static final int LDAP_REP_ADD = 105;
/*      */   static final int LDAP_REP_DELETE = 107;
/*      */   static final int LDAP_REP_MODRDN = 109;
/*      */   static final int LDAP_REP_COMPARE = 111;
/*      */   static final int LDAP_REP_EXTENSION = 120;
/*      */   static final int LDAP_REP_REFERRAL = 163;
/*      */   static final int LDAP_REP_EXT_OID = 138;
/*      */   static final int LDAP_REP_EXT_VAL = 139;
/*      */   static final int LDAP_CONTROLS = 160;
/*      */   static final String LDAP_CONTROL_MANAGE_DSA_IT = "2.16.840.1.113730.3.4.2";
/*      */   static final String LDAP_CONTROL_PREFERRED_LANG = "1.3.6.1.4.1.1466.20035";
/*      */   static final String LDAP_CONTROL_PAGED_RESULTS = "1.2.840.113556.1.4.319";
/*      */   static final String LDAP_CONTROL_SERVER_SORT_REQ = "1.2.840.113556.1.4.473";
/*      */   static final String LDAP_CONTROL_SERVER_SORT_RES = "1.2.840.113556.1.4.474";
/*      */   static final int LDAP_SUCCESS = 0;
/*      */   static final int LDAP_OPERATIONS_ERROR = 1;
/*      */   static final int LDAP_PROTOCOL_ERROR = 2;
/*      */   static final int LDAP_TIME_LIMIT_EXCEEDED = 3;
/*      */   static final int LDAP_SIZE_LIMIT_EXCEEDED = 4;
/*      */   static final int LDAP_COMPARE_FALSE = 5;
/*      */   static final int LDAP_COMPARE_TRUE = 6;
/*      */   static final int LDAP_AUTH_METHOD_NOT_SUPPORTED = 7;
/*      */   static final int LDAP_STRONG_AUTH_REQUIRED = 8;
/*      */   static final int LDAP_PARTIAL_RESULTS = 9;
/*      */   static final int LDAP_REFERRAL = 10;
/*      */   static final int LDAP_ADMIN_LIMIT_EXCEEDED = 11;
/*      */   static final int LDAP_UNAVAILABLE_CRITICAL_EXTENSION = 12;
/*      */   static final int LDAP_CONFIDENTIALITY_REQUIRED = 13;
/*      */   static final int LDAP_SASL_BIND_IN_PROGRESS = 14;
/*      */   static final int LDAP_NO_SUCH_ATTRIBUTE = 16;
/*      */   static final int LDAP_UNDEFINED_ATTRIBUTE_TYPE = 17;
/*      */   static final int LDAP_INAPPROPRIATE_MATCHING = 18;
/*      */   static final int LDAP_CONSTRAINT_VIOLATION = 19;
/*      */   static final int LDAP_ATTRIBUTE_OR_VALUE_EXISTS = 20;
/*      */   static final int LDAP_INVALID_ATTRIBUTE_SYNTAX = 21;
/*      */   static final int LDAP_NO_SUCH_OBJECT = 32;
/*      */   static final int LDAP_ALIAS_PROBLEM = 33;
/*      */   static final int LDAP_INVALID_DN_SYNTAX = 34;
/*      */   static final int LDAP_IS_LEAF = 35;
/*      */   static final int LDAP_ALIAS_DEREFERENCING_PROBLEM = 36;
/*      */   static final int LDAP_INAPPROPRIATE_AUTHENTICATION = 48;
/*      */   static final int LDAP_INVALID_CREDENTIALS = 49;
/*      */   static final int LDAP_INSUFFICIENT_ACCESS_RIGHTS = 50;
/*      */   static final int LDAP_BUSY = 51;
/*      */   static final int LDAP_UNAVAILABLE = 52;
/*      */   static final int LDAP_UNWILLING_TO_PERFORM = 53;
/*      */   static final int LDAP_LOOP_DETECT = 54;
/*      */   static final int LDAP_NAMING_VIOLATION = 64;
/*      */   static final int LDAP_OBJECT_CLASS_VIOLATION = 65;
/*      */   static final int LDAP_NOT_ALLOWED_ON_NON_LEAF = 66;
/*      */   static final int LDAP_NOT_ALLOWED_ON_RDN = 67;
/*      */   static final int LDAP_ENTRY_ALREADY_EXISTS = 68;
/*      */   static final int LDAP_OBJECT_CLASS_MODS_PROHIBITED = 69;
/*      */   static final int LDAP_AFFECTS_MULTIPLE_DSAS = 71;
/*      */   static final int LDAP_OTHER = 80;
/* 1331 */   static final String[] ldap_error_message = { "Success", "Operations Error", "Protocol Error", "Timelimit Exceeded", "Sizelimit Exceeded", "Compare False", "Compare True", "Authentication Method Not Supported", "Strong Authentication Required", null, "Referral", "Administrative Limit Exceeded", "Unavailable Critical Extension", "Confidentiality Required", "SASL Bind In Progress", null, "No Such Attribute", "Undefined Attribute Type", "Inappropriate Matching", "Constraint Violation", "Attribute Or Value Exists", "Invalid Attribute Syntax", null, null, null, null, null, null, null, null, null, null, "No Such Object", "Alias Problem", "Invalid DN Syntax", null, "Alias Dereferencing Problem", null, null, null, null, null, null, null, null, null, null, null, "Inappropriate Authentication", "Invalid Credentials", "Insufficient Access Rights", "Busy", "Unavailable", "Unwilling To Perform", "Loop Detect", null, null, null, null, null, null, null, null, null, "Naming Violation", "Object Class Violation", "Not Allowed On Non-leaf", "Not Allowed On RDN", "Entry Already Exists", "Object Class Modifications Prohibited", null, "Affects Multiple DSAs", null, null, null, null, null, null, null, null, "Other", null, null, null, null, null, null, null, null, null, null };
/*      */ 
/* 1485 */   private Vector unsolicited = new Vector(3);
/*      */ 
/*      */   LdapClient(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, OutputStream paramOutputStream, PoolCallback paramPoolCallback)
/*      */     throws NamingException
/*      */   {
/*  136 */     this.conn = new Connection(this, paramString1, paramInt1, paramString2, paramInt2, paramInt3, paramOutputStream);
/*      */ 
/*  139 */     this.pcb = paramPoolCallback;
/*  140 */     this.pooled = (paramPoolCallback != null);
/*      */   }
/*      */ 
/*      */   synchronized boolean authenticateCalled() {
/*  144 */     return this.authenticateCalled;
/*      */   }
/*      */ 
/*      */   synchronized LdapResult authenticate(boolean paramBoolean, String paramString1, Object paramObject, int paramInt, String paramString2, Control[] paramArrayOfControl, Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  152 */     int i = this.conn.readTimeout;
/*  153 */     this.conn.readTimeout = this.conn.connectTimeout;
/*  154 */     LdapResult localLdapResult = null;
/*      */     try {
/*  157 */       this.authenticateCalled = true;
/*      */       CommunicationException localCommunicationException1;
/*      */       try {
/*  160 */         ensureOpen();
/*      */       } catch (IOException localIOException1) {
/*  162 */         localCommunicationException1 = new CommunicationException();
/*  163 */         localCommunicationException1.setRootCause(localIOException1);
/*  164 */         throw localCommunicationException1;
/*      */       }
/*      */ 
/*  167 */       switch (paramInt) {
/*      */       case 3:
/*      */       case 32:
/*  170 */         this.isLdapv3 = true;
/*  171 */         break;
/*      */       case 2:
/*  173 */         this.isLdapv3 = false;
/*  174 */         break;
/*      */       default:
/*  176 */         throw new CommunicationException("Protocol version " + paramInt + " not supported");
/*      */       }
/*      */       CommunicationException localCommunicationException3;
/*  180 */       if ((paramString2.equalsIgnoreCase("none")) || (paramString2.equalsIgnoreCase("anonymous")))
/*      */       {
/*  185 */         if ((!paramBoolean) || (paramInt == 2) || (paramInt == 32) || ((paramArrayOfControl != null) && (paramArrayOfControl.length > 0)))
/*      */         {
/*      */           try
/*      */           {
/*  191 */             localLdapResult = ldapBind(paramString1 = null, (byte[])(paramObject = null), paramArrayOfControl, null, false);
/*      */ 
/*  193 */             if (localLdapResult.status == 0)
/*  194 */               this.conn.setBound();
/*      */           }
/*      */           catch (IOException localIOException2) {
/*  197 */             localCommunicationException1 = new CommunicationException("anonymous bind failed: " + this.conn.host + ":" + this.conn.port);
/*      */ 
/*  200 */             localCommunicationException1.setRootCause(localIOException2);
/*  201 */             throw localCommunicationException1;
/*      */           }
/*      */         }
/*      */         else {
/*  205 */           localLdapResult = new LdapResult();
/*  206 */           localLdapResult.status = 0;
/*      */         }
/*  208 */       } else if (paramString2.equalsIgnoreCase("simple"))
/*      */       {
/*  210 */         byte[] arrayOfByte = null;
/*      */         try {
/*  212 */           arrayOfByte = encodePassword(paramObject, this.isLdapv3);
/*  213 */           localLdapResult = ldapBind(paramString1, arrayOfByte, paramArrayOfControl, null, false);
/*  214 */           if (localLdapResult.status == 0) {
/*  215 */             this.conn.setBound();
/*      */           }
/*      */ 
/*  226 */           if ((arrayOfByte != paramObject) && (arrayOfByte != null))
/*  227 */             for (int j = 0; j < arrayOfByte.length; j++)
/*  228 */               arrayOfByte[j] = 0;
/*      */         }
/*      */         catch (IOException localIOException4)
/*      */         {
/*  218 */           localCommunicationException3 = new CommunicationException("simple bind failed: " + this.conn.host + ":" + this.conn.port);
/*      */ 
/*  221 */           localCommunicationException3.setRootCause(localIOException4);
/*  222 */           throw localCommunicationException3;
/*      */         }
/*      */         finally
/*      */         {
/*  226 */           if ((arrayOfByte != paramObject) && (arrayOfByte != null)) {
/*  227 */             for (int m = 0; m < arrayOfByte.length; m++)
/*  228 */               arrayOfByte[m] = 0;
/*      */           }
/*      */         }
/*      */       }
/*  232 */       else if (this.isLdapv3)
/*      */       {
/*      */         try {
/*  235 */           localLdapResult = LdapSasl.saslBind(this, this.conn, this.conn.host, paramString1, paramObject, paramString2, paramHashtable, paramArrayOfControl);
/*      */ 
/*  237 */           if (localLdapResult.status == 0)
/*  238 */             this.conn.setBound();
/*      */         }
/*      */         catch (IOException localIOException3) {
/*  241 */           CommunicationException localCommunicationException2 = new CommunicationException("SASL bind failed: " + this.conn.host + ":" + this.conn.port);
/*      */ 
/*  244 */           localCommunicationException2.setRootCause(localIOException3);
/*  245 */           throw localCommunicationException2;
/*      */         }
/*      */       } else {
/*  248 */         throw new AuthenticationNotSupportedException(paramString2);
/*      */       }
/*      */       Object localObject1;
/*  254 */       if ((paramBoolean) && (localLdapResult.status == 2) && (paramInt == 32) && ((paramString2.equalsIgnoreCase("none")) || (paramString2.equalsIgnoreCase("anonymous")) || (paramString2.equalsIgnoreCase("simple"))))
/*      */       {
/*  261 */         localObject1 = null;
/*      */         try {
/*  263 */           this.isLdapv3 = false;
/*  264 */           localObject1 = encodePassword(paramObject, false);
/*  265 */           localLdapResult = ldapBind(paramString1, (byte[])localObject1, paramArrayOfControl, null, false);
/*  266 */           if (localLdapResult.status == 0) {
/*  267 */             this.conn.setBound();
/*      */           }
/*      */ 
/*  278 */           if ((localObject1 != paramObject) && (localObject1 != null))
/*  279 */             for (int k = 0; k < localObject1.length; k++)
/*  280 */               localObject1[k] = 0;
/*      */         }
/*      */         catch (IOException localIOException5)
/*      */         {
/*  270 */           localCommunicationException3 = new CommunicationException(paramString2 + ":" + this.conn.host + ":" + this.conn.port);
/*      */ 
/*  273 */           localCommunicationException3.setRootCause(localIOException5);
/*  274 */           throw localCommunicationException3;
/*      */         }
/*      */         finally
/*      */         {
/*  278 */           if ((localObject1 != paramObject) && (localObject1 != null)) {
/*  279 */             for (int n = 0; n < localObject1.length; n++) {
/*  280 */               localObject1[n] = 0;
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  292 */       if (localLdapResult.status == 32) {
/*  293 */         throw new AuthenticationException(getErrorMessage(localLdapResult.status, localLdapResult.errorMessage));
/*      */       }
/*      */ 
/*  296 */       this.conn.setV3(this.isLdapv3);
/*  297 */       return localLdapResult;
/*      */     } finally {
/*  299 */       this.conn.readTimeout = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized LdapResult ldapBind(String paramString1, byte[] paramArrayOfByte, Control[] paramArrayOfControl, String paramString2, boolean paramBoolean)
/*      */     throws IOException, NamingException
/*      */   {
/*  315 */     ensureOpen();
/*      */ 
/*  318 */     this.conn.abandonOutstandingReqs(null);
/*      */ 
/*  320 */     BerEncoder localBerEncoder = new BerEncoder();
/*  321 */     int i = this.conn.getMsgId();
/*  322 */     LdapResult localLdapResult = new LdapResult();
/*  323 */     localLdapResult.status = 1;
/*      */ 
/*  328 */     localBerEncoder.beginSeq(48);
/*  329 */     localBerEncoder.encodeInt(i);
/*  330 */     localBerEncoder.beginSeq(96);
/*  331 */     localBerEncoder.encodeInt(this.isLdapv3 ? 3 : 2);
/*  332 */     localBerEncoder.encodeString(paramString1, this.isLdapv3);
/*      */ 
/*  335 */     if (paramString2 != null) {
/*  336 */       localBerEncoder.beginSeq(163);
/*  337 */       localBerEncoder.encodeString(paramString2, this.isLdapv3);
/*  338 */       if (paramArrayOfByte != null) {
/*  339 */         localBerEncoder.encodeOctetString(paramArrayOfByte, 4);
/*      */       }
/*      */ 
/*  342 */       localBerEncoder.endSeq();
/*      */     }
/*  344 */     else if (paramArrayOfByte != null) {
/*  345 */       localBerEncoder.encodeOctetString(paramArrayOfByte, 128);
/*      */     } else {
/*  347 */       localBerEncoder.encodeOctetString(null, 128, 0, 0);
/*      */     }
/*      */ 
/*  350 */     localBerEncoder.endSeq();
/*      */ 
/*  353 */     if (this.isLdapv3) {
/*  354 */       encodeControls(localBerEncoder, paramArrayOfControl);
/*      */     }
/*  356 */     localBerEncoder.endSeq();
/*      */ 
/*  358 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i, paramBoolean);
/*  359 */     if (paramArrayOfByte != null) {
/*  360 */       localBerEncoder.reset();
/*      */     }
/*      */ 
/*  364 */     BerDecoder localBerDecoder = this.conn.readReply(localLdapRequest);
/*      */ 
/*  366 */     localBerDecoder.parseSeq(null);
/*  367 */     localBerDecoder.parseInt();
/*  368 */     if (localBerDecoder.parseByte() != 97) {
/*  369 */       return localLdapResult;
/*      */     }
/*      */ 
/*  372 */     localBerDecoder.parseLength();
/*  373 */     parseResult(localBerDecoder, localLdapResult, this.isLdapv3);
/*      */ 
/*  376 */     if ((this.isLdapv3) && (localBerDecoder.bytesLeft() > 0) && (localBerDecoder.peekByte() == 135))
/*      */     {
/*  379 */       localLdapResult.serverCreds = localBerDecoder.parseOctetString(135, null);
/*      */     }
/*      */ 
/*  382 */     localLdapResult.resControls = (this.isLdapv3 ? parseControls(localBerDecoder) : null);
/*      */ 
/*  384 */     this.conn.removeRequest(localLdapRequest);
/*  385 */     return localLdapResult;
/*      */   }
/*      */ 
/*      */   boolean usingSaslStreams()
/*      */   {
/*  395 */     return this.conn.inStream instanceof SaslInputStream;
/*      */   }
/*      */ 
/*      */   synchronized void incRefCount() {
/*  399 */     this.referenceCount += 1;
/*      */   }
/*      */ 
/*      */   private static byte[] encodePassword(Object paramObject, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  411 */     if ((paramObject instanceof char[])) {
/*  412 */       paramObject = new String((char[])paramObject);
/*      */     }
/*      */ 
/*  415 */     if ((paramObject instanceof String)) {
/*  416 */       if (paramBoolean) {
/*  417 */         return ((String)paramObject).getBytes("UTF8");
/*      */       }
/*  419 */       return ((String)paramObject).getBytes("8859_1");
/*      */     }
/*      */ 
/*  422 */     return (byte[])paramObject;
/*      */   }
/*      */ 
/*      */   synchronized void close(Control[] paramArrayOfControl, boolean paramBoolean)
/*      */   {
/*  427 */     this.referenceCount -= 1;
/*      */ 
/*  435 */     if ((this.referenceCount <= 0) && (this.conn != null))
/*      */     {
/*  437 */       if (!this.pooled)
/*      */       {
/*  439 */         this.conn.cleanup(paramArrayOfControl, false);
/*  440 */         this.conn = null;
/*      */       }
/*  445 */       else if (paramBoolean) {
/*  446 */         this.conn.cleanup(paramArrayOfControl, false);
/*  447 */         this.conn = null;
/*  448 */         this.pcb.removePooledConnection(this);
/*      */       } else {
/*  450 */         this.pcb.releasePooledConnection(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void forceClose(boolean paramBoolean)
/*      */   {
/*  458 */     this.referenceCount = 0;
/*      */ 
/*  464 */     if (this.conn != null)
/*      */     {
/*  467 */       this.conn.cleanup(null, false);
/*  468 */       this.conn = null;
/*      */ 
/*  470 */       if (paramBoolean)
/*  471 */         this.pcb.removePooledConnection(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*  478 */     forceClose(this.pooled);
/*      */   }
/*      */ 
/*      */   public synchronized void closeConnection()
/*      */   {
/*  485 */     forceClose(false);
/*      */   }
/*      */ 
/*      */   void processConnectionClosure()
/*      */   {
/*  496 */     synchronized (this.unsolicited) {
/*  497 */       if (this.unsolicited.size() > 0)
/*      */       {
/*      */         String str;
/*  499 */         if (this.conn != null)
/*  500 */           str = this.conn.host + ":" + this.conn.port + " connection closed";
/*      */         else {
/*  502 */           str = "Connection closed";
/*      */         }
/*  504 */         notifyUnsolicited(new CommunicationException(str));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  509 */     if (this.pooled)
/*  510 */       this.pcb.removePooledConnection(this);
/*      */   }
/*      */ 
/*      */   LdapResult search(String paramString1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, String[] paramArrayOfString, String paramString2, int paramInt5, Control[] paramArrayOfControl, Hashtable paramHashtable, boolean paramBoolean2, int paramInt6)
/*      */     throws IOException, NamingException
/*      */   {
/*  531 */     ensureOpen();
/*      */ 
/*  533 */     LdapResult localLdapResult = new LdapResult();
/*      */ 
/*  535 */     BerEncoder localBerEncoder = new BerEncoder();
/*  536 */     int i = this.conn.getMsgId();
/*      */ 
/*  538 */     localBerEncoder.beginSeq(48);
/*  539 */     localBerEncoder.encodeInt(i);
/*  540 */     localBerEncoder.beginSeq(99);
/*  541 */     localBerEncoder.encodeString(paramString1 == null ? "" : paramString1, this.isLdapv3);
/*  542 */     localBerEncoder.encodeInt(paramInt1, 10);
/*  543 */     localBerEncoder.encodeInt(paramInt2, 10);
/*  544 */     localBerEncoder.encodeInt(paramInt3);
/*  545 */     localBerEncoder.encodeInt(paramInt4);
/*  546 */     localBerEncoder.encodeBoolean(paramBoolean1);
/*  547 */     Filter.encodeFilterString(localBerEncoder, paramString2, this.isLdapv3);
/*  548 */     localBerEncoder.beginSeq(48);
/*  549 */     localBerEncoder.encodeStringArray(paramArrayOfString, this.isLdapv3);
/*  550 */     localBerEncoder.endSeq();
/*  551 */     localBerEncoder.endSeq();
/*  552 */     if (this.isLdapv3) encodeControls(localBerEncoder, paramArrayOfControl);
/*  553 */     localBerEncoder.endSeq();
/*      */ 
/*  555 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i, false, paramInt6);
/*      */ 
/*  558 */     localLdapResult.msgId = i;
/*  559 */     localLdapResult.status = 0;
/*  560 */     if (paramBoolean2)
/*      */     {
/*  562 */       localLdapResult = getSearchReply(localLdapRequest, paramInt5, localLdapResult, paramHashtable);
/*      */     }
/*  564 */     return localLdapResult;
/*      */   }
/*      */ 
/*      */   void clearSearchReply(LdapResult paramLdapResult, Control[] paramArrayOfControl)
/*      */   {
/*  571 */     if ((paramLdapResult != null) && (this.conn != null))
/*      */     {
/*  575 */       LdapRequest localLdapRequest = this.conn.findRequest(paramLdapResult.msgId);
/*  576 */       if (localLdapRequest == null) {
/*  577 */         return;
/*      */       }
/*      */ 
/*  585 */       if (localLdapRequest.hasSearchCompleted())
/*  586 */         this.conn.removeRequest(localLdapRequest);
/*      */       else
/*  588 */         this.conn.abandonRequest(localLdapRequest, paramArrayOfControl);
/*      */     }
/*      */   }
/*      */ 
/*      */   LdapResult getSearchReply(int paramInt, LdapResult paramLdapResult, Hashtable paramHashtable)
/*      */     throws IOException, NamingException
/*      */   {
/*  599 */     ensureOpen();
/*      */     LdapRequest localLdapRequest;
/*  603 */     if ((localLdapRequest = this.conn.findRequest(paramLdapResult.msgId)) == null) {
/*  604 */       return null;
/*      */     }
/*      */ 
/*  607 */     return getSearchReply(localLdapRequest, paramInt, paramLdapResult, paramHashtable);
/*      */   }
/*      */ 
/*      */   private LdapResult getSearchReply(LdapRequest paramLdapRequest, int paramInt, LdapResult paramLdapResult, Hashtable paramHashtable)
/*      */     throws IOException, NamingException
/*      */   {
/*  614 */     if (paramInt == 0) {
/*  615 */       paramInt = 2147483647;
/*      */     }
/*  617 */     if (paramLdapResult.entries != null)
/*  618 */       paramLdapResult.entries.setSize(0);
/*      */     else {
/*  620 */       paramLdapResult.entries = new Vector(paramInt == 2147483647 ? 32 : paramInt);
/*      */     }
/*      */ 
/*  624 */     if (paramLdapResult.referrals != null) {
/*  625 */       paramLdapResult.referrals.setSize(0);
/*      */     }
/*      */ 
/*  638 */     for (int k = 0; k < paramInt; ) {
/*  639 */       BerDecoder localBerDecoder = this.conn.readReply(paramLdapRequest);
/*      */ 
/*  644 */       localBerDecoder.parseSeq(null);
/*  645 */       localBerDecoder.parseInt();
/*  646 */       int i = localBerDecoder.parseSeq(null);
/*      */ 
/*  648 */       if (i == 100)
/*      */       {
/*  651 */         BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*  652 */         String str = localBerDecoder.parseString(this.isLdapv3);
/*  653 */         LdapEntry localLdapEntry = new LdapEntry(str, localBasicAttributes);
/*  654 */         int[] arrayOfInt = new int[1];
/*      */ 
/*  656 */         localBerDecoder.parseSeq(arrayOfInt);
/*  657 */         int j = localBerDecoder.getParsePosition() + arrayOfInt[0];
/*  658 */         while ((localBerDecoder.getParsePosition() < j) && (localBerDecoder.bytesLeft() > 0))
/*      */         {
/*  660 */           Attribute localAttribute = parseAttribute(localBerDecoder, paramHashtable);
/*  661 */           localBasicAttributes.put(localAttribute);
/*      */         }
/*  663 */         localLdapEntry.respCtls = (this.isLdapv3 ? parseControls(localBerDecoder) : null);
/*      */ 
/*  665 */         paramLdapResult.entries.addElement(localLdapEntry);
/*  666 */         k++;
/*      */       }
/*  668 */       else if ((i == 115) && (this.isLdapv3))
/*      */       {
/*  671 */         Vector localVector = new Vector(4);
/*      */ 
/*  675 */         if (localBerDecoder.peekByte() == 48)
/*      */         {
/*  677 */           localBerDecoder.parseSeq(null);
/*      */         }
/*      */ 
/*  680 */         while ((localBerDecoder.bytesLeft() > 0) && (localBerDecoder.peekByte() == 4))
/*      */         {
/*  683 */           localVector.addElement(localBerDecoder.parseString(this.isLdapv3));
/*      */         }
/*      */ 
/*  686 */         if (paramLdapResult.referrals == null) {
/*  687 */           paramLdapResult.referrals = new Vector(4);
/*      */         }
/*  689 */         paramLdapResult.referrals.addElement(localVector);
/*  690 */         paramLdapResult.resControls = (this.isLdapv3 ? parseControls(localBerDecoder) : null);
/*      */       }
/*  694 */       else if (i == 120)
/*      */       {
/*  696 */         parseExtResponse(localBerDecoder, paramLdapResult);
/*      */       }
/*  698 */       else if (i == 101)
/*      */       {
/*  700 */         parseResult(localBerDecoder, paramLdapResult, this.isLdapv3);
/*  701 */         paramLdapResult.resControls = (this.isLdapv3 ? parseControls(localBerDecoder) : null);
/*      */ 
/*  703 */         this.conn.removeRequest(paramLdapRequest);
/*  704 */         return paramLdapResult;
/*      */       }
/*      */     }
/*      */ 
/*  708 */     return paramLdapResult;
/*      */   }
/*      */ 
/*      */   private Attribute parseAttribute(BerDecoder paramBerDecoder, Hashtable paramHashtable)
/*      */     throws IOException
/*      */   {
/*  714 */     int[] arrayOfInt = new int[1];
/*  715 */     int i = paramBerDecoder.parseSeq(null);
/*  716 */     String str = paramBerDecoder.parseString(this.isLdapv3);
/*  717 */     boolean bool = isBinaryValued(str, paramHashtable);
/*  718 */     LdapAttribute localLdapAttribute = new LdapAttribute(str);
/*      */ 
/*  720 */     if ((i = paramBerDecoder.parseSeq(arrayOfInt)) == 49) {
/*  721 */       int j = arrayOfInt[0];
/*      */       while (true) if ((paramBerDecoder.bytesLeft() > 0) && (j > 0)) {
/*      */           try {
/*  724 */             j -= parseAttributeValue(paramBerDecoder, localLdapAttribute, bool);
/*      */           } catch (IOException localIOException) {
/*  726 */             paramBerDecoder.seek(j);
/*      */           }
/*      */         }
/*      */     }
/*      */     else
/*      */     {
/*  732 */       paramBerDecoder.seek(arrayOfInt[0]);
/*      */     }
/*  734 */     return localLdapAttribute;
/*      */   }
/*      */ 
/*      */   private int parseAttributeValue(BerDecoder paramBerDecoder, Attribute paramAttribute, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  743 */     int[] arrayOfInt = new int[1];
/*      */ 
/*  745 */     if (paramBoolean)
/*  746 */       paramAttribute.add(paramBerDecoder.parseOctetString(paramBerDecoder.peekByte(), arrayOfInt));
/*      */     else {
/*  748 */       paramAttribute.add(paramBerDecoder.parseStringWithTag(4, this.isLdapv3, arrayOfInt));
/*      */     }
/*  750 */     return arrayOfInt[0];
/*      */   }
/*      */ 
/*      */   private boolean isBinaryValued(String paramString, Hashtable paramHashtable) {
/*  754 */     String str = paramString.toLowerCase();
/*      */ 
/*  756 */     return (str.indexOf(";binary") != -1) || (defaultBinaryAttrs.containsKey(str)) || ((paramHashtable != null) && (paramHashtable.containsKey(str)));
/*      */   }
/*      */ 
/*      */   static void parseResult(BerDecoder paramBerDecoder, LdapResult paramLdapResult, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  765 */     paramLdapResult.status = paramBerDecoder.parseEnumeration();
/*  766 */     paramLdapResult.matchedDN = paramBerDecoder.parseString(paramBoolean);
/*  767 */     paramLdapResult.errorMessage = paramBerDecoder.parseString(paramBoolean);
/*      */ 
/*  770 */     if ((paramBoolean) && (paramBerDecoder.bytesLeft() > 0) && (paramBerDecoder.peekByte() == 163))
/*      */     {
/*  774 */       Vector localVector = new Vector(4);
/*  775 */       int[] arrayOfInt = new int[1];
/*      */ 
/*  777 */       paramBerDecoder.parseSeq(arrayOfInt);
/*  778 */       int i = paramBerDecoder.getParsePosition() + arrayOfInt[0];
/*  779 */       while ((paramBerDecoder.getParsePosition() < i) && (paramBerDecoder.bytesLeft() > 0))
/*      */       {
/*  782 */         localVector.addElement(paramBerDecoder.parseString(paramBoolean));
/*      */       }
/*      */ 
/*  785 */       if (paramLdapResult.referrals == null) {
/*  786 */         paramLdapResult.referrals = new Vector(4);
/*      */       }
/*  788 */       paramLdapResult.referrals.addElement(localVector);
/*      */     }
/*      */   }
/*      */ 
/*      */   static Vector parseControls(BerDecoder paramBerDecoder)
/*      */     throws IOException
/*      */   {
/*  796 */     if ((paramBerDecoder.bytesLeft() > 0) && (paramBerDecoder.peekByte() == 160)) {
/*  797 */       Vector localVector = new Vector(4);
/*      */ 
/*  799 */       boolean bool = false;
/*  800 */       byte[] arrayOfByte = null;
/*  801 */       int[] arrayOfInt = new int[1];
/*      */ 
/*  803 */       paramBerDecoder.parseSeq(arrayOfInt);
/*  804 */       int i = paramBerDecoder.getParsePosition() + arrayOfInt[0];
/*  805 */       while ((paramBerDecoder.getParsePosition() < i) && (paramBerDecoder.bytesLeft() > 0))
/*      */       {
/*  808 */         paramBerDecoder.parseSeq(null);
/*  809 */         String str = paramBerDecoder.parseString(true);
/*      */ 
/*  811 */         if ((paramBerDecoder.bytesLeft() > 0) && (paramBerDecoder.peekByte() == 1))
/*      */         {
/*  813 */           bool = paramBerDecoder.parseBoolean();
/*      */         }
/*  815 */         if ((paramBerDecoder.bytesLeft() > 0) && (paramBerDecoder.peekByte() == 4))
/*      */         {
/*  817 */           arrayOfByte = paramBerDecoder.parseOctetString(4, null);
/*      */         }
/*      */ 
/*  820 */         if (str != null) {
/*  821 */           localVector.addElement(new BasicControl(str, bool, arrayOfByte));
/*      */         }
/*      */       }
/*      */ 
/*  825 */       return localVector;
/*      */     }
/*  827 */     return null;
/*      */   }
/*      */ 
/*      */   private void parseExtResponse(BerDecoder paramBerDecoder, LdapResult paramLdapResult)
/*      */     throws IOException
/*      */   {
/*  834 */     parseResult(paramBerDecoder, paramLdapResult, this.isLdapv3);
/*      */ 
/*  836 */     if ((paramBerDecoder.bytesLeft() > 0) && (paramBerDecoder.peekByte() == 138))
/*      */     {
/*  838 */       paramLdapResult.extensionId = paramBerDecoder.parseStringWithTag(138, this.isLdapv3, null);
/*      */     }
/*      */ 
/*  841 */     if ((paramBerDecoder.bytesLeft() > 0) && (paramBerDecoder.peekByte() == 139))
/*      */     {
/*  843 */       paramLdapResult.extensionValue = paramBerDecoder.parseOctetString(139, null);
/*      */     }
/*      */ 
/*  847 */     paramLdapResult.resControls = parseControls(paramBerDecoder);
/*      */   }
/*      */ 
/*      */   static void encodeControls(BerEncoder paramBerEncoder, Control[] paramArrayOfControl)
/*      */     throws IOException
/*      */   {
/*  856 */     if ((paramArrayOfControl == null) || (paramArrayOfControl.length == 0)) {
/*  857 */       return;
/*      */     }
/*      */ 
/*  862 */     paramBerEncoder.beginSeq(160);
/*      */ 
/*  864 */     for (int i = 0; i < paramArrayOfControl.length; i++) {
/*  865 */       paramBerEncoder.beginSeq(48);
/*  866 */       paramBerEncoder.encodeString(paramArrayOfControl[i].getID(), true);
/*  867 */       if (paramArrayOfControl[i].isCritical())
/*  868 */         paramBerEncoder.encodeBoolean(true);
/*      */       byte[] arrayOfByte;
/*  870 */       if ((arrayOfByte = paramArrayOfControl[i].getEncodedValue()) != null) {
/*  871 */         paramBerEncoder.encodeOctetString(arrayOfByte, 4);
/*      */       }
/*  873 */       paramBerEncoder.endSeq();
/*      */     }
/*  875 */     paramBerEncoder.endSeq();
/*      */   }
/*      */ 
/*      */   private LdapResult processReply(LdapRequest paramLdapRequest, LdapResult paramLdapResult, int paramInt)
/*      */     throws IOException, NamingException
/*      */   {
/*  885 */     BerDecoder localBerDecoder = this.conn.readReply(paramLdapRequest);
/*      */ 
/*  887 */     localBerDecoder.parseSeq(null);
/*  888 */     localBerDecoder.parseInt();
/*  889 */     if (localBerDecoder.parseByte() != paramInt) {
/*  890 */       return paramLdapResult;
/*      */     }
/*      */ 
/*  893 */     localBerDecoder.parseLength();
/*  894 */     parseResult(localBerDecoder, paramLdapResult, this.isLdapv3);
/*  895 */     paramLdapResult.resControls = (this.isLdapv3 ? parseControls(localBerDecoder) : null);
/*      */ 
/*  897 */     this.conn.removeRequest(paramLdapRequest);
/*      */ 
/*  899 */     return paramLdapResult;
/*      */   }
/*      */ 
/*      */   LdapResult modify(String paramString, int[] paramArrayOfInt, Attribute[] paramArrayOfAttribute, Control[] paramArrayOfControl)
/*      */     throws IOException, NamingException
/*      */   {
/*  923 */     ensureOpen();
/*      */ 
/*  925 */     LdapResult localLdapResult = new LdapResult();
/*  926 */     localLdapResult.status = 1;
/*      */ 
/*  928 */     if ((paramString == null) || (paramArrayOfInt.length != paramArrayOfAttribute.length)) {
/*  929 */       return localLdapResult;
/*      */     }
/*  931 */     BerEncoder localBerEncoder = new BerEncoder();
/*  932 */     int i = this.conn.getMsgId();
/*      */ 
/*  934 */     localBerEncoder.beginSeq(48);
/*  935 */     localBerEncoder.encodeInt(i);
/*  936 */     localBerEncoder.beginSeq(102);
/*  937 */     localBerEncoder.encodeString(paramString, this.isLdapv3);
/*  938 */     localBerEncoder.beginSeq(48);
/*  939 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/*  940 */       localBerEncoder.beginSeq(48);
/*  941 */       localBerEncoder.encodeInt(paramArrayOfInt[j], 10);
/*      */ 
/*  944 */       if ((paramArrayOfInt[j] == 0) && (hasNoValue(paramArrayOfAttribute[j]))) {
/*  945 */         throw new InvalidAttributeValueException("'" + paramArrayOfAttribute[j].getID() + "' has no values.");
/*      */       }
/*      */ 
/*  948 */       encodeAttribute(localBerEncoder, paramArrayOfAttribute[j]);
/*      */ 
/*  950 */       localBerEncoder.endSeq();
/*      */     }
/*  952 */     localBerEncoder.endSeq();
/*  953 */     localBerEncoder.endSeq();
/*  954 */     if (this.isLdapv3) encodeControls(localBerEncoder, paramArrayOfControl);
/*  955 */     localBerEncoder.endSeq();
/*      */ 
/*  957 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i);
/*      */ 
/*  959 */     return processReply(localLdapRequest, localLdapResult, 103);
/*      */   }
/*      */ 
/*      */   private void encodeAttribute(BerEncoder paramBerEncoder, Attribute paramAttribute)
/*      */     throws IOException, NamingException
/*      */   {
/*  965 */     paramBerEncoder.beginSeq(48);
/*  966 */     paramBerEncoder.encodeString(paramAttribute.getID(), this.isLdapv3);
/*  967 */     paramBerEncoder.beginSeq(49);
/*  968 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*      */ 
/*  970 */     while (localNamingEnumeration.hasMore()) {
/*  971 */       Object localObject = localNamingEnumeration.next();
/*  972 */       if ((localObject instanceof String))
/*  973 */         paramBerEncoder.encodeString((String)localObject, this.isLdapv3);
/*  974 */       else if ((localObject instanceof byte[]))
/*  975 */         paramBerEncoder.encodeOctetString((byte[])localObject, 4);
/*  976 */       else if (localObject != null)
/*      */       {
/*  979 */         throw new InvalidAttributeValueException("Malformed '" + paramAttribute.getID() + "' attribute value");
/*      */       }
/*      */     }
/*      */ 
/*  983 */     paramBerEncoder.endSeq();
/*  984 */     paramBerEncoder.endSeq();
/*      */   }
/*      */ 
/*      */   private static boolean hasNoValue(Attribute paramAttribute) throws NamingException {
/*  988 */     return (paramAttribute.size() == 0) || ((paramAttribute.size() == 1) && (paramAttribute.get() == null));
/*      */   }
/*      */ 
/*      */   LdapResult add(LdapEntry paramLdapEntry, Control[] paramArrayOfControl)
/*      */     throws IOException, NamingException
/*      */   {
/* 1001 */     ensureOpen();
/*      */ 
/* 1003 */     LdapResult localLdapResult = new LdapResult();
/* 1004 */     localLdapResult.status = 1;
/*      */ 
/* 1006 */     if ((paramLdapEntry == null) || (paramLdapEntry.DN == null)) {
/* 1007 */       return localLdapResult;
/*      */     }
/* 1009 */     BerEncoder localBerEncoder = new BerEncoder();
/* 1010 */     int i = this.conn.getMsgId();
/*      */ 
/* 1013 */     localBerEncoder.beginSeq(48);
/* 1014 */     localBerEncoder.encodeInt(i);
/* 1015 */     localBerEncoder.beginSeq(104);
/* 1016 */     localBerEncoder.encodeString(paramLdapEntry.DN, this.isLdapv3);
/* 1017 */     localBerEncoder.beginSeq(48);
/* 1018 */     NamingEnumeration localNamingEnumeration = paramLdapEntry.attributes.getAll();
/* 1019 */     while (localNamingEnumeration.hasMore()) {
/* 1020 */       Attribute localAttribute = (Attribute)localNamingEnumeration.next();
/*      */ 
/* 1023 */       if (hasNoValue(localAttribute)) {
/* 1024 */         throw new InvalidAttributeValueException("'" + localAttribute.getID() + "' has no values.");
/*      */       }
/*      */ 
/* 1027 */       encodeAttribute(localBerEncoder, localAttribute);
/*      */     }
/*      */ 
/* 1030 */     localBerEncoder.endSeq();
/* 1031 */     localBerEncoder.endSeq();
/* 1032 */     if (this.isLdapv3) encodeControls(localBerEncoder, paramArrayOfControl);
/* 1033 */     localBerEncoder.endSeq();
/*      */ 
/* 1035 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i);
/* 1036 */     return processReply(localLdapRequest, localLdapResult, 105);
/*      */   }
/*      */ 
/*      */   LdapResult delete(String paramString, Control[] paramArrayOfControl)
/*      */     throws IOException, NamingException
/*      */   {
/* 1049 */     ensureOpen();
/*      */ 
/* 1051 */     LdapResult localLdapResult = new LdapResult();
/* 1052 */     localLdapResult.status = 1;
/*      */ 
/* 1054 */     if (paramString == null) {
/* 1055 */       return localLdapResult;
/*      */     }
/* 1057 */     BerEncoder localBerEncoder = new BerEncoder();
/* 1058 */     int i = this.conn.getMsgId();
/*      */ 
/* 1060 */     localBerEncoder.beginSeq(48);
/* 1061 */     localBerEncoder.encodeInt(i);
/* 1062 */     localBerEncoder.encodeString(paramString, 74, this.isLdapv3);
/* 1063 */     if (this.isLdapv3) encodeControls(localBerEncoder, paramArrayOfControl);
/* 1064 */     localBerEncoder.endSeq();
/*      */ 
/* 1066 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i);
/*      */ 
/* 1068 */     return processReply(localLdapRequest, localLdapResult, 107);
/*      */   }
/*      */ 
/*      */   LdapResult moddn(String paramString1, String paramString2, boolean paramBoolean, String paramString3, Control[] paramArrayOfControl)
/*      */     throws IOException, NamingException
/*      */   {
/* 1088 */     ensureOpen();
/*      */ 
/* 1090 */     int i = (paramString3 != null) && (paramString3.length() > 0) ? 1 : 0;
/*      */ 
/* 1093 */     LdapResult localLdapResult = new LdapResult();
/* 1094 */     localLdapResult.status = 1;
/*      */ 
/* 1096 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 1097 */       return localLdapResult;
/*      */     }
/* 1099 */     BerEncoder localBerEncoder = new BerEncoder();
/* 1100 */     int j = this.conn.getMsgId();
/*      */ 
/* 1102 */     localBerEncoder.beginSeq(48);
/* 1103 */     localBerEncoder.encodeInt(j);
/* 1104 */     localBerEncoder.beginSeq(108);
/* 1105 */     localBerEncoder.encodeString(paramString1, this.isLdapv3);
/* 1106 */     localBerEncoder.encodeString(paramString2, this.isLdapv3);
/* 1107 */     localBerEncoder.encodeBoolean(paramBoolean);
/* 1108 */     if ((this.isLdapv3) && (i != 0))
/*      */     {
/* 1110 */       localBerEncoder.encodeString(paramString3, 128, this.isLdapv3);
/*      */     }
/* 1112 */     localBerEncoder.endSeq();
/* 1113 */     if (this.isLdapv3) encodeControls(localBerEncoder, paramArrayOfControl);
/* 1114 */     localBerEncoder.endSeq();
/*      */ 
/* 1117 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, j);
/*      */ 
/* 1119 */     return processReply(localLdapRequest, localLdapResult, 109);
/*      */   }
/*      */ 
/*      */   LdapResult compare(String paramString1, String paramString2, String paramString3, Control[] paramArrayOfControl)
/*      */     throws IOException, NamingException
/*      */   {
/* 1132 */     ensureOpen();
/*      */ 
/* 1134 */     LdapResult localLdapResult = new LdapResult();
/* 1135 */     localLdapResult.status = 1;
/*      */ 
/* 1137 */     if ((paramString1 == null) || (paramString2 == null) || (paramString3 == null)) {
/* 1138 */       return localLdapResult;
/*      */     }
/* 1140 */     BerEncoder localBerEncoder = new BerEncoder();
/* 1141 */     int i = this.conn.getMsgId();
/*      */ 
/* 1143 */     localBerEncoder.beginSeq(48);
/* 1144 */     localBerEncoder.encodeInt(i);
/* 1145 */     localBerEncoder.beginSeq(110);
/* 1146 */     localBerEncoder.encodeString(paramString1, this.isLdapv3);
/* 1147 */     localBerEncoder.beginSeq(48);
/* 1148 */     localBerEncoder.encodeString(paramString2, this.isLdapv3);
/*      */ 
/* 1151 */     byte[] arrayOfByte = this.isLdapv3 ? paramString3.getBytes("UTF8") : paramString3.getBytes("8859_1");
/*      */ 
/* 1153 */     localBerEncoder.encodeOctetString(Filter.unescapeFilterValue(arrayOfByte, 0, arrayOfByte.length), 4);
/*      */ 
/* 1157 */     localBerEncoder.endSeq();
/* 1158 */     localBerEncoder.endSeq();
/* 1159 */     if (this.isLdapv3) encodeControls(localBerEncoder, paramArrayOfControl);
/* 1160 */     localBerEncoder.endSeq();
/*      */ 
/* 1162 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i);
/*      */ 
/* 1164 */     return processReply(localLdapRequest, localLdapResult, 111);
/*      */   }
/*      */ 
/*      */   LdapResult extendedOp(String paramString, byte[] paramArrayOfByte, Control[] paramArrayOfControl, boolean paramBoolean)
/*      */     throws IOException, NamingException
/*      */   {
/* 1176 */     ensureOpen();
/*      */ 
/* 1178 */     LdapResult localLdapResult = new LdapResult();
/* 1179 */     localLdapResult.status = 1;
/*      */ 
/* 1181 */     if (paramString == null) {
/* 1182 */       return localLdapResult;
/*      */     }
/* 1184 */     BerEncoder localBerEncoder = new BerEncoder();
/* 1185 */     int i = this.conn.getMsgId();
/*      */ 
/* 1187 */     localBerEncoder.beginSeq(48);
/* 1188 */     localBerEncoder.encodeInt(i);
/* 1189 */     localBerEncoder.beginSeq(119);
/* 1190 */     localBerEncoder.encodeString(paramString, 128, this.isLdapv3);
/*      */ 
/* 1192 */     if (paramArrayOfByte != null) {
/* 1193 */       localBerEncoder.encodeOctetString(paramArrayOfByte, 129);
/*      */     }
/*      */ 
/* 1196 */     localBerEncoder.endSeq();
/* 1197 */     encodeControls(localBerEncoder, paramArrayOfControl);
/* 1198 */     localBerEncoder.endSeq();
/*      */ 
/* 1200 */     LdapRequest localLdapRequest = this.conn.writeRequest(localBerEncoder, i, paramBoolean);
/*      */ 
/* 1202 */     BerDecoder localBerDecoder = this.conn.readReply(localLdapRequest);
/*      */ 
/* 1204 */     localBerDecoder.parseSeq(null);
/* 1205 */     localBerDecoder.parseInt();
/* 1206 */     if (localBerDecoder.parseByte() != 120) {
/* 1207 */       return localLdapResult;
/*      */     }
/*      */ 
/* 1210 */     localBerDecoder.parseLength();
/* 1211 */     parseExtResponse(localBerDecoder, localLdapResult);
/* 1212 */     this.conn.removeRequest(localLdapRequest);
/*      */ 
/* 1214 */     return localLdapResult;
/*      */   }
/*      */ 
/*      */   static String getErrorMessage(int paramInt, String paramString)
/*      */   {
/* 1438 */     String str = "[LDAP: error code " + paramInt;
/*      */ 
/* 1440 */     if ((paramString != null) && (paramString.length() != 0))
/*      */     {
/* 1443 */       str = str + " - " + paramString + "]";
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/* 1449 */         if (ldap_error_message[paramInt] != null)
/* 1450 */           str = str + " - " + ldap_error_message[paramInt] + "]";
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*      */       {
/* 1454 */         str = str + "]";
/*      */       }
/*      */     }
/* 1457 */     return str;
/*      */   }
/*      */ 
/*      */   void addUnsolicited(LdapCtx paramLdapCtx)
/*      */   {
/* 1490 */     this.unsolicited.addElement(paramLdapCtx);
/*      */   }
/*      */ 
/*      */   void removeUnsolicited(LdapCtx paramLdapCtx)
/*      */   {
/* 1497 */     synchronized (this.unsolicited) {
/* 1498 */       if (this.unsolicited.size() == 0) {
/* 1499 */         return;
/*      */       }
/* 1501 */       this.unsolicited.removeElement(paramLdapCtx);
/*      */     }
/*      */   }
/*      */ 
/*      */   void processUnsolicited(BerDecoder paramBerDecoder)
/*      */   {
/* 1511 */     synchronized (this.unsolicited)
/*      */     {
/*      */       try {
/* 1514 */         LdapResult localLdapResult = new LdapResult();
/*      */ 
/* 1516 */         paramBerDecoder.parseSeq(null);
/* 1517 */         paramBerDecoder.parseInt();
/* 1518 */         if (paramBerDecoder.parseByte() != 120) {
/* 1519 */           throw new IOException("Unsolicited Notification must be an Extended Response");
/*      */         }
/*      */ 
/* 1522 */         paramBerDecoder.parseLength();
/* 1523 */         parseExtResponse(paramBerDecoder, localLdapResult);
/*      */ 
/* 1525 */         if ("1.3.6.1.4.1.1466.20036".equals(localLdapResult.extensionId))
/*      */         {
/* 1527 */           forceClose(this.pooled);
/*      */         }
/*      */ 
/* 1530 */         if (this.unsolicited.size() > 0)
/*      */         {
/* 1534 */           localObject1 = new UnsolicitedResponseImpl(localLdapResult.extensionId, localLdapResult.extensionValue, localLdapResult.referrals, localLdapResult.status, localLdapResult.errorMessage, localLdapResult.matchedDN, localLdapResult.resControls != null ? ((LdapCtx)this.unsolicited.elementAt(0)).convertControls(localLdapResult.resControls) : null);
/*      */ 
/* 1546 */           notifyUnsolicited(localObject1);
/*      */ 
/* 1550 */           if ("1.3.6.1.4.1.1466.20036".equals(localLdapResult.extensionId))
/* 1551 */             notifyUnsolicited(new CommunicationException("Connection closed"));
/*      */         }
/*      */       }
/*      */       catch (IOException localIOException)
/*      */       {
/* 1556 */         if (this.unsolicited.size() == 0) {
/* 1557 */           return;
/*      */         }
/* 1559 */         Object localObject1 = new CommunicationException("Problem parsing unsolicited notification");
/*      */ 
/* 1561 */         ((NamingException)localObject1).setRootCause(localIOException);
/*      */ 
/* 1563 */         notifyUnsolicited(localObject1);
/*      */       }
/*      */       catch (NamingException localNamingException) {
/* 1566 */         notifyUnsolicited(localNamingException);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void notifyUnsolicited(Object paramObject)
/*      */   {
/* 1573 */     for (int i = 0; i < this.unsolicited.size(); i++) {
/* 1574 */       ((LdapCtx)this.unsolicited.elementAt(i)).fireUnsolicited(paramObject);
/*      */     }
/* 1576 */     if ((paramObject instanceof NamingException))
/* 1577 */       this.unsolicited.setSize(0);
/*      */   }
/*      */ 
/*      */   private void ensureOpen() throws IOException
/*      */   {
/* 1582 */     if ((this.conn == null) || (!this.conn.useable)) {
/* 1583 */       if ((this.conn != null) && (this.conn.closureReason != null)) {
/* 1584 */         throw this.conn.closureReason;
/*      */       }
/* 1586 */       throw new IOException("connection closed");
/*      */     }
/*      */   }
/*      */ 
/*      */   static LdapClient getInstance(boolean paramBoolean, String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, OutputStream paramOutputStream, int paramInt4, String paramString3, Control[] paramArrayOfControl, String paramString4, String paramString5, Object paramObject, Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/* 1597 */     if ((paramBoolean) && 
/* 1598 */       (LdapPoolManager.isPoolingAllowed(paramString2, paramOutputStream, paramString3, paramString4, paramHashtable)))
/*      */     {
/* 1600 */       LdapClient localLdapClient = LdapPoolManager.getLdapClient(paramString1, paramInt1, paramString2, paramInt2, paramInt3, paramOutputStream, paramInt4, paramString3, paramArrayOfControl, paramString4, paramString5, paramObject, paramHashtable);
/*      */ 
/* 1604 */       localLdapClient.referenceCount = 1;
/* 1605 */       return localLdapClient;
/*      */     }
/*      */ 
/* 1608 */     return new LdapClient(paramString1, paramInt1, paramString2, paramInt2, paramInt3, paramOutputStream, null);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   86 */     defaultBinaryAttrs.put("userpassword", Boolean.TRUE);
/*   87 */     defaultBinaryAttrs.put("javaserializeddata", Boolean.TRUE);
/*      */ 
/*   89 */     defaultBinaryAttrs.put("javaserializedobject", Boolean.TRUE);
/*      */ 
/*   91 */     defaultBinaryAttrs.put("jpegphoto", Boolean.TRUE);
/*      */ 
/*   93 */     defaultBinaryAttrs.put("audio", Boolean.TRUE);
/*   94 */     defaultBinaryAttrs.put("thumbnailphoto", Boolean.TRUE);
/*      */ 
/*   96 */     defaultBinaryAttrs.put("thumbnaillogo", Boolean.TRUE);
/*      */ 
/*   98 */     defaultBinaryAttrs.put("usercertificate", Boolean.TRUE);
/*   99 */     defaultBinaryAttrs.put("cacertificate", Boolean.TRUE);
/*  100 */     defaultBinaryAttrs.put("certificaterevocationlist", Boolean.TRUE);
/*      */ 
/*  102 */     defaultBinaryAttrs.put("authorityrevocationlist", Boolean.TRUE);
/*  103 */     defaultBinaryAttrs.put("crosscertificatepair", Boolean.TRUE);
/*  104 */     defaultBinaryAttrs.put("photo", Boolean.TRUE);
/*  105 */     defaultBinaryAttrs.put("personalsignature", Boolean.TRUE);
/*      */ 
/*  107 */     defaultBinaryAttrs.put("x500uniqueidentifier", Boolean.TRUE);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapClient
 * JD-Core Version:    0.6.2
 */
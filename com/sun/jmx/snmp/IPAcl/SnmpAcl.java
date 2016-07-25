/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.snmp.InetAddressAcl;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Principal;
/*     */ import java.security.acl.AclEntry;
/*     */ import java.security.acl.NotOwnerException;
/*     */ import java.security.acl.Permission;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SnmpAcl
/*     */   implements InetAddressAcl, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6702287103824397063L;
/*  68 */   static final PermissionImpl READ = new PermissionImpl("READ");
/*  69 */   static final PermissionImpl WRITE = new PermissionImpl("WRITE");
/*     */ 
/* 466 */   private AclImpl acl = null;
/*     */ 
/* 471 */   private boolean alwaysAuthorized = false;
/*     */ 
/* 475 */   private String authorizedListFile = null;
/*     */ 
/* 479 */   private Hashtable<InetAddress, Vector<String>> trapDestList = null;
/*     */ 
/* 483 */   private Hashtable<InetAddress, Vector<String>> informDestList = null;
/*     */ 
/* 485 */   private PrincipalImpl owner = null;
/*     */ 
/*     */   public SnmpAcl(String paramString)
/*     */     throws UnknownHostException, IllegalArgumentException
/*     */   {
/*  83 */     this(paramString, null);
/*     */   }
/*     */ 
/*     */   public SnmpAcl(String paramString1, String paramString2)
/*     */     throws UnknownHostException, IllegalArgumentException
/*     */   {
/*  99 */     this.trapDestList = new Hashtable();
/* 100 */     this.informDestList = new Hashtable();
/*     */ 
/* 103 */     this.owner = new PrincipalImpl();
/*     */     try {
/* 105 */       this.acl = new AclImpl(this.owner, paramString1);
/* 106 */       AclEntryImpl localAclEntryImpl = new AclEntryImpl(this.owner);
/* 107 */       localAclEntryImpl.addPermission(READ);
/* 108 */       localAclEntryImpl.addPermission(WRITE);
/* 109 */       this.acl.addEntry(this.owner, localAclEntryImpl);
/*     */     } catch (NotOwnerException localNotOwnerException) {
/* 111 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 112 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpAcl.class.getName(), "SnmpAcl(String,String)", "Should never get NotOwnerException as the owner is built in this constructor");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 118 */     if (paramString2 == null) setDefaultFileName(); else
/* 119 */       setAuthorizedListFile(paramString2);
/* 120 */     readAuthorizedListFile();
/*     */   }
/*     */ 
/*     */   public Enumeration entries()
/*     */   {
/* 130 */     return this.acl.entries();
/*     */   }
/*     */ 
/*     */   public Enumeration<String> communities()
/*     */   {
/* 138 */     HashSet localHashSet = new HashSet();
/* 139 */     Vector localVector = new Vector();
/* 140 */     for (Object localObject = this.acl.entries(); ((Enumeration)localObject).hasMoreElements(); ) {
/* 141 */       AclEntryImpl localAclEntryImpl = (AclEntryImpl)((Enumeration)localObject).nextElement();
/* 142 */       Enumeration localEnumeration = localAclEntryImpl.communities();
/* 143 */       while (localEnumeration.hasMoreElements()) {
/* 144 */         localHashSet.add((String)localEnumeration.nextElement());
/*     */       }
/*     */     }
/* 147 */     localObject = (String[])localHashSet.toArray(new String[0]);
/* 148 */     for (int i = 0; i < localObject.length; i++) {
/* 149 */       localVector.addElement(localObject[i]);
/*     */     }
/* 151 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 160 */     return this.acl.getName();
/*     */   }
/*     */ 
/*     */   public static PermissionImpl getREAD()
/*     */   {
/* 169 */     return READ;
/*     */   }
/*     */ 
/*     */   public static PermissionImpl getWRITE()
/*     */   {
/* 178 */     return WRITE;
/*     */   }
/*     */ 
/*     */   public static String getDefaultAclFileName()
/*     */   {
/* 187 */     String str = System.getProperty("file.separator");
/*     */ 
/* 189 */     StringBuffer localStringBuffer = new StringBuffer(System.getProperty("java.home")).append(str).append("lib").append(str).append("snmp.acl");
/*     */ 
/* 193 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void setAuthorizedListFile(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 204 */     File localFile = new File(paramString);
/* 205 */     if (!localFile.isFile()) {
/* 206 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 207 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpAcl.class.getName(), "setAuthorizedListFile", "ACL file not found: " + paramString);
/*     */       }
/*     */ 
/* 210 */       throw new IllegalArgumentException("The specified file [" + localFile + "] " + "doesn't exist or is not a file, " + "no configuration loaded");
/*     */     }
/*     */ 
/* 215 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 216 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "setAuthorizedListFile", "Default file set to " + paramString);
/*     */     }
/*     */ 
/* 219 */     this.authorizedListFile = paramString;
/*     */   }
/*     */ 
/*     */   public void rereadTheFile()
/*     */     throws NotOwnerException, UnknownHostException
/*     */   {
/* 229 */     this.alwaysAuthorized = false;
/* 230 */     this.acl.removeAll(this.owner);
/* 231 */     this.trapDestList.clear();
/* 232 */     this.informDestList.clear();
/* 233 */     AclEntryImpl localAclEntryImpl = new AclEntryImpl(this.owner);
/* 234 */     localAclEntryImpl.addPermission(READ);
/* 235 */     localAclEntryImpl.addPermission(WRITE);
/* 236 */     this.acl.addEntry(this.owner, localAclEntryImpl);
/* 237 */     readAuthorizedListFile();
/*     */   }
/*     */ 
/*     */   public String getAuthorizedListFile()
/*     */   {
/* 246 */     return this.authorizedListFile;
/*     */   }
/*     */ 
/*     */   public boolean checkReadPermission(InetAddress paramInetAddress)
/*     */   {
/* 257 */     if (this.alwaysAuthorized) return true;
/* 258 */     PrincipalImpl localPrincipalImpl = new PrincipalImpl(paramInetAddress);
/* 259 */     return this.acl.checkPermission(localPrincipalImpl, READ);
/*     */   }
/*     */ 
/*     */   public boolean checkReadPermission(InetAddress paramInetAddress, String paramString)
/*     */   {
/* 271 */     if (this.alwaysAuthorized) return true;
/* 272 */     PrincipalImpl localPrincipalImpl = new PrincipalImpl(paramInetAddress);
/* 273 */     return this.acl.checkPermission(localPrincipalImpl, paramString, READ);
/*     */   }
/*     */ 
/*     */   public boolean checkCommunity(String paramString)
/*     */   {
/* 284 */     return this.acl.checkCommunity(paramString);
/*     */   }
/*     */ 
/*     */   public boolean checkWritePermission(InetAddress paramInetAddress)
/*     */   {
/* 295 */     if (this.alwaysAuthorized) return true;
/* 296 */     PrincipalImpl localPrincipalImpl = new PrincipalImpl(paramInetAddress);
/* 297 */     return this.acl.checkPermission(localPrincipalImpl, WRITE);
/*     */   }
/*     */ 
/*     */   public boolean checkWritePermission(InetAddress paramInetAddress, String paramString)
/*     */   {
/* 309 */     if (this.alwaysAuthorized) return true;
/* 310 */     PrincipalImpl localPrincipalImpl = new PrincipalImpl(paramInetAddress);
/* 311 */     return this.acl.checkPermission(localPrincipalImpl, paramString, WRITE);
/*     */   }
/*     */ 
/*     */   public Enumeration getTrapDestinations()
/*     */   {
/* 320 */     return this.trapDestList.keys();
/*     */   }
/*     */ 
/*     */   public Enumeration getTrapCommunities(InetAddress paramInetAddress)
/*     */   {
/* 331 */     Vector localVector = null;
/* 332 */     if ((localVector = (Vector)this.trapDestList.get(paramInetAddress)) != null) {
/* 333 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 334 */         JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "getTrapCommunities", "[" + paramInetAddress.toString() + "] is in list");
/*     */       }
/*     */ 
/* 337 */       return localVector.elements();
/*     */     }
/* 339 */     localVector = new Vector();
/* 340 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 341 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "getTrapCommunities", "[" + paramInetAddress.toString() + "] is not in list");
/*     */     }
/*     */ 
/* 344 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   public Enumeration getInformDestinations()
/*     */   {
/* 354 */     return this.informDestList.keys();
/*     */   }
/*     */ 
/*     */   public Enumeration getInformCommunities(InetAddress paramInetAddress)
/*     */   {
/* 365 */     Vector localVector = null;
/* 366 */     if ((localVector = (Vector)this.informDestList.get(paramInetAddress)) != null) {
/* 367 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 368 */         JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "getInformCommunities", "[" + paramInetAddress.toString() + "] is in list");
/*     */       }
/*     */ 
/* 371 */       return localVector.elements();
/*     */     }
/* 373 */     localVector = new Vector();
/* 374 */     if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 375 */       JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "getInformCommunities", "[" + paramInetAddress.toString() + "] is not in list");
/*     */     }
/*     */ 
/* 378 */     return localVector.elements();
/*     */   }
/*     */ 
/*     */   private void readAuthorizedListFile()
/*     */   {
/* 387 */     this.alwaysAuthorized = false;
/*     */     Enumeration localEnumeration1;
/* 389 */     if (this.authorizedListFile == null) {
/* 390 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 391 */         JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "readAuthorizedListFile", "alwaysAuthorized set to true");
/*     */       }
/*     */ 
/* 394 */       this.alwaysAuthorized = true;
/*     */     }
/*     */     else {
/* 397 */       Parser localParser = null;
/*     */       try {
/* 399 */         localParser = new Parser(new FileInputStream(getAuthorizedListFile()));
/*     */       } catch (FileNotFoundException localFileNotFoundException) {
/* 401 */         if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 402 */           JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpAcl.class.getName(), "readAuthorizedListFile", "The specified file was not found, authorize everybody");
/*     */         }
/*     */ 
/* 406 */         this.alwaysAuthorized = true;
/* 407 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 411 */         JDMSecurityDefs localJDMSecurityDefs = localParser.SecurityDefs();
/* 412 */         localJDMSecurityDefs.buildAclEntries(this.owner, this.acl);
/* 413 */         localJDMSecurityDefs.buildTrapEntries(this.trapDestList);
/* 414 */         localJDMSecurityDefs.buildInformEntries(this.informDestList);
/*     */       } catch (ParseException localParseException) {
/* 416 */         if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 417 */           JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpAcl.class.getName(), "readAuthorizedListFile", "Got parsing exception", localParseException);
/*     */         }
/*     */ 
/* 420 */         throw new IllegalArgumentException(localParseException.getMessage());
/*     */       } catch (Error localError) {
/* 422 */         if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 423 */           JmxProperties.SNMP_LOGGER.logp(Level.FINEST, SnmpAcl.class.getName(), "readAuthorizedListFile", "Got unexpected error", localError);
/*     */         }
/*     */ 
/* 426 */         throw new IllegalArgumentException(localError.getMessage());
/*     */       }
/*     */ 
/* 429 */       for (localEnumeration1 = this.acl.entries(); localEnumeration1.hasMoreElements(); ) {
/* 430 */         AclEntryImpl localAclEntryImpl = (AclEntryImpl)localEnumeration1.nextElement();
/* 431 */         if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 432 */           JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "readAuthorizedListFile", "===> " + localAclEntryImpl.getPrincipal().toString());
/*     */         }
/*     */ 
/* 436 */         for (localEnumeration2 = localAclEntryImpl.permissions(); localEnumeration2.hasMoreElements(); ) {
/* 437 */           Permission localPermission = (Permission)localEnumeration2.nextElement();
/* 438 */           if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER))
/* 439 */             JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpAcl.class.getName(), "readAuthorizedListFile", "perm = " + localPermission);
/*     */         }
/*     */       }
/*     */     }
/*     */     Enumeration localEnumeration2;
/*     */   }
/*     */ 
/*     */   private void setDefaultFileName()
/*     */   {
/*     */     try
/*     */     {
/* 453 */       setAuthorizedListFile(getDefaultAclFileName());
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.SnmpAcl
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.acl.NotOwnerException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ abstract class Host extends SimpleNode
/*     */   implements Serializable
/*     */ {
/*     */   public Host(int paramInt)
/*     */   {
/*  50 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public Host(Parser paramParser, int paramInt) {
/*  54 */     super(paramParser, paramInt);
/*     */   }
/*     */ 
/*     */   protected abstract PrincipalImpl createAssociatedPrincipal()
/*     */     throws UnknownHostException;
/*     */ 
/*     */   protected abstract String getHname();
/*     */ 
/*     */   public void buildAclEntries(PrincipalImpl paramPrincipalImpl, AclImpl paramAclImpl)
/*     */   {
/*  65 */     PrincipalImpl localPrincipalImpl = null;
/*     */     try {
/*  67 */       localPrincipalImpl = createAssociatedPrincipal();
/*     */     } catch (UnknownHostException localUnknownHostException1) {
/*  69 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  70 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, Host.class.getName(), "buildAclEntries", "Cannot create ACL entry; got exception", localUnknownHostException1);
/*     */       }
/*     */ 
/*  74 */       throw new IllegalArgumentException("Cannot create ACL entry for " + localUnknownHostException1.getMessage());
/*     */     }
/*     */ 
/*  79 */     AclEntryImpl localAclEntryImpl = null;
/*     */     try {
/*  81 */       localAclEntryImpl = new AclEntryImpl(localPrincipalImpl);
/*     */ 
/*  84 */       registerPermission(localAclEntryImpl);
/*  85 */       paramAclImpl.addEntry(paramPrincipalImpl, localAclEntryImpl);
/*     */     } catch (UnknownHostException localUnknownHostException2) {
/*  87 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  88 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, Host.class.getName(), "buildAclEntries", "Cannot create ACL entry; got exception", localUnknownHostException2);
/*     */       }
/*     */ 
/*  92 */       return;
/*     */     } catch (NotOwnerException localNotOwnerException) {
/*  94 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/*  95 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, Host.class.getName(), "buildAclEntries", "Cannot create ACL entry; got exception", localNotOwnerException);
/*     */       }
/*     */ 
/*  99 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void registerPermission(AclEntryImpl paramAclEntryImpl) {
/* 104 */     JDMHost localJDMHost = (JDMHost)jjtGetParent();
/* 105 */     JDMManagers localJDMManagers = (JDMManagers)localJDMHost.jjtGetParent();
/* 106 */     JDMAclItem localJDMAclItem = (JDMAclItem)localJDMManagers.jjtGetParent();
/* 107 */     JDMAccess localJDMAccess = localJDMAclItem.getAccess();
/* 108 */     localJDMAccess.putPermission(paramAclEntryImpl);
/* 109 */     JDMCommunities localJDMCommunities = localJDMAclItem.getCommunities();
/* 110 */     localJDMCommunities.buildCommunities(paramAclEntryImpl);
/*     */   }
/*     */ 
/*     */   public void buildTrapEntries(Hashtable<InetAddress, Vector<String>> paramHashtable)
/*     */   {
/* 115 */     JDMHostTrap localJDMHostTrap = (JDMHostTrap)jjtGetParent();
/* 116 */     JDMTrapInterestedHost localJDMTrapInterestedHost = (JDMTrapInterestedHost)localJDMHostTrap.jjtGetParent();
/* 117 */     JDMTrapItem localJDMTrapItem = (JDMTrapItem)localJDMTrapInterestedHost.jjtGetParent();
/* 118 */     JDMTrapCommunity localJDMTrapCommunity = localJDMTrapItem.getCommunity();
/* 119 */     String str = localJDMTrapCommunity.getCommunity();
/*     */ 
/* 121 */     InetAddress localInetAddress = null;
/*     */     try {
/* 123 */       localInetAddress = InetAddress.getByName(getHname());
/*     */     } catch (UnknownHostException localUnknownHostException) {
/* 125 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 126 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, Host.class.getName(), "buildTrapEntries", "Cannot create TRAP entry; got exception", localUnknownHostException);
/*     */       }
/*     */ 
/* 130 */       return;
/*     */     }
/*     */ 
/* 133 */     Vector localVector = null;
/* 134 */     if (paramHashtable.containsKey(localInetAddress)) {
/* 135 */       localVector = (Vector)paramHashtable.get(localInetAddress);
/* 136 */       if (!localVector.contains(str))
/* 137 */         localVector.addElement(str);
/*     */     }
/*     */     else {
/* 140 */       localVector = new Vector();
/* 141 */       localVector.addElement(str);
/* 142 */       paramHashtable.put(localInetAddress, localVector);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void buildInformEntries(Hashtable<InetAddress, Vector<String>> paramHashtable)
/*     */   {
/* 148 */     JDMHostInform localJDMHostInform = (JDMHostInform)jjtGetParent();
/* 149 */     JDMInformInterestedHost localJDMInformInterestedHost = (JDMInformInterestedHost)localJDMHostInform.jjtGetParent();
/* 150 */     JDMInformItem localJDMInformItem = (JDMInformItem)localJDMInformInterestedHost.jjtGetParent();
/* 151 */     JDMInformCommunity localJDMInformCommunity = localJDMInformItem.getCommunity();
/* 152 */     String str = localJDMInformCommunity.getCommunity();
/*     */ 
/* 154 */     InetAddress localInetAddress = null;
/*     */     try {
/* 156 */       localInetAddress = InetAddress.getByName(getHname());
/*     */     } catch (UnknownHostException localUnknownHostException) {
/* 158 */       if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINEST)) {
/* 159 */         JmxProperties.SNMP_LOGGER.logp(Level.FINEST, Host.class.getName(), "buildTrapEntries", "Cannot create INFORM entry; got exception", localUnknownHostException);
/*     */       }
/*     */ 
/* 163 */       return;
/*     */     }
/*     */ 
/* 166 */     Vector localVector = null;
/* 167 */     if (paramHashtable.containsKey(localInetAddress)) {
/* 168 */       localVector = (Vector)paramHashtable.get(localInetAddress);
/* 169 */       if (!localVector.contains(str))
/* 170 */         localVector.addElement(str);
/*     */     }
/*     */     else {
/* 173 */       localVector = new Vector();
/* 174 */       localVector.addElement(str);
/* 175 */       paramHashtable.put(localInetAddress, localVector);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.Host
 * JD-Core Version:    0.6.2
 */
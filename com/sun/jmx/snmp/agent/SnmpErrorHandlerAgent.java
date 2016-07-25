/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class SnmpErrorHandlerAgent extends SnmpMibAgent
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7751082923508885650L;
/*     */ 
/*     */   public void init()
/*     */     throws IllegalAccessException
/*     */   {
/*     */   }
/*     */ 
/*     */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */     throws Exception
/*     */   {
/*  79 */     return paramObjectName;
/*     */   }
/*     */ 
/*     */   public long[] getRootOid()
/*     */   {
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException
/*     */   {
/* 104 */     JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpErrorHandlerAgent.class.getName(), "get", "Get in Exception");
/*     */ 
/* 108 */     if (paramSnmpMibRequest.getVersion() == 0) {
/* 109 */       throw new SnmpStatusException(2);
/*     */     }
/* 111 */     Enumeration localEnumeration = paramSnmpMibRequest.getElements();
/* 112 */     while (localEnumeration.hasMoreElements()) {
/* 113 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/* 114 */       localSnmpVarBind.setNoSuchObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException
/*     */   {
/* 133 */     JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpErrorHandlerAgent.class.getName(), "check", "Check in Exception");
/*     */ 
/* 137 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException
/*     */   {
/* 150 */     JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpErrorHandlerAgent.class.getName(), "set", "Set in Exception, CANNOT be called");
/*     */ 
/* 154 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void getNext(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException
/*     */   {
/* 167 */     JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpErrorHandlerAgent.class.getName(), "getNext", "GetNext in Exception");
/*     */ 
/* 171 */     if (paramSnmpMibRequest.getVersion() == 0) {
/* 172 */       throw new SnmpStatusException(2);
/*     */     }
/* 174 */     Enumeration localEnumeration = paramSnmpMibRequest.getElements();
/* 175 */     while (localEnumeration.hasMoreElements()) {
/* 176 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/* 177 */       localSnmpVarBind.setEndOfMibView();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void getBulk(SnmpMibRequest paramSnmpMibRequest, int paramInt1, int paramInt2)
/*     */     throws SnmpStatusException
/*     */   {
/* 192 */     JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpErrorHandlerAgent.class.getName(), "getBulk", "GetBulk in Exception");
/*     */ 
/* 196 */     if (paramSnmpMibRequest.getVersion() == 0) {
/* 197 */       throw new SnmpStatusException(5, 0);
/*     */     }
/* 199 */     Enumeration localEnumeration = paramSnmpMibRequest.getElements();
/* 200 */     while (localEnumeration.hasMoreElements()) {
/* 201 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/* 202 */       localSnmpVarBind.setEndOfMibView();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpErrorHandlerAgent
 * JD-Core Version:    0.6.2
 */
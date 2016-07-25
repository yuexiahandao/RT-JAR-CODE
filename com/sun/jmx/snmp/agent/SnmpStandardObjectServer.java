/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class SnmpStandardObjectServer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4641068116505308488L;
/*     */ 
/*     */   public void get(SnmpStandardMetaServer paramSnmpStandardMetaServer, SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 122 */     Object localObject = paramSnmpMibSubRequest.getUserData();
/*     */ 
/* 124 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/* 125 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*     */       try {
/* 127 */         long l = localSnmpVarBind.oid.getOidArc(paramInt);
/* 128 */         localSnmpVarBind.value = paramSnmpStandardMetaServer.get(l, localObject);
/*     */       } catch (SnmpStatusException localSnmpStatusException) {
/* 130 */         paramSnmpMibSubRequest.registerGetException(localSnmpVarBind, localSnmpStatusException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(SnmpStandardMetaServer paramSnmpStandardMetaServer, SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 183 */     Object localObject = paramSnmpMibSubRequest.getUserData();
/*     */ 
/* 185 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/* 186 */       SnmpVarBind localSnmpVarBind = null;
/* 187 */       localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*     */       try
/*     */       {
/* 192 */         long l = localSnmpVarBind.oid.getOidArc(paramInt);
/* 193 */         localSnmpVarBind.value = paramSnmpStandardMetaServer.set(localSnmpVarBind.value, l, localObject);
/*     */       } catch (SnmpStatusException localSnmpStatusException) {
/* 195 */         paramSnmpMibSubRequest.registerSetException(localSnmpVarBind, localSnmpStatusException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void check(SnmpStandardMetaServer paramSnmpStandardMetaServer, SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 249 */     Object localObject = paramSnmpMibSubRequest.getUserData();
/*     */ 
/* 251 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/* 252 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*     */       try
/*     */       {
/* 257 */         long l = localSnmpVarBind.oid.getOidArc(paramInt);
/* 258 */         paramSnmpStandardMetaServer.check(localSnmpVarBind.value, l, localObject);
/*     */       } catch (SnmpStatusException localSnmpStatusException) {
/* 260 */         paramSnmpMibSubRequest.registerCheckException(localSnmpVarBind, localSnmpStatusException);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpStandardObjectServer
 * JD-Core Version:    0.6.2
 */
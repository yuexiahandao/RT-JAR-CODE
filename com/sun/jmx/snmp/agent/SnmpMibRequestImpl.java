/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpEngine;
/*     */ import com.sun.jmx.snmp.SnmpPdu;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class SnmpMibRequestImpl
/*     */   implements SnmpMibRequest
/*     */ {
/*     */   private Vector<SnmpVarBind> varbinds;
/*     */   private int version;
/*     */   private Object data;
/* 233 */   private SnmpPdu reqPdu = null;
/*     */ 
/* 235 */   private SnmpRequestTree tree = null;
/* 236 */   private SnmpEngine engine = null;
/* 237 */   private String principal = null;
/* 238 */   private int securityLevel = -1;
/* 239 */   private int securityModel = -1;
/* 240 */   private byte[] contextName = null;
/* 241 */   private byte[] accessContextName = null;
/*     */ 
/*     */   public SnmpMibRequestImpl(SnmpEngine paramSnmpEngine, SnmpPdu paramSnmpPdu, Vector<SnmpVarBind> paramVector, int paramInt1, Object paramObject, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/*  71 */     this.varbinds = paramVector;
/*  72 */     this.version = paramInt1;
/*  73 */     this.data = paramObject;
/*  74 */     this.reqPdu = paramSnmpPdu;
/*  75 */     this.engine = paramSnmpEngine;
/*  76 */     this.principal = paramString;
/*  77 */     this.securityLevel = paramInt2;
/*  78 */     this.securityModel = paramInt3;
/*  79 */     this.contextName = paramArrayOfByte1;
/*  80 */     this.accessContextName = paramArrayOfByte2;
/*     */   }
/*     */ 
/*     */   public SnmpEngine getEngine()
/*     */   {
/*  91 */     return this.engine;
/*     */   }
/*     */ 
/*     */   public String getPrincipal()
/*     */   {
/*  99 */     return this.principal;
/*     */   }
/*     */ 
/*     */   public int getSecurityLevel()
/*     */   {
/* 107 */     return this.securityLevel;
/*     */   }
/*     */ 
/*     */   public int getSecurityModel()
/*     */   {
/* 114 */     return this.securityModel;
/*     */   }
/*     */ 
/*     */   public byte[] getContextName()
/*     */   {
/* 121 */     return this.contextName;
/*     */   }
/*     */ 
/*     */   public byte[] getAccessContextName()
/*     */   {
/* 129 */     return this.accessContextName;
/*     */   }
/*     */ 
/*     */   public final SnmpPdu getPdu()
/*     */   {
/* 137 */     return this.reqPdu;
/*     */   }
/*     */ 
/*     */   public final Enumeration getElements()
/*     */   {
/* 144 */     return this.varbinds.elements();
/*     */   }
/*     */ 
/*     */   public final Vector<SnmpVarBind> getSubList()
/*     */   {
/* 150 */     return this.varbinds;
/*     */   }
/*     */ 
/*     */   public final int getSize()
/*     */   {
/* 157 */     if (this.varbinds == null) return 0;
/* 158 */     return this.varbinds.size();
/*     */   }
/*     */ 
/*     */   public final int getVersion()
/*     */   {
/* 165 */     return this.version;
/*     */   }
/*     */ 
/*     */   public final int getRequestPduVersion()
/*     */   {
/* 171 */     return this.reqPdu.version;
/*     */   }
/*     */ 
/*     */   public final Object getUserData()
/*     */   {
/* 177 */     return this.data;
/*     */   }
/*     */ 
/*     */   public final int getVarIndex(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 184 */     return this.varbinds.indexOf(paramSnmpVarBind);
/*     */   }
/*     */ 
/*     */   public void addVarBind(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 192 */     this.varbinds.addElement(paramSnmpVarBind);
/*     */   }
/*     */ 
/*     */   final void setRequestTree(SnmpRequestTree paramSnmpRequestTree)
/*     */   {
/* 209 */     this.tree = paramSnmpRequestTree;
/*     */   }
/*     */ 
/*     */   final SnmpRequestTree getRequestTree()
/*     */   {
/* 215 */     return this.tree;
/*     */   }
/*     */ 
/*     */   final Vector getVarbinds()
/*     */   {
/* 221 */     return this.varbinds;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibRequestImpl
 * JD-Core Version:    0.6.2
 */
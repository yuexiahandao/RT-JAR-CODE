/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class SnmpOidTableSupport
/*     */   implements SnmpOidTable
/*     */ {
/* 167 */   private Hashtable<String, SnmpOidRecord> oidStore = new Hashtable();
/*     */   private String myName;
/*     */ 
/*     */   public SnmpOidTableSupport(String paramString)
/*     */   {
/*  47 */     this.myName = paramString;
/*     */   }
/*     */ 
/*     */   public SnmpOidRecord resolveVarName(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/*  60 */     SnmpOidRecord localSnmpOidRecord = (SnmpOidRecord)this.oidStore.get(paramString);
/*  61 */     if (localSnmpOidRecord != null) {
/*  62 */       return localSnmpOidRecord;
/*     */     }
/*  64 */     throw new SnmpStatusException("Variable name <" + paramString + "> not found in Oid repository");
/*     */   }
/*     */ 
/*     */   public SnmpOidRecord resolveVarOid(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/*  80 */     int i = paramString.indexOf('.');
/*  81 */     if (i < 0) {
/*  82 */       throw new SnmpStatusException("Variable oid <" + paramString + "> not found in Oid repository");
/*     */     }
/*  84 */     if (i == 0)
/*     */     {
/*  87 */       paramString = paramString.substring(1, paramString.length());
/*     */     }
/*     */ 
/*  92 */     for (Enumeration localEnumeration = this.oidStore.elements(); localEnumeration.hasMoreElements(); ) {
/*  93 */       SnmpOidRecord localSnmpOidRecord = (SnmpOidRecord)localEnumeration.nextElement();
/*  94 */       if (localSnmpOidRecord.getOid().equals(paramString)) {
/*  95 */         return localSnmpOidRecord;
/*     */       }
/*     */     }
/*  98 */     throw new SnmpStatusException("Variable oid <" + paramString + "> not found in Oid repository");
/*     */   }
/*     */ 
/*     */   public Vector<SnmpOidRecord> getAllEntries()
/*     */   {
/* 107 */     Vector localVector = new Vector();
/*     */ 
/* 109 */     Enumeration localEnumeration = this.oidStore.elements();
/* 110 */     while (localEnumeration.hasMoreElements()) {
/* 111 */       localVector.addElement(localEnumeration.nextElement());
/*     */     }
/* 113 */     return localVector;
/*     */   }
/*     */ 
/*     */   public synchronized void loadMib(SnmpOidRecord[] paramArrayOfSnmpOidRecord)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       for (int i = 0; ; i++) {
/* 125 */         SnmpOidRecord localSnmpOidRecord = paramArrayOfSnmpOidRecord[i];
/* 126 */         if (JmxProperties.SNMP_LOGGER.isLoggable(Level.FINER)) {
/* 127 */           JmxProperties.SNMP_LOGGER.logp(Level.FINER, SnmpOidTableSupport.class.getName(), "loadMib", "Load " + localSnmpOidRecord.getName());
/*     */         }
/*     */ 
/* 131 */         this.oidStore.put(localSnmpOidRecord.getName(), localSnmpOidRecord);
/*     */       }
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 145 */     if (!(paramObject instanceof SnmpOidTableSupport)) {
/* 146 */       return false;
/*     */     }
/* 148 */     SnmpOidTableSupport localSnmpOidTableSupport = (SnmpOidTableSupport)paramObject;
/* 149 */     return this.myName.equals(localSnmpOidTableSupport.getName());
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 157 */     return this.myName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOidTableSupport
 * JD-Core Version:    0.6.2
 */
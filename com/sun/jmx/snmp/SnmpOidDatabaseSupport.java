/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SnmpOidDatabaseSupport
/*     */   implements SnmpOidDatabase
/*     */ {
/*     */   private Vector<SnmpOidTable> tables;
/*     */ 
/*     */   public SnmpOidDatabaseSupport()
/*     */   {
/*  36 */     this.tables = new Vector();
/*     */   }
/*     */ 
/*     */   public SnmpOidDatabaseSupport(SnmpOidTable paramSnmpOidTable)
/*     */   {
/*  44 */     this.tables = new Vector();
/*  45 */     this.tables.addElement(paramSnmpOidTable);
/*     */   }
/*     */ 
/*     */   public void add(SnmpOidTable paramSnmpOidTable)
/*     */   {
/*  53 */     if (!this.tables.contains(paramSnmpOidTable))
/*  54 */       this.tables.addElement(paramSnmpOidTable);
/*     */   }
/*     */ 
/*     */   public void remove(SnmpOidTable paramSnmpOidTable)
/*     */     throws SnmpStatusException
/*     */   {
/*  64 */     if (!this.tables.contains(paramSnmpOidTable)) {
/*  65 */       throw new SnmpStatusException("The specified SnmpOidTable does not exist in this SnmpOidDatabase");
/*     */     }
/*  67 */     this.tables.removeElement(paramSnmpOidTable);
/*     */   }
/*     */ 
/*     */   public SnmpOidRecord resolveVarName(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/*  79 */     for (int i = 0; i < this.tables.size(); i++) {
/*     */       try {
/*  81 */         return ((SnmpOidTable)this.tables.elementAt(i)).resolveVarName(paramString);
/*     */       }
/*     */       catch (SnmpStatusException localSnmpStatusException) {
/*  84 */         if (i == this.tables.size() - 1) {
/*  85 */           throw new SnmpStatusException(localSnmpStatusException.getMessage());
/*     */         }
/*     */       }
/*     */     }
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */   public SnmpOidRecord resolveVarOid(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 100 */     for (int i = 0; i < this.tables.size(); i++) {
/*     */       try {
/* 102 */         return ((SnmpOidTable)this.tables.elementAt(i)).resolveVarOid(paramString);
/*     */       }
/*     */       catch (SnmpStatusException localSnmpStatusException) {
/* 105 */         if (i == this.tables.size() - 1) {
/* 106 */           throw new SnmpStatusException(localSnmpStatusException.getMessage());
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */   public Vector<?> getAllEntries()
/*     */   {
/* 119 */     Vector localVector1 = new Vector();
/* 120 */     for (int i = 0; i < this.tables.size(); i++) {
/* 121 */       Vector localVector2 = (Vector)Util.cast(((SnmpOidTable)this.tables.elementAt(i)).getAllEntries());
/* 122 */       if (localVector2 != null) {
/* 123 */         for (int j = 0; j < localVector2.size(); j++) {
/* 124 */           localVector1.addElement(localVector2.elementAt(j));
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 129 */     return localVector1;
/*     */   }
/*     */ 
/*     */   public void removeAll()
/*     */   {
/* 136 */     this.tables.removeAllElements();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpOidDatabaseSupport
 * JD-Core Version:    0.6.2
 */
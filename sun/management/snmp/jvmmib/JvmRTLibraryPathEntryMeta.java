/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpString;
/*     */ import com.sun.jmx.snmp.SnmpValue;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibEntry;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibNode;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibSubRequest;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardMetaServer;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class JvmRTLibraryPathEntryMeta extends SnmpMibEntry
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmRTLibraryPathEntryMBean node;
/* 248 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmRTLibraryPathEntryMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  78 */     this.objectserver = paramSnmpStandardObjectServer;
/*  79 */     this.varList = new int[1];
/*  80 */     this.varList[0] = 2;
/*  81 */     SnmpMibNode.sort(this.varList);
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  89 */     switch ((int)paramLong) {
/*     */     case 2:
/*  91 */       return new SnmpString(this.node.getJvmRTLibraryPathItem());
/*     */     case 1:
/*  94 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/*  98 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 106 */     switch ((int)paramLong) {
/*     */     case 2:
/* 108 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 111 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 116 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 124 */     switch ((int)paramLong) {
/*     */     case 2:
/* 126 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 129 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 132 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmRTLibraryPathEntryMBean paramJvmRTLibraryPathEntryMBean)
/*     */   {
/* 140 */     this.node = paramJvmRTLibraryPathEntryMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 153 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 166 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 179 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 187 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/* 190 */       return true;
/*     */     }
/*     */ 
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 202 */     switch ((int)paramLong) {
/*     */     case 2:
/* 204 */       return true;
/*     */     }
/*     */ 
/* 208 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 220 */     switch ((int)paramLong) {
/*     */     case 1:
/* 222 */       return true;
/*     */     }
/*     */ 
/* 226 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 234 */     switch ((int)paramLong) {
/*     */     case 2:
/* 236 */       return "JvmRTLibraryPathItem";
/*     */     case 1:
/* 239 */       return "JvmRTLibraryPathIndex";
/*     */     }
/*     */ 
/* 244 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRTLibraryPathEntryMeta
 * JD-Core Version:    0.6.2
 */
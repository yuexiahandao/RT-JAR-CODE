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
/*     */ public class JvmMemMgrPoolRelEntryMeta extends SnmpMibEntry
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmMemMgrPoolRelEntryMBean node;
/* 245 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmMemMgrPoolRelEntryMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  78 */     this.objectserver = paramSnmpStandardObjectServer;
/*  79 */     this.varList = new int[2];
/*  80 */     this.varList[0] = 3;
/*  81 */     this.varList[1] = 2;
/*  82 */     SnmpMibNode.sort(this.varList);
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  90 */     switch ((int)paramLong) {
/*     */     case 3:
/*  92 */       return new SnmpString(this.node.getJvmMemMgrRelPoolName());
/*     */     case 2:
/*  95 */       return new SnmpString(this.node.getJvmMemMgrRelManagerName());
/*     */     }
/*     */ 
/* 100 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 108 */     switch ((int)paramLong) {
/*     */     case 3:
/* 110 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 113 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 118 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 126 */     switch ((int)paramLong) {
/*     */     case 3:
/* 128 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 131 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 134 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmMemMgrPoolRelEntryMBean paramJvmMemMgrPoolRelEntryMBean)
/*     */   {
/* 142 */     this.node = paramJvmMemMgrPoolRelEntryMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 155 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 168 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 181 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 189 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 3:
/* 192 */       return true;
/*     */     }
/*     */ 
/* 196 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 204 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 3:
/* 207 */       return true;
/*     */     }
/*     */ 
/* 211 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 223 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 231 */     switch ((int)paramLong) {
/*     */     case 3:
/* 233 */       return "JvmMemMgrRelPoolName";
/*     */     case 2:
/* 236 */       return "JvmMemMgrRelManagerName";
/*     */     }
/*     */ 
/* 241 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemMgrPoolRelEntryMeta
 * JD-Core Version:    0.6.2
 */
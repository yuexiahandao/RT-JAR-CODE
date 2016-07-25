/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpInt;
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
/*     */ public class JvmMemManagerEntryMeta extends SnmpMibEntry
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmMemManagerEntryMBean node;
/* 263 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmMemManagerEntryMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
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
/*  92 */       return new SnmpInt(this.node.getJvmMemManagerState());
/*     */     case 2:
/*  95 */       return new SnmpString(this.node.getJvmMemManagerName());
/*     */     case 1:
/*  98 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 102 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 110 */     switch ((int)paramLong) {
/*     */     case 3:
/* 112 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 115 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 118 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 123 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 131 */     switch ((int)paramLong) {
/*     */     case 3:
/* 133 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 136 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 139 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 142 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmMemManagerEntryMBean paramJvmMemManagerEntryMBean)
/*     */   {
/* 150 */     this.node = paramJvmMemManagerEntryMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 163 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 176 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 189 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 197 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/* 201 */       return true;
/*     */     }
/*     */ 
/* 205 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 213 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 3:
/* 216 */       return true;
/*     */     }
/*     */ 
/* 220 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 232 */     switch ((int)paramLong) {
/*     */     case 1:
/* 234 */       return true;
/*     */     }
/*     */ 
/* 238 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 246 */     switch ((int)paramLong) {
/*     */     case 3:
/* 248 */       return "JvmMemManagerState";
/*     */     case 2:
/* 251 */       return "JvmMemManagerName";
/*     */     case 1:
/* 254 */       return "JvmMemManagerIndex";
/*     */     }
/*     */ 
/* 259 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemManagerEntryMeta
 * JD-Core Version:    0.6.2
 */
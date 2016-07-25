/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpCounter64;
/*     */ import com.sun.jmx.snmp.SnmpOid;
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
/*     */ public class JvmThreadInstanceEntryMeta extends SnmpMibEntry
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmThreadInstanceEntryMBean node;
/* 391 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmThreadInstanceEntryMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  78 */     this.objectserver = paramSnmpStandardObjectServer;
/*  79 */     this.varList = new int[10];
/*  80 */     this.varList[0] = 9;
/*  81 */     this.varList[1] = 8;
/*  82 */     this.varList[2] = 7;
/*  83 */     this.varList[3] = 6;
/*  84 */     this.varList[4] = 5;
/*  85 */     this.varList[5] = 4;
/*  86 */     this.varList[6] = 3;
/*  87 */     this.varList[7] = 11;
/*  88 */     this.varList[8] = 2;
/*  89 */     this.varList[9] = 10;
/*  90 */     SnmpMibNode.sort(this.varList);
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/*  98 */     switch ((int)paramLong) {
/*     */     case 9:
/* 100 */       return new SnmpString(this.node.getJvmThreadInstName());
/*     */     case 8:
/* 103 */       return new SnmpCounter64(this.node.getJvmThreadInstCpuTimeNs());
/*     */     case 7:
/* 106 */       return new SnmpCounter64(this.node.getJvmThreadInstWaitTimeMs());
/*     */     case 6:
/* 109 */       return new SnmpCounter64(this.node.getJvmThreadInstWaitCount());
/*     */     case 5:
/* 112 */       return new SnmpCounter64(this.node.getJvmThreadInstBlockTimeMs());
/*     */     case 4:
/* 115 */       return new SnmpCounter64(this.node.getJvmThreadInstBlockCount());
/*     */     case 3:
/* 118 */       return new SnmpString(this.node.getJvmThreadInstState());
/*     */     case 11:
/* 121 */       return new SnmpOid(this.node.getJvmThreadInstLockOwnerPtr());
/*     */     case 2:
/* 124 */       return new SnmpCounter64(this.node.getJvmThreadInstId());
/*     */     case 10:
/* 127 */       return new SnmpString(this.node.getJvmThreadInstLockName());
/*     */     case 1:
/* 130 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 134 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 142 */     switch ((int)paramLong) {
/*     */     case 9:
/* 144 */       throw new SnmpStatusException(17);
/*     */     case 8:
/* 147 */       throw new SnmpStatusException(17);
/*     */     case 7:
/* 150 */       throw new SnmpStatusException(17);
/*     */     case 6:
/* 153 */       throw new SnmpStatusException(17);
/*     */     case 5:
/* 156 */       throw new SnmpStatusException(17);
/*     */     case 4:
/* 159 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 162 */       throw new SnmpStatusException(17);
/*     */     case 11:
/* 165 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 168 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 171 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 174 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 179 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 187 */     switch ((int)paramLong) {
/*     */     case 9:
/* 189 */       throw new SnmpStatusException(17);
/*     */     case 8:
/* 192 */       throw new SnmpStatusException(17);
/*     */     case 7:
/* 195 */       throw new SnmpStatusException(17);
/*     */     case 6:
/* 198 */       throw new SnmpStatusException(17);
/*     */     case 5:
/* 201 */       throw new SnmpStatusException(17);
/*     */     case 4:
/* 204 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 207 */       throw new SnmpStatusException(17);
/*     */     case 11:
/* 210 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 213 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 216 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 219 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 222 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmThreadInstanceEntryMBean paramJvmThreadInstanceEntryMBean)
/*     */   {
/* 230 */     this.node = paramJvmThreadInstanceEntryMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 243 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 256 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 269 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 277 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 289 */       return true;
/*     */     }
/*     */ 
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 301 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     case 10:
/*     */     case 11:
/* 312 */       return true;
/*     */     }
/*     */ 
/* 316 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 328 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/* 335 */       if (paramInt == 0) return true;
/*     */       break;
/*     */     case 1:
/* 338 */       return true;
/*     */     case 3:
/*     */     }
/*     */ 
/* 342 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 350 */     switch ((int)paramLong) {
/*     */     case 9:
/* 352 */       return "JvmThreadInstName";
/*     */     case 8:
/* 355 */       return "JvmThreadInstCpuTimeNs";
/*     */     case 7:
/* 358 */       return "JvmThreadInstWaitTimeMs";
/*     */     case 6:
/* 361 */       return "JvmThreadInstWaitCount";
/*     */     case 5:
/* 364 */       return "JvmThreadInstBlockTimeMs";
/*     */     case 4:
/* 367 */       return "JvmThreadInstBlockCount";
/*     */     case 3:
/* 370 */       return "JvmThreadInstState";
/*     */     case 11:
/* 373 */       return "JvmThreadInstLockOwnerPtr";
/*     */     case 2:
/* 376 */       return "JvmThreadInstId";
/*     */     case 10:
/* 379 */       return "JvmThreadInstLockName";
/*     */     case 1:
/* 382 */       return "JvmThreadInstIndex";
/*     */     }
/*     */ 
/* 387 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmThreadInstanceEntryMeta
 * JD-Core Version:    0.6.2
 */
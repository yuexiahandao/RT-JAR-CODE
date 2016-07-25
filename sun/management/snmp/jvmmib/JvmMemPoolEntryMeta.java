/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpCounter64;
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
/*     */ public class JvmMemPoolEntryMeta extends SnmpMibEntry
/*     */   implements Serializable, SnmpStandardMetaServer
/*     */ {
/*     */   protected JvmMemPoolEntryMBean node;
/* 582 */   protected SnmpStandardObjectServer objectserver = null;
/*     */ 
/*     */   public JvmMemPoolEntryMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  78 */     this.objectserver = paramSnmpStandardObjectServer;
/*  79 */     this.varList = new int[20];
/*  80 */     this.varList[0] = 33;
/*  81 */     this.varList[1] = 32;
/*  82 */     this.varList[2] = 31;
/*  83 */     this.varList[3] = 133;
/*  84 */     this.varList[4] = 132;
/*  85 */     this.varList[5] = 131;
/*  86 */     this.varList[6] = 13;
/*  87 */     this.varList[7] = 12;
/*  88 */     this.varList[8] = 11;
/*  89 */     this.varList[9] = 10;
/*  90 */     this.varList[10] = 112;
/*  91 */     this.varList[11] = 111;
/*  92 */     this.varList[12] = 110;
/*  93 */     this.varList[13] = 5;
/*  94 */     this.varList[14] = 4;
/*  95 */     this.varList[15] = 3;
/*  96 */     this.varList[16] = 2;
/*  97 */     this.varList[17] = 23;
/*  98 */     this.varList[18] = 22;
/*  99 */     this.varList[19] = 21;
/* 100 */     SnmpMibNode.sort(this.varList);
/*     */   }
/*     */ 
/*     */   public SnmpValue get(long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 108 */     switch ((int)paramLong) {
/*     */     case 33:
/* 110 */       return new SnmpCounter64(this.node.getJvmMemPoolCollectMaxSize());
/*     */     case 32:
/* 113 */       return new SnmpCounter64(this.node.getJvmMemPoolCollectCommitted());
/*     */     case 31:
/* 116 */       return new SnmpCounter64(this.node.getJvmMemPoolCollectUsed());
/*     */     case 133:
/* 119 */       return new SnmpInt(this.node.getJvmMemPoolCollectThreshdSupport());
/*     */     case 132:
/* 122 */       return new SnmpCounter64(this.node.getJvmMemPoolCollectThreshdCount());
/*     */     case 131:
/* 125 */       return new SnmpCounter64(this.node.getJvmMemPoolCollectThreshold());
/*     */     case 13:
/* 128 */       return new SnmpCounter64(this.node.getJvmMemPoolMaxSize());
/*     */     case 12:
/* 131 */       return new SnmpCounter64(this.node.getJvmMemPoolCommitted());
/*     */     case 11:
/* 134 */       return new SnmpCounter64(this.node.getJvmMemPoolUsed());
/*     */     case 10:
/* 137 */       return new SnmpCounter64(this.node.getJvmMemPoolInitSize());
/*     */     case 112:
/* 140 */       return new SnmpInt(this.node.getJvmMemPoolThreshdSupport());
/*     */     case 111:
/* 143 */       return new SnmpCounter64(this.node.getJvmMemPoolThreshdCount());
/*     */     case 110:
/* 146 */       return new SnmpCounter64(this.node.getJvmMemPoolThreshold());
/*     */     case 5:
/* 149 */       return new SnmpCounter64(this.node.getJvmMemPoolPeakReset());
/*     */     case 4:
/* 152 */       return new SnmpInt(this.node.getJvmMemPoolState());
/*     */     case 3:
/* 155 */       return new SnmpInt(this.node.getJvmMemPoolType());
/*     */     case 2:
/* 158 */       return new SnmpString(this.node.getJvmMemPoolName());
/*     */     case 23:
/* 161 */       return new SnmpCounter64(this.node.getJvmMemPoolPeakMaxSize());
/*     */     case 1:
/* 164 */       throw new SnmpStatusException(224);
/*     */     case 22:
/* 166 */       return new SnmpCounter64(this.node.getJvmMemPoolPeakCommitted());
/*     */     case 21:
/* 169 */       return new SnmpCounter64(this.node.getJvmMemPoolPeakUsed());
/*     */     }
/*     */ 
/* 174 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 182 */     switch ((int)paramLong) {
/*     */     case 33:
/* 184 */       throw new SnmpStatusException(17);
/*     */     case 32:
/* 187 */       throw new SnmpStatusException(17);
/*     */     case 31:
/* 190 */       throw new SnmpStatusException(17);
/*     */     case 133:
/* 193 */       throw new SnmpStatusException(17);
/*     */     case 132:
/* 196 */       throw new SnmpStatusException(17);
/*     */     case 131:
/* 199 */       if ((paramSnmpValue instanceof SnmpCounter64)) {
/* 200 */         this.node.setJvmMemPoolCollectThreshold(((SnmpCounter64)paramSnmpValue).toLong());
/* 201 */         return new SnmpCounter64(this.node.getJvmMemPoolCollectThreshold());
/*     */       }
/* 203 */       throw new SnmpStatusException(7);
/*     */     case 13:
/* 207 */       throw new SnmpStatusException(17);
/*     */     case 12:
/* 210 */       throw new SnmpStatusException(17);
/*     */     case 11:
/* 213 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 216 */       throw new SnmpStatusException(17);
/*     */     case 112:
/* 219 */       throw new SnmpStatusException(17);
/*     */     case 111:
/* 222 */       throw new SnmpStatusException(17);
/*     */     case 110:
/* 225 */       if ((paramSnmpValue instanceof SnmpCounter64)) {
/* 226 */         this.node.setJvmMemPoolThreshold(((SnmpCounter64)paramSnmpValue).toLong());
/* 227 */         return new SnmpCounter64(this.node.getJvmMemPoolThreshold());
/*     */       }
/* 229 */       throw new SnmpStatusException(7);
/*     */     case 5:
/* 233 */       if ((paramSnmpValue instanceof SnmpCounter64)) {
/* 234 */         this.node.setJvmMemPoolPeakReset(((SnmpCounter64)paramSnmpValue).toLong());
/* 235 */         return new SnmpCounter64(this.node.getJvmMemPoolPeakReset());
/*     */       }
/* 237 */       throw new SnmpStatusException(7);
/*     */     case 4:
/* 241 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 244 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 247 */       throw new SnmpStatusException(17);
/*     */     case 23:
/* 250 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 253 */       throw new SnmpStatusException(17);
/*     */     case 22:
/* 256 */       throw new SnmpStatusException(17);
/*     */     case 21:
/* 259 */       throw new SnmpStatusException(17);
/*     */     }
/*     */ 
/* 264 */     throw new SnmpStatusException(17);
/*     */   }
/*     */ 
/*     */   public void check(SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 272 */     switch ((int)paramLong) {
/*     */     case 33:
/* 274 */       throw new SnmpStatusException(17);
/*     */     case 32:
/* 277 */       throw new SnmpStatusException(17);
/*     */     case 31:
/* 280 */       throw new SnmpStatusException(17);
/*     */     case 133:
/* 283 */       throw new SnmpStatusException(17);
/*     */     case 132:
/* 286 */       throw new SnmpStatusException(17);
/*     */     case 131:
/* 289 */       if ((paramSnmpValue instanceof SnmpCounter64))
/* 290 */         this.node.checkJvmMemPoolCollectThreshold(((SnmpCounter64)paramSnmpValue).toLong());
/*     */       else {
/* 292 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 13:
/* 297 */       throw new SnmpStatusException(17);
/*     */     case 12:
/* 300 */       throw new SnmpStatusException(17);
/*     */     case 11:
/* 303 */       throw new SnmpStatusException(17);
/*     */     case 10:
/* 306 */       throw new SnmpStatusException(17);
/*     */     case 112:
/* 309 */       throw new SnmpStatusException(17);
/*     */     case 111:
/* 312 */       throw new SnmpStatusException(17);
/*     */     case 110:
/* 315 */       if ((paramSnmpValue instanceof SnmpCounter64))
/* 316 */         this.node.checkJvmMemPoolThreshold(((SnmpCounter64)paramSnmpValue).toLong());
/*     */       else {
/* 318 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/* 323 */       if ((paramSnmpValue instanceof SnmpCounter64))
/* 324 */         this.node.checkJvmMemPoolPeakReset(((SnmpCounter64)paramSnmpValue).toLong());
/*     */       else {
/* 326 */         throw new SnmpStatusException(7);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 4:
/* 331 */       throw new SnmpStatusException(17);
/*     */     case 3:
/* 334 */       throw new SnmpStatusException(17);
/*     */     case 2:
/* 337 */       throw new SnmpStatusException(17);
/*     */     case 23:
/* 340 */       throw new SnmpStatusException(17);
/*     */     case 1:
/* 343 */       throw new SnmpStatusException(17);
/*     */     case 22:
/* 346 */       throw new SnmpStatusException(17);
/*     */     case 21:
/* 349 */       throw new SnmpStatusException(17);
/*     */     default:
/* 352 */       throw new SnmpStatusException(17);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void setInstance(JvmMemPoolEntryMBean paramJvmMemPoolEntryMBean)
/*     */   {
/* 360 */     this.node = paramJvmMemPoolEntryMBean;
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 373 */     this.objectserver.get(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 386 */     this.objectserver.set(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 399 */     this.objectserver.check(this, paramSnmpMibSubRequest, paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVariable(long paramLong)
/*     */   {
/* 407 */     switch ((int)paramLong) {
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 110:
/*     */     case 111:
/*     */     case 112:
/*     */     case 131:
/*     */     case 132:
/*     */     case 133:
/* 429 */       return true;
/*     */     }
/*     */ 
/* 433 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isReadable(long paramLong)
/*     */   {
/* 441 */     switch ((int)paramLong) {
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 110:
/*     */     case 111:
/*     */     case 112:
/*     */     case 131:
/*     */     case 132:
/*     */     case 133:
/* 462 */       return true;
/*     */     }
/*     */ 
/* 466 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean skipVariable(long paramLong, Object paramObject, int paramInt)
/*     */   {
/* 478 */     switch ((int)paramLong) {
/*     */     case 5:
/*     */     case 10:
/*     */     case 11:
/*     */     case 12:
/*     */     case 13:
/*     */     case 23:
/*     */     case 31:
/*     */     case 32:
/*     */     case 33:
/*     */     case 110:
/*     */     case 111:
/*     */     case 131:
/*     */     case 132:
/* 492 */       if (paramInt == 0) return true;
/*     */       break;
/*     */     case 1:
/* 495 */       return true;
/*     */     case 21:
/*     */     case 22:
/* 498 */       if (paramInt == 0) return true;
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 503 */     return super.skipVariable(paramLong, paramObject, paramInt);
/*     */   }
/*     */ 
/*     */   public String getAttributeName(long paramLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 511 */     switch ((int)paramLong) {
/*     */     case 33:
/* 513 */       return "JvmMemPoolCollectMaxSize";
/*     */     case 32:
/* 516 */       return "JvmMemPoolCollectCommitted";
/*     */     case 31:
/* 519 */       return "JvmMemPoolCollectUsed";
/*     */     case 133:
/* 522 */       return "JvmMemPoolCollectThreshdSupport";
/*     */     case 132:
/* 525 */       return "JvmMemPoolCollectThreshdCount";
/*     */     case 131:
/* 528 */       return "JvmMemPoolCollectThreshold";
/*     */     case 13:
/* 531 */       return "JvmMemPoolMaxSize";
/*     */     case 12:
/* 534 */       return "JvmMemPoolCommitted";
/*     */     case 11:
/* 537 */       return "JvmMemPoolUsed";
/*     */     case 10:
/* 540 */       return "JvmMemPoolInitSize";
/*     */     case 112:
/* 543 */       return "JvmMemPoolThreshdSupport";
/*     */     case 111:
/* 546 */       return "JvmMemPoolThreshdCount";
/*     */     case 110:
/* 549 */       return "JvmMemPoolThreshold";
/*     */     case 5:
/* 552 */       return "JvmMemPoolPeakReset";
/*     */     case 4:
/* 555 */       return "JvmMemPoolState";
/*     */     case 3:
/* 558 */       return "JvmMemPoolType";
/*     */     case 2:
/* 561 */       return "JvmMemPoolName";
/*     */     case 23:
/* 564 */       return "JvmMemPoolPeakMaxSize";
/*     */     case 1:
/* 567 */       return "JvmMemPoolIndex";
/*     */     case 22:
/* 570 */       return "JvmMemPoolPeakCommitted";
/*     */     case 21:
/* 573 */       return "JvmMemPoolPeakUsed";
/*     */     }
/*     */ 
/* 578 */     throw new SnmpStatusException(225);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmMemPoolEntryMeta
 * JD-Core Version:    0.6.2
 */
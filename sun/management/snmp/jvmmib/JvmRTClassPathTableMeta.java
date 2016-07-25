/*     */ package sun.management.snmp.jvmmib;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.agent.SnmpMib;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibSubRequest;
/*     */ import com.sun.jmx.snmp.agent.SnmpMibTable;
/*     */ import com.sun.jmx.snmp.agent.SnmpStandardObjectServer;
/*     */ import com.sun.jmx.snmp.agent.SnmpTableEntryFactory;
/*     */ import java.io.Serializable;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class JvmRTClassPathTableMeta extends SnmpMibTable
/*     */   implements Serializable
/*     */ {
/*     */   private JvmRTClassPathEntryMeta node;
/*     */   protected SnmpStandardObjectServer objectserver;
/*     */ 
/*     */   public JvmRTClassPathTableMeta(SnmpMib paramSnmpMib, SnmpStandardObjectServer paramSnmpStandardObjectServer)
/*     */   {
/*  76 */     super(paramSnmpMib);
/*  77 */     this.objectserver = paramSnmpStandardObjectServer;
/*     */   }
/*     */ 
/*     */   protected JvmRTClassPathEntryMeta createJvmRTClassPathEntryMetaNode(String paramString1, String paramString2, SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/*  97 */     return new JvmRTClassPathEntryMeta(paramSnmpMib, this.objectserver);
/*     */   }
/*     */ 
/*     */   public void createNewEntry(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 110 */     if (this.factory != null)
/* 111 */       this.factory.createNewEntry(paramSnmpMibSubRequest, paramSnmpOid, paramInt, this);
/*     */     else
/* 113 */       throw new SnmpStatusException(6);
/*     */   }
/*     */ 
/*     */   public boolean isRegistrationRequired()
/*     */   {
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */   public void registerEntryNode(SnmpMib paramSnmpMib, MBeanServer paramMBeanServer)
/*     */   {
/* 133 */     this.node = createJvmRTClassPathEntryMetaNode("JvmRTClassPathEntry", "JvmRTClassPathTable", paramSnmpMib, paramMBeanServer);
/*     */   }
/*     */ 
/*     */   public synchronized void addEntry(SnmpOid paramSnmpOid, ObjectName paramObjectName, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 147 */     if (!(paramObject instanceof JvmRTClassPathEntryMBean)) {
/* 148 */       throw new ClassCastException("Entries for Table \"JvmRTClassPathTable\" must implement the \"JvmRTClassPathEntryMBean\" interface.");
/*     */     }
/*     */ 
/* 151 */     super.addEntry(paramSnmpOid, paramObjectName, paramObject);
/*     */   }
/*     */ 
/*     */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 164 */     JvmRTClassPathEntryMBean localJvmRTClassPathEntryMBean = (JvmRTClassPathEntryMBean)getEntry(paramSnmpOid);
/* 165 */     synchronized (this) {
/* 166 */       this.node.setInstance(localJvmRTClassPathEntryMBean);
/* 167 */       this.node.get(paramSnmpMibSubRequest, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 180 */     if (paramSnmpMibSubRequest.getSize() == 0) return;
/*     */ 
/* 182 */     JvmRTClassPathEntryMBean localJvmRTClassPathEntryMBean = (JvmRTClassPathEntryMBean)getEntry(paramSnmpOid);
/* 183 */     synchronized (this) {
/* 184 */       this.node.setInstance(localJvmRTClassPathEntryMBean);
/* 185 */       this.node.set(paramSnmpMibSubRequest, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 198 */     if (paramSnmpMibSubRequest.getSize() == 0) return;
/*     */ 
/* 200 */     JvmRTClassPathEntryMBean localJvmRTClassPathEntryMBean = (JvmRTClassPathEntryMBean)getEntry(paramSnmpOid);
/* 201 */     synchronized (this) {
/* 202 */       this.node.setInstance(localJvmRTClassPathEntryMBean);
/* 203 */       this.node.check(paramSnmpMibSubRequest, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void validateVarEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 212 */     this.node.validateVarId(paramLong, paramObject);
/*     */   }
/*     */ 
/*     */   public boolean isReadableEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 220 */     return this.node.isReadable(paramLong);
/*     */   }
/*     */ 
/*     */   public long getNextVarEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 228 */     long l = this.node.getNextVarId(paramLong, paramObject);
/* 229 */     while (!isReadableEntryId(paramSnmpOid, l, paramObject))
/* 230 */       l = this.node.getNextVarId(l, paramObject);
/* 231 */     return l;
/*     */   }
/*     */ 
/*     */   public boolean skipEntryVariable(SnmpOid paramSnmpOid, long paramLong, Object paramObject, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 243 */       JvmRTClassPathEntryMBean localJvmRTClassPathEntryMBean = (JvmRTClassPathEntryMBean)getEntry(paramSnmpOid);
/* 244 */       synchronized (this) {
/* 245 */         this.node.setInstance(localJvmRTClassPathEntryMBean);
/* 246 */         return this.node.skipVariable(paramLong, paramObject, paramInt);
/*     */       }
/*     */     } catch (SnmpStatusException localSnmpStatusException) {  }
/*     */ 
/* 249 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvmmib.JvmRTClassPathTableMeta
 * JD-Core Version:    0.6.2
 */
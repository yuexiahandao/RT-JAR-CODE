/*     */ package sun.management.snmp.util;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import com.sun.jmx.snmp.SnmpPdu;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.ThreadContext;
/*     */ import com.sun.jmx.snmp.agent.SnmpUserDataFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class JvmContextFactory
/*     */   implements SnmpUserDataFactory
/*     */ {
/*     */   public Object allocateUserData(SnmpPdu paramSnmpPdu)
/*     */     throws SnmpStatusException
/*     */   {
/*  71 */     return Collections.synchronizedMap(new HashMap());
/*     */   }
/*     */ 
/*     */   public void releaseUserData(Object paramObject, SnmpPdu paramSnmpPdu)
/*     */     throws SnmpStatusException
/*     */   {
/*  98 */     ((Map)paramObject).clear();
/*     */   }
/*     */ 
/*     */   public static Map<Object, Object> getUserData()
/*     */   {
/* 103 */     Object localObject = ThreadContext.get("SnmpUserData");
/*     */ 
/* 106 */     if ((localObject instanceof Map)) return (Map)Util.cast(localObject);
/* 107 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.util.JvmContextFactory
 * JD-Core Version:    0.6.2
 */
/*    */ package sun.management;
/*    */ 
/*    */ import javax.management.MBeanRegistration;
/*    */ import javax.management.MBeanServer;
/*    */ import javax.management.ObjectName;
/*    */ 
/*    */ public class HotspotInternal
/*    */   implements HotspotInternalMBean, MBeanRegistration
/*    */ {
/*    */   private static final String HOTSPOT_INTERNAL_MBEAN_NAME = "sun.management:type=HotspotInternal";
/* 44 */   private static ObjectName objName = Util.newObjectName("sun.management:type=HotspotInternal");
/* 45 */   private MBeanServer server = null;
/*    */ 
/*    */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*    */     throws Exception
/*    */   {
/* 58 */     ManagementFactoryHelper.registerInternalMBeans(paramMBeanServer);
/* 59 */     this.server = paramMBeanServer;
/* 60 */     return objName;
/*    */   }
/*    */ 
/*    */   public void postRegister(Boolean paramBoolean) {
/*    */   }
/*    */ 
/*    */   public void preDeregister() throws Exception {
/* 67 */     ManagementFactoryHelper.unregisterInternalMBeans(this.server);
/*    */   }
/*    */ 
/*    */   public void postDeregister()
/*    */   {
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.HotspotInternal
 * JD-Core Version:    0.6.2
 */
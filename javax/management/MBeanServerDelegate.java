/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class MBeanServerDelegate
/*     */   implements MBeanServerDelegateMBean, NotificationEmitter
/*     */ {
/*     */   private String mbeanServerId;
/*     */   private final NotificationBroadcasterSupport broadcaster;
/*  49 */   private static long oldStamp = 0L;
/*     */   private final long stamp;
/*  51 */   private long sequenceNumber = 1L;
/*     */   private static final MBeanNotificationInfo[] notifsInfo;
/* 217 */   public static final ObjectName DELEGATE_NAME = Util.newObjectName("JMImplementation:type=MBeanServerDelegate");
/*     */ 
/*     */   public MBeanServerDelegate()
/*     */   {
/*  71 */     this.stamp = getStamp();
/*  72 */     this.broadcaster = new NotificationBroadcasterSupport();
/*     */   }
/*     */ 
/*     */   public synchronized String getMBeanServerId()
/*     */   {
/*  82 */     if (this.mbeanServerId == null) {
/*     */       String str;
/*     */       try {
/*  85 */         str = InetAddress.getLocalHost().getHostName();
/*     */       } catch (UnknownHostException localUnknownHostException) {
/*  87 */         JmxProperties.MISC_LOGGER.finest("Can't get local host name, using \"localhost\" instead. Cause is: " + localUnknownHostException);
/*     */ 
/*  89 */         str = "localhost";
/*     */       }
/*  91 */       this.mbeanServerId = (str + "_" + this.stamp);
/*     */     }
/*  93 */     return this.mbeanServerId;
/*     */   }
/*     */ 
/*     */   public String getSpecificationName()
/*     */   {
/* 103 */     return "Java Management Extensions";
/*     */   }
/*     */ 
/*     */   public String getSpecificationVersion()
/*     */   {
/* 113 */     return "1.4";
/*     */   }
/*     */ 
/*     */   public String getSpecificationVendor()
/*     */   {
/* 123 */     return "Oracle Corporation";
/*     */   }
/*     */ 
/*     */   public String getImplementationName()
/*     */   {
/* 132 */     return "JMX";
/*     */   }
/*     */ 
/*     */   public String getImplementationVersion()
/*     */   {
/*     */     try
/*     */     {
/* 142 */       return System.getProperty("java.runtime.version"); } catch (SecurityException localSecurityException) {
/*     */     }
/* 144 */     return "";
/*     */   }
/*     */ 
/*     */   public String getImplementationVendor()
/*     */   {
/* 154 */     return "Oracle Corporation";
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 160 */     int i = notifsInfo.length;
/* 161 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = new MBeanNotificationInfo[i];
/*     */ 
/* 163 */     System.arraycopy(notifsInfo, 0, arrayOfMBeanNotificationInfo, 0, i);
/* 164 */     return arrayOfMBeanNotificationInfo;
/*     */   }
/*     */ 
/*     */   public synchronized void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws IllegalArgumentException
/*     */   {
/* 174 */     this.broadcaster.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public synchronized void removeNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 184 */     this.broadcaster.removeNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public synchronized void removeNotificationListener(NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 192 */     this.broadcaster.removeNotificationListener(paramNotificationListener);
/*     */   }
/*     */ 
/*     */   public void sendNotification(Notification paramNotification)
/*     */   {
/* 204 */     if (paramNotification.getSequenceNumber() < 1L) {
/* 205 */       synchronized (this) {
/* 206 */         paramNotification.setSequenceNumber(this.sequenceNumber++);
/*     */       }
/*     */     }
/* 209 */     this.broadcaster.sendNotification(paramNotification);
/*     */   }
/*     */ 
/*     */   private static synchronized long getStamp()
/*     */   {
/* 227 */     long l = System.currentTimeMillis();
/* 228 */     if (oldStamp >= l) {
/* 229 */       l = oldStamp + 1L;
/*     */     }
/* 231 */     oldStamp = l;
/* 232 */     return l;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  56 */     String[] arrayOfString = { "JMX.mbean.unregistered", "JMX.mbean.registered" };
/*     */ 
/*  60 */     notifsInfo = new MBeanNotificationInfo[1];
/*  61 */     notifsInfo[0] = new MBeanNotificationInfo(arrayOfString, "javax.management.MBeanServerNotification", "Notifications sent by the MBeanServerDelegate MBean");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerDelegate
 * JD-Core Version:    0.6.2
 */
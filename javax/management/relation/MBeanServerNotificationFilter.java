/*     */ package javax.management.relation;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.security.AccessController;
/*     */ import java.util.List;
/*     */ import java.util.Vector;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.MBeanServerNotification;
/*     */ import javax.management.Notification;
/*     */ import javax.management.NotificationFilterSupport;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class MBeanServerNotificationFilter extends NotificationFilterSupport
/*     */ {
/*     */   private static final long oldSerialVersionUID = 6001782699077323605L;
/*     */   private static final long newSerialVersionUID = 2605900539589789736L;
/*  78 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("mySelectObjNameList", Vector.class), new ObjectStreamField("myDeselectObjNameList", Vector.class) };
/*     */ 
/*  85 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("selectedNames", List.class), new ObjectStreamField("deselectedNames", List.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 108 */   private static boolean compat = false;
/*     */ 
/* 140 */   private List<ObjectName> selectedNames = new Vector();
/*     */ 
/* 150 */   private List<ObjectName> deselectedNames = null;
/*     */ 
/*     */   public MBeanServerNotificationFilter()
/*     */   {
/* 163 */     JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "MBeanServerNotificationFilter");
/*     */ 
/* 166 */     enableType("JMX.mbean.registered");
/* 167 */     enableType("JMX.mbean.unregistered");
/*     */ 
/* 169 */     JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "MBeanServerNotificationFilter");
/*     */   }
/*     */ 
/*     */   public synchronized void disableAllObjectNames()
/*     */   {
/* 184 */     JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "disableAllObjectNames");
/*     */ 
/* 187 */     this.selectedNames = new Vector();
/* 188 */     this.deselectedNames = null;
/*     */ 
/* 190 */     JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "disableAllObjectNames");
/*     */   }
/*     */ 
/*     */   public synchronized void disableObjectName(ObjectName paramObjectName)
/*     */     throws IllegalArgumentException
/*     */   {
/* 205 */     if (paramObjectName == null) {
/* 206 */       String str = "Invalid parameter.";
/* 207 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 210 */     JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "disableObjectName", paramObjectName);
/*     */ 
/* 214 */     if ((this.selectedNames != null) && 
/* 215 */       (this.selectedNames.size() != 0)) {
/* 216 */       this.selectedNames.remove(paramObjectName);
/*     */     }
/*     */ 
/* 221 */     if (this.deselectedNames != null)
/*     */     {
/* 223 */       if (!this.deselectedNames.contains(paramObjectName))
/*     */       {
/* 225 */         this.deselectedNames.add(paramObjectName);
/*     */       }
/*     */     }
/*     */ 
/* 229 */     JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "disableObjectName");
/*     */   }
/*     */ 
/*     */   public synchronized void enableAllObjectNames()
/*     */   {
/* 239 */     JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "enableAllObjectNames");
/*     */ 
/* 242 */     this.selectedNames = null;
/* 243 */     this.deselectedNames = new Vector();
/*     */ 
/* 245 */     JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "enableAllObjectNames");
/*     */   }
/*     */ 
/*     */   public synchronized void enableObjectName(ObjectName paramObjectName)
/*     */     throws IllegalArgumentException
/*     */   {
/* 260 */     if (paramObjectName == null) {
/* 261 */       String str = "Invalid parameter.";
/* 262 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 265 */     JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "enableObjectName", paramObjectName);
/*     */ 
/* 269 */     if ((this.deselectedNames != null) && 
/* 270 */       (this.deselectedNames.size() != 0)) {
/* 271 */       this.deselectedNames.remove(paramObjectName);
/*     */     }
/*     */ 
/* 276 */     if (this.selectedNames != null)
/*     */     {
/* 278 */       if (!this.selectedNames.contains(paramObjectName))
/*     */       {
/* 280 */         this.selectedNames.add(paramObjectName);
/*     */       }
/*     */     }
/*     */ 
/* 284 */     JmxProperties.RELATION_LOGGER.exiting(MBeanServerNotificationFilter.class.getName(), "enableObjectName");
/*     */   }
/*     */ 
/*     */   public synchronized Vector<ObjectName> getEnabledObjectNames()
/*     */   {
/* 299 */     if (this.selectedNames != null) {
/* 300 */       return new Vector(this.selectedNames);
/*     */     }
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized Vector<ObjectName> getDisabledObjectNames()
/*     */   {
/* 316 */     if (this.deselectedNames != null) {
/* 317 */       return new Vector(this.deselectedNames);
/*     */     }
/* 319 */     return null;
/*     */   }
/*     */ 
/*     */   public synchronized boolean isNotificationEnabled(Notification paramNotification)
/*     */     throws IllegalArgumentException
/*     */   {
/* 347 */     if (paramNotification == null) {
/* 348 */       str = "Invalid parameter.";
/* 349 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 352 */     JmxProperties.RELATION_LOGGER.entering(MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", paramNotification);
/*     */ 
/* 356 */     String str = paramNotification.getType();
/* 357 */     Vector localVector = getEnabledTypes();
/* 358 */     if (!localVector.contains(str)) {
/* 359 */       JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "Type not selected, exiting");
/*     */ 
/* 363 */       return false;
/*     */     }
/*     */ 
/* 367 */     MBeanServerNotification localMBeanServerNotification = (MBeanServerNotification)paramNotification;
/*     */ 
/* 370 */     ObjectName localObjectName = localMBeanServerNotification.getMBeanName();
/*     */ 
/* 372 */     boolean bool = false;
/* 373 */     if (this.selectedNames != null)
/*     */     {
/* 376 */       if (this.selectedNames.size() == 0)
/*     */       {
/* 378 */         JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "No ObjectNames selected, exiting");
/*     */ 
/* 382 */         return false;
/*     */       }
/*     */ 
/* 385 */       bool = this.selectedNames.contains(localObjectName);
/* 386 */       if (!bool)
/*     */       {
/* 388 */         JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName not in selected list, exiting");
/*     */ 
/* 392 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 396 */     if (!bool)
/*     */     {
/* 399 */       if (this.deselectedNames == null)
/*     */       {
/* 402 */         JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName not selected, and all names deselected, exiting");
/*     */ 
/* 407 */         return false;
/*     */       }
/* 409 */       if (this.deselectedNames.contains(localObjectName))
/*     */       {
/* 411 */         JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName explicitly not selected, exiting");
/*     */ 
/* 415 */         return false;
/*     */       }
/*     */     }
/*     */ 
/* 419 */     JmxProperties.RELATION_LOGGER.logp(Level.FINER, MBeanServerNotificationFilter.class.getName(), "isNotificationEnabled", "ObjectName selected, exiting");
/*     */ 
/* 423 */     return true;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 432 */     if (compat)
/*     */     {
/* 436 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 437 */       this.selectedNames = ((List)Util.cast(localGetField.get("mySelectObjNameList", null)));
/* 438 */       if (localGetField.defaulted("mySelectObjNameList"))
/*     */       {
/* 440 */         throw new NullPointerException("mySelectObjNameList");
/*     */       }
/* 442 */       this.deselectedNames = ((List)Util.cast(localGetField.get("myDeselectObjNameList", null)));
/* 443 */       if (localGetField.defaulted("myDeselectObjNameList"))
/*     */       {
/* 445 */         throw new NullPointerException("myDeselectObjNameList");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 452 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 462 */     if (compat)
/*     */     {
/* 466 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 467 */       localPutField.put("mySelectObjNameList", this.selectedNames);
/* 468 */       localPutField.put("myDeselectObjNameList", this.deselectedNames);
/* 469 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 475 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 111 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 112 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 113 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 117 */     if (compat) {
/* 118 */       serialPersistentFields = oldSerialPersistentFields;
/* 119 */       serialVersionUID = 6001782699077323605L;
/*     */     } else {
/* 121 */       serialPersistentFields = newSerialPersistentFields;
/* 122 */       serialVersionUID = 2605900539589789736L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.MBeanServerNotificationFilter
 * JD-Core Version:    0.6.2
 */
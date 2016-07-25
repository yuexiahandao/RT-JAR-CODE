/*     */ package javax.management.relation;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.management.Notification;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public class RelationNotification extends Notification
/*     */ {
/*     */   private static final long oldSerialVersionUID = -2126464566505527147L;
/*     */   private static final long newSerialVersionUID = -6871117877523310399L;
/*  77 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("myNewRoleValue", ArrayList.class), new ObjectStreamField("myOldRoleValue", ArrayList.class), new ObjectStreamField("myRelId", String.class), new ObjectStreamField("myRelObjName", ObjectName.class), new ObjectStreamField("myRelTypeName", String.class), new ObjectStreamField("myRoleName", String.class), new ObjectStreamField("myUnregMBeanList", ArrayList.class) };
/*     */ 
/*  89 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("newRoleValue", List.class), new ObjectStreamField("oldRoleValue", List.class), new ObjectStreamField("relationId", String.class), new ObjectStreamField("relationObjName", ObjectName.class), new ObjectStreamField("relationTypeName", String.class), new ObjectStreamField("roleName", String.class), new ObjectStreamField("unregisterMBeanList", List.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 120 */   private static boolean compat = false;
/*     */   public static final String RELATION_BASIC_CREATION = "jmx.relation.creation.basic";
/*     */   public static final String RELATION_MBEAN_CREATION = "jmx.relation.creation.mbean";
/*     */   public static final String RELATION_BASIC_UPDATE = "jmx.relation.update.basic";
/*     */   public static final String RELATION_MBEAN_UPDATE = "jmx.relation.update.mbean";
/*     */   public static final String RELATION_BASIC_REMOVAL = "jmx.relation.removal.basic";
/*     */   public static final String RELATION_MBEAN_REMOVAL = "jmx.relation.removal.mbean";
/* 176 */   private String relationId = null;
/*     */ 
/* 181 */   private String relationTypeName = null;
/*     */ 
/* 187 */   private ObjectName relationObjName = null;
/*     */ 
/* 193 */   private List<ObjectName> unregisterMBeanList = null;
/*     */ 
/* 198 */   private String roleName = null;
/*     */ 
/* 203 */   private List<ObjectName> oldRoleValue = null;
/*     */ 
/* 208 */   private List<ObjectName> newRoleValue = null;
/*     */ 
/*     */   public RelationNotification(String paramString1, Object paramObject, long paramLong1, long paramLong2, String paramString2, String paramString3, String paramString4, ObjectName paramObjectName, List<ObjectName> paramList)
/*     */     throws IllegalArgumentException
/*     */   {
/* 261 */     super(paramString1, paramObject, paramLong1, paramLong2, paramString2);
/*     */ 
/* 263 */     if ((!isValidBasicStrict(paramString1, paramObject, paramString3, paramString4)) || (!isValidCreate(paramString1))) {
/* 264 */       throw new IllegalArgumentException("Invalid parameter.");
/*     */     }
/*     */ 
/* 267 */     this.relationId = paramString3;
/* 268 */     this.relationTypeName = paramString4;
/* 269 */     this.relationObjName = safeGetObjectName(paramObjectName);
/* 270 */     this.unregisterMBeanList = safeGetObjectNameList(paramList);
/*     */   }
/*     */ 
/*     */   public RelationNotification(String paramString1, Object paramObject, long paramLong1, long paramLong2, String paramString2, String paramString3, String paramString4, ObjectName paramObjectName, String paramString5, List<ObjectName> paramList1, List<ObjectName> paramList2)
/*     */     throws IllegalArgumentException
/*     */   {
/* 311 */     super(paramString1, paramObject, paramLong1, paramLong2, paramString2);
/*     */ 
/* 313 */     if ((!isValidBasicStrict(paramString1, paramObject, paramString3, paramString4)) || (!isValidUpdate(paramString1, paramString5, paramList1, paramList2))) {
/* 314 */       throw new IllegalArgumentException("Invalid parameter.");
/*     */     }
/*     */ 
/* 317 */     this.relationId = paramString3;
/* 318 */     this.relationTypeName = paramString4;
/* 319 */     this.relationObjName = safeGetObjectName(paramObjectName);
/*     */ 
/* 321 */     this.roleName = paramString5;
/* 322 */     this.oldRoleValue = safeGetObjectNameList(paramList2);
/* 323 */     this.newRoleValue = safeGetObjectNameList(paramList1);
/*     */   }
/*     */ 
/*     */   public String getRelationId()
/*     */   {
/* 336 */     return this.relationId;
/*     */   }
/*     */ 
/*     */   public String getRelationTypeName()
/*     */   {
/* 345 */     return this.relationTypeName;
/*     */   }
/*     */ 
/*     */   public ObjectName getObjectName()
/*     */   {
/* 355 */     return this.relationObjName;
/*     */   }
/*     */ 
/*     */   public List<ObjectName> getMBeansToUnregister()
/*     */   {
/*     */     Object localObject;
/* 366 */     if (this.unregisterMBeanList != null)
/* 367 */       localObject = new ArrayList(this.unregisterMBeanList);
/*     */     else {
/* 369 */       localObject = Collections.emptyList();
/*     */     }
/* 371 */     return localObject;
/*     */   }
/*     */ 
/*     */   public String getRoleName()
/*     */   {
/* 380 */     String str = null;
/* 381 */     if (this.roleName != null) {
/* 382 */       str = this.roleName;
/*     */     }
/* 384 */     return str;
/*     */   }
/*     */ 
/*     */   public List<ObjectName> getOldRoleValue()
/*     */   {
/*     */     Object localObject;
/* 394 */     if (this.oldRoleValue != null)
/* 395 */       localObject = new ArrayList(this.oldRoleValue);
/*     */     else {
/* 397 */       localObject = Collections.emptyList();
/*     */     }
/* 399 */     return localObject;
/*     */   }
/*     */ 
/*     */   public List<ObjectName> getNewRoleValue()
/*     */   {
/*     */     Object localObject;
/* 409 */     if (this.newRoleValue != null)
/* 410 */       localObject = new ArrayList(this.newRoleValue);
/*     */     else {
/* 412 */       localObject = Collections.emptyList();
/*     */     }
/* 414 */     return localObject;
/*     */   }
/*     */ 
/*     */   private boolean isValidBasicStrict(String paramString1, Object paramObject, String paramString2, String paramString3)
/*     */   {
/* 466 */     if (paramObject == null) {
/* 467 */       return false;
/*     */     }
/* 469 */     return isValidBasic(paramString1, paramObject, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   private boolean isValidBasic(String paramString1, Object paramObject, String paramString2, String paramString3) {
/* 473 */     if ((paramString1 == null) || (paramString2 == null) || (paramString3 == null)) {
/* 474 */       return false;
/*     */     }
/*     */ 
/* 477 */     if ((paramObject != null) && (!(paramObject instanceof RelationService)) && (!(paramObject instanceof ObjectName)))
/*     */     {
/* 480 */       return false;
/*     */     }
/*     */ 
/* 483 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean isValidCreate(String paramString) {
/* 487 */     String[] arrayOfString = { "jmx.relation.creation.basic", "jmx.relation.creation.mbean", "jmx.relation.removal.basic", "jmx.relation.removal.mbean" };
/*     */ 
/* 492 */     HashSet localHashSet = new HashSet(Arrays.asList(arrayOfString));
/* 493 */     return localHashSet.contains(paramString);
/*     */   }
/*     */ 
/*     */   private boolean isValidUpdate(String paramString1, String paramString2, List<ObjectName> paramList1, List<ObjectName> paramList2)
/*     */   {
/* 499 */     if ((!paramString1.equals("jmx.relation.update.basic")) && (!paramString1.equals("jmx.relation.update.mbean")))
/*     */     {
/* 501 */       return false;
/*     */     }
/*     */ 
/* 504 */     if ((paramString2 == null) || (paramList2 == null) || (paramList1 == null)) {
/* 505 */       return false;
/*     */     }
/*     */ 
/* 508 */     return true;
/*     */   }
/*     */ 
/*     */   private ArrayList<ObjectName> safeGetObjectNameList(List<ObjectName> paramList) {
/* 512 */     ArrayList localArrayList = null;
/* 513 */     if (paramList != null) {
/* 514 */       localArrayList = new ArrayList();
/* 515 */       for (ObjectName localObjectName : paramList)
/*     */       {
/* 517 */         localArrayList.add(ObjectName.getInstance(localObjectName));
/*     */       }
/*     */     }
/* 520 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   private ObjectName safeGetObjectName(ObjectName paramObjectName) {
/* 524 */     ObjectName localObjectName = null;
/* 525 */     if (paramObjectName != null) {
/* 526 */       localObjectName = ObjectName.getInstance(paramObjectName);
/*     */     }
/* 528 */     return localObjectName;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 542 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/*     */     String str1;
/*     */     String str2;
/*     */     String str3;
/*     */     ObjectName localObjectName;
/*     */     List localList1;
/*     */     List localList2;
/*     */     List localList3;
/* 544 */     if (compat) {
/* 545 */       str1 = (String)localGetField.get("myRelId", null);
/* 546 */       str2 = (String)localGetField.get("myRelTypeName", null);
/* 547 */       str3 = (String)localGetField.get("myRoleName", null);
/*     */ 
/* 549 */       localObjectName = (ObjectName)localGetField.get("myRelObjName", null);
/* 550 */       localList1 = (List)Util.cast(localGetField.get("myNewRoleValue", null));
/* 551 */       localList2 = (List)Util.cast(localGetField.get("myOldRoleValue", null));
/* 552 */       localList3 = (List)Util.cast(localGetField.get("myUnregMBeanList", null));
/*     */     }
/*     */     else {
/* 555 */       str1 = (String)localGetField.get("relationId", null);
/* 556 */       str2 = (String)localGetField.get("relationTypeName", null);
/* 557 */       str3 = (String)localGetField.get("roleName", null);
/*     */ 
/* 559 */       localObjectName = (ObjectName)localGetField.get("relationObjName", null);
/* 560 */       localList1 = (List)Util.cast(localGetField.get("newRoleValue", null));
/* 561 */       localList2 = (List)Util.cast(localGetField.get("oldRoleValue", null));
/* 562 */       localList3 = (List)Util.cast(localGetField.get("unregisterMBeanList", null));
/*     */     }
/*     */ 
/* 568 */     String str4 = super.getType();
/* 569 */     if ((!isValidBasic(str4, super.getSource(), str1, str2)) || ((!isValidCreate(str4)) && (!isValidUpdate(str4, str3, localList1, localList2))))
/*     */     {
/* 573 */       super.setSource(null);
/* 574 */       throw new InvalidObjectException("Invalid object read");
/*     */     }
/*     */ 
/* 578 */     this.relationObjName = safeGetObjectName(localObjectName);
/* 579 */     this.newRoleValue = safeGetObjectNameList(localList1);
/* 580 */     this.oldRoleValue = safeGetObjectNameList(localList2);
/* 581 */     this.unregisterMBeanList = safeGetObjectNameList(localList3);
/*     */ 
/* 583 */     this.relationId = str1;
/* 584 */     this.relationTypeName = str2;
/* 585 */     this.roleName = str3;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 594 */     if (compat)
/*     */     {
/* 598 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 599 */       localPutField.put("myNewRoleValue", this.newRoleValue);
/* 600 */       localPutField.put("myOldRoleValue", this.oldRoleValue);
/* 601 */       localPutField.put("myRelId", this.relationId);
/* 602 */       localPutField.put("myRelObjName", this.relationObjName);
/* 603 */       localPutField.put("myRelTypeName", this.relationTypeName);
/* 604 */       localPutField.put("myRoleName", this.roleName);
/* 605 */       localPutField.put("myUnregMBeanList", this.unregisterMBeanList);
/* 606 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 612 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 123 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 124 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 125 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 129 */     if (compat) {
/* 130 */       serialPersistentFields = oldSerialPersistentFields;
/* 131 */       serialVersionUID = -2126464566505527147L;
/*     */     } else {
/* 133 */       serialPersistentFields = newSerialPersistentFields;
/* 134 */       serialVersionUID = -6871117877523310399L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RelationNotification
 * JD-Core Version:    0.6.2
 */
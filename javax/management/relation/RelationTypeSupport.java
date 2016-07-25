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
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class RelationTypeSupport
/*     */   implements RelationType
/*     */ {
/*     */   private static final long oldSerialVersionUID = -8179019472410837190L;
/*     */   private static final long newSerialVersionUID = 4611072955724144607L;
/*  82 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("myTypeName", String.class), new ObjectStreamField("myRoleName2InfoMap", HashMap.class), new ObjectStreamField("myIsInRelServFlg", Boolean.TYPE) };
/*     */ 
/*  90 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("typeName", String.class), new ObjectStreamField("roleName2InfoMap", Map.class), new ObjectStreamField("isInRelationService", Boolean.TYPE) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 107 */   private static boolean compat = false;
/*     */ 
/* 134 */   private String typeName = null;
/*     */ 
/* 140 */   private Map<String, RoleInfo> roleName2InfoMap = new HashMap();
/*     */ 
/* 147 */   private boolean isInRelationService = false;
/*     */ 
/*     */   public RelationTypeSupport(String paramString, RoleInfo[] paramArrayOfRoleInfo)
/*     */     throws IllegalArgumentException, InvalidRelationTypeException
/*     */   {
/* 171 */     if ((paramString == null) || (paramArrayOfRoleInfo == null)) {
/* 172 */       String str = "Invalid parameter.";
/* 173 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 176 */     JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "RelationTypeSupport", paramString);
/*     */ 
/* 181 */     initMembers(paramString, paramArrayOfRoleInfo);
/*     */ 
/* 183 */     JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "RelationTypeSupport");
/*     */   }
/*     */ 
/*     */   protected RelationTypeSupport(String paramString)
/*     */   {
/* 197 */     if (paramString == null) {
/* 198 */       String str = "Invalid parameter.";
/* 199 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 202 */     JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "RelationTypeSupport", paramString);
/*     */ 
/* 205 */     this.typeName = paramString;
/*     */ 
/* 207 */     JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "RelationTypeSupport");
/*     */   }
/*     */ 
/*     */   public String getRelationTypeName()
/*     */   {
/* 222 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public List<RoleInfo> getRoleInfos()
/*     */   {
/* 229 */     return new ArrayList(this.roleName2InfoMap.values());
/*     */   }
/*     */ 
/*     */   public RoleInfo getRoleInfo(String paramString)
/*     */     throws IllegalArgumentException, RoleInfoNotFoundException
/*     */   {
/* 249 */     if (paramString == null) {
/* 250 */       localObject = "Invalid parameter.";
/* 251 */       throw new IllegalArgumentException((String)localObject);
/*     */     }
/*     */ 
/* 254 */     JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "getRoleInfo", paramString);
/*     */ 
/* 258 */     Object localObject = (RoleInfo)this.roleName2InfoMap.get(paramString);
/*     */ 
/* 260 */     if (localObject == null) {
/* 261 */       StringBuilder localStringBuilder = new StringBuilder();
/* 262 */       String str = "No role info for role ";
/* 263 */       localStringBuilder.append(str);
/* 264 */       localStringBuilder.append(paramString);
/* 265 */       throw new RoleInfoNotFoundException(localStringBuilder.toString());
/*     */     }
/*     */ 
/* 268 */     JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "getRoleInfo");
/*     */ 
/* 270 */     return localObject;
/*     */   }
/*     */ 
/*     */   protected void addRoleInfo(RoleInfo paramRoleInfo)
/*     */     throws IllegalArgumentException, InvalidRelationTypeException
/*     */   {
/* 295 */     if (paramRoleInfo == null) {
/* 296 */       str1 = "Invalid parameter.";
/* 297 */       throw new IllegalArgumentException(str1);
/*     */     }
/*     */ 
/* 300 */     JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "addRoleInfo", paramRoleInfo);
/*     */ 
/* 303 */     if (this.isInRelationService)
/*     */     {
/* 305 */       str1 = "Relation type cannot be updated as it is declared in the Relation Service.";
/* 306 */       throw new RuntimeException(str1);
/*     */     }
/*     */ 
/* 309 */     String str1 = paramRoleInfo.getName();
/*     */ 
/* 312 */     if (this.roleName2InfoMap.containsKey(str1)) {
/* 313 */       StringBuilder localStringBuilder = new StringBuilder();
/* 314 */       String str2 = "Two role infos provided for role ";
/* 315 */       localStringBuilder.append(str2);
/* 316 */       localStringBuilder.append(str1);
/* 317 */       throw new InvalidRelationTypeException(localStringBuilder.toString());
/*     */     }
/*     */ 
/* 320 */     this.roleName2InfoMap.put(str1, new RoleInfo(paramRoleInfo));
/*     */ 
/* 322 */     JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "addRoleInfo");
/*     */   }
/*     */ 
/*     */   void setRelationServiceFlag(boolean paramBoolean)
/*     */   {
/* 330 */     this.isInRelationService = paramBoolean;
/*     */   }
/*     */ 
/*     */   private void initMembers(String paramString, RoleInfo[] paramArrayOfRoleInfo)
/*     */     throws IllegalArgumentException, InvalidRelationTypeException
/*     */   {
/* 349 */     if ((paramString == null) || (paramArrayOfRoleInfo == null)) {
/* 350 */       String str = "Invalid parameter.";
/* 351 */       throw new IllegalArgumentException(str);
/*     */     }
/*     */ 
/* 354 */     JmxProperties.RELATION_LOGGER.entering(RelationTypeSupport.class.getName(), "initMembers", paramString);
/*     */ 
/* 357 */     this.typeName = paramString;
/*     */ 
/* 361 */     checkRoleInfos(paramArrayOfRoleInfo);
/*     */ 
/* 363 */     for (int i = 0; i < paramArrayOfRoleInfo.length; i++) {
/* 364 */       RoleInfo localRoleInfo = paramArrayOfRoleInfo[i];
/* 365 */       this.roleName2InfoMap.put(localRoleInfo.getName(), new RoleInfo(localRoleInfo));
/*     */     }
/*     */ 
/* 369 */     JmxProperties.RELATION_LOGGER.exiting(RelationTypeSupport.class.getName(), "initMembers");
/*     */   }
/*     */ 
/*     */   static void checkRoleInfos(RoleInfo[] paramArrayOfRoleInfo)
/*     */     throws IllegalArgumentException, InvalidRelationTypeException
/*     */   {
/* 390 */     if (paramArrayOfRoleInfo == null) {
/* 391 */       localObject = "Invalid parameter.";
/* 392 */       throw new IllegalArgumentException((String)localObject);
/*     */     }
/*     */ 
/* 395 */     if (paramArrayOfRoleInfo.length == 0)
/*     */     {
/* 397 */       localObject = "No role info provided.";
/* 398 */       throw new InvalidRelationTypeException((String)localObject);
/*     */     }
/*     */ 
/* 402 */     Object localObject = new HashSet();
/*     */ 
/* 404 */     for (int i = 0; i < paramArrayOfRoleInfo.length; i++) {
/* 405 */       RoleInfo localRoleInfo = paramArrayOfRoleInfo[i];
/*     */ 
/* 407 */       if (localRoleInfo == null) {
/* 408 */         str1 = "Null role info provided.";
/* 409 */         throw new InvalidRelationTypeException(str1);
/*     */       }
/*     */ 
/* 412 */       String str1 = localRoleInfo.getName();
/*     */ 
/* 415 */       if (((Set)localObject).contains(str1)) {
/* 416 */         StringBuilder localStringBuilder = new StringBuilder();
/* 417 */         String str2 = "Two role infos provided for role ";
/* 418 */         localStringBuilder.append(str2);
/* 419 */         localStringBuilder.append(str1);
/* 420 */         throw new InvalidRelationTypeException(localStringBuilder.toString());
/*     */       }
/* 422 */       ((Set)localObject).add(str1);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 434 */     if (compat)
/*     */     {
/* 438 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 439 */       this.typeName = ((String)localGetField.get("myTypeName", null));
/* 440 */       if (localGetField.defaulted("myTypeName"))
/*     */       {
/* 442 */         throw new NullPointerException("myTypeName");
/*     */       }
/* 444 */       this.roleName2InfoMap = ((Map)Util.cast(localGetField.get("myRoleName2InfoMap", null)));
/* 445 */       if (localGetField.defaulted("myRoleName2InfoMap"))
/*     */       {
/* 447 */         throw new NullPointerException("myRoleName2InfoMap");
/*     */       }
/* 449 */       this.isInRelationService = localGetField.get("myIsInRelServFlg", false);
/* 450 */       if (localGetField.defaulted("myIsInRelServFlg"))
/*     */       {
/* 452 */         throw new NullPointerException("myIsInRelServFlg");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 459 */       paramObjectInputStream.defaultReadObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 469 */     if (compat)
/*     */     {
/* 473 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 474 */       localPutField.put("myTypeName", this.typeName);
/* 475 */       localPutField.put("myRoleName2InfoMap", this.roleName2InfoMap);
/* 476 */       localPutField.put("myIsInRelServFlg", this.isInRelationService);
/* 477 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 483 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 110 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 111 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 112 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 116 */     if (compat) {
/* 117 */       serialPersistentFields = oldSerialPersistentFields;
/* 118 */       serialVersionUID = -8179019472410837190L;
/*     */     } else {
/* 120 */       serialPersistentFields = newSerialPersistentFields;
/* 121 */       serialVersionUID = 4611072955724144607L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RelationTypeSupport
 * JD-Core Version:    0.6.2
 */
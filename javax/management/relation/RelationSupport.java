/*      */ package javax.management.relation;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.ReflectionException;
/*      */ 
/*      */ public class RelationSupport
/*      */   implements RelationSupportMBean, MBeanRegistration
/*      */ {
/*   79 */   private String myRelId = null;
/*      */ 
/*   87 */   private ObjectName myRelServiceName = null;
/*      */ 
/*   99 */   private MBeanServer myRelServiceMBeanServer = null;
/*      */ 
/*  103 */   private String myRelTypeName = null;
/*      */ 
/*  111 */   private final Map<String, Role> myRoleName2ValueMap = new HashMap();
/*      */ 
/*  114 */   private final AtomicBoolean myInRelServFlg = new AtomicBoolean();
/*      */ 
/*      */   public RelationSupport(String paramString1, ObjectName paramObjectName, String paramString2, RoleList paramRoleList)
/*      */     throws InvalidRoleValueException, IllegalArgumentException
/*      */   {
/*  165 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "RelationSupport");
/*      */ 
/*  169 */     initMembers(paramString1, paramObjectName, null, paramString2, paramRoleList);
/*      */ 
/*  175 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "RelationSupport");
/*      */   }
/*      */ 
/*      */   public RelationSupport(String paramString1, ObjectName paramObjectName, MBeanServer paramMBeanServer, String paramString2, RoleList paramRoleList)
/*      */     throws InvalidRoleValueException, IllegalArgumentException
/*      */   {
/*  237 */     if (paramMBeanServer == null) {
/*  238 */       String str = "Invalid parameter.";
/*  239 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/*  242 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "RelationSupport");
/*      */ 
/*  247 */     initMembers(paramString1, paramObjectName, paramMBeanServer, paramString2, paramRoleList);
/*      */ 
/*  253 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "RelationSupport");
/*      */   }
/*      */ 
/*      */   public List<ObjectName> getRole(String paramString)
/*      */     throws IllegalArgumentException, RoleNotFoundException, RelationServiceNotRegisteredException
/*      */   {
/*  284 */     if (paramString == null) {
/*  285 */       localObject = "Invalid parameter.";
/*  286 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  289 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRole", paramString);
/*      */ 
/*  294 */     Object localObject = (List)Util.cast(getRoleInt(paramString, false, null, false));
/*      */ 
/*  297 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRole");
/*  298 */     return localObject;
/*      */   }
/*      */ 
/*      */   public RoleResult getRoles(String[] paramArrayOfString)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException
/*      */   {
/*  322 */     if (paramArrayOfString == null) {
/*  323 */       localObject = "Invalid parameter.";
/*  324 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  327 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRoles");
/*      */ 
/*  330 */     Object localObject = getRolesInt(paramArrayOfString, false, null);
/*      */ 
/*  332 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRoles");
/*  333 */     return localObject;
/*      */   }
/*      */ 
/*      */   public RoleResult getAllRoles()
/*      */     throws RelationServiceNotRegisteredException
/*      */   {
/*  349 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getAllRoles");
/*      */ 
/*  352 */     RoleResult localRoleResult = null;
/*      */     try {
/*  354 */       localRoleResult = getAllRolesInt(false, null);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*      */     }
/*  359 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getAllRoles");
/*  360 */     return localRoleResult;
/*      */   }
/*      */ 
/*      */   public RoleList retrieveAllRoles()
/*      */   {
/*  370 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "retrieveAllRoles");
/*      */     RoleList localRoleList;
/*  374 */     synchronized (this.myRoleName2ValueMap) {
/*  375 */       localRoleList = new RoleList(new ArrayList(this.myRoleName2ValueMap.values()));
/*      */     }
/*      */ 
/*  379 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "retrieveAllRoles");
/*      */ 
/*  381 */     return localRoleList;
/*      */   }
/*      */ 
/*      */   public Integer getRoleCardinality(String paramString)
/*      */     throws IllegalArgumentException, RoleNotFoundException
/*      */   {
/*      */     Object localObject1;
/*  398 */     if (paramString == null) {
/*  399 */       localObject1 = "Invalid parameter.";
/*  400 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  403 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRoleCardinality", paramString);
/*      */ 
/*  408 */     synchronized (this.myRoleName2ValueMap)
/*      */     {
/*  410 */       localObject1 = (Role)this.myRoleName2ValueMap.get(paramString);
/*      */     }
/*  412 */     if (localObject1 == null) {
/*  413 */       int i = 1;
/*      */       try
/*      */       {
/*  419 */         RelationService.throwRoleProblemException(i, paramString);
/*      */       }
/*      */       catch (InvalidRoleValueException localInvalidRoleValueException)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  427 */     List localList = ((Role)localObject1).getRoleValue();
/*      */ 
/*  429 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRoleCardinality");
/*      */ 
/*  431 */     return Integer.valueOf(localList.size());
/*      */   }
/*      */ 
/*      */   public void setRole(Role paramRole)
/*      */     throws IllegalArgumentException, RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationNotFoundException
/*      */   {
/*  474 */     if (paramRole == null) {
/*  475 */       localObject = "Invalid parameter.";
/*  476 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  479 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRole", paramRole);
/*      */ 
/*  483 */     Object localObject = setRoleInt(paramRole, false, null, false);
/*      */ 
/*  485 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRole");
/*      */   }
/*      */ 
/*      */   public RoleResult setRoles(RoleList paramRoleList)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException
/*      */   {
/*  519 */     if (paramRoleList == null) {
/*  520 */       localObject = "Invalid parameter.";
/*  521 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  524 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRoles", paramRoleList);
/*      */ 
/*  527 */     Object localObject = setRolesInt(paramRoleList, false, null);
/*      */ 
/*  529 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRoles");
/*  530 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void handleMBeanUnregistration(ObjectName paramObjectName, String paramString)
/*      */     throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException
/*      */   {
/*  568 */     if ((paramObjectName == null) || (paramString == null)) {
/*  569 */       String str = "Invalid parameter.";
/*  570 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/*  573 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "handleMBeanUnregistration", new Object[] { paramObjectName, paramString });
/*      */ 
/*  579 */     handleMBeanUnregistrationInt(paramObjectName, paramString, false, null);
/*      */ 
/*  584 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "handleMBeanUnregistration");
/*      */   }
/*      */ 
/*      */   public Map<ObjectName, List<String>> getReferencedMBeans()
/*      */   {
/*  597 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getReferencedMBeans");
/*      */ 
/*  600 */     HashMap localHashMap = new HashMap();
/*      */     String str;
/*  603 */     synchronized (this.myRoleName2ValueMap)
/*      */     {
/*  605 */       for (Role localRole : this.myRoleName2ValueMap.values())
/*      */       {
/*  607 */         str = localRole.getRoleName();
/*      */ 
/*  609 */         List localList = localRole.getRoleValue();
/*      */ 
/*  611 */         for (ObjectName localObjectName : localList)
/*      */         {
/*  615 */           Object localObject1 = (List)localHashMap.get(localObjectName);
/*      */ 
/*  618 */           int i = 0;
/*  619 */           if (localObject1 == null) {
/*  620 */             i = 1;
/*  621 */             localObject1 = new ArrayList();
/*      */           }
/*  623 */           ((List)localObject1).add(str);
/*  624 */           if (i != 0) {
/*  625 */             localHashMap.put(localObjectName, localObject1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  631 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getReferencedMBeans");
/*      */ 
/*  633 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   public String getRelationTypeName()
/*      */   {
/*  640 */     return this.myRelTypeName;
/*      */   }
/*      */ 
/*      */   public ObjectName getRelationServiceName()
/*      */   {
/*  649 */     return this.myRelServiceName;
/*      */   }
/*      */ 
/*      */   public String getRelationId()
/*      */   {
/*  659 */     return this.myRelId;
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/*  676 */     this.myRelServiceMBeanServer = paramMBeanServer;
/*  677 */     return paramObjectName;
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/*      */   }
/*      */ 
/*      */   public Boolean isInRelationService()
/*      */   {
/*  705 */     return Boolean.valueOf(this.myInRelServFlg.get());
/*      */   }
/*      */ 
/*      */   public void setRelationServiceManagementFlag(Boolean paramBoolean)
/*      */     throws IllegalArgumentException
/*      */   {
/*  711 */     if (paramBoolean == null) {
/*  712 */       String str = "Invalid parameter.";
/*  713 */       throw new IllegalArgumentException(str);
/*      */     }
/*  715 */     this.myInRelServFlg.set(paramBoolean.booleanValue());
/*      */   }
/*      */ 
/*      */   Object getRoleInt(String paramString, boolean paramBoolean1, RelationService paramRelationService, boolean paramBoolean2)
/*      */     throws IllegalArgumentException, RoleNotFoundException, RelationServiceNotRegisteredException
/*      */   {
/*  776 */     if ((paramString == null) || ((paramBoolean1) && (paramRelationService == null)))
/*      */     {
/*  778 */       String str = "Invalid parameter.";
/*  779 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/*  782 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRoleInt", paramString);
/*      */ 
/*  785 */     int i = 0;
/*      */     Role localRole;
/*  788 */     synchronized (this.myRoleName2ValueMap)
/*      */     {
/*  790 */       localRole = (Role)this.myRoleName2ValueMap.get(paramString);
/*      */     }
/*      */ 
/*  793 */     if (localRole == null) {
/*  794 */       i = 1;
/*      */     }
/*      */     else
/*      */     {
/*  800 */       if (paramBoolean1)
/*      */       {
/*      */         try
/*      */         {
/*  806 */           ??? = paramRelationService.checkRoleReading(paramString, this.myRelTypeName);
/*      */         }
/*      */         catch (RelationTypeNotFoundException localRelationTypeNotFoundException) {
/*  809 */           throw new RuntimeException(localRelationTypeNotFoundException.getMessage());
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*  817 */         Object[] arrayOfObject = new Object[2];
/*  818 */         arrayOfObject[0] = paramString;
/*  819 */         arrayOfObject[1] = this.myRelTypeName;
/*  820 */         String[] arrayOfString = new String[2];
/*  821 */         arrayOfString[0] = "java.lang.String";
/*  822 */         arrayOfString[1] = "java.lang.String";
/*      */         try
/*      */         {
/*  830 */           ??? = (Integer)this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "checkRoleReading", arrayOfObject, arrayOfString);
/*      */         }
/*      */         catch (MBeanException localMBeanException)
/*      */         {
/*  836 */           throw new RuntimeException("incorrect relation type");
/*      */         } catch (ReflectionException localReflectionException) {
/*  838 */           throw new RuntimeException(localReflectionException.getMessage());
/*      */         } catch (InstanceNotFoundException localInstanceNotFoundException) {
/*  840 */           throw new RelationServiceNotRegisteredException(localInstanceNotFoundException.getMessage());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  845 */       i = ((Integer)???).intValue();
/*      */     }
/*      */ 
/*  850 */     if (i == 0)
/*      */     {
/*  853 */       if (!paramBoolean2)
/*      */       {
/*  858 */         ??? = new ArrayList(localRole.getRoleValue());
/*      */       }
/*      */       else
/*      */       {
/*  863 */         ??? = (Role)localRole.clone();
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  869 */       if (!paramBoolean2)
/*      */       {
/*      */         try
/*      */         {
/*  873 */           RelationService.throwRoleProblemException(i, paramString);
/*      */ 
/*  876 */           return null;
/*      */         } catch (InvalidRoleValueException localInvalidRoleValueException) {
/*  878 */           throw new RuntimeException(localInvalidRoleValueException.getMessage());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  884 */       ??? = new RoleUnresolved(paramString, null, i);
/*      */     }
/*      */ 
/*  888 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRoleInt");
/*  889 */     return ???;
/*      */   }
/*      */ 
/*      */   RoleResult getRolesInt(String[] paramArrayOfString, boolean paramBoolean, RelationService paramRelationService)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException
/*      */   {
/*  918 */     if ((paramArrayOfString == null) || ((paramBoolean) && (paramRelationService == null)))
/*      */     {
/*  920 */       localObject1 = "Invalid parameter.";
/*  921 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  924 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getRolesInt");
/*      */ 
/*  927 */     Object localObject1 = new RoleList();
/*  928 */     RoleUnresolvedList localRoleUnresolvedList = new RoleUnresolvedList();
/*      */ 
/*  930 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*      */     {
/*  931 */       String str = paramArrayOfString[i];
/*      */       Object localObject2;
/*      */       try
/*      */       {
/*  939 */         localObject2 = getRoleInt(str, paramBoolean, paramRelationService, true);
/*      */       }
/*      */       catch (RoleNotFoundException localRoleNotFoundException)
/*      */       {
/*  945 */         return null;
/*      */       }
/*      */ 
/*  948 */       if ((localObject2 instanceof Role))
/*      */       {
/*      */         try
/*      */         {
/*  952 */           ((RoleList)localObject1).add((Role)localObject2);
/*      */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/*  954 */           throw new RuntimeException(localIllegalArgumentException1.getMessage());
/*      */         }
/*      */       }
/*  957 */       else if ((localObject2 instanceof RoleUnresolved))
/*      */       {
/*      */         try
/*      */         {
/*  961 */           localRoleUnresolvedList.add((RoleUnresolved)localObject2);
/*      */         } catch (IllegalArgumentException localIllegalArgumentException2) {
/*  963 */           throw new RuntimeException(localIllegalArgumentException2.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  968 */     RoleResult localRoleResult = new RoleResult((RoleList)localObject1, localRoleUnresolvedList);
/*  969 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getRolesInt");
/*      */ 
/*  971 */     return localRoleResult;
/*      */   }
/*      */ 
/*      */   RoleResult getAllRolesInt(boolean paramBoolean, RelationService paramRelationService)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException
/*      */   {
/*      */     Object localObject1;
/*  989 */     if ((paramBoolean) && (paramRelationService == null)) {
/*  990 */       localObject1 = "Invalid parameter.";
/*  991 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  994 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "getAllRolesInt");
/*      */ 
/*  998 */     synchronized (this.myRoleName2ValueMap) {
/*  999 */       localObject1 = new ArrayList(this.myRoleName2ValueMap.keySet());
/*      */     }
/*      */ 
/* 1002 */     ??? = new String[((List)localObject1).size()];
/* 1003 */     ((List)localObject1).toArray((Object[])???);
/*      */ 
/* 1005 */     RoleResult localRoleResult = getRolesInt((String[])???, paramBoolean, paramRelationService);
/*      */ 
/* 1009 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "getAllRolesInt");
/*      */ 
/* 1011 */     return localRoleResult;
/*      */   }
/*      */ 
/*      */   Object setRoleInt(Role paramRole, boolean paramBoolean1, RelationService paramRelationService, boolean paramBoolean2)
/*      */     throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException
/*      */   {
/* 1080 */     if ((paramRole == null) || ((paramBoolean1) && (paramRelationService == null)))
/*      */     {
/* 1082 */       str = "Invalid parameter.";
/* 1083 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 1086 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRoleInt", new Object[] { paramRole, Boolean.valueOf(paramBoolean1), paramRelationService, Boolean.valueOf(paramBoolean2) });
/*      */ 
/* 1090 */     String str = paramRole.getRoleName();
/* 1091 */     int i = 0;
/*      */     Role localRole;
/* 1099 */     synchronized (this.myRoleName2ValueMap) {
/* 1100 */       localRole = (Role)this.myRoleName2ValueMap.get(str);
/*      */     }
/*      */     Boolean localBoolean;
/* 1106 */     if (localRole == null) {
/* 1107 */       localBoolean = Boolean.valueOf(true);
/* 1108 */       ??? = new ArrayList();
/*      */     }
/*      */     else {
/* 1111 */       localBoolean = Boolean.valueOf(false);
/* 1112 */       ??? = localRole.getRoleValue();
/*      */     }
/*      */     try
/*      */     {
/*      */       Integer localInteger;
/* 1120 */       if (paramBoolean1)
/*      */       {
/* 1126 */         localInteger = paramRelationService.checkRoleWriting(paramRole, this.myRelTypeName, localBoolean);
/*      */       }
/*      */       else
/*      */       {
/* 1135 */         localObject3 = new Object[3];
/* 1136 */         localObject3[0] = paramRole;
/* 1137 */         localObject3[1] = this.myRelTypeName;
/* 1138 */         localObject3[2] = localBoolean;
/* 1139 */         String[] arrayOfString = new String[3];
/* 1140 */         arrayOfString[0] = "javax.management.relation.Role";
/* 1141 */         arrayOfString[1] = "java.lang.String";
/* 1142 */         arrayOfString[2] = "java.lang.Boolean";
/*      */ 
/* 1152 */         localInteger = (Integer)this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "checkRoleWriting", (Object[])localObject3, arrayOfString);
/*      */       }
/*      */ 
/* 1159 */       i = localInteger.intValue();
/*      */     }
/*      */     catch (MBeanException localMBeanException)
/*      */     {
/* 1164 */       Object localObject3 = localMBeanException.getTargetException();
/* 1165 */       if ((localObject3 instanceof RelationTypeNotFoundException)) {
/* 1166 */         throw ((RelationTypeNotFoundException)localObject3);
/*      */       }
/*      */ 
/* 1169 */       throw new RuntimeException(((Exception)localObject3).getMessage());
/*      */     }
/*      */     catch (ReflectionException localReflectionException)
/*      */     {
/* 1173 */       throw new RuntimeException(localReflectionException.getMessage());
/*      */     }
/*      */     catch (RelationTypeNotFoundException localRelationTypeNotFoundException) {
/* 1176 */       throw new RuntimeException(localRelationTypeNotFoundException.getMessage());
/*      */     }
/*      */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 1179 */       throw new RelationServiceNotRegisteredException(localInstanceNotFoundException.getMessage());
/*      */     }
/*      */ 
/* 1182 */     Object localObject2 = null;
/*      */ 
/* 1184 */     if (i == 0)
/*      */     {
/* 1186 */       if (!localBoolean.booleanValue())
/*      */       {
/* 1197 */         sendRoleUpdateNotification(paramRole, (List)???, paramBoolean1, paramRelationService);
/*      */ 
/* 1204 */         updateRelationServiceMap(paramRole, (List)???, paramBoolean1, paramRelationService);
/*      */       }
/*      */ 
/* 1212 */       synchronized (this.myRoleName2ValueMap) {
/* 1213 */         this.myRoleName2ValueMap.put(str, (Role)paramRole.clone());
/*      */       }
/*      */ 
/* 1219 */       if (paramBoolean2)
/*      */       {
/* 1221 */         localObject2 = paramRole;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/* 1228 */       if (!paramBoolean2)
/*      */       {
/* 1233 */         RelationService.throwRoleProblemException(i, str);
/*      */ 
/* 1236 */         return null;
/*      */       }
/*      */ 
/* 1241 */       localObject2 = new RoleUnresolved(str, paramRole.getRoleValue(), i);
/*      */     }
/*      */ 
/* 1247 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRoleInt");
/* 1248 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private void sendRoleUpdateNotification(Role paramRole, List<ObjectName> paramList, boolean paramBoolean, RelationService paramRelationService)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException
/*      */   {
/* 1282 */     if ((paramRole == null) || (paramList == null) || ((paramBoolean) && (paramRelationService == null)))
/*      */     {
/* 1285 */       String str = "Invalid parameter.";
/* 1286 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 1289 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "sendRoleUpdateNotification", new Object[] { paramRole, paramList, Boolean.valueOf(paramBoolean), paramRelationService });
/*      */ 
/* 1293 */     if (paramBoolean)
/*      */     {
/*      */       try
/*      */       {
/* 1298 */         paramRelationService.sendRoleUpdateNotification(this.myRelId, paramRole, paramList);
/*      */       }
/*      */       catch (RelationNotFoundException localRelationNotFoundException)
/*      */       {
/* 1302 */         throw new RuntimeException(localRelationNotFoundException.getMessage());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1307 */       Object[] arrayOfObject = new Object[3];
/* 1308 */       arrayOfObject[0] = this.myRelId;
/* 1309 */       arrayOfObject[1] = paramRole;
/* 1310 */       arrayOfObject[2] = paramList;
/* 1311 */       String[] arrayOfString = new String[3];
/* 1312 */       arrayOfString[0] = "java.lang.String";
/* 1313 */       arrayOfString[1] = "javax.management.relation.Role";
/* 1314 */       arrayOfString[2] = "java.util.List";
/*      */       try
/*      */       {
/* 1325 */         this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "sendRoleUpdateNotification", arrayOfObject, arrayOfString);
/*      */       }
/*      */       catch (ReflectionException localReflectionException)
/*      */       {
/* 1330 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 1332 */         throw new RelationServiceNotRegisteredException(localInstanceNotFoundException.getMessage());
/*      */       }
/*      */       catch (MBeanException localMBeanException) {
/* 1335 */         Exception localException = localMBeanException.getTargetException();
/* 1336 */         if ((localException instanceof RelationNotFoundException)) {
/* 1337 */           throw ((RelationNotFoundException)localException);
/*      */         }
/* 1339 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1344 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "sendRoleUpdateNotification");
/*      */   }
/*      */ 
/*      */   private void updateRelationServiceMap(Role paramRole, List<ObjectName> paramList, boolean paramBoolean, RelationService paramRelationService)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException
/*      */   {
/* 1379 */     if ((paramRole == null) || (paramList == null) || ((paramBoolean) && (paramRelationService == null)))
/*      */     {
/* 1382 */       String str = "Invalid parameter.";
/* 1383 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 1386 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "updateRelationServiceMap", new Object[] { paramRole, paramList, Boolean.valueOf(paramBoolean), paramRelationService });
/*      */ 
/* 1390 */     if (paramBoolean)
/*      */     {
/*      */       try
/*      */       {
/* 1394 */         paramRelationService.updateRoleMap(this.myRelId, paramRole, paramList);
/*      */       }
/*      */       catch (RelationNotFoundException localRelationNotFoundException)
/*      */       {
/* 1398 */         throw new RuntimeException(localRelationNotFoundException.getMessage());
/*      */       }
/*      */     }
/*      */     else {
/* 1402 */       Object[] arrayOfObject = new Object[3];
/* 1403 */       arrayOfObject[0] = this.myRelId;
/* 1404 */       arrayOfObject[1] = paramRole;
/* 1405 */       arrayOfObject[2] = paramList;
/* 1406 */       String[] arrayOfString = new String[3];
/* 1407 */       arrayOfString[0] = "java.lang.String";
/* 1408 */       arrayOfString[1] = "javax.management.relation.Role";
/* 1409 */       arrayOfString[2] = "java.util.List";
/*      */       try
/*      */       {
/* 1417 */         this.myRelServiceMBeanServer.invoke(this.myRelServiceName, "updateRoleMap", arrayOfObject, arrayOfString);
/*      */       }
/*      */       catch (ReflectionException localReflectionException)
/*      */       {
/* 1422 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 1424 */         throw new RelationServiceNotRegisteredException(localInstanceNotFoundException.getMessage());
/*      */       }
/*      */       catch (MBeanException localMBeanException) {
/* 1427 */         Exception localException = localMBeanException.getTargetException();
/* 1428 */         if ((localException instanceof RelationNotFoundException)) {
/* 1429 */           throw ((RelationNotFoundException)localException);
/*      */         }
/* 1431 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1436 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "updateRelationServiceMap");
/*      */   }
/*      */ 
/*      */   RoleResult setRolesInt(RoleList paramRoleList, boolean paramBoolean, RelationService paramRelationService)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException
/*      */   {
/* 1481 */     if ((paramRoleList == null) || ((paramBoolean) && (paramRelationService == null)))
/*      */     {
/* 1483 */       localObject1 = "Invalid parameter.";
/* 1484 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1487 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "setRolesInt", new Object[] { paramRoleList, Boolean.valueOf(paramBoolean), paramRelationService });
/*      */ 
/* 1491 */     Object localObject1 = new RoleList();
/* 1492 */     RoleUnresolvedList localRoleUnresolvedList = new RoleUnresolvedList();
/*      */ 
/* 1494 */     for (Object localObject2 = paramRoleList.asList().iterator(); ((Iterator)localObject2).hasNext(); ) { Role localRole = (Role)((Iterator)localObject2).next();
/*      */ 
/* 1496 */       Object localObject3 = null;
/*      */       try
/*      */       {
/* 1505 */         localObject3 = setRoleInt(localRole, paramBoolean, paramRelationService, true);
/*      */       }
/*      */       catch (RoleNotFoundException localRoleNotFoundException)
/*      */       {
/*      */       }
/*      */       catch (InvalidRoleValueException localInvalidRoleValueException)
/*      */       {
/*      */       }
/*      */ 
/* 1515 */       if ((localObject3 instanceof Role))
/*      */       {
/*      */         try
/*      */         {
/* 1519 */           ((RoleList)localObject1).add((Role)localObject3);
/*      */         } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 1521 */           throw new RuntimeException(localIllegalArgumentException1.getMessage());
/*      */         }
/*      */       }
/* 1524 */       else if ((localObject3 instanceof RoleUnresolved))
/*      */       {
/*      */         try
/*      */         {
/* 1528 */           localRoleUnresolvedList.add((RoleUnresolved)localObject3);
/*      */         } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 1530 */           throw new RuntimeException(localIllegalArgumentException2.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1535 */     localObject2 = new RoleResult((RoleList)localObject1, localRoleUnresolvedList);
/*      */ 
/* 1537 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "setRolesInt");
/* 1538 */     return localObject2;
/*      */   }
/*      */ 
/*      */   private void initMembers(String paramString1, ObjectName paramObjectName, MBeanServer paramMBeanServer, String paramString2, RoleList paramRoleList)
/*      */     throws InvalidRoleValueException, IllegalArgumentException
/*      */   {
/* 1574 */     if ((paramString1 == null) || (paramObjectName == null) || (paramString2 == null))
/*      */     {
/* 1577 */       String str = "Invalid parameter.";
/* 1578 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 1581 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "initMembers", new Object[] { paramString1, paramObjectName, paramMBeanServer, paramString2, paramRoleList });
/*      */ 
/* 1585 */     this.myRelId = paramString1;
/* 1586 */     this.myRelServiceName = paramObjectName;
/* 1587 */     this.myRelServiceMBeanServer = paramMBeanServer;
/* 1588 */     this.myRelTypeName = paramString2;
/*      */ 
/* 1590 */     initRoleMap(paramRoleList);
/*      */ 
/* 1592 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "initMembers");
/*      */   }
/*      */ 
/*      */   private void initRoleMap(RoleList paramRoleList)
/*      */     throws InvalidRoleValueException
/*      */   {
/* 1607 */     if (paramRoleList == null) {
/* 1608 */       return;
/*      */     }
/*      */ 
/* 1611 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "initRoleMap", paramRoleList);
/*      */ 
/* 1614 */     synchronized (this.myRoleName2ValueMap)
/*      */     {
/* 1616 */       for (Role localRole : paramRoleList.asList())
/*      */       {
/* 1620 */         String str = localRole.getRoleName();
/*      */ 
/* 1622 */         if (this.myRoleName2ValueMap.containsKey(str))
/*      */         {
/* 1624 */           StringBuilder localStringBuilder = new StringBuilder("Role name ");
/* 1625 */           localStringBuilder.append(str);
/* 1626 */           localStringBuilder.append(" used for two roles.");
/* 1627 */           throw new InvalidRoleValueException(localStringBuilder.toString());
/*      */         }
/*      */ 
/* 1630 */         this.myRoleName2ValueMap.put(str, (Role)localRole.clone());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1635 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "initRoleMap");
/*      */   }
/*      */ 
/*      */   void handleMBeanUnregistrationInt(ObjectName paramObjectName, String paramString, boolean paramBoolean, RelationService paramRelationService)
/*      */     throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException
/*      */   {
/*      */     Object localObject1;
/* 1688 */     if ((paramObjectName == null) || (paramString == null) || ((paramBoolean) && (paramRelationService == null)))
/*      */     {
/* 1691 */       localObject1 = "Invalid parameter.";
/* 1692 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1695 */     JmxProperties.RELATION_LOGGER.entering(RelationSupport.class.getName(), "handleMBeanUnregistrationInt", new Object[] { paramObjectName, paramString, Boolean.valueOf(paramBoolean), paramRelationService });
/*      */ 
/* 1701 */     synchronized (this.myRoleName2ValueMap) {
/* 1702 */       localObject1 = (Role)this.myRoleName2ValueMap.get(paramString);
/*      */     }
/*      */ 
/* 1705 */     if (localObject1 == null) {
/* 1706 */       ??? = new StringBuilder();
/* 1707 */       localObject3 = "No role with name ";
/* 1708 */       ((StringBuilder)???).append((String)localObject3);
/* 1709 */       ((StringBuilder)???).append(paramString);
/* 1710 */       throw new RoleNotFoundException(((StringBuilder)???).toString());
/*      */     }
/* 1712 */     ??? = ((Role)localObject1).getRoleValue();
/*      */ 
/* 1716 */     Object localObject3 = new ArrayList((Collection)???);
/* 1717 */     ((List)localObject3).remove(paramObjectName);
/* 1718 */     Role localRole = new Role(paramString, (List)localObject3);
/*      */ 
/* 1723 */     Object localObject4 = setRoleInt(localRole, paramBoolean, paramRelationService, false);
/*      */ 
/* 1726 */     JmxProperties.RELATION_LOGGER.exiting(RelationSupport.class.getName(), "handleMBeanUnregistrationInt");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RelationSupport
 * JD-Core Version:    0.6.2
 */
/*      */ package javax.management.relation;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.Attribute;
/*      */ import javax.management.AttributeNotFoundException;
/*      */ import javax.management.InstanceNotFoundException;
/*      */ import javax.management.InvalidAttributeValueException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MBeanServerDelegate;
/*      */ import javax.management.MBeanServerNotification;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcasterSupport;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.ReflectionException;
/*      */ 
/*      */ public class RelationService extends NotificationBroadcasterSupport
/*      */   implements RelationServiceMBean, MBeanRegistration, NotificationListener
/*      */ {
/*   84 */   private Map<String, Object> myRelId2ObjMap = new HashMap();
/*      */ 
/*   88 */   private Map<String, String> myRelId2RelTypeMap = new HashMap();
/*      */ 
/*   92 */   private Map<ObjectName, String> myRelMBeanObjName2RelIdMap = new HashMap();
/*      */ 
/*   97 */   private Map<String, RelationType> myRelType2ObjMap = new HashMap();
/*      */ 
/*  103 */   private Map<String, List<String>> myRelType2RelIdsMap = new HashMap();
/*      */ 
/*  111 */   private final Map<ObjectName, Map<String, List<String>>> myRefedMBeanObjName2RelIdsMap = new HashMap();
/*      */ 
/*  121 */   private boolean myPurgeFlag = true;
/*      */ 
/*  126 */   private final AtomicLong atomicSeqNo = new AtomicLong();
/*      */ 
/*  129 */   private ObjectName myObjName = null;
/*      */ 
/*  132 */   private MBeanServer myMBeanServer = null;
/*      */ 
/*  136 */   private MBeanServerNotificationFilter myUnregNtfFilter = null;
/*      */ 
/*  141 */   private List<MBeanServerNotification> myUnregNtfList = new ArrayList();
/*      */ 
/*      */   public RelationService(boolean paramBoolean)
/*      */   {
/*  160 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "RelationService");
/*      */ 
/*  163 */     setPurgeFlag(paramBoolean);
/*      */ 
/*  165 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "RelationService");
/*      */   }
/*      */ 
/*      */   public void isActive()
/*      */     throws RelationServiceNotRegisteredException
/*      */   {
/*  180 */     if (this.myMBeanServer == null)
/*      */     {
/*  183 */       String str = "Relation Service not registered in the MBean Server.";
/*      */ 
/*  185 */       throw new RelationServiceNotRegisteredException(str);
/*      */     }
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/*  201 */     this.myMBeanServer = paramMBeanServer;
/*  202 */     this.myObjName = paramObjectName;
/*  203 */     return paramObjectName;
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
/*      */   public boolean getPurgeFlag()
/*      */   {
/*  239 */     return this.myPurgeFlag;
/*      */   }
/*      */ 
/*      */   public void setPurgeFlag(boolean paramBoolean)
/*      */   {
/*  256 */     this.myPurgeFlag = paramBoolean;
/*      */   }
/*      */ 
/*      */   public void createRelationType(String paramString, RoleInfo[] paramArrayOfRoleInfo)
/*      */     throws IllegalArgumentException, InvalidRelationTypeException
/*      */   {
/*  284 */     if ((paramString == null) || (paramArrayOfRoleInfo == null)) {
/*  285 */       localObject = "Invalid parameter.";
/*  286 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  289 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "createRelationType", paramString);
/*      */ 
/*  293 */     Object localObject = new RelationTypeSupport(paramString, paramArrayOfRoleInfo);
/*      */ 
/*  296 */     addRelationTypeInt((RelationType)localObject);
/*      */ 
/*  298 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "createRelationType");
/*      */   }
/*      */ 
/*      */   public void addRelationType(RelationType paramRelationType)
/*      */     throws IllegalArgumentException, InvalidRelationTypeException
/*      */   {
/*  323 */     if (paramRelationType == null) {
/*  324 */       localObject1 = "Invalid parameter.";
/*  325 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  328 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelationType");
/*      */ 
/*  332 */     Object localObject1 = paramRelationType.getRoleInfos();
/*  333 */     if (localObject1 == null) {
/*  334 */       localObject2 = "No role info provided.";
/*  335 */       throw new InvalidRelationTypeException((String)localObject2);
/*      */     }
/*      */ 
/*  338 */     Object localObject2 = new RoleInfo[((List)localObject1).size()];
/*  339 */     int i = 0;
/*  340 */     for (RoleInfo localRoleInfo : (List)localObject1) {
/*  341 */       localObject2[i] = localRoleInfo;
/*  342 */       i++;
/*      */     }
/*      */ 
/*  345 */     RelationTypeSupport.checkRoleInfos((RoleInfo[])localObject2);
/*      */ 
/*  347 */     addRelationTypeInt(paramRelationType);
/*      */ 
/*  349 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelationType");
/*      */   }
/*      */ 
/*      */   public List<String> getAllRelationTypeNames()
/*      */   {
/*      */     ArrayList localArrayList;
/*  361 */     synchronized (this.myRelType2ObjMap) {
/*  362 */       localArrayList = new ArrayList(this.myRelType2ObjMap.keySet());
/*      */     }
/*  364 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public List<RoleInfo> getRoleInfos(String paramString)
/*      */     throws IllegalArgumentException, RelationTypeNotFoundException
/*      */   {
/*  383 */     if (paramString == null) {
/*  384 */       localObject = "Invalid parameter.";
/*  385 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  388 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoleInfos", paramString);
/*      */ 
/*  392 */     Object localObject = getRelationType(paramString);
/*      */ 
/*  394 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoleInfos");
/*      */ 
/*  396 */     return ((RelationType)localObject).getRoleInfos();
/*      */   }
/*      */ 
/*      */   public RoleInfo getRoleInfo(String paramString1, String paramString2)
/*      */     throws IllegalArgumentException, RelationTypeNotFoundException, RoleInfoNotFoundException
/*      */   {
/*  419 */     if ((paramString1 == null) || (paramString2 == null)) {
/*  420 */       localObject = "Invalid parameter.";
/*  421 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  424 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoleInfo", new Object[] { paramString1, paramString2 });
/*      */ 
/*  428 */     Object localObject = getRelationType(paramString1);
/*      */ 
/*  431 */     RoleInfo localRoleInfo = ((RelationType)localObject).getRoleInfo(paramString2);
/*      */ 
/*  433 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoleInfo");
/*      */ 
/*  435 */     return localRoleInfo;
/*      */   }
/*      */ 
/*      */   public void removeRelationType(String paramString)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationTypeNotFoundException
/*      */   {
/*  457 */     isActive();
/*      */ 
/*  459 */     if (paramString == null) {
/*  460 */       localObject1 = "Invalid parameter.";
/*  461 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  464 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "removeRelationType", paramString);
/*      */ 
/*  469 */     Object localObject1 = getRelationType(paramString);
/*      */ 
/*  472 */     ArrayList localArrayList = null;
/*      */     Object localObject2;
/*  473 */     synchronized (this.myRelType2RelIdsMap)
/*      */     {
/*  476 */       localObject2 = (List)this.myRelType2RelIdsMap.get(paramString);
/*      */ 
/*  478 */       if (localObject2 != null) {
/*  479 */         localArrayList = new ArrayList((Collection)localObject2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  484 */     synchronized (this.myRelType2ObjMap) {
/*  485 */       this.myRelType2ObjMap.remove(paramString);
/*      */     }
/*  487 */     synchronized (this.myRelType2RelIdsMap) {
/*  488 */       this.myRelType2RelIdsMap.remove(paramString);
/*      */     }
/*      */ 
/*  492 */     if (localArrayList != null) {
/*  493 */       for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { localObject2 = (String)((Iterator)???).next();
/*      */         try
/*      */         {
/*  500 */           removeRelation((String)localObject2);
/*      */         } catch (RelationNotFoundException localRelationNotFoundException) {
/*  502 */           throw new RuntimeException(localRelationNotFoundException.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  507 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeRelationType");
/*      */   }
/*      */ 
/*      */   public void createRelation(String paramString1, String paramString2, RoleList paramRoleList)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RoleNotFoundException, InvalidRelationIdException, RelationTypeNotFoundException, InvalidRoleValueException
/*      */   {
/*  561 */     isActive();
/*      */ 
/*  563 */     if ((paramString1 == null) || (paramString2 == null))
/*      */     {
/*  565 */       localObject = "Invalid parameter.";
/*  566 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  569 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "createRelation", new Object[] { paramString1, paramString2, paramRoleList });
/*      */ 
/*  575 */     Object localObject = new RelationSupport(paramString1, this.myObjName, paramString2, paramRoleList);
/*      */ 
/*  585 */     addRelationInt(true, (RelationSupport)localObject, null, paramString1, paramString2, paramRoleList);
/*      */ 
/*  591 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "createRelation");
/*      */   }
/*      */ 
/*      */   public void addRelation(ObjectName paramObjectName)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, NoSuchMethodException, InvalidRelationIdException, InstanceNotFoundException, InvalidRelationServiceException, RelationTypeNotFoundException, RoleNotFoundException, InvalidRoleValueException
/*      */   {
/*      */     String str1;
/*  652 */     if (paramObjectName == null) {
/*  653 */       str1 = "Invalid parameter.";
/*  654 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/*  657 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelation", paramObjectName);
/*      */ 
/*  661 */     isActive();
/*      */ 
/*  666 */     if (!this.myMBeanServer.isInstanceOf(paramObjectName, "javax.management.relation.Relation")) {
/*  667 */       str1 = "This MBean does not implement the Relation interface.";
/*  668 */       throw new NoSuchMethodException(str1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  677 */       str1 = (String)this.myMBeanServer.getAttribute(paramObjectName, "RelationId");
/*      */     }
/*      */     catch (MBeanException localMBeanException1)
/*      */     {
/*  681 */       throw new RuntimeException(localMBeanException1.getTargetException().getMessage());
/*      */     }
/*      */     catch (ReflectionException localReflectionException1) {
/*  684 */       throw new RuntimeException(localReflectionException1.getMessage());
/*      */     } catch (AttributeNotFoundException localAttributeNotFoundException1) {
/*  686 */       throw new RuntimeException(localAttributeNotFoundException1.getMessage());
/*      */     }
/*      */     Object localObject1;
/*  689 */     if (str1 == null) {
/*  690 */       localObject1 = "This MBean does not provide a relation id.";
/*  691 */       throw new InvalidRelationIdException((String)localObject1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  700 */       localObject1 = (ObjectName)this.myMBeanServer.getAttribute(paramObjectName, "RelationServiceName");
/*      */     }
/*      */     catch (MBeanException localMBeanException2)
/*      */     {
/*  705 */       throw new RuntimeException(localMBeanException2.getTargetException().getMessage());
/*      */     }
/*      */     catch (ReflectionException localReflectionException2) {
/*  708 */       throw new RuntimeException(localReflectionException2.getMessage());
/*      */     } catch (AttributeNotFoundException localAttributeNotFoundException2) {
/*  710 */       throw new RuntimeException(localAttributeNotFoundException2.getMessage());
/*      */     }
/*      */ 
/*  713 */     int i = 0;
/*  714 */     if (localObject1 == null) {
/*  715 */       i = 1;
/*      */     }
/*  717 */     else if (!((ObjectName)localObject1).equals(this.myObjName))
/*  718 */       i = 1;
/*      */     String str2;
/*  720 */     if (i != 0) {
/*  721 */       str2 = "The Relation Service referenced in the MBean is not the current one.";
/*  722 */       throw new InvalidRelationServiceException(str2);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  730 */       str2 = (String)this.myMBeanServer.getAttribute(paramObjectName, "RelationTypeName");
/*      */     }
/*      */     catch (MBeanException localMBeanException3)
/*      */     {
/*  734 */       throw new RuntimeException(localMBeanException3.getTargetException().getMessage());
/*      */     }
/*      */     catch (ReflectionException localReflectionException3) {
/*  737 */       throw new RuntimeException(localReflectionException3.getMessage());
/*      */     } catch (AttributeNotFoundException localAttributeNotFoundException3) {
/*  739 */       throw new RuntimeException(localAttributeNotFoundException3.getMessage());
/*      */     }
/*      */     Object localObject2;
/*  741 */     if (str2 == null) {
/*  742 */       localObject2 = "No relation type provided.";
/*  743 */       throw new RelationTypeNotFoundException((String)localObject2);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  751 */       localObject2 = (RoleList)this.myMBeanServer.invoke(paramObjectName, "retrieveAllRoles", null, null);
/*      */     }
/*      */     catch (MBeanException localMBeanException4)
/*      */     {
/*  756 */       throw new RuntimeException(localMBeanException4.getTargetException().getMessage());
/*      */     }
/*      */     catch (ReflectionException localReflectionException4) {
/*  759 */       throw new RuntimeException(localReflectionException4.getMessage());
/*      */     }
/*      */ 
/*  764 */     addRelationInt(false, null, paramObjectName, str1, str2, (RoleList)localObject2);
/*      */ 
/*  771 */     synchronized (this.myRelMBeanObjName2RelIdMap) {
/*  772 */       this.myRelMBeanObjName2RelIdMap.put(paramObjectName, str1);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  780 */       this.myMBeanServer.setAttribute(paramObjectName, new Attribute("RelationServiceManagementFlag", Boolean.TRUE));
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/*  790 */     ArrayList localArrayList = new ArrayList();
/*  791 */     localArrayList.add(paramObjectName);
/*  792 */     updateUnregistrationListener(localArrayList, null);
/*      */ 
/*  794 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelation");
/*      */   }
/*      */ 
/*      */   public ObjectName isRelationMBean(String paramString)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/*  817 */     if (paramString == null) {
/*  818 */       localObject = "Invalid parameter.";
/*  819 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  822 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "isRelationMBean", paramString);
/*      */ 
/*  826 */     Object localObject = getRelation(paramString);
/*  827 */     if ((localObject instanceof ObjectName)) {
/*  828 */       return (ObjectName)localObject;
/*      */     }
/*  830 */     return null;
/*      */   }
/*      */ 
/*      */   public String isRelation(ObjectName paramObjectName)
/*      */     throws IllegalArgumentException
/*      */   {
/*  848 */     if (paramObjectName == null) {
/*  849 */       localObject1 = "Invalid parameter.";
/*  850 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/*  853 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "isRelation", paramObjectName);
/*      */ 
/*  856 */     Object localObject1 = null;
/*  857 */     synchronized (this.myRelMBeanObjName2RelIdMap) {
/*  858 */       String str = (String)this.myRelMBeanObjName2RelIdMap.get(paramObjectName);
/*  859 */       if (str != null) {
/*  860 */         localObject1 = str;
/*      */       }
/*      */     }
/*  863 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public Boolean hasRelation(String paramString)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     Object localObject;
/*  879 */     if (paramString == null) {
/*  880 */       localObject = "Invalid parameter.";
/*  881 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  884 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "hasRelation", paramString);
/*      */     try
/*      */     {
/*  889 */       localObject = getRelation(paramString);
/*  890 */       return Boolean.valueOf(true); } catch (RelationNotFoundException localRelationNotFoundException) {
/*      */     }
/*  892 */     return Boolean.valueOf(false);
/*      */   }
/*      */ 
/*      */   public List<String> getAllRelationIds()
/*      */   {
/*      */     ArrayList localArrayList;
/*  904 */     synchronized (this.myRelId2ObjMap) {
/*  905 */       localArrayList = new ArrayList(this.myRelId2ObjMap.keySet());
/*      */     }
/*  907 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public Integer checkRoleReading(String paramString1, String paramString2)
/*      */     throws IllegalArgumentException, RelationTypeNotFoundException
/*      */   {
/*      */     Object localObject;
/*  931 */     if ((paramString1 == null) || (paramString2 == null)) {
/*  932 */       localObject = "Invalid parameter.";
/*  933 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  936 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "checkRoleReading", new Object[] { paramString1, paramString2 });
/*      */ 
/*  942 */     RelationType localRelationType = getRelationType(paramString2);
/*      */     try
/*      */     {
/*  947 */       RoleInfo localRoleInfo = localRelationType.getRoleInfo(paramString1);
/*      */ 
/*  949 */       localObject = checkRoleInt(1, paramString1, null, localRoleInfo, false);
/*      */     }
/*      */     catch (RoleInfoNotFoundException localRoleInfoNotFoundException)
/*      */     {
/*  956 */       localObject = Integer.valueOf(1);
/*      */     }
/*      */ 
/*  959 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleReading");
/*      */ 
/*  961 */     return localObject;
/*      */   }
/*      */ 
/*      */   public Integer checkRoleWriting(Role paramRole, String paramString, Boolean paramBoolean)
/*      */     throws IllegalArgumentException, RelationTypeNotFoundException
/*      */   {
/*  991 */     if ((paramRole == null) || (paramString == null) || (paramBoolean == null))
/*      */     {
/*  994 */       localObject = "Invalid parameter.";
/*  995 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/*  998 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "checkRoleWriting", new Object[] { paramRole, paramString, paramBoolean });
/*      */ 
/* 1003 */     Object localObject = getRelationType(paramString);
/*      */ 
/* 1005 */     String str = paramRole.getRoleName();
/* 1006 */     List localList = paramRole.getRoleValue();
/* 1007 */     boolean bool = true;
/* 1008 */     if (paramBoolean.booleanValue()) {
/* 1009 */       bool = false;
/*      */     }
/*      */     RoleInfo localRoleInfo;
/*      */     try
/*      */     {
/* 1014 */       localRoleInfo = ((RelationType)localObject).getRoleInfo(str);
/*      */     } catch (RoleInfoNotFoundException localRoleInfoNotFoundException) {
/* 1016 */       JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleWriting");
/*      */ 
/* 1018 */       return Integer.valueOf(1);
/*      */     }
/*      */ 
/* 1021 */     Integer localInteger = checkRoleInt(2, str, localList, localRoleInfo, bool);
/*      */ 
/* 1027 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleWriting");
/*      */ 
/* 1029 */     return localInteger;
/*      */   }
/*      */ 
/*      */   public void sendRelationCreationNotification(String paramString)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 1053 */     if (paramString == null) {
/* 1054 */       localObject = "Invalid parameter.";
/* 1055 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 1058 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendRelationCreationNotification", paramString);
/*      */ 
/* 1062 */     Object localObject = new StringBuilder("Creation of relation ");
/* 1063 */     ((StringBuilder)localObject).append(paramString);
/*      */ 
/* 1066 */     sendNotificationInt(1, ((StringBuilder)localObject).toString(), paramString, null, null, null, null);
/*      */ 
/* 1074 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendRelationCreationNotification");
/*      */   }
/*      */ 
/*      */   public void sendRoleUpdateNotification(String paramString, Role paramRole, List<ObjectName> paramList)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 1107 */     if ((paramString == null) || (paramRole == null) || (paramList == null))
/*      */     {
/* 1110 */       str1 = "Invalid parameter.";
/* 1111 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/* 1114 */     if (!(paramList instanceof ArrayList)) {
/* 1115 */       paramList = new ArrayList(paramList);
/*      */     }
/* 1117 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendRoleUpdateNotification", new Object[] { paramString, paramRole, paramList });
/*      */ 
/* 1121 */     String str1 = paramRole.getRoleName();
/* 1122 */     List localList = paramRole.getRoleValue();
/*      */ 
/* 1125 */     String str2 = Role.roleValueToString(localList);
/* 1126 */     String str3 = Role.roleValueToString(paramList);
/* 1127 */     StringBuilder localStringBuilder = new StringBuilder("Value of role ");
/* 1128 */     localStringBuilder.append(str1);
/* 1129 */     localStringBuilder.append(" has changed\nOld value:\n");
/* 1130 */     localStringBuilder.append(str3);
/* 1131 */     localStringBuilder.append("\nNew value:\n");
/* 1132 */     localStringBuilder.append(str2);
/*      */ 
/* 1135 */     sendNotificationInt(2, localStringBuilder.toString(), paramString, null, str1, localList, paramList);
/*      */ 
/* 1143 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendRoleUpdateNotification");
/*      */   }
/*      */ 
/*      */   public void sendRelationRemovalNotification(String paramString, List<ObjectName> paramList)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 1170 */     if (paramString == null) {
/* 1171 */       String str = "Invalid parameter";
/* 1172 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 1175 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendRelationRemovalNotification", new Object[] { paramString, paramList });
/*      */ 
/* 1180 */     sendNotificationInt(3, "Removal of relation " + paramString, paramString, paramList, null, null, null);
/*      */ 
/* 1189 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendRelationRemovalNotification");
/*      */   }
/*      */ 
/*      */   public void updateRoleMap(String paramString, Role paramRole, List<ObjectName> paramList)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException
/*      */   {
/* 1222 */     if ((paramString == null) || (paramRole == null) || (paramList == null))
/*      */     {
/* 1225 */       localObject1 = "Invalid parameter.";
/* 1226 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1229 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "updateRoleMap", new Object[] { paramString, paramRole, paramList });
/*      */ 
/* 1233 */     isActive();
/*      */ 
/* 1237 */     Object localObject1 = getRelation(paramString);
/*      */ 
/* 1239 */     String str = paramRole.getRoleName();
/* 1240 */     List localList = paramRole.getRoleValue();
/*      */ 
/* 1243 */     ArrayList localArrayList1 = new ArrayList(paramList);
/*      */ 
/* 1247 */     ArrayList localArrayList2 = new ArrayList();
/*      */ 
/* 1249 */     for (Object localObject2 = localList.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (ObjectName)((Iterator)localObject2).next();
/*      */ 
/* 1255 */       int i = localArrayList1.indexOf(localObject3);
/*      */ 
/* 1257 */       if (i == -1)
/*      */       {
/* 1263 */         bool = addNewMBeanReference((ObjectName)localObject3, paramString, str);
/*      */ 
/* 1267 */         if (bool)
/*      */         {
/* 1269 */           localArrayList2.add(localObject3);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1277 */         localArrayList1.remove(i);
/*      */       }
/*      */     }
/* 1286 */     boolean bool;
/* 1282 */     localObject2 = new ArrayList();
/*      */ 
/* 1286 */     for (Object localObject3 = localArrayList1.iterator(); ((Iterator)localObject3).hasNext(); ) { ObjectName localObjectName = (ObjectName)((Iterator)localObject3).next();
/*      */ 
/* 1290 */       bool = removeMBeanReference(localObjectName, paramString, str, false);
/*      */ 
/* 1295 */       if (bool)
/*      */       {
/* 1297 */         ((List)localObject2).add(localObjectName);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1304 */     updateUnregistrationListener(localArrayList2, (List)localObject2);
/*      */ 
/* 1306 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "updateRoleMap");
/*      */   }
/*      */ 
/*      */   public void removeRelation(String paramString)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 1334 */     isActive();
/*      */ 
/* 1336 */     if (paramString == null) {
/* 1337 */       localObject1 = "Invalid parameter.";
/* 1338 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1341 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "removeRelation", paramString);
/*      */ 
/* 1346 */     Object localObject1 = getRelation(paramString);
/*      */ 
/* 1349 */     if ((localObject1 instanceof ObjectName)) {
/* 1350 */       localArrayList1 = new ArrayList();
/* 1351 */       localArrayList1.add((ObjectName)localObject1);
/*      */ 
/* 1353 */       updateUnregistrationListener(null, localArrayList1);
/*      */     }
/*      */ 
/* 1367 */     sendRelationRemovalNotification(paramString, null);
/*      */ 
/* 1379 */     ArrayList localArrayList1 = new ArrayList();
/*      */ 
/* 1382 */     ArrayList localArrayList2 = new ArrayList();
/*      */     Iterator localIterator;
/*      */     Object localObject2;
/* 1384 */     synchronized (this.myRefedMBeanObjName2RelIdsMap)
/*      */     {
/* 1387 */       for (localIterator = this.myRefedMBeanObjName2RelIdsMap.keySet().iterator(); localIterator.hasNext(); ) { localObject2 = (ObjectName)localIterator.next();
/*      */ 
/* 1390 */         Map localMap = (Map)this.myRefedMBeanObjName2RelIdsMap.get(localObject2);
/*      */ 
/* 1393 */         if (localMap.containsKey(paramString)) {
/* 1394 */           localMap.remove(paramString);
/* 1395 */           localArrayList1.add(localObject2);
/*      */         }
/*      */ 
/* 1398 */         if (localMap.isEmpty())
/*      */         {
/* 1402 */           localArrayList2.add(localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1408 */       for (localIterator = localArrayList2.iterator(); localIterator.hasNext(); ) { localObject2 = (ObjectName)localIterator.next();
/* 1409 */         this.myRefedMBeanObjName2RelIdsMap.remove(localObject2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1414 */     synchronized (this.myRelId2ObjMap) {
/* 1415 */       this.myRelId2ObjMap.remove(paramString);
/*      */     }
/*      */ 
/* 1418 */     if ((localObject1 instanceof ObjectName))
/*      */     {
/* 1420 */       synchronized (this.myRelMBeanObjName2RelIdMap) {
/* 1421 */         this.myRelMBeanObjName2RelIdMap.remove((ObjectName)localObject1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1428 */     synchronized (this.myRelId2RelTypeMap) {
/* 1429 */       ??? = (String)this.myRelId2RelTypeMap.get(paramString);
/* 1430 */       this.myRelId2RelTypeMap.remove(paramString);
/*      */     }
/*      */ 
/* 1433 */     synchronized (this.myRelType2RelIdsMap) {
/* 1434 */       localObject2 = (List)this.myRelType2RelIdsMap.get(???);
/* 1435 */       if (localObject2 != null)
/*      */       {
/* 1437 */         ((List)localObject2).remove(paramString);
/* 1438 */         if (((List)localObject2).isEmpty())
/*      */         {
/* 1440 */           this.myRelType2RelIdsMap.remove(???);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1445 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeRelation");
/*      */   }
/*      */ 
/*      */   public void purgeRelations()
/*      */     throws RelationServiceNotRegisteredException
/*      */   {
/* 1479 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "purgeRelations");
/*      */ 
/* 1483 */     isActive();
/*      */     ArrayList localArrayList;
/* 1495 */     synchronized (this.myRefedMBeanObjName2RelIdsMap) {
/* 1496 */       localArrayList = new ArrayList(this.myUnregNtfList);
/*      */ 
/* 1499 */       this.myUnregNtfList = new ArrayList();
/*      */     }
/*      */ 
/* 1509 */     ??? = new ArrayList();
/*      */ 
/* 1512 */     HashMap localHashMap = new HashMap();
/*      */     Object localObject2;
/*      */     Object localObject3;
/*      */     Object localObject4;
/*      */     Object localObject5;
/* 1515 */     synchronized (this.myRefedMBeanObjName2RelIdsMap) {
/* 1516 */       for (localObject2 = localArrayList.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (MBeanServerNotification)((Iterator)localObject2).next();
/*      */ 
/* 1518 */         localObject4 = ((MBeanServerNotification)localObject3).getMBeanName();
/*      */ 
/* 1522 */         ((List)???).add(localObject4);
/*      */ 
/* 1525 */         localObject5 = (Map)this.myRefedMBeanObjName2RelIdsMap.get(localObject4);
/*      */ 
/* 1527 */         localHashMap.put(localObject4, localObject5);
/*      */ 
/* 1529 */         this.myRefedMBeanObjName2RelIdsMap.remove(localObject4);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1535 */     updateUnregistrationListener(null, (List)???);
/*      */ 
/* 1537 */     for (??? = localArrayList.iterator(); ((Iterator)???).hasNext(); ) { localObject2 = (MBeanServerNotification)((Iterator)???).next();
/*      */ 
/* 1539 */       localObject3 = ((MBeanServerNotification)localObject2).getMBeanName();
/*      */ 
/* 1542 */       localObject4 = (Map)localHashMap.get(localObject3);
/*      */ 
/* 1548 */       for (localObject5 = ((Map)localObject4).entrySet().iterator(); ((Iterator)localObject5).hasNext(); ) { Map.Entry localEntry = (Map.Entry)((Iterator)localObject5).next();
/* 1549 */         String str = (String)localEntry.getKey();
/*      */ 
/* 1552 */         List localList = (List)localEntry.getValue();
/*      */         try
/*      */         {
/* 1566 */           handleReferenceUnregistration(str, (ObjectName)localObject3, localList);
/*      */         }
/*      */         catch (RelationNotFoundException localRelationNotFoundException)
/*      */         {
/* 1570 */           throw new RuntimeException(localRelationNotFoundException.getMessage());
/*      */         } catch (RoleNotFoundException localRoleNotFoundException) {
/* 1572 */           throw new RuntimeException(localRoleNotFoundException.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1577 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "purgeRelations");
/*      */   }
/*      */ 
/*      */   public Map<String, List<String>> findReferencingRelations(ObjectName paramObjectName, String paramString1, String paramString2)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1608 */     if (paramObjectName == null) {
/* 1609 */       localObject1 = "Invalid parameter.";
/* 1610 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1613 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "findReferencingRelations", new Object[] { paramObjectName, paramString1, paramString2 });
/*      */ 
/* 1617 */     Object localObject1 = new HashMap();
/*      */     Map localMap;
/*      */     Iterator localIterator;
/*      */     String str;
/*      */     Object localObject2;
/* 1619 */     synchronized (this.myRefedMBeanObjName2RelIdsMap)
/*      */     {
/* 1622 */       localMap = (Map)this.myRefedMBeanObjName2RelIdsMap.get(paramObjectName);
/*      */ 
/* 1625 */       if (localMap != null)
/*      */       {
/* 1628 */         Set localSet = localMap.keySet();
/*      */         ArrayList localArrayList;
/* 1633 */         if (paramString1 == null)
/*      */         {
/* 1635 */           localArrayList = new ArrayList(localSet);
/*      */         }
/*      */         else
/*      */         {
/* 1639 */           localArrayList = new ArrayList();
/*      */ 
/* 1643 */           for (localIterator = localSet.iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/*      */ 
/* 1647 */             synchronized (this.myRelId2RelTypeMap) {
/* 1648 */               localObject2 = (String)this.myRelId2RelTypeMap.get(str);
/*      */             }
/*      */ 
/* 1652 */             if (((String)localObject2).equals(paramString1))
/*      */             {
/* 1654 */               localArrayList.add(str);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 1663 */         for (localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { str = (String)localIterator.next();
/*      */ 
/* 1666 */           localObject2 = (List)localMap.get(str);
/*      */ 
/* 1669 */           if (paramString2 == null)
/*      */           {
/* 1673 */             ((Map)localObject1).put(str, new ArrayList((Collection)localObject2));
/*      */           }
/* 1676 */           else if (((List)localObject2).contains(paramString2))
/*      */           {
/* 1679 */             ??? = new ArrayList();
/* 1680 */             ((List)???).add(paramString2);
/* 1681 */             ((Map)localObject1).put(str, ???);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1687 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "findReferencingRelations");
/*      */ 
/* 1689 */     return localObject1;
/*      */   }
/*      */ 
/*      */   public Map<ObjectName, List<String>> findAssociatedMBeans(ObjectName paramObjectName, String paramString1, String paramString2)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1718 */     if (paramObjectName == null) {
/* 1719 */       localObject1 = "Invalid parameter.";
/* 1720 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1723 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "findAssociatedMBeans", new Object[] { paramObjectName, paramString1, paramString2 });
/*      */ 
/* 1729 */     Object localObject1 = findReferencingRelations(paramObjectName, paramString1, paramString2);
/*      */ 
/* 1734 */     HashMap localHashMap = new HashMap();
/*      */ 
/* 1737 */     for (Iterator localIterator1 = ((Map)localObject1).keySet().iterator(); localIterator1.hasNext(); )
/*      */     {
/* 1737 */       str = (String)localIterator1.next();
/*      */       Map localMap;
/*      */       try
/*      */       {
/* 1745 */         localMap = getReferencedMBeans(str);
/*      */       } catch (RelationNotFoundException localRelationNotFoundException) {
/* 1747 */         throw new RuntimeException(localRelationNotFoundException.getMessage());
/*      */       }
/*      */ 
/* 1752 */       for (ObjectName localObjectName : localMap.keySet())
/*      */       {
/* 1754 */         if (!localObjectName.equals(paramObjectName))
/*      */         {
/* 1758 */           Object localObject2 = (List)localHashMap.get(localObjectName);
/* 1759 */           if (localObject2 == null)
/*      */           {
/* 1761 */             localObject2 = new ArrayList();
/* 1762 */             ((List)localObject2).add(str);
/* 1763 */             localHashMap.put(localObjectName, localObject2);
/*      */           }
/*      */           else {
/* 1766 */             ((List)localObject2).add(str);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     String str;
/* 1772 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "findAssociatedMBeans");
/*      */ 
/* 1774 */     return localHashMap;
/*      */   }
/*      */ 
/*      */   public List<String> findRelationsOfType(String paramString)
/*      */     throws IllegalArgumentException, RelationTypeNotFoundException
/*      */   {
/* 1792 */     if (paramString == null) {
/* 1793 */       localObject1 = "Invalid parameter.";
/* 1794 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1797 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "findRelationsOfType");
/*      */ 
/* 1801 */     Object localObject1 = getRelationType(paramString);
/*      */     ArrayList localArrayList;
/* 1804 */     synchronized (this.myRelType2RelIdsMap) {
/* 1805 */       List localList = (List)this.myRelType2RelIdsMap.get(paramString);
/* 1806 */       if (localList == null)
/* 1807 */         localArrayList = new ArrayList();
/*      */       else {
/* 1809 */         localArrayList = new ArrayList(localList);
/*      */       }
/*      */     }
/* 1812 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "findRelationsOfType");
/*      */ 
/* 1814 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   public List<ObjectName> getRole(String paramString1, String paramString2)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException
/*      */   {
/* 1843 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 1844 */       localObject1 = "Invalid parameter.";
/* 1845 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 1848 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRole", new Object[] { paramString1, paramString2 });
/*      */ 
/* 1852 */     isActive();
/*      */ 
/* 1855 */     Object localObject1 = getRelation(paramString1);
/*      */     Object localObject2;
/* 1859 */     if ((localObject1 instanceof RelationSupport))
/*      */     {
/* 1862 */       localObject2 = (List)Util.cast(((RelationSupport)localObject1).getRoleInt(paramString2, true, this, false));
/*      */     }
/*      */     else
/*      */     {
/* 1870 */       Object[] arrayOfObject = new Object[1];
/* 1871 */       arrayOfObject[0] = paramString2;
/* 1872 */       String[] arrayOfString = new String[1];
/* 1873 */       arrayOfString[0] = "java.lang.String";
/*      */       try
/*      */       {
/* 1879 */         List localList = (List)Util.cast(this.myMBeanServer.invoke((ObjectName)localObject1, "getRole", arrayOfObject, arrayOfString));
/*      */ 
/* 1884 */         if ((localList == null) || ((localList instanceof ArrayList)))
/* 1885 */           localObject2 = localList;
/*      */         else
/* 1887 */           localObject2 = new ArrayList(localList);
/*      */       } catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 1889 */         throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */       } catch (ReflectionException localReflectionException) {
/* 1891 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (MBeanException localMBeanException) {
/* 1893 */         Exception localException = localMBeanException.getTargetException();
/* 1894 */         if ((localException instanceof RoleNotFoundException)) {
/* 1895 */           throw ((RoleNotFoundException)localException);
/*      */         }
/* 1897 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1902 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRole");
/* 1903 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public RoleResult getRoles(String paramString, String[] paramArrayOfString)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 1929 */     if ((paramString == null) || (paramArrayOfString == null)) {
/* 1930 */       localObject = "Invalid parameter.";
/* 1931 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 1934 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoles", paramString);
/*      */ 
/* 1938 */     isActive();
/*      */ 
/* 1941 */     Object localObject = getRelation(paramString);
/*      */     RoleResult localRoleResult;
/* 1945 */     if ((localObject instanceof RelationSupport))
/*      */     {
/* 1947 */       localRoleResult = ((RelationSupport)localObject).getRolesInt(paramArrayOfString, true, this);
/*      */     }
/*      */     else
/*      */     {
/* 1952 */       Object[] arrayOfObject = new Object[1];
/* 1953 */       arrayOfObject[0] = paramArrayOfString;
/* 1954 */       String[] arrayOfString = new String[1];
/*      */       try {
/* 1956 */         arrayOfString[0] = paramArrayOfString.getClass().getName();
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1964 */         localRoleResult = (RoleResult)this.myMBeanServer.invoke((ObjectName)localObject, "getRoles", arrayOfObject, arrayOfString);
/*      */       }
/*      */       catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */       {
/* 1970 */         throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */       } catch (ReflectionException localReflectionException) {
/* 1972 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (MBeanException localMBeanException) {
/* 1974 */         throw new RuntimeException(localMBeanException.getTargetException().getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1979 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoles");
/* 1980 */     return localRoleResult;
/*      */   }
/*      */ 
/*      */   public RoleResult getAllRoles(String paramString)
/*      */     throws IllegalArgumentException, RelationNotFoundException, RelationServiceNotRegisteredException
/*      */   {
/* 2002 */     if (paramString == null) {
/* 2003 */       localObject = "Invalid parameter.";
/* 2004 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 2007 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoles", paramString);
/*      */ 
/* 2011 */     Object localObject = getRelation(paramString);
/*      */     RoleResult localRoleResult;
/* 2015 */     if ((localObject instanceof RelationSupport))
/*      */     {
/* 2017 */       localRoleResult = ((RelationSupport)localObject).getAllRolesInt(true, this);
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/* 2023 */         localRoleResult = (RoleResult)this.myMBeanServer.getAttribute((ObjectName)localObject, "AllRoles");
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 2027 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/* 2031 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoles");
/* 2032 */     return localRoleResult;
/*      */   }
/*      */ 
/*      */   public Integer getRoleCardinality(String paramString1, String paramString2)
/*      */     throws IllegalArgumentException, RelationNotFoundException, RoleNotFoundException
/*      */   {
/* 2053 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 2054 */       localObject = "Invalid parameter.";
/* 2055 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 2058 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRoleCardinality", new Object[] { paramString1, paramString2 });
/*      */ 
/* 2062 */     Object localObject = getRelation(paramString1);
/*      */     Integer localInteger;
/* 2066 */     if ((localObject instanceof RelationSupport))
/*      */     {
/* 2069 */       localInteger = ((RelationSupport)localObject).getRoleCardinality(paramString2);
/*      */     }
/*      */     else
/*      */     {
/* 2073 */       Object[] arrayOfObject = new Object[1];
/* 2074 */       arrayOfObject[0] = paramString2;
/* 2075 */       String[] arrayOfString = new String[1];
/* 2076 */       arrayOfString[0] = "java.lang.String";
/*      */       try
/*      */       {
/* 2082 */         localInteger = (Integer)this.myMBeanServer.invoke((ObjectName)localObject, "getRoleCardinality", arrayOfObject, arrayOfString);
/*      */       }
/*      */       catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */       {
/* 2088 */         throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */       } catch (ReflectionException localReflectionException) {
/* 2090 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (MBeanException localMBeanException) {
/* 2092 */         Exception localException = localMBeanException.getTargetException();
/* 2093 */         if ((localException instanceof RoleNotFoundException)) {
/* 2094 */           throw ((RoleNotFoundException)localException);
/*      */         }
/* 2096 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2101 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRoleCardinality");
/*      */ 
/* 2103 */     return localInteger;
/*      */   }
/*      */ 
/*      */   public void setRole(String paramString, Role paramRole)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException, InvalidRoleValueException
/*      */   {
/* 2145 */     if ((paramString == null) || (paramRole == null)) {
/* 2146 */       localObject = "Invalid parameter.";
/* 2147 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 2150 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "setRole", new Object[] { paramString, paramRole });
/*      */ 
/* 2154 */     isActive();
/*      */ 
/* 2157 */     Object localObject = getRelation(paramString);
/*      */ 
/* 2159 */     if ((localObject instanceof RelationSupport))
/*      */     {
/*      */       try
/*      */       {
/* 2168 */         ((RelationSupport)localObject).setRoleInt(paramRole, true, this, false);
/*      */       }
/*      */       catch (RelationTypeNotFoundException localRelationTypeNotFoundException)
/*      */       {
/* 2174 */         throw new RuntimeException(localRelationTypeNotFoundException.getMessage());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2179 */       Object[] arrayOfObject = new Object[1];
/* 2180 */       arrayOfObject[0] = paramRole;
/* 2181 */       String[] arrayOfString = new String[1];
/* 2182 */       arrayOfString[0] = "javax.management.relation.Role";
/*      */       try
/*      */       {
/* 2190 */         this.myMBeanServer.setAttribute((ObjectName)localObject, new Attribute("Role", paramRole));
/*      */       }
/*      */       catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */       {
/* 2194 */         throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */       } catch (ReflectionException localReflectionException) {
/* 2196 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (MBeanException localMBeanException) {
/* 2198 */         Exception localException = localMBeanException.getTargetException();
/* 2199 */         if ((localException instanceof RoleNotFoundException))
/* 2200 */           throw ((RoleNotFoundException)localException);
/* 2201 */         if ((localException instanceof InvalidRoleValueException)) {
/* 2202 */           throw ((InvalidRoleValueException)localException);
/*      */         }
/* 2204 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */       catch (AttributeNotFoundException localAttributeNotFoundException)
/*      */       {
/* 2208 */         throw new RuntimeException(localAttributeNotFoundException.getMessage());
/*      */       } catch (InvalidAttributeValueException localInvalidAttributeValueException) {
/* 2210 */         throw new RuntimeException(localInvalidAttributeValueException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/* 2214 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "setRole");
/*      */   }
/*      */ 
/*      */   public RoleResult setRoles(String paramString, RoleList paramRoleList)
/*      */     throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 2245 */     if ((paramString == null) || (paramRoleList == null)) {
/* 2246 */       localObject = "Invalid parameter.";
/* 2247 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 2250 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "setRoles", new Object[] { paramString, paramRoleList });
/*      */ 
/* 2254 */     isActive();
/*      */ 
/* 2257 */     Object localObject = getRelation(paramString);
/*      */     RoleResult localRoleResult;
/* 2261 */     if ((localObject instanceof RelationSupport))
/*      */     {
/*      */       try
/*      */       {
/* 2268 */         localRoleResult = ((RelationSupport)localObject).setRolesInt(paramRoleList, true, this);
/*      */       }
/*      */       catch (RelationTypeNotFoundException localRelationTypeNotFoundException)
/*      */       {
/* 2272 */         throw new RuntimeException(localRelationTypeNotFoundException.getMessage());
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 2277 */       Object[] arrayOfObject = new Object[1];
/* 2278 */       arrayOfObject[0] = paramRoleList;
/* 2279 */       String[] arrayOfString = new String[1];
/* 2280 */       arrayOfString[0] = "javax.management.relation.RoleList";
/*      */       try
/*      */       {
/* 2284 */         localRoleResult = (RoleResult)this.myMBeanServer.invoke((ObjectName)localObject, "setRoles", arrayOfObject, arrayOfString);
/*      */       }
/*      */       catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */       {
/* 2290 */         throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */       } catch (ReflectionException localReflectionException) {
/* 2292 */         throw new RuntimeException(localReflectionException.getMessage());
/*      */       } catch (MBeanException localMBeanException) {
/* 2294 */         throw new RuntimeException(localMBeanException.getTargetException().getMessage());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2299 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "setRoles");
/* 2300 */     return localRoleResult;
/*      */   }
/*      */ 
/*      */   public Map<ObjectName, List<String>> getReferencedMBeans(String paramString)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 2321 */     if (paramString == null) {
/* 2322 */       localObject = "Invalid parameter.";
/* 2323 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 2326 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getReferencedMBeans", paramString);
/*      */ 
/* 2330 */     Object localObject = getRelation(paramString);
/*      */     Map localMap;
/* 2334 */     if ((localObject instanceof RelationSupport))
/*      */     {
/* 2336 */       localMap = ((RelationSupport)localObject).getReferencedMBeans();
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/* 2342 */         localMap = (Map)Util.cast(this.myMBeanServer.getAttribute((ObjectName)localObject, "ReferencedMBeans"));
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 2346 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/* 2350 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getReferencedMBeans");
/*      */ 
/* 2352 */     return localMap;
/*      */   }
/*      */ 
/*      */   public String getRelationTypeName(String paramString)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/* 2370 */     if (paramString == null) {
/* 2371 */       localObject = "Invalid parameter.";
/* 2372 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 2375 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRelationTypeName", paramString);
/*      */ 
/* 2379 */     Object localObject = getRelation(paramString);
/*      */     String str;
/* 2383 */     if ((localObject instanceof RelationSupport))
/*      */     {
/* 2385 */       str = ((RelationSupport)localObject).getRelationTypeName();
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/* 2391 */         str = (String)this.myMBeanServer.getAttribute((ObjectName)localObject, "RelationTypeName");
/*      */       }
/*      */       catch (Exception localException)
/*      */       {
/* 2395 */         throw new RuntimeException(localException.getMessage());
/*      */       }
/*      */     }
/*      */ 
/* 2399 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRelationTypeName");
/*      */ 
/* 2401 */     return str;
/*      */   }
/*      */ 
/*      */   public void handleNotification(Notification paramNotification, Object paramObject)
/*      */   {
/*      */     Object localObject1;
/* 2420 */     if (paramNotification == null) {
/* 2421 */       localObject1 = "Invalid parameter.";
/* 2422 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 2425 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "handleNotification", paramNotification);
/*      */ 
/* 2428 */     if ((paramNotification instanceof MBeanServerNotification))
/*      */     {
/* 2430 */       localObject1 = (MBeanServerNotification)paramNotification;
/* 2431 */       String str = paramNotification.getType();
/*      */ 
/* 2433 */       if (str.equals("JMX.mbean.unregistered"))
/*      */       {
/* 2435 */         ObjectName localObjectName = ((MBeanServerNotification)paramNotification).getMBeanName();
/*      */ 
/* 2440 */         int i = 0;
/* 2441 */         synchronized (this.myRefedMBeanObjName2RelIdsMap)
/*      */         {
/* 2443 */           if (this.myRefedMBeanObjName2RelIdsMap.containsKey(localObjectName))
/*      */           {
/* 2445 */             synchronized (this.myUnregNtfList) {
/* 2446 */               this.myUnregNtfList.add(localObject1);
/*      */             }
/* 2448 */             i = 1;
/*      */           }
/* 2450 */           if ((i != 0) && (this.myPurgeFlag))
/*      */           {
/*      */             try
/*      */             {
/* 2455 */               purgeRelations();
/*      */             } catch (Exception ) {
/* 2457 */               throw new RuntimeException(???.getMessage());
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2465 */         synchronized (this.myRelMBeanObjName2RelIdMap) {
/* 2466 */           ??? = (String)this.myRelMBeanObjName2RelIdMap.get(localObjectName);
/*      */         }
/* 2468 */         if (??? != null)
/*      */         {
/*      */           try
/*      */           {
/* 2476 */             removeRelation((String)???);
/*      */           } catch (Exception localException) {
/* 2478 */             throw new RuntimeException(localException.getMessage());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2484 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "handleNotification");
/*      */   }
/*      */ 
/*      */   public MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/* 2499 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getNotificationInfo");
/*      */ 
/* 2502 */     String str1 = "javax.management.relation.RelationNotification";
/*      */ 
/* 2504 */     String[] arrayOfString = { "jmx.relation.creation.basic", "jmx.relation.creation.mbean", "jmx.relation.update.basic", "jmx.relation.update.mbean", "jmx.relation.removal.basic", "jmx.relation.removal.mbean" };
/*      */ 
/* 2513 */     String str2 = "Sent when a relation is created, updated or deleted.";
/*      */ 
/* 2515 */     MBeanNotificationInfo localMBeanNotificationInfo = new MBeanNotificationInfo(arrayOfString, str1, str2);
/*      */ 
/* 2518 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getNotificationInfo");
/*      */ 
/* 2520 */     return new MBeanNotificationInfo[] { localMBeanNotificationInfo };
/*      */   }
/*      */ 
/*      */   private void addRelationTypeInt(RelationType paramRelationType)
/*      */     throws IllegalArgumentException, InvalidRelationTypeException
/*      */   {
/* 2538 */     if (paramRelationType == null) {
/* 2539 */       str1 = "Invalid parameter.";
/* 2540 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/* 2543 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelationTypeInt");
/*      */ 
/* 2546 */     String str1 = paramRelationType.getRelationTypeName();
/*      */     try
/*      */     {
/* 2552 */       RelationType localRelationType = getRelationType(str1);
/*      */ 
/* 2554 */       if (localRelationType != null) {
/* 2555 */         String str2 = "There is already a relation type in the Relation Service with name ";
/* 2556 */         StringBuilder localStringBuilder = new StringBuilder(str2);
/* 2557 */         localStringBuilder.append(str1);
/* 2558 */         throw new InvalidRelationTypeException(localStringBuilder.toString());
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (RelationTypeNotFoundException localRelationTypeNotFoundException)
/*      */     {
/*      */     }
/*      */ 
/* 2566 */     synchronized (this.myRelType2ObjMap) {
/* 2567 */       this.myRelType2ObjMap.put(str1, paramRelationType);
/*      */     }
/*      */ 
/* 2570 */     if ((paramRelationType instanceof RelationTypeSupport)) {
/* 2571 */       ((RelationTypeSupport)paramRelationType).setRelationServiceFlag(true);
/*      */     }
/*      */ 
/* 2574 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelationTypeInt");
/*      */   }
/*      */ 
/*      */   RelationType getRelationType(String paramString)
/*      */     throws IllegalArgumentException, RelationTypeNotFoundException
/*      */   {
/*      */     Object localObject1;
/* 2594 */     if (paramString == null) {
/* 2595 */       localObject1 = "Invalid parameter.";
/* 2596 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 2599 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRelationType", paramString);
/*      */ 
/* 2604 */     synchronized (this.myRelType2ObjMap) {
/* 2605 */       localObject1 = (RelationType)this.myRelType2ObjMap.get(paramString);
/*      */     }
/*      */ 
/* 2608 */     if (localObject1 == null) {
/* 2609 */       ??? = "No relation type created in the Relation Service with the name ";
/* 2610 */       StringBuilder localStringBuilder = new StringBuilder((String)???);
/* 2611 */       localStringBuilder.append(paramString);
/* 2612 */       throw new RelationTypeNotFoundException(localStringBuilder.toString());
/*      */     }
/*      */ 
/* 2615 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRelationType");
/*      */ 
/* 2617 */     return localObject1;
/*      */   }
/*      */ 
/*      */   Object getRelation(String paramString)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/*      */     Object localObject1;
/* 2638 */     if (paramString == null) {
/* 2639 */       localObject1 = "Invalid parameter.";
/* 2640 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 2643 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "getRelation", paramString);
/*      */ 
/* 2648 */     synchronized (this.myRelId2ObjMap) {
/* 2649 */       localObject1 = this.myRelId2ObjMap.get(paramString);
/*      */     }
/*      */ 
/* 2652 */     if (localObject1 == null) {
/* 2653 */       ??? = "No relation associated to relation id " + paramString;
/* 2654 */       throw new RelationNotFoundException((String)???);
/*      */     }
/*      */ 
/* 2657 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "getRelation");
/*      */ 
/* 2659 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private boolean addNewMBeanReference(ObjectName paramObjectName, String paramString1, String paramString2)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2681 */     if ((paramObjectName == null) || (paramString1 == null) || (paramString2 == null))
/*      */     {
/* 2684 */       String str = "Invalid parameter.";
/* 2685 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 2688 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addNewMBeanReference", new Object[] { paramObjectName, paramString1, paramString2 });
/*      */ 
/* 2692 */     boolean bool = false;
/*      */ 
/* 2694 */     synchronized (this.myRefedMBeanObjName2RelIdsMap)
/*      */     {
/* 2698 */       Object localObject1 = (Map)this.myRefedMBeanObjName2RelIdsMap.get(paramObjectName);
/*      */       Object localObject2;
/* 2701 */       if (localObject1 == null)
/*      */       {
/* 2704 */         bool = true;
/*      */ 
/* 2708 */         localObject2 = new ArrayList();
/* 2709 */         ((List)localObject2).add(paramString2);
/*      */ 
/* 2712 */         localObject1 = new HashMap();
/* 2713 */         ((Map)localObject1).put(paramString1, localObject2);
/*      */ 
/* 2715 */         this.myRefedMBeanObjName2RelIdsMap.put(paramObjectName, localObject1);
/*      */       }
/*      */       else
/*      */       {
/* 2721 */         localObject2 = (List)((Map)localObject1).get(paramString1);
/*      */ 
/* 2723 */         if (localObject2 == null)
/*      */         {
/* 2728 */           localObject2 = new ArrayList();
/* 2729 */           ((List)localObject2).add(paramString2);
/*      */ 
/* 2732 */           ((Map)localObject1).put(paramString1, localObject2);
/*      */         }
/*      */         else
/*      */         {
/* 2738 */           ((List)localObject2).add(paramString2);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2743 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addNewMBeanReference");
/*      */ 
/* 2745 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean removeMBeanReference(ObjectName paramObjectName, String paramString1, String paramString2, boolean paramBoolean)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2769 */     if ((paramObjectName == null) || (paramString1 == null) || (paramString2 == null))
/*      */     {
/* 2772 */       String str = "Invalid parameter.";
/* 2773 */       throw new IllegalArgumentException(str);
/*      */     }
/*      */ 
/* 2776 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "removeMBeanReference", new Object[] { paramObjectName, paramString1, paramString2, Boolean.valueOf(paramBoolean) });
/*      */ 
/* 2780 */     boolean bool = false;
/*      */ 
/* 2782 */     synchronized (this.myRefedMBeanObjName2RelIdsMap)
/*      */     {
/* 2789 */       Map localMap = (Map)this.myRefedMBeanObjName2RelIdsMap.get(paramObjectName);
/*      */ 
/* 2792 */       if (localMap == null)
/*      */       {
/* 2794 */         JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeMBeanReference");
/*      */ 
/* 2796 */         return true;
/*      */       }
/*      */ 
/* 2799 */       List localList = null;
/* 2800 */       if (!paramBoolean)
/*      */       {
/* 2803 */         localList = (List)localMap.get(paramString1);
/*      */ 
/* 2806 */         int i = localList.indexOf(paramString2);
/* 2807 */         if (i != -1) {
/* 2808 */           localList.remove(i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2814 */       if ((localList.isEmpty()) || (paramBoolean))
/*      */       {
/* 2817 */         localMap.remove(paramString1);
/*      */       }
/*      */ 
/* 2821 */       if (localMap.isEmpty())
/*      */       {
/* 2823 */         this.myRefedMBeanObjName2RelIdsMap.remove(paramObjectName);
/* 2824 */         bool = true;
/*      */       }
/*      */     }
/*      */ 
/* 2828 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "removeMBeanReference");
/*      */ 
/* 2830 */     return bool;
/*      */   }
/*      */ 
/*      */   private void updateUnregistrationListener(List<ObjectName> paramList1, List<ObjectName> paramList2)
/*      */     throws RelationServiceNotRegisteredException
/*      */   {
/* 2847 */     if ((paramList1 != null) && (paramList2 != null) && 
/* 2848 */       (paramList1.isEmpty()) && (paramList2.isEmpty()))
/*      */     {
/* 2850 */       return;
/*      */     }
/*      */ 
/* 2854 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "updateUnregistrationListener", new Object[] { paramList1, paramList2 });
/*      */ 
/* 2859 */     isActive();
/*      */ 
/* 2861 */     if ((paramList1 != null) || (paramList2 != null))
/*      */     {
/* 2863 */       int i = 0;
/* 2864 */       if (this.myUnregNtfFilter == null)
/*      */       {
/* 2866 */         this.myUnregNtfFilter = new MBeanServerNotificationFilter();
/* 2867 */         i = 1;
/*      */       }
/*      */ 
/* 2870 */       synchronized (this.myUnregNtfFilter)
/*      */       {
/*      */         Iterator localIterator;
/* 2873 */         if (paramList1 != null)
/* 2874 */           for (localIterator = paramList1.iterator(); localIterator.hasNext(); ) { localObjectName = (ObjectName)localIterator.next();
/* 2875 */             this.myUnregNtfFilter.enableObjectName(localObjectName);
/*      */           }
/*      */         ObjectName localObjectName;
/* 2878 */         if (paramList2 != null)
/*      */         {
/* 2880 */           for (localIterator = paramList2.iterator(); localIterator.hasNext(); ) { localObjectName = (ObjectName)localIterator.next();
/* 2881 */             this.myUnregNtfFilter.disableObjectName(localObjectName);
/*      */           }
/*      */         }
/*      */ 
/* 2885 */         if (i != 0) {
/*      */           try {
/* 2887 */             this.myMBeanServer.addNotificationListener(MBeanServerDelegate.DELEGATE_NAME, this, this.myUnregNtfFilter, null);
/*      */           }
/*      */           catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */           {
/* 2893 */             throw new RelationServiceNotRegisteredException(localInstanceNotFoundException.getMessage());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2940 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "updateUnregistrationListener");
/*      */   }
/*      */ 
/*      */   private void addRelationInt(boolean paramBoolean, RelationSupport paramRelationSupport, ObjectName paramObjectName, String paramString1, String paramString2, RoleList paramRoleList)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RoleNotFoundException, InvalidRelationIdException, RelationTypeNotFoundException, InvalidRoleValueException
/*      */   {
/*      */     Object localObject1;
/* 2995 */     if ((paramString1 == null) || (paramString2 == null) || ((paramBoolean) && ((paramRelationSupport == null) || (paramObjectName != null))) || ((!paramBoolean) && ((paramObjectName == null) || (paramRelationSupport != null))))
/*      */     {
/* 3003 */       localObject1 = "Invalid parameter.";
/* 3004 */       throw new IllegalArgumentException((String)localObject1);
/*      */     }
/*      */ 
/* 3007 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "addRelationInt", new Object[] { Boolean.valueOf(paramBoolean), paramRelationSupport, paramObjectName, paramString1, paramString2, paramRoleList });
/*      */ 
/* 3012 */     isActive();
/*      */     Object localObject3;
/*      */     try
/*      */     {
/* 3017 */       localObject1 = getRelation(paramString1);
/*      */ 
/* 3019 */       if (localObject1 != null)
/*      */       {
/* 3021 */         localObject2 = "There is already a relation with id ";
/* 3022 */         localObject3 = new StringBuilder((String)localObject2);
/* 3023 */         ((StringBuilder)localObject3).append(paramString1);
/* 3024 */         throw new InvalidRelationIdException(((StringBuilder)localObject3).toString());
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (RelationNotFoundException localRelationNotFoundException1)
/*      */     {
/*      */     }
/*      */ 
/* 3032 */     RelationType localRelationType = getRelationType(paramString2);
/*      */ 
/* 3040 */     Object localObject2 = new ArrayList(localRelationType.getRoleInfos());
/*      */ 
/* 3042 */     if (paramRoleList != null)
/*      */     {
/* 3044 */       for (localObject3 = paramRoleList.asList().iterator(); ((Iterator)localObject3).hasNext(); ) { localObject4 = (Role)((Iterator)localObject3).next();
/* 3045 */         String str = ((Role)localObject4).getRoleName();
/* 3046 */         List localList = ((Role)localObject4).getRoleValue();
/*      */         RoleInfo localRoleInfo;
/*      */         try {
/* 3052 */           localRoleInfo = localRelationType.getRoleInfo(str);
/*      */         } catch (RoleInfoNotFoundException localRoleInfoNotFoundException) {
/* 3054 */           throw new RoleNotFoundException(localRoleInfoNotFoundException.getMessage());
/*      */         }
/*      */ 
/* 3058 */         Integer localInteger = checkRoleInt(2, str, localList, localRoleInfo, false);
/*      */ 
/* 3063 */         int j = localInteger.intValue();
/* 3064 */         if (j != 0)
/*      */         {
/* 3067 */           throwRoleProblemException(j, str);
/*      */         }
/*      */ 
/* 3072 */         int k = ((List)localObject2).indexOf(localRoleInfo);
/*      */ 
/* 3074 */         ((List)localObject2).remove(k);
/*      */       }
/*      */     }
/*      */     Object localObject4;
/* 3080 */     initializeMissingRoles(paramBoolean, paramRelationSupport, paramObjectName, paramString1, paramString2, (List)localObject2);
/*      */ 
/* 3091 */     synchronized (this.myRelId2ObjMap) {
/* 3092 */       if (paramBoolean)
/*      */       {
/* 3094 */         this.myRelId2ObjMap.put(paramString1, paramRelationSupport);
/*      */       }
/* 3096 */       else this.myRelId2ObjMap.put(paramString1, paramObjectName);
/*      */ 
/*      */     }
/*      */ 
/* 3101 */     synchronized (this.myRelId2RelTypeMap) {
/* 3102 */       this.myRelId2RelTypeMap.put(paramString1, paramString2);
/*      */     }
/*      */ 
/* 3107 */     synchronized (this.myRelType2RelIdsMap) {
/* 3108 */       localObject4 = (List)this.myRelType2RelIdsMap.get(paramString2);
/*      */ 
/* 3110 */       int i = 0;
/* 3111 */       if (localObject4 == null) {
/* 3112 */         i = 1;
/* 3113 */         localObject4 = new ArrayList();
/*      */       }
/* 3115 */       ((List)localObject4).add(paramString1);
/* 3116 */       if (i != 0) {
/* 3117 */         this.myRelType2RelIdsMap.put(paramString2, localObject4);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3125 */     for (??? = paramRoleList.asList().iterator(); ((Iterator)???).hasNext(); ) { localObject4 = (Role)((Iterator)???).next();
/*      */ 
/* 3128 */       ArrayList localArrayList = new ArrayList();
/*      */       try
/*      */       {
/* 3132 */         updateRoleMap(paramString1, (Role)localObject4, localArrayList);
/*      */       }
/*      */       catch (RelationNotFoundException localRelationNotFoundException3)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 3142 */       sendRelationCreationNotification(paramString1);
/*      */     }
/*      */     catch (RelationNotFoundException localRelationNotFoundException2)
/*      */     {
/*      */     }
/*      */ 
/* 3148 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "addRelationInt");
/*      */   }
/*      */ 
/*      */   private Integer checkRoleInt(int paramInt, String paramString, List<ObjectName> paramList, RoleInfo paramRoleInfo, boolean paramBoolean)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3182 */     if ((paramString == null) || (paramRoleInfo == null) || ((paramInt == 2) && (paramList == null)))
/*      */     {
/* 3185 */       str1 = "Invalid parameter.";
/* 3186 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/* 3189 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "checkRoleInt", new Object[] { Integer.valueOf(paramInt), paramString, paramList, paramRoleInfo, Boolean.valueOf(paramBoolean) });
/*      */ 
/* 3194 */     String str1 = paramRoleInfo.getName();
/* 3195 */     if (!paramString.equals(str1)) {
/* 3196 */       JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3198 */       return Integer.valueOf(1);
/*      */     }
/*      */     boolean bool1;
/* 3202 */     if (paramInt == 1) {
/* 3203 */       bool1 = paramRoleInfo.isReadable();
/* 3204 */       if (!bool1) {
/* 3205 */         JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3207 */         return Integer.valueOf(2);
/*      */       }
/*      */ 
/* 3210 */       JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3212 */       return new Integer(0);
/*      */     }
/*      */ 
/* 3217 */     if (paramBoolean) {
/* 3218 */       bool1 = paramRoleInfo.isWritable();
/* 3219 */       if (!bool1) {
/* 3220 */         JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3222 */         return new Integer(3);
/*      */       }
/*      */     }
/*      */ 
/* 3226 */     int i = paramList.size();
/*      */ 
/* 3229 */     boolean bool2 = paramRoleInfo.checkMinDegree(i);
/* 3230 */     if (!bool2) {
/* 3231 */       JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3233 */       return new Integer(4);
/*      */     }
/*      */ 
/* 3237 */     boolean bool3 = paramRoleInfo.checkMaxDegree(i);
/* 3238 */     if (!bool3) {
/* 3239 */       JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3241 */       return new Integer(5);
/*      */     }
/*      */ 
/* 3250 */     String str2 = paramRoleInfo.getRefMBeanClassName();
/*      */ 
/* 3252 */     for (ObjectName localObjectName : paramList)
/*      */     {
/* 3255 */       if (localObjectName == null) {
/* 3256 */         JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3258 */         return new Integer(7);
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 3264 */         boolean bool4 = this.myMBeanServer.isInstanceOf(localObjectName, str2);
/*      */ 
/* 3266 */         if (!bool4) {
/* 3267 */           JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3269 */           return new Integer(6);
/*      */         }
/*      */       }
/*      */       catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 3273 */         JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3275 */         return new Integer(7);
/*      */       }
/*      */     }
/*      */ 
/* 3279 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "checkRoleInt");
/*      */ 
/* 3281 */     return new Integer(0);
/*      */   }
/*      */ 
/*      */   private void initializeMissingRoles(boolean paramBoolean, RelationSupport paramRelationSupport, ObjectName paramObjectName, String paramString1, String paramString2, List<RoleInfo> paramList)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, InvalidRoleValueException
/*      */   {
/* 3319 */     if (((paramBoolean) && ((paramRelationSupport == null) || (paramObjectName != null))) || ((!paramBoolean) && ((paramObjectName == null) || (paramRelationSupport != null))) || (paramString1 == null) || (paramString2 == null) || (paramList == null))
/*      */     {
/* 3328 */       localObject = "Invalid parameter.";
/* 3329 */       throw new IllegalArgumentException((String)localObject);
/*      */     }
/*      */ 
/* 3332 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "initializeMissingRoles", new Object[] { Boolean.valueOf(paramBoolean), paramRelationSupport, paramObjectName, paramString1, paramString2, paramList });
/*      */ 
/* 3338 */     isActive();
/*      */ 
/* 3345 */     for (Object localObject = paramList.iterator(); ((Iterator)localObject).hasNext(); ) { RoleInfo localRoleInfo = (RoleInfo)((Iterator)localObject).next();
/*      */ 
/* 3347 */       String str = localRoleInfo.getName();
/*      */ 
/* 3350 */       ArrayList localArrayList = new ArrayList();
/*      */ 
/* 3352 */       Role localRole = new Role(str, localArrayList);
/*      */ 
/* 3354 */       if (paramBoolean)
/*      */       {
/*      */         try
/*      */         {
/* 3363 */           paramRelationSupport.setRoleInt(localRole, true, this, false);
/*      */         }
/*      */         catch (RoleNotFoundException localRoleNotFoundException) {
/* 3366 */           throw new RuntimeException(localRoleNotFoundException.getMessage());
/*      */         } catch (RelationNotFoundException localRelationNotFoundException) {
/* 3368 */           throw new RuntimeException(localRelationNotFoundException.getMessage());
/*      */         } catch (RelationTypeNotFoundException localRelationTypeNotFoundException) {
/* 3370 */           throw new RuntimeException(localRelationTypeNotFoundException.getMessage());
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 3377 */         Object[] arrayOfObject = new Object[1];
/* 3378 */         arrayOfObject[0] = localRole;
/* 3379 */         String[] arrayOfString = new String[1];
/* 3380 */         arrayOfString[0] = "javax.management.relation.Role";
/*      */         try
/*      */         {
/* 3392 */           this.myMBeanServer.setAttribute(paramObjectName, new Attribute("Role", localRole));
/*      */         }
/*      */         catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */         {
/* 3396 */           throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */         } catch (ReflectionException localReflectionException) {
/* 3398 */           throw new RuntimeException(localReflectionException.getMessage());
/*      */         } catch (MBeanException localMBeanException) {
/* 3400 */           Exception localException = localMBeanException.getTargetException();
/* 3401 */           if ((localException instanceof InvalidRoleValueException)) {
/* 3402 */             throw ((InvalidRoleValueException)localException);
/*      */           }
/* 3404 */           throw new RuntimeException(localException.getMessage());
/*      */         }
/*      */         catch (AttributeNotFoundException localAttributeNotFoundException) {
/* 3407 */           throw new RuntimeException(localAttributeNotFoundException.getMessage());
/*      */         } catch (InvalidAttributeValueException localInvalidAttributeValueException) {
/* 3409 */           throw new RuntimeException(localInvalidAttributeValueException.getMessage());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 3414 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "initializeMissingRoles");
/*      */   }
/*      */ 
/*      */   static void throwRoleProblemException(int paramInt, String paramString)
/*      */     throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException
/*      */   {
/* 3440 */     if (paramString == null) {
/* 3441 */       String str1 = "Invalid parameter.";
/* 3442 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/* 3447 */     int i = 0;
/*      */ 
/* 3449 */     String str2 = null;
/*      */ 
/* 3451 */     switch (paramInt) {
/*      */     case 1:
/* 3453 */       str2 = " does not exist in relation.";
/* 3454 */       i = 1;
/* 3455 */       break;
/*      */     case 2:
/* 3457 */       str2 = " is not readable.";
/* 3458 */       i = 1;
/* 3459 */       break;
/*      */     case 3:
/* 3461 */       str2 = " is not writable.";
/* 3462 */       i = 1;
/* 3463 */       break;
/*      */     case 4:
/* 3465 */       str2 = " has a number of MBean references less than the expected minimum degree.";
/* 3466 */       i = 2;
/* 3467 */       break;
/*      */     case 5:
/* 3469 */       str2 = " has a number of MBean references greater than the expected maximum degree.";
/* 3470 */       i = 2;
/* 3471 */       break;
/*      */     case 6:
/* 3473 */       str2 = " has an MBean reference to an MBean not of the expected class of references for that role.";
/* 3474 */       i = 2;
/* 3475 */       break;
/*      */     case 7:
/* 3477 */       str2 = " has a reference to null or to an MBean not registered.";
/* 3478 */       i = 2;
/*      */     }
/*      */ 
/* 3483 */     StringBuilder localStringBuilder = new StringBuilder(paramString);
/* 3484 */     localStringBuilder.append(str2);
/* 3485 */     String str3 = localStringBuilder.toString();
/* 3486 */     if (i == 1) {
/* 3487 */       throw new RoleNotFoundException(str3);
/*      */     }
/* 3489 */     if (i == 2)
/* 3490 */       throw new InvalidRoleValueException(str3);
/*      */   }
/*      */ 
/*      */   private void sendNotificationInt(int paramInt, String paramString1, String paramString2, List<ObjectName> paramList1, String paramString3, List<ObjectName> paramList2, List<ObjectName> paramList3)
/*      */     throws IllegalArgumentException, RelationNotFoundException
/*      */   {
/*      */     String str1;
/* 3521 */     if ((paramString1 == null) || (paramString2 == null) || ((paramInt != 3) && (paramList1 != null)) || ((paramInt == 2) && ((paramString3 == null) || (paramList2 == null) || (paramList3 == null))))
/*      */     {
/* 3528 */       str1 = "Invalid parameter.";
/* 3529 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/* 3532 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "sendNotificationInt", new Object[] { Integer.valueOf(paramInt), paramString1, paramString2, paramList1, paramString3, paramList2, paramList3 });
/*      */ 
/* 3540 */     synchronized (this.myRelId2RelTypeMap) {
/* 3541 */       str1 = (String)this.myRelId2RelTypeMap.get(paramString2);
/*      */     }
/*      */ 
/* 3546 */     ??? = isRelationMBean(paramString2);
/*      */ 
/* 3548 */     String str2 = null;
/* 3549 */     if (??? != null) {
/* 3550 */       switch (paramInt) {
/*      */       case 1:
/* 3552 */         str2 = "jmx.relation.creation.mbean";
/* 3553 */         break;
/*      */       case 2:
/* 3555 */         str2 = "jmx.relation.update.mbean";
/* 3556 */         break;
/*      */       case 3:
/* 3558 */         str2 = "jmx.relation.removal.mbean";
/*      */       }
/*      */     }
/*      */     else {
/* 3562 */       switch (paramInt) {
/*      */       case 1:
/* 3564 */         str2 = "jmx.relation.creation.basic";
/* 3565 */         break;
/*      */       case 2:
/* 3567 */         str2 = "jmx.relation.update.basic";
/* 3568 */         break;
/*      */       case 3:
/* 3570 */         str2 = "jmx.relation.removal.basic";
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3576 */     Long localLong = Long.valueOf(this.atomicSeqNo.incrementAndGet());
/*      */ 
/* 3579 */     Date localDate = new Date();
/* 3580 */     long l = localDate.getTime();
/*      */ 
/* 3582 */     RelationNotification localRelationNotification = null;
/*      */ 
/* 3584 */     if ((str2.equals("jmx.relation.creation.basic")) || (str2.equals("jmx.relation.creation.mbean")) || (str2.equals("jmx.relation.removal.basic")) || (str2.equals("jmx.relation.removal.mbean")))
/*      */     {
/* 3590 */       localRelationNotification = new RelationNotification(str2, this, localLong.longValue(), l, paramString1, paramString2, str1, (ObjectName)???, paramList1);
/*      */     }
/* 3600 */     else if ((str2.equals("jmx.relation.update.basic")) || (str2.equals("jmx.relation.update.mbean")))
/*      */     {
/* 3605 */       localRelationNotification = new RelationNotification(str2, this, localLong.longValue(), l, paramString1, paramString2, str1, (ObjectName)???, paramString3, paramList2, paramList3);
/*      */     }
/*      */ 
/* 3618 */     sendNotification(localRelationNotification);
/*      */ 
/* 3620 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "sendNotificationInt");
/*      */   }
/*      */ 
/*      */   private void handleReferenceUnregistration(String paramString, ObjectName paramObjectName, List<String> paramList)
/*      */     throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException, RoleNotFoundException
/*      */   {
/* 3651 */     if ((paramString == null) || (paramList == null) || (paramObjectName == null))
/*      */     {
/* 3654 */       str1 = "Invalid parameter.";
/* 3655 */       throw new IllegalArgumentException(str1);
/*      */     }
/*      */ 
/* 3658 */     JmxProperties.RELATION_LOGGER.entering(RelationService.class.getName(), "handleReferenceUnregistration", new Object[] { paramString, paramObjectName, paramList });
/*      */ 
/* 3663 */     isActive();
/*      */ 
/* 3667 */     String str1 = getRelationTypeName(paramString);
/*      */ 
/* 3671 */     Object localObject = getRelation(paramString);
/*      */ 
/* 3674 */     int i = 0;
/*      */ 
/* 3676 */     for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { str2 = (String)localIterator.next();
/*      */ 
/* 3678 */       if (i != 0)
/*      */       {
/*      */         break;
/*      */       }
/*      */ 
/* 3687 */       int j = getRoleCardinality(paramString, str2).intValue();
/*      */ 
/* 3691 */       int k = j - 1;
/*      */       RoleInfo localRoleInfo;
/*      */       try
/*      */       {
/* 3699 */         localRoleInfo = getRoleInfo(str1, str2);
/*      */       }
/*      */       catch (RelationTypeNotFoundException localRelationTypeNotFoundException2) {
/* 3702 */         throw new RuntimeException(localRelationTypeNotFoundException2.getMessage());
/*      */       } catch (RoleInfoNotFoundException localRoleInfoNotFoundException) {
/* 3704 */         throw new RuntimeException(localRoleInfoNotFoundException.getMessage());
/*      */       }
/*      */ 
/* 3708 */       boolean bool = localRoleInfo.checkMinDegree(k);
/*      */ 
/* 3710 */       if (!bool)
/*      */       {
/* 3712 */         i = 1;
/*      */       }
/*      */     }
/*      */     String str2;
/* 3716 */     if (i != 0)
/*      */     {
/* 3718 */       removeRelation(paramString);
/*      */     }
/*      */     else
/*      */     {
/* 3733 */       for (localIterator = paramList.iterator(); localIterator.hasNext(); ) { str2 = (String)localIterator.next();
/*      */ 
/* 3735 */         if ((localObject instanceof RelationSupport))
/*      */         {
/*      */           try
/*      */           {
/* 3745 */             ((RelationSupport)localObject).handleMBeanUnregistrationInt(paramObjectName, str2, true, this);
/*      */           }
/*      */           catch (RelationTypeNotFoundException localRelationTypeNotFoundException1)
/*      */           {
/* 3751 */             throw new RuntimeException(localRelationTypeNotFoundException1.getMessage());
/*      */           } catch (InvalidRoleValueException localInvalidRoleValueException) {
/* 3753 */             throw new RuntimeException(localInvalidRoleValueException.getMessage());
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/* 3758 */           Object[] arrayOfObject = new Object[2];
/* 3759 */           arrayOfObject[0] = paramObjectName;
/* 3760 */           arrayOfObject[1] = str2;
/* 3761 */           String[] arrayOfString = new String[2];
/* 3762 */           arrayOfString[0] = "javax.management.ObjectName";
/* 3763 */           arrayOfString[1] = "java.lang.String";
/*      */           try
/*      */           {
/* 3769 */             this.myMBeanServer.invoke((ObjectName)localObject, "handleMBeanUnregistration", arrayOfObject, arrayOfString);
/*      */           }
/*      */           catch (InstanceNotFoundException localInstanceNotFoundException)
/*      */           {
/* 3774 */             throw new RuntimeException(localInstanceNotFoundException.getMessage());
/*      */           } catch (ReflectionException localReflectionException) {
/* 3776 */             throw new RuntimeException(localReflectionException.getMessage());
/*      */           } catch (MBeanException localMBeanException) {
/* 3778 */             Exception localException = localMBeanException.getTargetException();
/* 3779 */             throw new RuntimeException(localException.getMessage());
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 3786 */     JmxProperties.RELATION_LOGGER.exiting(RelationService.class.getName(), "handleReferenceUnregistration");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RelationService
 * JD-Core Version:    0.6.2
 */
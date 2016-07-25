package javax.management.relation;

import java.util.List;
import java.util.Map;
import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;

public abstract interface RelationServiceMBean
{
  public abstract void isActive()
    throws RelationServiceNotRegisteredException;

  public abstract boolean getPurgeFlag();

  public abstract void setPurgeFlag(boolean paramBoolean);

  public abstract void createRelationType(String paramString, RoleInfo[] paramArrayOfRoleInfo)
    throws IllegalArgumentException, InvalidRelationTypeException;

  public abstract void addRelationType(RelationType paramRelationType)
    throws IllegalArgumentException, InvalidRelationTypeException;

  public abstract List<String> getAllRelationTypeNames();

  public abstract List<RoleInfo> getRoleInfos(String paramString)
    throws IllegalArgumentException, RelationTypeNotFoundException;

  public abstract RoleInfo getRoleInfo(String paramString1, String paramString2)
    throws IllegalArgumentException, RelationTypeNotFoundException, RoleInfoNotFoundException;

  public abstract void removeRelationType(String paramString)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationTypeNotFoundException;

  public abstract void createRelation(String paramString1, String paramString2, RoleList paramRoleList)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RoleNotFoundException, InvalidRelationIdException, RelationTypeNotFoundException, InvalidRoleValueException;

  public abstract void addRelation(ObjectName paramObjectName)
    throws IllegalArgumentException, RelationServiceNotRegisteredException, NoSuchMethodException, InvalidRelationIdException, InstanceNotFoundException, InvalidRelationServiceException, RelationTypeNotFoundException, RoleNotFoundException, InvalidRoleValueException;

  public abstract ObjectName isRelationMBean(String paramString)
    throws IllegalArgumentException, RelationNotFoundException;

  public abstract String isRelation(ObjectName paramObjectName)
    throws IllegalArgumentException;

  public abstract Boolean hasRelation(String paramString)
    throws IllegalArgumentException;

  public abstract List<String> getAllRelationIds();

  public abstract Integer checkRoleReading(String paramString1, String paramString2)
    throws IllegalArgumentException, RelationTypeNotFoundException;

  public abstract Integer checkRoleWriting(Role paramRole, String paramString, Boolean paramBoolean)
    throws IllegalArgumentException, RelationTypeNotFoundException;

  public abstract void sendRelationCreationNotification(String paramString)
    throws IllegalArgumentException, RelationNotFoundException;

  public abstract void sendRoleUpdateNotification(String paramString, Role paramRole, List<ObjectName> paramList)
    throws IllegalArgumentException, RelationNotFoundException;

  public abstract void sendRelationRemovalNotification(String paramString, List<ObjectName> paramList)
    throws IllegalArgumentException, RelationNotFoundException;

  public abstract void updateRoleMap(String paramString, Role paramRole, List<ObjectName> paramList)
    throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationNotFoundException;

  public abstract void removeRelation(String paramString)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException;

  public abstract void purgeRelations()
    throws RelationServiceNotRegisteredException;

  public abstract Map<String, List<String>> findReferencingRelations(ObjectName paramObjectName, String paramString1, String paramString2)
    throws IllegalArgumentException;

  public abstract Map<ObjectName, List<String>> findAssociatedMBeans(ObjectName paramObjectName, String paramString1, String paramString2)
    throws IllegalArgumentException;

  public abstract List<String> findRelationsOfType(String paramString)
    throws IllegalArgumentException, RelationTypeNotFoundException;

  public abstract List<ObjectName> getRole(String paramString1, String paramString2)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException;

  public abstract RoleResult getRoles(String paramString, String[] paramArrayOfString)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException;

  public abstract RoleResult getAllRoles(String paramString)
    throws IllegalArgumentException, RelationNotFoundException, RelationServiceNotRegisteredException;

  public abstract Integer getRoleCardinality(String paramString1, String paramString2)
    throws IllegalArgumentException, RelationNotFoundException, RoleNotFoundException;

  public abstract void setRole(String paramString, Role paramRole)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException, RoleNotFoundException, InvalidRoleValueException, RelationTypeNotFoundException;

  public abstract RoleResult setRoles(String paramString, RoleList paramRoleList)
    throws RelationServiceNotRegisteredException, IllegalArgumentException, RelationNotFoundException;

  public abstract Map<ObjectName, List<String>> getReferencedMBeans(String paramString)
    throws IllegalArgumentException, RelationNotFoundException;

  public abstract String getRelationTypeName(String paramString)
    throws IllegalArgumentException, RelationNotFoundException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RelationServiceMBean
 * JD-Core Version:    0.6.2
 */
package javax.management.relation;

import java.util.List;
import java.util.Map;
import javax.management.ObjectName;

public abstract interface Relation
{
  public abstract List<ObjectName> getRole(String paramString)
    throws IllegalArgumentException, RoleNotFoundException, RelationServiceNotRegisteredException;

  public abstract RoleResult getRoles(String[] paramArrayOfString)
    throws IllegalArgumentException, RelationServiceNotRegisteredException;

  public abstract Integer getRoleCardinality(String paramString)
    throws IllegalArgumentException, RoleNotFoundException;

  public abstract RoleResult getAllRoles()
    throws RelationServiceNotRegisteredException;

  public abstract RoleList retrieveAllRoles();

  public abstract void setRole(Role paramRole)
    throws IllegalArgumentException, RoleNotFoundException, RelationTypeNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationNotFoundException;

  public abstract RoleResult setRoles(RoleList paramRoleList)
    throws IllegalArgumentException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException;

  public abstract void handleMBeanUnregistration(ObjectName paramObjectName, String paramString)
    throws IllegalArgumentException, RoleNotFoundException, InvalidRoleValueException, RelationServiceNotRegisteredException, RelationTypeNotFoundException, RelationNotFoundException;

  public abstract Map<ObjectName, List<String>> getReferencedMBeans();

  public abstract String getRelationTypeName();

  public abstract ObjectName getRelationServiceName();

  public abstract String getRelationId();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.Relation
 * JD-Core Version:    0.6.2
 */
package javax.management.relation;

public abstract interface RelationSupportMBean extends Relation
{
  public abstract Boolean isInRelationService();

  public abstract void setRelationServiceManagementFlag(Boolean paramBoolean)
    throws IllegalArgumentException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.relation.RelationSupportMBean
 * JD-Core Version:    0.6.2
 */
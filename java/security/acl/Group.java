package java.security.acl;

import java.security.Principal;
import java.util.Enumeration;

public abstract interface Group extends Principal
{
  public abstract boolean addMember(Principal paramPrincipal);

  public abstract boolean removeMember(Principal paramPrincipal);

  public abstract boolean isMember(Principal paramPrincipal);

  public abstract Enumeration<? extends Principal> members();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.acl.Group
 * JD-Core Version:    0.6.2
 */
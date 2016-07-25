package com.sun.security.auth;

import javax.security.auth.Subject;

public abstract interface PrincipalComparator
{
  public abstract boolean implies(Subject paramSubject);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.security.auth.PrincipalComparator
 * JD-Core Version:    0.6.2
 */
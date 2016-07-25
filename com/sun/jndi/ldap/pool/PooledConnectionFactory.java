package com.sun.jndi.ldap.pool;

import javax.naming.NamingException;

public abstract interface PooledConnectionFactory
{
  public abstract PooledConnection createPooledConnection(PoolCallback paramPoolCallback)
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.pool.PooledConnectionFactory
 * JD-Core Version:    0.6.2
 */
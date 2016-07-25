package com.sun.xml.internal.ws.policy;

abstract interface PolicyMapKeyHandler
{
  public abstract boolean areEqual(PolicyMapKey paramPolicyMapKey1, PolicyMapKey paramPolicyMapKey2);

  public abstract int generateHashCode(PolicyMapKey paramPolicyMapKey);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
 * JD-Core Version:    0.6.2
 */
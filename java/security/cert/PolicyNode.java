package java.security.cert;

import java.util.Iterator;
import java.util.Set;

public abstract interface PolicyNode
{
  public abstract PolicyNode getParent();

  public abstract Iterator<? extends PolicyNode> getChildren();

  public abstract int getDepth();

  public abstract String getValidPolicy();

  public abstract Set<? extends PolicyQualifierInfo> getPolicyQualifiers();

  public abstract Set<String> getExpectedPolicies();

  public abstract boolean isCritical();
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.PolicyNode
 * JD-Core Version:    0.6.2
 */
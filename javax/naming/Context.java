package javax.naming;

import java.util.Hashtable;

public abstract interface Context
{
  public static final String INITIAL_CONTEXT_FACTORY = "java.naming.factory.initial";
  public static final String OBJECT_FACTORIES = "java.naming.factory.object";
  public static final String STATE_FACTORIES = "java.naming.factory.state";
  public static final String URL_PKG_PREFIXES = "java.naming.factory.url.pkgs";
  public static final String PROVIDER_URL = "java.naming.provider.url";
  public static final String DNS_URL = "java.naming.dns.url";
  public static final String AUTHORITATIVE = "java.naming.authoritative";
  public static final String BATCHSIZE = "java.naming.batchsize";
  public static final String REFERRAL = "java.naming.referral";
  public static final String SECURITY_PROTOCOL = "java.naming.security.protocol";
  public static final String SECURITY_AUTHENTICATION = "java.naming.security.authentication";
  public static final String SECURITY_PRINCIPAL = "java.naming.security.principal";
  public static final String SECURITY_CREDENTIALS = "java.naming.security.credentials";
  public static final String LANGUAGE = "java.naming.language";
  public static final String APPLET = "java.naming.applet";

  public abstract Object lookup(Name paramName)
    throws NamingException;

  public abstract Object lookup(String paramString)
    throws NamingException;

  public abstract void bind(Name paramName, Object paramObject)
    throws NamingException;

  public abstract void bind(String paramString, Object paramObject)
    throws NamingException;

  public abstract void rebind(Name paramName, Object paramObject)
    throws NamingException;

  public abstract void rebind(String paramString, Object paramObject)
    throws NamingException;

  public abstract void unbind(Name paramName)
    throws NamingException;

  public abstract void unbind(String paramString)
    throws NamingException;

  public abstract void rename(Name paramName1, Name paramName2)
    throws NamingException;

  public abstract void rename(String paramString1, String paramString2)
    throws NamingException;

  public abstract NamingEnumeration<NameClassPair> list(Name paramName)
    throws NamingException;

  public abstract NamingEnumeration<NameClassPair> list(String paramString)
    throws NamingException;

  public abstract NamingEnumeration<Binding> listBindings(Name paramName)
    throws NamingException;

  public abstract NamingEnumeration<Binding> listBindings(String paramString)
    throws NamingException;

  public abstract void destroySubcontext(Name paramName)
    throws NamingException;

  public abstract void destroySubcontext(String paramString)
    throws NamingException;

  public abstract Context createSubcontext(Name paramName)
    throws NamingException;

  public abstract Context createSubcontext(String paramString)
    throws NamingException;

  public abstract Object lookupLink(Name paramName)
    throws NamingException;

  public abstract Object lookupLink(String paramString)
    throws NamingException;

  public abstract NameParser getNameParser(Name paramName)
    throws NamingException;

  public abstract NameParser getNameParser(String paramString)
    throws NamingException;

  public abstract Name composeName(Name paramName1, Name paramName2)
    throws NamingException;

  public abstract String composeName(String paramString1, String paramString2)
    throws NamingException;

  public abstract Object addToEnvironment(String paramString, Object paramObject)
    throws NamingException;

  public abstract Object removeFromEnvironment(String paramString)
    throws NamingException;

  public abstract Hashtable<?, ?> getEnvironment()
    throws NamingException;

  public abstract void close()
    throws NamingException;

  public abstract String getNameInNamespace()
    throws NamingException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.Context
 * JD-Core Version:    0.6.2
 */
/*     */ package com.sun.jndi.rmi.registry;
/*     */ 
/*     */ import java.rmi.AccessException;
/*     */ import java.rmi.AlreadyBoundException;
/*     */ import java.rmi.ConnectException;
/*     */ import java.rmi.ConnectIOException;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.RMISecurityManager;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.ServerException;
/*     */ import java.rmi.StubNotFoundException;
/*     */ import java.rmi.UnknownHostException;
/*     */ import java.rmi.UnmarshalException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.ExportException;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.SocketSecurityException;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameAlreadyBoundException;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NameParser;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.NoPermissionException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.ServiceUnavailableException;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.spi.NamingManager;
/*     */ 
/*     */ public class RegistryContext
/*     */   implements Context, Referenceable
/*     */ {
/*     */   private Hashtable environment;
/*     */   private Registry registry;
/*     */   private String host;
/*     */   private int port;
/*  53 */   private static final NameParser nameParser = new AtomicNameParser();
/*     */   private static final String SOCKET_FACTORY = "com.sun.jndi.rmi.factory.socket";
/*  56 */   Reference reference = null;
/*     */   public static final String SECURITY_MGR = "java.naming.rmi.security.manager";
/*     */ 
/*     */   public RegistryContext(String paramString, int paramInt, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  73 */     this.environment = (paramHashtable == null ? new Hashtable(5) : paramHashtable);
/*  74 */     if (this.environment.get("java.naming.rmi.security.manager") != null) {
/*  75 */       installSecurityMgr();
/*     */     }
/*     */ 
/*  79 */     if ((paramString != null) && (paramString.charAt(0) == '[')) {
/*  80 */       paramString = paramString.substring(1, paramString.length() - 1);
/*     */     }
/*     */ 
/*  83 */     RMIClientSocketFactory localRMIClientSocketFactory = (RMIClientSocketFactory)this.environment.get("com.sun.jndi.rmi.factory.socket");
/*     */ 
/*  85 */     this.registry = getRegistry(paramString, paramInt, localRMIClientSocketFactory);
/*  86 */     this.host = paramString;
/*  87 */     this.port = paramInt;
/*     */   }
/*     */ 
/*     */   RegistryContext(RegistryContext paramRegistryContext)
/*     */   {
/*  97 */     this.environment = ((Hashtable)paramRegistryContext.environment.clone());
/*  98 */     this.registry = paramRegistryContext.registry;
/*  99 */     this.host = paramRegistryContext.host;
/* 100 */     this.port = paramRegistryContext.port;
/* 101 */     this.reference = paramRegistryContext.reference;
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 105 */     close();
/*     */   }
/*     */ 
/*     */   public Object lookup(Name paramName) throws NamingException {
/* 109 */     if (paramName.isEmpty())
/* 110 */       return new RegistryContext(this);
/*     */     Remote localRemote;
/*     */     try
/*     */     {
/* 114 */       localRemote = this.registry.lookup(paramName.get(0));
/*     */     } catch (NotBoundException localNotBoundException) {
/* 116 */       throw new NameNotFoundException(paramName.get(0));
/*     */     } catch (RemoteException localRemoteException) {
/* 118 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/* 120 */     return decodeObject(localRemote, paramName.getPrefix(1));
/*     */   }
/*     */ 
/*     */   public Object lookup(String paramString) throws NamingException {
/* 124 */     return lookup(new CompositeName(paramString));
/*     */   }
/*     */ 
/*     */   public void bind(Name paramName, Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 132 */     if (paramName.isEmpty()) {
/* 133 */       throw new InvalidNameException("RegistryContext: Cannot bind empty name");
/*     */     }
/*     */     try
/*     */     {
/* 137 */       this.registry.bind(paramName.get(0), encodeObject(paramObject, paramName.getPrefix(1)));
/*     */     } catch (AlreadyBoundException localAlreadyBoundException) {
/* 139 */       NameAlreadyBoundException localNameAlreadyBoundException = new NameAlreadyBoundException(paramName.get(0));
/* 140 */       localNameAlreadyBoundException.setRootCause(localAlreadyBoundException);
/* 141 */       throw localNameAlreadyBoundException;
/*     */     } catch (RemoteException localRemoteException) {
/* 143 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void bind(String paramString, Object paramObject) throws NamingException {
/* 148 */     bind(new CompositeName(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void rebind(Name paramName, Object paramObject) throws NamingException {
/* 152 */     if (paramName.isEmpty()) {
/* 153 */       throw new InvalidNameException("RegistryContext: Cannot rebind empty name");
/*     */     }
/*     */     try
/*     */     {
/* 157 */       this.registry.rebind(paramName.get(0), encodeObject(paramObject, paramName.getPrefix(1)));
/*     */     } catch (RemoteException localRemoteException) {
/* 159 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void rebind(String paramString, Object paramObject) throws NamingException {
/* 164 */     rebind(new CompositeName(paramString), paramObject);
/*     */   }
/*     */ 
/*     */   public void unbind(Name paramName) throws NamingException {
/* 168 */     if (paramName.isEmpty()) {
/* 169 */       throw new InvalidNameException("RegistryContext: Cannot unbind empty name");
/*     */     }
/*     */     try
/*     */     {
/* 173 */       this.registry.unbind(paramName.get(0));
/*     */     } catch (NotBoundException localNotBoundException) {
/*     */     }
/*     */     catch (RemoteException localRemoteException) {
/* 177 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unbind(String paramString) throws NamingException {
/* 182 */     unbind(new CompositeName(paramString));
/*     */   }
/*     */ 
/*     */   public void rename(Name paramName1, Name paramName2)
/*     */     throws NamingException
/*     */   {
/* 190 */     bind(paramName2, lookup(paramName1));
/* 191 */     unbind(paramName1);
/*     */   }
/*     */ 
/*     */   public void rename(String paramString1, String paramString2) throws NamingException {
/* 195 */     rename(new CompositeName(paramString1), new CompositeName(paramString2));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(Name paramName) throws NamingException {
/* 199 */     if (!paramName.isEmpty()) {
/* 200 */       throw new InvalidNameException("RegistryContext: can only list \"\"");
/*     */     }
/*     */     try
/*     */     {
/* 204 */       String[] arrayOfString = this.registry.list();
/* 205 */       return new NameClassPairEnumeration(arrayOfString);
/*     */     } catch (RemoteException localRemoteException) {
/* 207 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration list(String paramString) throws NamingException {
/* 212 */     return list(new CompositeName(paramString));
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(Name paramName)
/*     */     throws NamingException
/*     */   {
/* 218 */     if (!paramName.isEmpty()) {
/* 219 */       throw new InvalidNameException("RegistryContext: can only list \"\"");
/*     */     }
/*     */     try
/*     */     {
/* 223 */       String[] arrayOfString = this.registry.list();
/* 224 */       return new BindingEnumeration(this, arrayOfString);
/*     */     } catch (RemoteException localRemoteException) {
/* 226 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */   }
/*     */ 
/*     */   public NamingEnumeration listBindings(String paramString) throws NamingException {
/* 231 */     return listBindings(new CompositeName(paramString));
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(Name paramName) throws NamingException {
/* 235 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public void destroySubcontext(String paramString) throws NamingException {
/* 239 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(Name paramName) throws NamingException {
/* 243 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public Context createSubcontext(String paramString) throws NamingException {
/* 247 */     throw new OperationNotSupportedException();
/*     */   }
/*     */ 
/*     */   public Object lookupLink(Name paramName) throws NamingException {
/* 251 */     return lookup(paramName);
/*     */   }
/*     */ 
/*     */   public Object lookupLink(String paramString) throws NamingException {
/* 255 */     return lookup(paramString);
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(Name paramName) throws NamingException {
/* 259 */     return nameParser;
/*     */   }
/*     */ 
/*     */   public NameParser getNameParser(String paramString) throws NamingException {
/* 263 */     return nameParser;
/*     */   }
/*     */ 
/*     */   public Name composeName(Name paramName1, Name paramName2) throws NamingException {
/* 267 */     Name localName = (Name)paramName2.clone();
/* 268 */     return localName.addAll(paramName1);
/*     */   }
/*     */ 
/*     */   public String composeName(String paramString1, String paramString2)
/*     */     throws NamingException
/*     */   {
/* 274 */     return composeName(new CompositeName(paramString1), new CompositeName(paramString2)).toString();
/*     */   }
/*     */ 
/*     */   public Object removeFromEnvironment(String paramString)
/*     */     throws NamingException
/*     */   {
/* 281 */     return this.environment.remove(paramString);
/*     */   }
/*     */ 
/*     */   public Object addToEnvironment(String paramString, Object paramObject)
/*     */     throws NamingException
/*     */   {
/* 287 */     if (paramString.equals("java.naming.rmi.security.manager")) {
/* 288 */       installSecurityMgr();
/*     */     }
/* 290 */     return this.environment.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Hashtable getEnvironment() throws NamingException {
/* 294 */     return (Hashtable)this.environment.clone();
/*     */   }
/*     */ 
/*     */   public void close() {
/* 298 */     this.environment = null;
/* 299 */     this.registry = null;
/*     */   }
/*     */ 
/*     */   public String getNameInNamespace()
/*     */   {
/* 305 */     return "";
/*     */   }
/*     */ 
/*     */   public Reference getReference()
/*     */     throws NamingException
/*     */   {
/* 320 */     if (this.reference != null) {
/* 321 */       return (Reference)this.reference.clone();
/*     */     }
/* 323 */     if ((this.host == null) || (this.host.equals("localhost"))) {
/* 324 */       throw new ConfigurationException("Cannot create a reference for an RMI registry whose host was unspecified or specified as \"localhost\"");
/*     */     }
/*     */ 
/* 328 */     String str = "rmi://";
/*     */ 
/* 331 */     str = str + this.host;
/*     */ 
/* 333 */     if (this.port > 0) {
/* 334 */       str = str + ":" + Integer.toString(this.port);
/*     */     }
/* 336 */     StringRefAddr localStringRefAddr = new StringRefAddr("URL", str);
/*     */ 
/* 338 */     return new Reference(RegistryContext.class.getName(), localStringRefAddr, RegistryContextFactory.class.getName(), null);
/*     */   }
/*     */ 
/*     */   public static NamingException wrapRemoteException(RemoteException paramRemoteException)
/*     */   {
/*     */     Object localObject;
/* 352 */     if ((paramRemoteException instanceof ConnectException)) {
/* 353 */       localObject = new ServiceUnavailableException();
/*     */     }
/* 355 */     else if ((paramRemoteException instanceof AccessException)) {
/* 356 */       localObject = new NoPermissionException();
/*     */     }
/* 358 */     else if (((paramRemoteException instanceof StubNotFoundException)) || ((paramRemoteException instanceof UnknownHostException)) || ((paramRemoteException instanceof SocketSecurityException)))
/*     */     {
/* 361 */       localObject = new ConfigurationException();
/*     */     }
/* 363 */     else if (((paramRemoteException instanceof ExportException)) || ((paramRemoteException instanceof ConnectIOException)) || ((paramRemoteException instanceof MarshalException)) || ((paramRemoteException instanceof UnmarshalException)) || ((paramRemoteException instanceof NoSuchObjectException)))
/*     */     {
/* 368 */       localObject = new CommunicationException();
/*     */     }
/* 370 */     else if (((paramRemoteException instanceof ServerException)) && ((paramRemoteException.detail instanceof RemoteException)))
/*     */     {
/* 372 */       localObject = wrapRemoteException((RemoteException)paramRemoteException.detail);
/*     */     }
/*     */     else {
/* 375 */       localObject = new NamingException();
/*     */     }
/* 377 */     ((NamingException)localObject).setRootCause(paramRemoteException);
/* 378 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static Registry getRegistry(String paramString, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 394 */       if (paramRMIClientSocketFactory == null) {
/* 395 */         return LocateRegistry.getRegistry(paramString, paramInt);
/*     */       }
/* 397 */       return LocateRegistry.getRegistry(paramString, paramInt, paramRMIClientSocketFactory);
/*     */     }
/*     */     catch (RemoteException localRemoteException) {
/* 400 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void installSecurityMgr()
/*     */   {
/*     */     try
/*     */     {
/* 411 */       System.setSecurityManager(new RMISecurityManager());
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private Remote encodeObject(Object paramObject, Name paramName)
/*     */     throws NamingException, RemoteException
/*     */   {
/* 428 */     paramObject = NamingManager.getStateToBind(paramObject, paramName, this, this.environment);
/*     */ 
/* 430 */     if ((paramObject instanceof Remote)) {
/* 431 */       return (Remote)paramObject;
/*     */     }
/* 433 */     if ((paramObject instanceof Reference)) {
/* 434 */       return new ReferenceWrapper((Reference)paramObject);
/*     */     }
/* 436 */     if ((paramObject instanceof Referenceable)) {
/* 437 */       return new ReferenceWrapper(((Referenceable)paramObject).getReference());
/*     */     }
/* 439 */     throw new IllegalArgumentException("RegistryContext: object to bind must be Remote, Reference, or Referenceable");
/*     */   }
/*     */ 
/*     */   private Object decodeObject(Remote paramRemote, Name paramName)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 453 */       Remote localRemote = (paramRemote instanceof RemoteReference) ? ((RemoteReference)paramRemote).getReference() : paramRemote;
/*     */ 
/* 456 */       return NamingManager.getObjectInstance(localRemote, paramName, this, this.environment);
/*     */     }
/*     */     catch (NamingException localNamingException1) {
/* 459 */       throw localNamingException1;
/*     */     } catch (RemoteException localRemoteException) {
/* 461 */       throw ((NamingException)wrapRemoteException(localRemoteException).fillInStackTrace());
/*     */     }
/*     */     catch (Exception localException) {
/* 464 */       NamingException localNamingException2 = new NamingException();
/* 465 */       localNamingException2.setRootCause(localException);
/* 466 */       throw localNamingException2;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.rmi.registry.RegistryContext
 * JD-Core Version:    0.6.2
 */
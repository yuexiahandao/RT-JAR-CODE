/*      */ package com.sun.jndi.cosnaming;
/*      */ 
/*      */ import com.sun.jndi.toolkit.corba.CorbaUtils;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.naming.CannotProceedException;
/*      */ import javax.naming.CommunicationException;
/*      */ import javax.naming.CompositeName;
/*      */ import javax.naming.ConfigurationException;
/*      */ import javax.naming.Context;
/*      */ import javax.naming.InvalidNameException;
/*      */ import javax.naming.Name;
/*      */ import javax.naming.NameNotFoundException;
/*      */ import javax.naming.NameParser;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.NotContextException;
/*      */ import javax.naming.RefAddr;
/*      */ import javax.naming.Reference;
/*      */ import javax.naming.spi.NamingManager;
/*      */ import javax.naming.spi.ResolveResult;
/*      */ import org.omg.CORBA.BAD_PARAM;
/*      */ import org.omg.CORBA.COMM_FAILURE;
/*      */ import org.omg.CORBA.INV_OBJREF;
/*      */ import org.omg.CORBA.ORB;
/*      */ import org.omg.CORBA.ORBPackage.InvalidName;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import org.omg.CosNaming.NameComponent;
/*      */ import org.omg.CosNaming.NamingContext;
/*      */ import org.omg.CosNaming.NamingContextHelper;
/*      */ import org.omg.CosNaming.NamingContextPackage.NotFound;
/*      */ import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
/*      */ 
/*      */ public class CNCtx
/*      */   implements Context
/*      */ {
/*      */   private static final boolean debug = false;
/*      */   private static ORB _defaultOrb;
/*      */   ORB _orb;
/*      */   public NamingContext _nc;
/*   78 */   private NameComponent[] _name = null;
/*      */   Hashtable _env;
/*   81 */   static final CNNameParser parser = new CNNameParser();
/*      */   private static final String FED_PROP = "com.sun.jndi.cosnaming.federation";
/*   84 */   boolean federation = false;
/*      */ 
/*   87 */   OrbReuseTracker orbTracker = null;
/*      */   int enumCount;
/*   89 */   boolean isCloseCalled = false;
/*      */ 
/*      */   private static synchronized ORB getDefaultOrb()
/*      */   {
/*   71 */     if (_defaultOrb == null) {
/*   72 */       _defaultOrb = CorbaUtils.getOrb(null, -1, new Hashtable());
/*      */     }
/*      */ 
/*   75 */     return _defaultOrb;
/*      */   }
/*      */ 
/*      */   CNCtx(Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  100 */     if (paramHashtable != null) {
/*  101 */       paramHashtable = (Hashtable)paramHashtable.clone();
/*      */     }
/*  103 */     this._env = paramHashtable;
/*  104 */     this.federation = "true".equals(paramHashtable != null ? paramHashtable.get("com.sun.jndi.cosnaming.federation") : null);
/*  105 */     initOrbAndRootContext(paramHashtable);
/*      */   }
/*      */ 
/*      */   private CNCtx()
/*      */   {
/*      */   }
/*      */ 
/*      */   public static ResolveResult createUsingURL(String paramString, Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  116 */     CNCtx localCNCtx = new CNCtx();
/*  117 */     if (paramHashtable != null) {
/*  118 */       paramHashtable = (Hashtable)paramHashtable.clone();
/*      */     }
/*  120 */     localCNCtx._env = paramHashtable;
/*  121 */     String str = localCNCtx.initUsingUrl(paramHashtable != null ? (ORB)paramHashtable.get("java.naming.corba.orb") : null, paramString, paramHashtable);
/*      */ 
/*  133 */     return new ResolveResult(localCNCtx, parser.parse(str));
/*      */   }
/*      */ 
/*      */   CNCtx(ORB paramORB, OrbReuseTracker paramOrbReuseTracker, NamingContext paramNamingContext, Hashtable paramHashtable, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NamingException
/*      */   {
/*  149 */     if ((paramORB == null) || (paramNamingContext == null)) {
/*  150 */       throw new ConfigurationException("Must supply ORB or NamingContext");
/*      */     }
/*  152 */     if (paramORB != null)
/*  153 */       this._orb = paramORB;
/*      */     else {
/*  155 */       this._orb = getDefaultOrb();
/*      */     }
/*  157 */     this._nc = paramNamingContext;
/*  158 */     this._env = paramHashtable;
/*  159 */     this._name = paramArrayOfNameComponent;
/*  160 */     this.federation = "true".equals(paramHashtable != null ? paramHashtable.get("com.sun.jndi.cosnaming.federation") : null);
/*      */   }
/*      */ 
/*      */   NameComponent[] makeFullName(NameComponent[] paramArrayOfNameComponent) {
/*  164 */     if ((this._name == null) || (this._name.length == 0)) {
/*  165 */       return paramArrayOfNameComponent;
/*      */     }
/*  167 */     NameComponent[] arrayOfNameComponent = new NameComponent[this._name.length + paramArrayOfNameComponent.length];
/*      */ 
/*  170 */     System.arraycopy(this._name, 0, arrayOfNameComponent, 0, this._name.length);
/*      */ 
/*  173 */     System.arraycopy(paramArrayOfNameComponent, 0, arrayOfNameComponent, this._name.length, paramArrayOfNameComponent.length);
/*  174 */     return arrayOfNameComponent;
/*      */   }
/*      */ 
/*      */   public String getNameInNamespace() throws NamingException
/*      */   {
/*  179 */     if ((this._name == null) || (this._name.length == 0)) {
/*  180 */       return "";
/*      */     }
/*  182 */     return CNNameParser.cosNameToInsString(this._name);
/*      */   }
/*      */ 
/*      */   private static boolean isCorbaUrl(String paramString)
/*      */   {
/*  190 */     return (paramString.startsWith("iiop://")) || (paramString.startsWith("iiopname://")) || (paramString.startsWith("corbaname:"));
/*      */   }
/*      */ 
/*      */   private void initOrbAndRootContext(Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  226 */     ORB localORB = null;
/*  227 */     String str1 = null;
/*      */ 
/*  229 */     if ((localORB == null) && (paramHashtable != null)) {
/*  230 */       localORB = (ORB)paramHashtable.get("java.naming.corba.orb");
/*      */     }
/*      */ 
/*  233 */     if (localORB == null) {
/*  234 */       localORB = getDefaultOrb();
/*      */     }
/*      */ 
/*  237 */     String str2 = null;
/*  238 */     if (paramHashtable != null) {
/*  239 */       str2 = (String)paramHashtable.get("java.naming.provider.url");
/*      */     }
/*      */ 
/*  242 */     if ((str2 != null) && (!isCorbaUrl(str2)))
/*      */     {
/*  245 */       str1 = getStringifiedIor(str2);
/*  246 */       setOrbAndRootContext(localORB, str1);
/*  247 */     } else if (str2 != null)
/*      */     {
/*  250 */       String str3 = initUsingUrl(localORB, str2, paramHashtable);
/*      */ 
/*  253 */       if (str3.length() > 0) {
/*  254 */         this._name = CNNameParser.nameToCosName(parser.parse(str3));
/*      */         try {
/*  256 */           org.omg.CORBA.Object localObject = this._nc.resolve(this._name);
/*  257 */           this._nc = NamingContextHelper.narrow(localObject);
/*  258 */           if (this._nc == null)
/*  259 */             throw new ConfigurationException(str3 + " does not name a NamingContext");
/*      */         }
/*      */         catch (BAD_PARAM localBAD_PARAM)
/*      */         {
/*  263 */           throw new ConfigurationException(str3 + " does not name a NamingContext");
/*      */         }
/*      */         catch (Exception localException) {
/*  266 */           throw ExceptionMapper.mapException(localException, this, this._name);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  274 */       setOrbAndRootContext(localORB, (String)null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private String initUsingUrl(ORB paramORB, String paramString, Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  281 */     if ((paramString.startsWith("iiop://")) || (paramString.startsWith("iiopname://"))) {
/*  282 */       return initUsingIiopUrl(paramORB, paramString, paramHashtable);
/*      */     }
/*  284 */     return initUsingCorbanameUrl(paramORB, paramString, paramHashtable);
/*      */   }
/*      */ 
/*      */   private String initUsingIiopUrl(ORB paramORB, String paramString, Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  294 */     if (paramORB == null)
/*  295 */       paramORB = getDefaultOrb();
/*      */     try
/*      */     {
/*  298 */       IiopUrl localIiopUrl = new IiopUrl(paramString);
/*      */ 
/*  300 */       Vector localVector = localIiopUrl.getAddresses();
/*      */ 
/*  302 */       java.lang.Object localObject = null;
/*      */ 
/*  304 */       for (int i = 0; i < localVector.size(); i++) {
/*  305 */         IiopUrl.Address localAddress = (IiopUrl.Address)localVector.elementAt(i);
/*      */         try
/*      */         {
/*  309 */           String str = "corbaloc:iiop:" + localAddress.host + ":" + localAddress.port + "/NameService";
/*      */ 
/*  314 */           org.omg.CORBA.Object localObject1 = paramORB.string_to_object(str);
/*      */ 
/*  316 */           setOrbAndRootContext(paramORB, localObject1);
/*  317 */           return localIiopUrl.getStringName();
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  327 */           setOrbAndRootContext(paramORB, (String)null);
/*  328 */           return localIiopUrl.getStringName();
/*      */         }
/*      */         catch (NamingException localNamingException) {
/*  331 */           localObject = localNamingException;
/*      */         }
/*      */       }
/*  334 */       if (localObject != null) {
/*  335 */         throw localObject;
/*      */       }
/*  337 */       throw new ConfigurationException("Problem with URL: " + paramString);
/*      */     }
/*      */     catch (MalformedURLException localMalformedURLException) {
/*  340 */       throw new ConfigurationException(localMalformedURLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private String initUsingCorbanameUrl(ORB paramORB, String paramString, Hashtable paramHashtable)
/*      */     throws NamingException
/*      */   {
/*  350 */     if (paramORB == null)
/*  351 */       paramORB = getDefaultOrb();
/*      */     try
/*      */     {
/*  354 */       CorbanameUrl localCorbanameUrl = new CorbanameUrl(paramString);
/*      */ 
/*  356 */       String str1 = localCorbanameUrl.getLocation();
/*  357 */       String str2 = localCorbanameUrl.getStringName();
/*      */ 
/*  359 */       setOrbAndRootContext(paramORB, str1);
/*      */ 
/*  361 */       return localCorbanameUrl.getStringName();
/*      */     } catch (MalformedURLException localMalformedURLException) {
/*  363 */       throw new ConfigurationException(localMalformedURLException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setOrbAndRootContext(ORB paramORB, String paramString) throws NamingException
/*      */   {
/*  369 */     this._orb = paramORB;
/*      */     try
/*      */     {
/*      */       org.omg.CORBA.Object localObject;
/*  372 */       if (paramString != null)
/*      */       {
/*  376 */         localObject = this._orb.string_to_object(paramString);
/*      */       }
/*  378 */       else localObject = this._orb.resolve_initial_references("NameService");
/*      */ 
/*  383 */       this._nc = NamingContextHelper.narrow(localObject);
/*  384 */       if (this._nc == null) {
/*  385 */         if (paramString != null) {
/*  386 */           throw new ConfigurationException("Cannot convert IOR to a NamingContext: " + paramString);
/*      */         }
/*      */ 
/*  389 */         throw new ConfigurationException("ORB.resolve_initial_references(\"NameService\") does not return a NamingContext");
/*      */       }
/*      */     }
/*      */     catch (InvalidName localInvalidName)
/*      */     {
/*  394 */       localObject1 = new ConfigurationException("COS Name Service not registered with ORB under the name 'NameService'");
/*      */ 
/*  397 */       ((NamingException)localObject1).setRootCause(localInvalidName);
/*  398 */       throw ((Throwable)localObject1);
/*      */     } catch (COMM_FAILURE localCOMM_FAILURE) {
/*  400 */       localObject1 = new CommunicationException("Cannot connect to ORB");
/*      */ 
/*  402 */       ((NamingException)localObject1).setRootCause(localCOMM_FAILURE);
/*  403 */       throw ((Throwable)localObject1);
/*      */     } catch (BAD_PARAM localBAD_PARAM) {
/*  405 */       localObject1 = new ConfigurationException("Invalid URL or IOR: " + paramString);
/*      */ 
/*  407 */       ((NamingException)localObject1).setRootCause(localBAD_PARAM);
/*  408 */       throw ((Throwable)localObject1);
/*      */     } catch (INV_OBJREF localINV_OBJREF) {
/*  410 */       java.lang.Object localObject1 = new ConfigurationException("Invalid object reference: " + paramString);
/*      */ 
/*  412 */       ((NamingException)localObject1).setRootCause(localINV_OBJREF);
/*  413 */       throw ((Throwable)localObject1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void setOrbAndRootContext(ORB paramORB, org.omg.CORBA.Object paramObject) throws NamingException
/*      */   {
/*  419 */     this._orb = paramORB;
/*      */     try {
/*  421 */       this._nc = NamingContextHelper.narrow(paramObject);
/*  422 */       if (this._nc == null)
/*  423 */         throw new ConfigurationException("Cannot convert object reference to NamingContext: " + paramObject);
/*      */     }
/*      */     catch (COMM_FAILURE localCOMM_FAILURE)
/*      */     {
/*  427 */       CommunicationException localCommunicationException = new CommunicationException("Cannot connect to ORB");
/*      */ 
/*  429 */       localCommunicationException.setRootCause(localCOMM_FAILURE);
/*  430 */       throw localCommunicationException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private String getStringifiedIor(String paramString) throws NamingException {
/*  435 */     if ((paramString.startsWith("IOR:")) || (paramString.startsWith("corbaloc:"))) {
/*  436 */       return paramString;
/*      */     }
/*  438 */     InputStream localInputStream = null;
/*      */     try {
/*  440 */       URL localURL = new URL(paramString);
/*  441 */       localInputStream = localURL.openStream();
/*  442 */       if (localInputStream != null) {
/*  443 */         localObject1 = new BufferedReader(new InputStreamReader(localInputStream, "8859_1"));
/*      */         String str1;
/*  446 */         while ((str1 = ((BufferedReader)localObject1).readLine()) != null) {
/*  447 */           if (str1.startsWith("IOR:"))
/*      */           {
/*      */             ConfigurationException localConfigurationException1;
/*  448 */             return str1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  459 */         if (localInputStream != null)
/*  460 */           localInputStream.close();
/*      */       }
/*      */       catch (IOException localIOException1) {
/*  463 */         localObject1 = new ConfigurationException("Invalid URL: " + paramString);
/*      */ 
/*  465 */         ((NamingException)localObject1).setRootCause(localIOException1);
/*      */         throw ((Throwable)localObject1);
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException2)
/*      */     {
/*  453 */       java.lang.Object localObject1 = new ConfigurationException("Invalid URL: " + paramString);
/*      */ 
/*  455 */       ((NamingException)localObject1).setRootCause(localIOException2);
/*  456 */       throw ((Throwable)localObject1);
/*      */     } finally {
/*      */       try {
/*  459 */         if (localInputStream != null)
/*  460 */           localInputStream.close();
/*      */       }
/*      */       catch (IOException localIOException4) {
/*  463 */         ConfigurationException localConfigurationException2 = new ConfigurationException("Invalid URL: " + paramString);
/*      */ 
/*  465 */         localConfigurationException2.setRootCause(localIOException4);
/*  466 */         throw localConfigurationException2;
/*      */       }
/*      */     }
/*  469 */     throw new ConfigurationException(paramString + " does not contain an IOR");
/*      */   }
/*      */ 
/*      */   java.lang.Object callResolve(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NamingException
/*      */   {
/*      */     try
/*      */     {
/*  488 */       org.omg.CORBA.Object localObject = this._nc.resolve(paramArrayOfNameComponent);
/*      */       try {
/*  490 */         NamingContext localNamingContext = NamingContextHelper.narrow(localObject);
/*      */ 
/*  492 */         if (localNamingContext != null) {
/*  493 */           return new CNCtx(this._orb, this.orbTracker, localNamingContext, this._env, makeFullName(paramArrayOfNameComponent));
/*      */         }
/*      */ 
/*  496 */         return localObject;
/*      */       }
/*      */       catch (SystemException localSystemException) {
/*  499 */         return localObject;
/*      */       }
/*      */     } catch (Exception localException) {
/*  502 */       throw ExceptionMapper.mapException(localException, this, paramArrayOfNameComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public java.lang.Object lookup(String paramString)
/*      */     throws NamingException
/*      */   {
/*  519 */     return lookup(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public java.lang.Object lookup(Name paramName)
/*      */     throws NamingException
/*      */   {
/*  533 */     if (this._nc == null) {
/*  534 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     }
/*  536 */     if (paramName.size() == 0)
/*  537 */       return this;
/*  538 */     NameComponent[] arrayOfNameComponent = CNNameParser.nameToCosName(paramName);
/*      */     try
/*      */     {
/*  541 */       java.lang.Object localObject = callResolve(arrayOfNameComponent);
/*      */       try
/*      */       {
/*  544 */         return NamingManager.getObjectInstance(localObject, paramName, this, this._env);
/*      */       } catch (NamingException localNamingException1) {
/*  546 */         throw localNamingException1;
/*      */       } catch (Exception localException) {
/*  548 */         NamingException localNamingException2 = new NamingException("problem generating object using object factory");
/*      */ 
/*  550 */         localNamingException2.setRootCause(localException);
/*  551 */         throw localNamingException2;
/*      */       }
/*      */     } catch (CannotProceedException localCannotProceedException) {
/*  554 */       Context localContext = getContinuationContext(localCannotProceedException);
/*  555 */       return localContext.lookup(localCannotProceedException.getRemainingName());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void callBindOrRebind(NameComponent[] paramArrayOfNameComponent, Name paramName, java.lang.Object paramObject, boolean paramBoolean)
/*      */     throws NamingException
/*      */   {
/*  577 */     if (this._nc == null) {
/*  578 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     }
/*      */     try
/*      */     {
/*  582 */       paramObject = NamingManager.getStateToBind(paramObject, paramName, this, this._env);
/*      */ 
/*  584 */       if ((paramObject instanceof CNCtx))
/*      */       {
/*  586 */         paramObject = ((CNCtx)paramObject)._nc;
/*      */       }
/*      */ 
/*  589 */       if ((paramObject instanceof NamingContext)) {
/*  590 */         NamingContext localNamingContext = NamingContextHelper.narrow((org.omg.CORBA.Object)paramObject);
/*      */ 
/*  592 */         if (paramBoolean)
/*  593 */           this._nc.rebind_context(paramArrayOfNameComponent, localNamingContext);
/*      */         else
/*  595 */           this._nc.bind_context(paramArrayOfNameComponent, localNamingContext);
/*      */       }
/*  597 */       else if ((paramObject instanceof org.omg.CORBA.Object)) {
/*  598 */         if (paramBoolean)
/*  599 */           this._nc.rebind(paramArrayOfNameComponent, (org.omg.CORBA.Object)paramObject);
/*      */         else
/*  601 */           this._nc.bind(paramArrayOfNameComponent, (org.omg.CORBA.Object)paramObject);
/*      */       }
/*      */       else {
/*  604 */         throw new IllegalArgumentException("Only instances of org.omg.CORBA.Object can be bound");
/*      */       }
/*      */     }
/*      */     catch (BAD_PARAM localBAD_PARAM) {
/*  608 */       NotContextException localNotContextException = new NotContextException(paramName.toString());
/*  609 */       localNotContextException.setRootCause(localBAD_PARAM);
/*  610 */       throw localNotContextException;
/*      */     } catch (Exception localException) {
/*  612 */       throw ExceptionMapper.mapException(localException, this, paramArrayOfNameComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void bind(Name paramName, java.lang.Object paramObject)
/*      */     throws NamingException
/*      */   {
/*  627 */     if (paramName.size() == 0) {
/*  628 */       throw new InvalidNameException("Name is empty");
/*      */     }
/*      */ 
/*  634 */     NameComponent[] arrayOfNameComponent = CNNameParser.nameToCosName(paramName);
/*      */     try
/*      */     {
/*  637 */       callBindOrRebind(arrayOfNameComponent, paramName, paramObject, false);
/*      */     } catch (CannotProceedException localCannotProceedException) {
/*  639 */       Context localContext = getContinuationContext(localCannotProceedException);
/*  640 */       localContext.bind(localCannotProceedException.getRemainingName(), paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Context getContinuationContext(CannotProceedException paramCannotProceedException) throws NamingException
/*      */   {
/*      */     try
/*      */     {
/*  648 */       return NamingManager.getContinuationContext(paramCannotProceedException);
/*      */     } catch (CannotProceedException localCannotProceedException) {
/*  650 */       java.lang.Object localObject = localCannotProceedException.getResolvedObj();
/*  651 */       if ((localObject instanceof Reference)) {
/*  652 */         Reference localReference = (Reference)localObject;
/*  653 */         RefAddr localRefAddr = localReference.get("nns");
/*  654 */         if ((localRefAddr.getContent() instanceof Context)) {
/*  655 */           NameNotFoundException localNameNotFoundException = new NameNotFoundException("No object reference bound for specified name");
/*      */ 
/*  657 */           localNameNotFoundException.setRootCause(paramCannotProceedException.getRootCause());
/*  658 */           localNameNotFoundException.setRemainingName(paramCannotProceedException.getRemainingName());
/*  659 */           throw localNameNotFoundException;
/*      */         }
/*      */       }
/*  662 */       throw localCannotProceedException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void bind(String paramString, java.lang.Object paramObject)
/*      */     throws NamingException
/*      */   {
/*  675 */     bind(new CompositeName(paramString), paramObject);
/*      */   }
/*      */ 
/*      */   public void rebind(Name paramName, java.lang.Object paramObject)
/*      */     throws NamingException
/*      */   {
/*  690 */     if (paramName.size() == 0) {
/*  691 */       throw new InvalidNameException("Name is empty");
/*      */     }
/*  693 */     NameComponent[] arrayOfNameComponent = CNNameParser.nameToCosName(paramName);
/*      */     try {
/*  695 */       callBindOrRebind(arrayOfNameComponent, paramName, paramObject, true);
/*      */     } catch (CannotProceedException localCannotProceedException) {
/*  697 */       Context localContext = getContinuationContext(localCannotProceedException);
/*  698 */       localContext.rebind(localCannotProceedException.getRemainingName(), paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rebind(String paramString, java.lang.Object paramObject)
/*      */     throws NamingException
/*      */   {
/*  712 */     rebind(new CompositeName(paramString), paramObject);
/*      */   }
/*      */ 
/*      */   private void callUnbind(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NamingException
/*      */   {
/*  725 */     if (this._nc == null)
/*  726 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     try
/*      */     {
/*  729 */       this._nc.unbind(paramArrayOfNameComponent);
/*      */     }
/*      */     catch (NotFound localNotFound)
/*      */     {
/*  734 */       if (!leafNotFound(localNotFound, paramArrayOfNameComponent[(paramArrayOfNameComponent.length - 1)]))
/*      */       {
/*  737 */         throw ExceptionMapper.mapException(localNotFound, this, paramArrayOfNameComponent);
/*      */       }
/*      */     } catch (Exception localException) {
/*  740 */       throw ExceptionMapper.mapException(localException, this, paramArrayOfNameComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean leafNotFound(NotFound paramNotFound, NameComponent paramNameComponent)
/*      */   {
/*      */     NameComponent localNameComponent;
/*  752 */     return (paramNotFound.why.value() == 0) && (paramNotFound.rest_of_name.length == 1) && ((localNameComponent = paramNotFound.rest_of_name[0]).id.equals(paramNameComponent.id)) && ((localNameComponent.kind == paramNameComponent.kind) || ((localNameComponent.kind != null) && (localNameComponent.kind.equals(paramNameComponent.kind))));
/*      */   }
/*      */ 
/*      */   public void unbind(String paramString)
/*      */     throws NamingException
/*      */   {
/*  769 */     unbind(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public void unbind(Name paramName)
/*      */     throws NamingException
/*      */   {
/*  781 */     if (paramName.size() == 0)
/*  782 */       throw new InvalidNameException("Name is empty");
/*  783 */     NameComponent[] arrayOfNameComponent = CNNameParser.nameToCosName(paramName);
/*      */     try {
/*  785 */       callUnbind(arrayOfNameComponent);
/*      */     } catch (CannotProceedException localCannotProceedException) {
/*  787 */       Context localContext = getContinuationContext(localCannotProceedException);
/*  788 */       localContext.unbind(localCannotProceedException.getRemainingName());
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rename(String paramString1, String paramString2)
/*      */     throws NamingException
/*      */   {
/*  802 */     rename(new CompositeName(paramString1), new CompositeName(paramString2));
/*      */   }
/*      */ 
/*      */   public void rename(Name paramName1, Name paramName2)
/*      */     throws NamingException
/*      */   {
/*  815 */     if (this._nc == null) {
/*  816 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     }
/*  818 */     if ((paramName1.size() == 0) || (paramName2.size() == 0))
/*  819 */       throw new InvalidNameException("One or both names empty");
/*  820 */     java.lang.Object localObject = lookup(paramName1);
/*  821 */     bind(paramName2, localObject);
/*  822 */     unbind(paramName1);
/*      */   }
/*      */ 
/*      */   public NamingEnumeration list(String paramString)
/*      */     throws NamingException
/*      */   {
/*  834 */     return list(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public NamingEnumeration list(Name paramName)
/*      */     throws NamingException
/*      */   {
/*  846 */     return listBindings(paramName);
/*      */   }
/*      */ 
/*      */   public NamingEnumeration listBindings(String paramString)
/*      */     throws NamingException
/*      */   {
/*  858 */     return listBindings(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public NamingEnumeration listBindings(Name paramName)
/*      */     throws NamingException
/*      */   {
/*  870 */     if (this._nc == null) {
/*  871 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     }
/*  873 */     if (paramName.size() > 0) {
/*      */       try {
/*  875 */         java.lang.Object localObject = lookup(paramName);
/*  876 */         if ((localObject instanceof CNCtx)) {
/*  877 */           return new CNBindingEnumeration((CNCtx)localObject, true, this._env);
/*      */         }
/*      */ 
/*  880 */         throw new NotContextException(paramName.toString());
/*      */       }
/*      */       catch (NamingException localNamingException) {
/*  883 */         throw localNamingException;
/*      */       } catch (BAD_PARAM localBAD_PARAM) {
/*  885 */         NotContextException localNotContextException = new NotContextException(paramName.toString());
/*      */ 
/*  887 */         localNotContextException.setRootCause(localBAD_PARAM);
/*  888 */         throw localNotContextException;
/*      */       }
/*      */     }
/*  891 */     return new CNBindingEnumeration(this, false, this._env);
/*      */   }
/*      */ 
/*      */   private void callDestroy(NamingContext paramNamingContext)
/*      */     throws NamingException
/*      */   {
/*  901 */     if (this._nc == null)
/*  902 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     try
/*      */     {
/*  905 */       paramNamingContext.destroy();
/*      */     } catch (Exception localException) {
/*  907 */       throw ExceptionMapper.mapException(localException, this, null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void destroySubcontext(String paramString)
/*      */     throws NamingException
/*      */   {
/*  919 */     destroySubcontext(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public void destroySubcontext(Name paramName)
/*      */     throws NamingException
/*      */   {
/*  931 */     if (this._nc == null) {
/*  932 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     }
/*  934 */     NamingContext localNamingContext = this._nc;
/*  935 */     NameComponent[] arrayOfNameComponent = CNNameParser.nameToCosName(paramName);
/*  936 */     if (paramName.size() > 0) {
/*      */       try {
/*  938 */         Context localContext = (Context)callResolve(arrayOfNameComponent);
/*      */ 
/*  940 */         localObject = (CNCtx)localContext;
/*  941 */         localNamingContext = ((CNCtx)localObject)._nc;
/*  942 */         ((CNCtx)localObject).close();
/*      */       } catch (ClassCastException localClassCastException) {
/*  944 */         throw new NotContextException(paramName.toString());
/*      */       } catch (CannotProceedException localCannotProceedException) {
/*  946 */         java.lang.Object localObject = getContinuationContext(localCannotProceedException);
/*  947 */         ((Context)localObject).destroySubcontext(localCannotProceedException.getRemainingName());
/*  948 */         return;
/*      */       }
/*      */       catch (NameNotFoundException localNameNotFoundException)
/*      */       {
/*  953 */         if (((localNameNotFoundException.getRootCause() instanceof NotFound)) && (leafNotFound((NotFound)localNameNotFoundException.getRootCause(), arrayOfNameComponent[(arrayOfNameComponent.length - 1)])))
/*      */         {
/*  956 */           return;
/*      */         }
/*  958 */         throw localNameNotFoundException;
/*      */       } catch (NamingException localNamingException) {
/*  960 */         throw localNamingException;
/*      */       }
/*      */     }
/*  963 */     callDestroy(localNamingContext);
/*  964 */     callUnbind(arrayOfNameComponent);
/*      */   }
/*      */ 
/*      */   private Context callBindNewContext(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NamingException
/*      */   {
/*  978 */     if (this._nc == null)
/*  979 */       throw new ConfigurationException("Context does not have a corresponding NamingContext");
/*      */     try
/*      */     {
/*  982 */       NamingContext localNamingContext = this._nc.bind_new_context(paramArrayOfNameComponent);
/*  983 */       return new CNCtx(this._orb, this.orbTracker, localNamingContext, this._env, makeFullName(paramArrayOfNameComponent));
/*      */     }
/*      */     catch (Exception localException) {
/*  986 */       throw ExceptionMapper.mapException(localException, this, paramArrayOfNameComponent);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Context createSubcontext(String paramString)
/*      */     throws NamingException
/*      */   {
/*  999 */     return createSubcontext(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public Context createSubcontext(Name paramName)
/*      */     throws NamingException
/*      */   {
/* 1011 */     if (paramName.size() == 0)
/* 1012 */       throw new InvalidNameException("Name is empty");
/* 1013 */     NameComponent[] arrayOfNameComponent = CNNameParser.nameToCosName(paramName);
/*      */     try {
/* 1015 */       return callBindNewContext(arrayOfNameComponent);
/*      */     } catch (CannotProceedException localCannotProceedException) {
/* 1017 */       Context localContext = getContinuationContext(localCannotProceedException);
/* 1018 */       return localContext.createSubcontext(localCannotProceedException.getRemainingName());
/*      */     }
/*      */   }
/*      */ 
/*      */   public java.lang.Object lookupLink(String paramString)
/*      */     throws NamingException
/*      */   {
/* 1029 */     return lookupLink(new CompositeName(paramString));
/*      */   }
/*      */ 
/*      */   public java.lang.Object lookupLink(Name paramName)
/*      */     throws NamingException
/*      */   {
/* 1039 */     return lookup(paramName);
/*      */   }
/*      */ 
/*      */   public NameParser getNameParser(String paramString)
/*      */     throws NamingException
/*      */   {
/* 1050 */     return parser;
/*      */   }
/*      */ 
/*      */   public NameParser getNameParser(Name paramName)
/*      */     throws NamingException
/*      */   {
/* 1061 */     return parser;
/*      */   }
/*      */ 
/*      */   public Hashtable getEnvironment()
/*      */     throws NamingException
/*      */   {
/* 1069 */     if (this._env == null) {
/* 1070 */       return new Hashtable(5, 0.75F);
/*      */     }
/* 1072 */     return (Hashtable)this._env.clone();
/*      */   }
/*      */ 
/*      */   public String composeName(String paramString1, String paramString2) throws NamingException
/*      */   {
/* 1077 */     return composeName(new CompositeName(paramString1), new CompositeName(paramString2)).toString();
/*      */   }
/*      */ 
/*      */   public Name composeName(Name paramName1, Name paramName2) throws NamingException
/*      */   {
/* 1082 */     Name localName = (Name)paramName2.clone();
/* 1083 */     return localName.addAll(paramName1);
/*      */   }
/*      */ 
/*      */   public java.lang.Object addToEnvironment(String paramString, java.lang.Object paramObject)
/*      */     throws NamingException
/*      */   {
/* 1097 */     if (this._env == null) {
/* 1098 */       this._env = new Hashtable(7, 0.75F);
/*      */     }
/*      */     else {
/* 1101 */       this._env = ((Hashtable)this._env.clone());
/*      */     }
/*      */ 
/* 1104 */     return this._env.put(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   public java.lang.Object removeFromEnvironment(String paramString)
/*      */     throws NamingException
/*      */   {
/* 1110 */     if ((this._env != null) && (this._env.get(paramString) != null))
/*      */     {
/* 1112 */       this._env = ((Hashtable)this._env.clone());
/* 1113 */       return this._env.remove(paramString);
/*      */     }
/* 1115 */     return null;
/*      */   }
/*      */ 
/*      */   public synchronized void incEnumCount() {
/* 1119 */     this.enumCount += 1;
/*      */   }
/*      */ 
/*      */   public synchronized void decEnumCount()
/*      */     throws NamingException
/*      */   {
/* 1127 */     this.enumCount -= 1;
/*      */ 
/* 1132 */     if ((this.enumCount == 0) && (this.isCloseCalled))
/* 1133 */       close();
/*      */   }
/*      */ 
/*      */   public synchronized void close()
/*      */     throws NamingException
/*      */   {
/* 1139 */     if (this.enumCount > 0) {
/* 1140 */       this.isCloseCalled = true;
/* 1141 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*      */     try
/*      */     {
/* 1151 */       close();
/*      */     }
/*      */     catch (NamingException localNamingException)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.CNCtx
 * JD-Core Version:    0.6.2
 */
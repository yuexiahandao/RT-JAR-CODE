/*      */ package com.sun.corba.se.impl.naming.pcosnaming;
/*      */ 
/*      */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*      */ import com.sun.corba.se.impl.naming.cosnaming.InterOperableNamingImpl;
/*      */ import com.sun.corba.se.impl.naming.cosnaming.NamingContextDataStore;
/*      */ import com.sun.corba.se.impl.naming.cosnaming.NamingUtils;
/*      */ import com.sun.corba.se.impl.naming.namingutil.INSURLHandler;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import org.omg.CORBA.BAD_PARAM;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.Policy;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import org.omg.CosNaming.BindingIterator;
/*      */ import org.omg.CosNaming.BindingIteratorHelper;
/*      */ import org.omg.CosNaming.BindingIteratorHolder;
/*      */ import org.omg.CosNaming.BindingListHolder;
/*      */ import org.omg.CosNaming.BindingType;
/*      */ import org.omg.CosNaming.BindingTypeHolder;
/*      */ import org.omg.CosNaming.NameComponent;
/*      */ import org.omg.CosNaming.NamingContext;
/*      */ import org.omg.CosNaming.NamingContextExtPOA;
/*      */ import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
/*      */ import org.omg.CosNaming.NamingContextHelper;
/*      */ import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
/*      */ import org.omg.CosNaming.NamingContextPackage.CannotProceed;
/*      */ import org.omg.CosNaming.NamingContextPackage.InvalidName;
/*      */ import org.omg.CosNaming.NamingContextPackage.NotEmpty;
/*      */ import org.omg.CosNaming.NamingContextPackage.NotFound;
/*      */ import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
/*      */ import org.omg.PortableServer.IdAssignmentPolicyValue;
/*      */ import org.omg.PortableServer.LifespanPolicyValue;
/*      */ import org.omg.PortableServer.POA;
/*      */ import org.omg.PortableServer.POAManager;
/*      */ import org.omg.PortableServer.ServantRetentionPolicyValue;
/*      */ 
/*      */ public class NamingContextImpl extends NamingContextExtPOA
/*      */   implements NamingContextDataStore, Serializable
/*      */ {
/*      */   private transient ORB orb;
/*      */   private final String objKey;
/*   93 */   private final Hashtable theHashtable = new Hashtable();
/*      */   private transient NameService theNameServiceHandle;
/*      */   private transient ServantManagerImpl theServantManagerImplHandle;
/*      */   private transient InterOperableNamingImpl insImpl;
/*      */   private transient NamingSystemException readWrapper;
/*      */   private transient NamingSystemException updateWrapper;
/*  112 */   private static POA biPOA = null;
/*      */   private static boolean debug;
/*      */ 
/*      */   public NamingContextImpl(ORB paramORB, String paramString, NameService paramNameService, ServantManagerImpl paramServantManagerImpl)
/*      */     throws Exception
/*      */   {
/*  130 */     this.orb = paramORB;
/*  131 */     this.readWrapper = NamingSystemException.get(paramORB, "naming.read");
/*      */ 
/*  133 */     this.updateWrapper = NamingSystemException.get(paramORB, "naming.update");
/*      */ 
/*  136 */     debug = true;
/*  137 */     this.objKey = paramString;
/*  138 */     this.theNameServiceHandle = paramNameService;
/*  139 */     this.theServantManagerImplHandle = paramServantManagerImpl;
/*  140 */     this.insImpl = new InterOperableNamingImpl();
/*      */   }
/*      */ 
/*      */   InterOperableNamingImpl getINSImpl()
/*      */   {
/*  146 */     if (this.insImpl == null)
/*      */     {
/*  150 */       this.insImpl = new InterOperableNamingImpl();
/*      */     }
/*      */ 
/*  153 */     return this.insImpl;
/*      */   }
/*      */ 
/*      */   public void setRootNameService(NameService paramNameService)
/*      */   {
/*  158 */     this.theNameServiceHandle = paramNameService;
/*      */   }
/*      */ 
/*      */   public void setORB(ORB paramORB) {
/*  162 */     this.orb = paramORB;
/*      */   }
/*      */ 
/*      */   public void setServantManagerImpl(ServantManagerImpl paramServantManagerImpl)
/*      */   {
/*  168 */     this.theServantManagerImplHandle = paramServantManagerImpl;
/*      */   }
/*      */ 
/*      */   public POA getNSPOA() {
/*  172 */     return this.theNameServiceHandle.getNSPOA();
/*      */   }
/*      */ 
/*      */   public void bind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*      */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*      */   {
/*  207 */     if (paramObject == null) {
/*  208 */       throw this.updateWrapper.objectIsNull();
/*      */     }
/*      */ 
/*  211 */     if (debug) {
/*  212 */       dprint("bind " + nameToString(paramArrayOfNameComponent) + " to " + paramObject);
/*      */     }
/*  214 */     NamingContextImpl localNamingContextImpl = this;
/*  215 */     doBind(localNamingContextImpl, paramArrayOfNameComponent, paramObject, false, BindingType.nobject);
/*      */   }
/*      */ 
/*      */   public void bind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*      */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*      */   {
/*  245 */     if (paramNamingContext == null) {
/*  246 */       throw this.updateWrapper.objectIsNull();
/*      */     }
/*      */ 
/*  249 */     NamingContextImpl localNamingContextImpl = this;
/*  250 */     doBind(localNamingContextImpl, paramArrayOfNameComponent, paramNamingContext, false, BindingType.ncontext);
/*      */   }
/*      */ 
/*      */   public void rebind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  279 */     if (paramObject == null)
/*      */     {
/*  281 */       throw this.updateWrapper.objectIsNull();
/*      */     }
/*      */     try {
/*  284 */       if (debug) {
/*  285 */         dprint("rebind " + nameToString(paramArrayOfNameComponent) + " to " + paramObject);
/*      */       }
/*  287 */       NamingContextImpl localNamingContextImpl = this;
/*  288 */       doBind(localNamingContextImpl, paramArrayOfNameComponent, paramObject, true, BindingType.nobject);
/*      */     }
/*      */     catch (AlreadyBound localAlreadyBound) {
/*  291 */       throw this.updateWrapper.namingCtxRebindAlreadyBound(localAlreadyBound);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rebind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*      */     try
/*      */     {
/*  321 */       if (debug) {
/*  322 */         dprint("rebind_context " + nameToString(paramArrayOfNameComponent) + " to " + paramNamingContext);
/*      */       }
/*  324 */       NamingContextImpl localNamingContextImpl = this;
/*  325 */       doBind(localNamingContextImpl, paramArrayOfNameComponent, paramNamingContext, true, BindingType.ncontext);
/*      */     }
/*      */     catch (AlreadyBound localAlreadyBound) {
/*  328 */       throw this.updateWrapper.namingCtxRebindAlreadyBound(localAlreadyBound);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object resolve(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  356 */     if (debug) {
/*  357 */       dprint("resolve " + nameToString(paramArrayOfNameComponent));
/*      */     }
/*  359 */     NamingContextImpl localNamingContextImpl = this;
/*  360 */     return doResolve(localNamingContextImpl, paramArrayOfNameComponent);
/*      */   }
/*      */ 
/*      */   public void unbind(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  384 */     if (debug) {
/*  385 */       dprint("unbind " + nameToString(paramArrayOfNameComponent));
/*      */     }
/*  387 */     NamingContextImpl localNamingContextImpl = this;
/*  388 */     doUnbind(localNamingContextImpl, paramArrayOfNameComponent);
/*      */   }
/*      */ 
/*      */   public void list(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
/*      */   {
/*  405 */     if (debug) {
/*  406 */       dprint("list(" + paramInt + ")");
/*      */     }
/*  408 */     NamingContextImpl localNamingContextImpl = this;
/*  409 */     synchronized (localNamingContextImpl) {
/*  410 */       localNamingContextImpl.List(paramInt, paramBindingListHolder, paramBindingIteratorHolder);
/*      */     }
/*  412 */     if ((debug) && (paramBindingListHolder.value != null))
/*  413 */       dprint("list(" + paramInt + ") -> bindings[" + paramBindingListHolder.value.length + "] + iterator: " + paramBindingIteratorHolder.value);
/*      */   }
/*      */ 
/*      */   public synchronized NamingContext new_context()
/*      */   {
/*  427 */     if (debug)
/*  428 */       dprint("new_context()");
/*  429 */     NamingContextImpl localNamingContextImpl = this;
/*  430 */     synchronized (localNamingContextImpl) {
/*  431 */       return localNamingContextImpl.NewContext();
/*      */     }
/*      */   }
/*      */ 
/*      */   public NamingContext bind_new_context(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, AlreadyBound, CannotProceed, InvalidName
/*      */   {
/*  463 */     NamingContext localNamingContext1 = null;
/*  464 */     NamingContext localNamingContext2 = null;
/*      */     try {
/*  466 */       if (debug) {
/*  467 */         dprint("bind_new_context " + nameToString(paramArrayOfNameComponent));
/*      */       }
/*  469 */       localNamingContext1 = new_context();
/*  470 */       bind_context(paramArrayOfNameComponent, localNamingContext1);
/*  471 */       localNamingContext2 = localNamingContext1;
/*  472 */       localNamingContext1 = null;
/*      */     } finally {
/*      */       try {
/*  475 */         if (localNamingContext1 != null)
/*  476 */           localNamingContext1.destroy();
/*      */       } catch (NotEmpty localNotEmpty2) {
/*      */       }
/*      */     }
/*  480 */     return localNamingContext2;
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */     throws NotEmpty
/*      */   {
/*  493 */     if (debug)
/*  494 */       dprint("destroy ");
/*  495 */     NamingContextImpl localNamingContextImpl = this;
/*  496 */     synchronized (localNamingContextImpl) {
/*  497 */       if (localNamingContextImpl.IsEmpty() == true)
/*      */       {
/*  499 */         localNamingContextImpl.Destroy();
/*      */       }
/*      */       else
/*  502 */         throw new NotEmpty();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doBind(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject, boolean paramBoolean, BindingType paramBindingType)
/*      */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*      */   {
/*  550 */     if (paramArrayOfNameComponent.length < 1)
/*  551 */       throw new InvalidName();
/*      */     java.lang.Object localObject1;
/*      */     java.lang.Object localObject2;
/*  554 */     if (paramArrayOfNameComponent.length == 1)
/*      */     {
/*  556 */       if ((paramArrayOfNameComponent[0].id.length() == 0) && (paramArrayOfNameComponent[0].kind.length() == 0)) {
/*  557 */         throw new InvalidName();
/*      */       }
/*      */ 
/*  560 */       synchronized (paramNamingContextDataStore)
/*      */       {
/*  562 */         localObject1 = new BindingTypeHolder();
/*  563 */         if (paramBoolean) {
/*  564 */           localObject2 = paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], (BindingTypeHolder)localObject1);
/*  565 */           if (localObject2 != null)
/*      */           {
/*  572 */             if (((BindingTypeHolder)localObject1).value.value() == BindingType.nobject.value()) {
/*  573 */               if (paramBindingType.value() == BindingType.ncontext.value()) {
/*  574 */                 throw new NotFound(NotFoundReason.not_context, paramArrayOfNameComponent);
/*      */               }
/*      */ 
/*      */             }
/*  579 */             else if (paramBindingType.value() == BindingType.nobject.value()) {
/*  580 */               throw new NotFound(NotFoundReason.not_object, paramArrayOfNameComponent);
/*      */             }
/*      */ 
/*  583 */             paramNamingContextDataStore.Unbind(paramArrayOfNameComponent[0]);
/*      */           }
/*      */         }
/*  586 */         else if (paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], (BindingTypeHolder)localObject1) != null) {
/*  587 */           throw new AlreadyBound();
/*      */         }
/*      */ 
/*  591 */         paramNamingContextDataStore.Bind(paramArrayOfNameComponent[0], paramObject, paramBindingType);
/*      */       }
/*      */     }
/*      */     else {
/*  595 */       ??? = resolveFirstAsContext(paramNamingContextDataStore, paramArrayOfNameComponent);
/*      */ 
/*  598 */       localObject1 = new NameComponent[paramArrayOfNameComponent.length - 1];
/*  599 */       System.arraycopy(paramArrayOfNameComponent, 1, localObject1, 0, paramArrayOfNameComponent.length - 1);
/*      */ 
/*  602 */       switch (paramBindingType.value())
/*      */       {
/*      */       case 0:
/*  606 */         if (paramBoolean)
/*  607 */           ((NamingContext)???).rebind((NameComponent[])localObject1, paramObject);
/*      */         else {
/*  609 */           ((NamingContext)???).bind((NameComponent[])localObject1, paramObject);
/*      */         }
/*  611 */         break;
/*      */       case 1:
/*  615 */         localObject2 = (NamingContext)paramObject;
/*      */ 
/*  617 */         if (paramBoolean)
/*  618 */           ((NamingContext)???).rebind_context((NameComponent[])localObject1, (NamingContext)localObject2);
/*      */         else {
/*  620 */           ((NamingContext)???).bind_context((NameComponent[])localObject1, (NamingContext)localObject2);
/*      */         }
/*  622 */         break;
/*      */       default:
/*  625 */         throw this.updateWrapper.namingCtxBadBindingtype();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static org.omg.CORBA.Object doResolve(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  660 */     org.omg.CORBA.Object localObject = null;
/*  661 */     BindingTypeHolder localBindingTypeHolder = new BindingTypeHolder();
/*      */ 
/*  664 */     if (paramArrayOfNameComponent.length < 1) {
/*  665 */       throw new InvalidName();
/*      */     }
/*      */ 
/*  668 */     if (paramArrayOfNameComponent.length == 1) {
/*  669 */       synchronized (paramNamingContextDataStore)
/*      */       {
/*  671 */         localObject = paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], localBindingTypeHolder);
/*      */       }
/*  673 */       if (localObject == null)
/*      */       {
/*  675 */         throw new NotFound(NotFoundReason.missing_node, paramArrayOfNameComponent);
/*      */       }
/*  677 */       return localObject;
/*      */     }
/*      */ 
/*  680 */     if ((paramArrayOfNameComponent[1].id.length() == 0) && (paramArrayOfNameComponent[1].kind.length() == 0)) {
/*  681 */       throw new InvalidName();
/*      */     }
/*  683 */     ??? = resolveFirstAsContext(paramNamingContextDataStore, paramArrayOfNameComponent);
/*      */ 
/*  686 */     NameComponent[] arrayOfNameComponent = new NameComponent[paramArrayOfNameComponent.length - 1];
/*  687 */     System.arraycopy(paramArrayOfNameComponent, 1, arrayOfNameComponent, 0, paramArrayOfNameComponent.length - 1);
/*      */ 
/*  690 */     return ((NamingContext)???).resolve(arrayOfNameComponent);
/*      */   }
/*      */ 
/*      */   public static void doUnbind(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  721 */     if (paramArrayOfNameComponent.length < 1) {
/*  722 */       throw new InvalidName();
/*      */     }
/*      */ 
/*  725 */     if (paramArrayOfNameComponent.length == 1)
/*      */     {
/*  727 */       if ((paramArrayOfNameComponent[0].id.length() == 0) && (paramArrayOfNameComponent[0].kind.length() == 0)) {
/*  728 */         throw new InvalidName();
/*      */       }
/*  730 */       localObject1 = null;
/*  731 */       synchronized (paramNamingContextDataStore)
/*      */       {
/*  733 */         localObject1 = paramNamingContextDataStore.Unbind(paramArrayOfNameComponent[0]);
/*      */       }
/*      */ 
/*  736 */       if (localObject1 == null)
/*      */       {
/*  738 */         throw new NotFound(NotFoundReason.missing_node, paramArrayOfNameComponent);
/*      */       }
/*  740 */       return;
/*      */     }
/*      */ 
/*  745 */     java.lang.Object localObject1 = resolveFirstAsContext(paramNamingContextDataStore, paramArrayOfNameComponent);
/*      */ 
/*  748 */     ??? = new NameComponent[paramArrayOfNameComponent.length - 1];
/*  749 */     System.arraycopy(paramArrayOfNameComponent, 1, ???, 0, paramArrayOfNameComponent.length - 1);
/*      */ 
/*  752 */     ((NamingContext)localObject1).unbind((NameComponent[])???);
/*      */   }
/*      */ 
/*      */   protected static NamingContext resolveFirstAsContext(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound
/*      */   {
/*  772 */     org.omg.CORBA.Object localObject = null;
/*  773 */     BindingTypeHolder localBindingTypeHolder = new BindingTypeHolder();
/*  774 */     NamingContext localNamingContext = null;
/*      */ 
/*  776 */     synchronized (paramNamingContextDataStore)
/*      */     {
/*  778 */       localObject = paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], localBindingTypeHolder);
/*  779 */       if (localObject == null)
/*      */       {
/*  781 */         throw new NotFound(NotFoundReason.missing_node, paramArrayOfNameComponent);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  786 */     if (localBindingTypeHolder.value != BindingType.ncontext)
/*      */     {
/*  788 */       throw new NotFound(NotFoundReason.not_context, paramArrayOfNameComponent);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  793 */       localNamingContext = NamingContextHelper.narrow(localObject);
/*      */     }
/*      */     catch (BAD_PARAM localBAD_PARAM) {
/*  796 */       throw new NotFound(NotFoundReason.not_context, paramArrayOfNameComponent);
/*      */     }
/*      */ 
/*  800 */     return localNamingContext;
/*      */   }
/*      */ 
/*      */   public static String nameToString(NameComponent[] paramArrayOfNameComponent)
/*      */   {
/*  805 */     StringBuffer localStringBuffer = new StringBuffer("{");
/*  806 */     if ((paramArrayOfNameComponent != null) || (paramArrayOfNameComponent.length > 0)) {
/*  807 */       for (int i = 0; i < paramArrayOfNameComponent.length; i++) {
/*  808 */         if (i > 0)
/*  809 */           localStringBuffer.append(",");
/*  810 */         localStringBuffer.append("[").append(paramArrayOfNameComponent[i].id).append(",").append(paramArrayOfNameComponent[i].kind).append("]");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  817 */     localStringBuffer.append("}");
/*  818 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static void dprint(String paramString)
/*      */   {
/*  825 */     NamingUtils.dprint("NamingContextImpl(" + Thread.currentThread().getName() + " at " + System.currentTimeMillis() + " ems): " + paramString);
/*      */   }
/*      */ 
/*      */   public void Bind(NameComponent paramNameComponent, org.omg.CORBA.Object paramObject, BindingType paramBindingType)
/*      */   {
/*  857 */     if (paramObject == null)
/*      */     {
/*  859 */       return;
/*      */     }
/*      */ 
/*  862 */     InternalBindingKey localInternalBindingKey = new InternalBindingKey(paramNameComponent);
/*      */     try
/*      */     {
/*      */       InternalBindingValue localInternalBindingValue;
/*  866 */       if (paramBindingType.value() == 0)
/*      */       {
/*  870 */         localInternalBindingValue = new InternalBindingValue(paramBindingType, this.orb.object_to_string(paramObject));
/*  871 */         localInternalBindingValue.setObjectRef(paramObject);
/*      */       }
/*      */       else
/*      */       {
/*  875 */         localObject = this.theNameServiceHandle.getObjectKey(paramObject);
/*  876 */         localInternalBindingValue = new InternalBindingValue(paramBindingType, (String)localObject);
/*  877 */         localInternalBindingValue.setObjectRef(paramObject);
/*      */       }
/*      */ 
/*  880 */       java.lang.Object localObject = (InternalBindingValue)this.theHashtable.put(localInternalBindingKey, localInternalBindingValue);
/*      */ 
/*  883 */       if (localObject != null)
/*      */       {
/*  886 */         throw this.updateWrapper.namingCtxRebindAlreadyBound();
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/*  891 */         this.theServantManagerImplHandle.updateContext(this.objKey, this);
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/*  895 */         throw this.updateWrapper.bindUpdateContextFailed(localException2);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (Exception localException1)
/*      */     {
/*  901 */       throw this.updateWrapper.bindFailure(localException1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object Resolve(NameComponent paramNameComponent, BindingTypeHolder paramBindingTypeHolder)
/*      */     throws SystemException
/*      */   {
/*  924 */     if ((paramNameComponent.id.length() == 0) && (paramNameComponent.kind.length() == 0))
/*      */     {
/*  927 */       paramBindingTypeHolder.value = BindingType.ncontext;
/*  928 */       return this.theNameServiceHandle.getObjectReferenceFromKey(this.objKey);
/*      */     }
/*      */ 
/*  932 */     InternalBindingKey localInternalBindingKey = new InternalBindingKey(paramNameComponent);
/*  933 */     InternalBindingValue localInternalBindingValue = (InternalBindingValue)this.theHashtable.get(localInternalBindingKey);
/*      */ 
/*  936 */     if (localInternalBindingValue == null)
/*      */     {
/*  940 */       return null;
/*      */     }
/*      */ 
/*  943 */     org.omg.CORBA.Object localObject = null;
/*  944 */     paramBindingTypeHolder.value = localInternalBindingValue.theBindingType;
/*      */     try
/*      */     {
/*  951 */       if (localInternalBindingValue.strObjectRef.startsWith("NC")) {
/*  952 */         paramBindingTypeHolder.value = BindingType.ncontext;
/*  953 */         return this.theNameServiceHandle.getObjectReferenceFromKey(localInternalBindingValue.strObjectRef);
/*      */       }
/*      */ 
/*  958 */       localObject = localInternalBindingValue.getObjectRef();
/*      */ 
/*  960 */       if (localObject == null) {
/*      */         try {
/*  962 */           localObject = this.orb.string_to_object(localInternalBindingValue.strObjectRef);
/*      */ 
/*  964 */           localInternalBindingValue.setObjectRef(localObject);
/*      */         } catch (Exception localException1) {
/*  966 */           throw this.readWrapper.resolveConversionFailure(CompletionStatus.COMPLETED_MAYBE, localException1);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException2)
/*      */     {
/*  972 */       throw this.readWrapper.resolveFailure(CompletionStatus.COMPLETED_MAYBE, localException2);
/*      */     }
/*      */ 
/*  976 */     return localObject;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object Unbind(NameComponent paramNameComponent)
/*      */     throws SystemException
/*      */   {
/*      */     try
/*      */     {
/*  998 */       InternalBindingKey localInternalBindingKey = new InternalBindingKey(paramNameComponent);
/*  999 */       InternalBindingValue localInternalBindingValue = null;
/*      */       try
/*      */       {
/* 1002 */         localInternalBindingValue = (InternalBindingValue)this.theHashtable.remove(localInternalBindingKey);
/*      */       }
/*      */       catch (Exception localException2)
/*      */       {
/*      */       }
/* 1007 */       this.theServantManagerImplHandle.updateContext(this.objKey, this);
/*      */ 
/* 1009 */       if (localInternalBindingValue == null) {
/* 1010 */         return null;
/*      */       }
/*      */ 
/* 1013 */       if (localInternalBindingValue.strObjectRef.startsWith("NC")) {
/* 1014 */         this.theServantManagerImplHandle.readInContext(localInternalBindingValue.strObjectRef);
/* 1015 */         return this.theNameServiceHandle.getObjectReferenceFromKey(localInternalBindingValue.strObjectRef);
/*      */       }
/*      */ 
/* 1019 */       org.omg.CORBA.Object localObject = localInternalBindingValue.getObjectRef();
/*      */ 
/* 1021 */       if (localObject == null);
/* 1022 */       return this.orb.string_to_object(localInternalBindingValue.strObjectRef);
/*      */     }
/*      */     catch (Exception localException1)
/*      */     {
/* 1029 */       throw this.updateWrapper.unbindFailure(CompletionStatus.COMPLETED_MAYBE, localException1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void List(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
/*      */     throws SystemException
/*      */   {
/* 1047 */     if (biPOA == null)
/* 1048 */       createbiPOA();
/*      */     try
/*      */     {
/* 1051 */       PersistentBindingIterator localPersistentBindingIterator = new PersistentBindingIterator(this.orb, (Hashtable)this.theHashtable.clone(), biPOA);
/*      */ 
/* 1055 */       localPersistentBindingIterator.list(paramInt, paramBindingListHolder);
/*      */ 
/* 1057 */       byte[] arrayOfByte = biPOA.activate_object(localPersistentBindingIterator);
/* 1058 */       org.omg.CORBA.Object localObject = biPOA.id_to_reference(arrayOfByte);
/*      */ 
/* 1061 */       BindingIterator localBindingIterator = BindingIteratorHelper.narrow(localObject);
/*      */ 
/* 1064 */       paramBindingIteratorHolder.value = localBindingIterator;
/*      */     } catch (SystemException localSystemException) {
/* 1066 */       throw localSystemException;
/*      */     } catch (Exception localException) {
/* 1068 */       throw this.readWrapper.transNcListGotExc(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void createbiPOA() {
/* 1073 */     if (biPOA != null)
/* 1074 */       return;
/*      */     try
/*      */     {
/* 1077 */       POA localPOA = (POA)this.orb.resolve_initial_references("RootPOA");
/*      */ 
/* 1079 */       localPOA.the_POAManager().activate();
/*      */ 
/* 1081 */       int i = 0;
/* 1082 */       Policy[] arrayOfPolicy = new Policy[3];
/* 1083 */       arrayOfPolicy[(i++)] = localPOA.create_lifespan_policy(LifespanPolicyValue.TRANSIENT);
/*      */ 
/* 1085 */       arrayOfPolicy[(i++)] = localPOA.create_id_assignment_policy(IdAssignmentPolicyValue.SYSTEM_ID);
/*      */ 
/* 1087 */       arrayOfPolicy[(i++)] = localPOA.create_servant_retention_policy(ServantRetentionPolicyValue.RETAIN);
/*      */ 
/* 1089 */       biPOA = localPOA.create_POA("BindingIteratorPOA", null, arrayOfPolicy);
/* 1090 */       biPOA.the_POAManager().activate();
/*      */     } catch (Exception localException) {
/* 1092 */       throw this.readWrapper.namingCtxBindingIteratorCreate(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public NamingContext NewContext()
/*      */     throws SystemException
/*      */   {
/*      */     try
/*      */     {
/* 1106 */       return this.theNameServiceHandle.NewContext();
/*      */     } catch (SystemException localSystemException) {
/* 1108 */       throw localSystemException;
/*      */     } catch (Exception localException) {
/* 1110 */       throw this.updateWrapper.transNcNewctxGotExc(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void Destroy()
/*      */     throws SystemException
/*      */   {
/*      */   }
/*      */ 
/*      */   public String to_string(NameComponent[] paramArrayOfNameComponent)
/*      */     throws InvalidName
/*      */   {
/* 1152 */     if ((paramArrayOfNameComponent == null) || (paramArrayOfNameComponent.length == 0))
/*      */     {
/* 1154 */       throw new InvalidName();
/*      */     }
/*      */ 
/* 1157 */     String str = getINSImpl().convertToString(paramArrayOfNameComponent);
/*      */ 
/* 1159 */     if (str == null)
/*      */     {
/* 1161 */       throw new InvalidName();
/*      */     }
/*      */ 
/* 1164 */     return str;
/*      */   }
/*      */ 
/*      */   public NameComponent[] to_name(String paramString)
/*      */     throws InvalidName
/*      */   {
/* 1179 */     if ((paramString == null) || (paramString.length() == 0))
/*      */     {
/* 1181 */       throw new InvalidName();
/*      */     }
/* 1183 */     NameComponent[] arrayOfNameComponent = getINSImpl().convertToNameComponent(paramString);
/*      */ 
/* 1185 */     if ((arrayOfNameComponent == null) || (arrayOfNameComponent.length == 0))
/*      */     {
/* 1187 */       throw new InvalidName();
/*      */     }
/* 1189 */     for (int i = 0; i < arrayOfNameComponent.length; i++)
/*      */     {
/* 1193 */       if (((arrayOfNameComponent[i].id == null) || (arrayOfNameComponent[i].id.length() == 0)) && ((arrayOfNameComponent[i].kind == null) || (arrayOfNameComponent[i].kind.length() == 0)))
/*      */       {
/* 1197 */         throw new InvalidName();
/*      */       }
/*      */     }
/* 1200 */     return arrayOfNameComponent;
/*      */   }
/*      */ 
/*      */   public String to_url(String paramString1, String paramString2)
/*      */     throws InvalidAddress, InvalidName
/*      */   {
/* 1222 */     if ((paramString2 == null) || (paramString2.length() == 0))
/*      */     {
/* 1224 */       throw new InvalidName();
/*      */     }
/* 1226 */     if (paramString1 == null)
/*      */     {
/* 1228 */       throw new InvalidAddress();
/*      */     }
/* 1230 */     String str = null;
/*      */     try {
/* 1232 */       str = getINSImpl().createURLBasedAddress(paramString1, paramString2);
/*      */     } catch (Exception localException) {
/* 1234 */       str = null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1239 */       INSURLHandler.getINSURLHandler().parseURL(str);
/*      */     } catch (BAD_PARAM localBAD_PARAM) {
/* 1241 */       throw new InvalidAddress();
/*      */     }
/*      */ 
/* 1244 */     return str;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object resolve_str(String paramString)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/* 1264 */     org.omg.CORBA.Object localObject = null;
/*      */ 
/* 1266 */     if ((paramString == null) || (paramString.length() == 0))
/*      */     {
/* 1268 */       throw new InvalidName();
/*      */     }
/* 1270 */     NameComponent[] arrayOfNameComponent = getINSImpl().convertToNameComponent(paramString);
/*      */ 
/* 1272 */     if ((arrayOfNameComponent == null) || (arrayOfNameComponent.length == 0))
/*      */     {
/* 1274 */       throw new InvalidName();
/*      */     }
/* 1276 */     localObject = resolve(arrayOfNameComponent);
/* 1277 */     return localObject;
/*      */   }
/*      */ 
/*      */   public boolean IsEmpty()
/*      */   {
/* 1285 */     return this.theHashtable.isEmpty();
/*      */   }
/*      */ 
/*      */   public void printSize()
/*      */   {
/* 1293 */     System.out.println("Hashtable Size = " + this.theHashtable.size());
/* 1294 */     Enumeration localEnumeration = this.theHashtable.keys();
/* 1295 */     while (localEnumeration.hasMoreElements())
/*      */     {
/* 1297 */       InternalBindingValue localInternalBindingValue = (InternalBindingValue)this.theHashtable.get(localEnumeration.nextElement());
/*      */ 
/* 1299 */       if (localInternalBindingValue != null)
/*      */       {
/* 1301 */         System.out.println("value = " + localInternalBindingValue.strObjectRef);
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.pcosnaming.NamingContextImpl
 * JD-Core Version:    0.6.2
 */
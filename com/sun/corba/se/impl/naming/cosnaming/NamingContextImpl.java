/*      */ package com.sun.corba.se.impl.naming.cosnaming;
/*      */ 
/*      */ import com.sun.corba.se.impl.logging.NamingSystemException;
/*      */ import com.sun.corba.se.impl.naming.namingutil.INSURLHandler;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import org.omg.CORBA.BAD_PARAM;
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
/*      */ import org.omg.PortableServer.POA;
/*      */ import org.omg.PortableServer.Servant;
/*      */ 
/*      */ public abstract class NamingContextImpl extends NamingContextExtPOA
/*      */   implements NamingContextDataStore
/*      */ {
/*      */   protected POA nsPOA;
/*      */   private Logger readLogger;
/*      */   private Logger updateLogger;
/*      */   private Logger lifecycleLogger;
/*      */   private NamingSystemException wrapper;
/*   98 */   private static NamingSystemException staticWrapper = NamingSystemException.get("naming.update");
/*      */   private InterOperableNamingImpl insImpl;
/*      */   protected transient ORB orb;
/*      */   public static final boolean debug = false;
/*      */ 
/*      */   public NamingContextImpl(ORB paramORB, POA paramPOA)
/*      */     throws Exception
/*      */   {
/*  112 */     this.orb = paramORB;
/*  113 */     this.wrapper = NamingSystemException.get(paramORB, "naming.update");
/*      */ 
/*  116 */     this.insImpl = new InterOperableNamingImpl();
/*  117 */     this.nsPOA = paramPOA;
/*  118 */     this.readLogger = paramORB.getLogger("naming.read");
/*  119 */     this.updateLogger = paramORB.getLogger("naming.update");
/*  120 */     this.lifecycleLogger = paramORB.getLogger("naming.lifecycle");
/*      */   }
/*      */ 
/*      */   public POA getNSPOA()
/*      */   {
/*  125 */     return this.nsPOA;
/*      */   }
/*      */ 
/*      */   public void bind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*      */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*      */   {
/*  158 */     if (paramObject == null)
/*      */     {
/*  160 */       this.updateLogger.warning("<<NAMING BIND>> unsuccessful because NULL Object cannot be Bound ");
/*      */ 
/*  162 */       throw this.wrapper.objectIsNull();
/*      */     }
/*      */ 
/*  165 */     NamingContextImpl localNamingContextImpl = this;
/*  166 */     doBind(localNamingContextImpl, paramArrayOfNameComponent, paramObject, false, BindingType.nobject);
/*  167 */     if (this.updateLogger.isLoggable(Level.FINE))
/*      */     {
/*  170 */       this.updateLogger.fine("<<NAMING BIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void bind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*      */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*      */   {
/*  204 */     if (paramNamingContext == null) {
/*  205 */       this.updateLogger.warning("<<NAMING BIND>><<FAILURE>> NULL Context cannot be Bound ");
/*      */ 
/*  207 */       throw new BAD_PARAM("Naming Context should not be null ");
/*      */     }
/*      */ 
/*  210 */     NamingContextImpl localNamingContextImpl = this;
/*  211 */     doBind(localNamingContextImpl, paramArrayOfNameComponent, paramNamingContext, false, BindingType.ncontext);
/*  212 */     if (this.updateLogger.isLoggable(Level.FINE))
/*      */     {
/*  215 */       this.updateLogger.fine("<<NAMING BIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rebind(NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  247 */     if (paramObject == null)
/*      */     {
/*  249 */       this.updateLogger.warning("<<NAMING REBIND>><<FAILURE>> NULL Object cannot be Bound ");
/*      */ 
/*  251 */       throw this.wrapper.objectIsNull();
/*      */     }
/*      */     try
/*      */     {
/*  255 */       NamingContextImpl localNamingContextImpl = this;
/*  256 */       doBind(localNamingContextImpl, paramArrayOfNameComponent, paramObject, true, BindingType.nobject);
/*      */     } catch (AlreadyBound localAlreadyBound) {
/*  258 */       this.updateLogger.warning("<<NAMING REBIND>><<FAILURE>>" + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent) + " is already bound to a Naming Context");
/*      */ 
/*  262 */       throw this.wrapper.namingCtxRebindAlreadyBound(localAlreadyBound);
/*      */     }
/*  264 */     if (this.updateLogger.isLoggable(Level.FINE))
/*      */     {
/*  267 */       this.updateLogger.fine("<<NAMING REBIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void rebind_context(NameComponent[] paramArrayOfNameComponent, NamingContext paramNamingContext)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  298 */     if (paramNamingContext == null)
/*      */     {
/*  300 */       this.updateLogger.warning("<<NAMING REBIND>><<FAILURE>> NULL Context cannot be Bound ");
/*      */ 
/*  302 */       throw this.wrapper.objectIsNull();
/*      */     }
/*      */     try
/*      */     {
/*  306 */       NamingContextImpl localNamingContextImpl = this;
/*  307 */       doBind(localNamingContextImpl, paramArrayOfNameComponent, paramNamingContext, true, BindingType.ncontext);
/*      */     }
/*      */     catch (AlreadyBound localAlreadyBound) {
/*  310 */       this.updateLogger.warning("<<NAMING REBIND>><<FAILURE>>" + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent) + " is already bound to a CORBA Object");
/*      */ 
/*  313 */       throw this.wrapper.namingCtxRebindctxAlreadyBound(localAlreadyBound);
/*      */     }
/*  315 */     if (this.updateLogger.isLoggable(Level.FINE))
/*      */     {
/*  318 */       this.updateLogger.fine("<<NAMING REBIND>><<SUCCESS>> Name = " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object resolve(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  349 */     NamingContextImpl localNamingContextImpl = this;
/*  350 */     org.omg.CORBA.Object localObject = doResolve(localNamingContextImpl, paramArrayOfNameComponent);
/*  351 */     if (localObject != null) {
/*  352 */       if (this.readLogger.isLoggable(Level.FINE)) {
/*  353 */         this.readLogger.fine("<<NAMING RESOLVE>><<SUCCESS>> Name: " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */       }
/*      */     }
/*      */     else {
/*  357 */       this.readLogger.warning("<<NAMING RESOLVE>><<FAILURE>> Name: " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */ 
/*  360 */     return localObject;
/*      */   }
/*      */ 
/*      */   public void unbind(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  387 */     NamingContextImpl localNamingContextImpl = this;
/*  388 */     doUnbind(localNamingContextImpl, paramArrayOfNameComponent);
/*  389 */     if (this.updateLogger.isLoggable(Level.FINE))
/*      */     {
/*  392 */       this.updateLogger.fine("<<NAMING UNBIND>><<SUCCESS>> Name: " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void list(int paramInt, BindingListHolder paramBindingListHolder, BindingIteratorHolder paramBindingIteratorHolder)
/*      */   {
/*  414 */     NamingContextImpl localNamingContextImpl = this;
/*  415 */     synchronized (localNamingContextImpl) {
/*  416 */       localNamingContextImpl.List(paramInt, paramBindingListHolder, paramBindingIteratorHolder);
/*      */     }
/*  418 */     if ((this.readLogger.isLoggable(Level.FINE)) && (paramBindingListHolder.value != null))
/*      */     {
/*  421 */       this.readLogger.fine("<<NAMING LIST>><<SUCCESS>>list(" + paramInt + ") -> bindings[" + paramBindingListHolder.value.length + "] + iterator: " + paramBindingIteratorHolder.value);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized NamingContext new_context()
/*      */   {
/*  437 */     this.lifecycleLogger.fine("Creating New Naming Context ");
/*  438 */     NamingContextImpl localNamingContextImpl = this;
/*  439 */     synchronized (localNamingContextImpl) {
/*  440 */       NamingContext localNamingContext = localNamingContextImpl.NewContext();
/*  441 */       if (localNamingContext != null) {
/*  442 */         this.lifecycleLogger.fine("<<LIFECYCLE CREATE>><<SUCCESS>>");
/*      */       }
/*      */       else
/*      */       {
/*  446 */         this.lifecycleLogger.severe("<<LIFECYCLE CREATE>><<FAILURE>>");
/*      */       }
/*  448 */       return localNamingContext;
/*      */     }
/*      */   }
/*      */ 
/*      */   public NamingContext bind_new_context(NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, AlreadyBound, CannotProceed, InvalidName
/*      */   {
/*  480 */     NamingContext localNamingContext1 = null;
/*  481 */     NamingContext localNamingContext2 = null;
/*      */     try
/*      */     {
/*  486 */       localNamingContext1 = new_context();
/*  487 */       bind_context(paramArrayOfNameComponent, localNamingContext1);
/*  488 */       localNamingContext2 = localNamingContext1;
/*  489 */       localNamingContext1 = null;
/*      */     } finally {
/*      */       try {
/*  492 */         if (localNamingContext1 != null)
/*  493 */           localNamingContext1.destroy();
/*      */       } catch (NotEmpty localNotEmpty2) {
/*      */       }
/*      */     }
/*  497 */     if (this.updateLogger.isLoggable(Level.FINE))
/*      */     {
/*  500 */       this.updateLogger.fine("<<NAMING BIND>>New Context Bound To " + NamingUtils.getDirectoryStructuredName(paramArrayOfNameComponent));
/*      */     }
/*      */ 
/*  504 */     return localNamingContext2;
/*      */   }
/*      */ 
/*      */   public void destroy()
/*      */     throws NotEmpty
/*      */   {
/*  518 */     this.lifecycleLogger.fine("Destroying Naming Context ");
/*  519 */     NamingContextImpl localNamingContextImpl = this;
/*  520 */     synchronized (localNamingContextImpl) {
/*  521 */       if (localNamingContextImpl.IsEmpty() == true)
/*      */       {
/*  523 */         localNamingContextImpl.Destroy();
/*  524 */         this.lifecycleLogger.fine("<<LIFECYCLE DESTROY>><<SUCCESS>>");
/*      */       }
/*      */       else
/*      */       {
/*  529 */         this.lifecycleLogger.warning("<<LIFECYCLE DESTROY>><<FAILURE>> NamingContext children are not destroyed still..");
/*      */ 
/*  531 */         throw new NotEmpty();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void doBind(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent, org.omg.CORBA.Object paramObject, boolean paramBoolean, BindingType paramBindingType)
/*      */     throws NotFound, CannotProceed, InvalidName, AlreadyBound
/*      */   {
/*  581 */     if (paramArrayOfNameComponent.length < 1)
/*  582 */       throw new InvalidName();
/*      */     java.lang.Object localObject1;
/*      */     java.lang.Object localObject2;
/*  585 */     if (paramArrayOfNameComponent.length == 1)
/*      */     {
/*  587 */       if ((paramArrayOfNameComponent[0].id.length() == 0) && (paramArrayOfNameComponent[0].kind.length() == 0)) {
/*  588 */         throw new InvalidName();
/*      */       }
/*      */ 
/*  592 */       synchronized (paramNamingContextDataStore)
/*      */       {
/*  594 */         localObject1 = new BindingTypeHolder();
/*  595 */         if (paramBoolean) {
/*  596 */           localObject2 = paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], (BindingTypeHolder)localObject1);
/*  597 */           if (localObject2 != null)
/*      */           {
/*  604 */             if (((BindingTypeHolder)localObject1).value.value() == BindingType.nobject.value()) {
/*  605 */               if (paramBindingType.value() == BindingType.ncontext.value()) {
/*  606 */                 throw new NotFound(NotFoundReason.not_context, paramArrayOfNameComponent);
/*      */               }
/*      */ 
/*      */             }
/*  612 */             else if (paramBindingType.value() == BindingType.nobject.value()) {
/*  613 */               throw new NotFound(NotFoundReason.not_object, paramArrayOfNameComponent);
/*      */             }
/*      */ 
/*  617 */             paramNamingContextDataStore.Unbind(paramArrayOfNameComponent[0]);
/*      */           }
/*      */ 
/*      */         }
/*  621 */         else if (paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], (BindingTypeHolder)localObject1) != null)
/*      */         {
/*  623 */           throw new AlreadyBound();
/*      */         }
/*      */ 
/*  627 */         paramNamingContextDataStore.Bind(paramArrayOfNameComponent[0], paramObject, paramBindingType);
/*      */       }
/*      */     }
/*      */     else {
/*  631 */       ??? = resolveFirstAsContext(paramNamingContextDataStore, paramArrayOfNameComponent);
/*      */ 
/*  634 */       localObject1 = new NameComponent[paramArrayOfNameComponent.length - 1];
/*  635 */       System.arraycopy(paramArrayOfNameComponent, 1, localObject1, 0, paramArrayOfNameComponent.length - 1);
/*      */ 
/*  638 */       switch (paramBindingType.value())
/*      */       {
/*      */       case 0:
/*  642 */         if (paramBoolean)
/*  643 */           ((NamingContext)???).rebind((NameComponent[])localObject1, paramObject);
/*      */         else {
/*  645 */           ((NamingContext)???).bind((NameComponent[])localObject1, paramObject);
/*      */         }
/*  647 */         break;
/*      */       case 1:
/*  652 */         localObject2 = (NamingContext)paramObject;
/*      */ 
/*  654 */         if (paramBoolean)
/*  655 */           ((NamingContext)???).rebind_context((NameComponent[])localObject1, (NamingContext)localObject2);
/*      */         else {
/*  657 */           ((NamingContext)???).bind_context((NameComponent[])localObject1, (NamingContext)localObject2);
/*      */         }
/*  659 */         break;
/*      */       default:
/*  662 */         throw staticWrapper.namingCtxBadBindingtype();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static org.omg.CORBA.Object doResolve(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  698 */     org.omg.CORBA.Object localObject = null;
/*  699 */     BindingTypeHolder localBindingTypeHolder = new BindingTypeHolder();
/*      */ 
/*  703 */     if (paramArrayOfNameComponent.length < 1) {
/*  704 */       throw new InvalidName();
/*      */     }
/*      */ 
/*  707 */     if (paramArrayOfNameComponent.length == 1) {
/*  708 */       synchronized (paramNamingContextDataStore)
/*      */       {
/*  710 */         localObject = paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], localBindingTypeHolder);
/*      */       }
/*  712 */       if (localObject == null)
/*      */       {
/*  714 */         throw new NotFound(NotFoundReason.missing_node, paramArrayOfNameComponent);
/*      */       }
/*  716 */       return localObject;
/*      */     }
/*      */ 
/*  719 */     if ((paramArrayOfNameComponent[1].id.length() == 0) && (paramArrayOfNameComponent[1].kind.length() == 0)) {
/*  720 */       throw new InvalidName();
/*      */     }
/*      */ 
/*  723 */     ??? = resolveFirstAsContext(paramNamingContextDataStore, paramArrayOfNameComponent);
/*      */ 
/*  726 */     NameComponent[] arrayOfNameComponent = new NameComponent[paramArrayOfNameComponent.length - 1];
/*  727 */     System.arraycopy(paramArrayOfNameComponent, 1, arrayOfNameComponent, 0, paramArrayOfNameComponent.length - 1);
/*      */     try
/*      */     {
/*  733 */       Servant localServant = paramNamingContextDataStore.getNSPOA().reference_to_servant((org.omg.CORBA.Object)???);
/*      */ 
/*  735 */       return doResolve((NamingContextDataStore)localServant, arrayOfNameComponent); } catch (Exception localException) {
/*      */     }
/*  737 */     return ((NamingContext)???).resolve(arrayOfNameComponent);
/*      */   }
/*      */ 
/*      */   public static void doUnbind(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  769 */     if (paramArrayOfNameComponent.length < 1) {
/*  770 */       throw new InvalidName();
/*      */     }
/*      */ 
/*  773 */     if (paramArrayOfNameComponent.length == 1)
/*      */     {
/*  775 */       if ((paramArrayOfNameComponent[0].id.length() == 0) && (paramArrayOfNameComponent[0].kind.length() == 0)) {
/*  776 */         throw new InvalidName();
/*      */       }
/*      */ 
/*  779 */       localObject1 = null;
/*  780 */       synchronized (paramNamingContextDataStore)
/*      */       {
/*  782 */         localObject1 = paramNamingContextDataStore.Unbind(paramArrayOfNameComponent[0]);
/*      */       }
/*      */ 
/*  785 */       if (localObject1 == null)
/*      */       {
/*  787 */         throw new NotFound(NotFoundReason.missing_node, paramArrayOfNameComponent);
/*      */       }
/*  789 */       return;
/*      */     }
/*      */ 
/*  794 */     java.lang.Object localObject1 = resolveFirstAsContext(paramNamingContextDataStore, paramArrayOfNameComponent);
/*      */ 
/*  797 */     ??? = new NameComponent[paramArrayOfNameComponent.length - 1];
/*  798 */     System.arraycopy(paramArrayOfNameComponent, 1, ???, 0, paramArrayOfNameComponent.length - 1);
/*      */ 
/*  801 */     ((NamingContext)localObject1).unbind((NameComponent[])???);
/*      */   }
/*      */ 
/*      */   protected static NamingContext resolveFirstAsContext(NamingContextDataStore paramNamingContextDataStore, NameComponent[] paramArrayOfNameComponent)
/*      */     throws NotFound
/*      */   {
/*  821 */     org.omg.CORBA.Object localObject = null;
/*  822 */     BindingTypeHolder localBindingTypeHolder = new BindingTypeHolder();
/*  823 */     NamingContext localNamingContext = null;
/*      */ 
/*  825 */     synchronized (paramNamingContextDataStore)
/*      */     {
/*  827 */       localObject = paramNamingContextDataStore.Resolve(paramArrayOfNameComponent[0], localBindingTypeHolder);
/*  828 */       if (localObject == null)
/*      */       {
/*  830 */         throw new NotFound(NotFoundReason.missing_node, paramArrayOfNameComponent);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  835 */     if (localBindingTypeHolder.value != BindingType.ncontext)
/*      */     {
/*  837 */       throw new NotFound(NotFoundReason.not_context, paramArrayOfNameComponent);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  842 */       localNamingContext = NamingContextHelper.narrow(localObject);
/*      */     }
/*      */     catch (BAD_PARAM localBAD_PARAM) {
/*  845 */       throw new NotFound(NotFoundReason.not_context, paramArrayOfNameComponent);
/*      */     }
/*      */ 
/*  849 */     return localNamingContext;
/*      */   }
/*      */ 
/*      */   public String to_string(NameComponent[] paramArrayOfNameComponent)
/*      */     throws InvalidName
/*      */   {
/*  865 */     if ((paramArrayOfNameComponent == null) || (paramArrayOfNameComponent.length == 0))
/*      */     {
/*  867 */       throw new InvalidName();
/*      */     }
/*  869 */     NamingContextImpl localNamingContextImpl = this;
/*      */ 
/*  871 */     String str = this.insImpl.convertToString(paramArrayOfNameComponent);
/*      */ 
/*  873 */     if (str == null)
/*      */     {
/*  875 */       throw new InvalidName();
/*      */     }
/*      */ 
/*  878 */     return str;
/*      */   }
/*      */ 
/*      */   public NameComponent[] to_name(String paramString)
/*      */     throws InvalidName
/*      */   {
/*  894 */     if ((paramString == null) || (paramString.length() == 0))
/*      */     {
/*  896 */       throw new InvalidName();
/*      */     }
/*  898 */     NamingContextImpl localNamingContextImpl = this;
/*  899 */     NameComponent[] arrayOfNameComponent = this.insImpl.convertToNameComponent(paramString);
/*      */ 
/*  901 */     if ((arrayOfNameComponent == null) || (arrayOfNameComponent.length == 0))
/*      */     {
/*  903 */       throw new InvalidName();
/*      */     }
/*  905 */     for (int i = 0; i < arrayOfNameComponent.length; i++)
/*      */     {
/*  909 */       if (((arrayOfNameComponent[i].id == null) || (arrayOfNameComponent[i].id.length() == 0)) && ((arrayOfNameComponent[i].kind == null) || (arrayOfNameComponent[i].kind.length() == 0)))
/*      */       {
/*  913 */         throw new InvalidName();
/*      */       }
/*      */     }
/*  916 */     return arrayOfNameComponent;
/*      */   }
/*      */ 
/*      */   public String to_url(String paramString1, String paramString2)
/*      */     throws InvalidAddress, InvalidName
/*      */   {
/*  938 */     if ((paramString2 == null) || (paramString2.length() == 0))
/*      */     {
/*  940 */       throw new InvalidName();
/*      */     }
/*  942 */     if (paramString1 == null)
/*      */     {
/*  944 */       throw new InvalidAddress();
/*      */     }
/*      */ 
/*  947 */     NamingContextImpl localNamingContextImpl = this;
/*  948 */     String str = null;
/*  949 */     str = this.insImpl.createURLBasedAddress(paramString1, paramString2);
/*      */     try
/*      */     {
/*  953 */       INSURLHandler.getINSURLHandler().parseURL(str);
/*      */     } catch (BAD_PARAM localBAD_PARAM) {
/*  955 */       throw new InvalidAddress();
/*      */     }
/*      */ 
/*  958 */     return str;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object resolve_str(String paramString)
/*      */     throws NotFound, CannotProceed, InvalidName
/*      */   {
/*  980 */     org.omg.CORBA.Object localObject = null;
/*      */ 
/*  982 */     if ((paramString == null) || (paramString.length() == 0))
/*      */     {
/*  984 */       throw new InvalidName();
/*      */     }
/*  986 */     NamingContextImpl localNamingContextImpl = this;
/*  987 */     NameComponent[] arrayOfNameComponent = this.insImpl.convertToNameComponent(paramString);
/*      */ 
/*  990 */     if ((arrayOfNameComponent == null) || (arrayOfNameComponent.length == 0))
/*      */     {
/*  992 */       throw new InvalidName();
/*      */     }
/*  994 */     localObject = resolve(arrayOfNameComponent);
/*  995 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static String nameToString(NameComponent[] paramArrayOfNameComponent)
/*      */   {
/* 1003 */     StringBuffer localStringBuffer = new StringBuffer("{");
/* 1004 */     if ((paramArrayOfNameComponent != null) || (paramArrayOfNameComponent.length > 0)) {
/* 1005 */       for (int i = 0; i < paramArrayOfNameComponent.length; i++) {
/* 1006 */         if (i > 0)
/* 1007 */           localStringBuffer.append(",");
/* 1008 */         localStringBuffer.append("[").append(paramArrayOfNameComponent[i].id).append(",").append(paramArrayOfNameComponent[i].kind).append("]");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1015 */     localStringBuffer.append("}");
/* 1016 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static void dprint(String paramString)
/*      */   {
/* 1023 */     NamingUtils.dprint("NamingContextImpl(" + Thread.currentThread().getName() + " at " + System.currentTimeMillis() + " ems): " + paramString);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.naming.cosnaming.NamingContextImpl
 * JD-Core Version:    0.6.2
 */
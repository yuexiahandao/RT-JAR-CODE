/*     */ package com.sun.corba.se.impl.javax.rmi.CORBA;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.AnyImpl;
/*     */ import com.sun.corba.se.impl.io.ValueHandlerImpl;
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.util.IdentityHashtable;
/*     */ import com.sun.corba.se.impl.util.JDKBridge;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import com.sun.corba.se.pept.transport.ContactInfoList;
/*     */ import com.sun.corba.se.spi.copyobject.CopierManager;
/*     */ import com.sun.corba.se.spi.copyobject.ObjectCopier;
/*     */ import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
/*     */ import com.sun.corba.se.spi.copyobject.ReflectiveCopyException;
/*     */ import com.sun.corba.se.spi.oa.OAInvocationInfo;
/*     */ import com.sun.corba.se.spi.orb.ORBVersion;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.rmi.AccessException;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.ServerError;
/*     */ import java.rmi.ServerException;
/*     */ import java.rmi.UnexpectedException;
/*     */ import java.rmi.server.RMIClassLoader;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.Enumeration;
/*     */ import javax.rmi.CORBA.Stub;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import javax.rmi.CORBA.UtilDelegate;
/*     */ import javax.rmi.CORBA.ValueHandler;
/*     */ import javax.transaction.InvalidTransactionException;
/*     */ import javax.transaction.TransactionRequiredException;
/*     */ import javax.transaction.TransactionRolledbackException;
/*     */ import org.omg.CORBA.ACTIVITY_COMPLETED;
/*     */ import org.omg.CORBA.ACTIVITY_REQUIRED;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.BAD_PARAM;
/*     */ import org.omg.CORBA.COMM_FAILURE;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.INVALID_ACTIVITY;
/*     */ import org.omg.CORBA.INVALID_TRANSACTION;
/*     */ import org.omg.CORBA.INV_OBJREF;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.NO_PERMISSION;
/*     */ import org.omg.CORBA.OBJECT_NOT_EXIST;
/*     */ import org.omg.CORBA.OBJ_ADAPTER;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TRANSACTION_REQUIRED;
/*     */ import org.omg.CORBA.TRANSACTION_ROLLEDBACK;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.UnknownException;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public class Util
/*     */   implements UtilDelegate
/*     */ {
/* 123 */   private static KeepAlive keepAlive = null;
/*     */ 
/* 126 */   private static IdentityHashtable exportedServants = new IdentityHashtable();
/*     */ 
/* 128 */   private static final ValueHandlerImpl valueHandlerSingleton = SharedSecrets.getJavaCorbaAccess().newValueHandlerImpl();
/*     */ 
/* 131 */   private UtilSystemException utilWrapper = UtilSystemException.get("rpc.encoding");
/*     */ 
/* 134 */   private static Util instance = null;
/*     */ 
/*     */   public Util() {
/* 137 */     setInstance(this);
/*     */   }
/*     */ 
/*     */   private static void setInstance(Util paramUtil) {
/* 141 */     assert (instance == null) : "Instance already defined";
/* 142 */     instance = paramUtil;
/*     */   }
/*     */ 
/*     */   public static Util getInstance() {
/* 146 */     return instance;
/*     */   }
/*     */ 
/*     */   public static boolean isInstanceDefined() {
/* 150 */     return instance != null;
/*     */   }
/*     */ 
/*     */   public void unregisterTargetsForORB(org.omg.CORBA.ORB paramORB)
/*     */   {
/* 157 */     for (Enumeration localEnumeration = exportedServants.keys(); localEnumeration.hasMoreElements(); )
/*     */     {
/* 159 */       java.lang.Object localObject = localEnumeration.nextElement();
/* 160 */       Remote localRemote = (Remote)((localObject instanceof Tie) ? ((Tie)localObject).getTarget() : localObject);
/*     */       try
/*     */       {
/* 165 */         if (paramORB == getTie(localRemote).orb())
/*     */           try {
/* 167 */             unexportObject(localRemote);
/*     */           }
/*     */           catch (NoSuchObjectException localNoSuchObjectException)
/*     */           {
/*     */           }
/*     */       }
/*     */       catch (BAD_OPERATION localBAD_OPERATION)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public RemoteException mapSystemException(SystemException paramSystemException)
/*     */   {
/* 186 */     if ((paramSystemException instanceof UnknownException)) {
/* 187 */       localObject1 = ((UnknownException)paramSystemException).originalEx;
/* 188 */       if ((localObject1 instanceof Error))
/* 189 */         return new ServerError("Error occurred in server thread", (Error)localObject1);
/* 190 */       if ((localObject1 instanceof RemoteException)) {
/* 191 */         return new ServerException("RemoteException occurred in server thread", (Exception)localObject1);
/*     */       }
/* 193 */       if ((localObject1 instanceof RuntimeException)) {
/* 194 */         throw ((RuntimeException)localObject1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 199 */     java.lang.Object localObject1 = paramSystemException.getClass().getName();
/* 200 */     String str1 = ((String)localObject1).substring(((String)localObject1).lastIndexOf('.') + 1);
/*     */     String str2;
/* 202 */     switch (paramSystemException.completed.value()) {
/*     */     case 0:
/* 204 */       str2 = "Yes";
/* 205 */       break;
/*     */     case 1:
/* 207 */       str2 = "No";
/* 208 */       break;
/*     */     case 2:
/*     */     default:
/* 211 */       str2 = "Maybe";
/*     */     }
/*     */ 
/* 215 */     String str3 = "CORBA " + str1 + " " + paramSystemException.minor + " " + str2;
/*     */ 
/* 218 */     if ((paramSystemException instanceof COMM_FAILURE))
/* 219 */       return new MarshalException(str3, paramSystemException);
/*     */     java.lang.Object localObject2;
/* 220 */     if ((paramSystemException instanceof INV_OBJREF)) {
/* 221 */       localObject2 = new NoSuchObjectException(str3);
/* 222 */       ((RemoteException)localObject2).detail = paramSystemException;
/* 223 */       return localObject2;
/* 224 */     }if ((paramSystemException instanceof NO_PERMISSION))
/* 225 */       return new AccessException(str3, paramSystemException);
/* 226 */     if ((paramSystemException instanceof MARSHAL))
/* 227 */       return new MarshalException(str3, paramSystemException);
/* 228 */     if ((paramSystemException instanceof OBJECT_NOT_EXIST)) {
/* 229 */       localObject2 = new NoSuchObjectException(str3);
/* 230 */       ((RemoteException)localObject2).detail = paramSystemException;
/* 231 */       return localObject2;
/* 232 */     }if ((paramSystemException instanceof TRANSACTION_REQUIRED)) {
/* 233 */       localObject2 = new TransactionRequiredException(str3);
/* 234 */       ((RemoteException)localObject2).detail = paramSystemException;
/* 235 */       return localObject2;
/* 236 */     }if ((paramSystemException instanceof TRANSACTION_ROLLEDBACK)) {
/* 237 */       localObject2 = new TransactionRolledbackException(str3);
/* 238 */       ((RemoteException)localObject2).detail = paramSystemException;
/* 239 */       return localObject2;
/* 240 */     }if ((paramSystemException instanceof INVALID_TRANSACTION)) {
/* 241 */       localObject2 = new InvalidTransactionException(str3);
/* 242 */       ((RemoteException)localObject2).detail = paramSystemException;
/* 243 */       return localObject2;
/* 244 */     }if ((paramSystemException instanceof BAD_PARAM)) {
/* 245 */       localObject2 = paramSystemException;
/*     */ 
/* 249 */       if ((paramSystemException.minor == 1398079489) || (paramSystemException.minor == 1330446342))
/*     */       {
/* 252 */         if (paramSystemException.getMessage() != null)
/* 253 */           localObject2 = new NotSerializableException(paramSystemException.getMessage());
/*     */         else {
/* 255 */           localObject2 = new NotSerializableException();
/*     */         }
/* 257 */         ((Exception)localObject2).initCause(paramSystemException);
/*     */       }
/*     */ 
/* 260 */       return new MarshalException(str3, (Exception)localObject2);
/*     */     }
/*     */     Class[] arrayOfClass;
/*     */     Constructor localConstructor;
/*     */     java.lang.Object[] arrayOfObject;
/* 261 */     if ((paramSystemException instanceof ACTIVITY_REQUIRED)) {
/*     */       try {
/* 263 */         localObject2 = SharedSecrets.getJavaCorbaAccess().loadClass("javax.activity.ActivityRequiredException");
/*     */ 
/* 265 */         arrayOfClass = new Class[2];
/* 266 */         arrayOfClass[0] = String.class;
/* 267 */         arrayOfClass[1] = Throwable.class;
/* 268 */         localConstructor = ((Class)localObject2).getConstructor(arrayOfClass);
/* 269 */         arrayOfObject = new java.lang.Object[2];
/* 270 */         arrayOfObject[0] = str3;
/* 271 */         arrayOfObject[1] = paramSystemException;
/* 272 */         return (RemoteException)localConstructor.newInstance(arrayOfObject);
/*     */       } catch (Throwable localThrowable1) {
/* 274 */         this.utilWrapper.classNotFound(localThrowable1, "javax.activity.ActivityRequiredException");
/*     */       }
/*     */     }
/* 277 */     else if ((paramSystemException instanceof ACTIVITY_COMPLETED)) {
/*     */       try {
/* 279 */         Class localClass1 = SharedSecrets.getJavaCorbaAccess().loadClass("javax.activity.ActivityCompletedException");
/*     */ 
/* 281 */         arrayOfClass = new Class[2];
/* 282 */         arrayOfClass[0] = String.class;
/* 283 */         arrayOfClass[1] = Throwable.class;
/* 284 */         localConstructor = localClass1.getConstructor(arrayOfClass);
/* 285 */         arrayOfObject = new java.lang.Object[2];
/* 286 */         arrayOfObject[0] = str3;
/* 287 */         arrayOfObject[1] = paramSystemException;
/* 288 */         return (RemoteException)localConstructor.newInstance(arrayOfObject);
/*     */       } catch (Throwable localThrowable2) {
/* 290 */         this.utilWrapper.classNotFound(localThrowable2, "javax.activity.ActivityCompletedException");
/*     */       }
/*     */     }
/* 293 */     else if ((paramSystemException instanceof INVALID_ACTIVITY)) {
/*     */       try {
/* 295 */         Class localClass2 = SharedSecrets.getJavaCorbaAccess().loadClass("javax.activity.InvalidActivityException");
/*     */ 
/* 297 */         arrayOfClass = new Class[2];
/* 298 */         arrayOfClass[0] = String.class;
/* 299 */         arrayOfClass[1] = Throwable.class;
/* 300 */         localConstructor = localClass2.getConstructor(arrayOfClass);
/* 301 */         arrayOfObject = new java.lang.Object[2];
/* 302 */         arrayOfObject[0] = str3;
/* 303 */         arrayOfObject[1] = paramSystemException;
/* 304 */         return (RemoteException)localConstructor.newInstance(arrayOfObject);
/*     */       } catch (Throwable localThrowable3) {
/* 306 */         this.utilWrapper.classNotFound(localThrowable3, "javax.activity.InvalidActivityException");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 312 */     return new RemoteException(str3, paramSystemException);
/*     */   }
/*     */ 
/*     */   public void writeAny(org.omg.CORBA.portable.OutputStream paramOutputStream, java.lang.Object paramObject)
/*     */   {
/* 323 */     org.omg.CORBA.ORB localORB = paramOutputStream.orb();
/*     */ 
/* 326 */     Any localAny = localORB.create_any();
/*     */ 
/* 329 */     java.lang.Object localObject = Utility.autoConnect(paramObject, localORB, false);
/*     */ 
/* 331 */     if ((localObject instanceof org.omg.CORBA.Object)) {
/* 332 */       localAny.insert_Object((org.omg.CORBA.Object)localObject);
/*     */     }
/* 334 */     else if (localObject == null)
/*     */     {
/* 337 */       localAny.insert_Value(null, createTypeCodeForNull(localORB));
/*     */     }
/* 339 */     else if ((localObject instanceof Serializable))
/*     */     {
/* 342 */       TypeCode localTypeCode = createTypeCode((Serializable)localObject, localAny, localORB);
/* 343 */       if (localTypeCode == null)
/* 344 */         localAny.insert_Value((Serializable)localObject);
/*     */       else
/* 346 */         localAny.insert_Value((Serializable)localObject, localTypeCode);
/* 347 */     } else if ((localObject instanceof Remote)) {
/* 348 */       ORBUtility.throwNotSerializableForCorba(localObject.getClass().getName());
/*     */     } else {
/* 350 */       ORBUtility.throwNotSerializableForCorba(localObject.getClass().getName());
/*     */     }
/*     */ 
/* 355 */     paramOutputStream.write_any(localAny);
/*     */   }
/*     */ 
/*     */   private TypeCode createTypeCode(Serializable paramSerializable, Any paramAny, org.omg.CORBA.ORB paramORB)
/*     */   {
/* 377 */     if (((paramAny instanceof AnyImpl)) && ((paramORB instanceof com.sun.corba.se.spi.orb.ORB)))
/*     */     {
/* 380 */       AnyImpl localAnyImpl = (AnyImpl)paramAny;
/*     */ 
/* 383 */       com.sun.corba.se.spi.orb.ORB localORB = (com.sun.corba.se.spi.orb.ORB)paramORB;
/*     */ 
/* 385 */       return localAnyImpl.createTypeCodeForClass(paramSerializable.getClass(), localORB);
/*     */     }
/*     */ 
/* 388 */     return null;
/*     */   }
/*     */ 
/*     */   private TypeCode createTypeCodeForNull(org.omg.CORBA.ORB paramORB)
/*     */   {
/* 400 */     if ((paramORB instanceof com.sun.corba.se.spi.orb.ORB))
/*     */     {
/* 402 */       localObject = (com.sun.corba.se.spi.orb.ORB)paramORB;
/*     */ 
/* 409 */       if ((!ORBVersionFactory.getFOREIGN().equals(((com.sun.corba.se.spi.orb.ORB)localObject).getORBVersion())) && (ORBVersionFactory.getNEWER().compareTo(((com.sun.corba.se.spi.orb.ORB)localObject).getORBVersion()) > 0))
/*     */       {
/* 412 */         return paramORB.get_primitive_tc(TCKind.tk_value);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 419 */     java.lang.Object localObject = "IDL:omg.org/CORBA/AbstractBase:1.0";
/*     */ 
/* 421 */     return paramORB.create_abstract_interface_tc((String)localObject, "");
/*     */   }
/*     */ 
/*     */   public java.lang.Object readAny(org.omg.CORBA.portable.InputStream paramInputStream)
/*     */   {
/* 431 */     Any localAny = paramInputStream.read_any();
/* 432 */     if (localAny.type().kind().value() == 14) {
/* 433 */       return localAny.extract_Object();
/*     */     }
/* 435 */     return localAny.extract_Value();
/*     */   }
/*     */ 
/*     */   public void writeRemoteObject(org.omg.CORBA.portable.OutputStream paramOutputStream, java.lang.Object paramObject)
/*     */   {
/* 452 */     java.lang.Object localObject = Utility.autoConnect(paramObject, paramOutputStream.orb(), false);
/* 453 */     paramOutputStream.write_Object((org.omg.CORBA.Object)localObject);
/*     */   }
/*     */ 
/*     */   public void writeAbstractObject(org.omg.CORBA.portable.OutputStream paramOutputStream, java.lang.Object paramObject)
/*     */   {
/* 470 */     java.lang.Object localObject = Utility.autoConnect(paramObject, paramOutputStream.orb(), false);
/* 471 */     ((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream).write_abstract_interface(localObject);
/*     */   }
/*     */ 
/*     */   public void registerTarget(Tie paramTie, Remote paramRemote)
/*     */   {
/* 482 */     synchronized (exportedServants)
/*     */     {
/* 484 */       if (lookupTie(paramRemote) == null)
/*     */       {
/* 486 */         exportedServants.put(paramRemote, paramTie);
/* 487 */         paramTie.setTarget(paramRemote);
/*     */ 
/* 490 */         if (keepAlive == null)
/*     */         {
/* 493 */           keepAlive = (KeepAlive)AccessController.doPrivileged(new PrivilegedAction() {
/*     */             public java.lang.Object run() {
/* 495 */               return new KeepAlive();
/*     */             }
/*     */           });
/* 498 */           keepAlive.start();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unexportObject(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 512 */     synchronized (exportedServants) {
/* 513 */       Tie localTie = lookupTie(paramRemote);
/* 514 */       if (localTie != null) {
/* 515 */         exportedServants.remove(paramRemote);
/* 516 */         Utility.purgeStubForTie(localTie);
/* 517 */         Utility.purgeTieAndServant(localTie);
/*     */         try {
/* 519 */           cleanUpTie(localTie);
/*     */         }
/*     */         catch (BAD_OPERATION localBAD_OPERATION)
/*     */         {
/*     */         }
/*     */         catch (OBJ_ADAPTER localOBJ_ADAPTER)
/*     */         {
/*     */         }
/*     */ 
/* 528 */         if (exportedServants.isEmpty()) {
/* 529 */           keepAlive.quit();
/* 530 */           keepAlive = null;
/*     */         }
/*     */       } else {
/* 533 */         throw new NoSuchObjectException("Tie not found");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void cleanUpTie(Tie paramTie)
/*     */     throws NoSuchObjectException
/*     */   {
/* 541 */     paramTie.setTarget(null);
/* 542 */     paramTie.deactivate();
/*     */   }
/*     */ 
/*     */   public Tie getTie(Remote paramRemote)
/*     */   {
/* 551 */     synchronized (exportedServants) {
/* 552 */       return lookupTie(paramRemote);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static Tie lookupTie(Remote paramRemote)
/*     */   {
/* 561 */     Tie localTie = (Tie)exportedServants.get(paramRemote);
/* 562 */     if ((localTie == null) && ((paramRemote instanceof Tie)) && 
/* 563 */       (exportedServants.contains(paramRemote))) {
/* 564 */       localTie = (Tie)paramRemote;
/*     */     }
/*     */ 
/* 567 */     return localTie;
/*     */   }
/*     */ 
/*     */   public ValueHandler createValueHandler()
/*     */   {
/* 577 */     return valueHandlerSingleton;
/*     */   }
/*     */ 
/*     */   public String getCodebase(Class paramClass)
/*     */   {
/* 586 */     return RMIClassLoader.getClassAnnotation(paramClass);
/*     */   }
/*     */ 
/*     */   public Class loadClass(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/* 602 */     return JDKBridge.loadClass(paramString1, paramString2, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public boolean isLocal(Stub paramStub)
/*     */     throws RemoteException
/*     */   {
/* 627 */     boolean bool = false;
/*     */     try
/*     */     {
/* 630 */       Delegate localDelegate = paramStub._get_delegate();
/* 631 */       if ((localDelegate instanceof CorbaClientDelegate))
/*     */       {
/* 633 */         CorbaClientDelegate localCorbaClientDelegate = (CorbaClientDelegate)localDelegate;
/* 634 */         ContactInfoList localContactInfoList = localCorbaClientDelegate.getContactInfoList();
/* 635 */         if ((localContactInfoList instanceof CorbaContactInfoList)) {
/* 636 */           CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)localContactInfoList;
/* 637 */           LocalClientRequestDispatcher localLocalClientRequestDispatcher = localCorbaContactInfoList.getLocalClientRequestDispatcher();
/* 638 */           bool = localLocalClientRequestDispatcher.useLocalInvocation(null);
/*     */         }
/*     */       }
/*     */       else {
/* 642 */         bool = localDelegate.is_local(paramStub);
/*     */       }
/*     */     } catch (SystemException localSystemException) {
/* 645 */       throw javax.rmi.CORBA.Util.mapSystemException(localSystemException);
/*     */     }
/*     */ 
/* 648 */     return bool;
/*     */   }
/*     */ 
/*     */   public RemoteException wrapException(Throwable paramThrowable)
/*     */   {
/* 659 */     if ((paramThrowable instanceof SystemException)) {
/* 660 */       return mapSystemException((SystemException)paramThrowable);
/*     */     }
/*     */ 
/* 663 */     if ((paramThrowable instanceof Error))
/* 664 */       return new ServerError("Error occurred in server thread", (Error)paramThrowable);
/* 665 */     if ((paramThrowable instanceof RemoteException)) {
/* 666 */       return new ServerException("RemoteException occurred in server thread", (Exception)paramThrowable);
/*     */     }
/* 668 */     if ((paramThrowable instanceof RuntimeException)) {
/* 669 */       throw ((RuntimeException)paramThrowable);
/*     */     }
/*     */ 
/* 672 */     if ((paramThrowable instanceof Exception)) {
/* 673 */       return new UnexpectedException(paramThrowable.toString(), (Exception)paramThrowable);
/*     */     }
/* 675 */     return new UnexpectedException(paramThrowable.toString());
/*     */   }
/*     */ 
/*     */   public java.lang.Object[] copyObjects(java.lang.Object[] paramArrayOfObject, org.omg.CORBA.ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 690 */     if (paramArrayOfObject == null)
/*     */     {
/* 695 */       throw new NullPointerException();
/*     */     }
/* 697 */     Class localClass = paramArrayOfObject.getClass().getComponentType();
/* 698 */     if ((Remote.class.isAssignableFrom(localClass)) && (!localClass.isInterface()))
/*     */     {
/* 702 */       Remote[] arrayOfRemote = new Remote[paramArrayOfObject.length];
/* 703 */       System.arraycopy(paramArrayOfObject, 0, arrayOfRemote, 0, paramArrayOfObject.length);
/* 704 */       return (java.lang.Object[])copyObject(arrayOfRemote, paramORB);
/*     */     }
/* 706 */     return (java.lang.Object[])copyObject(paramArrayOfObject, paramORB);
/*     */   }
/*     */ 
/*     */   public java.lang.Object copyObject(java.lang.Object paramObject, org.omg.CORBA.ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 720 */     if ((paramORB instanceof com.sun.corba.se.spi.orb.ORB)) {
/* 721 */       localObject1 = (com.sun.corba.se.spi.orb.ORB)paramORB;
/*     */       try
/*     */       {
/* 727 */         return ((com.sun.corba.se.spi.orb.ORB)localObject1).peekInvocationInfo().getCopierFactory().make().copy(paramObject);
/*     */       }
/*     */       catch (EmptyStackException localEmptyStackException)
/*     */       {
/* 733 */         localObject2 = ((com.sun.corba.se.spi.orb.ORB)localObject1).getCopierManager();
/* 734 */         ObjectCopier localObjectCopier = ((CopierManager)localObject2).getDefaultObjectCopierFactory().make();
/* 735 */         return localObjectCopier.copy(paramObject);
/*     */       }
/*     */       catch (ReflectiveCopyException localReflectiveCopyException) {
/* 738 */         java.lang.Object localObject2 = new RemoteException();
/* 739 */         ((RemoteException)localObject2).initCause(localReflectiveCopyException);
/* 740 */         throw ((Throwable)localObject2);
/*     */       }
/*     */     }
/* 743 */     java.lang.Object localObject1 = (org.omg.CORBA_2_3.portable.OutputStream)paramORB.create_output_stream();
/*     */ 
/* 745 */     ((org.omg.CORBA_2_3.portable.OutputStream)localObject1).write_value((Serializable)paramObject);
/* 746 */     org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)((org.omg.CORBA_2_3.portable.OutputStream)localObject1).create_input_stream();
/*     */ 
/* 748 */     return localInputStream.read_value();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.javax.rmi.CORBA.Util
 * JD-Core Version:    0.6.2
 */
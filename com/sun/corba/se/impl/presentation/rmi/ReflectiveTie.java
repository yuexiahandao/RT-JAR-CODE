/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
/*     */ import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ import org.omg.CORBA.portable.UnknownException;
/*     */ import org.omg.PortableServer.POA;
/*     */ import org.omg.PortableServer.POAPackage.ObjectNotActive;
/*     */ import org.omg.PortableServer.POAPackage.ServantNotActive;
/*     */ import org.omg.PortableServer.POAPackage.WrongPolicy;
/*     */ import org.omg.PortableServer.Servant;
/*     */ 
/*     */ public final class ReflectiveTie extends Servant
/*     */   implements Tie
/*     */ {
/*  57 */   private Remote target = null;
/*     */   private PresentationManager pm;
/*  59 */   private PresentationManager.ClassData classData = null;
/*  60 */   private ORBUtilSystemException wrapper = null;
/*     */ 
/*     */   public ReflectiveTie(PresentationManager paramPresentationManager, ORBUtilSystemException paramORBUtilSystemException)
/*     */   {
/*  64 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  65 */     if (localSecurityManager != null) {
/*  66 */       localSecurityManager.checkPermission(new DynamicAccessPermission("access"));
/*     */     }
/*  68 */     this.pm = paramPresentationManager;
/*  69 */     this.wrapper = paramORBUtilSystemException;
/*     */   }
/*     */ 
/*     */   public String[] _all_interfaces(POA paramPOA, byte[] paramArrayOfByte)
/*     */   {
/*  75 */     return this.classData.getTypeIds();
/*     */   }
/*     */ 
/*     */   public void setTarget(Remote paramRemote)
/*     */   {
/*  80 */     this.target = paramRemote;
/*     */ 
/*  82 */     if (paramRemote == null) {
/*  83 */       this.classData = null;
/*     */     } else {
/*  85 */       Class localClass = paramRemote.getClass();
/*  86 */       this.classData = this.pm.getClassData(localClass);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Remote getTarget()
/*     */   {
/*  92 */     return this.target;
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object thisObject()
/*     */   {
/*  97 */     return _this_object();
/*     */   }
/*     */ 
/*     */   public void deactivate()
/*     */   {
/*     */     try {
/* 103 */       _poa().deactivate_object(_poa().servant_to_id(this));
/*     */     }
/*     */     catch (WrongPolicy localWrongPolicy) {
/*     */     }
/*     */     catch (ObjectNotActive localObjectNotActive) {
/*     */     }
/*     */     catch (ServantNotActive localServantNotActive) {
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.ORB orb() {
/* 114 */     return _orb();
/*     */   }
/*     */ 
/*     */   public void orb(org.omg.CORBA.ORB paramORB) {
/*     */     try {
/* 119 */       com.sun.corba.se.spi.orb.ORB localORB = (com.sun.corba.se.spi.orb.ORB)paramORB;
/*     */ 
/* 121 */       ((org.omg.CORBA_2_3.ORB)paramORB).set_delegate(this);
/*     */     } catch (ClassCastException localClassCastException) {
/* 123 */       throw this.wrapper.badOrbForServant(localClassCastException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.portable.OutputStream _invoke(String paramString, org.omg.CORBA.portable.InputStream paramInputStream, ResponseHandler paramResponseHandler) {
/* 130 */     Method localMethod = null;
/* 131 */     DynamicMethodMarshaller localDynamicMethodMarshaller = null;
/*     */     java.lang.Object localObject1;
/*     */     try {
/* 134 */       org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream;
/*     */ 
/* 136 */       localMethod = this.classData.getIDLNameTranslator().getMethod(paramString);
/* 137 */       if (localMethod == null) {
/* 138 */         throw this.wrapper.methodNotFoundInTie(paramString, this.target.getClass().getName());
/*     */       }
/*     */ 
/* 141 */       localDynamicMethodMarshaller = this.pm.getDynamicMethodMarshaller(localMethod);
/*     */ 
/* 143 */       localObject1 = localDynamicMethodMarshaller.readArguments(localInputStream);
/*     */ 
/* 145 */       localObject2 = localMethod.invoke(this.target, (java.lang.Object[])localObject1);
/*     */ 
/* 147 */       org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createReply();
/*     */ 
/* 149 */       localDynamicMethodMarshaller.writeResult(localOutputStream, localObject2);
/*     */ 
/* 151 */       return localOutputStream;
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 153 */       throw this.wrapper.invocationErrorInReflectiveTie(localIllegalAccessException, localMethod.getName(), localMethod.getDeclaringClass().getName());
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 157 */       throw this.wrapper.invocationErrorInReflectiveTie(localIllegalArgumentException, localMethod.getName(), localMethod.getDeclaringClass().getName());
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException)
/*     */     {
/*     */       java.lang.Object localObject2;
/* 164 */       localObject1 = localInvocationTargetException.getCause();
/* 165 */       if ((localObject1 instanceof SystemException))
/* 166 */         throw ((SystemException)localObject1);
/* 167 */       if (((localObject1 instanceof Exception)) && (localDynamicMethodMarshaller.isDeclaredException((Throwable)localObject1)))
/*     */       {
/* 169 */         localObject2 = (org.omg.CORBA_2_3.portable.OutputStream)paramResponseHandler.createExceptionReply();
/* 170 */         localDynamicMethodMarshaller.writeException((org.omg.CORBA_2_3.portable.OutputStream)localObject2, (Exception)localObject1);
/* 171 */         return localObject2;
/*     */       }
/*     */     }
/* 173 */     throw new UnknownException((Throwable)localObject1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.ReflectiveTie
 * JD-Core Version:    0.6.2
 */
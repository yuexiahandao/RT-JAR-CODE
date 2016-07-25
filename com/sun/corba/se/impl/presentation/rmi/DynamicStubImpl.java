/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.ior.StubIORImpl;
/*     */ import com.sun.corba.se.impl.util.JDKBridge;
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*     */ import com.sun.corba.se.spi.presentation.rmi.DynamicStub;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.rmi.RemoteException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA_2_3.portable.ObjectImpl;
/*     */ 
/*     */ public class DynamicStubImpl extends ObjectImpl
/*     */   implements DynamicStub, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4852612040012087675L;
/*     */   private String[] typeIds;
/*     */   private StubIORImpl ior;
/*  65 */   private DynamicStub self = null;
/*     */ 
/*     */   public void setSelf(DynamicStub paramDynamicStub)
/*     */   {
/*  70 */     this.self = paramDynamicStub;
/*     */   }
/*     */ 
/*     */   public DynamicStub getSelf()
/*     */   {
/*  75 */     return this.self;
/*     */   }
/*     */ 
/*     */   public DynamicStubImpl(String[] paramArrayOfString)
/*     */   {
/*  80 */     this.typeIds = paramArrayOfString;
/*  81 */     this.ior = null;
/*     */   }
/*     */ 
/*     */   public void setDelegate(Delegate paramDelegate)
/*     */   {
/*  86 */     _set_delegate(paramDelegate);
/*     */   }
/*     */ 
/*     */   public Delegate getDelegate()
/*     */   {
/*  91 */     return _get_delegate();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.ORB getORB()
/*     */   {
/*  96 */     return _orb();
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 101 */     return this.typeIds;
/*     */   }
/*     */ 
/*     */   public String[] getTypeIds()
/*     */   {
/* 106 */     return _ids();
/*     */   }
/*     */ 
/*     */   public void connect(org.omg.CORBA.ORB paramORB) throws RemoteException
/*     */   {
/* 111 */     this.ior = StubConnectImpl.connect(this.ior, this.self, this, paramORB);
/*     */   }
/*     */ 
/*     */   public boolean isLocal()
/*     */   {
/* 116 */     return _is_local();
/*     */   }
/*     */ 
/*     */   public OutputStream request(String paramString, boolean paramBoolean)
/*     */   {
/* 122 */     return _request(paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 128 */     this.ior = new StubIORImpl();
/* 129 */     this.ior.doRead(paramObjectInputStream);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 135 */     if (this.ior == null)
/* 136 */       this.ior = new StubIORImpl(this);
/* 137 */     this.ior.doWrite(paramObjectOutputStream);
/*     */   }
/*     */ 
/*     */   public Object readResolve()
/*     */   {
/* 142 */     String str1 = this.ior.getRepositoryId();
/* 143 */     String str2 = RepositoryId.cache.getId(str1).getClassName();
/*     */ 
/* 145 */     Class localClass = null;
/*     */     try
/*     */     {
/* 148 */       localClass = JDKBridge.loadClass(str2, null, null);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/*     */     }
/* 153 */     PresentationManager localPresentationManager = com.sun.corba.se.spi.orb.ORB.getPresentationManager();
/*     */ 
/* 155 */     PresentationManager.ClassData localClassData = localPresentationManager.getClassData(localClass);
/* 156 */     InvocationHandlerFactoryImpl localInvocationHandlerFactoryImpl = (InvocationHandlerFactoryImpl)localClassData.getInvocationHandlerFactory();
/*     */ 
/* 158 */     return localInvocationHandlerFactoryImpl.getInvocationHandler(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.DynamicStubImpl
 * JD-Core Version:    0.6.2
 */
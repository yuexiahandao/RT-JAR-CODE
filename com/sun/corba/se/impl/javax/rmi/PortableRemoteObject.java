/*     */ package com.sun.corba.se.impl.javax.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import java.io.Externalizable;
/*     */ import java.io.Serializable;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.ExportException;
/*     */ import java.rmi.server.RemoteObject;
/*     */ import java.rmi.server.RemoteStub;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import javax.rmi.CORBA.PortableRemoteObjectDelegate;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.SystemException;
/*     */ 
/*     */ public class PortableRemoteObject
/*     */   implements PortableRemoteObjectDelegate
/*     */ {
/*     */   public void exportObject(Remote paramRemote)
/*     */     throws RemoteException
/*     */   {
/*  91 */     if (paramRemote == null) {
/*  92 */       throw new NullPointerException("invalid argument");
/*     */     }
/*     */ 
/*  97 */     if (Util.getTie(paramRemote) != null)
/*     */     {
/* 101 */       throw new ExportException(paramRemote.getClass().getName() + " already exported");
/*     */     }
/*     */ 
/* 106 */     Tie localTie = Utility.loadTie(paramRemote);
/*     */ 
/* 108 */     if (localTie != null)
/*     */     {
/* 112 */       Util.registerTarget(localTie, paramRemote);
/*     */     }
/*     */     else
/*     */     {
/* 119 */       UnicastRemoteObject.exportObject(paramRemote);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Remote toStub(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 134 */     Remote localRemote = null;
/* 135 */     if (paramRemote == null) {
/* 136 */       throw new NullPointerException("invalid argument");
/*     */     }
/*     */ 
/* 140 */     if (StubAdapter.isStub(paramRemote)) {
/* 141 */       return paramRemote;
/*     */     }
/*     */ 
/* 145 */     if ((paramRemote instanceof RemoteStub)) {
/* 146 */       return paramRemote;
/*     */     }
/*     */ 
/* 150 */     Tie localTie = Util.getTie(paramRemote);
/*     */ 
/* 152 */     if (localTie != null) {
/* 153 */       localRemote = Utility.loadStub(localTie, null, null, true);
/*     */     }
/* 155 */     else if (Utility.loadTie(paramRemote) == null) {
/* 156 */       localRemote = RemoteObject.toStub(paramRemote);
/*     */     }
/*     */ 
/* 160 */     if (localRemote == null) {
/* 161 */       throw new NoSuchObjectException("object not exported");
/*     */     }
/*     */ 
/* 164 */     return localRemote;
/*     */   }
/*     */ 
/*     */   public void unexportObject(Remote paramRemote)
/*     */     throws NoSuchObjectException
/*     */   {
/* 177 */     if (paramRemote == null) {
/* 178 */       throw new NullPointerException("invalid argument");
/*     */     }
/*     */ 
/* 181 */     if ((StubAdapter.isStub(paramRemote)) || ((paramRemote instanceof RemoteStub)))
/*     */     {
/* 183 */       throw new NoSuchObjectException("Can only unexport a server object.");
/*     */     }
/*     */ 
/* 187 */     Tie localTie = Util.getTie(paramRemote);
/* 188 */     if (localTie != null) {
/* 189 */       Util.unexportObject(paramRemote);
/*     */     }
/* 191 */     else if (Utility.loadTie(paramRemote) == null)
/* 192 */       UnicastRemoteObject.unexportObject(paramRemote, true);
/*     */     else
/* 194 */       throw new NoSuchObjectException("Object not exported.");
/*     */   }
/*     */ 
/*     */   public java.lang.Object narrow(java.lang.Object paramObject, Class paramClass)
/*     */     throws ClassCastException
/*     */   {
/* 210 */     java.lang.Object localObject1 = null;
/*     */ 
/* 212 */     if (paramObject == null) {
/* 213 */       return null;
/*     */     }
/* 215 */     if (paramClass == null)
/* 216 */       throw new NullPointerException("invalid argument");
/*     */     java.lang.Object localObject2;
/*     */     try {
/* 219 */       if (paramClass.isAssignableFrom(paramObject.getClass())) {
/* 220 */         return paramObject;
/*     */       }
/*     */ 
/* 224 */       if ((paramClass.isInterface()) && (paramClass != Serializable.class) && (paramClass != Externalizable.class))
/*     */       {
/* 228 */         org.omg.CORBA.Object localObject = (org.omg.CORBA.Object)paramObject;
/*     */ 
/* 232 */         localObject2 = RepositoryId.createForAnyType(paramClass);
/*     */ 
/* 234 */         if (localObject._is_a((String)localObject2)) {
/* 235 */           return Utility.loadStub(localObject, paramClass);
/*     */         }
/* 237 */         throw new ClassCastException("Object is not of remote type " + paramClass.getName());
/*     */       }
/*     */ 
/* 241 */       throw new ClassCastException("Class " + paramClass.getName() + " is not a valid remote interface");
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 245 */       localObject2 = new ClassCastException();
/* 246 */       ((ClassCastException)localObject2).initCause(localException);
/* 247 */     }throw ((Throwable)localObject2);
/*     */   }
/*     */ 
/*     */   public void connect(Remote paramRemote1, Remote paramRemote2)
/*     */     throws RemoteException
/*     */   {
/* 266 */     if ((paramRemote1 == null) || (paramRemote2 == null)) {
/* 267 */       throw new NullPointerException("invalid argument");
/*     */     }
/*     */ 
/* 270 */     ORB localORB1 = null;
/*     */     try {
/* 272 */       if (StubAdapter.isStub(paramRemote2)) {
/* 273 */         localORB1 = StubAdapter.getORB(paramRemote2);
/*     */       }
/*     */       else {
/* 276 */         Tie localTie1 = Util.getTie(paramRemote2);
/* 277 */         if (localTie1 != null)
/*     */         {
/* 289 */           localORB1 = localTie1.orb();
/*     */         }
/*     */       }
/*     */     } catch (SystemException localSystemException1) {
/* 293 */       throw new RemoteException("'source' object not connected", localSystemException1);
/*     */     }
/*     */ 
/* 296 */     int i = 0;
/* 297 */     Tie localTie2 = null;
/* 298 */     if (StubAdapter.isStub(paramRemote1)) {
/* 299 */       i = 1;
/*     */     } else {
/* 301 */       localTie2 = Util.getTie(paramRemote1);
/* 302 */       if (localTie2 != null) {
/* 303 */         i = 1;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 313 */     if (i == 0)
/*     */     {
/* 318 */       if (localORB1 != null) {
/* 319 */         throw new RemoteException("'source' object exported to IIOP, 'target' is JRMP");
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 325 */       if (localORB1 == null) {
/* 326 */         throw new RemoteException("'source' object is JRMP, 'target' is IIOP");
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 332 */         if (localTie2 != null)
/*     */           try
/*     */           {
/* 335 */             ORB localORB2 = localTie2.orb();
/*     */ 
/* 338 */             if (localORB2 == localORB1)
/*     */             {
/* 341 */               return;
/*     */             }
/*     */ 
/* 344 */             throw new RemoteException("'target' object was already connected");
/*     */           }
/*     */           catch (SystemException localSystemException2)
/*     */           {
/* 350 */             localTie2.orb(localORB1);
/*     */           }
/* 352 */         else StubAdapter.connect(paramRemote1, localORB1);
/*     */ 
/*     */       }
/*     */       catch (SystemException localSystemException3)
/*     */       {
/* 357 */         throw new RemoteException("'target' object was already connected", localSystemException3);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.javax.rmi.PortableRemoteObject
 * JD-Core Version:    0.6.2
 */
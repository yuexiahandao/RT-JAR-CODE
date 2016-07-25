/*     */ package com.sun.corba.se.impl.javax.rmi.CORBA;
/*     */ 
/*     */ import com.sun.corba.se.impl.ior.StubIORImpl;
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.impl.presentation.rmi.StubConnectImpl;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.rmi.RemoteException;
/*     */ import javax.rmi.CORBA.Stub;
/*     */ import javax.rmi.CORBA.StubDelegate;
/*     */ import org.omg.CORBA.ORB;
/*     */ 
/*     */ public class StubDelegateImpl
/*     */   implements StubDelegate
/*     */ {
/*  65 */   static UtilSystemException wrapper = UtilSystemException.get("rmiiiop");
/*     */   private StubIORImpl ior;
/*     */ 
/*     */   public StubIORImpl getIOR()
/*     */   {
/*  72 */     return this.ior;
/*     */   }
/*     */ 
/*     */   public StubDelegateImpl()
/*     */   {
/*  77 */     this.ior = null;
/*     */   }
/*     */ 
/*     */   private void init(Stub paramStub)
/*     */   {
/*  87 */     if (this.ior == null)
/*  88 */       this.ior = new StubIORImpl(paramStub);
/*     */   }
/*     */ 
/*     */   public int hashCode(Stub paramStub)
/*     */   {
/*  98 */     init(paramStub);
/*  99 */     return this.ior.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Stub paramStub, Object paramObject)
/*     */   {
/* 111 */     if (paramStub == paramObject) {
/* 112 */       return true;
/*     */     }
/*     */ 
/* 115 */     if (!(paramObject instanceof Stub)) {
/* 116 */       return false;
/*     */     }
/*     */ 
/* 121 */     Stub localStub = (Stub)paramObject;
/* 122 */     if (localStub.hashCode() != paramStub.hashCode()) {
/* 123 */       return false;
/*     */     }
/*     */ 
/* 131 */     return paramStub.toString().equals(localStub.toString());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 136 */     if (this == paramObject) {
/* 137 */       return true;
/*     */     }
/* 139 */     if (!(paramObject instanceof StubDelegateImpl)) {
/* 140 */       return false;
/*     */     }
/* 142 */     StubDelegateImpl localStubDelegateImpl = (StubDelegateImpl)paramObject;
/*     */ 
/* 144 */     if (this.ior == null) {
/* 145 */       return this.ior == localStubDelegateImpl.ior;
/*     */     }
/* 147 */     return this.ior.equals(localStubDelegateImpl.ior);
/*     */   }
/*     */ 
/*     */   public String toString(Stub paramStub)
/*     */   {
/* 157 */     if (this.ior == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     return this.ior.toString();
/*     */   }
/*     */ 
/*     */   public void connect(Stub paramStub, ORB paramORB)
/*     */     throws RemoteException
/*     */   {
/* 177 */     this.ior = StubConnectImpl.connect(this.ior, paramStub, paramStub, paramORB);
/*     */   }
/*     */ 
/*     */   public void readObject(Stub paramStub, ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 186 */     if (this.ior == null) {
/* 187 */       this.ior = new StubIORImpl();
/*     */     }
/* 189 */     this.ior.doRead(paramObjectInputStream);
/*     */   }
/*     */ 
/*     */   public void writeObject(Stub paramStub, ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 203 */     init(paramStub);
/* 204 */     this.ior.doWrite(paramObjectOutputStream);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.javax.rmi.CORBA.StubDelegateImpl
 * JD-Core Version:    0.6.2
 */
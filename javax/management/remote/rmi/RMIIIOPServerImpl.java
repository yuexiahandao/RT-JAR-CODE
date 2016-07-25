/*     */ package javax.management.remote.rmi;
/*     */ 
/*     */ import com.sun.jmx.remote.internal.IIOPHelper;
/*     */ import java.io.IOException;
/*     */ import java.rmi.Remote;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.Subject;
/*     */ 
/*     */ public class RMIIIOPServerImpl extends RMIServerImpl
/*     */ {
/*     */   private final Map<String, ?> env;
/*     */   private final AccessControlContext callerACC;
/*     */ 
/*     */   public RMIIIOPServerImpl(Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/*  61 */     super(paramMap);
/*     */ 
/*  63 */     this.env = (paramMap == null ? Collections.emptyMap() : paramMap);
/*     */ 
/*  65 */     this.callerACC = AccessController.getContext();
/*     */   }
/*     */ 
/*     */   protected void export() throws IOException {
/*  69 */     IIOPHelper.exportObject(this);
/*     */   }
/*     */ 
/*     */   protected String getProtocol() {
/*  73 */     return "iiop";
/*     */   }
/*     */ 
/*     */   public Remote toStub()
/*     */     throws IOException
/*     */   {
/*  87 */     Remote localRemote = IIOPHelper.toStub(this);
/*     */ 
/*  93 */     return localRemote;
/*     */   }
/*     */ 
/*     */   protected RMIConnection makeClient(String paramString, Subject paramSubject)
/*     */     throws IOException
/*     */   {
/* 115 */     if (paramString == null) {
/* 116 */       throw new NullPointerException("Null connectionId");
/*     */     }
/* 118 */     RMIConnectionImpl localRMIConnectionImpl = new RMIConnectionImpl(this, paramString, getDefaultClassLoader(), paramSubject, this.env);
/*     */ 
/* 121 */     IIOPHelper.exportObject(localRMIConnectionImpl);
/* 122 */     return localRMIConnectionImpl;
/*     */   }
/*     */ 
/*     */   protected void closeClient(RMIConnection paramRMIConnection) throws IOException {
/* 126 */     IIOPHelper.unexportObject(paramRMIConnection);
/*     */   }
/*     */ 
/*     */   protected void closeServer()
/*     */     throws IOException
/*     */   {
/* 138 */     IIOPHelper.unexportObject(this);
/*     */   }
/*     */ 
/*     */   RMIConnection doNewClient(final Object paramObject) throws IOException
/*     */   {
/* 143 */     if (this.callerACC == null)
/* 144 */       throw new SecurityException("AccessControlContext cannot be null");
/*     */     try
/*     */     {
/* 147 */       return (RMIConnection)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public RMIConnection run() throws IOException {
/* 150 */           return RMIIIOPServerImpl.this.superDoNewClient(paramObject);
/*     */         }
/*     */       }
/*     */       , this.callerACC);
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException)
/*     */     {
/* 154 */       throw ((IOException)localPrivilegedActionException.getCause());
/*     */     }
/*     */   }
/*     */ 
/*     */   RMIConnection superDoNewClient(Object paramObject) throws IOException {
/* 159 */     return super.doNewClient(paramObject);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIIIOPServerImpl
 * JD-Core Version:    0.6.2
 */
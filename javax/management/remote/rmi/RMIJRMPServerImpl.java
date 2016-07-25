/*     */ package javax.management.remote.rmi;
/*     */ 
/*     */ import com.sun.jmx.remote.internal.RMIExporter;
/*     */ import com.sun.jmx.remote.util.EnvHelp;
/*     */ import java.io.IOException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.RemoteObject;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import javax.security.auth.Subject;
/*     */ import sun.rmi.server.UnicastServerRef;
/*     */ import sun.rmi.server.UnicastServerRef2;
/*     */ 
/*     */ public class RMIJRMPServerImpl extends RMIServerImpl
/*     */ {
/*     */   private final int port;
/*     */   private final RMIClientSocketFactory csf;
/*     */   private final RMIServerSocketFactory ssf;
/*     */   private final Map<String, ?> env;
/*     */ 
/*     */   public RMIJRMPServerImpl(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory, Map<String, ?> paramMap)
/*     */     throws IOException
/*     */   {
/*  83 */     super(paramMap);
/*     */ 
/*  85 */     if (paramInt < 0) {
/*  86 */       throw new IllegalArgumentException("Negative port: " + paramInt);
/*     */     }
/*  88 */     this.port = paramInt;
/*  89 */     this.csf = paramRMIClientSocketFactory;
/*  90 */     this.ssf = paramRMIServerSocketFactory;
/*  91 */     this.env = (paramMap == null ? Collections.emptyMap() : paramMap);
/*     */   }
/*     */ 
/*     */   protected void export() throws IOException {
/*  95 */     export(this);
/*     */   }
/*     */ 
/*     */   private void export(Remote paramRemote) throws RemoteException {
/*  99 */     RMIExporter localRMIExporter = (RMIExporter)this.env.get("com.sun.jmx.remote.rmi.exporter");
/*     */ 
/* 101 */     boolean bool = EnvHelp.isServerDaemon(this.env);
/*     */ 
/* 103 */     if ((bool) && (localRMIExporter != null)) {
/* 104 */       throw new IllegalArgumentException("If jmx.remote.x.daemon is specified as true, com.sun.jmx.remote.rmi.exporter cannot be used to specify an exporter!");
/*     */     }
/*     */ 
/* 109 */     if (bool) {
/* 110 */       if ((this.csf == null) && (this.ssf == null))
/* 111 */         new UnicastServerRef(this.port).exportObject(paramRemote, null, true);
/*     */       else
/* 113 */         new UnicastServerRef2(this.port, this.csf, this.ssf).exportObject(paramRemote, null, true);
/*     */     }
/* 115 */     else if (localRMIExporter != null)
/* 116 */       localRMIExporter.exportObject(paramRemote, this.port, this.csf, this.ssf);
/*     */     else
/* 118 */       UnicastRemoteObject.exportObject(paramRemote, this.port, this.csf, this.ssf);
/*     */   }
/*     */ 
/*     */   private void unexport(Remote paramRemote, boolean paramBoolean)
/*     */     throws NoSuchObjectException
/*     */   {
/* 124 */     RMIExporter localRMIExporter = (RMIExporter)this.env.get("com.sun.jmx.remote.rmi.exporter");
/*     */ 
/* 126 */     if (localRMIExporter == null)
/* 127 */       UnicastRemoteObject.unexportObject(paramRemote, paramBoolean);
/*     */     else
/* 129 */       localRMIExporter.unexportObject(paramRemote, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected String getProtocol() {
/* 133 */     return "rmi";
/*     */   }
/*     */ 
/*     */   public Remote toStub()
/*     */     throws IOException
/*     */   {
/* 145 */     return RemoteObject.toStub(this);
/*     */   }
/*     */ 
/*     */   protected RMIConnection makeClient(String paramString, Subject paramSubject)
/*     */     throws IOException
/*     */   {
/* 169 */     if (paramString == null) {
/* 170 */       throw new NullPointerException("Null connectionId");
/*     */     }
/* 172 */     RMIConnectionImpl localRMIConnectionImpl = new RMIConnectionImpl(this, paramString, getDefaultClassLoader(), paramSubject, this.env);
/*     */ 
/* 175 */     export(localRMIConnectionImpl);
/* 176 */     return localRMIConnectionImpl;
/*     */   }
/*     */ 
/*     */   protected void closeClient(RMIConnection paramRMIConnection) throws IOException {
/* 180 */     unexport(paramRMIConnection, true);
/*     */   }
/*     */ 
/*     */   protected void closeServer()
/*     */     throws IOException
/*     */   {
/* 192 */     unexport(this, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.rmi.RMIJRMPServerImpl
 * JD-Core Version:    0.6.2
 */
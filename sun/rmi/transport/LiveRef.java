/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.util.Arrays;
/*     */ import sun.rmi.transport.tcp.TCPEndpoint;
/*     */ 
/*     */ public class LiveRef
/*     */   implements Cloneable
/*     */ {
/*     */   private final Endpoint ep;
/*     */   private final ObjID id;
/*     */   private transient Channel ch;
/*     */   private final boolean isLocal;
/*     */ 
/*     */   public LiveRef(ObjID paramObjID, Endpoint paramEndpoint, boolean paramBoolean)
/*     */   {
/*  64 */     this.ep = paramEndpoint;
/*  65 */     this.id = paramObjID;
/*  66 */     this.isLocal = paramBoolean;
/*     */   }
/*     */ 
/*     */   public LiveRef(int paramInt)
/*     */   {
/*  74 */     this(new ObjID(), paramInt);
/*     */   }
/*     */ 
/*     */   public LiveRef(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */   {
/*  85 */     this(new ObjID(), paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory);
/*     */   }
/*     */ 
/*     */   public LiveRef(ObjID paramObjID, int paramInt)
/*     */   {
/*  93 */     this(paramObjID, TCPEndpoint.getLocalEndpoint(paramInt), true);
/*     */   }
/*     */ 
/*     */   public LiveRef(ObjID paramObjID, int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*     */   {
/* 103 */     this(paramObjID, TCPEndpoint.getLocalEndpoint(paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory), true);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 111 */       return (LiveRef)super.clone();
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 114 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 122 */     return ((TCPEndpoint)this.ep).getPort();
/*     */   }
/*     */ 
/*     */   public RMIClientSocketFactory getClientSocketFactory()
/*     */   {
/* 133 */     return ((TCPEndpoint)this.ep).getClientSocketFactory();
/*     */   }
/*     */ 
/*     */   public RMIServerSocketFactory getServerSocketFactory()
/*     */   {
/* 140 */     return ((TCPEndpoint)this.ep).getServerSocketFactory();
/*     */   }
/*     */ 
/*     */   public void exportObject(Target paramTarget)
/*     */     throws RemoteException
/*     */   {
/* 147 */     this.ep.exportObject(paramTarget);
/*     */   }
/*     */ 
/*     */   public Channel getChannel() throws RemoteException {
/* 151 */     if (this.ch == null) {
/* 152 */       this.ch = this.ep.getChannel();
/*     */     }
/* 154 */     return this.ch;
/*     */   }
/*     */ 
/*     */   public ObjID getObjID() {
/* 158 */     return this.id;
/*     */   }
/*     */ 
/*     */   Endpoint getEndpoint() {
/* 162 */     return this.ep;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     String str;
/* 168 */     if (this.isLocal)
/* 169 */       str = "local";
/*     */     else
/* 171 */       str = "remote";
/* 172 */     return "[endpoint:" + this.ep + "(" + str + ")," + "objID:" + this.id + "]";
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 177 */     return this.id.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 181 */     if ((paramObject != null) && ((paramObject instanceof LiveRef))) {
/* 182 */       LiveRef localLiveRef = (LiveRef)paramObject;
/*     */ 
/* 184 */       return (this.ep.equals(localLiveRef.ep)) && (this.id.equals(localLiveRef.id)) && (this.isLocal == localLiveRef.isLocal);
/*     */     }
/*     */ 
/* 187 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean remoteEquals(Object paramObject)
/*     */   {
/* 192 */     if ((paramObject != null) && ((paramObject instanceof LiveRef))) {
/* 193 */       LiveRef localLiveRef = (LiveRef)paramObject;
/*     */ 
/* 195 */       TCPEndpoint localTCPEndpoint1 = (TCPEndpoint)this.ep;
/* 196 */       TCPEndpoint localTCPEndpoint2 = (TCPEndpoint)localLiveRef.ep;
/*     */ 
/* 198 */       RMIClientSocketFactory localRMIClientSocketFactory1 = localTCPEndpoint1.getClientSocketFactory();
/*     */ 
/* 200 */       RMIClientSocketFactory localRMIClientSocketFactory2 = localTCPEndpoint2.getClientSocketFactory();
/*     */ 
/* 211 */       if ((localTCPEndpoint1.getPort() != localTCPEndpoint2.getPort()) || (!localTCPEndpoint1.getHost().equals(localTCPEndpoint2.getHost())))
/*     */       {
/* 214 */         return false;
/*     */       }
/* 216 */       if (((localRMIClientSocketFactory1 == null ? 1 : 0) ^ (localRMIClientSocketFactory2 == null ? 1 : 0)) != 0) {
/* 217 */         return false;
/*     */       }
/* 219 */       if ((localRMIClientSocketFactory1 != null) && ((localRMIClientSocketFactory1.getClass() != localRMIClientSocketFactory2.getClass()) || (!localRMIClientSocketFactory1.equals(localRMIClientSocketFactory2))))
/*     */       {
/* 224 */         return false;
/*     */       }
/* 226 */       return this.id.equals(localLiveRef.id);
/*     */     }
/* 228 */     return false;
/*     */   }
/*     */ 
/*     */   public void write(ObjectOutput paramObjectOutput, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 235 */     boolean bool = false;
/* 236 */     if ((paramObjectOutput instanceof ConnectionOutputStream)) {
/* 237 */       ConnectionOutputStream localConnectionOutputStream = (ConnectionOutputStream)paramObjectOutput;
/* 238 */       bool = localConnectionOutputStream.isResultStream();
/*     */ 
/* 256 */       if (this.isLocal) {
/* 257 */         ObjectEndpoint localObjectEndpoint = new ObjectEndpoint(this.id, this.ep.getInboundTransport());
/*     */ 
/* 259 */         Target localTarget = ObjectTable.getTarget(localObjectEndpoint);
/*     */ 
/* 261 */         if (localTarget != null) {
/* 262 */           Remote localRemote = localTarget.getImpl();
/* 263 */           if (localRemote != null)
/* 264 */             localConnectionOutputStream.saveObject(localRemote);
/*     */         }
/*     */       }
/*     */       else {
/* 268 */         localConnectionOutputStream.saveObject(this);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 274 */     if (paramBoolean)
/* 275 */       ((TCPEndpoint)this.ep).write(paramObjectOutput);
/*     */     else {
/* 277 */       ((TCPEndpoint)this.ep).writeHostPortFormat(paramObjectOutput);
/*     */     }
/* 279 */     this.id.write(paramObjectOutput);
/* 280 */     paramObjectOutput.writeBoolean(bool);
/*     */   }
/*     */ 
/*     */   public static LiveRef read(ObjectInput paramObjectInput, boolean paramBoolean)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/*     */     TCPEndpoint localTCPEndpoint;
/* 291 */     if (paramBoolean)
/* 292 */       localTCPEndpoint = TCPEndpoint.read(paramObjectInput);
/*     */     else {
/* 294 */       localTCPEndpoint = TCPEndpoint.readHostPortFormat(paramObjectInput);
/*     */     }
/* 296 */     ObjID localObjID = ObjID.read(paramObjectInput);
/* 297 */     boolean bool = paramObjectInput.readBoolean();
/*     */ 
/* 299 */     LiveRef localLiveRef = new LiveRef(localObjID, localTCPEndpoint, false);
/*     */ 
/* 301 */     if ((paramObjectInput instanceof ConnectionInputStream)) {
/* 302 */       ConnectionInputStream localConnectionInputStream = (ConnectionInputStream)paramObjectInput;
/*     */ 
/* 305 */       localConnectionInputStream.saveRef(localLiveRef);
/* 306 */       if (bool)
/*     */       {
/* 309 */         localConnectionInputStream.setAckNeeded();
/*     */       }
/*     */     } else {
/* 312 */       DGCClient.registerRefs(localTCPEndpoint, Arrays.asList(new LiveRef[] { localLiveRef }));
/*     */     }
/*     */ 
/* 315 */     return localLiveRef;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.LiveRef
 * JD-Core Version:    0.6.2
 */
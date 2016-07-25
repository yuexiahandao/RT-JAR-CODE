/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.server.UID;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.server.MarshalInputStream;
/*     */ 
/*     */ class ConnectionInputStream extends MarshalInputStream
/*     */ {
/*  43 */   private boolean dgcAckNeeded = false;
/*     */ 
/*  46 */   private Map<Endpoint, List<LiveRef>> incomingRefTable = new HashMap(5);
/*     */   private UID ackID;
/*     */ 
/*     */   ConnectionInputStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  56 */     super(paramInputStream);
/*     */   }
/*     */ 
/*     */   void readID() throws IOException {
/*  60 */     this.ackID = UID.read(this);
/*     */   }
/*     */ 
/*     */   void saveRef(LiveRef paramLiveRef)
/*     */   {
/*  70 */     Endpoint localEndpoint = paramLiveRef.getEndpoint();
/*     */ 
/*  73 */     Object localObject = (List)this.incomingRefTable.get(localEndpoint);
/*     */ 
/*  75 */     if (localObject == null) {
/*  76 */       localObject = new ArrayList();
/*  77 */       this.incomingRefTable.put(localEndpoint, localObject);
/*     */     }
/*     */ 
/*  81 */     ((List)localObject).add(paramLiveRef);
/*     */   }
/*     */ 
/*     */   void registerRefs()
/*     */     throws IOException
/*     */   {
/*  91 */     if (!this.incomingRefTable.isEmpty())
/*     */     {
/*  93 */       for (Map.Entry localEntry : this.incomingRefTable.entrySet())
/*  94 */         DGCClient.registerRefs((Endpoint)localEntry.getKey(), (List)localEntry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   void setAckNeeded()
/*     */   {
/* 104 */     this.dgcAckNeeded = true;
/*     */   }
/*     */ 
/*     */   void done(Connection paramConnection)
/*     */   {
/* 117 */     if (this.dgcAckNeeded) {
/* 118 */       Connection localConnection = null;
/* 119 */       Channel localChannel = null;
/* 120 */       boolean bool = true;
/*     */ 
/* 122 */       DGCImpl.dgcLog.log(Log.VERBOSE, "send ack");
/*     */       try
/*     */       {
/* 125 */         localChannel = paramConnection.getChannel();
/* 126 */         localConnection = localChannel.newConnection();
/* 127 */         DataOutputStream localDataOutputStream = new DataOutputStream(localConnection.getOutputStream());
/*     */ 
/* 129 */         localDataOutputStream.writeByte(84);
/* 130 */         if (this.ackID == null) {
/* 131 */           this.ackID = new UID();
/*     */         }
/* 133 */         this.ackID.write(localDataOutputStream);
/* 134 */         localConnection.releaseOutputStream();
/*     */ 
/* 143 */         localConnection.getInputStream().available();
/* 144 */         localConnection.releaseInputStream();
/*     */       } catch (RemoteException localRemoteException1) {
/* 146 */         bool = false;
/*     */       } catch (IOException localIOException) {
/* 148 */         bool = false;
/*     */       }
/*     */       try {
/* 151 */         if (localConnection != null)
/* 152 */           localChannel.free(localConnection, bool);
/*     */       }
/*     */       catch (RemoteException localRemoteException2)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.ConnectionInputStream
 * JD-Core Version:    0.6.2
 */
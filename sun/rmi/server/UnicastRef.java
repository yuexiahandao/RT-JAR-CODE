/*     */ package sun.rmi.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.UnmarshalException;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.Operation;
/*     */ import java.rmi.server.RemoteCall;
/*     */ import java.rmi.server.RemoteObject;
/*     */ import java.rmi.server.RemoteRef;
/*     */ import java.security.AccessController;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.transport.Channel;
/*     */ import sun.rmi.transport.Connection;
/*     */ import sun.rmi.transport.LiveRef;
/*     */ import sun.rmi.transport.StreamRemoteCall;
/*     */ import sun.security.action.GetBooleanAction;
/*     */ 
/*     */ public class UnicastRef
/*     */   implements RemoteRef
/*     */ {
/*  58 */   public static final Log clientRefLog = Log.getLog("sun.rmi.client.ref", "transport", Util.logLevel);
/*     */ 
/*  64 */   public static final Log clientCallLog = Log.getLog("sun.rmi.client.call", "RMI", ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.rmi.client.logCalls"))).booleanValue());
/*     */   private static final long serialVersionUID = 8258372400816541186L;
/*     */   protected LiveRef ref;
/*     */ 
/*     */   public UnicastRef()
/*     */   {
/*     */   }
/*     */ 
/*     */   public UnicastRef(LiveRef paramLiveRef)
/*     */   {
/*  82 */     this.ref = paramLiveRef;
/*     */   }
/*     */ 
/*     */   public LiveRef getLiveRef()
/*     */   {
/*  94 */     return this.ref;
/*     */   }
/*     */ 
/*     */   public Object invoke(Remote paramRemote, Method paramMethod, Object[] paramArrayOfObject, long paramLong)
/*     */     throws Exception
/*     */   {
/* 121 */     if (clientRefLog.isLoggable(Log.VERBOSE)) {
/* 122 */       clientRefLog.log(Log.VERBOSE, "method: " + paramMethod);
/*     */     }
/*     */ 
/* 125 */     if (clientCallLog.isLoggable(Log.VERBOSE)) {
/* 126 */       logClientCall(paramRemote, paramMethod);
/*     */     }
/*     */ 
/* 129 */     Connection localConnection = this.ref.getChannel().newConnection();
/* 130 */     StreamRemoteCall localStreamRemoteCall = null;
/* 131 */     boolean bool = true;
/*     */ 
/* 136 */     int i = 0;
/*     */     try
/*     */     {
/* 139 */       if (clientRefLog.isLoggable(Log.VERBOSE)) {
/* 140 */         clientRefLog.log(Log.VERBOSE, "opnum = " + paramLong);
/*     */       }
/*     */ 
/* 144 */       localStreamRemoteCall = new StreamRemoteCall(localConnection, this.ref.getObjID(), -1, paramLong);
/*     */       Object localObject1;
/*     */       try
/*     */       {
/* 148 */         ObjectOutput localObjectOutput = localStreamRemoteCall.getOutputStream();
/* 149 */         marshalCustomCallData(localObjectOutput);
/* 150 */         localObject1 = paramMethod.getParameterTypes();
/* 151 */         for (int j = 0; j < localObject1.length; j++)
/* 152 */           marshalValue(localObject1[j], paramArrayOfObject[j], localObjectOutput);
/*     */       }
/*     */       catch (IOException localIOException1) {
/* 155 */         clientRefLog.log(Log.BRIEF, "IOException marshalling arguments: ", localIOException1);
/*     */ 
/* 157 */         throw new MarshalException("error marshalling arguments", localIOException1);
/*     */       }
/*     */ 
/* 161 */       localStreamRemoteCall.executeCall();
/*     */       try
/*     */       {
/* 164 */         Class localClass = paramMethod.getReturnType();
/* 165 */         if (localClass == Void.TYPE) {
/* 166 */           localObject1 = null;
/*     */           try
/*     */           {
/* 200 */             localStreamRemoteCall.done();
/*     */           }
/*     */           catch (IOException localIOException3)
/*     */           {
/* 208 */             bool = false;
/*     */           }
/*     */ 
/* 256 */           return localObject1;
/*     */         }
/* 167 */         localObject1 = localStreamRemoteCall.getInputStream();
/*     */ 
/* 174 */         Object localObject2 = unmarshalValue(localClass, (ObjectInput)localObject1);
/*     */ 
/* 179 */         i = 1;
/*     */ 
/* 182 */         clientRefLog.log(Log.BRIEF, "free connection (reuse = true)");
/*     */ 
/* 185 */         this.ref.getChannel().free(localConnection, true);
/*     */ 
/* 187 */         Object localObject3 = localObject2;
/*     */         try
/*     */         {
/* 200 */           localStreamRemoteCall.done();
/*     */         }
/*     */         catch (IOException localIOException4)
/*     */         {
/* 208 */           bool = false;
/*     */         }
/*     */ 
/* 256 */         return localObject3;
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/* 190 */         clientRefLog.log(Log.BRIEF, "IOException unmarshalling return: ", localIOException2);
/*     */ 
/* 192 */         throw new UnmarshalException("error unmarshalling return", localIOException2);
/*     */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 194 */         clientRefLog.log(Log.BRIEF, "ClassNotFoundException unmarshalling return: ", localClassNotFoundException);
/*     */ 
/* 197 */         throw new UnmarshalException("error unmarshalling return", localClassNotFoundException);
/*     */       } finally {
/*     */         try {
/* 200 */           localStreamRemoteCall.done();
/*     */         }
/*     */         catch (IOException localIOException5)
/*     */         {
/* 208 */           bool = false;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 221 */       if ((localStreamRemoteCall == null) || (((StreamRemoteCall)localStreamRemoteCall).getServerException() != localRuntimeException))
/*     */       {
/* 224 */         bool = false;
/*     */       }
/* 226 */       throw localRuntimeException;
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/* 236 */       bool = false;
/* 237 */       throw localRemoteException;
/*     */     }
/*     */     catch (Error localError)
/*     */     {
/* 243 */       bool = false;
/* 244 */       throw localError;
/*     */     }
/*     */     finally
/*     */     {
/* 251 */       if (i == 0) {
/* 252 */         if (clientRefLog.isLoggable(Log.BRIEF)) {
/* 253 */           clientRefLog.log(Log.BRIEF, "free connection (reuse = " + bool + ")");
/*     */         }
/*     */ 
/* 256 */         this.ref.getChannel().free(localConnection, bool);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void marshalCustomCallData(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected static void marshalValue(Class<?> paramClass, Object paramObject, ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/* 272 */     if (paramClass.isPrimitive()) {
/* 273 */       if (paramClass == Integer.TYPE)
/* 274 */         paramObjectOutput.writeInt(((Integer)paramObject).intValue());
/* 275 */       else if (paramClass == Boolean.TYPE)
/* 276 */         paramObjectOutput.writeBoolean(((Boolean)paramObject).booleanValue());
/* 277 */       else if (paramClass == Byte.TYPE)
/* 278 */         paramObjectOutput.writeByte(((Byte)paramObject).byteValue());
/* 279 */       else if (paramClass == Character.TYPE)
/* 280 */         paramObjectOutput.writeChar(((Character)paramObject).charValue());
/* 281 */       else if (paramClass == Short.TYPE)
/* 282 */         paramObjectOutput.writeShort(((Short)paramObject).shortValue());
/* 283 */       else if (paramClass == Long.TYPE)
/* 284 */         paramObjectOutput.writeLong(((Long)paramObject).longValue());
/* 285 */       else if (paramClass == Float.TYPE)
/* 286 */         paramObjectOutput.writeFloat(((Float)paramObject).floatValue());
/* 287 */       else if (paramClass == Double.TYPE)
/* 288 */         paramObjectOutput.writeDouble(((Double)paramObject).doubleValue());
/*     */       else
/* 290 */         throw new Error("Unrecognized primitive type: " + paramClass);
/*     */     }
/*     */     else
/* 293 */       paramObjectOutput.writeObject(paramObject);
/*     */   }
/*     */ 
/*     */   protected static Object unmarshalValue(Class<?> paramClass, ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 304 */     if (paramClass.isPrimitive()) {
/* 305 */       if (paramClass == Integer.TYPE)
/* 306 */         return Integer.valueOf(paramObjectInput.readInt());
/* 307 */       if (paramClass == Boolean.TYPE)
/* 308 */         return Boolean.valueOf(paramObjectInput.readBoolean());
/* 309 */       if (paramClass == Byte.TYPE)
/* 310 */         return Byte.valueOf(paramObjectInput.readByte());
/* 311 */       if (paramClass == Character.TYPE)
/* 312 */         return Character.valueOf(paramObjectInput.readChar());
/* 313 */       if (paramClass == Short.TYPE)
/* 314 */         return Short.valueOf(paramObjectInput.readShort());
/* 315 */       if (paramClass == Long.TYPE)
/* 316 */         return Long.valueOf(paramObjectInput.readLong());
/* 317 */       if (paramClass == Float.TYPE)
/* 318 */         return Float.valueOf(paramObjectInput.readFloat());
/* 319 */       if (paramClass == Double.TYPE) {
/* 320 */         return Double.valueOf(paramObjectInput.readDouble());
/*     */       }
/* 322 */       throw new Error("Unrecognized primitive type: " + paramClass);
/*     */     }
/*     */ 
/* 325 */     return paramObjectInput.readObject();
/*     */   }
/*     */ 
/*     */   public RemoteCall newCall(RemoteObject paramRemoteObject, Operation[] paramArrayOfOperation, int paramInt, long paramLong)
/*     */     throws RemoteException
/*     */   {
/* 339 */     clientRefLog.log(Log.BRIEF, "get connection");
/*     */ 
/* 341 */     Connection localConnection = this.ref.getChannel().newConnection();
/*     */     try {
/* 343 */       clientRefLog.log(Log.VERBOSE, "create call context");
/*     */ 
/* 346 */       if (clientCallLog.isLoggable(Log.VERBOSE)) {
/* 347 */         logClientCall(paramRemoteObject, paramArrayOfOperation[paramInt]);
/*     */       }
/*     */ 
/* 350 */       StreamRemoteCall localStreamRemoteCall = new StreamRemoteCall(localConnection, this.ref.getObjID(), paramInt, paramLong);
/*     */       try
/*     */       {
/* 353 */         marshalCustomCallData(localStreamRemoteCall.getOutputStream());
/*     */       } catch (IOException localIOException) {
/* 355 */         throw new MarshalException("error marshaling custom call data");
/*     */       }
/*     */ 
/* 358 */       return localStreamRemoteCall;
/*     */     } catch (RemoteException localRemoteException) {
/* 360 */       this.ref.getChannel().free(localConnection, false);
/* 361 */       throw localRemoteException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void invoke(RemoteCall paramRemoteCall)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 376 */       clientRefLog.log(Log.VERBOSE, "execute call");
/*     */ 
/* 378 */       paramRemoteCall.executeCall();
/*     */     }
/*     */     catch (RemoteException localRemoteException)
/*     */     {
/* 384 */       clientRefLog.log(Log.BRIEF, "exception: ", localRemoteException);
/* 385 */       free(paramRemoteCall, false);
/* 386 */       throw localRemoteException;
/*     */     }
/*     */     catch (Error localError)
/*     */     {
/* 392 */       clientRefLog.log(Log.BRIEF, "error: ", localError);
/* 393 */       free(paramRemoteCall, false);
/* 394 */       throw localError;
/*     */     }
/*     */     catch (RuntimeException localRuntimeException)
/*     */     {
/* 402 */       clientRefLog.log(Log.BRIEF, "exception: ", localRuntimeException);
/* 403 */       free(paramRemoteCall, false);
/* 404 */       throw localRuntimeException;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 411 */       clientRefLog.log(Log.BRIEF, "exception: ", localException);
/* 412 */       free(paramRemoteCall, true);
/*     */ 
/* 414 */       throw localException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void free(RemoteCall paramRemoteCall, boolean paramBoolean)
/*     */     throws RemoteException
/*     */   {
/* 429 */     Connection localConnection = ((StreamRemoteCall)paramRemoteCall).getConnection();
/* 430 */     this.ref.getChannel().free(localConnection, paramBoolean);
/*     */   }
/*     */ 
/*     */   public void done(RemoteCall paramRemoteCall)
/*     */     throws RemoteException
/*     */   {
/* 444 */     clientRefLog.log(Log.BRIEF, "free connection (reuse = true)");
/*     */ 
/* 447 */     free(paramRemoteCall, true);
/*     */     try
/*     */     {
/* 450 */       paramRemoteCall.done();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void logClientCall(Object paramObject1, Object paramObject2)
/*     */   {
/* 465 */     clientCallLog.log(Log.VERBOSE, "outbound call: " + this.ref + " : " + paramObject1.getClass().getName() + this.ref.getObjID().toString() + ": " + paramObject2);
/*     */   }
/*     */ 
/*     */   public String getRefClass(ObjectOutput paramObjectOutput)
/*     */   {
/* 474 */     return "UnicastRef";
/*     */   }
/*     */ 
/*     */   public void writeExternal(ObjectOutput paramObjectOutput)
/*     */     throws IOException
/*     */   {
/* 481 */     this.ref.write(paramObjectOutput, false);
/*     */   }
/*     */ 
/*     */   public void readExternal(ObjectInput paramObjectInput)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 492 */     this.ref = LiveRef.read(paramObjectInput, false);
/*     */   }
/*     */ 
/*     */   public String remoteToString()
/*     */   {
/* 500 */     return Util.getUnqualifiedName(getClass()) + " [liveRef: " + this.ref + "]";
/*     */   }
/*     */ 
/*     */   public int remoteHashCode()
/*     */   {
/* 507 */     return this.ref.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean remoteEquals(RemoteRef paramRemoteRef)
/*     */   {
/* 513 */     if ((paramRemoteRef instanceof UnicastRef))
/* 514 */       return this.ref.remoteEquals(((UnicastRef)paramRemoteRef).ref);
/* 515 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.UnicastRef
 * JD-Core Version:    0.6.2
 */
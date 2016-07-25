/*     */ package com.sun.corba.se.spi.activation;
/*     */ 
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORBHelper;
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ 
/*     */ public abstract class _ServerManagerImplBase extends ObjectImpl
/*     */   implements ServerManager, InvokeHandler
/*     */ {
/*  20 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 287 */   private static String[] __ids = { "IDL:activation/ServerManager:1.0", "IDL:activation/Activator:1.0", "IDL:activation/Locator:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  41 */     OutputStream localOutputStream = null;
/*  42 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  43 */     if (localInteger == null)
/*  44 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     Object localObject1;
/*     */     Object localObject2;
/*  46 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*     */       try
/*     */       {
/*  53 */         int i = ServerIdHelper.read(paramInputStream);
/*  54 */         localObject1 = ServerHelper.read(paramInputStream);
/*  55 */         active(i, (Server)localObject1);
/*  56 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered1) {
/*  58 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  59 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered1);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/*  69 */         int j = ServerIdHelper.read(paramInputStream);
/*  70 */         localObject1 = ORBidHelper.read(paramInputStream);
/*  71 */         localObject2 = EndpointInfoListHelper.read(paramInputStream);
/*  72 */         registerEndpoints(j, (String)localObject1, (EndPointInfo[])localObject2);
/*  73 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered2) {
/*  75 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  76 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered2);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint1) {
/*  78 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  79 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint1);
/*     */       } catch (ORBAlreadyRegistered localORBAlreadyRegistered) {
/*  81 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  82 */         ORBAlreadyRegisteredHelper.write(localOutputStream, localORBAlreadyRegistered);
/*     */       }
/*     */ 
/*     */     case 2:
/*  91 */       int[] arrayOfInt = null;
/*  92 */       arrayOfInt = getActiveServers();
/*  93 */       localOutputStream = paramResponseHandler.createReply();
/*  94 */       ServerIdsHelper.write(localOutputStream, arrayOfInt);
/*  95 */       break;
/*     */     case 3:
/*     */       try
/*     */       {
/* 103 */         int k = ServerIdHelper.read(paramInputStream);
/* 104 */         activate(k);
/* 105 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerAlreadyActive localServerAlreadyActive) {
/* 107 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 108 */         ServerAlreadyActiveHelper.write(localOutputStream, localServerAlreadyActive);
/*     */       } catch (ServerNotRegistered localServerNotRegistered3) {
/* 110 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 111 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered3);
/*     */       } catch (ServerHeldDown localServerHeldDown1) {
/* 113 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 114 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown1);
/*     */       }
/*     */ 
/*     */     case 4:
/*     */       try
/*     */       {
/* 124 */         int m = ServerIdHelper.read(paramInputStream);
/* 125 */         shutdown(m);
/* 126 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotActive localServerNotActive) {
/* 128 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 129 */         ServerNotActiveHelper.write(localOutputStream, localServerNotActive);
/*     */       } catch (ServerNotRegistered localServerNotRegistered4) {
/* 131 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 132 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered4);
/*     */       }
/*     */ 
/*     */     case 5:
/*     */       try
/*     */       {
/* 142 */         int n = ServerIdHelper.read(paramInputStream);
/* 143 */         install(n);
/* 144 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered5) {
/* 146 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 147 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered5);
/*     */       } catch (ServerHeldDown localServerHeldDown2) {
/* 149 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 150 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown2);
/*     */       } catch (ServerAlreadyInstalled localServerAlreadyInstalled) {
/* 152 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 153 */         ServerAlreadyInstalledHelper.write(localOutputStream, localServerAlreadyInstalled);
/*     */       }
/*     */ 
/*     */     case 6:
/*     */       try
/*     */       {
/* 163 */         int i1 = ServerIdHelper.read(paramInputStream);
/* 164 */         localObject1 = null;
/* 165 */         localObject1 = getORBNames(i1);
/* 166 */         localOutputStream = paramResponseHandler.createReply();
/* 167 */         ORBidListHelper.write(localOutputStream, (String[])localObject1);
/*     */       } catch (ServerNotRegistered localServerNotRegistered6) {
/* 169 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 170 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered6);
/*     */       }
/*     */ 
/*     */     case 7:
/*     */       try
/*     */       {
/* 180 */         int i2 = ServerIdHelper.read(paramInputStream);
/* 181 */         uninstall(i2);
/* 182 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered7) {
/* 184 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 185 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered7);
/*     */       } catch (ServerHeldDown localServerHeldDown3) {
/* 187 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 188 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown3);
/*     */       } catch (ServerAlreadyUninstalled localServerAlreadyUninstalled) {
/* 190 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 191 */         ServerAlreadyUninstalledHelper.write(localOutputStream, localServerAlreadyUninstalled);
/*     */       }
/*     */ 
/*     */     case 8:
/*     */       try
/*     */       {
/* 201 */         int i3 = ServerIdHelper.read(paramInputStream);
/* 202 */         localObject1 = paramInputStream.read_string();
/* 203 */         localObject2 = null;
/* 204 */         localObject2 = locateServer(i3, (String)localObject1);
/* 205 */         localOutputStream = paramResponseHandler.createReply();
/* 206 */         ServerLocationHelper.write(localOutputStream, (ServerLocation)localObject2);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint2) {
/* 208 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 209 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint2);
/*     */       } catch (ServerNotRegistered localServerNotRegistered8) {
/* 211 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 212 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered8);
/*     */       } catch (ServerHeldDown localServerHeldDown4) {
/* 214 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 215 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown4);
/*     */       }
/*     */ 
/*     */     case 9:
/*     */       try
/*     */       {
/* 225 */         int i4 = ServerIdHelper.read(paramInputStream);
/* 226 */         localObject1 = ORBidHelper.read(paramInputStream);
/* 227 */         localObject2 = null;
/* 228 */         localObject2 = locateServerForORB(i4, (String)localObject1);
/* 229 */         localOutputStream = paramResponseHandler.createReply();
/* 230 */         ServerLocationPerORBHelper.write(localOutputStream, (ServerLocationPerORB)localObject2);
/*     */       } catch (InvalidORBid localInvalidORBid) {
/* 232 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 233 */         InvalidORBidHelper.write(localOutputStream, localInvalidORBid);
/*     */       } catch (ServerNotRegistered localServerNotRegistered9) {
/* 235 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 236 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered9);
/*     */       } catch (ServerHeldDown localServerHeldDown5) {
/* 238 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 239 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown5);
/*     */       }
/*     */ 
/*     */     case 10:
/*     */       try
/*     */       {
/* 249 */         String str1 = paramInputStream.read_string();
/* 250 */         int i5 = 0;
/* 251 */         i5 = getEndpoint(str1);
/* 252 */         localOutputStream = paramResponseHandler.createReply();
/* 253 */         localOutputStream.write_long(i5);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint3) {
/* 255 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 256 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint3);
/*     */       }
/*     */ 
/*     */     case 11:
/*     */       try
/*     */       {
/* 266 */         ServerLocationPerORB localServerLocationPerORB = ServerLocationPerORBHelper.read(paramInputStream);
/* 267 */         String str2 = paramInputStream.read_string();
/* 268 */         int i6 = 0;
/* 269 */         i6 = getServerPortForType(localServerLocationPerORB, str2);
/* 270 */         localOutputStream = paramResponseHandler.createReply();
/* 271 */         localOutputStream.write_long(i6);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint4) {
/* 273 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 274 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint4);
/*     */       }
/*     */ 
/*     */     default:
/* 280 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 283 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 294 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  23 */     _methods.put("active", new Integer(0));
/*  24 */     _methods.put("registerEndpoints", new Integer(1));
/*  25 */     _methods.put("getActiveServers", new Integer(2));
/*  26 */     _methods.put("activate", new Integer(3));
/*  27 */     _methods.put("shutdown", new Integer(4));
/*  28 */     _methods.put("install", new Integer(5));
/*  29 */     _methods.put("getORBNames", new Integer(6));
/*  30 */     _methods.put("uninstall", new Integer(7));
/*  31 */     _methods.put("locateServer", new Integer(8));
/*  32 */     _methods.put("locateServerForORB", new Integer(9));
/*  33 */     _methods.put("getEndpoint", new Integer(10));
/*  34 */     _methods.put("getServerPortForType", new Integer(11));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._ServerManagerImplBase
 * JD-Core Version:    0.6.2
 */
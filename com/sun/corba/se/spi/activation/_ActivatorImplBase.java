/*     */ package com.sun.corba.se.spi.activation;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ 
/*     */ public abstract class _ActivatorImplBase extends ObjectImpl
/*     */   implements Activator, InvokeHandler
/*     */ {
/*  20 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 200 */   private static String[] __ids = { "IDL:activation/Activator:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  37 */     OutputStream localOutputStream = null;
/*  38 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  39 */     if (localInteger == null)
/*  40 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     Object localObject;
/*  42 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*     */       try
/*     */       {
/*  49 */         int i = ServerIdHelper.read(paramInputStream);
/*  50 */         localObject = ServerHelper.read(paramInputStream);
/*  51 */         active(i, (Server)localObject);
/*  52 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered1) {
/*  54 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  55 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered1);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/*  65 */         int j = ServerIdHelper.read(paramInputStream);
/*  66 */         localObject = ORBidHelper.read(paramInputStream);
/*  67 */         EndPointInfo[] arrayOfEndPointInfo = EndpointInfoListHelper.read(paramInputStream);
/*  68 */         registerEndpoints(j, (String)localObject, arrayOfEndPointInfo);
/*  69 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered2) {
/*  71 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  72 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered2);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint) {
/*  74 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  75 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint);
/*     */       } catch (ORBAlreadyRegistered localORBAlreadyRegistered) {
/*  77 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  78 */         ORBAlreadyRegisteredHelper.write(localOutputStream, localORBAlreadyRegistered);
/*     */       }
/*     */ 
/*     */     case 2:
/*  87 */       int[] arrayOfInt = null;
/*  88 */       arrayOfInt = getActiveServers();
/*  89 */       localOutputStream = paramResponseHandler.createReply();
/*  90 */       ServerIdsHelper.write(localOutputStream, arrayOfInt);
/*  91 */       break;
/*     */     case 3:
/*     */       try
/*     */       {
/*  99 */         int k = ServerIdHelper.read(paramInputStream);
/* 100 */         activate(k);
/* 101 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerAlreadyActive localServerAlreadyActive) {
/* 103 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 104 */         ServerAlreadyActiveHelper.write(localOutputStream, localServerAlreadyActive);
/*     */       } catch (ServerNotRegistered localServerNotRegistered3) {
/* 106 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 107 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered3);
/*     */       } catch (ServerHeldDown localServerHeldDown1) {
/* 109 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 110 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown1);
/*     */       }
/*     */ 
/*     */     case 4:
/*     */       try
/*     */       {
/* 120 */         int m = ServerIdHelper.read(paramInputStream);
/* 121 */         shutdown(m);
/* 122 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotActive localServerNotActive) {
/* 124 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 125 */         ServerNotActiveHelper.write(localOutputStream, localServerNotActive);
/*     */       } catch (ServerNotRegistered localServerNotRegistered4) {
/* 127 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 128 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered4);
/*     */       }
/*     */ 
/*     */     case 5:
/*     */       try
/*     */       {
/* 138 */         int n = ServerIdHelper.read(paramInputStream);
/* 139 */         install(n);
/* 140 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered5) {
/* 142 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 143 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered5);
/*     */       } catch (ServerHeldDown localServerHeldDown2) {
/* 145 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 146 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown2);
/*     */       } catch (ServerAlreadyInstalled localServerAlreadyInstalled) {
/* 148 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 149 */         ServerAlreadyInstalledHelper.write(localOutputStream, localServerAlreadyInstalled);
/*     */       }
/*     */ 
/*     */     case 6:
/*     */       try
/*     */       {
/* 159 */         int i1 = ServerIdHelper.read(paramInputStream);
/* 160 */         localObject = null;
/* 161 */         localObject = getORBNames(i1);
/* 162 */         localOutputStream = paramResponseHandler.createReply();
/* 163 */         ORBidListHelper.write(localOutputStream, (String[])localObject);
/*     */       } catch (ServerNotRegistered localServerNotRegistered6) {
/* 165 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 166 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered6);
/*     */       }
/*     */ 
/*     */     case 7:
/*     */       try
/*     */       {
/* 176 */         int i2 = ServerIdHelper.read(paramInputStream);
/* 177 */         uninstall(i2);
/* 178 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered7) {
/* 180 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 181 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered7);
/*     */       } catch (ServerHeldDown localServerHeldDown3) {
/* 183 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 184 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown3);
/*     */       } catch (ServerAlreadyUninstalled localServerAlreadyUninstalled) {
/* 186 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 187 */         ServerAlreadyUninstalledHelper.write(localOutputStream, localServerAlreadyUninstalled);
/*     */       }
/*     */ 
/*     */     default:
/* 193 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 196 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 205 */     return (String[])__ids.clone();
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
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._ActivatorImplBase
 * JD-Core Version:    0.6.2
 */
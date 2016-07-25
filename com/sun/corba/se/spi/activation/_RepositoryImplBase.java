/*     */ package com.sun.corba.se.spi.activation;
/*     */ 
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.StringSeqHelper;
/*     */ import java.util.Hashtable;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.InvokeHandler;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.ResponseHandler;
/*     */ 
/*     */ public abstract class _RepositoryImplBase extends ObjectImpl
/*     */   implements Repository, InvokeHandler
/*     */ {
/*  20 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 197 */   private static String[] __ids = { "IDL:activation/Repository:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  38 */     OutputStream localOutputStream = null;
/*  39 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  40 */     if (localInteger == null)
/*  41 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     boolean bool;
/*     */     Object localObject;
/*  43 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*     */       try
/*     */       {
/*  50 */         ServerDef localServerDef1 = ServerDefHelper.read(paramInputStream);
/*  51 */         int i1 = 0;
/*  52 */         i1 = registerServer(localServerDef1);
/*  53 */         localOutputStream = paramResponseHandler.createReply();
/*  54 */         localOutputStream.write_long(i1);
/*     */       } catch (ServerAlreadyRegistered localServerAlreadyRegistered) {
/*  56 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  57 */         ServerAlreadyRegisteredHelper.write(localOutputStream, localServerAlreadyRegistered);
/*     */       } catch (BadServerDefinition localBadServerDefinition) {
/*  59 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  60 */         BadServerDefinitionHelper.write(localOutputStream, localBadServerDefinition);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/*  70 */         int i = ServerIdHelper.read(paramInputStream);
/*  71 */         unregisterServer(i);
/*  72 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered1) {
/*  74 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  75 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered1);
/*     */       }
/*     */ 
/*     */     case 2:
/*     */       try
/*     */       {
/*  85 */         int j = ServerIdHelper.read(paramInputStream);
/*  86 */         ServerDef localServerDef2 = null;
/*  87 */         localServerDef2 = getServer(j);
/*  88 */         localOutputStream = paramResponseHandler.createReply();
/*  89 */         ServerDefHelper.write(localOutputStream, localServerDef2);
/*     */       } catch (ServerNotRegistered localServerNotRegistered2) {
/*  91 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  92 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered2);
/*     */       }
/*     */ 
/*     */     case 3:
/*     */       try
/*     */       {
/* 102 */         int k = ServerIdHelper.read(paramInputStream);
/* 103 */         bool = false;
/* 104 */         bool = isInstalled(k);
/* 105 */         localOutputStream = paramResponseHandler.createReply();
/* 106 */         localOutputStream.write_boolean(bool);
/*     */       } catch (ServerNotRegistered localServerNotRegistered3) {
/* 108 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 109 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered3);
/*     */       }
/*     */ 
/*     */     case 4:
/*     */       try
/*     */       {
/* 119 */         int m = ServerIdHelper.read(paramInputStream);
/* 120 */         install(m);
/* 121 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered4) {
/* 123 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 124 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered4);
/*     */       } catch (ServerAlreadyInstalled localServerAlreadyInstalled) {
/* 126 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 127 */         ServerAlreadyInstalledHelper.write(localOutputStream, localServerAlreadyInstalled);
/*     */       }
/*     */ 
/*     */     case 5:
/*     */       try
/*     */       {
/* 137 */         int n = ServerIdHelper.read(paramInputStream);
/* 138 */         uninstall(n);
/* 139 */         localOutputStream = paramResponseHandler.createReply();
/*     */       } catch (ServerNotRegistered localServerNotRegistered5) {
/* 141 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 142 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered5);
/*     */       } catch (ServerAlreadyUninstalled localServerAlreadyUninstalled) {
/* 144 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 145 */         ServerAlreadyUninstalledHelper.write(localOutputStream, localServerAlreadyUninstalled);
/*     */       }
/*     */ 
/*     */     case 6:
/* 154 */       localObject = null;
/* 155 */       localObject = listRegisteredServers();
/* 156 */       localOutputStream = paramResponseHandler.createReply();
/* 157 */       ServerIdsHelper.write(localOutputStream, (int[])localObject);
/* 158 */       break;
/*     */     case 7:
/* 165 */       localObject = null;
/* 166 */       localObject = getApplicationNames();
/* 167 */       localOutputStream = paramResponseHandler.createReply();
/* 168 */       StringSeqHelper.write(localOutputStream, (String[])localObject);
/* 169 */       break;
/*     */     case 8:
/*     */       try
/*     */       {
/* 177 */         localObject = paramInputStream.read_string();
/* 178 */         bool = false;
/* 179 */         int i2 = getServerID((String)localObject);
/* 180 */         localOutputStream = paramResponseHandler.createReply();
/* 181 */         localOutputStream.write_long(i2);
/*     */       } catch (ServerNotRegistered localServerNotRegistered6) {
/* 183 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 184 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered6);
/*     */       }
/*     */ 
/*     */     default:
/* 190 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 193 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 202 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  23 */     _methods.put("registerServer", new Integer(0));
/*  24 */     _methods.put("unregisterServer", new Integer(1));
/*  25 */     _methods.put("getServer", new Integer(2));
/*  26 */     _methods.put("isInstalled", new Integer(3));
/*  27 */     _methods.put("install", new Integer(4));
/*  28 */     _methods.put("uninstall", new Integer(5));
/*  29 */     _methods.put("listRegisteredServers", new Integer(6));
/*  30 */     _methods.put("getApplicationNames", new Integer(7));
/*  31 */     _methods.put("getServerID", new Integer(8));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._RepositoryImplBase
 * JD-Core Version:    0.6.2
 */
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
/*     */ public abstract class _LocatorImplBase extends ObjectImpl
/*     */   implements Locator, InvokeHandler
/*     */ {
/*  20 */   private static Hashtable _methods = new Hashtable();
/*     */ 
/* 131 */   private static String[] __ids = { "IDL:activation/Locator:1.0" };
/*     */ 
/*     */   public OutputStream _invoke(String paramString, InputStream paramInputStream, ResponseHandler paramResponseHandler)
/*     */   {
/*  33 */     OutputStream localOutputStream = null;
/*  34 */     Integer localInteger = (Integer)_methods.get(paramString);
/*  35 */     if (localInteger == null)
/*  36 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     String str2;
/*     */     Object localObject;
/*  38 */     switch (localInteger.intValue())
/*     */     {
/*     */     case 0:
/*     */       try
/*     */       {
/*  45 */         int i = ServerIdHelper.read(paramInputStream);
/*  46 */         str2 = paramInputStream.read_string();
/*  47 */         localObject = null;
/*  48 */         localObject = locateServer(i, str2);
/*  49 */         localOutputStream = paramResponseHandler.createReply();
/*  50 */         ServerLocationHelper.write(localOutputStream, (ServerLocation)localObject);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint1) {
/*  52 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  53 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint1);
/*     */       } catch (ServerNotRegistered localServerNotRegistered1) {
/*  55 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  56 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered1);
/*     */       } catch (ServerHeldDown localServerHeldDown1) {
/*  58 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  59 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown1);
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/*  69 */         int j = ServerIdHelper.read(paramInputStream);
/*  70 */         str2 = ORBidHelper.read(paramInputStream);
/*  71 */         localObject = null;
/*  72 */         localObject = locateServerForORB(j, str2);
/*  73 */         localOutputStream = paramResponseHandler.createReply();
/*  74 */         ServerLocationPerORBHelper.write(localOutputStream, (ServerLocationPerORB)localObject);
/*     */       } catch (InvalidORBid localInvalidORBid) {
/*  76 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  77 */         InvalidORBidHelper.write(localOutputStream, localInvalidORBid);
/*     */       } catch (ServerNotRegistered localServerNotRegistered2) {
/*  79 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  80 */         ServerNotRegisteredHelper.write(localOutputStream, localServerNotRegistered2);
/*     */       } catch (ServerHeldDown localServerHeldDown2) {
/*  82 */         localOutputStream = paramResponseHandler.createExceptionReply();
/*  83 */         ServerHeldDownHelper.write(localOutputStream, localServerHeldDown2);
/*     */       }
/*     */ 
/*     */     case 2:
/*     */       try
/*     */       {
/*  93 */         String str1 = paramInputStream.read_string();
/*  94 */         int k = 0;
/*  95 */         k = getEndpoint(str1);
/*  96 */         localOutputStream = paramResponseHandler.createReply();
/*  97 */         localOutputStream.write_long(k);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint2) {
/*  99 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 100 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint2);
/*     */       }
/*     */ 
/*     */     case 3:
/*     */       try
/*     */       {
/* 110 */         ServerLocationPerORB localServerLocationPerORB = ServerLocationPerORBHelper.read(paramInputStream);
/* 111 */         String str3 = paramInputStream.read_string();
/* 112 */         int m = 0;
/* 113 */         m = getServerPortForType(localServerLocationPerORB, str3);
/* 114 */         localOutputStream = paramResponseHandler.createReply();
/* 115 */         localOutputStream.write_long(m);
/*     */       } catch (NoSuchEndPoint localNoSuchEndPoint3) {
/* 117 */         localOutputStream = paramResponseHandler.createExceptionReply();
/* 118 */         NoSuchEndPointHelper.write(localOutputStream, localNoSuchEndPoint3);
/*     */       }
/*     */ 
/*     */     default:
/* 124 */       throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 127 */     return localOutputStream;
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 136 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  23 */     _methods.put("locateServer", new Integer(0));
/*  24 */     _methods.put("locateServerForORB", new Integer(1));
/*  25 */     _methods.put("getEndpoint", new Integer(2));
/*  26 */     _methods.put("getServerPortForType", new Integer(3));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._LocatorImplBase
 * JD-Core Version:    0.6.2
 */
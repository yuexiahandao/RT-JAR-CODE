/*     */ package com.sun.corba.se.spi.activation;
/*     */ 
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocation;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationHelper;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORB;
/*     */ import com.sun.corba.se.spi.activation.LocatorPackage.ServerLocationPerORBHelper;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Properties;
/*     */ import org.omg.CORBA.MARSHAL;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.portable.ApplicationException;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.ObjectImpl;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.CORBA.portable.RemarshalException;
/*     */ 
/*     */ public class _LocatorStub extends ObjectImpl
/*     */   implements Locator
/*     */ {
/* 126 */   private static String[] __ids = { "IDL:activation/Locator:1.0" };
/*     */ 
/*     */   public ServerLocation locateServer(int paramInt, String paramString)
/*     */     throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown
/*     */   {
/*  18 */     InputStream localInputStream = null;
/*     */     try {
/*  20 */       OutputStream localOutputStream = _request("locateServer", true);
/*  21 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  22 */       localOutputStream.write_string(paramString);
/*  23 */       localInputStream = _invoke(localOutputStream);
/*  24 */       localObject1 = ServerLocationHelper.read(localInputStream);
/*  25 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  27 */       localInputStream = localApplicationException.getInputStream();
/*  28 */       localObject1 = localApplicationException.getId();
/*  29 */       if (((String)localObject1).equals("IDL:activation/NoSuchEndPoint:1.0"))
/*  30 */         throw NoSuchEndPointHelper.read(localInputStream);
/*  31 */       if (((String)localObject1).equals("IDL:activation/ServerNotRegistered:1.0"))
/*  32 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*  33 */       if (((String)localObject1).equals("IDL:activation/ServerHeldDown:1.0")) {
/*  34 */         throw ServerHeldDownHelper.read(localInputStream);
/*     */       }
/*  36 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  38 */       return locateServer(paramInt, paramString);
/*     */     } finally {
/*  40 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerLocationPerORB locateServerForORB(int paramInt, String paramString)
/*     */     throws InvalidORBid, ServerNotRegistered, ServerHeldDown
/*     */   {
/*  48 */     InputStream localInputStream = null;
/*     */     try {
/*  50 */       OutputStream localOutputStream = _request("locateServerForORB", true);
/*  51 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  52 */       ORBidHelper.write(localOutputStream, paramString);
/*  53 */       localInputStream = _invoke(localOutputStream);
/*  54 */       localObject1 = ServerLocationPerORBHelper.read(localInputStream);
/*  55 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  57 */       localInputStream = localApplicationException.getInputStream();
/*  58 */       localObject1 = localApplicationException.getId();
/*  59 */       if (((String)localObject1).equals("IDL:activation/InvalidORBid:1.0"))
/*  60 */         throw InvalidORBidHelper.read(localInputStream);
/*  61 */       if (((String)localObject1).equals("IDL:activation/ServerNotRegistered:1.0"))
/*  62 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*  63 */       if (((String)localObject1).equals("IDL:activation/ServerHeldDown:1.0")) {
/*  64 */         throw ServerHeldDownHelper.read(localInputStream);
/*     */       }
/*  66 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  68 */       return locateServerForORB(paramInt, paramString);
/*     */     } finally {
/*  70 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getEndpoint(String paramString)
/*     */     throws NoSuchEndPoint
/*     */   {
/*  78 */     InputStream localInputStream = null;
/*     */     try {
/*  80 */       OutputStream localOutputStream = _request("getEndpoint", true);
/*  81 */       localOutputStream.write_string(paramString);
/*  82 */       localInputStream = _invoke(localOutputStream);
/*  83 */       int i = TCPPortHelper.read(localInputStream);
/*  84 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/*  86 */       localInputStream = localApplicationException.getInputStream();
/*  87 */       String str = localApplicationException.getId();
/*  88 */       if (str.equals("IDL:activation/NoSuchEndPoint:1.0")) {
/*  89 */         throw NoSuchEndPointHelper.read(localInputStream);
/*     */       }
/*  91 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  93 */       return getEndpoint(paramString);
/*     */     } finally {
/*  95 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getServerPortForType(ServerLocationPerORB paramServerLocationPerORB, String paramString)
/*     */     throws NoSuchEndPoint
/*     */   {
/* 103 */     InputStream localInputStream = null;
/*     */     try {
/* 105 */       OutputStream localOutputStream = _request("getServerPortForType", true);
/* 106 */       ServerLocationPerORBHelper.write(localOutputStream, paramServerLocationPerORB);
/* 107 */       localOutputStream.write_string(paramString);
/* 108 */       localInputStream = _invoke(localOutputStream);
/* 109 */       int i = TCPPortHelper.read(localInputStream);
/* 110 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/* 112 */       localInputStream = localApplicationException.getInputStream();
/* 113 */       String str = localApplicationException.getId();
/* 114 */       if (str.equals("IDL:activation/NoSuchEndPoint:1.0")) {
/* 115 */         throw NoSuchEndPointHelper.read(localInputStream);
/*     */       }
/* 117 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 119 */       return getServerPortForType(paramServerLocationPerORB, paramString);
/*     */     } finally {
/* 121 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 131 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 136 */     String str = paramObjectInputStream.readUTF();
/* 137 */     String[] arrayOfString = null;
/* 138 */     Properties localProperties = null;
/* 139 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 140 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 141 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 146 */     String[] arrayOfString = null;
/* 147 */     Properties localProperties = null;
/* 148 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 149 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._LocatorStub
 * JD-Core Version:    0.6.2
 */
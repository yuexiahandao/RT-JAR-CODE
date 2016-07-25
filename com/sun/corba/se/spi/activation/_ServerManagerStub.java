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
/*     */ public class _ServerManagerStub extends ObjectImpl
/*     */   implements ServerManager
/*     */ {
/* 337 */   private static String[] __ids = { "IDL:activation/ServerManager:1.0", "IDL:activation/Activator:1.0", "IDL:activation/Locator:1.0" };
/*     */ 
/*     */   public void active(int paramInt, Server paramServer)
/*     */     throws ServerNotRegistered
/*     */   {
/*  18 */     InputStream localInputStream = null;
/*     */     try {
/*  20 */       OutputStream localOutputStream = _request("active", true);
/*  21 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  22 */       ServerHelper.write(localOutputStream, paramServer);
/*  23 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  26 */       localInputStream = localApplicationException.getInputStream();
/*  27 */       String str = localApplicationException.getId();
/*  28 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0")) {
/*  29 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/*  31 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  33 */       active(paramInt, paramServer);
/*     */     } finally {
/*  35 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerEndpoints(int paramInt, String paramString, EndPointInfo[] paramArrayOfEndPointInfo)
/*     */     throws ServerNotRegistered, NoSuchEndPoint, ORBAlreadyRegistered
/*     */   {
/*  43 */     InputStream localInputStream = null;
/*     */     try {
/*  45 */       OutputStream localOutputStream = _request("registerEndpoints", true);
/*  46 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  47 */       ORBidHelper.write(localOutputStream, paramString);
/*  48 */       EndpointInfoListHelper.write(localOutputStream, paramArrayOfEndPointInfo);
/*  49 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  52 */       localInputStream = localApplicationException.getInputStream();
/*  53 */       String str = localApplicationException.getId();
/*  54 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0"))
/*  55 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*  56 */       if (str.equals("IDL:activation/NoSuchEndPoint:1.0"))
/*  57 */         throw NoSuchEndPointHelper.read(localInputStream);
/*  58 */       if (str.equals("IDL:activation/ORBAlreadyRegistered:1.0")) {
/*  59 */         throw ORBAlreadyRegisteredHelper.read(localInputStream);
/*     */       }
/*  61 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  63 */       registerEndpoints(paramInt, paramString, paramArrayOfEndPointInfo);
/*     */     } finally {
/*  65 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] getActiveServers()
/*     */   {
/*  73 */     InputStream localInputStream = null;
/*     */     try {
/*  75 */       OutputStream localOutputStream = _request("getActiveServers", true);
/*  76 */       localInputStream = _invoke(localOutputStream);
/*  77 */       localObject1 = ServerIdsHelper.read(localInputStream);
/*  78 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  80 */       localInputStream = localApplicationException.getInputStream();
/*  81 */       localObject1 = localApplicationException.getId();
/*  82 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  84 */       return getActiveServers();
/*     */     } finally {
/*  86 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void activate(int paramInt)
/*     */     throws ServerAlreadyActive, ServerNotRegistered, ServerHeldDown
/*     */   {
/*  94 */     InputStream localInputStream = null;
/*     */     try {
/*  96 */       OutputStream localOutputStream = _request("activate", true);
/*  97 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  98 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 101 */       localInputStream = localApplicationException.getInputStream();
/* 102 */       String str = localApplicationException.getId();
/* 103 */       if (str.equals("IDL:activation/ServerAlreadyActive:1.0"))
/* 104 */         throw ServerAlreadyActiveHelper.read(localInputStream);
/* 105 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0"))
/* 106 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 107 */       if (str.equals("IDL:activation/ServerHeldDown:1.0")) {
/* 108 */         throw ServerHeldDownHelper.read(localInputStream);
/*     */       }
/* 110 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 112 */       activate(paramInt);
/*     */     } finally {
/* 114 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void shutdown(int paramInt)
/*     */     throws ServerNotActive, ServerNotRegistered
/*     */   {
/* 122 */     InputStream localInputStream = null;
/*     */     try {
/* 124 */       OutputStream localOutputStream = _request("shutdown", true);
/* 125 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 126 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 129 */       localInputStream = localApplicationException.getInputStream();
/* 130 */       String str = localApplicationException.getId();
/* 131 */       if (str.equals("IDL:activation/ServerNotActive:1.0"))
/* 132 */         throw ServerNotActiveHelper.read(localInputStream);
/* 133 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0")) {
/* 134 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/* 136 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 138 */       shutdown(paramInt);
/*     */     } finally {
/* 140 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void install(int paramInt)
/*     */     throws ServerNotRegistered, ServerHeldDown, ServerAlreadyInstalled
/*     */   {
/* 148 */     InputStream localInputStream = null;
/*     */     try {
/* 150 */       OutputStream localOutputStream = _request("install", true);
/* 151 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 152 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 155 */       localInputStream = localApplicationException.getInputStream();
/* 156 */       String str = localApplicationException.getId();
/* 157 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0"))
/* 158 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 159 */       if (str.equals("IDL:activation/ServerHeldDown:1.0"))
/* 160 */         throw ServerHeldDownHelper.read(localInputStream);
/* 161 */       if (str.equals("IDL:activation/ServerAlreadyInstalled:1.0")) {
/* 162 */         throw ServerAlreadyInstalledHelper.read(localInputStream);
/*     */       }
/* 164 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 166 */       install(paramInt);
/*     */     } finally {
/* 168 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] getORBNames(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/* 176 */     InputStream localInputStream = null;
/*     */     try {
/* 178 */       OutputStream localOutputStream = _request("getORBNames", true);
/* 179 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 180 */       localInputStream = _invoke(localOutputStream);
/* 181 */       localObject1 = ORBidListHelper.read(localInputStream);
/* 182 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 184 */       localInputStream = localApplicationException.getInputStream();
/* 185 */       localObject1 = localApplicationException.getId();
/* 186 */       if (((String)localObject1).equals("IDL:activation/ServerNotRegistered:1.0")) {
/* 187 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/* 189 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 191 */       return getORBNames(paramInt);
/*     */     } finally {
/* 193 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void uninstall(int paramInt)
/*     */     throws ServerNotRegistered, ServerHeldDown, ServerAlreadyUninstalled
/*     */   {
/* 201 */     InputStream localInputStream = null;
/*     */     try {
/* 203 */       OutputStream localOutputStream = _request("uninstall", true);
/* 204 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 205 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 208 */       localInputStream = localApplicationException.getInputStream();
/* 209 */       String str = localApplicationException.getId();
/* 210 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0"))
/* 211 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 212 */       if (str.equals("IDL:activation/ServerHeldDown:1.0"))
/* 213 */         throw ServerHeldDownHelper.read(localInputStream);
/* 214 */       if (str.equals("IDL:activation/ServerAlreadyUninstalled:1.0")) {
/* 215 */         throw ServerAlreadyUninstalledHelper.read(localInputStream);
/*     */       }
/* 217 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 219 */       uninstall(paramInt);
/*     */     } finally {
/* 221 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerLocation locateServer(int paramInt, String paramString)
/*     */     throws NoSuchEndPoint, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 229 */     InputStream localInputStream = null;
/*     */     try {
/* 231 */       OutputStream localOutputStream = _request("locateServer", true);
/* 232 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 233 */       localOutputStream.write_string(paramString);
/* 234 */       localInputStream = _invoke(localOutputStream);
/* 235 */       localObject1 = ServerLocationHelper.read(localInputStream);
/* 236 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 238 */       localInputStream = localApplicationException.getInputStream();
/* 239 */       localObject1 = localApplicationException.getId();
/* 240 */       if (((String)localObject1).equals("IDL:activation/NoSuchEndPoint:1.0"))
/* 241 */         throw NoSuchEndPointHelper.read(localInputStream);
/* 242 */       if (((String)localObject1).equals("IDL:activation/ServerNotRegistered:1.0"))
/* 243 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 244 */       if (((String)localObject1).equals("IDL:activation/ServerHeldDown:1.0")) {
/* 245 */         throw ServerHeldDownHelper.read(localInputStream);
/*     */       }
/* 247 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 249 */       return locateServer(paramInt, paramString);
/*     */     } finally {
/* 251 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerLocationPerORB locateServerForORB(int paramInt, String paramString)
/*     */     throws InvalidORBid, ServerNotRegistered, ServerHeldDown
/*     */   {
/* 259 */     InputStream localInputStream = null;
/*     */     try {
/* 261 */       OutputStream localOutputStream = _request("locateServerForORB", true);
/* 262 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 263 */       ORBidHelper.write(localOutputStream, paramString);
/* 264 */       localInputStream = _invoke(localOutputStream);
/* 265 */       localObject1 = ServerLocationPerORBHelper.read(localInputStream);
/* 266 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 268 */       localInputStream = localApplicationException.getInputStream();
/* 269 */       localObject1 = localApplicationException.getId();
/* 270 */       if (((String)localObject1).equals("IDL:activation/InvalidORBid:1.0"))
/* 271 */         throw InvalidORBidHelper.read(localInputStream);
/* 272 */       if (((String)localObject1).equals("IDL:activation/ServerNotRegistered:1.0"))
/* 273 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 274 */       if (((String)localObject1).equals("IDL:activation/ServerHeldDown:1.0")) {
/* 275 */         throw ServerHeldDownHelper.read(localInputStream);
/*     */       }
/* 277 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 279 */       return locateServerForORB(paramInt, paramString);
/*     */     } finally {
/* 281 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getEndpoint(String paramString)
/*     */     throws NoSuchEndPoint
/*     */   {
/* 289 */     InputStream localInputStream = null;
/*     */     try {
/* 291 */       OutputStream localOutputStream = _request("getEndpoint", true);
/* 292 */       localOutputStream.write_string(paramString);
/* 293 */       localInputStream = _invoke(localOutputStream);
/* 294 */       int i = TCPPortHelper.read(localInputStream);
/* 295 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/* 297 */       localInputStream = localApplicationException.getInputStream();
/* 298 */       String str = localApplicationException.getId();
/* 299 */       if (str.equals("IDL:activation/NoSuchEndPoint:1.0")) {
/* 300 */         throw NoSuchEndPointHelper.read(localInputStream);
/*     */       }
/* 302 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 304 */       return getEndpoint(paramString);
/*     */     } finally {
/* 306 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getServerPortForType(ServerLocationPerORB paramServerLocationPerORB, String paramString)
/*     */     throws NoSuchEndPoint
/*     */   {
/* 314 */     InputStream localInputStream = null;
/*     */     try {
/* 316 */       OutputStream localOutputStream = _request("getServerPortForType", true);
/* 317 */       ServerLocationPerORBHelper.write(localOutputStream, paramServerLocationPerORB);
/* 318 */       localOutputStream.write_string(paramString);
/* 319 */       localInputStream = _invoke(localOutputStream);
/* 320 */       int i = TCPPortHelper.read(localInputStream);
/* 321 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/* 323 */       localInputStream = localApplicationException.getInputStream();
/* 324 */       String str = localApplicationException.getId();
/* 325 */       if (str.equals("IDL:activation/NoSuchEndPoint:1.0")) {
/* 326 */         throw NoSuchEndPointHelper.read(localInputStream);
/*     */       }
/* 328 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 330 */       return getServerPortForType(paramServerLocationPerORB, paramString);
/*     */     } finally {
/* 332 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 344 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 349 */     String str = paramObjectInputStream.readUTF();
/* 350 */     String[] arrayOfString = null;
/* 351 */     Properties localProperties = null;
/* 352 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 353 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 354 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 359 */     String[] arrayOfString = null;
/* 360 */     Properties localProperties = null;
/* 361 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 362 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._ServerManagerStub
 * JD-Core Version:    0.6.2
 */
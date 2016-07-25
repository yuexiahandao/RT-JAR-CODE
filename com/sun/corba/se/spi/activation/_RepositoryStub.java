/*     */ package com.sun.corba.se.spi.activation;
/*     */ 
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDefHelper;
/*     */ import com.sun.corba.se.spi.activation.RepositoryPackage.StringSeqHelper;
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
/*     */ public class _RepositoryStub extends ObjectImpl
/*     */   implements Repository
/*     */ {
/* 235 */   private static String[] __ids = { "IDL:activation/Repository:1.0" };
/*     */ 
/*     */   public int registerServer(ServerDef paramServerDef)
/*     */     throws ServerAlreadyRegistered, BadServerDefinition
/*     */   {
/*  18 */     InputStream localInputStream = null;
/*     */     try {
/*  20 */       OutputStream localOutputStream = _request("registerServer", true);
/*  21 */       ServerDefHelper.write(localOutputStream, paramServerDef);
/*  22 */       localInputStream = _invoke(localOutputStream);
/*  23 */       int i = ServerIdHelper.read(localInputStream);
/*  24 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/*  26 */       localInputStream = localApplicationException.getInputStream();
/*  27 */       String str = localApplicationException.getId();
/*  28 */       if (str.equals("IDL:activation/ServerAlreadyRegistered:1.0"))
/*  29 */         throw ServerAlreadyRegisteredHelper.read(localInputStream);
/*  30 */       if (str.equals("IDL:activation/BadServerDefinition:1.0")) {
/*  31 */         throw BadServerDefinitionHelper.read(localInputStream);
/*     */       }
/*  33 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  35 */       return registerServer(paramServerDef);
/*     */     } finally {
/*  37 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregisterServer(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/*  45 */     InputStream localInputStream = null;
/*     */     try {
/*  47 */       OutputStream localOutputStream = _request("unregisterServer", true);
/*  48 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  49 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/*  52 */       localInputStream = localApplicationException.getInputStream();
/*  53 */       String str = localApplicationException.getId();
/*  54 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0")) {
/*  55 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/*  57 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/*  59 */       unregisterServer(paramInt);
/*     */     } finally {
/*  61 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ServerDef getServer(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/*  69 */     InputStream localInputStream = null;
/*     */     try {
/*  71 */       OutputStream localOutputStream = _request("getServer", true);
/*  72 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  73 */       localInputStream = _invoke(localOutputStream);
/*  74 */       localObject1 = ServerDefHelper.read(localInputStream);
/*  75 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/*  77 */       localInputStream = localApplicationException.getInputStream();
/*  78 */       localObject1 = localApplicationException.getId();
/*  79 */       if (((String)localObject1).equals("IDL:activation/ServerNotRegistered:1.0")) {
/*  80 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/*  82 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/*  84 */       return getServer(paramInt);
/*     */     } finally {
/*  86 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isInstalled(int paramInt)
/*     */     throws ServerNotRegistered
/*     */   {
/*  94 */     InputStream localInputStream = null;
/*     */     try {
/*  96 */       OutputStream localOutputStream = _request("isInstalled", true);
/*  97 */       ServerIdHelper.write(localOutputStream, paramInt);
/*  98 */       localInputStream = _invoke(localOutputStream);
/*  99 */       boolean bool1 = localInputStream.read_boolean();
/* 100 */       return bool1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 102 */       localInputStream = localApplicationException.getInputStream();
/* 103 */       String str = localApplicationException.getId();
/* 104 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0")) {
/* 105 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/* 107 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 109 */       return isInstalled(paramInt);
/*     */     } finally {
/* 111 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void install(int paramInt)
/*     */     throws ServerNotRegistered, ServerAlreadyInstalled
/*     */   {
/* 119 */     InputStream localInputStream = null;
/*     */     try {
/* 121 */       OutputStream localOutputStream = _request("install", true);
/* 122 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 123 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 126 */       localInputStream = localApplicationException.getInputStream();
/* 127 */       String str = localApplicationException.getId();
/* 128 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0"))
/* 129 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 130 */       if (str.equals("IDL:activation/ServerAlreadyInstalled:1.0")) {
/* 131 */         throw ServerAlreadyInstalledHelper.read(localInputStream);
/*     */       }
/* 133 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 135 */       install(paramInt);
/*     */     } finally {
/* 137 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void uninstall(int paramInt)
/*     */     throws ServerNotRegistered, ServerAlreadyUninstalled
/*     */   {
/* 145 */     InputStream localInputStream = null;
/*     */     try {
/* 147 */       OutputStream localOutputStream = _request("uninstall", true);
/* 148 */       ServerIdHelper.write(localOutputStream, paramInt);
/* 149 */       localInputStream = _invoke(localOutputStream);
/*     */     }
/*     */     catch (ApplicationException localApplicationException) {
/* 152 */       localInputStream = localApplicationException.getInputStream();
/* 153 */       String str = localApplicationException.getId();
/* 154 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0"))
/* 155 */         throw ServerNotRegisteredHelper.read(localInputStream);
/* 156 */       if (str.equals("IDL:activation/ServerAlreadyUninstalled:1.0")) {
/* 157 */         throw ServerAlreadyUninstalledHelper.read(localInputStream);
/*     */       }
/* 159 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 161 */       uninstall(paramInt);
/*     */     } finally {
/* 163 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int[] listRegisteredServers()
/*     */   {
/* 171 */     InputStream localInputStream = null;
/*     */     try {
/* 173 */       OutputStream localOutputStream = _request("listRegisteredServers", true);
/* 174 */       localInputStream = _invoke(localOutputStream);
/* 175 */       localObject1 = ServerIdsHelper.read(localInputStream);
/* 176 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 178 */       localInputStream = localApplicationException.getInputStream();
/* 179 */       localObject1 = localApplicationException.getId();
/* 180 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 182 */       return listRegisteredServers();
/*     */     } finally {
/* 184 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] getApplicationNames()
/*     */   {
/* 192 */     InputStream localInputStream = null;
/*     */     try {
/* 194 */       OutputStream localOutputStream = _request("getApplicationNames", true);
/* 195 */       localInputStream = _invoke(localOutputStream);
/* 196 */       localObject1 = StringSeqHelper.read(localInputStream);
/* 197 */       return localObject1;
/*     */     } catch (ApplicationException localApplicationException) {
/* 199 */       localInputStream = localApplicationException.getInputStream();
/* 200 */       localObject1 = localApplicationException.getId();
/* 201 */       throw new MARSHAL((String)localObject1);
/*     */     }
/*     */     catch (RemarshalException localRemarshalException)
/*     */     {
/*     */       java.lang.Object localObject1;
/* 203 */       return getApplicationNames();
/*     */     } finally {
/* 205 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getServerID(String paramString)
/*     */     throws ServerNotRegistered
/*     */   {
/* 213 */     InputStream localInputStream = null;
/*     */     try {
/* 215 */       OutputStream localOutputStream = _request("getServerID", true);
/* 216 */       localOutputStream.write_string(paramString);
/* 217 */       localInputStream = _invoke(localOutputStream);
/* 218 */       int i = ServerIdHelper.read(localInputStream);
/* 219 */       return i;
/*     */     } catch (ApplicationException localApplicationException) {
/* 221 */       localInputStream = localApplicationException.getInputStream();
/* 222 */       String str = localApplicationException.getId();
/* 223 */       if (str.equals("IDL:activation/ServerNotRegistered:1.0")) {
/* 224 */         throw ServerNotRegisteredHelper.read(localInputStream);
/*     */       }
/* 226 */       throw new MARSHAL(str);
/*     */     } catch (RemarshalException localRemarshalException) {
/* 228 */       return getServerID(paramString);
/*     */     } finally {
/* 230 */       _releaseReply(localInputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String[] _ids()
/*     */   {
/* 240 */     return (String[])__ids.clone();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException
/*     */   {
/* 245 */     String str = paramObjectInputStream.readUTF();
/* 246 */     String[] arrayOfString = null;
/* 247 */     Properties localProperties = null;
/* 248 */     org.omg.CORBA.Object localObject = ORB.init(arrayOfString, localProperties).string_to_object(str);
/* 249 */     Delegate localDelegate = ((ObjectImpl)localObject)._get_delegate();
/* 250 */     _set_delegate(localDelegate);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 255 */     String[] arrayOfString = null;
/* 256 */     Properties localProperties = null;
/* 257 */     String str = ORB.init(arrayOfString, localProperties).object_to_string(this);
/* 258 */     paramObjectOutputStream.writeUTF(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.activation._RepositoryStub
 * JD-Core Version:    0.6.2
 */
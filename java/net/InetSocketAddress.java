/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectInputStream.GetField;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamException;
/*     */ import java.io.ObjectStreamField;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ public class InetSocketAddress extends SocketAddress
/*     */ {
/*     */   private final transient InetSocketAddressHolder holder;
/*     */   private static final long serialVersionUID = 5076001401234631237L;
/* 262 */   private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("hostname", String.class), new ObjectStreamField("addr", InetAddress.class), new ObjectStreamField("port", Integer.TYPE) };
/*     */   private static final long FIELDS_OFFSET;
/*     */   private static final Unsafe UNSAFE;
/*     */ 
/*     */   private static int checkPort(int paramInt)
/*     */   {
/* 142 */     if ((paramInt < 0) || (paramInt > 65535))
/* 143 */       throw new IllegalArgumentException("port out of range:" + paramInt);
/* 144 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private static String checkHost(String paramString) {
/* 148 */     if (paramString == null)
/* 149 */       throw new IllegalArgumentException("hostname can't be null");
/* 150 */     return paramString;
/*     */   }
/*     */ 
/*     */   public InetSocketAddress(int paramInt)
/*     */   {
/* 166 */     this(InetAddress.anyLocalAddress(), paramInt);
/*     */   }
/*     */ 
/*     */   public InetSocketAddress(InetAddress paramInetAddress, int paramInt)
/*     */   {
/* 185 */     this.holder = new InetSocketAddressHolder(null, paramInetAddress == null ? InetAddress.anyLocalAddress() : paramInetAddress, checkPort(paramInt), null);
/*     */   }
/*     */ 
/*     */   public InetSocketAddress(String paramString, int paramInt)
/*     */   {
/* 216 */     checkHost(paramString);
/* 217 */     InetAddress localInetAddress = null;
/* 218 */     String str = null;
/*     */     try {
/* 220 */       localInetAddress = InetAddress.getByName(paramString);
/*     */     } catch (UnknownHostException localUnknownHostException) {
/* 222 */       str = paramString;
/*     */     }
/* 224 */     this.holder = new InetSocketAddressHolder(str, localInetAddress, checkPort(paramInt), null);
/*     */   }
/*     */ 
/*     */   private InetSocketAddress(int paramInt, String paramString)
/*     */   {
/* 229 */     this.holder = new InetSocketAddressHolder(paramString, null, paramInt, null);
/*     */   }
/*     */ 
/*     */   public static InetSocketAddress createUnresolved(String paramString, int paramInt)
/*     */   {
/* 254 */     return new InetSocketAddress(checkPort(paramInt), checkHost(paramString));
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 271 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 272 */     localPutField.put("hostname", this.holder.hostname);
/* 273 */     localPutField.put("addr", this.holder.addr);
/* 274 */     localPutField.put("port", this.holder.port);
/* 275 */     paramObjectOutputStream.writeFields();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 282 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 283 */     String str = (String)localGetField.get("hostname", null);
/* 284 */     InetAddress localInetAddress = (InetAddress)localGetField.get("addr", null);
/* 285 */     int i = localGetField.get("port", -1);
/*     */ 
/* 288 */     checkPort(i);
/* 289 */     if ((str == null) && (localInetAddress == null)) {
/* 290 */       throw new InvalidObjectException("hostname and addr can't both be null");
/*     */     }
/*     */ 
/* 293 */     InetSocketAddressHolder localInetSocketAddressHolder = new InetSocketAddressHolder(str, localInetAddress, i, null);
/*     */ 
/* 296 */     UNSAFE.putObject(this, FIELDS_OFFSET, localInetSocketAddressHolder);
/*     */   }
/*     */ 
/*     */   private void readObjectNoData()
/*     */     throws ObjectStreamException
/*     */   {
/* 302 */     throw new InvalidObjectException("Stream data required");
/*     */   }
/*     */ 
/*     */   public final int getPort()
/*     */   {
/* 324 */     return this.holder.getPort();
/*     */   }
/*     */ 
/*     */   public final InetAddress getAddress()
/*     */   {
/* 334 */     return this.holder.getAddress();
/*     */   }
/*     */ 
/*     */   public final String getHostName()
/*     */   {
/* 345 */     return this.holder.getHostName();
/*     */   }
/*     */ 
/*     */   public final String getHostString()
/*     */   {
/* 357 */     return this.holder.getHostString();
/*     */   }
/*     */ 
/*     */   public final boolean isUnresolved()
/*     */   {
/* 367 */     return this.holder.isUnresolved();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 380 */     return this.holder.toString();
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object paramObject)
/*     */   {
/* 405 */     if ((paramObject == null) || (!(paramObject instanceof InetSocketAddress)))
/* 406 */       return false;
/* 407 */     return this.holder.equals(((InetSocketAddress)paramObject).holder);
/*     */   }
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 417 */     return this.holder.hashCode();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 309 */       Unsafe localUnsafe = Unsafe.getUnsafe();
/* 310 */       FIELDS_OFFSET = localUnsafe.objectFieldOffset(InetSocketAddress.class.getDeclaredField("holder"));
/*     */ 
/* 312 */       UNSAFE = localUnsafe;
/*     */     } catch (ReflectiveOperationException localReflectiveOperationException) {
/* 314 */       throw new Error(localReflectiveOperationException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class InetSocketAddressHolder
/*     */   {
/*     */     private String hostname;
/*     */     private InetAddress addr;
/*     */     private int port;
/*     */ 
/*     */     private InetSocketAddressHolder(String paramString, InetAddress paramInetAddress, int paramInt)
/*     */     {
/*  65 */       this.hostname = paramString;
/*  66 */       this.addr = paramInetAddress;
/*  67 */       this.port = paramInt;
/*     */     }
/*     */ 
/*     */     private int getPort() {
/*  71 */       return this.port;
/*     */     }
/*     */ 
/*     */     private InetAddress getAddress() {
/*  75 */       return this.addr;
/*     */     }
/*     */ 
/*     */     private String getHostName() {
/*  79 */       if (this.hostname != null)
/*  80 */         return this.hostname;
/*  81 */       if (this.addr != null)
/*  82 */         return this.addr.getHostName();
/*  83 */       return null;
/*     */     }
/*     */ 
/*     */     private String getHostString() {
/*  87 */       if (this.hostname != null)
/*  88 */         return this.hostname;
/*  89 */       if (this.addr != null) {
/*  90 */         if (this.addr.holder().getHostName() != null) {
/*  91 */           return this.addr.holder().getHostName();
/*     */         }
/*  93 */         return this.addr.getHostAddress();
/*     */       }
/*  95 */       return null;
/*     */     }
/*     */ 
/*     */     private boolean isUnresolved() {
/*  99 */       return this.addr == null;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 104 */       if (isUnresolved()) {
/* 105 */         return this.hostname + ":" + this.port;
/*     */       }
/* 107 */       return this.addr.toString() + ":" + this.port;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 113 */       if ((paramObject == null) || (!(paramObject instanceof InetSocketAddressHolder)))
/* 114 */         return false;
/* 115 */       InetSocketAddressHolder localInetSocketAddressHolder = (InetSocketAddressHolder)paramObject;
/*     */       boolean bool;
/* 117 */       if (this.addr != null)
/* 118 */         bool = this.addr.equals(localInetSocketAddressHolder.addr);
/* 119 */       else if (this.hostname != null) {
/* 120 */         bool = (localInetSocketAddressHolder.addr == null) && (this.hostname.equalsIgnoreCase(localInetSocketAddressHolder.hostname));
/*     */       }
/*     */       else
/* 123 */         bool = (localInetSocketAddressHolder.addr == null) && (localInetSocketAddressHolder.hostname == null);
/* 124 */       return (bool) && (this.port == localInetSocketAddressHolder.port);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 129 */       if (this.addr != null)
/* 130 */         return this.addr.hashCode() + this.port;
/* 131 */       if (this.hostname != null)
/* 132 */         return this.hostname.toLowerCase().hashCode() + this.port;
/* 133 */       return this.port;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.InetSocketAddress
 * JD-Core Version:    0.6.2
 */
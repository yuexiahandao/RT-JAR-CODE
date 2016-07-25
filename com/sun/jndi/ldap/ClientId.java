/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.net.SocketFactory;
/*     */ 
/*     */ class ClientId
/*     */ {
/*     */   private final int version;
/*     */   private final String hostname;
/*     */   private final int port;
/*     */   private final String protocol;
/*     */   private final Control[] bindCtls;
/*     */   private final OutputStream trace;
/*     */   private final String socketFactory;
/*     */   private final int myHash;
/*     */   private final int ctlHash;
/*  66 */   private SocketFactory factory = null;
/*  67 */   private Method sockComparator = null;
/*  68 */   private boolean isDefaultSockFactory = false;
/*     */   public static final boolean debug = false;
/*     */ 
/*     */   ClientId(int paramInt1, String paramString1, int paramInt2, String paramString2, Control[] paramArrayOfControl, OutputStream paramOutputStream, String paramString3)
/*     */   {
/*  73 */     this.version = paramInt1;
/*  74 */     this.hostname = paramString1.toLowerCase();
/*  75 */     this.port = paramInt2;
/*  76 */     this.protocol = paramString2;
/*  77 */     this.bindCtls = (paramArrayOfControl != null ? (Control[])paramArrayOfControl.clone() : null);
/*  78 */     this.trace = paramOutputStream;
/*     */ 
/*  82 */     this.socketFactory = paramString3;
/*  83 */     if ((paramString3 != null) && (!paramString3.equals("javax.net.ssl.SSLSocketFactory"))) {
/*     */       try
/*     */       {
/*  86 */         Class localClass1 = Obj.helper.loadClass(paramString3);
/*  87 */         Class localClass2 = Class.forName("java.lang.Object");
/*  88 */         this.sockComparator = localClass1.getMethod("compare", new Class[] { localClass2, localClass2 });
/*     */ 
/*  90 */         Method localMethod = localClass1.getMethod("getDefault", new Class[0]);
/*     */ 
/*  92 */         this.factory = ((SocketFactory)localMethod.invoke(null, new Object[0]));
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 102 */       this.isDefaultSockFactory = true;
/*     */     }
/*     */ 
/* 109 */     this.myHash = (paramInt1 + paramInt2 + (paramOutputStream != null ? paramOutputStream.hashCode() : 0) + (this.hostname != null ? this.hostname.hashCode() : 0) + (paramString2 != null ? paramString2.hashCode() : 0) + (this.ctlHash = hashCodeControls(paramArrayOfControl)));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 117 */     if (!(paramObject instanceof ClientId)) {
/* 118 */       return false;
/*     */     }
/*     */ 
/* 121 */     ClientId localClientId = (ClientId)paramObject;
/*     */ 
/* 123 */     return (this.myHash == localClientId.myHash) && (this.version == localClientId.version) && (this.port == localClientId.port) && (this.trace == localClientId.trace) && ((this.hostname == localClientId.hostname) || ((this.hostname != null) && (this.hostname.equals(localClientId.hostname)))) && ((this.protocol == localClientId.protocol) || ((this.protocol != null) && (this.protocol.equals(localClientId.protocol)))) && (this.ctlHash == localClientId.ctlHash) && (equalsControls(this.bindCtls, localClientId.bindCtls)) && (equalsSockFactory(localClientId));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 137 */     return this.myHash;
/*     */   }
/*     */ 
/*     */   private static int hashCodeControls(Control[] paramArrayOfControl) {
/* 141 */     if (paramArrayOfControl == null) {
/* 142 */       return 0;
/*     */     }
/*     */ 
/* 145 */     int i = 0;
/* 146 */     for (int j = 0; j < paramArrayOfControl.length; j++) {
/* 147 */       i = i * 31 + paramArrayOfControl[j].getID().hashCode();
/*     */     }
/* 149 */     return i;
/*     */   }
/*     */ 
/*     */   private static boolean equalsControls(Control[] paramArrayOfControl1, Control[] paramArrayOfControl2) {
/* 153 */     if (paramArrayOfControl1 == paramArrayOfControl2) {
/* 154 */       return true;
/*     */     }
/* 156 */     if ((paramArrayOfControl1 == null) || (paramArrayOfControl2 == null)) {
/* 157 */       return false;
/*     */     }
/* 159 */     if (paramArrayOfControl1.length != paramArrayOfControl2.length) {
/* 160 */       return false;
/*     */     }
/*     */ 
/* 163 */     for (int i = 0; i < paramArrayOfControl1.length; i++) {
/* 164 */       if ((!paramArrayOfControl1[i].getID().equals(paramArrayOfControl2[i].getID())) || (paramArrayOfControl1[i].isCritical() != paramArrayOfControl2[i].isCritical()) || (!Arrays.equals(paramArrayOfControl1[i].getEncodedValue(), paramArrayOfControl2[i].getEncodedValue())))
/*     */       {
/* 168 */         return false;
/*     */       }
/*     */     }
/* 171 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean equalsSockFactory(ClientId paramClientId) {
/* 175 */     if ((this.isDefaultSockFactory) && (paramClientId.isDefaultSockFactory)) {
/* 176 */       return true;
/*     */     }
/* 178 */     if (!paramClientId.isDefaultSockFactory) {
/* 179 */       return invokeComparator(paramClientId, this);
/*     */     }
/* 181 */     return invokeComparator(this, paramClientId);
/*     */   }
/*     */ 
/*     */   private boolean invokeComparator(ClientId paramClientId1, ClientId paramClientId2)
/*     */   {
/*     */     Object localObject;
/*     */     try
/*     */     {
/* 190 */       localObject = paramClientId1.sockComparator.invoke(paramClientId1.factory, new Object[] { paramClientId1.socketFactory, paramClientId2.socketFactory });
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 198 */       return false;
/*     */     }
/* 200 */     if (((Integer)localObject).intValue() == 0) {
/* 201 */       return true;
/*     */     }
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   private static String toStringControls(Control[] paramArrayOfControl) {
/* 207 */     if (paramArrayOfControl == null) {
/* 208 */       return "";
/*     */     }
/* 210 */     StringBuffer localStringBuffer = new StringBuffer();
/* 211 */     for (int i = 0; i < paramArrayOfControl.length; i++) {
/* 212 */       localStringBuffer.append(paramArrayOfControl[i].getID());
/* 213 */       localStringBuffer.append(' ');
/*     */     }
/* 215 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 219 */     return this.hostname + ":" + this.port + ":" + (this.protocol != null ? this.protocol : "") + ":" + toStringControls(this.bindCtls) + ":" + this.socketFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.ClientId
 * JD-Core Version:    0.6.2
 */
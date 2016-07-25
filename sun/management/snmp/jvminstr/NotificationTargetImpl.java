/*     */ package sun.management.snmp.jvminstr;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ 
/*     */ public class NotificationTargetImpl
/*     */   implements NotificationTarget
/*     */ {
/*     */   private InetAddress address;
/*     */   private int port;
/*     */   private String community;
/*     */ 
/*     */   public NotificationTargetImpl(String paramString)
/*     */     throws IllegalArgumentException, UnknownHostException
/*     */   {
/*  48 */     parseTarget(paramString);
/*     */   }
/*     */ 
/*     */   public NotificationTargetImpl(String paramString1, int paramInt, String paramString2)
/*     */     throws UnknownHostException
/*     */   {
/*  54 */     this(InetAddress.getByName(paramString1), paramInt, paramString2);
/*     */   }
/*     */ 
/*     */   public NotificationTargetImpl(InetAddress paramInetAddress, int paramInt, String paramString)
/*     */   {
/*  59 */     this.address = paramInetAddress;
/*  60 */     this.port = paramInt;
/*  61 */     this.community = paramString;
/*     */   }
/*     */ 
/*     */   private void parseTarget(String paramString)
/*     */     throws IllegalArgumentException, UnknownHostException
/*     */   {
/*  67 */     if ((paramString == null) || (paramString.length() == 0))
/*  68 */       throw new IllegalArgumentException("Invalid target [" + paramString + "]");
/*     */     int j;
/*     */     String str;
/*  72 */     if (paramString.startsWith("[")) {
/*  73 */       i = paramString.indexOf("]");
/*  74 */       j = paramString.lastIndexOf(":");
/*  75 */       if (i == -1) {
/*  76 */         throw new IllegalArgumentException("Host starts with [ but does not end with ]");
/*     */       }
/*  78 */       str = paramString.substring(1, i);
/*  79 */       this.port = Integer.parseInt(paramString.substring(i + 2, j));
/*     */ 
/*  81 */       if (!isNumericIPv6Address(str)) {
/*  82 */         throw new IllegalArgumentException("Address inside [...] must be numeric IPv6 address");
/*     */       }
/*     */ 
/*  85 */       if (str.startsWith("["))
/*  86 */         throw new IllegalArgumentException("More than one [[...]]");
/*     */     } else {
/*  88 */       i = paramString.indexOf(":");
/*  89 */       j = paramString.lastIndexOf(":");
/*  90 */       if (i == -1) throw new IllegalArgumentException("Missing port separator \":\"");
/*     */ 
/*  92 */       str = paramString.substring(0, i);
/*     */ 
/*  94 */       this.port = Integer.parseInt(paramString.substring(i + 1, j));
/*     */     }
/*     */ 
/*  98 */     this.address = InetAddress.getByName(str);
/*     */ 
/* 101 */     int i = paramString.lastIndexOf(":");
/*     */ 
/* 103 */     this.community = paramString.substring(i + 1, paramString.length());
/*     */   }
/*     */ 
/*     */   private static boolean isNumericIPv6Address(String paramString)
/*     */   {
/* 112 */     return paramString.indexOf(':') >= 0;
/*     */   }
/*     */ 
/*     */   public String getCommunity() {
/* 116 */     return this.community;
/*     */   }
/*     */ 
/*     */   public InetAddress getAddress() {
/* 120 */     return this.address;
/*     */   }
/*     */ 
/*     */   public int getPort() {
/* 124 */     return this.port;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 128 */     return "address : " + this.address + " port : " + this.port + " community : " + this.community;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.snmp.jvminstr.NotificationTargetImpl
 * JD-Core Version:    0.6.2
 */
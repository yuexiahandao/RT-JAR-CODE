/*     */ package sun.management.jdp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ 
/*     */ public final class JdpJmxPacket extends JdpGenericPacket
/*     */   implements JdpPacket
/*     */ {
/*     */   public static final String UUID_KEY = "DISCOVERABLE_SESSION_UUID";
/*     */   public static final String MAIN_CLASS_KEY = "MAIN_CLASS";
/*     */   public static final String JMX_SERVICE_URL_KEY = "JMX_SERVICE_URL";
/*     */   public static final String INSTANCE_NAME_KEY = "INSTANCE_NAME";
/*     */   private UUID id;
/*     */   private String mainClass;
/*     */   private String jmxServiceUrl;
/*     */   private String instanceName;
/*     */ 
/*     */   public JdpJmxPacket(UUID paramUUID, String paramString)
/*     */   {
/*  80 */     this.id = paramUUID;
/*  81 */     this.jmxServiceUrl = paramString;
/*     */   }
/*     */ 
/*     */   public JdpJmxPacket(byte[] paramArrayOfByte)
/*     */     throws JdpException
/*     */   {
/*  94 */     JdpPacketReader localJdpPacketReader = new JdpPacketReader(paramArrayOfByte);
/*  95 */     Map localMap = localJdpPacketReader.getDiscoveryDataAsMap();
/*     */ 
/*  97 */     String str = (String)localMap.get("DISCOVERABLE_SESSION_UUID");
/*  98 */     this.id = (str == null ? null : UUID.fromString(str));
/*  99 */     this.jmxServiceUrl = ((String)localMap.get("JMX_SERVICE_URL"));
/* 100 */     this.mainClass = ((String)localMap.get("MAIN_CLASS"));
/* 101 */     this.instanceName = ((String)localMap.get("INSTANCE_NAME"));
/*     */   }
/*     */ 
/*     */   public void setMainClass(String paramString)
/*     */   {
/* 110 */     this.mainClass = paramString;
/*     */   }
/*     */ 
/*     */   public void setInstanceName(String paramString)
/*     */   {
/* 119 */     this.instanceName = paramString;
/*     */   }
/*     */ 
/*     */   public UUID getId()
/*     */   {
/* 126 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getMainClass()
/*     */   {
/* 134 */     return this.mainClass;
/*     */   }
/*     */ 
/*     */   public String getJmxServiceUrl()
/*     */   {
/* 142 */     return this.jmxServiceUrl;
/*     */   }
/*     */ 
/*     */   public String getInstanceName()
/*     */   {
/* 150 */     return this.instanceName;
/*     */   }
/*     */ 
/*     */   public byte[] getPacketData()
/*     */     throws IOException
/*     */   {
/* 162 */     JdpPacketWriter localJdpPacketWriter = new JdpPacketWriter();
/* 163 */     localJdpPacketWriter.addEntry("DISCOVERABLE_SESSION_UUID", this.id == null ? null : this.id.toString());
/* 164 */     localJdpPacketWriter.addEntry("MAIN_CLASS", this.mainClass);
/* 165 */     localJdpPacketWriter.addEntry("JMX_SERVICE_URL", this.jmxServiceUrl);
/* 166 */     localJdpPacketWriter.addEntry("INSTANCE_NAME", this.instanceName);
/* 167 */     return localJdpPacketWriter.getPacketBytes();
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 176 */     int i = 1;
/* 177 */     i = i * 31 + this.id.hashCode();
/* 178 */     i = i * 31 + this.jmxServiceUrl.hashCode();
/* 179 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 191 */     if ((paramObject == null) || (!(paramObject instanceof JdpJmxPacket))) {
/* 192 */       return false;
/*     */     }
/*     */ 
/* 195 */     JdpJmxPacket localJdpJmxPacket = (JdpJmxPacket)paramObject;
/* 196 */     return (Objects.equals(this.id, localJdpJmxPacket.getId())) && (Objects.equals(this.jmxServiceUrl, localJdpJmxPacket.getJmxServiceUrl()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpJmxPacket
 * JD-Core Version:    0.6.2
 */
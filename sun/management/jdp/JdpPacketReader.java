/*     */ package sun.management.jdp;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public final class JdpPacketReader
/*     */ {
/*     */   private final DataInputStream pkt;
/*  43 */   private Map<String, String> pmap = null;
/*     */ 
/*     */   public JdpPacketReader(byte[] paramArrayOfByte)
/*     */     throws JdpException
/*     */   {
/*  53 */     ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*  54 */     this.pkt = new DataInputStream(localByteArrayInputStream);
/*     */     try
/*     */     {
/*  57 */       int i = this.pkt.readInt();
/*  58 */       JdpGenericPacket.checkMagic(i);
/*     */     } catch (IOException localIOException1) {
/*  60 */       throw new JdpException("Invalid JDP packet received, bad magic");
/*     */     }
/*     */     try
/*     */     {
/*  64 */       short s = this.pkt.readShort();
/*  65 */       JdpGenericPacket.checkVersion(s);
/*     */     } catch (IOException localIOException2) {
/*  67 */       throw new JdpException("Invalid JDP packet received, bad protocol version");
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getEntry()
/*     */     throws EOFException, JdpException
/*     */   {
/*     */     try
/*     */     {
/*  82 */       int i = this.pkt.readShort();
/*     */ 
/*  85 */       if ((i < 1) && (i > this.pkt.available())) {
/*  86 */         throw new JdpException("Broken JDP packet. Invalid entry length field.");
/*     */       }
/*     */ 
/*  89 */       byte[] arrayOfByte = new byte[i];
/*  90 */       if (this.pkt.read(arrayOfByte) != i) {
/*  91 */         throw new JdpException("Broken JDP packet. Unable to read entry.");
/*     */       }
/*  93 */       return new String(arrayOfByte, "UTF-8");
/*     */     }
/*     */     catch (EOFException localEOFException) {
/*  96 */       throw localEOFException;
/*     */     } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
/*  98 */       throw new JdpException("Broken JDP packet. Unable to decode entry."); } catch (IOException localIOException) {
/*     */     }
/* 100 */     throw new JdpException("Broken JDP packet. Unable to read entry.");
/*     */   }
/*     */ 
/*     */   public Map<String, String> getDiscoveryDataAsMap()
/*     */     throws JdpException
/*     */   {
/* 117 */     if (this.pmap != null) {
/* 118 */       return this.pmap;
/*     */     }
/*     */ 
/* 121 */     String str1 = null; String str2 = null;
/*     */ 
/* 123 */     HashMap localHashMap = new HashMap();
/*     */     try {
/*     */       while (true) {
/* 126 */         str1 = getEntry();
/* 127 */         str2 = getEntry();
/* 128 */         localHashMap.put(str1, str2);
/*     */       }
/*     */     }
/*     */     catch (EOFException localEOFException)
/*     */     {
/* 133 */       if (str2 == null) {
/* 134 */         throw new JdpException("Broken JDP packet. Key without value." + str1);
/*     */       }
/*     */ 
/* 138 */       this.pmap = Collections.unmodifiableMap(localHashMap);
/* 139 */     }return this.pmap;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpPacketReader
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import sun.misc.HexDumpEncoder;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.DerOutputStream;
/*     */ 
/*     */ class OCSPRequest
/*     */ {
/*  77 */   private static final Debug debug = Debug.getInstance("certpath");
/*  78 */   private static final boolean dump = Debug.isOn("ocsp");
/*     */   private final List<CertId> certIds;
/*     */ 
/*     */   OCSPRequest(CertId paramCertId)
/*     */   {
/*  88 */     this.certIds = Collections.singletonList(paramCertId);
/*     */   }
/*     */ 
/*     */   OCSPRequest(List<CertId> paramList) {
/*  92 */     this.certIds = paramList;
/*     */   }
/*     */ 
/*     */   byte[] encodeBytes()
/*     */     throws IOException
/*     */   {
/*  98 */     DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*  99 */     DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 100 */     for (Object localObject1 = this.certIds.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (CertId)((Iterator)localObject1).next();
/* 101 */       localObject3 = new DerOutputStream();
/* 102 */       ((CertId)localObject2).encode((DerOutputStream)localObject3);
/* 103 */       localDerOutputStream2.write((byte)48, (DerOutputStream)localObject3);
/*     */     }
/*     */ 
/* 106 */     localDerOutputStream1.write((byte)48, localDerOutputStream2);
/*     */ 
/* 108 */     localObject1 = new DerOutputStream();
/* 109 */     ((DerOutputStream)localObject1).write((byte)48, localDerOutputStream1);
/*     */ 
/* 112 */     Object localObject2 = new DerOutputStream();
/* 113 */     ((DerOutputStream)localObject2).write((byte)48, (DerOutputStream)localObject1);
/*     */ 
/* 115 */     Object localObject3 = ((DerOutputStream)localObject2).toByteArray();
/*     */ 
/* 117 */     if (dump) {
/* 118 */       HexDumpEncoder localHexDumpEncoder = new HexDumpEncoder();
/* 119 */       debug.println("\nOCSPRequest bytes... ");
/* 120 */       debug.println(localHexDumpEncoder.encode((byte[])localObject3) + "\n");
/*     */     }
/*     */ 
/* 123 */     return localObject3;
/*     */   }
/*     */ 
/*     */   List<CertId> getCertIds() {
/* 127 */     return this.certIds;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.OCSPRequest
 * JD-Core Version:    0.6.2
 */
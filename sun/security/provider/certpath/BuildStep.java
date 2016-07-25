/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class BuildStep
/*     */ {
/*  42 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private Vertex vertex;
/*     */   private X509Certificate cert;
/*     */   private Throwable throwable;
/*     */   private int result;
/*     */   public static final int POSSIBLE = 1;
/*     */   public static final int BACK = 2;
/*     */   public static final int FOLLOW = 3;
/*     */   public static final int FAIL = 4;
/*     */   public static final int SUCCEED = 5;
/*     */ 
/*     */   public BuildStep(Vertex paramVertex, int paramInt)
/*     */   {
/*  87 */     this.vertex = paramVertex;
/*  88 */     if (this.vertex != null) {
/*  89 */       this.cert = ((X509Certificate)this.vertex.getCertificate());
/*  90 */       this.throwable = this.vertex.getThrowable();
/*     */     }
/*  92 */     this.result = paramInt;
/*     */   }
/*     */ 
/*     */   public Vertex getVertex()
/*     */   {
/* 101 */     return this.vertex;
/*     */   }
/*     */ 
/*     */   public X509Certificate getCertificate()
/*     */   {
/* 110 */     return this.cert;
/*     */   }
/*     */ 
/*     */   public String getIssuerName()
/*     */   {
/* 120 */     return this.cert == null ? null : this.cert.getIssuerX500Principal().toString();
/*     */   }
/*     */ 
/*     */   public String getIssuerName(String paramString)
/*     */   {
/* 134 */     return this.cert == null ? paramString : this.cert.getIssuerX500Principal().toString();
/*     */   }
/*     */ 
/*     */   public String getSubjectName()
/*     */   {
/* 145 */     return this.cert == null ? null : this.cert.getSubjectX500Principal().toString();
/*     */   }
/*     */ 
/*     */   public String getSubjectName(String paramString)
/*     */   {
/* 161 */     return this.cert == null ? paramString : this.cert.getSubjectX500Principal().toString();
/*     */   }
/*     */ 
/*     */   public Throwable getThrowable()
/*     */   {
/* 171 */     return this.throwable;
/*     */   }
/*     */ 
/*     */   public int getResult()
/*     */   {
/* 181 */     return this.result;
/*     */   }
/*     */ 
/*     */   public String resultToString(int paramInt)
/*     */   {
/* 192 */     String str = "";
/* 193 */     switch (paramInt) {
/*     */     case 1:
/* 195 */       str = "Certificate to be tried.\n";
/* 196 */       break;
/*     */     case 2:
/* 198 */       str = "Certificate backed out since path does not satisfy build requirements.\n";
/*     */ 
/* 200 */       break;
/*     */     case 3:
/* 202 */       str = "Certificate satisfies conditions.\n";
/* 203 */       break;
/*     */     case 4:
/* 205 */       str = "Certificate backed out since path does not satisfy conditions.\n";
/*     */ 
/* 207 */       break;
/*     */     case 5:
/* 209 */       str = "Certificate satisfies conditions.\n";
/* 210 */       break;
/*     */     default:
/* 212 */       str = "Internal error: Invalid step result value.\n";
/*     */     }
/* 214 */     return str;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 224 */     String str = "Internal Error\n";
/* 225 */     switch (this.result) {
/*     */     case 2:
/*     */     case 4:
/* 228 */       str = resultToString(this.result);
/* 229 */       str = str + this.vertex.throwableToString();
/* 230 */       break;
/*     */     case 1:
/*     */     case 3:
/*     */     case 5:
/* 234 */       str = resultToString(this.result);
/* 235 */       break;
/*     */     default:
/* 237 */       str = "Internal Error: Invalid step result\n";
/*     */     }
/* 239 */     return str;
/*     */   }
/*     */ 
/*     */   public String verboseToString()
/*     */   {
/* 250 */     String str = resultToString(getResult());
/* 251 */     switch (this.result) {
/*     */     case 2:
/*     */     case 4:
/* 254 */       str = str + this.vertex.throwableToString();
/* 255 */       break;
/*     */     case 3:
/*     */     case 5:
/* 258 */       str = str + this.vertex.moreToString();
/* 259 */       break;
/*     */     case 1:
/* 261 */       break;
/*     */     }
/*     */ 
/* 265 */     str = str + "Certificate contains:\n" + this.vertex.certToString();
/* 266 */     return str;
/*     */   }
/*     */ 
/*     */   public String fullToString()
/*     */   {
/* 276 */     String str = resultToString(getResult());
/* 277 */     str = str + this.vertex.toString();
/* 278 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.BuildStep
 * JD-Core Version:    0.6.2
 */
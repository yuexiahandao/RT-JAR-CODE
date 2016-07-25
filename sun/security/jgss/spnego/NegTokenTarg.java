/*     */ package sun.security.jgss.spnego;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class NegTokenTarg extends SpNegoToken
/*     */ {
/*  57 */   private int negResult = 0;
/*  58 */   private Oid supportedMech = null;
/*  59 */   private byte[] responseToken = null;
/*  60 */   private byte[] mechListMIC = null;
/*     */ 
/*     */   NegTokenTarg(int paramInt, Oid paramOid, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/*  64 */     super(1);
/*  65 */     this.negResult = paramInt;
/*  66 */     this.supportedMech = paramOid;
/*  67 */     this.responseToken = paramArrayOfByte1;
/*  68 */     this.mechListMIC = paramArrayOfByte2;
/*     */   }
/*     */ 
/*     */   public NegTokenTarg(byte[] paramArrayOfByte)
/*     */     throws GSSException
/*     */   {
/*  74 */     super(1);
/*  75 */     parseToken(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   final byte[] encode() throws GSSException
/*     */   {
/*     */     try {
/*  81 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/*  84 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/*  85 */       localDerOutputStream2.putEnumerated(this.negResult);
/*  86 */       localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), localDerOutputStream2);
/*     */ 
/*  90 */       if (this.supportedMech != null) {
/*  91 */         localDerOutputStream3 = new DerOutputStream();
/*  92 */         byte[] arrayOfByte = this.supportedMech.getDER();
/*  93 */         localDerOutputStream3.write(arrayOfByte);
/*  94 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream3);
/*     */       }
/*     */ 
/*  99 */       if (this.responseToken != null) {
/* 100 */         localDerOutputStream3 = new DerOutputStream();
/* 101 */         localDerOutputStream3.putOctetString(this.responseToken);
/* 102 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream3);
/*     */       }
/*     */ 
/* 107 */       if (this.mechListMIC != null) {
/* 108 */         if (DEBUG) {
/* 109 */           System.out.println("SpNegoToken NegTokenTarg: sending MechListMIC");
/*     */         }
/*     */ 
/* 112 */         localDerOutputStream3 = new DerOutputStream();
/* 113 */         localDerOutputStream3.putOctetString(this.mechListMIC);
/* 114 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream3);
/*     */       }
/* 116 */       else if (GSSUtil.useMSInterop())
/*     */       {
/* 118 */         if (this.responseToken != null) {
/* 119 */           if (DEBUG) {
/* 120 */             System.out.println("SpNegoToken NegTokenTarg: sending additional token for MS Interop");
/*     */           }
/*     */ 
/* 123 */           localDerOutputStream3 = new DerOutputStream();
/* 124 */           localDerOutputStream3.putOctetString(this.responseToken);
/* 125 */           localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream3);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 131 */       DerOutputStream localDerOutputStream3 = new DerOutputStream();
/* 132 */       localDerOutputStream3.write((byte)48, localDerOutputStream1);
/*     */ 
/* 134 */       return localDerOutputStream3.toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 137 */       throw new GSSException(10, -1, "Invalid SPNEGO NegTokenTarg token : " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseToken(byte[] paramArrayOfByte) throws GSSException
/*     */   {
/*     */     try {
/* 144 */       DerValue localDerValue1 = new DerValue(paramArrayOfByte);
/*     */ 
/* 146 */       if (!localDerValue1.isContextSpecific((byte)1)) {
/* 147 */         throw new IOException("SPNEGO NegoTokenTarg : did not have the right token type");
/*     */       }
/*     */ 
/* 150 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 151 */       if (localDerValue2.tag != 48) {
/* 152 */         throw new IOException("SPNEGO NegoTokenTarg : did not have the Sequence tag");
/*     */       }
/*     */ 
/* 157 */       int i = -1;
/* 158 */       while (localDerValue2.data.available() > 0) {
/* 159 */         DerValue localDerValue3 = localDerValue2.data.getDerValue();
/* 160 */         if (localDerValue3.isContextSpecific((byte)0)) {
/* 161 */           i = checkNextField(i, 0);
/* 162 */           this.negResult = localDerValue3.data.getEnumerated();
/* 163 */           if (DEBUG) {
/* 164 */             System.out.println("SpNegoToken NegTokenTarg: negotiated result = " + getNegoResultString(this.negResult));
/*     */           }
/*     */         }
/* 167 */         else if (localDerValue3.isContextSpecific((byte)1)) {
/* 168 */           i = checkNextField(i, 1);
/* 169 */           ObjectIdentifier localObjectIdentifier = localDerValue3.data.getOID();
/* 170 */           this.supportedMech = new Oid(localObjectIdentifier.toString());
/* 171 */           if (DEBUG) {
/* 172 */             System.out.println("SpNegoToken NegTokenTarg: supported mechanism = " + this.supportedMech);
/*     */           }
/*     */         }
/* 175 */         else if (localDerValue3.isContextSpecific((byte)2)) {
/* 176 */           i = checkNextField(i, 2);
/* 177 */           this.responseToken = localDerValue3.data.getOctetString();
/* 178 */         } else if (localDerValue3.isContextSpecific((byte)3)) {
/* 179 */           i = checkNextField(i, 3);
/* 180 */           if (!GSSUtil.useMSInterop()) {
/* 181 */             this.mechListMIC = localDerValue3.data.getOctetString();
/* 182 */             if (DEBUG) {
/* 183 */               System.out.println("SpNegoToken NegTokenTarg: MechListMIC Token = " + getHexBytes(this.mechListMIC));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 191 */       throw new GSSException(10, -1, "Invalid SPNEGO NegTokenTarg token : " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   int getNegotiatedResult()
/*     */   {
/* 197 */     return this.negResult;
/*     */   }
/*     */ 
/*     */   public Oid getSupportedMech()
/*     */   {
/* 203 */     return this.supportedMech;
/*     */   }
/*     */ 
/*     */   byte[] getResponseToken() {
/* 207 */     return this.responseToken;
/*     */   }
/*     */ 
/*     */   byte[] getMechListMIC() {
/* 211 */     return this.mechListMIC;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.spnego.NegTokenTarg
 * JD-Core Version:    0.6.2
 */
/*     */ package sun.security.jgss.spnego;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class NegTokenInit extends SpNegoToken
/*     */ {
/*  66 */   private byte[] mechTypes = null;
/*  67 */   private Oid[] mechTypeList = null;
/*     */ 
/*  69 */   private BitArray reqFlags = null;
/*  70 */   private byte[] mechToken = null;
/*  71 */   private byte[] mechListMIC = null;
/*     */ 
/*     */   NegTokenInit(byte[] paramArrayOfByte1, BitArray paramBitArray, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */   {
/*  76 */     super(0);
/*  77 */     this.mechTypes = paramArrayOfByte1;
/*  78 */     this.reqFlags = paramBitArray;
/*  79 */     this.mechToken = paramArrayOfByte2;
/*  80 */     this.mechListMIC = paramArrayOfByte3;
/*     */   }
/*     */ 
/*     */   public NegTokenInit(byte[] paramArrayOfByte)
/*     */     throws GSSException
/*     */   {
/*  86 */     super(0);
/*  87 */     parseToken(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   final byte[] encode() throws GSSException
/*     */   {
/*     */     try {
/*  93 */       DerOutputStream localDerOutputStream1 = new DerOutputStream();
/*     */ 
/*  96 */       if (this.mechTypes != null) {
/*  97 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)0), this.mechTypes);
/*     */       }
/*     */ 
/* 102 */       if (this.reqFlags != null) {
/* 103 */         localDerOutputStream2 = new DerOutputStream();
/* 104 */         localDerOutputStream2.putUnalignedBitString(this.reqFlags);
/* 105 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)1), localDerOutputStream2);
/*     */       }
/*     */ 
/* 110 */       if (this.mechToken != null) {
/* 111 */         localDerOutputStream2 = new DerOutputStream();
/* 112 */         localDerOutputStream2.putOctetString(this.mechToken);
/* 113 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)2), localDerOutputStream2);
/*     */       }
/*     */ 
/* 118 */       if (this.mechListMIC != null) {
/* 119 */         if (DEBUG) {
/* 120 */           System.out.println("SpNegoToken NegTokenInit: sending MechListMIC");
/*     */         }
/*     */ 
/* 123 */         localDerOutputStream2 = new DerOutputStream();
/* 124 */         localDerOutputStream2.putOctetString(this.mechListMIC);
/* 125 */         localDerOutputStream1.write(DerValue.createTag((byte)-128, true, (byte)3), localDerOutputStream2);
/*     */       }
/*     */ 
/* 130 */       DerOutputStream localDerOutputStream2 = new DerOutputStream();
/* 131 */       localDerOutputStream2.write((byte)48, localDerOutputStream1);
/*     */ 
/* 133 */       return localDerOutputStream2.toByteArray();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 136 */       throw new GSSException(10, -1, "Invalid SPNEGO NegTokenInit token : " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseToken(byte[] paramArrayOfByte) throws GSSException
/*     */   {
/*     */     try {
/* 143 */       DerValue localDerValue1 = new DerValue(paramArrayOfByte);
/*     */ 
/* 145 */       if (!localDerValue1.isContextSpecific((byte)0)) {
/* 146 */         throw new IOException("SPNEGO NegoTokenInit : did not have right token type");
/*     */       }
/*     */ 
/* 149 */       DerValue localDerValue2 = localDerValue1.data.getDerValue();
/* 150 */       if (localDerValue2.tag != 48) {
/* 151 */         throw new IOException("SPNEGO NegoTokenInit : did not have the Sequence tag");
/*     */       }
/*     */ 
/* 156 */       int i = -1;
/* 157 */       while (localDerValue2.data.available() > 0) {
/* 158 */         DerValue localDerValue3 = localDerValue2.data.getDerValue();
/* 159 */         if (localDerValue3.isContextSpecific((byte)0))
/*     */         {
/* 161 */           i = checkNextField(i, 0);
/* 162 */           DerInputStream localDerInputStream = localDerValue3.data;
/* 163 */           this.mechTypes = localDerInputStream.toByteArray();
/*     */ 
/* 166 */           DerValue[] arrayOfDerValue = localDerInputStream.getSequence(0);
/* 167 */           this.mechTypeList = new Oid[arrayOfDerValue.length];
/* 168 */           ObjectIdentifier localObjectIdentifier = null;
/* 169 */           for (int j = 0; j < arrayOfDerValue.length; j++) {
/* 170 */             localObjectIdentifier = arrayOfDerValue[j].getOID();
/* 171 */             if (DEBUG) {
/* 172 */               System.out.println("SpNegoToken NegTokenInit: reading Mechanism Oid = " + localObjectIdentifier);
/*     */             }
/*     */ 
/* 175 */             this.mechTypeList[j] = new Oid(localObjectIdentifier.toString());
/*     */           }
/* 177 */         } else if (localDerValue3.isContextSpecific((byte)1)) {
/* 178 */           i = checkNextField(i, 1);
/*     */         }
/* 180 */         else if (localDerValue3.isContextSpecific((byte)2)) {
/* 181 */           i = checkNextField(i, 2);
/* 182 */           if (DEBUG) {
/* 183 */             System.out.println("SpNegoToken NegTokenInit: reading Mech Token");
/*     */           }
/*     */ 
/* 186 */           this.mechToken = localDerValue3.data.getOctetString();
/* 187 */         } else if (localDerValue3.isContextSpecific((byte)3)) {
/* 188 */           i = checkNextField(i, 3);
/* 189 */           if (!GSSUtil.useMSInterop()) {
/* 190 */             this.mechListMIC = localDerValue3.data.getOctetString();
/* 191 */             if (DEBUG) {
/* 192 */               System.out.println("SpNegoToken NegTokenInit: MechListMIC Token = " + getHexBytes(this.mechListMIC));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 200 */       throw new GSSException(10, -1, "Invalid SPNEGO NegTokenInit token : " + localIOException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   byte[] getMechTypes()
/*     */   {
/* 206 */     return this.mechTypes;
/*     */   }
/*     */ 
/*     */   public Oid[] getMechTypeList()
/*     */   {
/* 212 */     return this.mechTypeList;
/*     */   }
/*     */ 
/*     */   BitArray getReqFlags() {
/* 216 */     return this.reqFlags;
/*     */   }
/*     */ 
/*     */   public byte[] getMechToken()
/*     */   {
/* 222 */     return this.mechToken;
/*     */   }
/*     */ 
/*     */   byte[] getMechListMIC() {
/* 226 */     return this.mechListMIC;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.spnego.NegTokenInit
 * JD-Core Version:    0.6.2
 */
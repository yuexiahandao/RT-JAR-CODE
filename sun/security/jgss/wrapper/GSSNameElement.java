/*     */ package sun.security.jgss.wrapper;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.Provider;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ import sun.security.jgss.GSSExceptionImpl;
/*     */ import sun.security.jgss.GSSUtil;
/*     */ import sun.security.jgss.spi.GSSNameSpi;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class GSSNameElement
/*     */   implements GSSNameSpi
/*     */ {
/*  50 */   long pName = 0L;
/*     */   private String printableName;
/*     */   private Oid printableType;
/*     */   private GSSLibStub cStub;
/*  55 */   static final GSSNameElement DEF_ACCEPTOR = new GSSNameElement();
/*     */ 
/*     */   private static Oid getNativeNameType(Oid paramOid, GSSLibStub paramGSSLibStub) {
/*  58 */     if (GSSUtil.NT_GSS_KRB5_PRINCIPAL.equals(paramOid)) {
/*  59 */       Oid[] arrayOfOid = null;
/*     */       try {
/*  61 */         arrayOfOid = paramGSSLibStub.inquireNamesForMech();
/*     */       } catch (GSSException localGSSException1) {
/*  63 */         if ((localGSSException1.getMajor() == 2) && (GSSUtil.isSpNegoMech(paramGSSLibStub.getMech())))
/*     */         {
/*     */           try
/*     */           {
/*  67 */             paramGSSLibStub = GSSLibStub.getInstance(GSSUtil.GSS_KRB5_MECH_OID);
/*     */ 
/*  69 */             arrayOfOid = paramGSSLibStub.inquireNamesForMech();
/*     */           }
/*     */           catch (GSSException localGSSException2) {
/*  72 */             SunNativeProvider.debug("Name type list unavailable: " + localGSSException2.getMajorString());
/*     */           }
/*     */         }
/*     */         else {
/*  76 */           SunNativeProvider.debug("Name type list unavailable: " + localGSSException1.getMajorString());
/*     */         }
/*     */       }
/*     */ 
/*  80 */       if (arrayOfOid != null) {
/*  81 */         for (int i = 0; i < arrayOfOid.length; i++) {
/*  82 */           if (arrayOfOid[i].equals(paramOid)) return paramOid;
/*     */         }
/*     */ 
/*  85 */         SunNativeProvider.debug("Override " + paramOid + " with mechanism default(null)");
/*     */ 
/*  87 */         return null;
/*     */       }
/*     */     }
/*  90 */     return paramOid;
/*     */   }
/*     */ 
/*     */   private GSSNameElement() {
/*  94 */     this.printableName = "<DEFAULT ACCEPTOR>";
/*     */   }
/*     */ 
/*     */   GSSNameElement(long paramLong, GSSLibStub paramGSSLibStub) throws GSSException {
/*  98 */     assert (paramGSSLibStub != null);
/*  99 */     if (paramLong == 0L) {
/* 100 */       throw new GSSException(3);
/*     */     }
/*     */ 
/* 103 */     this.pName = paramLong;
/* 104 */     this.cStub = paramGSSLibStub;
/* 105 */     setPrintables();
/*     */   }
/*     */ 
/*     */   GSSNameElement(byte[] paramArrayOfByte, Oid paramOid, GSSLibStub paramGSSLibStub) throws GSSException
/*     */   {
/* 110 */     assert (paramGSSLibStub != null);
/* 111 */     if (paramArrayOfByte == null) {
/* 112 */       throw new GSSException(3);
/*     */     }
/* 114 */     this.cStub = paramGSSLibStub;
/* 115 */     byte[] arrayOfByte1 = paramArrayOfByte;
/*     */ 
/* 117 */     if (paramOid != null)
/*     */     {
/* 120 */       paramOid = getNativeNameType(paramOid, paramGSSLibStub);
/*     */ 
/* 122 */       if (GSSName.NT_EXPORT_NAME.equals(paramOid))
/*     */       {
/* 126 */         byte[] arrayOfByte2 = null;
/* 127 */         DerOutputStream localDerOutputStream = new DerOutputStream();
/* 128 */         Oid localOid = this.cStub.getMech();
/*     */         try {
/* 130 */           localDerOutputStream.putOID(new ObjectIdentifier(localOid.toString()));
/*     */         } catch (IOException localIOException) {
/* 132 */           throw new GSSExceptionImpl(11, localIOException);
/*     */         }
/* 134 */         arrayOfByte2 = localDerOutputStream.toByteArray();
/* 135 */         arrayOfByte1 = new byte[4 + arrayOfByte2.length + 4 + paramArrayOfByte.length];
/* 136 */         int i = 0;
/* 137 */         arrayOfByte1[(i++)] = 4;
/* 138 */         arrayOfByte1[(i++)] = 1;
/* 139 */         arrayOfByte1[(i++)] = ((byte)(arrayOfByte2.length >>> 8));
/* 140 */         arrayOfByte1[(i++)] = ((byte)arrayOfByte2.length);
/* 141 */         System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i, arrayOfByte2.length);
/* 142 */         i += arrayOfByte2.length;
/* 143 */         arrayOfByte1[(i++)] = ((byte)(paramArrayOfByte.length >>> 24));
/* 144 */         arrayOfByte1[(i++)] = ((byte)(paramArrayOfByte.length >>> 16));
/* 145 */         arrayOfByte1[(i++)] = ((byte)(paramArrayOfByte.length >>> 8));
/* 146 */         arrayOfByte1[(i++)] = ((byte)paramArrayOfByte.length);
/* 147 */         System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, i, paramArrayOfByte.length);
/*     */       }
/*     */     }
/* 150 */     this.pName = this.cStub.importName(arrayOfByte1, paramOid);
/* 151 */     setPrintables();
/*     */ 
/* 153 */     SunNativeProvider.debug("Imported " + this.printableName + " w/ type " + this.printableType);
/*     */   }
/*     */ 
/*     */   private void setPrintables() throws GSSException
/*     */   {
/* 158 */     Object[] arrayOfObject = null;
/* 159 */     arrayOfObject = this.cStub.displayName(this.pName);
/* 160 */     assert ((arrayOfObject != null) && (arrayOfObject.length == 2));
/* 161 */     this.printableName = ((String)arrayOfObject[0]);
/* 162 */     assert (this.printableName != null);
/* 163 */     this.printableType = ((Oid)arrayOfObject[1]);
/* 164 */     if (this.printableType == null)
/* 165 */       this.printableType = GSSName.NT_USER_NAME;
/*     */   }
/*     */ 
/*     */   public String getKrbName()
/*     */     throws GSSException
/*     */   {
/* 171 */     long l = 0L;
/* 172 */     GSSLibStub localGSSLibStub = this.cStub;
/* 173 */     if (!GSSUtil.isKerberosMech(this.cStub.getMech())) {
/* 174 */       localGSSLibStub = GSSLibStub.getInstance(GSSUtil.GSS_KRB5_MECH_OID);
/*     */     }
/* 176 */     l = localGSSLibStub.canonicalizeName(this.pName);
/* 177 */     Object[] arrayOfObject = localGSSLibStub.displayName(l);
/* 178 */     localGSSLibStub.releaseName(l);
/* 179 */     SunNativeProvider.debug("Got kerberized name: " + arrayOfObject[0]);
/* 180 */     return (String)arrayOfObject[0];
/*     */   }
/*     */ 
/*     */   public Provider getProvider() {
/* 184 */     return SunNativeProvider.INSTANCE;
/*     */   }
/*     */ 
/*     */   public boolean equals(GSSNameSpi paramGSSNameSpi) throws GSSException {
/* 188 */     if (!(paramGSSNameSpi instanceof GSSNameElement)) {
/* 189 */       return false;
/*     */     }
/* 191 */     return this.cStub.compareName(this.pName, ((GSSNameElement)paramGSSNameSpi).pName);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 195 */     if (!(paramObject instanceof GSSNameElement))
/* 196 */       return false;
/*     */     try
/*     */     {
/* 199 */       return equals((GSSNameElement)paramObject); } catch (GSSException localGSSException) {
/*     */     }
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 206 */     return new Long(this.pName).hashCode();
/*     */   }
/*     */ 
/*     */   public byte[] export() throws GSSException {
/* 210 */     byte[] arrayOfByte1 = this.cStub.exportName(this.pName);
/*     */ 
/* 214 */     int i = 0;
/* 215 */     if ((arrayOfByte1[(i++)] != 4) || (arrayOfByte1[(i++)] != 1))
/*     */     {
/* 217 */       throw new GSSException(3);
/*     */     }
/* 219 */     int j = (0xFF & arrayOfByte1[(i++)]) << 8 | 0xFF & arrayOfByte1[(i++)];
/*     */ 
/* 221 */     ObjectIdentifier localObjectIdentifier = null;
/*     */     try {
/* 223 */       DerInputStream localDerInputStream = new DerInputStream(arrayOfByte1, i, j);
/*     */ 
/* 225 */       localObjectIdentifier = new ObjectIdentifier(localDerInputStream);
/*     */     } catch (IOException localIOException) {
/* 227 */       throw new GSSExceptionImpl(3, localIOException);
/*     */     }
/* 229 */     Oid localOid = new Oid(localObjectIdentifier.toString());
/* 230 */     assert (localOid.equals(getMechanism()));
/* 231 */     i += j;
/* 232 */     int k = (0xFF & arrayOfByte1[(i++)]) << 24 | (0xFF & arrayOfByte1[(i++)]) << 16 | (0xFF & arrayOfByte1[(i++)]) << 8 | 0xFF & arrayOfByte1[(i++)];
/*     */ 
/* 236 */     if (k < 0) {
/* 237 */       throw new GSSException(3);
/*     */     }
/* 239 */     byte[] arrayOfByte2 = new byte[k];
/* 240 */     System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, k);
/* 241 */     return arrayOfByte2;
/*     */   }
/*     */ 
/*     */   public Oid getMechanism() {
/* 245 */     return this.cStub.getMech();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 249 */     return this.printableName;
/*     */   }
/*     */ 
/*     */   public Oid getStringNameType() {
/* 253 */     return this.printableType;
/*     */   }
/*     */ 
/*     */   public boolean isAnonymousName() {
/* 257 */     return GSSName.NT_ANONYMOUS.equals(this.printableType);
/*     */   }
/*     */ 
/*     */   public void dispose() {
/* 261 */     if (this.pName != 0L) {
/* 262 */       this.cStub.releaseName(this.pName);
/* 263 */       this.pName = 0L;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize() throws Throwable {
/* 268 */     dispose();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.wrapper.GSSNameElement
 * JD-Core Version:    0.6.2
 */
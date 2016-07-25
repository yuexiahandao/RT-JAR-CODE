/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import sun.security.krb5.Asn1Exception;
/*     */ import sun.security.krb5.internal.util.KerberosFlags;
/*     */ import sun.security.util.BitArray;
/*     */ import sun.security.util.DerInputStream;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public class TicketFlags extends KerberosFlags
/*     */ {
/*     */   public TicketFlags()
/*     */   {
/*  59 */     super(32);
/*     */   }
/*     */ 
/*     */   public TicketFlags(boolean[] paramArrayOfBoolean) throws Asn1Exception {
/*  63 */     super(paramArrayOfBoolean);
/*  64 */     if (paramArrayOfBoolean.length > 32)
/*  65 */       throw new Asn1Exception(502);
/*     */   }
/*     */ 
/*     */   public TicketFlags(int paramInt, byte[] paramArrayOfByte) throws Asn1Exception
/*     */   {
/*  70 */     super(paramInt, paramArrayOfByte);
/*  71 */     if ((paramInt > paramArrayOfByte.length * 8) || (paramInt > 32))
/*  72 */       throw new Asn1Exception(502);
/*     */   }
/*     */ 
/*     */   public TicketFlags(DerValue paramDerValue) throws IOException, Asn1Exception {
/*  76 */     this(paramDerValue.getUnalignedBitString(true).toBooleanArray());
/*     */   }
/*     */ 
/*     */   public static TicketFlags parse(DerInputStream paramDerInputStream, byte paramByte, boolean paramBoolean)
/*     */     throws Asn1Exception, IOException
/*     */   {
/*  92 */     if ((paramBoolean) && (((byte)paramDerInputStream.peekByte() & 0x1F) != paramByte))
/*  93 */       return null;
/*  94 */     DerValue localDerValue1 = paramDerInputStream.getDerValue();
/*  95 */     if (paramByte != (localDerValue1.getTag() & 0x1F)) {
/*  96 */       throw new Asn1Exception(906);
/*     */     }
/*     */ 
/*  99 */     DerValue localDerValue2 = localDerValue1.getData().getDerValue();
/* 100 */     return new TicketFlags(localDerValue2);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try {
/* 106 */       return new TicketFlags(toBooleanArray());
/*     */     } catch (Exception localException) {
/*     */     }
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean match(LoginOptions paramLoginOptions)
/*     */   {
/* 114 */     boolean bool = false;
/*     */ 
/* 116 */     if ((get(1) == paramLoginOptions.get(1)) && 
/* 117 */       (get(3) == paramLoginOptions.get(3)) && 
/* 118 */       (get(8) == paramLoginOptions.get(8))) {
/* 119 */       bool = true;
/*     */     }
/*     */ 
/* 123 */     return bool;
/*     */   }
/*     */   public boolean match(TicketFlags paramTicketFlags) {
/* 126 */     boolean bool = true;
/* 127 */     for (int i = 0; i <= 31; i++) {
/* 128 */       if (get(i) != paramTicketFlags.get(i)) {
/* 129 */         return false;
/*     */       }
/*     */     }
/* 132 */     return bool;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     StringBuffer localStringBuffer = new StringBuffer();
/* 141 */     boolean[] arrayOfBoolean = toBooleanArray();
/* 142 */     for (int i = 0; i < arrayOfBoolean.length; i++) {
/* 143 */       if (arrayOfBoolean[i] == 1) {
/* 144 */         switch (i) {
/*     */         case 0:
/* 146 */           localStringBuffer.append("RESERVED;");
/* 147 */           break;
/*     */         case 1:
/* 149 */           localStringBuffer.append("FORWARDABLE;");
/* 150 */           break;
/*     */         case 2:
/* 152 */           localStringBuffer.append("FORWARDED;");
/* 153 */           break;
/*     */         case 3:
/* 155 */           localStringBuffer.append("PROXIABLE;");
/* 156 */           break;
/*     */         case 4:
/* 158 */           localStringBuffer.append("PROXY;");
/* 159 */           break;
/*     */         case 5:
/* 161 */           localStringBuffer.append("MAY-POSTDATE;");
/* 162 */           break;
/*     */         case 6:
/* 164 */           localStringBuffer.append("POSTDATED;");
/* 165 */           break;
/*     */         case 7:
/* 167 */           localStringBuffer.append("INVALID;");
/* 168 */           break;
/*     */         case 8:
/* 170 */           localStringBuffer.append("RENEWABLE;");
/* 171 */           break;
/*     */         case 9:
/* 173 */           localStringBuffer.append("INITIAL;");
/* 174 */           break;
/*     */         case 10:
/* 176 */           localStringBuffer.append("PRE-AUTHENT;");
/* 177 */           break;
/*     */         case 11:
/* 179 */           localStringBuffer.append("HW-AUTHENT;");
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 184 */     String str = localStringBuffer.toString();
/* 185 */     if (str.length() > 0) {
/* 186 */       str = str.substring(0, str.length() - 1);
/*     */     }
/* 188 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.TicketFlags
 * JD-Core Version:    0.6.2
 */
/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import sun.security.krb5.KrbException;
/*     */ import sun.security.krb5.PrincipalName;
/*     */ import sun.security.krb5.Realm;
/*     */ import sun.security.util.DerValue;
/*     */ 
/*     */ public final class KerberosPrincipal
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7374788026156829911L;
/*     */   public static final int KRB_NT_UNKNOWN = 0;
/*     */   public static final int KRB_NT_PRINCIPAL = 1;
/*     */   public static final int KRB_NT_SRV_INST = 2;
/*     */   public static final int KRB_NT_SRV_HST = 3;
/*     */   public static final int KRB_NT_SRV_XHST = 4;
/*     */   public static final int KRB_NT_UID = 5;
/*     */   private transient String fullName;
/*     */   private transient String realm;
/*     */   private transient int nameType;
/*     */   private static final char NAME_REALM_SEPARATOR = '@';
/*     */ 
/*     */   public KerberosPrincipal(String paramString)
/*     */   {
/* 119 */     PrincipalName localPrincipalName = null;
/*     */     try
/*     */     {
/* 123 */       localPrincipalName = new PrincipalName(paramString, 1);
/*     */     } catch (KrbException localKrbException) {
/* 125 */       throw new IllegalArgumentException(localKrbException.getMessage());
/*     */     }
/* 127 */     this.nameType = 1;
/* 128 */     this.fullName = localPrincipalName.toString();
/* 129 */     this.realm = localPrincipalName.getRealmString();
/*     */   }
/*     */ 
/*     */   public KerberosPrincipal(String paramString, int paramInt)
/*     */   {
/* 162 */     PrincipalName localPrincipalName = null;
/*     */     try
/*     */     {
/* 166 */       localPrincipalName = new PrincipalName(paramString, paramInt);
/*     */     } catch (KrbException localKrbException) {
/* 168 */       throw new IllegalArgumentException(localKrbException.getMessage());
/*     */     }
/*     */ 
/* 171 */     this.nameType = paramInt;
/* 172 */     this.fullName = localPrincipalName.toString();
/* 173 */     this.realm = localPrincipalName.getRealmString();
/*     */   }
/*     */ 
/*     */   public String getRealm()
/*     */   {
/* 181 */     return this.realm;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 194 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 212 */     if (paramObject == this) {
/* 213 */       return true;
/*     */     }
/* 215 */     if (!(paramObject instanceof KerberosPrincipal)) {
/* 216 */       return false;
/*     */     }
/* 218 */     String str1 = getName();
/* 219 */     String str2 = ((KerberosPrincipal)paramObject).getName();
/* 220 */     if ((this.nameType == ((KerberosPrincipal)paramObject).nameType) && (str1.equals(str2)))
/*     */     {
/* 222 */       return true;
/*     */     }
/*     */ 
/* 225 */     return false;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 240 */     PrincipalName localPrincipalName = null;
/*     */     try {
/* 242 */       localPrincipalName = new PrincipalName(this.fullName, this.nameType);
/* 243 */       paramObjectOutputStream.writeObject(localPrincipalName.asn1Encode());
/* 244 */       paramObjectOutputStream.writeObject(localPrincipalName.getRealm().asn1Encode());
/*     */     } catch (Exception localException) {
/* 246 */       IOException localIOException = new IOException(localException.getMessage());
/* 247 */       localIOException.initCause(localException);
/* 248 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 258 */     byte[] arrayOfByte1 = (byte[])paramObjectInputStream.readObject();
/* 259 */     byte[] arrayOfByte2 = (byte[])paramObjectInputStream.readObject();
/*     */     try {
/* 261 */       PrincipalName localPrincipalName = new PrincipalName(new DerValue(arrayOfByte1));
/*     */ 
/* 263 */       this.realm = new Realm(new DerValue(arrayOfByte2)).toString();
/* 264 */       this.fullName = (localPrincipalName.toString() + '@' + this.realm.toString());
/*     */ 
/* 266 */       this.nameType = localPrincipalName.getNameType();
/*     */     } catch (Exception localException) {
/* 268 */       IOException localIOException = new IOException(localException.getMessage());
/* 269 */       localIOException.initCause(localException);
/* 270 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 282 */     return this.fullName;
/*     */   }
/*     */ 
/*     */   public int getNameType()
/*     */   {
/* 295 */     return this.nameType;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 300 */     return getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.KerberosPrincipal
 * JD-Core Version:    0.6.2
 */
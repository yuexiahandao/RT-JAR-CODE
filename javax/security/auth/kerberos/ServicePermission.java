/*     */ package javax.security.auth.kerberos;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ 
/*     */ public final class ServicePermission extends Permission
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -1227585031618624935L;
/*     */   private static final int INITIATE = 1;
/*     */   private static final int ACCEPT = 2;
/*     */   private static final int ALL = 3;
/*     */   private static final int NONE = 0;
/*     */   private transient int mask;
/*     */   private String actions;
/*     */ 
/*     */   public ServicePermission(String paramString1, String paramString2)
/*     */   {
/* 148 */     super(paramString1);
/* 149 */     init(paramString1, getMask(paramString2));
/*     */   }
/*     */ 
/*     */   private void init(String paramString, int paramInt)
/*     */   {
/* 158 */     if (paramString == null) {
/* 159 */       throw new NullPointerException("service principal can't be null");
/*     */     }
/* 161 */     if ((paramInt & 0x3) != paramInt) {
/* 162 */       throw new IllegalArgumentException("invalid actions mask");
/*     */     }
/* 164 */     this.mask = paramInt;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 179 */     if (!(paramPermission instanceof ServicePermission)) {
/* 180 */       return false;
/*     */     }
/* 182 */     ServicePermission localServicePermission = (ServicePermission)paramPermission;
/*     */ 
/* 184 */     return ((this.mask & localServicePermission.mask) == localServicePermission.mask) && (impliesIgnoreMask(localServicePermission));
/*     */   }
/*     */ 
/*     */   boolean impliesIgnoreMask(ServicePermission paramServicePermission)
/*     */   {
/* 190 */     return (getName().equals("*")) || (getName().equals(paramServicePermission.getName()));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 204 */     if (paramObject == this) {
/* 205 */       return true;
/*     */     }
/* 207 */     if (!(paramObject instanceof ServicePermission)) {
/* 208 */       return false;
/*     */     }
/* 210 */     ServicePermission localServicePermission = (ServicePermission)paramObject;
/* 211 */     return ((this.mask & localServicePermission.mask) == localServicePermission.mask) && (getName().equals(localServicePermission.getName()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 224 */     return getName().hashCode() ^ this.mask;
/*     */   }
/*     */ 
/*     */   private static String getActions(int paramInt)
/*     */   {
/* 239 */     StringBuilder localStringBuilder = new StringBuilder();
/* 240 */     int i = 0;
/*     */ 
/* 242 */     if ((paramInt & 0x1) == 1) {
/* 243 */       if (i != 0) localStringBuilder.append(','); else
/* 244 */         i = 1;
/* 245 */       localStringBuilder.append("initiate");
/*     */     }
/*     */ 
/* 248 */     if ((paramInt & 0x2) == 2) {
/* 249 */       if (i != 0) localStringBuilder.append(','); else
/* 250 */         i = 1;
/* 251 */       localStringBuilder.append("accept");
/*     */     }
/*     */ 
/* 254 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 264 */     if (this.actions == null) {
/* 265 */       this.actions = getActions(this.mask);
/*     */     }
/* 267 */     return this.actions;
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 285 */     return new KrbServicePermissionCollection();
/*     */   }
/*     */ 
/*     */   int getMask()
/*     */   {
/* 295 */     return this.mask;
/*     */   }
/*     */ 
/*     */   private static int getMask(String paramString)
/*     */   {
/* 307 */     if (paramString == null) {
/* 308 */       throw new NullPointerException("action can't be null");
/*     */     }
/*     */ 
/* 311 */     if (paramString.equals("")) {
/* 312 */       throw new IllegalArgumentException("action can't be empty");
/*     */     }
/*     */ 
/* 315 */     int i = 0;
/*     */ 
/* 317 */     char[] arrayOfChar = paramString.toCharArray();
/*     */ 
/* 319 */     int j = arrayOfChar.length - 1;
/* 320 */     if (j < 0) {
/* 321 */       return i;
/*     */     }
/* 323 */     while (j != -1)
/*     */     {
/*     */       int k;
/* 327 */       while ((j != -1) && (((k = arrayOfChar[j]) == ' ') || (k == 13) || (k == 10) || (k == 12) || (k == 9)))
/*     */       {
/* 332 */         j--;
/*     */       }
/*     */       int m;
/* 337 */       if ((j >= 7) && ((arrayOfChar[(j - 7)] == 'i') || (arrayOfChar[(j - 7)] == 'I')) && ((arrayOfChar[(j - 6)] == 'n') || (arrayOfChar[(j - 6)] == 'N')) && ((arrayOfChar[(j - 5)] == 'i') || (arrayOfChar[(j - 5)] == 'I')) && ((arrayOfChar[(j - 4)] == 't') || (arrayOfChar[(j - 4)] == 'T')) && ((arrayOfChar[(j - 3)] == 'i') || (arrayOfChar[(j - 3)] == 'I')) && ((arrayOfChar[(j - 2)] == 'a') || (arrayOfChar[(j - 2)] == 'A')) && ((arrayOfChar[(j - 1)] == 't') || (arrayOfChar[(j - 1)] == 'T')) && ((arrayOfChar[j] == 'e') || (arrayOfChar[j] == 'E')))
/*     */       {
/* 346 */         m = 8;
/* 347 */         i |= 1;
/*     */       }
/* 349 */       else if ((j >= 5) && ((arrayOfChar[(j - 5)] == 'a') || (arrayOfChar[(j - 5)] == 'A')) && ((arrayOfChar[(j - 4)] == 'c') || (arrayOfChar[(j - 4)] == 'C')) && ((arrayOfChar[(j - 3)] == 'c') || (arrayOfChar[(j - 3)] == 'C')) && ((arrayOfChar[(j - 2)] == 'e') || (arrayOfChar[(j - 2)] == 'E')) && ((arrayOfChar[(j - 1)] == 'p') || (arrayOfChar[(j - 1)] == 'P')) && ((arrayOfChar[j] == 't') || (arrayOfChar[j] == 'T')))
/*     */       {
/* 356 */         m = 6;
/* 357 */         i |= 2;
/*     */       }
/*     */       else
/*     */       {
/* 361 */         throw new IllegalArgumentException("invalid permission: " + paramString);
/*     */       }
/*     */ 
/* 367 */       int n = 0;
/* 368 */       while ((j >= m) && (n == 0)) {
/* 369 */         switch (arrayOfChar[(j - m)]) {
/*     */         case ',':
/* 371 */           n = 1;
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case ' ':
/* 375 */           break;
/*     */         default:
/* 377 */           throw new IllegalArgumentException("invalid permission: " + paramString);
/*     */         }
/*     */ 
/* 380 */         j--;
/*     */       }
/*     */ 
/* 384 */       j -= m;
/*     */     }
/*     */ 
/* 387 */     return i;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 401 */     if (this.actions == null)
/* 402 */       getActions();
/* 403 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 414 */     paramObjectInputStream.defaultReadObject();
/* 415 */     init(getName(), getMask(this.actions));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.kerberos.ServicePermission
 * JD-Core Version:    0.6.2
 */
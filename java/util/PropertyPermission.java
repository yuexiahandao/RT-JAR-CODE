/*     */ package java.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.security.BasicPermission;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ 
/*     */ public final class PropertyPermission extends BasicPermission
/*     */ {
/*     */   private static final int READ = 1;
/*     */   private static final int WRITE = 2;
/*     */   private static final int ALL = 3;
/*     */   private static final int NONE = 0;
/*     */   private transient int mask;
/*     */   private String actions;
/*     */   private static final long serialVersionUID = 885438825399942851L;
/*     */ 
/*     */   private void init(int paramInt)
/*     */   {
/* 137 */     if ((paramInt & 0x3) != paramInt) {
/* 138 */       throw new IllegalArgumentException("invalid actions mask");
/*     */     }
/* 140 */     if (paramInt == 0) {
/* 141 */       throw new IllegalArgumentException("invalid actions mask");
/*     */     }
/* 143 */     if (getName() == null) {
/* 144 */       throw new NullPointerException("name can't be null");
/*     */     }
/* 146 */     this.mask = paramInt;
/*     */   }
/*     */ 
/*     */   public PropertyPermission(String paramString1, String paramString2)
/*     */   {
/* 166 */     super(paramString1, paramString2);
/* 167 */     init(getMask(paramString2));
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 188 */     if (!(paramPermission instanceof PropertyPermission)) {
/* 189 */       return false;
/*     */     }
/* 191 */     PropertyPermission localPropertyPermission = (PropertyPermission)paramPermission;
/*     */ 
/* 196 */     return ((this.mask & localPropertyPermission.mask) == localPropertyPermission.mask) && (super.implies(localPropertyPermission));
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 209 */     if (paramObject == this) {
/* 210 */       return true;
/*     */     }
/* 212 */     if (!(paramObject instanceof PropertyPermission)) {
/* 213 */       return false;
/*     */     }
/* 215 */     PropertyPermission localPropertyPermission = (PropertyPermission)paramObject;
/*     */ 
/* 217 */     return (this.mask == localPropertyPermission.mask) && (getName().equals(localPropertyPermission.getName()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 231 */     return getName().hashCode();
/*     */   }
/*     */ 
/*     */   private static int getMask(String paramString)
/*     */   {
/* 243 */     int i = 0;
/*     */ 
/* 245 */     if (paramString == null) {
/* 246 */       return i;
/*     */     }
/*     */ 
/* 250 */     if (paramString == "read")
/* 251 */       return 1;
/* 252 */     if (paramString == "write")
/* 253 */       return 2;
/* 254 */     if (paramString == "read,write") {
/* 255 */       return 3;
/*     */     }
/*     */ 
/* 258 */     char[] arrayOfChar = paramString.toCharArray();
/*     */ 
/* 260 */     int j = arrayOfChar.length - 1;
/* 261 */     if (j < 0) {
/* 262 */       return i;
/*     */     }
/* 264 */     while (j != -1)
/*     */     {
/*     */       int k;
/* 268 */       while ((j != -1) && (((k = arrayOfChar[j]) == ' ') || (k == 13) || (k == 10) || (k == 12) || (k == 9)))
/*     */       {
/* 273 */         j--;
/*     */       }
/*     */       int m;
/* 278 */       if ((j >= 3) && ((arrayOfChar[(j - 3)] == 'r') || (arrayOfChar[(j - 3)] == 'R')) && ((arrayOfChar[(j - 2)] == 'e') || (arrayOfChar[(j - 2)] == 'E')) && ((arrayOfChar[(j - 1)] == 'a') || (arrayOfChar[(j - 1)] == 'A')) && ((arrayOfChar[j] == 'd') || (arrayOfChar[j] == 'D')))
/*     */       {
/* 283 */         m = 4;
/* 284 */         i |= 1;
/*     */       }
/* 286 */       else if ((j >= 4) && ((arrayOfChar[(j - 4)] == 'w') || (arrayOfChar[(j - 4)] == 'W')) && ((arrayOfChar[(j - 3)] == 'r') || (arrayOfChar[(j - 3)] == 'R')) && ((arrayOfChar[(j - 2)] == 'i') || (arrayOfChar[(j - 2)] == 'I')) && ((arrayOfChar[(j - 1)] == 't') || (arrayOfChar[(j - 1)] == 'T')) && ((arrayOfChar[j] == 'e') || (arrayOfChar[j] == 'E')))
/*     */       {
/* 292 */         m = 5;
/* 293 */         i |= 2;
/*     */       }
/*     */       else
/*     */       {
/* 297 */         throw new IllegalArgumentException("invalid permission: " + paramString);
/*     */       }
/*     */ 
/* 303 */       int n = 0;
/* 304 */       while ((j >= m) && (n == 0)) {
/* 305 */         switch (arrayOfChar[(j - m)]) {
/*     */         case ',':
/* 307 */           n = 1;
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case ' ':
/* 311 */           break;
/*     */         default:
/* 313 */           throw new IllegalArgumentException("invalid permission: " + paramString);
/*     */         }
/*     */ 
/* 316 */         j--;
/*     */       }
/*     */ 
/* 320 */       j -= m;
/*     */     }
/*     */ 
/* 323 */     return i;
/*     */   }
/*     */ 
/*     */   static String getActions(int paramInt)
/*     */   {
/* 336 */     StringBuilder localStringBuilder = new StringBuilder();
/* 337 */     int i = 0;
/*     */ 
/* 339 */     if ((paramInt & 0x1) == 1) {
/* 340 */       i = 1;
/* 341 */       localStringBuilder.append("read");
/*     */     }
/*     */ 
/* 344 */     if ((paramInt & 0x2) == 2) {
/* 345 */       if (i != 0) localStringBuilder.append(','); else
/* 346 */         i = 1;
/* 347 */       localStringBuilder.append("write");
/*     */     }
/* 349 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public String getActions()
/*     */   {
/* 363 */     if (this.actions == null) {
/* 364 */       this.actions = getActions(this.mask);
/*     */     }
/* 366 */     return this.actions;
/*     */   }
/*     */ 
/*     */   int getMask()
/*     */   {
/* 377 */     return this.mask;
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection()
/*     */   {
/* 390 */     return new PropertyPermissionCollection();
/*     */   }
/*     */ 
/*     */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 406 */     if (this.actions == null)
/* 407 */       getActions();
/* 408 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 419 */     paramObjectInputStream.defaultReadObject();
/* 420 */     init(getMask(this.actions));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.PropertyPermission
 * JD-Core Version:    0.6.2
 */
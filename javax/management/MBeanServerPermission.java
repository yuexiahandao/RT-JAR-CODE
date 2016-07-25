/*     */ package javax.management;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.security.BasicPermission;
/*     */ import java.security.Permission;
/*     */ import java.security.PermissionCollection;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class MBeanServerPermission extends BasicPermission
/*     */ {
/*     */   private static final long serialVersionUID = -5661980843569388590L;
/*     */   private static final int CREATE = 0;
/*     */   private static final int FIND = 1;
/*     */   private static final int NEW = 2;
/*     */   private static final int RELEASE = 3;
/*     */   private static final int N_NAMES = 4;
/*  79 */   private static final String[] names = { "createMBeanServer", "findMBeanServer", "newMBeanServer", "releaseMBeanServer" };
/*     */   private static final int CREATE_MASK = 1;
/*     */   private static final int FIND_MASK = 2;
/*     */   private static final int NEW_MASK = 4;
/*     */   private static final int RELEASE_MASK = 8;
/*     */   private static final int ALL_MASK = 15;
/* 101 */   private static final String[] canonicalNames = new String[16];
/*     */   transient int mask;
/*     */ 
/*     */   public MBeanServerPermission(String paramString)
/*     */   {
/* 124 */     this(paramString, null);
/*     */   }
/*     */ 
/*     */   public MBeanServerPermission(String paramString1, String paramString2)
/*     */   {
/* 144 */     super(getCanonicalName(parseMask(paramString1)), paramString2);
/*     */ 
/* 151 */     this.mask = parseMask(paramString1);
/*     */ 
/* 154 */     if ((paramString2 != null) && (paramString2.length() > 0))
/* 155 */       throw new IllegalArgumentException("MBeanServerPermission actions must be null: " + paramString2);
/*     */   }
/*     */ 
/*     */   MBeanServerPermission(int paramInt)
/*     */   {
/* 161 */     super(getCanonicalName(paramInt));
/* 162 */     this.mask = impliedMask(paramInt);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 167 */     paramObjectInputStream.defaultReadObject();
/* 168 */     this.mask = parseMask(getName());
/*     */   }
/*     */ 
/*     */   static int simplifyMask(int paramInt) {
/* 172 */     if ((paramInt & 0x1) != 0)
/* 173 */       paramInt &= -5;
/* 174 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static int impliedMask(int paramInt) {
/* 178 */     if ((paramInt & 0x1) != 0)
/* 179 */       paramInt |= 4;
/* 180 */     return paramInt;
/*     */   }
/*     */ 
/*     */   static String getCanonicalName(int paramInt) {
/* 184 */     if (paramInt == 15) {
/* 185 */       return "*";
/*     */     }
/* 187 */     paramInt = simplifyMask(paramInt);
/*     */ 
/* 189 */     synchronized (canonicalNames) {
/* 190 */       if (canonicalNames[paramInt] == null) {
/* 191 */         canonicalNames[paramInt] = makeCanonicalName(paramInt);
/*     */       }
/*     */     }
/* 194 */     return canonicalNames[paramInt];
/*     */   }
/*     */ 
/*     */   private static String makeCanonicalName(int paramInt) {
/* 198 */     StringBuilder localStringBuilder = new StringBuilder();
/* 199 */     for (int i = 0; i < 4; i++) {
/* 200 */       if ((paramInt & 1 << i) != 0) {
/* 201 */         if (localStringBuilder.length() > 0)
/* 202 */           localStringBuilder.append(',');
/* 203 */         localStringBuilder.append(names[i]);
/*     */       }
/*     */     }
/* 206 */     return localStringBuilder.toString().intern();
/*     */   }
/*     */ 
/*     */   private static int parseMask(String paramString)
/*     */   {
/* 216 */     if (paramString == null) {
/* 217 */       throw new NullPointerException("MBeanServerPermission: target name can't be null");
/*     */     }
/*     */ 
/* 221 */     paramString = paramString.trim();
/* 222 */     if (paramString.equals("*")) {
/* 223 */       return 15;
/*     */     }
/*     */ 
/* 226 */     if (paramString.indexOf(',') < 0) {
/* 227 */       return impliedMask(1 << nameIndex(paramString.trim()));
/*     */     }
/* 229 */     int i = 0;
/*     */ 
/* 231 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/* 232 */     while (localStringTokenizer.hasMoreTokens()) {
/* 233 */       String str = localStringTokenizer.nextToken();
/* 234 */       int j = nameIndex(str.trim());
/* 235 */       i |= 1 << j;
/*     */     }
/*     */ 
/* 238 */     return impliedMask(i);
/*     */   }
/*     */ 
/*     */   private static int nameIndex(String paramString) throws IllegalArgumentException
/*     */   {
/* 243 */     for (int i = 0; i < 4; i++) {
/* 244 */       if (names[i].equals(paramString))
/* 245 */         return i;
/*     */     }
/* 247 */     String str = "Invalid MBeanServerPermission name: \"" + paramString + "\"";
/*     */ 
/* 249 */     throw new IllegalArgumentException(str);
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/* 253 */     return this.mask;
/*     */   }
/*     */ 
/*     */   public boolean implies(Permission paramPermission)
/*     */   {
/* 276 */     if (!(paramPermission instanceof MBeanServerPermission)) {
/* 277 */       return false;
/*     */     }
/* 279 */     MBeanServerPermission localMBeanServerPermission = (MBeanServerPermission)paramPermission;
/*     */ 
/* 281 */     return (this.mask & localMBeanServerPermission.mask) == localMBeanServerPermission.mask;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 293 */     if (paramObject == this) {
/* 294 */       return true;
/*     */     }
/* 296 */     if (!(paramObject instanceof MBeanServerPermission)) {
/* 297 */       return false;
/*     */     }
/* 299 */     MBeanServerPermission localMBeanServerPermission = (MBeanServerPermission)paramObject;
/*     */ 
/* 301 */     return this.mask == localMBeanServerPermission.mask;
/*     */   }
/*     */ 
/*     */   public PermissionCollection newPermissionCollection() {
/* 305 */     return new MBeanServerPermissionCollection();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanServerPermission
 * JD-Core Version:    0.6.2
 */
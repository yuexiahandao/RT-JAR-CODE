/*     */ package java.nio.file.attribute;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ public final class PosixFilePermissions
/*     */ {
/*     */   private static void writeBits(StringBuilder paramStringBuilder, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/*  43 */     if (paramBoolean1)
/*  44 */       paramStringBuilder.append('r');
/*     */     else {
/*  46 */       paramStringBuilder.append('-');
/*     */     }
/*  48 */     if (paramBoolean2)
/*  49 */       paramStringBuilder.append('w');
/*     */     else {
/*  51 */       paramStringBuilder.append('-');
/*     */     }
/*  53 */     if (paramBoolean3)
/*  54 */       paramStringBuilder.append('x');
/*     */     else
/*  56 */       paramStringBuilder.append('-');
/*     */   }
/*     */ 
/*     */   public static String toString(Set<PosixFilePermission> paramSet)
/*     */   {
/*  74 */     StringBuilder localStringBuilder = new StringBuilder(9);
/*  75 */     writeBits(localStringBuilder, paramSet.contains(PosixFilePermission.OWNER_READ), paramSet.contains(PosixFilePermission.OWNER_WRITE), paramSet.contains(PosixFilePermission.OWNER_EXECUTE));
/*     */ 
/*  77 */     writeBits(localStringBuilder, paramSet.contains(PosixFilePermission.GROUP_READ), paramSet.contains(PosixFilePermission.GROUP_WRITE), paramSet.contains(PosixFilePermission.GROUP_EXECUTE));
/*     */ 
/*  79 */     writeBits(localStringBuilder, paramSet.contains(PosixFilePermission.OTHERS_READ), paramSet.contains(PosixFilePermission.OTHERS_WRITE), paramSet.contains(PosixFilePermission.OTHERS_EXECUTE));
/*     */ 
/*  81 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isSet(char paramChar1, char paramChar2) {
/*  85 */     if (paramChar1 == paramChar2)
/*  86 */       return true;
/*  87 */     if (paramChar1 == '-')
/*  88 */       return false;
/*  89 */     throw new IllegalArgumentException("Invalid mode");
/*     */   }
/*  91 */   private static boolean isR(char paramChar) { return isSet(paramChar, 'r'); } 
/*  92 */   private static boolean isW(char paramChar) { return isSet(paramChar, 'w'); } 
/*  93 */   private static boolean isX(char paramChar) { return isSet(paramChar, 'x'); }
/*     */ 
/*     */ 
/*     */   public static Set<PosixFilePermission> fromString(String paramString)
/*     */   {
/* 127 */     if (paramString.length() != 9)
/* 128 */       throw new IllegalArgumentException("Invalid mode");
/* 129 */     EnumSet localEnumSet = EnumSet.noneOf(PosixFilePermission.class);
/* 130 */     if (isR(paramString.charAt(0))) localEnumSet.add(PosixFilePermission.OWNER_READ);
/* 131 */     if (isW(paramString.charAt(1))) localEnumSet.add(PosixFilePermission.OWNER_WRITE);
/* 132 */     if (isX(paramString.charAt(2))) localEnumSet.add(PosixFilePermission.OWNER_EXECUTE);
/* 133 */     if (isR(paramString.charAt(3))) localEnumSet.add(PosixFilePermission.GROUP_READ);
/* 134 */     if (isW(paramString.charAt(4))) localEnumSet.add(PosixFilePermission.GROUP_WRITE);
/* 135 */     if (isX(paramString.charAt(5))) localEnumSet.add(PosixFilePermission.GROUP_EXECUTE);
/* 136 */     if (isR(paramString.charAt(6))) localEnumSet.add(PosixFilePermission.OTHERS_READ);
/* 137 */     if (isW(paramString.charAt(7))) localEnumSet.add(PosixFilePermission.OTHERS_WRITE);
/* 138 */     if (isX(paramString.charAt(8))) localEnumSet.add(PosixFilePermission.OTHERS_EXECUTE);
/* 139 */     return localEnumSet;
/*     */   }
/*     */ 
/*     */   public static FileAttribute<Set<PosixFilePermission>> asFileAttribute(Set<PosixFilePermission> paramSet)
/*     */   {
/* 163 */     paramSet = new HashSet(paramSet);
/* 164 */     for (Object localObject = paramSet.iterator(); ((Iterator)localObject).hasNext(); ) { PosixFilePermission localPosixFilePermission = (PosixFilePermission)((Iterator)localObject).next();
/* 165 */       if (localPosixFilePermission == null)
/* 166 */         throw new NullPointerException();
/*     */     }
/* 168 */     localObject = paramSet;
/* 169 */     return new FileAttribute()
/*     */     {
/*     */       public String name() {
/* 172 */         return "posix:permissions";
/*     */       }
/*     */ 
/*     */       public Set<PosixFilePermission> value() {
/* 176 */         return Collections.unmodifiableSet(this.val$value);
/*     */       }
/*     */     };
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.file.attribute.PosixFilePermissions
 * JD-Core Version:    0.6.2
 */
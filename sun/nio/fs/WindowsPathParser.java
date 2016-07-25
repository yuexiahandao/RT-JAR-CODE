/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.nio.file.InvalidPathException;
/*     */ 
/*     */ class WindowsPathParser
/*     */ {
/*     */   private static final String reservedChars = "<>:\"|?*";
/*     */ 
/*     */   static Result parse(String paramString)
/*     */   {
/*  77 */     return parse(paramString, true);
/*     */   }
/*     */ 
/*     */   static Result parseNormalizedPath(String paramString)
/*     */   {
/*  85 */     return parse(paramString, false);
/*     */   }
/*     */ 
/*     */   private static Result parse(String paramString, boolean paramBoolean)
/*     */   {
/*  95 */     String str1 = "";
/*  96 */     WindowsPathType localWindowsPathType = null;
/*     */ 
/*  98 */     int i = paramString.length();
/*  99 */     int j = 0;
/* 100 */     if (i > 1) {
/* 101 */       char c1 = paramString.charAt(0);
/* 102 */       char c2 = paramString.charAt(1);
/* 103 */       int k = 0;
/* 104 */       int m = 2;
/* 105 */       if ((isSlash(c1)) && (isSlash(c2)))
/*     */       {
/* 109 */         localWindowsPathType = WindowsPathType.UNC;
/* 110 */         j = nextNonSlash(paramString, m, i);
/* 111 */         m = nextSlash(paramString, j, i);
/* 112 */         if (j == m)
/* 113 */           throw new InvalidPathException(paramString, "UNC path is missing hostname");
/* 114 */         String str2 = paramString.substring(j, m);
/* 115 */         j = nextNonSlash(paramString, m, i);
/* 116 */         m = nextSlash(paramString, j, i);
/* 117 */         if (j == m)
/* 118 */           throw new InvalidPathException(paramString, "UNC path is missing sharename");
/* 119 */         str1 = "\\\\" + str2 + "\\" + paramString.substring(j, m) + "\\";
/* 120 */         j = m;
/*     */       }
/* 122 */       else if ((isLetter(c1)) && (c2 == ':'))
/*     */       {
/*     */         int n;
/* 124 */         if ((i > 2) && (isSlash(n = paramString.charAt(2))))
/*     */         {
/* 126 */           if (n == 92)
/* 127 */             str1 = paramString.substring(0, 3);
/*     */           else {
/* 129 */             str1 = paramString.substring(0, 2) + '\\';
/*     */           }
/* 131 */           j = 3;
/* 132 */           localWindowsPathType = WindowsPathType.ABSOLUTE;
/*     */         } else {
/* 134 */           str1 = paramString.substring(0, 2);
/* 135 */           j = 2;
/* 136 */           localWindowsPathType = WindowsPathType.DRIVE_RELATIVE;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 141 */     if (j == 0) {
/* 142 */       if ((i > 0) && (isSlash(paramString.charAt(0)))) {
/* 143 */         localWindowsPathType = WindowsPathType.DIRECTORY_RELATIVE;
/* 144 */         str1 = "\\";
/*     */       } else {
/* 146 */         localWindowsPathType = WindowsPathType.RELATIVE;
/*     */       }
/*     */     }
/*     */ 
/* 150 */     if (paramBoolean) {
/* 151 */       StringBuilder localStringBuilder = new StringBuilder(paramString.length());
/* 152 */       localStringBuilder.append(str1);
/* 153 */       return new Result(localWindowsPathType, str1, normalize(localStringBuilder, paramString, j));
/*     */     }
/* 155 */     return new Result(localWindowsPathType, str1, paramString);
/*     */   }
/*     */ 
/*     */   private static String normalize(StringBuilder paramStringBuilder, String paramString, int paramInt)
/*     */   {
/* 164 */     int i = paramString.length();
/* 165 */     paramInt = nextNonSlash(paramString, paramInt, i);
/* 166 */     int j = paramInt;
/* 167 */     char c1 = '\000';
/* 168 */     while (paramInt < i) {
/* 169 */       char c2 = paramString.charAt(paramInt);
/* 170 */       if (isSlash(c2)) {
/* 171 */         if (c1 == ' ') {
/* 172 */           throw new InvalidPathException(paramString, "Trailing char <" + c1 + ">", paramInt - 1);
/*     */         }
/*     */ 
/* 175 */         paramStringBuilder.append(paramString, j, paramInt);
/* 176 */         paramInt = nextNonSlash(paramString, paramInt, i);
/* 177 */         if (paramInt != i)
/* 178 */           paramStringBuilder.append('\\');
/* 179 */         j = paramInt;
/*     */       } else {
/* 181 */         if (isInvalidPathChar(c2)) {
/* 182 */           throw new InvalidPathException(paramString, "Illegal char <" + c2 + ">", paramInt);
/*     */         }
/*     */ 
/* 185 */         c1 = c2;
/* 186 */         paramInt++;
/*     */       }
/*     */     }
/* 189 */     if (j != paramInt) {
/* 190 */       if (c1 == ' ') {
/* 191 */         throw new InvalidPathException(paramString, "Trailing char <" + c1 + ">", paramInt - 1);
/*     */       }
/*     */ 
/* 194 */       paramStringBuilder.append(paramString, j, paramInt);
/*     */     }
/* 196 */     return paramStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static final boolean isSlash(char paramChar) {
/* 200 */     return (paramChar == '\\') || (paramChar == '/');
/*     */   }
/*     */ 
/*     */   private static final int nextNonSlash(String paramString, int paramInt1, int paramInt2) {
/* 204 */     while ((paramInt1 < paramInt2) && (isSlash(paramString.charAt(paramInt1)))) paramInt1++;
/* 205 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   private static final int nextSlash(String paramString, int paramInt1, int paramInt2)
/*     */   {
/*     */     char c;
/* 210 */     while ((paramInt1 < paramInt2) && (!isSlash(c = paramString.charAt(paramInt1)))) {
/* 211 */       if (isInvalidPathChar(c)) {
/* 212 */         throw new InvalidPathException(paramString, "Illegal character [" + c + "] in path", paramInt1);
/*     */       }
/*     */ 
/* 215 */       paramInt1++;
/*     */     }
/* 217 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   private static final boolean isLetter(char paramChar) {
/* 221 */     return ((paramChar >= 'a') && (paramChar <= 'z')) || ((paramChar >= 'A') && (paramChar <= 'Z'));
/*     */   }
/*     */ 
/*     */   private static final boolean isInvalidPathChar(char paramChar)
/*     */   {
/* 227 */     return (paramChar < ' ') || ("<>:\"|?*".indexOf(paramChar) != -1);
/*     */   }
/*     */ 
/*     */   static class Result
/*     */   {
/*     */     private final WindowsPathType type;
/*     */     private final String root;
/*     */     private final String path;
/*     */ 
/*     */     Result(WindowsPathType paramWindowsPathType, String paramString1, String paramString2)
/*     */     {
/*  46 */       this.type = paramWindowsPathType;
/*  47 */       this.root = paramString1;
/*  48 */       this.path = paramString2;
/*     */     }
/*     */ 
/*     */     WindowsPathType type()
/*     */     {
/*  55 */       return this.type;
/*     */     }
/*     */ 
/*     */     String root()
/*     */     {
/*  62 */       return this.root;
/*     */     }
/*     */ 
/*     */     String path()
/*     */     {
/*  69 */       return this.path;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsPathParser
 * JD-Core Version:    0.6.2
 */
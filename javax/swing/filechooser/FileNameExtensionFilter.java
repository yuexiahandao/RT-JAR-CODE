/*     */ package javax.swing.filechooser;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Arrays;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public final class FileNameExtensionFilter extends FileFilter
/*     */ {
/*     */   private final String description;
/*     */   private final String[] extensions;
/*     */   private final String[] lowerCaseExtensions;
/*     */ 
/*     */   public FileNameExtensionFilter(String paramString, String[] paramArrayOfString)
/*     */   {
/*  75 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
/*  76 */       throw new IllegalArgumentException("Extensions must be non-null and not empty");
/*     */     }
/*     */ 
/*  79 */     this.description = paramString;
/*  80 */     this.extensions = new String[paramArrayOfString.length];
/*  81 */     this.lowerCaseExtensions = new String[paramArrayOfString.length];
/*  82 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  83 */       if ((paramArrayOfString[i] == null) || (paramArrayOfString[i].length() == 0)) {
/*  84 */         throw new IllegalArgumentException("Each extension must be non-null and not empty");
/*     */       }
/*     */ 
/*  87 */       this.extensions[i] = paramArrayOfString[i];
/*  88 */       this.lowerCaseExtensions[i] = paramArrayOfString[i].toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean accept(File paramFile)
/*     */   {
/* 102 */     if (paramFile != null) {
/* 103 */       if (paramFile.isDirectory()) {
/* 104 */         return true;
/*     */       }
/*     */ 
/* 111 */       String str1 = paramFile.getName();
/* 112 */       int i = str1.lastIndexOf('.');
/* 113 */       if ((i > 0) && (i < str1.length() - 1)) {
/* 114 */         String str2 = str1.substring(i + 1).toLowerCase(Locale.ENGLISH);
/*     */ 
/* 116 */         for (String str3 : this.lowerCaseExtensions) {
/* 117 */           if (str2.equals(str3)) {
/* 118 */             return true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return false;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 132 */     return this.description;
/*     */   }
/*     */ 
/*     */   public String[] getExtensions()
/*     */   {
/* 141 */     String[] arrayOfString = new String[this.extensions.length];
/* 142 */     System.arraycopy(this.extensions, 0, arrayOfString, 0, this.extensions.length);
/* 143 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 155 */     return super.toString() + "[description=" + getDescription() + " extensions=" + Arrays.asList(getExtensions()) + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.FileNameExtensionFilter
 * JD-Core Version:    0.6.2
 */
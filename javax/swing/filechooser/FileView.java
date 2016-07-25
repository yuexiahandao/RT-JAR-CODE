/*     */ package javax.swing.filechooser;
/*     */ 
/*     */ import java.io.File;
/*     */ import javax.swing.Icon;
/*     */ 
/*     */ public abstract class FileView
/*     */ {
/*     */   public String getName(File paramFile)
/*     */   {
/*  74 */     return null;
/*     */   }
/*     */ 
/*     */   public String getDescription(File paramFile)
/*     */   {
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   public String getTypeDescription(File paramFile)
/*     */   {
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   public Icon getIcon(File paramFile)
/*     */   {
/*  99 */     return null;
/*     */   }
/*     */ 
/*     */   public Boolean isTraversable(File paramFile)
/*     */   {
/* 108 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.filechooser.FileView
 * JD-Core Version:    0.6.2
 */
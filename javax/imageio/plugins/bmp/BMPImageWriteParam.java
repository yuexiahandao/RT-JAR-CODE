/*     */ package javax.imageio.plugins.bmp;
/*     */ 
/*     */ import com.sun.imageio.plugins.bmp.BMPConstants;
/*     */ import java.util.Locale;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ 
/*     */ public class BMPImageWriteParam extends ImageWriteParam
/*     */ {
/*  67 */   private boolean topDown = false;
/*     */ 
/*     */   public BMPImageWriteParam(Locale paramLocale)
/*     */   {
/*  78 */     super(paramLocale);
/*     */ 
/*  81 */     this.compressionTypes = ((String[])BMPConstants.compressionTypeNames.clone());
/*     */ 
/*  84 */     this.canWriteCompressed = true;
/*  85 */     this.compressionMode = 3;
/*  86 */     this.compressionType = this.compressionTypes[0];
/*     */   }
/*     */ 
/*     */   public BMPImageWriteParam()
/*     */   {
/*  94 */     this(null);
/*     */   }
/*     */ 
/*     */   public void setTopDown(boolean paramBoolean)
/*     */   {
/* 104 */     this.topDown = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean isTopDown()
/*     */   {
/* 114 */     return this.topDown;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.plugins.bmp.BMPImageWriteParam
 * JD-Core Version:    0.6.2
 */
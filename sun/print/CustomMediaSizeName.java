/*     */ package sun.print;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.standard.Media;
/*     */ import javax.print.attribute.standard.MediaSize;
/*     */ import javax.print.attribute.standard.MediaSizeName;
/*     */ 
/*     */ class CustomMediaSizeName extends MediaSizeName
/*     */ {
/*  36 */   private static ArrayList customStringTable = new ArrayList();
/*  37 */   private static ArrayList customEnumTable = new ArrayList();
/*     */   private String choiceName;
/*     */   private MediaSizeName mediaName;
/*     */   private static final long serialVersionUID = 7412807582228043717L;
/*     */ 
/*     */   private CustomMediaSizeName(int paramInt)
/*     */   {
/*  42 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   private static synchronized int nextValue(String paramString)
/*     */   {
/*  47 */     customStringTable.add(paramString);
/*     */ 
/*  49 */     return customStringTable.size() - 1;
/*     */   }
/*     */ 
/*     */   public CustomMediaSizeName(String paramString) {
/*  53 */     super(nextValue(paramString));
/*  54 */     customEnumTable.add(this);
/*  55 */     this.choiceName = null;
/*  56 */     this.mediaName = null;
/*     */   }
/*     */ 
/*     */   public CustomMediaSizeName(String paramString1, String paramString2, float paramFloat1, float paramFloat2)
/*     */   {
/*  61 */     super(nextValue(paramString1));
/*  62 */     this.choiceName = paramString2;
/*  63 */     customEnumTable.add(this);
/*  64 */     this.mediaName = null;
/*     */     try {
/*  66 */       this.mediaName = MediaSize.findMedia(paramFloat1, paramFloat2, 25400);
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getChoiceName()
/*     */   {
/*  81 */     return this.choiceName;
/*     */   }
/*     */ 
/*     */   public MediaSizeName getStandardMedia()
/*     */   {
/*  89 */     return this.mediaName;
/*     */   }
/*     */ 
/*     */   public static MediaSizeName findMedia(Media[] paramArrayOfMedia, float paramFloat1, float paramFloat2, int paramInt)
/*     */   {
/* 101 */     if ((paramFloat1 <= 0.0F) || (paramFloat2 <= 0.0F) || (paramInt < 1)) {
/* 102 */       throw new IllegalArgumentException("args must be +ve values");
/*     */     }
/*     */ 
/* 105 */     if ((paramArrayOfMedia == null) || (paramArrayOfMedia.length == 0)) {
/* 106 */       throw new IllegalArgumentException("args must have valid array of media");
/*     */     }
/*     */ 
/* 109 */     int i = 0;
/* 110 */     MediaSizeName[] arrayOfMediaSizeName = new MediaSizeName[paramArrayOfMedia.length];
/* 111 */     for (int j = 0; j < paramArrayOfMedia.length; j++) {
/* 112 */       if ((paramArrayOfMedia[j] instanceof MediaSizeName)) {
/* 113 */         arrayOfMediaSizeName[(i++)] = ((MediaSizeName)paramArrayOfMedia[j]);
/*     */       }
/*     */     }
/*     */ 
/* 117 */     if (i == 0) {
/* 118 */       return null;
/*     */     }
/*     */ 
/* 121 */     j = 0;
/*     */ 
/* 123 */     double d1 = paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2;
/*     */ 
/* 126 */     float f1 = paramFloat1;
/* 127 */     float f2 = paramFloat2;
/*     */ 
/* 129 */     for (int k = 0; k < i; k++) {
/* 130 */       MediaSize localMediaSize = MediaSize.getMediaSizeForName(arrayOfMediaSizeName[k]);
/* 131 */       if (localMediaSize != null)
/*     */       {
/* 134 */         float[] arrayOfFloat = localMediaSize.getSize(paramInt);
/* 135 */         if ((paramFloat1 == arrayOfFloat[0]) && (paramFloat2 == arrayOfFloat[1])) {
/* 136 */           j = k;
/* 137 */           break;
/*     */         }
/* 139 */         f1 = paramFloat1 - arrayOfFloat[0];
/* 140 */         f2 = paramFloat2 - arrayOfFloat[1];
/* 141 */         double d2 = f1 * f1 + f2 * f2;
/* 142 */         if (d2 < d1) {
/* 143 */           d1 = d2;
/* 144 */           j = k;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 149 */     return arrayOfMediaSizeName[j];
/*     */   }
/*     */ 
/*     */   public Media[] getSuperEnumTable()
/*     */   {
/* 156 */     return (Media[])super.getEnumValueTable();
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 164 */     String[] arrayOfString = new String[customStringTable.size()];
/* 165 */     return (String[])customStringTable.toArray(arrayOfString);
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 172 */     MediaSizeName[] arrayOfMediaSizeName = new MediaSizeName[customEnumTable.size()];
/* 173 */     return (MediaSizeName[])customEnumTable.toArray(arrayOfMediaSizeName);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.CustomMediaSizeName
 * JD-Core Version:    0.6.2
 */
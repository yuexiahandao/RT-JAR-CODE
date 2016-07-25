/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.Attribute;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ 
/*     */ public class MediaTray extends Media
/*     */   implements Attribute
/*     */ {
/*     */   private static final long serialVersionUID = -982503611095214703L;
/*  55 */   public static final MediaTray TOP = new MediaTray(0);
/*     */ 
/*  60 */   public static final MediaTray MIDDLE = new MediaTray(1);
/*     */ 
/*  65 */   public static final MediaTray BOTTOM = new MediaTray(2);
/*     */ 
/*  70 */   public static final MediaTray ENVELOPE = new MediaTray(3);
/*     */ 
/*  75 */   public static final MediaTray MANUAL = new MediaTray(4);
/*     */ 
/*  80 */   public static final MediaTray LARGE_CAPACITY = new MediaTray(5);
/*     */ 
/*  85 */   public static final MediaTray MAIN = new MediaTray(6);
/*     */ 
/*  90 */   public static final MediaTray SIDE = new MediaTray(7);
/*     */ 
/* 102 */   private static final String[] myStringTable = { "top", "middle", "bottom", "envelope", "manual", "large-capacity", "main", "side" };
/*     */ 
/* 113 */   private static final MediaTray[] myEnumValueTable = { TOP, MIDDLE, BOTTOM, ENVELOPE, MANUAL, LARGE_CAPACITY, MAIN, SIDE };
/*     */ 
/*     */   protected MediaTray(int paramInt)
/*     */   {
/*  99 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 129 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 136 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.MediaTray
 * JD-Core Version:    0.6.2
 */
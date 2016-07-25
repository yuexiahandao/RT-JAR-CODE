/*     */ package sun.print;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ import javax.print.attribute.standard.MediaTray;
/*     */ 
/*     */ public class Win32MediaTray extends MediaTray
/*     */ {
/*  40 */   static final Win32MediaTray ENVELOPE_MANUAL = new Win32MediaTray(0, 6);
/*     */ 
/*  42 */   static final Win32MediaTray AUTO = new Win32MediaTray(1, 7);
/*     */ 
/*  44 */   static final Win32MediaTray TRACTOR = new Win32MediaTray(2, 8);
/*     */ 
/*  46 */   static final Win32MediaTray SMALL_FORMAT = new Win32MediaTray(3, 9);
/*     */ 
/*  48 */   static final Win32MediaTray LARGE_FORMAT = new Win32MediaTray(4, 10);
/*     */ 
/*  50 */   static final Win32MediaTray FORMSOURCE = new Win32MediaTray(5, 15);
/*     */ 
/*  53 */   private static ArrayList winStringTable = new ArrayList();
/*  54 */   private static ArrayList winEnumTable = new ArrayList();
/*     */   public int winID;
/*  77 */   private static final String[] myStringTable = { "Manual-Envelope", "Automatic-Feeder", "Tractor-Feeder", "Small-Format", "Large-Format", "Form-Source" };
/*     */ 
/*  86 */   private static final MediaTray[] myEnumValueTable = { ENVELOPE_MANUAL, AUTO, TRACTOR, SMALL_FORMAT, LARGE_FORMAT, FORMSOURCE };
/*     */ 
/*     */   private Win32MediaTray(int paramInt1, int paramInt2)
/*     */   {
/*  58 */     super(paramInt1);
/*  59 */     this.winID = paramInt2;
/*     */   }
/*     */ 
/*     */   private static synchronized int nextValue(String paramString) {
/*  63 */     winStringTable.add(paramString);
/*  64 */     return getTraySize() - 1;
/*     */   }
/*     */ 
/*     */   protected Win32MediaTray(int paramInt, String paramString) {
/*  68 */     super(nextValue(paramString));
/*  69 */     this.winID = paramInt;
/*  70 */     winEnumTable.add(this);
/*     */   }
/*     */ 
/*     */   public int getDMBinID() {
/*  74 */     return this.winID;
/*     */   }
/*     */ 
/*     */   protected static int getTraySize()
/*     */   {
/*  96 */     return myStringTable.length + winStringTable.size();
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable() {
/* 100 */     ArrayList localArrayList = new ArrayList();
/* 101 */     for (int i = 0; i < myStringTable.length; i++) {
/* 102 */       localArrayList.add(myStringTable[i]);
/*     */     }
/* 104 */     localArrayList.addAll(winStringTable);
/* 105 */     String[] arrayOfString = new String[localArrayList.size()];
/* 106 */     return (String[])localArrayList.toArray(arrayOfString);
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable() {
/* 110 */     ArrayList localArrayList = new ArrayList();
/* 111 */     for (int i = 0; i < myEnumValueTable.length; i++) {
/* 112 */       localArrayList.add(myEnumValueTable[i]);
/*     */     }
/* 114 */     localArrayList.addAll(winEnumTable);
/* 115 */     MediaTray[] arrayOfMediaTray = new MediaTray[localArrayList.size()];
/* 116 */     return (MediaTray[])localArrayList.toArray(arrayOfMediaTray);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.Win32MediaTray
 * JD-Core Version:    0.6.2
 */
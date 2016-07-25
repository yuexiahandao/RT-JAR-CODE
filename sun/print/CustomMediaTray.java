/*    */ package sun.print;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import javax.print.attribute.EnumSyntax;
/*    */ import javax.print.attribute.standard.Media;
/*    */ import javax.print.attribute.standard.MediaTray;
/*    */ 
/*    */ class CustomMediaTray extends MediaTray
/*    */ {
/* 34 */   private static ArrayList customStringTable = new ArrayList();
/* 35 */   private static ArrayList customEnumTable = new ArrayList();
/*    */   private String choiceName;
/*    */   private static final long serialVersionUID = 1019451298193987013L;
/*    */ 
/*    */   private CustomMediaTray(int paramInt)
/*    */   {
/* 39 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   private static synchronized int nextValue(String paramString)
/*    */   {
/* 44 */     customStringTable.add(paramString);
/* 45 */     return customStringTable.size() - 1;
/*    */   }
/*    */ 
/*    */   public CustomMediaTray(String paramString1, String paramString2)
/*    */   {
/* 50 */     super(nextValue(paramString1));
/* 51 */     this.choiceName = paramString2;
/* 52 */     customEnumTable.add(this);
/*    */   }
/*    */ 
/*    */   public String getChoiceName()
/*    */   {
/* 65 */     return this.choiceName;
/*    */   }
/*    */ 
/*    */   public Media[] getSuperEnumTable()
/*    */   {
/* 73 */     return (Media[])super.getEnumValueTable();
/*    */   }
/*    */ 
/*    */   protected String[] getStringTable()
/*    */   {
/* 81 */     String[] arrayOfString = new String[customStringTable.size()];
/* 82 */     return (String[])customStringTable.toArray(arrayOfString);
/*    */   }
/*    */ 
/*    */   protected EnumSyntax[] getEnumValueTable()
/*    */   {
/* 89 */     MediaTray[] arrayOfMediaTray = new MediaTray[customEnumTable.size()];
/* 90 */     return (MediaTray[])customEnumTable.toArray(arrayOfMediaTray);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.CustomMediaTray
 * JD-Core Version:    0.6.2
 */
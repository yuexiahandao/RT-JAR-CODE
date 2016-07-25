/*    */ package sun.misc.resources;
/*    */ 
/*    */ import java.util.ListResourceBundle;
/*    */ 
/*    */ public class Messages_ja extends ListResourceBundle
/*    */ {
/* 46 */   private static final Object[][] contents = { { "optpkg.versionerror", "エラー: JARファイル{0}で無効なバージョン形式が使用されています。サポートされるバージョン形式についてのドキュメントを参照してください。" }, { "optpkg.attributeerror", "エラー: 必要なJARマニフェスト属性{0}がJARファイル{1}に設定されていません。" }, { "optpkg.attributeserror", "エラー: 複数の必要なJARマニフェスト属性がJARファイル{0}に設定されていません。" } };
/*    */ 
/*    */   public Object[][] getContents()
/*    */   {
/* 43 */     return contents;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.resources.Messages_ja
 * JD-Core Version:    0.6.2
 */
/*   */ package sun.tools.jar.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class jar_ja extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 7 */     return new Object[][] { { "error.bad.cflag", "フラグ'c'ではマニフェストまたは入力ファイルの指定が必要です。" }, { "error.bad.eflag", "'e'フラグと'Main-Class'属性を持つマニフェストは同時に\n指定できません。" }, { "error.bad.option", "オプション-{ctxu}のうちの1つを指定する必要があります。" }, { "error.bad.uflag", "フラグ'u'ではマニフェストか'e'フラグ、または入力ファイルの指定が必要です。" }, { "error.cant.open", "{0}を開くことができません " }, { "error.create.dir", "ディレクトリ{0}を作成できませんでした" }, { "error.illegal.option", "不正なオプション: {0}" }, { "error.incorrect.length", "{0}の処理中に不正な長さがありました" }, { "error.nosuch.fileordir", "{0}というファイルまたはディレクトリはありません" }, { "error.write.file", "既存jarファイルの書込み中にエラーが発生しました" }, { "out.added.manifest", "マニフェストが追加されました" }, { "out.adding", "{0}を追加中です" }, { "out.create", "  {0}が作成されました" }, { "out.deflated", "({0}%収縮されました)" }, { "out.extracted", "{0}が抽出されました" }, { "out.ignore.entry", "エントリ{0}を無視します" }, { "out.inflated", " {0}が展開されました" }, { "out.size", "(入={0})(出={1})" }, { "out.stored", "(0%格納されました)" }, { "out.update.manifest", "マニフェストが更新されました" }, { "usage", "使用方法: jar {ctxui}[vfm0Me] [jar-file] [manifest-file] [entry-point] [-C dir] files ...\nオプション:\n   -c アーカイブを新規作成する\n   -t アーカイブの内容を一覧表示する\n   -x 指定の(またはすべての)ファイルをアーカイブから抽出する\n   -u 既存アーカイブを更新する\n   -v 標準出力に詳細な出力を生成する\n   -f アーカイブ・ファイル名を指定する\n   -m 指定のマニフェスト・ファイルからマニフェスト情報を取り込む\n   -e 実行可能jarファイルにバンドルされたスタンドアロン・アプリケーションの\n      エントリ・ポイントを指定する\n   -0 格納のみ。ZIP圧縮を使用しない\n   -M エントリのマニフェスト・ファイルを作成しない\n   -i 指定のjarファイルの索引情報を生成する\n   -C 指定のディレクトリに変更し、以下のファイルを取り込む\nファイルがディレクトリの場合は再帰的に処理されます。\nマニフェスト・ファイル名、アーカイブ・ファイル名およびエントリ・ポイント名は、\nフラグ'm'、'f'、'e'の指定と同じ順番で指定する必要があります。\n\n例1: 2つのクラス・ファイルをアーカイブclasses.jarに保存する:\n     jar cvf classes.jar Foo.class Bar.class\n例2: 既存のマニフェスト・ファイル'mymanifest'を使用し、foo/ディレクトリの\n     全ファイルを'classes.jar'にアーカイブする:\n     jar cvfm classes.jar mymanifest -C foo/ .\n" } };
/*   */   }
/*   */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tools.jar.resources.jar_ja
 * JD-Core Version:    0.6.2
 */
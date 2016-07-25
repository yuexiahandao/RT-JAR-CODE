/*   */ package sun.tools.jar.resources;
/*   */ 
/*   */ import java.util.ListResourceBundle;
/*   */ 
/*   */ public final class jar_zh_CN extends ListResourceBundle
/*   */ {
/*   */   protected final Object[][] getContents()
/*   */   {
/* 7 */     return new Object[][] { { "error.bad.cflag", "'c' 标记要求指定清单或输入文件!" }, { "error.bad.eflag", "不能同时指定 'e' 标记和具有 'Main-Class' 属性的\n清单!" }, { "error.bad.option", "必须指定 {ctxu} 中的任一选项。" }, { "error.bad.uflag", "'u' 标记要求指定清单, 'e' 标记或输入文件!" }, { "error.cant.open", "无法打开: {0} " }, { "error.create.dir", "{0}: 无法创建目录" }, { "error.illegal.option", "非法选项: {0}" }, { "error.incorrect.length", "处理时遇到不正确的长度: {0}" }, { "error.nosuch.fileordir", "{0}: 没有这个文件或目录" }, { "error.write.file", "写入现有的 jar 文件时出错" }, { "out.added.manifest", "已添加清单" }, { "out.adding", "正在添加: {0}" }, { "out.create", "  已创建: {0}" }, { "out.deflated", "(压缩了 {0}%)" }, { "out.extracted", "已提取: {0}" }, { "out.ignore.entry", "正在忽略条目{0}" }, { "out.inflated", "  已解压: {0}" }, { "out.size", "(输入 = {0}) (输出 = {1})" }, { "out.stored", "(存储了 0%)" }, { "out.update.manifest", "已更新清单" }, { "usage", "用法: jar {ctxui}[vfm0Me] [jar-file] [manifest-file] [entry-point] [-C dir] files ...\n选项包括: \n    -c  创建新的归档文件\n    -t  列出归档目录\n    -x  从档案中提取指定的 (或所有) 文件\n    -u  更新现有的归档文件\n    -v  在标准输出中生成详细输出\n    -f  指定归档文件名\n    -m  包含指定清单文件中的清单信息\n    -e  为捆绑到可执行 jar 文件的独立应用程序\n        指定应用程序入口点\n    -0  仅存储; 不使用情况任何 ZIP 压缩\n    -M  不创建条目的清单文件\n    -i  为指定的 jar 文件生成索引信息\n    -C  更改为指定的目录并包含其中的文件\n如果有任何目录文件, 则对其进行递归处理。\n清单文件名, 归档文件名和入口点名称的指定顺序\n与 'm', 'f' 和 'e' 标记的指定顺序相同。\n\n示例 1: 将两个类文件归档到一个名为 classes.jar 的归档文件中: \n       jar cvf classes.jar Foo.class Bar.class \n示例 2: 使用现有的清单文件 'mymanifest' 并\n           将 foo/ 目录中的所有文件归档到 'classes.jar' 中: \n       jar cvfm classes.jar mymanifest -C foo/ .\n" } };
/*   */   }
/*   */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.tools.jar.resources.jar_zh_CN
 * JD-Core Version:    0.6.2
 */
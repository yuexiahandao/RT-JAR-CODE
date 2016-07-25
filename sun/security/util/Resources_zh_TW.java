/*     */ package sun.security.util;
/*     */ 
/*     */ import java.util.ListResourceBundle;
/*     */ 
/*     */ public class Resources_zh_TW extends ListResourceBundle
/*     */ {
/*  35 */   private static final Object[][] contents = { { "SPACE", " " }, { "2SPACE", "  " }, { "6SPACE", "      " }, { "COMMA", ", " }, { "NEWLINE", "\n" }, { "STAR", "*******************************************" }, { "STARNN", "*******************************************\n\n" }, { ".OPTION.", " [OPTION]..." }, { "Options.", "選項:" }, { "Use.keytool.help.for.all.available.commands", "使用 \"keytool -help\" 取得所有可用的命令" }, { "Key.and.Certificate.Management.Tool", "金鑰與憑證管理工具" }, { "Commands.", "命令:" }, { "Use.keytool.command.name.help.for.usage.of.command.name", "使用 \"keytool -command_name -help\" 取得 command_name 的用法" }, { "Generates.a.certificate.request", "產生憑證要求" }, { "Changes.an.entry.s.alias", "變更項目的別名" }, { "Deletes.an.entry", "刪除項目" }, { "Exports.certificate", "匯出憑證" }, { "Generates.a.key.pair", "產生金鑰組" }, { "Generates.a.secret.key", "產生秘密金鑰" }, { "Generates.certificate.from.a.certificate.request", "從憑證要求產生憑證" }, { "Generates.CRL", "產生 CRL" }, { "Imports.entries.from.a.JDK.1.1.x.style.identity.database", "從 JDK 1.1.x-style 識別資料庫匯入項目" }, { "Imports.a.certificate.or.a.certificate.chain", "匯入憑證或憑證鏈" }, { "Imports.one.or.all.entries.from.another.keystore", "從其他金鑰儲存庫匯入一個或全部項目" }, { "Clones.a.key.entry", "複製金鑰項目" }, { "Changes.the.key.password.of.an.entry", "變更項目的金鑰密碼" }, { "Lists.entries.in.a.keystore", "列示金鑰儲存庫中的項目" }, { "Prints.the.content.of.a.certificate", "列印憑證的內容" }, { "Prints.the.content.of.a.certificate.request", "列印憑證要求的內容" }, { "Prints.the.content.of.a.CRL.file", "列印 CRL 檔案的內容" }, { "Generates.a.self.signed.certificate", "產生自行簽署的憑證" }, { "Changes.the.store.password.of.a.keystore", "變更金鑰儲存庫的儲存密碼" }, { "alias.name.of.the.entry.to.process", "要處理項目的別名名稱" }, { "destination.alias", "目的地別名" }, { "destination.key.password", "目的地金鑰密碼" }, { "destination.keystore.name", "目的地金鑰儲存庫名稱" }, { "destination.keystore.password.protected", "目的地金鑰儲存庫密碼保護" }, { "destination.keystore.provider.name", "目的地金鑰儲存庫提供者名稱" }, { "destination.keystore.password", "目的地金鑰儲存庫密碼" }, { "destination.keystore.type", "目的地金鑰儲存庫類型" }, { "distinguished.name", "辨別名稱" }, { "X.509.extension", "X.509 擴充套件" }, { "output.file.name", "輸出檔案名稱" }, { "input.file.name", "輸入檔案名稱" }, { "key.algorithm.name", "金鑰演算法名稱" }, { "key.password", "金鑰密碼" }, { "key.bit.size", "金鑰位元大小" }, { "keystore.name", "金鑰儲存庫名稱" }, { "new.password", "新密碼" }, { "do.not.prompt", "不要提示" }, { "password.through.protected.mechanism", "經由保護機制的密碼" }, { "provider.argument", "提供者引數" }, { "provider.class.name", "提供者類別名稱" }, { "provider.name", "提供者名稱" }, { "provider.classpath", "提供者類別路徑" }, { "output.in.RFC.style", "以 RFC 樣式輸出" }, { "signature.algorithm.name", "簽章演算法名稱" }, { "source.alias", "來源別名" }, { "source.key.password", "來源金鑰密碼" }, { "source.keystore.name", "來源金鑰儲存庫名稱" }, { "source.keystore.password.protected", "來源金鑰儲存庫密碼保護" }, { "source.keystore.provider.name", "來源金鑰儲存庫提供者名稱" }, { "source.keystore.password", "來源金鑰儲存庫密碼" }, { "source.keystore.type", "來源金鑰儲存庫類型" }, { "SSL.server.host.and.port", "SSL 伺服器主機與連接埠" }, { "signed.jar.file", "簽署的 jar 檔案" }, { "certificate.validity.start.date.time", "憑證有效性開始日期/時間" }, { "keystore.password", "金鑰儲存庫密碼" }, { "keystore.type", "金鑰儲存庫類型" }, { "trust.certificates.from.cacerts", "來自 cacerts 的信任憑證" }, { "verbose.output", "詳細資訊輸出" }, { "validity.number.of.days", "有效性日數" }, { "Serial.ID.of.cert.to.revoke", "要撤銷憑證的序列 ID" }, { "keytool.error.", "金鑰工具錯誤: " }, { "Illegal.option.", "無效的選項:" }, { "Illegal.value.", "無效值: " }, { "Unknown.password.type.", "不明的密碼類型: " }, { "Cannot.find.environment.variable.", "找不到環境變數: " }, { "Cannot.find.file.", "找不到檔案: " }, { "Command.option.flag.needs.an.argument.", "命令選項 {0} 需要引數。" }, { "Warning.Different.store.and.key.passwords.not.supported.for.PKCS12.KeyStores.Ignoring.user.specified.command.value.", "警告: PKCS12 金鑰儲存庫不支援不同的儲存庫和金鑰密碼。忽略使用者指定的 {0} 值。" }, { ".keystore.must.be.NONE.if.storetype.is.{0}", "如果 -storetype 為 {0}，則 -keystore 必須為 NONE" }, { "Too.many.retries.program.terminated", "重試次數太多，程式已終止" }, { ".storepasswd.and.keypasswd.commands.not.supported.if.storetype.is.{0}", "如果 -storetype 為 {0}，則不支援 -storepasswd 和 -keypasswd 命令" }, { ".keypasswd.commands.not.supported.if.storetype.is.PKCS12", "如果 -storetype 為 PKCS12，則不支援 -keypasswd 命令" }, { ".keypass.and.new.can.not.be.specified.if.storetype.is.{0}", "如果 -storetype 為 {0}，則不能指定 -keypass 和 -new" }, { "if.protected.is.specified.then.storepass.keypass.and.new.must.not.be.specified", "如果指定 -protected，則不能指定 -storepass、-keypass 和 -new" }, { "if.srcprotected.is.specified.then.srcstorepass.and.srckeypass.must.not.be.specified", "如果指定 -srcprotected，則不能指定 -srcstorepass 和 -srckeypass" }, { "if.keystore.is.not.password.protected.then.storepass.keypass.and.new.must.not.be.specified", "如果金鑰儲存庫不受密碼保護，則不能指定 -storepass、-keypass 和 -new" }, { "if.source.keystore.is.not.password.protected.then.srcstorepass.and.srckeypass.must.not.be.specified", "如果來源金鑰儲存庫不受密碼保護，則不能指定 -srcstorepass 和 -srckeypass" }, { "Illegal.startdate.value", "無效的 startdate 值" }, { "Validity.must.be.greater.than.zero", "有效性必須大於零" }, { "provName.not.a.provider", "{0} 不是一個提供者" }, { "Usage.error.no.command.provided", "用法錯誤: 未提供命令" }, { "Source.keystore.file.exists.but.is.empty.", "來源金鑰儲存庫檔案存在，但為空: " }, { "Please.specify.srckeystore", "請指定 -srckeystore" }, { "Must.not.specify.both.v.and.rfc.with.list.command", " 'list' 命令不能同時指定 -v 及 -rfc" }, { "Key.password.must.be.at.least.6.characters", "金鑰密碼必須至少為 6 個字元" }, { "New.password.must.be.at.least.6.characters", "新的密碼必須至少為 6 個字元" }, { "Keystore.file.exists.but.is.empty.", "金鑰儲存庫檔案存在，但為空白: " }, { "Keystore.file.does.not.exist.", "金鑰儲存庫檔案不存在: " }, { "Must.specify.destination.alias", "必須指定目的地別名" }, { "Must.specify.alias", "必須指定別名" }, { "Keystore.password.must.be.at.least.6.characters", "金鑰儲存庫密碼必須至少為 6 個字元" }, { "Enter.keystore.password.", "輸入金鑰儲存庫密碼:  " }, { "Enter.source.keystore.password.", "請輸入來源金鑰儲存庫密碼: " }, { "Enter.destination.keystore.password.", "請輸入目的地金鑰儲存庫密碼: " }, { "Keystore.password.is.too.short.must.be.at.least.6.characters", "金鑰儲存庫密碼太短 - 必須至少為 6 個字元" }, { "Unknown.Entry.Type", "不明的項目類型" }, { "Too.many.failures.Alias.not.changed", "太多錯誤。未變更別名" }, { "Entry.for.alias.alias.successfully.imported.", "已成功匯入別名 {0} 的項目。" }, { "Entry.for.alias.alias.not.imported.", "未匯入別名 {0} 的項目。" }, { "Problem.importing.entry.for.alias.alias.exception.Entry.for.alias.alias.not.imported.", "匯入別名 {0} 的項目時出現問題: {1}。\n未匯入別名 {0} 的項目。" }, { "Import.command.completed.ok.entries.successfully.imported.fail.entries.failed.or.cancelled", "已完成匯入命令: 成功匯入 {0} 個項目，{1} 個項目失敗或已取消" }, { "Warning.Overwriting.existing.alias.alias.in.destination.keystore", "警告: 正在覆寫目的地金鑰儲存庫中的現有別名 {0}" }, { "Existing.entry.alias.alias.exists.overwrite.no.", "現有項目別名 {0} 存在，是否覆寫？[否]:  " }, { "Too.many.failures.try.later", "太多錯誤 - 請稍後再試" }, { "Certification.request.stored.in.file.filename.", "認證要求儲存在檔案 <{0}>" }, { "Submit.this.to.your.CA", "將此送出至您的 CA" }, { "if.alias.not.specified.destalias.srckeypass.and.destkeypass.must.not.be.specified", "如果未指定別名，則不能指定 destalias、srckeypass 及 destkeypass" }, { "Certificate.stored.in.file.filename.", "憑證儲存在檔案 <{0}>" }, { "Certificate.reply.was.installed.in.keystore", "憑證回覆已安裝在金鑰儲存庫中" }, { "Certificate.reply.was.not.installed.in.keystore", "憑證回覆未安裝在金鑰儲存庫中" }, { "Certificate.was.added.to.keystore", "憑證已新增至金鑰儲存庫中" }, { "Certificate.was.not.added.to.keystore", "憑證未新增至金鑰儲存庫中" }, { ".Storing.ksfname.", "[儲存 {0}]" }, { "alias.has.no.public.key.certificate.", "{0} 沒有公開金鑰 (憑證)" }, { "Cannot.derive.signature.algorithm", "無法取得簽章演算法" }, { "Alias.alias.does.not.exist", "別名 <{0}> 不存在" }, { "Alias.alias.has.no.certificate", "別名 <{0}> 沒有憑證" }, { "Key.pair.not.generated.alias.alias.already.exists", "沒有建立金鑰組，別名 <{0}> 已經存在" }, { "Generating.keysize.bit.keyAlgName.key.pair.and.self.signed.certificate.sigAlgName.with.a.validity.of.validality.days.for", "針對 {4} 產生有效期 {3} 天的 {0} 位元 {1} 金鑰組以及自我簽署憑證 ({2})\n\t" }, { "Enter.key.password.for.alias.", "輸入 <{0}> 的金鑰密碼" }, { ".RETURN.if.same.as.keystore.password.", "\t(RETURN 如果和金鑰儲存庫密碼相同):  " }, { "Key.password.is.too.short.must.be.at.least.6.characters", "金鑰密碼太短 - 必須至少為 6 個字元" }, { "Too.many.failures.key.not.added.to.keystore", "太多錯誤 - 金鑰未新增至金鑰儲存庫" }, { "Destination.alias.dest.already.exists", "目的地別名 <{0}> 已經存在" }, { "Password.is.too.short.must.be.at.least.6.characters", "密碼太短 - 必須至少為 6 個字元" }, { "Too.many.failures.Key.entry.not.cloned", "太多錯誤。未複製金鑰項目" }, { "key.password.for.alias.", "<{0}> 的金鑰密碼" }, { "Keystore.entry.for.id.getName.already.exists", "<{0}> 的金鑰儲存庫項目已經存在" }, { "Creating.keystore.entry.for.id.getName.", "建立 <{0}> 的金鑰儲存庫項目..." }, { "No.entries.from.identity.database.added", "沒有新增來自識別資料庫的項目" }, { "Alias.name.alias", "別名名稱: {0}" }, { "Creation.date.keyStore.getCreationDate.alias.", "建立日期: {0,date}" }, { "alias.keyStore.getCreationDate.alias.", "{0}, {1,date}, " }, { "alias.", "{0}, " }, { "Entry.type.type.", "項目類型: {0}" }, { "Certificate.chain.length.", "憑證鏈長度: " }, { "Certificate.i.1.", "憑證 [{0,number,integer}]:" }, { "Certificate.fingerprint.SHA1.", "憑證指紋 (SHA1): " }, { "Keystore.type.", "金鑰儲存庫類型: " }, { "Keystore.provider.", "金鑰儲存庫提供者: " }, { "Your.keystore.contains.keyStore.size.entry", "您的金鑰儲存庫包含 {0,number,integer} 項目" }, { "Your.keystore.contains.keyStore.size.entries", "您的金鑰儲存庫包含 {0,number,integer} 項目" }, { "Failed.to.parse.input", "無法剖析輸入" }, { "Empty.input", "空輸入" }, { "Not.X.509.certificate", "非 X.509 憑證" }, { "alias.has.no.public.key", "{0} 無公開金鑰" }, { "alias.has.no.X.509.certificate", "{0} 無 X.509 憑證" }, { "New.certificate.self.signed.", "新憑證 (自我簽署): " }, { "Reply.has.no.certificates", "回覆不含憑證" }, { "Certificate.not.imported.alias.alias.already.exists", "憑證未輸入，別名 <{0}> 已經存在" }, { "Input.not.an.X.509.certificate", "輸入的不是 X.509 憑證" }, { "Certificate.already.exists.in.keystore.under.alias.trustalias.", "金鑰儲存庫中的 <{0}> 別名之下，憑證已經存在" }, { "Do.you.still.want.to.add.it.no.", "您仍然想要將之新增嗎？ [否]:  " }, { "Certificate.already.exists.in.system.wide.CA.keystore.under.alias.trustalias.", "整個系統 CA 金鑰儲存庫中的 <{0}> 別名之下，憑證已經存在" }, { "Do.you.still.want.to.add.it.to.your.own.keystore.no.", "您仍然想要將之新增至自己的金鑰儲存庫嗎？ [否]:  " }, { "Trust.this.certificate.no.", "信任這個憑證？ [否]:  " }, { "YES", "是" }, { "New.prompt.", "新 {0}: " }, { "Passwords.must.differ", "必須是不同的密碼" }, { "Re.enter.new.prompt.", "重新輸入新 {0}: " }, { "Re.enter.new.password.", "重新輸入新密碼: " }, { "They.don.t.match.Try.again", "它們不相符。請重試" }, { "Enter.prompt.alias.name.", "輸入 {0} 別名名稱:  " }, { "Enter.new.alias.name.RETURN.to.cancel.import.for.this.entry.", "請輸入新的別名名稱\t(RETURN 以取消匯入此項目):" }, { "Enter.alias.name.", "輸入別名名稱:  " }, { ".RETURN.if.same.as.for.otherAlias.", "\t(RETURN 如果和 <{0}> 的相同)" }, { ".PATTERN.printX509Cert", "擁有者: {0}\n發出者: {1}\n序號: {2}\n有效期自: {3} 到: {4}\n憑證指紋:\n\t MD5:  {5}\n\t SHA1: {6}\n\t SHA256: {7}\n\t 簽章演算法名稱: {8}\n\t 版本: {9}" }, { "What.is.your.first.and.last.name.", "您的名字與姓氏為何？" }, { "What.is.the.name.of.your.organizational.unit.", "您的組織單位名稱為何？" }, { "What.is.the.name.of.your.organization.", "您的組織名稱為何？" }, { "What.is.the.name.of.your.City.or.Locality.", "您所在的城市或地區名稱為何？" }, { "What.is.the.name.of.your.State.or.Province.", "您所在的州及省份名稱為何？" }, { "What.is.the.two.letter.country.code.for.this.unit.", "此單位的兩個字母國別代碼為何？" }, { "Is.name.correct.", "{0} 正確嗎？" }, { "no", "否" }, { "yes", "是" }, { "y", "y" }, { ".defaultValue.", "  [{0}]:  " }, { "Alias.alias.has.no.key", "別名 <{0}> 沒有金鑰" }, { "Alias.alias.references.an.entry.type.that.is.not.a.private.key.entry.The.keyclone.command.only.supports.cloning.of.private.key", "別名 <{0}> 所參照的項目不是私密金鑰類型。-keyclone 命令僅支援私密金鑰項目的複製" }, { ".WARNING.WARNING.WARNING.", "*****************  WARNING WARNING WARNING  *****************" }, { "Signer.d.", "簽署者 #%d:" }, { "Timestamp.", "時戳:" }, { "Signature.", "簽章:" }, { "CRLs.", "CRL:" }, { "Certificate.owner.", "憑證擁有者: " }, { "Not.a.signed.jar.file", "不是簽署的 jar 檔案" }, { "No.certificate.from.the.SSL.server", "沒有來自 SSL 伺服器的憑證" }, { ".The.integrity.of.the.information.stored.in.your.keystore.", "* 尚未驗證儲存於金鑰儲存庫中資訊  *\n* 的完整性！若要驗證其完整性，*\n* 您必須提供您的金鑰儲存庫密碼。                  *" }, { ".The.integrity.of.the.information.stored.in.the.srckeystore.", "* 尚未驗證儲存於 srckeystore 中資訊*\n* 的完整性！若要驗證其完整性，您必須 *\n* 提供 srckeystore 密碼。          *" }, { "Certificate.reply.does.not.contain.public.key.for.alias.", "憑證回覆並未包含 <{0}> 的公開金鑰" }, { "Incomplete.certificate.chain.in.reply", "回覆時的憑證鏈不完整" }, { "Certificate.chain.in.reply.does.not.verify.", "回覆時的憑證鏈未驗證: " }, { "Top.level.certificate.in.reply.", "回覆時的最高級憑證:\n" }, { ".is.not.trusted.", "... 是不被信任的。" }, { "Install.reply.anyway.no.", "還是要安裝回覆？ [否]:  " }, { "NO", "否" }, { "Public.keys.in.reply.and.keystore.don.t.match", "回覆時的公開金鑰與金鑰儲存庫不符" }, { "Certificate.reply.and.certificate.in.keystore.are.identical", "憑證回覆與金鑰儲存庫中的憑證是相同的" }, { "Failed.to.establish.chain.from.reply", "無法從回覆中將鏈建立起來" }, { "n", "n" }, { "Wrong.answer.try.again", "錯誤的答案，請再試一次" }, { "Secret.key.not.generated.alias.alias.already.exists", "未產生秘密金鑰，別名 <{0}> 已存在" }, { "Please.provide.keysize.for.secret.key.generation", "請提供 -keysize 以產生秘密金鑰" }, { "Extensions.", "擴充套件: " }, { ".Empty.value.", "(空白值)" }, { "Extension.Request.", "擴充套件要求:" }, { "PKCS.10.Certificate.Request.Version.1.0.Subject.s.Public.Key.s.format.s.key.", "PKCS #10 憑證要求 (版本 1.0)\n主體: %s\n公用金鑰: %s 格式 %s 金鑰\n" }, { "Unknown.keyUsage.type.", "不明的 keyUsage 類型: " }, { "Unknown.extendedkeyUsage.type.", "不明的 extendedkeyUsage 類型: " }, { "Unknown.AccessDescription.type.", "不明的 AccessDescription 類型: " }, { "Unrecognized.GeneralName.type.", "無法辨識的 GeneralName 類型: " }, { "This.extension.cannot.be.marked.as.critical.", "此擴充套件無法標示為關鍵。" }, { "Odd.number.of.hex.digits.found.", "找到十六進位數字的奇數: " }, { "Unknown.extension.type.", "不明的擴充套件類型: " }, { "command.{0}.is.ambiguous.", "命令 {0} 不明確:" }, { "Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured.", "警告: 別名 {0} 的公開金鑰不存在。請確定金鑰儲存庫配置正確。" }, { "Warning.Class.not.found.class", "警告: 找不到類別 {0}" }, { "Warning.Invalid.argument.s.for.constructor.arg", "警告: 無效的建構子引數: {0}" }, { "Illegal.Principal.Type.type", "無效的 Principal 類型: {0}" }, { "Illegal.option.option", "無效的選項: {0}" }, { "Usage.policytool.options.", "用法: policytool [options]" }, { ".file.file.policy.file.location", "  [-file <file>]    原則檔案位置" }, { "New", "新增" }, { "Open", "開啟" }, { "Save", "儲存" }, { "Save.As", "另存新檔" }, { "View.Warning.Log", "檢視警告記錄" }, { "Exit", "結束" }, { "Add.Policy.Entry", "新增原則項目" }, { "Edit.Policy.Entry", "編輯原則項目" }, { "Remove.Policy.Entry", "移除原則項目" }, { "Edit", "編輯" }, { "Retain", "保留" }, { "Warning.File.name.may.include.escaped.backslash.characters.It.is.not.necessary.to.escape.backslash.characters.the.tool.escapes", "警告: 檔案名稱包含遁離反斜線字元。不需要遁離反斜線字元 (撰寫原則內容至永久存放區時需要工具遁離字元)。\n\n按一下「保留」以保留輸入的名稱，或按一下「編輯」以編輯名稱。" }, { "Add.Public.Key.Alias", "新增公開金鑰別名" }, { "Remove.Public.Key.Alias", "移除公開金鑰別名" }, { "File", "檔案" }, { "KeyStore", "金鑰儲存庫" }, { "Policy.File.", "原則檔案: " }, { "Could.not.open.policy.file.policyFile.e.toString.", "無法開啟原則檔案: {0}: {1}" }, { "Policy.Tool", "原則工具" }, { "Errors.have.occurred.while.opening.the.policy.configuration.View.the.Warning.Log.for.more.information.", "開啟原則記置時發生錯誤。請檢視警告記錄以取得更多的資訊" }, { "Error", "錯誤" }, { "OK", "確定" }, { "Status", "狀態" }, { "Warning", "警告" }, { "Permission.", "權限:                                                       " }, { "Principal.Type.", "Principal 類型: " }, { "Principal.Name.", "Principal 名稱: " }, { "Target.Name.", "目標名稱:                                                    " }, { "Actions.", "動作:                                                             " }, { "OK.to.overwrite.existing.file.filename.", "確認覆寫現存的檔案 {0}？" }, { "Cancel", "取消" }, { "CodeBase.", "CodeBase:" }, { "SignedBy.", "SignedBy:" }, { "Add.Principal", "新增 Principal" }, { "Edit.Principal", "編輯 Principal" }, { "Remove.Principal", "移除 Principal" }, { "Principals.", "Principal:" }, { ".Add.Permission", "  新增權限" }, { ".Edit.Permission", "  編輯權限" }, { "Remove.Permission", "移除權限" }, { "Done", "完成" }, { "KeyStore.URL.", "金鑰儲存庫 URL: " }, { "KeyStore.Type.", "金鑰儲存庫類型:" }, { "KeyStore.Provider.", "金鑰儲存庫提供者:" }, { "KeyStore.Password.URL.", "金鑰儲存庫密碼 URL: " }, { "Principals", "Principal" }, { ".Edit.Principal.", "  編輯 Principal: " }, { ".Add.New.Principal.", "  新增 Principal: " }, { "Permissions", "權限" }, { ".Edit.Permission.", "  編輯權限:" }, { ".Add.New.Permission.", "  新增權限:" }, { "Signed.By.", "簽署人: " }, { "Cannot.Specify.Principal.with.a.Wildcard.Class.without.a.Wildcard.Name", "沒有萬用字元名稱，無法指定含有萬用字元類別的 Principal" }, { "Cannot.Specify.Principal.without.a.Name", "沒有名稱，無法指定 Principal" }, { "Permission.and.Target.Name.must.have.a.value", "權限及目標名稱必須有一個值。" }, { "Remove.this.Policy.Entry.", "移除這個原則項目？" }, { "Overwrite.File", "覆寫檔案" }, { "Policy.successfully.written.to.filename", "原則成功寫入至 {0}" }, { "null.filename", "空值檔名" }, { "Save.changes.", "儲存變更？" }, { "Yes", "是" }, { "No", "否" }, { "Policy.Entry", "原則項目" }, { "Save.Changes", "儲存變更" }, { "No.Policy.Entry.selected", "沒有選取原則項目" }, { "Unable.to.open.KeyStore.ex.toString.", "無法開啟金鑰儲存庫: {0}" }, { "No.principal.selected", "未選取 Principal" }, { "No.permission.selected", "沒有選取權限" }, { "name", "名稱" }, { "configuration.type", "組態類型" }, { "environment.variable.name", "環境變數名稱" }, { "library.name", "程式庫名稱" }, { "package.name", "套裝程式名稱" }, { "policy.type", "原則類型" }, { "property.name", "屬性名稱" }, { "Principal.List", "Principal 清單" }, { "Permission.List", "權限清單" }, { "Code.Base", "代碼基準" }, { "KeyStore.U.R.L.", "金鑰儲存庫 URL:" }, { "KeyStore.Password.U.R.L.", "金鑰儲存庫密碼 URL:" }, { "invalid.null.input.s.", "無效空值輸入" }, { "actions.can.only.be.read.", "動作只能被「讀取」" }, { "permission.name.name.syntax.invalid.", "權限名稱 [{0}] 是無效的語法: " }, { "Credential.Class.not.followed.by.a.Principal.Class.and.Name", "Credential 類別後面不是 Principal 類別及名稱" }, { "Principal.Class.not.followed.by.a.Principal.Name", "Principal 類別後面不是 Principal 名稱" }, { "Principal.Name.must.be.surrounded.by.quotes", "Principal 名稱必須以引號圈住" }, { "Principal.Name.missing.end.quote", "Principal 名稱缺少下引號" }, { "PrivateCredentialPermission.Principal.Class.can.not.be.a.wildcard.value.if.Principal.Name.is.not.a.wildcard.value", "如果 Principal 名稱不是一個萬用字元 (*) 值，那麼 PrivateCredentialPermission Principal 類別就不能是萬用字元 (*) 值" }, { "CredOwner.Principal.Class.class.Principal.Name.name", "CredOwner:\n\tPrincipal 類別 = {0}\n\tPrincipal 名稱 = {1}" }, { "provided.null.name", "提供空值名稱" }, { "provided.null.keyword.map", "提供空值關鍵字對映" }, { "provided.null.OID.map", "提供空值 OID 對映" }, { "invalid.null.AccessControlContext.provided", "提供無效的空值 AccessControlContext" }, { "invalid.null.action.provided", "提供無效的空值動作" }, { "invalid.null.Class.provided", "提供無效的空值類別" }, { "Subject.", "主題:\n" }, { ".Principal.", "\tPrincipal: " }, { ".Public.Credential.", "\t公用證明資料: " }, { ".Private.Credentials.inaccessible.", "\t私人證明資料無法存取\n" }, { ".Private.Credential.", "\t私人證明資料: " }, { ".Private.Credential.inaccessible.", "\t私人證明資料無法存取\n" }, { "Subject.is.read.only", "主題為唯讀" }, { "attempting.to.add.an.object.which.is.not.an.instance.of.java.security.Principal.to.a.Subject.s.Principal.Set", "試圖新增一個非 java.security.Principal 執行處理的物件至主題的 Principal 群中" }, { "attempting.to.add.an.object.which.is.not.an.instance.of.class", "試圖新增一個非 {0} 執行處理的物件" }, { "LoginModuleControlFlag.", "LoginModuleControlFlag: " }, { "Invalid.null.input.name", "無效空值輸入: 名稱" }, { "No.LoginModules.configured.for.name", "無針對 {0} 配置的 LoginModules" }, { "invalid.null.Subject.provided", "提供無效空值主題" }, { "invalid.null.CallbackHandler.provided", "提供無效空值 CallbackHandler" }, { "null.subject.logout.called.before.login", "空值主題 - 在登入之前即呼叫登出" }, { "unable.to.instantiate.LoginModule.module.because.it.does.not.provide.a.no.argument.constructor", "無法創設 LoginModule，{0}，因為它並未提供非引數的建構子" }, { "unable.to.instantiate.LoginModule", "無法建立 LoginModule" }, { "unable.to.instantiate.LoginModule.", "無法建立 LoginModule: " }, { "unable.to.find.LoginModule.class.", "找不到 LoginModule 類別: " }, { "unable.to.access.LoginModule.", "無法存取 LoginModule: " }, { "Login.Failure.all.modules.ignored", "登入失敗: 忽略所有模組" }, { "java.security.policy.error.parsing.policy.message", "java.security.policy: 剖析錯誤 {0}: \n\t{1}" }, { "java.security.policy.error.adding.Permission.perm.message", "java.security.policy: 新增權限錯誤 {0}: \n\t{1}" }, { "java.security.policy.error.adding.Entry.message", "java.security.policy: 新增項目錯誤: \n\t{0}" }, { "alias.name.not.provided.pe.name.", "未提供別名名稱 ({0})" }, { "unable.to.perform.substitution.on.alias.suffix", "無法對別名執行替換，{0}" }, { "substitution.value.prefix.unsupported", "不支援的替換值，{0}" }, { "LPARAM", "(" }, { "RPARAM", ")" }, { "type.can.t.be.null", "輸入不能為空值" }, { "keystorePasswordURL.can.not.be.specified.without.also.specifying.keystore", "指定 keystorePasswordURL 需要同時指定金鑰儲存庫" }, { "expected.keystore.type", "預期的金鑰儲存庫類型" }, { "expected.keystore.provider", "預期的金鑰儲存庫提供者" }, { "multiple.Codebase.expressions", "多重 Codebase 表示式" }, { "multiple.SignedBy.expressions", "多重 SignedBy 表示式" }, { "SignedBy.has.empty.alias", "SignedBy 有空別名" }, { "can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name", "沒有萬用字元名稱，無法指定含有萬用字元類別的 Principal" }, { "expected.codeBase.or.SignedBy.or.Principal", "預期的 codeBase 或 SignedBy 或 Principal" }, { "expected.permission.entry", "預期的權限項目" }, { "number.", "號碼 " }, { "expected.expect.read.end.of.file.", "預期的 [{0}], 讀取 [end of file]" }, { "expected.read.end.of.file.", "預期的 [;], 讀取 [end of file]" }, { "line.number.msg", "行 {0}: {1}" }, { "line.number.expected.expect.found.actual.", "行 {0}: 預期的 [{1}]，發現 [{2}]" }, { "null.principalClass.or.principalName", "空值 principalClass 或 principalName" }, { "PKCS11.Token.providerName.Password.", "PKCS11 記號 [{0}] 密碼: " }, { "unable.to.instantiate.Subject.based.policy", "無法建立主題式的原則" } };
/*     */ 
/*     */   public Object[][] getContents()
/*     */   {
/* 658 */     return contents;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.Resources_zh_TW
 * JD-Core Version:    0.6.2
 */
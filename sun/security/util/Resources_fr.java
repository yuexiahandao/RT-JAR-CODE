/*     */ package sun.security.util;
/*     */ 
/*     */ import java.util.ListResourceBundle;
/*     */ 
/*     */ public class Resources_fr extends ListResourceBundle
/*     */ {
/*  35 */   private static final Object[][] contents = { { "SPACE", " " }, { "2SPACE", "  " }, { "6SPACE", "      " }, { "COMMA", ", " }, { "NEWLINE", "\n" }, { "STAR", "*******************************************" }, { "STARNN", "*******************************************\n\n" }, { ".OPTION.", " [OPTION]..." }, { "Options.", "Options :" }, { "Use.keytool.help.for.all.available.commands", "Utiliser \"keytool -help\" pour toutes les commandes disponibles" }, { "Key.and.Certificate.Management.Tool", "Outil de gestion de certificats et de clés" }, { "Commands.", "Commandes :" }, { "Use.keytool.command.name.help.for.usage.of.command.name", "Utiliser \"keytool -command_name -help\" pour la syntaxe de command_name" }, { "Generates.a.certificate.request", "Génère une demande de certificat" }, { "Changes.an.entry.s.alias", "Modifie l'alias d'une entrée" }, { "Deletes.an.entry", "Supprime une entrée" }, { "Exports.certificate", "Exporte le certificat" }, { "Generates.a.key.pair", "Génère une paire de clés" }, { "Generates.a.secret.key", "Génère une clé secrète" }, { "Generates.certificate.from.a.certificate.request", "Génère le certificat à partir d'une demande de certificat" }, { "Generates.CRL", "Génère la liste des certificats révoqués (CRL)" }, { "Imports.entries.from.a.JDK.1.1.x.style.identity.database", "Importe les entrées à partir d'une base de données d'identités de type JDK 1.1.x" }, { "Imports.a.certificate.or.a.certificate.chain", "Importe un certificat ou une chaîne de certificat" }, { "Imports.one.or.all.entries.from.another.keystore", "Importe une entrée ou la totalité des entrées depuis un autre fichier de clés" }, { "Clones.a.key.entry", "Clone une entrée de clé" }, { "Changes.the.key.password.of.an.entry", "Modifie le mot de passe de clé d'une entrée" }, { "Lists.entries.in.a.keystore", "Répertorie les entrées d'un fichier de clés" }, { "Prints.the.content.of.a.certificate", "Imprime le contenu d'un certificat" }, { "Prints.the.content.of.a.certificate.request", "Imprime le contenu d'une demande de certificat" }, { "Prints.the.content.of.a.CRL.file", "Imprime le contenu d'un fichier de liste des certificats révoqués (CRL)" }, { "Generates.a.self.signed.certificate", "Génère un certificat auto-signé" }, { "Changes.the.store.password.of.a.keystore", "Modifie le mot de passe de banque d'un fichier de clés" }, { "alias.name.of.the.entry.to.process", "nom d'alias de l'entrée à traiter" }, { "destination.alias", "alias de destination" }, { "destination.key.password", "mot de passe de la clé de destination" }, { "destination.keystore.name", "nom du fichier de clés de destination" }, { "destination.keystore.password.protected", "mot de passe du fichier de clés de destination protégé" }, { "destination.keystore.provider.name", "nom du fournisseur du fichier de clés de destination" }, { "destination.keystore.password", "mot de passe du fichier de clés de destination" }, { "destination.keystore.type", "type du fichier de clés de destination" }, { "distinguished.name", "nom distinctif" }, { "X.509.extension", "extension X.509" }, { "output.file.name", "nom du fichier de sortie" }, { "input.file.name", "nom du fichier d'entrée" }, { "key.algorithm.name", "nom de l'algorithme de clé" }, { "key.password", "mot de passe de la clé" }, { "key.bit.size", "taille en bits de la clé" }, { "keystore.name", "nom du fichier de clés" }, { "new.password", "nouveau mot de passe" }, { "do.not.prompt", "ne pas inviter" }, { "password.through.protected.mechanism", "mot de passe via mécanisme protégé" }, { "provider.argument", "argument du fournisseur" }, { "provider.class.name", "nom de la classe de fournisseur" }, { "provider.name", "nom du fournisseur" }, { "provider.classpath", "variable d'environnement CLASSPATH du fournisseur" }, { "output.in.RFC.style", "sortie au style RFC" }, { "signature.algorithm.name", "nom de l'algorithme de signature" }, { "source.alias", "alias source" }, { "source.key.password", "mot de passe de la clé source" }, { "source.keystore.name", "nom du fichier de clés source" }, { "source.keystore.password.protected", "mot de passe du fichier de clés source protégé" }, { "source.keystore.provider.name", "nom du fournisseur du fichier de clés source" }, { "source.keystore.password", "mot de passe du fichier de clés source" }, { "source.keystore.type", "type du fichier de clés source" }, { "SSL.server.host.and.port", "Port et hôte du serveur SSL" }, { "signed.jar.file", "fichier JAR signé" }, { "certificate.validity.start.date.time", "date/heure de début de validité du certificat" }, { "keystore.password", "mot de passe du fichier de clés" }, { "keystore.type", "type du fichier de clés" }, { "trust.certificates.from.cacerts", "certificats sécurisés issus de certificats CA" }, { "verbose.output", "sortie en mode verbose" }, { "validity.number.of.days", "nombre de jours de validité" }, { "Serial.ID.of.cert.to.revoke", "ID de série du certificat à révoquer" }, { "keytool.error.", "erreur keytool : " }, { "Illegal.option.", "Option non admise :  " }, { "Illegal.value.", "Valeur non admise : " }, { "Unknown.password.type.", "Type de mot de passe inconnu : " }, { "Cannot.find.environment.variable.", "Variable d'environnement introuvable : " }, { "Cannot.find.file.", "Fichier introuvable : " }, { "Command.option.flag.needs.an.argument.", "L''option de commande {0} requiert un argument." }, { "Warning.Different.store.and.key.passwords.not.supported.for.PKCS12.KeyStores.Ignoring.user.specified.command.value.", "Avertissement : les mots de passe de clé et de banque distincts ne sont pas pris en charge pour les fichiers de clés d''accès PKCS12. La valeur {0} spécifiée par l''utilisateur est ignorée." }, { ".keystore.must.be.NONE.if.storetype.is.{0}", "-keystore doit être défini sur NONE si -storetype est {0}" }, { "Too.many.retries.program.terminated", "Trop de tentatives, fin du programme" }, { ".storepasswd.and.keypasswd.commands.not.supported.if.storetype.is.{0}", "Les commandes -storepasswd et -keypasswd ne sont pas prises en charge si -storetype est défini sur {0}" }, { ".keypasswd.commands.not.supported.if.storetype.is.PKCS12", "Les commandes -keypasswd ne sont pas prises en charge si -storetype est défini sur PKCS12" }, { ".keypass.and.new.can.not.be.specified.if.storetype.is.{0}", "Les commandes -keypass et -new ne peuvent pas être spécifiées si -storetype est défini sur {0}" }, { "if.protected.is.specified.then.storepass.keypass.and.new.must.not.be.specified", "si -protected est spécifié, -storepass, -keypass et -new ne doivent pas être indiqués" }, { "if.srcprotected.is.specified.then.srcstorepass.and.srckeypass.must.not.be.specified", "Si -srcprotected est indiqué, les commandes -srcstorepass et -srckeypass ne doivent pas être spécifiées" }, { "if.keystore.is.not.password.protected.then.storepass.keypass.and.new.must.not.be.specified", "Si le fichier de clés n'est pas protégé par un mot de passe, les commandes -storepass, -keypass et -new ne doivent pas être spécifiées" }, { "if.source.keystore.is.not.password.protected.then.srcstorepass.and.srckeypass.must.not.be.specified", "Si le fichier de clés source n'est pas protégé par un mot de passe, les commandes -srcstorepass et -srckeypass ne doivent pas être spécifiées" }, { "Illegal.startdate.value", "Valeur de date de début non admise" }, { "Validity.must.be.greater.than.zero", "La validité doit être supérieure à zéro" }, { "provName.not.a.provider", "{0} n''est pas un fournisseur" }, { "Usage.error.no.command.provided", "Erreur de syntaxe : aucune commande fournie" }, { "Source.keystore.file.exists.but.is.empty.", "Le fichier de clés source existe mais il est vide : " }, { "Please.specify.srckeystore", "Indiquez -srckeystore" }, { "Must.not.specify.both.v.and.rfc.with.list.command", "-v et -rfc ne doivent pas être spécifiés avec la commande 'list'" }, { "Key.password.must.be.at.least.6.characters", "Un mot de passe de clé doit comporter au moins 6 caractères" }, { "New.password.must.be.at.least.6.characters", "Le nouveau mot de passe doit comporter au moins 6 caractères" }, { "Keystore.file.exists.but.is.empty.", "Fichier de clés existant mais vide : " }, { "Keystore.file.does.not.exist.", "Le fichier de clés n'existe pas : " }, { "Must.specify.destination.alias", "L'alias de destination doit être spécifié" }, { "Must.specify.alias", "L'alias doit être spécifié" }, { "Keystore.password.must.be.at.least.6.characters", "Un mot de passe de fichier de clés doit comporter au moins 6 caractères" }, { "Enter.keystore.password.", "Entrez le mot de passe du fichier de clés :  " }, { "Enter.source.keystore.password.", "Entrez le mot de passe du fichier de clés source :  " }, { "Enter.destination.keystore.password.", "Entrez le mot de passe du fichier de clés de destination :  " }, { "Keystore.password.is.too.short.must.be.at.least.6.characters", "Le mot de passe du fichier de clés est trop court : il doit comporter au moins 6 caractères" }, { "Unknown.Entry.Type", "Type d'entrée inconnu" }, { "Too.many.failures.Alias.not.changed", "Trop d'erreurs. Alias non modifié" }, { "Entry.for.alias.alias.successfully.imported.", "L''entrée de l''alias {0} a été importée." }, { "Entry.for.alias.alias.not.imported.", "L''entrée de l''alias {0} n''a pas été importée." }, { "Problem.importing.entry.for.alias.alias.exception.Entry.for.alias.alias.not.imported.", "Problème lors de l''import de l''entrée de l''alias {0} : {1}.\nL''entrée de l''alias {0} n''a pas été importée." }, { "Import.command.completed.ok.entries.successfully.imported.fail.entries.failed.or.cancelled", "Commande d''import exécutée : {0} entrées importées, échec ou annulation de {1} entrées" }, { "Warning.Overwriting.existing.alias.alias.in.destination.keystore", "Avertissement : l''alias {0} existant sera remplacé dans le fichier de clés d''accès de destination" }, { "Existing.entry.alias.alias.exists.overwrite.no.", "L''alias d''entrée {0} existe déjà. Voulez-vous le remplacer ? [non] :  " }, { "Too.many.failures.try.later", "Trop d'erreurs. Réessayez plus tard" }, { "Certification.request.stored.in.file.filename.", "Demande de certification stockée dans le fichier <{0}>" }, { "Submit.this.to.your.CA", "Soumettre à votre CA" }, { "if.alias.not.specified.destalias.srckeypass.and.destkeypass.must.not.be.specified", "si l'alias n'est pas spécifié, destalias, srckeypass et destkeypass ne doivent pas être spécifiés" }, { "Certificate.stored.in.file.filename.", "Certificat stocké dans le fichier <{0}>" }, { "Certificate.reply.was.installed.in.keystore", "Réponse de certificat installée dans le fichier de clés" }, { "Certificate.reply.was.not.installed.in.keystore", "Réponse de certificat non installée dans le fichier de clés" }, { "Certificate.was.added.to.keystore", "Certificat ajouté au fichier de clés" }, { "Certificate.was.not.added.to.keystore", "Certificat non ajouté au fichier de clés" }, { ".Storing.ksfname.", "[Stockage de {0}]" }, { "alias.has.no.public.key.certificate.", "{0} ne possède pas de clé publique (certificat)" }, { "Cannot.derive.signature.algorithm", "Impossible de déduire l'algorithme de signature" }, { "Alias.alias.does.not.exist", "L''alias <{0}> n''existe pas" }, { "Alias.alias.has.no.certificate", "L''alias <{0}> ne possède pas de certificat" }, { "Key.pair.not.generated.alias.alias.already.exists", "Paire de clés non générée, l''alias <{0}> existe déjà" }, { "Generating.keysize.bit.keyAlgName.key.pair.and.self.signed.certificate.sigAlgName.with.a.validity.of.validality.days.for", "Génération d''une paire de clés {1} de {0} bits et d''un certificat auto-signé ({2}) d''une validité de {3} jours\n\tpour : {4}" }, { "Enter.key.password.for.alias.", "Entrez le mot de passe de la clé pour <{0}>" }, { ".RETURN.if.same.as.keystore.password.", "\t(appuyez sur Entrée s'il s'agit du mot de passe du fichier de clés) :  " }, { "Key.password.is.too.short.must.be.at.least.6.characters", "Le mot de passe de la clé est trop court : il doit comporter au moins 6 caractères" }, { "Too.many.failures.key.not.added.to.keystore", "Trop d'erreurs. Clé non ajoutée au fichier de clés" }, { "Destination.alias.dest.already.exists", "L''alias de la destination <{0}> existe déjà" }, { "Password.is.too.short.must.be.at.least.6.characters", "Le mot de passe est trop court : il doit comporter au moins 6 caractères" }, { "Too.many.failures.Key.entry.not.cloned", "Trop d'erreurs. Entrée de clé non clonée" }, { "key.password.for.alias.", "mot de passe de clé pour <{0}>" }, { "Keystore.entry.for.id.getName.already.exists", "L''entrée de fichier de clés d''accès pour <{0}> existe déjà" }, { "Creating.keystore.entry.for.id.getName.", "Création d''une entrée de fichier de clés d''accès pour <{0}>..." }, { "No.entries.from.identity.database.added", "Aucune entrée ajoutée à partir de la base de données d'identités" }, { "Alias.name.alias", "Nom d''alias : {0}" }, { "Creation.date.keyStore.getCreationDate.alias.", "Date de création : {0,date}" }, { "alias.keyStore.getCreationDate.alias.", "{0}, {1,date}, " }, { "alias.", "{0}, " }, { "Entry.type.type.", "Type d''entrée : {0}" }, { "Certificate.chain.length.", "Longueur de chaîne du certificat : " }, { "Certificate.i.1.", "Certificat[{0,number,integer}]:" }, { "Certificate.fingerprint.SHA1.", "Empreinte du certificat (SHA1) : " }, { "Keystore.type.", "Type de fichier de clés : " }, { "Keystore.provider.", "Fournisseur de fichier de clés : " }, { "Your.keystore.contains.keyStore.size.entry", "Votre fichier de clés d''accès contient {0,number,integer} entrée" }, { "Your.keystore.contains.keyStore.size.entries", "Votre fichier de clés d''accès contient {0,number,integer} entrées" }, { "Failed.to.parse.input", "L'analyse de l'entrée a échoué" }, { "Empty.input", "Entrée vide" }, { "Not.X.509.certificate", "Pas un certificat X.509" }, { "alias.has.no.public.key", "{0} ne possède pas de clé publique" }, { "alias.has.no.X.509.certificate", "{0} ne possède pas de certificat X.509" }, { "New.certificate.self.signed.", "Nouveau certificat (auto-signé) :" }, { "Reply.has.no.certificates", "La réponse n'a pas de certificat" }, { "Certificate.not.imported.alias.alias.already.exists", "Certificat non importé, l''alias <{0}> existe déjà" }, { "Input.not.an.X.509.certificate", "L'entrée n'est pas un certificat X.509" }, { "Certificate.already.exists.in.keystore.under.alias.trustalias.", "Le certificat existe déjà dans le fichier de clés d''accès sous l''alias <{0}>" }, { "Do.you.still.want.to.add.it.no.", "Voulez-vous toujours l'ajouter ? [non] :  " }, { "Certificate.already.exists.in.system.wide.CA.keystore.under.alias.trustalias.", "Le certificat existe déjà dans le fichier de clés d''accès CA système sous l''alias <{0}>" }, { "Do.you.still.want.to.add.it.to.your.own.keystore.no.", "Voulez-vous toujours l'ajouter à votre fichier de clés ? [non] :  " }, { "Trust.this.certificate.no.", "Faire confiance à ce certificat ? [non] :  " }, { "YES", "OUI" }, { "New.prompt.", "Nouveau {0} : " }, { "Passwords.must.differ", "Les mots de passe doivent différer" }, { "Re.enter.new.prompt.", "Indiquez encore le nouveau {0} : " }, { "Re.enter.new.password.", "Ressaisissez le nouveau mot de passe : " }, { "They.don.t.match.Try.again", "Ils sont différents. Réessayez." }, { "Enter.prompt.alias.name.", "Indiquez le nom d''alias {0} :  " }, { "Enter.new.alias.name.RETURN.to.cancel.import.for.this.entry.", "Saisissez le nom du nouvel alias\t(ou appuyez sur Entrée pour annuler l'import de cette entrée) :  " }, { "Enter.alias.name.", "Indiquez le nom d'alias :  " }, { ".RETURN.if.same.as.for.otherAlias.", "\t(appuyez sur Entrée si le résultat est identique à <{0}>)" }, { ".PATTERN.printX509Cert", "Propriétaire : {0}\nEmetteur : {1}\nNuméro de série : {2}\nValide du : {3} au : {4}\nEmpreintes du certificat :\n\t MD5:  {5}\n\t SHA1 : {6}\n\t SHA256 : {7}\n\t Nom de l''algorithme de signature : {8}\n\t Version : {9}" }, { "What.is.your.first.and.last.name.", "Quels sont vos nom et prénom ?" }, { "What.is.the.name.of.your.organizational.unit.", "Quel est le nom de votre unité organisationnelle ?" }, { "What.is.the.name.of.your.organization.", "Quel est le nom de votre entreprise ?" }, { "What.is.the.name.of.your.City.or.Locality.", "Quel est le nom de votre ville de résidence ?" }, { "What.is.the.name.of.your.State.or.Province.", "Quel est le nom de votre état ou province ?" }, { "What.is.the.two.letter.country.code.for.this.unit.", "Quel est le code pays à deux lettres pour cette unité ?" }, { "Is.name.correct.", "Est-ce {0} ?" }, { "no", "non" }, { "yes", "oui" }, { "y", "o" }, { ".defaultValue.", "  [{0}]:  " }, { "Alias.alias.has.no.key", "L''alias <{0}> n''est associé à aucune clé" }, { "Alias.alias.references.an.entry.type.that.is.not.a.private.key.entry.The.keyclone.command.only.supports.cloning.of.private.key", "L''entrée à laquelle l''alias <{0}> fait référence n''est pas une entrée de type clé privée. La commande -keyclone prend uniquement en charge le clonage des clés privées" }, { ".WARNING.WARNING.WARNING.", "*****************  WARNING WARNING WARNING  *****************" }, { "Signer.d.", "Signataire n°%d :" }, { "Timestamp.", "Horodatage :" }, { "Signature.", "Signature :" }, { "CRLs.", "Listes des certificats révoqués (CRL) :" }, { "Certificate.owner.", "Propriétaire du certificat : " }, { "Not.a.signed.jar.file", "Fichier JAR non signé" }, { "No.certificate.from.the.SSL.server", "Aucun certificat du serveur SSL" }, { ".The.integrity.of.the.information.stored.in.your.keystore.", "* L'intégrité des informations stockées dans votre fichier de clés  *\n* n'a PAS été vérifiée. Pour cela, *\n* vous devez fournir le mot de passe de votre fichier de clés.                  *" }, { ".The.integrity.of.the.information.stored.in.the.srckeystore.", "* L'intégrité des informations stockées dans le fichier de clés source  *\n* n'a PAS été vérifiée. Pour cela, *\n* vous devez fournir le mot de passe de votre fichier de clés source.                  *" }, { "Certificate.reply.does.not.contain.public.key.for.alias.", "La réponse au certificat ne contient pas de clé publique pour <{0}>" }, { "Incomplete.certificate.chain.in.reply", "Chaîne de certificat incomplète dans la réponse" }, { "Certificate.chain.in.reply.does.not.verify.", "La chaîne de certificat de la réponse ne concorde pas : " }, { "Top.level.certificate.in.reply.", "Certificat de niveau supérieur dans la réponse :\n" }, { ".is.not.trusted.", "... non sécurisé. " }, { "Install.reply.anyway.no.", "Installer la réponse quand même ? [non] :  " }, { "NO", "NON" }, { "Public.keys.in.reply.and.keystore.don.t.match", "Les clés publiques de la réponse et du fichier de clés ne concordent pas" }, { "Certificate.reply.and.certificate.in.keystore.are.identical", "La réponse au certificat et le certificat du fichier de clés sont identiques" }, { "Failed.to.establish.chain.from.reply", "Impossible de créer une chaîne à partir de la réponse" }, { "n", "n" }, { "Wrong.answer.try.again", "Réponse incorrecte, recommencez" }, { "Secret.key.not.generated.alias.alias.already.exists", "Clé secrète non générée, l''alias <{0}> existe déjà" }, { "Please.provide.keysize.for.secret.key.generation", "Indiquez -keysize pour la génération de la clé secrète" }, { "Extensions.", "Extensions : " }, { ".Empty.value.", "(Valeur vide)" }, { "Extension.Request.", "Demande d'extension :" }, { "PKCS.10.Certificate.Request.Version.1.0.Subject.s.Public.Key.s.format.s.key.", "Demande de certificat PKCS #10 (version 1.0)\nSujet : %s\nClé publique : format %s pour la clé %s\n" }, { "Unknown.keyUsage.type.", "Type keyUsage inconnu : " }, { "Unknown.extendedkeyUsage.type.", "Type extendedkeyUsage inconnu : " }, { "Unknown.AccessDescription.type.", "Type AccessDescription inconnu : " }, { "Unrecognized.GeneralName.type.", "Type GeneralName non reconnu : " }, { "This.extension.cannot.be.marked.as.critical.", "Cette extension ne peut pas être marquée comme critique. " }, { "Odd.number.of.hex.digits.found.", "Nombre impair de chiffres hexadécimaux trouvé : " }, { "Unknown.extension.type.", "Type d'extension inconnu : " }, { "command.{0}.is.ambiguous.", "commande {0} ambiguë :" }, { "Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured.", "Avertissement : il n''existe pas de clé publique pour l''alias {0}. Vérifiez que le fichier de clés d''accès est correctement configuré." }, { "Warning.Class.not.found.class", "Avertissement : classe introuvable - {0}" }, { "Warning.Invalid.argument.s.for.constructor.arg", "Avertissement : arguments non valides pour le constructeur - {0}" }, { "Illegal.Principal.Type.type", "Type de principal non admis : {0}" }, { "Illegal.option.option", "Option non admise : {0}" }, { "Usage.policytool.options.", "Syntaxe : policytool [options]" }, { ".file.file.policy.file.location", "  [-file <file>]    emplacement du fichier de règles" }, { "New", "Nouveau" }, { "Open", "Ouvrir" }, { "Save", "Enregistrer" }, { "Save.As", "Enregistrer sous" }, { "View.Warning.Log", "Afficher le journal des avertissements" }, { "Exit", "Quitter" }, { "Add.Policy.Entry", "Ajouter une règle" }, { "Edit.Policy.Entry", "Modifier une règle" }, { "Remove.Policy.Entry", "Enlever une règle" }, { "Edit", "Modifier" }, { "Retain", "Conserver" }, { "Warning.File.name.may.include.escaped.backslash.characters.It.is.not.necessary.to.escape.backslash.characters.the.tool.escapes", "Avertissement : il se peut que le nom de fichier contienne des barres obliques inverses avec caractère d'échappement. Il n'est pas nécessaire d'ajouter un caractère d'échappement aux barres obliques inverses. (L'outil procède à l'échappement si nécessaire lorsqu'il écrit le contenu des règles dans la zone de stockage persistant).\n\nCliquez sur Conserver pour garder le nom saisi ou sur Modifier pour le remplacer." }, { "Add.Public.Key.Alias", "Ajouter un alias de clé publique" }, { "Remove.Public.Key.Alias", "Enlever un alias de clé publique" }, { "File", "Fichier" }, { "KeyStore", "Fichier de clés" }, { "Policy.File.", "Fichier de règles :" }, { "Could.not.open.policy.file.policyFile.e.toString.", "Impossible d''ouvrir le fichier de règles : {0}: {1}" }, { "Policy.Tool", "Policy Tool" }, { "Errors.have.occurred.while.opening.the.policy.configuration.View.the.Warning.Log.for.more.information.", "Des erreurs se sont produites à l'ouverture de la configuration de règles. Pour plus d'informations, consultez le journal des avertissements." }, { "Error", "Erreur" }, { "OK", "OK" }, { "Status", "Statut" }, { "Warning", "Avertissement" }, { "Permission.", "Droit :                                                       " }, { "Principal.Type.", "Type de principal :" }, { "Principal.Name.", "Nom de principal :" }, { "Target.Name.", "Nom de cible :                                                    " }, { "Actions.", "Actions :                                                             " }, { "OK.to.overwrite.existing.file.filename.", "Remplacer le fichier existant {0} ?" }, { "Cancel", "Annuler" }, { "CodeBase.", "Base de code :" }, { "SignedBy.", "Signé par :" }, { "Add.Principal", "Ajouter un principal" }, { "Edit.Principal", "Modifier un principal" }, { "Remove.Principal", "Enlever un principal" }, { "Principals.", "Principaux :" }, { ".Add.Permission", "  Ajouter un droit" }, { ".Edit.Permission", "  Modifier un droit" }, { "Remove.Permission", "Enlever un droit" }, { "Done", "Terminé" }, { "KeyStore.URL.", "URL du fichier de clés :" }, { "KeyStore.Type.", "Type du fichier de clés :" }, { "KeyStore.Provider.", "Fournisseur du fichier de clés :" }, { "KeyStore.Password.URL.", "URL du mot de passe du fichier de clés :" }, { "Principals", "Principaux" }, { ".Edit.Principal.", "  Modifier un principal :" }, { ".Add.New.Principal.", "  Ajouter un principal :" }, { "Permissions", "Droits" }, { ".Edit.Permission.", "  Modifier un droit :" }, { ".Add.New.Permission.", "  Ajouter un droit :" }, { "Signed.By.", "Signé par :" }, { "Cannot.Specify.Principal.with.a.Wildcard.Class.without.a.Wildcard.Name", "Impossible de spécifier un principal avec une classe générique sans nom générique" }, { "Cannot.Specify.Principal.without.a.Name", "Impossible de spécifier un principal sans nom" }, { "Permission.and.Target.Name.must.have.a.value", "Le droit et le nom de cible doivent avoir une valeur" }, { "Remove.this.Policy.Entry.", "Enlever cette règle ?" }, { "Overwrite.File", "Remplacer le fichier" }, { "Policy.successfully.written.to.filename", "Règle écrite dans {0}" }, { "null.filename", "nom de fichier NULL" }, { "Save.changes.", "Enregistrer les modifications ?" }, { "Yes", "Oui" }, { "No", "Non" }, { "Policy.Entry", "Règle" }, { "Save.Changes", "Enregistrer les modifications" }, { "No.Policy.Entry.selected", "Aucune règle sélectionnée" }, { "Unable.to.open.KeyStore.ex.toString.", "Impossible d''ouvrir le fichier de clés d''accès : {0}" }, { "No.principal.selected", "Aucun principal sélectionné" }, { "No.permission.selected", "Aucun droit sélectionné" }, { "name", "nom" }, { "configuration.type", "type de configuration" }, { "environment.variable.name", "Nom de variable d'environnement" }, { "library.name", "nom de bibliothèque" }, { "package.name", "nom de package" }, { "policy.type", "type de règle" }, { "property.name", "nom de propriété" }, { "Principal.List", "Liste de principaux" }, { "Permission.List", "Liste de droits" }, { "Code.Base", "Base de code" }, { "KeyStore.U.R.L.", "URL du fichier de clés :" }, { "KeyStore.Password.U.R.L.", "URL du mot de passe du fichier de clés :" }, { "invalid.null.input.s.", "entrées NULL non valides" }, { "actions.can.only.be.read.", "les actions sont accessibles en lecture uniquement" }, { "permission.name.name.syntax.invalid.", "syntaxe de nom de droit [{0}] non valide : " }, { "Credential.Class.not.followed.by.a.Principal.Class.and.Name", "Classe Credential non suivie d'une classe et d'un nom de principal" }, { "Principal.Class.not.followed.by.a.Principal.Name", "Classe de principal non suivie d'un nom de principal" }, { "Principal.Name.must.be.surrounded.by.quotes", "Le nom de principal doit être indiqué entre guillemets" }, { "Principal.Name.missing.end.quote", "Guillemet fermant manquant pour le nom de principal" }, { "PrivateCredentialPermission.Principal.Class.can.not.be.a.wildcard.value.if.Principal.Name.is.not.a.wildcard.value", "La classe de principal PrivateCredentialPermission ne peut pas être une valeur générique (*) si le nom de principal n'est pas une valeur générique (*)" }, { "CredOwner.Principal.Class.class.Principal.Name.name", "CredOwner :\n\tClasse de principal = {0}\n\tNom de principal = {1}" }, { "provided.null.name", "nom NULL fourni" }, { "provided.null.keyword.map", "mappage de mots-clés NULL fourni" }, { "provided.null.OID.map", "mappage OID NULL fourni" }, { "invalid.null.AccessControlContext.provided", "AccessControlContext NULL fourni non valide" }, { "invalid.null.action.provided", "action NULL fournie non valide" }, { "invalid.null.Class.provided", "classe NULL fournie non valide" }, { "Subject.", "Objet :\n" }, { ".Principal.", "\tPrincipal : " }, { ".Public.Credential.", "\tInformations d'identification publiques : " }, { ".Private.Credentials.inaccessible.", "\tInformations d'identification privées inaccessibles\n" }, { ".Private.Credential.", "\tInformations d'identification privées : " }, { ".Private.Credential.inaccessible.", "\tInformations d'identification privées inaccessibles\n" }, { "Subject.is.read.only", "Sujet en lecture seule" }, { "attempting.to.add.an.object.which.is.not.an.instance.of.java.security.Principal.to.a.Subject.s.Principal.Set", "tentative d'ajout d'un objet qui n'est pas une instance de java.security.Principal dans un ensemble de principaux du sujet" }, { "attempting.to.add.an.object.which.is.not.an.instance.of.class", "tentative d''ajout d''un objet qui n''est pas une instance de {0}" }, { "LoginModuleControlFlag.", "LoginModuleControlFlag : " }, { "Invalid.null.input.name", "Entrée NULL non valide : nom" }, { "No.LoginModules.configured.for.name", "Aucun LoginModule configuré pour {0}" }, { "invalid.null.Subject.provided", "sujet NULL fourni non valide" }, { "invalid.null.CallbackHandler.provided", "CallbackHandler NULL fourni non valide" }, { "null.subject.logout.called.before.login", "sujet NULL - Tentative de déconnexion avant la connexion" }, { "unable.to.instantiate.LoginModule.module.because.it.does.not.provide.a.no.argument.constructor", "impossible d''instancier LoginModule {0} car il ne fournit pas de constructeur sans argument" }, { "unable.to.instantiate.LoginModule", "impossible d'instancier LoginModule" }, { "unable.to.instantiate.LoginModule.", "impossible d'instancier LoginModule : " }, { "unable.to.find.LoginModule.class.", "classe LoginModule introuvable : " }, { "unable.to.access.LoginModule.", "impossible d'accéder à LoginModule : " }, { "Login.Failure.all.modules.ignored", "Echec de connexion : tous les modules ont été ignorés" }, { "java.security.policy.error.parsing.policy.message", "java.security.policy : erreur d''analyse de {0} :\n\t{1}" }, { "java.security.policy.error.adding.Permission.perm.message", "java.security.policy : erreur d''ajout de droit, {0} :\n\t{1}" }, { "java.security.policy.error.adding.Entry.message", "java.security.policy : erreur d''ajout d''entrée :\n\t{0}" }, { "alias.name.not.provided.pe.name.", "nom d''alias non fourni ({0})" }, { "unable.to.perform.substitution.on.alias.suffix", "impossible d''effectuer une substitution pour l''alias, {0}" }, { "substitution.value.prefix.unsupported", "valeur de substitution, {0}, non prise en charge" }, { "LPARAM", "(" }, { "RPARAM", ")" }, { "type.can.t.be.null", "le type ne peut être NULL" }, { "keystorePasswordURL.can.not.be.specified.without.also.specifying.keystore", "Impossible de spécifier keystorePasswordURL sans indiquer aussi le fichier de clés" }, { "expected.keystore.type", "type de fichier de clés attendu" }, { "expected.keystore.provider", "fournisseur de fichier de clés attendu" }, { "multiple.Codebase.expressions", "expressions Codebase multiples" }, { "multiple.SignedBy.expressions", "expressions SignedBy multiples" }, { "SignedBy.has.empty.alias", "SignedBy possède un alias vide" }, { "can.not.specify.Principal.with.a.wildcard.class.without.a.wildcard.name", "impossible de spécifier le principal avec une classe générique sans nom générique" }, { "expected.codeBase.or.SignedBy.or.Principal", "codeBase, SignedBy ou Principal attendu" }, { "expected.permission.entry", "entrée de droit attendue" }, { "number.", "nombre " }, { "expected.expect.read.end.of.file.", "attendu [{0}], lu [fin de fichier]" }, { "expected.read.end.of.file.", "attendu [;], lu [fin de fichier]" }, { "line.number.msg", "ligne {0} : {1}" }, { "line.number.expected.expect.found.actual.", "ligne {0} : attendu [{1}], trouvé [{2}]" }, { "null.principalClass.or.principalName", "principalClass ou principalName NULL" }, { "PKCS11.Token.providerName.Password.", "Mot de passe PKCS11 Token [{0}] : " }, { "unable.to.instantiate.Subject.based.policy", "impossible d'instancier les règles basées sur le sujet" } };
/*     */ 
/*     */   public Object[][] getContents()
/*     */   {
/* 658 */     return contents;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.util.Resources_fr
 * JD-Core Version:    0.6.2
 */
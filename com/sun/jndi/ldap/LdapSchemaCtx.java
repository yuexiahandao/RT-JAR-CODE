/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.dir.HierMemDirCtx;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.ModificationItem;
/*     */ import javax.naming.directory.SchemaViolationException;
/*     */ 
/*     */ final class LdapSchemaCtx extends HierMemDirCtx
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private static final int LEAF = 0;
/*     */   private static final int SCHEMA_ROOT = 1;
/*     */   static final int OBJECTCLASS_ROOT = 2;
/*     */   static final int ATTRIBUTE_ROOT = 3;
/*     */   static final int SYNTAX_ROOT = 4;
/*     */   static final int MATCHRULE_ROOT = 5;
/*     */   static final int OBJECTCLASS = 6;
/*     */   static final int ATTRIBUTE = 7;
/*     */   static final int SYNTAX = 8;
/*     */   static final int MATCHRULE = 9;
/*  59 */   private SchemaInfo info = null;
/*  60 */   private boolean setupMode = true;
/*     */   private int objectType;
/*     */ 
/*     */   static DirContext createSchemaTree(Hashtable paramHashtable, String paramString, LdapCtx paramLdapCtx, Attributes paramAttributes, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/*  68 */       LdapSchemaParser localLdapSchemaParser = new LdapSchemaParser(paramBoolean);
/*     */ 
/*  70 */       SchemaInfo localSchemaInfo = new SchemaInfo(paramString, paramLdapCtx, localLdapSchemaParser);
/*     */ 
/*  73 */       LdapSchemaCtx localLdapSchemaCtx = new LdapSchemaCtx(1, paramHashtable, localSchemaInfo);
/*  74 */       LdapSchemaParser.LDAP2JNDISchema(paramAttributes, localLdapSchemaCtx);
/*  75 */       return localLdapSchemaCtx;
/*     */     } catch (NamingException localNamingException) {
/*  77 */       paramLdapCtx.close();
/*  78 */       throw localNamingException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private LdapSchemaCtx(int paramInt, Hashtable paramHashtable, SchemaInfo paramSchemaInfo)
/*     */   {
/*  84 */     super(paramHashtable, true);
/*     */ 
/*  86 */     this.objectType = paramInt;
/*  87 */     this.info = paramSchemaInfo;
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException
/*     */   {
/*  92 */     this.info.close();
/*     */   }
/*     */ 
/*     */   public final void bind(Name paramName, Object paramObject, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/*  99 */     if (!this.setupMode) {
/* 100 */       if (paramObject != null) {
/* 101 */         throw new IllegalArgumentException("obj must be null");
/*     */       }
/*     */ 
/* 105 */       addServerSchema(paramAttributes);
/*     */     }
/*     */ 
/* 109 */     LdapSchemaCtx localLdapSchemaCtx = (LdapSchemaCtx)super.doCreateSubcontext(paramName, paramAttributes);
/*     */   }
/*     */ 
/*     */   protected final void doBind(Name paramName, Object paramObject, Attributes paramAttributes, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 115 */     if (!this.setupMode) {
/* 116 */       throw new SchemaViolationException("Cannot bind arbitrary object; use createSubcontext()");
/*     */     }
/*     */ 
/* 119 */     super.doBind(paramName, paramObject, paramAttributes, false);
/*     */   }
/*     */ 
/*     */   public final void rebind(Name paramName, Object paramObject, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 127 */       doLookup(paramName, false);
/* 128 */       throw new SchemaViolationException("Cannot replace existing schema object");
/*     */     }
/*     */     catch (NameNotFoundException localNameNotFoundException) {
/* 131 */       bind(paramName, paramObject, paramAttributes);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void doRebind(Name paramName, Object paramObject, Attributes paramAttributes, boolean paramBoolean) throws NamingException
/*     */   {
/* 137 */     if (!this.setupMode) {
/* 138 */       throw new SchemaViolationException("Cannot bind arbitrary object; use createSubcontext()");
/*     */     }
/*     */ 
/* 141 */     super.doRebind(paramName, paramObject, paramAttributes, false);
/*     */   }
/*     */ 
/*     */   protected final void doUnbind(Name paramName) throws NamingException
/*     */   {
/* 146 */     if (!this.setupMode)
/*     */     {
/*     */       try
/*     */       {
/* 150 */         LdapSchemaCtx localLdapSchemaCtx = (LdapSchemaCtx)doLookup(paramName, false);
/*     */ 
/* 152 */         deleteServerSchema(localLdapSchemaCtx.attrs);
/*     */       } catch (NameNotFoundException localNameNotFoundException) {
/* 154 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 158 */     super.doUnbind(paramName);
/*     */   }
/*     */ 
/*     */   protected final void doRename(Name paramName1, Name paramName2) throws NamingException
/*     */   {
/* 163 */     if (!this.setupMode) {
/* 164 */       throw new SchemaViolationException("Cannot rename a schema object");
/*     */     }
/* 166 */     super.doRename(paramName1, paramName2);
/*     */   }
/*     */ 
/*     */   protected final void doDestroySubcontext(Name paramName) throws NamingException
/*     */   {
/* 171 */     if (!this.setupMode)
/*     */     {
/*     */       try
/*     */       {
/* 175 */         LdapSchemaCtx localLdapSchemaCtx = (LdapSchemaCtx)doLookup(paramName, false);
/*     */ 
/* 177 */         deleteServerSchema(localLdapSchemaCtx.attrs);
/*     */       } catch (NameNotFoundException localNameNotFoundException) {
/* 179 */         return;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 184 */     super.doDestroySubcontext(paramName);
/*     */   }
/*     */ 
/*     */   final LdapSchemaCtx setup(int paramInt, String paramString, Attributes paramAttributes) throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 191 */       this.setupMode = true;
/* 192 */       LdapSchemaCtx localLdapSchemaCtx1 = (LdapSchemaCtx)super.doCreateSubcontext(new CompositeName(paramString), paramAttributes);
/*     */ 
/* 196 */       localLdapSchemaCtx1.objectType = paramInt;
/* 197 */       localLdapSchemaCtx1.setupMode = false;
/* 198 */       return localLdapSchemaCtx1;
/*     */     } finally {
/* 200 */       this.setupMode = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final DirContext doCreateSubcontext(Name paramName, Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/* 207 */     if ((paramAttributes == null) || (paramAttributes.size() == 0)) {
/* 208 */       throw new SchemaViolationException("Must supply attributes describing schema");
/*     */     }
/*     */ 
/* 212 */     if (!this.setupMode)
/*     */     {
/* 214 */       addServerSchema(paramAttributes);
/*     */     }
/*     */ 
/* 218 */     LdapSchemaCtx localLdapSchemaCtx = (LdapSchemaCtx)super.doCreateSubcontext(paramName, paramAttributes);
/*     */ 
/* 220 */     return localLdapSchemaCtx;
/*     */   }
/*     */ 
/*     */   private static final Attributes deepClone(Attributes paramAttributes) throws NamingException
/*     */   {
/* 225 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/* 226 */     NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/* 227 */     while (localNamingEnumeration.hasMore()) {
/* 228 */       localBasicAttributes.put((Attribute)((Attribute)localNamingEnumeration.next()).clone());
/*     */     }
/* 230 */     return localBasicAttributes;
/*     */   }
/*     */ 
/*     */   protected final void doModifyAttributes(ModificationItem[] paramArrayOfModificationItem) throws NamingException
/*     */   {
/* 235 */     if (this.setupMode) {
/* 236 */       super.doModifyAttributes(paramArrayOfModificationItem);
/*     */     } else {
/* 238 */       Attributes localAttributes = deepClone(this.attrs);
/*     */ 
/* 241 */       applyMods(paramArrayOfModificationItem, localAttributes);
/*     */ 
/* 244 */       modifyServerSchema(this.attrs, localAttributes);
/*     */ 
/* 247 */       this.attrs = localAttributes;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final HierMemDirCtx createNewCtx()
/*     */   {
/* 255 */     LdapSchemaCtx localLdapSchemaCtx = new LdapSchemaCtx(0, this.myEnv, this.info);
/* 256 */     return localLdapSchemaCtx;
/*     */   }
/*     */ 
/*     */   private final void addServerSchema(Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/*     */     Attribute localAttribute;
/* 264 */     switch (this.objectType) {
/*     */     case 2:
/* 266 */       localAttribute = this.info.parser.stringifyObjDesc(paramAttributes);
/* 267 */       break;
/*     */     case 3:
/* 270 */       localAttribute = this.info.parser.stringifyAttrDesc(paramAttributes);
/* 271 */       break;
/*     */     case 4:
/* 274 */       localAttribute = this.info.parser.stringifySyntaxDesc(paramAttributes);
/* 275 */       break;
/*     */     case 5:
/* 278 */       localAttribute = this.info.parser.stringifyMatchRuleDesc(paramAttributes);
/* 279 */       break;
/*     */     case 1:
/* 282 */       throw new SchemaViolationException("Cannot create new entry under schema root");
/*     */     default:
/* 286 */       throw new SchemaViolationException("Cannot create child of schema object");
/*     */     }
/*     */ 
/* 290 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/* 291 */     localBasicAttributes.put(localAttribute);
/*     */ 
/* 294 */     this.info.modifyAttributes(this.myEnv, 1, localBasicAttributes);
/*     */   }
/*     */ 
/*     */   private final void deleteServerSchema(Attributes paramAttributes)
/*     */     throws NamingException
/*     */   {
/*     */     Attribute localAttribute;
/* 310 */     switch (this.objectType) {
/*     */     case 2:
/* 312 */       localAttribute = this.info.parser.stringifyObjDesc(paramAttributes);
/* 313 */       break;
/*     */     case 3:
/* 316 */       localAttribute = this.info.parser.stringifyAttrDesc(paramAttributes);
/* 317 */       break;
/*     */     case 4:
/* 320 */       localAttribute = this.info.parser.stringifySyntaxDesc(paramAttributes);
/* 321 */       break;
/*     */     case 5:
/* 324 */       localAttribute = this.info.parser.stringifyMatchRuleDesc(paramAttributes);
/* 325 */       break;
/*     */     case 1:
/* 328 */       throw new SchemaViolationException("Cannot delete schema root");
/*     */     default:
/* 332 */       throw new SchemaViolationException("Cannot delete child of schema object");
/*     */     }
/*     */ 
/* 336 */     ModificationItem[] arrayOfModificationItem = new ModificationItem[1];
/* 337 */     arrayOfModificationItem[0] = new ModificationItem(3, localAttribute);
/*     */ 
/* 339 */     this.info.modifyAttributes(this.myEnv, arrayOfModificationItem);
/*     */   }
/*     */ 
/*     */   private final void modifyServerSchema(Attributes paramAttributes1, Attributes paramAttributes2)
/*     */     throws NamingException
/*     */   {
/*     */     Attribute localAttribute2;
/*     */     Attribute localAttribute1;
/* 354 */     switch (this.objectType) {
/*     */     case 6:
/* 356 */       localAttribute2 = this.info.parser.stringifyObjDesc(paramAttributes1);
/* 357 */       localAttribute1 = this.info.parser.stringifyObjDesc(paramAttributes2);
/* 358 */       break;
/*     */     case 7:
/* 361 */       localAttribute2 = this.info.parser.stringifyAttrDesc(paramAttributes1);
/* 362 */       localAttribute1 = this.info.parser.stringifyAttrDesc(paramAttributes2);
/* 363 */       break;
/*     */     case 8:
/* 366 */       localAttribute2 = this.info.parser.stringifySyntaxDesc(paramAttributes1);
/* 367 */       localAttribute1 = this.info.parser.stringifySyntaxDesc(paramAttributes2);
/* 368 */       break;
/*     */     case 9:
/* 371 */       localAttribute2 = this.info.parser.stringifyMatchRuleDesc(paramAttributes1);
/* 372 */       localAttribute1 = this.info.parser.stringifyMatchRuleDesc(paramAttributes2);
/* 373 */       break;
/*     */     default:
/* 376 */       throw new SchemaViolationException("Cannot modify schema root");
/*     */     }
/*     */ 
/* 380 */     ModificationItem[] arrayOfModificationItem = new ModificationItem[2];
/* 381 */     arrayOfModificationItem[0] = new ModificationItem(3, localAttribute2);
/* 382 */     arrayOfModificationItem[1] = new ModificationItem(1, localAttribute1);
/*     */ 
/* 384 */     this.info.modifyAttributes(this.myEnv, arrayOfModificationItem); } 
/*     */   private static final class SchemaInfo { private LdapCtx schemaEntry;
/*     */     private String schemaEntryName;
/*     */     LdapSchemaParser parser;
/*     */     private String host;
/*     */     private int port;
/*     */     private boolean hasLdapsScheme;
/*     */ 
/* 397 */     SchemaInfo(String paramString, LdapCtx paramLdapCtx, LdapSchemaParser paramLdapSchemaParser) { this.schemaEntryName = paramString;
/* 398 */       this.schemaEntry = paramLdapCtx;
/* 399 */       this.parser = paramLdapSchemaParser;
/* 400 */       this.port = paramLdapCtx.port_number;
/* 401 */       this.host = paramLdapCtx.hostname;
/* 402 */       this.hasLdapsScheme = paramLdapCtx.hasLdapsScheme; }
/*     */ 
/*     */     synchronized void close() throws NamingException
/*     */     {
/* 406 */       if (this.schemaEntry != null) {
/* 407 */         this.schemaEntry.close();
/* 408 */         this.schemaEntry = null;
/*     */       }
/*     */     }
/*     */ 
/*     */     private LdapCtx reopenEntry(Hashtable paramHashtable) throws NamingException
/*     */     {
/* 414 */       return new LdapCtx(this.schemaEntryName, this.host, this.port, paramHashtable, this.hasLdapsScheme);
/*     */     }
/*     */ 
/*     */     synchronized void modifyAttributes(Hashtable paramHashtable, ModificationItem[] paramArrayOfModificationItem)
/*     */       throws NamingException
/*     */     {
/* 420 */       if (this.schemaEntry == null) {
/* 421 */         this.schemaEntry = reopenEntry(paramHashtable);
/*     */       }
/* 423 */       this.schemaEntry.modifyAttributes("", paramArrayOfModificationItem);
/*     */     }
/*     */ 
/*     */     synchronized void modifyAttributes(Hashtable paramHashtable, int paramInt, Attributes paramAttributes) throws NamingException
/*     */     {
/* 428 */       if (this.schemaEntry == null) {
/* 429 */         this.schemaEntry = reopenEntry(paramHashtable);
/*     */       }
/* 431 */       this.schemaEntry.modifyAttributes("", paramInt, paramAttributes);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapSchemaCtx
 * JD-Core Version:    0.6.2
 */
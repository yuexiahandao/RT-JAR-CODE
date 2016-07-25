/*      */ package com.sun.org.apache.xerces.internal.impl.xs;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.ObjectListImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMap4Types;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*      */ import com.sun.org.apache.xerces.internal.parsers.DOMParser;
/*      */ import com.sun.org.apache.xerces.internal.parsers.SAXParser;
/*      */ import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*      */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*      */ import com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar;
/*      */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSParticle;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSWildcard;
/*      */ import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.util.Vector;
/*      */ import org.xml.sax.SAXException;
/*      */ 
/*      */ public class SchemaGrammar
/*      */   implements XSGrammar, XSNamespaceItem
/*      */ {
/*      */   String fTargetNamespace;
/*      */   SymbolHash fGlobalAttrDecls;
/*      */   SymbolHash fGlobalAttrGrpDecls;
/*      */   SymbolHash fGlobalElemDecls;
/*      */   SymbolHash fGlobalGroupDecls;
/*      */   SymbolHash fGlobalNotationDecls;
/*      */   SymbolHash fGlobalIDConstraintDecls;
/*      */   SymbolHash fGlobalTypeDecls;
/*      */   SymbolHash fGlobalAttrDeclsExt;
/*      */   SymbolHash fGlobalAttrGrpDeclsExt;
/*      */   SymbolHash fGlobalElemDeclsExt;
/*      */   SymbolHash fGlobalGroupDeclsExt;
/*      */   SymbolHash fGlobalNotationDeclsExt;
/*      */   SymbolHash fGlobalIDConstraintDeclsExt;
/*      */   SymbolHash fGlobalTypeDeclsExt;
/*      */   SymbolHash fAllGlobalElemDecls;
/*  111 */   XSDDescription fGrammarDescription = null;
/*      */ 
/*  114 */   XSAnnotationImpl[] fAnnotations = null;
/*      */   int fNumAnnotations;
/*  120 */   private SymbolTable fSymbolTable = null;
/*      */ 
/*  122 */   private SoftReference fSAXParser = null;
/*  123 */   private SoftReference fDOMParser = null;
/*      */ 
/*  126 */   private boolean fIsImmutable = false;
/*      */   private static final int BASICSET_COUNT = 29;
/*      */   private static final int FULLSET_COUNT = 46;
/*      */   private static final int GRAMMAR_XS = 1;
/*      */   private static final int GRAMMAR_XSI = 2;
/*  800 */   Vector fImported = null;
/*      */   private static final int INITIAL_SIZE = 16;
/*      */   private static final int INC_SIZE = 16;
/* 1062 */   private int fCTCount = 0;
/* 1063 */   private XSComplexTypeDecl[] fComplexTypeDecls = new XSComplexTypeDecl[16];
/* 1064 */   private SimpleLocator[] fCTLocators = new SimpleLocator[16];
/*      */   private static final int REDEFINED_GROUP_INIT_SIZE = 2;
/* 1069 */   private int fRGCount = 0;
/* 1070 */   private XSGroupDecl[] fRedefinedGroupDecls = new XSGroupDecl[2];
/* 1071 */   private SimpleLocator[] fRGLocators = new SimpleLocator[1];
/*      */ 
/* 1075 */   boolean fFullChecked = false;
/*      */ 
/* 1159 */   private int fSubGroupCount = 0;
/* 1160 */   private XSElementDecl[] fSubGroups = new XSElementDecl[16];
/*      */ 
/* 1173 */   public static final XSComplexTypeDecl fAnyType = new XSAnyType();
/*      */ 
/* 1296 */   public static final BuiltinSchemaGrammar SG_SchemaNS = new BuiltinSchemaGrammar(1, (short)1);
/* 1297 */   private static final BuiltinSchemaGrammar SG_SchemaNSExtended = new BuiltinSchemaGrammar(1, (short)2);
/*      */ 
/* 1299 */   public static final XSSimpleType fAnySimpleType = (XSSimpleType)SG_SchemaNS.getGlobalTypeDecl("anySimpleType");
/*      */ 
/* 1302 */   public static final BuiltinSchemaGrammar SG_XSI = new BuiltinSchemaGrammar(2, (short)1);
/*      */   private static final short MAX_COMP_IDX = 16;
/* 1341 */   private static final boolean[] GLOBAL_COMP = { false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true };
/*      */ 
/* 1361 */   private XSNamedMap[] fComponents = null;
/* 1362 */   private ObjectList[] fComponentsExt = null;
/*      */ 
/* 1366 */   private Vector fDocuments = null;
/* 1367 */   private Vector fLocations = null;
/*      */ 
/*      */   protected SchemaGrammar()
/*      */   {
/*      */   }
/*      */ 
/*      */   public SchemaGrammar(String targetNamespace, XSDDescription grammarDesc, SymbolTable symbolTable)
/*      */   {
/*  145 */     this.fTargetNamespace = targetNamespace;
/*  146 */     this.fGrammarDescription = grammarDesc;
/*  147 */     this.fSymbolTable = symbolTable;
/*      */ 
/*  152 */     this.fGlobalAttrDecls = new SymbolHash();
/*  153 */     this.fGlobalAttrGrpDecls = new SymbolHash();
/*  154 */     this.fGlobalElemDecls = new SymbolHash();
/*  155 */     this.fGlobalGroupDecls = new SymbolHash();
/*  156 */     this.fGlobalNotationDecls = new SymbolHash();
/*  157 */     this.fGlobalIDConstraintDecls = new SymbolHash();
/*      */ 
/*  160 */     this.fGlobalAttrDeclsExt = new SymbolHash();
/*  161 */     this.fGlobalAttrGrpDeclsExt = new SymbolHash();
/*  162 */     this.fGlobalElemDeclsExt = new SymbolHash();
/*  163 */     this.fGlobalGroupDeclsExt = new SymbolHash();
/*  164 */     this.fGlobalNotationDeclsExt = new SymbolHash();
/*  165 */     this.fGlobalIDConstraintDeclsExt = new SymbolHash();
/*  166 */     this.fGlobalTypeDeclsExt = new SymbolHash();
/*      */ 
/*  169 */     this.fAllGlobalElemDecls = new SymbolHash();
/*      */ 
/*  174 */     if (this.fTargetNamespace == SchemaSymbols.URI_SCHEMAFORSCHEMA)
/*  175 */       this.fGlobalTypeDecls = SG_SchemaNS.fGlobalTypeDecls.makeClone();
/*      */     else
/*  177 */       this.fGlobalTypeDecls = new SymbolHash();
/*      */   }
/*      */ 
/*      */   public SchemaGrammar(SchemaGrammar grammar)
/*      */   {
/*  182 */     this.fTargetNamespace = grammar.fTargetNamespace;
/*  183 */     this.fGrammarDescription = grammar.fGrammarDescription.makeClone();
/*      */ 
/*  185 */     this.fSymbolTable = grammar.fSymbolTable;
/*      */ 
/*  187 */     this.fGlobalAttrDecls = grammar.fGlobalAttrDecls.makeClone();
/*  188 */     this.fGlobalAttrGrpDecls = grammar.fGlobalAttrGrpDecls.makeClone();
/*  189 */     this.fGlobalElemDecls = grammar.fGlobalElemDecls.makeClone();
/*  190 */     this.fGlobalGroupDecls = grammar.fGlobalGroupDecls.makeClone();
/*  191 */     this.fGlobalNotationDecls = grammar.fGlobalNotationDecls.makeClone();
/*  192 */     this.fGlobalIDConstraintDecls = grammar.fGlobalIDConstraintDecls.makeClone();
/*  193 */     this.fGlobalTypeDecls = grammar.fGlobalTypeDecls.makeClone();
/*      */ 
/*  196 */     this.fGlobalAttrDeclsExt = grammar.fGlobalAttrDeclsExt.makeClone();
/*  197 */     this.fGlobalAttrGrpDeclsExt = grammar.fGlobalAttrGrpDeclsExt.makeClone();
/*  198 */     this.fGlobalElemDeclsExt = grammar.fGlobalElemDeclsExt.makeClone();
/*  199 */     this.fGlobalGroupDeclsExt = grammar.fGlobalGroupDeclsExt.makeClone();
/*  200 */     this.fGlobalNotationDeclsExt = grammar.fGlobalNotationDeclsExt.makeClone();
/*  201 */     this.fGlobalIDConstraintDeclsExt = grammar.fGlobalIDConstraintDeclsExt.makeClone();
/*  202 */     this.fGlobalTypeDeclsExt = grammar.fGlobalTypeDeclsExt.makeClone();
/*      */ 
/*  205 */     this.fAllGlobalElemDecls = grammar.fAllGlobalElemDecls.makeClone();
/*      */ 
/*  208 */     this.fNumAnnotations = grammar.fNumAnnotations;
/*  209 */     if (this.fNumAnnotations > 0) {
/*  210 */       this.fAnnotations = new XSAnnotationImpl[grammar.fAnnotations.length];
/*  211 */       System.arraycopy(grammar.fAnnotations, 0, this.fAnnotations, 0, this.fNumAnnotations);
/*      */     }
/*      */ 
/*  215 */     this.fSubGroupCount = grammar.fSubGroupCount;
/*  216 */     if (this.fSubGroupCount > 0) {
/*  217 */       this.fSubGroups = new XSElementDecl[grammar.fSubGroups.length];
/*  218 */       System.arraycopy(grammar.fSubGroups, 0, this.fSubGroups, 0, this.fSubGroupCount);
/*      */     }
/*      */ 
/*  222 */     this.fCTCount = grammar.fCTCount;
/*  223 */     if (this.fCTCount > 0) {
/*  224 */       this.fComplexTypeDecls = new XSComplexTypeDecl[grammar.fComplexTypeDecls.length];
/*  225 */       this.fCTLocators = new SimpleLocator[grammar.fCTLocators.length];
/*  226 */       System.arraycopy(grammar.fComplexTypeDecls, 0, this.fComplexTypeDecls, 0, this.fCTCount);
/*  227 */       System.arraycopy(grammar.fCTLocators, 0, this.fCTLocators, 0, this.fCTCount);
/*      */     }
/*      */ 
/*  231 */     this.fRGCount = grammar.fRGCount;
/*  232 */     if (this.fRGCount > 0) {
/*  233 */       this.fRedefinedGroupDecls = new XSGroupDecl[grammar.fRedefinedGroupDecls.length];
/*  234 */       this.fRGLocators = new SimpleLocator[grammar.fRGLocators.length];
/*  235 */       System.arraycopy(grammar.fRedefinedGroupDecls, 0, this.fRedefinedGroupDecls, 0, this.fRGCount);
/*  236 */       System.arraycopy(grammar.fRGLocators, 0, this.fRGLocators, 0, this.fRGCount);
/*      */     }
/*      */ 
/*  240 */     if (grammar.fImported != null) {
/*  241 */       this.fImported = new Vector();
/*  242 */       for (int i = 0; i < grammar.fImported.size(); i++) {
/*  243 */         this.fImported.add(grammar.fImported.elementAt(i));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  248 */     if (grammar.fLocations != null)
/*  249 */       for (int k = 0; k < grammar.fLocations.size(); k++)
/*  250 */         addDocument(null, (String)grammar.fLocations.elementAt(k));
/*      */   }
/*      */ 
/*      */   public XMLGrammarDescription getGrammarDescription()
/*      */   {
/*  792 */     return this.fGrammarDescription;
/*      */   }
/*      */ 
/*      */   public boolean isNamespaceAware()
/*      */   {
/*  797 */     return true;
/*      */   }
/*      */ 
/*      */   public void setImportedGrammars(Vector importedGrammars)
/*      */   {
/*  803 */     this.fImported = importedGrammars;
/*      */   }
/*      */ 
/*      */   public Vector getImportedGrammars() {
/*  807 */     return this.fImported;
/*      */   }
/*      */ 
/*      */   public final String getTargetNamespace()
/*      */   {
/*  814 */     return this.fTargetNamespace;
/*      */   }
/*      */ 
/*      */   public void addGlobalAttributeDecl(XSAttributeDecl decl)
/*      */   {
/*  821 */     this.fGlobalAttrDecls.put(decl.fName, decl);
/*  822 */     decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalAttributeDecl(XSAttributeDecl decl, String location) {
/*  826 */     this.fGlobalAttrDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
/*  827 */     if (decl.getNamespaceItem() == null)
/*  828 */       decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl)
/*      */   {
/*  836 */     this.fGlobalAttrGrpDecls.put(decl.fName, decl);
/*  837 */     decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl, String location) {
/*  841 */     this.fGlobalAttrGrpDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
/*  842 */     if (decl.getNamespaceItem() == null)
/*  843 */       decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalElementDeclAll(XSElementDecl decl)
/*      */   {
/*  851 */     if (this.fAllGlobalElemDecls.get(decl) == null) {
/*  852 */       this.fAllGlobalElemDecls.put(decl, decl);
/*      */ 
/*  855 */       if (decl.fSubGroup != null) {
/*  856 */         if (this.fSubGroupCount == this.fSubGroups.length)
/*  857 */           this.fSubGroups = resize(this.fSubGroups, this.fSubGroupCount + 16);
/*  858 */         this.fSubGroups[(this.fSubGroupCount++)] = decl;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void addGlobalElementDecl(XSElementDecl decl) {
/*  864 */     this.fGlobalElemDecls.put(decl.fName, decl);
/*  865 */     decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalElementDecl(XSElementDecl decl, String location) {
/*  869 */     this.fGlobalElemDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
/*  870 */     if (decl.getNamespaceItem() == null)
/*  871 */       decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalGroupDecl(XSGroupDecl decl)
/*      */   {
/*  879 */     this.fGlobalGroupDecls.put(decl.fName, decl);
/*  880 */     decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalGroupDecl(XSGroupDecl decl, String location) {
/*  884 */     this.fGlobalGroupDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
/*  885 */     if (decl.getNamespaceItem() == null)
/*  886 */       decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalNotationDecl(XSNotationDecl decl)
/*      */   {
/*  894 */     this.fGlobalNotationDecls.put(decl.fName, decl);
/*  895 */     decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalNotationDecl(XSNotationDecl decl, String location) {
/*  899 */     this.fGlobalNotationDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
/*  900 */     if (decl.getNamespaceItem() == null)
/*  901 */       decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalTypeDecl(XSTypeDefinition decl)
/*      */   {
/*  909 */     this.fGlobalTypeDecls.put(decl.getName(), decl);
/*  910 */     if ((decl instanceof XSComplexTypeDecl)) {
/*  911 */       ((XSComplexTypeDecl)decl).setNamespaceItem(this);
/*      */     }
/*  913 */     else if ((decl instanceof XSSimpleTypeDecl))
/*  914 */       ((XSSimpleTypeDecl)decl).setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalTypeDecl(XSTypeDefinition decl, String location)
/*      */   {
/*  919 */     this.fGlobalTypeDeclsExt.put((location != null ? location : "") + "," + decl.getName(), decl);
/*  920 */     if (decl.getNamespaceItem() == null)
/*  921 */       if ((decl instanceof XSComplexTypeDecl)) {
/*  922 */         ((XSComplexTypeDecl)decl).setNamespaceItem(this);
/*      */       }
/*  924 */       else if ((decl instanceof XSSimpleTypeDecl))
/*  925 */         ((XSSimpleTypeDecl)decl).setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl)
/*      */   {
/*  934 */     this.fGlobalTypeDecls.put(decl.getName(), decl);
/*  935 */     decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl, String location) {
/*  939 */     this.fGlobalTypeDeclsExt.put((location != null ? location : "") + "," + decl.getName(), decl);
/*  940 */     if (decl.getNamespaceItem() == null)
/*  941 */       decl.setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalSimpleTypeDecl(XSSimpleType decl)
/*      */   {
/*  949 */     this.fGlobalTypeDecls.put(decl.getName(), decl);
/*  950 */     if ((decl instanceof XSSimpleTypeDecl))
/*  951 */       ((XSSimpleTypeDecl)decl).setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public void addGlobalSimpleTypeDecl(XSSimpleType decl, String location)
/*      */   {
/*  956 */     this.fGlobalTypeDeclsExt.put((location != null ? location : "") + "," + decl.getName(), decl);
/*  957 */     if ((decl.getNamespaceItem() == null) && ((decl instanceof XSSimpleTypeDecl)))
/*  958 */       ((XSSimpleTypeDecl)decl).setNamespaceItem(this);
/*      */   }
/*      */ 
/*      */   public final void addIDConstraintDecl(XSElementDecl elmDecl, IdentityConstraint decl)
/*      */   {
/*  966 */     elmDecl.addIDConstraint(decl);
/*  967 */     this.fGlobalIDConstraintDecls.put(decl.getIdentityConstraintName(), decl);
/*      */   }
/*      */ 
/*      */   public final void addIDConstraintDecl(XSElementDecl elmDecl, IdentityConstraint decl, String location) {
/*  971 */     this.fGlobalIDConstraintDeclsExt.put((location != null ? location : "") + "," + decl.getIdentityConstraintName(), decl);
/*      */   }
/*      */ 
/*      */   public final XSAttributeDecl getGlobalAttributeDecl(String declName)
/*      */   {
/*  978 */     return (XSAttributeDecl)this.fGlobalAttrDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final XSAttributeDecl getGlobalAttributeDecl(String declName, String location) {
/*  982 */     return (XSAttributeDecl)this.fGlobalAttrDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String declName)
/*      */   {
/*  989 */     return (XSAttributeGroupDecl)this.fGlobalAttrGrpDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String declName, String location) {
/*  993 */     return (XSAttributeGroupDecl)this.fGlobalAttrGrpDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final XSElementDecl getGlobalElementDecl(String declName)
/*      */   {
/* 1000 */     return (XSElementDecl)this.fGlobalElemDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final XSElementDecl getGlobalElementDecl(String declName, String location) {
/* 1004 */     return (XSElementDecl)this.fGlobalElemDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final XSGroupDecl getGlobalGroupDecl(String declName)
/*      */   {
/* 1011 */     return (XSGroupDecl)this.fGlobalGroupDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final XSGroupDecl getGlobalGroupDecl(String declName, String location) {
/* 1015 */     return (XSGroupDecl)this.fGlobalGroupDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final XSNotationDecl getGlobalNotationDecl(String declName)
/*      */   {
/* 1022 */     return (XSNotationDecl)this.fGlobalNotationDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final XSNotationDecl getGlobalNotationDecl(String declName, String location) {
/* 1026 */     return (XSNotationDecl)this.fGlobalNotationDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final XSTypeDefinition getGlobalTypeDecl(String declName)
/*      */   {
/* 1033 */     return (XSTypeDefinition)this.fGlobalTypeDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final XSTypeDefinition getGlobalTypeDecl(String declName, String location) {
/* 1037 */     return (XSTypeDefinition)this.fGlobalTypeDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final IdentityConstraint getIDConstraintDecl(String declName)
/*      */   {
/* 1044 */     return (IdentityConstraint)this.fGlobalIDConstraintDecls.get(declName);
/*      */   }
/*      */ 
/*      */   public final IdentityConstraint getIDConstraintDecl(String declName, String location) {
/* 1048 */     return (IdentityConstraint)this.fGlobalIDConstraintDeclsExt.get((location != null ? location : "") + "," + declName);
/*      */   }
/*      */ 
/*      */   public final boolean hasIDConstraints()
/*      */   {
/* 1055 */     return this.fGlobalIDConstraintDecls.getLength() > 0;
/*      */   }
/*      */ 
/*      */   public void addComplexTypeDecl(XSComplexTypeDecl decl, SimpleLocator locator)
/*      */   {
/* 1081 */     if (this.fCTCount == this.fComplexTypeDecls.length) {
/* 1082 */       this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount + 16);
/* 1083 */       this.fCTLocators = resize(this.fCTLocators, this.fCTCount + 16);
/*      */     }
/* 1085 */     this.fCTLocators[this.fCTCount] = locator;
/* 1086 */     this.fComplexTypeDecls[(this.fCTCount++)] = decl;
/*      */   }
/*      */ 
/*      */   public void addRedefinedGroupDecl(XSGroupDecl derived, XSGroupDecl base, SimpleLocator locator)
/*      */   {
/* 1093 */     if (this.fRGCount == this.fRedefinedGroupDecls.length)
/*      */     {
/* 1095 */       this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount << 1);
/* 1096 */       this.fRGLocators = resize(this.fRGLocators, this.fRGCount);
/*      */     }
/* 1098 */     this.fRGLocators[(this.fRGCount / 2)] = locator;
/* 1099 */     this.fRedefinedGroupDecls[(this.fRGCount++)] = derived;
/* 1100 */     this.fRedefinedGroupDecls[(this.fRGCount++)] = base;
/*      */   }
/*      */ 
/*      */   final XSComplexTypeDecl[] getUncheckedComplexTypeDecls()
/*      */   {
/* 1107 */     if (this.fCTCount < this.fComplexTypeDecls.length) {
/* 1108 */       this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
/* 1109 */       this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
/*      */     }
/* 1111 */     return this.fComplexTypeDecls;
/*      */   }
/*      */ 
/*      */   final SimpleLocator[] getUncheckedCTLocators()
/*      */   {
/* 1118 */     if (this.fCTCount < this.fCTLocators.length) {
/* 1119 */       this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
/* 1120 */       this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
/*      */     }
/* 1122 */     return this.fCTLocators;
/*      */   }
/*      */ 
/*      */   final XSGroupDecl[] getRedefinedGroupDecls()
/*      */   {
/* 1129 */     if (this.fRGCount < this.fRedefinedGroupDecls.length) {
/* 1130 */       this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount);
/* 1131 */       this.fRGLocators = resize(this.fRGLocators, this.fRGCount / 2);
/*      */     }
/* 1133 */     return this.fRedefinedGroupDecls;
/*      */   }
/*      */ 
/*      */   final SimpleLocator[] getRGLocators()
/*      */   {
/* 1140 */     if (this.fRGCount < this.fRedefinedGroupDecls.length) {
/* 1141 */       this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount);
/* 1142 */       this.fRGLocators = resize(this.fRGLocators, this.fRGCount / 2);
/*      */     }
/* 1144 */     return this.fRGLocators;
/*      */   }
/*      */ 
/*      */   final void setUncheckedTypeNum(int newSize)
/*      */   {
/* 1152 */     this.fCTCount = newSize;
/* 1153 */     this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
/* 1154 */     this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
/*      */   }
/*      */ 
/*      */   final XSElementDecl[] getSubstitutionGroups()
/*      */   {
/* 1166 */     if (this.fSubGroupCount < this.fSubGroups.length)
/* 1167 */       this.fSubGroups = resize(this.fSubGroups, this.fSubGroupCount);
/* 1168 */     return this.fSubGroups;
/*      */   }
/*      */ 
/*      */   public static SchemaGrammar getS4SGrammar(short schemaVersion)
/*      */   {
/* 1305 */     if (schemaVersion == 1) {
/* 1306 */       return SG_SchemaNS;
/*      */     }
/*      */ 
/* 1309 */     return SG_SchemaNSExtended;
/*      */   }
/*      */ 
/*      */   static final XSComplexTypeDecl[] resize(XSComplexTypeDecl[] oldArray, int newSize)
/*      */   {
/* 1314 */     XSComplexTypeDecl[] newArray = new XSComplexTypeDecl[newSize];
/* 1315 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/* 1316 */     return newArray;
/*      */   }
/*      */ 
/*      */   static final XSGroupDecl[] resize(XSGroupDecl[] oldArray, int newSize) {
/* 1320 */     XSGroupDecl[] newArray = new XSGroupDecl[newSize];
/* 1321 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/* 1322 */     return newArray;
/*      */   }
/*      */ 
/*      */   static final XSElementDecl[] resize(XSElementDecl[] oldArray, int newSize) {
/* 1326 */     XSElementDecl[] newArray = new XSElementDecl[newSize];
/* 1327 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/* 1328 */     return newArray;
/*      */   }
/*      */ 
/*      */   static final SimpleLocator[] resize(SimpleLocator[] oldArray, int newSize) {
/* 1332 */     SimpleLocator[] newArray = new SimpleLocator[newSize];
/* 1333 */     System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
/* 1334 */     return newArray;
/*      */   }
/*      */ 
/*      */   public synchronized void addDocument(Object document, String location)
/*      */   {
/* 1370 */     if (this.fDocuments == null) {
/* 1371 */       this.fDocuments = new Vector();
/* 1372 */       this.fLocations = new Vector();
/*      */     }
/* 1374 */     this.fDocuments.addElement(document);
/* 1375 */     this.fLocations.addElement(location);
/*      */   }
/*      */ 
/*      */   public synchronized void removeDocument(int index) {
/* 1379 */     if ((this.fDocuments != null) && (index >= 0) && (index < this.fDocuments.size()))
/*      */     {
/* 1382 */       this.fDocuments.removeElementAt(index);
/* 1383 */       this.fLocations.removeElementAt(index);
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getSchemaNamespace()
/*      */   {
/* 1393 */     return this.fTargetNamespace;
/*      */   }
/*      */ 
/*      */   synchronized DOMParser getDOMParser()
/*      */   {
/* 1398 */     if (this.fDOMParser != null) {
/* 1399 */       DOMParser parser = (DOMParser)this.fDOMParser.get();
/* 1400 */       if (parser != null) {
/* 1401 */         return parser;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1406 */     XML11Configuration config = new XML11Configuration(this.fSymbolTable);
/*      */ 
/* 1410 */     config.setFeature("http://xml.org/sax/features/namespaces", true);
/* 1411 */     config.setFeature("http://xml.org/sax/features/validation", false);
/*      */ 
/* 1413 */     DOMParser parser = new DOMParser(config);
/*      */     try {
/* 1415 */       parser.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
/*      */     } catch (SAXException exc) {
/*      */     }
/* 1418 */     this.fDOMParser = new SoftReference(parser);
/* 1419 */     return parser;
/*      */   }
/*      */ 
/*      */   synchronized SAXParser getSAXParser() {
/* 1423 */     if (this.fSAXParser != null) {
/* 1424 */       SAXParser parser = (SAXParser)this.fSAXParser.get();
/* 1425 */       if (parser != null) {
/* 1426 */         return parser;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1431 */     XML11Configuration config = new XML11Configuration(this.fSymbolTable);
/*      */ 
/* 1435 */     config.setFeature("http://xml.org/sax/features/namespaces", true);
/* 1436 */     config.setFeature("http://xml.org/sax/features/validation", false);
/* 1437 */     SAXParser parser = new SAXParser(config);
/* 1438 */     this.fSAXParser = new SoftReference(parser);
/* 1439 */     return parser;
/*      */   }
/*      */ 
/*      */   public synchronized XSNamedMap getComponents(short objectType)
/*      */   {
/* 1456 */     if ((objectType <= 0) || (objectType > 16) || (GLOBAL_COMP[objectType] == 0))
/*      */     {
/* 1458 */       return XSNamedMapImpl.EMPTY_MAP;
/*      */     }
/*      */ 
/* 1461 */     if (this.fComponents == null) {
/* 1462 */       this.fComponents = new XSNamedMap[17];
/*      */     }
/*      */ 
/* 1465 */     if (this.fComponents[objectType] == null) {
/* 1466 */       SymbolHash table = null;
/* 1467 */       switch (objectType) {
/*      */       case 3:
/*      */       case 15:
/*      */       case 16:
/* 1471 */         table = this.fGlobalTypeDecls;
/* 1472 */         break;
/*      */       case 1:
/* 1474 */         table = this.fGlobalAttrDecls;
/* 1475 */         break;
/*      */       case 2:
/* 1477 */         table = this.fGlobalElemDecls;
/* 1478 */         break;
/*      */       case 5:
/* 1480 */         table = this.fGlobalAttrGrpDecls;
/* 1481 */         break;
/*      */       case 6:
/* 1483 */         table = this.fGlobalGroupDecls;
/* 1484 */         break;
/*      */       case 11:
/* 1486 */         table = this.fGlobalNotationDecls;
/*      */       case 4:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 12:
/*      */       case 13:
/* 1492 */       case 14: } if ((objectType == 15) || (objectType == 16))
/*      */       {
/* 1494 */         this.fComponents[objectType] = new XSNamedMap4Types(this.fTargetNamespace, table, objectType);
/*      */       }
/*      */       else {
/* 1497 */         this.fComponents[objectType] = new XSNamedMapImpl(this.fTargetNamespace, table);
/*      */       }
/*      */     }
/*      */ 
/* 1501 */     return this.fComponents[objectType];
/*      */   }
/*      */ 
/*      */   public synchronized ObjectList getComponentsExt(short objectType) {
/* 1505 */     if ((objectType <= 0) || (objectType > 16) || (GLOBAL_COMP[objectType] == 0))
/*      */     {
/* 1507 */       return ObjectListImpl.EMPTY_LIST;
/*      */     }
/*      */ 
/* 1510 */     if (this.fComponentsExt == null) {
/* 1511 */       this.fComponentsExt = new ObjectList[17];
/*      */     }
/*      */ 
/* 1514 */     if (this.fComponentsExt[objectType] == null) {
/* 1515 */       SymbolHash table = null;
/* 1516 */       switch (objectType) {
/*      */       case 3:
/*      */       case 15:
/*      */       case 16:
/* 1520 */         table = this.fGlobalTypeDeclsExt;
/* 1521 */         break;
/*      */       case 1:
/* 1523 */         table = this.fGlobalAttrDeclsExt;
/* 1524 */         break;
/*      */       case 2:
/* 1526 */         table = this.fGlobalElemDeclsExt;
/* 1527 */         break;
/*      */       case 5:
/* 1529 */         table = this.fGlobalAttrGrpDeclsExt;
/* 1530 */         break;
/*      */       case 6:
/* 1532 */         table = this.fGlobalGroupDeclsExt;
/* 1533 */         break;
/*      */       case 11:
/* 1535 */         table = this.fGlobalNotationDeclsExt;
/*      */       case 4:
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 12:
/*      */       case 13:
/* 1539 */       case 14: } Object[] entries = table.getEntries();
/* 1540 */       this.fComponentsExt[objectType] = new ObjectListImpl(entries, entries.length);
/*      */     }
/*      */ 
/* 1543 */     return this.fComponentsExt[objectType];
/*      */   }
/*      */ 
/*      */   public synchronized void resetComponents() {
/* 1547 */     this.fComponents = null;
/* 1548 */     this.fComponentsExt = null;
/*      */   }
/*      */ 
/*      */   public XSTypeDefinition getTypeDefinition(String name)
/*      */   {
/* 1559 */     return getGlobalTypeDecl(name);
/*      */   }
/*      */ 
/*      */   public XSAttributeDeclaration getAttributeDeclaration(String name)
/*      */   {
/* 1569 */     return getGlobalAttributeDecl(name);
/*      */   }
/*      */ 
/*      */   public XSElementDeclaration getElementDeclaration(String name)
/*      */   {
/* 1579 */     return getGlobalElementDecl(name);
/*      */   }
/*      */ 
/*      */   public XSAttributeGroupDefinition getAttributeGroup(String name)
/*      */   {
/* 1589 */     return getGlobalAttributeGroupDecl(name);
/*      */   }
/*      */ 
/*      */   public XSModelGroupDefinition getModelGroupDefinition(String name)
/*      */   {
/* 1600 */     return getGlobalGroupDecl(name);
/*      */   }
/*      */ 
/*      */   public XSNotationDeclaration getNotationDeclaration(String name)
/*      */   {
/* 1611 */     return getGlobalNotationDecl(name);
/*      */   }
/*      */ 
/*      */   public StringList getDocumentLocations()
/*      */   {
/* 1621 */     return new StringListImpl(this.fLocations);
/*      */   }
/*      */ 
/*      */   public XSModel toXSModel()
/*      */   {
/* 1631 */     return new XSModelImpl(new SchemaGrammar[] { this });
/*      */   }
/*      */ 
/*      */   public XSModel toXSModel(XSGrammar[] grammars) {
/* 1635 */     if ((grammars == null) || (grammars.length == 0)) {
/* 1636 */       return toXSModel();
/*      */     }
/* 1638 */     int len = grammars.length;
/* 1639 */     boolean hasSelf = false;
/* 1640 */     for (int i = 0; i < len; i++) {
/* 1641 */       if (grammars[i] == this) {
/* 1642 */         hasSelf = true;
/* 1643 */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1647 */     SchemaGrammar[] gs = new SchemaGrammar[hasSelf ? len : len + 1];
/* 1648 */     for (int i = 0; i < len; i++)
/* 1649 */       gs[i] = ((SchemaGrammar)grammars[i]);
/* 1650 */     if (!hasSelf)
/* 1651 */       gs[len] = this;
/* 1652 */     return new XSModelImpl(gs);
/*      */   }
/*      */ 
/*      */   public XSObjectList getAnnotations()
/*      */   {
/* 1659 */     if (this.fNumAnnotations == 0) {
/* 1660 */       return XSObjectListImpl.EMPTY_LIST;
/*      */     }
/* 1662 */     return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
/*      */   }
/*      */ 
/*      */   public void addAnnotation(XSAnnotationImpl annotation) {
/* 1666 */     if (annotation == null) {
/* 1667 */       return;
/*      */     }
/* 1669 */     if (this.fAnnotations == null) {
/* 1670 */       this.fAnnotations = new XSAnnotationImpl[2];
/*      */     }
/* 1672 */     else if (this.fNumAnnotations == this.fAnnotations.length) {
/* 1673 */       XSAnnotationImpl[] newArray = new XSAnnotationImpl[this.fNumAnnotations << 1];
/* 1674 */       System.arraycopy(this.fAnnotations, 0, newArray, 0, this.fNumAnnotations);
/* 1675 */       this.fAnnotations = newArray;
/*      */     }
/* 1677 */     this.fAnnotations[(this.fNumAnnotations++)] = annotation;
/*      */   }
/*      */ 
/*      */   public void setImmutable(boolean isImmutable) {
/* 1681 */     this.fIsImmutable = isImmutable;
/*      */   }
/*      */ 
/*      */   public boolean isImmutable() {
/* 1685 */     return this.fIsImmutable;
/*      */   }
/*      */ 
/*      */   private static class BuiltinAttrDecl extends XSAttributeDecl
/*      */   {
/*      */     public BuiltinAttrDecl(String name, String tns, XSSimpleType type, short scope)
/*      */     {
/* 1269 */       this.fName = name;
/* 1270 */       this.fTargetNamespace = tns;
/* 1271 */       this.fType = type;
/* 1272 */       this.fScope = scope;
/*      */     }
/*      */ 
/*      */     public void setValues(String name, String targetNamespace, XSSimpleType simpleType, short constraintType, short scope, ValidatedInfo valInfo, XSComplexTypeDecl enclosingCT)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void reset()
/*      */     {
/*      */     }
/*      */ 
/*      */     public XSAnnotation getAnnotation()
/*      */     {
/* 1286 */       return null;
/*      */     }
/*      */ 
/*      */     public XSNamespaceItem getNamespaceItem() {
/* 1290 */       return SchemaGrammar.SG_XSI;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static class BuiltinSchemaGrammar extends SchemaGrammar
/*      */   {
/*      */     private static final String EXTENDED_SCHEMA_FACTORY_CLASS = "com.sun.org.apache.xerces.internal.impl.dv.xs.ExtendedSchemaDVFactoryImpl";
/*      */ 
/*      */     public BuiltinSchemaGrammar(int grammar, short schemaVersion)
/*      */     {
/*      */       SchemaDVFactory schemaFactory;
/*      */       SchemaDVFactory schemaFactory;
/*  277 */       if (schemaVersion == 1) {
/*  278 */         schemaFactory = SchemaDVFactory.getInstance();
/*      */       }
/*      */       else {
/*  281 */         schemaFactory = SchemaDVFactory.getInstance("com.sun.org.apache.xerces.internal.impl.dv.xs.ExtendedSchemaDVFactoryImpl");
/*      */       }
/*      */ 
/*  284 */       if (grammar == 1)
/*      */       {
/*  286 */         this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
/*      */ 
/*  289 */         this.fGrammarDescription = new XSDDescription();
/*  290 */         this.fGrammarDescription.fContextType = 3;
/*  291 */         this.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
/*      */ 
/*  294 */         this.fGlobalAttrDecls = new SymbolHash(1);
/*  295 */         this.fGlobalAttrGrpDecls = new SymbolHash(1);
/*  296 */         this.fGlobalElemDecls = new SymbolHash(1);
/*  297 */         this.fGlobalGroupDecls = new SymbolHash(1);
/*  298 */         this.fGlobalNotationDecls = new SymbolHash(1);
/*  299 */         this.fGlobalIDConstraintDecls = new SymbolHash(1);
/*      */ 
/*  302 */         this.fGlobalAttrDeclsExt = new SymbolHash(1);
/*  303 */         this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
/*  304 */         this.fGlobalElemDeclsExt = new SymbolHash(1);
/*  305 */         this.fGlobalGroupDeclsExt = new SymbolHash(1);
/*  306 */         this.fGlobalNotationDeclsExt = new SymbolHash(1);
/*  307 */         this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
/*  308 */         this.fGlobalTypeDeclsExt = new SymbolHash(1);
/*      */ 
/*  311 */         this.fAllGlobalElemDecls = new SymbolHash(1);
/*      */ 
/*  314 */         this.fGlobalTypeDecls = schemaFactory.getBuiltInTypes();
/*      */ 
/*  318 */         int length = this.fGlobalTypeDecls.getLength();
/*  319 */         XSTypeDefinition[] typeDefinitions = new XSTypeDefinition[length];
/*  320 */         this.fGlobalTypeDecls.getValues(typeDefinitions, 0);
/*  321 */         for (int i = 0; i < length; i++) {
/*  322 */           XSTypeDefinition xtd = typeDefinitions[i];
/*  323 */           if ((xtd instanceof XSSimpleTypeDecl)) {
/*  324 */             ((XSSimpleTypeDecl)xtd).setNamespaceItem(this);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  329 */         this.fGlobalTypeDecls.put(fAnyType.getName(), fAnyType);
/*      */       }
/*  331 */       else if (grammar == 2)
/*      */       {
/*  333 */         this.fTargetNamespace = SchemaSymbols.URI_XSI;
/*      */ 
/*  335 */         this.fGrammarDescription = new XSDDescription();
/*  336 */         this.fGrammarDescription.fContextType = 3;
/*  337 */         this.fGrammarDescription.setNamespace(SchemaSymbols.URI_XSI);
/*      */ 
/*  340 */         this.fGlobalAttrGrpDecls = new SymbolHash(1);
/*  341 */         this.fGlobalElemDecls = new SymbolHash(1);
/*  342 */         this.fGlobalGroupDecls = new SymbolHash(1);
/*  343 */         this.fGlobalNotationDecls = new SymbolHash(1);
/*  344 */         this.fGlobalIDConstraintDecls = new SymbolHash(1);
/*  345 */         this.fGlobalTypeDecls = new SymbolHash(1);
/*      */ 
/*  348 */         this.fGlobalAttrDeclsExt = new SymbolHash(1);
/*  349 */         this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
/*  350 */         this.fGlobalElemDeclsExt = new SymbolHash(1);
/*  351 */         this.fGlobalGroupDeclsExt = new SymbolHash(1);
/*  352 */         this.fGlobalNotationDeclsExt = new SymbolHash(1);
/*  353 */         this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
/*  354 */         this.fGlobalTypeDeclsExt = new SymbolHash(1);
/*      */ 
/*  357 */         this.fAllGlobalElemDecls = new SymbolHash(1);
/*      */ 
/*  360 */         this.fGlobalAttrDecls = new SymbolHash(8);
/*  361 */         String name = null;
/*  362 */         String tns = null;
/*  363 */         XSSimpleType type = null;
/*  364 */         short scope = 1;
/*      */ 
/*  367 */         name = SchemaSymbols.XSI_TYPE;
/*  368 */         tns = SchemaSymbols.URI_XSI;
/*  369 */         type = schemaFactory.getBuiltInType("QName");
/*  370 */         this.fGlobalAttrDecls.put(name, new SchemaGrammar.BuiltinAttrDecl(name, tns, type, scope));
/*      */ 
/*  373 */         name = SchemaSymbols.XSI_NIL;
/*  374 */         tns = SchemaSymbols.URI_XSI;
/*  375 */         type = schemaFactory.getBuiltInType("boolean");
/*  376 */         this.fGlobalAttrDecls.put(name, new SchemaGrammar.BuiltinAttrDecl(name, tns, type, scope));
/*      */ 
/*  378 */         XSSimpleType anyURI = schemaFactory.getBuiltInType("anyURI");
/*      */ 
/*  381 */         name = SchemaSymbols.XSI_SCHEMALOCATION;
/*  382 */         tns = SchemaSymbols.URI_XSI;
/*  383 */         type = schemaFactory.createTypeList("#AnonType_schemaLocation", SchemaSymbols.URI_XSI, (short)0, anyURI, null);
/*  384 */         if ((type instanceof XSSimpleTypeDecl)) {
/*  385 */           ((XSSimpleTypeDecl)type).setAnonymous(true);
/*      */         }
/*  387 */         this.fGlobalAttrDecls.put(name, new SchemaGrammar.BuiltinAttrDecl(name, tns, type, scope));
/*      */ 
/*  390 */         name = SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION;
/*  391 */         tns = SchemaSymbols.URI_XSI;
/*  392 */         type = anyURI;
/*  393 */         this.fGlobalAttrDecls.put(name, new SchemaGrammar.BuiltinAttrDecl(name, tns, type, scope));
/*      */       }
/*      */     }
/*      */ 
/*      */     public XMLGrammarDescription getGrammarDescription()
/*      */     {
/*  400 */       return this.fGrammarDescription.makeClone();
/*      */     }
/*      */ 
/*      */     public void setImportedGrammars(Vector importedGrammars)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeDecl(XSAttributeDecl decl)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeDecl(XSAttributeDecl decl, String location)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalElementDecl(XSElementDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalElementDecl(XSElementDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalElementDeclAll(XSElementDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalGroupDecl(XSGroupDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalGroupDecl(XSGroupDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalNotationDecl(XSNotationDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalNotationDecl(XSNotationDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalTypeDecl(XSTypeDefinition decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalTypeDecl(XSTypeDefinition decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalSimpleTypeDecl(XSSimpleType decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalSimpleTypeDecl(XSSimpleType decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addComplexTypeDecl(XSComplexTypeDecl decl, SimpleLocator locator) {
/*      */     }
/*      */ 
/*      */     public void addRedefinedGroupDecl(XSGroupDecl derived, XSGroupDecl base, SimpleLocator locator) {
/*      */     }
/*      */ 
/*      */     public synchronized void addDocument(Object document, String location) {
/*      */     }
/*      */ 
/*      */     synchronized DOMParser getDOMParser() {
/*  471 */       return null;
/*      */     }
/*      */     synchronized SAXParser getSAXParser() {
/*  474 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class Schema4Annotations extends SchemaGrammar
/*      */   {
/*  490 */     public static final Schema4Annotations INSTANCE = new Schema4Annotations();
/*      */ 
/*      */     private Schema4Annotations()
/*      */     {
/*  499 */       this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
/*      */ 
/*  502 */       this.fGrammarDescription = new XSDDescription();
/*  503 */       this.fGrammarDescription.fContextType = 3;
/*  504 */       this.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
/*      */ 
/*  508 */       this.fGlobalAttrDecls = new SymbolHash(1);
/*  509 */       this.fGlobalAttrGrpDecls = new SymbolHash(1);
/*  510 */       this.fGlobalElemDecls = new SymbolHash(6);
/*  511 */       this.fGlobalGroupDecls = new SymbolHash(1);
/*  512 */       this.fGlobalNotationDecls = new SymbolHash(1);
/*  513 */       this.fGlobalIDConstraintDecls = new SymbolHash(1);
/*      */ 
/*  516 */       this.fGlobalAttrDeclsExt = new SymbolHash(1);
/*  517 */       this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
/*  518 */       this.fGlobalElemDeclsExt = new SymbolHash(6);
/*  519 */       this.fGlobalGroupDeclsExt = new SymbolHash(1);
/*  520 */       this.fGlobalNotationDeclsExt = new SymbolHash(1);
/*  521 */       this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
/*  522 */       this.fGlobalTypeDeclsExt = new SymbolHash(1);
/*      */ 
/*  525 */       this.fAllGlobalElemDecls = new SymbolHash(6);
/*      */ 
/*  528 */       this.fGlobalTypeDecls = SG_SchemaNS.fGlobalTypeDecls;
/*      */ 
/*  531 */       XSElementDecl annotationDecl = createAnnotationElementDecl(SchemaSymbols.ELT_ANNOTATION);
/*  532 */       XSElementDecl documentationDecl = createAnnotationElementDecl(SchemaSymbols.ELT_DOCUMENTATION);
/*  533 */       XSElementDecl appinfoDecl = createAnnotationElementDecl(SchemaSymbols.ELT_APPINFO);
/*      */ 
/*  536 */       this.fGlobalElemDecls.put(annotationDecl.fName, annotationDecl);
/*  537 */       this.fGlobalElemDecls.put(documentationDecl.fName, documentationDecl);
/*  538 */       this.fGlobalElemDecls.put(appinfoDecl.fName, appinfoDecl);
/*      */ 
/*  540 */       this.fGlobalElemDeclsExt.put("," + annotationDecl.fName, annotationDecl);
/*  541 */       this.fGlobalElemDeclsExt.put("," + documentationDecl.fName, documentationDecl);
/*  542 */       this.fGlobalElemDeclsExt.put("," + appinfoDecl.fName, appinfoDecl);
/*      */ 
/*  544 */       this.fAllGlobalElemDecls.put(annotationDecl, annotationDecl);
/*  545 */       this.fAllGlobalElemDecls.put(documentationDecl, documentationDecl);
/*  546 */       this.fAllGlobalElemDecls.put(appinfoDecl, appinfoDecl);
/*      */ 
/*  549 */       XSComplexTypeDecl annotationType = new XSComplexTypeDecl();
/*  550 */       XSComplexTypeDecl documentationType = new XSComplexTypeDecl();
/*  551 */       XSComplexTypeDecl appinfoType = new XSComplexTypeDecl();
/*      */ 
/*  554 */       annotationDecl.fType = annotationType;
/*  555 */       documentationDecl.fType = documentationType;
/*  556 */       appinfoDecl.fType = appinfoType;
/*      */ 
/*  559 */       XSAttributeGroupDecl annotationAttrs = new XSAttributeGroupDecl();
/*  560 */       XSAttributeGroupDecl documentationAttrs = new XSAttributeGroupDecl();
/*  561 */       XSAttributeGroupDecl appinfoAttrs = new XSAttributeGroupDecl();
/*      */ 
/*  566 */       XSAttributeUseImpl annotationIDAttr = new XSAttributeUseImpl();
/*  567 */       annotationIDAttr.fAttrDecl = new XSAttributeDecl();
/*  568 */       annotationIDAttr.fAttrDecl.setValues(SchemaSymbols.ATT_ID, null, (XSSimpleType)this.fGlobalTypeDecls.get("ID"), (short)0, (short)2, null, annotationType, null);
/*      */ 
/*  570 */       annotationIDAttr.fUse = 0;
/*  571 */       annotationIDAttr.fConstraintType = 0;
/*      */ 
/*  573 */       XSAttributeUseImpl documentationSourceAttr = new XSAttributeUseImpl();
/*  574 */       documentationSourceAttr.fAttrDecl = new XSAttributeDecl();
/*  575 */       documentationSourceAttr.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, null, (XSSimpleType)this.fGlobalTypeDecls.get("anyURI"), (short)0, (short)2, null, documentationType, null);
/*      */ 
/*  577 */       documentationSourceAttr.fUse = 0;
/*  578 */       documentationSourceAttr.fConstraintType = 0;
/*      */ 
/*  580 */       XSAttributeUseImpl documentationLangAttr = new XSAttributeUseImpl();
/*  581 */       documentationLangAttr.fAttrDecl = new XSAttributeDecl();
/*  582 */       documentationLangAttr.fAttrDecl.setValues("lang".intern(), NamespaceContext.XML_URI, (XSSimpleType)this.fGlobalTypeDecls.get("language"), (short)0, (short)2, null, documentationType, null);
/*      */ 
/*  584 */       documentationLangAttr.fUse = 0;
/*  585 */       documentationLangAttr.fConstraintType = 0;
/*      */ 
/*  587 */       XSAttributeUseImpl appinfoSourceAttr = new XSAttributeUseImpl();
/*  588 */       appinfoSourceAttr.fAttrDecl = new XSAttributeDecl();
/*  589 */       appinfoSourceAttr.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, null, (XSSimpleType)this.fGlobalTypeDecls.get("anyURI"), (short)0, (short)2, null, appinfoType, null);
/*      */ 
/*  591 */       appinfoSourceAttr.fUse = 0;
/*  592 */       appinfoSourceAttr.fConstraintType = 0;
/*      */ 
/*  595 */       XSWildcardDecl otherAttrs = new XSWildcardDecl();
/*  596 */       otherAttrs.fNamespaceList = new String[] { this.fTargetNamespace, null };
/*  597 */       otherAttrs.fType = 2;
/*  598 */       otherAttrs.fProcessContents = 3;
/*      */ 
/*  601 */       annotationAttrs.addAttributeUse(annotationIDAttr);
/*  602 */       annotationAttrs.fAttributeWC = otherAttrs;
/*      */ 
/*  604 */       documentationAttrs.addAttributeUse(documentationSourceAttr);
/*  605 */       documentationAttrs.addAttributeUse(documentationLangAttr);
/*  606 */       documentationAttrs.fAttributeWC = otherAttrs;
/*      */ 
/*  608 */       appinfoAttrs.addAttributeUse(appinfoSourceAttr);
/*  609 */       appinfoAttrs.fAttributeWC = otherAttrs;
/*      */ 
/*  613 */       XSParticleDecl annotationParticle = createUnboundedModelGroupParticle();
/*      */ 
/*  615 */       XSModelGroupImpl annotationChoice = new XSModelGroupImpl();
/*  616 */       annotationChoice.fCompositor = 101;
/*  617 */       annotationChoice.fParticleCount = 2;
/*  618 */       annotationChoice.fParticles = new XSParticleDecl[2];
/*  619 */       annotationChoice.fParticles[0] = createChoiceElementParticle(appinfoDecl);
/*  620 */       annotationChoice.fParticles[1] = createChoiceElementParticle(documentationDecl);
/*  621 */       annotationParticle.fValue = annotationChoice;
/*      */ 
/*  625 */       XSParticleDecl anyWCSequenceParticle = createUnboundedAnyWildcardSequenceParticle();
/*      */ 
/*  628 */       annotationType.setValues("#AnonType_" + SchemaSymbols.ELT_ANNOTATION, this.fTargetNamespace, SchemaGrammar.fAnyType, (short)2, (short)0, (short)3, (short)2, false, annotationAttrs, null, annotationParticle, new XSObjectListImpl(null, 0));
/*      */ 
/*  631 */       annotationType.setName("#AnonType_" + SchemaSymbols.ELT_ANNOTATION);
/*  632 */       annotationType.setIsAnonymous();
/*      */ 
/*  634 */       documentationType.setValues("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION, this.fTargetNamespace, SchemaGrammar.fAnyType, (short)2, (short)0, (short)3, (short)3, false, documentationAttrs, null, anyWCSequenceParticle, new XSObjectListImpl(null, 0));
/*      */ 
/*  637 */       documentationType.setName("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION);
/*  638 */       documentationType.setIsAnonymous();
/*      */ 
/*  640 */       appinfoType.setValues("#AnonType_" + SchemaSymbols.ELT_APPINFO, this.fTargetNamespace, SchemaGrammar.fAnyType, (short)2, (short)0, (short)3, (short)3, false, appinfoAttrs, null, anyWCSequenceParticle, new XSObjectListImpl(null, 0));
/*      */ 
/*  643 */       appinfoType.setName("#AnonType_" + SchemaSymbols.ELT_APPINFO);
/*  644 */       appinfoType.setIsAnonymous();
/*      */     }
/*      */ 
/*      */     public XMLGrammarDescription getGrammarDescription()
/*      */     {
/*  651 */       return this.fGrammarDescription.makeClone();
/*      */     }
/*      */ 
/*      */     public void setImportedGrammars(Vector importedGrammars)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeDecl(XSAttributeDecl decl)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeDecl(XSAttributeGroupDecl decl, String location)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalElementDecl(XSElementDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalElementDecl(XSElementDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalElementDeclAll(XSElementDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalGroupDecl(XSGroupDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalGroupDecl(XSGroupDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalNotationDecl(XSNotationDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalNotationDecl(XSNotationDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalTypeDecl(XSTypeDefinition decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalTypeDecl(XSTypeDefinition decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addGlobalSimpleTypeDecl(XSSimpleType decl) {
/*      */     }
/*      */ 
/*      */     public void addGlobalSimpleTypeDecl(XSSimpleType decl, String location) {
/*      */     }
/*      */ 
/*      */     public void addComplexTypeDecl(XSComplexTypeDecl decl, SimpleLocator locator) {
/*      */     }
/*      */ 
/*      */     public void addRedefinedGroupDecl(XSGroupDecl derived, XSGroupDecl base, SimpleLocator locator) {
/*      */     }
/*      */ 
/*      */     public synchronized void addDocument(Object document, String location) {
/*      */     }
/*      */ 
/*      */     synchronized DOMParser getDOMParser() {
/*  722 */       return null;
/*      */     }
/*      */     synchronized SAXParser getSAXParser() {
/*  725 */       return null;
/*      */     }
/*      */ 
/*      */     private XSElementDecl createAnnotationElementDecl(String localName)
/*      */     {
/*  733 */       XSElementDecl eDecl = new XSElementDecl();
/*  734 */       eDecl.fName = localName;
/*  735 */       eDecl.fTargetNamespace = this.fTargetNamespace;
/*  736 */       eDecl.setIsGlobal();
/*  737 */       eDecl.fBlock = 7;
/*      */ 
/*  739 */       eDecl.setConstraintType((short)0);
/*  740 */       return eDecl;
/*      */     }
/*      */ 
/*      */     private XSParticleDecl createUnboundedModelGroupParticle() {
/*  744 */       XSParticleDecl particle = new XSParticleDecl();
/*  745 */       particle.fMinOccurs = 0;
/*  746 */       particle.fMaxOccurs = -1;
/*  747 */       particle.fType = 3;
/*  748 */       return particle;
/*      */     }
/*      */ 
/*      */     private XSParticleDecl createChoiceElementParticle(XSElementDecl ref) {
/*  752 */       XSParticleDecl particle = new XSParticleDecl();
/*  753 */       particle.fMinOccurs = 1;
/*  754 */       particle.fMaxOccurs = 1;
/*  755 */       particle.fType = 1;
/*  756 */       particle.fValue = ref;
/*  757 */       return particle;
/*      */     }
/*      */ 
/*      */     private XSParticleDecl createUnboundedAnyWildcardSequenceParticle() {
/*  761 */       XSParticleDecl particle = createUnboundedModelGroupParticle();
/*  762 */       XSModelGroupImpl sequence = new XSModelGroupImpl();
/*  763 */       sequence.fCompositor = 102;
/*  764 */       sequence.fParticleCount = 1;
/*  765 */       sequence.fParticles = new XSParticleDecl[1];
/*  766 */       sequence.fParticles[0] = createAnyLaxWildcardParticle();
/*  767 */       particle.fValue = sequence;
/*  768 */       return particle;
/*      */     }
/*      */ 
/*      */     private XSParticleDecl createAnyLaxWildcardParticle() {
/*  772 */       XSParticleDecl particle = new XSParticleDecl();
/*  773 */       particle.fMinOccurs = 1;
/*  774 */       particle.fMaxOccurs = 1;
/*  775 */       particle.fType = 2;
/*      */ 
/*  777 */       XSWildcardDecl anyWC = new XSWildcardDecl();
/*  778 */       anyWC.fNamespaceList = null;
/*  779 */       anyWC.fType = 1;
/*  780 */       anyWC.fProcessContents = 3;
/*      */ 
/*  782 */       particle.fValue = anyWC;
/*  783 */       return particle;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class XSAnyType extends XSComplexTypeDecl
/*      */   {
/*      */     public XSAnyType()
/*      */     {
/* 1176 */       this.fName = "anyType";
/* 1177 */       this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
/* 1178 */       this.fBaseType = this;
/* 1179 */       this.fDerivedBy = 2;
/* 1180 */       this.fContentType = 3;
/*      */ 
/* 1182 */       this.fParticle = null;
/* 1183 */       this.fAttrGrp = null;
/*      */     }
/*      */ 
/*      */     public void setValues(String name, String targetNamespace, XSTypeDefinition baseType, short derivedBy, short schemaFinal, short block, short contentType, boolean isAbstract, XSAttributeGroupDecl attrGrp, XSSimpleType simpleType, XSParticleDecl particle)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setName(String name)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setIsAbstractType()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setContainsTypeID()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void setIsAnonymous()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void reset()
/*      */     {
/*      */     }
/*      */ 
/*      */     public XSObjectList getAttributeUses()
/*      */     {
/* 1216 */       return XSObjectListImpl.EMPTY_LIST;
/*      */     }
/*      */ 
/*      */     public XSAttributeGroupDecl getAttrGrp() {
/* 1220 */       XSWildcardDecl wildcard = new XSWildcardDecl();
/* 1221 */       wildcard.fProcessContents = 3;
/* 1222 */       XSAttributeGroupDecl attrGrp = new XSAttributeGroupDecl();
/* 1223 */       attrGrp.fAttributeWC = wildcard;
/* 1224 */       return attrGrp;
/*      */     }
/*      */ 
/*      */     public XSWildcard getAttributeWildcard() {
/* 1228 */       XSWildcardDecl wildcard = new XSWildcardDecl();
/* 1229 */       wildcard.fProcessContents = 3;
/* 1230 */       return wildcard;
/*      */     }
/*      */ 
/*      */     public XSParticle getParticle()
/*      */     {
/* 1236 */       XSWildcardDecl wildcard = new XSWildcardDecl();
/* 1237 */       wildcard.fProcessContents = 3;
/*      */ 
/* 1239 */       XSParticleDecl particleW = new XSParticleDecl();
/* 1240 */       particleW.fMinOccurs = 0;
/* 1241 */       particleW.fMaxOccurs = -1;
/* 1242 */       particleW.fType = 2;
/* 1243 */       particleW.fValue = wildcard;
/*      */ 
/* 1245 */       XSModelGroupImpl group = new XSModelGroupImpl();
/* 1246 */       group.fCompositor = 102;
/* 1247 */       group.fParticleCount = 1;
/* 1248 */       group.fParticles = new XSParticleDecl[1];
/* 1249 */       group.fParticles[0] = particleW;
/*      */ 
/* 1251 */       XSParticleDecl particleG = new XSParticleDecl();
/* 1252 */       particleG.fType = 3;
/* 1253 */       particleG.fValue = group;
/*      */ 
/* 1255 */       return particleG;
/*      */     }
/*      */ 
/*      */     public XSObjectList getAnnotations() {
/* 1259 */       return XSObjectListImpl.EMPTY_LIST;
/*      */     }
/*      */ 
/*      */     public XSNamespaceItem getNamespaceItem() {
/* 1263 */       return SchemaGrammar.SG_SchemaNS;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
 * JD-Core Version:    0.6.2
 */
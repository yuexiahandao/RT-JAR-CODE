/*      */ package com.sun.org.apache.xerces.internal.impl.xs;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*      */ import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.CMBuilder;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.models.XSCMValidator;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
/*      */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
/*      */ import com.sun.org.apache.xerces.internal.util.SymbolHash;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*      */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class XSConstraints
/*      */ {
/*      */   static final int OCCURRENCE_UNKNOWN = -2;
/*   58 */   static final XSSimpleType STRING_TYPE = (XSSimpleType)SchemaGrammar.SG_SchemaNS.getGlobalTypeDecl("string");
/*      */ 
/*   60 */   private static XSParticleDecl fEmptyParticle = null;
/*      */ 
/*   77 */   private static final Comparator ELEMENT_PARTICLE_COMPARATOR = new Comparator()
/*      */   {
/*      */     public int compare(Object o1, Object o2) {
/*   80 */       XSParticleDecl pDecl1 = (XSParticleDecl)o1;
/*   81 */       XSParticleDecl pDecl2 = (XSParticleDecl)o2;
/*   82 */       XSElementDecl decl1 = (XSElementDecl)pDecl1.fValue;
/*   83 */       XSElementDecl decl2 = (XSElementDecl)pDecl2.fValue;
/*      */ 
/*   85 */       String namespace1 = decl1.getNamespace();
/*   86 */       String namespace2 = decl2.getNamespace();
/*   87 */       String name1 = decl1.getName();
/*   88 */       String name2 = decl2.getName();
/*      */ 
/*   90 */       boolean sameNamespace = namespace1 == namespace2;
/*   91 */       int namespaceComparison = 0;
/*      */ 
/*   93 */       if (!sameNamespace) {
/*   94 */         if (namespace1 != null) {
/*   95 */           if (namespace2 != null) {
/*   96 */             namespaceComparison = namespace1.compareTo(namespace2);
/*      */           }
/*      */           else {
/*   99 */             namespaceComparison = 1;
/*      */           }
/*      */         }
/*      */         else {
/*  103 */           namespaceComparison = -1;
/*      */         }
/*      */       }
/*      */ 
/*  107 */       return namespaceComparison != 0 ? namespaceComparison : name1.compareTo(name2);
/*      */     }
/*   77 */   };
/*      */ 
/*      */   public static XSParticleDecl getEmptySequence()
/*      */   {
/*   62 */     if (fEmptyParticle == null) {
/*   63 */       XSModelGroupImpl group = new XSModelGroupImpl();
/*   64 */       group.fCompositor = 102;
/*   65 */       group.fParticleCount = 0;
/*   66 */       group.fParticles = null;
/*   67 */       group.fAnnotations = XSObjectListImpl.EMPTY_LIST;
/*   68 */       XSParticleDecl particle = new XSParticleDecl();
/*   69 */       particle.fType = 3;
/*   70 */       particle.fValue = group;
/*   71 */       particle.fAnnotations = XSObjectListImpl.EMPTY_LIST;
/*   72 */       fEmptyParticle = particle;
/*      */     }
/*   74 */     return fEmptyParticle;
/*      */   }
/*      */ 
/*      */   public static boolean checkTypeDerivationOk(XSTypeDefinition derived, XSTypeDefinition base, short block)
/*      */   {
/*  117 */     if (derived == SchemaGrammar.fAnyType) {
/*  118 */       return derived == base;
/*      */     }
/*      */ 
/*  121 */     if (derived == SchemaGrammar.fAnySimpleType) {
/*  122 */       return (base == SchemaGrammar.fAnyType) || (base == SchemaGrammar.fAnySimpleType);
/*      */     }
/*      */ 
/*  127 */     if (derived.getTypeCategory() == 16)
/*      */     {
/*  129 */       if (base.getTypeCategory() == 15)
/*      */       {
/*  132 */         if (base == SchemaGrammar.fAnyType)
/*  133 */           base = SchemaGrammar.fAnySimpleType;
/*      */         else
/*  135 */           return false;
/*      */       }
/*  137 */       return checkSimpleDerivation((XSSimpleType)derived, (XSSimpleType)base, block);
/*      */     }
/*      */ 
/*  141 */     return checkComplexDerivation((XSComplexTypeDecl)derived, base, block);
/*      */   }
/*      */ 
/*      */   public static boolean checkSimpleDerivationOk(XSSimpleType derived, XSTypeDefinition base, short block)
/*      */   {
/*  152 */     if (derived == SchemaGrammar.fAnySimpleType) {
/*  153 */       return (base == SchemaGrammar.fAnyType) || (base == SchemaGrammar.fAnySimpleType);
/*      */     }
/*      */ 
/*  158 */     if (base.getTypeCategory() == 15)
/*      */     {
/*  161 */       if (base == SchemaGrammar.fAnyType)
/*  162 */         base = SchemaGrammar.fAnySimpleType;
/*      */       else
/*  164 */         return false;
/*      */     }
/*  166 */     return checkSimpleDerivation(derived, (XSSimpleType)base, block);
/*      */   }
/*      */ 
/*      */   public static boolean checkComplexDerivationOk(XSComplexTypeDecl derived, XSTypeDefinition base, short block)
/*      */   {
/*  176 */     if (derived == SchemaGrammar.fAnyType)
/*  177 */       return derived == base;
/*  178 */     return checkComplexDerivation(derived, base, block);
/*      */   }
/*      */ 
/*      */   private static boolean checkSimpleDerivation(XSSimpleType derived, XSSimpleType base, short block)
/*      */   {
/*  188 */     if (derived == base) {
/*  189 */       return true;
/*      */     }
/*      */ 
/*  193 */     if (((block & 0x2) != 0) || ((derived.getBaseType().getFinal() & 0x2) != 0))
/*      */     {
/*  195 */       return false;
/*      */     }
/*      */ 
/*  200 */     XSSimpleType directBase = (XSSimpleType)derived.getBaseType();
/*  201 */     if (directBase == base) {
/*  202 */       return true;
/*      */     }
/*      */ 
/*  205 */     if ((directBase != SchemaGrammar.fAnySimpleType) && (checkSimpleDerivation(directBase, base, block)))
/*      */     {
/*  207 */       return true;
/*      */     }
/*      */ 
/*  211 */     if (((derived.getVariety() == 2) || (derived.getVariety() == 3)) && (base == SchemaGrammar.fAnySimpleType))
/*      */     {
/*  214 */       return true;
/*      */     }
/*      */ 
/*  218 */     if (base.getVariety() == 3) {
/*  219 */       XSObjectList subUnionMemberDV = base.getMemberTypes();
/*  220 */       int subUnionSize = subUnionMemberDV.getLength();
/*  221 */       for (int i = 0; i < subUnionSize; i++) {
/*  222 */         base = (XSSimpleType)subUnionMemberDV.item(i);
/*  223 */         if (checkSimpleDerivation(derived, base, block)) {
/*  224 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*  228 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean checkComplexDerivation(XSComplexTypeDecl derived, XSTypeDefinition base, short block)
/*      */   {
/*  238 */     if (derived == base) {
/*  239 */       return true;
/*      */     }
/*      */ 
/*  242 */     if ((derived.fDerivedBy & block) != 0) {
/*  243 */       return false;
/*      */     }
/*      */ 
/*  246 */     XSTypeDefinition directBase = derived.fBaseType;
/*      */ 
/*  248 */     if (directBase == base) {
/*  249 */       return true;
/*      */     }
/*      */ 
/*  253 */     if ((directBase == SchemaGrammar.fAnyType) || (directBase == SchemaGrammar.fAnySimpleType))
/*      */     {
/*  255 */       return false;
/*      */     }
/*      */ 
/*  260 */     if (directBase.getTypeCategory() == 15) {
/*  261 */       return checkComplexDerivation((XSComplexTypeDecl)directBase, base, block);
/*      */     }
/*      */ 
/*  264 */     if (directBase.getTypeCategory() == 16)
/*      */     {
/*  266 */       if (base.getTypeCategory() == 15)
/*      */       {
/*  269 */         if (base == SchemaGrammar.fAnyType)
/*  270 */           base = SchemaGrammar.fAnySimpleType;
/*      */         else
/*  272 */           return false;
/*      */       }
/*  274 */       return checkSimpleDerivation((XSSimpleType)directBase, (XSSimpleType)base, block);
/*      */     }
/*      */ 
/*  278 */     return false;
/*      */   }
/*      */ 
/*      */   public static Object ElementDefaultValidImmediate(XSTypeDefinition type, String value, ValidationContext context, ValidatedInfo vinfo)
/*      */   {
/*  288 */     XSSimpleType dv = null;
/*      */ 
/*  293 */     if (type.getTypeCategory() == 16) {
/*  294 */       dv = (XSSimpleType)type;
/*      */     }
/*      */     else
/*      */     {
/*  300 */       XSComplexTypeDecl ctype = (XSComplexTypeDecl)type;
/*      */ 
/*  303 */       if (ctype.fContentType == 1) {
/*  304 */         dv = ctype.fXSSimpleType;
/*      */       }
/*  307 */       else if (ctype.fContentType == 3) {
/*  308 */         if (!((XSParticleDecl)ctype.getParticle()).emptiable())
/*  309 */           return null;
/*      */       }
/*      */       else {
/*  312 */         return null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  317 */     Object actualValue = null;
/*  318 */     if (dv == null)
/*      */     {
/*  322 */       dv = STRING_TYPE;
/*      */     }
/*      */     try
/*      */     {
/*  326 */       actualValue = dv.validate(value, context, vinfo);
/*      */ 
/*  328 */       if (vinfo != null)
/*  329 */         actualValue = dv.validate(vinfo.stringValue(), context, vinfo);
/*      */     } catch (InvalidDatatypeValueException ide) {
/*  331 */       return null;
/*      */     }
/*      */ 
/*  334 */     return actualValue;
/*      */   }
/*      */ 
/*      */   static void reportSchemaError(XMLErrorReporter errorReporter, SimpleLocator loc, String key, Object[] args)
/*      */   {
/*  340 */     if (loc != null) {
/*  341 */       errorReporter.reportError(loc, "http://www.w3.org/TR/xml-schema-1", key, args, (short)1);
/*      */     }
/*      */     else
/*      */     {
/*  345 */       errorReporter.reportError("http://www.w3.org/TR/xml-schema-1", key, args, (short)1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void fullSchemaChecking(XSGrammarBucket grammarBucket, SubstitutionGroupHandler SGHandler, CMBuilder cmBuilder, XMLErrorReporter errorReporter)
/*      */   {
/*  362 */     SchemaGrammar[] grammars = grammarBucket.getGrammars();
/*  363 */     for (int i = grammars.length - 1; i >= 0; i--) {
/*  364 */       SGHandler.addSubstitutionGroup(grammars[i].getSubstitutionGroups());
/*      */     }
/*      */ 
/*  367 */     XSParticleDecl fakeDerived = new XSParticleDecl();
/*  368 */     XSParticleDecl fakeBase = new XSParticleDecl();
/*  369 */     fakeDerived.fType = 3;
/*  370 */     fakeBase.fType = 3;
/*      */     XSGroupDecl[] redefinedGroups;
/*      */     SimpleLocator[] rgLocators;
/*      */     int i;
/*  373 */     for (int g = grammars.length - 1; g >= 0; g--) {
/*  374 */       redefinedGroups = grammars[g].getRedefinedGroupDecls();
/*  375 */       rgLocators = grammars[g].getRGLocators();
/*  376 */       for (i = 0; i < redefinedGroups.length; ) {
/*  377 */         XSGroupDecl derivedGrp = redefinedGroups[(i++)];
/*  378 */         XSModelGroupImpl derivedMG = derivedGrp.fModelGroup;
/*  379 */         XSGroupDecl baseGrp = redefinedGroups[(i++)];
/*  380 */         XSModelGroupImpl baseMG = baseGrp.fModelGroup;
/*  381 */         fakeDerived.fValue = derivedMG;
/*  382 */         fakeBase.fValue = baseMG;
/*  383 */         if (baseMG == null) {
/*  384 */           if (derivedMG != null) {
/*  385 */             reportSchemaError(errorReporter, rgLocators[(i / 2 - 1)], "src-redefine.6.2.2", new Object[] { derivedGrp.fName, "rcase-Recurse.2" });
/*      */           }
/*      */ 
/*      */         }
/*  389 */         else if (derivedMG == null) {
/*  390 */           if (!fakeBase.emptiable()) {
/*  391 */             reportSchemaError(errorReporter, rgLocators[(i / 2 - 1)], "src-redefine.6.2.2", new Object[] { derivedGrp.fName, "rcase-Recurse.2" });
/*      */           }
/*      */         }
/*      */         else {
/*      */           try
/*      */           {
/*  397 */             particleValidRestriction(fakeDerived, SGHandler, fakeBase, SGHandler);
/*      */           } catch (XMLSchemaException e) {
/*  399 */             String key = e.getKey();
/*  400 */             reportSchemaError(errorReporter, rgLocators[(i / 2 - 1)], key, e.getArgs());
/*      */ 
/*  403 */             reportSchemaError(errorReporter, rgLocators[(i / 2 - 1)], "src-redefine.6.2.2", new Object[] { derivedGrp.fName, key });
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  425 */     SymbolHash elemTable = new SymbolHash();
/*  426 */     for (int i = grammars.length - 1; i >= 0; i--)
/*      */     {
/*  428 */       int keepType = 0;
/*  429 */       boolean fullChecked = grammars[i].fFullChecked;
/*  430 */       XSComplexTypeDecl[] types = grammars[i].getUncheckedComplexTypeDecls();
/*  431 */       SimpleLocator[] ctLocators = grammars[i].getUncheckedCTLocators();
/*      */ 
/*  433 */       for (int j = 0; j < types.length; j++)
/*      */       {
/*  436 */         if (!fullChecked)
/*      */         {
/*  438 */           if (types[j].fParticle != null) {
/*  439 */             elemTable.clear();
/*      */             try {
/*  441 */               checkElementDeclsConsistent(types[j], types[j].fParticle, elemTable, SGHandler);
/*      */             }
/*      */             catch (XMLSchemaException e)
/*      */             {
/*  445 */               reportSchemaError(errorReporter, ctLocators[j], e.getKey(), e.getArgs());
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  454 */         if ((types[j].fBaseType != null) && (types[j].fBaseType != SchemaGrammar.fAnyType) && (types[j].fDerivedBy == 2) && ((types[j].fBaseType instanceof XSComplexTypeDecl)))
/*      */         {
/*  459 */           XSParticleDecl derivedParticle = types[j].fParticle;
/*  460 */           XSParticleDecl baseParticle = ((XSComplexTypeDecl)types[j].fBaseType).fParticle;
/*      */ 
/*  462 */           if (derivedParticle == null) {
/*  463 */             if ((baseParticle != null) && (!baseParticle.emptiable())) {
/*  464 */               reportSchemaError(errorReporter, ctLocators[j], "derivation-ok-restriction.5.3.2", new Object[] { types[j].fName, types[j].fBaseType.getName() });
/*      */             }
/*      */ 
/*      */           }
/*  469 */           else if (baseParticle != null) {
/*      */             try {
/*  471 */               particleValidRestriction(types[j].fParticle, SGHandler, ((XSComplexTypeDecl)types[j].fBaseType).fParticle, SGHandler);
/*      */             }
/*      */             catch (XMLSchemaException e)
/*      */             {
/*  476 */               reportSchemaError(errorReporter, ctLocators[j], e.getKey(), e.getArgs());
/*      */ 
/*  479 */               reportSchemaError(errorReporter, ctLocators[j], "derivation-ok-restriction.5.4.2", new Object[] { types[j].fName });
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*  485 */             reportSchemaError(errorReporter, ctLocators[j], "derivation-ok-restriction.5.4.2", new Object[] { types[j].fName });
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  492 */         XSCMValidator cm = types[j].getContentModel(cmBuilder);
/*  493 */         boolean further = false;
/*  494 */         if (cm != null) {
/*      */           try {
/*  496 */             further = cm.checkUniqueParticleAttribution(SGHandler);
/*      */           } catch (XMLSchemaException e) {
/*  498 */             reportSchemaError(errorReporter, ctLocators[j], e.getKey(), e.getArgs());
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  513 */         if ((!fullChecked) && (further)) {
/*  514 */           types[(keepType++)] = types[j];
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  524 */       if (!fullChecked) {
/*  525 */         grammars[i].setUncheckedTypeNum(keepType);
/*  526 */         grammars[i].fFullChecked = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void checkElementDeclsConsistent(XSComplexTypeDecl type, XSParticleDecl particle, SymbolHash elemDeclHash, SubstitutionGroupHandler sgHandler)
/*      */     throws XMLSchemaException
/*      */   {
/*  543 */     int pType = particle.fType;
/*      */ 
/*  545 */     if (pType == 2) {
/*  546 */       return;
/*      */     }
/*  548 */     if (pType == 1) {
/*  549 */       XSElementDecl elem = (XSElementDecl)particle.fValue;
/*  550 */       findElemInTable(type, elem, elemDeclHash);
/*      */ 
/*  552 */       if (elem.fScope == 1)
/*      */       {
/*  554 */         XSElementDecl[] subGroup = sgHandler.getSubstitutionGroup(elem);
/*  555 */         for (int i = 0; i < subGroup.length; i++) {
/*  556 */           findElemInTable(type, subGroup[i], elemDeclHash);
/*      */         }
/*      */       }
/*  559 */       return;
/*      */     }
/*      */ 
/*  562 */     XSModelGroupImpl group = (XSModelGroupImpl)particle.fValue;
/*  563 */     for (int i = 0; i < group.fParticleCount; i++)
/*  564 */       checkElementDeclsConsistent(type, group.fParticles[i], elemDeclHash, sgHandler);
/*      */   }
/*      */ 
/*      */   public static void findElemInTable(XSComplexTypeDecl type, XSElementDecl elem, SymbolHash elemDeclHash)
/*      */     throws XMLSchemaException
/*      */   {
/*  572 */     String name = elem.fName + "," + elem.fTargetNamespace;
/*      */ 
/*  574 */     XSElementDecl existingElem = null;
/*  575 */     if ((existingElem = (XSElementDecl)elemDeclHash.get(name)) == null)
/*      */     {
/*  577 */       elemDeclHash.put(name, elem);
/*      */     }
/*      */     else
/*      */     {
/*  581 */       if (elem == existingElem) {
/*  582 */         return;
/*      */       }
/*  584 */       if (elem.fType != existingElem.fType)
/*      */       {
/*  586 */         throw new XMLSchemaException("cos-element-consistent", new Object[] { type.fName, elem.fName });
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean particleValidRestriction(XSParticleDecl dParticle, SubstitutionGroupHandler dSGHandler, XSParticleDecl bParticle, SubstitutionGroupHandler bSGHandler)
/*      */     throws XMLSchemaException
/*      */   {
/*  605 */     return particleValidRestriction(dParticle, dSGHandler, bParticle, bSGHandler, true);
/*      */   }
/*      */ 
/*      */   private static boolean particleValidRestriction(XSParticleDecl dParticle, SubstitutionGroupHandler dSGHandler, XSParticleDecl bParticle, SubstitutionGroupHandler bSGHandler, boolean checkWCOccurrence)
/*      */     throws XMLSchemaException
/*      */   {
/*  615 */     Vector dChildren = null;
/*  616 */     Vector bChildren = null;
/*  617 */     int dMinEffectiveTotalRange = -2;
/*  618 */     int dMaxEffectiveTotalRange = -2;
/*      */ 
/*  621 */     boolean bExpansionHappened = false;
/*      */ 
/*  625 */     if ((dParticle.isEmpty()) && (!bParticle.emptiable())) {
/*  626 */       throw new XMLSchemaException("cos-particle-restrict.a", null);
/*      */     }
/*  628 */     if ((!dParticle.isEmpty()) && (bParticle.isEmpty())) {
/*  629 */       throw new XMLSchemaException("cos-particle-restrict.b", null);
/*      */     }
/*      */ 
/*  640 */     short dType = dParticle.fType;
/*      */ 
/*  644 */     if (dType == 3) {
/*  645 */       dType = ((XSModelGroupImpl)dParticle.fValue).fCompositor;
/*      */ 
/*  650 */       XSParticleDecl dtmp = getNonUnaryGroup(dParticle);
/*  651 */       if (dtmp != dParticle)
/*      */       {
/*  653 */         dParticle = dtmp;
/*  654 */         dType = dParticle.fType;
/*  655 */         if (dType == 3) {
/*  656 */           dType = ((XSModelGroupImpl)dParticle.fValue).fCompositor;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  661 */       dChildren = removePointlessChildren(dParticle);
/*      */     }
/*      */ 
/*  664 */     int dMinOccurs = dParticle.fMinOccurs;
/*  665 */     int dMaxOccurs = dParticle.fMaxOccurs;
/*      */ 
/*  670 */     if ((dSGHandler != null) && (dType == 1)) {
/*  671 */       XSElementDecl dElement = (XSElementDecl)dParticle.fValue;
/*      */ 
/*  673 */       if (dElement.fScope == 1)
/*      */       {
/*  677 */         XSElementDecl[] subGroup = dSGHandler.getSubstitutionGroup(dElement);
/*  678 */         if (subGroup.length > 0)
/*      */         {
/*  681 */           dType = 101;
/*  682 */           dMinEffectiveTotalRange = dMinOccurs;
/*  683 */           dMaxEffectiveTotalRange = dMaxOccurs;
/*      */ 
/*  686 */           dChildren = new Vector(subGroup.length + 1);
/*  687 */           for (int i = 0; i < subGroup.length; i++) {
/*  688 */             addElementToParticleVector(dChildren, subGroup[i]);
/*      */           }
/*  690 */           addElementToParticleVector(dChildren, dElement);
/*  691 */           Collections.sort(dChildren, ELEMENT_PARTICLE_COMPARATOR);
/*      */ 
/*  695 */           dSGHandler = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  700 */     short bType = bParticle.fType;
/*      */ 
/*  704 */     if (bType == 3) {
/*  705 */       bType = ((XSModelGroupImpl)bParticle.fValue).fCompositor;
/*      */ 
/*  710 */       XSParticleDecl btmp = getNonUnaryGroup(bParticle);
/*  711 */       if (btmp != bParticle)
/*      */       {
/*  713 */         bParticle = btmp;
/*  714 */         bType = bParticle.fType;
/*  715 */         if (bType == 3) {
/*  716 */           bType = ((XSModelGroupImpl)bParticle.fValue).fCompositor;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  721 */       bChildren = removePointlessChildren(bParticle);
/*      */     }
/*      */ 
/*  724 */     int bMinOccurs = bParticle.fMinOccurs;
/*  725 */     int bMaxOccurs = bParticle.fMaxOccurs;
/*      */ 
/*  727 */     if ((bSGHandler != null) && (bType == 1)) {
/*  728 */       XSElementDecl bElement = (XSElementDecl)bParticle.fValue;
/*      */ 
/*  730 */       if (bElement.fScope == 1)
/*      */       {
/*  734 */         XSElementDecl[] bsubGroup = bSGHandler.getSubstitutionGroup(bElement);
/*  735 */         if (bsubGroup.length > 0)
/*      */         {
/*  737 */           bType = 101;
/*      */ 
/*  739 */           bChildren = new Vector(bsubGroup.length + 1);
/*  740 */           for (int i = 0; i < bsubGroup.length; i++) {
/*  741 */             addElementToParticleVector(bChildren, bsubGroup[i]);
/*      */           }
/*  743 */           addElementToParticleVector(bChildren, bElement);
/*  744 */           Collections.sort(bChildren, ELEMENT_PARTICLE_COMPARATOR);
/*      */ 
/*  747 */           bSGHandler = null;
/*      */ 
/*  750 */           bExpansionHappened = true;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  758 */     switch (dType)
/*      */     {
/*      */     case 1:
/*  761 */       switch (bType)
/*      */       {
/*      */       case 1:
/*  766 */         checkNameAndTypeOK((XSElementDecl)dParticle.fValue, dMinOccurs, dMaxOccurs, (XSElementDecl)bParticle.fValue, bMinOccurs, bMaxOccurs);
/*      */ 
/*  768 */         return bExpansionHappened;
/*      */       case 2:
/*  774 */         checkNSCompat((XSElementDecl)dParticle.fValue, dMinOccurs, dMaxOccurs, (XSWildcardDecl)bParticle.fValue, bMinOccurs, bMaxOccurs, checkWCOccurrence);
/*      */ 
/*  777 */         return bExpansionHappened;
/*      */       case 101:
/*  785 */         dChildren = new Vector();
/*  786 */         dChildren.addElement(dParticle);
/*      */ 
/*  788 */         checkRecurseLax(dChildren, 1, 1, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  790 */         return bExpansionHappened;
/*      */       case 102:
/*      */       case 103:
/*  797 */         dChildren = new Vector();
/*  798 */         dChildren.addElement(dParticle);
/*      */ 
/*  800 */         checkRecurse(dChildren, 1, 1, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  802 */         return bExpansionHappened;
/*      */       }
/*      */ 
/*  807 */       throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
/*      */     case 2:
/*  815 */       switch (bType)
/*      */       {
/*      */       case 2:
/*  820 */         checkNSSubset((XSWildcardDecl)dParticle.fValue, dMinOccurs, dMaxOccurs, (XSWildcardDecl)bParticle.fValue, bMinOccurs, bMaxOccurs);
/*      */ 
/*  822 */         return bExpansionHappened;
/*      */       case 1:
/*      */       case 101:
/*      */       case 102:
/*      */       case 103:
/*  830 */         throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "any:choice,sequence,all,elt" });
/*      */       }
/*      */ 
/*  836 */       throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
/*      */     case 103:
/*  844 */       switch (bType)
/*      */       {
/*      */       case 2:
/*  849 */         if (dMinEffectiveTotalRange == -2)
/*  850 */           dMinEffectiveTotalRange = dParticle.minEffectiveTotalRange();
/*  851 */         if (dMaxEffectiveTotalRange == -2) {
/*  852 */           dMaxEffectiveTotalRange = dParticle.maxEffectiveTotalRange();
/*      */         }
/*  854 */         checkNSRecurseCheckCardinality(dChildren, dMinEffectiveTotalRange, dMaxEffectiveTotalRange, dSGHandler, bParticle, bMinOccurs, bMaxOccurs, checkWCOccurrence);
/*      */ 
/*  860 */         return bExpansionHappened;
/*      */       case 103:
/*  865 */         checkRecurse(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  867 */         return bExpansionHappened;
/*      */       case 1:
/*      */       case 101:
/*      */       case 102:
/*  874 */         throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "all:choice,sequence,elt" });
/*      */       }
/*      */ 
/*  880 */       throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
/*      */     case 101:
/*  888 */       switch (bType)
/*      */       {
/*      */       case 2:
/*  893 */         if (dMinEffectiveTotalRange == -2)
/*  894 */           dMinEffectiveTotalRange = dParticle.minEffectiveTotalRange();
/*  895 */         if (dMaxEffectiveTotalRange == -2) {
/*  896 */           dMaxEffectiveTotalRange = dParticle.maxEffectiveTotalRange();
/*      */         }
/*  898 */         checkNSRecurseCheckCardinality(dChildren, dMinEffectiveTotalRange, dMaxEffectiveTotalRange, dSGHandler, bParticle, bMinOccurs, bMaxOccurs, checkWCOccurrence);
/*      */ 
/*  903 */         return bExpansionHappened;
/*      */       case 101:
/*  908 */         checkRecurseLax(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  910 */         return bExpansionHappened;
/*      */       case 1:
/*      */       case 102:
/*      */       case 103:
/*  917 */         throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "choice:all,sequence,elt" });
/*      */       }
/*      */ 
/*  923 */       throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
/*      */     case 102:
/*  932 */       switch (bType)
/*      */       {
/*      */       case 2:
/*  937 */         if (dMinEffectiveTotalRange == -2)
/*  938 */           dMinEffectiveTotalRange = dParticle.minEffectiveTotalRange();
/*  939 */         if (dMaxEffectiveTotalRange == -2) {
/*  940 */           dMaxEffectiveTotalRange = dParticle.maxEffectiveTotalRange();
/*      */         }
/*  942 */         checkNSRecurseCheckCardinality(dChildren, dMinEffectiveTotalRange, dMaxEffectiveTotalRange, dSGHandler, bParticle, bMinOccurs, bMaxOccurs, checkWCOccurrence);
/*      */ 
/*  947 */         return bExpansionHappened;
/*      */       case 103:
/*  952 */         checkRecurseUnordered(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  954 */         return bExpansionHappened;
/*      */       case 102:
/*  959 */         checkRecurse(dChildren, dMinOccurs, dMaxOccurs, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  961 */         return bExpansionHappened;
/*      */       case 101:
/*  966 */         int min1 = dMinOccurs * dChildren.size();
/*  967 */         int max1 = dMaxOccurs == -1 ? dMaxOccurs : dMaxOccurs * dChildren.size();
/*      */ 
/*  969 */         checkMapAndSum(dChildren, min1, max1, dSGHandler, bChildren, bMinOccurs, bMaxOccurs, bSGHandler);
/*      */ 
/*  971 */         return bExpansionHappened;
/*      */       case 1:
/*  976 */         throw new XMLSchemaException("cos-particle-restrict.2", new Object[] { "seq:elt" });
/*      */       }
/*      */ 
/*  982 */       throw new XMLSchemaException("Internal-Error", new Object[] { "in particleValidRestriction" });
/*      */     }
/*      */ 
/*  990 */     return bExpansionHappened;
/*      */   }
/*      */ 
/*      */   private static void addElementToParticleVector(Vector v, XSElementDecl d)
/*      */   {
/*  995 */     XSParticleDecl p = new XSParticleDecl();
/*  996 */     p.fValue = d;
/*  997 */     p.fType = 1;
/*  998 */     v.addElement(p);
/*      */   }
/*      */ 
/*      */   private static XSParticleDecl getNonUnaryGroup(XSParticleDecl p)
/*      */   {
/* 1004 */     if ((p.fType == 1) || (p.fType == 2))
/*      */     {
/* 1006 */       return p;
/*      */     }
/* 1008 */     if ((p.fMinOccurs == 1) && (p.fMaxOccurs == 1) && (p.fValue != null) && (((XSModelGroupImpl)p.fValue).fParticleCount == 1))
/*      */     {
/* 1010 */       return getNonUnaryGroup(((XSModelGroupImpl)p.fValue).fParticles[0]);
/*      */     }
/* 1012 */     return p;
/*      */   }
/*      */ 
/*      */   private static Vector removePointlessChildren(XSParticleDecl p)
/*      */   {
/* 1017 */     if ((p.fType == 1) || (p.fType == 2))
/*      */     {
/* 1019 */       return null;
/*      */     }
/* 1021 */     Vector children = new Vector();
/*      */ 
/* 1023 */     XSModelGroupImpl group = (XSModelGroupImpl)p.fValue;
/* 1024 */     for (int i = 0; i < group.fParticleCount; i++) {
/* 1025 */       gatherChildren(group.fCompositor, group.fParticles[i], children);
/*      */     }
/* 1027 */     return children;
/*      */   }
/*      */ 
/*      */   private static void gatherChildren(int parentType, XSParticleDecl p, Vector children)
/*      */   {
/* 1033 */     int min = p.fMinOccurs;
/* 1034 */     int max = p.fMaxOccurs;
/* 1035 */     int type = p.fType;
/* 1036 */     if (type == 3) {
/* 1037 */       type = ((XSModelGroupImpl)p.fValue).fCompositor;
/*      */     }
/* 1039 */     if ((type == 1) || (type == 2))
/*      */     {
/* 1041 */       children.addElement(p);
/* 1042 */       return;
/*      */     }
/*      */ 
/* 1045 */     if ((min != 1) || (max != 1)) {
/* 1046 */       children.addElement(p);
/*      */     }
/* 1048 */     else if (parentType == type) {
/* 1049 */       XSModelGroupImpl group = (XSModelGroupImpl)p.fValue;
/* 1050 */       for (int i = 0; i < group.fParticleCount; i++)
/* 1051 */         gatherChildren(type, group.fParticles[i], children);
/*      */     }
/* 1053 */     else if (!p.isEmpty()) {
/* 1054 */       children.addElement(p);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkNameAndTypeOK(XSElementDecl dElement, int dMin, int dMax, XSElementDecl bElement, int bMin, int bMax)
/*      */     throws XMLSchemaException
/*      */   {
/* 1067 */     if ((dElement.fName != bElement.fName) || (dElement.fTargetNamespace != bElement.fTargetNamespace))
/*      */     {
/* 1069 */       throw new XMLSchemaException("rcase-NameAndTypeOK.1", new Object[] { dElement.fName, dElement.fTargetNamespace, bElement.fName, bElement.fTargetNamespace });
/*      */     }
/*      */ 
/* 1077 */     if ((!bElement.getNillable()) && (dElement.getNillable())) {
/* 1078 */       throw new XMLSchemaException("rcase-NameAndTypeOK.2", new Object[] { dElement.fName });
/*      */     }
/*      */ 
/* 1085 */     if (!checkOccurrenceRange(dMin, dMax, bMin, bMax)) {
/* 1086 */       throw new XMLSchemaException("rcase-NameAndTypeOK.3", new Object[] { dElement.fName, Integer.toString(dMin), dMax == -1 ? "unbounded" : Integer.toString(dMax), Integer.toString(bMin), bMax == -1 ? "unbounded" : Integer.toString(bMax) });
/*      */     }
/*      */ 
/* 1098 */     if (bElement.getConstraintType() == 2)
/*      */     {
/* 1100 */       if (dElement.getConstraintType() != 2) {
/* 1101 */         throw new XMLSchemaException("rcase-NameAndTypeOK.4.a", new Object[] { dElement.fName, bElement.fDefault.stringValue() });
/*      */       }
/*      */ 
/* 1106 */       boolean isSimple = false;
/* 1107 */       if ((dElement.fType.getTypeCategory() == 16) || (((XSComplexTypeDecl)dElement.fType).fContentType == 1))
/*      */       {
/* 1109 */         isSimple = true;
/*      */       }
/*      */ 
/* 1113 */       if (((!isSimple) && (!bElement.fDefault.normalizedValue.equals(dElement.fDefault.normalizedValue))) || ((isSimple) && (!bElement.fDefault.actualValue.equals(dElement.fDefault.actualValue))))
/*      */       {
/* 1115 */         throw new XMLSchemaException("rcase-NameAndTypeOK.4.b", new Object[] { dElement.fName, dElement.fDefault.stringValue(), bElement.fDefault.stringValue() });
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1125 */     checkIDConstraintRestriction(dElement, bElement);
/*      */ 
/* 1130 */     int blockSet1 = dElement.fBlock;
/* 1131 */     int blockSet2 = bElement.fBlock;
/* 1132 */     if (((blockSet1 & blockSet2) != blockSet2) || ((blockSet1 == 0) && (blockSet2 != 0)))
/*      */     {
/* 1134 */       throw new XMLSchemaException("rcase-NameAndTypeOK.6", new Object[] { dElement.fName });
/*      */     }
/*      */ 
/* 1141 */     if (!checkTypeDerivationOk(dElement.fType, bElement.fType, (short)25))
/*      */     {
/* 1143 */       throw new XMLSchemaException("rcase-NameAndTypeOK.7", new Object[] { dElement.fName, dElement.fType.getName(), bElement.fType.getName() });
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkIDConstraintRestriction(XSElementDecl derivedElemDecl, XSElementDecl baseElemDecl)
/*      */     throws XMLSchemaException
/*      */   {
/*      */   }
/*      */ 
/*      */   private static boolean checkOccurrenceRange(int min1, int max1, int min2, int max2)
/*      */   {
/* 1159 */     if ((min1 >= min2) && ((max2 == -1) || ((max1 != -1) && (max1 <= max2))))
/*      */     {
/* 1162 */       return true;
/*      */     }
/* 1164 */     return false;
/*      */   }
/*      */ 
/*      */   private static void checkNSCompat(XSElementDecl elem, int min1, int max1, XSWildcardDecl wildcard, int min2, int max2, boolean checkWCOccurrence)
/*      */     throws XMLSchemaException
/*      */   {
/* 1173 */     if ((checkWCOccurrence) && (!checkOccurrenceRange(min1, max1, min2, max2))) {
/* 1174 */       throw new XMLSchemaException("rcase-NSCompat.2", new Object[] { elem.fName, Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1184 */     if (!wildcard.allowNamespace(elem.fTargetNamespace))
/* 1185 */       throw new XMLSchemaException("rcase-NSCompat.1", new Object[] { elem.fName, elem.fTargetNamespace });
/*      */   }
/*      */ 
/*      */   private static void checkNSSubset(XSWildcardDecl dWildcard, int min1, int max1, XSWildcardDecl bWildcard, int min2, int max2)
/*      */     throws XMLSchemaException
/*      */   {
/* 1196 */     if (!checkOccurrenceRange(min1, max1, min2, max2)) {
/* 1197 */       throw new XMLSchemaException("rcase-NSSubset.2", new Object[] { Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1205 */     if (!dWildcard.isSubsetOf(bWildcard)) {
/* 1206 */       throw new XMLSchemaException("rcase-NSSubset.1", null);
/*      */     }
/*      */ 
/* 1209 */     if (dWildcard.weakerProcessContents(bWildcard))
/* 1210 */       throw new XMLSchemaException("rcase-NSSubset.3", new Object[] { dWildcard.getProcessContentsAsString(), bWildcard.getProcessContentsAsString() });
/*      */   }
/*      */ 
/*      */   private static void checkNSRecurseCheckCardinality(Vector children, int min1, int max1, SubstitutionGroupHandler dSGHandler, XSParticleDecl wildcard, int min2, int max2, boolean checkWCOccurrence)
/*      */     throws XMLSchemaException
/*      */   {
/* 1226 */     if ((checkWCOccurrence) && (!checkOccurrenceRange(min1, max1, min2, max2))) {
/* 1227 */       throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.2", new Object[] { Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1235 */     int count = children.size();
/*      */     try {
/* 1237 */       for (int i = 0; i < count; i++) {
/* 1238 */         XSParticleDecl particle1 = (XSParticleDecl)children.elementAt(i);
/* 1239 */         particleValidRestriction(particle1, dSGHandler, wildcard, null, false);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (XMLSchemaException e)
/*      */     {
/* 1246 */       throw new XMLSchemaException("rcase-NSRecurseCheckCardinality.1", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkRecurse(Vector dChildren, int min1, int max1, SubstitutionGroupHandler dSGHandler, Vector bChildren, int min2, int max2, SubstitutionGroupHandler bSGHandler)
/*      */     throws XMLSchemaException
/*      */   {
/* 1258 */     if (!checkOccurrenceRange(min1, max1, min2, max2)) {
/* 1259 */       throw new XMLSchemaException("rcase-Recurse.1", new Object[] { Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1266 */     int count1 = dChildren.size();
/* 1267 */     int count2 = bChildren.size();
/*      */ 
/* 1269 */     int current = 0;
/* 1270 */     for (int i = 0; i < count1; i++)
/*      */     {
/* 1272 */       XSParticleDecl particle1 = (XSParticleDecl)dChildren.elementAt(i);
/* 1273 */       for (int j = current; j < count2; j++) {
/* 1274 */         XSParticleDecl particle2 = (XSParticleDecl)bChildren.elementAt(j);
/* 1275 */         current++;
/*      */         try {
/* 1277 */           particleValidRestriction(particle1, dSGHandler, particle2, bSGHandler);
/*      */         }
/*      */         catch (XMLSchemaException e)
/*      */         {
/* 1281 */           if (!particle2.emptiable())
/* 1282 */             throw new XMLSchemaException("rcase-Recurse.2", null);
/*      */         }
/*      */       }
/* 1285 */       throw new XMLSchemaException("rcase-Recurse.2", null);
/*      */     }
/*      */ 
/* 1289 */     for (int j = current; j < count2; j++) {
/* 1290 */       XSParticleDecl particle2 = (XSParticleDecl)bChildren.elementAt(j);
/* 1291 */       if (!particle2.emptiable())
/* 1292 */         throw new XMLSchemaException("rcase-Recurse.2", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkRecurseUnordered(Vector dChildren, int min1, int max1, SubstitutionGroupHandler dSGHandler, Vector bChildren, int min2, int max2, SubstitutionGroupHandler bSGHandler)
/*      */     throws XMLSchemaException
/*      */   {
/* 1306 */     if (!checkOccurrenceRange(min1, max1, min2, max2)) {
/* 1307 */       throw new XMLSchemaException("rcase-RecurseUnordered.1", new Object[] { Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1314 */     int count1 = dChildren.size();
/* 1315 */     int count2 = bChildren.size();
/*      */ 
/* 1317 */     boolean[] foundIt = new boolean[count2];
/*      */ 
/* 1319 */     for (int i = 0; i < count1; i++) {
/* 1320 */       XSParticleDecl particle1 = (XSParticleDecl)dChildren.elementAt(i);
/*      */ 
/* 1322 */       for (int j = 0; j < count2; j++) {
/* 1323 */         XSParticleDecl particle2 = (XSParticleDecl)bChildren.elementAt(j);
/*      */         try {
/* 1325 */           particleValidRestriction(particle1, dSGHandler, particle2, bSGHandler);
/* 1326 */           if (foundIt[j] != 0) {
/* 1327 */             throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
/*      */           }
/* 1329 */           foundIt[j] = true;
/*      */         }
/*      */         catch (XMLSchemaException e)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1337 */       throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
/*      */     }
/*      */ 
/* 1341 */     for (int j = 0; j < count2; j++) {
/* 1342 */       XSParticleDecl particle2 = (XSParticleDecl)bChildren.elementAt(j);
/* 1343 */       if ((foundIt[j] == 0) && (!particle2.emptiable()))
/* 1344 */         throw new XMLSchemaException("rcase-RecurseUnordered.2", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkRecurseLax(Vector dChildren, int min1, int max1, SubstitutionGroupHandler dSGHandler, Vector bChildren, int min2, int max2, SubstitutionGroupHandler bSGHandler)
/*      */     throws XMLSchemaException
/*      */   {
/* 1357 */     if (!checkOccurrenceRange(min1, max1, min2, max2)) {
/* 1358 */       throw new XMLSchemaException("rcase-RecurseLax.1", new Object[] { Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1365 */     int count1 = dChildren.size();
/* 1366 */     int count2 = bChildren.size();
/*      */ 
/* 1368 */     int current = 0;
/* 1369 */     for (int i = 0; i < count1; i++)
/*      */     {
/* 1371 */       XSParticleDecl particle1 = (XSParticleDecl)dChildren.elementAt(i);
/* 1372 */       for (int j = current; j < count2; j++) {
/* 1373 */         XSParticleDecl particle2 = (XSParticleDecl)bChildren.elementAt(j);
/* 1374 */         current++;
/*      */         try
/*      */         {
/* 1378 */           if (particleValidRestriction(particle1, dSGHandler, particle2, bSGHandler)) {
/* 1379 */             current--;
/*      */           }
/*      */         }
/*      */         catch (XMLSchemaException e)
/*      */         {
/*      */         }
/*      */       }
/* 1386 */       throw new XMLSchemaException("rcase-RecurseLax.2", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void checkMapAndSum(Vector dChildren, int min1, int max1, SubstitutionGroupHandler dSGHandler, Vector bChildren, int min2, int max2, SubstitutionGroupHandler bSGHandler)
/*      */     throws XMLSchemaException
/*      */   {
/* 1413 */     if (!checkOccurrenceRange(min1, max1, min2, max2)) {
/* 1414 */       throw new XMLSchemaException("rcase-MapAndSum.2", new Object[] { Integer.toString(min1), max1 == -1 ? "unbounded" : Integer.toString(max1), Integer.toString(min2), max2 == -1 ? "unbounded" : Integer.toString(max2) });
/*      */     }
/*      */ 
/* 1421 */     int count1 = dChildren.size();
/* 1422 */     int count2 = bChildren.size();
/*      */ 
/* 1424 */     for (int i = 0; i < count1; i++)
/*      */     {
/* 1426 */       XSParticleDecl particle1 = (XSParticleDecl)dChildren.elementAt(i);
/* 1427 */       for (int j = 0; j < count2; j++) {
/* 1428 */         XSParticleDecl particle2 = (XSParticleDecl)bChildren.elementAt(j);
/*      */         try {
/* 1430 */           particleValidRestriction(particle1, dSGHandler, particle2, bSGHandler);
/*      */         }
/*      */         catch (XMLSchemaException e)
/*      */         {
/*      */         }
/*      */       }
/*      */ 
/* 1437 */       throw new XMLSchemaException("rcase-MapAndSum.1", null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static boolean overlapUPA(XSElementDecl element1, XSElementDecl element2, SubstitutionGroupHandler sgHandler)
/*      */   {
/* 1445 */     if ((element1.fName == element2.fName) && (element1.fTargetNamespace == element2.fTargetNamespace))
/*      */     {
/* 1447 */       return true;
/*      */     }
/*      */ 
/* 1452 */     XSElementDecl[] subGroup = sgHandler.getSubstitutionGroup(element1);
/* 1453 */     for (int i = subGroup.length - 1; i >= 0; i--) {
/* 1454 */       if ((subGroup[i].fName == element2.fName) && (subGroup[i].fTargetNamespace == element2.fTargetNamespace))
/*      */       {
/* 1456 */         return true;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1462 */     subGroup = sgHandler.getSubstitutionGroup(element2);
/* 1463 */     for (int i = subGroup.length - 1; i >= 0; i--) {
/* 1464 */       if ((subGroup[i].fName == element1.fName) && (subGroup[i].fTargetNamespace == element1.fTargetNamespace))
/*      */       {
/* 1466 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1470 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean overlapUPA(XSElementDecl element, XSWildcardDecl wildcard, SubstitutionGroupHandler sgHandler)
/*      */   {
/* 1479 */     if (wildcard.allowNamespace(element.fTargetNamespace)) {
/* 1480 */       return true;
/*      */     }
/*      */ 
/* 1483 */     XSElementDecl[] subGroup = sgHandler.getSubstitutionGroup(element);
/* 1484 */     for (int i = subGroup.length - 1; i >= 0; i--) {
/* 1485 */       if (wildcard.allowNamespace(subGroup[i].fTargetNamespace)) {
/* 1486 */         return true;
/*      */       }
/*      */     }
/* 1489 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean overlapUPA(XSWildcardDecl wildcard1, XSWildcardDecl wildcard2)
/*      */   {
/* 1495 */     XSWildcardDecl intersect = wildcard1.performIntersectionWith(wildcard2, wildcard1.fProcessContents);
/* 1496 */     if ((intersect == null) || (intersect.fType != 3) || (intersect.fNamespaceList.length != 0))
/*      */     {
/* 1499 */       return true;
/*      */     }
/*      */ 
/* 1502 */     return false;
/*      */   }
/*      */ 
/*      */   public static boolean overlapUPA(Object decl1, Object decl2, SubstitutionGroupHandler sgHandler)
/*      */   {
/* 1508 */     if ((decl1 instanceof XSElementDecl)) {
/* 1509 */       if ((decl2 instanceof XSElementDecl)) {
/* 1510 */         return overlapUPA((XSElementDecl)decl1, (XSElementDecl)decl2, sgHandler);
/*      */       }
/*      */ 
/* 1515 */       return overlapUPA((XSElementDecl)decl1, (XSWildcardDecl)decl2, sgHandler);
/*      */     }
/*      */ 
/* 1521 */     if ((decl2 instanceof XSElementDecl)) {
/* 1522 */       return overlapUPA((XSElementDecl)decl2, (XSWildcardDecl)decl1, sgHandler);
/*      */     }
/*      */ 
/* 1527 */     return overlapUPA((XSWildcardDecl)decl1, (XSWildcardDecl)decl2);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSConstraints
 * JD-Core Version:    0.6.2
 */
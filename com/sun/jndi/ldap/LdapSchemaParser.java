/*      */ package com.sun.jndi.ldap;
/*      */ 
/*      */ import java.util.Vector;
/*      */ import javax.naming.ConfigurationException;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.directory.Attribute;
/*      */ import javax.naming.directory.Attributes;
/*      */ import javax.naming.directory.BasicAttribute;
/*      */ import javax.naming.directory.BasicAttributes;
/*      */ import javax.naming.directory.DirContext;
/*      */ import javax.naming.directory.InvalidAttributeIdentifierException;
/*      */ import javax.naming.directory.InvalidAttributeValueException;
/*      */ 
/*      */ final class LdapSchemaParser
/*      */ {
/*      */   private static final boolean debug = false;
/*      */   static final String OBJECTCLASSDESC_ATTR_ID = "objectClasses";
/*      */   static final String ATTRIBUTEDESC_ATTR_ID = "attributeTypes";
/*      */   static final String SYNTAXDESC_ATTR_ID = "ldapSyntaxes";
/*      */   static final String MATCHRULEDESC_ATTR_ID = "matchingRules";
/*      */   static final String OBJECTCLASS_DEFINITION_NAME = "ClassDefinition";
/*   55 */   private static final String[] CLASS_DEF_ATTRS = { "objectclass", "ClassDefinition" };
/*      */   static final String ATTRIBUTE_DEFINITION_NAME = "AttributeDefinition";
/*   59 */   private static final String[] ATTR_DEF_ATTRS = { "objectclass", "AttributeDefinition" };
/*      */   static final String SYNTAX_DEFINITION_NAME = "SyntaxDefinition";
/*   63 */   private static final String[] SYNTAX_DEF_ATTRS = { "objectclass", "SyntaxDefinition" };
/*      */   static final String MATCHRULE_DEFINITION_NAME = "MatchingRule";
/*   67 */   private static final String[] MATCHRULE_DEF_ATTRS = { "objectclass", "MatchingRule" };
/*      */   private static final char SINGLE_QUOTE = '\'';
/*      */   private static final char WHSP = ' ';
/*      */   private static final char OID_LIST_BEGIN = '(';
/*      */   private static final char OID_LIST_END = ')';
/*      */   private static final char OID_SEPARATOR = '$';
/*      */   private static final String NUMERICOID_ID = "NUMERICOID";
/*      */   private static final String NAME_ID = "NAME";
/*      */   private static final String DESC_ID = "DESC";
/*      */   private static final String OBSOLETE_ID = "OBSOLETE";
/*      */   private static final String SUP_ID = "SUP";
/*      */   private static final String PRIVATE_ID = "X-";
/*      */   private static final String ABSTRACT_ID = "ABSTRACT";
/*      */   private static final String STRUCTURAL_ID = "STRUCTURAL";
/*      */   private static final String AUXILARY_ID = "AUXILIARY";
/*      */   private static final String MUST_ID = "MUST";
/*      */   private static final String MAY_ID = "MAY";
/*      */   private static final String EQUALITY_ID = "EQUALITY";
/*      */   private static final String ORDERING_ID = "ORDERING";
/*      */   private static final String SUBSTR_ID = "SUBSTR";
/*      */   private static final String SYNTAX_ID = "SYNTAX";
/*      */   private static final String SINGLE_VAL_ID = "SINGLE-VALUE";
/*      */   private static final String COLLECTIVE_ID = "COLLECTIVE";
/*      */   private static final String NO_USER_MOD_ID = "NO-USER-MODIFICATION";
/*      */   private static final String USAGE_ID = "USAGE";
/*      */   private static final String SCHEMA_TRUE_VALUE = "true";
/*      */   private boolean netscapeBug;
/*      */ 
/*      */   LdapSchemaParser(boolean paramBoolean)
/*      */   {
/*  109 */     this.netscapeBug = paramBoolean;
/*      */   }
/*      */ 
/*      */   static final void LDAP2JNDISchema(Attributes paramAttributes, LdapSchemaCtx paramLdapSchemaCtx) throws NamingException
/*      */   {
/*  114 */     Attribute localAttribute1 = null;
/*  115 */     Attribute localAttribute2 = null;
/*  116 */     Attribute localAttribute3 = null;
/*  117 */     Attribute localAttribute4 = null;
/*      */ 
/*  119 */     localAttribute1 = paramAttributes.get("objectClasses");
/*  120 */     if (localAttribute1 != null) {
/*  121 */       objectDescs2ClassDefs(localAttribute1, paramLdapSchemaCtx);
/*      */     }
/*      */ 
/*  124 */     localAttribute2 = paramAttributes.get("attributeTypes");
/*  125 */     if (localAttribute2 != null) {
/*  126 */       attrDescs2AttrDefs(localAttribute2, paramLdapSchemaCtx);
/*      */     }
/*      */ 
/*  129 */     localAttribute3 = paramAttributes.get("ldapSyntaxes");
/*  130 */     if (localAttribute3 != null) {
/*  131 */       syntaxDescs2SyntaxDefs(localAttribute3, paramLdapSchemaCtx);
/*      */     }
/*      */ 
/*  134 */     localAttribute4 = paramAttributes.get("matchingRules");
/*  135 */     if (localAttribute4 != null)
/*  136 */       matchRuleDescs2MatchRuleDefs(localAttribute4, paramLdapSchemaCtx);
/*      */   }
/*      */ 
/*      */   private static final DirContext objectDescs2ClassDefs(Attribute paramAttribute, LdapSchemaCtx paramLdapSchemaCtx)
/*      */     throws NamingException
/*      */   {
/*  149 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*  150 */     localBasicAttributes.put(CLASS_DEF_ATTRS[0], CLASS_DEF_ATTRS[1]);
/*  151 */     LdapSchemaCtx localLdapSchemaCtx = paramLdapSchemaCtx.setup(2, "ClassDefinition", localBasicAttributes);
/*      */ 
/*  154 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*      */ 
/*  156 */     while (localNamingEnumeration.hasMore()) {
/*  157 */       String str2 = (String)localNamingEnumeration.next();
/*      */       try {
/*  159 */         Object[] arrayOfObject = desc2Def(str2);
/*  160 */         String str1 = (String)arrayOfObject[0];
/*  161 */         Attributes localAttributes = (Attributes)arrayOfObject[1];
/*  162 */         localLdapSchemaCtx.setup(6, str1, localAttributes);
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  169 */     return localLdapSchemaCtx;
/*      */   }
/*      */ 
/*      */   private static final DirContext attrDescs2AttrDefs(Attribute paramAttribute, LdapSchemaCtx paramLdapSchemaCtx)
/*      */     throws NamingException
/*      */   {
/*  181 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*  182 */     localBasicAttributes.put(ATTR_DEF_ATTRS[0], ATTR_DEF_ATTRS[1]);
/*  183 */     LdapSchemaCtx localLdapSchemaCtx = paramLdapSchemaCtx.setup(3, "AttributeDefinition", localBasicAttributes);
/*      */ 
/*  186 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*      */ 
/*  188 */     while (localNamingEnumeration.hasMore()) {
/*  189 */       String str2 = (String)localNamingEnumeration.next();
/*      */       try {
/*  191 */         Object[] arrayOfObject = desc2Def(str2);
/*  192 */         String str1 = (String)arrayOfObject[0];
/*  193 */         Attributes localAttributes = (Attributes)arrayOfObject[1];
/*  194 */         localLdapSchemaCtx.setup(7, str1, localAttributes);
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  201 */     return localLdapSchemaCtx;
/*      */   }
/*      */ 
/*      */   private static final DirContext syntaxDescs2SyntaxDefs(Attribute paramAttribute, LdapSchemaCtx paramLdapSchemaCtx)
/*      */     throws NamingException
/*      */   {
/*  214 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*  215 */     localBasicAttributes.put(SYNTAX_DEF_ATTRS[0], SYNTAX_DEF_ATTRS[1]);
/*  216 */     LdapSchemaCtx localLdapSchemaCtx = paramLdapSchemaCtx.setup(4, "SyntaxDefinition", localBasicAttributes);
/*      */ 
/*  219 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*      */ 
/*  221 */     while (localNamingEnumeration.hasMore()) {
/*  222 */       String str2 = (String)localNamingEnumeration.next();
/*      */       try {
/*  224 */         Object[] arrayOfObject = desc2Def(str2);
/*  225 */         String str1 = (String)arrayOfObject[0];
/*  226 */         Attributes localAttributes = (Attributes)arrayOfObject[1];
/*  227 */         localLdapSchemaCtx.setup(8, str1, localAttributes);
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  234 */     return localLdapSchemaCtx;
/*      */   }
/*      */ 
/*      */   private static final DirContext matchRuleDescs2MatchRuleDefs(Attribute paramAttribute, LdapSchemaCtx paramLdapSchemaCtx)
/*      */     throws NamingException
/*      */   {
/*  247 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*  248 */     localBasicAttributes.put(MATCHRULE_DEF_ATTRS[0], MATCHRULE_DEF_ATTRS[1]);
/*  249 */     LdapSchemaCtx localLdapSchemaCtx = paramLdapSchemaCtx.setup(5, "MatchingRule", localBasicAttributes);
/*      */ 
/*  252 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*      */ 
/*  254 */     while (localNamingEnumeration.hasMore()) {
/*  255 */       String str2 = (String)localNamingEnumeration.next();
/*      */       try {
/*  257 */         Object[] arrayOfObject = desc2Def(str2);
/*  258 */         String str1 = (String)arrayOfObject[0];
/*  259 */         Attributes localAttributes = (Attributes)arrayOfObject[1];
/*  260 */         localLdapSchemaCtx.setup(9, str1, localAttributes);
/*      */       }
/*      */       catch (NamingException localNamingException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  267 */     return localLdapSchemaCtx;
/*      */   }
/*      */ 
/*      */   private static final Object[] desc2Def(String paramString)
/*      */     throws NamingException
/*      */   {
/*  274 */     BasicAttributes localBasicAttributes = new BasicAttributes(true);
/*  275 */     Attribute localAttribute = null;
/*  276 */     int[] arrayOfInt = { 1 };
/*  277 */     int i = 1;
/*      */ 
/*  280 */     localAttribute = readNumericOID(paramString, arrayOfInt);
/*  281 */     String str = (String)localAttribute.get(0);
/*  282 */     localBasicAttributes.put(localAttribute);
/*      */ 
/*  284 */     skipWhitespace(paramString, arrayOfInt);
/*      */ 
/*  286 */     while (i != 0) {
/*  287 */       localAttribute = readNextTag(paramString, arrayOfInt);
/*  288 */       localBasicAttributes.put(localAttribute);
/*      */ 
/*  290 */       if (localAttribute.getID().equals("NAME")) {
/*  291 */         str = (String)localAttribute.get(0);
/*      */       }
/*      */ 
/*  294 */       skipWhitespace(paramString, arrayOfInt);
/*      */ 
/*  296 */       if (arrayOfInt[0] >= paramString.length() - 1) {
/*  297 */         i = 0;
/*      */       }
/*      */     }
/*      */ 
/*  301 */     return new Object[] { str, localBasicAttributes };
/*      */   }
/*      */ 
/*      */   private static final int findTrailingWhitespace(String paramString, int paramInt)
/*      */   {
/*  307 */     for (int i = paramInt; i > 0; i--) {
/*  308 */       if (paramString.charAt(i) != ' ') {
/*  309 */         return i + 1;
/*      */       }
/*      */     }
/*  312 */     return 0;
/*      */   }
/*      */ 
/*      */   private static final void skipWhitespace(String paramString, int[] paramArrayOfInt) {
/*  316 */     for (int i = paramArrayOfInt[0]; i < paramString.length(); i++)
/*  317 */       if (paramString.charAt(i) != ' ') {
/*  318 */         paramArrayOfInt[0] = i;
/*      */ 
/*  322 */         return;
/*      */       }
/*      */   }
/*      */ 
/*      */   private static final Attribute readNumericOID(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  335 */     String str = null;
/*      */ 
/*  337 */     skipWhitespace(paramString, paramArrayOfInt);
/*      */ 
/*  339 */     int i = paramArrayOfInt[0];
/*  340 */     int j = paramString.indexOf(' ', i);
/*      */ 
/*  342 */     if ((j == -1) || (j - i < 1)) {
/*  343 */       throw new InvalidAttributeValueException("no numericoid found: " + paramString);
/*      */     }
/*      */ 
/*  347 */     str = paramString.substring(i, j);
/*      */ 
/*  349 */     paramArrayOfInt[0] += str.length();
/*      */ 
/*  351 */     return new BasicAttribute("NUMERICOID", str);
/*      */   }
/*      */ 
/*      */   private static final Attribute readNextTag(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  357 */     BasicAttribute localBasicAttribute = null;
/*  358 */     String str = null;
/*  359 */     String[] arrayOfString = null;
/*      */ 
/*  361 */     skipWhitespace(paramString, paramArrayOfInt);
/*      */ 
/*  368 */     int i = paramString.indexOf(' ', paramArrayOfInt[0]);
/*      */ 
/*  371 */     if (i < 0)
/*  372 */       str = paramString.substring(paramArrayOfInt[0], paramString.length() - 1);
/*      */     else {
/*  374 */       str = paramString.substring(paramArrayOfInt[0], i);
/*      */     }
/*      */ 
/*  377 */     arrayOfString = readTag(str, paramString, paramArrayOfInt);
/*      */ 
/*  380 */     if (arrayOfString.length < 0) {
/*  381 */       throw new InvalidAttributeValueException("no values for attribute \"" + str + "\"");
/*      */     }
/*      */ 
/*  387 */     localBasicAttribute = new BasicAttribute(str, arrayOfString[0]);
/*      */ 
/*  390 */     for (int j = 1; j < arrayOfString.length; j++) {
/*  391 */       localBasicAttribute.add(arrayOfString[j]);
/*      */     }
/*      */ 
/*  394 */     return localBasicAttribute;
/*      */   }
/*      */ 
/*      */   private static final String[] readTag(String paramString1, String paramString2, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  405 */     paramArrayOfInt[0] += paramString1.length();
/*  406 */     skipWhitespace(paramString2, paramArrayOfInt);
/*      */ 
/*  408 */     if (paramString1.equals("NAME")) {
/*  409 */       return readQDescrs(paramString2, paramArrayOfInt);
/*      */     }
/*      */ 
/*  412 */     if (paramString1.equals("DESC")) {
/*  413 */       return readQDString(paramString2, paramArrayOfInt);
/*      */     }
/*      */ 
/*  416 */     if ((paramString1.equals("EQUALITY")) || (paramString1.equals("ORDERING")) || (paramString1.equals("SUBSTR")) || (paramString1.equals("SYNTAX")))
/*      */     {
/*  421 */       return readWOID(paramString2, paramArrayOfInt);
/*      */     }
/*      */ 
/*  424 */     if ((paramString1.equals("OBSOLETE")) || (paramString1.equals("ABSTRACT")) || (paramString1.equals("STRUCTURAL")) || (paramString1.equals("AUXILIARY")) || (paramString1.equals("SINGLE-VALUE")) || (paramString1.equals("COLLECTIVE")) || (paramString1.equals("NO-USER-MODIFICATION")))
/*      */     {
/*  431 */       return new String[] { "true" };
/*      */     }
/*      */ 
/*  434 */     if ((paramString1.equals("SUP")) || (paramString1.equals("MUST")) || (paramString1.equals("MAY")) || (paramString1.equals("USAGE")))
/*      */     {
/*  438 */       return readOIDs(paramString2, paramArrayOfInt);
/*      */     }
/*      */ 
/*  442 */     return readQDStrings(paramString2, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static final String[] readQDString(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  450 */     int i = paramString.indexOf('\'', paramArrayOfInt[0]) + 1;
/*  451 */     int j = paramString.indexOf('\'', i);
/*      */ 
/*  458 */     if ((i == -1) || (j == -1) || (i == j)) {
/*  459 */       throw new InvalidAttributeIdentifierException("malformed QDString: " + paramString);
/*      */     }
/*      */ 
/*  465 */     if (paramString.charAt(i - 1) != '\'') {
/*  466 */       throw new InvalidAttributeIdentifierException("qdstring has no end mark: " + paramString);
/*      */     }
/*      */ 
/*  471 */     paramArrayOfInt[0] = (j + 1);
/*  472 */     return new String[] { paramString.substring(i, j) };
/*      */   }
/*      */ 
/*      */   private static final String[] readQDStrings(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  484 */     return readQDescrs(paramString, paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   private static final String[] readQDescrs(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  501 */     skipWhitespace(paramString, paramArrayOfInt);
/*      */ 
/*  503 */     switch (paramString.charAt(paramArrayOfInt[0])) {
/*      */     case '(':
/*  505 */       return readQDescrList(paramString, paramArrayOfInt);
/*      */     case '\'':
/*  507 */       return readQDString(paramString, paramArrayOfInt);
/*      */     }
/*  509 */     throw new InvalidAttributeValueException("unexpected oids string: " + paramString);
/*      */   }
/*      */ 
/*      */   private static final String[] readQDescrList(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  523 */     Vector localVector = new Vector(5);
/*      */ 
/*  529 */     paramArrayOfInt[0] += 1;
/*  530 */     skipWhitespace(paramString, paramArrayOfInt);
/*  531 */     int i = paramArrayOfInt[0];
/*  532 */     int j = paramString.indexOf(')', i);
/*      */ 
/*  534 */     if (j == -1) {
/*  535 */       throw new InvalidAttributeValueException("oidlist has no end mark: " + paramString);
/*      */     }
/*      */ 
/*  539 */     while (i < j) {
/*  540 */       arrayOfString = readQDString(paramString, paramArrayOfInt);
/*      */ 
/*  547 */       localVector.addElement(arrayOfString[0]);
/*  548 */       skipWhitespace(paramString, paramArrayOfInt);
/*  549 */       i = paramArrayOfInt[0];
/*      */     }
/*      */ 
/*  552 */     paramArrayOfInt[0] = (j + 1);
/*      */ 
/*  554 */     String[] arrayOfString = new String[localVector.size()];
/*  555 */     for (int k = 0; k < arrayOfString.length; k++) {
/*  556 */       arrayOfString[k] = ((String)localVector.elementAt(k));
/*      */     }
/*  558 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private static final String[] readWOID(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  568 */     skipWhitespace(paramString, paramArrayOfInt);
/*      */ 
/*  570 */     if (paramString.charAt(paramArrayOfInt[0]) == '\'')
/*      */     {
/*  572 */       return readQDString(paramString, paramArrayOfInt);
/*      */     }
/*      */ 
/*  577 */     int i = paramArrayOfInt[0];
/*  578 */     int j = paramString.indexOf(' ', i);
/*      */ 
/*  585 */     if ((j == -1) || (i == j)) {
/*  586 */       throw new InvalidAttributeIdentifierException("malformed OID: " + paramString);
/*      */     }
/*      */ 
/*  590 */     paramArrayOfInt[0] = (j + 1);
/*      */ 
/*  592 */     return new String[] { paramString.substring(i, j) };
/*      */   }
/*      */ 
/*      */   private static final String[] readOIDs(String paramString, int[] paramArrayOfInt)
/*      */     throws NamingException
/*      */   {
/*  606 */     skipWhitespace(paramString, paramArrayOfInt);
/*      */ 
/*  609 */     if (paramString.charAt(paramArrayOfInt[0]) != '(') {
/*  610 */       return readWOID(paramString, paramArrayOfInt);
/*      */     }
/*      */ 
/*  616 */     String str = null;
/*  617 */     Vector localVector = new Vector(5);
/*      */ 
/*  623 */     paramArrayOfInt[0] += 1;
/*  624 */     skipWhitespace(paramString, paramArrayOfInt);
/*  625 */     int i = paramArrayOfInt[0];
/*  626 */     int k = paramString.indexOf(')', i);
/*  627 */     int j = paramString.indexOf('$', i);
/*      */ 
/*  629 */     if (k == -1) {
/*  630 */       throw new InvalidAttributeValueException("oidlist has no end mark: " + paramString);
/*      */     }
/*      */ 
/*  634 */     if ((j == -1) || (k < j)) {
/*  635 */       j = k;
/*      */     }
/*      */ 
/*  638 */     while ((j < k) && (j > 0)) {
/*  639 */       m = findTrailingWhitespace(paramString, j - 1);
/*  640 */       str = paramString.substring(i, m);
/*      */ 
/*  645 */       localVector.addElement(str);
/*  646 */       paramArrayOfInt[0] = (j + 1);
/*  647 */       skipWhitespace(paramString, paramArrayOfInt);
/*  648 */       i = paramArrayOfInt[0];
/*  649 */       j = paramString.indexOf('$', i);
/*      */     }
/*      */ 
/*  658 */     int m = findTrailingWhitespace(paramString, k - 1);
/*  659 */     str = paramString.substring(i, m);
/*  660 */     localVector.addElement(str);
/*      */ 
/*  662 */     paramArrayOfInt[0] = (k + 1);
/*      */ 
/*  664 */     String[] arrayOfString = new String[localVector.size()];
/*  665 */     for (int n = 0; n < arrayOfString.length; n++) {
/*  666 */       arrayOfString[n] = ((String)localVector.elementAt(n));
/*      */     }
/*  668 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   private final String classDef2ObjectDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/*  771 */     StringBuffer localStringBuffer = new StringBuffer("( ");
/*      */ 
/*  773 */     Attribute localAttribute = null;
/*  774 */     int i = 0;
/*      */ 
/*  778 */     localAttribute = paramAttributes.get("NUMERICOID");
/*  779 */     if (localAttribute != null) {
/*  780 */       localStringBuffer.append(writeNumericOID(localAttribute));
/*  781 */       i++;
/*      */     } else {
/*  783 */       throw new ConfigurationException("Class definition doesn'thave a numeric OID");
/*      */     }
/*      */ 
/*  787 */     localAttribute = paramAttributes.get("NAME");
/*  788 */     if (localAttribute != null) {
/*  789 */       localStringBuffer.append(writeQDescrs(localAttribute));
/*  790 */       i++;
/*      */     }
/*      */ 
/*  793 */     localAttribute = paramAttributes.get("DESC");
/*  794 */     if (localAttribute != null) {
/*  795 */       localStringBuffer.append(writeQDString(localAttribute));
/*  796 */       i++;
/*      */     }
/*      */ 
/*  799 */     localAttribute = paramAttributes.get("OBSOLETE");
/*  800 */     if (localAttribute != null) {
/*  801 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  802 */       i++;
/*      */     }
/*      */ 
/*  805 */     localAttribute = paramAttributes.get("SUP");
/*  806 */     if (localAttribute != null) {
/*  807 */       localStringBuffer.append(writeOIDs(localAttribute));
/*  808 */       i++;
/*      */     }
/*      */ 
/*  811 */     localAttribute = paramAttributes.get("ABSTRACT");
/*  812 */     if (localAttribute != null) {
/*  813 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  814 */       i++;
/*      */     }
/*      */ 
/*  817 */     localAttribute = paramAttributes.get("STRUCTURAL");
/*  818 */     if (localAttribute != null) {
/*  819 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  820 */       i++;
/*      */     }
/*      */ 
/*  823 */     localAttribute = paramAttributes.get("AUXILIARY");
/*  824 */     if (localAttribute != null) {
/*  825 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  826 */       i++;
/*      */     }
/*      */ 
/*  829 */     localAttribute = paramAttributes.get("MUST");
/*  830 */     if (localAttribute != null) {
/*  831 */       localStringBuffer.append(writeOIDs(localAttribute));
/*  832 */       i++;
/*      */     }
/*      */ 
/*  835 */     localAttribute = paramAttributes.get("MAY");
/*  836 */     if (localAttribute != null) {
/*  837 */       localStringBuffer.append(writeOIDs(localAttribute));
/*  838 */       i++;
/*      */     }
/*      */ 
/*  842 */     if (i < paramAttributes.size()) {
/*  843 */       String str = null;
/*      */ 
/*  846 */       NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/*  847 */       while (localNamingEnumeration.hasMoreElements())
/*      */       {
/*  849 */         localAttribute = (Attribute)localNamingEnumeration.next();
/*  850 */         str = localAttribute.getID();
/*      */ 
/*  853 */         if ((!str.equals("NUMERICOID")) && (!str.equals("NAME")) && (!str.equals("SUP")) && (!str.equals("MAY")) && (!str.equals("MUST")) && (!str.equals("STRUCTURAL")) && (!str.equals("DESC")) && (!str.equals("AUXILIARY")) && (!str.equals("ABSTRACT")) && (!str.equals("OBSOLETE")))
/*      */         {
/*  866 */           localStringBuffer.append(writeQDStrings(localAttribute));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  871 */     localStringBuffer.append(")");
/*      */ 
/*  873 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private final String attrDef2AttrDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/*  883 */     StringBuffer localStringBuffer = new StringBuffer("( ");
/*      */ 
/*  885 */     Attribute localAttribute = null;
/*  886 */     int i = 0;
/*      */ 
/*  890 */     localAttribute = paramAttributes.get("NUMERICOID");
/*  891 */     if (localAttribute != null) {
/*  892 */       localStringBuffer.append(writeNumericOID(localAttribute));
/*  893 */       i++;
/*      */     } else {
/*  895 */       throw new ConfigurationException("Attribute type doesn'thave a numeric OID");
/*      */     }
/*      */ 
/*  899 */     localAttribute = paramAttributes.get("NAME");
/*  900 */     if (localAttribute != null) {
/*  901 */       localStringBuffer.append(writeQDescrs(localAttribute));
/*  902 */       i++;
/*      */     }
/*      */ 
/*  905 */     localAttribute = paramAttributes.get("DESC");
/*  906 */     if (localAttribute != null) {
/*  907 */       localStringBuffer.append(writeQDString(localAttribute));
/*  908 */       i++;
/*      */     }
/*      */ 
/*  911 */     localAttribute = paramAttributes.get("OBSOLETE");
/*  912 */     if (localAttribute != null) {
/*  913 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  914 */       i++;
/*      */     }
/*      */ 
/*  917 */     localAttribute = paramAttributes.get("SUP");
/*  918 */     if (localAttribute != null) {
/*  919 */       localStringBuffer.append(writeWOID(localAttribute));
/*  920 */       i++;
/*      */     }
/*      */ 
/*  923 */     localAttribute = paramAttributes.get("EQUALITY");
/*  924 */     if (localAttribute != null) {
/*  925 */       localStringBuffer.append(writeWOID(localAttribute));
/*  926 */       i++;
/*      */     }
/*      */ 
/*  929 */     localAttribute = paramAttributes.get("ORDERING");
/*  930 */     if (localAttribute != null) {
/*  931 */       localStringBuffer.append(writeWOID(localAttribute));
/*  932 */       i++;
/*      */     }
/*      */ 
/*  935 */     localAttribute = paramAttributes.get("SUBSTR");
/*  936 */     if (localAttribute != null) {
/*  937 */       localStringBuffer.append(writeWOID(localAttribute));
/*  938 */       i++;
/*      */     }
/*      */ 
/*  941 */     localAttribute = paramAttributes.get("SYNTAX");
/*  942 */     if (localAttribute != null) {
/*  943 */       localStringBuffer.append(writeWOID(localAttribute));
/*  944 */       i++;
/*      */     }
/*      */ 
/*  947 */     localAttribute = paramAttributes.get("SINGLE-VALUE");
/*  948 */     if (localAttribute != null) {
/*  949 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  950 */       i++;
/*      */     }
/*      */ 
/*  953 */     localAttribute = paramAttributes.get("COLLECTIVE");
/*  954 */     if (localAttribute != null) {
/*  955 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  956 */       i++;
/*      */     }
/*      */ 
/*  959 */     localAttribute = paramAttributes.get("NO-USER-MODIFICATION");
/*  960 */     if (localAttribute != null) {
/*  961 */       localStringBuffer.append(writeBoolean(localAttribute));
/*  962 */       i++;
/*      */     }
/*      */ 
/*  965 */     localAttribute = paramAttributes.get("USAGE");
/*  966 */     if (localAttribute != null) {
/*  967 */       localStringBuffer.append(writeQDString(localAttribute));
/*  968 */       i++;
/*      */     }
/*      */ 
/*  972 */     if (i < paramAttributes.size()) {
/*  973 */       String str = null;
/*      */ 
/*  976 */       NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/*  977 */       while (localNamingEnumeration.hasMoreElements())
/*      */       {
/*  979 */         localAttribute = (Attribute)localNamingEnumeration.next();
/*  980 */         str = localAttribute.getID();
/*      */ 
/*  983 */         if ((!str.equals("NUMERICOID")) && (!str.equals("NAME")) && (!str.equals("SYNTAX")) && (!str.equals("DESC")) && (!str.equals("SINGLE-VALUE")) && (!str.equals("EQUALITY")) && (!str.equals("ORDERING")) && (!str.equals("SUBSTR")) && (!str.equals("NO-USER-MODIFICATION")) && (!str.equals("USAGE")) && (!str.equals("SUP")) && (!str.equals("COLLECTIVE")) && (!str.equals("OBSOLETE")))
/*      */         {
/*  999 */           localStringBuffer.append(writeQDStrings(localAttribute));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1004 */     localStringBuffer.append(")");
/*      */ 
/* 1006 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private final String syntaxDef2SyntaxDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/* 1016 */     StringBuffer localStringBuffer = new StringBuffer("( ");
/*      */ 
/* 1018 */     Attribute localAttribute = null;
/* 1019 */     int i = 0;
/*      */ 
/* 1023 */     localAttribute = paramAttributes.get("NUMERICOID");
/* 1024 */     if (localAttribute != null) {
/* 1025 */       localStringBuffer.append(writeNumericOID(localAttribute));
/* 1026 */       i++;
/*      */     } else {
/* 1028 */       throw new ConfigurationException("Attribute type doesn'thave a numeric OID");
/*      */     }
/*      */ 
/* 1032 */     localAttribute = paramAttributes.get("DESC");
/* 1033 */     if (localAttribute != null) {
/* 1034 */       localStringBuffer.append(writeQDString(localAttribute));
/* 1035 */       i++;
/*      */     }
/*      */ 
/* 1039 */     if (i < paramAttributes.size()) {
/* 1040 */       String str = null;
/*      */ 
/* 1043 */       NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/* 1044 */       while (localNamingEnumeration.hasMoreElements())
/*      */       {
/* 1046 */         localAttribute = (Attribute)localNamingEnumeration.next();
/* 1047 */         str = localAttribute.getID();
/*      */ 
/* 1050 */         if ((!str.equals("NUMERICOID")) && (!str.equals("DESC")))
/*      */         {
/* 1055 */           localStringBuffer.append(writeQDStrings(localAttribute));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1060 */     localStringBuffer.append(")");
/*      */ 
/* 1062 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private final String matchRuleDef2MatchRuleDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/* 1072 */     StringBuffer localStringBuffer = new StringBuffer("( ");
/*      */ 
/* 1074 */     Attribute localAttribute = null;
/* 1075 */     int i = 0;
/*      */ 
/* 1079 */     localAttribute = paramAttributes.get("NUMERICOID");
/* 1080 */     if (localAttribute != null) {
/* 1081 */       localStringBuffer.append(writeNumericOID(localAttribute));
/* 1082 */       i++;
/*      */     } else {
/* 1084 */       throw new ConfigurationException("Attribute type doesn'thave a numeric OID");
/*      */     }
/*      */ 
/* 1088 */     localAttribute = paramAttributes.get("NAME");
/* 1089 */     if (localAttribute != null) {
/* 1090 */       localStringBuffer.append(writeQDescrs(localAttribute));
/* 1091 */       i++;
/*      */     }
/*      */ 
/* 1094 */     localAttribute = paramAttributes.get("DESC");
/* 1095 */     if (localAttribute != null) {
/* 1096 */       localStringBuffer.append(writeQDString(localAttribute));
/* 1097 */       i++;
/*      */     }
/*      */ 
/* 1100 */     localAttribute = paramAttributes.get("OBSOLETE");
/* 1101 */     if (localAttribute != null) {
/* 1102 */       localStringBuffer.append(writeBoolean(localAttribute));
/* 1103 */       i++;
/*      */     }
/*      */ 
/* 1106 */     localAttribute = paramAttributes.get("SYNTAX");
/* 1107 */     if (localAttribute != null) {
/* 1108 */       localStringBuffer.append(writeWOID(localAttribute));
/* 1109 */       i++;
/*      */     } else {
/* 1111 */       throw new ConfigurationException("Attribute type doesn'thave a syntax OID");
/*      */     }
/*      */ 
/* 1116 */     if (i < paramAttributes.size()) {
/* 1117 */       String str = null;
/*      */ 
/* 1120 */       NamingEnumeration localNamingEnumeration = paramAttributes.getAll();
/* 1121 */       while (localNamingEnumeration.hasMoreElements())
/*      */       {
/* 1123 */         localAttribute = (Attribute)localNamingEnumeration.next();
/* 1124 */         str = localAttribute.getID();
/*      */ 
/* 1127 */         if ((!str.equals("NUMERICOID")) && (!str.equals("NAME")) && (!str.equals("SYNTAX")) && (!str.equals("DESC")) && (!str.equals("OBSOLETE")))
/*      */         {
/* 1135 */           localStringBuffer.append(writeQDStrings(localAttribute));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1140 */     localStringBuffer.append(")");
/*      */ 
/* 1142 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private final String writeNumericOID(Attribute paramAttribute) throws NamingException
/*      */   {
/* 1147 */     if (paramAttribute.size() != 1) {
/* 1148 */       throw new InvalidAttributeValueException("A class definition must have exactly one numeric OID");
/*      */     }
/*      */ 
/* 1151 */     return (String)paramAttribute.get() + ' ';
/*      */   }
/*      */ 
/*      */   private final String writeWOID(Attribute paramAttribute) throws NamingException {
/* 1155 */     if (this.netscapeBug) {
/* 1156 */       return writeQDString(paramAttribute);
/*      */     }
/* 1158 */     return paramAttribute.getID() + ' ' + paramAttribute.get() + ' ';
/*      */   }
/*      */ 
/*      */   private final String writeQDString(Attribute paramAttribute)
/*      */     throws NamingException
/*      */   {
/* 1164 */     if (paramAttribute.size() != 1) {
/* 1165 */       throw new InvalidAttributeValueException(paramAttribute.getID() + " must have exactly one value");
/*      */     }
/*      */ 
/* 1169 */     return paramAttribute.getID() + ' ' + '\'' + paramAttribute.get() + '\'' + ' ';
/*      */   }
/*      */ 
/*      */   private final String writeQDStrings(Attribute paramAttribute)
/*      */     throws NamingException
/*      */   {
/* 1180 */     return writeQDescrs(paramAttribute);
/*      */   }
/*      */ 
/*      */   private final String writeQDescrs(Attribute paramAttribute)
/*      */     throws NamingException
/*      */   {
/* 1190 */     switch (paramAttribute.size()) {
/*      */     case 0:
/* 1192 */       throw new InvalidAttributeValueException(paramAttribute.getID() + "has no values");
/*      */     case 1:
/* 1195 */       return writeQDString(paramAttribute);
/*      */     }
/*      */ 
/* 1200 */     StringBuffer localStringBuffer = new StringBuffer(paramAttribute.getID());
/* 1201 */     localStringBuffer.append(' ');
/* 1202 */     localStringBuffer.append('(');
/*      */ 
/* 1204 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/*      */ 
/* 1206 */     while (localNamingEnumeration.hasMore()) {
/* 1207 */       localStringBuffer.append(' ');
/* 1208 */       localStringBuffer.append('\'');
/* 1209 */       localStringBuffer.append((String)localNamingEnumeration.next());
/* 1210 */       localStringBuffer.append('\'');
/* 1211 */       localStringBuffer.append(' ');
/*      */     }
/*      */ 
/* 1214 */     localStringBuffer.append(')');
/* 1215 */     localStringBuffer.append(' ');
/*      */ 
/* 1217 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private final String writeOIDs(Attribute paramAttribute)
/*      */     throws NamingException
/*      */   {
/* 1223 */     switch (paramAttribute.size()) {
/*      */     case 0:
/* 1225 */       throw new InvalidAttributeValueException(paramAttribute.getID() + "has no values");
/*      */     case 1:
/* 1229 */       if (!this.netscapeBug)
/*      */       {
/* 1232 */         return writeWOID(paramAttribute);
/*      */       }
/*      */       break;
/*      */     }
/*      */ 
/* 1237 */     StringBuffer localStringBuffer = new StringBuffer(paramAttribute.getID());
/* 1238 */     localStringBuffer.append(' ');
/* 1239 */     localStringBuffer.append('(');
/*      */ 
/* 1241 */     NamingEnumeration localNamingEnumeration = paramAttribute.getAll();
/* 1242 */     localStringBuffer.append(' ');
/* 1243 */     localStringBuffer.append(localNamingEnumeration.next());
/*      */ 
/* 1245 */     while (localNamingEnumeration.hasMore()) {
/* 1246 */       localStringBuffer.append(' ');
/* 1247 */       localStringBuffer.append('$');
/* 1248 */       localStringBuffer.append(' ');
/* 1249 */       localStringBuffer.append((String)localNamingEnumeration.next());
/*      */     }
/*      */ 
/* 1252 */     localStringBuffer.append(' ');
/* 1253 */     localStringBuffer.append(')');
/* 1254 */     localStringBuffer.append(' ');
/*      */ 
/* 1256 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private final String writeBoolean(Attribute paramAttribute) throws NamingException
/*      */   {
/* 1261 */     return paramAttribute.getID() + ' ';
/*      */   }
/*      */ 
/*      */   final Attribute stringifyObjDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/* 1270 */     BasicAttribute localBasicAttribute = new BasicAttribute("objectClasses");
/* 1271 */     localBasicAttribute.add(classDef2ObjectDesc(paramAttributes));
/* 1272 */     return localBasicAttribute;
/*      */   }
/*      */ 
/*      */   final Attribute stringifyAttrDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/* 1280 */     BasicAttribute localBasicAttribute = new BasicAttribute("attributeTypes");
/* 1281 */     localBasicAttribute.add(attrDef2AttrDesc(paramAttributes));
/* 1282 */     return localBasicAttribute;
/*      */   }
/*      */ 
/*      */   final Attribute stringifySyntaxDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/* 1290 */     BasicAttribute localBasicAttribute = new BasicAttribute("ldapSyntaxes");
/* 1291 */     localBasicAttribute.add(syntaxDef2SyntaxDesc(paramAttributes));
/* 1292 */     return localBasicAttribute;
/*      */   }
/*      */ 
/*      */   final Attribute stringifyMatchRuleDesc(Attributes paramAttributes)
/*      */     throws NamingException
/*      */   {
/* 1300 */     BasicAttribute localBasicAttribute = new BasicAttribute("matchingRules");
/* 1301 */     localBasicAttribute.add(matchRuleDef2MatchRuleDesc(paramAttributes));
/* 1302 */     return localBasicAttribute;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapSchemaParser
 * JD-Core Version:    0.6.2
 */
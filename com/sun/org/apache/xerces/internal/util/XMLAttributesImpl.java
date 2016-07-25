/*      */ package com.sun.org.apache.xerces.internal.util;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.xni.Augmentations;
/*      */ import com.sun.org.apache.xerces.internal.xni.QName;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
/*      */ import com.sun.org.apache.xerces.internal.xni.XMLString;
/*      */ import com.sun.xml.internal.stream.XMLBufferListener;
/*      */ 
/*      */ public class XMLAttributesImpl
/*      */   implements XMLAttributes, XMLBufferListener
/*      */ {
/*      */   protected static final int TABLE_SIZE = 101;
/*      */   protected static final int SIZE_LIMIT = 20;
/*  111 */   protected boolean fNamespaces = true;
/*      */ 
/*  120 */   protected int fLargeCount = 1;
/*      */   protected int fLength;
/*  126 */   protected Attribute[] fAttributes = new Attribute[4];
/*      */   protected Attribute[] fAttributeTableView;
/*      */   protected int[] fAttributeTableViewChainState;
/*      */   protected int fTableViewBuckets;
/*      */   protected boolean fIsTableViewConsistent;
/*      */ 
/*      */   public XMLAttributesImpl()
/*      */   {
/*  158 */     this(101);
/*      */   }
/*      */ 
/*      */   public XMLAttributesImpl(int tableSize)
/*      */   {
/*  165 */     this.fTableViewBuckets = tableSize;
/*  166 */     for (int i = 0; i < this.fAttributes.length; i++)
/*  167 */       this.fAttributes[i] = new Attribute();
/*      */   }
/*      */ 
/*      */   public void setNamespaces(boolean namespaces)
/*      */   {
/*  184 */     this.fNamespaces = namespaces;
/*      */   }
/*      */ 
/*      */   public int addAttribute(QName name, String type, String value)
/*      */   {
/*  220 */     return addAttribute(name, type, value, null);
/*      */   }
/*      */ 
/*      */   public int addAttribute(QName name, String type, String value, XMLString valueCache)
/*      */   {
/*      */     int index;
/*  225 */     if (this.fLength < 20) {
/*  226 */       int index = (name.uri != null) && (!name.uri.equals("")) ? getIndexFast(name.uri, name.localpart) : getIndexFast(name.rawname);
/*      */ 
/*  230 */       if (index == -1) {
/*  231 */         index = this.fLength;
/*  232 */         if (this.fLength++ == this.fAttributes.length) {
/*  233 */           Attribute[] attributes = new Attribute[this.fAttributes.length + 4];
/*  234 */           System.arraycopy(this.fAttributes, 0, attributes, 0, this.fAttributes.length);
/*  235 */           for (int i = this.fAttributes.length; i < attributes.length; i++) {
/*  236 */             attributes[i] = new Attribute();
/*      */           }
/*  238 */           this.fAttributes = attributes;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       int index;
/*  242 */       if ((name.uri == null) || (name.uri.length() == 0) || ((index = getIndexFast(name.uri, name.localpart)) == -1))
/*      */       {
/*  254 */         if ((!this.fIsTableViewConsistent) || (this.fLength == 20)) {
/*  255 */           prepareAndPopulateTableView();
/*  256 */           this.fIsTableViewConsistent = true;
/*      */         }
/*      */ 
/*  259 */         int bucket = getTableViewBucket(name.rawname);
/*      */ 
/*  263 */         if (this.fAttributeTableViewChainState[bucket] != this.fLargeCount) {
/*  264 */           int index = this.fLength;
/*  265 */           if (this.fLength++ == this.fAttributes.length) {
/*  266 */             Attribute[] attributes = new Attribute[this.fAttributes.length << 1];
/*  267 */             System.arraycopy(this.fAttributes, 0, attributes, 0, this.fAttributes.length);
/*  268 */             for (int i = this.fAttributes.length; i < attributes.length; i++) {
/*  269 */               attributes[i] = new Attribute();
/*      */             }
/*  271 */             this.fAttributes = attributes;
/*      */           }
/*      */ 
/*  275 */           this.fAttributeTableViewChainState[bucket] = this.fLargeCount;
/*  276 */           this.fAttributes[index].next = null;
/*  277 */           this.fAttributeTableView[bucket] = this.fAttributes[index];
/*      */         }
/*      */         else
/*      */         {
/*  283 */           Attribute found = this.fAttributeTableView[bucket];
/*  284 */           while ((found != null) && 
/*  285 */             (found.name.rawname != name.rawname))
/*      */           {
/*  288 */             found = found.next;
/*      */           }
/*      */ 
/*  291 */           if (found == null) {
/*  292 */             int index = this.fLength;
/*  293 */             if (this.fLength++ == this.fAttributes.length) {
/*  294 */               Attribute[] attributes = new Attribute[this.fAttributes.length << 1];
/*  295 */               System.arraycopy(this.fAttributes, 0, attributes, 0, this.fAttributes.length);
/*  296 */               for (int i = this.fAttributes.length; i < attributes.length; i++) {
/*  297 */                 attributes[i] = new Attribute();
/*      */               }
/*  299 */               this.fAttributes = attributes;
/*      */             }
/*      */ 
/*  303 */             this.fAttributes[index].next = this.fAttributeTableView[bucket];
/*  304 */             this.fAttributeTableView[bucket] = this.fAttributes[index];
/*      */           }
/*      */           else
/*      */           {
/*  308 */             index = getIndexFast(name.rawname);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  314 */     Attribute attribute = this.fAttributes[index];
/*  315 */     attribute.name.setValues(name);
/*  316 */     attribute.type = type;
/*  317 */     attribute.value = value;
/*  318 */     attribute.xmlValue = valueCache;
/*  319 */     attribute.nonNormalizedValue = value;
/*  320 */     attribute.specified = false;
/*      */ 
/*  323 */     if (attribute.augs != null) {
/*  324 */       attribute.augs.removeAllItems();
/*      */     }
/*  326 */     return index;
/*      */   }
/*      */ 
/*      */   public void removeAllAttributes()
/*      */   {
/*  335 */     this.fLength = 0;
/*      */   }
/*      */ 
/*      */   public void removeAttributeAt(int attrIndex)
/*      */   {
/*  347 */     this.fIsTableViewConsistent = false;
/*  348 */     if (attrIndex < this.fLength - 1) {
/*  349 */       Attribute removedAttr = this.fAttributes[attrIndex];
/*  350 */       System.arraycopy(this.fAttributes, attrIndex + 1, this.fAttributes, attrIndex, this.fLength - attrIndex - 1);
/*      */ 
/*  354 */       this.fAttributes[(this.fLength - 1)] = removedAttr;
/*      */     }
/*  356 */     this.fLength -= 1;
/*      */   }
/*      */ 
/*      */   public void setName(int attrIndex, QName attrName)
/*      */   {
/*  366 */     this.fAttributes[attrIndex].name.setValues(attrName);
/*      */   }
/*      */ 
/*      */   public void getName(int attrIndex, QName attrName)
/*      */   {
/*  377 */     attrName.setValues(this.fAttributes[attrIndex].name);
/*      */   }
/*      */ 
/*      */   public void setType(int attrIndex, String attrType)
/*      */   {
/*  394 */     this.fAttributes[attrIndex].type = attrType;
/*      */   }
/*      */ 
/*      */   public void setValue(int attrIndex, String attrValue)
/*      */   {
/*  407 */     setValue(attrIndex, attrValue, null);
/*      */   }
/*      */ 
/*      */   public void setValue(int attrIndex, String attrValue, XMLString value) {
/*  411 */     Attribute attribute = this.fAttributes[attrIndex];
/*  412 */     attribute.value = attrValue;
/*  413 */     attribute.nonNormalizedValue = attrValue;
/*  414 */     attribute.xmlValue = value;
/*      */   }
/*      */ 
/*      */   public void setNonNormalizedValue(int attrIndex, String attrValue)
/*      */   {
/*  425 */     if (attrValue == null) {
/*  426 */       attrValue = this.fAttributes[attrIndex].value;
/*      */     }
/*  428 */     this.fAttributes[attrIndex].nonNormalizedValue = attrValue;
/*      */   }
/*      */ 
/*      */   public String getNonNormalizedValue(int attrIndex)
/*      */   {
/*  439 */     String value = this.fAttributes[attrIndex].nonNormalizedValue;
/*  440 */     return value;
/*      */   }
/*      */ 
/*      */   public void setSpecified(int attrIndex, boolean specified)
/*      */   {
/*  452 */     this.fAttributes[attrIndex].specified = specified;
/*      */   }
/*      */ 
/*      */   public boolean isSpecified(int attrIndex)
/*      */   {
/*  461 */     return this.fAttributes[attrIndex].specified;
/*      */   }
/*      */ 
/*      */   public int getLength()
/*      */   {
/*  477 */     return this.fLength;
/*      */   }
/*      */ 
/*      */   public String getType(int index)
/*      */   {
/*  501 */     if ((index < 0) || (index >= this.fLength)) {
/*  502 */       return null;
/*      */     }
/*  504 */     return getReportableType(this.fAttributes[index].type);
/*      */   }
/*      */ 
/*      */   public String getType(String qname)
/*      */   {
/*  519 */     int index = getIndex(qname);
/*  520 */     return index != -1 ? getReportableType(this.fAttributes[index].type) : null;
/*      */   }
/*      */ 
/*      */   public String getValue(int index)
/*      */   {
/*  537 */     if ((index < 0) || (index >= this.fLength)) {
/*  538 */       return null;
/*      */     }
/*  540 */     if ((this.fAttributes[index].value == null) && (this.fAttributes[index].xmlValue != null))
/*  541 */       this.fAttributes[index].value = this.fAttributes[index].xmlValue.toString();
/*  542 */     return this.fAttributes[index].value;
/*      */   }
/*      */ 
/*      */   public String getValue(String qname)
/*      */   {
/*  557 */     int index = getIndex(qname);
/*  558 */     if (index == -1)
/*  559 */       return null;
/*  560 */     if (this.fAttributes[index].value == null)
/*  561 */       this.fAttributes[index].value = this.fAttributes[index].xmlValue.toString();
/*  562 */     return this.fAttributes[index].value;
/*      */   }
/*      */ 
/*      */   public String getName(int index)
/*      */   {
/*  586 */     if ((index < 0) || (index >= this.fLength)) {
/*  587 */       return null;
/*      */     }
/*  589 */     return this.fAttributes[index].name.rawname;
/*      */   }
/*      */ 
/*      */   public int getIndex(String qName)
/*      */   {
/*  604 */     for (int i = 0; i < this.fLength; i++) {
/*  605 */       Attribute attribute = this.fAttributes[i];
/*  606 */       if ((attribute.name.rawname != null) && (attribute.name.rawname.equals(qName)))
/*      */       {
/*  608 */         return i;
/*      */       }
/*      */     }
/*  611 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getIndex(String uri, String localPart)
/*      */   {
/*  624 */     for (int i = 0; i < this.fLength; i++) {
/*  625 */       Attribute attribute = this.fAttributes[i];
/*  626 */       if ((attribute.name.localpart != null) && (attribute.name.localpart.equals(localPart)) && ((uri == attribute.name.uri) || ((uri != null) && (attribute.name.uri != null) && (attribute.name.uri.equals(uri)))))
/*      */       {
/*  630 */         return i;
/*      */       }
/*      */     }
/*  633 */     return -1;
/*      */   }
/*      */ 
/*      */   public int getIndexByLocalName(String localPart)
/*      */   {
/*  645 */     for (int i = 0; i < this.fLength; i++) {
/*  646 */       Attribute attribute = this.fAttributes[i];
/*  647 */       if ((attribute.name.localpart != null) && (attribute.name.localpart.equals(localPart)))
/*      */       {
/*  649 */         return i;
/*      */       }
/*      */     }
/*  652 */     return -1;
/*      */   }
/*      */ 
/*      */   public String getLocalName(int index)
/*      */   {
/*  665 */     if (!this.fNamespaces) {
/*  666 */       return "";
/*      */     }
/*  668 */     if ((index < 0) || (index >= this.fLength)) {
/*  669 */       return null;
/*      */     }
/*  671 */     return this.fAttributes[index].name.localpart;
/*      */   }
/*      */ 
/*      */   public String getQName(int index)
/*      */   {
/*  684 */     if ((index < 0) || (index >= this.fLength)) {
/*  685 */       return null;
/*      */     }
/*  687 */     String rawname = this.fAttributes[index].name.rawname;
/*  688 */     return rawname != null ? rawname : "";
/*      */   }
/*      */ 
/*      */   public QName getQualifiedName(int index) {
/*  692 */     if ((index < 0) || (index >= this.fLength)) {
/*  693 */       return null;
/*      */     }
/*  695 */     return this.fAttributes[index].name;
/*      */   }
/*      */ 
/*      */   public String getType(String uri, String localName)
/*      */   {
/*  712 */     if (!this.fNamespaces) {
/*  713 */       return null;
/*      */     }
/*  715 */     int index = getIndex(uri, localName);
/*  716 */     return index != -1 ? getType(index) : null;
/*      */   }
/*      */ 
/*      */   public int getIndexFast(String qName)
/*      */   {
/*  731 */     for (int i = 0; i < this.fLength; i++) {
/*  732 */       Attribute attribute = this.fAttributes[i];
/*  733 */       if (attribute.name.rawname == qName) {
/*  734 */         return i;
/*      */       }
/*      */     }
/*  737 */     return -1;
/*      */   }
/*      */ 
/*      */   public void addAttributeNS(QName name, String type, String value)
/*      */   {
/*  768 */     int index = this.fLength;
/*  769 */     if (this.fLength++ == this.fAttributes.length)
/*      */     {
/*      */       Attribute[] attributes;
/*      */       Attribute[] attributes;
/*  771 */       if (this.fLength < 20) {
/*  772 */         attributes = new Attribute[this.fAttributes.length + 4];
/*      */       }
/*      */       else {
/*  775 */         attributes = new Attribute[this.fAttributes.length << 1];
/*      */       }
/*  777 */       System.arraycopy(this.fAttributes, 0, attributes, 0, this.fAttributes.length);
/*  778 */       for (int i = this.fAttributes.length; i < attributes.length; i++) {
/*  779 */         attributes[i] = new Attribute();
/*      */       }
/*  781 */       this.fAttributes = attributes;
/*      */     }
/*      */ 
/*  785 */     Attribute attribute = this.fAttributes[index];
/*  786 */     attribute.name.setValues(name);
/*  787 */     attribute.type = type;
/*  788 */     attribute.value = value;
/*  789 */     attribute.nonNormalizedValue = value;
/*  790 */     attribute.specified = false;
/*      */ 
/*  793 */     attribute.augs.removeAllItems();
/*      */   }
/*      */ 
/*      */   public QName checkDuplicatesNS()
/*      */   {
/*  810 */     if (this.fLength <= 20) {
/*  811 */       for (int i = 0; i < this.fLength - 1; i++) {
/*  812 */         Attribute att1 = this.fAttributes[i];
/*  813 */         for (int j = i + 1; j < this.fLength; j++) {
/*  814 */           Attribute att2 = this.fAttributes[j];
/*  815 */           if ((att1.name.localpart == att2.name.localpart) && (att1.name.uri == att2.name.uri))
/*      */           {
/*  817 */             return att2.name;
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  826 */       this.fIsTableViewConsistent = false;
/*      */ 
/*  828 */       prepareTableView();
/*      */ 
/*  833 */       for (int i = this.fLength - 1; i >= 0; i--) {
/*  834 */         Attribute attr = this.fAttributes[i];
/*  835 */         int bucket = getTableViewBucket(attr.name.localpart, attr.name.uri);
/*      */ 
/*  839 */         if (this.fAttributeTableViewChainState[bucket] != this.fLargeCount) {
/*  840 */           this.fAttributeTableViewChainState[bucket] = this.fLargeCount;
/*  841 */           attr.next = null;
/*  842 */           this.fAttributeTableView[bucket] = attr;
/*      */         }
/*      */         else
/*      */         {
/*  848 */           Attribute found = this.fAttributeTableView[bucket];
/*  849 */           while (found != null) {
/*  850 */             if ((found.name.localpart == attr.name.localpart) && (found.name.uri == attr.name.uri))
/*      */             {
/*  852 */               return attr.name;
/*      */             }
/*  854 */             found = found.next;
/*      */           }
/*      */ 
/*  858 */           attr.next = this.fAttributeTableView[bucket];
/*  859 */           this.fAttributeTableView[bucket] = attr;
/*      */         }
/*      */       }
/*      */     }
/*  863 */     return null;
/*      */   }
/*      */ 
/*      */   public int getIndexFast(String uri, String localPart)
/*      */   {
/*  881 */     for (int i = 0; i < this.fLength; i++) {
/*  882 */       Attribute attribute = this.fAttributes[i];
/*  883 */       if ((attribute.name.localpart == localPart) && (attribute.name.uri == uri))
/*      */       {
/*  885 */         return i;
/*      */       }
/*      */     }
/*  888 */     return -1;
/*      */   }
/*      */ 
/*      */   private String getReportableType(String type)
/*      */   {
/*  899 */     if (type.charAt(0) == '(') {
/*  900 */       return "NMTOKEN";
/*      */     }
/*  902 */     return type;
/*      */   }
/*      */ 
/*      */   protected int getTableViewBucket(String qname)
/*      */   {
/*  914 */     return (qname.hashCode() & 0x7FFFFFFF) % this.fTableViewBuckets;
/*      */   }
/*      */ 
/*      */   protected int getTableViewBucket(String localpart, String uri)
/*      */   {
/*  927 */     if (uri == null) {
/*  928 */       return (localpart.hashCode() & 0x7FFFFFFF) % this.fTableViewBuckets;
/*      */     }
/*      */ 
/*  931 */     return (localpart.hashCode() + uri.hashCode() & 0x7FFFFFFF) % this.fTableViewBuckets;
/*      */   }
/*      */ 
/*      */   protected void cleanTableView()
/*      */   {
/*  940 */     if (++this.fLargeCount < 0)
/*      */     {
/*  942 */       if (this.fAttributeTableViewChainState != null) {
/*  943 */         for (int i = this.fTableViewBuckets - 1; i >= 0; i--) {
/*  944 */           this.fAttributeTableViewChainState[i] = 0;
/*      */         }
/*      */       }
/*  947 */       this.fLargeCount = 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void prepareTableView()
/*      */   {
/*  955 */     if (this.fAttributeTableView == null) {
/*  956 */       this.fAttributeTableView = new Attribute[this.fTableViewBuckets];
/*  957 */       this.fAttributeTableViewChainState = new int[this.fTableViewBuckets];
/*      */     }
/*      */     else {
/*  960 */       cleanTableView();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void prepareAndPopulateTableView()
/*      */   {
/*  970 */     prepareTableView();
/*      */ 
/*  974 */     for (int i = 0; i < this.fLength; i++) {
/*  975 */       Attribute attr = this.fAttributes[i];
/*  976 */       int bucket = getTableViewBucket(attr.name.rawname);
/*  977 */       if (this.fAttributeTableViewChainState[bucket] != this.fLargeCount) {
/*  978 */         this.fAttributeTableViewChainState[bucket] = this.fLargeCount;
/*  979 */         attr.next = null;
/*  980 */         this.fAttributeTableView[bucket] = attr;
/*      */       }
/*      */       else
/*      */       {
/*  984 */         attr.next = this.fAttributeTableView[bucket];
/*  985 */         this.fAttributeTableView[bucket] = attr;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String getPrefix(int index)
/*      */   {
/*  997 */     if ((index < 0) || (index >= this.fLength)) {
/*  998 */       return null;
/*      */     }
/* 1000 */     String prefix = this.fAttributes[index].name.prefix;
/*      */ 
/* 1002 */     return prefix != null ? prefix : "";
/*      */   }
/*      */ 
/*      */   public String getURI(int index)
/*      */   {
/* 1013 */     if ((index < 0) || (index >= this.fLength)) {
/* 1014 */       return null;
/*      */     }
/* 1016 */     String uri = this.fAttributes[index].name.uri;
/* 1017 */     return uri;
/*      */   }
/*      */ 
/*      */   public String getValue(String uri, String localName)
/*      */   {
/* 1035 */     int index = getIndex(uri, localName);
/* 1036 */     return index != -1 ? getValue(index) : null;
/*      */   }
/*      */ 
/*      */   public Augmentations getAugmentations(String uri, String localName)
/*      */   {
/* 1047 */     int index = getIndex(uri, localName);
/* 1048 */     return index != -1 ? this.fAttributes[index].augs : null;
/*      */   }
/*      */ 
/*      */   public Augmentations getAugmentations(String qName)
/*      */   {
/* 1061 */     int index = getIndex(qName);
/* 1062 */     return index != -1 ? this.fAttributes[index].augs : null;
/*      */   }
/*      */ 
/*      */   public Augmentations getAugmentations(int attributeIndex)
/*      */   {
/* 1074 */     if ((attributeIndex < 0) || (attributeIndex >= this.fLength)) {
/* 1075 */       return null;
/*      */     }
/* 1077 */     return this.fAttributes[attributeIndex].augs;
/*      */   }
/*      */ 
/*      */   public void setAugmentations(int attrIndex, Augmentations augs)
/*      */   {
/* 1087 */     this.fAttributes[attrIndex].augs = augs;
/*      */   }
/*      */ 
/*      */   public void setURI(int attrIndex, String uri)
/*      */   {
/* 1097 */     this.fAttributes[attrIndex].name.uri = uri;
/*      */   }
/*      */ 
/*      */   public void setSchemaId(int attrIndex, boolean schemaId)
/*      */   {
/* 1102 */     this.fAttributes[attrIndex].schemaId = schemaId;
/*      */   }
/*      */ 
/*      */   public boolean getSchemaId(int index) {
/* 1106 */     if ((index < 0) || (index >= this.fLength)) {
/* 1107 */       return false;
/*      */     }
/* 1109 */     return this.fAttributes[index].schemaId;
/*      */   }
/*      */ 
/*      */   public boolean getSchemaId(String qname) {
/* 1113 */     int index = getIndex(qname);
/* 1114 */     return index != -1 ? this.fAttributes[index].schemaId : false;
/*      */   }
/*      */ 
/*      */   public boolean getSchemaId(String uri, String localName) {
/* 1118 */     if (!this.fNamespaces) {
/* 1119 */       return false;
/*      */     }
/* 1121 */     int index = getIndex(uri, localName);
/* 1122 */     return index != -1 ? this.fAttributes[index].schemaId : false;
/*      */   }
/*      */ 
/*      */   public void refresh()
/*      */   {
/* 1131 */     if (this.fLength > 0)
/* 1132 */       for (int i = 0; i < this.fLength; i++)
/* 1133 */         getValue(i);
/*      */   }
/*      */ 
/*      */   public void refresh(int pos)
/*      */   {
/*      */   }
/*      */ 
/*      */   static class Attribute
/*      */   {
/* 1158 */     public QName name = new QName();
/*      */     public String type;
/*      */     public String value;
/*      */     public XMLString xmlValue;
/*      */     public String nonNormalizedValue;
/*      */     public boolean specified;
/*      */     public boolean schemaId;
/* 1183 */     public Augmentations augs = new AugmentationsImpl();
/*      */     public Attribute next;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.XMLAttributesImpl
 * JD-Core Version:    0.6.2
 */
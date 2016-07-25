/*     */ package com.sun.org.apache.xalan.internal.xsltc.dom;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.utils.ObjectFactory;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.CollatorFactory;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.DOM;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.TransletException;
/*     */ import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
/*     */ import com.sun.org.apache.xml.internal.utils.StringComparable;
/*     */ import java.text.Collator;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public abstract class NodeSortRecord
/*     */ {
/*     */   public static final int COMPARE_STRING = 0;
/*     */   public static final int COMPARE_NUMERIC = 1;
/*     */   public static final int COMPARE_ASCENDING = 0;
/*     */   public static final int COMPARE_DESCENDING = 1;
/*     */ 
/*     */   /** @deprecated */
/*  54 */   private static final Collator DEFAULT_COLLATOR = Collator.getInstance();
/*     */ 
/*     */   /** @deprecated */
/*  61 */   protected Collator _collator = DEFAULT_COLLATOR;
/*     */   protected Collator[] _collators;
/*     */ 
/*     */   /** @deprecated */
/*     */   protected Locale _locale;
/*     */   protected CollatorFactory _collatorFactory;
/*     */   protected SortSettings _settings;
/*  75 */   private DOM _dom = null;
/*     */   private int _node;
/*  77 */   private int _last = 0;
/*  78 */   private int _scanned = 0;
/*     */   private Object[] _values;
/*     */ 
/*     */   public NodeSortRecord(int node)
/*     */   {
/*  89 */     this._node = node;
/*     */   }
/*     */ 
/*     */   public NodeSortRecord() {
/*  93 */     this(0);
/*     */   }
/*     */ 
/*     */   public final void initialize(int node, int last, DOM dom, SortSettings settings)
/*     */     throws TransletException
/*     */   {
/* 104 */     this._dom = dom;
/* 105 */     this._node = node;
/* 106 */     this._last = last;
/* 107 */     this._settings = settings;
/*     */ 
/* 109 */     int levels = settings.getSortOrders().length;
/* 110 */     this._values = new Object[levels];
/*     */ 
/* 112 */     String colFactClassname = null;
/*     */     try
/*     */     {
/* 115 */       colFactClassname = SecuritySupport.getSystemProperty("com.sun.org.apache.xalan.internal.xsltc.COLLATOR_FACTORY");
/*     */     }
/*     */     catch (SecurityException e)
/*     */     {
/*     */     }
/*     */ 
/* 122 */     if (colFactClassname != null) {
/*     */       try {
/* 124 */         Object candObj = ObjectFactory.findProviderClass(colFactClassname, true);
/* 125 */         this._collatorFactory = ((CollatorFactory)candObj);
/*     */       } catch (ClassNotFoundException e) {
/* 127 */         throw new TransletException(e);
/*     */       }
/* 129 */       Locale[] locales = settings.getLocales();
/* 130 */       this._collators = new Collator[levels];
/* 131 */       for (int i = 0; i < levels; i++) {
/* 132 */         this._collators[i] = this._collatorFactory.getCollator(locales[i]);
/*     */       }
/* 134 */       this._collator = this._collators[0];
/*     */     } else {
/* 136 */       this._collators = settings.getCollators();
/* 137 */       this._collator = this._collators[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getNode()
/*     */   {
/* 145 */     return this._node;
/*     */   }
/*     */ 
/*     */   public final int compareDocOrder(NodeSortRecord other)
/*     */   {
/* 152 */     return this._node - other._node;
/*     */   }
/*     */ 
/*     */   private final Comparable stringValue(int level)
/*     */   {
/* 162 */     if (this._scanned <= level) {
/* 163 */       AbstractTranslet translet = this._settings.getTranslet();
/* 164 */       Locale[] locales = this._settings.getLocales();
/* 165 */       String[] caseOrder = this._settings.getCaseOrders();
/*     */ 
/* 168 */       String str = extractValueFromDOM(this._dom, this._node, level, translet, this._last);
/*     */ 
/* 170 */       Comparable key = StringComparable.getComparator(str, locales[level], this._collators[level], caseOrder[level]);
/*     */ 
/* 174 */       this._values[(this._scanned++)] = key;
/* 175 */       return key;
/*     */     }
/* 177 */     return (Comparable)this._values[level];
/*     */   }
/*     */ 
/*     */   private final Double numericValue(int level)
/*     */   {
/* 182 */     if (this._scanned <= level) {
/* 183 */       AbstractTranslet translet = this._settings.getTranslet();
/*     */ 
/* 186 */       String str = extractValueFromDOM(this._dom, this._node, level, translet, this._last);
/*     */       Double num;
/*     */       try {
/* 190 */         num = new Double(str);
/*     */       }
/*     */       catch (NumberFormatException e)
/*     */       {
/* 194 */         num = new Double((-1.0D / 0.0D));
/*     */       }
/* 196 */       this._values[(this._scanned++)] = num;
/* 197 */       return num;
/*     */     }
/* 199 */     return (Double)this._values[level];
/*     */   }
/*     */ 
/*     */   public int compareTo(NodeSortRecord other)
/*     */   {
/* 211 */     int[] sortOrder = this._settings.getSortOrders();
/* 212 */     int levels = this._settings.getSortOrders().length;
/* 213 */     int[] compareTypes = this._settings.getTypes();
/*     */ 
/* 215 */     for (int level = 0; level < levels; level++)
/*     */     {
/*     */       int cmp;
/*     */       int cmp;
/* 217 */       if (compareTypes[level] == 1) {
/* 218 */         Double our = numericValue(level);
/* 219 */         Double their = other.numericValue(level);
/* 220 */         cmp = our.compareTo(their);
/*     */       }
/*     */       else {
/* 223 */         Comparable our = stringValue(level);
/* 224 */         Comparable their = other.stringValue(level);
/* 225 */         cmp = our.compareTo(their);
/*     */       }
/*     */ 
/* 229 */       if (cmp != 0) {
/* 230 */         return sortOrder[level] == 1 ? 0 - cmp : cmp;
/*     */       }
/*     */     }
/*     */ 
/* 234 */     return this._node - other._node;
/*     */   }
/*     */ 
/*     */   public Collator[] getCollator()
/*     */   {
/* 242 */     return this._collators;
/*     */   }
/*     */ 
/*     */   public abstract String extractValueFromDOM(DOM paramDOM, int paramInt1, int paramInt2, AbstractTranslet paramAbstractTranslet, int paramInt3);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.dom.NodeSortRecord
 * JD-Core Version:    0.6.2
 */
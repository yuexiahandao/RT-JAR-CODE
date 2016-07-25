/*     */ package com.sun.org.apache.xml.internal.dtm.ref.sax2dtm;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMManager;
/*     */ import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
/*     */ import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
/*     */ import com.sun.org.apache.xml.internal.utils.IntStack;
/*     */ import com.sun.org.apache.xml.internal.utils.IntVector;
/*     */ import com.sun.org.apache.xml.internal.utils.StringVector;
/*     */ import com.sun.org.apache.xml.internal.utils.SuballocatedIntVector;
/*     */ import com.sun.org.apache.xml.internal.utils.XMLStringFactory;
/*     */ import java.util.Vector;
/*     */ import javax.xml.transform.Source;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class SAX2RTFDTM extends SAX2DTM
/*     */ {
/*     */   private static final boolean DEBUG = false;
/*  70 */   private int m_currentDocumentNode = -1;
/*     */ 
/*  73 */   IntStack mark_size = new IntStack();
/*     */ 
/*  75 */   IntStack mark_data_size = new IntStack();
/*     */ 
/*  77 */   IntStack mark_char_size = new IntStack();
/*     */ 
/*  79 */   IntStack mark_doq_size = new IntStack();
/*     */ 
/*  85 */   IntStack mark_nsdeclset_size = new IntStack();
/*     */ 
/*  91 */   IntStack mark_nsdeclelem_size = new IntStack();
/*     */   int m_emptyNodeCount;
/*     */   int m_emptyNSDeclSetCount;
/*     */   int m_emptyNSDeclSetElemsCount;
/*     */   int m_emptyDataCount;
/*     */   int m_emptyCharsCount;
/*     */   int m_emptyDataQNCount;
/*     */ 
/*     */   public SAX2RTFDTM(DTMManager mgr, Source source, int dtmIdentity, DTMWSFilter whiteSpaceFilter, XMLStringFactory xstringfactory, boolean doIndexing)
/*     */   {
/* 128 */     super(mgr, source, dtmIdentity, whiteSpaceFilter, xstringfactory, doIndexing);
/*     */ 
/* 134 */     this.m_useSourceLocationProperty = false;
/* 135 */     this.m_sourceSystemId = (this.m_useSourceLocationProperty ? new StringVector() : null);
/*     */ 
/* 137 */     this.m_sourceLine = (this.m_useSourceLocationProperty ? new IntVector() : null);
/* 138 */     this.m_sourceColumn = (this.m_useSourceLocationProperty ? new IntVector() : null);
/*     */ 
/* 143 */     this.m_emptyNodeCount = this.m_size;
/* 144 */     this.m_emptyNSDeclSetCount = (this.m_namespaceDeclSets == null ? 0 : this.m_namespaceDeclSets.size());
/*     */ 
/* 146 */     this.m_emptyNSDeclSetElemsCount = (this.m_namespaceDeclSetElements == null ? 0 : this.m_namespaceDeclSetElements.size());
/*     */ 
/* 148 */     this.m_emptyDataCount = this.m_data.size();
/* 149 */     this.m_emptyCharsCount = this.m_chars.size();
/* 150 */     this.m_emptyDataQNCount = this.m_dataOrQName.size();
/*     */   }
/*     */ 
/*     */   public int getDocument()
/*     */   {
/* 168 */     return makeNodeHandle(this.m_currentDocumentNode);
/*     */   }
/*     */ 
/*     */   public int getDocumentRoot(int nodeHandle)
/*     */   {
/* 184 */     for (int id = makeNodeIdentity(nodeHandle); id != -1; id = _parent(id)) {
/* 185 */       if (_type(id) == 9) {
/* 186 */         return makeNodeHandle(id);
/*     */       }
/*     */     }
/*     */ 
/* 190 */     return -1;
/*     */   }
/*     */ 
/*     */   protected int _documentRoot(int nodeIdentifier)
/*     */   {
/* 203 */     if (nodeIdentifier == -1) return -1;
/*     */ 
/* 205 */     for (int parent = _parent(nodeIdentifier); 
/* 206 */       parent != -1; 
/* 207 */       parent = _parent(nodeIdentifier)) nodeIdentifier = parent;
/*     */ 
/* 210 */     return nodeIdentifier;
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */     throws SAXException
/*     */   {
/* 228 */     this.m_endDocumentOccured = false;
/* 229 */     this.m_prefixMappings = new Vector();
/* 230 */     this.m_contextIndexes = new IntStack();
/* 231 */     this.m_parents = new IntStack();
/*     */ 
/* 233 */     this.m_currentDocumentNode = this.m_size;
/* 234 */     super.startDocument();
/*     */   }
/*     */ 
/*     */   public void endDocument()
/*     */     throws SAXException
/*     */   {
/* 251 */     charactersFlush();
/*     */ 
/* 253 */     this.m_nextsib.setElementAt(-1, this.m_currentDocumentNode);
/*     */ 
/* 255 */     if (this.m_firstch.elementAt(this.m_currentDocumentNode) == -2) {
/* 256 */       this.m_firstch.setElementAt(-1, this.m_currentDocumentNode);
/*     */     }
/* 258 */     if (-1 != this.m_previous) {
/* 259 */       this.m_nextsib.setElementAt(-1, this.m_previous);
/*     */     }
/* 261 */     this.m_parents = null;
/* 262 */     this.m_prefixMappings = null;
/* 263 */     this.m_contextIndexes = null;
/*     */ 
/* 265 */     this.m_currentDocumentNode = -1;
/* 266 */     this.m_endDocumentOccured = true;
/*     */   }
/*     */ 
/*     */   public void pushRewindMark()
/*     */   {
/* 282 */     if ((this.m_indexing) || (this.m_elemIndexes != null)) {
/* 283 */       throw new NullPointerException("Coding error; Don't try to mark/rewind an indexed DTM");
/*     */     }
/*     */ 
/* 287 */     this.mark_size.push(this.m_size);
/* 288 */     this.mark_nsdeclset_size.push(this.m_namespaceDeclSets == null ? 0 : this.m_namespaceDeclSets.size());
/*     */ 
/* 291 */     this.mark_nsdeclelem_size.push(this.m_namespaceDeclSetElements == null ? 0 : this.m_namespaceDeclSetElements.size());
/*     */ 
/* 296 */     this.mark_data_size.push(this.m_data.size());
/* 297 */     this.mark_char_size.push(this.m_chars.size());
/* 298 */     this.mark_doq_size.push(this.m_dataOrQName.size());
/*     */   }
/*     */ 
/*     */   public boolean popRewindMark()
/*     */   {
/* 328 */     boolean top = this.mark_size.empty();
/*     */ 
/* 330 */     this.m_size = (top ? this.m_emptyNodeCount : this.mark_size.pop());
/* 331 */     this.m_exptype.setSize(this.m_size);
/* 332 */     this.m_firstch.setSize(this.m_size);
/* 333 */     this.m_nextsib.setSize(this.m_size);
/* 334 */     this.m_prevsib.setSize(this.m_size);
/* 335 */     this.m_parent.setSize(this.m_size);
/*     */ 
/* 337 */     this.m_elemIndexes = ((int[][][])null);
/*     */ 
/* 339 */     int ds = top ? this.m_emptyNSDeclSetCount : this.mark_nsdeclset_size.pop();
/* 340 */     if (this.m_namespaceDeclSets != null) {
/* 341 */       this.m_namespaceDeclSets.setSize(ds);
/*     */     }
/*     */ 
/* 344 */     int ds1 = top ? this.m_emptyNSDeclSetElemsCount : this.mark_nsdeclelem_size.pop();
/* 345 */     if (this.m_namespaceDeclSetElements != null) {
/* 346 */       this.m_namespaceDeclSetElements.setSize(ds1);
/*     */     }
/*     */ 
/* 350 */     this.m_data.setSize(top ? this.m_emptyDataCount : this.mark_data_size.pop());
/* 351 */     this.m_chars.setLength(top ? this.m_emptyCharsCount : this.mark_char_size.pop());
/* 352 */     this.m_dataOrQName.setSize(top ? this.m_emptyDataQNCount : this.mark_doq_size.pop());
/*     */ 
/* 355 */     return this.m_size == 0;
/*     */   }
/*     */ 
/*     */   public boolean isTreeIncomplete()
/*     */   {
/* 362 */     return !this.m_endDocumentOccured;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2RTFDTM
 * JD-Core Version:    0.6.2
 */
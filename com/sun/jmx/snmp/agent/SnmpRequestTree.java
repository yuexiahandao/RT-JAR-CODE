/*      */ package com.sun.jmx.snmp.agent;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.snmp.SnmpEngine;
/*      */ import com.sun.jmx.snmp.SnmpOid;
/*      */ import com.sun.jmx.snmp.SnmpPdu;
/*      */ import com.sun.jmx.snmp.SnmpStatusException;
/*      */ import com.sun.jmx.snmp.SnmpVarBind;
/*      */ import java.util.Arrays;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ final class SnmpRequestTree
/*      */ {
/* 1059 */   private Hashtable<Object, Handler> hashtable = null;
/*      */ 
/* 1061 */   private SnmpMibRequest request = null;
/* 1062 */   private int version = 0;
/* 1063 */   private boolean creationflag = false;
/*      */ 
/* 1065 */   private boolean getnextflag = false;
/*      */ 
/* 1067 */   private int type = 0;
/*      */ 
/* 1069 */   private boolean setreqflag = false;
/*      */ 
/*      */   SnmpRequestTree(SnmpMibRequest paramSnmpMibRequest, boolean paramBoolean, int paramInt)
/*      */   {
/*   77 */     this.request = paramSnmpMibRequest;
/*   78 */     this.version = paramSnmpMibRequest.getVersion();
/*   79 */     this.creationflag = paramBoolean;
/*   80 */     this.hashtable = new Hashtable();
/*   81 */     setPduType(paramInt);
/*      */   }
/*      */ 
/*      */   public static int mapSetException(int paramInt1, int paramInt2)
/*      */     throws SnmpStatusException
/*      */   {
/*   87 */     int i = paramInt1;
/*      */ 
/*   89 */     if (paramInt2 == 0) {
/*   90 */       return i;
/*      */     }
/*   92 */     int j = i;
/*      */ 
/*   96 */     if (i == 225)
/*      */     {
/*   98 */       j = 17;
/*      */     }
/*  100 */     else if (i == 224)
/*      */     {
/*  102 */       j = 17;
/*      */     }
/*  104 */     return j;
/*      */   }
/*      */ 
/*      */   public static int mapGetException(int paramInt1, int paramInt2)
/*      */     throws SnmpStatusException
/*      */   {
/*  110 */     int i = paramInt1;
/*  111 */     if (paramInt2 == 0) {
/*  112 */       return i;
/*      */     }
/*  114 */     int j = i;
/*      */ 
/*  118 */     if (i == 225)
/*      */     {
/*  121 */       j = i;
/*      */     }
/*  123 */     else if (i == 224)
/*      */     {
/*  126 */       j = i;
/*      */     }
/*  139 */     else if (i == 6)
/*      */     {
/*  142 */       j = 224;
/*      */     }
/*  154 */     else if (i == 18)
/*      */     {
/*  156 */       j = 224;
/*      */     }
/*  165 */     else if ((i >= 7) && (i <= 12))
/*      */     {
/*  167 */       j = 224;
/*      */     }
/*  172 */     else if (i == 4) {
/*  173 */       j = 224;
/*      */     }
/*  178 */     else if ((i != 16) && (i != 5))
/*      */     {
/*  180 */       j = 225;
/*      */     }
/*      */ 
/*  185 */     return j;
/*      */   }
/*      */ 
/*      */   public Object getUserData()
/*      */   {
/*  718 */     return this.request.getUserData();
/*      */   }
/*      */ 
/*      */   public boolean isCreationAllowed()
/*      */   {
/*  726 */     return this.creationflag;
/*      */   }
/*      */ 
/*      */   public boolean isSetRequest()
/*      */   {
/*  734 */     return this.setreqflag;
/*      */   }
/*      */ 
/*      */   public int getVersion()
/*      */   {
/*  743 */     return this.version;
/*      */   }
/*      */ 
/*      */   public int getRequestPduVersion()
/*      */   {
/*  751 */     return this.request.getRequestPduVersion();
/*      */   }
/*      */ 
/*      */   public SnmpMibNode getMetaNode(Handler paramHandler)
/*      */   {
/*  759 */     return paramHandler.meta;
/*      */   }
/*      */ 
/*      */   public int getOidDepth(Handler paramHandler)
/*      */   {
/*  768 */     return paramHandler.depth;
/*      */   }
/*      */ 
/*      */   public Enumeration getSubRequests(Handler paramHandler)
/*      */   {
/*  779 */     return new Enum(this, paramHandler);
/*      */   }
/*      */ 
/*      */   public Enumeration getHandlers()
/*      */   {
/*  787 */     return this.hashtable.elements();
/*      */   }
/*      */ 
/*      */   public void add(SnmpMibNode paramSnmpMibNode, int paramInt, SnmpVarBind paramSnmpVarBind)
/*      */     throws SnmpStatusException
/*      */   {
/*  796 */     registerNode(paramSnmpMibNode, paramInt, null, paramSnmpVarBind, false, null);
/*      */   }
/*      */ 
/*      */   public void add(SnmpMibNode paramSnmpMibNode, int paramInt, SnmpOid paramSnmpOid, SnmpVarBind paramSnmpVarBind, boolean paramBoolean)
/*      */     throws SnmpStatusException
/*      */   {
/*  806 */     registerNode(paramSnmpMibNode, paramInt, paramSnmpOid, paramSnmpVarBind, paramBoolean, null);
/*      */   }
/*      */ 
/*      */   public void add(SnmpMibNode paramSnmpMibNode, int paramInt, SnmpOid paramSnmpOid, SnmpVarBind paramSnmpVarBind1, boolean paramBoolean, SnmpVarBind paramSnmpVarBind2)
/*      */     throws SnmpStatusException
/*      */   {
/*  818 */     registerNode(paramSnmpMibNode, paramInt, paramSnmpOid, paramSnmpVarBind1, paramBoolean, paramSnmpVarBind2);
/*      */   }
/*      */ 
/*      */   void setPduType(int paramInt)
/*      */   {
/*  833 */     this.type = paramInt;
/*  834 */     this.setreqflag = ((paramInt == 253) || (paramInt == 163));
/*      */   }
/*      */ 
/*      */   void setGetNextFlag()
/*      */   {
/*  843 */     this.getnextflag = true;
/*      */   }
/*      */ 
/*      */   void switchCreationFlag(boolean paramBoolean)
/*      */   {
/*  850 */     this.creationflag = paramBoolean;
/*      */   }
/*      */ 
/*      */   SnmpMibSubRequest getSubRequest(Handler paramHandler)
/*      */   {
/*  860 */     if (paramHandler == null) return null;
/*  861 */     return new SnmpMibSubRequestImpl(this.request, paramHandler.getSubList(), null, false, this.getnextflag, null);
/*      */   }
/*      */ 
/*      */   SnmpMibSubRequest getSubRequest(Handler paramHandler, SnmpOid paramSnmpOid)
/*      */   {
/*  871 */     if (paramHandler == null) return null;
/*  872 */     int i = paramHandler.getEntryPos(paramSnmpOid);
/*  873 */     if (i == -1) return null;
/*  874 */     return new SnmpMibSubRequestImpl(this.request, paramHandler.getEntrySubList(i), paramHandler.getEntryOid(i), paramHandler.isNewEntry(i), this.getnextflag, paramHandler.getRowStatusVarBind(i));
/*      */   }
/*      */ 
/*      */   SnmpMibSubRequest getSubRequest(Handler paramHandler, int paramInt)
/*      */   {
/*  889 */     if (paramHandler == null) return null;
/*  890 */     return new SnmpMibSubRequestImpl(this.request, paramHandler.getEntrySubList(paramInt), paramHandler.getEntryOid(paramInt), paramHandler.isNewEntry(paramInt), this.getnextflag, paramHandler.getRowStatusVarBind(paramInt));
/*      */   }
/*      */ 
/*      */   private void put(Object paramObject, Handler paramHandler)
/*      */   {
/*  909 */     if (paramHandler == null) return;
/*  910 */     if (paramObject == null) return;
/*  911 */     if (this.hashtable == null) this.hashtable = new Hashtable();
/*  912 */     this.hashtable.put(paramObject, paramHandler);
/*      */   }
/*      */ 
/*      */   private Handler get(Object paramObject)
/*      */   {
/*  920 */     if (paramObject == null) return null;
/*  921 */     if (this.hashtable == null) return null;
/*  922 */     return (Handler)this.hashtable.get(paramObject);
/*      */   }
/*      */ 
/*      */   private static int findOid(SnmpOid[] paramArrayOfSnmpOid, int paramInt, SnmpOid paramSnmpOid)
/*      */   {
/*  931 */     int i = paramInt;
/*  932 */     int j = 0;
/*  933 */     int k = i - 1;
/*  934 */     int m = j + (k - j) / 2;
/*      */ 
/*  936 */     while (j <= k)
/*      */     {
/*  938 */       SnmpOid localSnmpOid = paramArrayOfSnmpOid[m];
/*      */ 
/*  943 */       int n = paramSnmpOid.compareTo(localSnmpOid);
/*  944 */       if (n == 0) {
/*  945 */         return m;
/*      */       }
/*  947 */       if (paramSnmpOid.equals(localSnmpOid)) {
/*  948 */         return m;
/*      */       }
/*  950 */       if (n > 0)
/*  951 */         j = m + 1;
/*      */       else {
/*  953 */         k = m - 1;
/*      */       }
/*  955 */       m = j + (k - j) / 2;
/*      */     }
/*  957 */     return -1;
/*      */   }
/*      */ 
/*      */   private static int getInsertionPoint(SnmpOid[] paramArrayOfSnmpOid, int paramInt, SnmpOid paramSnmpOid)
/*      */   {
/*  967 */     SnmpOid[] arrayOfSnmpOid = paramArrayOfSnmpOid;
/*  968 */     int i = paramInt;
/*  969 */     int j = 0;
/*  970 */     int k = i - 1;
/*  971 */     int m = j + (k - j) / 2;
/*      */ 
/*  974 */     while (j <= k)
/*      */     {
/*  976 */       SnmpOid localSnmpOid = arrayOfSnmpOid[m];
/*      */ 
/*  980 */       int n = paramSnmpOid.compareTo(localSnmpOid);
/*      */ 
/*  990 */       if (n == 0) {
/*  991 */         return m;
/*      */       }
/*  993 */       if (n > 0)
/*  994 */         j = m + 1;
/*      */       else {
/*  996 */         k = m - 1;
/*      */       }
/*  998 */       m = j + (k - j) / 2;
/*      */     }
/* 1000 */     return m;
/*      */   }
/*      */ 
/*      */   private void registerNode(SnmpMibNode paramSnmpMibNode, int paramInt, SnmpOid paramSnmpOid, SnmpVarBind paramSnmpVarBind1, boolean paramBoolean, SnmpVarBind paramSnmpVarBind2)
/*      */     throws SnmpStatusException
/*      */   {
/* 1011 */     if (paramSnmpMibNode == null) {
/* 1012 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpRequestTree.class.getName(), "registerNode", "meta-node is null!");
/*      */ 
/* 1015 */       return;
/*      */     }
/* 1017 */     if (paramSnmpVarBind1 == null) {
/* 1018 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpRequestTree.class.getName(), "registerNode", "varbind is null!");
/*      */ 
/* 1021 */       return;
/*      */     }
/*      */ 
/* 1024 */     SnmpMibNode localSnmpMibNode = paramSnmpMibNode;
/*      */ 
/* 1028 */     Handler localHandler = get(localSnmpMibNode);
/*      */ 
/* 1031 */     if (localHandler == null)
/*      */     {
/* 1035 */       localHandler = new Handler(this.type);
/* 1036 */       localHandler.meta = paramSnmpMibNode;
/* 1037 */       localHandler.depth = paramInt;
/* 1038 */       put(localSnmpMibNode, localHandler);
/*      */     }
/*      */ 
/* 1047 */     if (paramSnmpOid == null)
/* 1048 */       localHandler.addVarbind(paramSnmpVarBind1);
/*      */     else
/* 1050 */       localHandler.addVarbind(paramSnmpVarBind1, paramSnmpOid, paramBoolean, paramSnmpVarBind2);
/*      */   }
/*      */ 
/*      */   static final class Enum
/*      */     implements Enumeration
/*      */   {
/*      */     private final SnmpRequestTree.Handler handler;
/*      */     private final SnmpRequestTree hlist;
/*  202 */     private int entry = 0;
/*  203 */     private int iter = 0;
/*  204 */     private int size = 0;
/*      */ 
/*      */     Enum(SnmpRequestTree paramSnmpRequestTree, SnmpRequestTree.Handler paramHandler)
/*      */     {
/*  196 */       this.handler = paramHandler;
/*  197 */       this.hlist = paramSnmpRequestTree;
/*  198 */       this.size = paramHandler.getSubReqCount();
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements()
/*      */     {
/*  207 */       return this.iter < this.size;
/*      */     }
/*      */ 
/*      */     public Object nextElement() throws NoSuchElementException {
/*  211 */       if ((this.iter == 0) && 
/*  212 */         (this.handler.sublist != null)) {
/*  213 */         this.iter += 1;
/*  214 */         return this.hlist.getSubRequest(this.handler);
/*      */       }
/*      */ 
/*  217 */       this.iter += 1;
/*  218 */       if (this.iter > this.size) throw new NoSuchElementException();
/*  219 */       SnmpMibSubRequest localSnmpMibSubRequest = this.hlist.getSubRequest(this.handler, this.entry);
/*  220 */       this.entry += 1;
/*  221 */       return localSnmpMibSubRequest;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class Handler
/*      */   {
/*      */     SnmpMibNode meta;
/*      */     int depth;
/*      */     Vector<SnmpVarBind> sublist;
/*  470 */     SnmpOid[] entryoids = null;
/*  471 */     Vector<SnmpVarBind>[] entrylists = null;
/*  472 */     boolean[] isentrynew = null;
/*  473 */     SnmpVarBind[] rowstatus = null;
/*  474 */     int entrycount = 0;
/*  475 */     int entrysize = 0;
/*      */     final int type;
/*      */     private static final int Delta = 10;
/*      */ 
/*      */     public Handler(int paramInt)
/*      */     {
/*  481 */       this.type = paramInt;
/*      */     }
/*      */ 
/*      */     public void addVarbind(SnmpVarBind paramSnmpVarBind)
/*      */     {
/*  488 */       if (this.sublist == null) this.sublist = new Vector();
/*  489 */       this.sublist.addElement(paramSnmpVarBind);
/*      */     }
/*      */ 
/*      */     void add(int paramInt, SnmpOid paramSnmpOid, Vector<SnmpVarBind> paramVector, boolean paramBoolean, SnmpVarBind paramSnmpVarBind)
/*      */     {
/*  502 */       if (this.entryoids == null)
/*      */       {
/*  505 */         this.entryoids = new SnmpOid[10];
/*  506 */         this.entrylists = new Vector[10];
/*  507 */         this.isentrynew = new boolean[10];
/*  508 */         this.rowstatus = new SnmpVarBind[10];
/*  509 */         this.entrysize = 10;
/*  510 */         paramInt = 0;
/*      */       }
/*  512 */       else if ((paramInt >= this.entrysize) || (this.entrycount == this.entrysize))
/*      */       {
/*  516 */         SnmpOid[] arrayOfSnmpOid = this.entryoids;
/*  517 */         Vector[] arrayOfVector = this.entrylists;
/*  518 */         boolean[] arrayOfBoolean = this.isentrynew;
/*  519 */         SnmpVarBind[] arrayOfSnmpVarBind = this.rowstatus;
/*      */ 
/*  522 */         this.entrysize += 10;
/*  523 */         this.entryoids = new SnmpOid[this.entrysize];
/*  524 */         this.entrylists = new Vector[this.entrysize];
/*  525 */         this.isentrynew = new boolean[this.entrysize];
/*  526 */         this.rowstatus = new SnmpVarBind[this.entrysize];
/*      */ 
/*  529 */         if (paramInt > this.entrycount) paramInt = this.entrycount;
/*  530 */         if (paramInt < 0) paramInt = 0;
/*      */ 
/*  532 */         int k = paramInt;
/*  533 */         int m = this.entrycount - paramInt;
/*      */ 
/*  536 */         if (k > 0) {
/*  537 */           System.arraycopy(arrayOfSnmpOid, 0, this.entryoids, 0, k);
/*      */ 
/*  539 */           System.arraycopy(arrayOfVector, 0, this.entrylists, 0, k);
/*      */ 
/*  541 */           System.arraycopy(arrayOfBoolean, 0, this.isentrynew, 0, k);
/*      */ 
/*  543 */           System.arraycopy(arrayOfSnmpVarBind, 0, this.rowstatus, 0, k);
/*      */         }
/*      */ 
/*  549 */         if (m > 0) {
/*  550 */           int n = k + 1;
/*  551 */           System.arraycopy(arrayOfSnmpOid, k, this.entryoids, n, m);
/*      */ 
/*  553 */           System.arraycopy(arrayOfVector, k, this.entrylists, n, m);
/*      */ 
/*  555 */           System.arraycopy(arrayOfBoolean, k, this.isentrynew, n, m);
/*      */ 
/*  557 */           System.arraycopy(arrayOfSnmpVarBind, k, this.rowstatus, n, m);
/*      */         }
/*      */ 
/*      */       }
/*  562 */       else if (paramInt < this.entrycount)
/*      */       {
/*  567 */         int i = paramInt + 1;
/*  568 */         int j = this.entrycount - paramInt;
/*      */ 
/*  570 */         System.arraycopy(this.entryoids, paramInt, this.entryoids, i, j);
/*      */ 
/*  572 */         System.arraycopy(this.entrylists, paramInt, this.entrylists, i, j);
/*      */ 
/*  574 */         System.arraycopy(this.isentrynew, paramInt, this.isentrynew, i, j);
/*      */ 
/*  576 */         System.arraycopy(this.rowstatus, paramInt, this.rowstatus, i, j);
/*      */       }
/*      */ 
/*  581 */       this.entryoids[paramInt] = paramSnmpOid;
/*  582 */       this.entrylists[paramInt] = paramVector;
/*  583 */       this.isentrynew[paramInt] = paramBoolean;
/*  584 */       this.rowstatus[paramInt] = paramSnmpVarBind;
/*  585 */       this.entrycount += 1;
/*      */     }
/*      */ 
/*      */     public void addVarbind(SnmpVarBind paramSnmpVarBind1, SnmpOid paramSnmpOid, boolean paramBoolean, SnmpVarBind paramSnmpVarBind2)
/*      */       throws SnmpStatusException
/*      */     {
/*  591 */       Vector localVector = null;
/*  592 */       SnmpVarBind localSnmpVarBind = paramSnmpVarBind2;
/*      */ 
/*  594 */       if (this.entryoids == null)
/*      */       {
/*  598 */         localVector = new Vector();
/*      */ 
/*  602 */         add(0, paramSnmpOid, localVector, paramBoolean, localSnmpVarBind);
/*      */       }
/*      */       else
/*      */       {
/*  606 */         int i = SnmpRequestTree.getInsertionPoint(this.entryoids, this.entrycount, paramSnmpOid);
/*      */ 
/*  608 */         if ((i > -1) && (i < this.entrycount) && (paramSnmpOid.compareTo(this.entryoids[i]) == 0))
/*      */         {
/*  610 */           localVector = this.entrylists[i];
/*  611 */           localSnmpVarBind = this.rowstatus[i];
/*      */         }
/*      */         else
/*      */         {
/*  617 */           localVector = new Vector();
/*      */ 
/*  621 */           add(i, paramSnmpOid, localVector, paramBoolean, localSnmpVarBind);
/*      */         }
/*      */ 
/*  625 */         if (paramSnmpVarBind2 != null) {
/*  626 */           if ((localSnmpVarBind != null) && (localSnmpVarBind != paramSnmpVarBind2) && ((this.type == 253) || (this.type == 163)))
/*      */           {
/*  629 */             throw new SnmpStatusException(12);
/*      */           }
/*      */ 
/*  632 */           this.rowstatus[i] = paramSnmpVarBind2;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  639 */       if (paramSnmpVarBind2 != paramSnmpVarBind1)
/*  640 */         localVector.addElement(paramSnmpVarBind1);
/*      */     }
/*      */ 
/*      */     public int getSubReqCount() {
/*  644 */       int i = 0;
/*  645 */       if (this.sublist != null) i++;
/*      */ 
/*  647 */       if (this.entryoids != null) i += this.entrycount;
/*  648 */       return i;
/*      */     }
/*      */ 
/*      */     public Vector<SnmpVarBind> getSubList() {
/*  652 */       return this.sublist;
/*      */     }
/*      */ 
/*      */     public int getEntryPos(SnmpOid paramSnmpOid)
/*      */     {
/*  657 */       return SnmpRequestTree.findOid(this.entryoids, this.entrycount, paramSnmpOid);
/*      */     }
/*      */ 
/*      */     public SnmpOid getEntryOid(int paramInt) {
/*  661 */       if (this.entryoids == null) return null;
/*      */ 
/*  663 */       if ((paramInt == -1) || (paramInt >= this.entrycount)) return null;
/*      */ 
/*  665 */       return this.entryoids[paramInt];
/*      */     }
/*      */ 
/*      */     public boolean isNewEntry(int paramInt) {
/*  669 */       if (this.entryoids == null) return false;
/*      */ 
/*  671 */       if ((paramInt == -1) || (paramInt >= this.entrycount)) return false;
/*      */ 
/*  673 */       return this.isentrynew[paramInt];
/*      */     }
/*      */ 
/*      */     public SnmpVarBind getRowStatusVarBind(int paramInt) {
/*  677 */       if (this.entryoids == null) return null;
/*      */ 
/*  679 */       if ((paramInt == -1) || (paramInt >= this.entrycount)) return null;
/*      */ 
/*  681 */       return this.rowstatus[paramInt];
/*      */     }
/*      */ 
/*      */     public Vector<SnmpVarBind> getEntrySubList(int paramInt) {
/*  685 */       if (this.entrylists == null) return null;
/*      */ 
/*  687 */       if ((paramInt == -1) || (paramInt >= this.entrycount)) return null;
/*      */ 
/*  689 */       return this.entrylists[paramInt];
/*      */     }
/*      */ 
/*      */     public Iterator<SnmpOid> getEntryOids() {
/*  693 */       if (this.entryoids == null) return null;
/*      */ 
/*  695 */       return Arrays.asList(this.entryoids).iterator();
/*      */     }
/*      */ 
/*      */     public int getEntryCount() {
/*  699 */       if (this.entryoids == null) return 0;
/*      */ 
/*  701 */       return this.entrycount;
/*      */     }
/*      */   }
/*      */ 
/*      */   static final class SnmpMibSubRequestImpl
/*      */     implements SnmpMibSubRequest
/*      */   {
/*      */     private final Vector<SnmpVarBind> varbinds;
/*      */     private final SnmpMibRequest global;
/*      */     private final int version;
/*      */     private final boolean isnew;
/*      */     private final SnmpOid entryoid;
/*      */     private final boolean getnextflag;
/*      */     private final SnmpVarBind statusvb;
/*      */ 
/*      */     SnmpMibSubRequestImpl(SnmpMibRequest paramSnmpMibRequest, Vector<SnmpVarBind> paramVector, SnmpOid paramSnmpOid, boolean paramBoolean1, boolean paramBoolean2, SnmpVarBind paramSnmpVarBind)
/*      */     {
/*  234 */       this.global = paramSnmpMibRequest;
/*  235 */       this.varbinds = paramVector;
/*  236 */       this.version = paramSnmpMibRequest.getVersion();
/*  237 */       this.entryoid = paramSnmpOid;
/*  238 */       this.isnew = paramBoolean1;
/*  239 */       this.getnextflag = paramBoolean2;
/*  240 */       this.statusvb = paramSnmpVarBind;
/*      */     }
/*      */ 
/*      */     public Enumeration getElements()
/*      */     {
/*  256 */       return this.varbinds.elements();
/*      */     }
/*      */ 
/*      */     public Vector<SnmpVarBind> getSubList()
/*      */     {
/*  264 */       return this.varbinds;
/*      */     }
/*      */ 
/*      */     public final int getSize()
/*      */     {
/*  272 */       if (this.varbinds == null) return 0;
/*  273 */       return this.varbinds.size();
/*      */     }
/*      */ 
/*      */     public void addVarBind(SnmpVarBind paramSnmpVarBind)
/*      */     {
/*  284 */       this.varbinds.addElement(paramSnmpVarBind);
/*  285 */       this.global.addVarBind(paramSnmpVarBind);
/*      */     }
/*      */ 
/*      */     public boolean isNewEntry()
/*      */     {
/*  293 */       return this.isnew;
/*      */     }
/*      */ 
/*      */     public SnmpOid getEntryOid()
/*      */     {
/*  301 */       return this.entryoid;
/*      */     }
/*      */ 
/*      */     public int getVarIndex(SnmpVarBind paramSnmpVarBind)
/*      */     {
/*  309 */       if (paramSnmpVarBind == null) return 0;
/*  310 */       return this.global.getVarIndex(paramSnmpVarBind);
/*      */     }
/*      */ 
/*      */     public Object getUserData()
/*      */     {
/*  317 */       return this.global.getUserData();
/*      */     }
/*      */ 
/*      */     public void registerGetException(SnmpVarBind paramSnmpVarBind, SnmpStatusException paramSnmpStatusException)
/*      */       throws SnmpStatusException
/*      */     {
/*  331 */       if (this.version == 0) {
/*  332 */         throw new SnmpStatusException(paramSnmpStatusException, getVarIndex(paramSnmpVarBind) + 1);
/*      */       }
/*  334 */       if (paramSnmpVarBind == null) {
/*  335 */         throw paramSnmpStatusException;
/*      */       }
/*      */ 
/*  338 */       if (this.getnextflag) {
/*  339 */         paramSnmpVarBind.value = SnmpVarBind.endOfMibView;
/*  340 */         return;
/*      */       }
/*      */ 
/*  343 */       int i = SnmpRequestTree.mapGetException(paramSnmpStatusException.getStatus(), this.version);
/*      */ 
/*  348 */       if (i == 225)
/*      */       {
/*  351 */         paramSnmpVarBind.value = SnmpVarBind.noSuchObject;
/*      */       }
/*  353 */       else if (i == 224)
/*      */       {
/*  356 */         paramSnmpVarBind.value = SnmpVarBind.noSuchInstance;
/*      */       }
/*      */       else
/*  359 */         throw new SnmpStatusException(i, getVarIndex(paramSnmpVarBind) + 1);
/*      */     }
/*      */ 
/*      */     public void registerSetException(SnmpVarBind paramSnmpVarBind, SnmpStatusException paramSnmpStatusException)
/*      */       throws SnmpStatusException
/*      */     {
/*  373 */       if (this.version == 0) {
/*  374 */         throw new SnmpStatusException(paramSnmpStatusException, getVarIndex(paramSnmpVarBind) + 1);
/*      */       }
/*      */ 
/*  382 */       throw new SnmpStatusException(15, getVarIndex(paramSnmpVarBind) + 1);
/*      */     }
/*      */ 
/*      */     public void registerCheckException(SnmpVarBind paramSnmpVarBind, SnmpStatusException paramSnmpStatusException)
/*      */       throws SnmpStatusException
/*      */     {
/*  398 */       int i = paramSnmpStatusException.getStatus();
/*  399 */       int j = SnmpRequestTree.mapSetException(i, this.version);
/*      */ 
/*  402 */       if (i != j) {
/*  403 */         throw new SnmpStatusException(j, getVarIndex(paramSnmpVarBind) + 1);
/*      */       }
/*      */ 
/*  406 */       throw new SnmpStatusException(paramSnmpStatusException, getVarIndex(paramSnmpVarBind) + 1);
/*      */     }
/*      */ 
/*      */     public int getVersion()
/*      */     {
/*  414 */       return this.version;
/*      */     }
/*      */ 
/*      */     public SnmpVarBind getRowStatusVarBind() {
/*  418 */       return this.statusvb;
/*      */     }
/*      */ 
/*      */     public SnmpPdu getPdu() {
/*  422 */       return this.global.getPdu();
/*      */     }
/*      */ 
/*      */     public int getRequestPduVersion() {
/*  426 */       return this.global.getRequestPduVersion();
/*      */     }
/*      */ 
/*      */     public SnmpEngine getEngine() {
/*  430 */       return this.global.getEngine();
/*      */     }
/*      */ 
/*      */     public String getPrincipal() {
/*  434 */       return this.global.getPrincipal();
/*      */     }
/*      */ 
/*      */     public int getSecurityLevel() {
/*  438 */       return this.global.getSecurityLevel();
/*      */     }
/*      */ 
/*      */     public int getSecurityModel() {
/*  442 */       return this.global.getSecurityModel();
/*      */     }
/*      */ 
/*      */     public byte[] getContextName() {
/*  446 */       return this.global.getContextName();
/*      */     }
/*      */ 
/*      */     public byte[] getAccessContextName() {
/*  450 */       return this.global.getAccessContextName();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpRequestTree
 * JD-Core Version:    0.6.2
 */
/*      */ package com.sun.org.apache.xml.internal.resolver;
/*      */ 
/*      */ import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;
/*      */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*      */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*      */ import com.sun.org.apache.xml.internal.resolver.helpers.FileURL;
/*      */ import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;
/*      */ import com.sun.org.apache.xml.internal.resolver.readers.CatalogReader;
/*      */ import com.sun.org.apache.xml.internal.resolver.readers.SAXCatalogReader;
/*      */ import com.sun.org.apache.xml.internal.resolver.readers.TR9401CatalogReader;
/*      */ import java.io.DataInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import javax.xml.parsers.SAXParserFactory;
/*      */ 
/*      */ public class Catalog
/*      */ {
/*  200 */   public static final int BASE = CatalogEntry.addEntryType("BASE", 1);
/*      */ 
/*  203 */   public static final int CATALOG = CatalogEntry.addEntryType("CATALOG", 1);
/*      */ 
/*  206 */   public static final int DOCUMENT = CatalogEntry.addEntryType("DOCUMENT", 1);
/*      */ 
/*  209 */   public static final int OVERRIDE = CatalogEntry.addEntryType("OVERRIDE", 1);
/*      */ 
/*  212 */   public static final int SGMLDECL = CatalogEntry.addEntryType("SGMLDECL", 1);
/*      */ 
/*  215 */   public static final int DELEGATE_PUBLIC = CatalogEntry.addEntryType("DELEGATE_PUBLIC", 2);
/*      */ 
/*  218 */   public static final int DELEGATE_SYSTEM = CatalogEntry.addEntryType("DELEGATE_SYSTEM", 2);
/*      */ 
/*  221 */   public static final int DELEGATE_URI = CatalogEntry.addEntryType("DELEGATE_URI", 2);
/*      */ 
/*  224 */   public static final int DOCTYPE = CatalogEntry.addEntryType("DOCTYPE", 2);
/*      */ 
/*  227 */   public static final int DTDDECL = CatalogEntry.addEntryType("DTDDECL", 2);
/*      */ 
/*  230 */   public static final int ENTITY = CatalogEntry.addEntryType("ENTITY", 2);
/*      */ 
/*  233 */   public static final int LINKTYPE = CatalogEntry.addEntryType("LINKTYPE", 2);
/*      */ 
/*  236 */   public static final int NOTATION = CatalogEntry.addEntryType("NOTATION", 2);
/*      */ 
/*  239 */   public static final int PUBLIC = CatalogEntry.addEntryType("PUBLIC", 2);
/*      */ 
/*  242 */   public static final int SYSTEM = CatalogEntry.addEntryType("SYSTEM", 2);
/*      */ 
/*  245 */   public static final int URI = CatalogEntry.addEntryType("URI", 2);
/*      */ 
/*  248 */   public static final int REWRITE_SYSTEM = CatalogEntry.addEntryType("REWRITE_SYSTEM", 2);
/*      */ 
/*  251 */   public static final int REWRITE_URI = CatalogEntry.addEntryType("REWRITE_URI", 2);
/*      */ 
/*  253 */   public static final int SYSTEM_SUFFIX = CatalogEntry.addEntryType("SYSTEM_SUFFIX", 2);
/*      */ 
/*  255 */   public static final int URI_SUFFIX = CatalogEntry.addEntryType("URI_SUFFIX", 2);
/*      */   protected URL base;
/*      */   protected URL catalogCwd;
/*  267 */   protected Vector catalogEntries = new Vector();
/*      */ 
/*  270 */   protected boolean default_override = true;
/*      */ 
/*  273 */   protected CatalogManager catalogManager = CatalogManager.getStaticManager();
/*      */ 
/*  286 */   protected Vector catalogFiles = new Vector();
/*      */ 
/*  305 */   protected Vector localCatalogFiles = new Vector();
/*      */ 
/*  324 */   protected Vector catalogs = new Vector();
/*      */ 
/*  341 */   protected Vector localDelegate = new Vector();
/*      */ 
/*  350 */   protected Hashtable readerMap = new Hashtable();
/*      */ 
/*  360 */   protected Vector readerArr = new Vector();
/*      */ 
/*      */   public Catalog()
/*      */   {
/*      */   }
/*      */ 
/*      */   public Catalog(CatalogManager manager)
/*      */   {
/*  381 */     this.catalogManager = manager;
/*      */   }
/*      */ 
/*      */   public CatalogManager getCatalogManager()
/*      */   {
/*  389 */     return this.catalogManager;
/*      */   }
/*      */ 
/*      */   public void setCatalogManager(CatalogManager manager)
/*      */   {
/*  397 */     this.catalogManager = manager;
/*      */   }
/*      */ 
/*      */   public void setupReaders()
/*      */   {
/*  404 */     SAXParserFactory spf = this.catalogManager.useServicesMechanism() ? SAXParserFactory.newInstance() : new SAXParserFactoryImpl();
/*      */ 
/*  406 */     spf.setNamespaceAware(true);
/*  407 */     spf.setValidating(false);
/*      */ 
/*  409 */     SAXCatalogReader saxReader = new SAXCatalogReader(spf);
/*      */ 
/*  411 */     saxReader.setCatalogParser(null, "XMLCatalog", "com.sun.org.apache.xml.internal.resolver.readers.XCatalogReader");
/*      */ 
/*  414 */     saxReader.setCatalogParser("urn:oasis:names:tc:entity:xmlns:xml:catalog", "catalog", "com.sun.org.apache.xml.internal.resolver.readers.OASISXMLCatalogReader");
/*      */ 
/*  418 */     addReader("application/xml", saxReader);
/*      */ 
/*  420 */     TR9401CatalogReader textReader = new TR9401CatalogReader();
/*  421 */     addReader("text/plain", textReader);
/*      */   }
/*      */ 
/*      */   public void addReader(String mimeType, CatalogReader reader)
/*      */   {
/*  445 */     if (this.readerMap.containsKey(mimeType)) {
/*  446 */       Integer pos = (Integer)this.readerMap.get(mimeType);
/*  447 */       this.readerArr.set(pos.intValue(), reader);
/*      */     } else {
/*  449 */       this.readerArr.add(reader);
/*  450 */       Integer pos = new Integer(this.readerArr.size() - 1);
/*  451 */       this.readerMap.put(mimeType, pos);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void copyReaders(Catalog newCatalog)
/*      */   {
/*  466 */     Vector mapArr = new Vector(this.readerMap.size());
/*      */ 
/*  469 */     for (int count = 0; count < this.readerMap.size(); count++) {
/*  470 */       mapArr.add(null);
/*      */     }
/*      */ 
/*  473 */     Enumeration en = this.readerMap.keys();
/*  474 */     while (en.hasMoreElements()) {
/*  475 */       String mimeType = (String)en.nextElement();
/*  476 */       Integer pos = (Integer)this.readerMap.get(mimeType);
/*  477 */       mapArr.set(pos.intValue(), mimeType);
/*      */     }
/*      */ 
/*  480 */     for (int count = 0; count < mapArr.size(); count++) {
/*  481 */       String mimeType = (String)mapArr.get(count);
/*  482 */       Integer pos = (Integer)this.readerMap.get(mimeType);
/*  483 */       newCatalog.addReader(mimeType, (CatalogReader)this.readerArr.get(pos.intValue()));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Catalog newCatalog()
/*      */   {
/*  502 */     String catalogClass = getClass().getName();
/*      */     try
/*      */     {
/*  505 */       Catalog c = (Catalog)Class.forName(catalogClass).newInstance();
/*  506 */       c.setCatalogManager(this.catalogManager);
/*  507 */       copyReaders(c);
/*  508 */       return c;
/*      */     } catch (ClassNotFoundException cnfe) {
/*  510 */       this.catalogManager.debug.message(1, "Class Not Found Exception: " + catalogClass);
/*      */     } catch (IllegalAccessException iae) {
/*  512 */       this.catalogManager.debug.message(1, "Illegal Access Exception: " + catalogClass);
/*      */     } catch (InstantiationException ie) {
/*  514 */       this.catalogManager.debug.message(1, "Instantiation Exception: " + catalogClass);
/*      */     } catch (ClassCastException cce) {
/*  516 */       this.catalogManager.debug.message(1, "Class Cast Exception: " + catalogClass);
/*      */     } catch (Exception e) {
/*  518 */       this.catalogManager.debug.message(1, "Other Exception: " + catalogClass);
/*      */     }
/*      */ 
/*  521 */     Catalog c = new Catalog();
/*  522 */     c.setCatalogManager(this.catalogManager);
/*  523 */     copyReaders(c);
/*  524 */     return c;
/*      */   }
/*      */ 
/*      */   public String getCurrentBase()
/*      */   {
/*  531 */     return this.base.toString();
/*      */   }
/*      */ 
/*      */   public String getDefaultOverride()
/*      */   {
/*  542 */     if (this.default_override) {
/*  543 */       return "yes";
/*      */     }
/*  545 */     return "no";
/*      */   }
/*      */ 
/*      */   public void loadSystemCatalogs()
/*      */     throws MalformedURLException, IOException
/*      */   {
/*  563 */     Vector catalogs = this.catalogManager.getCatalogFiles();
/*  564 */     if (catalogs != null) {
/*  565 */       for (int count = 0; count < catalogs.size(); count++) {
/*  566 */         this.catalogFiles.addElement(catalogs.elementAt(count));
/*      */       }
/*      */     }
/*      */ 
/*  570 */     if (this.catalogFiles.size() > 0)
/*      */     {
/*  583 */       String catfile = (String)this.catalogFiles.lastElement();
/*  584 */       this.catalogFiles.removeElement(catfile);
/*  585 */       parseCatalog(catfile);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void parseCatalog(String fileName)
/*      */     throws MalformedURLException, IOException
/*      */   {
/*  601 */     this.default_override = this.catalogManager.getPreferPublic();
/*  602 */     this.catalogManager.debug.message(4, "Parse catalog: " + fileName);
/*      */ 
/*  607 */     this.catalogFiles.addElement(fileName);
/*      */ 
/*  610 */     parsePendingCatalogs();
/*      */   }
/*      */ 
/*      */   public synchronized void parseCatalog(String mimeType, InputStream is)
/*      */     throws IOException, CatalogException
/*      */   {
/*  629 */     this.default_override = this.catalogManager.getPreferPublic();
/*  630 */     this.catalogManager.debug.message(4, "Parse " + mimeType + " catalog on input stream");
/*      */ 
/*  632 */     CatalogReader reader = null;
/*      */ 
/*  634 */     if (this.readerMap.containsKey(mimeType)) {
/*  635 */       int arrayPos = ((Integer)this.readerMap.get(mimeType)).intValue();
/*  636 */       reader = (CatalogReader)this.readerArr.get(arrayPos);
/*      */     }
/*      */ 
/*  639 */     if (reader == null) {
/*  640 */       String msg = "No CatalogReader for MIME type: " + mimeType;
/*  641 */       this.catalogManager.debug.message(2, msg);
/*  642 */       throw new CatalogException(6, msg);
/*      */     }
/*      */ 
/*  645 */     reader.readCatalog(this, is);
/*      */ 
/*  648 */     parsePendingCatalogs();
/*      */   }
/*      */ 
/*      */   public synchronized void parseCatalog(URL aUrl)
/*      */     throws IOException
/*      */   {
/*  667 */     this.catalogCwd = aUrl;
/*  668 */     this.base = aUrl;
/*      */ 
/*  670 */     this.default_override = this.catalogManager.getPreferPublic();
/*  671 */     this.catalogManager.debug.message(4, "Parse catalog: " + aUrl.toString());
/*      */ 
/*  673 */     DataInputStream inStream = null;
/*  674 */     boolean parsed = false;
/*      */ 
/*  676 */     for (int count = 0; (!parsed) && (count < this.readerArr.size()); count++) {
/*  677 */       CatalogReader reader = (CatalogReader)this.readerArr.get(count);
/*      */       try
/*      */       {
/*  680 */         inStream = new DataInputStream(aUrl.openStream());
/*      */       }
/*      */       catch (FileNotFoundException fnfe) {
/*  683 */         break;
/*      */       }
/*      */       try
/*      */       {
/*  687 */         reader.readCatalog(this, inStream);
/*  688 */         parsed = true;
/*      */       } catch (CatalogException ce) {
/*  690 */         if (ce.getExceptionType() != 7) break label140;
/*      */       }
/*  692 */       break;
/*      */       try
/*      */       {
/*  699 */         label140: inStream.close();
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*      */       }
/*      */     }
/*  705 */     if (parsed) parsePendingCatalogs();
/*      */   }
/*      */ 
/*      */   protected synchronized void parsePendingCatalogs()
/*      */     throws MalformedURLException, IOException
/*      */   {
/*  717 */     if (!this.localCatalogFiles.isEmpty())
/*      */     {
/*  720 */       Vector newQueue = new Vector();
/*  721 */       Enumeration q = this.localCatalogFiles.elements();
/*  722 */       while (q.hasMoreElements()) {
/*  723 */         newQueue.addElement(q.nextElement());
/*      */       }
/*      */ 
/*  727 */       for (int curCat = 0; curCat < this.catalogFiles.size(); curCat++) {
/*  728 */         String catfile = (String)this.catalogFiles.elementAt(curCat);
/*  729 */         newQueue.addElement(catfile);
/*      */       }
/*      */ 
/*  732 */       this.catalogFiles = newQueue;
/*  733 */       this.localCatalogFiles.clear();
/*      */     }
/*      */ 
/*  739 */     if ((this.catalogFiles.isEmpty()) && (!this.localDelegate.isEmpty())) {
/*  740 */       Enumeration e = this.localDelegate.elements();
/*  741 */       while (e.hasMoreElements()) {
/*  742 */         this.catalogEntries.addElement(e.nextElement());
/*      */       }
/*  744 */       this.localDelegate.clear();
/*      */     }
/*      */ 
/*  750 */     while (!this.catalogFiles.isEmpty()) {
/*  751 */       String catfile = (String)this.catalogFiles.elementAt(0);
/*      */       try {
/*  753 */         this.catalogFiles.remove(0);
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException e)
/*      */       {
/*      */       }
/*  758 */       if ((this.catalogEntries.size() == 0) && (this.catalogs.size() == 0))
/*      */       {
/*      */         try
/*      */         {
/*  762 */           parseCatalogFile(catfile);
/*      */         } catch (CatalogException ce) {
/*  764 */           System.out.println("FIXME: " + ce.toString());
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  769 */         this.catalogs.addElement(catfile);
/*      */       }
/*      */ 
/*  772 */       if (!this.localCatalogFiles.isEmpty())
/*      */       {
/*  775 */         Vector newQueue = new Vector();
/*  776 */         Enumeration q = this.localCatalogFiles.elements();
/*  777 */         while (q.hasMoreElements()) {
/*  778 */           newQueue.addElement(q.nextElement());
/*      */         }
/*      */ 
/*  782 */         for (int curCat = 0; curCat < this.catalogFiles.size(); curCat++) {
/*  783 */           catfile = (String)this.catalogFiles.elementAt(curCat);
/*  784 */           newQueue.addElement(catfile);
/*      */         }
/*      */ 
/*  787 */         this.catalogFiles = newQueue;
/*  788 */         this.localCatalogFiles.clear();
/*      */       }
/*      */ 
/*  791 */       if (!this.localDelegate.isEmpty()) {
/*  792 */         Enumeration e = this.localDelegate.elements();
/*  793 */         while (e.hasMoreElements()) {
/*  794 */           this.catalogEntries.addElement(e.nextElement());
/*      */         }
/*  796 */         this.localDelegate.clear();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  801 */     this.catalogFiles.clear();
/*      */   }
/*      */ 
/*      */   protected synchronized void parseCatalogFile(String fileName)
/*      */     throws MalformedURLException, IOException, CatalogException
/*      */   {
/*      */     try
/*      */     {
/*  823 */       this.catalogCwd = FileURL.makeURL("basename");
/*      */     } catch (MalformedURLException e) {
/*  825 */       String userdir = SecuritySupport.getSystemProperty("user.dir");
/*  826 */       userdir.replace('\\', '/');
/*  827 */       this.catalogManager.debug.message(1, "Malformed URL on cwd", userdir);
/*  828 */       this.catalogCwd = null;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/*  833 */       this.base = new URL(this.catalogCwd, fixSlashes(fileName));
/*      */     } catch (MalformedURLException e) {
/*      */       try {
/*  836 */         this.base = new URL("file:" + fixSlashes(fileName));
/*      */       } catch (MalformedURLException e2) {
/*  838 */         this.catalogManager.debug.message(1, "Malformed URL on catalog filename", fixSlashes(fileName));
/*      */ 
/*  840 */         this.base = null;
/*      */       }
/*      */     }
/*      */ 
/*  844 */     this.catalogManager.debug.message(2, "Loading catalog", fileName);
/*  845 */     this.catalogManager.debug.message(4, "Default BASE", this.base.toString());
/*      */ 
/*  847 */     fileName = this.base.toString();
/*      */ 
/*  849 */     DataInputStream inStream = null;
/*  850 */     boolean parsed = false;
/*  851 */     boolean notFound = false;
/*      */ 
/*  853 */     for (int count = 0; (!parsed) && (count < this.readerArr.size()); count++) {
/*  854 */       CatalogReader reader = (CatalogReader)this.readerArr.get(count);
/*      */       try
/*      */       {
/*  857 */         notFound = false;
/*  858 */         inStream = new DataInputStream(this.base.openStream());
/*      */       }
/*      */       catch (FileNotFoundException fnfe) {
/*  861 */         notFound = true;
/*  862 */         break;
/*      */       }
/*      */       try
/*      */       {
/*  866 */         reader.readCatalog(this, inStream);
/*  867 */         parsed = true;
/*      */       } catch (CatalogException ce) {
/*  869 */         if (ce.getExceptionType() != 7) break label279;
/*      */       }
/*  871 */       break;
/*      */       try
/*      */       {
/*  878 */         label279: inStream.close();
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*      */       }
/*      */     }
/*  884 */     if (!parsed)
/*  885 */       if (notFound)
/*  886 */         this.catalogManager.debug.message(3, "Catalog does not exist", fileName);
/*      */       else
/*  888 */         this.catalogManager.debug.message(1, "Failed to parse catalog", fileName);
/*      */   }
/*      */ 
/*      */   public void addEntry(CatalogEntry entry)
/*      */   {
/*  904 */     int type = entry.getEntryType();
/*      */ 
/*  906 */     if (type == BASE) {
/*  907 */       String value = entry.getEntryArg(0);
/*  908 */       URL newbase = null;
/*      */ 
/*  910 */       if (this.base == null)
/*  911 */         this.catalogManager.debug.message(5, "BASE CUR", "null");
/*      */       else {
/*  913 */         this.catalogManager.debug.message(5, "BASE CUR", this.base.toString());
/*      */       }
/*  915 */       this.catalogManager.debug.message(4, "BASE STR", value);
/*      */       try
/*      */       {
/*  918 */         value = fixSlashes(value);
/*  919 */         newbase = new URL(this.base, value);
/*      */       } catch (MalformedURLException e) {
/*      */         try {
/*  922 */           newbase = new URL("file:" + value);
/*      */         } catch (MalformedURLException e2) {
/*  924 */           this.catalogManager.debug.message(1, "Malformed URL on base", value);
/*  925 */           newbase = null;
/*      */         }
/*      */       }
/*      */ 
/*  929 */       if (newbase != null) {
/*  930 */         this.base = newbase;
/*      */       }
/*      */ 
/*  933 */       this.catalogManager.debug.message(5, "BASE NEW", this.base.toString());
/*  934 */     } else if (type == CATALOG) {
/*  935 */       String fsi = makeAbsolute(entry.getEntryArg(0));
/*      */ 
/*  937 */       this.catalogManager.debug.message(4, "CATALOG", fsi);
/*      */ 
/*  939 */       this.localCatalogFiles.addElement(fsi);
/*  940 */     } else if (type == PUBLIC) {
/*  941 */       String publicid = PublicId.normalize(entry.getEntryArg(0));
/*  942 */       String systemid = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/*  944 */       entry.setEntryArg(0, publicid);
/*  945 */       entry.setEntryArg(1, systemid);
/*      */ 
/*  947 */       this.catalogManager.debug.message(4, "PUBLIC", publicid, systemid);
/*      */ 
/*  949 */       this.catalogEntries.addElement(entry);
/*  950 */     } else if (type == SYSTEM) {
/*  951 */       String systemid = normalizeURI(entry.getEntryArg(0));
/*  952 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/*  954 */       entry.setEntryArg(1, fsi);
/*      */ 
/*  956 */       this.catalogManager.debug.message(4, "SYSTEM", systemid, fsi);
/*      */ 
/*  958 */       this.catalogEntries.addElement(entry);
/*  959 */     } else if (type == URI) {
/*  960 */       String uri = normalizeURI(entry.getEntryArg(0));
/*  961 */       String altURI = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/*  963 */       entry.setEntryArg(1, altURI);
/*      */ 
/*  965 */       this.catalogManager.debug.message(4, "URI", uri, altURI);
/*      */ 
/*  967 */       this.catalogEntries.addElement(entry);
/*  968 */     } else if (type == DOCUMENT) {
/*  969 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(0)));
/*  970 */       entry.setEntryArg(0, fsi);
/*      */ 
/*  972 */       this.catalogManager.debug.message(4, "DOCUMENT", fsi);
/*      */ 
/*  974 */       this.catalogEntries.addElement(entry);
/*  975 */     } else if (type == OVERRIDE) {
/*  976 */       this.catalogManager.debug.message(4, "OVERRIDE", entry.getEntryArg(0));
/*      */ 
/*  978 */       this.catalogEntries.addElement(entry);
/*  979 */     } else if (type == SGMLDECL)
/*      */     {
/*  981 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(0)));
/*  982 */       entry.setEntryArg(0, fsi);
/*      */ 
/*  984 */       this.catalogManager.debug.message(4, "SGMLDECL", fsi);
/*      */ 
/*  986 */       this.catalogEntries.addElement(entry);
/*  987 */     } else if (type == DELEGATE_PUBLIC) {
/*  988 */       String ppi = PublicId.normalize(entry.getEntryArg(0));
/*  989 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/*  991 */       entry.setEntryArg(0, ppi);
/*  992 */       entry.setEntryArg(1, fsi);
/*      */ 
/*  994 */       this.catalogManager.debug.message(4, "DELEGATE_PUBLIC", ppi, fsi);
/*      */ 
/*  996 */       addDelegate(entry);
/*  997 */     } else if (type == DELEGATE_SYSTEM) {
/*  998 */       String psi = normalizeURI(entry.getEntryArg(0));
/*  999 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/* 1001 */       entry.setEntryArg(0, psi);
/* 1002 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1004 */       this.catalogManager.debug.message(4, "DELEGATE_SYSTEM", psi, fsi);
/*      */ 
/* 1006 */       addDelegate(entry);
/* 1007 */     } else if (type == DELEGATE_URI) {
/* 1008 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1009 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/* 1011 */       entry.setEntryArg(0, pui);
/* 1012 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1014 */       this.catalogManager.debug.message(4, "DELEGATE_URI", pui, fsi);
/*      */ 
/* 1016 */       addDelegate(entry);
/* 1017 */     } else if (type == REWRITE_SYSTEM) {
/* 1018 */       String psi = normalizeURI(entry.getEntryArg(0));
/* 1019 */       String rpx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/* 1021 */       entry.setEntryArg(0, psi);
/* 1022 */       entry.setEntryArg(1, rpx);
/*      */ 
/* 1024 */       this.catalogManager.debug.message(4, "REWRITE_SYSTEM", psi, rpx);
/*      */ 
/* 1026 */       this.catalogEntries.addElement(entry);
/* 1027 */     } else if (type == REWRITE_URI) {
/* 1028 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1029 */       String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/* 1031 */       entry.setEntryArg(0, pui);
/* 1032 */       entry.setEntryArg(1, upx);
/*      */ 
/* 1034 */       this.catalogManager.debug.message(4, "REWRITE_URI", pui, upx);
/*      */ 
/* 1036 */       this.catalogEntries.addElement(entry);
/* 1037 */     } else if (type == SYSTEM_SUFFIX) {
/* 1038 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1039 */       String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/* 1041 */       entry.setEntryArg(0, pui);
/* 1042 */       entry.setEntryArg(1, upx);
/*      */ 
/* 1044 */       this.catalogManager.debug.message(4, "SYSTEM_SUFFIX", pui, upx);
/*      */ 
/* 1046 */       this.catalogEntries.addElement(entry);
/* 1047 */     } else if (type == URI_SUFFIX) {
/* 1048 */       String pui = normalizeURI(entry.getEntryArg(0));
/* 1049 */       String upx = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/*      */ 
/* 1051 */       entry.setEntryArg(0, pui);
/* 1052 */       entry.setEntryArg(1, upx);
/*      */ 
/* 1054 */       this.catalogManager.debug.message(4, "URI_SUFFIX", pui, upx);
/*      */ 
/* 1056 */       this.catalogEntries.addElement(entry);
/* 1057 */     } else if (type == DOCTYPE) {
/* 1058 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1059 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1061 */       this.catalogManager.debug.message(4, "DOCTYPE", entry.getEntryArg(0), fsi);
/*      */ 
/* 1063 */       this.catalogEntries.addElement(entry);
/* 1064 */     } else if (type == DTDDECL)
/*      */     {
/* 1066 */       String fpi = PublicId.normalize(entry.getEntryArg(0));
/* 1067 */       entry.setEntryArg(0, fpi);
/* 1068 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1069 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1071 */       this.catalogManager.debug.message(4, "DTDDECL", fpi, fsi);
/*      */ 
/* 1073 */       this.catalogEntries.addElement(entry);
/* 1074 */     } else if (type == ENTITY) {
/* 1075 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1076 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1078 */       this.catalogManager.debug.message(4, "ENTITY", entry.getEntryArg(0), fsi);
/*      */ 
/* 1080 */       this.catalogEntries.addElement(entry);
/* 1081 */     } else if (type == LINKTYPE)
/*      */     {
/* 1083 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1084 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1086 */       this.catalogManager.debug.message(4, "LINKTYPE", entry.getEntryArg(0), fsi);
/*      */ 
/* 1088 */       this.catalogEntries.addElement(entry);
/* 1089 */     } else if (type == NOTATION) {
/* 1090 */       String fsi = makeAbsolute(normalizeURI(entry.getEntryArg(1)));
/* 1091 */       entry.setEntryArg(1, fsi);
/*      */ 
/* 1093 */       this.catalogManager.debug.message(4, "NOTATION", entry.getEntryArg(0), fsi);
/*      */ 
/* 1095 */       this.catalogEntries.addElement(entry);
/*      */     } else {
/* 1097 */       this.catalogEntries.addElement(entry);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void unknownEntry(Vector strings)
/*      */   {
/* 1108 */     if ((strings != null) && (strings.size() > 0)) {
/* 1109 */       String keyword = (String)strings.elementAt(0);
/* 1110 */       this.catalogManager.debug.message(2, "Unrecognized token parsing catalog", keyword);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void parseAllCatalogs()
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1147 */     for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
/* 1148 */       Catalog c = null;
/*      */       try
/*      */       {
/* 1151 */         c = (Catalog)this.catalogs.elementAt(catPos);
/*      */       } catch (ClassCastException e) {
/* 1153 */         String catfile = (String)this.catalogs.elementAt(catPos);
/* 1154 */         c = newCatalog();
/*      */ 
/* 1156 */         c.parseCatalog(catfile);
/* 1157 */         this.catalogs.setElementAt(c, catPos);
/* 1158 */         c.parseAllCatalogs();
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1163 */     Enumeration en = this.catalogEntries.elements();
/* 1164 */     while (en.hasMoreElements()) {
/* 1165 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1166 */       if ((e.getEntryType() == DELEGATE_PUBLIC) || (e.getEntryType() == DELEGATE_SYSTEM) || (e.getEntryType() == DELEGATE_URI))
/*      */       {
/* 1169 */         Catalog dcat = newCatalog();
/* 1170 */         dcat.parseCatalog(e.getEntryArg(1));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public String resolveDoctype(String entityName, String publicId, String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1196 */     String resolved = null;
/*      */ 
/* 1198 */     this.catalogManager.debug.message(3, "resolveDoctype(" + entityName + "," + publicId + "," + systemId + ")");
/*      */ 
/* 1201 */     systemId = normalizeURI(systemId);
/*      */ 
/* 1203 */     if ((publicId != null) && (publicId.startsWith("urn:publicid:"))) {
/* 1204 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */ 
/* 1207 */     if ((systemId != null) && (systemId.startsWith("urn:publicid:"))) {
/* 1208 */       systemId = PublicId.decodeURN(systemId);
/* 1209 */       if ((publicId != null) && (!publicId.equals(systemId))) {
/* 1210 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1211 */         systemId = null;
/*      */       } else {
/* 1213 */         publicId = systemId;
/* 1214 */         systemId = null;
/*      */       }
/*      */     }
/*      */ 
/* 1218 */     if (systemId != null)
/*      */     {
/* 1220 */       resolved = resolveLocalSystem(systemId);
/* 1221 */       if (resolved != null) {
/* 1222 */         return resolved;
/*      */       }
/*      */     }
/*      */ 
/* 1226 */     if (publicId != null)
/*      */     {
/* 1228 */       resolved = resolveLocalPublic(DOCTYPE, entityName, publicId, systemId);
/*      */ 
/* 1232 */       if (resolved != null) {
/* 1233 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1238 */     boolean over = this.default_override;
/* 1239 */     Enumeration en = this.catalogEntries.elements();
/* 1240 */     while (en.hasMoreElements()) {
/* 1241 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1242 */       if (e.getEntryType() == OVERRIDE) {
/* 1243 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */       }
/* 1247 */       else if ((e.getEntryType() == DOCTYPE) && (e.getEntryArg(0).equals(entityName)))
/*      */       {
/* 1249 */         if ((over) || (systemId == null)) {
/* 1250 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1256 */     return resolveSubordinateCatalogs(DOCTYPE, entityName, publicId, systemId);
/*      */   }
/*      */ 
/*      */   public String resolveDocument()
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1275 */     this.catalogManager.debug.message(3, "resolveDocument");
/*      */ 
/* 1277 */     Enumeration en = this.catalogEntries.elements();
/* 1278 */     while (en.hasMoreElements()) {
/* 1279 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1280 */       if (e.getEntryType() == DOCUMENT) {
/* 1281 */         return e.getEntryArg(0);
/*      */       }
/*      */     }
/*      */ 
/* 1285 */     return resolveSubordinateCatalogs(DOCUMENT, null, null, null);
/*      */   }
/*      */ 
/*      */   public String resolveEntity(String entityName, String publicId, String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1309 */     String resolved = null;
/*      */ 
/* 1311 */     this.catalogManager.debug.message(3, "resolveEntity(" + entityName + "," + publicId + "," + systemId + ")");
/*      */ 
/* 1314 */     systemId = normalizeURI(systemId);
/*      */ 
/* 1316 */     if ((publicId != null) && (publicId.startsWith("urn:publicid:"))) {
/* 1317 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */ 
/* 1320 */     if ((systemId != null) && (systemId.startsWith("urn:publicid:"))) {
/* 1321 */       systemId = PublicId.decodeURN(systemId);
/* 1322 */       if ((publicId != null) && (!publicId.equals(systemId))) {
/* 1323 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1324 */         systemId = null;
/*      */       } else {
/* 1326 */         publicId = systemId;
/* 1327 */         systemId = null;
/*      */       }
/*      */     }
/*      */ 
/* 1331 */     if (systemId != null)
/*      */     {
/* 1333 */       resolved = resolveLocalSystem(systemId);
/* 1334 */       if (resolved != null) {
/* 1335 */         return resolved;
/*      */       }
/*      */     }
/*      */ 
/* 1339 */     if (publicId != null)
/*      */     {
/* 1341 */       resolved = resolveLocalPublic(ENTITY, entityName, publicId, systemId);
/*      */ 
/* 1345 */       if (resolved != null) {
/* 1346 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1351 */     boolean over = this.default_override;
/* 1352 */     Enumeration en = this.catalogEntries.elements();
/* 1353 */     while (en.hasMoreElements()) {
/* 1354 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1355 */       if (e.getEntryType() == OVERRIDE) {
/* 1356 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */       }
/* 1360 */       else if ((e.getEntryType() == ENTITY) && (e.getEntryArg(0).equals(entityName)))
/*      */       {
/* 1362 */         if ((over) || (systemId == null)) {
/* 1363 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1369 */     return resolveSubordinateCatalogs(ENTITY, entityName, publicId, systemId);
/*      */   }
/*      */ 
/*      */   public String resolveNotation(String notationName, String publicId, String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1395 */     String resolved = null;
/*      */ 
/* 1397 */     this.catalogManager.debug.message(3, "resolveNotation(" + notationName + "," + publicId + "," + systemId + ")");
/*      */ 
/* 1400 */     systemId = normalizeURI(systemId);
/*      */ 
/* 1402 */     if ((publicId != null) && (publicId.startsWith("urn:publicid:"))) {
/* 1403 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */ 
/* 1406 */     if ((systemId != null) && (systemId.startsWith("urn:publicid:"))) {
/* 1407 */       systemId = PublicId.decodeURN(systemId);
/* 1408 */       if ((publicId != null) && (!publicId.equals(systemId))) {
/* 1409 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1410 */         systemId = null;
/*      */       } else {
/* 1412 */         publicId = systemId;
/* 1413 */         systemId = null;
/*      */       }
/*      */     }
/*      */ 
/* 1417 */     if (systemId != null)
/*      */     {
/* 1419 */       resolved = resolveLocalSystem(systemId);
/* 1420 */       if (resolved != null) {
/* 1421 */         return resolved;
/*      */       }
/*      */     }
/*      */ 
/* 1425 */     if (publicId != null)
/*      */     {
/* 1427 */       resolved = resolveLocalPublic(NOTATION, notationName, publicId, systemId);
/*      */ 
/* 1431 */       if (resolved != null) {
/* 1432 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1437 */     boolean over = this.default_override;
/* 1438 */     Enumeration en = this.catalogEntries.elements();
/* 1439 */     while (en.hasMoreElements()) {
/* 1440 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1441 */       if (e.getEntryType() == OVERRIDE) {
/* 1442 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */       }
/* 1446 */       else if ((e.getEntryType() == NOTATION) && (e.getEntryArg(0).equals(notationName)))
/*      */       {
/* 1448 */         if ((over) || (systemId == null)) {
/* 1449 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1455 */     return resolveSubordinateCatalogs(NOTATION, notationName, publicId, systemId);
/*      */   }
/*      */ 
/*      */   public String resolvePublic(String publicId, String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1487 */     this.catalogManager.debug.message(3, "resolvePublic(" + publicId + "," + systemId + ")");
/*      */ 
/* 1489 */     systemId = normalizeURI(systemId);
/*      */ 
/* 1491 */     if ((publicId != null) && (publicId.startsWith("urn:publicid:"))) {
/* 1492 */       publicId = PublicId.decodeURN(publicId);
/*      */     }
/*      */ 
/* 1495 */     if ((systemId != null) && (systemId.startsWith("urn:publicid:"))) {
/* 1496 */       systemId = PublicId.decodeURN(systemId);
/* 1497 */       if ((publicId != null) && (!publicId.equals(systemId))) {
/* 1498 */         this.catalogManager.debug.message(1, "urn:publicid: system identifier differs from public identifier; using public identifier");
/* 1499 */         systemId = null;
/*      */       } else {
/* 1501 */         publicId = systemId;
/* 1502 */         systemId = null;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1507 */     if (systemId != null) {
/* 1508 */       String resolved = resolveLocalSystem(systemId);
/* 1509 */       if (resolved != null) {
/* 1510 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1515 */     String resolved = resolveLocalPublic(PUBLIC, null, publicId, systemId);
/*      */ 
/* 1519 */     if (resolved != null) {
/* 1520 */       return resolved;
/*      */     }
/*      */ 
/* 1524 */     return resolveSubordinateCatalogs(PUBLIC, null, publicId, systemId);
/*      */   }
/*      */ 
/*      */   protected synchronized String resolveLocalPublic(int entityType, String entityName, String publicId, String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1585 */     publicId = PublicId.normalize(publicId);
/*      */ 
/* 1588 */     if (systemId != null) {
/* 1589 */       String resolved = resolveLocalSystem(systemId);
/* 1590 */       if (resolved != null) {
/* 1591 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1596 */     boolean over = this.default_override;
/* 1597 */     Enumeration en = this.catalogEntries.elements();
/* 1598 */     while (en.hasMoreElements()) {
/* 1599 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1600 */       if (e.getEntryType() == OVERRIDE) {
/* 1601 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */       }
/* 1605 */       else if ((e.getEntryType() == PUBLIC) && (e.getEntryArg(0).equals(publicId)))
/*      */       {
/* 1607 */         if ((over) || (systemId == null)) {
/* 1608 */           return e.getEntryArg(1);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1614 */     over = this.default_override;
/* 1615 */     en = this.catalogEntries.elements();
/* 1616 */     Vector delCats = new Vector();
/* 1617 */     while (en.hasMoreElements()) {
/* 1618 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1619 */       if (e.getEntryType() == OVERRIDE) {
/* 1620 */         over = e.getEntryArg(0).equalsIgnoreCase("YES");
/*      */       }
/* 1624 */       else if ((e.getEntryType() == DELEGATE_PUBLIC) && ((over) || (systemId == null)))
/*      */       {
/* 1626 */         String p = e.getEntryArg(0);
/* 1627 */         if ((p.length() <= publicId.length()) && (p.equals(publicId.substring(0, p.length()))))
/*      */         {
/* 1631 */           delCats.addElement(e.getEntryArg(1));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1636 */     if (delCats.size() > 0) {
/* 1637 */       Enumeration enCats = delCats.elements();
/*      */ 
/* 1639 */       if (this.catalogManager.debug.getDebug() > 1) {
/* 1640 */         this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
/* 1641 */         while (enCats.hasMoreElements()) {
/* 1642 */           String delegatedCatalog = (String)enCats.nextElement();
/* 1643 */           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
/*      */         }
/*      */       }
/*      */ 
/* 1647 */       Catalog dcat = newCatalog();
/*      */ 
/* 1649 */       enCats = delCats.elements();
/* 1650 */       while (enCats.hasMoreElements()) {
/* 1651 */         String delegatedCatalog = (String)enCats.nextElement();
/* 1652 */         dcat.parseCatalog(delegatedCatalog);
/*      */       }
/*      */ 
/* 1655 */       return dcat.resolvePublic(publicId, null);
/*      */     }
/*      */ 
/* 1659 */     return null;
/*      */   }
/*      */ 
/*      */   public String resolveSystem(String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1683 */     this.catalogManager.debug.message(3, "resolveSystem(" + systemId + ")");
/*      */ 
/* 1685 */     systemId = normalizeURI(systemId);
/*      */ 
/* 1687 */     if ((systemId != null) && (systemId.startsWith("urn:publicid:"))) {
/* 1688 */       systemId = PublicId.decodeURN(systemId);
/* 1689 */       return resolvePublic(systemId, null);
/*      */     }
/*      */ 
/* 1693 */     if (systemId != null) {
/* 1694 */       String resolved = resolveLocalSystem(systemId);
/* 1695 */       if (resolved != null) {
/* 1696 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1701 */     return resolveSubordinateCatalogs(SYSTEM, null, null, systemId);
/*      */   }
/*      */ 
/*      */   protected String resolveLocalSystem(String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1721 */     String osname = SecuritySupport.getSystemProperty("os.name");
/* 1722 */     boolean windows = osname.indexOf("Windows") >= 0;
/* 1723 */     Enumeration en = this.catalogEntries.elements();
/* 1724 */     while (en.hasMoreElements()) {
/* 1725 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1726 */       if ((e.getEntryType() == SYSTEM) && ((e.getEntryArg(0).equals(systemId)) || ((windows) && (e.getEntryArg(0).equalsIgnoreCase(systemId)))))
/*      */       {
/* 1730 */         return e.getEntryArg(1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1735 */     en = this.catalogEntries.elements();
/* 1736 */     String startString = null;
/* 1737 */     String prefix = null;
/* 1738 */     while (en.hasMoreElements()) {
/* 1739 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/*      */ 
/* 1741 */       if (e.getEntryType() == REWRITE_SYSTEM) {
/* 1742 */         String p = e.getEntryArg(0);
/* 1743 */         if ((p.length() <= systemId.length()) && (p.equals(systemId.substring(0, p.length()))))
/*      */         {
/* 1746 */           if ((startString == null) || (p.length() > startString.length()))
/*      */           {
/* 1748 */             startString = p;
/* 1749 */             prefix = e.getEntryArg(1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1755 */     if (prefix != null)
/*      */     {
/* 1757 */       return prefix + systemId.substring(startString.length());
/*      */     }
/*      */ 
/* 1761 */     en = this.catalogEntries.elements();
/* 1762 */     String suffixString = null;
/* 1763 */     String suffixURI = null;
/* 1764 */     while (en.hasMoreElements()) {
/* 1765 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/*      */ 
/* 1767 */       if (e.getEntryType() == SYSTEM_SUFFIX) {
/* 1768 */         String p = e.getEntryArg(0);
/* 1769 */         if ((p.length() <= systemId.length()) && (systemId.endsWith(p)))
/*      */         {
/* 1772 */           if ((suffixString == null) || (p.length() > suffixString.length()))
/*      */           {
/* 1774 */             suffixString = p;
/* 1775 */             suffixURI = e.getEntryArg(1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1781 */     if (suffixURI != null)
/*      */     {
/* 1783 */       return suffixURI;
/*      */     }
/*      */ 
/* 1787 */     en = this.catalogEntries.elements();
/* 1788 */     Vector delCats = new Vector();
/* 1789 */     while (en.hasMoreElements()) {
/* 1790 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/*      */ 
/* 1792 */       if (e.getEntryType() == DELEGATE_SYSTEM) {
/* 1793 */         String p = e.getEntryArg(0);
/* 1794 */         if ((p.length() <= systemId.length()) && (p.equals(systemId.substring(0, p.length()))))
/*      */         {
/* 1798 */           delCats.addElement(e.getEntryArg(1));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1803 */     if (delCats.size() > 0) {
/* 1804 */       Enumeration enCats = delCats.elements();
/*      */ 
/* 1806 */       if (this.catalogManager.debug.getDebug() > 1) {
/* 1807 */         this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
/* 1808 */         while (enCats.hasMoreElements()) {
/* 1809 */           String delegatedCatalog = (String)enCats.nextElement();
/* 1810 */           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
/*      */         }
/*      */       }
/*      */ 
/* 1814 */       Catalog dcat = newCatalog();
/*      */ 
/* 1816 */       enCats = delCats.elements();
/* 1817 */       while (enCats.hasMoreElements()) {
/* 1818 */         String delegatedCatalog = (String)enCats.nextElement();
/* 1819 */         dcat.parseCatalog(delegatedCatalog);
/*      */       }
/*      */ 
/* 1822 */       return dcat.resolveSystem(systemId);
/*      */     }
/*      */ 
/* 1825 */     return null;
/*      */   }
/*      */ 
/*      */   public String resolveURI(String uri)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1847 */     this.catalogManager.debug.message(3, "resolveURI(" + uri + ")");
/*      */ 
/* 1849 */     uri = normalizeURI(uri);
/*      */ 
/* 1851 */     if ((uri != null) && (uri.startsWith("urn:publicid:"))) {
/* 1852 */       uri = PublicId.decodeURN(uri);
/* 1853 */       return resolvePublic(uri, null);
/*      */     }
/*      */ 
/* 1857 */     if (uri != null) {
/* 1858 */       String resolved = resolveLocalURI(uri);
/* 1859 */       if (resolved != null) {
/* 1860 */         return resolved;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1865 */     return resolveSubordinateCatalogs(URI, null, null, uri);
/*      */   }
/*      */ 
/*      */   protected String resolveLocalURI(String uri)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 1883 */     Enumeration en = this.catalogEntries.elements();
/* 1884 */     while (en.hasMoreElements()) {
/* 1885 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/* 1886 */       if ((e.getEntryType() == URI) && (e.getEntryArg(0).equals(uri)))
/*      */       {
/* 1888 */         return e.getEntryArg(1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1893 */     en = this.catalogEntries.elements();
/* 1894 */     String startString = null;
/* 1895 */     String prefix = null;
/* 1896 */     while (en.hasMoreElements()) {
/* 1897 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/*      */ 
/* 1899 */       if (e.getEntryType() == REWRITE_URI) {
/* 1900 */         String p = e.getEntryArg(0);
/* 1901 */         if ((p.length() <= uri.length()) && (p.equals(uri.substring(0, p.length()))))
/*      */         {
/* 1904 */           if ((startString == null) || (p.length() > startString.length()))
/*      */           {
/* 1906 */             startString = p;
/* 1907 */             prefix = e.getEntryArg(1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1913 */     if (prefix != null)
/*      */     {
/* 1915 */       return prefix + uri.substring(startString.length());
/*      */     }
/*      */ 
/* 1919 */     en = this.catalogEntries.elements();
/* 1920 */     String suffixString = null;
/* 1921 */     String suffixURI = null;
/* 1922 */     while (en.hasMoreElements()) {
/* 1923 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/*      */ 
/* 1925 */       if (e.getEntryType() == URI_SUFFIX) {
/* 1926 */         String p = e.getEntryArg(0);
/* 1927 */         if ((p.length() <= uri.length()) && (uri.endsWith(p)))
/*      */         {
/* 1930 */           if ((suffixString == null) || (p.length() > suffixString.length()))
/*      */           {
/* 1932 */             suffixString = p;
/* 1933 */             suffixURI = e.getEntryArg(1);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1939 */     if (suffixURI != null)
/*      */     {
/* 1941 */       return suffixURI;
/*      */     }
/*      */ 
/* 1945 */     en = this.catalogEntries.elements();
/* 1946 */     Vector delCats = new Vector();
/* 1947 */     while (en.hasMoreElements()) {
/* 1948 */       CatalogEntry e = (CatalogEntry)en.nextElement();
/*      */ 
/* 1950 */       if (e.getEntryType() == DELEGATE_URI) {
/* 1951 */         String p = e.getEntryArg(0);
/* 1952 */         if ((p.length() <= uri.length()) && (p.equals(uri.substring(0, p.length()))))
/*      */         {
/* 1956 */           delCats.addElement(e.getEntryArg(1));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1961 */     if (delCats.size() > 0) {
/* 1962 */       Enumeration enCats = delCats.elements();
/*      */ 
/* 1964 */       if (this.catalogManager.debug.getDebug() > 1) {
/* 1965 */         this.catalogManager.debug.message(2, "Switching to delegated catalog(s):");
/* 1966 */         while (enCats.hasMoreElements()) {
/* 1967 */           String delegatedCatalog = (String)enCats.nextElement();
/* 1968 */           this.catalogManager.debug.message(2, "\t" + delegatedCatalog);
/*      */         }
/*      */       }
/*      */ 
/* 1972 */       Catalog dcat = newCatalog();
/*      */ 
/* 1974 */       enCats = delCats.elements();
/* 1975 */       while (enCats.hasMoreElements()) {
/* 1976 */         String delegatedCatalog = (String)enCats.nextElement();
/* 1977 */         dcat.parseCatalog(delegatedCatalog);
/*      */       }
/*      */ 
/* 1980 */       return dcat.resolveURI(uri);
/*      */     }
/*      */ 
/* 1983 */     return null;
/*      */   }
/*      */ 
/*      */   protected synchronized String resolveSubordinateCatalogs(int entityType, String entityName, String publicId, String systemId)
/*      */     throws MalformedURLException, IOException
/*      */   {
/* 2020 */     for (int catPos = 0; catPos < this.catalogs.size(); catPos++) {
/* 2021 */       Catalog c = null;
/*      */       try
/*      */       {
/* 2024 */         c = (Catalog)this.catalogs.elementAt(catPos);
/*      */       } catch (ClassCastException e) {
/* 2026 */         String catfile = (String)this.catalogs.elementAt(catPos);
/* 2027 */         c = newCatalog();
/*      */         try
/*      */         {
/* 2030 */           c.parseCatalog(catfile);
/*      */         } catch (MalformedURLException mue) {
/* 2032 */           this.catalogManager.debug.message(1, "Malformed Catalog URL", catfile);
/*      */         } catch (FileNotFoundException fnfe) {
/* 2034 */           this.catalogManager.debug.message(1, "Failed to load catalog, file not found", catfile);
/*      */         }
/*      */         catch (IOException ioe) {
/* 2037 */           this.catalogManager.debug.message(1, "Failed to load catalog, I/O error", catfile);
/*      */         }
/*      */ 
/* 2040 */         this.catalogs.setElementAt(c, catPos);
/*      */       }
/*      */ 
/* 2043 */       String resolved = null;
/*      */ 
/* 2046 */       if (entityType == DOCTYPE) {
/* 2047 */         resolved = c.resolveDoctype(entityName, publicId, systemId);
/*      */       }
/* 2050 */       else if (entityType == DOCUMENT)
/* 2051 */         resolved = c.resolveDocument();
/* 2052 */       else if (entityType == ENTITY) {
/* 2053 */         resolved = c.resolveEntity(entityName, publicId, systemId);
/*      */       }
/* 2056 */       else if (entityType == NOTATION) {
/* 2057 */         resolved = c.resolveNotation(entityName, publicId, systemId);
/*      */       }
/* 2060 */       else if (entityType == PUBLIC)
/* 2061 */         resolved = c.resolvePublic(publicId, systemId);
/* 2062 */       else if (entityType == SYSTEM)
/* 2063 */         resolved = c.resolveSystem(systemId);
/* 2064 */       else if (entityType == URI) {
/* 2065 */         resolved = c.resolveURI(systemId);
/*      */       }
/*      */ 
/* 2068 */       if (resolved != null) {
/* 2069 */         return resolved;
/*      */       }
/*      */     }
/*      */ 
/* 2073 */     return null;
/*      */   }
/*      */ 
/*      */   protected String fixSlashes(String sysid)
/*      */   {
/* 2087 */     return sysid.replace('\\', '/');
/*      */   }
/*      */ 
/*      */   protected String makeAbsolute(String sysid)
/*      */   {
/* 2099 */     URL local = null;
/*      */ 
/* 2101 */     sysid = fixSlashes(sysid);
/*      */     try
/*      */     {
/* 2104 */       local = new URL(this.base, sysid);
/*      */     } catch (MalformedURLException e) {
/* 2106 */       this.catalogManager.debug.message(1, "Malformed URL on system identifier", sysid);
/*      */     }
/*      */ 
/* 2109 */     if (local != null) {
/* 2110 */       return local.toString();
/*      */     }
/* 2112 */     return sysid;
/*      */   }
/*      */ 
/*      */   protected String normalizeURI(String uriref)
/*      */   {
/* 2123 */     if (uriref == null) {
/* 2124 */       return null;
/*      */     }
/*      */     byte[] bytes;
/*      */     try
/*      */     {
/* 2129 */       bytes = uriref.getBytes("UTF-8");
/*      */     }
/*      */     catch (UnsupportedEncodingException uee) {
/* 2132 */       this.catalogManager.debug.message(1, "UTF-8 is an unsupported encoding!?");
/* 2133 */       return uriref;
/*      */     }
/*      */ 
/* 2136 */     StringBuilder newRef = new StringBuilder(bytes.length);
/* 2137 */     for (int count = 0; count < bytes.length; count++) {
/* 2138 */       int ch = bytes[count] & 0xFF;
/*      */ 
/* 2140 */       if ((ch <= 32) || (ch > 127) || (ch == 34) || (ch == 60) || (ch == 62) || (ch == 92) || (ch == 94) || (ch == 96) || (ch == 123) || (ch == 124) || (ch == 125) || (ch == 127))
/*      */       {
/* 2152 */         newRef.append(encodedByte(ch));
/*      */       }
/* 2154 */       else newRef.append((char)bytes[count]);
/*      */ 
/*      */     }
/*      */ 
/* 2158 */     return newRef.toString();
/*      */   }
/*      */ 
/*      */   protected String encodedByte(int b)
/*      */   {
/* 2169 */     String hex = Integer.toHexString(b).toUpperCase();
/* 2170 */     if (hex.length() < 2) {
/* 2171 */       return "%0" + hex;
/*      */     }
/* 2173 */     return "%" + hex;
/*      */   }
/*      */ 
/*      */   protected void addDelegate(CatalogEntry entry)
/*      */   {
/* 2189 */     int pos = 0;
/* 2190 */     String partial = entry.getEntryArg(0);
/*      */ 
/* 2192 */     Enumeration local = this.localDelegate.elements();
/* 2193 */     while (local.hasMoreElements()) {
/* 2194 */       CatalogEntry dpe = (CatalogEntry)local.nextElement();
/* 2195 */       String dp = dpe.getEntryArg(0);
/* 2196 */       if (dp.equals(partial))
/*      */       {
/* 2198 */         return;
/*      */       }
/* 2200 */       if (dp.length() > partial.length()) {
/* 2201 */         pos++;
/*      */       }
/* 2203 */       if (dp.length() < partial.length())
/*      */       {
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/* 2209 */     if (this.localDelegate.size() == 0)
/* 2210 */       this.localDelegate.addElement(entry);
/*      */     else
/* 2212 */       this.localDelegate.insertElementAt(entry, pos);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.Catalog
 * JD-Core Version:    0.6.2
 */
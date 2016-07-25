/*     */ package com.sun.org.apache.xml.internal.resolver;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.BootstrapResolver;
/*     */ import com.sun.org.apache.xml.internal.resolver.helpers.Debug;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.PropertyResourceBundle;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class CatalogManager
/*     */ {
/* 127 */   private static String pFiles = "xml.catalog.files";
/* 128 */   private static String pVerbosity = "xml.catalog.verbosity";
/* 129 */   private static String pPrefer = "xml.catalog.prefer";
/* 130 */   private static String pStatic = "xml.catalog.staticCatalog";
/* 131 */   private static String pAllowPI = "xml.catalog.allowPI";
/* 132 */   private static String pClassname = "xml.catalog.className";
/* 133 */   private static String pIgnoreMissing = "xml.catalog.ignoreMissing";
/*     */ 
/* 136 */   private static CatalogManager staticManager = new CatalogManager();
/*     */ 
/* 139 */   private BootstrapResolver bResolver = new BootstrapResolver();
/*     */ 
/* 142 */   private boolean ignoreMissingProperties = (SecuritySupport.getSystemProperty(pIgnoreMissing) != null) || (SecuritySupport.getSystemProperty(pFiles) != null);
/*     */   private ResourceBundle resources;
/* 150 */   private String propertyFile = "CatalogManager.properties";
/*     */ 
/* 153 */   private URL propertyFileURI = null;
/*     */ 
/* 156 */   private String defaultCatalogFiles = "./xcatalog";
/*     */ 
/* 159 */   private String catalogFiles = null;
/*     */ 
/* 162 */   private boolean fromPropertiesFile = false;
/*     */ 
/* 165 */   private int defaultVerbosity = 1;
/*     */ 
/* 168 */   private Integer verbosity = null;
/*     */ 
/* 171 */   private boolean defaultPreferPublic = true;
/*     */ 
/* 174 */   private Boolean preferPublic = null;
/*     */ 
/* 177 */   private boolean defaultUseStaticCatalog = true;
/*     */ 
/* 180 */   private Boolean useStaticCatalog = null;
/*     */ 
/* 183 */   private static Catalog staticCatalog = null;
/*     */ 
/* 186 */   private boolean defaultOasisXMLCatalogPI = true;
/*     */ 
/* 189 */   private Boolean oasisXMLCatalogPI = null;
/*     */ 
/* 192 */   private boolean defaultRelativeCatalogs = true;
/*     */ 
/* 195 */   private Boolean relativeCatalogs = null;
/*     */ 
/* 198 */   private String catalogClassName = null;
/*     */   private boolean useServicesMechanism;
/* 211 */   public Debug debug = null;
/*     */ 
/*     */   public CatalogManager()
/*     */   {
/* 215 */     init();
/*     */   }
/*     */ 
/*     */   public CatalogManager(String propertyFile)
/*     */   {
/* 220 */     this.propertyFile = propertyFile;
/* 221 */     init();
/*     */   }
/*     */ 
/*     */   private void init() {
/* 225 */     this.debug = new Debug();
/*     */ 
/* 231 */     if (System.getSecurityManager() == null)
/* 232 */       this.useServicesMechanism = true;
/*     */   }
/*     */ 
/*     */   public void setBootstrapResolver(BootstrapResolver resolver)
/*     */   {
/* 237 */     this.bResolver = resolver;
/*     */   }
/*     */ 
/*     */   public BootstrapResolver getBootstrapResolver()
/*     */   {
/* 242 */     return this.bResolver;
/*     */   }
/*     */ 
/*     */   private synchronized void readProperties()
/*     */   {
/*     */     try
/*     */     {
/* 251 */       this.propertyFileURI = CatalogManager.class.getResource("/" + this.propertyFile);
/* 252 */       InputStream in = CatalogManager.class.getResourceAsStream("/" + this.propertyFile);
/*     */ 
/* 254 */       if (in == null) {
/* 255 */         if (!this.ignoreMissingProperties) {
/* 256 */           System.err.println("Cannot find " + this.propertyFile);
/*     */ 
/* 258 */           this.ignoreMissingProperties = true;
/*     */         }
/* 260 */         return;
/*     */       }
/* 262 */       this.resources = new PropertyResourceBundle(in);
/*     */     } catch (MissingResourceException mre) {
/* 264 */       if (!this.ignoreMissingProperties)
/* 265 */         System.err.println("Cannot read " + this.propertyFile);
/*     */     }
/*     */     catch (IOException e) {
/* 268 */       if (!this.ignoreMissingProperties) {
/* 269 */         System.err.println("Failure trying to read " + this.propertyFile);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 276 */     if (this.verbosity == null)
/*     */       try {
/* 278 */         String verbStr = this.resources.getString("verbosity");
/* 279 */         int verb = Integer.parseInt(verbStr.trim());
/* 280 */         this.debug.setDebug(verb);
/* 281 */         this.verbosity = new Integer(verb);
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/*     */       }
/*     */   }
/*     */ 
/*     */   public static CatalogManager getStaticManager()
/*     */   {
/* 292 */     return staticManager;
/*     */   }
/*     */ 
/*     */   public boolean getIgnoreMissingProperties()
/*     */   {
/* 303 */     return this.ignoreMissingProperties;
/*     */   }
/*     */ 
/*     */   public void setIgnoreMissingProperties(boolean ignore)
/*     */   {
/* 314 */     this.ignoreMissingProperties = ignore;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public void ignoreMissingProperties(boolean ignore)
/*     */   {
/* 327 */     setIgnoreMissingProperties(ignore);
/*     */   }
/*     */ 
/*     */   private int queryVerbosity()
/*     */   {
/* 337 */     String defaultVerbStr = Integer.toString(this.defaultVerbosity);
/*     */ 
/* 339 */     String verbStr = SecuritySupport.getSystemProperty(pVerbosity);
/*     */ 
/* 341 */     if (verbStr == null) {
/* 342 */       if (this.resources == null) readProperties();
/* 343 */       if (this.resources != null)
/*     */         try {
/* 345 */           verbStr = this.resources.getString("verbosity");
/*     */         } catch (MissingResourceException e) {
/* 347 */           verbStr = defaultVerbStr;
/*     */         }
/*     */       else {
/* 350 */         verbStr = defaultVerbStr;
/*     */       }
/*     */     }
/*     */ 
/* 354 */     int verb = this.defaultVerbosity;
/*     */     try
/*     */     {
/* 357 */       verb = Integer.parseInt(verbStr.trim());
/*     */     } catch (Exception e) {
/* 359 */       System.err.println("Cannot parse verbosity: \"" + verbStr + "\"");
/*     */     }
/*     */ 
/* 365 */     if (this.verbosity == null) {
/* 366 */       this.debug.setDebug(verb);
/* 367 */       this.verbosity = new Integer(verb);
/*     */     }
/*     */ 
/* 370 */     return verb;
/*     */   }
/*     */ 
/*     */   public int getVerbosity()
/*     */   {
/* 377 */     if (this.verbosity == null) {
/* 378 */       this.verbosity = new Integer(queryVerbosity());
/*     */     }
/*     */ 
/* 381 */     return this.verbosity.intValue();
/*     */   }
/*     */ 
/*     */   public void setVerbosity(int verbosity)
/*     */   {
/* 388 */     this.verbosity = new Integer(verbosity);
/* 389 */     this.debug.setDebug(verbosity);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public int verbosity()
/*     */   {
/* 398 */     return getVerbosity();
/*     */   }
/*     */ 
/*     */   private boolean queryRelativeCatalogs()
/*     */   {
/* 408 */     if (this.resources == null) readProperties();
/*     */ 
/* 410 */     if (this.resources == null) return this.defaultRelativeCatalogs;
/*     */     try
/*     */     {
/* 413 */       String allow = this.resources.getString("relative-catalogs");
/* 414 */       return (allow.equalsIgnoreCase("true")) || (allow.equalsIgnoreCase("yes")) || (allow.equalsIgnoreCase("1"));
/*     */     }
/*     */     catch (MissingResourceException e) {
/*     */     }
/* 418 */     return this.defaultRelativeCatalogs;
/*     */   }
/*     */ 
/*     */   public boolean getRelativeCatalogs()
/*     */   {
/* 443 */     if (this.relativeCatalogs == null) {
/* 444 */       this.relativeCatalogs = new Boolean(queryRelativeCatalogs());
/*     */     }
/*     */ 
/* 447 */     return this.relativeCatalogs.booleanValue();
/*     */   }
/*     */ 
/*     */   public void setRelativeCatalogs(boolean relative)
/*     */   {
/* 456 */     this.relativeCatalogs = new Boolean(relative);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean relativeCatalogs()
/*     */   {
/* 465 */     return getRelativeCatalogs();
/*     */   }
/*     */ 
/*     */   private String queryCatalogFiles()
/*     */   {
/* 474 */     String catalogList = SecuritySupport.getSystemProperty(pFiles);
/* 475 */     this.fromPropertiesFile = false;
/*     */ 
/* 477 */     if (catalogList == null) {
/* 478 */       if (this.resources == null) readProperties();
/* 479 */       if (this.resources != null) {
/*     */         try {
/* 481 */           catalogList = this.resources.getString("catalogs");
/* 482 */           this.fromPropertiesFile = true;
/*     */         } catch (MissingResourceException e) {
/* 484 */           System.err.println(this.propertyFile + ": catalogs not found.");
/* 485 */           catalogList = null;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 490 */     if (catalogList == null) {
/* 491 */       catalogList = this.defaultCatalogFiles;
/*     */     }
/*     */ 
/* 494 */     return catalogList;
/*     */   }
/*     */ 
/*     */   public Vector getCatalogFiles()
/*     */   {
/* 504 */     if (this.catalogFiles == null) {
/* 505 */       this.catalogFiles = queryCatalogFiles();
/*     */     }
/*     */ 
/* 508 */     StringTokenizer files = new StringTokenizer(this.catalogFiles, ";");
/* 509 */     Vector catalogs = new Vector();
/* 510 */     while (files.hasMoreTokens()) {
/* 511 */       String catalogFile = files.nextToken();
/* 512 */       URL absURI = null;
/*     */ 
/* 514 */       if ((this.fromPropertiesFile) && (!relativeCatalogs())) {
/*     */         try {
/* 516 */           absURI = new URL(this.propertyFileURI, catalogFile);
/* 517 */           catalogFile = absURI.toString();
/*     */         } catch (MalformedURLException mue) {
/* 519 */           absURI = null;
/*     */         }
/*     */       }
/*     */ 
/* 523 */       catalogs.add(catalogFile);
/*     */     }
/*     */ 
/* 526 */     return catalogs;
/*     */   }
/*     */ 
/*     */   public void setCatalogFiles(String fileList)
/*     */   {
/* 533 */     this.catalogFiles = fileList;
/* 534 */     this.fromPropertiesFile = false;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public Vector catalogFiles()
/*     */   {
/* 546 */     return getCatalogFiles();
/*     */   }
/*     */ 
/*     */   private boolean queryPreferPublic()
/*     */   {
/* 559 */     String prefer = SecuritySupport.getSystemProperty(pPrefer);
/*     */ 
/* 561 */     if (prefer == null) {
/* 562 */       if (this.resources == null) readProperties();
/* 563 */       if (this.resources == null) return this.defaultPreferPublic; try
/*     */       {
/* 565 */         prefer = this.resources.getString("prefer");
/*     */       } catch (MissingResourceException e) {
/* 567 */         return this.defaultPreferPublic;
/*     */       }
/*     */     }
/*     */ 
/* 571 */     if (prefer == null) {
/* 572 */       return this.defaultPreferPublic;
/*     */     }
/*     */ 
/* 575 */     return prefer.equalsIgnoreCase("public");
/*     */   }
/*     */ 
/*     */   public boolean getPreferPublic()
/*     */   {
/* 584 */     if (this.preferPublic == null) {
/* 585 */       this.preferPublic = new Boolean(queryPreferPublic());
/*     */     }
/* 587 */     return this.preferPublic.booleanValue();
/*     */   }
/*     */ 
/*     */   public void setPreferPublic(boolean preferPublic)
/*     */   {
/* 594 */     this.preferPublic = new Boolean(preferPublic);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean preferPublic()
/*     */   {
/* 605 */     return getPreferPublic();
/*     */   }
/*     */ 
/*     */   private boolean queryUseStaticCatalog()
/*     */   {
/* 618 */     String staticCatalog = SecuritySupport.getSystemProperty(pStatic);
/*     */ 
/* 620 */     if (staticCatalog == null) {
/* 621 */       if (this.resources == null) readProperties();
/* 622 */       if (this.resources == null) return this.defaultUseStaticCatalog; try
/*     */       {
/* 624 */         staticCatalog = this.resources.getString("static-catalog");
/*     */       } catch (MissingResourceException e) {
/* 626 */         return this.defaultUseStaticCatalog;
/*     */       }
/*     */     }
/*     */ 
/* 630 */     if (staticCatalog == null) {
/* 631 */       return this.defaultUseStaticCatalog;
/*     */     }
/*     */ 
/* 634 */     return (staticCatalog.equalsIgnoreCase("true")) || (staticCatalog.equalsIgnoreCase("yes")) || (staticCatalog.equalsIgnoreCase("1"));
/*     */   }
/*     */ 
/*     */   public boolean getUseStaticCatalog()
/*     */   {
/* 643 */     if (this.useStaticCatalog == null) {
/* 644 */       this.useStaticCatalog = new Boolean(queryUseStaticCatalog());
/*     */     }
/*     */ 
/* 647 */     return this.useStaticCatalog.booleanValue();
/*     */   }
/*     */ 
/*     */   public void setUseStaticCatalog(boolean useStatic)
/*     */   {
/* 654 */     this.useStaticCatalog = new Boolean(useStatic);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean staticCatalog()
/*     */   {
/* 663 */     return getUseStaticCatalog();
/*     */   }
/*     */ 
/*     */   public Catalog getPrivateCatalog()
/*     */   {
/* 672 */     Catalog catalog = staticCatalog;
/*     */ 
/* 674 */     if (this.useStaticCatalog == null) {
/* 675 */       this.useStaticCatalog = new Boolean(getUseStaticCatalog());
/*     */     }
/*     */ 
/* 678 */     if ((catalog == null) || (!this.useStaticCatalog.booleanValue()))
/*     */     {
/*     */       try {
/* 681 */         String catalogClassName = getCatalogClassName();
/*     */ 
/* 683 */         if (catalogClassName == null)
/* 684 */           catalog = new Catalog();
/*     */         else {
/*     */           try {
/* 687 */             catalog = (Catalog)ReflectUtil.forName(catalogClassName).newInstance();
/*     */           } catch (ClassNotFoundException cnfe) {
/* 689 */             this.debug.message(1, "Catalog class named '" + catalogClassName + "' could not be found. Using default.");
/*     */ 
/* 692 */             catalog = new Catalog();
/*     */           } catch (ClassCastException cnfe) {
/* 694 */             this.debug.message(1, "Class named '" + catalogClassName + "' is not a Catalog. Using default.");
/*     */ 
/* 697 */             catalog = new Catalog();
/*     */           }
/*     */         }
/*     */ 
/* 701 */         catalog.setCatalogManager(this);
/* 702 */         catalog.setupReaders();
/* 703 */         catalog.loadSystemCatalogs();
/*     */       } catch (Exception ex) {
/* 705 */         ex.printStackTrace();
/*     */       }
/*     */ 
/* 708 */       if (this.useStaticCatalog.booleanValue()) {
/* 709 */         staticCatalog = catalog;
/*     */       }
/*     */     }
/*     */ 
/* 713 */     return catalog;
/*     */   }
/*     */ 
/*     */   public Catalog getCatalog()
/*     */   {
/* 723 */     Catalog catalog = staticCatalog;
/*     */ 
/* 725 */     if (this.useStaticCatalog == null) {
/* 726 */       this.useStaticCatalog = new Boolean(getUseStaticCatalog());
/*     */     }
/*     */ 
/* 729 */     if ((catalog == null) || (!this.useStaticCatalog.booleanValue())) {
/* 730 */       catalog = getPrivateCatalog();
/* 731 */       if (this.useStaticCatalog.booleanValue()) {
/* 732 */         staticCatalog = catalog;
/*     */       }
/*     */     }
/*     */ 
/* 736 */     return catalog;
/*     */   }
/*     */ 
/*     */   public boolean queryAllowOasisXMLCatalogPI()
/*     */   {
/* 749 */     String allow = SecuritySupport.getSystemProperty(pAllowPI);
/*     */ 
/* 751 */     if (allow == null) {
/* 752 */       if (this.resources == null) readProperties();
/* 753 */       if (this.resources == null) return this.defaultOasisXMLCatalogPI; try
/*     */       {
/* 755 */         allow = this.resources.getString("allow-oasis-xml-catalog-pi");
/*     */       } catch (MissingResourceException e) {
/* 757 */         return this.defaultOasisXMLCatalogPI;
/*     */       }
/*     */     }
/*     */ 
/* 761 */     if (allow == null) {
/* 762 */       return this.defaultOasisXMLCatalogPI;
/*     */     }
/*     */ 
/* 765 */     return (allow.equalsIgnoreCase("true")) || (allow.equalsIgnoreCase("yes")) || (allow.equalsIgnoreCase("1"));
/*     */   }
/*     */ 
/*     */   public boolean getAllowOasisXMLCatalogPI()
/*     */   {
/* 774 */     if (this.oasisXMLCatalogPI == null) {
/* 775 */       this.oasisXMLCatalogPI = new Boolean(queryAllowOasisXMLCatalogPI());
/*     */     }
/*     */ 
/* 778 */     return this.oasisXMLCatalogPI.booleanValue();
/*     */   }
/*     */ 
/*     */   public boolean useServicesMechanism() {
/* 782 */     return this.useServicesMechanism;
/*     */   }
/*     */ 
/*     */   public void setAllowOasisXMLCatalogPI(boolean allowPI)
/*     */   {
/* 788 */     this.oasisXMLCatalogPI = new Boolean(allowPI);
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public boolean allowOasisXMLCatalogPI()
/*     */   {
/* 797 */     return getAllowOasisXMLCatalogPI();
/*     */   }
/*     */ 
/*     */   public String queryCatalogClassName()
/*     */   {
/* 805 */     String className = SecuritySupport.getSystemProperty(pClassname);
/*     */ 
/* 807 */     if (className == null) {
/* 808 */       if (this.resources == null) readProperties();
/* 809 */       if (this.resources == null) return null; try
/*     */       {
/* 811 */         return this.resources.getString("catalog-class-name");
/*     */       } catch (MissingResourceException e) {
/* 813 */         return null;
/*     */       }
/*     */     }
/*     */ 
/* 817 */     return className;
/*     */   }
/*     */ 
/*     */   public String getCatalogClassName()
/*     */   {
/* 824 */     if (this.catalogClassName == null) {
/* 825 */       this.catalogClassName = queryCatalogClassName();
/*     */     }
/*     */ 
/* 828 */     return this.catalogClassName;
/*     */   }
/*     */ 
/*     */   public void setCatalogClassName(String className)
/*     */   {
/* 835 */     this.catalogClassName = className;
/*     */   }
/*     */ 
/*     */   /** @deprecated */
/*     */   public String catalogClassName()
/*     */   {
/* 844 */     return getCatalogClassName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.resolver.CatalogManager
 * JD-Core Version:    0.6.2
 */
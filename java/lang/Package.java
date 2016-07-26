/*     */
package java.lang;
/*     */ 
/*     */

import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.jar.Attributes;
/*     */ import java.util.jar.Attributes.Name;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import sun.net.www.ParseUtil;
/*     */ import sun.reflect.CallerSensitive;
/*     */ import sun.reflect.Reflection;

/*     */
/*     */ public class Package
/*     */ implements AnnotatedElement
/*     */ {
    /* 591 */   private static Map<String, Package> pkgs = new HashMap(31);
    /*     */
/* 594 */   private static Map<String, URL> urls = new HashMap(10);
    /*     */
/* 597 */   private static Map<String, Manifest> mans = new HashMap(10);
    /*     */   private final String pkgName;
    /*     */   private final String specTitle;
    /*     */   private final String specVersion;
    /*     */   private final String specVendor;
    /*     */   private final String implTitle;
    /*     */   private final String implVersion;
    /*     */   private final String implVendor;
    /*     */   private final URL sealBase;
    /*     */   private final transient ClassLoader loader;
    /*     */   private transient Class packageInfo;

    /*     */
/*     */
    public String getName()
/*     */ {
/* 118 */
        return this.pkgName;
/*     */
    }

    /*     */
/*     */
    public String getSpecificationTitle()
/*     */ {
/* 127 */
        return this.specTitle;
/*     */
    }

    /*     */
/*     */
    public String getSpecificationVersion()
/*     */ {
/* 140 */
        return this.specVersion;
/*     */
    }

    /*     */
/*     */
    public String getSpecificationVendor()
/*     */ {
/* 150 */
        return this.specVendor;
/*     */
    }

    /*     */
/*     */
    public String getImplementationTitle()
/*     */ {
/* 158 */
        return this.implTitle;
/*     */
    }

    /*     */
/*     */
    public String getImplementationVersion()
/*     */ {
/* 171 */
        return this.implVersion;
/*     */
    }

    /*     */
/*     */
    public String getImplementationVendor()
/*     */ {
/* 180 */
        return this.implVendor;
/*     */
    }

    /*     */
/*     */
    public boolean isSealed()
/*     */ {
/* 189 */
        return this.sealBase != null;
/*     */
    }

    /*     */
/*     */
    public boolean isSealed(URL paramURL)
/*     */ {
/* 200 */
        return paramURL.equals(this.sealBase);
/*     */
    }

    /*     */
/*     */
    public boolean isCompatibleWith(String paramString)
/*     */     throws NumberFormatException
/*     */ {
/* 228 */
        if ((this.specVersion == null) || (this.specVersion.length() < 1)) {
/* 229 */
            throw new NumberFormatException("Empty version string");
/*     */
        }
/*     */ 
/* 232 */
        String[] arrayOfString1 = this.specVersion.split("\\.", -1);
/* 233 */
        int[] arrayOfInt1 = new int[arrayOfString1.length];
/* 234 */
        for (int i = 0; i < arrayOfString1.length; i++) {
/* 235 */
            arrayOfInt1[i] = Integer.parseInt(arrayOfString1[i]);
/* 236 */
            if (arrayOfInt1[i] < 0) {
/* 237 */
                throw NumberFormatException.forInputString("" + arrayOfInt1[i]);
/*     */
            }
/*     */
        }
/* 240 */
        String[] arrayOfString2 = paramString.split("\\.", -1);
/* 241 */
        int[] arrayOfInt2 = new int[arrayOfString2.length];
/* 242 */
        for (int j = 0; j < arrayOfString2.length; j++) {
/* 243 */
            arrayOfInt2[j] = Integer.parseInt(arrayOfString2[j]);
/* 244 */
            if (arrayOfInt2[j] < 0) {
/* 245 */
                throw NumberFormatException.forInputString("" + arrayOfInt2[j]);
/*     */
            }
/*     */
        }
/* 248 */
        j = Math.max(arrayOfInt2.length, arrayOfInt1.length);
/* 249 */
        for (int k = 0; k < j; k++) {
/* 250 */
            int m = k < arrayOfInt2.length ? arrayOfInt2[k] : 0;
/* 251 */
            int n = k < arrayOfInt1.length ? arrayOfInt1[k] : 0;
/* 252 */
            if (n < m)
/* 253 */ return false;
/* 254 */
            if (n > m)
/* 255 */ return true;
/*     */
        }
/* 257 */
        return true;
/*     */
    }

    /*     */
/*     */
    @CallerSensitive
/*     */ public static Package getPackage(String paramString)
/*     */ {
/* 278 */
        ClassLoader localClassLoader = ClassLoader.getClassLoader(Reflection.getCallerClass());
/* 279 */
        if (localClassLoader != null) {
/* 280 */
            return localClassLoader.getPackage(paramString);
/*     */
        }
/* 282 */
        return getSystemPackage(paramString);
/*     */
    }

    /*     */
/*     */
    @CallerSensitive
/*     */ public static Package[] getPackages()
/*     */ {
/* 300 */
        ClassLoader localClassLoader = ClassLoader.getClassLoader(Reflection.getCallerClass());
/* 301 */
        if (localClassLoader != null) {
/* 302 */
            return localClassLoader.getPackages();
/*     */
        }
/* 304 */
        return getSystemPackages();
/*     */
    }

    /*     */
/*     */
    static Package getPackage(Class<?> paramClass)
/*     */ {
/* 326 */
        String str = paramClass.getName();
/* 327 */
        int i = str.lastIndexOf('.');
/* 328 */
        if (i != -1) {
/* 329 */
            str = str.substring(0, i);
/* 330 */
            ClassLoader localClassLoader = paramClass.getClassLoader();
/* 331 */
            if (localClassLoader != null) {
/* 332 */
                return localClassLoader.getPackage(str);
/*     */
            }
/* 334 */
            return getSystemPackage(str);
/*     */
        }
/*     */ 
/* 337 */
        return null;
/*     */
    }

    /*     */
/*     */
    public int hashCode()
/*     */ {
/* 346 */
        return this.pkgName.hashCode();
/*     */
    }

    /*     */
/*     */
    public String toString()
/*     */ {
/* 357 */
        String str1 = this.specTitle;
/* 358 */
        String str2 = this.specVersion;
/* 359 */
        if ((str1 != null) && (str1.length() > 0))
/* 360 */ str1 = ", " + str1;
/*     */
        else
/* 362 */       str1 = "";
/* 363 */
        if ((str2 != null) && (str2.length() > 0))
/* 364 */ str2 = ", version " + str2;
/*     */
        else
/* 366 */       str2 = "";
/* 367 */
        return "package " + this.pkgName + str1 + str2;
/*     */
    }

    /*     */
/*     */
    private Class<?> getPackageInfo() {
/* 371 */
        if (this.packageInfo == null) {
/*     */
            try {
/* 373 */
                this.packageInfo = Class.forName(this.pkgName + ".package-info", false, this.loader);
/*     */
            }
/*     */ catch (ClassNotFoundException localClassNotFoundException)
/*     */ {
/* 377 */
                this.packageInfo = 1 PackageInfoProxy.class;
/*     */
            }
/*     */
        }
/* 380 */
        return this.packageInfo;
/*     */
    }

    /*     */
/*     */
    public <A extends Annotation> A getAnnotation(Class<A> paramClass)
/*     */ {
/* 388 */
        return getPackageInfo().getAnnotation(paramClass);
/*     */
    }

    /*     */
/*     */
    public boolean isAnnotationPresent(Class<? extends Annotation> paramClass)
/*     */ {
/* 397 */
        return getPackageInfo().isAnnotationPresent(paramClass);
/*     */
    }

    /*     */
/*     */
    public Annotation[] getAnnotations()
/*     */ {
/* 404 */
        return getPackageInfo().getAnnotations();
/*     */
    }

    /*     */
/*     */
    public Annotation[] getDeclaredAnnotations()
/*     */ {
/* 411 */
        return getPackageInfo().getDeclaredAnnotations();
/*     */
    }

    /*     */
/*     */   Package(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, URL paramURL, ClassLoader paramClassLoader)
/*     */ {
/* 431 */
        this.pkgName = paramString1;
/* 432 */
        this.implTitle = paramString5;
/* 433 */
        this.implVersion = paramString6;
/* 434 */
        this.implVendor = paramString7;
/* 435 */
        this.specTitle = paramString2;
/* 436 */
        this.specVersion = paramString3;
/* 437 */
        this.specVendor = paramString4;
/* 438 */
        this.sealBase = paramURL;
/* 439 */
        this.loader = paramClassLoader;
/*     */
    }

    /*     */
/*     */
    private Package(String paramString, Manifest paramManifest, URL paramURL, ClassLoader paramClassLoader)
/*     */ {
/* 450 */
        String str1 = paramString.replace('.', '/').concat("/");
/* 451 */
        String str2 = null;
/* 452 */
        String str3 = null;
/* 453 */
        String str4 = null;
/* 454 */
        String str5 = null;
/* 455 */
        String str6 = null;
/* 456 */
        String str7 = null;
/* 457 */
        String str8 = null;
/* 458 */
        URL localURL = null;
/* 459 */
        Attributes localAttributes = paramManifest.getAttributes(str1);
/* 460 */
        if (localAttributes != null) {
/* 461 */
            str3 = localAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/* 462 */
            str4 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/* 463 */
            str5 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/* 464 */
            str6 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/* 465 */
            str7 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/* 466 */
            str8 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/* 467 */
            str2 = localAttributes.getValue(Attributes.Name.SEALED);
/*     */
        }
/* 469 */
        localAttributes = paramManifest.getMainAttributes();
/* 470 */
        if (localAttributes != null) {
/* 471 */
            if (str3 == null) {
/* 472 */
                str3 = localAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
/*     */
            }
/* 474 */
            if (str4 == null) {
/* 475 */
                str4 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
/*     */
            }
/* 477 */
            if (str5 == null) {
/* 478 */
                str5 = localAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
/*     */
            }
/* 480 */
            if (str6 == null) {
/* 481 */
                str6 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
/*     */
            }
/* 483 */
            if (str7 == null) {
/* 484 */
                str7 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
/*     */
            }
/* 486 */
            if (str8 == null) {
/* 487 */
                str8 = localAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
/*     */
            }
/* 489 */
            if (str2 == null) {
/* 490 */
                str2 = localAttributes.getValue(Attributes.Name.SEALED);
/*     */
            }
/*     */
        }
/* 493 */
        if ("true".equalsIgnoreCase(str2)) {
/* 494 */
            localURL = paramURL;
/*     */
        }
/* 496 */
        this.pkgName = paramString;
/* 497 */
        this.specTitle = str3;
/* 498 */
        this.specVersion = str4;
/* 499 */
        this.specVendor = str5;
/* 500 */
        this.implTitle = str6;
/* 501 */
        this.implVersion = str7;
/* 502 */
        this.implVendor = str8;
/* 503 */
        this.sealBase = localURL;
/* 504 */
        this.loader = paramClassLoader;
/*     */
    }

    /*     */
/*     */
    static Package getSystemPackage(String paramString)
/*     */ {
/* 511 */
        synchronized (pkgs) {
/* 512 */
            Package localPackage = (Package) pkgs.get(paramString);
/* 513 */
            if (localPackage == null) {
/* 514 */
                paramString = paramString.replace('.', '/').concat("/");
/* 515 */
                String str = getSystemPackage0(paramString);
/* 516 */
                if (str != null) {
/* 517 */
                    localPackage = defineSystemPackage(paramString, str);
/*     */
                }
/*     */
            }
/* 520 */
            return localPackage;
/*     */
        }
/*     */
    }

    /*     */
/*     */
    static Package[] getSystemPackages()
/*     */ {
/* 529 */
        String[] arrayOfString = getSystemPackages0();
/* 530 */
        synchronized (pkgs) {
/* 531 */
            for (int i = 0; i < arrayOfString.length; i++) {
/* 532 */
                defineSystemPackage(arrayOfString[i], getSystemPackage0(arrayOfString[i]));
/*     */
            }
/* 534 */
            return (Package[]) pkgs.values().toArray(new Package[pkgs.size()]);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    private static Package defineSystemPackage(String paramString1, final String paramString2)
/*     */ {
/* 541 */
        return (Package) AccessController.doPrivileged(new PrivilegedAction() {
            /*     */
            public Package run() {
/* 543 */
                String str = this.val$iname;
/*     */ 
/* 545 */
                URL localURL = (URL) Package.urls.get(paramString2);
/*     */
                Object localObject;
/* 546 */
                if (localURL == null)
/*     */ {
/* 548 */
                    localObject = new File(paramString2);
/*     */
                    try {
/* 550 */
                        localURL = ParseUtil.fileToEncodedURL((File) localObject);
/*     */
                    } catch (MalformedURLException localMalformedURLException) {
/*     */
                    }
/* 553 */
                    if (localURL != null) {
/* 554 */
                        Package.urls.put(paramString2, localURL);
/*     */ 
/* 556 */
                        if (((File) localObject).isFile()) {
/* 557 */
                            Package.mans.put(paramString2, Package.loadManifest(paramString2));
/*     */
                        }
/*     */
                    }
/*     */
                }
/*     */ 
/* 562 */
                str = str.substring(0, str.length() - 1).replace('/', '.');
/*     */ 
/* 564 */
                Manifest localManifest = (Manifest) Package.mans.get(paramString2);
/* 565 */
                if (localManifest != null)
/* 566 */ localObject = new Package(str, localManifest, localURL, null, null);
/*     */
                else {
/* 568 */
                    localObject = new Package(str, null, null, null, null, null, null, null, null);
/*     */
                }
/*     */ 
/* 571 */
                Package.pkgs.put(str, localObject);
/* 572 */
                return localObject;
/*     */
            }
/*     */
        });
/*     */
    }

    /*     */
/*     */
    private static Manifest loadManifest(String paramString)
/*     */ {
/*     */
        try
/*     */ {
/* 581 */
            FileInputStream localFileInputStream = new FileInputStream(paramString);
            Object localObject1 = null;
/*     */
            try {
                JarInputStream localJarInputStream = new JarInputStream(localFileInputStream, false);
/*     */ 
/* 581 */
                Object localObject2 = null;
/*     */
                try
/*     */ {
/* 584 */
                    return localJarInputStream.getManifest();
/*     */
                }
/*     */ catch (Throwable localThrowable2)
/*     */ {
/* 581 */
                    localObject2 = localThrowable2;
                    throw localThrowable2;
                } finally {
                }
            } catch (Throwable localThrowable1) {
                localObject1 = localThrowable1;
                throw localThrowable1;
/*     */
            }
/*     */ finally
/*     */ {
/* 585 */
                if (localFileInputStream != null) if (localObject1 != null) try {
                    localFileInputStream.close();
                } catch (Throwable localThrowable6) {
                    localObject1.addSuppressed(localThrowable6);
                }
                else localFileInputStream.close();
            }
        } catch (IOException localIOException) {
        }
/*     */ 
/* 586 */
        return null;
/*     */
    }

    /*     */
/*     */
    private static native String getSystemPackage0(String paramString);

    /*     */
/*     */
    private static native String[] getSystemPackages0();
/*     */
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.Package
 * JD-Core Version:    0.6.2
 */
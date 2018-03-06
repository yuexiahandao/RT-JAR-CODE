package java.nio.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.spi.CharsetProvider;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import sun.misc.ASCIICaseInsensitiveComparator;
import sun.misc.VM;
import sun.nio.cs.StandardCharsets;
import sun.nio.cs.ThreadLocalCoders;
import sun.security.action.GetPropertyAction;

public abstract class Charset
        implements Comparable<Charset> {
    private static volatile String bugLevel = null;

    private static CharsetProvider standardProvider = new StandardCharsets();

    private static volatile Object[] cache1 = null;
    private static volatile Object[] cache2 = null;

    private static ThreadLocal<ThreadLocal> gate = new ThreadLocal();
    private static volatile Charset defaultCharset;
    private final String name;
    private final String[] aliases;
    private Set<String> aliasSet = null;

    static boolean atBugLevel(String paramString) {
        String str = bugLevel;
        if (str == null) {
            if (!VM.isBooted())
                return false;
            bugLevel = str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.cs.bugLevel", ""));
        }

        return str.equals(paramString);
    }

    private static void checkName(String paramString) {
        int i = paramString.length();
        if ((!atBugLevel("1.4")) &&
                (i == 0)) {
            throw new IllegalCharsetNameException(paramString);
        }
        for (int j = 0; j < i; j++) {
            int k = paramString.charAt(j);
            if (((k < 65) || (k > 90)) &&
                    ((k < 97) || (k > 122)) &&
                    ((k < 48) || (k > 57)) &&
                    ((k != 45) || (j == 0)) &&
                    ((k != 43) || (j == 0)) &&
                    ((k != 58) || (j == 0)) &&
                    ((k != 95) || (j == 0)) && (
                    (k != 46) || (j == 0)))
                throw new IllegalCharsetNameException(paramString);
        }
    }

    private static void cache(String paramString, Charset paramCharset) {
        cache2 = cache1;
        cache1 = new Object[]{paramString, paramCharset};
    }

    private static Iterator providers() {
        return new Iterator() {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            ServiceLoader<CharsetProvider> sl = ServiceLoader.load(CharsetProvider.class, this.cl);

            Iterator<CharsetProvider> i = this.sl.iterator();

            Object next = null;

            private boolean getNext() {
                while (this.next == null) {
                    try {
                        if (!this.i.hasNext())
                            return false;
                        this.next = this.i.next();
                    } catch (ServiceConfigurationError localServiceConfigurationError) {
                    }
                    if (!(localServiceConfigurationError.getCause() instanceof SecurityException)) {
                        throw localServiceConfigurationError;
                    }
                }
                return true;
            }

            public boolean hasNext() {
                return getNext();
            }

            public Object next() {
                if (!getNext())
                    throw new NoSuchElementException();
                Object localObject = this.next;
                this.next = null;
                return localObject;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private static Charset lookupViaProviders(String paramString) {
        if (!VM.isBooted()) {
            return null;
        }
        if (gate.get() != null) {
            return null;
        }
        try {
            gate.set(gate);

            return (Charset) AccessController.doPrivileged(new PrivilegedAction() {
                public Charset run() {
                    for (Iterator localIterator = Charset.access$000(); localIterator.hasNext(); ) {
                        CharsetProvider localCharsetProvider = (CharsetProvider) localIterator.next();
                        Charset localCharset = localCharsetProvider.charsetForName(this.val$charsetName);
                        if (localCharset != null)
                            return localCharset;
                    }
                    return null;
                }
            });
        } finally {
            gate.set(null);
        }
    }

    private static Charset lookupExtendedCharset(String paramString) {
        CharsetProvider localCharsetProvider = ExtendedProviderHolder.extendedProvider;
        return localCharsetProvider != null ? localCharsetProvider.charsetForName(paramString) : null;
    }

    private static Charset lookup(String paramString) {
        if (paramString == null)
            throw new IllegalArgumentException("Null charset name");
        Object[] arrayOfObject;
        if (((arrayOfObject = cache1) != null) && (paramString.equals(arrayOfObject[0]))) {
            return (Charset) arrayOfObject[1];
        }

        return lookup2(paramString);
    }

    private static Charset lookup2(String paramString) {
        Object[] arrayOfObject;
        if (((arrayOfObject = cache2) != null) && (paramString.equals(arrayOfObject[0]))) {
            cache2 = cache1;
            cache1 = arrayOfObject;
            return (Charset) arrayOfObject[1];
        }
        Charset localCharset;
        if (((localCharset = standardProvider.charsetForName(paramString)) != null) || ((localCharset = lookupExtendedCharset(paramString)) != null) || ((localCharset = lookupViaProviders(paramString)) != null)) {
            cache(paramString, localCharset);
            return localCharset;
        }

        checkName(paramString);
        return null;
    }

    public static boolean isSupported(String paramString) {
        return lookup(paramString) != null;
    }

    public static Charset forName(String paramString) {
        Charset localCharset = lookup(paramString);
        if (localCharset != null)
            return localCharset;
        throw new UnsupportedCharsetException(paramString);
    }

    private static void put(Iterator<Charset> paramIterator, Map<String, Charset> paramMap) {
        while (paramIterator.hasNext()) {
            Charset localCharset = (Charset) paramIterator.next();
            if (!paramMap.containsKey(localCharset.name()))
                paramMap.put(localCharset.name(), localCharset);
        }
    }

    public static SortedMap<String, Charset> availableCharsets() {
        return (SortedMap) AccessController.doPrivileged(new PrivilegedAction() {
            public SortedMap<String, Charset> run() {
                TreeMap localTreeMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);

                Charset.put(Charset.standardProvider.charsets(), localTreeMap);
                CharsetProvider localCharsetProvider1 = Charset.ExtendedProviderHolder.extendedProvider;
                if (localCharsetProvider1 != null)
                    Charset.put(localCharsetProvider1.charsets(), localTreeMap);
                for (Iterator localIterator = Charset.access$000(); localIterator.hasNext(); ) {
                    CharsetProvider localCharsetProvider2 = (CharsetProvider) localIterator.next();
                    Charset.put(localCharsetProvider2.charsets(), localTreeMap);
                }
                return Collections.unmodifiableSortedMap(localTreeMap);
            }
        });
    }

    public static Charset defaultCharset() {
        if (defaultCharset == null) {
            synchronized (Charset.class) {
                String str = (String) AccessController.doPrivileged(new GetPropertyAction("file.encoding"));

                Charset localCharset = lookup(str);
                if (localCharset != null)
                    defaultCharset = localCharset;
                else
                    defaultCharset = forName("UTF-8");
            }
        }
        return defaultCharset;
    }

    protected Charset(String paramString, String[] paramArrayOfString) {
        checkName(paramString);
        String[] arrayOfString = paramArrayOfString == null ? new String[0] : paramArrayOfString;
        for (int i = 0; i < arrayOfString.length; i++)
            checkName(arrayOfString[i]);
        this.name = paramString;
        this.aliases = arrayOfString;
    }

    public final String name() {
        return this.name;
    }

    public final Set<String> aliases() {
        if (this.aliasSet != null)
            return this.aliasSet;
        int i = this.aliases.length;
        HashSet localHashSet = new HashSet(i);
        for (int j = 0; j < i; j++)
            localHashSet.add(this.aliases[j]);
        this.aliasSet = Collections.unmodifiableSet(localHashSet);
        return this.aliasSet;
    }

    public String displayName() {
        return this.name;
    }

    public final boolean isRegistered() {
        return (!this.name.startsWith("X-")) && (!this.name.startsWith("x-"));
    }

    public String displayName(Locale paramLocale) {
        return this.name;
    }

    public abstract boolean contains(Charset paramCharset);

    public abstract CharsetDecoder newDecoder();

    public abstract CharsetEncoder newEncoder();

    public boolean canEncode() {
        return true;
    }

    public final CharBuffer decode(ByteBuffer paramByteBuffer) {
        try {
            return ThreadLocalCoders.decoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(paramByteBuffer);
        } catch (CharacterCodingException localCharacterCodingException) {
            throw new Error(localCharacterCodingException);
        }
    }

    public final ByteBuffer encode(CharBuffer paramCharBuffer) {
        try {
            return ThreadLocalCoders.encoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).encode(paramCharBuffer);
        } catch (CharacterCodingException localCharacterCodingException) {
            throw new Error(localCharacterCodingException);
        }
    }

    public final ByteBuffer encode(String paramString) {
        return encode(CharBuffer.wrap(paramString));
    }

    public final int compareTo(Charset paramCharset) {
        return name().compareToIgnoreCase(paramCharset.name());
    }

    public final int hashCode() {
        return name().hashCode();
    }

    public final boolean equals(Object paramObject) {
        if (!(paramObject instanceof Charset))
            return false;
        if (this == paramObject)
            return true;
        return this.name.equals(((Charset) paramObject).name());
    }

    public final String toString() {
        return name();
    }

    private static class ExtendedProviderHolder {
        static final CharsetProvider extendedProvider = extendedProvider();

        private static CharsetProvider extendedProvider() {
            return (CharsetProvider) AccessController.doPrivileged(new PrivilegedAction() {
                public CharsetProvider run() {
                    try {
                        Class localClass = Class.forName("sun.nio.cs.ext.ExtendedCharsets");

                        return (CharsetProvider) localClass.newInstance();
                    } catch (ClassNotFoundException localClassNotFoundException) {
                    } catch (InstantiationException | IllegalAccessException localInstantiationException) {
                        throw new Error(localInstantiationException);
                    }
                    return null;
                }
            });
        }
    }
}

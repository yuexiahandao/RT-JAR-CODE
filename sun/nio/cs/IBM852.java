package sun.nio.cs;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class IBM852 extends Charset
        implements HistoricallyNamedCharset {
    private static final String b2cTable = "";
    private static final char[] b2c = "".toCharArray();
    private static final char[] c2b = new char[1024];
    private static final char[] c2bIndex = new char[256];

    public IBM852() {
        super("IBM852", StandardCharsets.aliases_IBM852);
    }

    public String historicalName() {
        return "Cp852";
    }

    public boolean contains(Charset paramCharset) {
        return paramCharset instanceof IBM852;
    }

    public CharsetDecoder newDecoder() {
        return new SingleByte.Decoder(this, b2c);
    }

    public CharsetEncoder newEncoder() {
        return new SingleByte.Encoder(this, c2b, c2bIndex);
    }

    public String getDecoderSingleByteMappings() {
        return "";
    }

    public char[] getEncoderIndex2() {
        return c2b;
    }

    public char[] getEncoderIndex1() {
        return c2bIndex;
    }

    static {
        char[] arrayOfChar1 = b2c;
        char[] arrayOfChar2 = null;
        SingleByte.initC2B(arrayOfChar1, arrayOfChar2, c2b, c2bIndex);
    }
}

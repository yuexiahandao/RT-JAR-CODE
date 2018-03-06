package sun.nio.ch;

import java.nio.channels.spi.SelectorProvider;

public class DefaultSelectorProvider {
    public static SelectorProvider create() {
        return new WindowsSelectorProvider();
    }
}

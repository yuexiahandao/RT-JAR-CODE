package java.awt.datatransfer;

import java.util.Map;

public abstract interface FlavorMap
{
  public abstract Map<DataFlavor, String> getNativesForFlavors(DataFlavor[] paramArrayOfDataFlavor);

  public abstract Map<String, DataFlavor> getFlavorsForNatives(String[] paramArrayOfString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.FlavorMap
 * JD-Core Version:    0.6.2
 */
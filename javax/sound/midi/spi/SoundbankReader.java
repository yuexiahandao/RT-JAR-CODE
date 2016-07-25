package javax.sound.midi.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Soundbank;

public abstract class SoundbankReader
{
  public abstract Soundbank getSoundbank(URL paramURL)
    throws InvalidMidiDataException, IOException;

  public abstract Soundbank getSoundbank(InputStream paramInputStream)
    throws InvalidMidiDataException, IOException;

  public abstract Soundbank getSoundbank(File paramFile)
    throws InvalidMidiDataException, IOException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.spi.SoundbankReader
 * JD-Core Version:    0.6.2
 */
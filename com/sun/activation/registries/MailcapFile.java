package com.sun.activation.registries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class MailcapFile {
    private Map type_hash = new HashMap();

    private Map fallback_hash = new HashMap();

    private Map native_commands = new HashMap();

    private static boolean addReverse = false;

    public MailcapFile(String new_fname)
            throws IOException {
        if (LogSupport.isLoggable())
            LogSupport.log("new MailcapFile: file " + new_fname);
        FileReader reader = null;
        try {
            reader = new FileReader(new_fname);
            parse(new BufferedReader(reader));
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException ex) {
                }
        }
    }

    public MailcapFile(InputStream is)
            throws IOException {
        if (LogSupport.isLoggable())
            LogSupport.log("new MailcapFile: InputStream");
        parse(new BufferedReader(new InputStreamReader(is, "iso-8859-1")));
    }

    public MailcapFile() {
        if (LogSupport.isLoggable())
            LogSupport.log("new MailcapFile: default");
    }

    public Map getMailcapList(String mime_type) {
        Map search_result = null;
        Map wildcard_result = null;

        search_result = (Map) this.type_hash.get(mime_type);

        int separator = mime_type.indexOf('/');
        String subtype = mime_type.substring(separator + 1);
        if (!subtype.equals("*")) {
            String type = mime_type.substring(0, separator + 1) + "*";
            wildcard_result = (Map) this.type_hash.get(type);

            if (wildcard_result != null) {
                if (search_result != null) {
                    search_result = mergeResults(search_result, wildcard_result);
                } else
                    search_result = wildcard_result;
            }
        }
        return search_result;
    }

    public Map getMailcapFallbackList(String mime_type) {
        Map search_result = null;
        Map wildcard_result = null;

        search_result = (Map) this.fallback_hash.get(mime_type);

        int separator = mime_type.indexOf('/');
        String subtype = mime_type.substring(separator + 1);
        if (!subtype.equals("*")) {
            String type = mime_type.substring(0, separator + 1) + "*";
            wildcard_result = (Map) this.fallback_hash.get(type);

            if (wildcard_result != null) {
                if (search_result != null) {
                    search_result = mergeResults(search_result, wildcard_result);
                } else
                    search_result = wildcard_result;
            }
        }
        return search_result;
    }

    public String[] getMimeTypes() {
        Set types = new HashSet(this.type_hash.keySet());
        types.addAll(this.fallback_hash.keySet());
        types.addAll(this.native_commands.keySet());
        String[] mts = new String[types.size()];
        mts = (String[]) types.toArray(mts);
        return mts;
    }

    public String[] getNativeCommands(String mime_type) {
        String[] cmds = null;
        List v = (List) this.native_commands.get(mime_type.toLowerCase(Locale.ENGLISH));

        if (v != null) {
            cmds = new String[v.size()];
            cmds = (String[]) v.toArray(cmds);
        }
        return cmds;
    }

    private Map mergeResults(Map first, Map second) {
        Iterator verb_enum = second.keySet().iterator();
        Map clonedHash = new HashMap(first);

        while (verb_enum.hasNext()) {
            String verb = (String) verb_enum.next();
            List cmdVector = (List) clonedHash.get(verb);
            if (cmdVector == null) {
                clonedHash.put(verb, second.get(verb));
            } else {
                List oldV = (List) second.get(verb);
                cmdVector = new ArrayList(cmdVector);
                cmdVector.addAll(oldV);
                clonedHash.put(verb, cmdVector);
            }
        }
        return clonedHash;
    }

    public void appendToMailcap(String mail_cap) {
        if (LogSupport.isLoggable())
            LogSupport.log("appendToMailcap: " + mail_cap);
        try {
            parse(new StringReader(mail_cap));
        } catch (IOException ex) {
        }
    }

    private void parse(Reader reader)
            throws IOException {
        BufferedReader buf_reader = new BufferedReader(reader);
        String line = null;
        String continued = null;

        while ((line = buf_reader.readLine()) != null) {
            line = line.trim();
            try {
                if (line.charAt(0) != '#') {
                    if (line.charAt(line.length() - 1) == '\\') {
                        if (continued != null)
                            continued = continued + line.substring(0, line.length() - 1);
                        else
                            continued = line.substring(0, line.length() - 1);
                    } else if (continued != null) {
                        continued = continued + line;
                        try {
                            parseLine(continued);
                        } catch (MailcapParseException e) {
                        }
                        continued = null;
                    } else {
                        try {
                            parseLine(line);
                        } catch (MailcapParseException e) {
                        }
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
            }
        }
    }

    protected void parseLine(String mailcapEntry)
            throws MailcapParseException, IOException {
        MailcapTokenizer tokenizer = new MailcapTokenizer(mailcapEntry);
        tokenizer.setIsAutoquoting(false);

        if (LogSupport.isLoggable()) {
            LogSupport.log("parse: " + mailcapEntry);
        }
        int currentToken = tokenizer.nextToken();
        if (currentToken != 2) {
            reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
        }

        String primaryType = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);

        String subType = "*";

        currentToken = tokenizer.nextToken();
        if ((currentToken != 47) && (currentToken != 59)) {
            reportParseError(47, 59, currentToken, tokenizer.getCurrentTokenValue());
        }

        if (currentToken == 47) {
            currentToken = tokenizer.nextToken();
            if (currentToken != 2) {
                reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
            }

            subType = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);

            currentToken = tokenizer.nextToken();
        }

        String mimeType = primaryType + "/" + subType;

        if (LogSupport.isLoggable()) {
            LogSupport.log("  Type: " + mimeType);
        }

        Map commands = new LinkedHashMap();

        if (currentToken != 59) {
            reportParseError(59, currentToken, tokenizer.getCurrentTokenValue());
        }

        tokenizer.setIsAutoquoting(true);
        currentToken = tokenizer.nextToken();
        tokenizer.setIsAutoquoting(false);
        if ((currentToken != 2) && (currentToken != 59)) {
            reportParseError(2, 59, currentToken, tokenizer.getCurrentTokenValue());
        }

        if (currentToken == 2) {
            List v = (List) this.native_commands.get(mimeType);
            if (v == null) {
                v = new ArrayList();
                v.add(mailcapEntry);
                this.native_commands.put(mimeType, v);
            } else {
                v.add(mailcapEntry);
            }

        }

        if (currentToken != 59) {
            currentToken = tokenizer.nextToken();
        }

        if (currentToken == 59) {
            boolean isFallback = false;
            do {
                currentToken = tokenizer.nextToken();
                if (currentToken != 2) {
                    reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
                }

                String paramName = tokenizer.getCurrentTokenValue().toLowerCase(Locale.ENGLISH);

                currentToken = tokenizer.nextToken();
                if ((currentToken != 61) && (currentToken != 59) && (currentToken != 5)) {
                    reportParseError(61, 59, 5, currentToken, tokenizer.getCurrentTokenValue());
                }

                if (currentToken == 61) {
                    tokenizer.setIsAutoquoting(true);
                    currentToken = tokenizer.nextToken();
                    tokenizer.setIsAutoquoting(false);
                    if (currentToken != 2) {
                        reportParseError(2, currentToken, tokenizer.getCurrentTokenValue());
                    }

                    String paramValue = tokenizer.getCurrentTokenValue();

                    if (paramName.startsWith("x-java-")) {
                        String commandName = paramName.substring(7);

                        if ((commandName.equals("fallback-entry")) && (paramValue.equalsIgnoreCase("true"))) {
                            isFallback = true;
                        } else {
                            if (LogSupport.isLoggable()) {
                                LogSupport.log("    Command: " + commandName + ", Class: " + paramValue);
                            }
                            List classes = (List) commands.get(commandName);
                            if (classes == null) {
                                classes = new ArrayList();
                                commands.put(commandName, classes);
                            }
                            if (addReverse)
                                classes.add(0, paramValue);
                            else {
                                classes.add(paramValue);
                            }
                        }
                    }

                    currentToken = tokenizer.nextToken();
                }
            }
            while (currentToken == 59);

            Map masterHash = isFallback ? this.fallback_hash : this.type_hash;
            Map curcommands = (Map) masterHash.get(mimeType);

            if (curcommands == null) {
                masterHash.put(mimeType, commands);
            } else {
                if (LogSupport.isLoggable()) {
                    LogSupport.log("Merging commands for type " + mimeType);
                }

                Iterator cn = curcommands.keySet().iterator();
                while (cn.hasNext()) {
                    String cmdName = (String) cn.next();
                    List ccv = (List) curcommands.get(cmdName);
                    List cv = (List) commands.get(cmdName);
                    if (cv != null) {
                        Iterator cvn = cv.iterator();
                        while (cvn.hasNext()) {
                            String clazz = (String) cvn.next();
                            if (!ccv.contains(clazz))
                                if (addReverse)
                                    ccv.add(0, clazz);
                                else
                                    ccv.add(clazz);
                        }
                    }
                }
                cn = commands.keySet().iterator();
                while (cn.hasNext()) {
                    String cmdName = (String) cn.next();
                    if (!curcommands.containsKey(cmdName)) {
                        List cv = (List) commands.get(cmdName);
                        curcommands.put(cmdName, cv);
                    }
                }
            }
        } else if (currentToken != 5) {
            reportParseError(5, 59, currentToken, tokenizer.getCurrentTokenValue());
        }
    }

    protected static void reportParseError(int expectedToken, int actualToken, String actualTokenValue)
            throws MailcapParseException {
        throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + " token.");
    }

    protected static void reportParseError(int expectedToken, int otherExpectedToken, int actualToken, String actualTokenValue)
            throws MailcapParseException {
        throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + " or a " + MailcapTokenizer.nameForToken(otherExpectedToken) + " token.");
    }

    protected static void reportParseError(int expectedToken, int otherExpectedToken, int anotherExpectedToken, int actualToken, String actualTokenValue)
            throws MailcapParseException {
        if (LogSupport.isLoggable()) {
            LogSupport.log("PARSE ERROR: Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + ", a " + MailcapTokenizer.nameForToken(otherExpectedToken) + ", or a " + MailcapTokenizer.nameForToken(anotherExpectedToken) + " token.");
        }

        throw new MailcapParseException("Encountered a " + MailcapTokenizer.nameForToken(actualToken) + " token (" + actualTokenValue + ") while expecting a " + MailcapTokenizer.nameForToken(expectedToken) + ", a " + MailcapTokenizer.nameForToken(otherExpectedToken) + ", or a " + MailcapTokenizer.nameForToken(anotherExpectedToken) + " token.");
    }

    static {
        try {
            addReverse = Boolean.getBoolean("javax.activation.addreverse");
        } catch (Throwable t) {
        }
    }
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.activation.registries.MailcapFile
 * JD-Core Version:    0.6.2
 */
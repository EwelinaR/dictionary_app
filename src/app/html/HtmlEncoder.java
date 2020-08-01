package app.html;

public class HtmlEncoder {

    private static final String[][] ENCRYPTION_TABLE = {
            {"&nbsp;", " "},
            {"&quot;", "\""},
            {"&amp;", "&"},
            {"&apos;", "'"},
            {"&lt;", "<"},
            {"&gt;", ">"}
    };

    public static String encode(String sentence){
        for (String[] symbol : ENCRYPTION_TABLE) {
            if (!sentence.contains("&"))
                return sentence;
            sentence = sentence.replace(symbol[0], symbol[1]);
        }
        return sentence;
    }
}

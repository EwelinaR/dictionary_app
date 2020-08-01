package app.html;

import app.model.PhraseDescription;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlManager {

    private HtmlParser parser;
    private boolean isValidPhrase;

    public void setPhrase(String phrase) {
        parser = new DikiHtmlParser(phrase);
        try {
            URL url = new URL("https://www.diki.pl/" + phrase);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            parser.setPage(br);

            is.close();
            br.close();
        } catch (IOException ie) {
            isValidPhrase = false;
        }
        isValidPhrase = parser.getOriginalPhrase() != null;
    }

    public boolean isValidPhrase() {
        return isValidPhrase;
    }

    public String getOriginalPhrase() {
        return parser.getOriginalPhrase();
    }

    public void updatePhrase(PhraseDescription phrase) {
        phrase.setOriginalPhrase(parser.getOriginalPhrase());
        phrase.setAllTranslatedPhrases(parser.getTranslatedPhrases());
        System.out.println(phrase.getOriginalPhrase().toString());
        System.out.println(phrase.getAllTranslatedPhrases().toString());
        phrase.setPronunciationImage(parser.getPronunciationImage());
        phrase.setAllPhraseImages(parser.getAllPhraseImages());
        phrase.setAllExamples(parser.getExamples());
        phrase.setAudioUrl(parser.getAudioUrl());
        phrase.updateDefaultValues();
    }
}

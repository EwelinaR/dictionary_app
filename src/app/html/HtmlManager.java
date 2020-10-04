package app.html;

import app.exceptions.InvalidPhraseException;
import app.model.PhraseDescription;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlManager {

    private HtmlParser parser;

    public void loadPage(String phrase) throws IOException, InvalidPhraseException {
        parser = new DikiHtmlParser(phrase);
        URL url = new URL("https://www.diki.pl/" + phrase);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.connect();
        if (!connection.getResponseMessage().equals("OK")) throw new InvalidPhraseException();

        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        parser.setPage(br);
        is.close();
        br.close();
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

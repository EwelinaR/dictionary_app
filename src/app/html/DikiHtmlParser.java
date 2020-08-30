package app.html;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DikiHtmlParser extends HtmlParser {

    DikiHtmlParser(String originalPhrase) {
        super(originalPhrase);
    }

    private String getPage(BufferedReader br){
        String line;
        StringBuilder sb = new StringBuilder();
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private String getPartOfPage(String page){
        List<String> contents = getTagContent(page, "div", new String[]{"class"}, new String[]{"dictionaryEntity"});
        for(String content : contents){
            if(content.contains("foreignToNativeMeanings")){
                return content;
            }
        }
        return "";
    }

    @Override
    public String getOriginalPhrase(){
        if(!isValidHtml) return null;
        try {
            String h1 = getTagContent(html, "h1", new String[]{}, new String[]{}).get(0);
            String originalPhraseBox =  getTagContent(h1, "span", new String[]{}, new String[]{}).get(0);
            return getFirstTagContent(originalPhraseBox, "a");

        }catch(IndexOutOfBoundsException | IllegalArgumentException e){
            return null;
        }
    }

    private void setOriginalPhrase(){
        String container = getTagContent(html, "h1", new String[]{}, new String[]{}).get(0);
        List<String> phrases = getTagContent(container, "span", new String[]{"class"}, new String[]{"hw"});
        if(phrases.size() == 1)
            originalPhrase = removeTagAndAttributes(phrases.get(0));
        else if(phrases.size() > 1) {
            phrases = phrases.stream().filter(phrase -> !phrase.startsWith("<a")).collect(Collectors.toList());
            if (phrases.size() > 0)
                originalPhrase = phrases.get(0);
        } else {
            isValidHtml = false;
        }
    }

    @Override
    public void setPage(BufferedReader br) {
        html = getPartOfPage(getPage(br));
        if (!html.isEmpty()) {   // TODO add more conditions
            isValidHtml = true;
            setOriginalPhrase();
        }
    }

    @Override
    public List<String> getTranslatedPhrases() {
        if(!isValidHtml) return new LinkedList<>();
        List<String> containers = getTagContent(html, "ol", new String[]{"class"}, new String[]{"foreignToNativeMeanings"});
        List<String> phrases, result = new LinkedList<>();
        for(String container : containers) {
            phrases = (getTagContent(container, "span", new String[]{"class"}, new String[]{"hw"}));
            for (String phrase : phrases) {
                result.add(removeTagAndAttributes(phrase));
            }
        }
        return result;
    }

    private int getPronunciationImageIndex(String container){
        List<String> containers = getTagContent(container, "span", new String[]{"class"}, new String[]{"hw"});
        int imageIndex = -1;
        for(String s : containers){
            imageIndex++;
            if(removeTagAndAttributes(s).contains(this.originalPhrase)){
                return imageIndex;
            }
        }
        return -1;
    }

    private String getPronunciationImageUrl(String container, int index){
        List<String> containers = getTagContent(container, "span", new String[]{"class"}, new String[]{"phoneticTranscription"});
        System.out.println(containers.size() + "   "+ index);
        List<String> tag = getTag(container, "img", new String[]{"class"}, new String[]{"absmiddle"} );
        if(tag == null || tag.size() < index){
            return "";
        }
        String url = tag.get(index);
        url = url.substring(url.indexOf("src=\"")+5);
        return url.substring(0, url.indexOf("\""));
    }

    @Override
    public Image getPronunciationImage() {
        if(!isValidHtml) return null;
        try {
            List<String> h1 = getTagContent(html, "h1", new String[]{}, new String[]{});
            int index = getPronunciationImageIndex(h1.get(0));
            if(index >= 0) {
                String url = getPronunciationImageUrl(h1.get(0), index);
                return new javafx.scene.image.Image(url);
            }
            return null;
        }catch(IndexOutOfBoundsException | IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Map<String, String> getExamples() {
        if(!isValidHtml)
            return null;
        Map<String, String> examples = new HashMap<>();
        List<String> exampleBoxes = getTagContent(html, "div", new String[]{"class"}, new String[]{"exampleSentence"});
        String example;
        String originalExample, translatedExample;
        for(String s : exampleBoxes){
            example = HtmlEncoder.encode(removeTagAndAttributes(s));
            originalExample = example.split("[ ]{10}")[0];
            try {
                translatedExample = example.split("[ ]{10}")[1].substring(1);
                translatedExample = translatedExample.substring(0, translatedExample.length() - 1);
            } catch(StringIndexOutOfBoundsException e) {
                translatedExample = "";
            }
            examples.put(originalExample, translatedExample);
        }
        return examples;
    }

    @Override
    public List<Image> getAllPhraseImages() {
        if(!isValidHtml) return null;
        List<Image> images = new ArrayList<>();

        List<String> imageTags = getTag(html, "img", new String[]{"class"}, new String[]{"pict"});
        String url;
        for(String imgUrl : imageTags){
            try {
                url = imgUrl.substring(imgUrl.indexOf("src=\"")+5);
                url = url.substring(0, url.indexOf("\""));
                if(url.startsWith("/")){
                    url = "https://www.diki.pl"+url;
                }
                images.add(new javafx.scene.image.Image(url));
            }catch(IndexOutOfBoundsException | IllegalArgumentException e){
                return images;
            }
        }
        return images;
    }

    public String getAudioUrl(){
        try {
            String url = getTag(html, "span", new String[]{"class"}, new String[]{"audioIcon icon-sound dontprint soundOnClick"}).get(0);
            url = url.substring(url.indexOf("data-audio-url=") + 16);
            return "https://www.diki.pl"+url.substring(0, url.indexOf("\""));
        }catch(Exception e){
            return "";
        }
    }
}

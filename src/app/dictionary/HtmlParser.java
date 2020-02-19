package app.dictionary;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class HtmlParser {
    protected String html;
    protected boolean isValidHtml = false;
    protected String originalPhrase;

    public HtmlParser(String originalPhrase){
        this.originalPhrase = originalPhrase;
    }
    public abstract void setPageContent(BufferedReader br);
    public abstract List<String> getTranslatedPhrases();
    public abstract Image getPronunciationImage();
    public abstract List<String> getExamples();
    public abstract List<Image> getAllPhraseImages();


    protected String getFirstTagContent(String html, String tag){
        if(!html.contains("<" + tag))
            return null;

        String container = html;
        StringBuilder result = new StringBuilder();
        int tagBeginning, tagEnding, nextMark, tagCounter = 1;
        container = container.substring(container.indexOf("<"+tag));
        container = container.substring(container.indexOf(">")+1);
        while(tagCounter > 0){
            tagBeginning = container.substring(1).indexOf("<"+tag)+1;
            tagEnding = container.substring(1).indexOf("</"+tag+">")+1;
            if(tagBeginning == 0 && tagEnding == 0)
                break;
            if(tagBeginning > 0 && (tagBeginning < tagEnding || tagEnding == 0)){
                tagCounter++;
                nextMark = tagBeginning;
            }
            else{
                tagCounter--;
                nextMark = tagEnding;
            }
            result.append(container, 0, nextMark);
            container = container.substring(nextMark);
        }
        return result.toString();
    }

    protected String removeTagAndAttributes(String text){
        String tmp;
        while(text.contains("<")){
            tmp = text.substring(0, text.indexOf("<"));
            tmp += text.substring(text.indexOf(">")+1);
            text = tmp;
        }
        return text;
    }

    private boolean isTagMatching(String tag, String[] attributes){
        for(String attribute : attributes){
            if(!tag.contains(attribute)){
                return false;
            }
        }
        return true;
    }

    private String[] getFormattedAttributedList(String[] attributes, String[] values){
        String[] attributesList = new String[attributes.length];
        for(int i = 0; i < attributesList.length; i++)
            attributesList[i] = " "+attributes[i]+"=\""+values[i]+"\"";
        return attributesList;
    }


    protected String getTag(String html, String tag, String[] attributes, String[] values){
        String[] attributesList = getFormattedAttributedList(attributes, values);
        int nextTagIndex = getFirstMatchTagIndex(html, tag, attributesList);
        String result = null;
        if(nextTagIndex != -1){
            result = html.substring(nextTagIndex);
            result = result.substring(0, result.indexOf(">")+1);
        }
        return result;
    }

    private int getFirstMatchTagIndex(String html, String tag, String[] attributes){
        String container = html;
        int matchedTagIndex = 0;
        int tagBeginning = container.indexOf("<" + tag);
        while(tagBeginning != -1) {
            container = container.substring(tagBeginning);
            matchedTagIndex += tagBeginning;
            if (isTagMatching(container.substring(0, container.indexOf(">") + 1), attributes)) {
                return matchedTagIndex;
            }
            container = container.substring(1);
            matchedTagIndex+=1;
            tagBeginning = container.indexOf("<"+tag);
        }
        return -1;
    }

    protected List<String> getTagContent(String html, String tag, String[] attributes, String[] values){
        if(attributes.length != values.length)
            return new ArrayList<>();

        String[] attributesList = getFormattedAttributedList(attributes, values);
        List<String> result = new LinkedList<>();
        String container = html;

        int nextTagIndex = getFirstMatchTagIndex(container, tag, attributesList);
        while(nextTagIndex != -1){
            container = container.substring(nextTagIndex);
            result.add(getFirstTagContent(container, tag).trim());
            container = container.substring(1);
            nextTagIndex = getFirstMatchTagIndex(container, tag, attributesList);
        }
        return result;
    }
}

package app.dictionary;

import javafx.scene.image.Image;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DikiHtmlParser extends HtmlParser {

    public DikiHtmlParser(String originalPhrase) {
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
    public void setPageContent(BufferedReader br) {
        html = getPartOfPage(getPage(br));
        if(!html.isEmpty()){
            isValidHtml = true;
        }
    }

    @Override
    public List<String> getTranslatedPhrases() {
        if(!isValidHtml) return new LinkedList<>();
        List<String> containers = getTagContent(html, "ol", new String[]{"class"}, new String[]{"foreignToNativeMeanings"});
        if(containers.size() > 0) {
            List<String> result = (getTagContent(containers.get(0), "span", new String[]{"class"}, new String[]{"hw"}));
            for(int i = 0; i < result.size(); i++){
                result.set(i, removeTagAndAttributes(result.get(i)));
            }
            return result;
        }
        return new LinkedList<>();
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
        String tag = getTag(containers.get(index), "img", new String[]{"class"}, new String[]{"absmiddle"} );
        if(tag == null){
            return "";
        }
        String url = tag.substring(tag.indexOf("src=\"")+5);
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
    public List<String> getExamples() {
        return null;
    }

    @Override
    public List<Image> getAllPhraseImages() {
        return null;
    }
}

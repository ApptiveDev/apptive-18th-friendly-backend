package apptive.team1.friendly.global.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

public class HtmlTextExtractor {

    public static String htmlToText(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.body().select("*");

        StringBuilder result = new StringBuilder();
        for (Element element : elements) {
            if (element.ownText() != null && !element.ownText().isEmpty()) {
                result.append(element.ownText() + "\n");
            }
        }

        return result.toString();
    }

    public static String removeHtmlTagsAndReplaceWithNewline(String html) {
        String noTags = Pattern.compile("<[^>]*>").matcher(html).replaceAll("\n");
        return noTags.replaceAll("\n{3,}", "\n\n");
    }
}
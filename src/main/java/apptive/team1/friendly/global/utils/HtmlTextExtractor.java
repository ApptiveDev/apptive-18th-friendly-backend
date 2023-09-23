package apptive.team1.friendly.global.utils;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
}
package project.gasnow.common.util;

public class Utility {

    public static String XSSHandling(String content){


        content = content.replaceAll("&", "&amp;");
        content = content.replaceAll("<", "&li;");
        content = content.replaceAll(">", "&et;");
        content = content.replaceAll("/", "&quot;");

        return content;
    }
}

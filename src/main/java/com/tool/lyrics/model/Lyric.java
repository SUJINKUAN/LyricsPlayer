package com.tool.lyrics.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public class Lyric {
    private long time;
    private String text;

    public static List<Lyric> valueOf(String template) {
        List<Lyric> lyrics = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\[(\\d{2}):(\\d{2}\\.\\d{2,3})])+([^\\[]*)");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String allTimestamps = matcher.group(0);
            String lyricText = matcher.group(4).trim();

            Matcher timestampMatcher = Pattern.compile("\\[(\\d{2}):(\\d{2}\\.\\d{2,3})]").matcher(allTimestamps);
            while (timestampMatcher.find()) {
                int minute = Integer.parseInt(timestampMatcher.group(1));
                double second = Double.parseDouble(timestampMatcher.group(2));
                Lyric lyric = new Lyric();
                lyric.setTime((long) ((minute * 60L + second) * 1000));
                lyric.setText(lyricText);
                lyrics.add(lyric);
            }
        }
        return lyrics;
    }
}

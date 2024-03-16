package com.tool.lyrics.model;

import lombok.Getter;
import lombok.Setter;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
public class Song {
    private int sortIndex;
    private String name;
    private List<Lyric> lyrics = new ArrayList<>();


    public static Song valueOf(File file) {
        Song song = new Song();
        song.setName(file.getName().split("\\.")[0]);
        try {
            String inputEncoding = detectCharset(file);
            System.out.println(inputEncoding);
            if (!inputEncoding.equals("UTF-8")) {
                Path tempPath = Files.createTempFile("",".txt");
                convertFileToUtf8(file, inputEncoding, tempPath.toFile());
                file = tempPath.toFile();
                System.out.println("文件编码转换完成。");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Lyric> lyrics = parseLyrics(file);
        song.setLyrics(lyrics);
        return song;
    }

    private static List<Lyric> parseLyrics(File file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            List<Lyric> totalLyrics = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                List<Lyric> lyrics = Lyric.valueOf(line);
                totalLyrics.addAll(lyrics);
            }

            if (totalLyrics.isEmpty()) {
                throw new RuntimeException("解析歌詞失敗");
            }

            totalLyrics.sort(Comparator.comparingDouble(Lyric::getTime));
            return totalLyrics;
        } catch (IOException e) {
            throw new RuntimeException("解析歌詞失敗");
        }
    }

    public static String detectCharset(File file) throws IOException {
        byte[] buf = new byte[4096];
        FileInputStream fis = new FileInputStream(file);
        UniversalDetector detector = new UniversalDetector(null);

        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        detector.dataEnd();

        String encoding = detector.getDetectedCharset();
        detector.reset();
        fis.close();

        return encoding;
    }

    /**
     * 将给定的文件从原始编码转换为UTF-8编码。
     *
     * @param inputFile     输入文件的路径。
     * @param inputEncoding 输入文件的原始编码。
     * @param outputFile    输出文件的路径。
     * @throws IOException 如果读取或写入过程中出现错误。
     */
    public static void convertFileToUtf8(File inputFile, String inputEncoding, File outputFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), inputEncoding));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
            char[] buffer = new char[4096];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, read);
            }
        }
    }


}

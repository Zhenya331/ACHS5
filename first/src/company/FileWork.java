package company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public class FileWork {
    public static String pathToDirectory = "C:\\Users\\lorgo\\Desktop\\Study\\Магистратура\\1 курс\\Средства разработки\\ACHS4\\TestDirectory\\";

    private static String getFileExtension(String fileName) {
        int index = fileName.indexOf('.');
        return index == -1? null : fileName.substring(index + 1);
    }

    public static String readFile(String fp, boolean toLower) throws UnsupportedEncodingException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try(InputStream inputStream = new FileInputStream(fp)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String content = result.toString("UTF-8");
        if (!toLower) {
            return content;
        }
        return content
                .chars()
                .map(s -> Character.isUpperCase(s) ? Character.toLowerCase(s) : Character.toLowerCase(s))
                .mapToObj(s -> (char) s)
                .map(Object::toString)
                .collect(joining());
    }

    public static ArrayList<String> GetFileNames(File folder) {
        ArrayList<String> result = new ArrayList<>();
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                result.addAll(GetFileNames(fileEntry));
            } else {
                String extension = getFileExtension(fileEntry.getPath());
                for(int i = 0; i < FileExtensions.data.length; i++) {
                    assert extension != null;
                    if (extension.equals(FileExtensions.data[i])) {
                        result.add(fileEntry.getPath());
                    }
                }
            }
        }
        return result;
    }

    public static HashMap<String, FileStatistic> GetFilesStatistic(ArrayList<String> fileNames) throws UnsupportedEncodingException {
        HashMap<String, FileStatistic> dirStatistic = new HashMap<>();
        for(String fileName: fileNames) {
            String text = FileWork.readFile(fileName, true);
            text = Symbols.Spec(text);
            Map<String, Integer> result = new HashMap<>();
            String buf = "";
            for (int i = 0; i < text.length(); i++) {
                if (Symbols.Equals(text.charAt(i))) {
                    if (!buf.equals("")) {
                        if (result.containsKey(buf)) {
                            result.put(buf, result.get(buf) + 1);
                        } else {
                            result.put(buf, 1);
                        }
                        buf = "";
                    }
                    continue;
                }
                buf += text.charAt(i);
            }
            dirStatistic.put(fileName, new FileStatistic(result, FileWork.readFile(fileName, true)));
        }
        return dirStatistic;
    }
}

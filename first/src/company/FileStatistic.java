package company;

import java.util.Map;

public class FileStatistic {
    public Map<String, Integer> statistic;
    public String text;

    public FileStatistic(Map<String, Integer> statistic, String text) {
        this.statistic = statistic;
        this.text = text;
    }
}

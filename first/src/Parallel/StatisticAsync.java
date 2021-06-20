package Parallel;

import company.FileWork;

import java.io.UnsupportedEncodingException;

public class StatisticAsync implements Runnable{
    public static boolean[] endThreads;
    private final ParallelParams params;

    public StatisticAsync(ParallelParams params) {
        this.params = params;
    }

    @Override
    public void run() {
        try {
            ParallelResult.data.set(params.numOfThread, FileWork.GetFilesStatistic(params.fileNames));
            endThreads[params.numOfThread] = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
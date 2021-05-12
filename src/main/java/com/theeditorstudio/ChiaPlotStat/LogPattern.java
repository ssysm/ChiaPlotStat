package com.theeditorstudio.ChiaPlotStat;

import java.util.regex.Pattern;

public class LogPattern {
    public static Pattern startTSPattern = Pattern.compile("(^.*)  chia.plotting.create_plots");
    public static Pattern kSizePattern = Pattern.compile("size (\\d*),");
    public static Pattern bufferSizePattern = Pattern.compile("Buffer size is: (\\d*)MiB");
    public static Pattern bucketPattern = Pattern.compile("Using (.*\\d*) buckets");
    public static Pattern threadPattern = Pattern.compile("Using (.*\\d*) threads");
    public static Pattern phasePattern = Pattern.compile("phase \\d = (\\d+\\.\\d*)", Pattern.DOTALL);
    public static Pattern copyTimePattern = Pattern.compile("Copy time = (.\\d+\\.\\d*)");
    public static Pattern totalTimePattern = Pattern.compile("Total time = (.\\d*.\\d*)");
    public static Pattern workingSpacePattern = Pattern.compile("Approximate working space used \\(without final file\\): (\\d*.*) GiB");
    public static Pattern finalFileSize = Pattern.compile("Final File size: (\\d*.*) GiB");
}

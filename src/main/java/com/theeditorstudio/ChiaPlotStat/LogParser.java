package com.theeditorstudio.ChiaPlotStat;

import java.time.Instant;
import java.util.Date;
import java.util.regex.Matcher;

public class LogParser {
    public static PlotSession parsePlotSession(String plotLogStr) {
        Matcher startTSMatcher = LogPattern.startTSPattern.matcher(plotLogStr),
                kSizeMatcher = LogPattern.kSizePattern.matcher(plotLogStr),
                bufferSizeMatcher = LogPattern.bufferSizePattern.matcher(plotLogStr),
                bucketSizeMatcher = LogPattern.bucketPattern.matcher(plotLogStr),
                threadSizeMatcher = LogPattern.threadPattern.matcher(plotLogStr),
                phaseTimeMatcher = LogPattern.phasePattern.matcher(plotLogStr),
                copyTimeMatcher = LogPattern.copyTimePattern.matcher(plotLogStr),
                totalTimeMatcher = LogPattern.totalTimePattern.matcher(plotLogStr),
                workingSizeMatcher = LogPattern.workingSpacePattern.matcher(plotLogStr),
                finalFileSizeMatcher = LogPattern.finalFileSize.matcher(plotLogStr);

        String startTSStr;
        int extractedKSize, extractedBufferSize, extractedBucketSize, extractedThreadSize;
        double extractedPhase1Time = 0, extractedPhase2Time = 0, extractedPhase3Time = 0, extractedPhase4Time = 0,
                extractedCopyTime, extractedTotalTime,
                extractedWorkingSpaceSize, extractedFinalFileSize;

        if(!startTSMatcher.find()){
            Application.getLogger().warning("Can not find start timestamp.");
            return null;
        }else{
            startTSStr = startTSMatcher.group(1);
        }

        if(!kSizeMatcher.find()){
            Application.getLogger().warning("Can not find kSize.");
            return null;
        }else{
            extractedKSize = Integer.parseInt(kSizeMatcher.group(1));
        }

        if(!bufferSizeMatcher.find()){
            Application.getLogger().warning("Can not find buffer size.");
            return null;
        }else{
            extractedBufferSize = Integer.parseInt(bufferSizeMatcher.group(1));
        }

        if(!bucketSizeMatcher.find()){
            Application.getLogger().warning("Can not find bucket size.");
            return null;
        }else{
            extractedBucketSize = Integer.parseInt(bucketSizeMatcher.group(1));
        }
        if(!threadSizeMatcher.find()){
            Application.getLogger().warning("Can not find thread size.");
            return null;
        }else{
            extractedThreadSize = Integer.parseInt(threadSizeMatcher.group(1));
        }

        if(!copyTimeMatcher.find()){
            extractedCopyTime = 0;
            Application.getLogger().info("Copy time does not exist, went to 0");
        }else{
            extractedCopyTime = Double.parseDouble(copyTimeMatcher.group(1));
        }

        if(!totalTimeMatcher.find()){
            extractedTotalTime = 0;
            Application.getLogger().info("total time does not exist, went to 0");
        }else{
            extractedTotalTime = Double.parseDouble(totalTimeMatcher.group(1));
        }

        if(!workingSizeMatcher.find()){
            extractedWorkingSpaceSize = 0;
            Application.getLogger().info("Working size does not exist, went to 0 GiB");
        }else{
            extractedWorkingSpaceSize = Double.parseDouble(workingSizeMatcher.group(1));
        }

        if(!finalFileSizeMatcher.find()){
            extractedFinalFileSize = 0;
            Application.getLogger().info("Working size does not exist, went to 0 GiB");
        }else{
            extractedFinalFileSize = Double.parseDouble(finalFileSizeMatcher.group(1));
        }

        for(int i = 1; i <= 4; i++){
            if(!phaseTimeMatcher.find()){
                continue;
            }
            switch (i){
                case 1:
                    extractedPhase1Time = Double.parseDouble(phaseTimeMatcher.group(1));
                    break;
                case 2:
                    extractedPhase2Time = Double.parseDouble(phaseTimeMatcher.group(1));
                    break;
                case 3:
                    extractedPhase3Time = Double.parseDouble(phaseTimeMatcher.group(1));
                    break;
                case 4:
                    extractedPhase4Time = Double.parseDouble(phaseTimeMatcher.group(1));
                    break;
            }
        }



        Instant ts = Instant.parse(startTSStr+'Z');
        Date startTS = Date.from(ts);

        return new PlotSession(startTS,extractedKSize,extractedThreadSize,extractedBufferSize,extractedBucketSize,
                extractedPhase1Time, extractedPhase2Time, extractedPhase3Time, extractedPhase4Time, extractedCopyTime,
                extractedTotalTime, extractedWorkingSpaceSize, extractedFinalFileSize);

    }
}

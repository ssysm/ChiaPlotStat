package com.theeditorstudio.ChiaPlotStat;

import java.io.Serializable;
import java.util.Date;

public class PlotSession implements Serializable {
    private final Date startTime;
    private final int kSize, threadCount, bufferSize, bucketSize;
    private final double phase1Time, phase2Time, phase3Time, phase4Time;
    private final double copyTime, totalTime;
    private final double workingSpace, finalFileSize;

    public PlotSession(Date startTime, int kSize, int threadCount, int bufferSize, int bucketSize, double phase1Time,
                       double phase2Time, double phase3Time, double phase4Time, double copyTime,
                       double totalTime, double workingSpace, double finalFileSize)  {
        this.startTime = startTime;
        this.kSize = kSize;
        this.threadCount = threadCount;
        this.bufferSize = bufferSize;
        this.bucketSize = bucketSize;
        this.phase1Time = phase1Time;
        this.phase2Time = phase2Time;
        this.phase3Time = phase3Time;
        this.phase4Time = phase4Time;
        this.copyTime = copyTime;
        this.totalTime = totalTime;
        this.workingSpace = workingSpace;
        this.finalFileSize = finalFileSize;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getkSize() {
        return kSize;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public int getBucketSize() {
        return bucketSize;
    }

    public double getPhase1Time() {
        return phase1Time;
    }

    public double getPhase2Time() {
        return phase2Time;
    }

    public double getPhase3Time() {
        return phase3Time;
    }

    public double getPhase4Time() {
        return phase4Time;
    }

    public double getCopyTime() {
        return copyTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getWorkingSpace() {
        return workingSpace;
    }

    public double getFinalFileSize() {
        return finalFileSize;
    }

    public boolean isCompletePlottingSession(){
        return (phase1Time > 0) && (phase2Time > 0) && (phase3Time > 0)
               && (phase4Time > 0) && (copyTime > 0) && (totalTime > 0);
    }
}

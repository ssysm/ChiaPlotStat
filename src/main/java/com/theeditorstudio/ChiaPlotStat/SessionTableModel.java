package com.theeditorstudio.ChiaPlotStat;

import javax.swing.table.AbstractTableModel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SessionTableModel extends AbstractTableModel {

    private boolean isMinimalMode = true, hideFailedPlot = true;

    private static final String[] fullFieldNames = { "Start Time", "Total Time", "Phase 1 Time", "Phase 2 Time",
            "Phase 3 Time", "Phase 4 Time", "Copy Time", "Working Space", "Final Space", "kSize", "Thread", "Buffer Size",
            "Bucket Size" };
    private static final String[] minimalFieldNames = { "Start Time", "Total Time", "Phase 1 Time", "Phase 2 Time",
            "Phase 3 Time", "Phase 4 Time", "Thread", "Buffer Size"};

    private Object[][] data = { };
    private ArrayList<PlotSession> plotSessionsBackup = new ArrayList<>();
    private DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return isMinimalMode ? minimalFieldNames.length : fullFieldNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return isMinimalMode ? minimalFieldNames[column] : fullFieldNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void populateTable(ArrayList<PlotSession> plotSessions){
        ArrayList<String[]> strObjArr = new ArrayList<>();
        plotSessionsBackup = plotSessions;
        for(PlotSession plot : plotSessions){
            if(!plot.isCompletePlottingSession() && isHideFailedPlot()){
                continue;
            }
            String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(plot.getStartTime()),
                    totalTimeMin = df.format(plot.getTotalTime()/60/60) + " Hr",
                    threadStr = String.valueOf(plot.getThreadCount()),
                    bufferStr = plot.getBufferSize() + " MiB";
            String p1Min, p2Min, p3Min, p4Min;
            if(plot.getPhase1Time()/60 > 60){
                p1Min = df.format(plot.getPhase1Time()/60/60) + " Hr";

            }else{
                p1Min = df.format((plot.getPhase1Time()/60)) + " Min";
            }
            if(plot.getPhase2Time()/60 > 60){
                p2Min = df.format(plot.getPhase2Time()/60/60) + " Hr";
            }else{
                p2Min = df.format(plot.getPhase2Time()/60) + " Min";
            }
            if(plot.getPhase3Time()/60 > 60){
                p3Min = df.format(plot.getPhase3Time()/60/60) + " Hr";
            }else{
                p3Min = df.format(plot.getPhase3Time()/60) + " Min";
            }
            if(plot.getPhase4Time()/60 > 60){
                p4Min = df.format(plot.getPhase4Time()/60/60) + " Hr";
            }else{
                p4Min = df.format(plot.getPhase4Time()/60) + " Min";
            }
            if(isMinimalMode){
                String[] plotStrArr = {dateString, totalTimeMin, p1Min,p2Min,p3Min,p4Min,threadStr,bufferStr };
                strObjArr.add(plotStrArr);
            }else{
                String copyTimeMin = df.format(plot.getCopyTime()/60) + " Min",
                        workingSpaceStr = plot.getWorkingSpace() + " GiB",
                        finalSpaceStr = plot.getFinalFileSize() + "GiB",
                        kSizeStr = String.valueOf(plot.getkSize()),
                        bucketStr = String.valueOf(plot.getBucketSize());
                String[] plotStrArr = {dateString, totalTimeMin, p1Min,p2Min,p3Min,p4Min, copyTimeMin, workingSpaceStr,
                        finalSpaceStr, kSizeStr, threadStr,bufferStr, bucketStr};
                strObjArr.add(plotStrArr);
            }
        }
        data = strObjArr.toArray(new String[0][0]);
    }

    public boolean isMinimalMode() {
        return isMinimalMode;
    }

    public void setMinimalMode(boolean minimalMode) {
        isMinimalMode = minimalMode;
    }

    public boolean isHideFailedPlot() {
        return hideFailedPlot;
    }

    public void setHideFailedPlot(boolean hideFailedPlot) {
        this.hideFailedPlot = hideFailedPlot;
    }
    
    @Override
    public void fireTableStructureChanged() {
        populateTable(plotSessionsBackup);
        super.fireTableStructureChanged();
    }
}

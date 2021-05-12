package com.theeditorstudio.ChiaPlotStat;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.swing.JOptionPane.showMessageDialog;

public class Application {

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args) {
        JMenuBar menuBar = new JMenuBar();
        JFrame frame = new JFrame("Chia Plot Stat");
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));

        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        JLabel statusLabel = new JLabel("Chia Plot Stat");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        menuBar.add(fileMenu);
        menuBar.add(optionsMenu);

        JMenuItem openDirectory = new JMenuItem("Open Directory",
                KeyEvent.VK_O);
        openDirectory.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        JMenuItem openFile = new JMenuItem("Open File",
                KeyEvent.VK_F);
        openFile.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.ALT_MASK));
        JMenuItem exitBtn = new JMenuItem("Exit",
                KeyEvent.VK_Q);
        openFile.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        fileMenu.add(openDirectory);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(exitBtn);

        JCheckBoxMenuItem toggleFailedSession = new JCheckBoxMenuItem("Include failed plotting session");
        JCheckBoxMenuItem toggleDisplayMode = new JCheckBoxMenuItem("Show more details");
        optionsMenu.add(toggleFailedSession);
        optionsMenu.add(toggleDisplayMode);

        SessionTableModel tableModel = new SessionTableModel();
        JTable table =new JTable(tableModel);
        JScrollPane scrollPane =new JScrollPane(table);

        openDirectory.addActionListener(e ->{
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int ret = fileChooser.showDialog(frame,"OK");
            if(ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
            ArrayList<PlotSession> logFiles = new ArrayList<>();
            for(final File logFile: Objects.requireNonNull(fileChooser.getSelectedFile().listFiles())){
                statusLabel.setText("Loading file :" + logFile.getName());
                String logStr = null;
                try {
                    logStr = Files.readString(logFile.getAbsoluteFile().toPath(), UTF_8);
                } catch (IOException ioException) {
                    statusLabel.setText("Error while reading log file: " + logFile.getName());
                    ioException.printStackTrace();
                }
                PlotSession parsedPlotSession = LogParser.parsePlotSession(logStr);
                if(parsedPlotSession == null){
                    statusLabel.setText("File null:" + logFile.getName());
                    continue;
                }
                logFiles.add(parsedPlotSession);
            }
            statusLabel.setText("Reading complete");
            tableModel.populateTable(logFiles);
            tableModel.fireTableDataChanged();
        });

        openFile.addActionListener(e ->{
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int ret = fileChooser.showOpenDialog(frame);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File logFile = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    String logStr = Files.readString(logFile.getAbsoluteFile().toPath(), UTF_8);
                    PlotSession parsedPlotSession = LogParser.parsePlotSession(logStr);

                    assert parsedPlotSession != null;
                    if(!parsedPlotSession.isCompletePlottingSession()){
                        showMessageDialog(frame, "You opened a failed plotting log!");
                        return;
                    }

                    ArrayList<PlotSession> plotSessions = new ArrayList<>();
                    plotSessions.add(parsedPlotSession);
                    tableModel.populateTable(plotSessions);
                    tableModel.fireTableDataChanged();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    showMessageDialog(frame, "Error while reading file!");
                }
            }
        });

        exitBtn.addActionListener(e ->{
            System.exit(0);
        });

        toggleFailedSession.addActionListener(e ->{
            tableModel.setHideFailedPlot(!tableModel.isHideFailedPlot());
            toggleFailedSession.setState(!tableModel.isHideFailedPlot());
            tableModel.fireTableStructureChanged();
        });

        toggleDisplayMode.addActionListener(e ->{
            tableModel.setMinimalMode(!tableModel.isMinimalMode());
            toggleDisplayMode.setState(!tableModel.isMinimalMode());
            tableModel.fireTableStructureChanged();
        });

        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 640);
        frame.setJMenuBar(menuBar);
        frame.add(scrollPane);
        frame.add(statusPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public static Logger getLogger() {
        return logger;
    }

}

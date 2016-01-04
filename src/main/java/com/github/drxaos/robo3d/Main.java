package com.github.drxaos.robo3d;

import com.github.drxaos.robo3d.editor.NBEditor;
import com.github.drxaos.robo3d.graphics.App;
import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.util.JmeFormatter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.Callable;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class Main {

    private static JmeCanvasContext context;
    private static Canvas canvas;
    private static App app;
    private static JFrame frame;
    private static Container generalCanvasPanel, controlsPanel;
    private static JTabbedPane tabbedPane, srcTabs;
    private static JSplitPane splitPane;
    private static NBEditor libEdit, progEdit, logs;
    private static boolean initialized = false;

    private static void createTabs() {
        tabbedPane = new JTabbedPane() {
            @Override
            public Component getComponentAt(int index) {
                if (initialized && index == 1) {
                    return splitPane;
                } else {
                    return super.getComponentAt(index);
                }
            }
        };
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    app.showCameraView(false);
                } else if (tabbedPane.getSelectedIndex() == 1) {
                    boolean done = app.showCameraView(true);
                    if (!done) {
                        tabbedPane.setSelectedIndex(0);
                    }
                }
            }
        });

        generalCanvasPanel = new JPanel();
        generalCanvasPanel.setLayout(new BorderLayout());

        {
            controlsPanel = new JPanel();
            controlsPanel.setLayout(new BorderLayout());

            libEdit = new NBEditor();
            progEdit = new NBEditor();
            srcTabs = new JTabbedPane();
            srcTabs.addTab("Library", libEdit);
            srcTabs.addTab("Program", progEdit);

            logs = new NBEditor();
            logs.setPreferredSize(new Dimension(100, 100));

            controlsPanel.add(srcTabs, BorderLayout.CENTER);
            controlsPanel.add(logs, BorderLayout.SOUTH);
        }

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, generalCanvasPanel, controlsPanel);
        splitPane.setPreferredSize(new Dimension(1024, 768));
        splitPane.setOneTouchExpandable(false);
        splitPane.setDividerLocation(768);

        Dimension minimumSize = new Dimension(100, 50);
        generalCanvasPanel.setMinimumSize(minimumSize);
        controlsPanel.setMinimumSize(minimumSize);

        tabbedPane.addTab("General", splitPane);
        tabbedPane.addTab("Camera", new JPanel());

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        initialized = true;
    }

    private static void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menuTortureMethods = new JMenu("Canvas Torture Methods");
        menuBar.add(menuTortureMethods);

        final JMenuItem itemRemoveCanvas = new JMenuItem("Remove Canvas");
        menuTortureMethods.add(itemRemoveCanvas);
        itemRemoveCanvas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemRemoveCanvas.getText().equals("Remove Canvas")) {
//                    currentPanel.remove(canvas);

                    itemRemoveCanvas.setText("Add Canvas");
                } else if (itemRemoveCanvas.getText().equals("Add Canvas")) {
//                    currentPanel.add(canvas, BorderLayout.CENTER);

                    itemRemoveCanvas.setText("Remove Canvas");
                }
            }
        });

        final JMenuItem itemHideCanvas = new JMenuItem("Hide Canvas");
        menuTortureMethods.add(itemHideCanvas);
        itemHideCanvas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemHideCanvas.getText().equals("Hide Canvas")) {
                    canvas.setVisible(false);
                    itemHideCanvas.setText("Show Canvas");
                } else if (itemHideCanvas.getText().equals("Show Canvas")) {
                    canvas.setVisible(true);
                    itemHideCanvas.setText("Hide Canvas");
                }
            }
        });

        final JMenuItem itemSwitchTab = new JMenuItem("Switch to tab #2");
        menuTortureMethods.add(itemSwitchTab);
        itemSwitchTab.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (itemSwitchTab.getText().equals("Switch to tab #2")) {
                    generalCanvasPanel.remove(canvas);
//                    cameraCanvasPanel.add(canvas, BorderLayout.CENTER);
//                    currentPanel = cameraCanvasPanel;
                    itemSwitchTab.setText("Switch to tab #1");
                } else if (itemSwitchTab.getText().equals("Switch to tab #1")) {
//                    cameraCanvasPanel.remove(canvas);
                    generalCanvasPanel.add(canvas, BorderLayout.CENTER);
//                    currentPanel = generalCanvasPanel;
                    itemSwitchTab.setText("Switch to tab #2");
                }
            }
        });

        JMenuItem itemSwitchLaf = new JMenuItem("Switch Look and Feel");
        menuTortureMethods.add(itemSwitchLaf);
        itemSwitchLaf.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                SwingUtilities.updateComponentTreeUI(frame);
                frame.pack();
            }
        });

        JMenuItem itemSmallSize = new JMenuItem("Set size to (0, 0)");
        menuTortureMethods.add(itemSmallSize);
        itemSmallSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Dimension preferred = frame.getPreferredSize();
                frame.setPreferredSize(new Dimension(0, 0));
                frame.pack();
                frame.setPreferredSize(preferred);
            }
        });

        JMenuItem itemKillCanvas = new JMenuItem("Stop/Start Canvas");
        menuTortureMethods.add(itemKillCanvas);
        itemKillCanvas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                currentPanel.remove(canvas);
                app.stop(true);

                createCanvas();
//                currentPanel.add(canvas, BorderLayout.CENTER);
                frame.pack();
                startApp();
            }
        });

        JMenuItem itemExit = new JMenuItem("Exit");
        menuTortureMethods.add(itemExit);
        itemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                frame.dispose();
                app.stop();
            }
        });
    }

    private static void createFrame() {
        frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                app.stop();
            }
        });

        createTabs();
        createMenu();
    }

    public static void createCanvas() {
        AppSettings settings = new AppSettings(true);
        settings.setWidth(800);
        settings.setHeight(768);
        settings.setFrameRate(30);
        settings.setDepthBits(24);
        settings.setBitsPerPixel(24);
        settings.setSamples(8);

        app = new App();
        app.setPauseOnLostFocus(false);
        app.setSettings(settings);
        app.createCanvas();
        app.startCanvas();

        context = (JmeCanvasContext) app.getContext();
        canvas = context.getCanvas();
        canvas.setSize(settings.getWidth(), settings.getHeight());
    }

    public static void startApp() {
        app.startCanvas();
        app.enqueue(new Callable<Void>() {
            public Void call() {
                if (app instanceof SimpleApplication) {
                    SimpleApplication simpleApp = (SimpleApplication) app;
                    //simpleApp.getFlyByCamera().setDragToRotate(true);
                }
                return null;
            }
        });

    }

    public static void main(String[] args) {
        JmeFormatter formatter = new JmeFormatter();

        Handler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(formatter);

        Logger.getLogger("").removeHandler(Logger.getLogger("").getHandlers()[0]);
        Logger.getLogger("").addHandler(consoleHandler);

        createCanvas();

        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JPopupMenu.setDefaultLightWeightPopupEnabled(false);

                createFrame();

                generalCanvasPanel.add(canvas, BorderLayout.CENTER);

                frame.pack();
                startApp();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}
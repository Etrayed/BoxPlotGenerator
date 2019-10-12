package de.etrayed.boxplotgen.visual.component;

import de.etrayed.boxplotgen.BoxPlotGenerator;
import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.Callback;
import de.etrayed.boxplotgen.visual.Window;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Predicate;

/**
 * @author Etrayed
 */
public class BPGMenuBar extends JMenuBar {

    private static final Callback<BoxPlotInfo> CREATE_CALLBACK;

    static {
        CREATE_CALLBACK = new Callback<BoxPlotInfo>() {

            @Override
            public void handleSuccess(BoxPlotInfo info) {
                BoxPlotGenerator.getInstance().getBoxPlotInfoList().add(info);
                BoxPlotGenerator.getInstance().repaintCanvas();
            }

            @Override
            public void handleException(Exception e) {
                e.printStackTrace();
            }
        };
    }

    private final JDialog scalingDialog, deleteDialog;

    private final JFileChooser fileChooser;

    private final BoxPlotCreateDialog rawCreateDialog, calculatedCreateDialog;

    private JComboBox<String> boxPlotChooser;

    public BPGMenuBar(JFrame parent) {
        this.scalingDialog = new JDialog(parent, "Skalierung");
        this.deleteDialog = new JDialog(parent, "Löschen");
        this.fileChooser = new JFileChooser();
        this.rawCreateDialog = new BoxPlotCreateDialog(parent, BoxPlotCreateDialog.RAW, CREATE_CALLBACK);
        this.calculatedCreateDialog = new BoxPlotCreateDialog(parent, BoxPlotCreateDialog.CALCULATED, CREATE_CALLBACK);

        setupScalingDialog(parent);
        setupDeleteDialog(parent);
        setupFileChooser();
        setupNewMenu();
        setupManageMenu();
    }

    private void setupScalingDialog(JFrame parent) {
        JSpinner scalingSpinner = new JSpinner(new SpinnerNumberModel(1, 0, Integer.MAX_VALUE,
                1));

        scalingDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        scalingDialog.setBounds(parent.getX() + Window.center(parent.getWidth(), 75), parent.getY()
                + Window.center(parent.getHeight(), 75), 75, 90);
        scalingDialog.setResizable(false);
        scalingDialog.setLayout(null);

        scalingSpinner.setSize(192, 50);
        scalingSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                BoxPlotGenerator.getInstance().setGlobalScaling((Integer) scalingSpinner.getValue());
                BoxPlotGenerator.getInstance().repaintCanvas();
            }
        });

        scalingDialog.add(scalingSpinner);
    }

    private void setupDeleteDialog(JFrame parent) {
        deleteDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        deleteDialog.setBounds(parent.getX() + Window.center(parent.getWidth(), 75), parent.getY()
                + Window.center(parent.getHeight(), 75), 75, 90);
        deleteDialog.setResizable(false);
        deleteDialog.setLayout(null);

        boxPlotChooser = new JComboBox<>();
        boxPlotChooser.setSize(192, 50);
        boxPlotChooser.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                BoxPlotGenerator.getInstance().getBoxPlotInfoList().removeIf(new Predicate<BoxPlotInfo>() {

                    @Override
                    public boolean test(BoxPlotInfo info) {
                        return info.name.equals(e.getItem());
                    }
                });
                BoxPlotGenerator.getInstance().repaintCanvas();
            }
        });

        deleteDialog.add(boxPlotChooser);
    }

    private void setupFileChooser() {
        fileChooser.resetChoosableFileFilters();
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileHidingEnabled(true);
        fileChooser.setDialogTitle("Speicherort auswählen");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Portable Network Graphics (PNG, png)",
                "PNG", "png");

        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setFileFilter(filter);
    }

    private void setupNewMenu() {
        JMenu newMenu = new JMenu("  Neu  ");

        JMenuItem newRawItem = new JMenuItem("Rohe Werte");
        JMenuItem newCalculatedItem = new JMenuItem("Ausgerechnete Werte");

        newRawItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                rawCreateDialog.setVisible(true);
            }
        });

        newCalculatedItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                calculatedCreateDialog.setVisible(true);
            }
        });

        newMenu.add(newRawItem);
        newMenu.addSeparator();
        newMenu.add(newCalculatedItem);

        this.add(newMenu);
    }

    private void setupManageMenu() {
        JMenu manageMenu = new JMenu("  Verwalten  ");
        JMenuItem removeItem = new JMenuItem("Entfernen");
        JMenuItem exportItem = new JMenuItem("Exportieren");
        JMenuItem scalingItem = new JMenuItem("Skalierung");

        removeItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boxPlotChooser.removeAllItems();

                for (BoxPlotInfo boxPlotInfo : BoxPlotGenerator.getInstance().getBoxPlotInfoList()) {
                    boxPlotChooser.addItem(boxPlotInfo.name);
                }

                deleteDialog.setVisible(true);
            }
        });

        exportItem.addActionListener(new ActionListener() {

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showSaveDialog(getParent());

                if(result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    try {
                        if(!selectedFile.exists()) {
                            selectedFile.createNewFile();
                        }

                        OutputStream outputStream = new FileOutputStream(selectedFile);

                        BoxPlotGenerator.getInstance().exportCurrentToFile(outputStream);

                        outputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        scalingItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                scalingDialog.setVisible(true);
            }
        });

        manageMenu.add(removeItem);
        manageMenu.add(exportItem);
        manageMenu.addSeparator();
        manageMenu.add(scalingItem);

        this.add(manageMenu);
    }
}

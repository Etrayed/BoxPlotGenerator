package de.etrayed.boxplotgen;

import de.etrayed.boxplotgen.plot.BoxPlotInfo;
import de.etrayed.boxplotgen.util.FileExporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Etrayed
 */
public class BoxPlotGenerator {

    private static BoxPlotGenerator instance;

    private final FileExporter fileExporter;

    private final List<BoxPlotInfo> boxPlotInfoList = new ArrayList<>();

    private BoxPlotGenerator() {
        this.fileExporter = new FileExporter();
    }

    void runCommandLoop() {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String commandLine;

            while ((commandLine = reader.readLine()) != null) {
                try {
                    if (commandLine.equalsIgnoreCase("end")) {
                        break;
                    }

                    String[] args = commandLine.split(" ");
                    String commandName = args[0];

                    args = Arrays.copyOfRange(args, 1, args.length);

                    if (args.length == 1 && args[0].equalsIgnoreCase("/?")) {
                        if (commandName.equalsIgnoreCase("addr")) {
                            System.out.println("addr value;value;value Name...");
                        } else if (commandName.equalsIgnoreCase("add")) {
                            System.out.println("add min q1 median q2 max Name...");
                        } else if (commandName.equalsIgnoreCase("remove")) {
                            System.out.println("remove Name...");
                        } else if (commandName.equalsIgnoreCase("export")) {
                            System.out.println("export scaling Path....");
                        }
                        continue;
                    }

                    if(commandName.equalsIgnoreCase("addr")) {
                        String[] rawSplit = args[0].split(";");
                        double[] values = new double[rawSplit.length];

                        for(int i = 0; i < rawSplit.length; i++) {
                            values[i] = Double.parseDouble(rawSplit[i]);
                        }

                        boxPlotInfoList.add(BoxPlotInfo.createNew(compile(args, 1), values));

                        System.out.println("Successfully added.");
                    } else if(commandName.equalsIgnoreCase("add")) {
                        boxPlotInfoList.add(BoxPlotInfo.createNew(compile(args, 5), Double.parseDouble(args[0]),
                                Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]),
                                Double.parseDouble(args[4])));

                        System.out.println("Successfully added.");
                    } else if(commandName.equalsIgnoreCase("remove")) {
                        BoxPlotInfo result = null;

                        for(BoxPlotInfo boxPlotInfo : boxPlotInfoList) {
                            if(boxPlotInfo.name.equalsIgnoreCase(args[0])) {
                                result = boxPlotInfo;
                            }
                        }

                        if(result != null) {
                            boxPlotInfoList.remove(result);

                            System.out.println("Removed " + result.name + "!");
                        } else {
                            System.out.println("Not found.");
                        }
                    } else if(commandName.equalsIgnoreCase("export")) {
                        fileExporter.export(Paths.get(compile(args, 1)), Integer.parseInt(args[0]));

                        System.out.println("Exported!");
                    } else if(commandName.equalsIgnoreCase("list")) {
                        System.out.println(boxPlotInfoList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String compile(String[] args, int offset) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = offset; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }

        if(stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return new String(stringBuilder);
    }

    public List<BoxPlotInfo> getBoxPlotInfoList() {
        return boxPlotInfoList;
    }

    public static BoxPlotGenerator getInstance() {
        return instance;
    }

    static BoxPlotGenerator newInstance() {
        return instance = new BoxPlotGenerator();
    }
}

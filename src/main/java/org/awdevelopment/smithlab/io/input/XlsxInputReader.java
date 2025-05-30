package org.awdevelopment.smithlab.io.input;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.awdevelopment.smithlab.data.*;
import org.awdevelopment.smithlab.data.experiment.Experiment;
import org.awdevelopment.smithlab.io.Header;
import org.awdevelopment.smithlab.io.HeaderType;
import org.awdevelopment.smithlab.io.Headers;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.awdevelopment.smithlab.io.exceptions.*;
import org.awdevelopment.smithlab.logging.LoggerHelper;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

public class XlsxInputReader {

    private final File xlsxFile;
    private static final int INPUT_SHEET_INDEX = 0;
    private static final double DELTA = 0.0000001;
    private final HashSet<Strain> strains;
    private final HashSet<Condition> conditions;
    private final LoggerHelper LOGGER;
    private static final Condition DEFAULT_CONDITION = new Condition("Unknown");

    public XlsxInputReader(File xlsxFile, LoggerHelper logger) {
        this.xlsxFile = xlsxFile;
        this.LOGGER = logger;
        this.strains = new HashSet<>();
        this.conditions = new HashSet<>();
    }

    public Headers readHeaders(XSSFSheet sheet) throws InvalidHeadersException {
        return XlsxHeaderReader.readHeaders(LOGGER, sheet);
    }

    private Optional<Sample> readSampleFromRow(Headers headers, XSSFRow row) {
        Optional<Condition> conditionOptional = Optional.empty();
        Optional<Strain> strainOptional = Optional.empty();
        HashSet<Timepoint> timepoints = new HashSet<>();
        int sampleNumber = -1, baseline = -1;
        XSSFCell baselineCell = null;
        for (int i = 0; i < row.getLastCellNum(); i++) {
            XSSFCell curCell = row.getCell(i);
            Optional<Header> headerOptional = headers.getHeader(i);
            if (headerOptional.isPresent()) {
                Header header = headerOptional.get();
                switch (header.type()) {
                    case HeaderType.SAMPLE_NUMBER -> {
                        try {
                            sampleNumber = readSampleNumber(curCell);
                        } catch (InvalidSampleNumberException e) {
                            LOGGER.atWarn(e + ": " + e.getMessage());
                            return Optional.empty();
                        }
                    }
                    case HeaderType.BASELINE -> {
                        try {
                            baseline = readBaseline(curCell);
                            baselineCell = curCell;
                        } catch (InvalidBaselineValueException e) { LOGGER.atWarn(e + ": " + e.getMessage()); }
                    }
                    case HeaderType.CONDITION -> {
                        try {
                            conditionOptional = Optional.of(readCondition(curCell));
                        } catch (InvalidConditionValueException e) {
                            LOGGER.atWarn(e + ": " + e.getMessage());
                            conditionOptional = Optional.empty();
                        }
                    }
                    case HeaderType.STRAIN -> {
                        try {
                            Condition condition;
                            if (conditionOptional.isEmpty()) {
                                LOGGER.atWarn("Strain cell \"" + curCell.getRawValue() + "\" is missing a condition label! Labeling with \"Unknown\"");
                                condition = DEFAULT_CONDITION;
                            }
                            else condition = conditionOptional.get();
                            strainOptional = Optional.of(readStrain(curCell, condition));
                        } catch (InvalidStrainValueException e) {
                            LOGGER.atWarn(e + ": " + e.getMessage());
                            strainOptional = Optional.empty();
                        }
                    }

                    case HeaderType.UNKNOWN -> LOGGER.atWarn("Skipping cell \"" + curCell.getRawValue() + "\" with unknown header: \"" + header.name()+  "\"");
                }
            }
            readTimepoint(headers, i, curCell, timepoints);
        }
        if (sampleNumber == -1) {
            LOGGER.atWarn("Row number "+row.getRowNum()+" missing valid sample number! Skipping...");
            return Optional.empty();
        } else if (conditionOptional.isEmpty() && strainOptional.isEmpty()) {
            LOGGER.atWarn("Row number "+row.getRowNum()+" missing at least one condition/strain label! Skipping...");
            return Optional.empty();
        } else if (timepoints.isEmpty()) {
            LOGGER.atWarn("Row number" + row.getRowNum() + " missing at least one valid timepoint! Skipping...");
            return Optional.empty();
        }
        if (baseline == -1) {
            Timepoint curFirstTimepoint = timepoints.iterator().next();
            for (Timepoint timepoint : timepoints) {
                if (curFirstTimepoint.dayNumber() > timepoint.dayNumber()) {
                    curFirstTimepoint = timepoint;
                }
            }
            baseline = curFirstTimepoint.colonies();
            baselineCell = curFirstTimepoint.originalCell();
        }
        Sample sample;
        Optional<Strain> finalStrainOptional = strainOptional;
        int finalSampleNumber = sampleNumber, finalBaseline = baseline;
        XSSFCell finalBaselineCell = baselineCell;
        sample = conditionOptional.map(condition -> finalStrainOptional.map(
                strain -> new Sample(finalSampleNumber, finalBaseline, finalBaselineCell, strain, condition, timepoints)).
                orElseGet(() -> new Sample(finalSampleNumber, finalBaseline, finalBaselineCell, condition, timepoints))).
                orElseGet(() -> new Sample(finalSampleNumber, finalBaseline, finalBaselineCell, finalStrainOptional.get(), timepoints));
        if (conditionOptional.isPresent() && strainOptional.isPresent()) {
            conditionOptional.get().addSample(sample);
            conditionOptional.get().addStrain(strainOptional.get());
            strainOptional.get().addSample(sample);
            strainOptional.get().setCondition(conditionOptional.get());
            LOGGER.atDebug( new String[] { "Added sample number: " + sampleNumber + " to condition " +
                        conditionOptional.get().getName() + " and strain " + strainOptional.get().getName(),
                        "Baseline: " + baseline,
                        "Timepoints: " + timepoints
            });
        } else if (conditionOptional.isPresent()) {
            conditionOptional.get().addSample(sample);
            LOGGER.atDebug(new String[] { "Added sample number: " + sampleNumber + " to condition " +
                        conditionOptional.get().getName(),
                        "Baseline: " + baseline,
                        "Timepoints: " + timepoints
            });
        } else {
            strainOptional.get().addSample(sample);
            LOGGER.atDebug( new String[]{ "Added sample number: " + sampleNumber + " to strain " +
                        strainOptional.get().getName(),
                        "Baseline: " + baseline,
                        "Timepoints: " + timepoints
            });
        }
        return Optional.of(sample);
    }

    private void readTimepoint(Headers headers, int i, XSSFCell curCell, HashSet<Timepoint> timepoints) {
        Optional<Day> dayOptional = headers.getDay(i);
        if (dayOptional.isPresent()) {
            Day day = dayOptional.get();
            if (day.dayColumnIndex() == 0) {
                int colIndex = curCell.getColumnIndex();
                XSSFCell neighborCell = curCell.getRow().getCell(colIndex + 1);
                try {
                    Timepoint timepoint = new Timepoint(day.day(), readColonies(curCell), readDilution(neighborCell), curCell, neighborCell);
                    timepoints.add(timepoint);
                } catch (InvalidColoniesNumberException | InvalidDilutionValueException e) {
                    LOGGER.atWarn(e + ": " + e.getMessage());
                }
            }
        }
    }

    private int readSampleNumber(XSSFCell cell) throws InvalidSampleNumberException {
        if (cell.getCellType() != CellType.NUMERIC) throw new InvalidSampleNumberException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        double cellValue = cell.getNumericCellValue();
        return (int) cellValue;
    }

    private int readBaseline(XSSFCell cell) throws InvalidBaselineValueException {
        int intCellValue = getInteger(cell);
        if (intCellValue != -1) return intCellValue;
        throw new InvalidBaselineValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());

    }

    private int readColonies(XSSFCell cell) throws InvalidColoniesNumberException {
        int intCellValue = getInteger(cell);
        if (intCellValue != -1) return intCellValue;
        throw new InvalidColoniesNumberException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
    }

    private int getInteger(XSSFCell cell) {
        CellType cellType = cell.getCellType();
        if (cellType == CellType.NUMERIC || cellType == CellType.FORMULA) {
            double cellValue = cell.getNumericCellValue();
            int intCellValue = (int) cellValue;
            if (Math.abs(cellValue - intCellValue) < DELTA) return intCellValue;
        }
        return -1;
    }

    private Dilution readDilution(XSSFCell cell) throws InvalidDilutionValueException {
        double cellValue = -1.0;
        LOGGER.atFatal("Reading dilution from cell with raw content: \""+cell.getRawValue()+"\"");
        LOGGER.atFatal("Cell string value: \""+cell.getStringCellValue()+"\"");
        LOGGER.atFatal("Cell address: "+cell.getAddress());
        LOGGER.atFatal("Cell type: "+cell.getCellType());
        if (cell.getCellType() == CellType.NUMERIC) {
            cellValue = cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            String cellContent = cell.getStringCellValue();
            if (cellContent.equalsIgnoreCase("x1000")) {
                return Dilution.x1000;
            } else if (cellContent.equalsIgnoreCase("x100")) {
                return Dilution.x100;
            } else if (cellContent.equalsIgnoreCase("x10")) {
                return Dilution.x10;
            } else if (cellContent.equalsIgnoreCase("1")) {
                return Dilution.x1000;
            } else if (cellContent.equalsIgnoreCase("0.1")) {
                return Dilution.x100;
            } else if (cellContent.equalsIgnoreCase("0.01")) {
                return Dilution.x10;
            }
        }
        if (Math.abs(cellValue - 1) < DELTA) {
            return Dilution.x1000;
        } else if (Math.abs(cellValue - 0.1) < DELTA) {
            return Dilution.x100;
        } else if (Math.abs(cellValue - 0.01) < DELTA) {
            return Dilution.x10;
        } else throw new InvalidDilutionValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());

    }

    private Condition readCondition(XSSFCell cell) throws InvalidConditionValueException {
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            String cellContent = cell.getStringCellValue();
            for (Condition condition : conditions) {
                if (condition.getName().equalsIgnoreCase(cellContent)) return condition;
            }
            Condition newCondition = new Condition(cellContent);
            conditions.add(newCondition);
            return newCondition;
        } else if (cellType == CellType.BLANK || cellType == CellType._NONE) {
            int rowIndex = cell.getRowIndex();
            int colIndex = cell.getColumnIndex();
            for (int i = rowIndex - 1; i > 1; i--) {
                XSSFCell nextCell = cell.getSheet().getRow(i).getCell(colIndex);
                if (nextCell.getCellType() == CellType.STRING) return readCondition(nextCell);
            }
            throw new InvalidConditionValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        } else {
            throw new InvalidConditionValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        }
    }

    private boolean isCellInMergedRegion(XSSFCell cell) {
        XSSFSheet sheet = cell.getSheet();
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();
        return sheet.getMergedRegions().stream().anyMatch(region -> region.isInRange(rowIndex, colIndex));
    }

    private Strain readStrain(XSSFCell cell, Condition condition) throws InvalidStrainValueException {
        int rowIndex = cell.getRowIndex();
        int colIndex = cell.getColumnIndex();
        // Check if the cell is in a merged region
        if (cell == null || isCellInMergedRegion(cell)) {
            while (cell == null || cell.getCellType() == CellType.BLANK || cell.getCellType() == CellType.ERROR) {
                if (rowIndex == 0) break;
                rowIndex--;
                cell = cell.getSheet().getRow(rowIndex).getCell(colIndex);
                rowIndex = cell.getRowIndex();
                colIndex = cell.getColumnIndex();
            }
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            String cellContent = cell.getStringCellValue();
            for (Strain strain : strains) {
                if (strain.getName().equalsIgnoreCase(cellContent)) return strain;
            }
            Strain newStrain = new Strain(cellContent);
            newStrain.setCondition(condition);
            strains.add(newStrain);
            return newStrain;
        } else if (cellType == CellType.BLANK || cellType == CellType._NONE) {
            for (int i = rowIndex - 1; i > 1; i--) {
                XSSFCell nextCell = cell.getSheet().getRow(i).getCell(colIndex);
                if (nextCell.getCellType() == CellType.STRING) return readStrain(nextCell, condition);
            }
        throw new InvalidStrainValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        } else {
            throw new InvalidStrainValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        }
    }

    public Experiment readExperimentData() throws InputFileException {
        LOGGER.atDebug("Reading in experiment data from file: "+xlsxFile);
        XSSFSheet sheet = getWorkbook().getSheetAt(INPUT_SHEET_INDEX);
        Headers headers = readHeaders(sheet);
        Experiment experiment = new Experiment();
        LOGGER.atDebug(new String[]{ "Successfully read in the following headers: ", headers.toString() });
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Optional<Sample> sampleOptional = readSampleFromRow(headers, sheet.getRow(i));
            if (sampleOptional.isPresent()) {
                LOGGER.atDebug("Got sample from row zero-indexed: "+i);
                Sample sample = sampleOptional.get();
                experiment.addSample(sample);
            }
        }
        experiment.addConditions(conditions);
        experiment.addStrains(strains);
        if (!experiment.getSamples().isEmpty()) return experiment;
        else throw new NoValidSamplesException(xlsxFile);
    }

    private XSSFWorkbook getWorkbook() throws FailedToOpenWorkbookException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile)) {
            return workbook;
        } catch (IOException | InvalidFormatException e) {
            throw new FailedToOpenWorkbookException(xlsxFile, e.getMessage());
        }
    }
}

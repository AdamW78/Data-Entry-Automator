package org.awdevelopment.smithlab.io.input;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.awdevelopment.smithlab.config.Config;
import org.awdevelopment.smithlab.data.*;
import org.awdevelopment.smithlab.io.Header;
import org.awdevelopment.smithlab.io.HeaderType;
import org.awdevelopment.smithlab.io.Headers;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.awdevelopment.smithlab.io.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;

public class XlsxInputReader {

    private final File xlsxFile;
    private final boolean verbose;
    private static final int INPUT_SHEET_INDEX = 0;
    private static final double DELTA = 0.0000001;
    private final HashSet<Strain> strains;
    private final HashSet<Condition> conditions;

    public XlsxInputReader(Config config) {
        this.xlsxFile = config.inputFile();
        this.verbose = config.verbose();
        strains = new HashSet<>();
        conditions = new HashSet<>();
    }

    public Headers readHeaders(XSSFSheet sheet) {
        return XlsxHeaderReader.readHeaders(sheet);
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
                    case HeaderType.SAMPLE_NUMBER -> sampleNumber = readSampleNumber(curCell);
                    case HeaderType.BASELINE -> {
                        baseline = readBaseline(curCell);
                        baselineCell = curCell;
                    }
                    case HeaderType.CONDITION -> conditionOptional = Optional.of(readCondition(curCell));
                    case HeaderType.STRAIN -> strainOptional = Optional.of(readStrain(curCell));
                    case HeaderType.UNKNOWN -> {
                        if (verbose) System.out.println("Skipping cell \"" + curCell.getRawValue() + "\" with unknown header: \"" + header.name()+  "\"");
                    }
                }
            }
            Optional<Day> dayOptional = headers.getDay(i);
            if (dayOptional.isPresent()) {
                Day day = dayOptional.get();
                if (day.dayColumnIndex() == 0) {
                    int colIndex = curCell.getColumnIndex();
                    XSSFCell neighborCell = curCell.getRow().getCell(colIndex + 1);
                    try {
                        Timepoint timepoint = new Timepoint(day.day(), readColonies(curCell), readDilution(neighborCell), curCell);
                        timepoints.add(timepoint);
                    } catch (InvalidColoniesNumberException | InvalidDilutionValueException e) {
                        if (verbose) System.out.println("Warning: " + e.getMessage() + " Skipping...");
                    }
                }
            }
        }
        if (sampleNumber == -1) {
            if (verbose) System.out.println("Warning: Row number "+row.getRowNum()+" missing valid sample number! Skipping...");
            return Optional.empty();
        } else if (conditionOptional.isEmpty() && strainOptional.isEmpty()) {
            if (verbose) System.out.println("Warning: Row number "+row.getRowNum()+" missing at least one condition/strain label! Skipping...");
            return Optional.empty();
        } else if (timepoints.isEmpty()) {
            if (verbose) System.out.println("Warning: Row number" + row.getRowNum() + " missing at least one valid timepoint! Skipping...");
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
        if (conditionOptional.isEmpty()) sample = new Sample(sampleNumber, baseline, baselineCell, strainOptional.get(), timepoints);
        else if (strainOptional.isEmpty()) sample = new Sample(sampleNumber, baseline, baselineCell, conditionOptional.get(), timepoints);
        else sample = new Sample(sampleNumber, baseline, baselineCell, strainOptional.get(), conditionOptional.get(), timepoints);
        if (conditionOptional.isPresent() && strainOptional.isPresent()) {
            conditionOptional.get().addSample(sample);
            conditionOptional.get().addStrain(strainOptional.get());
            strainOptional.get().addSample(sample);
            strainOptional.get().addCondition(conditionOptional.get());
            if (verbose) {
                System.out.println("Added sample number: " + sampleNumber + " to condition " +
                        conditionOptional.get().getName() + " and strain " + strainOptional.get().getName());
                System.out.println("Baseline: " + baseline);
                System.out.println("Timepoints: " + timepoints);
            }
        } else if (conditionOptional.isPresent()) {
            conditionOptional.get().addSample(sample);
            if (verbose) {
                System.out.println("Added sample number: " + sampleNumber + " to condition " +
                        conditionOptional.get().getName());
                System.out.println("Baseline: " + baseline);
                System.out.println("Timepoints: " + timepoints);
            }
        } else {
            strainOptional.get().addSample(sample);
            if (verbose) {
                System.out.println("Added sample number: " + sampleNumber + " to strain " +
                        strainOptional.get().getName());
                System.out.println("Baseline: " + baseline);
                System.out.println("Timepoints: " + timepoints);
            }
        }
        return Optional.of(sample);
    }

    private int readSampleNumber(XSSFCell cell) {
        if (cell.getCellType() != CellType.NUMERIC) throw new InvalidSampleNumberException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        double cellValue = cell.getNumericCellValue();
        return (int) cellValue;
    }

    private int readBaseline(XSSFCell cell) {
        int intCellValue = getInteger(cell);
        if (intCellValue != -1) return intCellValue;
        throw new InvalidBaselineValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());

    }

    private int readColonies(XSSFCell cell) {
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

    private Dilution readDilution(XSSFCell cell) {
        if (cell.getCellType() != CellType.NUMERIC) throw new InvalidDilutionValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        double cellValue = cell.getNumericCellValue();
        if (Math.abs(cellValue - 1) < DELTA) {
            return Dilution.x1000;
        } else if (Math.abs(cellValue - 0.1) < DELTA) {
            return Dilution.x100;
        } else if (Math.abs(cellValue - 0.01) < DELTA) {
            return Dilution.x10;
        } else throw new InvalidDilutionValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());

    }

    private Condition readCondition(XSSFCell cell) {
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

    private Strain readStrain(XSSFCell cell) {
        CellType cellType = cell.getCellType();
        if (cellType == CellType.STRING) {
            String cellContent = cell.getStringCellValue();
            for (Strain strain : strains) {
                if (strain.getName().equalsIgnoreCase(cellContent)) return strain;
            }
            Strain newStrain = new Strain(cellContent);
            strains.add(newStrain);
            return newStrain;
        } else if (cellType == CellType.BLANK || cellType == CellType._NONE) {
            int rowIndex = cell.getRowIndex();
            int colIndex = cell.getColumnIndex();
            for (int i = rowIndex - 1; i > 1; i--) {
                XSSFCell nextCell = cell.getSheet().getRow(i).getCell(colIndex);
                if (nextCell.getCellType() == CellType.STRING) return readStrain(nextCell);
            }
        throw new InvalidStrainValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        } else {
            throw new InvalidStrainValueException(cell.getRawValue(), cell.getRowIndex(), cell.getColumnIndex());
        }
    }

    public Experiment readExperimentData() {
        XSSFSheet sheet = getWorkbook().getSheetAt(INPUT_SHEET_INDEX);
        Headers headers = readHeaders(sheet);
        Experiment experiment = new Experiment();
        if (verbose) {
            System.out.println("Successfully read in the following headers: ");
            System.out.println(headers);
        }
        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Optional<Sample> sampleOptional = readSampleFromRow(headers, sheet.getRow(i));
            if(sampleOptional.isPresent()) {
                if (verbose) System.out.println("Got sample from row zero-indexed: "+i);
                Sample sample = sampleOptional.get();
                experiment.addSample(sample);
            }
        }
        experiment.addConditions(conditions);
        experiment.addStrains(strains);
        return experiment;
    }

    private XSSFWorkbook getWorkbook() {
        try (XSSFWorkbook workbook = new XSSFWorkbook(xlsxFile)) {
            return workbook;
        } catch (IOException e) {
            throw new RuntimeException("IOException: " + e.getMessage());
        } catch (InvalidFormatException e) {
            throw new RuntimeException("InvalidFormatException: " + e.getMessage());
        }
    }
}

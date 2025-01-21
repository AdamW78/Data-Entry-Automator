package org.awdevelopment.smithlab.args;

import org.awdevelopment.smithlab.args.exceptions.HelpException;
import org.awdevelopment.smithlab.args.exceptions.NoInputFileException;
import org.awdevelopment.smithlab.args.exceptions.NoSuchArgumentException;
import org.awdevelopment.smithlab.config.Mode;
import org.awdevelopment.smithlab.io.output.formats.OutputType;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentsTest {

    private static final String TEST_INPUT_PATH = "test_input/ynb.xlsx";

    @Test
    void testGetInputFile() {
        String[] args = {"-i", TEST_INPUT_PATH};
        Arguments arguments = new Arguments(args);
        assertEquals(new File(TEST_INPUT_PATH), arguments.getInputFile());
    }

    @Test
    void testGetOutputType() {
        String[] args = {"-i", TEST_INPUT_PATH, "-t", "BOTH"};
        Arguments arguments = new Arguments(args);
        assertEquals(OutputType.BOTH, arguments.getOutputType());
    }

    @Test
    void testGetMode() {
        String[] args = {"-i", TEST_INPUT_PATH, "-m", "GENERATE_OUTPUT_SHEETS"};
        Arguments arguments = new Arguments(args);
        assertEquals(Mode.GENERATE_OUTPUT_SHEETS, arguments.getMode());
    }

    @Test
    void testWriteToDifferentFile() {
        String[] args = {"-i", TEST_INPUT_PATH, "-d"};
        Arguments arguments = new Arguments(args);
        assertTrue(arguments.writeToDifferentFile());
    }

    @Test
    void testGetOutputFileName() {
        String[] args = {"-i", TEST_INPUT_PATH, "-o", "output.xlsx"};
        Arguments arguments = new Arguments(args);
        assertEquals("output.xlsx", arguments.getOutputFileName());
    }

    @Test
    void testIsVerbose() {
        String[] args = {"-i", TEST_INPUT_PATH, "-v"};
        Arguments arguments = new Arguments(args);
        assertTrue(arguments.isVerbose());
    }

    @Test
    void testNoInputFile() {
        String[] args = {"-t", "BOTH"};
        Arguments arguments = new Arguments();
        assertThrows(NoInputFileException.class, () -> arguments.readArguments(args));
    }

    @Test
    void testInvalidArgument() {
        String[] args = {"-i", TEST_INPUT_PATH, "-z"};
        Arguments arguments = new Arguments();
        assertThrows(NoSuchArgumentException.class, () -> arguments.readArguments(args));
    }

    @Test
    void testHelp() {
        String[] args = {"-h"};
        Arguments arguments = new Arguments();
        assertThrows(HelpException.class, () -> arguments.readArguments(args));
    }

}
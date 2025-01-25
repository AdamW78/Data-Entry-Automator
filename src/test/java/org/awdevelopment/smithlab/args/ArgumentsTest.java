package org.awdevelopment.smithlab.args;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    void testGetInputFile() {
        String[] args = {"-i", TEST_INPUT_PATH, "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(args, LOGGER);
        assertEquals(new File(TEST_INPUT_PATH), arguments.getInputFile());
    }

    @Test
    void testGetOutputType() {
        String[] args = {"-i", TEST_INPUT_PATH, "-t", "BOTH", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(args, LOGGER);
        assertEquals(OutputType.BOTH, arguments.getOutputType());
    }

    @Test
    void testGetMode() {
        String[] args = {"-i", TEST_INPUT_PATH, "-m", "GENERATE_OUTPUT_SHEETS", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(args, LOGGER);
        assertEquals(Mode.GENERATE_OUTPUT_SHEETS, arguments.getMode());
    }

    @Test
    void testWriteToDifferentFile() {
        String[] args = {"-i", TEST_INPUT_PATH, "-d", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(args, LOGGER);
        assertTrue(arguments.writeToDifferentFile());
    }

    @Test
    void testGetOutputFileName() {
        String[] args = {"-i", TEST_INPUT_PATH, "-o", "output.xlsx", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(args, LOGGER);
        assertEquals("output.xlsx", arguments.getOutputFileName());
    }

    @Test
    void testNoInputFile() {
        String[] args = {"-t", "BOTH", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(LOGGER);
        assertThrows(NoInputFileException.class, () -> arguments.readArguments(args));
    }

    @Test
    void testInvalidArgument() {
        String[] args = {"-i", TEST_INPUT_PATH, "-z", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(LOGGER);
        assertThrows(NoSuchArgumentException.class, () -> arguments.readArguments(args));
    }

    @Test
    void testHelp() {
        String[] args = {"-h", "--number-of-replicates", "3"};
        Arguments arguments = new Arguments(LOGGER);
        assertThrows(HelpException.class, () -> arguments.readArguments(args));
    }

}
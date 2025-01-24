package org.awdevelopment.smithlab.args.exceptions;

import org.awdevelopment.smithlab.io.output.formats.OutputType;

public class NoReplicatesProvidedException extends RuntimeException {
    public NoReplicatesProvidedException(OutputType outputType) {
        super("Error: No replicates provided with selected output type \"" + outputType+ "\". Output type \"" + outputType + "\" requires replicates (Types requiring replicates: BOTH, OTHER).");
    }
}

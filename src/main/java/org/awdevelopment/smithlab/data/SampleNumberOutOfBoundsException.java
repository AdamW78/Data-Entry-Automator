package org.awdevelopment.smithlab.data;

public class SampleNumberOutOfBoundsException extends IndexOutOfBoundsException {
    public SampleNumberOutOfBoundsException(int sampleNumber, int maxSampleNumber) {
        super("Sample number " + sampleNumber + " is out of bounds. The maximum sample number is " + maxSampleNumber);
    }
}

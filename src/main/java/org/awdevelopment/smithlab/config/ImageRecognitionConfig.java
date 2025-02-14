package org.awdevelopment.smithlab.config;

import org.awdevelopment.smithlab.logging.LoggerHelper;

public class ImageRecognitionConfig extends AbstractConfig {

    public ImageRecognitionConfig(LoggerHelper logger) {
        super(logger);
    }

    @Override
    public String toString() { return "Image Recognition Configuration"; }

    @Override
    public Mode mode() { return Mode.IMAGE_RECOGNITION; }
}

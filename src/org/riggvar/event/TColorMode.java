package org.riggvar.event;

public enum TColorMode {
    ColorMode_None, ColorMode_Error, ColorMode_Fleet;

    public TColorMode next() {
        switch (this) {
        case ColorMode_None:
            return ColorMode_Error;
        case ColorMode_Error:
            return ColorMode_Fleet;
        case ColorMode_Fleet:
            return ColorMode_None;
        default:
            return ColorMode_None;
        }
    }
}

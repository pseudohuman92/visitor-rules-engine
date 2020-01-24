import { Dimension } from './Constants';

export function stackDimensions (windowDimensions : Dimension) : Dimension {
    const {width, height} = windowDimensions;
    return {
        width: width * 0.15,
        height: height * 0.48,
    };
}

export function boardSideDimensions (windowDimensions : Dimension) : Dimension {
    const {width, height} = windowDimensions;
    return {
        width: width * 0.85,
        height: height * 0.3,
    };
}
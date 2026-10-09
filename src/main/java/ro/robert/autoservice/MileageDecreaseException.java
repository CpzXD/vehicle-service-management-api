package ro.robert.autoservice;

public class MileageDecreaseException extends RuntimeException {

    public MileageDecreaseException(
            Integer currentMileage,
            Integer requestedMileage) {

        super(
            "Kilometrajul nu poate scadea de la "
            + currentMileage + " la " + requestedMileage
        );
    }
}
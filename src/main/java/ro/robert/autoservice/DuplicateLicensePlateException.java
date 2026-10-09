package ro.robert.autoservice;

public class DuplicateLicensePlateException extends RuntimeException {

    public DuplicateLicensePlateException(String licensePlate) {
        super("Exista deja un vehicul cu numarul: " + licensePlate);
    }
}
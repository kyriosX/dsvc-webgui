package validator;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan Kirilyuk on 20.01.15.
 *
 * A Class contains handled exceptions.
 */
public class GUIException implements Serializable{

    private List<ValidationException> exceptions;

    public GUIException() {}

    public GUIException(List<ValidationException> exceptions) {
        this.exceptions = exceptions;
    }


    public List<ValidationException> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ValidationException> exceptions) {
        this.exceptions = exceptions;
    }
}

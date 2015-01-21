package validator;

/**
 * Created by Ivan Kirilyuk on 18.01.15.
 *
 */
public class ValidationException {

    private String fieldName = "";
    private String reason = "";

    public ValidationException(String fieldName, String reason) {
        this.fieldName = fieldName;
        this.reason = reason;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

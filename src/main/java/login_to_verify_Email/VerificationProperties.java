package login_to_verify_Email;

public class VerificationProperties {
    private long EXPIRATION_TIME;
    private int verificationCode;

    public long getEXPIRATION_TIME() {
        return EXPIRATION_TIME;
    }

    public void setEXPIRATION_TIME(long EXPIRATION_TIME) {
        this.EXPIRATION_TIME = EXPIRATION_TIME;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(int verificationCode) {
        this.verificationCode = verificationCode;
    }
}

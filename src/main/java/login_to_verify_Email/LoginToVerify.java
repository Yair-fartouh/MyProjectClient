/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login_to_verify_Email;

public class LoginToVerify extends SendingACodeByEmail {
    
    private String To;
    private int Code_verification;
    
    public LoginToVerify(String To) {
        super(To);
    }
    
    public String getTo() {
        return To;
    }
    
    public void setTo(String To) {
        this.To = To;
    }
    
    public int getCode_verification() {
        return Code_verification;
    }
    
    public void setCode_verification(int Code_verification) {
        this.Code_verification = Code_verification;
    }
    
    public VerificationProperties sendCodeInEmail() {
        return super.sendEmail();
    }
    
}

package woowacourse.shoppingcart.application.dto;

public class CustomerUpdatePasswordRequest {

    private String prevPassword;
    private String newPassword;

    public CustomerUpdatePasswordRequest() {
    }

    public CustomerUpdatePasswordRequest(String prevPassword, String newPassword) {
        this.prevPassword = prevPassword;
        this.newPassword = newPassword;
    }

    public String getPrevPassword() {
        return prevPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}

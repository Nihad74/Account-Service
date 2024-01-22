package Entity;

public class Password {
    private String new_password;

    public Password(String password) {
        this.new_password = password;
    }

    public Password() {
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String password) {
        this.new_password = password;
    }
}
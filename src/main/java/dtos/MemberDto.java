package dtos;

public class MemberDto {
    private int id;
    private String login;
    private String password;
    private boolean isContact;

    public MemberDto() {
    }

    public MemberDto(int id, String login, String password, boolean isContact) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.isContact = isContact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsContact() { return isContact; }

    public void setIsContact(boolean isContact) { this.isContact = isContact; }
}

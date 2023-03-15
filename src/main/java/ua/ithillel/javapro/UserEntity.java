package ua.ithillel.javapro;

import ua.ithillel.javapro.reflection.Column;
import ua.ithillel.javapro.reflection.Id;
import ua.ithillel.javapro.reflection.Table;

import java.util.Date;

@Table(name = "user")
public class UserEntity {

    @Id
    private Integer id;

    private String fio;

    private String role;

    private String login;

    private String phone;

    private String password;

    private float balance;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "created_at")
    private Date createdAt;

    //private AddressEntity address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        System.out.println("setId method called");
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", role='" + role + '\'' +
                ", login='" + login + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", birthday=" + birthday +
                ", createdAt=" + createdAt +
                '}';
    }

    public static UserEntity build() {
        return new UserEntity();
    }

}

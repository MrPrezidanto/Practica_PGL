package LuisAcosta.soprane;

public class User {

    private String dni, name, second, date, email;

    public User(){}

    public User(String i, String n, String s, String d, String e){
        dni = i;
        name = n;
        second = s;
        date = d;
        email = e;
    }

    public String getDNI() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getSecond() {
        return second;
    }

    public String getDate() {
        return date;
    }

    public String getEmail() {
        return email;
    }
}

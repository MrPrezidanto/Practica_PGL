package LuisAcosta.soprane;

import java.util.Arrays;
import java.util.List;

public class Doctor {

    private String specialty, dni, name, second, email;
    private List<String> appointment;

    public Doctor(String es, String i, String n, String s, String e){
        specialty = es;
        dni = i;
        name = n;
        second = s;
        email = e;
        appointment = Arrays.asList("01/01/2000");
    }

    public Doctor(String es, String i, String n, String s, String e, List<String> a){
        specialty = es;
        dni = i;
        name = n;
        second = s;
        email = e;
        appointment = a;
    }

    public String getSpecialty() {
        return specialty;
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

    public List<String> getAppointment() {
        return appointment;
    }

    public String getEmail() {
        return email;
    }
}

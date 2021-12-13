public class Adress {
    private String streetAdress;
    private String postalCode;
    private String city;

    public Adress(String streetAdress, String postalCode, String city) {
        this.streetAdress = streetAdress;
        this.postalCode = postalCode;
        this.city = city;
    }

    public Adress() {
    }

    public String getStreetAdress() {
        return streetAdress;
    }

    public void setStreetAdress(String streetAdress) {
        this.streetAdress = streetAdress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "streetAdress='" + streetAdress + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

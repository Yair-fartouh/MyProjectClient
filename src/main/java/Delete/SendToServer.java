package Delete;

import ExcelReader.Location;

import java.io.Serializable;

public class SendToServer implements Serializable {
    private String kindOfHelp;
    private String phone;
    private Location location;

    public String getKindOfHelp() {
        return kindOfHelp;
    }

    public void setKindOfHelp(String kindOfHelp) {
        this.kindOfHelp = kindOfHelp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

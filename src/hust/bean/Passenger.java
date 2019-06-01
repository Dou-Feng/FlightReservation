package hust.bean;

public class Passenger {
    private String No;
    private String name;
    private short cerType; // 用来记录证件类型，身份证 01，护照 02， 户口本 03，出生证明 04
    private String cerNo;
    private short pType; // 用来记录乘客类型，1：成人；2：小孩；3：婴儿
    private String phoneNo;

    public Passenger(int no) {
        No = String.valueOf(no);
        cerType = (short) 1;
        pType = (short) 1;
    }
    public Passenger(String name, short cerType, String cerNo, short pType) {
        this.name = name;
        this.cerType = cerType;
        this.cerNo = cerNo;
        this.pType = pType;
    }

    public Passenger(String name, short cerType, String cerNo, short pType, String phoneNo) {
        this.name = name;
        this.cerType = cerType;
        this.cerNo = cerNo;
        this.pType = pType;
        this.phoneNo = phoneNo;
    }

    public Passenger(String no, String name, short cerType, String cerNo, short pType, String phoneNo) {
        No = no;
        this.name = name;
        this.cerType = cerType;
        this.cerNo = cerNo;
        this.pType = pType;
        this.phoneNo = phoneNo;
    }

    public String getNo() {
        return No;
    }

    public boolean isValid() {
        return !(cerNo == null || cerNo.equals("")) && !(name == null || name.equals(""));
    }

    public void setNo(String no) {
        No = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCerNo() {
        return cerNo;
    }

    public void setCerNo(String cerNo) {
        this.cerNo = cerNo;
    }

    public short getCerType() {
        return cerType;
    }

    public void setCerType(short cerType) {
        this.cerType = cerType;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public short getpType() {
        return pType;
    }

    public void setpType(short pType) {
        this.pType = pType;
    }
}

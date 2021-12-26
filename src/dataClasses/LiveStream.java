package dataClasses;

public class LiveStream {
    private int size;
    private String host;
    private String code;

    LiveStream(String host,String code,int size){
        this.code=code;
        this.host=host;
        this.size=size;
    }

    public int getSize(){
        return this.size;
    }
    public String getHost(){
        return this.host;
    }
    public String getCode(){
        return this.code;
    }
}

package utilityClasses;

public class BlowFishDemo {
    public static void main(String[] args) throws InvalidInputException{
        //System.out.println(new BlowFishEncryption().encryptData("123456789"));
        System.out.println(new BlowFishEncryption().encryptData("12345678"));
        //System.out.println(new BlowFishEncryption().decryptData(new BlowFishEncryption().encryptData("123456789")));
        System.out.println(new BlowFishEncryption().decryptData(new BlowFishEncryption().encryptData("12345678")));
    }
}

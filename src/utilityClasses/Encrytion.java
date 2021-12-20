package utilityClasses;

public interface Encrytion {
    public String encryptData(String text) throws InvalidInputException;
    public String decryptData(String cipherText);
}

package utilityClasses;

import javax.sound.midi.SysexMessage;

public class BlowFishEncryption implements Encrytion {

    @Override
    public String encryptData(String text) throws InvalidInputException{
        if(text.isEmpty()){
            throw new InvalidInputException("Empty Password");
        }
        String cipherText="";
        char[][] block=new char[text.length()/3+(text.length()%3==0?0:1)][];
        for(int i=0;i<text.length();i++){
            if(i%3==0){
                block[i/3]=new char[3];
            }
            block[i/3][i%3]=text.charAt(i);
            if(i+1==text.length()){
                while(i%3!=2){
                    i++;
                    block[i/3][i%3]=' ';
                }
            }
        }
        for(int i=0;i<block.length;i++){
            if(i==0) {
                cipherText=new String(block[i]);
            }else{
                cipherText=cipherText+function(block[i-1],block[i]);
            }
        }
        return cipherText;
    }

    @Override
    public String decryptData(String cipherText) {
        String text="";
        char[][] block=new char[cipherText.length()/3][];
        for(int i=0;i<cipherText.length();i++) {
            if(i%3==0){
                block[i/3]=new char[3];
            }
            if(i/3==0){
                block[i/3][i%3]=cipherText.charAt(i);
                text=new String(block[i/3]);
            }else{
                block[(i/3)][i%3]=cipherText.charAt(i);
                block[(i/3)-1][i%3]=text.charAt(i-3);
            }
            if(i%3==2&&i!=2){
                text=text+function(block[i/3],block[(i/3)-1]);
            }
        }
        return text.stripTrailing();
    }

    private String function(char[] block1,char[] block2){
        char[] temp=new char[3];
        for(int i=0;i<3;i++){
            temp[i]= (char) (block1[i]^block2[i]);
        }
        return new String(temp);
    }
}

public class CaesarCipher {

    final private char[] alfabet = new char[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Є', 'Ж', 'З', 'И', 'І',
            'Ї', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т',
            'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ь', 'Ю', 'Я',
            'а', 'б', 'в', 'г', 'д', 'е', 'є', 'ж', 'з', 'и', 'і',
            'ї', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т',
            'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ю', 'я',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            '.', ' ', '«', '»', '"', '\'', ':', '!', ',', '?', '&', '#', '%'
    };

    public String encrypt(String textInFile, int key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < textInFile.length(); i++) {
            boolean flag = false;
            for (int j = 0; j < alfabet.length; j++) {
                if (textInFile.charAt(i) == alfabet[j]) {
                result.append(alfabet[(j + key) % alfabet.length]);
                flag = true;
                break;
                }
            }
               if (!flag) {
               result.append(textInFile.charAt(i));
            }
        }
        return result.toString();
    }

    public String decrypt(String result, int key) {
    int keyy = alfabet.length - (key % alfabet.length);
    String encrypt = encrypt(result, keyy);
    return encrypt.toString();
    }

    public int bruteForce(String result) {
        int counterNumber = 0;
        for (int i = 1; i < alfabet.length; i++) {
            String decryptedText = decrypt(result, i);
            int indexSymbol = decryptedText.indexOf(",");
            if(indexSymbol > 0){
                String nextSymbol =  decryptedText.substring(indexSymbol +1, indexSymbol +2);
                if (nextSymbol.equals(" ")){
                    counterNumber = i;
                    break;
                }
            }
        }
        return counterNumber;
    }
}


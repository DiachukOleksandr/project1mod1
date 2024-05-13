
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import javax.swing.*;

public class CLI extends JFrame{

    FileService fileService = new FileService();
    CaesarCipher caesarCipher = new CaesarCipher();

    private final JButton encrypt;
    private final JButton decrypt;
    private final JButton bruteForse;

    private final JTextField inputWay = new JTextField("");
    private final JLabel labelWay = new JLabel("Введіть шлях до файлу. Наприклад: C:\\Users\\Desktop\\FileName.txt");

    private final JTextField inputKey = new JTextField("");
    private final JLabel labelKey = new JLabel("Введіть ключ");

    private JFrame frame;
    private JTextArea jTextArea;


    public CLI() throws HeadlessException {
        super("Програма для обробки тексту шифром Цезаря");
        this.setBounds(300, 300, 500, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(7,1,1,1));
        container.add(labelWay);
        container.add(inputWay);
        container.add(labelKey);
        container.add(inputKey);

        encrypt = new JButton("ENCRYPT");
        encrypt.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String way = inputWay.getText();
                String key = inputKey.getText();
                if (key != null && key.matches("[0-9]+")) {
                    String textInFile = fileService.read(getWay(way));
                    if (textInFile.isEmpty()) {
                        String message = "У файлі відсутній текст. Обери інший файл";
                        JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String encrypted = caesarCipher.encrypt(textInFile, getKey(key));
                        Path newFilePath = fileService.addFileNameAnnotation(getWay(way), "[ENCRYPTED]_key_" + getKey(key));
                        fileService.write(newFilePath, encrypted);
                        viewText(newFilePath.toString());
                        jTextArea.setText("");
                        jTextArea.append(encrypted);
                        inputWay.setText(newFilePath.toString());

                        String message = "";
                        message += "Filepath: " + inputWay.getText();
                        message += "\nKey " + inputKey.getText();
                        JOptionPane.showMessageDialog(null, message, "ENCRYPTED", JOptionPane.PLAIN_MESSAGE);
                        }
                    } else {
                    String message = "Введено невірний символ ключа. Ключ може бути тільки цілим числом і більше за 0";
                    JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        container.add(encrypt);


        decrypt = new JButton("DECRYPT");
        decrypt.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String way = inputWay.getText();
                String key = inputKey.getText();
                if (key != null && key.matches("[0-9]+")) {
                     String newEncrypt = fileService.read(getWay(way));
                     if (newEncrypt.isEmpty()) {
                        String message = "У файлі відсутній текст. Обери інший файл";
                        JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
                        } else {
                        String decrypt = caesarCipher.decrypt(newEncrypt, getKey(key));
                        Path newDecryptedFilePath = fileService.addFileNameAnnotation(getWay(way), "[DECRYPTED]_key_" + key);
                        fileService.write(newDecryptedFilePath, decrypt);
                        viewText(newDecryptedFilePath.toString());
                        jTextArea.setText("");
                        jTextArea.append(decrypt);

                        String message = "";
                        message += "Filepath: " + newDecryptedFilePath.toString();
                        message += "\nКлюч: " + inputKey.getText();

                        JOptionPane.showMessageDialog(null, message, "DECRYPTED", JOptionPane.PLAIN_MESSAGE);
                        inputKey.setText("");
                     }
                } else {
                     String message = "Введено невірний символ ключа. Ключ може бути тільки цілим числом і більше за 0";
                     JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
                }
             }
        });
        container.add(decrypt);


        bruteForse = new JButton("BRUTE_FORCE");
        bruteForse.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String way = inputWay.getText();
                String newEncrypt = fileService.read(getWay(way));
                if (newEncrypt.isEmpty()) {
                    String message = "У файлі відсутній текст. Обери інший файл";
                    JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
                } else {
                        int numberCount = caesarCipher.bruteForce(newEncrypt);
                        String decrypt = caesarCipher.decrypt(newEncrypt, numberCount);
                        Path newDecryptedFilePath = fileService.addFileNameAnnotation(getWay(way), "[DECRYPTED_BRUTE_FORCE]_key_" + numberCount);
                        fileService.write(newDecryptedFilePath, decrypt);
                        viewText(newDecryptedFilePath.toString());
                        jTextArea.setText("");
                        jTextArea.append(decrypt);
                        String key = String.valueOf(numberCount);
                        inputKey.setText(key);

                        String message = "";
                        message += "Filepath: " + newDecryptedFilePath.toString();
                        message += "\nНайбільш ймовірніше, що підійде ключ: " + numberCount;
                        JOptionPane.showMessageDialog(null, message, "BRUTE_FORCE", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        container.add(bruteForse);
    }

    public int getKey (String key) {
        int keyInt = 0;
        try {
            keyInt = Integer.parseInt(key);
        } catch (NumberFormatException numberFormatException) {
            System.out.println("\n Невірний формат зі String до int \n");
            String message = "Невірний формат. Потрібно ввести цифру";
            JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.PLAIN_MESSAGE);
        }
        return keyInt;
    }

    public Path getWay(String way){
        Path path = null;
        String a = " ";
        try {
            if (way.isEmpty()) {
                String message = "Переконайся що шлях уведено вірно";
                JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
            } else if (way.equals(a)) {
                String message = "Переконайся чи замість FilePath не стоїть пробіл";
                JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
            } else if (Files.isDirectory(Path.of(way))) {
                String message = "Це шлях до директорії. Додай назву файлу та формат .txt";
                JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
            } else if (Files.notExists(Path.of(way))) {
                String message = "Переконайся що файл з такою назвою існує";
                JOptionPane.showMessageDialog(null, message, "Result", JOptionPane.WARNING_MESSAGE);
            } else if (Files.isRegularFile(Path.of(way))) {
                path = Path.of(way);
            }
        } catch (InvalidPathException invalidPathException) {
            System.out.println(" \n Введено невірний FilePath \n");
            String message = "Введено невірний FilePath";
            JOptionPane.showMessageDialog(null, message, "Результат", JOptionPane.PLAIN_MESSAGE);
        }
        return path;
    }

    private void viewText(String title){
        jTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(jTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        frame = new JFrame(title);
        frame.add(scrollPane);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}




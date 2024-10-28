import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final int LIFES_COUNT = 4;
    public static File file = new File("src/", "file.txt");
    public static final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        questionToPlayer();
    }

    private static void questionToPlayer() {
        int choiseOfPlayer;
        // Метод принимает число с консоли, делает проверку на символы и цифры
        // Запускается лишь в случае получения 0 или 1
        while (true) {
            System.out.println("Хотите начать новую игру?\n0 - Нет\n1 - Да");
            if (scanner.hasNextInt()) {
                choiseOfPlayer = scanner.nextInt();
                break; // Выход из цикла, если ввод корректный
            } else {
                System.out.println("Ошибка: введите корректное число.");
                scanner.next();
            }
        }
        if (choiseOfPlayer == 1) {
            String[] words = getDataFromTxtFile(file); //Массив слов, из которых будет рандомно выбрано слово
            startGame(words);
        } else if (choiseOfPlayer == 0){
            scanner.close();
            System.out.println("Больно надо!");
        } else {
            System.out.println("Введите 0 или 1");
            questionToPlayer(); // На случай введения других чисел
        }
    }


    private static String[] getDataFromTxtFile(File file) {
        String[] words;
        try (Reader reader = new InputStreamReader(new FileInputStream(file))) {
            int a = reader.read();
            StringBuilder result = new StringBuilder();
            while (a > 0) {
                result.append((char) a);
                a = reader.read();
            }
            words = result.toString().split("\n"); // Загружаем слова в массив, разделив их
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return words;
    }


    private static void startGame(String[] arrayOfWords) {
        int tries = 0;
        ArrayList<Character> usedLetters = new ArrayList<>();
        String randomWord = arrayOfWords[(int) (Math.random() * arrayOfWords.length)]; // Берем рандомное слово из массива
        StringBuilder changedWord = new StringBuilder(randomWord.replaceAll(".", "*")); // Меняем все буквы в слове на символ '*'
        randomWord = randomWord.toUpperCase(); //Поднимаем все буквы в слове в верхний регистр
        System.out.println(changedWord);
        char inputLetter;
        while (tries < LIFES_COUNT) {
            if (!usedLetters.isEmpty()) {
                System.out.print("Вы уже ввели: ");
                usedLetters.stream()
                        .map(let -> let + " ")
                        .forEach(System.out::print);
            }
            System.out.println();
            //System.out.println("Введите букву");
            inputLetter = inputLetterFromConsole();
            if (usedLetters.contains(inputLetter)) {
                System.out.println("Вы уже вводили эту букву");
                continue;
            }
            if (randomWord.contains(String.valueOf(inputLetter))) {
                for (int i = 0; i < randomWord.length(); i++) {
                    if (randomWord.charAt(i) == inputLetter) {
                        changedWord.setCharAt(i, inputLetter);
                    }
                }
                if (!String.valueOf(changedWord).contains("*")) {
                    System.out.println("Поздравляю, Вы отгадали слово " + changedWord);
                    questionToPlayer();
                    break;
                }
                //System.out.println(changedWord);
            } else {
                System.out.println("Такой буквы нет");
                hangMan(tries);
                tries++;
                if (tries == LIFES_COUNT) {
                    System.out.println("К сожалению, Вы проиграли. Было загадано " + randomWord);
                    questionToPlayer();
                    break;
                }

            }
            usedLetters.add(inputLetter);
            System.out.println(changedWord);
            System.out.println();

        }
    }
    //Валидация вводимого символа
    private static char inputLetterFromConsole() {
        char letter;
        System.out.println("Введите русскую букву");
        letter = scanner.next().toUpperCase().charAt(0);
        if (!Character.toString(letter).matches("[А-ЯЁ]")) {
            do {
                System.out.println("Ошибка: введите русскую букву");
                letter = scanner.next().toUpperCase().charAt(0);
            } while (!Character.toString(letter).matches("[А-ЯЁ]"));
        }
        return letter;
    }

    private static void hangMan(int tries) {
        String[] hangsArr = { "" +
                "|\n" + "|\n" + "|\n" + "|\n",
                "____\n" + "|\n" + "|\n" + "|\n" + "|\n",
                "____\n" + "|  |\n" + "|\n" + "|\n" + "|\n",
                "____\n" + "|  |\n" + "|  O\n" + "| /|\\\n" + "| /|\\\n"
        };
        System.out.println(hangsArr[tries]);
    }
}
package org.valeriygulin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Service {
    private File root;

    public Service(File root) {
        this.root = root;
    }

    /**
     * 2. Программа должна сканировать папку раз в 10 секунд
     * и проверять наличие в ней новых файлов.
     * Файлы имеют название RE_FRAUD_LIST_yyyyMMdd_000000_00000.txt
     * Проверка если файла нет с таким имененем вообще то просто перенести,
     * если есть, то посмотреть если у него число ,
     * если числа нет то присвоить 1, если число есть то найти наибольшее
     * На основании имеющихся файлов сгенерировать новые файлы и записать
     * их в соответствующую папку с названием ОПЕРАТОРА, находящихся в папке processed_data
     * Все обработанные файлы из папки "new_data" перенести в папку "processed_data\processed".
     * В случае, если в целевой папке уже имеются файлы с такими же названиями,
     * то перенести файлы под новыми именами по шаблону имя_файла(номер_начиная_с_единицы).txt
     */
    public void start() throws IOException {
        while (true) {
            File fileNewData = new File(this.root, "new_data");
            fileNewData.mkdirs();
            File[] files = fileNewData.listFiles();
            for (File file : files) {
                if (!file.getName().startsWith("RE_FRAUD_LIST")) {
                    throw new IllegalArgumentException("Некорректное название файла");
                }
                HashMap<String, ArrayList<String>> res = new HashMap<>();
                Path path = file.toPath();
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(String.valueOf(path)))) {
                    bufferedReader.readLine();
                    bufferedReader.readLine();
                    while (bufferedReader.ready()) {
                        String str = bufferedReader.readLine();
                        String[] split = str.split("\\|");
                        if (split[4].equals("FRAUD")) {
                            ArrayList<String> list = res.getOrDefault(split[2], new ArrayList<>());
                            list.add(str);
                            res.put(split[2], list);
                        }
                    }
                }
                File fileDirProcessedData = new File(this.root, "processed_data");
                fileDirProcessedData.mkdirs();
                for (Map.Entry<String, ArrayList<String>> pair : res.entrySet()) {
                    File fileDir = new File(fileDirProcessedData, pair.getKey());
                    fileDir.mkdirs();
                    int count = this.maxCount(fileDir) + 1;
                    String name = file.getName();
                    String[] s = name.split("_");
                    File fileRes = new File(fileDir, pair.getKey() + "_FRAUD_LIST_"
                            + s[4] + "_" + count + ".txt");
                    Files.write(fileRes.toPath(), pair.getValue());
                }
                File fileResult = new File(fileDirProcessedData, "processed\\");
                fileResult.mkdirs();
                File file1 = new File(fileResult + "\\" + file.getName());
                if (!file1.exists()) {
                    file.renameTo(new File(fileResult + "\\" + file.getName()));
                } else if (file1.exists()) {
                    if (isExists(fileResult, file1)) {
                        int count = this.maxCount2(fileResult, file1) + 1;
                        String replace = file1.toString().replace(".txt", "(" + count + ").txt");
                        file.renameTo(new File(replace));
                    } else {
                        String replace = file1.toString().replace(".txt", "(1).txt");
                        file.renameTo(new File(replace));
                    }
                }
            }
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Проверка, существует ли такой файл.
     */
    private boolean isExists(File fileDir, File file) {
        String[] list = fileDir.list();
        String replace = file.getName().replace(".txt", "");
        for (String str : list) {
            if (str.startsWith(replace)) {
                return true;
            }
        }
        return false;
    }


    private int maxCount2(File fileDir, File file) {
        String[] list = fileDir.list();
        ArrayList<String> arrayList = new ArrayList<>();
        String replace = file.getName().replace(".txt", "");
        for (String str : list) {
            if (str.startsWith(replace)) {
                arrayList.add(str);
            }
        }
        return arrayList.stream().filter(x -> x.contains("(")).map(x -> x.substring(x.indexOf("(") + 1, x.length() - 5)).
                mapToInt(Integer::parseInt).max().orElse(0);
    }

    private int maxCount(File fileDir) {
        String[] list = fileDir.list();
        return Arrays.stream(list).map(x -> x.split("_")[4].replace(".txt", "")).
                mapToInt(Integer::parseInt).max().orElse(0);
    }


}






package Home5;

import java.util.ArrayList;

public class MainHomeWork5 {
    static final int size = 10000000;
    static final int h = size / 2;
    static float[] arr = new float[size];

    public static void main(String[] args) {
        // ------------------------------------1-й метод------------------------------------
        // заполняю единицами
        for (int i = 0; i < size; i++) { arr[i] = 1; }
        // получаю начало времени выполнения
        long startTime1 = System.currentTimeMillis();

        // заполняю массив новыми данными по формуле
        for (int i = 0; i < size; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        // вывожу время выполнения
        System.out.println("Время выполнения 1-го метода - "+(System.currentTimeMillis() - startTime1));

        // ------------------------------------2-й метод------------------------------------
        // заполняю единицами
        for (int i = 0; i < size; i++) { arr[i] = 1; }

        // задаю количество потоков
        int countThread = 15;
        // получаю количество значений в 1-ом промежуточном массиве
        int sizeArr = size / countThread;
        // переменная для остатка "size / countThread"
        int remainderSize = size;

        // т.к. число потоков может быть нечетным,
        // то нахожу остаток после деления "size / countThread"
        for (int i = 0; i < countThread; i++) {
            remainderSize = remainderSize - sizeArr;
        }

        // создаю массивы
        ArrayList<float[]> arrayThread = new ArrayList<float[]>();
        for (int i = 0; i < countThread-1; i++) {
            arrayThread.add(new float[sizeArr]);
        }
        // к последнему массиву прибавляю остаток
        arrayThread.add(new float[sizeArr+remainderSize]);

        // получаю начало времени выполнения
        long startTime2 = System.currentTimeMillis();

        // разбиваю основной массив на подмассивы
        int intermediateCount = 0;
        for (float[] arth : arrayThread) {
            System.arraycopy(arr, intermediateCount, arth, 0, arth.length);
            intermediateCount = arth.length;
        }

        // создаю потоки со своими массивами
        Thread myThreads[] = new Thread[countThread];
        int t = 0;
        for (float[] arth : arrayThread) {
            myThreads[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    int len = arth.length;
                    for (int j = 0; j < len; j++) {
                        arth[j] = (float)(arth[j] * Math.sin(0.2f + j / 5) * Math.cos(0.2f + j / 5) * Math.cos(0.4f + j / 2));
                    }
                }
            });
            myThreads[t].start();
            t++;
        }
        // джойню потоки
        for (int i = 0; i < countThread; i++) {
            try {
                myThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // склеиваю массивы обратно в один общий массив
        intermediateCount = 0;
        for (float[] arth : arrayThread) {

            System.arraycopy(arth, 0, arr, intermediateCount, arth.length);
            intermediateCount = arth.length;
        }
        // вывожу время выполнения 2-го метода
        System.out.println("Время выполнения 2-го метода - " + (System.currentTimeMillis() - startTime2));
    }
}
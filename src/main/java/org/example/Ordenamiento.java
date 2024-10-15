package org.example;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Ordenamiento {

    public static void main(String[] args) throws IOException {
        int[] array = loadArrayFromFile("src/main/java/documentos/numbers_1m.txt");

        // Almacena los tiempos de ejecución
        long bubbleSortTime = measureTime(() -> bubbleSort(Arrays.copyOf(array, array.length)));
        System.out.println(bubbleSortTime);
        long quickSortTime = measureTime(() -> quickSort(Arrays.copyOf(array, array.length), 0, array.length - 1));
        System.out.println(quickSortTime);
        //long stoogeSortTime = measureTime(() -> stoogeSort(Arrays.copyOf(array, array.length), 0, array.length - 1));
        //System.out.println(stoogeSortTime);
        long pigeonholeSortTime = measureTime(() -> pigeonholeSort(Arrays.copyOf(array, array.length)));
        System.out.println(pigeonholeSortTime);
        long mergeSortTime = measureTime(() -> mergeSort(Arrays.copyOf(array, array.length), new int[array.length], 0, array.length - 1));
        System.out.println(mergeSortTime);
        long bitonicSortTime = measureTime(() -> bitonicSort(Arrays.copyOf(array, array.length), 0, array.length, 1));
        System.out.println(bitonicSortTime);

        // Crear dataset para los tiempos
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(bubbleSortTime, "Tiempo", "Burbuja");
        dataset.addValue(quickSortTime, "Tiempo", "Quicksort");
        //dataset.addValue(stoogeSortTime, "Tiempo", "Stooge Sort");
        dataset.addValue(pigeonholeSortTime, "Tiempo", "Pigeonhole Sort");
        dataset.addValue(mergeSortTime, "Tiempo", "Merge Sort");
        dataset.addValue(bitonicSortTime, "Tiempo", "Bitonic Sort");

        // Crear gráfico de barras
        JFreeChart barChart = ChartFactory.createBarChart(
                "Tiempos de ejecución de algoritmos de ordenamiento",
                "Algoritmos",
                "Tiempo (ms)",
                dataset
        );

        // Mostrar gráfico en un JFrame
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        JFrame frame = new JFrame("Comparación de tiempos de ordenamiento");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(chartPanel);
        frame.pack();
        frame.setVisible(true);
    }

    // Función para medir tiempo de ejecución de un algoritmo
    public static long measureTime(Runnable algorithm) {
        long startTime = System.nanoTime();
        algorithm.run();
        return (System.nanoTime() - startTime) / 1000000; // Convertir a milisegundos
    }

    // Cargar arreglo desde un archivo
    public static int[] loadArrayFromFile(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        return br.lines()  // Leer todas las líneas
                .mapToInt(Integer::parseInt)  // Convertir cada línea a int
                .toArray();
    }

    // Implementaciones de algoritmos de ordenamiento

    // Bubble Sort
    public static void bubbleSort(int[] array) {
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    // Quick Sort
    public static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pi = partition(array, low, high);
            quickSort(array, low, pi - 1);
            quickSort(array, pi + 1, high);
        }
    }

    public static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    // Stooge Sort
    public static void stoogeSort(int[] array, int l, int h) {
        if (l >= h) return;
        if (array[l] > array[h]) {
            int temp = array[l];
            array[l] = array[h];
            array[h] = temp;
        }
        if (h - l + 1 > 2) {
            int t = (h - l + 1) / 3;
            stoogeSort(array, l, h - t);
            stoogeSort(array, l + t, h);
            stoogeSort(array, l, h - t);
        }
    }

    // Pigeonhole Sort
    public static void pigeonholeSort(int[] array) {
        int min = Arrays.stream(array).min().orElse(Integer.MIN_VALUE);
        int max = Arrays.stream(array).max().orElse(Integer.MAX_VALUE);
        int range = max - min + 1;
        int[] pigeonholes = new int[range];

        for (int i : array) pigeonholes[i - min]++;

        int index = 0;
        for (int i = 0; i < range; i++) {
            while (pigeonholes[i]-- > 0) {
                array[index++] = i + min;
            }
        }
    }

    // Merge Sort
    public static void mergeSort(int[] array, int[] temp, int leftStart, int rightEnd) {
        if (leftStart >= rightEnd) return;
        int middle = (leftStart + rightEnd) / 2;
        mergeSort(array, temp, leftStart, middle);
        mergeSort(array, temp, middle + 1, rightEnd);
        mergeHalves(array, temp, leftStart, rightEnd);
    }

    public static void mergeHalves(int[] array, int[] temp, int leftStart, int rightEnd) {
        int leftEnd = (rightEnd + leftStart) / 2;
        int rightStart = leftEnd + 1;
        int size = rightEnd - leftStart + 1;

        int left = leftStart;
        int right = rightStart;
        int index = leftStart;

        while (left <= leftEnd && right <= rightEnd) {
            if (array[left] <= array[right]) {
                temp[index] = array[left];
                left++;
            } else {
                temp[index] = array[right];
                right++;
            }
            index++;
        }

        System.arraycopy(array, left, temp, index, leftEnd - left + 1);
        System.arraycopy(array, right, temp, index, rightEnd - right + 1);
        System.arraycopy(temp, leftStart, array, leftStart, size);
    }

    // Bitonic Sort
    public static void bitonicSort(int[] array, int low, int cnt, int dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            bitonicSort(array, low, k, 1);
            bitonicSort(array, low + k, k, 0);
            bitonicMerge(array, low, cnt, dir);
        }
    }

    public static void bitonicMerge(int[] array, int low, int cnt, int dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            for (int i = low; i < low + k; i++) {
                if ((dir == 1 && array[i] > array[i + k]) || (dir == 0 && array[i] < array[i + k])) {
                    int temp = array[i];
                    array[i] = array[i + k];
                    array[i + k] = temp;
                }
            }
            bitonicMerge(array, low, k, dir);
            bitonicMerge(array, low + k, k, dir);
        }
    }
}

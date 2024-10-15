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

public class Busqueda {

    public static void main(String[] args) throws IOException {
        int[] array = loadArrayFromFile("src/main/java/documentos/numbers_1m_ordenado.txt");
        int target = 55224089;  // Número que queremos buscar

        // Almacena los tiempos de ejecución
        long linearSearchTime = measureTime(() -> linearSearch(array, target));
        System.out.println("Tiempo Búsqueda Lineal: " + linearSearchTime);

        long limitedLinearSearchTime = measureTime(() -> limitedLinearSearch(array, target, 100)); // Límite arbitrario
        System.out.println("Tiempo Búsqueda Lineal Limitada: " + limitedLinearSearchTime);

        long binarySearchTime = measureTime(() -> binarySearch(array, target, 0, array.length - 1));
        System.out.println("Tiempo Búsqueda Binaria: " + binarySearchTime);

        long jumpSearchTime = measureTime(() -> jumpSearch(array, target));
        System.out.println("Tiempo Búsqueda por Saltos: " + jumpSearchTime);

        // Crear dataset para los tiempos
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(linearSearchTime, "Tiempo", "Búsqueda Lineal");
        dataset.addValue(limitedLinearSearchTime, "Tiempo", "Búsqueda Lineal Limitada");
        dataset.addValue(binarySearchTime, "Tiempo", "Búsqueda Binaria");
        dataset.addValue(jumpSearchTime, "Tiempo", "Búsqueda por Saltos");

        // Crear gráfico de barras
        JFreeChart barChart = ChartFactory.createBarChart(
                "Tiempos de ejecución de algoritmos de búsqueda",
                "Algoritmos",
                "Tiempo (ms)",
                dataset
        );

        // Mostrar gráfico en un JFrame
        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));

        JFrame frame = new JFrame("Comparación de tiempos de búsqueda");
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

    // Búsqueda Lineal
    public static int linearSearch(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;  // Encontrado
            }
        }
        return -1;  // No encontrado
    }

    // Búsqueda Lineal Limitada (con un límite en las iteraciones)
    public static int limitedLinearSearch(int[] array, int target, int limit) {
        for (int i = 0; i < Math.min(limit, array.length); i++) {
            if (array[i] == target) {
                return i;  // Encontrado
            }
        }
        return -1;  // No encontrado dentro del límite
    }

    // Búsqueda Binaria (el array debe estar ordenado)
    public static int binarySearch(int[] array, int target, int low, int high) {
        if (high >= low) {
            int mid = low + (high - low) / 2;

            // Si el elemento está presente en el medio
            if (array[mid] == target)
                return mid;

            // Si el elemento es menor que mid, solo buscar en el subarray izquierdo
            if (array[mid] > target)
                return binarySearch(array, target, low, mid - 1);

            // Sino, buscar en el subarray derecho
            return binarySearch(array, target, mid + 1, high);
        }

        // Elemento no presente
        return -1;
    }

    // Búsqueda por Saltos (Jump Search)
    public static int jumpSearch(int[] array, int target) {
        int n = array.length;
        int step = (int) Math.sqrt(n); // Tamaño del bloque a saltar
        int prev = 0;

        // Encuentra el bloque donde el objetivo puede estar
        while (array[Math.min(step, n) - 1] < target) {
            prev = step;
            step += (int) Math.sqrt(n);
            if (prev >= n)
                return -1;
        }

        // Realiza búsqueda lineal dentro del bloque encontrado
        while (array[prev] < target) {
            prev++;
            if (prev == Math.min(step, n))
                return -1;
        }

        // Si se encuentra el objetivo
        if (array[prev] == target)
            return prev;

        return -1;  // No encontrado
    }
}

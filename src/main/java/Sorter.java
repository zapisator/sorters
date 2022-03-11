import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

public class Sorter {

  public static void heapSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    for (int i = array.length / 2 - 1; i >= 0; i--) {
      heapify(array, array.length, i);
    }
    for (int i = array.length - 1; i > 0; i--) {
      swap(array, 0, i);
      heapify(array, i, 0);
    }
  }

  private static void heapify(int[] array, int arrayLookupDepth, int previousSmallest) {
    boolean didSwap = true;

    while (didSwap) {
      int currentSmallest = previousSmallest;
      final int leftChild = 2 * currentSmallest + 1;
      final int rightChild = 2 * currentSmallest + 2;

      if (leftChild < arrayLookupDepth && array[leftChild] > array[currentSmallest]) {
        currentSmallest = leftChild;
      }
      if (rightChild < arrayLookupDepth && array[rightChild] > array[currentSmallest]) {
        currentSmallest = rightChild;
      }
      if (currentSmallest != previousSmallest) {
        swap(array, previousSmallest, currentSmallest);
        previousSmallest = currentSmallest;
      } else {
        didSwap = false;
      }
    }
  }

  public static void bubbleSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    for (int i = 0; i < array.length; i++) {
      for (int j = 0; j < array.length; j++) {
        if (array[i] < array[j]) {
          swap(array, i, j);
        }
      }
    }
  }

  public static void bubbleSortOptimized(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    boolean hasSwaped = true;

    for (int i = 0; i < array.length && hasSwaped; i++) {
      hasSwaped = false;
      for (int j = 0; j < array.length - 1; j++) {
        if (array[j] > array[j + 1]) {
          swap(array, j, j + 1);
          hasSwaped = true;
        }
      }
    }
  }

  public static void quickSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    final Queue<int[]> partitions = new LinkedList<>();
    final int lowIndex = 0;
    final int highIndex = 1;

    addIfMoreThenOne(partitions, 0, array.length - 1);
    while (!partitions.isEmpty()) {
      final int[] ends = partitions.poll();
      final int low = ends[lowIndex];
      final int high = ends[highIndex];
      final int partition = partition(array, low, high);

      addIfMoreThenOne(partitions, low, partition - 1);
      addIfMoreThenOne(partitions, partition + 1, high);
    }
  }

  public static void mergeSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    final Queue<int[]> parts = new LinkedList<>();

    splitIntoPeacesOfLengthOneAndTwo(parts, array);
    mergePairsSortingly(parts, array);
    System.arraycopy(Objects.requireNonNull(parts.poll()), 0, array, 0, array.length);
  }

  public static void selectionSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    int subarrayMinimumIndex;

    for (int sorted = 0; sorted < array.length; sorted++) {
      subarrayMinimumIndex = sorted;
      for (int unsorted = sorted; unsorted < array.length; unsorted++) {
        if (array[unsorted] < array[subarrayMinimumIndex]) {
          subarrayMinimumIndex = unsorted;
        }
      }
      swap(array, sorted, subarrayMinimumIndex);
    }
  }

  public static void insertionSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    for (int unsorted = 1; unsorted < array.length; unsorted++) {
      for (int sorted = unsorted - 1; sorted >= 0 && array[sorted + 1] < array[sorted]; sorted--) {
        swap(array, sorted, sorted + 1);
      }
    }
  }

  public static void insertionSortOptimized(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    for (int unsorted = 1; unsorted < array.length; unsorted++) {
      final int key = array[unsorted];
      final int placeToInsert = Math.abs(Arrays.binarySearch(array, 0, unsorted, key) + 1);

      System.arraycopy(array, placeToInsert, array, placeToInsert + 1, unsorted - placeToInsert);
      array[placeToInsert] = key;
    }
  }

  public static void countingSort(int[] array) {
    countingSort(array, 256);
  }

  public static void countingSort(int[] array, int maxRange) {

    if (array == null || array.length < 2) {
      return;
    }

    final Extremum extremum = Extremum.create(array);

    if (extremum.max - extremum.min > maxRange) {
      return;
    }

    final Function<Integer, Integer> index = (value) -> value - extremum.min;
    final int[] counts = countValues(array, extremum.max - extremum.min + 1, index);
    countsToCumulativeForm(counts);

    final int[] output = output(array, index, counts);
    System.arraycopy(output, 0, array, 0, output.length);
  }

  public static void countingSort(int[] array, int exponent, int radix) {

    if (array == null || array.length < 2) {
      return;
    }

    final Function<Integer, Integer> index = (value) -> (value / exponent) % radix;
    final int[] counts = countValues(array, radix, index);
    countsToCumulativeForm(counts);

    final int[] output = output(array, index, counts);
    System.arraycopy(output, 0, array, 0, output.length);
  }

  public static void radixSort(int[] array) {

    if (array == null || array.length < 2) {
      return;
    }

    final Extremum extremum = Extremum.create(array);
    int ofset = 0;

    if (extremum.min < 0) {
      if (extremum.max - extremum.min > Integer.MAX_VALUE - extremum.max) {
        return;
      }
      ofset = -extremum.min;
      for (int i = 0; i < array.length; i++) {
        array[i] += ofset;
      }
    }

    radixSort(array, extremum.max + ofset, extremum.max + ofset);

    if (extremum.min < 0) {
      for (int i = 0; i < array.length; i++) {
        array[i] -= ofset;
      }
    }
  }

  private static void radixSort(int[] array, final int max, int radix) {

    if (array == null || array.length < 2 || radix < 2) {
      return;
    }

    for (int exponent = 1; max / exponent > 0; exponent *= radix) {
      countingSort(array, exponent, radix);
    }
  }

  private static int[] output(int[] array, Function<Integer, Integer> index, int[] counts) {
    final int[] output = new int[array.length];

    for (int i = array.length - 1; i >= 0; i--) {
      final int value = array[i];
      final int debiasedValue = index.apply(value);
      final int valueIndexInOutput = counts[debiasedValue] - 1;

      output[valueIndexInOutput] = value;
      counts[debiasedValue]--;
    }
    return output;
  }

  private static void countsToCumulativeForm(int[] counts) {
    for (int i = 1; i < counts.length; i++) {
      counts[i] += counts[i - 1];
    }
  }

  private static int[] countValues(int[] array, int countLength, Function<Integer, Integer> index) {
    final int[] counts = new int[countLength];

    for (int value : array) {
      counts[index.apply(value)]++;
    }
    return counts;
  }

  private static void addIfMoreThenOne(Queue<int[]> partitions, int low, int high) {
    if (high - low >= 1) {
      partitions.add(new int[]{low, high});
    }
  }

  private static void mergePairsSortingly(Queue<int[]> parts, int[] array) {
    if (parts.size() % 2 != 0) {
      return;
    }

    while (!parts.isEmpty() && parts.peek().length != array.length) {
      final int[] left = parts.poll();
      final int[] right = parts.poll();
      final int[] result = new int[left.length + right.length];
      int leftIndex = 0;
      int rightIndex = 0;
      int resultIndex = 0;

      while (leftIndex < left.length && rightIndex < right.length) {
        if (left[leftIndex] < right[rightIndex]) {
          result[resultIndex++] = left[leftIndex++];
        } else {
          result[resultIndex++] = right[rightIndex++];
        }
      }
      while (leftIndex < left.length) {
        result[resultIndex] = left[leftIndex];
        leftIndex++;
        resultIndex++;
      }
      while (rightIndex < right.length) {
        result[resultIndex] = right[rightIndex];
        rightIndex++;
        resultIndex++;
      }
      parts.add(result);
    }
  }

  private static void splitIntoPeacesOfLengthOneAndTwo(Queue<int[]> parts, int[] array) {
    parts.add(array);
    while (!parts.isEmpty() && parts.peek().length >= 2) {
      final int[] part = parts.poll();
      final int middleIndex = part.length / 2;

      parts.add(Arrays.copyOfRange(part, 0, middleIndex));
      parts.add(Arrays.copyOfRange(part, middleIndex, part.length));
    }
  }

  private static int partition(final int[] array, final int low, final int high) {
    if (high - low < 1) {
      return 0;
    }

    int pivot = array[low];
    int rightMargin = high;

    for (int i = high; i >= low; i--) {
      if (array[i] > pivot) {
        swap(array, i, rightMargin);
        rightMargin--;
      }
    }
    swap(array, low, rightMargin);
    return rightMargin;
  }

  private static void swap(int[] array, int i, int j) {
    final int temp = array[i];

    array[i] = array[j];
    array[j] = temp;
  }
}

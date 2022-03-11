import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Test {

  public static void main(String[] args) {
    final int counter = 10;
    final List<Consumer<int[]>> sorters = List.of(
        Sorter::bubbleSort,
        Sorter::bubbleSortOptimized,
        Sorter::quickSort,
        Sorter::mergeSort,
        Sorter::selectionSort,
        Sorter::insertionSort,
        Sorter::insertionSortOptimized,
        Sorter::countingSort,
        Sorter::radixSort,
        Sorter::heapSort
    );

    testPositiveWithoutSkipsAndDuplicates(sorters, counter);
    testPositiveWithSkips(sorters, counter);
    testPositiveWithDuplicates(sorters, counter);
    testNegatives(sorters, counter);
    testNullEmptyAndLengthOne(sorters, counter);
  }

  private static void test(int[] expected, int[] array, Consumer<int[]> sorter, int counter) {
    final String input = Arrays.toString(array);

    sorter.accept(array);
    System.out.printf(
        "input: %s\t\texpected: %s\t\tresult: %s\t\t%s\n",
        input, Arrays.toString(expected),
        Arrays.toString(array),
        Arrays.equals(expected, array) ? "SUCCED" : "FAILED"
    );

    if (counter > 1) {
      test(expected, shuffledArrayCopy(array), sorter, counter - 1);
    } else {
      System.out.println();
    }
  }

  private static int[] shuffledArrayCopy(int[] array) {

    if (array == null || array.length < 2) {
      return array;
    }

    final List<Integer> copy = Arrays.stream(Arrays.copyOf(array, array.length))
        .boxed()
        .collect(Collectors.toList());

    Collections.shuffle(copy);
    return copy.stream()
        .mapToInt(number -> number)
        .toArray();
  }

  public static void testPositiveWithoutSkipsAndDuplicates(List<Consumer<int[]>> sorters,
      int counter) {
    final int[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

    sorters.forEach(sorter -> test(expected, shuffledArrayCopy(expected), sorter, counter));
  }

  public static void testPositiveWithSkips(List<Consumer<int[]>> sorters, int counter) {
    final int[] expected = {1, 3, 5, 13, 15, 45, 78, 111};

    sorters.forEach(sorter -> test(expected, shuffledArrayCopy(expected), sorter, counter));
  }

  public static void testPositiveWithDuplicates(List<Consumer<int[]>> sorters, int counter) {
    final int[] expected = {1, 1, 2, 2, 4, 5, 7};

    sorters.forEach(sorter -> test(expected, shuffledArrayCopy(expected), sorter, counter));
  }

  public static void testNegatives(List<Consumer<int[]>> sorters, int counter) {
    final int[] expected = {-2, -1, 0, 1, 2, 3, 4, 5};

    sorters.forEach(sorter -> test(expected, shuffledArrayCopy(expected), sorter, counter));
  }

  private static void testNullEmptyAndLengthOne(List<Consumer<int[]>> sorters, int counter) {
    final int[] emptyArray = {};
    final int[] lengthOne = new int[]{new Random().nextInt()};

    sorters.forEach(sorter -> test(null, null, sorter, counter));
    sorters.forEach(sorter -> test(emptyArray, emptyArray, sorter, counter));
    sorters.forEach(sorter -> test(lengthOne, lengthOne, sorter, counter));
  }

}

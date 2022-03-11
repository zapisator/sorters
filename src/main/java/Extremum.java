public class Extremum {

  public final int min;
  public final int max;

  private Extremum(int min, int max) {
    this.min = min;
    this.max = max;
  }

  public static Extremum create(int[] array) {
    int min = array[0];
    int max = array[0];

    for (int value : array) {
      if (value < min) {
        min = value;
      } else if (value > max) {
        max = value;
      }
    }
    return new Extremum(min, max);
  }
}

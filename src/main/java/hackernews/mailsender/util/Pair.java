package hackernews.mailsender.util;

public class Pair<T, V> {

  private T left;
  private V right;

  public Pair(final T left, final V right) {
    this.left = left;
    this.right = right;
  }

  public T getLeft() {
    return this.left;
  }

  public V getRight() {
    return this.right;
  }

}

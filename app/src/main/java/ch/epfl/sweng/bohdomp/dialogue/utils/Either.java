package ch.epfl.sweng.bohdomp.dialogue.utils;

/**
 * Either type, inspired by Scala.
 * @param <L> type of left value
 * @param <R> type of right value*
 */
public abstract class Either<L, R> {

    abstract public boolean isLeft();

    abstract public boolean isRight();

    abstract public L getLeft();

    abstract public R getRight();

}

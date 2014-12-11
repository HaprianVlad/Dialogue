package ch.epfl.sweng.bohdomp.dialogue.utils;

import java.util.NoSuchElementException;

/**
 *  Represents a left either.
 *  @param <L> type of left value
 *  @param <R> type of right value (ignored)
 */
public class Left<L, R> extends Either<L, R> {

    private L mValue;

    public Left(L value) {
        //explicitly no null check
        mValue = value;
    }

    @Override
    public boolean isLeft() {
        return true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    public L getLeft() {
        return mValue;
    }

    @Override
    public R getRight() {
        throw new NoSuchElementException();
    }
}

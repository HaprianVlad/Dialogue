package ch.epfl.sweng.bohdomp.dialogue.utils;

import java.util.NoSuchElementException;

/**
 *  Represents a right either.
 *  @param <L> type of left value (ignored)
 *  @param <R> type of right value
 */
public class Right<L, R> extends Either<L, R> {

    private R mValue;

    public Right(R value) {
        //explicitly no null check
        mValue = value;
    }

    @Override
    public boolean isLeft() {
        return false;
    }

    @Override
    public boolean isRight() {
        return true;
    }

    @Override
    public L getLeft() {
        throw new NoSuchElementException();
    }

    @Override
    public R getRight() {
        return mValue;
    }
}
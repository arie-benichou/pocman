
package pocman.graph.functions;

import com.google.common.base.Predicate;

public final class Integers {

    public static class Predicates {

        private final static Predicate<Integer> IS_EVEN = new isEven();

        private final static Predicate<Integer> IS_ODD = com.google.common.base.Predicates.not(new isEven());

        private final static class isEven implements Predicate<Integer> {

            @Override
            public boolean apply(final Integer integer) {
                return integer % 2 == 0;
            }
        }

        private final static class isSame implements Predicate<Integer> {

            private final Integer integer;

            public isSame(final int integer) {
                this.integer = integer;
            }

            @Override
            public boolean apply(final Integer integer) {
                return this.integer.equals(integer);

            }
        }

        public static Predicate<Integer> isEven() {
            return IS_EVEN;
        }

        public static Predicate<Integer> isOdd() {
            return IS_ODD;
        }

        public static Predicate<Integer> isSame(final int integer) {
            return new isSame(integer);
        }

    }

}

package pocman.graph.features;

import com.google.common.base.Predicate;

// TODO move into lib package
public final class Integers {

    public static class Predicates {

        private final static Predicate<Integer> IS_EVEN = new IsEven();

        private final static Predicate<Integer> IS_ODD = com.google.common.base.Predicates.not(new IsEven());

        private final static class IsEven implements Predicate<Integer> {

            @Override
            public boolean apply(final Integer integer) {
                return integer % 2 == 0;
            }
        }

        private final static class Is implements Predicate<Integer> {

            private final Integer integer;

            public Is(final int integer) {
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

        public static Predicate<Integer> is(final int integer) {
            return new Is(integer);
        }

        public static Predicate<Integer> isNot(final int integer) {
            return com.google.common.base.Predicates.not(new Is(integer));
        }

    }

}
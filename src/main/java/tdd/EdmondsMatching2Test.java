
package tdd;

public class EdmondsMatching2Test {

    public static void main(final String[] args) {
        main1();
        System.out.println();
        System.out.println();
        main2();
    }

    public static void main1() {
        final UndirectedGraph<String> graph = new UndirectedGraph<String>();

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");
        graph.addNode("G");
        graph.addNode("H");
        graph.addNode("I");
        graph.addNode("J");

        graph.addEdge("A", "B");

        graph.addEdge("B", "C");
        graph.addEdge("B", "G");

        graph.addEdge("C", "D");
        graph.addEdge("C", "H");

        graph.addEdge("D", "E");
        graph.addEdge("D", "I");

        graph.addEdge("F", "G");
        graph.addEdge("G", "H");

        graph.addEdge("I", "H");
        graph.addEdge("I", "J");

        for (final String string : graph) {
            System.out.print(string + ": ");
            System.out.println(graph.edgesFrom(string));
        }

        System.out.println();

        final UndirectedGraph<String> maximumMatching = EdmondsMatching2.maximumMatching(graph);

        for (final String string : maximumMatching) {
            System.out.print(string + ": ");
            System.out.println(maximumMatching.edgesFrom(string));
        }
    }

    public static void main2() {

        final UndirectedGraph<String> graph = new UndirectedGraph<String>();

        graph.addNode("B");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("G");

        graph.addEdge("D", "B");
        graph.addEdge("D", "E");
        graph.addEdge("D", "G");

        for (final String string : graph) {
            System.out.print(string + ": ");
            System.out.println(graph.edgesFrom(string));
        }

        System.out.println();

        final UndirectedGraph<String> maximumMatching = EdmondsMatching2.maximumMatching(graph);

        for (final String string : maximumMatching) {
            System.out.print(string + ": ");
            System.out.println(maximumMatching.edgesFrom(string));
        }

    }

}

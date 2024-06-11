import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

        class Landmark {
            String name;

            public Landmark(String name) {
                this.name =name ;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        class Edge {
            Landmark source;
            Landmark destination;
            double weight;
            double attractivenessScore;

            public Edge(Landmark  source, Landmark destination, double weight, double attractivenessScore) {
                this.source = source;
                this.destination = destination;
                this.weight = weight;
                this.attractivenessScore=attractivenessScore;
            }

            public Landmark getSource() {
                return source;
            }

            public void setSource(Landmark source) {
                this.source = source;
            }

            public Landmark getDestination() {
                return destination;
            }

            public void setDestination(Landmark destination) {
                this.destination = destination;
            }

            public double getWeight() {
                return weight;
            }

            public void setWeight(double weight) {
                this.weight = weight;
            }

            public double getAttractivenessScore() {
                return attractivenessScore;
            }

            public void setAttractivenessScore(double attractivenessScore) {
                this.attractivenessScore = attractivenessScore;
            }
        }


        class CityTourPlanner {
            List<Landmark> landmarks;
            List<Edge> edges;


            public CityTourPlanner(List<Landmark> landmarks, List<Edge> edges) {
                this.landmarks = landmarks;
                this.edges = edges;
            }
        }

       class CityTourOptimizer {

            static DirectedGraph map =new DirectedGraph();
            public static void main(String[] args) {

                String[][] personalInterestArr =new String[100][2];
                int lineCount=0;
                try {
                    File myObj = new File("personal_interest.txt");
                    Scanner myReader = new Scanner(myObj);
                    myReader.nextLine();


                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        personalInterestArr[lineCount][0]=data.split("\t")[0];
                        personalInterestArr[lineCount][1]=data.split("\t")[1];
                        lineCount++;
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                String[][] visitorLoadArr =new String[100][2];
                int lineCount2=0;
                try {
                    File myObj2 = new File("visitor_load.txt");
                    Scanner myReader = new Scanner(myObj2);
                    myReader.nextLine();


                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        visitorLoadArr[lineCount2][0]=data.split("\t")[0];
                        visitorLoadArr[lineCount2][1]=data.split("\t")[1];
                        lineCount2++;
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                List<Edge> edgelist= new ArrayList<>() ;
                Landmark firstLandmark=null;

                try {
                    File myObj = new File("landmark_map_data.txt");
                    Scanner myReader = new Scanner(myObj);
                    myReader.nextLine();
                    String data = myReader.nextLine();
                    String temp = data.split("\t")[0];
                    while (myReader.hasNextLine()) {
                        if (temp.equals("Hotel")){
                            firstLandmark=new Landmark(temp);
                        }

                        String source = data.split("\t")[0];
                        String destination = data.split("\t")[1];
                        double baseScore = Double.parseDouble(data.split("\t")[2]);
                        double travelTime = Double.parseDouble(data.split("\t")[3]);
                        double personalInterest=0;
                        double visitorLoad=0;
                        for (int i=0;i< lineCount;i++){
                            if (personalInterestArr[i][0].equals(destination)){
                                personalInterest = Double.parseDouble(personalInterestArr[i][1]);
                            }
                        }
                        for (int i=0;i< lineCount2;i++){
                            if (visitorLoadArr[i][0].equals(destination)){
                                visitorLoad = Double.parseDouble(visitorLoadArr[i][1]);
                            }
                        }
                        double attractiveScore = baseScore*personalInterest*(1-visitorLoad);
                        Landmark fromlandmark=new Landmark(source);
                        Landmark tolandmark=new Landmark(destination);
                        Edge edge =new Edge(fromlandmark,tolandmark,travelTime,attractiveScore);
                        if (temp.equals(source))
                            edgelist.add(edge);
                        else {
                            Landmark mainSource =new Landmark(temp);
                            map.addEdges(mainSource,edgelist);
                            edgelist=new ArrayList<>();
                            temp=source;
                            edgelist.add(edge);
                        }

                        data = myReader.nextLine();
                    }
                    String source = data.split("\t")[0];
                    String destination = data.split("\t")[1];
                    double baseScore = Double.parseDouble(data.split("\t")[2]);
                    double travelTime = Double.parseDouble(data.split("\t")[3]);
                    double personalInterest=0;
                    double visitorLoad=0;
                    for (int i=0;i< lineCount;i++){
                        if (personalInterestArr[i][0].equals(destination)){
                            personalInterest = Double.parseDouble(personalInterestArr[i][1]);
                        }
                    }
                    for (int i=0;i< lineCount2;i++){
                        if (visitorLoadArr[i][0].equals(destination)){
                            visitorLoad = Double.parseDouble(visitorLoadArr[i][1]);
                        }
                    }
                    double attractiveScore = baseScore * personalInterest * (1-visitorLoad);
                    Landmark fromlandmark=new Landmark(source);
                    Landmark tolandmark=new Landmark(destination);
                    Edge edge =new Edge(fromlandmark,tolandmark,travelTime,attractiveScore);
                    edgelist.add(edge);
                    Landmark mainSource =new Landmark(temp);
                    map.addEdges(mainSource,edgelist);

                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
                Scanner scn = new Scanner(System.in);
                int n = 0;
                System.out.print("Please enter the total number of landmarks (including Hotel): ");
                n = scn.nextInt();
                if (n<2)
                    System.out.println("Landmark number must be bigger than 2.");
                else if (n>lineCount)
                    System.out.println("Landmark number must be smaller than "+n);
                else {
                    long start=System.nanoTime();
                    List<String> optimalRoute = findOptimalRoute(map,n);
                    System.out.println("Optimal Route: " + optimalRoute);
                    long end=System.nanoTime();
                    long time= end-start;
                    System.out.println("Time spend " +time/100000000+" ms");
                }

            }

           //Time complexity is O(2^n)
         public static List<String> findOptimalRoute(DirectedGraph graph, int numLandmarks) {
             List<Landmark> landmarks = new ArrayList<>(graph.getLandmarks());
             Landmark startLandmark =null;
             for (Landmark landmark : landmarks){
                 if (landmark.getName().equals("Hotel"))
                     startLandmark = landmark;
             }

             List<List<String>> allRoutes = new ArrayList<>();
             List<String> currentRoute = new ArrayList<>();
             Set<Landmark> visited = new HashSet<>();
             visited.add(startLandmark);

             findRoutes(graph, startLandmark, visited, currentRoute, allRoutes, numLandmarks);

             double maxTotalAttractivenessScore = Double.MIN_VALUE;
             List<String> optimalRoute = new ArrayList<>();
             for (List<String> route : allRoutes) {
                 double totalAttractivenessScore = calculateTotalAttractivenessScore(graph, route);
                 if (totalAttractivenessScore > maxTotalAttractivenessScore) {
                     maxTotalAttractivenessScore = totalAttractivenessScore;
                     optimalRoute = new ArrayList<>(route);
                 }
             }

             System.out.println("Total attractiveness score: " + maxTotalAttractivenessScore);
             System.out.println("Total travel time: " + calculateTotalTravelTime(map,optimalRoute)+" min");
             return optimalRoute;
         }


         //Time complexity is O(2^n)
          private static void findRoutes(DirectedGraph graph, Landmark currentLandmark, Set<Landmark> visited,
                                         List<String> currentRoute, List<List<String>> allRoutes, int numLandmarks) {
              if (currentRoute.size() == numLandmarks-1) {
                  currentRoute.add(0, "Hotel");
                  currentRoute.add("Hotel");
                  allRoutes.add(new ArrayList<>(currentRoute));
                  return;
              }
              List<Landmark> landmarks = new ArrayList<>(graph.getLandmarks());
              Map<Landmark, List<Edge>> edges = graph.getGraph();
              List<Edge> currentEdges = edges.get(currentLandmark);
              for (Edge edge : currentEdges) {
                  Landmark nextLandmark =null;
                  for (Landmark landmark : landmarks){
                      if (landmark.getName().equals(edge.getDestination().getName()))
                          nextLandmark = landmark;
                  }
                  if (!visited.contains(nextLandmark) && !nextLandmark.getName().equals("Hotel")) {
                      visited.add(nextLandmark);
                      List<String> newRoute = new ArrayList<>(currentRoute);
                      newRoute.add(nextLandmark.getName());
                      findRoutes(graph, nextLandmark, visited, newRoute, allRoutes, numLandmarks);
                      visited.remove(nextLandmark); // Backtrack: Remove the visited landmark to explore other paths
                  }
              }
          }

           //Time complexity O(n^2)
           private static double calculateTotalTravelTime(DirectedGraph graph, List<String> route) {
              double totalTravelTime=0;
               List<Landmark> landmarks = new ArrayList<>(graph.getLandmarks());
               for (int i = 0; i < route.size() - 1; i++) {
                   Landmark fromLandmark =null;
                   for (Landmark landmark : landmarks){
                       if (landmark.getName().equals(route.get(i)))
                           fromLandmark = landmark;
                   }
                   Landmark toLandmark =null;
                   for (Landmark landmark : landmarks){
                       if (landmark.getName().equals(route.get(i+1)))
                           toLandmark = landmark;
                   }

                   List<Edge> edges = graph.getGraph().get(fromLandmark);
                   for (Edge edge : edges){
                       if (edge.getDestination().getName().equals(toLandmark.getName()))
                           totalTravelTime += edge.getWeight();
                   }

               }
               return totalTravelTime;
           }


           //Time complexity O(n^2)
           private static double calculateTotalAttractivenessScore(DirectedGraph graph, List<String> route) {
               double totalAttractivenessScore = 0;
               List<Landmark> landmarks = new ArrayList<>(graph.getLandmarks());
               for (int i = 0; i < route.size() - 1; i++) {
                   Landmark fromLandmark =null;
                   for (Landmark landmark : landmarks){
                       if (landmark.getName().equals(route.get(i)))
                           fromLandmark = landmark;
                   }
                   Landmark toLandmark =null;
                   for (Landmark landmark : landmarks){
                       if (landmark.getName().equals(route.get(i+1)))
                           toLandmark = landmark;
                   }

                   List<Edge> edges = graph.getGraph().get(fromLandmark);
                   for (Edge edge : edges){
                       if (edge.getDestination().getName().equals(toLandmark.getName()))
                           totalAttractivenessScore += edge.getAttractivenessScore();
                   }

               }
               return totalAttractivenessScore;
           }

        }

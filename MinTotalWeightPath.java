/**
 * @author fatih goncagül
 */

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class MinTotalWeightPath {


    public static void main(String[] args) {

        Tree tree = new Tree();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter size");
        int size = scanner.nextInt();
        int WN[] = new int[size];
        int [][] WE = new int[WN.length][WN.length];



        generateWeights(WN,WE);
        tree.root = tree.LevelOrder(WN,WE,tree.root, 0);


        System.out.println("GREEDY ALGORITHM");
        greedySol(WN,tree.root);

        ArrayList<Integer> totalWeightList = new ArrayList<>();
        ArrayList<String> pathList = new ArrayList<>();
        String path="";
        int sum=0;
        System.out.println("RECURSIVE ALGORITHM");
        long start = System.currentTimeMillis();
recursiveSol(tree.root,totalWeightList,pathList,sum,path);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;



        System.out.println("RECURSIVE RUNTIME: "+ timeElapsed);


        System.out.println("DYNAMIC PROGRAMMING ALGORITHM");
        int[] arrayEmpty = new int[size];




        Instant start2 = Instant.now();
 dynamicSol(tree.root,0,arrayEmpty);
        Instant finish2 = Instant.now();
        long timeElapsed2 = Duration.between(start2, finish2).toMillis();
        System.out.println("DYNAMIC RUNTIME: "+ timeElapsed2);
        System.out.println("min total weight is "+ arrayEmpty[0]);
    }


    public static void generateWeights(int[]WN, int[][] WE){
        Random random = new Random();

        //generating node weights
        for(int i=0;i<WN.length;i++){
            WN[i] = random.nextInt(20) + 1;
        }
        //generating edge weights
        for(int row=0;row<WE.length;row++){

            for (int column =0;column<WE[row].length;column++){

                if(2*row+1<WE[row].length){
                    WE[row][2*row+1]=random.nextInt(20) + 1;
                }

                if(2*row+2<WE[row].length) {
                    WE[row][2*row+2] = random.nextInt(20) + 1;
                }
            }
        }
    }
    //Greedy solution, it choses the min total weight that encounters first
    public static void greedySol(int[]WN, Tree.Node node) {
        int sumL= 0;
        int sumR= 0;
        if (node!=null) {
            int totalWeight = node.nodeWeight;
            System.out.print("path: 0");

            for (int i = 0; i < WN.length; i++) {

                int left = 2 * i + 1;
                int right = 2 * i + 2;
                int rightWeight;
                int leftWeight;

                if (node.left != null) {
                    if (left < WN.length) {
                        leftWeight = node.leftEdgeWeight;
                        sumL += node.left.nodeWeight + leftWeight;
                    }
                    if (right < WN.length && node.right != null) {
                        rightWeight = node.rightEdgeWeight;
                        sumR += node.right.nodeWeight + rightWeight;
                    }
                    if (sumR>= sumL|| node.right==null) {
                        totalWeight += sumL;
                        node = node.left;
                        System.out.print("-" + node.id);
                    } else {
                        totalWeight += sumR;
                        node = node.right;
                        System.out.print("-" + node.id);
                    }
                    sumR = 0;
                    sumL = 0;
                }
            }
            System.out.println("total weight:  " + totalWeight + ".");
        }
    }

    public static void recursiveSol(Tree.Node node, ArrayList<Integer> totalList, ArrayList<String> pathList, int sum, String path){
        //base case
        if(node == null){
            return;
        }

        sum+= node.nodeWeight;
        path+=node.id;
        if(node.left!=null||node.right!=null) {
            path += "-";
        }
        //base case
        if (isLeaf(node)) {
            totalList.add(sum);
            pathList.add(path);
            return;
        }

        if(node.left!=null){
            sum+=node.leftEdgeWeight;
            recursiveSol(node.left,totalList, pathList,sum,path);
        }

        sum-=node.leftEdgeWeight;

        if(node.right != null){
            sum+=node.rightEdgeWeight;
            recursiveSol(node.right,totalList, pathList,sum,path);
        }

        sum-=node.rightEdgeWeight;
        if(sum==node.nodeWeight){
            int index=minIndex(totalList);
            System.out.println("path: "+ pathList.get(index)
                    +" , total weight "+totalList.get(index));
        }
    }

    public static int minIndex (ArrayList<Integer> list) {
        return list.indexOf (Collections.min(list)); }

    public static boolean isLeaf(Tree.Node node) {
        return (node.left == null && node.right == null);
    }
    static void dynamicSol( Tree.Node node, int a,int[] array) {

        array[a] =node.nodeWeight;
        int min= Integer.MAX_VALUE;

        if (node.left!=null) {
            dynamicSol(node.left,node.left.id,array);
            min = Math.min(min, (array[node.left.id]+node.leftEdgeWeight));
        }

        if ( node.right!=null) {
            dynamicSol(node.right, node.right.id,array);
            min = Math.min(min, (array[node.right.id]+node.rightEdgeWeight));
        }
        if (node.left == null && node.right == null) {
            min=0;
        }
        array[a] += min;
    }

}
class Tree {
    Node root;

    static class Node {
        int nodeWeight,leftEdgeWeight,rightEdgeWeight, id;;
        Node left, right;
        Node(int nodeWeight)
        {
            this.nodeWeight = nodeWeight;
            this.leftEdgeWeight=0;
            this.rightEdgeWeight=0;
            this.left = null;
            this.right = null;
        }
        public static Node goUpLevel(Node node){
            node = new Node((node.id -1)/2);
            return node;
        }


    }

    public static Node LevelOrder(int[] WN, int [][] WE, Node root,
                                  int i)
    {

        if (i < WN.length) {
            Node temp = new Node(WN[i]);
            root = temp;

            root.id =i;

            if(2*i+1<WN.length&&root.leftEdgeWeight==0){
                root.leftEdgeWeight=WE[i][2*i+1];
            }
            root.left = LevelOrder(WN,WE, root.left,
                    2 * i + 1);


            if(2*i+2<WN.length&&root.rightEdgeWeight==0){
                root.rightEdgeWeight=WE[i][2*i+2];
            }
            root.right = LevelOrder(WN,WE,root.right,
                    2 * i + 2);
        }
        return root;
    }






}
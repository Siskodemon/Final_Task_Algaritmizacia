import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public class HashMap{
        private class Entity{
            int Key;
            int Value;
        }
        private class Basket{
            Node Head;
            private class Node{
                Entity entity;
                Node next;
            }

            Entity find(int Key){
                Node current = Head;
                if(Head != null){
                    while (current != null){
                        if(current.entity.Key == Key){
                            return current.entity;
                        }
                        current = current.next;
                    }
                }
                return null;
            }

            private boolean push(Entity entity){
                Node node = new Node();
                node.entity = entity;

                Node current = Head;
                while(current != null){
                    if(current.entity == entity){
                        return false;
                    }
                    if(current.next == null){
                        current.next = node;
                        return true;
                    }
                    current = current.next;
                }
                Head = node;
                return true;
            }

            private boolean del(int Key){
                Node current = Head;
                while(current != null){
                    if(current.next.entity.Key == Key){
                        if(current.next != null) {
                            current.next = current.next.next;
                        }else{
                            current.next = null;
                        }
                        return true;
                    }
                    current = current.next;
                }
                return false;
            }
        }
        private static final int INIT_SIZE = 16;
        private static final int Size = 0;
        private static final double LOAD_FACTOR = 0.75;

        private Basket baskets[];

        public HashMap() {
            this(INIT_SIZE);
        }

        public HashMap(int size) {
            baskets = new Basket[size];
        }

        int calcIndex(int Key){
            return Key % baskets.length;
        }

        public Entity find(int Key){
            int index = calcIndex(Key);
            Basket basket = baskets[index];

            if(basket != null){
                return basket.find(Key);
            }
            return null;
        }

        public void push(int Key, int Value){
            int index = calcIndex(Key);
            Basket basket = baskets[index];

            Entity entity = new Entity();
            entity.Value = Value;
            entity.Key = Key;

            if(basket == null){
                basket = new Basket();
                baskets[index] = basket;
            }

            basket.push(entity);

        }

        public void del(int Key){
            int index = calcIndex(Key);
            Basket basket = baskets[index];

            if(basket != null){
                basket.del(Key);
            }
        }
    }

    public static class RBTree{
        private Node Root;
        private class Node{
            private int Value;
            private Color Color;
            private Node Left;
            private Node Right;
        }

        private enum Color {
            Red, Black;
        }

        public boolean addNode (int value) {
            if (Root != null) {
                boolean result = push(Root, value);
                Root = rebalance(Root);
                Root.Color = Color.Black;
                return  result;
            }else {
                Root = new Node();
                Root.Color = Color.Black;
                Root.Value = value;
                return true;
            }
        }
        public boolean find(int Value){
            Node current = Root;
            while(current != null){
                if(current.Value == Value)
                    return true;

                if(Value < current.Value){
                    current = current.Left;
                }else{
                    current = current.Right;
                }
            }
            return false;
        }

        public Node rebalance (Node node){
            Node current = node;
            boolean need_rebalance;
            do {
                need_rebalance = false;
                if (current.Right != null && current.Right.Color == Color.Red && (current.Left == null || current.Left.Color == Color.Black)){
                    need_rebalance = true;
                    current = Right_Razv(current);
                }
                if (current.Left != null && current.Left.Color == Color.Red && current.Left.Left != null && current.Left.Left.Color == Color.Red){
                    need_rebalance = true;
                    current = Left_Razv(current);
                }
                if (current.Left != null && current.Left.Color == Color.Red && current.Right != null && current.Right.Color == Color.Red){
                    need_rebalance = true;
                    Swap(current);
                }
            }while (need_rebalance);
            return current;
        }
        private Node Right_Razv(Node current){
            Node Child_Right = current.Right;
            Node Child_Swap = Child_Right.Left;
            Child_Right.Left = current;
            current.Right = Child_Swap;
            Child_Right.Color = current.Color;
            current.Color = Color.Black;
            return Child_Right;
        }
        private Node Left_Razv(Node current){
            Node Child_Left = current.Left;
            Node Child_Swap = Child_Left.Right;
            Child_Left.Right = current;
            current.Left = Child_Swap;
            Child_Left.Color = current.Color;
            current.Color = Color.Red;
            return Child_Left;
        }
        private void Swap(Node current){
            current.Left.Color = Color.Black;
            current.Right.Color = Color.Black;
            current.Color = Color.Red;
        }

        public boolean push(Node node, int  Value) {
            if (node.Value == Value) {
                return false;
            } else {
                if (node.Value > Value) {
                    if (node.Left != null) {
                        boolean result = push(node.Left, Value);
                        node.Left = rebalance(node.Left);
                        return result;
                    } else {
                        node.Left = new Node();
                        node.Left.Color = Color.Red;
                        node.Left.Value = Value;
                        return true;
                    }
                } else {
                    if (node.Right != null) {
                        boolean result = push(node.Right, Value);
                        node.Right = rebalance(node.Right);
                        return result;
                    } else {
                        node.Right = new Node();
                        node.Right.Color = Color.Red;
                        node.Right.Value = Value;
                        return true;
                    }
                }
            }
        }

    }
    public static void main(String[] args) {
        final RBTree tree = new RBTree();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
            while (true) {
                try {
                    int value = Integer.parseInt(reader.readLine());
                    tree.addNode(value);
                    System.out.println("Выполнено!");
                }catch (Exception ignored) {

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
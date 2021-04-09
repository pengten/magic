package org.pengten.magic;

public class TrasformerTest {

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            test();
            Thread.sleep(1000);
        }
    }

    public static void test(){
        System.out.println(TrasformerTest.class.getName()+"@test2");
    }
}

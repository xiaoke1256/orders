package util;

import org.junit.Test;

import java.util.Random;

public class RandomTest {
    @Test
    public void testRandom(){
        System.out.println(new Random().nextInt());
        System.out.println(new Random().nextInt());
        System.out.println(new Random().nextInt());
    }
}

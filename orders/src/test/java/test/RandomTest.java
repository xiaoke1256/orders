package test;


import java.math.BigDecimal;
import java.util.Random;

public class RandomTest {
	public static void main(String[] args){
		Random r = new Random();
		
		System.out.println("reslt:"+(BigDecimal.valueOf(Math.round(r.nextInt(100*100)))).divide(BigDecimal.valueOf(100)));
	}
}

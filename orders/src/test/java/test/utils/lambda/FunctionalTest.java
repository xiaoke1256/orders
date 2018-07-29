package test.utils.lambda;

import java.util.function.Predicate;

import javax.swing.JButton;

public class FunctionalTest {
	public static Runnable helloWord = ()->System.out.println("Hello word!");
	
	public static void main(String[] args) {
		Thread t = new Thread(helloWord);
		t.start();
		
		Checker c = new Checker();
		//以下编译不通过
		//c.check(x -> x>5);
		c.check((IntPred)x -> x>5);
	}
	
	public static JButton createButton() {
		JButton b = new JButton();
		b.addActionListener(event -> System.out.println(event.getActionCommand()) );
		return b;
	}
	
	public static interface IntPred{
		boolean test(Integer value);
	}
	
	public static class Checker {
		private Integer value = 11;
		boolean check(Predicate<Integer> predicate) {
			return predicate.test(value);
		}
		
		boolean check(IntPred predicate) {
			return predicate.test(value);
		}
	}
}

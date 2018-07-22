package test.utils.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import static java.util.stream.Collectors.*;

public class CollectorsTest {
	public static void main(String[] args) {
		List<String> list = Arrays.asList("a","d","c").stream().sorted().collect(toList());
		System.out.println(list);
		
		//reduce 
		StringBuilder s = Arrays.asList("a","d","c").stream().reduce(new StringBuilder(), ( BiFunction<StringBuilder,String,StringBuilder>)(builder,name)->{
																					if(builder.length()>0) {
																						builder.append(",");
																					}
																					return builder.append(name);
																				}, (BinaryOperator<StringBuilder>)(l,r)->{
																					return l.append(r);
																				});
		System.out.println(s);
		
		/*
		 * reduce
<U> U reduce(U identity,
             BiFunction<U,? super T,U> accumulator,
             BinaryOperator<U> combiner)

Performs a reduction on the elements of this stream, using the provided identity, accumulation and combining functions. This is equivalent to: 
     U result = identity;
     for (T element : this stream)
         result = accumulator.apply(result, element)
     return result;
 
but is not constrained to execute sequentially. 
The identity value must be an identity for the combiner function. This means that for all u, combiner(identity, u) is equal to u. Additionally, the combiner function must be compatible with the accumulator function; for all u and t, the following must hold: 

     combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t)
 

This is a terminal operation.
API Note:Many reductions using this form can be represented more simply by an explicit combination of map and reduce operations. The accumulator function acts as a fused mapper and accumulator, which can sometimes be more efficient than separate mapping and reduction, such as when knowing the previously reduced value allows you to avoid some computation.Type Parameters:U - The type of the resultParameters:identity - the identity value for the combiner functionaccumulator - an associative, non-interfering, stateless function for incorporating an additional element into a resultcombiner - an associative, non-interfering, stateless function for combining two values, which must be compatible with the accumulator functionReturns:the result of the reductionSee Also:reduce(BinaryOperator), reduce(Object, BinaryOperator)
		 */
	}
}

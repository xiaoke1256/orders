package test.com.xiaoke1256.orders.common.zookeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoke1256.orders.common.zookeeper.ChildrenCache;


public class ChildrenCacheTest {
	private ChildrenCache cache = new ChildrenCache(new ArrayList<String>());
	
	@Test
	public void testRemovedAndSet() {
		List<String> list = cache.removedAndSet(Arrays.asList("aaaaa"));
		System.out.println("list:"+list);
		Assert.assertTrue(list.isEmpty());
	}
}

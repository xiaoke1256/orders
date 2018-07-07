package com.xiaoke1256.jvmtest.bo;

/**
 * 某业务对象
 * @author Administrator
 *
 */
public class SomeBo {
	private byte[] by;
	
	private Integer id;

	public byte[] getBy() {
		return by;
	}

	public void setBy(byte[] by) {
		this.by = by;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SomeBo [id=" + id + "]";
	}
	
}

package com.wtb.javatool.vo;

import java.io.Serializable;

/**
 * 	供声明式调用使用
 * 
 * @Author linjintian
 * @Date 2019年3月4日
 */
public class Entity implements Serializable {
	private Integer id;
	private String name;

	public Entity(String name) {
		this.name = name;
	}

	public Entity(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Entity() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Entity [id=" + id + ", name=" + name + "]";
	}
	
	
}

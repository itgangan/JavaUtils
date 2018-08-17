package com.javautils;

import java.nio.ByteBuffer;


public class ByteBufferTest {

	
	
	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocate(1024);
		
		
		bb.putLong(123456789);
		bb.putLong(123456789);
		bb.putLong(123456789);
		bb.putLong(1L);
		
		bb.rewind();
		bb.flip();
		
		System.out.println(bb.getLong());
		System.out.println(bb.getLong());
		System.out.println(bb.getLong());
		System.out.println(bb.getShort());
		System.out.println(bb.getShort());
		System.out.println(bb.getInt());
	}
}

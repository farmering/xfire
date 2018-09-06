package com.sdjz.eshop.util;

import java.util.Random;

/**
 * 工具类- 随机数生成
 * @author Lee
 *
 */
public class RandomString{
	  private static final Random rnd = new Random();
	    private static final int NORMAL = -1;
	    private static final int NUMBER = 0;
	    private static final int UPPER_CHARACTER = 1;
	    private static final int LOWER_CHARACTER = 2;
	    private static final int SIZE = 3;
	    private static final int[] MAX_MEMBER = { 10, 26, 26 };
	    private static final int[] BASE_ASCII = { 48, 65, 97 };

	    /** *//**
	     * 随机生成数字和字母组成的字符串。
	     * 
	     * @param length
	     *            字符串长度
	     * @param type
	     *            类型(NORMAL/NUMBER)
	     * @return 字符串
	     */
	    private static String nextString(int length, int type) {
	        char[] charArray = new char[length];
	        int curType = type;
	        for (int i = 0; i < length; i++) {
	            if (type == NORMAL)
	                curType = rnd.nextInt(SIZE);
	            charArray[i] = (char) (rnd.nextInt(MAX_MEMBER[curType]) + BASE_ASCII[curType]);
	        }
	        return new String(charArray);
	    }

	    /** *//**
	     * 随机生成数字和字母组成的字符串。
	     * 
	     * @param length
	     *            字符串长度
	     * @return 字符串
	     */
	    public static String nextString(int length) {
	        return nextString(length, NORMAL);
	    }

	    /** *//**
	     * 随机生成数字组成的字符串。
	     * 
	     * @param length
	     *            字符串长度
	     * @return 字符串
	     */
	    public static String nextNumber(int length) {
	        return nextString(length, NUMBER);
	    }

	    /** *//**
	     * 随机生成大写字母组成的字符串。
	     * 
	     * @param length
	     *            字符串长度
	     * @return 字符串
	     */
	    public static String nextUpperString(int length) {
	        return nextString(length, UPPER_CHARACTER);
	    }

	    /** *//**
	     * 随机生成小写字母组成的字符串。
	     * 
	     * @param length
	     *            字符串长度
	     * @return 字符串
	     */
	    public static String nextLowerString(int length) {
	        return nextString(length, LOWER_CHARACTER);
	    }

	    /** *//**
	     * 随机生成大于零的整数。
	     * 
	     * @see {@link Random#nextInt()}
	     * @return 整数
	     */
	    public static int nextInt() {
	        return rnd.nextInt();
	    }

	    /** *//**
	     * 随机生成0<value<max的整数
	     * 
	     * @see {@link Random#nextInt(int)}
	     * @param max
	     *            最大整数
	     * @return 整数
	     */
	    public static int nextInt(int max) {
	        return rnd.nextInt(max);
	    }

	    /** *//**
	     * 生成布尔值。
	     * 
	     * @see {@link Random#nextBoolean()}
	     * @return 布尔值
	     */
	    public static boolean nextBoolean() {
	        return rnd.nextBoolean();
	    }

	    /** *//**
	     * 生成字节数组。
	     * 
	     * @see {@link Random#nextBytes(byte[])}
	     * @param bytes
	     *            用来存储生成的字节数组
	     */
	    public static void nextBytes(byte[] bytes) {
	        rnd.nextBytes(bytes);
	    }

	    /** *//**
	     * 生成浮点数。
	     * 
	     * @see {@link Random#nextDouble()}
	     * @return 双精度浮点数
	     */
	    public static double nextDouble() {
	        return rnd.nextDouble();
	    }

	    /** *//**
	     * 生成浮点数。
	     * 
	     * @see {@link Random#nextFloat()}
	     * @return 单精度浮点数
	     */
	    public static float nextFloat() {
	        return rnd.nextFloat();
	    }

	    /** *//**
	     * 生成高斯浮点数。
	     * 
	     * @see {@link Random#nextGaussian()}
	     * @return 双精度浮点数
	     */
	    public static double nextGaussian() {
	        return rnd.nextGaussian();
	    }

	    /** *//**
	     * 生成长整数。
	     * 
	     * @see {@link Random#nextLong()}
	     * @return 长整数
	     */
	    public static long nextLong() {
	        return rnd.nextLong();
	    }
	    
	    
	    /**  
	     * 生成随即密码  
	     *   
	     * @param pwd_len  
	     *            生成的密码的总长度  
	     * @return 密码的字符串  
	     */  
	    public static String genRandomNum(int pwd_len) {   
	        // 35是因为数组是从0开始的，26个字母+10个数字   
	        
	        int i; // 生成的随机数   
	        int count = 0; // 生成的密码的长度   
	        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',  
	                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',  
	                'X', 'Y', 'Z','a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',  
	                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',  
	                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	        int maxNum = str.length;   
	        //char[] str = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };   
	  
	        StringBuffer pwd = new StringBuffer("");   
	        Random r = new Random();   
	        while (count < pwd_len) {   
	            // 生成随机数，取绝对值，防止生成负数，   
	  
	            i = Math.abs(r.nextInt(maxNum)); // 生成的数最大为36-1   
	  
	            if (i >= 0 && i < str.length) {   
	                pwd.append(str[i]);   
	                count++;   
	            }   
	        }   
	  
	        return pwd.toString();   
	    }  
	    /* ***********Japan*** */
	    
	/**.
     */
    public static final String BAD_PASSWORD_CHARS = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~0OIl1";
    
    /**.
     */
    private String notIncludedChars = null;

    /**.
     */
    private int size = 0;

    /**.
     */
    public RandomString() {
        this.size = 0;
        this.notIncludedChars = "";
    }

    /**.
     */
    public RandomString(final int newSize) {
        this.size = newSize;
        this.notIncludedChars = "";
    }

    /**.
     */
    public RandomString(final int newSize, final String newNotIncludedChars) {
        this.size = newSize;
        this.notIncludedChars = newNotIncludedChars;
    }

    /**.
     */
    public RandomString(final String newNotIncludedChars) {
        this.size = 0;
        this.notIncludedChars = newNotIncludedChars;
    }

    /**.
     */
    public final String getRandomString() {
        StringBuffer randomString = new StringBuffer();
        for (int i = 0; i < this.size; i++) {
            randomString.append(getRandomCharcterValue());
        }
        return new String(randomString);
    }

    /**.
     */
    public final char getRandomCharactor() {
        return getRandomCharcterValue();
    }

    /**.
     */
    private char getRandomCharcterValue() {
        char random;
        do {
            random = (char) ('~' * Math.random() + 1);
        } while (
            random < '!'
                || random > '~'
                || notIncludedChars.indexOf(random) != -1);
        return random;
    }

    /**.
     */
    public final int getSize() {
        return size;
    }

    /**.
     */
    public final String getNotIncludedChars() {
        return notIncludedChars;
    }

    /**.
     */
    public final void setSize(final int sizee) {
        this.size = sizee;
    }

    /**.
     */
    public final void setNotIncludedChars(final String notIncludedCharss) {
        this.notIncludedChars = notIncludedCharss;
    }

	    
	    public static void main(String[] args) {   
	        // TODO Auto-generated method stub   
	        for(int i=0 ;i<100;i++){   
	            System.out.println(genRandomNum(6));   
	        }          
	    }   

	    
	}



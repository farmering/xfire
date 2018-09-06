package com.sdjz.eshop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * 字符串工具集合
 * @see {@link org.apache.commons.lang.StringUtils}
 * @author jz
 */
public class JZStringUtils extends org.apache.commons.lang.StringUtils {

    /**
     * 如果系统中存在旧版本的数据，则此值不能修改，否则在进行密码解析的时候出错
     */
    private static final String PASSWORD_CRYPT_KEY = "__jDlog_";
    private final static String DES = "DES";
    public static String getUUID(){   
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");   
        return uuid;   
    } 
    // 判断是否移动手机号码
    public static boolean isChinaMobile(String mobile) {
        return RegExp.test("(^(13)[4-9][0-9]{8}$)|(^(15)[012789][0-9]{8}$)|(^(18)[78][0-9]{8}$)",
                        mobile);
    }
    /**
     * 替换json字符串中的特殊字符
     */
    public final static String string2Json(String s) {
        StringBuilder sb = new StringBuilder(s.length() + 20);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\'':
                sb.append("\\\'");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }
    /**
     * @param orin
     * @return
     * @author li_hchun
     */
    public static String trimStr(String orin) {
        if (orin == null || orin.equalsIgnoreCase("null")) {
            orin = "";
        }
        return orin.trim();
    }
    /**
	 * 字符串长度不足，右侧补0
	 * @param str
	 * @param strLength
	 * @return
	 */
	public static String addZeroForNum(String str, int strLength) {
	    int strLen = str.length();
	    if (strLength > 0 && strLen < strLength) {
	        while (strLen < strLength) {
	            StringBuffer sb = new StringBuffer();
	            //sb.append("0").append(str);// 左补0
	            sb.append(str).append("0");//右补0
	            str = sb.toString();
	            strLen = str.length();
	        }
	    }
	    return str;
	}
    /**
     * BASE64编码
     * @param s
     * @return String
     */
    public static byte[] enBASE64(byte[] bytes) {
        return Base64Code.encode(bytes);
    }
    /**
     * BASE64反编码
     * @param bytes
     * @return byte[]
     */
    public static byte[] deBASE64(byte[] bytes) {
        return Base64Code.decode(bytes);
    }
    /**
     * BASE64编码
     * @param s
     * @return String
     */
    public static String enBASE64(String s) {
        if (s != null) {
            byte abyte0[] = s.getBytes();
            abyte0 = Base64Code.encode(abyte0);
            s = new String(abyte0);
            return s;
        }
        return null;
    }

    /**
     * BASE64反编码
     * @param s
     * @return String
     */
    public static String deBASE64(String s) {
        if (s != null) {
            byte abyte0[] = s.getBytes();
            abyte0 = Base64Code.decode(abyte0);
            s = new String(abyte0);
            abyte0 = null;
            return s;
        }
        return null;
    }

    /**
     * 加密
     * @param src  数据源
     * @param key  密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(src);
    }
    /**
     * 解密
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    /**
     * 数据解密
     * 
     * @param data
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data, String key) {
        if (data != null)
            try {
                return new String(decrypt(hex2byte(data.getBytes()), key
                        .getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * 数据加密
     * 
     * @param data
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public final static String encrypt(String data, String key) {
        if (data != null)
            try {
                return byte2hex(encrypt(data.getBytes(), key.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * 密码解密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public final static String decryptPassword(String data) {
        if (data != null)
            try {
                return new String(decrypt(hex2byte(data.getBytes()),
                        PASSWORD_CRYPT_KEY.getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * 密码加密
     * 
     * @param password
     * @return
     * @throws Exception
     */
    public final static String encryptPassword(String password) {
        if (password != null)
            try {
                return byte2hex(encrypt(password.getBytes(), PASSWORD_CRYPT_KEY
                        .getBytes()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }

    /**
     * 二行制转字符串
     * 
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 大小写无关的字符串替换策略
     * 
     * @param str
     * @param src
     * @param obj
     * @return
     */
    public static String replaceIgnoreCase(String str, String src, String obj) {
        String l_str = str.toLowerCase();
        String l_src = src.toLowerCase();
        int fromIdx = 0;
        StringBuffer result = new StringBuffer();
        do {
            int idx = l_str.indexOf(l_src, fromIdx);
            if (idx == -1)
                break;
            result.append(str.substring(fromIdx, idx));
            result.append(obj);
            fromIdx = idx + src.length();
        } while (true);
        result.append(str.substring(fromIdx));
        return result.toString();
    }

    /**
     * 用户名必须是数字或者字母的结合
     * 
     * @param username
     * @return
     */
    public static boolean isLegalUsername(String username) {
        for (int i = 0; i < username.length(); i++) {
            char ch = username.charAt(i);
            if (!isAscii(ch) && ch != '.' && ch != '_' && ch != '-'
                    && ch != '+' && ch != '(' && ch != ')' && ch != '*'
                    && ch != '^' && ch != '@' && ch != '%' && ch != '$'
                    && ch != '#' && ch != '~' && ch != '-')
                return false;
        }
        return true;
    }

    /**
     * 判断是否是字母和数字的结合
     * 
     * @param name
     * @return
     */
    public static boolean isAsciiOrDigit(String name) {
        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!isAscii(ch))
                return false;
        }
        return true;
    }

    public static boolean isAscii(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
                || (ch >= '0' && ch <= '9');
    }
    /**
     * 判断输入的是否全部为英文字母
     */
    public static boolean isAllLetter(String str) {
    	  String regex = "^[a-zA-Z]+$";
    	  return str.matches(regex);
    }
    /**
     * 判断输入的是否包含英文字母
     */
    public static boolean isContainLetter(String str) {
    	  String regex = "^.*[a-zA-z].*+$";
    	  return str.matches(regex);
    }
    public static void main(String[] args) {
        System.out.println(JZStringUtils.isAllLetter("aa"));
        System.out.println(JZStringUtils.isContainLetter("汉子aa"));
        String a = "fd汉子7bb";
		StringBuffer sb = new StringBuffer();
		for(int i = 0;i<a.length();i++){
			char c = a.charAt(i);
			if((c<='z'&&c>='a')||(c<='Z'&&c>='A')){
				sb.append(c);
			}else{
				
			}
		}
		System.out.println(sb.toString());
    }

	private int size = 20;

	/**
	 * 取得随机数+时间的文件名
	 */
	public final String getFileName() {
		RandomString randomstring = 
			new RandomString(this.size - Integer.parseInt("12"));
		randomstring.setNotIncludedChars(RandomString.BAD_PASSWORD_CHARS);
		String filename = randomstring.getRandomString();
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyMMddHHmmss");
		filename += sdf.format(Calendar.getInstance().getTime());
		return new String(filename);
	}
	/**
	 * 取得随机数文件不加时间
	 */
	public final String getFileNameNotTime() {

		RandomString randomstring = new RandomString(this.size);
		randomstring.setNotIncludedChars(RandomString.BAD_PASSWORD_CHARS);
		String filename = randomstring.getRandomString();
		return new String(filename);
	}
	/**
	 * Returns the size.
	 * @return int
	 */
	public final int getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 * @param strSize The size to set
	 */
	public final void setSize(final int strSize) {
		this.size = strSize;
	}
	/**
	 * 判断数组是否为空
	 */
	public static boolean isNullOrEmpty(final Object[] objs) {
		return (objs == null || objs.length == 0);
	}
	/**
	 * 判定集合是否为空
	 */
	public static boolean isNullOrEmpty(final Collection<?> col) {
		return (col == null || col.isEmpty());
	}
	/**
	 * 判定字符串是否为空
	 */
	public static boolean isNullOrBlank(final String str) {
		return (str == null || str.trim().length() == 0);
	}
	/**
	 * 判断字符串是否为空去除全角半角空格
	 */
	public static boolean isNullOrBlankFull(String key) {
		String value = key;
		if (value == null) {
			return true;
		} else {
			value = value.trim();
			while (value.length() > 0
				&& (value.lastIndexOf(" ") == value.length() - 1 || value
					.lastIndexOf(" ") == value.length() - 1)) {
				value = value.substring(0, value.length() - 1);
			}
		}
		return (value.length() == 0);
	}
	/** 
     * 判断字符串是否数值  
     * @param str 
     * @return true:是数值 ；false：不是数值  
     */  
	public static boolean isNumber(String str){  
        Pattern pattern = Pattern.compile("^\\d+$|^\\d+\\.\\d+$|-\\d+$");
        Matcher match=pattern.matcher(str);  
        return match.matches();  
    }
	public static boolean isByte(final String str) {
		boolean bValid = true;
		try {
		} catch (NumberFormatException ne) {
			bValid = false;
		}
		if ("NaN".equals(str)) {
			bValid = false;
			
		} else {
			bValid = false;
		}
		return bValid;
	}
	/**
	 * 是否为半角
	 */
	public static boolean isHalfSize(final String str) {
		byte[] byts = null;
		boolean bValid = true;
		try {
//			byts = str.toString().getBytes("Shift_JIS");
			byts = str.toString().getBytes(
			Resources.getString("CHARACTER_SET"));
		} catch (UnsupportedEncodingException e) {
			bValid = false;
		}
		if (byts.length != str.length()) {
			bValid = false;
		}
		return bValid;
	}
	/**
	 * 是否为半角
	 */
	public static boolean isHalfSize(
		final String str, 
		final String pattern) {
		byte[] byts = null;
		boolean bValid = true;
		try {
			byts = str.toString().getBytes(pattern);
		} catch (UnsupportedEncodingException e) {
			bValid = false;
		}
		if (byts.length != str.length()) {
			bValid = false;
		}
		return bValid;
	}
	public static boolean isFullSize(final String inStr) {
		byte[] bytes = null;
		//String encode = "MS932";
		String encode = Resources.getString("CHARACTER_SET_FULL");
		try {
			bytes = inStr.getBytes(encode);
		} catch (UnsupportedEncodingException uee) {
			return false;
		}
		int beams = inStr.length() * 2;
		if  (beams == bytes.length)  {

			return true;
		} else {
			return false;
		}
	}
	public static boolean isFullSize(
		final String inStr, 
		final String pattern) {
		
		byte[] bytes = null;
		try {
			bytes = inStr.getBytes(pattern);
		} catch (UnsupportedEncodingException uee) {
			return false;
		}

		int beams = inStr.length() * 2;

		if (beams == bytes.length) {

			return true;
		} else {
			return false;
		}
	}
	public static boolean isLengthB(final String str, final int i) {
		byte[] byts = null;

		try {
			byts = str.toString().getBytes(
				Resources.getString("CHARACTER_SET_FULL"));
			if (byts.length <= i) {
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException ue) {
			return false;
		}
	}
	
	public static boolean isLengthB(
		final String str, 
		final String pattern, 
		final int i) {
		byte[] byts = null;
		try {
			byts = str.toString().getBytes(pattern);
			if (byts.length <= i) {
				return true;
			} else {
				return false;
			}
		} catch (UnsupportedEncodingException ue) {
			return false;
		}
	}

	public static boolean isIp(final String str) {
		Pattern pattern = Pattern
				.compile("[0-9]{0,3}.[0-9]{0,3}.[0-9]{0,3}.[0-9]{0,3}");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	/**
	 * @param  str String
	 * @return List
	 */
	public static List<String> string2List(final String str) {
		ArrayList<String> list = new ArrayList<String>();
		String strLine = null;
		try {
			if (!isNullOrBlank(str)) {
				BufferedReader read = new BufferedReader(new StringReader(str));
				while (true) {			
					strLine = read.readLine();			
					if (strLine == null) {
						break;
					}
					list.add(strLine);
				}	
			}		
		} catch (IOException e) {
			return new ArrayList<String>();
		}
		return list;
	}
}

/**
 * BASE64编码解码实现类
 * @author liusoft created on 2002-12-19
 */
class Base64Code {
    protected static byte[] _encode_map = { (byte) 'A', (byte) 'B', (byte) 'C',
            (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H',
            (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M',
            (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R',
            (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W',
            (byte) 'X', (byte) 'Y', (byte) 'Z',

            (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e',
            (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j',
            (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o',
            (byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't',
            (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y',
            (byte) 'z',

            (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
            (byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9',

            (byte) '+', (byte) '/' };

    protected static byte _decode_map[] = new byte[128];
    static {
        for (int i = 0; i < _encode_map.length; i++) {
            _decode_map[_encode_map[i]] = (byte) i;
        }
    }

    /**
     * This class isn't meant to be instantiated.
     */
    private Base64Code() {

    }

    /**
     * This method encodes the given byte[] using the Base64 encoding
     * 
     * 
     * @param data
     *            the data to encode.
     * @return the Base64 encoded <var>data</var>
     */
    public final static byte[] encode(byte[] data) {

        if (data == null) {
            return (null);
        }

        /*
         * Craete a buffer to hold the results
         */
        byte dest[] = new byte[((data.length + 2) / 3) * 4];

        /*
         * 3-byte to 4-byte conversion and 0-63 to ascii printable conversion
         */
        int i, j;
        int data_len = data.length - 2;
        for (i = 0, j = 0; i < data_len; i += 3) {

            dest[j++] = _encode_map[(data[i] >>> 2) & 077];
            dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017 | (data[i] << 4)
                    & 077];
            dest[j++] = _encode_map[(data[i + 2] >>> 6) & 003
                    | (data[i + 1] << 2) & 077];
            dest[j++] = _encode_map[data[i + 2] & 077];
        }

        if (i < data.length) {
            dest[j++] = _encode_map[(data[i] >>> 2) & 077];

            if (i < data.length - 1) {
                dest[j++] = _encode_map[(data[i + 1] >>> 4) & 017
                        | (data[i] << 4) & 077];
                dest[j++] = _encode_map[(data[i + 1] << 2) & 077];
            } else {
                dest[j++] = _encode_map[(data[i] << 4) & 077];
            }
        }

        /*
         * Pad with "=" characters
         */
        for (; j < dest.length; j++) {
            dest[j] = (byte) '=';
        }

        return (dest);
    }

    /**
     * This method decodes the given byte[] using the Base64 encoding
     * 
     * 
     * @param data
     *            the Base64 encoded data to decode.
     * @return the decoded <var>data</var>.
     */
    public final static byte[] decode(byte[] data) {

        if (data == null)
            return (null);

        /*
         * Remove the padding on the end
         */
        int ending = data.length;
        if (ending < 1) {
            return (null);
        }
        while (data[ending - 1] == '=')
            ending--;

        /*
         * Create a buffer to hold the results
         */
        byte dest[] = new byte[ending - data.length / 4];

        /*
         * ASCII printable to 0-63 conversion
         */
        for (int i = 0; i < data.length; i++) {
            data[i] = _decode_map[data[i]];
        }

        /*
         * 4-byte to 3-byte conversion
         */
        int i, j;
        int dest_len = dest.length - 2;
        for (i = 0, j = 0; j < dest_len; i += 4, j += 3) {
            dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
            dest[j + 1] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
            dest[j + 2] = (byte) (((data[i + 2] << 6) & 255) | (data[i + 3] & 077));
        }

        if (j < dest.length) {
            dest[j] = (byte) (((data[i] << 2) & 255) | ((data[i + 1] >>> 4) & 003));
        }

        j++;
        if (j < dest.length) {
            dest[j] = (byte) (((data[i + 1] << 4) & 255) | ((data[i + 2] >>> 2) & 017));
        }

        return (dest);
    }
}
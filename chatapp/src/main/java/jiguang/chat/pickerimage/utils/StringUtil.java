package jiguang.chat.pickerimage.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.UUID;

public class StringUtil {
	
	public static String getPercentString(float percent) {
		return String.format(Locale.US, "%d%%", (int) (percent * 100));
	}
	/**
	 * 删除字符串中的空白符
	 *
	 * @param content
	 * @return String
	 */
	public static String removeBlanks(String content) {
		if (content == null) {
			return null;
		}
		StringBuilder buff = new StringBuilder();
		buff.append(content);
		for (int i = buff.length() - 1; i >= 0; i--) {
			if (' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i))
					|| ('\r' == buff.charAt(i))) {
				buff.deleteCharAt(i);
			}
		}
		return buff.toString();
	}
	/**
	 * 获取32位uuid
	 *
	 * @return
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static boolean isEmpty(String input) {
		return TextUtils.isEmpty(input);
	}
	
	/**
	 * 生成唯一号
	 *
	 * @return
	 */
	public static String get36UUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}
	
	public static String makeMd5(String source) {
		return MD5.getStringMD5(source);
	}
	
    public static final String filterUCS4(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}

		if (str.codePointCount(0, str.length()) == str.length()) {
			return str;
		}

		StringBuilder sb = new StringBuilder();

		int index = 0;
		while (index < str.length()) {
			int codePoint = str.codePointAt(index);
			index += Character.charCount(codePoint);
			if (Character.isSupplementaryCodePoint(codePoint)) {
				continue;
			}

			sb.appendCodePoint(codePoint);
		}

		return sb.toString();
	}

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = (int) str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }

    public static String getJSONObjectString(JSONObject jsonObject, String key){
		try {
			return jsonObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 自定义消息类型
	 */
	public static final String TYPE = "type";
	public static final String LINK = "link";
	public static final String OID = "oid";

	public static final String TYPE_CURRICULUM_VITAE = "1";//简历
	public static final String IMG = "img";
	public static final String NAME = "name";
	public static final String SEX = "sex";
	public static final String AGE = "age";
	public static final String WORKYEAR = "workYear";
	public static final String EXPECT_POSITION = "expectPosition";
	public static final String EXPECT_SALARY = "expectSalary";
	public static final String TEL = "tel";


	public static final String TYPE_POSITION = "2";//职位
	public static final String TITLE = "title";
	public static final String COMPANY_NAME = "companyName";
	public static final String LOCATION = "location";
	public static final String MONEY = "money";
	public static final String COIN = "coin";




}

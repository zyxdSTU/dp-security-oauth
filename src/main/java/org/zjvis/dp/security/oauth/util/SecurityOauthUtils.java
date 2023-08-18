package org.zjvis.dp.security.oauth.util;

import java.util.Random;

/**
 * @author zhouyu
 * @create 2023-07-28 11:22
 */
public class SecurityOauthUtils {

    public static String getStringRandom(int length) {

        StringBuilder result = new StringBuilder();
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < length; i++) {
            //输出字母还是数字
            int chatTypa = random.nextInt(3);
            switch (chatTypa) {
                case 0:
                    //数字
                    result.append(random.nextInt(10));
                    break;
                case 1:
                    //小写字母
                    result.append((char) (random.nextInt(26) + 97));
                    break;
                case 2:
                    //大写字母
                    result.append((char) (random.nextInt(26) + 65));
                    break;
            }
        }
        return result.toString();
    }

    public static String getStringWithAsterisk(String str) {
        int asterRiskLength = 10;
        if(str.length() < asterRiskLength) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0; i < asterRiskLength; i++) {
            stringBuilder.append('*');
        }

        return stringBuilder + str.substring(asterRiskLength);
    }
}

package co.yiiu.pybbs.util.identicon.generator.impl;

import co.yiiu.pybbs.util.identicon.generator.IBaseGenerator;
import com.google.common.base.Preconditions;
import com.google.common.math.DoubleMath;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Author: Bryant Hang
 * Date: 15/1/10
 * Time: aa2:44
 */
public class DefaultGenerator implements IBaseGenerator {
    private String hash;
    private boolean[][] booleanValueArray;
    private Random random = new Random();

    @Override
    public boolean[][] getBooleanValueArray(String hash) {
        Preconditions.checkArgument(!StringUtils.isEmpty(hash) && hash.length() >= 16, "illegal argument hash:not null "
                + "and size >= 16");

        this.hash = hash;

        boolean[][] array = new boolean[6][5];

        //aaaaaa
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                array[i][j] = false;
            }
        }

        for (int i = 0; i < hash.length(); i += 2) {
            int s = i / 2; //aahashaaaaaaa（a0aa）aaa

            boolean v = DoubleMath.roundToInt(Integer.parseInt(hash.charAt(i) + "", 16) / 10.0, RoundingMode.HALF_UP) > 0 ?
                    true : false;
            if (s % 3 == 0) {
                array[s / 3][0] = v;
                array[s / 3][4] = v;
            } else if (s % 3 == 1) {
                array[s / 3][1] = v;
                array[s / 3][3] = v;
            } else {
                array[s / 3][2] = v;
            }
        }

        this.booleanValueArray = array;

        return this.booleanValueArray;
    }

    @Override
    public Color getBackgroundColor() {
        //        int r = Integer.parseInt(String.valueOf(this.hash.charAt(0)), 16) * 16;
        //        int g = Integer.parseInt(String.valueOf(this.hash.charAt(1)), 16) * 16;
        //        int b = Integer.parseInt(String.valueOf(this.hash.charAt(2)), 16) * 16;

        //        return new Color(r, g, b);
        return new Color(236, 236, 236);
    }

    @Override
    public Color getForegroundColor() {
        //        int r = Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 1)), 16) * 16;
        //        int g = Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 2)), 16) * 16;
        //        int b = Integer.parseInt(String.valueOf(hash.charAt(hash.length() - 3)), 16) * 16;
        //
        //        return new Color(r, g, b);

        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }
}

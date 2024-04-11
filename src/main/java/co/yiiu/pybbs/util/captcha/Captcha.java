package co.yiiu.pybbs.util.captcha;

import java.awt.*;
import java.io.OutputStream;

import static co.yiiu.pybbs.util.captcha.Randoms.alpha;
import static co.yiiu.pybbs.util.captcha.Randoms.num;

/**
 * <p>aaaaaa,aaaaaaa</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public abstract class Captcha {

    protected Font font = new Font("Verdana", Font.ITALIC | Font.BOLD, 28);   // aa
    protected int len = 4;  // aaaaaaaaa
    protected int width = 120;  // aaaaaaa
    protected int height = 32;  // aaaaaaa
    private String chars = null;  // aaaaa

    /**
     * aaaaaaaa
     *
     * @return aaaa
     */
    protected char[] alphas() {
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = alpha();
        }
        chars = new String(cs);
        return cs;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * aaaaaaaaaa
     *
     * @return Color aaaa
     */
    protected Color color(int fc, int bc) {
        if (fc > 255) fc = 255;
        if (bc > 255) bc = 255;
        int r = fc + num(bc - fc);
        int g = fc + num(bc - fc);
        int b = fc + num(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * aaaaa,aaaa，aaaaa
     *
     * @param os aaa
     */
    public abstract void out(OutputStream os);

    /**
     * aaaaaaa
     *
     * @return string
     */
    public String text() {
        return chars;
    }
}

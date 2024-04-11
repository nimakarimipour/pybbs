package co.yiiu.pybbs.util.captcha;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static co.yiiu.pybbs.util.captcha.Randoms.num;

/**
 * <p>pngaaaaa</p>
 *
 * @author: wuhongjun
 * @version:1.0
 */
public class SpecCaptcha extends Captcha {
    public SpecCaptcha() {
    }

    public SpecCaptcha(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public SpecCaptcha(int width, int height, int len) {
        this(width, height);
        this.len = len;
    }

    public SpecCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        this.font = font;
    }

    /**
     * aaaaa
     *
     * @throws java.io.IOException IOaa
     */
    @Override
    public void out(OutputStream out) {
        graphicsImage(alphas(), out);
    }

    /**
     * aaaaa
     *
     * @param strs aa
     * @param out  aaa
     */
    private boolean graphicsImage(char[] strs, OutputStream out) {
        boolean ok = false;
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) bi.getGraphics();
            AlphaComposite ac3;
            Color color;
            int len = strs.length;
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // aaaaaaaa
            for (int i = 0; i < 15; i++) {
                color = color(150, 250);
                g.setColor(color);
                g.drawOval(num(width), num(height), 5 + num(10), 5 + num(10));// aaa，aaaaaaaa
                color = null;
            }
            g.setFont(font);
            int h = height - ((height - font.getSize()) >> 1), w = width / len, size = w - font.getSize() + 1;
            /* aaaa */
            for (int i = 0; i < len; i++) {
                ac3 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);// aaaaa
                g.setComposite(ac3);
                color = new Color(20 + num(110), 20 + num(110), 20 + num(110));// aaaaaaaaaaa
                g.setColor(color);
                g.drawString(strs[i] + "", (width - (len - i) * w) + size, h - 4);
                color = null;
                ac3 = null;
            }
            ImageIO.write(bi, "png", out);
            out.flush();
            ok = true;
        } catch (IOException e) {
            ok = false;
        } finally {
            Streams.close(out);
        }
        return ok;
    }
}
